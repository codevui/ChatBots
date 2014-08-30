package team68.chatbots.activity;

import java.io.IOException;
import java.util.List;

import team68.chatbots.R;
import team68.chatbots.controller.NLPCenter;
import team68.chatbots.controller.Network;
import team68.chatbots.model.entity.Course;
import team68.chatbots.model.sqlite.CourseDbSqlite;
import team68.chatbots.model.sqlite.DatabaseHelper;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fpt.lib.asr.Alternative;
import com.fpt.lib.asr.Languages;
import com.fpt.lib.asr.Result;
import com.fpt.lib.asr.SpeakToText;
import com.fpt.lib.asr.SpeakToTextListener;
import com.fpt.lib.asr.Transcript;
import com.fpt.robot.Robot;
import com.fpt.robot.app.RobotActivity;

public class CourseActivity extends RobotActivity implements SpeakToTextListener{

	ListView listCourse;
	ArrayAdapter<String> adapter;
	List<Course> list; 
	private int courseId;
	SpeakToText stt;
	String[] values = new String[100];
	Robot mRobot;
	Context context;
	NLPCenter nlpCenter;
	EditText etCourse;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_activity);
		listCourse = (ListView) findViewById(R.id.listCourse);
		getCourseList();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		listCourse.setAdapter(adapter);
		listCourse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intentSlide = new Intent(getApplicationContext(),SlideActivity.class);
				intentSlide.putExtra("CourseId", list.get(arg2).getId());
				intentSlide.putExtra("NumberSlide",list.get(arg2).getNumberSlide());
				startActivity(intentSlide);
				
			}
		});
		stt = new SpeakToText(Languages.VIETNAMESE, this);
		stt.setApiKey("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
		context = getApplicationContext();
		nlpCenter = new NLPCenter(context,this);
	}
	
	public void getCourseList(){
		Context context = getApplicationContext();
		DatabaseHelper myDbHelper = new DatabaseHelper(context);
		try {
			myDbHelper.createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myDbHelper.openDatabase();
		SQLiteDatabase myDatabase = myDbHelper.getMyDatabase();
		CourseDbSqlite courseSql = new CourseDbSqlite(context,myDatabase);
		list = courseSql.getListCourse();
		myDatabase.close();
		for (int i = 0 ; i<=list.size()-1; i++){
			values[i] = list.get(i).getTitle();
		}
	}
	public void startLearn(View v){
		courseId = 0;
		Intent intentSlide = new Intent(getApplicationContext(),SlideActivity.class);
		intentSlide.putExtra("CourseId", list.get(courseId).getId());
		intentSlide.putExtra("NumberSlide",list.get(courseId).getNumberSlide());
		startActivity(intentSlide);
	}
	public void continueLearn(View v){
		getData();
		Intent intentSlide = new Intent(getApplicationContext(),SlideActivity.class);
		intentSlide.putExtra("CourseId", courseId);
		intentSlide.putExtra("NumberSlide",list.get(courseId-1).getNumberSlide());
		startActivity(intentSlide);
	}
	public void resetCourse(View V) {
		
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
	public void goHome(){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
	public void getData(){
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		courseId= pref.getInt("CourseId", 1);
	}

	public void voiceTextCourse(View v){
		
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
					progressDialog = new ProgressDialog(CourseActivity.this);
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
