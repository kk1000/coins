package hu.droidium.coins;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	private static final String PREFS_NAME = "CoinPreferences";
	public static final String IS_RUNNING = "Is game running?";
	public static final String TARGET_VALUE = "Target value";
	public static final String TARGET_COUNT = "Target count";
	public static final String GAME_ENDED = "Game ended";

	private static final Random random = new Random();

	private static final int EASY_MIN_TARGET = 5;
	private static final int EASY_MAX_TARGET = 13;
	private static final int EASY_MIN_COIN = 2;
	private static final int EASY_MAX_COIN = 5;

	private static final int MEDIUM_MIN_TARGET = 14;
	private static final int MEDIUM_MAX_TARGET = 20;
	private static final int MEDIUM_MIN_COIN = 4;
	private static final int MEDIUM_MAX_COIN = 8;

	private static final int HARD_MIN_TARGET = 21;
	private static final int HARD_MAX_TARGET = 35;
	private static final int HARD_MIN_COIN = 3;
	private static final int HARD_MAX_COIN = 12;

	SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		// Check if game is running
		boolean running = prefs.getBoolean(IS_RUNNING, false);
		int targetValue = prefs.getInt(TARGET_VALUE, -1);
		int targetCount = prefs.getInt(TARGET_COUNT, -1);
		Intent starterIntent = getIntent();
		if ((!starterIntent.hasExtra(GAME_ENDED)) && running && targetValue != -1 && targetCount != -1) {
			Intent intent = new Intent(this, MakeWithExactCoinsActivity.class);
			intent.putExtra(TARGET_VALUE, targetValue);
			intent.putExtra(TARGET_COUNT, targetCount);
			finish();
			startActivity(intent);
		} else {
			prefs.edit().remove(IS_RUNNING).remove(TARGET_VALUE).remove(TARGET_COUNT).commit();
			setContentView(R.layout.main_screen);
			findViewById(R.id.easyButton).setOnClickListener(this);
			findViewById(R.id.mediumButton).setOnClickListener(this);
			findViewById(R.id.hardButton).setOnClickListener(this);
		}
	}
	@Override
	public void onClick(View v) {
		int minTarget;
		int targetRange;
		int minCount;
		int countRange;
		switch (v.getId()) {
		case R.id.easyButton:
			minTarget = EASY_MIN_TARGET;
			targetRange = EASY_MAX_TARGET - EASY_MIN_TARGET;
			minCount = EASY_MIN_COIN;
			countRange = EASY_MAX_COIN - EASY_MIN_COIN;
			break;
		case R.id.mediumButton:
			minTarget = MEDIUM_MIN_TARGET;
			targetRange = MEDIUM_MAX_TARGET - MEDIUM_MIN_TARGET;
			minCount = MEDIUM_MIN_COIN;
			countRange = MEDIUM_MAX_COIN - MEDIUM_MIN_COIN;
			break;
		case R.id.hardButton:
			minTarget = HARD_MIN_TARGET;
			targetRange = HARD_MAX_TARGET - HARD_MIN_TARGET;
			minCount = HARD_MIN_COIN;
			countRange = HARD_MAX_COIN - HARD_MIN_COIN;
			break;
		default:
			minTarget = EASY_MIN_TARGET;
			targetRange = EASY_MAX_TARGET - EASY_MIN_TARGET;
			minCount = EASY_MIN_COIN;
			countRange = EASY_MAX_COIN - EASY_MIN_COIN;
			break;
		}
		MakeItWithExactCoins game = new MakeItWithExactCoins(minTarget + random.nextInt(targetRange), minCount + random.nextInt(countRange));
		int targetValue = game.getValue();
		int targetCount = game.getCorrectCoinCount();
		prefs.edit()
			.putBoolean(IS_RUNNING, true)
			.putInt(TARGET_VALUE, targetValue)
			.putInt(TARGET_COUNT, targetCount)
			.commit();
		Intent intent = new Intent(this, MakeWithExactCoinsActivity.class);
		intent.putExtra(TARGET_VALUE, targetValue);
		intent.putExtra(TARGET_COUNT, targetCount);
		finish();
		startActivity(intent);
	}
}
