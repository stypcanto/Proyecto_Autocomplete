package styp.com.pagina_autocomplete;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import org.json.JSONArray;

@WebServlet("/autocomplete")
public class AutocompleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String term = request.getParameter("term");
        List<String> results = new ArrayList<>();

        // Leer variables de entorno
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Si no hay variables de entorno (local), usar valores locales
        if (url == null || user == null || password == null) {
            url = "jdbc:postgresql://localhost:5432/autocomplete";
            user = "postgres";
            password = "postgres";
            System.out.println("🌐 Usando configuración LOCAL de base de datos");
        } else {

            System.out.println("🌐 Usando configuración desde variables de entorno (Docker/Render)");

            try {
            // Cargar driver
            Class.forName("org.postgresql.Driver");

            // Conexión a la base de datos
            Connection conn = DriverManager.getConnection(url, user, password);

            // Consulta preparada para autocompletado insensible a mayúsculas
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT nombre FROM personas WHERE nombre ILIKE ?"
            );
            stmt.setString(1, term + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(rs.getString("nombre"));
            }

            // Cierre de recursos
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Enviar respuesta JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new JSONArray(results));
        out.flush();
    }
}
