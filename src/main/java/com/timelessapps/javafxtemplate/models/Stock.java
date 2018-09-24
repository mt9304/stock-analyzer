package main.java.com.timelessapps.javafxtemplate.models;

public class Stock {
	
	String ticker;
	Double price;
	Double pe;
	String volume;
	Boolean positiveLatestIncome;
	
	Boolean hasIncreasingAnnualRevenue;
	Double annualRevenueIncreasePrecent;
	Boolean hasIncreasingAnnualEPS;
	Double annualEPSIncreasePrecent;
	Boolean hasIncreasingAnnualROE;
	Double annualROEIncreasePrecent;
	
	Boolean analystsRecommend;
	Boolean hasMoreInsiderBuys;
	String industry;
	String sector;
	
	Boolean hasIncreasingQuarterlyRevenue;
	Double quarterlyRevenueIncreasePrecent;
	Boolean hasIncreasingQuarterlyEPS;
	Double quarterlyEPSIncreasePrecent;
	Boolean hasIncreasingQuarterlyROE;
	Double quarterlyROEIncreasePrecent;
	
	private Stock(Builder builder) {
		
	}
	
	public static class Builder {
		String ticker;
		Double price;
		Double pe;
		String volume;
		Boolean positiveLatestIncome;
		
		Boolean hasIncreasingAnnualRevenue;
		Double annualRevenueIncreasePrecent;
		Boolean hasIncreasingAnnualEPS;
		Double annualEPSIncreasePrecent;
		Boolean hasIncreasingAnnualROE;
		Double annualROEIncreasePrecent;
		
		Boolean analystsRecommend;
		Boolean hasMoreInsiderBuys;
		String industry;
		String sector;
		
		Boolean hasIncreasingQuarterlyRevenue;
		Double quarterlyRevenueIncreasePrecent;
		Boolean hasIncreasingQuarterlyEPS;
		Double quarterlyEPSIncreasePrecent;
		Boolean hasIncreasingQuarterlyROE;
		Double quarterlyROEIncreasePrecent;
		
		public Builder(String ticker) {
			this.ticker = ticker;
		}
		
		public Builder price(Double price) {
			this.price = price;
			return this;
		}
		
		public Builder volume(String volume) {
			this.volume = volume;
			return this;
		}
		
		//Continue the pattern here to complete. then instantiate with Stock stock = new Stock.Builder("MSFT").price(102.13).volume("123342")....build();
		
		public Stock build() {
			return new Stock(this);
		}
		
	}
}
