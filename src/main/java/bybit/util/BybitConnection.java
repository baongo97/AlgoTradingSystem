package bybit.util;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.restApi.BybitApiAccountRestClient;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;

import bybit.credential.ByBitApiKey;

public class BybitConnection {
	private static BybitConnection instance;
	private BybitApiClientFactory factory;
	private BybitApiTradeRestClient tradeClient;
	private BybitApiAccountRestClient accountClient;
	private BybitApiMarketRestClient marketClient;
	
	private BybitConnection() {
		factory = BybitApiClientFactory.newInstance(ByBitApiKey.API_KEY, ByBitApiKey.API_SECRETE,
				BybitApiConfig.DEMO_TRADING_DOMAIN);
	}
	public static BybitConnection getConnection() {
		if (instance == null) {
			instance = new BybitConnection();
		}
		return instance;
	}
	
	public BybitApiTradeRestClient makeBybitTradeClient() {
		if (tradeClient == null) {
			tradeClient = factory.newTradeRestClient();
		}
		return tradeClient;
	}
	
	public BybitApiAccountRestClient makeBybitAccountClient() {
		if (accountClient == null) {
			accountClient = factory.newAccountRestClient();
		}
		return accountClient;
	}
	
	public BybitApiMarketRestClient makeBybitMarketClient() {
		if (marketClient == null) {
			marketClient = factory.newMarketDataRestClient();
		}
		return marketClient;
	}
	public static void main(String[] args) {
		BybitConnection conn = BybitConnection.getConnection();
		BybitApiMarketRestClient client = conn.makeBybitMarketClient();
		System.out.println(client);
	}
}
