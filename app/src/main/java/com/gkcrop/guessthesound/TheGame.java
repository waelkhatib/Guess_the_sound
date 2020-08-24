package com.gkcrop.guessthesound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.loopj.android.image.SmartImageView;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class TheGame extends Activity {
	// Variables
	InterstitialAd interstitial;
	private Button[] word_btn;
	private String lvl = "0";
	private String coins = "0";
	private String[] chars = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private String[] word_array;
	private String theWord = "999";
	private String resultWord = "";
	public Button[] randBtn;


	SoundPool soundPool;
	Context mContext;
	String SoundFile,Ribbon;
	TextView txt_ribon;

	Button btn_first,btn_bomb,btn_skip,btn_back,btn_ask;
	boolean loaded = false,isLast=false;
	private int soundID,Count=0;
	StringBuilder sb;
	public TheGame() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 9) {
			try {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			} catch (Exception e) {
			}
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		mContext=TheGame.this;
		sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().toString()).append(File.separator).append(getString(R.string.app_name));

		txt_ribon=(TextView)findViewById(R.id.txt_ribon);
		btn_first=(Button)findViewById(R.id.button5);
		btn_bomb=(Button)findViewById(R.id.button4);
		btn_skip=(Button)findViewById(R.id.button3);
		btn_back=(Button)findViewById(R.id.button1);
		btn_ask=(Button)findViewById(R.id.button6);

		Button button = (Button)findViewById(R.id.button8);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.rippleanimset);
		animation.setFillAfter(false);
		animation.setRepeatCount(0x186a0);
		button.startAnimation(animation);

		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest.Builder().build());


		// 12 orange buttons where appear letters of the word, and other letters
		randBtn = new Button[] { (Button) findViewById(R.id.char1),
				(Button) findViewById(R.id.char2),
				(Button) findViewById(R.id.char3),
				(Button) findViewById(R.id.char4),
				(Button) findViewById(R.id.char5),
				(Button) findViewById(R.id.char6),
				(Button) findViewById(R.id.char7),
				(Button) findViewById(R.id.char8),
				(Button) findViewById(R.id.char9),
				(Button) findViewById(R.id.char10),
				(Button) findViewById(R.id.char11),
				(Button) findViewById(R.id.char12) };
		Intent intent = getIntent();
		lvl = readData().split("\\|")[0];
		coins = readData().split("\\|")[1];
		if (Integer.parseInt(coins) < 0) {
			coins = "0";
		}
		parseXML(Integer.parseInt(lvl)-1);
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		if(!isLast)
		{

			int sound_id = mContext.getResources().getIdentifier(SoundFile, "raw",
					mContext.getPackageName());
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					loaded = true;
				}
			});
			soundID = soundPool.load(this, sound_id, 1);

			txt_ribon.setText(Ribbon);
			word_array = getWord(theWord);
			createWord(word_array.length);
			randomChars();
			TextView lvl_txt = (TextView) findViewById(R.id.textView2);
			lvl_txt.setText(" " + lvl + " ");
			TextView coins_txt = (TextView) findViewById(R.id.textView1);
			coins_txt.setText(coins);
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.reset_msg_1));
			builder.setMessage(getString(R.string.reset_msg_2));
			builder.setIcon(R.drawable.ic_launcher);
			builder.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					TheGame.this.finish();
				}
			});
			builder.setNegativeButton(getString(R.string.reset_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					writeData(getString(R.string.point_give));
					dialog.dismiss();
					TheGame.this.finish();
				}
			});
			AlertDialog alert = builder.create();
			alert.setCancelable(false);
			alert.show();
		}


		((Button)findViewById(R.id.button7)).setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view)
			{

				Count+=1;
				if (Count %2==1) {
					if(loaded)
					{
						soundPool.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);

					}
					else
					{
						Toast.makeText(getApplicationContext(), "Wait Sound is Loaded", Toast.LENGTH_SHORT).show();
					}

				}
				if (Count % 2==0) {
					soundPool.stop(soundID);
					soundPool.play(soundID, 1.0F, 1.0F, 0, 0, 1.0F);
				}

			}
		});


		btn_first.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							if (Integer.parseInt(coins) >= Integer.parseInt(getString(R.string.how_much_for_first_letter))) {
								btn_first.setVisibility(View.INVISIBLE);
								coins = "" + (Integer.parseInt(coins) - Integer.parseInt(getString(R.string.how_much_for_first_letter)));
								TextView coins_txt = (TextView) findViewById(R.id.textView1);
								coins_txt.setText(coins);
								writeData("" + (Integer.parseInt(lvl)) + "|"
										+ (Integer.parseInt(coins)));
								word_btn[0].setText(word_array[0].toUpperCase());
								word_btn[0].setOnClickListener(null);
								for (int i = 0; i < 12; i++) {
									if (randBtn[i].getText().equals(
											word_array[0].toUpperCase())) {
										randBtn[i]
												.setVisibility(View.INVISIBLE);
										i = 12;
									}
								}
							}
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};

				// Check if sufficient coins
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TheGame.this);
				builder.setTitle(getString(R.string.first_letter_msg_3)).setIcon(
						R.drawable.help);
				if (Integer.parseInt(coins) >= Integer.parseInt(getString(R.string.how_much_for_first_letter))) {
					builder.setMessage(getString(R.string.first_letter_msg_1));
					builder.setNegativeButton(getString(R.string.no), dialogClickListener)
					.setPositiveButton(getString(R.string.yes), dialogClickListener)
					.show();
				} else {
					builder.setMessage(getString(R.string.first_letter_msg_2));
					builder.setNegativeButton(getString(R.string.ok), dialogClickListener)
					.show();
				}

			}
		});

		btn_bomb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							if (Integer.parseInt(coins) >= Integer.parseInt(getString(R.string.how_much_for_bomb))) {
								btn_bomb.setVisibility(View.INVISIBLE);
								coins = "" + (Integer.parseInt(coins) - Integer.parseInt(getString(R.string.how_much_for_bomb)));
								TextView coins_txt = (TextView) findViewById(R.id.textView1);
								coins_txt.setText(coins);
								writeData("" + (Integer.parseInt(lvl)) + "|"
										+ (Integer.parseInt(coins)));
								remove3Chars();
							}
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};

				// Check if sufficient coins
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TheGame.this);
				builder.setTitle(getString(R.string.bomb_msg_3)).setIcon(R.drawable.help);
				if (Integer.parseInt(coins) >= Integer.parseInt(getString(R.string.how_much_for_bomb))) {
					builder.setMessage(getString(R.string.bomb_msg_1));
					builder.setNegativeButton(getString(R.string.no), dialogClickListener)
					.setPositiveButton(getString(R.string.yes), dialogClickListener)
					.show();
				} else {
					builder.setMessage(getString(R.string.bomb_msg_2));
					builder.setNegativeButton(getString(R.string.ok), dialogClickListener)
					.show();
				}
			}
		});

		btn_skip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							if (Integer.parseInt(coins) >= Integer.parseInt(getString(R.string.how_much_for_skip))) {
								btn_skip.setVisibility(View.INVISIBLE);
								coins = "" + (Integer.parseInt(coins) - Integer.parseInt(getString(R.string.how_much_for_skip)));
								TextView coins_txt = (TextView) findViewById(R.id.textView1);
								coins_txt.setText(coins);
								writeData("" + (Integer.parseInt(lvl) + 1) + "|"
										+ (Integer.parseInt(coins)));
								finish();
								startActivity(getIntent());
							}
							break;
						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};

				// Check if sufficient coins
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TheGame.this);
				builder.setTitle(getString(R.string.skip_msg_3)).setIcon(R.drawable.help);
				if (Integer.parseInt(coins) >= Integer.parseInt(getString(R.string.how_much_for_skip))) {
					builder.setMessage(getString(R.string.skip_msg_1));
					builder.setNegativeButton(getString(R.string.no), dialogClickListener)
					.setPositiveButton(getString(R.string.yes), dialogClickListener)
					.show();
				} else {
					builder.setMessage(getString(R.string.skip_msg_2));
					builder.setNegativeButton(getString(R.string.ok), dialogClickListener)
					.show();
				}
			}
		});


		if (Integer.parseInt(lvl) % Integer.parseInt(getString(R.string.number_of_stage_ad)) == 0) {
			interstitial = new InterstitialAd(this);
			interstitial.setAdUnitId(getString(R.string.admob_intertestial_id));
			interstitial.loadAd(new AdRequest.Builder().build());
			interstitial.show();
			if (!interstitial.isLoaded()) {
				AdRequest adRequest1 = new AdRequest.Builder()
				.build();
				// Begin loading your interstitial.
				interstitial.loadAd(adRequest1);
			}
			interstitial.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					interstitial.show();
				}
			});
		} 

		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		btn_ask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String path=SaveBackground();
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("image/jpeg");
				share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
				startActivity(Intent.createChooser(share, "Share Image"));

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		soundPool.release();
	}

	// Function that generate black squares, depending on the number of letters
	// in the word
	private void createWord(int length) {
		LinearLayout world_layout = (LinearLayout) findViewById(R.id.world_layout);
		LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, length);

		word_btn = new Button[length];

		for (int i = 0; i < length; i++) {
			word_btn[i] = new Button(getApplicationContext());
			word_btn[i].setText("");
			word_btn[i].setId(i);
			word_btn[i].setTextColor(Color.parseColor("#ffffff"));
			word_btn[i].setTextSize(24);
			word_btn[i].setTypeface(Typeface.DEFAULT_BOLD);
			word_btn[i].setLayoutParams(param);
			word_btn[i].setBackgroundResource(R.drawable.matchbox);
			world_layout.addView(word_btn[i]);
			word_btn[i].setOnClickListener(charOnClick(word_btn[i]));
		}
	}

	// Function that generate random letters + word's leter on orange buttons
	private void randomChars() {
		for (int i = 0; i < 12; i++) {
			randBtn[i].setOnClickListener(randCharClick(randBtn[i]));
			Random r = new Random();
			int i1 = r.nextInt(25 - 0) + 0;
			randBtn[i].setText(chars[i1]);
		}

		List<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < 12; i++) {
			list.add(i);
		}

		Collections.shuffle(list);

		for (int x = 0; x < word_array.length; x++) {
			int value = list.remove(0);
			randBtn[value].setText(word_array[x].toUpperCase());
		}
	}

	// Fuction that clear wrong letter from black squares
	private OnClickListener charOnClick(final Button button) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				for (int i = 0; i < 12; i++) {
					if (randBtn[i].getVisibility() == View.INVISIBLE
							&& randBtn[i].getText() == button.getText())
						randBtn[i].setVisibility(View.VISIBLE);
				}

				button.setText("");

			}
		};
	}

	// Function for orange buttons
	private OnClickListener randCharClick(final Button btn) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				for (int i = 0; i < word_array.length; i++) {
					if (word_btn[i].getText() == "") {
						word_btn[i].setText(btn.getText());
						i = word_array.length;
					}
				}
				createResult();
			}
		};
	}

	// Function that check if the word is correct and showing correct/wrong
	// dialog
	private void createResult() {
		resultWord = "";
		for (int i = 0; i < word_array.length; i++) {
			if (word_btn[i].getText() != "") {
				resultWord += word_btn[i].getText();
			}
		}

		if (resultWord.length() == word_array.length) {
			if (resultWord.equalsIgnoreCase(theWord)) {
				showMyDialog(1, null);
			} else {
				showMyDialog(2, null);
			}
		}
	}

	// Function that transform the word to array
	private String[] getWord(String str) {
		String[] chars = str.split("");
		List<String> selected_chars = new ArrayList<String>();
		for (int i = 0; i < chars.length; i++) {
			selected_chars.add(chars[i]);
		}
		selected_chars.remove(0);
		return selected_chars.toArray(new String[selected_chars.size()]);
	}

	// //Function that showing dialogs: correct, wrong or zooming image
	private void showMyDialog(final int type, String bmp) {
		final Dialog dialog = new Dialog(TheGame.this, R.style.dialogStyle);
		dialog.setContentView(R.layout.dialog);
		dialog.getWindow().getDecorView()
		.setBackgroundResource(R.drawable.dialog_bg);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		String points = ""
				+ ((new Random().nextInt(10 - 3) + 3) + word_array.length);
		SmartImageView image = (SmartImageView) dialog
				.findViewById(R.id.imageDialog);
		Button dialogBtn = (Button) dialog.findViewById(R.id.dialogBtn);
		TextView score = (TextView) dialog.findViewById(R.id.points);

		if (type == 1) {
			image.setImageResource(R.drawable.corect);
			dialogBtn.setText(" Continue "); // Next level button
			score.setText("+" + points);
			writeData("" + (Integer.parseInt(lvl) + 1) + "|"
					+ (Integer.parseInt(coins) + Integer.parseInt(points)));
		} else if (type == 2) {
			image.setImageResource(R.drawable.gresit);
			dialogBtn.setText("  Try Again  "); // Try again button, restart
			// current level
			score.setText("-5");
			if (Integer.parseInt(coins) > 0 && Integer.parseInt(coins) <= 5) {
				writeData("" + (Integer.parseInt(lvl)) + "|"
						+ (Integer.parseInt("0")));
			} else {
				writeData("" + (Integer.parseInt(lvl)) + "|"
						+ (Integer.parseInt(coins) - 5));
			}
		} else {
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			score.setVisibility(View.GONE);
			dialogBtn.setVisibility(View.GONE);
			ImageView coinicon = (ImageView) dialog
					.findViewById(R.id.dialogIcon);
			coinicon.setVisibility(View.GONE);
			image.setImageUrl(bmp);
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}

		dialog.show();

		dialogBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (type > 0) {
					finish();
					startActivity(getIntent());
				}
				dialog.dismiss();
			}
		});

	}
	//		// Button that open "Share on Facebook" dialog
	//		fb.setOnClickListener(new OnClickListener() {
	//			@Override
	//			public void onClick(View v) {
	//				ByteArrayOutputStream stream = new ByteArrayOutputStream();
	//				getBitmapFromView().compress(Bitmap.CompressFormat.PNG, 100,
	//						stream);
	//				byte[] byteArray = stream.toByteArray();
	////				Intent i = new Intent(TheGame.this, LoginFragment.class);
	////				i.putExtra("image", byteArray);
	////				i.putExtra("lvl", lvl);
	////				startActivity(i);
	//				dialog.dismiss();
	//			}
	//		});


	// Function that save all user data. Current level, coins
	private void writeData(String dataStr) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					openFileOutput("thewords.dat", Context.MODE_PRIVATE));
			outputStreamWriter.write(dataStr);
			outputStreamWriter.close();
		} catch (IOException e) {
		}
	}

	// Function that read user data
	private String readData() {
		String ret = "";
		try {
			InputStream inputStream = openFileInput("thewords.dat");
			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();
				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}
				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return ret;
	}



	// Function that hide 3 orange buttons (letters)
	public void remove3Chars() {
		Button[] removeBtn = { (Button) findViewById(R.id.char1),
				(Button) findViewById(R.id.char2),
				(Button) findViewById(R.id.char3),
				(Button) findViewById(R.id.char4),
				(Button) findViewById(R.id.char5),
				(Button) findViewById(R.id.char6),
				(Button) findViewById(R.id.char7),
				(Button) findViewById(R.id.char8),
				(Button) findViewById(R.id.char9),
				(Button) findViewById(R.id.char10),
				(Button) findViewById(R.id.char11),
				(Button) findViewById(R.id.char12) };
		int x = 0;
		List<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < 12; i++) {
			list.add(i);
		}
		Collections.shuffle(list);
		while (x != 3) {
			int value = list.remove(0);
			if (!Arrays.asList(word_array).contains(
					removeBtn[value].getText().toString().toUpperCase())) {
				removeBtn[value].setVisibility(View.INVISIBLE);
				x += 1;

			}
		}
	}

	private void parseXML(int i) {
		AssetManager assetManager = getBaseContext().getAssets();
		try {
			InputStream is = assetManager.open("LevelData.xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			LevelSAXParserHandler myXMLHandler = new LevelSAXParserHandler();
			xr.setContentHandler(myXMLHandler);
			InputSource inStream = new InputSource(is);
			xr.parse(inStream);

			ArrayList<Level> cartList = myXMLHandler.getCartList();
			if(i>=cartList.size())
			{	
				isLast=true;
			}
			else
			{
				Level level=cartList.get(i); 
				theWord=level.getAnswer();
				SoundFile=level.getMusicId();
				Ribbon=level.getRibbon();

			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public String SaveBackground()
	{
		Bitmap bitmap;
		RelativeLayout panelResult = (RelativeLayout) findViewById(R.id.root);
		panelResult.invalidate();
		panelResult.setDrawingCacheEnabled(true);
		panelResult.buildDrawingCache();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int i = displaymetrics.heightPixels;
		int j = displaymetrics.widthPixels;
		bitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(panelResult.getDrawingCache()), j, i, true);
		panelResult.setDrawingCacheEnabled(false);
		String s = null;
		File file;
		boolean flag;
		file = new File(sb.toString());
		flag = file.isDirectory();
		s = null;
		if (flag)
		{
		}
		file.mkdir();
		FileOutputStream fileoutputstream1 = null;
		s = (new StringBuilder(String.valueOf("guess"))).append("_sound_").append(System.currentTimeMillis()).append(".png").toString();
		try {
			fileoutputstream1 = new FileOutputStream(new File(file, s));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fileoutputstream = fileoutputstream1;

		StringBuilder stringbuilder1;
		bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fileoutputstream);
		stringbuilder1 = new StringBuilder();
		stringbuilder1.append(sb.toString()).append(File.separator).append(s);

		try {
			fileoutputstream.flush();
			fileoutputstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ""+stringbuilder1;

	}
}