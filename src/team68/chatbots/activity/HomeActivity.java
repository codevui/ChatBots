package team68.chatbots.activity;

import team68.chatbots.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fpt.robot.app.RobotActivity;

public class HomeActivity extends RobotActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
	}

	public void startCourse(View v) {
		Intent intent = new Intent(this, CourseActivity.class);
		startActivity(intent);
	}

	public void startTest(View v) {
		Intent intent = new Intent(this, TestActivity.class);
		startActivity(intent);
	}

	public void startScan(View v) {
		scan();
	}
	public void startControll(View v) {
		Intent intent = new Intent(this, RobotControllActivity.class);
		startActivity(intent);
	}

	public void startAssist(View v) {
		Intent intent = new Intent(this, MessageActivity.class);
		startActivity(intent);
	}

	public void quit(View v) {

	}
}
