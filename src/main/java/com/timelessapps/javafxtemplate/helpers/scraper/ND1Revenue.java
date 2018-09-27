package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ND1Revenue extends NDCore
{
	public ND1Revenue(String tickerSymbol) throws IOException, InterruptedException
	{
		super(tickerSymbol);
		incomeDocument = Jsoup.connect(incomeUrl).get();
		Thread.sleep(scrapeDelay);
		incomeQuarterDocument = Jsoup.connect(incomeQuarterUrl).get();
		Thread.sleep(scrapeDelay);
	}

	/** *************************** **/
	/** ND #1 Revenue Section START **/
	/** *************************** **/
	
	//The period can be Years or Quarters. Years would be in the format "2013", Quarters would be in the format "30-Sep-2016"
	public String getRevenuePeriodHeader(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element yearNode = null;
		Boolean isYear = true;
		Boolean isQuarter = false;
		try { 
			yearNode = document.select("table.crDataTable:contains(5-year trend)").get(0).select("th[scope]").get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getRevenuePeriodHeader(document, " + index + "), node not found. Trying for Quarter Document. ");
			isYear = false;
			isQuarter = true;
		}
		
		if (!isYear) {
			try { 
				System.out.println(tickerSymbol + ": Finding Quarter Document. ");
				yearNode = document.select("table.crDataTable:contains(5-qtr trend)").get(0).select("th[scope]").get(index);
			} catch (IndexOutOfBoundsException e) {
				isQuarter = false;
				System.out.println(tickerSymbol + ": Could not getRevenuePeriodHeader(document, " + index + "), node not found. ");
				return null;
			}
		}
		if (isQuarter) {
			System.out.println(tickerSymbol + ": Found Quarter Document. ");
		}
		String revenueYear = yearNode.text();
		return revenueYear;
	}
	
	//Gets the revenue values by index, 0 would be oldest period (2013 for years) and 4 would be latest period (2017 for years) at the current year of 2018. The scraper content is an HTML table. 
	public String getRevenuePeriodValue(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element revenueNode;
		
		try {
			revenueNode = document.select("tbody > tr.partialSum:contains(Sales/Revenue)").get(0).select("td.valueCell").get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getRevenuePeriodValue(document, " + index + "), node not found. ");
			return null;
		}
		String revenueValue = revenueNode.text().replaceAll("[)]", "").replaceAll("[(]", "-");
		return revenueValue;
	}
	
	//Returns a map of data like so: { "2015"="1.2b", "2016"="2.4b", "2017"="2.2b" }.
	public Map<String, String> getRevenueByYears() throws IOException, InterruptedException {
		Map<String, String> revenueByYears = new LinkedHashMap<String, String>();

		for (int i = 0; i < 5; i++) {
			String yearValue = getRevenuePeriodHeader(incomeDocument, i);
			String revenueValue = getRevenuePeriodValue(incomeDocument, i);
			if (!yearValue.isEmpty() && !revenueValue.isEmpty()) {
				revenueByYears.put(yearValue, revenueValue);
			}
		}
		return revenueByYears;
	}
	
	public Map<String, String> getRevenueByQuarters() throws IOException, InterruptedException {
		Map<String, String> revenueByQuarters = new LinkedHashMap<String, String>();
		
		for (int i = 0; i < 5; i++) {
			String quarterValue = getRevenuePeriodHeader(incomeQuarterDocument, i);
			String revenueValue = getRevenuePeriodValue(incomeQuarterDocument, i);
			
			//Converts the quarter end date from "30-Sep-2016" to "2016-Q3"
			if (!quarterValue.isEmpty()) {
				String month = quarterValue.split("-")[1];
				String year = quarterValue.split("-")[2];
				StringBuilder parsedQuarterValueBuilder = new StringBuilder();
				parsedQuarterValueBuilder.append(year+"-Q");
				
				switch (month) {
					case "Jan": parsedQuarterValueBuilder.append("1");
								break;
					case "Feb": parsedQuarterValueBuilder.append("1");
								break;
					case "Mar": parsedQuarterValueBuilder.append("1");
								break;
					case "Apr": parsedQuarterValueBuilder.append("2");
								break;
					case "May": parsedQuarterValueBuilder.append("2");
								break;
					case "Jun": parsedQuarterValueBuilder.append("2");
								break;
					case "Jul": parsedQuarterValueBuilder.append("3");
								break;
					case "Aug": parsedQuarterValueBuilder.append("3");
								break;
					case "Sep": parsedQuarterValueBuilder.append("3");
								break;
					case "Oct": parsedQuarterValueBuilder.append("4");
								break;
					case "Nov": parsedQuarterValueBuilder.append("4");
								break;
					case "Dec": parsedQuarterValueBuilder.append("4");
								break;
					default: 	parsedQuarterValueBuilder.setLength(0);
								parsedQuarterValueBuilder.append(quarterValue);
								//Log unexpected month. 
								break;
				}
				String parsedQuarterValue = parsedQuarterValueBuilder.toString();
				revenueByQuarters.put(parsedQuarterValue, revenueValue);
			}
		}
		return revenueByQuarters;
	}
	
	/** ************************* **/
	/** ND #1 Revenue Section END **/
	/** ************************* **/
}
