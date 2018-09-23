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
		url = "jdbc:sqlite:C:/dbs/" + fileName + ".db";
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
            pstmt.setDouble(2, price);  
            pstmt.setDouble(3, pe);  
            pstmt.setString(4, volume);  
            pstmt.setBoolean(5, positiveLatestIncome);  
            pstmt.setBoolean(6, hasIncreasingAnnualRevenue);  
            pstmt.setDouble(7, annualRevenueIncreasePrecent);  
            pstmt.setBoolean(8, hasIncreasingAnnualEPS);  
            pstmt.setDouble(9, annualEPSIncreasePrecent);  
            pstmt.setBoolean(10, hasIncreasingAnnualROE);  
            pstmt.setDouble(11, annualROEIncreasePrecent);  
            pstmt.setBoolean(12, analystsRecommend);  
            pstmt.setBoolean(13, hasMoreInsiderBuys);  
            pstmt.setString(14, industry);  
            pstmt.setString(15, sector);  
            pstmt.setBoolean(16, hasIncreasingQuarterlyRevenue);  
            pstmt.setDouble(17, quarterlyRevenueIncreasePrecent);  
            pstmt.setBoolean(18, hasIncreasingQuarterlyEPS);  
            pstmt.setDouble(19, quarterlyEPSIncreasePrecent);  
            pstmt.setBoolean(20, hasIncreasingQuarterlyROE);  
            pstmt.setDouble(21, quarterlyROEIncreasePrecent);  
            pstmt.executeUpdate();  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }  

}
