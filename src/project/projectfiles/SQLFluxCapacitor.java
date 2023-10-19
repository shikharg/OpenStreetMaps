package project.projectfiles;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sun.org.apache.xpath.internal.Arg;

public class SQLFluxCapacitor {
    private static Connection connect;
    private Statement statement;
    private static final String selectQuery = "SELECT * from TrafficData where freeway=";
    private static final String insertQuery = "INSERT INTO TrafficData"
            + "(time, average_number_of_cars, average_speed_of_cars, freeway) VALUES"
            + "(?,?,?,?)";
    public SQLFluxCapacitor()
    {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            this.connect = DriverManager
                .getConnection("jdbc:mysql://localhost/CarData?"
                    + "user=root&password=root");
            // Statements allow to issue SQL queries to the database
            this.statement = connect.createStatement();
          } catch (Exception e) {
              e.printStackTrace();
              
          }   
    }
    /****
     * retrieveData returns a Map of the type <Time in hh:mm format, double[]>
     * where double[0] is the number of cars on the map at that time
     * and double[1] is the average speed of the cars on the map at that time
     * @return
     * @throws SQLException
     */
    public Map retrieveData(String...freeway) throws SQLException, IllegalArgumentException
    {
     Map<String, Double[]> returnMap = new HashMap<String, Double[]>();
     String queryAppend = "%";
     if(freeway.length == 1){
    	 queryAppend = freeway[0];
     }
     else if(freeway.length > 1)
     {
    	 throw new IllegalArgumentException("Too many parameters supplied: only one parameter can be parsed.");
     }
     try{
         ResultSet rs = this.statement.executeQuery(this.selectQuery + "'" + queryAppend + "'");
         while(rs.next())
         {
         Double basicSet[] = new Double[2];
         Timestamp timestamp = rs.getTimestamp("time");
         Integer numberOfCars = rs.getInt("average_number_of_cars");
         Double averageSpeedOfCars = rs.getDouble("average_speed_of_cars");
         basicSet[0] = (double)numberOfCars;
         basicSet[1] = averageSpeedOfCars;
         String time = formatTimestampAsTime(timestamp);
         returnMap.put(time, basicSet);
         }
     }
     catch(SQLException e)
     {
         e.printStackTrace();
     }
     return returnMap;
     
        
    }
    private String formatTimestampAsTime(Timestamp time)
    {
        DateFormat format = new SimpleDateFormat( "HH:mm" );
        String str = format.format(time);
        return str;
    }
    public void updateAggregateData(Map newData) throws SQLException
    {
        int averageNumberOfCars = 0;
        if(newData.get("average_number_of_cars") instanceof Integer)
        {
            averageNumberOfCars = (Integer)newData.get("average_number_of_cars");
        }
        double averageCarSpeed = 0.0;
        if(newData.get("average_speed_of_cars") instanceof Double)
        {
            averageCarSpeed = (Double)newData.get("average_speed_of_cars");
        }
        String freewayNullValue = "%";
        String insertTableSQL = insertQuery;
        PreparedStatement preparedStatement = this.connect.prepareStatement(insertTableSQL);
        preparedStatement.setTimestamp(1, getTimestamp());
        preparedStatement.setString(4, freewayNullValue);
        preparedStatement.setInt(2, averageNumberOfCars);
        preparedStatement.setDouble(3, averageCarSpeed);

        preparedStatement.executeUpdate();
    }
    public void updateFreewaySpecificData(Map newData) throws SQLException
    {
    	Iterator it = newData.entrySet().iterator();
    	while(it.hasNext())
    	{
    	String freeway = "";
   		Map.Entry pairs = (Map.Entry)it.next();	
   		if(pairs.getKey() instanceof String)
   		{
   			freeway = (String)pairs.getKey();
   		}
        String insertTableSQL = insertQuery;
        PreparedStatement preparedStatement = this.connect.prepareStatement(insertTableSQL);
        preparedStatement.setTimestamp(1, getTimestamp());
        preparedStatement.setString(4, freeway);
        Double vals[] = null;
        if(pairs.getValue() instanceof Double[])
        vals = (Double[]) pairs.getValue();
        int num_cars = (int)vals[0].doubleValue();
        preparedStatement.setInt(2,num_cars);
        preparedStatement.setDouble(3, vals[1]);
        preparedStatement.executeUpdate();
    	}

    }
    private static Timestamp getTimestamp()
    {
        //from http://www.mkyong.com/jdbc/jdbc-preparestatement-example-insert-a-record/
        java.util.Date now = new java.util.Date();
        return new Timestamp(now.getTime());
    }
}
