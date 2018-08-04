package InfuraOffice.RemoteAgent;

import InfuraOffice.DataEntity.DatabaseEntity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

abstract public class SQLAgent {
    protected DatabaseEntity databaseEntity;

    protected SQLAgent(DatabaseEntity databaseEntity) {
        this.databaseEntity = databaseEntity;
    }

    abstract public Connection createConnection() throws Exception;

    abstract public Connection createConnection(String username) throws Exception;

    abstract public ArrayList<HashMap<String, String>> queryForData(String username, String sql);

    abstract public int queryToModify(String username, String sql);

    public static SQLAgent getSQLAgent(DatabaseEntity databaseEntity) throws Exception {
        switch (databaseEntity.type) {
            case DatabaseEntity.DB_TYPE_MYSQL:
                return new MySQLAgent(databaseEntity);
            default:
                throw new Exception("Unsupported database engine type");
        }
    }

}
