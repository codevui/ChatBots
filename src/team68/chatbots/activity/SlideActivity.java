package team68.chatbots.activity;

import java.util.Random;

import team68.chatbots.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.tts.RobotTextToSpeech;

public class SlideActivity extends RobotActivity {
	private int courseId;
	private int slideId;
	private int numberSlide;
	private String[] listDetailSlide = {
			"Xin chào ! Tôi là NAO",
			"Hôm nay, tớ sẽ giới thiệu cho bạn \n về đèn giao thông",
			"Đèn giao thông được dựng ở ngã tư đường",
			"Đèn có 3 màu : xanh , đỏ , vàng , sáng lần lượt",
			"Đèn xanh, xe cộ được phép đi",
			"Đèn đỏ , xe cộ phải dừng lại",
			"Đèn vàng , xe cộ giảm tốc độ",
	};
	private String[] listPresentSlide=  {
			"Xin chào ! Tôi là NAO",
			"Hôm nay, tớ sẽ giới thiệu cho bạn \n về đèn giao thông",
			"Đèn giao thông được dựng ở ngã tư đường",
			"Đèn có 3 màu : xanh , đỏ , vàng , sáng lần lượt",
			"Đèn xanh, xe cộ được phép đi",
			"Đèn đỏ , xe cộ phải dừng lại",
			"Đèn vàng , xe cộ giảm tốc độ",
	};
	static Random rand = new Random();
	ImageView imgSlide;
	TextView txSlide;
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_activity);
		imgSlide = (ImageView) findViewById(R.id.imgSlide);
		txSlide = (TextView) findViewById(R.id.txSlide);
		numberSlide = 7;
		courseId = 1;
		context = getApplicationContext();
		setContentSlide(1);
	}
	
	public void setContentSlide(int id){
		slideId = id;
		String imgName = "course" + courseId + "slide" + id;
		int imgId = getResources().getIdentifier(imgName, "drawable", getPackageName());
		imgSlide.setImageResource(imgId);
		txSlide.setText(listDetailSlide[id]);
		
		robotTalk(listPresentSlide[id]);
		
		
	}
	
	public void nextSlide(View v){
		if (slideId < numberSlide) {
			setContentSlide(slideId + 1);
		}
		
	}
	public void prevSlide(View v){
		if (slideId > 1){
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
	public void makeToast(final String m) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, m, Toast.LENGTH_LONG).show();
			}
		});
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
}
