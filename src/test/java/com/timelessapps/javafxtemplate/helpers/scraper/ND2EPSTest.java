package test.java.com.timelessapps.javafxtemplate.helpers.scraper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import main.java.com.timelessapps.javafxtemplate.helpers.scraper.ND2EPS;

//This class gets the Basic EPS from https://www.marketwatch.com/investing/stock/${tickerSymbol}/financials/income(/quarter)
public class ND2EPSTest {
	String tickerSymbol = "MSFT";
	String mainUrl;
	String incomeUrl;
	String incomeQuarterUrl;
	String balanceUrl;
	String cashUrl;
	
	ND2EPS webScraper = new ND2EPS("MSFT");
	int scrapeDelay = 500;
	
	public ND2EPSTest () throws IOException, InterruptedException {
		mainUrl = "https://www.marketwatch.com/investing/stock/" + tickerSymbol;
		incomeUrl = mainUrl + "/financials";
		incomeQuarterUrl = incomeUrl+"/income/quarter";
		balanceUrl = mainUrl + "/balance-sheet";
		cashUrl = mainUrl + "/cash-flow";
	}

	//Tests if the site is still following the same HTML layout. Header should be a four digit number like 2013. If stock is new, this is okay to fail. 
	@Test
	public void testGetFirstEPSPeriodHeader_firstAnnualHeader_isYear() throws InterruptedException, IOException {
		Document incomeDocument = getDocument(incomeUrl);
		Thread.sleep(scrapeDelay);
		String firstHeaderValue = webScraper.getEPSPeriodHeader(incomeDocument, 0);
		String regex = "\\d{4}";
		if (!firstHeaderValue.matches(regex)) {
			System.out.print("testGetEPSPeriodHeader_firstAnnualHeader_isYear: ");
			System.out.println("First available year for " + tickerSymbol +" is: "+ firstHeaderValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstHeaderValue.matches(regex));
	}
	
	//For EPS, there may not be a number for latest year. 
	@Test
	public void testGetFirstEPSPeriodValue_firstValue_hasNumbers() throws InterruptedException, IOException {
		Document incomeDocument = getDocument(incomeUrl);
		Thread.sleep(scrapeDelay);
		String firstEPSValue = webScraper.getEPSPeriodValue(incomeDocument,0);
		String regex = "\\d.*";
		if (!firstEPSValue.matches(regex)) {
			System.out.print("testGetEPSPeriodValue_firstValue_hasNumbers: ");
			System.out.println("First available value for " + tickerSymbol +" is: "+ firstEPSValue +" and does not match the regex: " + regex);
		}
		assertTrue(firstEPSValue.matches(regex));
	}
	
	//Tests the Map (LinkedHashMap) to see if it matches the pattern like {2013=77.65B, 2014=86.73B, 2015=92.97B, 2016=84.7B, 2017=89.4B}. 
	@Test
	public void testGetEPSByYears() throws NumberFormatException, IOException, InterruptedException {
		Map<String, String> epsByYears = webScraper.getEPSByYears();
		Set<String> keys = epsByYears.keySet();
		boolean matchesPattern = true;
		for(String k:keys) {
			if (!k.matches("\\d{4}")) {
				System.out.print("testGetEPSByYears: ");
				System.out.println("Key pattern: \"" + k + "\" does not match pattern for Year \"\\d{4}\"");
				matchesPattern = false;
			}
			if (!epsByYears.get(k).matches("\\d.*")) {
				System.out.print("testGetEPSByYears: ");
				System.out.println("Key: " + "\"" + k + "\" value's pattern: \"" + epsByYears.get(k) + "\" does not match pattern for EPS \"\\d.*\"");
				matchesPattern = false;
			}
		}
		assertTrue(matchesPattern);
	}
	
	//Tests the Map (LinkedHashMap) to see if it matches the pattern like {2017-Q2=23.18B, 2017-Q3=24.43B, 2017-Q4=28.9B, 2018-Q1=26.81B, 2018-Q2=30.09B}. 
	@Test
	public void testGetEPSByQuarters() throws NumberFormatException, IOException, InterruptedException {
		Map<String, String> epsByQuarters = webScraper.getEPSByQuarters();
		Set<String> keys = epsByQuarters.keySet();
		boolean matchesPattern = true;
		for(String k:keys) {
			if (!k.matches("\\d{4}[-][A-Z][\\d]")) {
				System.out.print("testGetEPSByQuarters: ");
				System.out.println("Key pattern: \"" + k + "\" does not match pattern for Year \"\\d{4}[-][A-Z][\\d]\"");
				matchesPattern = false;
			}
			if (!epsByQuarters.get(k).matches(".*\\d.*")) {
				System.out.print("testGetEPSByQuarters: ");
				System.out.println("Key: " + "\"" + k + "\" value's pattern: \"" + epsByQuarters.get(k) + "\" does not match pattern for EPS \"\\d.*\"");
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
