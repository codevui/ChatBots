package team68.chatbots.model.sqlite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.SlideDb;
import team68.chatbots.model.entity.Slide;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

public class SlideDbSqlite extends SQLiteOpenHelper implements SlideDb {
	// ten CSDL
	private static final String DB_NAME = "team68.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 1;
	private static final String LOG = "SlideDbSqlite";
	
	Context context;
	public SlideDbSqlite(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
		
	}

	@Override
	public Slide getById(int id) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = this.getReadableDatabase();
		String sql = "SELECT * FROM Slide WHERE id = " + id;
		LogUtil.LogD(LOG, sql);
		Cursor cur = database.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
		}
		Slide slide = new Slide();
		slide.setId(cur.getInt(1));
		slide.setIdCourse(cur.getInt(2));
		slide.setOrder(cur.getInt(3));
		slide.setImage(cur.getString(4));
		slide.setTextPreview(cur.getString(5));
		slide.setTextToSay(cur.getString(6));
		
		return slide;
	}

	@Override
	public List<Slide> getListSlide() {
		List<Slide> listSlide = new ArrayList<Slide>();
		
		SQLiteDatabase database = null;
		
		String dir = Environment.getExternalStorageDirectory().toString();
		try{
	        String myPath = dir+"/"+DB_NAME; // also check the extension of you db file 
	        File dbfile = new File(myPath);                
	            if( dbfile.exists())
	             Toast.makeText(context, "database exists", Toast.LENGTH_LONG).show(); 
	            else
	             Toast.makeText(context, "cant find database", Toast.LENGTH_LONG).show();
	        }
	        catch(SQLiteException e){
	            System.out.println("Database doesn't exist");
	        }
		try{
			database = SQLiteDatabase.openDatabase(DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
		}catch(Exception e){
			e.printStackTrace();
		}
		String sql = "SELECT * FROM Slide";
		
		LogUtil.LogD(LOG, sql);
		Cursor cur = null;
		try{
			cur = database.rawQuery(sql, null);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if (cur != null){
			cur.moveToFirst();
			do{
				Slide slide = new Slide();
				slide.setId(cur.getInt(1));
				slide.setIdCourse(cur.getInt(2));
				slide.setOrder(cur.getInt(3));
				slide.setImage(cur.getString(4));
				slide.setTextPreview(cur.getString(5));
				slide.setTextToSay(cur.getString(6));
				listSlide.add(slide);
			} while (cur.moveToNext());
		}
		return listSlide;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Slide> getSlideByCourseId() {
		// TODO Auto-generated method stub
		return null;
	}

}