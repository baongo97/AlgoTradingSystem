package util;

public class RSICalculator {
    public static double calculateRSI(double[] prices, int period) {
        if (prices.length < period + 1) {
            throw new IllegalArgumentException("Not enough data points to calculate RSI");
        }
        
        double gain = 0, loss = 0;
        
        // Reverse the prices array to have the latest price at the end
        double[] reversedPrices = new double[prices.length];
        for (int i = 0; i < prices.length; i++) {
            reversedPrices[i] = prices[prices.length - 1 - i];
        }
        
        // Initial calculation of average gain and loss
        for (int i = 1; i <= period; i++) {
            double change = reversedPrices[i] - reversedPrices[i - 1];
            if (change > 0) {
                gain += change;
            } else {
                loss -= change; // Convert loss to positive
            }
        }
        
        gain /= period;
        loss /= period;
        
        // Calculate RSI for the rest of the data
        for (int i = period + 1; i < reversedPrices.length; i++) {
            double change = reversedPrices[i] - reversedPrices[i - 1];
            
            if (change > 0) {
                gain = ((gain * (period - 1)) + change) / period;
                loss = (loss * (period - 1)) / period;
            } else {
                gain = (gain * (period - 1)) / period;
                loss = ((loss * (period - 1)) - change) / period;
            }
        }
        
        double rs = (loss == 0) ? 100 : gain / loss;
        return 100 - (100 / (1 + rs));
    }
    
    public static void main(String[] args) {
        double[] prices = {45.64, 46.22, 46.41, 46.03, 45.89, 46.08, 45.84, 45.42, 45.10, 44.83, 44.33, 43.61, 44.15, 44.09, 44.34};
        int period = 14;
        
        double rsi = calculateRSI(prices, period);
        System.out.println("RSI: " + rsi);
    }
}

