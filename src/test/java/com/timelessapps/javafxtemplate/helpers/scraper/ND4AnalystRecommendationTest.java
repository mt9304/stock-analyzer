package test.java.com.timelessapps.javafxtemplate.helpers.scraper;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import main.java.com.timelessapps.javafxtemplate.helpers.scraper.ND4AnalystRecommendation;

public class ND4AnalystRecommendationTest {
	String tickerSymbol = "MSFT";
	String mainUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}
	String incomeUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials
	String incomeQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/income/quarter
	String balanceSheetUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/balance-sheet
	String balanceSheetQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/balance-sheet/quarter
	String cashflowUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/cash-flow
	String cashflowQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/cash-flow/quarter
	
	
	ND4AnalystRecommendation webScraper = new ND4AnalystRecommendation("MSFT");
	int scrapeDelay = 500;
	
	public ND4AnalystRecommendationTest () throws IOException, InterruptedException {
		mainUrl = "https://www.marketwatch.com/investing/stock/" + tickerSymbol;
		incomeUrl = mainUrl + "/financials";
		incomeQuarterUrl = incomeUrl + "/income/quarter";
		balanceSheetUrl = incomeUrl + "/balance-sheet";
		balanceSheetQuarterUrl = balanceSheetUrl + "/quarter";
		cashflowUrl = mainUrl + "/cash-flow";
		cashflowQuarterUrl = cashflowUrl + "/quarter";
	}
	
	@Test
	public void testGetAnalystRecommendation() throws InterruptedException, IOException {
		String analystRecommendation = webScraper.getAnalystRecommendation();
		String regex = "^[A-Z]*";
		if (!analystRecommendation.matches(regex)) {
			System.out.print("testGetAnalystRecommendation: ");
			System.out.println("Analyst Recommendation for " + tickerSymbol +" is: "+ analystRecommendation +" and does not match the regex: " + regex);
		}
		assertTrue(analystRecommendation.matches(regex));
	}

}
