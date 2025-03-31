package bybit.entity;

import lombok.Getter;

@Getter
public enum OrderSide {
	BUY("Buy"),
	SELL("Sell");
	
	private final String orderSideValue;
	OrderSide(String orderSideValue){
		this.orderSideValue = orderSideValue;
	}
}
