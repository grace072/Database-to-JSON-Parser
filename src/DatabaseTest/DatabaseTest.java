package DatabaseTest;

import java.sql.*;
import java.util.ArrayList;
import org.json.simple.*;

public class DatabaseTest {
    
//    {
//	'firstname': [],
//        'middleinitial': [],
//        'lastname': [],
//        'address': [],
//        'city': [],
//        'state': [],
//        'zip': []
//    }

  
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pSelect = null, pUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        JSONArray records = new JSONArray();
        
        String value;
        ArrayList<String> key = new ArrayList<>();
        
        boolean hasresults;
        int resultCount, columnCount, updateCount = 0;
        
        try {
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "CS488";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(server, username, password);
            if (conn.isValid(0)) {
                pSelect = conn.prepareStatement("SELECT * FROM people");
                
                boolean result = pSelect.execute();      
                System.out.println(result);
                                
                while (result || pSelect.getUpdateCount() != -1 ) {

                    if (result) {
                        resultset = pSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                                              
                        System.out.println("Get column names");
                        for (int i = 1; i <= columnCount; i++) {
                            key.add(metadata.getColumnLabel(i));
                        }
                        
                        while(resultset.next()) {
                                                     
                           JSONObject object = new JSONObject();

                            for (int i = 1; i <= columnCount; i++) {
                                
                                JSONObject jsonObject = new JSONObject();
                                value = resultset.getString(i);

                                if (resultset.wasNull()) {
                                    jsonObject.put(key.get(i-2), "NULL");
                                    jsonObject.toJSONString();
                                }
                                else {
                                    jsonObject.put(key.get(i-2), value);
                                    jsonObject.toString();
                                }
                                object.putAll(jsonObject);
                            }
                            records.add(object);
                        }
                    }
                    else {
                        resultCount = pSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }
                    }
                    hasresults = pSelect.getMoreResults();
                }
            }
            conn.close();
        }
        catch (Exception e) { e.printStackTrace(); }
        
        System.out.println(records);
    }
}

