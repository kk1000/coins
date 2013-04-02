package hu.droidium.coins;

import hu.droidium.coins.Coin.State;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CoinSurface extends SurfaceView implements SurfaceHolder.Callback,
		View.OnTouchListener {
	private Context context;
	private CoinDrawThread thread;
	private Drawable background;
	private Drawable[] coinDrawables = new Drawable[8];

	private ArrayList<Coin> coins = new ArrayList<Coin>();
	private int numberOfCoinTypes = 8;
	private Coin draggedCoin = null;
	private int dropZoneHeight;
	private int dropZoneY;
	private int dropZoneWidth;
	private int dropZoneX;
	private int canvasWidth;
	private int canvasHeight;
	private int coinWidth;
	private int offsetX;
	private int offsetY;
	
	private Paint dropZonePaint = new Paint();
	private CoinListener listener;
	{
		dropZonePaint.setColor(Color.argb(150, 255, 255, 255));
		dropZonePaint.setStyle(Paint.Style.STROKE);
		dropZonePaint.setStrokeWidth(15);
		dropZonePaint.setPathEffect(new DashPathEffect(new float[] {30,30}, 0));
	}

	public CoinSurface(Context context) {
		super(context);
		Log.e("Surface constructor", "Surface constructor 1");
		this.context = context;
		init();
	}

	public CoinSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.e("Surface constructor", "Surface constructor 2");
		this.context = context;
		int count = attrs.getAttributeIntValue(null, "numberOfCoins", -1);
		if (count != -1) {
			init(count);
		} else {
			init();
		}
	}

	public CoinSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		int count = attrs.getAttributeIntValue(null, "numberOfCoins", -1);
		if (count == -1) {
			init(count);
		} else {
			init();
		}
	}
	
	public void setListener(CoinListener listener) {
		this.listener = listener;
	}

	private void init() {
		getHolder().addCallback(this);
		setOnTouchListener(this);
		background = context.getResources().getDrawable(R.drawable.green);
		coinDrawables[0] = context.getResources()
				.getDrawable(R.drawable.euro_1);
		coinDrawables[1] = context.getResources()
				.getDrawable(R.drawable.euro_2);
		coinDrawables[2] = context.getResources()
				.getDrawable(R.drawable.euro_5);
		coinDrawables[3] = context.getResources().getDrawable(
				R.drawable.euro_10);
		coinDrawables[4] = context.getResources().getDrawable(
				R.drawable.euro_20);
		coinDrawables[5] = context.getResources().getDrawable(
				R.drawable.euro_50);
		coinDrawables[6] = context.getResources().getDrawable(
				R.drawable.euro_100);
		coinDrawables[7] = context.getResources().getDrawable(
				R.drawable.euro_200);
		Coin oneCent = new Coin(coinDrawables[0], 1);
		oneCent.setState(State.BASE);
		coins.add(oneCent);
		Coin twoCent = new Coin(coinDrawables[1], 2);
		twoCent.setState(State.BASE);
		coins.add(twoCent);
		Coin fiveCent = new Coin(coinDrawables[2], 5);
		fiveCent.setState(State.BASE);
		coins.add(fiveCent);
		Coin tenCent = new Coin(coinDrawables[3], 10);
		tenCent.setState(State.BASE);
		coins.add(tenCent);
		Coin twentyCent = new Coin(coinDrawables[4], 20);
		twentyCent.setState(State.BASE);
		coins.add(twentyCent);
		Coin fiftyCent = new Coin(coinDrawables[5], 50);
		fiftyCent.setState(State.BASE);
		coins.add(fiftyCent);
		Coin oneEuro = new Coin(coinDrawables[6], 100);
		oneEuro.setState(State.BASE);
		coins.add(oneEuro);
		Coin twoEuro = new Coin(coinDrawables[7], 200);
		twoEuro.setState(State.BASE);
		coins.add(twoEuro);
	}
	
	private void init(int numberOfCoinTypes) {
		this.numberOfCoinTypes = numberOfCoinTypes;
		init();
	}

	public void drawCoins(Canvas canvas) {
		// Draw background
		background.setBounds(canvas.getClipBounds());
		background.draw(canvas);
		// Draw drop zone on canvas
		canvas.drawColor(android.R.color.white);
		canvas.drawRect(dropZoneX - dropZoneWidth / 2,
				dropZoneY - dropZoneHeight / 2,
				dropZoneX + dropZoneWidth / 2,
				dropZoneY + dropZoneHeight / 2,
				dropZonePaint);
		// Draw coins on table
		int left = 0;
		int right;
		int bottom = canvasHeight;
		int top = canvasHeight - coinWidth;
		for (int i = 0; i < numberOfCoinTypes; i++) {
			right = left + coinWidth;
			coins.get(i).setPosition((left + right) / 2, (top + bottom) /2);
			coins.get(i).setSize(coinWidth, coinWidth);
			coins.get(i).draw(canvas);
			left += coinWidth;
		}
		// Draw coins in lot
		synchronized (coins) {
			for (Coin coin : coins) {
				coin.draw(canvas);
			}
			
		}
		// Draw dragged coin
		if (draggedCoin != null) {
			draggedCoin.draw(canvas);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		canvasWidth = width;
		canvasHeight = height;
		coinWidth = canvasWidth / numberOfCoinTypes;
		offsetX = (int)(width * 0.1);
		offsetY = (int)(height * 0.1);
		dropZoneWidth = width - offsetX * 2;
		dropZoneHeight = height - coinWidth - offsetY * 2;
		dropZoneX = dropZoneWidth / 2 + offsetX;
		dropZoneY = dropZoneHeight / 2 + offsetY;
		Log.e("Surface constructor", "Surface constructor");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e("Surface created", "Surface created");
		if (thread != null) {
			thread.setRunning(false);
		}
		thread = new CoinDrawThread(holder, context, this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e("Surface constructor", "Surface constructor");
		if (thread != null) {
			thread.setRunning(false);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getPointerCount() > 1) {
	        Log.w("Multitouch", "Multitouch not allowed!");
	        return true;
	    }		
		int touchedX = (int)event.getX();
		int touchedY = (int)event.getY();

		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Find touched coin
			for (int i = coins.size() - 1; i >= 0; i--) {
				if(coins.get(i).touched(touchedX, touchedY)) {
					if (coins.get(i).getState() == State.BASE) {
						// Create a new coin to drag
						Log.e("Pick up coin", "On base " + coins.get(i).getValue());
						draggedCoin = new Coin(coins.get(i));
						
					} else {
						Log.e("Pick up coin", "Already added " + coins.get(i).getValue());
						synchronized (coins) {
							draggedCoin = coins.remove(i);
							listener.remove(draggedCoin.getValue());
						}
					}
					draggedCoin.pickUp();
					break;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// Move coin if in drag mode
			if (draggedCoin != null) {
				draggedCoin.setPosition(touchedX, touchedY);
			}
			break;
		case MotionEvent.ACTION_UP:
			// Drop coin and check if in valid position
			if (draggedCoin != null) {
				Log.e("Dropping", "Dropping");
				if (draggedCoin.drop(touchedX, touchedY, dropZoneX, dropZoneY, dropZoneWidth, dropZoneHeight)) {
					Log.e("Realy dropping", "Dropping");
					synchronized (coins) {
						listener.add(draggedCoin.getValue());
						coins.add(draggedCoin);
					}
				}
			}
			draggedCoin = null;
			break;
		case MotionEvent.ACTION_CANCEL:
			draggedCoin = null;
			break;
		case MotionEvent.ACTION_OUTSIDE:
			draggedCoin = null;
			break;
		}
		return true;
	}
}