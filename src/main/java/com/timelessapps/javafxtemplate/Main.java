/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.timelessapps.javafxtemplate;

import java.util.Arrays;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.com.timelessapps.javafxtemplate.app.businesslogic.MainBotRoutine;
import main.java.com.timelessapps.javafxtemplate.app.businesslogic.ScrapeWithoutGUI;
import main.java.com.timelessapps.javafxtemplate.app.supportingthreads.GlobalKeyListener;
import main.java.com.timelessapps.javafxtemplate.controllers.contentarea.LogsPageController;

public class Main extends Application
{
    private static Stage stage;
    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws Exception
    {
        stage.initStyle(StageStyle.UNDECORATED);
        setPrimaryStage(stage);
        setPrimaryScene(scene);
        Parent root = FXMLLoader.load(getClass().getResource("view/fxml/alwaysdisplayed/Main.fxml"));

        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();  
    }
    
    public static void main(String[] args)
    {
    	if (args.length == 0)
    	{
    		launch(args);
    	} else {
	    	for (int i =0; i < args.length; i++) {
	    		if (args[i].equals("runWithoutGUI")) {
	    			runWithoutGUI();
	    		} else {
	    			System.out.println("Invalid args detect, exiting: " + Arrays.toString(args));
	    			System.exit(0);
	    		}
	    	}
    	}
    }

    private void setPrimaryStage(Stage stage) 
    {
        Main.stage = stage;
    }
    
    public static Stage getMainStage() 
    {
        return Main.stage;
    }
    
    private void setPrimaryScene(Scene scene)
    {
        Main.scene = scene;
    }
    
    public static Scene getMainScene()
    {
        return Main.scene;
    }
    
    public static void runWithoutGUI() {
    	System.out.println("Running without GUI. ");
    	ScrapeWithoutGUI scrapeWithoutGUI = new ScrapeWithoutGUI();
    	scrapeWithoutGUI.setDaemon(true);
    	scrapeWithoutGUI.start();

    	GlobalKeyListener globalKeyListener = new GlobalKeyListener(scrapeWithoutGUI);
    	globalKeyListener.setDaemon(true);
    	globalKeyListener.start();
    	
    	try {
			scrapeWithoutGUI.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

    	System.out.println("Run completed without GUI, exiting. ");
    	System.exit(0);
    }
}
