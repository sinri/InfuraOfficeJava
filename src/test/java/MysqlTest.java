import java.sql.*;
import java.util.Objects;

public class MysqlTest {
    public static void main(String[] argv) {
        Connection connection = null;
        String sql;

        String conn_str = "jdbc:mysql://1.1.1.1:3306/test?"
                + "user=a&password=b&useUnicode=true&characterEncoding=UTF8";
        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            System.out.println("成功加载MySQL驱动程序");

            // 一个Connection代表一个数据库连接
            connection = DriverManager.getConnection(conn_str);
            Statement stmt = connection.createStatement();
            sql = "show full processlist ;";
            ResultSet result = stmt.executeQuery(sql);
            if (result != null) {
                while (result.next()) {
                    int columnCount = result.getMetaData().getColumnCount();
                    System.out.println("Row " + result.getRow() + " columns count " + columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(result.getMetaData().getColumnLabel(i) + "=" + result.getString(i) + "\t");
                    }
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(connection).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
