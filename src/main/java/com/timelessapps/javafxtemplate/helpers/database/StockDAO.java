package main.java.com.timelessapps.javafxtemplate.helpers.database;

import java.io.File;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StockDAO {
	String fileName;
	//Connection string. 
	String url;
	
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	LocalDateTime todaysDate = LocalDateTime.now();
	String dateText = dateTimeFormatter.format(todaysDate);
	String tableName = "Stocks_" + dateText;
	
	public StockDAO(String fileName) {
		this.fileName = fileName;
		url = "jdbc:sqlite:C:/stockdb/db/" + fileName + ".db";
	}
	
    private Connection connect() {  
        Connection conn = null;  
        try {  
            conn = DriverManager.getConnection(url);  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
        return conn;  
    }  

	//Adds .db to the end of file name. Checks if file name already exists. If not, creates a new database. 
	public void createNewDatabase() {
        File db = new File("C:/dbs/" + fileName + ".db");
        
        if (!db.exists()) {
	        try (Connection conn = DriverManager.getConnection(url)) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                System.out.println("The driver name is " + meta.getDriverName());
	                System.out.println("A new database has been created.");
	            }
	            else {
	            	System.out.println("Database connection may be null. ");
	            }
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
        } else {
        	System.out.println("Database " + url + " already exists. ");
        }

    }
	
    public void createTodaysTable() {   
        //SQL statement for creating a new table with today's date. 
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"  
                + " id integer PRIMARY KEY,\n"  
                + " Ticker text NOT NULL,\n"  
                + " Price real,\n" 
                + " PE real,\n"  
                + " Volume text,\n" 
                + " PositiveLatestIncome boolean,\n"  
                + " HasIncreasingAnnualRevenue boolean,\n" 
                + " AnnualRevenueIncreasePrecent real,\n"  
                + " HasIncreasingAnnualEPS boolean,\n"  
                + " AnnualEPSIncreasePrecent real,\n"  
                + " HasIncreasingAnnualROE boolean,\n" 
                + " AnnualROEIncreasePrecent real,\n"  
                + " AnalystsRecommend boolean,\n"  
                + " HasMoreInsiderBuys boolean,\n"  
                + " Industry text,\n"  
                + " Sector text,\n"  
                + " HasIncreasingQuarterlyRevenue boolean,\n" 
                + " QuarterlyRevenueIncreasePrecent real,\n"  
                + " HasIncreasingQuarterlyEPS boolean,\n"  
                + " QuarterlyEPSIncreasePrecent real,\n"  
                + " HasIncreasingQuarterlyROE boolean,\n" 
                + " QuarterlyROEIncreasePrecent real,\n"  
                + " DateTime timestamp\n"  
                + ");";  
          
        try{  
            Connection conn = DriverManager.getConnection(url);  
            Statement stmt = conn.createStatement();  
            stmt.execute(sql);  
        } catch (SQLException e) {
            System.out.println(e.getMessage());  
        }  
    }
    
    public void insert(String ticker, Double price, Double pe, String volume, Boolean positiveLatestIncome, Boolean hasIncreasingAnnualRevenue, Double annualRevenueIncreasePrecent,Boolean hasIncreasingAnnualEPS, Double annualEPSIncreasePrecent, Boolean hasIncreasingAnnualROE, Double annualROEIncreasePrecent, Boolean analystsRecommend, Boolean hasMoreInsiderBuys, String industry, String sector, Boolean hasIncreasingQuarterlyRevenue, Double quarterlyRevenueIncreasePrecent, Boolean hasIncreasingQuarterlyEPS, Double quarterlyEPSIncreasePrecent, Boolean hasIncreasingQuarterlyROE, Double quarterlyROEIncreasePrecent) {  
        String sql = "INSERT INTO "  + tableName + 
        		"(Ticker, Price, PE, Volume, PositiveLatestIncome, "
        		+ "HasIncreasingAnnualRevenue, AnnualRevenueIncreasePrecent, HasIncreasingAnnualEPS, AnnualEPSIncreasePrecent, HasIncreasingAnnualROE, AnnualROEIncreasePrecent, "
        		+ "AnalystsRecommend, HasMoreInsiderBuys, Industry, Sector, "
        		+ "HasIncreasingQuarterlyRevenue, QuarterlyRevenueIncreasePrecent, HasIncreasingQuarterlyEPS, QuarterlyEPSIncreasePrecent, HasIncreasingQuarterlyROE, QuarterlyROEIncreasePrecent, "
        		+ "DateTime) "
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
   
        try{  
        	
            Connection conn = this.connect();  
            PreparedStatement pstmt = conn.prepareStatement(sql);  
            pstmt.setString(1, ticker);  
            setDataOrNull(pstmt, 2, price);  
            setDataOrNull(pstmt, 3, pe);  
            pstmt.setString(4, volume);  
            setDataOrNull(pstmt, 5, positiveLatestIncome);  
            setDataOrNull(pstmt, 6, hasIncreasingAnnualRevenue);  
            setDataOrNull(pstmt, 7, annualRevenueIncreasePrecent);  
            setDataOrNull(pstmt, 8, hasIncreasingAnnualEPS);  
            setDataOrNull(pstmt, 9, annualEPSIncreasePrecent);  
            setDataOrNull(pstmt, 10, hasIncreasingAnnualROE);  
            setDataOrNull(pstmt, 11, annualROEIncreasePrecent);  
            setDataOrNull(pstmt, 12, analystsRecommend);  
            setDataOrNull(pstmt, 13, hasMoreInsiderBuys);  
            pstmt.setString(14, industry);  
            pstmt.setString(15, sector);  
            setDataOrNull(pstmt, 16, hasIncreasingQuarterlyRevenue);  
            setDataOrNull(pstmt, 17, quarterlyRevenueIncreasePrecent);  
            setDataOrNull(pstmt, 18, hasIncreasingQuarterlyEPS);  
            setDataOrNull(pstmt, 19, quarterlyEPSIncreasePrecent);  
            setDataOrNull(pstmt, 20, hasIncreasingQuarterlyROE);  
            setDataOrNull(pstmt, 21, quarterlyROEIncreasePrecent);  
            pstmt.executeUpdate();  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }  
    
    /** For setting values to null if the returned value doesnt exist, since nulls with run into errors without this.  */
    public void setDataOrNull(PreparedStatement pstmt, int column, Integer value) throws SQLException
    {
        if (value != null) {
            pstmt.setInt(column, value);
        } else {
            pstmt.setNull(column, java.sql.Types.INTEGER);
        }
    }
      
    public void setDataOrNull(PreparedStatement pstmt, int column, Boolean value) throws SQLException
    {
        if (value != null) {
            pstmt.setBoolean(column, value);
        } else {
            pstmt.setNull(column, java.sql.Types.BOOLEAN);
        }
    }
    
    public void setDataOrNull(PreparedStatement pstmt, int column, Double value) throws SQLException
    {
        if (value != null) {
            pstmt.setDouble(column, value);
        } else {
            pstmt.setNull(column, java.sql.Types.DOUBLE);
        }
    }

}
