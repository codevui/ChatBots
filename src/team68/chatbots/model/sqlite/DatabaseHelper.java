package team68.chatbots.model.sqlite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "team68v8.db";
	private String DB_PATH;
	private final Context myContext;
	private SQLiteDatabase myDatabase;
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 2);
		this.myContext = context;
		
		DB_PATH = "/data/data/" + myContext.getApplicationContext().getPackageName() + "/databases/";
		
	}
	/*
	 * Create a database and rewrite it
	 */
	/**
	 * @throws IOException
	 */
	public void createDatabase () throws IOException {
		boolean dbExist = checkDatabase();
		if (dbExist) {
			this.getReadableDatabase();
			copyDatabase();
		} else {
			this.getReadableDatabase();
			copyDatabase();
		}
		
	}
	/*
	 * Copies database from local assets-folder to the just created database in system folder
	 */
	private void copyDatabase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		
		String outFileName = DB_PATH + DB_NAME;
		
		OutputStream myOutput = new FileOutputStream(outFileName);
		//transfer bytes from the inputFile to the outputFile
		
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) >0 ){
			myOutput.write(buffer,0,length);
		}
		
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
	/*
	 * Check if database exist , avoid re-copying everytime application run
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDatabase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e){
			
		}
		
		if (checkDB != null){
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}
	
	public SQLiteDatabase getMyDatabase() {
		return myDatabase;
	}
	public void setMyDatabase(SQLiteDatabase myDatabase) {
		this.myDatabase = myDatabase;
	}
	public void openDatabase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}
	 @Override
	 public synchronized void close() {
	  
	 if(myDatabase != null)
	 myDatabase.close();
	  
	 super.close();
	  
	 }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
