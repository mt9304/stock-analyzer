package main.java.com.timelessapps.javafxtemplate.helpers.scraper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.nodes.Element;

import main.java.com.timelessapps.javafxtemplate.helpers.abstractsandenums.LogType;

public class ND9Industry extends ND4AnalystRecommendation
{

	public ND9Industry(String tickerSymbol) throws IOException, InterruptedException
	{
		super(tickerSymbol);
	}
	
	/** **************************** **/
	/** ND #9 Industry Section START **/
	/** **************************** 
	 * @throws FileNotFoundException **/
	
	//These functions only take care of getting the industry of the stock. Calculations for average industry values will be done after stock is stored in database. 
	public String getIndustry() throws InterruptedException, InterruptedException {
		Element industryNode;
		try {
			industryNode = profileDocument.select("div.twowide.addgutter").get(0).select("div.section").get(1).select("p").get(1);
		} catch (IndexOutOfBoundsException e) {
			try
			{
				log.appendToEventLogsFile("(" + tickerSymbol + ") Could not getIndustry(), node not found. (" + e + ")", LogType.TRACE);
			} catch (FileNotFoundException e1)
			{
				
			}
			return null;
		}
		String industry = industryNode.text();
		return industry;
	}
	
	public String getSector() throws InterruptedException, InterruptedException {
		Element sectorNode;
		try {
			sectorNode = profileDocument.select("div.twowide.addgutter").get(0).select("div.section").get(2).select("p").get(1);
		} catch (IndexOutOfBoundsException e) {
			try
			{
				log.appendToEventLogsFile("(" + tickerSymbol + ") Could not getSector(), node not found. (" + e + ")", LogType.TRACE);
			} catch (FileNotFoundException e1)
			{
				
			}
			return null;
		}
		String sector = sectorNode.text();
		return sector;
	}
	
	/** ************************** **/
	/** ND #9 Industry Section END **/
	/** ************************** **/
}
