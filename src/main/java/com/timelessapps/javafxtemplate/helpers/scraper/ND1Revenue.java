package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.LogType;

public class ND1Revenue extends NDCore
{
	public ND1Revenue(String tickerSymbol) throws IOException, InterruptedException
	{
		super(tickerSymbol);
	}

	/** *************************** **/
	/** ND #1 Revenue Section START **/
	/** *************************** **/
	
	//The period can be Years or Quarters. Years would be in the format "2013", Quarters would be in the format "30-Sep-2016"
	public String getRevenuePeriodHeader(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element yearNode = null;
		Boolean isYear = true;
		try { 
			yearNode = document.select("table.crDataTable:contains(5-year trend)").get(0).select("th[scope]").get(index);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			isYear = false;
		}
		
		if (!isYear) {
			try { 
				yearNode = document.select("table.crDataTable:contains(5-qtr trend)").get(0).select("th[scope]").get(index);
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				try
				{
					log.appendToEventLogsFile("(" + tickerSymbol + ") Could not getRevenuePeriodHeader(document, " + index + "), both year and quarter nodes not found. (" + e + ")", LogType.TRACE);
				} catch (FileNotFoundException e1)
				{
					 
				}
				return null;
			}
		}

		String revenueYear = yearNode.text();
		return revenueYear;
	}
	
	//Gets the revenue values by index, 0 would be oldest period (2013 for years) and 4 would be latest period (2017 for years) at the current year of 2018. The scraper content is an HTML table. 
	public String getRevenuePeriodValue(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element revenueNode;
		
		try {
			revenueNode = document.select("tbody > tr.partialSum:contains(Sales/Revenue)").get(0).select("td.valueCell").get(index);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			try
			{
				log.appendToEventLogsFile("(" + tickerSymbol + ") Could not getRevenuePeriodValue(document, " + index + "), node not found. (" + e + ")", LogType.TRACE);
			} catch (FileNotFoundException e1)
			{
				 
			}
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
			if (yearValue == null || revenueValue == null) {
				//Company does not have revenue available. 
				revenueByYears.put("null", "null");
				return revenueByYears;
			}
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
			
			//Should only return null if there is no statement available, would be blank or - if there were empty values, so it is okay to terminate here and return null.  
			if (quarterValue == null) {
				return null;
			}
			
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
								log.appendToEventLogsFile("(" + tickerSymbol + ") Cannot convert to Map, unexpected Quarter Month value: " + month, LogType.WARN);
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
