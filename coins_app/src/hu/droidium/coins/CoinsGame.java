package hu.droidium.coins;

import android.util.SparseIntArray;

public abstract class CoinsGame {
	public static final int[] COINS = {1, 2, 5, 10, 20, 50, 100};
	public static final SparseIntArray COIN_INDEXES = new SparseIntArray();
	static {
		for (int i = 0; i < COINS.length; i++) {
			COIN_INDEXES.put(COINS[i], i);
		}
	}
}
