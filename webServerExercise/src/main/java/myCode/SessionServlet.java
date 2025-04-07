package myCode;

import Obj.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

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

@WebServlet(name = "SessionServlet", value = "/SessionServlet")
public class SessionServlet extends HttpServlet {
    private PreparedStatement preparedStatement;

    @Override
    public void init() throws ServletException {
        initializeJDBC();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        // GET parameters from get request
        String bookId = request.getParameter("bookId");
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String author = request.getParameter("author");

        Book book = new Book();
        book.setBookId(Integer.parseInt(bookId));
        book.setName(name);
        book.setAuthor(author);
        book.setPrice(Integer.parseInt(price));

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("book", book);

        PrintWriter out = response.getWriter();
        out.println("You entered the following sessions:");
        out.println("<p>Book Id:" + bookId + "</p>");
        out.println("<br />");
        out.println("<p>Name:" + name + "</p>");
        out.println("<br />");
        out.println("<p>Price:" + price + "</p>");
        out.println("<br />");
        out.println("<p>Author:" + author + "</p>");
        out.println("<br />");

        out.println("<form method=\"post\" action=\"/sessionRegis\">");
        out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Book book = (Book) httpSession.getAttribute("book");


        try {
            storeBook(book.getBookId(), book.getName(), book.getPrice(), book.getAuthor());
            PrintWriter out = response.getWriter();
            out.println("Book " + book.getName() + " has been added to the database.");
            out.close();
        } catch (Exception e){
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
                    "insert into book" +
                            "(bookId, name, price, author) " +
                            "values (?,?,?,?)"
            );
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void storeBook(int id, String name, int price,String author) {
        try {
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,name);
            preparedStatement.setInt(3,price);
            preparedStatement.setString(4,author);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
