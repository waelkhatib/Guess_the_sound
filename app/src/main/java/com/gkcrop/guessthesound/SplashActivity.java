package com.gkcrop.guessthesound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SplashActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		ImageView img_logo = findViewById(R.id.imageView1);
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
		fadeIn.setDuration(500);
		img_logo.setAnimation(fadeIn);
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 2000) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {

					Intent i = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(i);
					finish();

				}
			}
		};
		splashThread.start();
	}

}