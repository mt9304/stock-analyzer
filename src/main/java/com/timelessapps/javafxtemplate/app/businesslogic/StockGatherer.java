package main.java.com.timelessapps.javafxtemplate.app.businesslogic;

import java.io.IOException;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.Period;
import main.java.com.timelessapps.javafxtemplate.helpers.database.StockDAO;
import main.java.com.timelessapps.javafxtemplate.helpers.scraper.NDWrapper;

public class StockGatherer {
	
	String ticker;
	StockDAO db;
	
	public StockGatherer(String ticker, StockDAO db) {
		this.ticker = ticker;
		this.db = db;
	}
	
	public void gather() throws NumberFormatException, IOException, InterruptedException {
		
		System.out.println("Gathering information for " + ticker);
		NDWrapper stockCalc = new NDWrapper(ticker);
		
		Double price = Double.parseDouble(stockCalc.getCurrentPrice());
		
		//Should consider making the function return a Double instead of string. Will need to change database structure as well. 
		Double pe;
		if (stockCalc.getPERatio().equals(null) || stockCalc.getPERatio().equals("")) {
			pe = null;
		} else {
			pe = Double.parseDouble(stockCalc.getPERatio());
		}
		
		String volume = stockCalc.getVolume();
		Boolean positiveLatestIncome = stockCalc.hasPositiveLatestIncome();
		
		Boolean hasIncreasingAnnualRevenue = stockCalc.hasIncreasingRevenue(Period.YEAR);
		Double annualRevenueIncreasePrecent = stockCalc.revenuePercentIncrease(Period.YEAR);
		Boolean hasIncreasingAnnualEPS = stockCalc.hasIncreasingEPS(Period.YEAR);
		Double annualEPSIncreasePrecent = stockCalc.epsPercentIncrease(Period.YEAR);
		Boolean hasIncreasingAnnualROE = stockCalc.hasIncreasingROE(Period.YEAR);
		Double annualROEIncreasePrecent = stockCalc.roePercentIncrease(Period.YEAR);
		
		Boolean analystsRecommend = stockCalc.analystRecommendationIsPositive();
		Boolean hasMoreInsiderBuys = stockCalc.hasMoreInsiderBuysThanSells();
		String industry = stockCalc.getIndustry();
		String sector = stockCalc.getSector();
		
		Boolean hasIncreasingQuarterlyRevenue = stockCalc.hasIncreasingRevenue(Period.QUARTER);
		Double quarterlyRevenueIncreasePrecent = stockCalc.revenuePercentIncrease(Period.QUARTER);
		Boolean hasIncreasingQuarterlyEPS = stockCalc.hasIncreasingEPS(Period.QUARTER);
		Double quarterlyEPSIncreasePrecent = stockCalc.epsPercentIncrease(Period.QUARTER);
		Boolean hasIncreasingQuarterlyROE = stockCalc.hasIncreasingROE(Period.QUARTER);
		Double quarterlyROEIncreasePrecent = stockCalc.roePercentIncrease(Period.QUARTER);

		db.insert(ticker, price, pe, volume, positiveLatestIncome, hasIncreasingAnnualRevenue, annualRevenueIncreasePrecent, hasIncreasingAnnualEPS, annualEPSIncreasePrecent, hasIncreasingAnnualROE, annualROEIncreasePrecent, analystsRecommend, hasMoreInsiderBuys, industry, sector, hasIncreasingQuarterlyRevenue, quarterlyRevenueIncreasePrecent, hasIncreasingQuarterlyEPS, quarterlyEPSIncreasePrecent, hasIncreasingQuarterlyROE, quarterlyROEIncreasePrecent);
		
	}

}
