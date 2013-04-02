package hu.droidium.coins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MakeWithExactCoinsActivity extends Activity implements CoinListener, OnClickListener {
	private int targetValue;
	private int targetCount;
	private int total = 0;
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);	
		window.getDecorView().getBackground().setDither(true);

		Intent intent = getIntent();
		targetValue = intent.getIntExtra(MainActivity.TARGET_VALUE, -1);
		targetCount = intent.getIntExtra(MainActivity.TARGET_COUNT, -1);
		setContentView(R.layout.coin_layout);
		((CoinSurface)findViewById(R.id.surface)).setListener(this);
		((TextView)findViewById(R.id.targetTextHolder)).setText("" + targetValue);
		((TextView)findViewById(R.id.targetCountHolder)).setText("" + targetCount);
		findViewById(R.id.readyButton).setOnClickListener(this);
	}

	@Override
	public void add(int value) {
		total += value;
		count ++;
	}

	@Override
	public void remove(int value) {
		total -= value;
		count --;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.GAME_ENDED, true);
		startActivity(intent);
		finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (count == targetCount && total == targetValue) {
			Toast.makeText(this, "Nagyon ügyes!", Toast.LENGTH_LONG).show();
		} else if (count == targetCount){
			Toast.makeText(this, "A pénzérmék száma stimmel, az érték nem.", Toast.LENGTH_LONG).show();
		} else if (total == targetValue){
			Toast.makeText(this, "A pénzérmék értéke stimmel, a számuk nem.", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Valamit még változtatni kellene rajta.", Toast.LENGTH_LONG).show();
		}
	}
}