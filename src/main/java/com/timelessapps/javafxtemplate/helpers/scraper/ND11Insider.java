package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class ND11Insider extends ND9Industry {

	public ND11Insider(String tickerSymbol) throws IOException, InterruptedException {
		super(tickerSymbol);
	}
	
	public Integer getSharesPurchasedInLastThreeMonths() throws IndexOutOfBoundsException {
		Element insiderTransactionNode;
		try {
			insiderTransactionNode = insiderDocument.getElementById("insiderTransactionSummary").getElementsByClass("shares").select("div").get(0);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getSharesPurchasedInLastThreeMonths(), node not found. ");
			return null;
		}
		int sharesPurchasedInLastThreeMonths = Integer.parseInt(insiderTransactionNode.text().replace(",", "")); //Active value may have double quotes around it.  
		return sharesPurchasedInLastThreeMonths;
	}
	
	public Integer getSharesSoldInLastThreeMonths() throws IndexOutOfBoundsException {
		Element insiderTransactionNode;
		try {
			insiderTransactionNode = insiderDocument.getElementById("insiderTransactionSummary").getElementsByClass("shares").select("div").get(1);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getSharesSoldInLastThreeMonths(), node not found. ");
			return null;
		}
		int sharesSoldInLastThreeMonths = Integer.parseInt(insiderTransactionNode.text().replace(",", "")); //Active value may have double quotes around it.  
		return sharesSoldInLastThreeMonths;
	}

}
