package styp.com.pagina_autocomplete;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import org.json.JSONArray;

// Esta anotación indica que este servlet atenderá solicitudes hacia /autocomplete
@WebServlet("/autocomplete")
public class AutocompleteServlet extends HttpServlet {

    // Método que se ejecuta cuando llega una solicitud HTTP GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Obtener el término de búsqueda desde la URL, por ejemplo: ?term=Mar
        String term = request.getParameter("term");
        List<String> results = new ArrayList<>();

        // Leer variables de entorno para conexión a base de datos (Render o Docker)
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Si no hay variables de entorno (es decir, estás corriendo localmente)
        if (url == null || user == null || password == null) {
            url = "jdbc:postgresql://localhost:5432/autocomplete"; // URL local
            user = "postgres";                                     // Usuario local
            password = "postgres";                                 // Contraseña local
            System.out.println("🌐 Usando configuración LOCAL de base de datos");
        } else {
            System.out.println("🌐 Usando configuración desde variables de entorno (Docker/Render)");
        }

        try {
            // Cargar el driver de PostgreSQL (necesario para conexiones JDBC)
            Class.forName("org.postgresql.Driver");

            // Establecer conexión a la base de datos
            Connection conn = DriverManager.getConnection(url, user, password);

            // Consulta SQL con ILIKE para autocompletado insensible a mayúsculas
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT nombre FROM personas WHERE nombre ILIKE ?"
            );
            stmt.setString(1, term + "%"); // Buscar coincidencias que empiecen con "term"

            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();

            // Agregar los resultados a la lista
            while (rs.next()) {
                results.add(rs.getString("nombre"));
            }

            // Cerrar recursos
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace(); // Mostrar errores por consola
        }

        // Enviar la lista como respuesta JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new JSONArray(results)); // Convertimos la lista a formato JSON
        out.flush();
    } // <- FIN del método doGet

} // <- FIN de la clase AutocompleteServlet
