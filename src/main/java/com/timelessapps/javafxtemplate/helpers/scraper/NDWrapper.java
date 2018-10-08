package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.LogType;
import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.Period;
import main.java.com.timelessapps.javafxtemplate.helpers.services.LoggingService;

public class NDWrapper {
				String tickerSymbol;
				ND11Insider webScraper;
				LoggingService log = new LoggingService();
				
				public NDWrapper(String tickerSymbol) throws IOException, InterruptedException{
					this.tickerSymbol = tickerSymbol;
					webScraper = new ND11Insider(tickerSymbol);
				}
				
				/** *************** **/
				/** Start: 0 Basics **/
				/** *************** **/
				
				public String getCurrentPrice() throws IOException {
					return webScraper.getCurrentPrice();
				}
				
				public String getPERatio() throws InterruptedException, IndexOutOfBoundsException {
					return webScraper.getPERatio();
				}
				
				public String getVolume() throws InterruptedException, IndexOutOfBoundsException {
					return webScraper.getVolume();
				}
				
				public Boolean hasPositiveLatestIncome() {
					String latestIncomeValue = webScraper.getLatestIncomeValue();
					
					if (latestIncomeValue == null || latestIncomeValue.equals("")) {
						return null;
					}
					
					if (latestIncomeValue.matches("\\d.*")) {
						return true;
					} else if (latestIncomeValue.matches("^[-].*")) {
						return false; 
					}
					return null;
				}
				
				/** ************ **/
				/** END: 0 Basic **/
				/** ************ **/
				
				/** **************** **/
				/** Start: 1 Revenue **/
				/** **************** **/
				
				//Uses function that divides latest period by second latest period to get the percent increase/decrease. 
				public Double revenuePercentIncrease(Period period) throws IOException, InterruptedException {
					Map<String, String> revenueByPeriod = null;
					Double percentIncrease = null;
					
					switch (period) { 
						case YEAR: 		revenueByPeriod = webScraper.getRevenueByYears();	
										break;
						case QUARTER: 	revenueByPeriod = webScraper.getRevenueByQuarters();
										break;
						default: 		try {
											log.appendToEventLogsFile("(" + tickerSymbol + ") Invalid period entered: " + period, LogType.ERROR);
										} catch (FileNotFoundException e) { }
										break;
					}

					Set<String> keys;

					try {
						keys = revenueByPeriod.keySet();
					} catch (NullPointerException e) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") No revenuePercentIncrease available. (" + e + ")", LogType.TRACE);
						return null;
					}
					
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") Not enough " + period + " data. ", LogType.TRACE);
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodRevenue = revenueByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodRevenue = revenueByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						
						try {
							//Sometimes certain years/quarters will have no information and just return - or blank. 
							if (unParsedLatestPeriodRevenue.equals("-") || unParsedLatestPeriodRevenue.equals("") || unParsedSecondLatestPeriodRevenue.equals("-") || unParsedSecondLatestPeriodRevenue.equals("")) {
								return null;
							}
						} catch (NullPointerException e) {
							log.appendToEventLogsFile("(" + tickerSymbol + ") Missing revenue information for " + period + ". (" + e + ")", LogType.TRACE);
							return null;
						}
						
						//Need to parse the alphanumeric values like 999M and 1.01B
						Long latestPeriodRevenue = webScraper.getParsedAlphaNumericMoney(unParsedLatestPeriodRevenue);	
						Long secondLatestPeriodRevenue = webScraper.getParsedAlphaNumericMoney(unParsedSecondLatestPeriodRevenue);
						percentIncrease = webScraper.convertDifferenceToPercent(latestPeriodRevenue, secondLatestPeriodRevenue);
					}
					return percentIncrease;
				}
				
				//For YEAR, compares only the latest complete fiscal year and the year before that. 
				//For QUARTER, compares only latest complete quarter and the quarter before that. 
				public Boolean hasIncreasingRevenue(Period period) throws IOException, InterruptedException {
					Map<String, String> revenueByPeriod = null;
					
					switch (period) { 
						case YEAR: 		revenueByPeriod = webScraper.getRevenueByYears();
										break;
						case QUARTER: 	revenueByPeriod = webScraper.getRevenueByQuarters();
										break;
						default: 		try {
											log.appendToEventLogsFile("(" + tickerSymbol + ") Invalid period entered: " + period, LogType.ERROR);
										} catch (FileNotFoundException e) { }
										break;
					}

					Set<String> keys;
					
					try {
						keys = revenueByPeriod.keySet();
					} catch (NullPointerException e) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") No nodes available for hasIncreasingRevenue(). (" + e + ")", LogType.TRACE);
						return null;
					}
					
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") Not enough " + period + " data. ", LogType.TRACE);
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodRevenue = revenueByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodRevenue = revenueByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						
						try {
							//Sometimes certain years/quarters will have no information and just return - or blank. 
							if (unParsedLatestPeriodRevenue.equals("-") || unParsedLatestPeriodRevenue.equals("") || unParsedSecondLatestPeriodRevenue.equals("-") || unParsedSecondLatestPeriodRevenue.equals("")) {
								return null;
							}
						} catch (NullPointerException e) {
								log.appendToEventLogsFile("(" + tickerSymbol + ") Missing revenue information for " + period + ". (" + e + ")", LogType.TRACE);
							return null;
						}
						
						Long latestPeriodRevenue = webScraper.getParsedAlphaNumericMoney(unParsedLatestPeriodRevenue);
						Long secondLatestPeriodRevenue = webScraper.getParsedAlphaNumericMoney(unParsedSecondLatestPeriodRevenue);
						
						//Sometime companies have incomplete paperwork. 
						if (latestPeriodRevenue == null | secondLatestPeriodRevenue == null) {
							return null;
						}
						
						if (latestPeriodRevenue > secondLatestPeriodRevenue) {
							return true;
						} else if (latestPeriodRevenue < secondLatestPeriodRevenue) {
							return false;
						}
					}
					return null;
				}
				
				/** ************** **/
				/** End: 1 Revenue **/
				/** ************** **/
				
				/** ************ **/
				/** Start: 2 EPS **/
				/** ************ **/
				
				//Uses function that divides latest period by second latest period to get the percent increase/decrease. 
				public Double epsPercentIncrease(Period period) throws IOException, InterruptedException {
					Map<String, String> epsByPeriod = null;
					Double percentIncrease = null;
					
					switch (period) { 
						case YEAR: 		epsByPeriod = webScraper.getEPSByYears();	
										break;
						case QUARTER: 	epsByPeriod = webScraper.getEPSByQuarters();
										break;
						default: 		try {
											log.appendToEventLogsFile("(" + tickerSymbol + ") Invalid period entered: " + period, LogType.ERROR);
										} catch (FileNotFoundException e) { }
										break;
					}

					Set<String> keys;
					
					try {
						keys = epsByPeriod.keySet();
					} catch (NullPointerException e) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") No epsPercentIncrease available. (" + e + ")", LogType.TRACE);
						return null;
					}
					
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") Not enough " + period + " data. ", LogType.TRACE);
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodEPS = epsByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodEPS = epsByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						
						try {
							//Sometimes certain years/quarters will have no information and just return - or blank. 
							if (unParsedLatestPeriodEPS.equals("-") || unParsedLatestPeriodEPS.equals("") || unParsedSecondLatestPeriodEPS.equals("-") || unParsedSecondLatestPeriodEPS.equals("")) {
								return null;
							}
						} catch (NullPointerException e) {
							log.appendToEventLogsFile("(" + tickerSymbol + ") Missing revenue information for " + period + ". (" + e + ")", LogType.TRACE);
							return null;
						}
						
						//Need to parse the alphanumeric values like 999M and 1.01B
						Double latestPeriodEPS = Double.parseDouble(unParsedLatestPeriodEPS);	
						Double secondLatestPeriodEPS = Double.parseDouble(unParsedSecondLatestPeriodEPS);
						percentIncrease = webScraper.convertDifferenceToPercent(latestPeriodEPS, secondLatestPeriodEPS);
					}
					return percentIncrease;
				}
				
				//For YEAR, compares only the latest complete fiscal year and the year before that. 
				//For QUARTER, compares only latest complete quarter and the quarter before that. 
				public Boolean hasIncreasingEPS(Period period) throws IOException, InterruptedException {
					Map<String, String> epsByPeriod = null;
					
					switch (period) { 
						case YEAR: 		epsByPeriod = webScraper.getEPSByYears();
										break;
						case QUARTER: 	epsByPeriod = webScraper.getEPSByQuarters();
										break;
						default: 		try {
											log.appendToEventLogsFile("(" + tickerSymbol + ") Invalid period entered: " + period, LogType.ERROR);
										} catch (FileNotFoundException e) { }
										break;
					}

					Set<String> keys;
					
					try {
						keys = epsByPeriod.keySet();
					} catch (NullPointerException e) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") No epsByPeriod available. (" + e + ")", LogType.TRACE);
						return null;
					}
					
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") Not enough " + period + " data. ", LogType.TRACE);
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodEPS = epsByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodEPS = epsByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						
						try {
							//Sometimes certain years/quarters will have no information and just return - or blank. 
							if (unParsedLatestPeriodEPS.equals("-") || unParsedLatestPeriodEPS.equals("") || unParsedSecondLatestPeriodEPS.equals("-") || unParsedSecondLatestPeriodEPS.equals("")) {
								return null;
							}
						} catch (NullPointerException e) {
							log.appendToEventLogsFile("(" + tickerSymbol + ") Missing revenue information for " + period + ". (" + e + ")", LogType.TRACE);
							return null;
						}
						
						Double latestPeriodEPS = Double.parseDouble(unParsedLatestPeriodEPS);
						Double secondLatestPeriodEPS = Double.parseDouble(unParsedSecondLatestPeriodEPS);
						
						if (latestPeriodEPS > secondLatestPeriodEPS) {
							return true;
						} else if (latestPeriodEPS < secondLatestPeriodEPS) {
							return false;
						}
					}
					return null;
				}
				
				/** ********** **/
				/** End: 2 EPS **/
				/** ********** **/
				
				/** ************ **/
				/** Start: 3 ROE **/
				/** ************ **/
				
				//Uses function that divides latest period by second latest period to get the percent increase/decrease. 
				public Double roePercentIncrease(Period period) throws IOException, InterruptedException {
					Map<String, String> roeByPeriod = null;
					Double percentIncrease = null;
					
					switch (period) { 
						case YEAR: 		roeByPeriod = webScraper.getROEByYears();	
										break;
						case QUARTER: 	roeByPeriod = webScraper.getROEByQuarters();
										break;
						default: 		try {
											log.appendToEventLogsFile("(" + tickerSymbol + ") Invalid period entered: " + period, LogType.ERROR);
										} catch (FileNotFoundException e) { }
										break;
					}

					Set<String> keys;
					
					try {
						keys = roeByPeriod.keySet();
					} catch (NullPointerException e) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") No roePercentIncrease available. (" + e + ")", LogType.TRACE);
						return null;
					}
					
					ArrayList<String> allPeriods = new ArrayList<>();
					
					if (keys.size() < 2) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") Not enough " + period + " data. ", LogType.TRACE);
						return null;
					} else {
						for (String k : keys) {
							allPeriods.add(k);
						}
						
						String unParsedLatestPeriodROE = roeByPeriod.get(allPeriods.get(allPeriods.size() - 1));
						String unParsedSecondLatestPeriodROE = roeByPeriod.get(allPeriods.get(allPeriods.size() - 2));
						
						try {
							//Sometimes certain years/quarters will have no information and just return - or blank. 
							if (unParsedLatestPeriodROE.equals("-") || unParsedLatestPeriodROE.equals("") || unParsedSecondLatestPeriodROE.equals("-") || unParsedSecondLatestPeriodROE.equals("")) {
								return null;
							}
						} catch (NullPointerException e) {
							log.appendToEventLogsFile("(" + tickerSymbol + ") Missing revenue information for " + period + ". (" + e + ")", LogType.TRACE);
							return null;
						}
						
						//Need to parse the alphanumeric values like 999M and 1.01B
						Double latestPeriodROE = Double.parseDouble(unParsedLatestPeriodROE);	
						Double secondLatestPeriodROE = Double.parseDouble(unParsedSecondLatestPeriodROE);
						percentIncrease = webScraper.convertDifferenceToPercent(latestPeriodROE, secondLatestPeriodROE);
					}
					return percentIncrease;
				}
				
				public Boolean hasIncreasingROE(Period period) throws IOException, InterruptedException {
					Double percentIncrease = roePercentIncrease(period);
					
					try {
						if (percentIncrease > 0) {
							return true;
						} else if (percentIncrease <= 0) {
							return false;
						}
					} catch (NullPointerException e) {
						log.appendToEventLogsFile("(" + tickerSymbol + ") Could not find hasIncreasingROE. (" + e + ")", LogType.TRACE);
					}
					return null;
				}
				
				/** ********** **/
				/** End: 3 ROE **/
				/** ********** **/
				
				/** ******************************* **/
				/** Start: 4 Analyst Recommendation **/
				/** ******************************* **/
				
				public String getAnalystRecommendation() throws IndexOutOfBoundsException {
					return webScraper.getAnalystRecommendation();
				}
				
				public Boolean analystRecommendationIsPositive() throws IndexOutOfBoundsException {
					String analystRecommendation = getAnalystRecommendation();
					
					if (analystRecommendation == null) {
						return null;
					}
					
					if (analystRecommendation.equals("BUY") || analystRecommendation.equals("OVER") || analystRecommendation.equals("OVERWEIGHT")) {
						return true;
					} else if (analystRecommendation.equals("SELL") || analystRecommendation.equals("UNDER") || analystRecommendation.equals("UNDERWEIGHT") || analystRecommendation.equals("HOLD")) {
						return false;
					}
					return null;
				}
				
				/** ***************************** **/
				/** End: 4 Analyst Recommendation **/
				/** ***************************** **/
				
				/** ***************** **/
				/** Start: 9 Industry **/
				/** ***************** **/
				
				public String getIndustry() throws InterruptedException {
					return webScraper.getIndustry();
				}
				
				public String getSector() throws InterruptedException {
					return webScraper.getSector();
				}
				
				/** *************** **/
				/** End: 9 Industry **/
				/** *************** **/
				
				/** ************************** **/
				/** Start: 11 Insider Activity **/
				/** ************************** **/
				
				public Boolean hasMoreInsiderBuysThanSells() throws IndexOutOfBoundsException {
					Integer sharesPurchased = webScraper.getSharesPurchasedInLastThreeMonths();
					Integer sharesSold = webScraper.getSharesSoldInLastThreeMonths();
					
					if (sharesPurchased != null && sharesSold != null) {
						if (sharesPurchased > sharesSold) {
							return true;
						} else if (sharesSold > sharesPurchased) {
							return false; 
						}
					}
					return null;
				}
				
				/** ************************ **/
				/** End: 11 Insider Activity **/
				/** ************************ **/
}
