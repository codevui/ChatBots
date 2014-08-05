package team68.chatbots.controller;

import android.app.ProgressDialog;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.motion.RobotPosture;

import team68.chatbots.activity.MessageActivity;

public class RobotController extends RobotActivity  {
	MessageActivity myActivity;
	private ProgressDialog progressDialog = null;
	
	public RobotController(MessageActivity m){
		this.myActivity = m;
	}
	public void sitdown(final Robot robot){
		/*
		 * Make robot goto posture
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (robot == null) {
					myActivity.makeToast("Plz connect to robot");
				} else {
					boolean result = false;
					try {
						showProgress("Sitdown...");
						// go to crouch state with speed is 0.5 (max speed is
						// 1.0)
						result = RobotPosture.goToPosture(robot, "Sitdown",
								0.5f);
						// set motor off
						RobotMotionStiffnessController.rest(robot);
					} catch (RobotException e) {
						cancelProgress();
						myActivity.makeToast(
								"Crouch failed: " + e.getMessage());
						e.printStackTrace();
						return;
					}
					cancelProgress();
					if (result) {
						myActivity.makeToast("Sitdown successfully");
					} else {
						myActivity.makeToast("Sitdown failed");
					}
				}
			}
		}).start();
	}
	protected void showProgress(final String message) {
		// Log.d(TAG, "showProgress('" +message+ "')");
		runOnUiThread(new Runnable() {
			public void run() {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(myActivity);
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
