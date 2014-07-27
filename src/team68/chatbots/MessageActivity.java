package team68.chatbots;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.lib.asr.Alternative;
import com.fpt.lib.asr.Languages;
import com.fpt.lib.asr.Result;
import com.fpt.lib.asr.SpeakToText;
import com.fpt.lib.asr.SpeakToTextListener;
import com.fpt.lib.asr.Transcript;


public class MessageActivity extends RobotActivity implements SpeakToTextListener {

	ArrayList<Message> messages;
	ListApdapter adapter;
	EditText text;
	ListView listView;
	Robot mRobot;
	ImageButton searchBtn;
	SpeakToText stt;
	protected static final int RESULT_SPEECH = 1;
	static Random rand = new Random();
	static String sender;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		text = (EditText) this.findViewById(R.id.text);
		listView = (ListView) this.findViewById(R.id.list);
		searchBtn = (ImageButton) this.findViewById(R.id.searchBtn);
		sender = "Dorae";
		this.setTitle(sender);
		messages = new ArrayList<Message>();

		adapter = new ListApdapter(this, messages);
		listView.setAdapter(adapter);
		
		stt = new SpeakToText(Languages.VIETNAMESE,this);
		
		scan();

	}
	public void scanRobot(View v){
		scan();
	}

	public void sendMessage(View v) {
		/*
		 * Text Input
		 */
		InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		String newMessage = text.getText().toString().trim();
		if (newMessage.length() > 0) {
			text.setText("");
			addNewMessage(new Message(newMessage, true),false);
			if (Network.hasConnection(getApplicationContext())) {
				OnlineBot onlineBot = new OnlineBot(this);
				onlineBot.setRequest(newMessage);
				onlineBot.execute();
			} else {
				lostInternet();
			}

		}
	}

	public void lostInternet() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle("Mất kết nối");
		builder1.setMessage("Vui lòng kết nối internet để tiếp tục trò chuyện");
		builder1.setCancelable(true);
		builder1.setNeutralButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	

	void addNewMessage(Message m, Boolean type) {
		messages.add(m);
		adapter.notifyDataSetChanged();
		listView.setSelection(messages.size() - 1);
		if (type ) {
			robotTalk(m.getMessage());
		}
	}
	

	void robotTalk(String s) {
		final String text = s;
		final Robot mRobot = getRobot();
		if (mRobot == null) {
			Toast.makeText(getApplicationContext(), "You have no Robot \n Click Search to find one", Toast.LENGTH_LONG).show();
		
		}
		if (mRobot != null) {
			if (text == null || TextUtils.isEmpty(text)) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(MessageActivity.this, "Text is empty",
								Toast.LENGTH_LONG).show();
					}
				});
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// say text with VietNamese language
							RobotTextToSpeech.say(mRobot, text,
									RobotTextToSpeech.ROBOT_TTS_LANG_VI);
						} catch (RobotException e) {
							e.printStackTrace();
						}

					}
				}).start();
			}
		}
	}
	public void voiceText(View v) {
		/*
		 * Convert voice to text
		 */
		if (Network.hasConnection(getApplicationContext())) {
////			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
////			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
////					RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//			try {
////				startActivityForResult(intent, RESULT_SPEECH);
//
//			} catch (ActivityNotFoundException a) {
//				Toast t = Toast.makeText(getApplicationContext(),
//						"Oops! Your device doesn't support Speech to Text",
//						Toast.LENGTH_SHORT);
//				t.show();
//			}
			
			/*
			 * Using Fpt speaktotext
			 */
			new Thread (new Runnable() {

				@Override
				public void run() {
					stt.recognize(1000, 5000);
				}
				
			}).start();
		} else {
			lostInternet();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				addNewMessage(new Message(text.get(0), true),false);
				OnlineBot onlineBot = new OnlineBot(this);
				onlineBot.setRequest(text.get(0));
				onlineBot.execute();
			}
			break;
		}
		}
	}
	private ProgressDialog progressDialog = null;

	protected void showProgress(final String message) {
		// Log.d(TAG, "showProgress('" +message+ "')");
		runOnUiThread(new Runnable() {
			public void run() {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(MessageActivity.this);
				}
				// no title
				if (message != null) {
					progressDialog.setMessage(message);
				}
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}
		});
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void cancelProgress() {
		// Log.d(TAG, "cancelProgress()");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog != null) {
					// progressDialog.cancel();
					progressDialog.dismiss();
				}
			}
		});
	}
	@Override
	public void onWaiting() {
		showProgress("Waiting sound...");
	}

	@Override
	public void onRecording() {
		showProgress("Recording...");
	}

	@Override
	public void onError(Exception ex) {
		setText("on error:" + ex.getMessage());
		cancelProgress();
	}

	@Override
	public void onTimeout() {
		setText("on timeout \n");
		cancelProgress();
	}

	private void setText(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				addNewMessage(new Message(text,true), false);
			}
		});
	}

	@Override
	public void onProcessing() {
		showProgress("Processing...");

	}

	@Override
	public void onResult(Result result) {
		
		Alternative[] res = result.result.clone();
		Transcript[] tra = res[0].alternative.clone();
		String text = tra[0].transcript;
		setText(text);
		
		OnlineBot onlineBot = new OnlineBot(this);
		onlineBot.setRequest(text);
		onlineBot.execute();
		cancelProgress();
	}

	@Override
	public void onStopped() {
		setText("On stopped");
		cancelProgress();
	}

}