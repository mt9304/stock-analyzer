package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ND3ROE extends ND2EPS {

	//This class gets the Return On Equity percent value by getting the Net Income divided by the Total Shareholder's Equity. 
	public ND3ROE(String tickerSymbol) throws IOException, InterruptedException {
		super(tickerSymbol);
		balanceSheetDocument = Jsoup.connect(balanceSheetUrl).get();
		Thread.sleep(scrapeDelay);
		balanceSheetQuarterDocument = Jsoup.connect(balanceSheetQuarterUrl).get();
		Thread.sleep(scrapeDelay);
	}
	
	/** *********************** **/
	/** ND #3 ROE Section START **/
	/** *********************** **/
	
	/** Total Net Income Start. **/
	//The period can be Years or Quarters. Years would be in the format "2013", Quarters would be in the format "30-Sep-2016"
	public String getNetIncomePeriodHeader(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element yearNode; 
		try {
			yearNode = document.getElementsByClass("crDataTable").get(1).select("th[scope]").get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getNetIncomePeriodHeader(document, " + index + "), node not found. ");
			return null;
		}
		String netIncomeYear = yearNode.text();
		return netIncomeYear;
	}
	
	//Gets the EPS values by index, 0 would be oldest period (2013 for years) and 4 would be latest period (2017 for years) at the current year of 2018. The scraper content is an HTML table. 
	public String getNetIncomePeriodValue(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element netIncomeNode;
		try {
			netIncomeNode = document.getElementsByClass("crDataTable").get(1).select("tbody > tr.totalRow").get(0).select("td.valueCell").get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getNetIncomePeriodValue(document, " + index + "), node not found. ");
			return null;
		}
		String netIncomeValue = netIncomeNode.text().replaceAll("[)]", "").replaceAll("[(]", "-"); //Sometimes values will have brackets like "(0.08)". 
		return netIncomeValue;
	}
	/** Total Net Income End. **/
	
	/** Total Shareholder's Equity Start. **/
	//The period can be Years or Quarters. Years would be in the format "2013", Quarters would be in the format "30-Sep-2016"
	public String getShareHolderEquityPeriodHeader(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element yearNode = null;
		Boolean isYear = true;
		Boolean isQuarter = false;
		try { 
			yearNode = document.select("table.crDataTable:contains(5-year trend)").get(1).select("th[scope]").get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getShareHolderEquityPeriodHeader(annualDocument, " + index + "), node not found. Trying for Quarter Document. ");
			isYear = false;
			isQuarter = true;
		}
		
		if (!isYear) {
			try { 
				System.out.println(tickerSymbol + ": Finding Quarter Document. ");
				yearNode = document.select("table.crDataTable:contains(5-qtr trend)").get(1).select("th[scope]").get(index);
			} catch (IndexOutOfBoundsException e) {
				isQuarter = false;
				System.out.println(tickerSymbol + ": Could not getShareHolderEquityPeriodHeader(quarterDocument, " + index + "), node not found. ");
				return null;
			}
		}
		if (isQuarter) {
			System.out.println(tickerSymbol + ": Found Quarter Document. ");
		}
		String shareHolderEquityYear = yearNode.text();
		return shareHolderEquityYear;
	}
	
	//Gets the EPS values by index, 0 would be oldest period (2013 for years) and 4 would be latest period (2017 for years) at the current year of 2018. The scraper content is an HTML table. 
	public String getShareHolderEquityPeriodValue(Document document, int index) throws InterruptedException, IndexOutOfBoundsException {
		Element shareHolderEquityNode;
		try {
			shareHolderEquityNode = document.select("tbody > tr.partialSum:contains(Total Shareholders)").get(0).select("td.valueCell").get(index);
		} catch (IndexOutOfBoundsException e) {
			System.out.println(tickerSymbol + ": Could not getShareHolderEquityPeriodValue(document, " + index + "), node not found. ");
			return null;
		}
		String shareHolderEquityValue = shareHolderEquityNode.text().replaceAll("[)]", "").replaceAll("[(]", "-"); //Sometimes values will have brackets like "(0.08)". 
		return shareHolderEquityValue;
	}
	/** Total Shareholder's Equity End. **/
	
	//Returns a map of data like so: { "2015"="12.21", "2016"="22.24", "2017"="22.22" } with values representing percent. 
	public Map<String, String> getROEByYears() throws IOException, InterruptedException {
		Map<String, String> roeByYears = new LinkedHashMap<String, String>();

		for (int i = 0; i < 5; i++) {
			String netIncomeYearValue = getNetIncomePeriodHeader(incomeDocument, i);
			String shareHolderEquityYearValue = getShareHolderEquityPeriodHeader(balanceSheetDocument, i);
			
			String netIncomeValue = getNetIncomePeriodValue(incomeDocument, i);
			String shareHolderEquityValue = getShareHolderEquityPeriodValue(balanceSheetDocument, i);
			
			if (!netIncomeYearValue.equals(shareHolderEquityYearValue)) {
				System.out.println("["+tickerSymbol+"]: Cannot convert to Map, years for Net Income and Share Holder Equity are different. ");
				return null;
			}
			
			//If all fields are available, divide Net Income by Share Holder Equity to get Return On Equity ratio. Then multiple by 100 and use only 4 digits to get the percent. Output should be something like 23.45. 
			if (netIncomeValue.matches(".*\\d.*") && shareHolderEquityValue.matches(".*\\d.*") && !netIncomeYearValue.isEmpty()) {
				Double rawNetIncomeValue = (double)getParsedAlphaNumericMoney(netIncomeValue);
				Double rawShareHolderEquityValue = (double)getParsedAlphaNumericMoney(shareHolderEquityValue);
				String rawROEValue = Double.toString(rawNetIncomeValue/rawShareHolderEquityValue*100);
				String roeValue;
				
				//Convert to only use decimal of last digit. 
				if (rawROEValue.contains(".")) {
					int decimalIndex = rawROEValue.indexOf(".");
					roeValue = rawROEValue.substring(0,decimalIndex+3);
				} else {
					roeValue = rawROEValue.substring(0,5);
				}

				roeByYears.put(netIncomeYearValue, roeValue);
			}
		}
		return roeByYears;
	}
	
	//Do not use for calculating ROE, use latest COMPLETE year. Keeping function incase I need it as an indicator in the future. 
	public Map<String, String> getROEByQuarters() throws IOException, InterruptedException {
		Map<String, String> roeByQuarters = new LinkedHashMap<String, String>();
		
		for (int i = 0; i < 5; i++) {
			String netIncomeQuarterValue = getNetIncomePeriodHeader(incomeQuarterDocument, i);
			String shareHolderEquityQuarterValue = getShareHolderEquityPeriodHeader(balanceSheetQuarterDocument, i);
			
			String netIncomeValue = getNetIncomePeriodValue(incomeQuarterDocument, i);
			String shareHolderEquityValue = getShareHolderEquityPeriodValue(balanceSheetQuarterDocument, i);
			
			if (!netIncomeQuarterValue.equals(shareHolderEquityQuarterValue)) {
				System.out.println("["+tickerSymbol+"]: Cannot convert to Map, quarters for Net Income and Share Holder Equity are different. ");
				return null;
			}
			
			//Converts the quarter end date from "30-Sep-2016" to "2016-Q3"
			if (netIncomeValue.matches(".*\\d.*") && shareHolderEquityValue.matches(".*\\d.*") && !netIncomeQuarterValue.isEmpty()) {
				String month = netIncomeQuarterValue.split("-")[1];
				String year = netIncomeQuarterValue.split("-")[2];
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
								parsedQuarterValueBuilder.append(netIncomeQuarterValue);
								System.out.println("["+ tickerSymbol + "]: Cannot convert to Map, unexpected Quarter Month value: " + month);
								break;
				}
				String parsedQuarterValue = parsedQuarterValueBuilder.toString();
				
				//If all fields are available, divide Net Income by Share Holder Equity to get Return On Equity ratio. Then multiple by 100 and use only 4 digits to get the percent. Output should be something like 23.45. 
				if (netIncomeValue.matches(".*\\d.*") && shareHolderEquityValue.matches(".*\\d.*") && !netIncomeQuarterValue.isEmpty()) {
					Double rawNetIncomeValue = (double)getParsedAlphaNumericMoney(netIncomeValue);
					Double rawShareHolderEquityValue = (double)getParsedAlphaNumericMoney(shareHolderEquityValue);
					String rawROEValue = Double.toString(rawNetIncomeValue/rawShareHolderEquityValue*100);
					String roeValue;
					
					//Convert to only use decimal of last 2 digits. 
					if (rawROEValue.contains(".")) {
						int decimalIndex = rawROEValue.indexOf(".");
						roeValue = rawROEValue.substring(0,decimalIndex+3);
					} else {
						roeValue = rawROEValue.substring(0,5);
					}
					roeByQuarters.put(parsedQuarterValue, roeValue);
				}
			}
		}
		return roeByQuarters;
	}
	
	/** ********************* **/
	/** ND #3 ROE Section END **/
	/** ********************* **/
}