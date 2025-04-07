package book;

import Obj.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet(name = "BookServlet", value = "/BookServlet")
public class BookServlet extends HttpServlet {
    private PreparedStatement preparedStatement;

    @Override
    public void init() throws ServletException {
        initializeJDBC();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            preparedStatement.setString(1,request.getParameter("bookId"));
            ResultSet resultSet = preparedStatement.executeQuery();
            Book book = null;

            if (resultSet.next()) {
                book = new Book();
                book.setBookId(resultSet.getInt("bookId"));
                book.setName(resultSet.getString("name"));
                book.setPrice(resultSet.getInt("price"));
                book.setAuthor(resultSet.getString("author"));
            }

            request.setAttribute("book", book);
            request.getRequestDispatcher("/book.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

            preparedStatement = connection.prepareStatement("select * from book where bookId = ?");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
