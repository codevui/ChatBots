package team68.chatbots.controller;

import team68.chatbots.activity.HomeActivity;
import team68.chatbots.activity.SignsActivity;
import android.content.Context;
import android.content.Intent;

import com.fpt.robot.app.RobotActivity;

public class NLPCenter {
	RobotActivity fromActivity;
	public NLPCenter(Context context, RobotActivity activity) {
		super();
		fromActivity = activity;
	}

	public void solveText(String input){
		//Check if input is a GoHome command
		if (isCommand(input,cmdGoHome)){
			goHome();
		}
		//Check if input is a TraCuuBienBao command
		if(isCommand(input,cmdRegSigns)){
			startRegSigns();
			
		}
	}
	public boolean isCommand(String input, String[] listString) {
		for (int i=0 ; i < listString.length ;i++ ){
			if (sameTwoString(input,listString[i]) >= 0.5f){
				return true;
			}
			
		}
		return false;
	}
	private double sameTwoString(String str1, String str2) {
		String[] lis1= str1.trim().split(" ");
		String[] lis2= str2.trim().split(" ");
		int c=0;
		for(int cs=0;cs<lis1.length;cs++) {
			for (int i=0;i<lis2.length;i++) if (lis2[i].compareTo(lis1[cs])==0) {
				c++;
				break;
			}
		}
		return (double)c/lis2.length;
	}
	public void goHome(){
		Intent intent = new Intent(fromActivity, HomeActivity.class);
		fromActivity.startActivity(intent);
	}
	public void startRegSigns(){
		Intent intent = new Intent(fromActivity, SignsActivity.class);
		fromActivity.startActivity(intent);
	}
	String[] cmdTakePicture = { 
			"chụp ảnh", 
			"chụp ảnh đi" 
	};
	String[] cmdGoHome = { 
			"về nhà", 
			"trang chủ", 
			"trang bắt đầu" 
	};
	
	String[] cmdStartCourse = { 
			"bắt đầu học",  
			"học bài" 
	};
	String[] cmdContinueCourse = { 
			"tiếp tục học",  
			"học tiếp" 
	};
	String[] cmdRegSigns = { 
			"tra cứu biển báo",  
			"biển báo" 
	};
	String[] cmdPushServer = { 
			"bắt đầu tra cứu",   
	};
	
	
}
