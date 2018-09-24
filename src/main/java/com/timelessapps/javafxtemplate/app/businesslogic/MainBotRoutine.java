package main.java.com.timelessapps.javafxtemplate.app.businesslogic;

import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.Routine;
import main.java.com.timelessapps.javafxtemplate.helpers.database.StockDAO;
import main.java.com.timelessapps.javafxtemplate.helpers.services.CustomSceneHelper;

public class MainBotRoutine extends Routine
{
   
    public void run()
    {
    	System.out.println("Running");
		StockDAO db = new StockDAO("Test123a");
		db.createNewDatabase();
		db.createTodaysTable();
    	
	    synchronized (this)
	    {
	        try 
	        {
	            disableStartButton();
	            while(running)
	            {
	            	checkIfPausedOrStopped();
	                /** Start scraper functions here. **/
	                
	                StockGatherer gatherer = new StockGatherer("MSFT", db);
	                
	                /** End scraper functions. **/
	                Thread.sleep(1000);
	                checkIfPausedOrStopped();
	            }
	            
	        } 
	        catch (InterruptedException ex) {Logger.getLogger(MainBotRoutine.class.getName()).log(Level.SEVERE, null, ex);}
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
    