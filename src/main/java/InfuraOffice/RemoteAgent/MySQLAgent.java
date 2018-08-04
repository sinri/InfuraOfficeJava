package InfuraOffice.RemoteAgent;

import InfuraOffice.DataEntity.DatabaseEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class MySQLAgent extends SQLAgent {
    public MySQLAgent(DatabaseEntity databaseEntity) {
        super(databaseEntity);
    }

    public Connection createConnection() throws Exception {
        return createConnection(null);
    }

    public Connection createConnection(String username) throws Exception {
        if (username == null) {
            if (databaseEntity.accounts.size() <= 0) throw new Exception("No any available account");
            username = (String) databaseEntity.accounts.keySet().toArray()[0];
        }
        String password = databaseEntity.accounts.get(username);
        if (password == null) throw new Exception("No password stored for this username");
        String conn_str = "jdbc:mysql://" + databaseEntity.host + ":" + databaseEntity.port + "/?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=UTF8";

        Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
        System.out.println("成功加载MySQL驱动程序");

        // 一个Connection代表一个数据库连接
        return DriverManager.getConnection(conn_str);
    }

    public ArrayList<HashMap<String, String>> queryForData(String username, String sql) {
        try (Connection connection = createConnection(username)) {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            ArrayList<HashMap<String, String>> data = new ArrayList<>();

            if (result != null) {
                while (result.next()) {
                    HashMap<String, String> row = new HashMap<>();
                    int columnCount = result.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(result.getMetaData().getColumnLabel(i), result.getString(i));
                    }
                    data.add(row);
                }
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int queryToModify(String username, String sql) {
        try (Connection connection = createConnection(username)) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);//return 0 or number of affected rows
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
