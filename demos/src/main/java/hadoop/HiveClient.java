package hadoop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by quanchengyun on 2017/10/12.
 */
public class HiveClient {

    private static String hive_driver="org.apache.hive.jdbc.HiveDriver";
    private  static String hive_url="";


    public void getConnect(){

        try {
            Class.forName(hive_driver);
            Connection conn = DriverManager.getConnection("jdbc:hive2://localhost:10002/default", "wyp", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
