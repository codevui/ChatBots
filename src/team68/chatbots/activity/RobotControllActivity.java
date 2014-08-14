package team68.chatbots.activity;

import team68.chatbots.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.motion.RobotPosture;
import com.fpt.robot.vision.RobotCamera;

public class RobotControllActivity extends RobotActivity {
	Context context;
	private boolean SET_PICTURE_FILE_NAME = true;
	private RobotCamera[] mCamera = new RobotCamera[2];
	int selectedCameraIndex = 1;
	ImageView ivTakenPicture;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.robot_activity);
		context = getApplicationContext();
		ivTakenPicture = (ImageView) findViewById(R.id.imgTakenPicture);

	}

	public void sitdown(View v) {
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

	public void standup(View v) {
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
					ivTakenPicture.setImageBitmap(bm);
				}
			});
		} else {
			makeToast("Picture saved to " + picturePath + "!");
		}
	}
	public void makeToast(final String m) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, m, Toast.LENGTH_LONG).show();
			}
		});
	}

	private ProgressDialog progressDialog = null;

	protected void showProgress(final String message) {
		// Log.d(TAG, "showProgress('" +message+ "')");
		runOnUiThread(new Runnable() {
			public void run() {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(
							RobotControllActivity.this);
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

}
