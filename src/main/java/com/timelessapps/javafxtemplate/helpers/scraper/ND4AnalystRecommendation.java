package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.nodes.Element;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.LogType;

public class ND4AnalystRecommendation extends ND3ROE{

	public ND4AnalystRecommendation(String tickerSymbol) throws IOException, InterruptedException {
		super(tickerSymbol);
	}
	
	public String getAnalystRecommendation() throws IndexOutOfBoundsException {
		Element analystRecommendationNode;
		try {
			analystRecommendationNode = analystDocument.select("table.ratings > tbody > tr").get(5).getElementsByClass("current").get(0);
		} catch (IndexOutOfBoundsException e) {
			try
			{
				log.appendToEventLogsFile("(" + tickerSymbol + ") Could not getAnalystRecommendation(), node not found. (" + e + ")", LogType.TRACE);
			} catch (FileNotFoundException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
		String analystRecommendation = analystRecommendationNode.text().replaceAll("[()]\"", "").toUpperCase(); //Active value may have double quotes around it.  
		return analystRecommendation;
	}

}
