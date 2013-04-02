package hu.droidium.coins;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MakeWithTheFiewestCoinsActivity extends Activity implements CoinListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coin_layout);
		((CoinSurface)findViewById(R.id.surface)).setListener(this);
	}

	@Override
	public void add(int value) {
		Log.e("Added coin", "" + value);
	}

	@Override
	public void remove(int value) {
		Log.e("Removed coin", "" + value);
	}
}