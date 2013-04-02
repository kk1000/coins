package hu.droidium.coins;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class Coin {
	public enum State {
		BASE, DRAG, PLAYED
	}
	private final int value;
	private final Drawable drawable;
	private int x;
	private int y;
	private int width;
	private int height;
	
	private State state;
	
	public Coin(Drawable drawable, int value) {
		this.drawable = drawable;
		this.value = value;
		this.state = State.BASE;
	}

	public Coin(Coin coin) {
		this.drawable = coin.drawable;
		this.value = coin.value;
		this.x = coin.x;
		this.y = coin.y;
		this.width = coin.width;
		this.height = coin.height;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
	}

	public int getValue() {
		return value;
	}

	public void draw(Canvas canvas, Rect rect) {
		drawable.setBounds(rect);
		drawable.draw(canvas);
	}

	public void draw(Canvas canvas) {
		Rect rect = new Rect(x - width/2, y - height/2, x + width / 2, y + height / 2);
		drawable.setBounds(rect);
		drawable.draw(canvas);
	}

	public boolean touched(int x, int y) {
		int diffX = this.x - x;
		int diffY = this.y - y;
		// Too far
		if (diffX > width/2 || diffY > height / 2) {
			return false;
		}
		// Possibly in range
		double diff = Math.sqrt(diffX * diffX + diffY * diffY);
		return (diff < width / 2);
	}
	
	public void pickUp() {
		state = State.DRAG;
	}

	public boolean drop(int x, int y, int dropZoneX, int dropZoneY, int dropZoneWidth, int dropZoneHeight) {
		System.out.println("X " + x);
		System.out.println("Y " + y);
		System.out.println("DX " + dropZoneX);
		System.out.println("DY " + dropZoneY);
		System.out.println("w  " + dropZoneWidth);
		System.out.println("h  " + dropZoneHeight);
		
		if (x > dropZoneX - dropZoneWidth /2 && y > dropZoneY - dropZoneHeight /2 && x < dropZoneX + dropZoneWidth /2 && y < dropZoneY + dropZoneHeight / 2) {
			state = State.PLAYED;
			return true;
		}
		return false;
	}
}