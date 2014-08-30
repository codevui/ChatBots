package team68.chatbots.activity;

import java.util.ArrayList;
import java.util.Random;

import team68.chatbots.R;
import team68.chatbots.controller.Network;
import team68.chatbots.controller.OnlineBot;
import team68.chatbots.controller.RobotController;
import team68.chatbots.model.Defination;
import team68.chatbots.model.ListApdapter;
import team68.chatbots.model.Message;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.fpt.robot.binder.RobotValue;
import com.fpt.robot.event.RobotEvent;
import com.fpt.robot.event.RobotEventSubscriber;
import com.fpt.robot.event.RobotEventSubscriber.OnRobotEventListener;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.motion.RobotPosture;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.robot.vision.RobotCamera;

public class MessageActivity extends RobotActivity implements
		SpeakToTextListener, OnRobotEventListener {

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
	Context context;
	RobotController robotController;
	int selectedCameraIndex = 1;
	Defination defination;
	private RobotEventSubscriber mEventSubscriber = null;
	private boolean SET_PICTURE_FILE_NAME = true;
	private RobotCamera[] mCamera = new RobotCamera[2];
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = getApplicationContext();
		text = (EditText) this.findViewById(R.id.text);
		listView = (ListView) this.findViewById(R.id.list);
		searchBtn = (ImageButton) this.findViewById(R.id.searchBtn);
		sender = "Dorae";
		this.setTitle(sender);
		messages = new ArrayList<Message>();

		adapter = new ListApdapter(this, messages);
		listView.setAdapter(adapter);

		stt = new SpeakToText(Languages.VIETNAMESE, this);
		robotController = new RobotController(this);
		defination = new Defination();
		
		scan();
		
		mCamera[0] = RobotCamera.getCamera(getRobot(),
				RobotCamera.CAMERA_TOP);
		mCamera[1] = RobotCamera.getCamera(getRobot(),
				RobotCamera.CAMERA_BOTTOM);
		
	}
	
	public void startScan(View v) {
		Intent intentImageReg = new Intent(getApplicationContext(),ImageRecognize.class);
		startActivity(intentImageReg);
	}
	public void scanRobot(View v) {
		makeToast("Scan : Started");
		scan();
	}
	public void subscribeEvent(){
		subscribeEvent(RobotEvent.MIDDLE_TACTIL_TOUCHED);
		subscribeEvent(RobotEvent.FRONT_TACTIL_TOUCHED);
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
			makeToast(newMessage);
			addNewMessage(new Message(newMessage, true), false);
			processText(newMessage);

		}
	}

	public void processText(String text) {
		if (text.trim().equals(Defination.SITDOWN)) 
				sitdown();
//		switch (text) {
//		case Defination.SITDOWN:
//			sitdown();
//			break;
//		case "đứng lên":
//			standup();
//			break;
//		case "quay sang trái":
//			turnLeft();
//			break;
//		case "quay sang phải":
//			turnRight();
//			break;
//		case "bước tới":
//			stepForward();
//			break;
//		case "bước lui":
//			stepBackward();
//			break;
//		case "dừng lại":
//			stopWalk();
//			break;
//		case "đăng ký":
//			subscribeEvent();
//			break;
//		case "chụp ảnh":
//			takePicture();
//			break;
//		default:
//			robotAnswer(text);
//
//		}
	}

	public void robotAnswer(String text) {
		if (Network.hasConnection(getApplicationContext())) {
			OnlineBot onlineBot = new OnlineBot(this);
			onlineBot.setRequest(text);
			onlineBot.execute();
		} else {
			lostInternet();
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

	public void addNewMessage(Message m, Boolean type) {
		messages.add(m);
		adapter.notifyDataSetChanged();
		listView.setSelection(messages.size() - 1);
		if (type) {
			robotTalk(m.getMessage());
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
				handMotionBehavior("random");
				// new Thread(new Runnable() {
				// @Override
				// public void run() {
				// try {
				// // say text with VietNamese language
				//
				// String[] list = RobotGesture.getGestureList(mRobot);
				// Random rand = new Random();
				//
				// RobotGesture.runGesture(mRobot,
				// list[rand.nextInt(list.length - 1)]);
				//
				// } catch (RobotException e) {
				// e.printStackTrace();
				// }
				//
				// }
				// }).start();
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

	public void voiceText(View v) {
		/*
		 * Convert voice to text
		 */
		if (Network.hasConnection(getApplicationContext())) {
			// // Intent intent = new
			// Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			// // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
			// // RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
			// try {
			// // startActivityForResult(intent, RESULT_SPEECH);
			//
			// } catch (ActivityNotFoundException a) {
			// Toast t = Toast.makeText(getApplicationContext(),
			// "Oops! Your device doesn't support Speech to Text",
			// Toast.LENGTH_SHORT);
			// t.show();
			// }

			/*
			 * Using Fpt speaktotext
			 */
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				addNewMessage(new Message(text.get(0), true), false);
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
		makeToast("Tôi không nghe thấy gì !");
		cancelProgress();
	}

	@Override
	public void onTimeout() {
		makeToast("Tôi không nghe thấy gì !");
		cancelProgress();
	}

	private void setText(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				addNewMessage(new Message(text, true), false);
			}
		});
	}

	public void makeToast(final String m) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, m, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onProcessing() {
		showProgress("Processing...");

	}

	@Override
	public void onResult(Result result) {
		if (result != null) {
			Alternative[] res = result.result.clone();
			Transcript[] tra = res[0].alternative.clone();
			String text = tra[0].transcript;

			if (text == null | text.equals(""))
				makeToast("Tôi không nghe thấy gì !");
			setText(text);
			processText(text);
//			OnlineBot onlineBot = new OnlineBot(this);
//			onlineBot.setRequest(text);
//			onlineBot.execute();
			cancelProgress();
		} else {
			setText("null");
			// Toast.makeText(context, "Tôi không nghe thấy gì !",
			// Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onStopped() {
		makeToast("Tôi nghỉ đây !");
		cancelProgress();
	}

	@Override
	public void onRobotEvent(String arg0, RobotValue arg1) {
		//makeToast(arg0);
		switch (arg0) {
		case RobotEvent.MIDDLE_TACTIL_TOUCHED :
			makeToast("Event : "+ RobotEvent.MIDDLE_TACTIL_TOUCHED);
			break;
		case RobotEvent.FRONT_TACTIL_TOUCHED :
			makeToast("Event : "+ RobotEvent.FRONT_TACTIL_TOUCHED);
			break;
		}
		

	}

	public void sitdown() {
		/*
		 * Make robot goto posture
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (getRobot() == null) {
					makeToast("Plz connect to robot");
				} else {
					boolean result = false;
					try {
						showProgress("Sitdown...");
						// go to crouch state with speed is 0.5 (max speed is
						// 1.0)
						result = RobotPosture.goToPosture(getRobot(),
								"Sitdown", 0.5f);
						// set motor off
						RobotMotionStiffnessController.rest(getRobot());
						
					} catch (RobotException e) {
						cancelProgress();
						makeToast("Crouch failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
					cancelProgress();
					if (result) {
						makeToast("Sitdown successfully");
					} else {
						makeToast("Sitdown failed");
					}
				}
			}
		}).start();
	}

	public void standup() {
		/*
		 * Make robot standup
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (getRobot() == null) {
					makeToast("Plz connect to robot");
				} else {
					boolean result = false;
					try {
						// RobotMotionStiffnessController.wakeUp(getRobot());
						// RobotMotionStiffnessController.rest(getRobot());
						showProgress("Standup...");
						// go to crouch state with speed is 0.5 (max speed is
						// 1.0)
						result = RobotMotionAction.standUp(getRobot(), 0.5f);

					} catch (RobotException e) {
						cancelProgress();
						makeToast("Standup failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
					cancelProgress();
					if (result) {
						makeToast("Standup successfully");
					} else {
						makeToast("Standup failed");
					}
				}
			}
		}).start();
	}
	public void turnLeft() {
		/*
		 * Make robot turnleft
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (getRobot() == null) {
					makeToast("Please connect with robot first");
				} else {
					try {
						// Robot turn left and only stop if stopWalk is called
						RobotMotionAction.turnLeft(getRobot(), 0.5f);
					} catch (RobotException e) {
						makeToast("Turn Left failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	public void turnRight() {
		/*
		 * Make robot turnleft
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (getRobot() == null) {
					makeToast("Please connect with robot first");
				} else {
					try {
						// Robot turn left and only stop if stopWalk is called
						RobotMotionAction.turnRight(getRobot(), 0.5f);
					} catch (RobotException e) {
						makeToast("Turn Right failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	public void stepForward() {
		/*
		 * Make robot turnleft
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (getRobot() == null) {
					makeToast("Please connect with robot first");
				} else {
					try {
						// Robot turn left and only stop if stopWalk is called
						RobotMotionAction.stepForward(getRobot(), 0.5f);
					} catch (RobotException e) {
						makeToast("Turn Left failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	public void stepBackward() {
		/*
		 * Make robot turnleft
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (getRobot() == null) {
					makeToast("Please connect with robot first");
				} else {
					try {
						// Robot turn left and only stop if stopWalk is called
						RobotMotionAction.stepBackward(getRobot(), 0.5f);
					} catch (RobotException e) {
						makeToast("Step Backward failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	public void stopWalk() {
		/*
		 * Make robot stopWalk
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (getRobot() == null) {
					makeToast("Please connect with robot first");
				} else {
					try {
						// Robot turn left and only stop if stopWalk is called
						RobotMotionAction.stopWalk(getRobot());
					} catch (RobotException e) {
						makeToast("Stop walk failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();
	}
	protected void subscribeEvent(final String eventName) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					makeToast("Please connect with robot first");
				} else {
					// initialize RobotEventSubscriber
					if (mEventSubscriber == null) {
						mEventSubscriber = new RobotEventSubscriber(getRobot(),
								MessageActivity.this);
					}

					boolean result;
					try {
						// start subscribe
						result = mEventSubscriber.subscribe(eventName,
								RobotEvent.TYPE_SYSTEM);
					} catch (final RobotException e) {
						e.printStackTrace();
						makeToast("subscribe event" + eventName + " failed! "
								+ e.getMessage());
						return;
					}
					if (result) {
						makeToast("subscribe event '" + eventName
								+ "' sucessfully!");
					} else {
						makeToast("subscribe event '" + eventName + "' failed!");
					}
				}
			}
		}).start();
	}
	protected void takePicture() {
		selectedCameraIndex = 0;
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				// if take more picture, name of pictures which are saved in
				// sdcard will be set to default
				
				String picture = null;
				showProgress("taking picture...");
				// default resolution
				int resolution = RobotCamera.PICTURE_RESOLUTION_VGA;
				// default picture format (can change to JPG,PNG)
				String pictureFormat = RobotCamera.PICTURE_FORMAT_BMP;
				// get selected resolution
//				String resolutionStr = spTakePictureResolution
//						.getSelectedItem().toString();
			//	int resolution = RobotCamera.PICTURE_RESOLUTION_VGA;
//				if (resolutionStr.equals("QQVGA")) {
//					resolution = RobotCamera.PICTURE_RESOLUTION_QQVGA;
//				} else if (resolutionStr.equals("QVGA")) {
//					resolution = RobotCamera.PICTURE_RESOLUTION_QVGA;
//				} else if (resolutionStr.equals("VGA")) {
//					resolution = RobotCamera.PICTURE_RESOLUTION_VGA;
//				} else if (resolutionStr.equals("4VGA")) {
//					resolution = RobotCamera.PICTURE_RESOLUTION_4VGA;
//				} else {
//					makeToast("Invalid selected resolution!");
//					return;
//				}

				try {
					if (SET_PICTURE_FILE_NAME) {
						// default name to save picture
						String savedPath = Environment
								.getExternalStorageDirectory().getPath()
								+ "/picture";
						if (pictureFormat == RobotCamera.PICTURE_FORMAT_JPG) {
							savedPath = savedPath + ".jpg";
						} else if (pictureFormat == RobotCamera.PICTURE_FORMAT_PNG) {
							savedPath = savedPath + ".png";
						} else if (pictureFormat == RobotCamera.PICTURE_FORMAT_BMP) {
							savedPath = savedPath + ".bmp";
						}
						boolean result = false;
						result = mCamera[selectedCameraIndex].takePicture(
								resolution, pictureFormat, savedPath);
//						if (enableAction) {
//							// take picture with action
//							result = mCamera[selectedCameraIndex]
//									.takePictureWithAction(resolution,
//											pictureFormat, savedPath);
//						} else {
//							// take picture no action
//							result = mCamera[selectedCameraIndex].takePicture(
//									resolution, pictureFormat, savedPath);
//						}
						if (result) {
							picture = savedPath;
						}
					}
//					 else {
//						// take picture with specified resolution and format
//						if (enableAction) {
//							if (morePictures) {
//								// get number of pictures will be taken
//								int numberOfPictures = spNumberOfPictures
//										.getSelectedItemPosition() + 1;
//								// time delay for each picture
//								long delay = 1000;
//								// take more picture with action
//								String[] path = mCamera[selectedCameraIndex]
//										.takePicturesWithAction(resolution,
//												pictureFormat,
//												numberOfPictures, delay);
//								if (path != null && path[0] != null) {
//									picture = path[0];
//								}
//							} else {
//								// take 1 picture with action
//								picture = mCamera[selectedCameraIndex]
//										.takePictureWithAction(resolution,
//												pictureFormat);
//							}
//						} else {
//							if (morePictures) {
//								// get number of pictures will be taken
//								int numberOfPictures = spNumberOfPictures
//										.getSelectedItemPosition() + 1;
//								// time delay for each picture
//								long delay = 1000;
//								// taken more pictures no action
//								String[] path = mCamera[selectedCameraIndex]
//										.takePictures(resolution,
//												pictureFormat,
//												numberOfPictures, delay);
//								if (path != null && path[0] != null) {
//									picture = path[0];
//								}
//							} else {
//								// take one picture no action
//								picture = mCamera[selectedCameraIndex]
//										.takePicture(resolution, pictureFormat);
//							}
//						}
//					}
				} catch (RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast(
							"Take picture : " +
							"Take picture from camera failed! "
									+ e.getMessage());
					return;
				}
				cancelProgress();
				if (picture == null || picture.isEmpty()) {
				//	Log.e(TAG, "can not take picture!");
					makeToast(	"Take picture : " +
							"Cannot take picture from camera!");
					return;
				} else {
				
					makeToast(	"Take picture : "  + picture + "!");
					//displayPicture(picture);
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
}