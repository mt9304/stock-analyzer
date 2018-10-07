package test.java.com.timelessapps.javafxtemplate.helpers.scraper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import main.java.com.timelessapps.javafxtemplate.helpers.scraper.ND3ROE;

public class ND3ROETest {
	String tickerSymbol = "MSFT";
	String mainUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}
	String incomeUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials
	String incomeQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/income/quarter
	String balanceSheetUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/balance-sheet
	String balanceSheetQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/balance-sheet/quarter
	String cashflowUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/cash-flow
	String cashflowQuarterUrl; //https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/cash-flow/quarter
	
	
	ND3ROE webScraper = new ND3ROE("MSFT");
	int scrapeDelay = 500;
	
	public ND3ROETest () throws IOException, InterruptedException {
		mainUrl = "https://www.marketwatch.com/investing/stock/" + tickerSymbol;
		incomeUrl = mainUrl + "/financials";
		incomeQuarterUrl = incomeUrl + "/income/quarter";
		balanceSheetUrl = incomeUrl + "/balance-sheet";
		balanceSheetQuarterUrl = balanceSheetUrl + "/quarter";
		cashflowUrl = mainUrl + "/cash-flow";
		cashflowQuarterUrl = cashflowUrl + "/quarter";
	}

	//Tests if the site is still following the same HTML layout. Header should be a four digit number like 2013. If stock is new, this is okay to fail. 
	@Test
	public void testGetFirstNetIncomePeriodHeader_firstAnnualHeader_isYear() throws InterruptedException, IOException {
		Document incomeDocument = getDocument(incomeUrl);
		Thread.sleep(scrapeDelay);
		String firstHeaderValue = webScraper.getNetIncomePeriodHeader(incomeDocument, 0);
		String regex = "\\d{4}";
		if (!firstHeaderValue.matches(regex)) {
			System.out.print("testGetNetIncomePeriodHeader_firstAnnualHeader_isYear: ");
			System.out.println("First available year for " + tickerSymbol +" is: "+ firstHeaderValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstHeaderValue.matches(regex));
	}
	
	@Test
	public void testGetFirstNetIncomePeriodValue_firstValue_hasNumbers() throws InterruptedException, IOException {
		Document incomeDocument = getDocument(incomeUrl);
		Thread.sleep(scrapeDelay);
		String firstNetIncomeValue = webScraper.getNetIncomePeriodValue(incomeDocument,0);
		String regex = "\\d.*";
		if (!firstNetIncomeValue.matches(regex)) {
			System.out.print("testGetNetIncomePeriodValue_firstValue_hasNumbers: ");
			System.out.println("First available value for " + tickerSymbol +" is: "+ firstNetIncomeValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstNetIncomeValue.matches(regex));
	}
	
	//Tests if the site is still following the same HTML layout. Header should be a four digit number like 2013. If stock is new, this is okay to fail. 
	@Test
	public void testGetFirstShareHolderEquityPeriodHeader_firstAnnualHeader_isYear() throws InterruptedException, IOException {
		Document balanceSheetDocument = getDocument(balanceSheetUrl);
		Thread.sleep(scrapeDelay);
		String firstHeaderValue = webScraper.getShareHolderEquityPeriodHeader(balanceSheetDocument, 0);
		String regex = "\\d{4}";
		if (!firstHeaderValue.matches(regex)) {
			System.out.print("testGetShareHolderEquityPeriodHeader_firstAnnualHeader_isYear: ");
			System.out.println("First available year for " + tickerSymbol +" is: "+ firstHeaderValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstHeaderValue.matches(regex));
	}
	
	//For EPS, there may not be a number for latest year. 
	@Test
	public void testGetFirstShareHolderEquityPeriodValue_firstValue_hasNumbers() throws InterruptedException, IOException {
		Document balanceSheetDocument = getDocument(balanceSheetUrl);
		Thread.sleep(scrapeDelay);
		String firstShareHolderEquityValue = webScraper.getShareHolderEquityPeriodValue(balanceSheetDocument,0);
		String regex = "\\d.*";
		if (!firstShareHolderEquityValue.matches(regex)) {
			System.out.print("testGetShareHolderEquityPeriodValue_firstValue_hasNumbers: ");
			System.out.println("First available value for " + tickerSymbol +" is: "+ firstShareHolderEquityValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstShareHolderEquityValue.matches(regex));
	}
	
	
	//Tests the Map (LinkedHashMap) to see if it matches the pattern like {2013=77.65B, 2014=86.73B, 2015=92.97B, 2016=84.7B, 2017=89.4B}. 
	@Test
	public void testGetROEByYears() throws NumberFormatException, IOException, InterruptedException {
		Map<String, String> roeByYears = webScraper.getROEByYears();
		Set<String> keys = roeByYears.keySet();
		boolean matchesPattern = true;
		for(String k:keys) {
			if (!k.matches("\\d{4}")) {
				System.out.print("testGetEPSByYears: ");
				System.out.println("Key pattern: \"" + k + "\" does not match pattern for Year \"\\d{4}\"");
				matchesPattern = false;
			}
			if (!roeByYears.get(k).matches("\\d.*")) {
				System.out.print("testGetEPSByYears: ");
				System.out.println("Key: " + "\"" + k + "\" value's pattern: \"" + roeByYears.get(k) + "\" does not match pattern for EPS \"\\d.*\"");
				matchesPattern = false;
			}
		}
		assertTrue(matchesPattern);
	}
	
	//Tests the Map (LinkedHashMap) to see if it matches the pattern like {2017-Q2=23.18B, 2017-Q3=24.43B, 2017-Q4=28.9B, 2018-Q1=26.81B, 2018-Q2=30.09B}. 
	@Test
	public void testGetROEByQuarters() throws NumberFormatException, IOException, InterruptedException {
		Map<String, String> roeByQuarters = webScraper.getROEByQuarters();
		Set<String> keys = roeByQuarters.keySet();
		boolean matchesPattern = true;
		for(String k:keys) {
			if (!k.matches("\\d{4}[-][A-Z][\\d]")) {
				System.out.print("testGetROEByQuarters: ");
				System.out.println("Key pattern: \"" + k + "\" does not match pattern for Year \"\\d{4}[-][A-Z][\\d]\"");
				matchesPattern = false;
			}
			if (!roeByQuarters.get(k).matches(".*\\d.*")) {
				System.out.print("testGetROEByQuarters: ");
				System.out.println("Key: " + "\"" + k + "\" value's pattern: \"" + roeByQuarters.get(k) + "\" does not match pattern for ROE \"\\d.*\"");
				matchesPattern = false;
			}
		}
		assertTrue(matchesPattern);
	}
	
	//For getting documents and avoiding timeout error. 
	public Document getDocument(String url) {
	    Document doc = null;

	    for (int i=0;i<3;i++) {
	        try {
	        	System.out.println(tickerSymbol + ": Getting document for " + url);
	            doc = Jsoup.connect(url).get();
	            break;
	        } catch (SocketTimeoutException ex){
	            System.out.println(tickerSymbol + ": Could not get document for " + url + ". Trying " + i+1 + "/3");              
	        }
	        catch (IOException e) {
	        	System.out.println(tickerSymbol + ": Could not get document for " + url + ". Trying " + i+1 + "/3"); 
	        }           
	    }
	    return doc;
	}
}
