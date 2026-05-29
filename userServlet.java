import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class UserServlet extends HttpServlet {

    static final String URL = "jdbc:mysql://localhost:3306/shopping_cart_app";
    static final String USER = "root";
    static final String PASSWORD = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            if ("insert".equals(action)) {

                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users (name, email, password) VALUES (?, ?, ?)");

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);

                ps.executeUpdate();

                out.println("<h3>User inserted successfully!</h3>");

                ps.close();
            }

            else if ("view".equals(action)) {

                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM users");

                out.println("<h3>Users List:</h3><ul>");

                while (rs.next()) {
                    out.println("<li>"
                            + rs.getInt("id") + " | "
                            + rs.getString("name") + " | "
                            + rs.getString("email")
                            + "</li>");
                }

                out.println("</ul>");

                rs.close();
                st.close();
            }

            con.close();

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}