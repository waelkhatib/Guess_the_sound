package com.gkcrop.guessthesound;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import android.Manifest;

public class MainActivity extends Activity {

	private InterstitialAd mInterstitial;
    private  final int INTERNET_FOR_STORE=1;
    private  final int ACCESS_NETWORK_STATE_FOR_STORE=2;
    private  final int INTERNET_FOR_FACEBOOK=3;
    private  final int ACCESS_NETWORK_STATE_FOR_FACEBOOK=4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        AdView mAdView = findViewById(R.id.adView);
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

		Button letsPlay = findViewById(R.id.letsPlay);
		Button googlePlayApps = findViewById(R.id.AllAppsGoogle);
		// --Commented out by Inspection (18/09/2020 20:47):private Context context;
		//Button movieShadow;
		Button resButton = findViewById(R.id.restartGame);
		Button facebook = findViewById(R.id.button1);
		//context = this;

		letsPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intgame=new Intent(MainActivity.this,TheGame.class);
				startActivity(intgame);

			}
		});

		googlePlayApps.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					// only for gingerbread and newer versions

					if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
						// Permission is not granted
						if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
								Manifest.permission.INTERNET)) {
							// No explanation needed; request the permission
							ActivityCompat.requestPermissions(MainActivity.this,
									new String[]{Manifest.permission.INTERNET},
									INTERNET_FOR_STORE);

					}

				}
					check_connect_to_store();


				}else
				connect_to_SyriaStore();
		}});

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

		facebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					// only for gingerbread and newer versions

					if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
						// Permission is not granted
						if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
								Manifest.permission.INTERNET)) {
							// No explanation needed; request the permission
							ActivityCompat.requestPermissions(MainActivity.this,
									new String[]{Manifest.permission.INTERNET},
									INTERNET_FOR_FACEBOOK);

						}

					}
					check_connect_facebook();


				}else
					connect_to_facebook();
			}
		});



	}

	private void check_connect_to_store() {
		if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
				// Permission is not granted
				if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
						Manifest.permission.ACCESS_NETWORK_STATE)) {
					// No explanation needed; request the permission
					ActivityCompat.requestPermissions(MainActivity.this,
							new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
							ACCESS_NETWORK_STATE_FOR_STORE);

			}else
				connect_to_SyriaStore();

		}else
			connect_to_SyriaStore();
	}

	private void check_connect_facebook() {
		if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
					Manifest.permission.ACCESS_NETWORK_STATE)) {
				// No explanation needed; request the permission
				ActivityCompat.requestPermissions(MainActivity.this,
						new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
						ACCESS_NETWORK_STATE_FOR_FACEBOOK);

			}else
				connect_to_facebook();

		}else
			connect_to_facebook();
	}

	private void connect_to_facebook() {
		if(isConnected(false)) {
			startActivity(new Intent(
					Intent.ACTION_VIEW,
					Uri.parse(getString(R.string.facebook_url))));
		}else {
			createDialog(R.string.you_must_have_internet_access);
		}
	}

	private void connect_to_SyriaStore() {
		if(isConnected(true)) {
			startActivity(new Intent(
					Intent.ACTION_VIEW,
					Uri.parse(getString(R.string.play_more_apps))));
		}else {
			createDialog(R.string.you_must_turn_on_mobile_data);
		}
	}

	private void createDialog(int messageId) {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				MainActivity.this);
		alert.setTitle(getString(R.string.warning));
		alert.setMessage(getString(messageId));

		alert.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub


					}
				});


		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private boolean isConnected(boolean is_data) {
		boolean connected = false;
		try {
			ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo nInfo = cm.getActiveNetworkInfo();
			if(is_data)
				connected = nInfo != null &&nInfo.getType()==ConnectivityManager.TYPE_MOBILE && nInfo.isAvailable() && nInfo.isConnected();
			else
				connected = nInfo != null &&(nInfo.getType()==ConnectivityManager.TYPE_MOBILE||nInfo.getType()==ConnectivityManager.TYPE_WIFI) && nInfo.isAvailable() && nInfo.isConnected();
			return connected;
		} catch (Exception e) {
			Log.e("Connectivity Exception", e.getMessage());
		}
		return connected;
	}

	private boolean fileExist(){
		File file = new File(getFilesDir()+File.separator+"thewords.dat");
		return file.exists();
	}

	private void writeData(String dataStr){
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("thewords.dat", Context.MODE_PRIVATE));
			outputStreamWriter.write(dataStr);
			outputStreamWriter.close();
		}
		catch (IOException ignored) {}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode==ACCESS_NETWORK_STATE_FOR_STORE&& grantResults.length>0
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
			connect_to_SyriaStore();
		}else
		if(requestCode==ACCESS_NETWORK_STATE_FOR_FACEBOOK && grantResults.length>0
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
			connect_to_facebook();
		}else
		if(requestCode==INTERNET_FOR_STORE && grantResults.length>0
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
			check_connect_to_store();
		}else
		if(requestCode==INTERNET_FOR_FACEBOOK && grantResults.length>0
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
			check_connect_facebook();
		}

	}
}
