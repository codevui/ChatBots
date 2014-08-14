package team68.chatbots.model.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.SlideDb;
import team68.chatbots.model.entity.Course;
import team68.chatbots.model.entity.Slide;

public class SlideDbSqlite extends SQLiteOpenHelper implements SlideDb {
	// ten CSDL
	private static final String DB_NAME = "team68.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 1;
	private static final String LOG = "SlideDbSqlite";

	public SlideDbSqlite(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
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
		SQLiteDatabase database = this.getReadableDatabase();
		String sql = "SELECT * FROM Slide";
		
		LogUtil.LogD(LOG, sql);
		
		Cursor cur = database.rawQuery(sql, null);
		
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

}