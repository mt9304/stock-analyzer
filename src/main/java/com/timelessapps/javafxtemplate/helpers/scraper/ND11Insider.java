package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.nodes.Element;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.LogType;
import main.java.com.timelessapps.javafxtemplate.helpers.services.LoggingService;

public class ND11Insider extends ND9Industry {

	LoggingService log = new LoggingService();
	
	public ND11Insider(String tickerSymbol) throws IOException, InterruptedException {
		super(tickerSymbol);
	}
	
	public Integer getSharesPurchasedInLastThreeMonths() throws IndexOutOfBoundsException  {
		Element insiderTransactionNode;
		try {
			insiderTransactionNode = insiderDocument.getElementById("insiderTransactionSummary").getElementsByClass("shares").select("div").get(0);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			try
			{
				log.appendToEventLogsFile("(" + tickerSymbol + ") Could not getSharesPurchasedInLastThreeMonths(), node not found. (" + e + ")", LogType.TRACE);
			} catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			}
			return null;
		}
		int sharesPurchasedInLastThreeMonths = Integer.parseInt(insiderTransactionNode.text().replace(",", "")); //Active value may have double quotes around it.  
		return sharesPurchasedInLastThreeMonths;
	}
	
	public Integer getSharesSoldInLastThreeMonths() throws IndexOutOfBoundsException  {
		Element insiderTransactionNode;
		try {
			insiderTransactionNode = insiderDocument.getElementById("insiderTransactionSummary").getElementsByClass("shares").select("div").get(1);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			try
			{
				log.appendToEventLogsFile("(" + tickerSymbol + ") Could not getSharesSoldInLastThreeMonths(), node not found. (" + e + ")", LogType.TRACE);
			} catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			}
			return null;
		}
		int sharesSoldInLastThreeMonths = Integer.parseInt(insiderTransactionNode.text().replace(",", "")); //Active value may have double quotes around it.  
		return sharesSoldInLastThreeMonths;
	}

}
