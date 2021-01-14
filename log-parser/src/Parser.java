import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author TTY280
 * Log Parser
 * Description: An application that parses access logs files in LTSV format, stores the data into a database, and queries the uri and reqtime columns.
 */
public class Parser {
    private final String currentDirectory = System.getProperty("user.dir");
    private final String accessLogFilePath = currentDirectory + "\\logs\\";
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws IOException, SQLException {
        
        // Extract access log archived files
        ExtractArchivedFile accessLogArchivedFile = new ExtractArchivedFile();
        accessLogArchivedFile.extractArchivedFile();
        
        // Parse access log files and stores data into database
        ParseAccessLogFile accessLogFile = new ParseAccessLogFile();
        accessLogFile.parseAccessLogFile();
        
        // Query database for uri and reqtime
        Query query = new Query();
        query.queryUri();
        query.queryReqtime();
    }
    
    private static class ExtractArchivedFile {

        public void extractArchivedFile() throws IOException {
            Parser parser = new Parser();
            byte[] buffer = new byte[1024];
            
            // List all gzip files in the logs directory
            File filePath = new File(parser.accessLogFilePath);
            File[] files = filePath.listFiles();
            
            // Extract all gzip files in the logs directory
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".gz")) {
                    try {
                        GZIPInputStream input = new GZIPInputStream(new FileInputStream(file.toString()));
                        FileOutputStream output = new FileOutputStream(file.toString().replace(".gz", ""));

                        int length;
                        while ((length = input.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                        input.close();
                        output.close();

                        System.out.println("Completed extracting archived file: " + file.getName());
                    } 
                    catch (IOException ex) {
                        System.err.println("An exception occured. " + file.getName() + " is either corrupted or empty.");
                        System.err.print(ex);
                    }
                }
            }
        }
    }
    
    private static class ParseAccessLogFile {
        String getRow;
        String[] listArray;
        ArrayList<String[]> ltsvArray = new ArrayList<>();
        int count = 0;
        int batchSize = 5000;
        Map ltsv = new HashMap();
        
        public void parseAccessLogFile() throws IOException, SQLException {
            Parser parser = new Parser();
            Database database = new Database();
            
            // List all files in the logs directory
            File filePath = new File(parser.accessLogFilePath);
            File[] files = filePath.listFiles();
            
            // Parse all log files in the logs directory
            for (File file : files) {
                BufferedReader input = new BufferedReader(new FileReader(file));
                if (file.isFile() && file.getName().endsWith(".log")) {
                    try {
                        System.out.println("Parsing file: " + file.getName() + " Please wait...");
                        // Stores parsed data in an array in batch sizes
                        while(true) {
                            getRow = input.readLine();
                            if (getRow != null)
                            {
                                listArray = getRow.split("\t");
                                ltsvArray.add(listArray);
                                if(++count % batchSize == 0) {
                                    // Stores parsed data as key and value in a hash map
                                    for (String[] ltsvString : ltsvArray) {
                                        for (String each : ltsvString) {
                                            if(each.startsWith("time")) {
                                                java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(each.replace("time:", ""));
                                                ltsv.put("time", timeStamp);
                                            }
                                            else if(each.startsWith("id")) {
                                                ltsv.put("id", each.replace("id:", ""));
                                            }
                                            else if(each.startsWith("level")) {
                                                ltsv.put("level", each.replace("level:", ""));
                                            }
                                            else if(each.startsWith("method")) {
                                                ltsv.put("method", each.replace("method:", ""));
                                            }
                                            else if(each.startsWith("uri")) {
                                                ltsv.put("uri", each.replace("uri:", ""));
                                            }
                                            else if(each.startsWith("reqtime")) {
                                                double reqTime = Double.parseDouble(each.replace("reqtime:", ""));
                                                ltsv.put("reqtime", reqTime);
                                            }
                                        }
                                        // Sets parsed data for database query
                                        database.setTime((Timestamp) ltsv.get("time"));
                                        database.setId(ltsv.get("id").toString());
                                        database.setLevel(ltsv.get("level").toString());
                                        database.setMethod(ltsv.get("method").toString());
                                        database.setUri(ltsv.get("uri").toString());
                                        database.setReqtime((double) ltsv.get("reqtime"));
                                        database.insertDB();
                                    }
                                    ltsvArray.clear();
                                }
                            }
                            else if (getRow == null) {
                                break;
                            }
                        }
                    } 
                    catch (IOException | SQLException ex) {
                        System.err.println("An exception occured. " + file.getName() + " is either corrupted or empty.");
                        System.err.print(ex);
                    }
                    System.out.println("Completed parsing file: " + file.getName());
                    input.close();
                }
            }
        }
    }
}