package team68.chatbots.activity;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

import team68.chatbots.R;
import team68.chatbots.model.entity.Signs;
import team68.chatbots.model.sqlite.DatabaseHelper;
import team68.chatbots.model.sqlite.SignsDbSqlite;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.robot.vision.RobotCamera;
import com.fpt.robot.vision.RobotCameraPreview;
import com.fpt.robot.vision.RobotCameraPreviewView;

public class SignsActivity extends RobotActivity {
	
	private EditText ipAddr;
	private Button bConnect;
	private String ipAddress;
	private SocketChannel socketChannel = null;
	private RobotCameraPreviewView mCameraPreview;
	private RobotCameraPreview mRobotCameraPreview;
	private boolean SET_PICTURE_FILE_NAME = true;
	private RobotCamera[] mCamera = new RobotCamera[2];
	int selectedCameraIndex = 1;
	static Random rand = new Random();
	Context context;
	String idImageResponse;
	boolean isFinish;
	Message m = new Message();
	protected static final int MSG_ID = 0x1337;
	private Button btnImageScan;
	DatabaseHelper myDbHelper;
	SQLiteDatabase myDatabase;
	ImageView imgImageScan;
	SignsDbSqlite sSql;
	TextView stt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_activity);
		ipAddr = (EditText)findViewById(R.id.ipAddr);
		context = getApplicationContext();
		btnImageScan = (Button) findViewById(R.id.btnImageScan);
		imgImageScan = (ImageView) findViewById(R.id.imgImageScan);
		myDbHelper = new DatabaseHelper(context);
		try {
			myDbHelper.createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myDbHelper.openDatabase();
		myDatabase = myDbHelper.getMyDatabase();
		sSql = new SignsDbSqlite(context,myDatabase);
		stt = (TextView) findViewById(R.id.tvImg);
	}
	
	
	public void imageScanI(View v){
		ipAddress = ipAddr.getText().toString();
		final String filename = "picture.bmp";
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					socketChannel = SocketChannel.open();
					socketChannel.connect(new InetSocketAddress(ipAddress, 6969));
					
					// send filename, filesize
					
					long fileSize = 0;
					String PATH = Environment.getExternalStorageDirectory() + "/" + filename;
					File dir = new File(PATH);
					fileSize = dir.length();
					String buf = filename + "|" + fileSize + "|";
					socketChannel.write(ByteBuffer.wrap(buf.getBytes()));
					
					RandomAccessFile aFile     = new RandomAccessFile(PATH, "rw");
					// send file
					while (fileSize > 0) {
						//String d = System.getProperty("user.dir");
						
						byte[] bb = new byte[4096];
						int le = aFile.read(bb);
						if (le > 0) {
							socketChannel.write(ByteBuffer.wrap(bb, 0, le));
						}
						fileSize -= le;
					}
					aFile.close();
					ByteBuffer bufer = ByteBuffer.allocate(1024);
					int numRead = socketChannel.read(bufer);
					idImageResponse = new String(bufer.array(), 0, numRead);
					int id = Integer.parseInt(idImageResponse);
					displaySignInfo(id);
					socketChannel.finishConnect();
					makeToast(idImageResponse);
				} catch (IOException e) {
					e.printStackTrace();
					makeToast("Server Disconnect");	
				}
			}
		}).start();
	}
	
	public void takePicture(View v) {
		mCamera[0] = RobotCamera.getCamera(getRobot(), RobotCamera.CAMERA_TOP);
		mCamera[1] = RobotCamera.getCamera(getRobot(),
				RobotCamera.CAMERA_BOTTOM);
		selectedCameraIndex = 0;
		new Thread(new Runnable() {

			@Override
			public void run() {

				String picture = null;
				showProgress("taking picture...");

				int resolution = RobotCamera.PICTURE_RESOLUTION_VGA;

				String pictureFormat = RobotCamera.PICTURE_FORMAT_BMP;

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
						//
						if (result) {
							picture = savedPath;
						}
					}
					//
				} catch (RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("Take picture : "
							+ "Take picture from camera failed! "
							+ e.getMessage());
					return;
				}
				cancelProgress();
				if (picture == null || picture.isEmpty()) {
					// Log.e(TAG, "can not take picture!");
					makeToast("Take picture : "
							+ "Cannot take picture from camera!");
					return;
				} else {

					makeToast("Take picture : " + picture + "!");
					displayPicture(picture);
				}
			}
		}).start();
	}
	public void displayPicture(final String picture) {
		final String picturePath = picture;
		// decode to bitmap
		final Bitmap bm = BitmapFactory.decodeFile(picturePath);
		if (bm != null) {
			runOnUiThread(new Runnable() {
				public void run() {
					// display image to image view
					imgImageScan.setImageBitmap(bm);
				}
			});
		} else {
			makeToast("Picture saved to " + picturePath + "!");
		}
	}
	public void displaySignInfo(final int id) {
		
		// decode to bitmap
			runOnUiThread(new Runnable() {
				public void run() {
					if (id == 0) {
						stt.setText("Không nhận ra biển báo");
					} else {
						Signs s = sSql.getById(id);
						stt.setText(s.getTitle());
						robotTalk(s.getText());
					}
					
					
				}
			});
	}
		
	public void startPreview(View v){
		// get robot camera preview
				if (getRobot() == null) {
					//showInfoDialogFromThread(TAG, "Please connect with robot first");
				} else {
					mRobotCameraPreview = RobotCamera.getCameraPreview(getRobot(),
							0);
				}
				// mRobotCameraPreview.setPreviewDisplay(mCameraPreview.getHolder());
				// set view to display on the screen
				mRobotCameraPreview.setPreviewDisplay(mCameraPreview);
				// set quality for image get from camera
				mRobotCameraPreview.setQuality(mRobotCameraPreview.QUALITY_MEDIUM);
				// get quality of robot camera preview
				//int cameraQuality = mRobotCameraPreview.getQuality();
				//makeToast("Camera quality: " + cameraQuality);
				// set speed
				mRobotCameraPreview.setSpeed(mRobotCameraPreview.SPEED_MEDIUM);
				//int cameraSpeed = mRobotCameraPreview.getSpeed();
				//makeToast("Camera speed: " + cameraSpeed);

				new Thread(new Runnable() {
					@Override
					public void run() {
						boolean result = false;
						try {
							// start preview and display image to view on the screen
							result = mRobotCameraPreview.startPreview();
						} catch (final RobotException e) {
							e.printStackTrace();
					//		makeToast("start camera preview failed! " + e.getMessage());
							return;
						}
						if (result) {
					//		makeToast("camera preview started!");
						} else {
					//		makeToast("start camera preview failed!");
						}
					}
				}).start();
	}
	public void stopPreview(View v) {
		if (mRobotCameraPreview == null) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				try {
					// stop view
					result = mRobotCameraPreview.stopPreview();
				} catch (final RobotException e) {
					e.printStackTrace();
				//	makeToast("stop camera preview failed! " + e.getMessage());
					return;
				}
				if (result) {
				//	makeToast("camera preview stopped!");
				} else {
				//	makeToast("stop camera preview failed!");
				}
			}
		}).start();
	}
	

	private ProgressDialog progressDialog = null;

	protected void showProgress(final String message) {
		// Log.d(TAG, "showProgress('" +message+ "')");
		runOnUiThread(new Runnable() {
			public void run() {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(
							SignsActivity.this);
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
						Toast.makeText(SignsActivity.this, "Text is empty",
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
}
