package test.java.com.timelessapps.javafxtemplate.helpers.scraper;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.Period;
import main.java.com.timelessapps.javafxtemplate.helpers.scraper.NDWrapper;

public class NDWrapperTest {
	String tickerSymbol = "MSFT";
	NDWrapper ndCalc = new NDWrapper(tickerSymbol);
	
	public NDWrapperTest() throws IOException, InterruptedException {
		
	}

	/** ***************************** **/
	/** revenuePercentIncrease Start: **/
	/** ***************************** **/
	@Test
	public void testRevenuePercentIncrease_year() throws IOException, InterruptedException {
		Double percentIncrease = ndCalc.revenuePercentIncrease(Period.YEAR);
		String regex = "\\d.*";
		if (!percentIncrease.toString().matches(regex)) {
			System.out.print("testRevenuePercentIncrease_year: ");
			System.out.println("Revenue increase/decrease percent for " + tickerSymbol +" is: "+ percentIncrease +" and does not match the regex: " + regex);
		}
		assertTrue(percentIncrease.toString().matches(regex));
	}
	
	@Test
	public void testRevenuePercentIncrease_quarter() throws IOException, InterruptedException {
		Double percentIncrease = ndCalc.revenuePercentIncrease(Period.QUARTER);
		String regex = "\\d.*";
		if (!percentIncrease.toString().matches(regex)) {
			System.out.print("testRevenuePercentIncrease_quarter: ");
			System.out.println("Revenue increase/decrease percent for " + tickerSymbol +" is: "+ percentIncrease +" and does not match the regex: " + regex);
		}
		assertTrue(percentIncrease.toString().matches(regex));
	}
	
	/** *************************** **/
	/** revenuePercentIncrease End: **/
	/** *************************** **/
	
	/** *************************** **/
	/** hasIncreasingRevenue Start: **/
	/** *************************** **/
	
	@Test
	public void testHasIncreasingRevenue_yearShouldNotBeNull() throws IOException, InterruptedException {
		boolean shouldNotBeNull = ndCalc.hasIncreasingRevenue(Period.YEAR);
		assertNotNull(shouldNotBeNull);
	}
	
	@Test
	public void testHasIncreasingRevenue_quarterShouldNotBeNull() throws IOException, InterruptedException {
		boolean shouldNotBeNull = ndCalc.hasIncreasingRevenue(Period.QUARTER);
		assertNotNull(shouldNotBeNull);
	}
	
	/** ******************************* **/
	/** hasIncreasingRevenue End: **/
	/** ******************************* **/
	
	/** ************************* **/
	/** epsPercentIncrease Start: **/
	/** ************************* **/
	@Test
	public void testEPSPercentIncrease_year() throws IOException, InterruptedException {
		Double percentIncrease = ndCalc.epsPercentIncrease(Period.YEAR);
		String regex = ".*\\d.*";
		if (!percentIncrease.toString().matches(regex)) {
			System.out.print("testEPSPercentIncrease_year: ");
			System.out.println("EPS increase/decrease percent for " + tickerSymbol +" is: "+ percentIncrease +" and does not match the regex: " + regex);
		}
		assertTrue(percentIncrease.toString().matches(regex));
	}
	
	@Test
	public void testEPSPercentIncrease_quarter() throws IOException, InterruptedException {
		Double percentIncrease = ndCalc.epsPercentIncrease(Period.QUARTER);
		String regex = ".*\\d.*";
		if (!percentIncrease.toString().matches(regex)) {
			System.out.print("testEPSPercentIncrease_quarter: ");
			System.out.println("EPS increase/decrease percent for " + tickerSymbol +" is: "+ percentIncrease +" and does not match the regex: " + regex);
		}
		assertTrue(percentIncrease.toString().matches(regex));
	}
	
	/** *********************** **/
	/** epsPercentIncrease End: **/
	/** *********************** **/
	
	/** *********************** **/
	/** hasIncreasingEPS Start: **/
	/** *********************** **/
	
	@Test
	public void testHasIncreasingEPS_yearShouldNotBeNull() throws IOException, InterruptedException {
		boolean shouldNotBeNull = ndCalc.hasIncreasingEPS(Period.YEAR);
		assertNotNull(shouldNotBeNull);
	}
	
	@Test
	public void testHasIncreasingEPS_quarterShouldNotBeNull() throws IOException, InterruptedException {
		boolean shouldNotBeNull = ndCalc.hasIncreasingEPS(Period.QUARTER);
		assertNotNull(shouldNotBeNull);
	}
	
	/** ********************* **/
	/** hasIncreasingEPS End: **/
	/** ********************* **/
	
	/** ************************* **/
	/** roePercentIncrease Start: **/
	/** ************************* **/
	@Test
	public void testROEPercentIncrease_year() throws IOException, InterruptedException {
		Double percentIncrease = ndCalc.roePercentIncrease(Period.YEAR);
		String regex = ".*\\d.*";
		if (!percentIncrease.toString().matches(regex)) {
			System.out.print("testROEPercentIncrease_year: ");
			System.out.println("ROE increase/decrease percent for " + tickerSymbol +" is: "+ percentIncrease +" and does not match the regex: " + regex);
		}
		assertTrue(percentIncrease.toString().matches(regex));
	}
	
	@Test
	public void testROEPercentIncrease_quarter() throws IOException, InterruptedException {
		Double percentIncrease = ndCalc.roePercentIncrease(Period.QUARTER);
		String regex = ".*\\d.*";
		if (!percentIncrease.toString().matches(regex)) {
			System.out.print("testROEPercentIncrease_quarter: ");
			System.out.println("ROE increase/decrease percent for " + tickerSymbol +" is: "+ percentIncrease +" and does not match the regex: " + regex);
		}
		assertTrue(percentIncrease.toString().matches(regex));
	}
	
	/** *********************** **/
	/** roePercentIncrease End: **/
	/** *********************** **/
	
	/** *********************** **/
	/** hasIncreasingROE Start: **/
	/** *********************** **/
	
	@Test
	public void testHasIncreasingROE_yearShouldNotBeNull() throws IOException, InterruptedException {
		boolean shouldNotBeNull = ndCalc.hasIncreasingROE(Period.YEAR);
		assertNotNull(shouldNotBeNull);
	}
	
	@Test
	public void testHasIncreasingROE_quarterShouldNotBeNull() throws IOException, InterruptedException {
		boolean shouldNotBeNull = ndCalc.hasIncreasingROE(Period.QUARTER);
		assertNotNull(shouldNotBeNull);
	}
	
	/** ********************* **/
	/** hasIncreasingROE End: **/
	/** ********************* **/
	
	/** ********************************** **/
	/** hasMoreInsiderBuysThanSells Start: **/
	/** ********************************** **/
	
	@Test
	public void testHasMoreInsiderBuysThanSells() throws IOException, InterruptedException {
		boolean shouldNotBeNull = ndCalc.hasMoreInsiderBuysThanSells();
		assertNotNull(shouldNotBeNull);
	}
	
	/** ******************************** **/
	/** hasMoreInsiderBuysThanSells End: **/
	/** ******************************** **/

}
