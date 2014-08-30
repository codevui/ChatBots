package team68.chatbots.model.sqlite;

import java.util.ArrayList;
import java.util.List;

import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.SlideDb;
import team68.chatbots.model.entity.Slide;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SlideDbSqlite extends SQLiteOpenHelper implements SlideDb {
	// ten CSDL
	private static final String DB_NAME = "team68v8.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 2;
	private static final String LOG = "SlideDbSqlite";
	
	Context context;
	SQLiteDatabase myDatabase;
	public SlideDbSqlite(Context context, SQLiteDatabase database) {
		super(context, DB_NAME, null, DB_VERSION);
		
		this.context = context;
		myDatabase = database;
		
	}

	@Override
	public Slide getById(int id) {
		
		String sql = "SELECT * FROM Slide WHERE id = " + id;
		LogUtil.LogD(LOG, sql);
		Cursor cur = myDatabase.rawQuery(sql, null);
		
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
		String sql = "SELECT * FROM Slide";
		
		LogUtil.LogD(LOG, sql);
		Cursor cur = null;
		try{
			cur = myDatabase.rawQuery(sql, null);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if (cur != null){
			cur.moveToFirst();
			do{
				Slide slide = new Slide();
				slide.setId(cur.getInt(cur.getColumnIndex("id")));
				slide.setIdCourse(cur.getInt(cur.getColumnIndex("idCourse")));
				slide.setOrder(cur.getInt(cur.getColumnIndex("stt")));
				slide.setImage(cur.getString(cur.getColumnIndex("image")));
				slide.setTextPreview(cur.getString(cur.getColumnIndex("textPreview")));
				slide.setTextToSay(cur.getString(cur.getColumnIndex("textToSay")));
				listSlide.add(slide);
			} while (cur.moveToNext());
		}
		return listSlide;
	}
	@Override
	public List<Slide> getSlideByCourseId(int id) {
		List<Slide> listSlide = new ArrayList<Slide>();
		String sql = "SELECT * FROM Slide WHERE idCourse = " + id + " ORDER BY stt";
		
		LogUtil.LogD(LOG, sql);
		Cursor cur = null;
		try{
			cur = myDatabase.rawQuery(sql, null);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if (cur != null){
			cur.moveToFirst();
			do{
				Slide slide = new Slide();
				slide.setId(cur.getInt(cur.getColumnIndex("id")));
				slide.setIdCourse(cur.getInt(cur.getColumnIndex("idCourse")));
				slide.setOrder(cur.getInt(cur.getColumnIndex("stt")));
				slide.setImage(cur.getString(cur.getColumnIndex("image")));
				slide.setTextPreview(cur.getString(cur.getColumnIndex("textPreview")));
				slide.setTextToSay(cur.getString(cur.getColumnIndex("textToSay")));
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

	

}