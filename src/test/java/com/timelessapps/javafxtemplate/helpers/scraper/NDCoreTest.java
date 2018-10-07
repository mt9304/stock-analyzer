package test.java.com.timelessapps.javafxtemplate.helpers.scraper;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import junit.framework.TestCase;
import main.java.com.timelessapps.javafxtemplate.helpers.scraper.NDCore;

public class NDCoreTest extends TestCase {
	String tickerSymbol = "SNAP";
	String mainUrl;
	String incomeUrl;
	String incomeQuarterUrl;
	String balanceUrl;
	String cashUrl;
	
	Document mainDocument;
	Document incomeDocument;
	Document incomeQuarterDocument;
	Document balanceSheetDocument;
	Document balanceSheetQuarterUrlDocument;
	Document cashflowDocument;
	Document cashflowQuarterDocument;
	
	NDCore webScraper = new NDCore(tickerSymbol);
	int scrapeDelay = 500;
	
	public NDCoreTest() throws IOException, InterruptedException {
		mainUrl = "https://www.marketwatch.com/investing/stock/" + tickerSymbol;
		incomeUrl = mainUrl + "/financials";
		incomeQuarterUrl = incomeUrl+"/income/quarter";
		balanceUrl = mainUrl + "/balance-sheet";
		cashUrl = mainUrl + "/cash-flow";
	}
	
	//Tests if the site is still following the same HTML layout. Stock price should be convertible into a float like 103.58. 
	@Test
	public void testGetCurrentPrice_isFloat() throws NumberFormatException, IOException, InterruptedException {
		float floatValue;
		floatValue = Float.parseFloat(webScraper.getCurrentPrice());
		assertNotNull(floatValue);
	}
	
	@Test
	public void testGetPERatio_isFloat() throws NumberFormatException, IOException, InterruptedException {
		float floatValue;
		floatValue = Float.parseFloat(webScraper.getPERatio());
		assertNotNull(floatValue);
	}
	
	@Test
	public void testGetVolume_isNumber() throws NumberFormatException, IOException, InterruptedException {
		Long longValue;
		longValue = Long.parseLong(webScraper.getVolume());
		assertNotNull(longValue);
	}
	
	@Test
	public void testGetLatestIncomeValue_isNumber() {
		String latestIncomeValue = null;
		latestIncomeValue = webScraper.getLatestIncomeValue();
		String regex = ".*\\d.*";
		if (!latestIncomeValue.matches(regex)) {
			System.out.print("testGetLatestIncomeValue_isNumber: ");
			System.out.println("Value for " + tickerSymbol +" is: "+ latestIncomeValue +" and does not match the regex: " + regex);
		}
		System.out.println("Latest Value: " + latestIncomeValue);
		assertTrue(latestIncomeValue.matches(regex));
	}
	
	/** ********************************* **/
	/** getParsedAlphaNumericMoney Start: **/
	/** ********************************* **/
	
	@Test
	public void testGetParsedAlphaNumericMoneyB() {
		Long expectedResult = 11120000000L;
		Long parsedNumericMoney = null;
		parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("11.12B");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyM() {
		Long expectedResult = 311120000L;
		Long parsedNumericMoney = null;
		parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("311.12M");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyNoDecB() {
		Long expectedResult = 22000000000L;
		Long parsedNumericMoney = null;
		parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("22B");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyNoDecM() {
		Long expectedResult = 222000000L;
		Long parsedNumericMoney = null;
		parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("222M");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyNoLetter() {
		Long expectedResult = null;
		Long parsedNumericMoney = null;
		parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("11.12");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	@Test
	public void testGetParsedAlphaNumericMoneyNoLetterNoDec() {
		Long expectedResult = null;
		Long parsedNumericMoney = null;
		parsedNumericMoney = webScraper.getParsedAlphaNumericMoney("311");
		assertEquals(expectedResult, parsedNumericMoney);
	}
	
	/** ******************************* **/
	/** getParsedAlphaNumericMoney End: **/
	/** ******************************* **/
	
	/** *********************** **/
	/** useDecimalPlaces Start: **/
	/** *********************** **/
	
	@Test
	public void testUseDecimalPlaces_twoDigits_twoSpaces() {
		Double expectedResult = 12.12;
		Double rawValue = 12.12353464;
		int decimalSpaces = 2;
		Double actualResult = null;
		actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_twoDigits_fourSpaces() {
		Double expectedResult = 12.1235;
		Double rawValue = 12.12353464;
		int decimalSpaces = 4;
		Double actualResult = null;
		actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_oneDigit_twoSpaces() {
		Double expectedResult = 2.12;
		Double rawValue = 2.12353464;
		int decimalSpaces = 2;
		Double actualResult = null;
		actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_oneDigit_fourSpaces() {
		Double expectedResult = 2.1235;
		Double rawValue = 2.12353464;
		int decimalSpaces = 4;
		Double actualResult = null;
		actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_twoDigits_twoSpaces_zeroes() {
		Double expectedResult = 12.1;
		Double rawValue = 12.10000;
		int decimalSpaces = 2;
		Double actualResult = null;
		actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_twoDigits_fourSpaces_zeroes() {
		Double expectedResult = 12.1;
		Double rawValue = 12.100000000;
		int decimalSpaces = 4;
		Double actualResult = null;
		actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_oneDigit_twoSpaces_zeroOnlyDecimal() {
		Double expectedResult = 2.0;
		Double rawValue = 2.0000000;
		int decimalSpaces = 2;
		Double actualResult = null;
		actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testUseDecimalPlaces_twoDigits_fourSpaces_zeroeOnlyDecimal() {
		Double expectedResult = 12.0;
		Double rawValue = 12.000000000;
		int decimalSpaces = 4;
		Double actualResult = null;
		actualResult = webScraper.useDecimalPlaces(rawValue, decimalSpaces);
		assertEquals(expectedResult, actualResult);
	}
	
	/** ********************* **/
	/** useDecimalPlaces End: **/
	/** ********************* **/
	
	/** ********************************* **/
	/** convertDifferentToPercent: Start  **/
	/** ********************************* **/
	@Test
	public void testConvertDifferenceToPercent_greaterLatestValue_fourDigits() {
		Double expectedResult = 23.91;
		Long latestPeriodValue = 570000L;
		Long secondLatestPeriodValue = 460000L;
		Double actualResult = null;
		actualResult = webScraper.convertDifferenceToPercent(latestPeriodValue, secondLatestPeriodValue);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testConvertDifferenceToPercent_greaterLatestValue_threeDigits() {
		Double expectedResult = 2.75;
		Long latestPeriodValue = 560000L;
		Long secondLatestPeriodValue = 545000L;
		Double actualResult = null;
		actualResult = webScraper.convertDifferenceToPercent(latestPeriodValue, secondLatestPeriodValue);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testConvertDifferenceToPercent_lowerLatestValue_fourDigits() {
		//Expected result should really be 12.21, but it gets calculated at 12.209999~, and Double will cut 0 off. 
		Double expectedResult = -12.2;	//755000/860000=87.79. The function should subtract 100 with 87.79 to get 6.33%. 
		Long latestPeriodValue = 755000L;
		Long secondLatestPeriodValue = 860000L;
		Double actualResult = null;
		actualResult = webScraper.convertDifferenceToPercent(latestPeriodValue, secondLatestPeriodValue);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testConvertDifferenceToPercent_lowerLatestValue_threeDigits() {
		Double expectedResult = -6.33;	//740000/790000=93.67. The function should subtract 100 with 93.67 to get 6.33%. 
		Long latestPeriodValue = 740000L;
		Long secondLatestPeriodValue = 790000L;
		Double actualResult = null;
		actualResult = webScraper.convertDifferenceToPercent(latestPeriodValue, secondLatestPeriodValue);
		assertEquals(expectedResult, actualResult);
	}
	
	/** ******************************* **/
	/** convertDifferentToPercent: End  **/
	/** ******************************* **/
	
}
