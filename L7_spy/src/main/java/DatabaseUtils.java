import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    static final String DB_URL = "jdbc:mysql://localhost:3306/forbidden_words?useUnicode=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "root";

    public static void writeToDB(String[] forbiddenWords) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO word (name) values (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            for(String s: forbiddenWords) {
                preparedStatement.setString(1, s);
                preparedStatement.executeUpdate();
            }

        } catch (Exception ex) {
            System.out.println("Connection failed");
            System.out.println(ex);
        } finally {
        }
    }
    public static void clearDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            conn.prepareStatement("DELETE FROM word").executeUpdate();
        }catch (Exception ex) {
            System.out.println("Connection failed");
            System.out.println(ex);
        }
    }

    public static Connection getDatabaseConnection() {

        System.out.println("Testing connection to MySQL JDBC");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return null;
        }

        System.out.println("MySQL JDBC Driver successfully connected");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
        return connection;

    }

    public static List<String> readFromDB(Connection cn) {
        List<String> forbiddenWords = new ArrayList<>();
        try {
            Statement st = cn.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery("SELECT * FROM word");
            while (rs.next()) {
                String word = rs.getString(2);
                forbiddenWords.add(word);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());

        }
        return forbiddenWords;
    }



}
