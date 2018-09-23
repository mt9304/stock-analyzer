package test.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import junit.framework.TestCase;
import main.java.com.timelessapps.javafxtemplate.helpers.scraper.ND1Revenue;

public class ND1RevenueTest extends TestCase {
	String tickerSymbol = "MSFT";
	String mainUrl;
	String incomeUrl;
	String incomeQuarterUrl;
	String balanceUrl;
	String cashUrl;
	
	ND1Revenue webScraper = new ND1Revenue(tickerSymbol);
	int scrapeDelay = 500;

	public ND1RevenueTest() throws IOException, InterruptedException
	{
		mainUrl = "https://www.marketwatch.com/investing/stock/" + tickerSymbol;
		incomeUrl = mainUrl + "/financials";
		incomeQuarterUrl = incomeUrl+"/income/quarter";
		balanceUrl = mainUrl + "/balance-sheet";
		cashUrl = mainUrl + "/cash-flow";
	}

	//Tests if the site is still following the same HTML layout. Header should be a four digit number like 2013. If stock is new, this is okay to fail. 
	@Test
	public void testGetRevenuePeriodHeader_firstAnnualHeader_isYear() throws NumberFormatException, IOException, InterruptedException {
		Document incomeDocument = Jsoup.connect(incomeUrl).get();
		Thread.sleep(scrapeDelay);
		String firstHeaderValue = webScraper.getRevenuePeriodHeader(incomeDocument, 0);
		String regex = "\\d{4}";
		if (!firstHeaderValue.matches(regex)) {
			System.out.print("testGetRevenuePeriodHeader_firstAnnualHeader_isYear: ");
			System.out.println("First available year for " + tickerSymbol +" is: "+ firstHeaderValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstHeaderValue.matches(regex));
	}
	
	//Tests if the site is still following the same HTML layout. Header should be a four digit number like 2017. 
	public void testGetRevenuePeriodHeader_lastAnnualHeader_isYears() throws NumberFormatException, IOException, InterruptedException {
		Document incomeDocument = Jsoup.connect(incomeUrl).get();
		Thread.sleep(scrapeDelay);
		String lastHeaderValue = webScraper.getRevenuePeriodHeader(incomeDocument, 4);
		String regex = "\\d{4}";
		if (!lastHeaderValue.matches(regex)) {
			System.out.print("testGetRevenuePeriodHeader_lastAnnualHeader_isYears: ");
			System.out.println("Last available year for " + tickerSymbol +" is: "+ lastHeaderValue +" and does not match the regex: " + regex);
		}
		assertTrue(lastHeaderValue.matches(regex));
	}
	
	//Tests if the site is still following the same HTML layout. Revenue value should contain at least one number like 20.2M. If stock is new, this is okay to fail.
	@Test
	public void testGetRevenuePeriodValue_firstValue_hasNumbers() throws NumberFormatException, IOException, InterruptedException {
		Document incomeDocument = Jsoup.connect(incomeUrl).get();
		Thread.sleep(scrapeDelay);
		String firstRevenueValue = webScraper.getRevenuePeriodValue(incomeDocument,0);
		String regex = "\\d.*";
		if (!firstRevenueValue.matches(regex)) {
			System.out.print("testGetRevenuePeriodValue_firstValue_hasNumbers: ");
			System.out.println("First available value for " + tickerSymbol +" is: "+ firstRevenueValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstRevenueValue.matches(regex));
	}
	
	//Tests if the site is still following the same HTML layout. Revenue value should contain at least one number like 4.2B. 
	@Test
	public void testGetRevenuePeriodValue_lastValue_hasNumbers() throws NumberFormatException, IOException, InterruptedException {
		Document incomeDocument = Jsoup.connect(incomeUrl).get();
		Thread.sleep(scrapeDelay);
		String lastHeaderValue = webScraper.getRevenuePeriodValue(incomeDocument, 4);
		String regex = "\\d.*";
		if (!lastHeaderValue.matches(regex)) {
			System.out.print("testGetRevenuePeriodValue_lastValue_hasNumbers: ");
			System.out.println("Last available value for " + tickerSymbol +" is: "+ lastHeaderValue +" and does not match the regex: " + regex);
		}
		assertTrue(lastHeaderValue.matches(regex));
	}
	
	//Tests if the site is still following the same HTML layout. Header should be a full date like 31-Dec-2016. If stock is new, this is okay to fail. 
	@Test
	public void testGetRevenuePeriodHeader_firstQuarterHeader_isQuarter() throws NumberFormatException, IOException, InterruptedException {
		Document incomeQuarterDocument = Jsoup.connect(incomeQuarterUrl).get();
		Thread.sleep(scrapeDelay);
		String firstHeaderValue = webScraper.getRevenuePeriodHeader(incomeQuarterDocument, 0);
		String regex = "\\d{2}[-][A-Z][a-z]{2}[-]\\d{4}";
		if (!firstHeaderValue.matches(regex)) {
			System.out.print("testGetRevenuePeriodHeader_firstQuarterHeader_isQuarter: ");
			System.out.println("First available year for " + tickerSymbol +" is: "+ firstHeaderValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstHeaderValue.matches(regex));
	}
	
	//Tests if the site is still following the same HTML layout. Header should be a full date like 31-Dec-2016. 
	@Test
	public void testGetRevenuePeriodHeader_lastQuarterHeader_isQuarter() throws NumberFormatException, IOException, InterruptedException {
		Document incomeQuarterDocument = Jsoup.connect(incomeQuarterUrl).get();
		Thread.sleep(scrapeDelay);
		String lastHeaderValue = webScraper.getRevenuePeriodHeader(incomeQuarterDocument, 4);
		String regex = "\\d{2}[-][A-Z][a-z]{2}[-]\\d{4}";
		 if (!lastHeaderValue.matches(regex)) {
			System.out.print("testGetRevenuePeriodHeader_lastQuarterHeader_isQuarter: ");
			System.out.println("Last available year for " + tickerSymbol +" is: "+ lastHeaderValue +" and does not match the regex: " + regex);
		 }
		assertTrue(lastHeaderValue.matches(regex));
	}
	
	//Tests if the site is still following the same HTML layout. Revenue value should contain at least one number like 20.2M. If stock is new, this is okay to fail.
	@Test
	public void testGetRevenuePeriodValue_firstQuarterValue_hasNumbers() throws NumberFormatException, IOException, InterruptedException {
		Document incomeQuarterDocument = Jsoup.connect(incomeQuarterUrl).get();
		Thread.sleep(scrapeDelay);
		String firstRevenueValue = webScraper.getRevenuePeriodValue(incomeQuarterDocument,0);
		String regex = "\\d.*";
		 if (!firstRevenueValue.matches(regex)) {
			 System.out.print("testGetRevenuePeriodValue_firstQuarterValue_hasNumbers: ");
			 System.out.println("First Available Value For " + tickerSymbol +" Is: "+ firstRevenueValue +" and does not match the regex: " + regex);
		 }
		assertTrue(firstRevenueValue.matches(regex));
	}
	
	//Tests if the site is still following the same HTML layout. Revenue value should contain at least one number like 4.2B. 
	@Test
	public void testGetRevenuePeriodValue_lastQuarterValue_hasNumbers() throws NumberFormatException, IOException, InterruptedException {
		Document incomeQuarterDocument = Jsoup.connect(incomeQuarterUrl).get();
		Thread.sleep(scrapeDelay);
		String lastHeaderValue = webScraper.getRevenuePeriodValue(incomeQuarterDocument, 4);
		String regex = "\\d.*";
		 if (!lastHeaderValue.matches(regex)) {
			 System.out.print("testGetRevenuePeriodValue_lastQuarterValue_hasNumbers: ");
			 System.out.println("Last available value for " + tickerSymbol +" is: "+ lastHeaderValue +" and does not match the regex: " + regex);
		 }
		assertTrue(lastHeaderValue.matches(regex));
	}
	
	//Tests the Map (LinkedHashMap) to see if it matches the pattern like {2013=77.65B, 2014=86.73B, 2015=92.97B, 2016=84.7B, 2017=89.4B}. 
	@Test
	public void testGetRevenueByYears() throws NumberFormatException, IOException, InterruptedException {
		Map<String, String> revenueByYears = webScraper.getRevenueByYears();
		Set<String> keys = revenueByYears.keySet();
		boolean matchesPattern = true;
		for(String k:keys) {
			if (!k.matches("\\d{4}")) {
				System.out.print("testGetRevenueByYears: ");
				System.out.println("Key pattern: \"" + k + "\" does not match pattern for Year \"\\d{4}\"");
				matchesPattern = false;
			}
			if (!revenueByYears.get(k).matches("\\d.*")) {
				System.out.print("testGetRevenueByYears: ");
				System.out.println("Key: " + "\"" + k + "\" value's pattern: \"" + revenueByYears.get(k) + "\" does not match pattern for Revenue \"\\d.*\"");
				matchesPattern = false;
			}
		}
		assertTrue(matchesPattern);
	}
	
	//Tests the Map (LinkedHashMap) to see if it matches the pattern like {2017-Q2=23.18B, 2017-Q3=24.43B, 2017-Q4=28.9B, 2018-Q1=26.81B, 2018-Q2=30.09B}. 
	@Test
	public void testGetRevenueByQuarters() throws NumberFormatException, IOException, InterruptedException {
		Map<String, String> revenueByQuarters = webScraper.getRevenueByQuarters();
		Set<String> keys = revenueByQuarters.keySet();
		boolean matchesPattern = true;
		for(String k:keys) {
			if (!k.matches("\\d{4}[-][A-Z][\\d]")) {
				System.out.print("testGetRevenueByQuarters: ");
				System.out.println("Key pattern: \"" + k + "\" does not match pattern for Year \"\\d{4}[-][A-Z][\\d]\"");
				matchesPattern = false;
			}
			if (!revenueByQuarters.get(k).matches("\\d.*")) {
				System.out.print("testGetRevenueByQuarters: ");
				System.out.println("Key: " + "\"" + k + "\" value's pattern: \"" + revenueByQuarters.get(k) + "\" does not match pattern for Revenue \"\\d.*\"");
				matchesPattern = false;
			}
		}
		assertTrue(matchesPattern);
	}

}
