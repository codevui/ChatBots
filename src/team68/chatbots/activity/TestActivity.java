package team68.chatbots.activity;

import team68.chatbots.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.fpt.robot.app.RobotActivity;

public class TestActivity extends RobotActivity {
	ViewFlipper viewFlipper;
	int[] listAnswer = { 0, 1, 2 };
	int[] listNumberAnswer = { 0, 2, 3 };
	String[] listQuestion = {
			"gioi thieu",
			"Bạn nhỏ trong hình qua đường có đúng không ?",
			"Khi tham gia giao thông, trường hợp nào dưới đây là \nkhông an toàn, gây nguy hiểm ?" };
	String[][] listStringAnswer = {
			{ "", "", "", "", "" },
			{ "", "A. Đúng", "B.Sai", "", "" },
			{ "", "A.Đi qua đường cùng người lớn.",
					"B.Không đội mũ bảo hiểm khi ngồi trên xe mô tô, xe máy.",
					"C.Đi xe đạp chở 1 người ngồi sau", "" }, };
	Button planA1, planA2, planB1, planB2, planC1, planC2, planD1, planD2;
	int type = 1;
	int questionId =1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper01);
		
		if (type == 1) setContentView1();
		else setContentView2();
	}

	public void setContentView1() {
		viewFlipper.setDisplayedChild(viewFlipper
				.indexOfChild(findViewById(R.id.view1)));
		planA1 = (Button) findViewById(R.id.planA1);
		planB1 = (Button) findViewById(R.id.planB1);
		planC1 = (Button) findViewById(R.id.planC1);
		planD1 = (Button) findViewById(R.id.planD1);
	}

	public void setContentView2() {
		viewFlipper.setDisplayedChild(viewFlipper
				.indexOfChild(findViewById(R.id.view2)));
		planA2 = (Button) findViewById(R.id.planA2);
		planB2 = (Button) findViewById(R.id.planB2);
		planC2 = (Button) findViewById(R.id.planC2);
		planD2 = (Button) findViewById(R.id.planD2);
	}

	public void planA(View v) {

	}

	public void planB(View v) {

	}

	public void planC(View v) {

	}

	public void planD(View v) {

	}

	public void nextQuestion() {

	}
	public void rightAnswer() {
		
	}
	public void wrongAnswer() {
		
	}

	public void goHome() {
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
}
