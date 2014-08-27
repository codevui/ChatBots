package team68.chatbots.activity;

import java.io.IOException;
import java.util.List;

import team68.chatbots.R;
import team68.chatbots.model.entity.Course;
import team68.chatbots.model.sqlite.CourseDbSqlite;
import team68.chatbots.model.sqlite.DatabaseHelper;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fpt.robot.Robot;
import com.fpt.robot.app.RobotActivity;

public class CourseActivity extends RobotActivity implements OnItemClickListener {

	ListView listCourse;
	ArrayAdapter<String> adapter;
	List<Course> list;
	private int courseId;
	
	String[] values = new String[] { "Bài 1 : Phương tiện giao thông",
			"Bài 2 : Đèn báo hiệu", "Bài 3 : Biển báo hiệu",
			"Bài 4 : Đi sang đường", "Bài 5 : Ngồi xe máy an toàn",
			"Baì 6 : Ngồi ôtô an toàn", "Baì 7 : Ngồi tàu hỏa an toàn",
			"Bài 8 : Đi máy bay an toàn", "Bài 9 : Đi tàu thủy an toàn",
			"Baì 6 : Ngồi ôtô an toàn", "Baì 7 : Ngồi tàu hỏa an toàn",
			"Bài 8 : Đi máy bay an toàn", "Bài 9 : Đi tàu thủy an toàn",
			"Baì 6 : Ngồi ôtô an toàn", "Baì 7 : Ngồi tàu hỏa an toàn",
			"Bài 8 : Đi máy bay an toàn", "Bài 9 : Đi tàu thủy an toàn",

	};
	Robot mRobot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_activity);
		listCourse = (ListView) findViewById(R.id.listCourse);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		listCourse.setAdapter(adapter);
		
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
	}
	public void startLearn(View v){
		courseId = 1;
		Intent intentSlide = new Intent(getApplicationContext(),SlideActivity.class);
		intentSlide.putExtra("CourseId", courseId);
		startActivity(intentSlide);
	}
	public void continueLearn(View v){
		courseId = 1;
		Intent intentSlide = new Intent(getApplicationContext(),SlideActivity.class);
		intentSlide.putExtra("CourseId", courseId);
		startActivity(intentSlide);
	}
	public void resetCourse(View V) {
		
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
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		courseId = 1;
		Intent intentSlide = new Intent(getApplicationContext(),SlideActivity.class);
		intentSlide.putExtra("CourseId", courseId);
		startActivity(intentSlide);
	}
}
