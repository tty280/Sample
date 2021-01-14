import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author TTY280
 * Description: The query class connects the application to the database to query the uri and reqtime columns.
 */
public class Query {
    
    // Get count of each uri per day
    public void queryUri() throws SQLException, IOException {
        Database database = new Database();
        database.connectDB();
        Connection connectDB = database.conn;
        String sql = "SELECT DATE(time), COUNT(uri) AS count "
                    + "FROM public.access_logs "
                    + "GROUP BY DATE(time)";
        
        try { 
            Statement statement = connectDB.createStatement();
            ResultSet queryUriCount = statement.executeQuery(sql);
            String currentDirectory = System.getProperty("user.dir");
            
            File queryUriLog = new File(currentDirectory + "//" + "queryURI.log");
            if(queryUriLog.createNewFile()) {
                System.out.println("\nCreating log file for 'Count of each uri per day'... ");
            }
            else {
                System.out.println("\nLog file for query URI already exist. Overwritting file...");
            }
            
            FileWriter queryUriLogFile = new FileWriter(queryUriLog);
            PrintWriter queryUriLogPrint = new PrintWriter(queryUriLogFile);
            queryUriLogPrint.println("\nCount of each uri per day: ");
            while (queryUriCount.next())
            {
                String date = queryUriCount.getString("date");
                String count = queryUriCount.getString("count");
                queryUriLogPrint.printf("%s %s \n", date, count);
            }
            queryUriLogPrint.close();
            
            
        } catch (SQLException ex) { 
            System.err.println("An exception occured."); 
            System.err.println(ex);
        }
        connectDB.close();
    }
    
    // Get top 10 of reqtime per day
    public void queryReqtime() throws SQLException, IOException {
        Database database = new Database();
        database.connectDB();
        Connection connectDB = database.conn;
        String sql = "SELECT DATE(time), reqtime "
                + "FROM public.access_logs "
                + "WHERE DATE(time) IN "
                + "( "
                + "SELECT DATE(time) FROM public.access_logs "
                + "GROUP BY DATE(time), reqtime "
                + "LIMIT 10 "
                + ")";
        
        try { 
            Statement statement = connectDB.createStatement();
            ResultSet queryTopReqtime = statement.executeQuery(sql);
            String currentDirectory = System.getProperty("user.dir");
            
            File queryReqtimeLog = new File(currentDirectory + "//" + "queryReqtime.log");
            if(queryReqtimeLog.createNewFile()) {
                System.out.println("\nCreating log file for 'Top 10 of reqtime per day'... ");
            }
            else {
                System.out.println("\nLog file for query Reqtime already exist. Overwritting file...");
            }
            
            FileWriter queryReqtimeLogFile = new FileWriter(queryReqtimeLog);
            PrintWriter queryReqtimeLogPrint = new PrintWriter(queryReqtimeLogFile);
            queryReqtimeLogPrint.println("\nTop 10 of reqtime per day: ");
            while (queryTopReqtime.next())
            {
                String date = queryTopReqtime.getString("date");
                String reqtime = queryTopReqtime.getString("reqtime");
                queryReqtimeLogPrint.printf("%s %s \n", date, reqtime);
            }
            queryReqtimeLogPrint.close();
            
        } catch (SQLException ex) { 
            System.err.println("An exception occured."); 
            System.err.println(ex);
        } 
        connectDB.close();
    }
}
