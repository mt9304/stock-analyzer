package main.java.com.timelessapps.javafxtemplate.app.businesslogic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.Routine;
import main.java.com.timelessapps.javafxtemplate.helpers.database.StockDAO;
import main.java.com.timelessapps.javafxtemplate.helpers.services.FileHelper;

public class ScrapeWithoutGUI extends Routine
{
    public void run()
    {
    	System.out.println("Running");
		StockDAO db = new StockDAO("StockDB");
		db.createNewDatabase();
		db.createTodaysTable();
		
		FileHelper fileHelper = new FileHelper();
		String[] tickerList = null;
		try { 
			tickerList = fileHelper.getTextFromFile("C:/stockdb/tickers.txt").replaceAll("\r", "").split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
	    synchronized (this)
	    {
	        try 
	        {
	            while(running)
	            {
	            	System.out.println("Starting to scrape. ");
	            	for (int i = 0; i < tickerList.length; i++) {
		            	checkIfPausedOrStopped();
		                /** Start scraper functions here. **/
		                
		                StockGatherer gatherer = new StockGatherer(tickerList[i], db);
		                gatherer.gather();
		                
		                /** End scraper functions. **/
		                Thread.sleep(1000);
		                checkIfPausedOrStopped();
	            	}
	            	running = false;
	            	System.out.println("Finished list of stocks. ");
	            }
	        } 
	        catch (InterruptedException ex) {Logger.getLogger(MainBotRoutine.class.getName()).log(Level.SEVERE, null, ex);} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    System.out.println("Done. ");
    }
}
