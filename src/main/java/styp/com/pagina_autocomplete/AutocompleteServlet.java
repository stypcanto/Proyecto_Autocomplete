package styp.com.pagina_autocomplete; // Paquete base del proyecto, debe coincidir con la estructura del proyecto

// Importaciones necesarias para trabajar con Servlets y HTTP (versión javax para compatibilidad con Tomcat 9/10)
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import org.json.JSONArray; // Para crear la respuesta JSON fácilmente

// Mapeo del servlet a la URL /autocomplete
@WebServlet("/autocomplete")
public class AutocompleteServlet extends HttpServlet {

    // Credenciales de conexión a PostgreSQL — asegúrate que la base esté activa
    private static final String JDBC_URL = "jdbc:postgresql://db:5432/autocomplete";

    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Capturar el término de búsqueda enviado desde el cliente
        String term = request.getParameter("term");
        List<String> results = new ArrayList<>();

        try {
            // Cargar el driver JDBC (opcional en versiones modernas, pero por compatibilidad mejor incluirlo)
            Class.forName("org.postgresql.Driver");

            // Establecer la conexión
            Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

            // Consulta preparada con ILIKE para búsqueda insensible a mayúsculas/minúsculas
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT nombre FROM personas WHERE nombre ILIKE ?"
            );
            stmt.setString(1, term + "%"); // Comienza con lo que escribió el usuario

            ResultSet rs = stmt.executeQuery();

            // Procesar resultados
            while (rs.next()) {
                results.add(rs.getString("nombre"));
            }

            // Cerrar todo
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error en consola
        }

        // Configurar respuesta como JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new JSONArray(results)); // Convertir lista a JSON
        out.flush();
    }
}
