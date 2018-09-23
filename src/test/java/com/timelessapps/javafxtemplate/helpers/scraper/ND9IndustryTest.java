package test.java.com.timelessapps.javafxtemplate.helpers.scraper;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import main.java.com.timelessapps.javafxtemplate.helpers.scraper.ND9Industry;

public class ND9IndustryTest
{
	String tickerSymbol = "MSFT";
	String mainUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}
	String incomeUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials
	String incomeQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/income/quarter
	String balanceSheetUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/balance-sheet
	String balanceSheetQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/balance-sheet/quarter
	String cashflowUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/cash-flow
	String cashflowQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/cash-flow/quarter
	
	
	ND9Industry webScraper = new ND9Industry("MSFT");
	int scrapeDelay = 500;
	
	public ND9IndustryTest () throws IOException, InterruptedException {
		mainUrl = "https://www.marketwatch.com/investing/stock/" + tickerSymbol;
		incomeUrl = mainUrl + "/financials";
		incomeQuarterUrl = incomeUrl + "/income/quarter";
		balanceSheetUrl = incomeUrl + "/balance-sheet";
		balanceSheetQuarterUrl = balanceSheetUrl + "/quarter";
		cashflowUrl = mainUrl + "/cash-flow";
		cashflowQuarterUrl = cashflowUrl + "/quarter";
	}
	
	@Test
	public void testGetIndustry() throws InterruptedException, IOException {
		String industry = webScraper.getIndustry();
		String regex = "^[a-zA-Z\\/+\\s]*";
		if (!industry.matches(regex)) {
			System.out.print("testGetIndustry: ");
			System.out.println("Industry for " + tickerSymbol +" is: "+ industry +" and does not match the regex: " + regex);
		}
		assertTrue(industry.matches(regex));
	}
	
	@Test
	public void testGetSector() throws InterruptedException, IOException {
		String sector = webScraper.getSector();
		String regex = "^[a-zA-Z\\/+\\s]*";
		if (!sector.matches(regex)) {
			System.out.print("testGetSector: ");
			System.out.println("Sector for " + tickerSymbol +" is: "+ sector +" and does not match the regex: " + regex);
		}
		assertTrue(sector.matches(regex));
	}
}
