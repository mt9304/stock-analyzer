package test.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.IOException;
import static org.junit.Assert.*;

import org.junit.Test;

import main.java.com.timelessapps.javafxtemplate.helpers.scraper.ND11Insider;

public class ND11InsiderTest {
	String tickerSymbol = "MSFT";
	String mainUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}
	String incomeUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials
	String incomeQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/income/quarter
	String balanceSheetUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/balance-sheet
	String balanceSheetQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/balance-sheet/quarter
	String cashflowUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/cash-flow
	String cashflowQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/cash-flow/quarter
	
	
	ND11Insider webScraper = new ND11Insider("MSFT");
	int scrapeDelay = 500;
	
	public ND11InsiderTest () throws IOException, InterruptedException {
		
	}
	
	@Test
	public void testGetSharesPurchasedInLastThreeMonths() throws InterruptedException, IOException {
		int sharesPurchasedInLastThreeMonths = webScraper.getSharesPurchasedInLastThreeMonths();
		String regex = "^\\d*";
		if (!Integer.toString(sharesPurchasedInLastThreeMonths).matches(regex)) {
			System.out.print("testGetSharesPurchasedInLastThreeMonths: ");
			System.out.println("Shares purchased for " + tickerSymbol +" is: "+ sharesPurchasedInLastThreeMonths +" and does not match the regex: " + regex);
		}
		assertTrue(Integer.toString(sharesPurchasedInLastThreeMonths).matches(regex));
	}
	
	@Test
	public void testGetSharesSoldInLastThreeMonths() throws InterruptedException, IOException {
		int sharesSoldInLastThreeMonths = webScraper.getSharesSoldInLastThreeMonths();
		String regex = "^\\d*";
		if (!Integer.toString(sharesSoldInLastThreeMonths).matches(regex)) {
			System.out.print("testGetSharesPurchasedInLastThreeMonths: ");
			System.out.println("Shares purchased for " + tickerSymbol +" is: "+ sharesSoldInLastThreeMonths +" and does not match the regex: " + regex);
		}
		assertTrue(Integer.toString(sharesSoldInLastThreeMonths).matches(regex));
	}
}