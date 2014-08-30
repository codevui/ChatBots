package team68.chatbots.activity;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import team68.chatbots.R;
import team68.chatbots.model.entity.PlanAnswer;
import team68.chatbots.model.entity.Question;
import team68.chatbots.model.sqlite.DatabaseHelper;
import team68.chatbots.model.sqlite.PlanAnswerDbSqlite;
import team68.chatbots.model.sqlite.QuestionDbSqlite;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.fpt.lib.asr.Languages;
import com.fpt.lib.asr.Result;
import com.fpt.lib.asr.SpeakToText;
import com.fpt.lib.asr.SpeakToTextListener;
import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.tts.RobotTextToSpeech;

public class TestActivity extends RobotActivity implements SpeakToTextListener  {

	List<Question> listQuestion;
	int idQuestion;
	int courseId;
	int qId;
	int numberQuestion;
	int result;
	SpeakToText stt;

	List<PlanAnswer> listPlanAnswer;
	static Random rand = new Random();

	ViewFlipper viewFlipper;
	int[] listAnswer = { 0, 1, 2 };
	int[] listNumberAnswer = { 0, 2, 3 };
	Button planA1, planA2, planB1, planB2, planC1, planC2, planD1, planD2;
	int type = 1;
	int questionId = 1;
	Context context;
	DatabaseHelper myDbHelper;
	SQLiteDatabase myDatabase;
	ImageView imgQuestion;
	TextView txQuestion, txQuestion2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);

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
		Bundle bundle = getIntent().getExtras();
		courseId = bundle.getInt("CourseId");
		getListQuestionByCourse(courseId);
		viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper01);
		imgQuestion = (ImageView) findViewById(R.id.imgQuestion);
		txQuestion = (TextView) findViewById(R.id.txQuestion);
		txQuestion2 = (TextView) findViewById(R.id.txQuestion2);
		setViewQuestion(0);
		stt = new SpeakToText(Languages.VIETNAMESE, this);
		stt.setApiKey("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
	}

	private void getListQuestionByCourse(int courseId2) {
		QuestionDbSqlite qSql = new QuestionDbSqlite(context, myDatabase);
		if (courseId2 == -1) {
			listQuestion = qSql.getListQuestion();
		} else {
			listQuestion = qSql.getListQuestionByCourseId(courseId2);
		}
		numberQuestion = Math.min(5, listQuestion.size());

	}

	public void nextQuestion() {
		if (qId < numberQuestion - 1) {
			setViewQuestion(qId + 1);
		} else {
			goHome();
		}

	}

	private void setViewQuestion(int id) {
		qId = id;
		Question question = listQuestion.get(id);
		PlanAnswerDbSqlite pAnswer = new PlanAnswerDbSqlite(context, myDatabase);
		listPlanAnswer = pAnswer.getAnswerByQuestionId(listQuestion.get(id)
				.getId());
		String talk = question.getContent();
		if (question.getType() == 1) {
			setContentView1();
			String imgName = question.getImage();
			int imgId = getResources().getIdentifier(imgName, "drawable",
					getPackageName());
			imgQuestion.setImageResource(imgId);
			txQuestion.setText(question.getContent());

			if (listPlanAnswer.size() > 0) {
				planA1.setEnabled(true);
				planA1.setText(listPlanAnswer.get(0).getContent());
				talk += " " + listPlanAnswer.get(0).getContent();
				// robotTalk(listPlanAnswer.get(0).getContent());
				if (listPlanAnswer.get(0).getIsTrue())
					result = 0;
			}
			if (listPlanAnswer.size() > 1) {
				planB1.setEnabled(true);
				planB1.setText(listPlanAnswer.get(1).getContent());
				talk += " " + listPlanAnswer.get(1).getContent();
				// robotTalk(listPlanAnswer.get(1).getContent());
				if (listPlanAnswer.get(1).getIsTrue())
					result = 1;
			}
			if (listPlanAnswer.size() > 2) {
				planC1.setEnabled(true);
				planC1.setText(listPlanAnswer.get(2).getContent());
				talk += " " + listPlanAnswer.get(2).getContent();
				// robotTalk(listPlanAnswer.get(2).getContent());
				if (listPlanAnswer.get(2).getIsTrue())
					result = 2;
			}
			if (listPlanAnswer.size() > 3) {
				planD1.setEnabled(true);
				planD1.setText(listPlanAnswer.get(3).getContent());
				talk += " " + listPlanAnswer.get(3).getContent();
				// robotTalk(listPlanAnswer.get(3).getContent());
				if (listPlanAnswer.get(3).getIsTrue())
					result = 3;
			}
		} else {
			setContentView2();
			txQuestion2.setText(question.getContent());

			if (listPlanAnswer.size() > 0) {
				planA2.setEnabled(true);
				planA2.setText(listPlanAnswer.get(0).getContent());
				talk += " " + listPlanAnswer.get(0).getContent();
				// robotTalk(listPlanAnswer.get(0).getContent());
				if (listPlanAnswer.get(0).getIsTrue())
					result = 0;
			}
			if (listPlanAnswer.size() > 1) {
				planB2.setEnabled(true);
				planB2.setText(listPlanAnswer.get(1).getContent());
				talk += " " + listPlanAnswer.get(1).getContent();
				// robotTalk(listPlanAnswer.get(1).getContent());
				if (listPlanAnswer.get(1).getIsTrue())
					result = 1;
			}
			if (listPlanAnswer.size() > 2) {
				planC2.setEnabled(true);
				planC2.setText(listPlanAnswer.get(2).getContent());
				talk += " " + listPlanAnswer.get(2).getContent();
				// robotTalk(listPlanAnswer.get(2).getContent());
				if (listPlanAnswer.get(2).getIsTrue())
					result = 2;
			}
			if (listPlanAnswer.size() > 3) {
				planD2.setEnabled(true);
				planD2.setText(listPlanAnswer.get(3).getContent());
				talk += " " + listPlanAnswer.get(3).getContent();
				// robotTalk(listPlanAnswer.get(3).getContent());
				if (listPlanAnswer.get(3).getIsTrue())
					result = 3;
			}
		}
		robotTalk(talk);
	}

	public void setContentView1() {
		viewFlipper.setDisplayedChild(viewFlipper
				.indexOfChild(findViewById(R.id.view1)));
		planA1 = (Button) findViewById(R.id.planA1);

		planB1 = (Button) findViewById(R.id.planB1);
		planC1 = (Button) findViewById(R.id.planC1);
		planD1 = (Button) findViewById(R.id.planD1);
		planA1.setEnabled(false);
		planB1.setEnabled(false);
		planC1.setEnabled(false);
		planD1.setEnabled(false);
	}

	public void setContentView2() {
		viewFlipper.setDisplayedChild(viewFlipper
				.indexOfChild(findViewById(R.id.view2)));
		planA2 = (Button) findViewById(R.id.planA2);
		planB2 = (Button) findViewById(R.id.planB2);
		planC2 = (Button) findViewById(R.id.planC2);
		planD2 = (Button) findViewById(R.id.planD2);

		planA2.setEnabled(false);
		planB2.setEnabled(false);
		planC2.setEnabled(false);
		planD2.setEnabled(false);
	}

	public void planA(View v) {
		if (result == 0) {
			rightAnswer();
		} else
			wrongAnswer();

	}

	public void planB(View v) {
		if (result == 1) {
			rightAnswer();
		} else
			wrongAnswer();
	}

	public void planC(View v) {
		if (result == 2) {
			rightAnswer();
		} else
			wrongAnswer();
	}

	public void planD(View v) {
		if (result == 3) {
			rightAnswer();
		} else
			wrongAnswer();
	}

	public void rightAnswer() {
		String s =  "Chúc mừng ! Em đã trả lời đúng !";
		if (qId == numberQuestion -1 ) 
			s = s+ "Em đã hoàn thành phần thi của mình !";
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
						Toast.makeText(TestActivity.this, "Text is empty",
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
							nextQuestionUI();
						} catch (RobotException e) {
							e.printStackTrace();
						}

					}
				}).start();
				handMotionBehavior("random");
			}
		}
	}

	public void wrongAnswer() {
		robotTalk("Em trả lời sai rồi, Thử lại nhé !");
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
						Toast.makeText(TestActivity.this, "Text is empty",
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
				Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void nextQuestionUI() {
			runOnUiThread(new Runnable() {
				public void run() {
					nextQuestion();
				}
			});
	}

	@Override
	public void onError(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProcessing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRecording() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResult(Result arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWaiting() {
		// TODO Auto-generated method stub
		
	}
}
