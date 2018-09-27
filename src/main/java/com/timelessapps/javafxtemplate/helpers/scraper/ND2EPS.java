package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ND2EPS extends ND1Revenue {

	public ND2EPS(String tickerSymbol) throws IOException, InterruptedException {
		super(tickerSymbol);
		
	}
	
	/** *********************** **/
	/** ND #2 EPS Section START **/
	/** *********************** **/
	
	//The period can be Years or Quarters. Years would be in the format "2013", Quarters would be in the format "30-Sep-2016"
	public String getEPSPeriodHeader(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element yearNode = null;
		Boolean isYear = true;
		Boolean isQuarter = false;
		try { 
			yearNode = document.select("table.crDataTable:contains(5-year trend)").get(1).select("th[scope]").get(index);
		} catch (IndexOutOfBoundsException e) {
			//System.out.println(tickerSymbol + ": Could not getEPSPeriodHeader(document, " + index + "), node not found. Trying for Quarter Document. ");
			isYear = false;
			isQuarter = true;
		}
		
		if (!isYear) {
			try { 
				//System.out.println(tickerSymbol + ": Finding Quarter Document. ");
				yearNode = document.select("table.crDataTable:contains(5-qtr trend)").get(1).select("th[scope]").get(index);
			} catch (IndexOutOfBoundsException e) {
				isQuarter = false;
				System.out.println(tickerSymbol + ": Could not getEPSPeriodHeader(document, " + index + "), both year and quarter nodes not found. ");
				return null;
			}
		}
		if (isQuarter) {
			//System.out.println(tickerSymbol + ": Found Quarter Document. ");
		}
		String epsYear = yearNode.text();
		return epsYear;
	}
	
	//Gets the EPS values by index, 0 would be oldest period (2013 for years) and 4 would be latest period (2017 for years) at the current year of 2018. The scraper content is an HTML table. 
	public String getEPSPeriodValue(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element epsNode;
		try {
			epsNode = document.getElementsByClass("crDataTable").get(1).select("tbody > tr.mainRow:contains(EPS (Basic))").get(0).select("td.valueCell").get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getEPSPeriodValue(document, " + index + "), node not found. ");
			return null;
		}
		String epsValue = epsNode.text().replaceAll("[)]", "").replaceAll("[(]", "-");//Sometimes values will have brackets like "(0.08)", indicating negative numbers. 
		return epsValue;
	}
	
	//Returns a map of data like so: { "2015"="1.2b", "2016"="2.4b", "2017"="2.2b" }.
	public Map<String, String> getEPSByYears() throws IOException, InterruptedException {
		Map<String, String> epsByYears = new LinkedHashMap<String, String>();

		for (int i = 0; i < 5; i++) {
			String yearValue = getEPSPeriodHeader(incomeDocument, i);
			String epsValue = getEPSPeriodValue(incomeDocument, i);
			if (!yearValue.isEmpty()) {
				epsByYears.put(yearValue, epsValue);
			}
		}
		return epsByYears;
	}
	
	public Map<String, String> getEPSByQuarters() throws IOException, InterruptedException {
		Map<String, String> epsByQuarters = new LinkedHashMap<String, String>();
		
		for (int i = 0; i < 5; i++) {
			String quarterValue = getEPSPeriodHeader(incomeQuarterDocument, i);
			String epsValue = getEPSPeriodValue(incomeQuarterDocument, i);
			
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
							System.out.println("["+ tickerSymbol + "]: Cannot convert to Map, unexpected Quarter Month value: " + month);
							break;
				}
				String parsedQuarterValue = parsedQuarterValueBuilder.toString();
				epsByQuarters.put(parsedQuarterValue, epsValue);
			}
		}
		return epsByQuarters;
	}
	
	/** ********************* **/
	/** ND #2 EPS Section END **/
	/** ********************* **/

}
