package bybit.market;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.MarketInterval;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import bybit.util.BybitConnection;
import lombok.var;
import util.JsonConverter;
import util.RSICalculator;

public class BybitMarket {
	private static final String[] symbols = 
		{"BTCUSDT", "ETHUSDT", "XRPUSDT", "SOLUSDT",
		"ADAUSDT", "MNTUSDT", "APEXUSDT", "DOGEUSDT",
		"SUIUSDT", "TRUMPUSDT", "LTCUSDT", "HBARUSDT"};
	private BybitApiMarketRestClient client;
	public BybitMarket() {
		BybitConnection conn = BybitConnection.getConnection();
		client = conn.makeBybitMarketClient();
	}
	
	public double[] getHistoricalPrice(String symbol, CategoryType cat, MarketInterval interval){
		var request = MarketDataRequest.builder()
				.category(cat)
				.symbol(symbol)
				.marketInterval(interval)
				.build();
		var response = client.getMarketLinesData(request);
		JsonNode responseJson= null;
		try {
			responseJson = JsonConverter.convertBybitResponseObjectToJsonNode(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		JsonNode candles = responseJson.get("result").get("list");
		double[] historicalPrice = new double[candles.size()-1];
		for (int i = 0; i < candles.size()-1; i++) {
			double candlePrice = candles.get(i+1).get(4).asDouble();
			historicalPrice[i] = candlePrice;
		}
		return historicalPrice;
		
	}
	public double getSecurityLastPrice(CategoryType cat, String symbol) {
		var request = MarketDataRequest.builder()
				.category(cat)
				.symbol(symbol)
				.build();
		var response = client.getMarketTickers(request);
		JsonNode responseJson= null;
		try {
			responseJson = JsonConverter.convertBybitResponseObjectToJsonNode(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		double lastPrice = responseJson.get("result").get("list").get(0).get("lastPrice").asDouble();
		return lastPrice;
	}
	public double getSecurityRSI(String symbol, CategoryType cat, MarketInterval interval, int period) {
		double[] prices = this.getHistoricalPrice(symbol, cat, interval);
		return RSICalculator.calculateRSI(prices, period);
	}
	public static void main(String[] args) {
		BybitMarket bybitMarket = new BybitMarket();
		
//		for (String symbol : symbols) {
//			System.out.println(symbol + ":");
//			double RSI = bybitMarket.getSecurityRSI(symbol, CategoryType.SPOT, MarketInterval.FIFTEEN_MINUTES, 14);
//			System.out.println(RSI);
//		}
		
		System.out.println(bybitMarket.getSecurityLastPrice(CategoryType.LINEAR, "BTCUSDT"));
	}
}
