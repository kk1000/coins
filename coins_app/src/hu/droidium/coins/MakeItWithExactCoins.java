package hu.droidium.coins;

import java.util.ArrayList;

public class MakeItWithExactCoins extends CoinsGame {
	public static final int RESULT_VALUE_NOT_CORRECT = -1;
	public static final int RESULT_COUNT_NOT_CORRECT = -2;
	public static final int RESULT_WELL_DONE = 0;
	private int targetTotal;
	private int[] solution = new int[COINS.length];
	private int targetCount;
	
	public MakeItWithExactCoins(int targetTotal, int estimatedTargetCoins) {
		this.targetTotal = targetTotal;
		ArrayList<Integer> solution;
		for (solution = null; (solution == null) && (targetTotal > estimatedTargetCoins); estimatedTargetCoins++){
			solution = solve(targetTotal, estimatedTargetCoins);
		}
		if (solution == null) {
			throw new IllegalArgumentException();
		} else {
			for (Integer coin : solution) {
				this.solution[COIN_INDEXES.get(coin)] ++;
			}
			this.targetCount = estimatedTargetCoins;
		}
	}
	
	private ArrayList<Integer> solve(int targetTotal, int estimatedTargetCoins) {
		if ((estimatedTargetCoins < 0)||(targetTotal < 0)) {
			return null;
		} else if ((estimatedTargetCoins == 0) && (targetTotal > 0)) {
			return null;
		} else if ((estimatedTargetCoins < 0) && (targetTotal == 0)) {
			return null;
		} else if ((estimatedTargetCoins == 0) && (targetTotal == 0)) {
			return new ArrayList<Integer>();
		} else {
			for (int i = COINS.length -1; i >= 0; i++) {
				if(COINS[i] < targetTotal) {
					ArrayList<Integer> subSolution = solve(targetTotal - COINS[i], estimatedTargetCoins - 1);
					if (subSolution != null) {
						subSolution.add(COINS[i]);
						return subSolution;
					}
				}
			}
			return null;
		}
	}
	
	public int getValue(){
		return targetTotal;
	}
	
	public int getCorrectCoinCount(){
		return targetCount;
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
		if (total != targetTotal) {
			return RESULT_VALUE_NOT_CORRECT;
		}
		if (count == targetCount) {
			return RESULT_WELL_DONE;
		} else {
			return RESULT_COUNT_NOT_CORRECT;
		}
	}
}
