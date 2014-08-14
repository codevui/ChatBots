package team68.chatbots.activity;

import team68.chatbots.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fpt.robot.Robot;
import com.fpt.robot.app.RobotActivity;

public class CourseActivity extends RobotActivity {

	ListView listCourse;
	ArrayAdapter<String> adapter;
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
	public void startLearn(View v){
		Intent intentSlide = new Intent(getApplicationContext(),SlideActivity.class);
	//	intentSlide.putExtra("robot",mRobot);
		startActivity(intentSlide);
	}

}
