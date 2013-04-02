package hu.droidium.coins;

public class MakeItWithTheFiewstCoins extends CoinsGame {
	public static final int RESULT_VALUE_NOT_CORRECT = -1;
	public static final int RESULT_TOO_MANY_COINS = -2;
	public static final int RESULT_WELL_DONE = 0;
	private int target;
	private int[] solution;
	private int minCoins;
	
	public MakeItWithTheFiewstCoins(int target) {
		this.target = target;
		int remaining = target;
		int checkingCoin = COINS.length - 1;
		solution = new int[COINS.length];
		for (int i = 0; i < solution.length; i++){
			solution[i] = 0;
		}
		while (remaining > 0) {
			if (COINS[checkingCoin] <= remaining) {
				solution[checkingCoin]++;
				remaining -= COINS[checkingCoin];
			} else {
				checkingCoin--;
			}
			// There is no solution - with properly chosen coin values, you cannot get here
			if (checkingCoin < 0) {
				solution = null;
				break;
			}
		}
		if (solution == null) {
			throw new IllegalArgumentException();
		} else {
			minCoins = 0;
			for (int i : solution){
				minCoins += i;
			}
		}
	}
	
	public int getValue(){
		return target;
	}
	
	public int getCorrectCoinCount(){
		return minCoins;
	}

	public int checkResult(final int[] coins) {
		int total = 0;
		int count = 0;
		for (int i = 0; i < Math.min(coins.length, COINS.length); i++) {
			if (coins[i] < 0) {
				throw new IllegalArgumentException();
			}
			total += coins[i] * COINS[i];
			count += coins[i];
		}
		if (total != target) {
			return RESULT_VALUE_NOT_CORRECT;
		}
		if (count == minCoins) {
			return RESULT_WELL_DONE;
		} else {
			return RESULT_TOO_MANY_COINS;
		}
	}
}
