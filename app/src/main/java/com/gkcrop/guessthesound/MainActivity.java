package com.gkcrop.guessthesound;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends Activity {

	Button Facebook;
	Button GooglePlayApps;
	Button LetsPlay;
	Context context;
	Button movieShadow;
	Button resButton;
	private AdView mAdView;
	private InterstitialAd mInterstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAdView = (AdView) findViewById(R.id.adView);
		mAdView.loadAd(new AdRequest.Builder().build());

		mInterstitial = new InterstitialAd(this);
		mInterstitial.setAdUnitId(getResources().getString(R.string.admob_intertestial_id));
		mInterstitial.loadAd(new AdRequest.Builder().build());

		mInterstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// TODO Auto-generated method stub
				super.onAdLoaded();
				if (mInterstitial.isLoaded()) {
					mInterstitial.show();
				}
			}
		});

		if(!fileExist()){writeData("1|10");}

		LetsPlay = (Button)findViewById(R.id.letsPlay);
		GooglePlayApps = (Button)findViewById(R.id.AllAppsGoogle);
		resButton = (Button)findViewById(R.id.restartGame);
		Facebook = (Button)findViewById(R.id.button1);
		context = this;

		LetsPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intgame=new Intent(MainActivity.this,TheGame.class);
				startActivity(intgame);

			}
		});

		GooglePlayApps.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse(getString(R.string.play_more_apps))));

			}
		});

		resButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder alert = new AlertDialog.Builder(
						MainActivity.this);
				alert.setTitle(getString(R.string.reset_title));
				alert.setMessage(getString(R.string.reset_msg));

				alert.setPositiveButton(getString(R.string.reset_yes),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						writeData(getString(R.string.point_give));
					}

				});

				alert.setNegativeButton(getString(R.string.reset_no),
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub


					}
				});
				alert.show();	


			}
		});

		Facebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse(getString(R.string.facebook_url))));
			}
		});



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean fileExist(){
		File file = new File(getFilesDir()+File.separator+"thewords.dat");
		if(file.exists()){return true;}
		else{return false;}    
	}

	private void writeData(String dataStr){
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("thewords.dat", Context.MODE_PRIVATE));
			outputStreamWriter.write(dataStr);
			outputStreamWriter.close();
		}
		catch (IOException e) {} 

	}

}
