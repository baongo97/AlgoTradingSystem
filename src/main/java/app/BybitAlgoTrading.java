package app;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.MarketInterval;

import bybit.market.BybitMarket;
import bybit.trade.BybitTradeService;

public class BybitAlgoTrading implements Runnable{
	private static final String[] symbols = 
		{"BTCUSDT", "ETHUSDT", "XRPUSDT", "SOLUSDT",
		"ADAUSDT", "MNTUSDT", "APEUSDT", "DOGEUSDT",
		"SUIUSDT", "ADAUSDT", "LTCUSDT", "ZILUSDT"};
	public void run() {
		BybitMarket bybitMarket = new BybitMarket();
		BybitTradeService tradeService = new BybitTradeService();
		
		for (String symbol : symbols) {
			System.out.println(symbol + ":");
			double RSI = bybitMarket.getSecurityRSI(symbol, CategoryType.SPOT, MarketInterval.FIVE_MINUTES, 14);
			System.out.println(RSI);
			if (RSI <= 30) {
				System.out.println(tradeService.placeSpotBuyMarketOrder(symbol, 1000));
			} else if (RSI >= 70) {
				System.out.println(tradeService.placeSpotSellMarketOrder(symbol, 1000));
			}
		}
	}
	public static void main(String[] args) {
		BybitAlgoTrading tradeApp = new BybitAlgoTrading();
		tradeApp.run();
	}
}