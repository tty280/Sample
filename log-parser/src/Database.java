import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author TTY280
 * Description: The database class connects the application to a postgres database and insert the parsed data.
 */
public class Database {
    private final String currentDirectory = System.getProperty("user.dir");
    private final String configFilePath = currentDirectory + "\\config\\";
    private String sqlHostname = null;
    private String sqlDatabase = null;
    private String sqlUsername = null;
    private String sqlPassword = null;
    private String sqlUrl = null;
    public Connection conn;
    
    private Timestamp time;
    private String id;
    private String level;
    private String method;
    private String uri;
    private double reqtime;
    
    /**
     * @throws java.sql.SQLException
     * @throws java.io.FileNotFoundException
     */
    public void connectDB() throws SQLException, IOException {
        try {
            BufferedReader input = new BufferedReader(new FileReader(configFilePath + "\\config.cfg"));
            String getSqlConfig;
            String[] configArray;
            
            while(true) {
                getSqlConfig = input.readLine();
                if(getSqlConfig == null) {
                    break;
                }
                else {
                    configArray = getSqlConfig.split("\t");
                    for (String each : configArray) {
                        if(each.startsWith("Hostname:")) {
                            sqlHostname = each.replace("Hostname:", "").replaceAll("\\s", "");
                        }
                        else if(each.startsWith("Database:")) {
                            sqlDatabase = each.replace("Database:", "").replaceAll("\\s", "");
                        }
                        else if(each.startsWith("Username:")) {
                            sqlUsername = each.replace("Username:", "").replaceAll("\\s", "");
                        }
                        else if(each.startsWith("Password:")) {
                            sqlPassword = each.replace("Password:", "").replaceAll("\\s", "");
                        }
                    }
                }
            }
            sqlUrl = "jdbc:postgresql://" + sqlHostname + "/" + sqlDatabase;
            conn = DriverManager.getConnection(sqlUrl,sqlUsername,sqlPassword);
            // add retry, then timeout

        } catch (SQLException | IOException | NullPointerException ex) { 
            System.err.println("An exception occured."); 
            System.err.println(ex);
        } 
    }
    public void insertDB() throws SQLException, IOException {
        try { 
            connectDB();
            String sql = "INSERT INTO public.access_logs(time,id,level,method,uri,reqtime) VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setTimestamp(1, time);
            statement.setString(2, id);
            statement.setString(3, level);
            statement.setString(4, method);
            statement.setString(5, uri);
            statement.setDouble(6, reqtime);
            statement.executeUpdate();
            
        } catch (SQLException | IOException ex) { 
            System.err.println("An exception occured."); 
            System.err.println(ex);
        }
        conn.close();
    }
    
    public void setTime(Timestamp time){
        this.time = time;
    }
    public Timestamp getTime(){
        return time;
    }
    
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }
    
    public void setLevel(String level){
        this.level = level;
    }
    public String getLevel(){
        return level;
    }
    
    public void setMethod(String method){
        this.method = method;
    }
    public String getMethod(){
        return method;
    }
    
    public void setUri(String uri){
        this.uri = uri;
    }
    public String getUri(){
        return uri;
    }
    
    public void setReqtime(double reqtime){
        this.reqtime = reqtime;
    }
    public double getReqtime(){
        return reqtime;
    }
}