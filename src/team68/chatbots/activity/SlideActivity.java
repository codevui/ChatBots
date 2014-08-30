package team68.chatbots.activity;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import team68.chatbots.R;
import team68.chatbots.controller.NLPCenter;
import team68.chatbots.controller.Network;
import team68.chatbots.model.dao.db.CourseDb;
import team68.chatbots.model.dao.db.SlideDb;
import team68.chatbots.model.entity.Slide;
import team68.chatbots.model.sqlite.DatabaseHelper;
import team68.chatbots.model.sqlite.SlideDbSqlite;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.lib.asr.Alternative;
import com.fpt.lib.asr.Languages;
import com.fpt.lib.asr.Result;
import com.fpt.lib.asr.SpeakToText;
import com.fpt.lib.asr.SpeakToTextListener;
import com.fpt.lib.asr.Transcript;
import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.tts.RobotTextToSpeech;

public class SlideActivity extends RobotActivity implements SpeakToTextListener{
	private int courseId;
	private int slideId;
	private int numberSlide;
	CourseDb coursedb;
	SlideDb slideDb;
	List<Slide> listSlide;
	DatabaseHelper myDbHelper;
	SQLiteDatabase myDatabase;
	SpeakToText stt;
	static Random rand = new Random();
	ImageView imgSlide;
	TextView txSlide;
	Context context;
	NLPCenter nlpCenter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_activity);
		imgSlide = (ImageView) findViewById(R.id.imgSlide);
		txSlide = (TextView) findViewById(R.id.txSlide);
		
		Bundle bundle = getIntent().getExtras();
		
		courseId = bundle.getInt("CourseId");
		numberSlide = bundle.getInt("NumberSlide");
		
		context = getApplicationContext();
		myDbHelper = new DatabaseHelper(context);
		try {
			myDbHelper.createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myDbHelper.openDatabase();
		myDatabase = myDbHelper.getMyDatabase();
		SlideDbSqlite slidesql = new SlideDbSqlite(context,myDatabase);
		listSlide = slidesql.getSlideByCourseId(courseId);
		myDatabase.close();
		setContentSlide(0);
		storeData();
		stt = new SpeakToText(Languages.VIETNAMESE, this);
		stt.setApiKey("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
		nlpCenter = new NLPCenter(context,this);
	}
	
	public void setContentSlide(int id){
		Slide slide = listSlide.get(id);
		String imgName = slide.getImage();
		int imgId = getResources().getIdentifier(imgName, "drawable", getPackageName());
		imgSlide.setImageResource(imgId);
		
		txSlide.setText(slide.getTextPreview());
		
		robotTalk(slide.getTextToSay());
		slideId = id;
		
	}
	
	public void nextSlide(View v){
		if (slideId < numberSlide-1) {
			setContentSlide(slideId + 1);
		}
		else {
			Intent intent = new Intent(this, TestActivity.class);
			intent.putExtra("CourseId", courseId);
			startActivity(intent);
		}
		
	}
	public void prevSlide(View v){
		if (slideId > 0){
			setContentSlide(slideId -1);
		}
	}
	void robotTalk(String s) {
		final String text = s;
		final Robot mRobot = getRobot();
		if (mRobot == null) {
			Toast.makeText(getApplicationContext(),
					"You have no Robot \n Click Search to find one",
					Toast.LENGTH_LONG).show();

		}
		if (mRobot != null) {
			if (text == null || TextUtils.isEmpty(text)) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(SlideActivity.this, "Text is empty",
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
				handMotionBehavior("random");
			}
		}
	}
	public void handMotionBehavior(final String type) {
		final Robot mRobot = getRobot();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					String[] list = RobotGesture.getGestureList(mRobot);
					makeToast(list.toString());
					switch (type) {
					case "random":
						int i = rand.nextInt(10) + 1;

						RobotGesture.runGesture(mRobot, "HandMotionBehavior"
								+ i);

						break;
					default:
						break;

					}
				} catch (RobotException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void goHome(){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.scan:
	            scan();
	            return true;
	        case R.id.home:
	            goHome();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void storeData(){
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		Editor editor = pref.edit();
		editor.putInt("CourseId", courseId);
		editor.commit();
	}
public void voiceTextSlide(View v){
		
		if (Network.hasConnection(getApplicationContext())) {
			//Using FPT ASR
			new Thread(new Runnable() {

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
	public void onProcessing() {
		showProgress("Đang xử lý...");

	}

	@Override
	public void onResult(Result result) {
		if (result != null) {
			Alternative[] res = result.result.clone();
			Transcript[] tra = res[0].alternative.clone();
			String text = tra[0].transcript;

			if (text == null | text.equals(""))
				makeToast("Tôi không nghe thấy gì !"); 
			else {
				nlpCenter.solveText(text);
			}
			cancelProgress();
		} else {
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

	private ProgressDialog progressDialog = null;

	protected void showProgress(final String message) {
		// Log.d(TAG, "showProgress('" +message+ "')");
		runOnUiThread(new Runnable() {
			public void run() {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(SlideActivity.this);
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
		showProgress("Đang lưu...");
	}

	@Override
	public void onError(Exception ex) {
		makeToast("Tôi không nghe thấy gì !");
		cancelProgress();
	}

	@Override
	public void onTimeout() {
		makeToast("Tôi không nghe thấy gì !");
		cancelProgress();
	}

	@Override
	public void onStopped() {
		makeToast("Tôi nghỉ đây !");
		cancelProgress();
	}
	public void makeToast(final String m) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, m, Toast.LENGTH_LONG).show();
			}
		});
	}
}
