package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.LogType;

public class ND4AnalystRecommendation extends ND3ROE{

	public ND4AnalystRecommendation(String tickerSymbol) throws IOException, InterruptedException {
		super(tickerSymbol);
	}
	
	public String getAnalystRecommendation() throws IndexOutOfBoundsException, FileNotFoundException {
		Element analystRecommendationNode;
		try {
			analystRecommendationNode = analystDocument.select("table.ratings > tbody > tr").get(5).getElementsByClass("current").get(0);
		} catch (IndexOutOfBoundsException e) {
			log.appendToEventLogsFile("(" + tickerSymbol + ") Could not getAnalystRecommendation(), node not found. (" + e + ")", LogType.TRACE);
			return null;
		}
		String analystRecommendation = analystRecommendationNode.text().replaceAll("[()]\"", "").toUpperCase(); //Active value may have double quotes around it.  
		return analystRecommendation;
	}

}
