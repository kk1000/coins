package hu.droidium.coins;

import java.util.ArrayList;

import android.util.Log;

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
			Log.e("Checking for " + targetTotal, "With coins " + estimatedTargetCoins);
			solution = solve(targetTotal, estimatedTargetCoins);
		}
		if (solution == null) {
			throw new IllegalArgumentException(targetTotal + " " + estimatedTargetCoins);
		} else {
			for (Integer coin : solution) {
				this.solution[COIN_INDEXES.get(coin)] ++;
			}
			this.targetCount = estimatedTargetCoins;
		}
	}
	
	private ArrayList<Integer> solve(int targetTotal, int estimatedTargetCoins) {
		if ((estimatedTargetCoins < 0)||(targetTotal < 0)) {
			Log.w("Stopped sub zero", "Coins " + estimatedTargetCoins + " value " + targetTotal);			
			return null;
		} else if ((estimatedTargetCoins == 0) && (targetTotal > 0)) {
			Log.w("Stopped no more coins", "Coins " + estimatedTargetCoins + " value " + targetTotal);			
			return null;
		} else if ((estimatedTargetCoins < 0) && (targetTotal == 0)) {
			Log.w("Stopped no more value", "Coins " + estimatedTargetCoins + " value " + targetTotal);			
			return null;
		} else if ((estimatedTargetCoins == 0) && (targetTotal == 0)) {
			Log.w("We're there", "Coins " + estimatedTargetCoins + " value " + targetTotal);			
			return new ArrayList<Integer>();
		} else {
			Log.i("Checking", "Coins " + estimatedTargetCoins + " value " + targetTotal);			
			for (int i = COINS.length -1; i >= 0; i--) {
				if(COINS[i] <= targetTotal) {
					Log.d("Checking coin", "" + COINS[i]);
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