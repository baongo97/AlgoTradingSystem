package bybit.trade;

import java.util.HashMap;
import java.util.Map;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;

import bybit.entity.OrderSide;
import bybit.entity.OrderType;
import bybit.market.BybitMarket;
import bybit.util.BybitConnection;


public class BybitTradeService {
	private final BybitApiTradeRestClient client;
	private final double TAKE_PROFIT_RATE = 1.1;
	private final double STOP_LOSS_RATE = 0.9;
	public BybitTradeService() {
		BybitConnection conn = BybitConnection.getConnection();
		client = conn.makeBybitTradeClient();
	}

	public Object placeSpotBuyMarketOrder(String symbol, double valueInUsdt) {
		return placeOrder(CategoryType.SPOT, OrderSide.BUY, OrderType.MARKET, symbol, valueInUsdt, 0);
	}
	public Object placeSpotSellMarketOrder(String symbol, double valueInUsdt) {
		return placeOrder(CategoryType.SPOT, OrderSide.SELL, OrderType.MARKET, symbol, valueInUsdt, 0);
	}
	public Object placeSpotBuyLimitOrder(String symbol, double valueInUsdt, double price) {
		double qty = convertUsdtValueToQty(valueInUsdt, price);
		return placeOrder(CategoryType.SPOT, OrderSide.BUY, OrderType.LIMIT, symbol, qty, price);
	}
	public Object placeSpotSellLimitOrder(String symbol, double valueInUsdt, double price) {
		double qty = convertUsdtValueToQty(valueInUsdt, price);
		return placeOrder(CategoryType.SPOT, OrderSide.SELL, OrderType.LIMIT, symbol, qty, price);
	}
	public Object placeFutureBuyMarketOrder(String symbol, double valueInUsdt) {
		BybitMarket bybitMarket = new BybitMarket();
		double securityLastPrice = bybitMarket.getSecurityLastPrice(CategoryType.LINEAR, symbol);
		double qty = convertUsdtValueToQty(valueInUsdt, securityLastPrice);
		return placeOrder(CategoryType.LINEAR, OrderSide.BUY, OrderType.MARKET, symbol, qty, 0);
	}
	public Object placeFutureSellMarketOrder(String symbol, double valueInUsdt) {
		BybitMarket bybitMarket = new BybitMarket();
		double securityLastPrice = bybitMarket.getSecurityLastPrice(CategoryType.LINEAR, symbol);
		double qty = convertUsdtValueToQty(valueInUsdt, securityLastPrice);
		return placeOrder(CategoryType.LINEAR, OrderSide.SELL, OrderType.MARKET, symbol, qty, 0);
	}
	public Object placeFutureBuyLimitOrder(String symbol, double valueInUsdt, double price) {
		double qty = convertUsdtValueToQty(valueInUsdt, price);
		return placeOrder(CategoryType.LINEAR, OrderSide.BUY, OrderType.LIMIT, symbol, qty, price);
	}
	public Object placeFutureSellLimitOrder(String symbol, double valueInUsdt, double price) {
		double qty = convertUsdtValueToQty(valueInUsdt, price);
		return placeOrder(CategoryType.LINEAR, OrderSide.SELL, OrderType.LIMIT, symbol, qty, price);
	}
	
	private double convertUsdtValueToQty(double valueInUsdt, double price) {
		double qty = Math.round((valueInUsdt/price)*100.0)/100.0;
		if (qty >= 1) {
			qty = Math.round(qty);
		}
		return qty;
	}
	private double calculateTakeProfitPrice(double price) {
		int decimalPlace = getNumberOfDecimal(price);
		double takeProfitPrice = price*TAKE_PROFIT_RATE;
		String rounded = String.format("%."+decimalPlace+"f", takeProfitPrice);
		return Double.parseDouble(rounded);
	}
	private double calculateStopLossPrice(double price) {
		int decimalPlace = getNumberOfDecimal(price);
		double takeProfitPrice = price*STOP_LOSS_RATE;
		String rounded = String.format("%."+decimalPlace+"f", takeProfitPrice);
		return Double.parseDouble(rounded);
	}
	private int getNumberOfDecimal(double number) {
		String numberStr = Double.toString(number);
		int decimalPlace;
		if (numberStr.contains(".")) {
			decimalPlace = numberStr.length() - numberStr.indexOf(".")-1;
		} else {
			decimalPlace = 0;
		}
		return decimalPlace;
	}
	
	private Object placeOrder(CategoryType cat, OrderSide side,
			OrderType orderType, String symbol,
			double valueInUSDT, double price) {
		Map<String, Object> order = new HashMap<>();
		order.put("category", cat);
		order.put("symbol", symbol);
		order.put("side", side);
		order.put("orderType", orderType.getOrderTypeValue());
		order.put("qty", "" + valueInUSDT);
		order.put("price", "" + price);
		order.put("marketUnit", "quoteCoin");
		return client.createOrder(order);
	}
	
	public static void main(String[] args) {
		BybitTradeService ser = new BybitTradeService();
		//0.114262
		System.out.println(ser.placeSpotBuyMarketOrder("BTCUSDT", 1000));
		
	}
}
