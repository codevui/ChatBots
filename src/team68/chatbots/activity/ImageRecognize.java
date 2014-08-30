package team68.chatbots.activity;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import team68.chatbots.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.vision.RobotCamera;
import com.fpt.robot.vision.RobotCameraPreview;
import com.fpt.robot.vision.RobotCameraPreviewView;

public class ImageRecognize extends RobotActivity {
	
	private EditText ipAddr;
	private Button bConnect;
	private String ipAddress;
	private SocketChannel socketChannel = null;
	private RobotCameraPreviewView mCameraPreview;
	private RobotCameraPreview mRobotCameraPreview;
	private boolean SET_PICTURE_FILE_NAME = true;
	private RobotCamera[] mCamera = new RobotCamera[2];
	int selectedCameraIndex = 1;
	Context context;
	String idImageResponse;
	boolean isFinish;
	Message m = new Message();
	protected static final int MSG_ID = 0x1337;
	private Button btnImageScan;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_activity);
		bConnect=(Button)findViewById(R.id.bConnect);
		ipAddr = (EditText)findViewById(R.id.ipAddr);
		bConnect.setEnabled(true);
		mCameraPreview = (RobotCameraPreviewView) findViewById(R.id.cameraPreview);
		context = getApplicationContext();
		btnImageScan = (Button) findViewById(R.id.btnImageScan);
		btnImageScan.setEnabled(false);
	}
	
	
	public void imageScanI(View v){
		ipAddress = "10.3.9.106";
		final String filename = "picture.bmp";
		bConnect.setEnabled(false);
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
					btnImageScan.setEnabled(true);
				}
			});
		} else {
			makeToast("Picture saved to " + picturePath + "!");
		}
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
							ImageRecognize.this);
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


}
