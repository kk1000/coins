package hu.droidium.coins;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class CoinDrawThread extends Thread {
	boolean run;
	Canvas canvas;
	SurfaceHolder surfaceHolder;
	Context context;
	CoinSurface coinSurface;

	public CoinDrawThread(SurfaceHolder surfaceHolder, Context context, CoinSurface coinSurface) {
		this.surfaceHolder = surfaceHolder;
		this.context = context;
		this.coinSurface = coinSurface;
		run = true;
	}
	
	@Override
	public void run() {
		while(run){
			canvas = surfaceHolder.lockCanvas();
			if (canvas != null) {
				coinSurface.drawCoins(canvas);
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setRunning(boolean run) {
		this.run = run;
	}
}
