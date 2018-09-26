package main.java.com.timelessapps.javafxtemplate.app.businesslogic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.Routine;
import main.java.com.timelessapps.javafxtemplate.helpers.database.StockDAO;
import main.java.com.timelessapps.javafxtemplate.helpers.services.CustomSceneHelper;
import main.java.com.timelessapps.javafxtemplate.helpers.services.FileHelper;

public class MainBotRoutine extends Routine
{
   
    public void run()
    {
    	System.out.println("Running");
		StockDAO db = new StockDAO("Test123a");
		db.createNewDatabase();
		db.createTodaysTable();
		
		FileHelper fileHelper = new FileHelper();
		String[] tickerList = null;
		try { 
			tickerList = fileHelper.getTextFromFile("C:/stockdb/tickers.txt").replaceAll("\r", "").split("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("0: " + tickerList[1] + "1");
		//System.out.println("0: " + tickerList[125] + "1");
    	
	    synchronized (this)
	    {
	        try 
	        {
	            disableStartButton();
	            while(running)
	            {
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
    }
    
    @Override
    public void checkIfPausedOrStopped() throws InterruptedException
    {
    	waitIfPaused();
    	if (!running)
    	{
    		enableStartButton();
    	}
    }

	private void disableStartButton() 
	{
		CustomSceneHelper sceneHelper = new CustomSceneHelper();
		sceneHelper.getNodeById("startButton").setDisable(true);
	}

	private void enableStartButton() 
	{
		CustomSceneHelper sceneHelper = new CustomSceneHelper();
		sceneHelper.getNodeById("startButton").setDisable(false);
	}
	
}
    