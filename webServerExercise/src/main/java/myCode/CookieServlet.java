package myCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "CookieServlet", value = "/CookieServlet")
public class CookieServlet extends HttpServlet {
    private PreparedStatement preparedStatement;

    @Override
    public void init() throws ServletException {
        initializeJDBC();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String bookId = request.getParameter("bookId");
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String author = request.getParameter("author");

        addSafeCookie(response, "bookId", bookId);
        addSafeCookie(response, "name", name);
        addSafeCookie(response, "price", price);
        addSafeCookie(response, "author", author);

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h3>You entered the following cookies:</h3>");
        out.println("<p>Book Id: " + bookId + "</p>");
        out.println("<p>Name: " + name + "</p>");
        out.println("<p>Price: " + price + "</p>");
        out.println("<p>Author: " + author + "</p>");
        out.println("<br/>");
        out.println("<form method=\"post\" action=\"/cookieRegis\">");
        out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form>");
        out.println("</body></html>");
        out.close();
    }

    private void addSafeCookie(HttpServletResponse response, String name, String value) throws UnsupportedEncodingException {
        String safeValue = (value != null) ? URLEncoder.encode(value, StandardCharsets.UTF_8.toString()) : "";
        Cookie cookie = new Cookie(name, safeValue);
        response.addCookie(cookie);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = 0, price = 0;
        String name = null, author = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "bookId":
                        bookId = Integer.parseInt(cookie.getValue());
                        break;
                    case "name":
                        name = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.toString());
                        break;
                    case "price":
                        price = Integer.parseInt(cookie.getValue());
                        break;
                    case "author":
                        author = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.toString());
                        break;
                }
            }
        }

        try {
            storeBook(bookId, name, price, author);
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("Book " + bookId + " has been added to the database.");
            out.println("</body></html>");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeJDBC() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded...");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/kevindb",
                    "kevin",
                    "kevin1203"
            );

            System.out.println("Connecting to database...");

            preparedStatement = connection.prepareStatement(
                    "INSERT INTO book (bookId, name, price, author) VALUES (?, ?, ?, ?)"
            );
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void storeBook(int id, String name, int price, String author) {
        try {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, price);
            preparedStatement.setString(4, author);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}