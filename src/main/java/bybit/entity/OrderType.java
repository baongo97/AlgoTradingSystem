package bybit.entity;

import lombok.Getter;

@Getter
public enum OrderType {
	MARKET("Market"),
	LIMIT("Limit");
	
	private final String orderTypeValue;
	OrderType(String orderTypeValue){
		this.orderTypeValue = orderTypeValue;
	}
}
