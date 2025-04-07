package myCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.print.Book;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "PostFormServlet", value = "/PostFormServlet")
public class PostFormServlet extends HttpServlet {
    private PreparedStatement preparedStatement;
    PrintWriter out;

    @Override
    public void init() throws ServletException {
        initializeJDBC();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookId = request.getParameter("bookId");
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String author = request.getParameter("author");

        storeBook(Integer.parseInt(bookId),name,Integer.parseInt(price),author);
        out = response.getWriter();
        out.println(
                "bookId: " + bookId + " " +
                "name: " + name + " " +
                "price: " + price + " " +
                "author: " + author
        );
        out.close();
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
