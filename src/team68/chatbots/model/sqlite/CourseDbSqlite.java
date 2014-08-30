package team68.chatbots.model.sqlite;

import java.util.ArrayList;
import java.util.List;

import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.CourseDb;
import team68.chatbots.model.entity.Course;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseDbSqlite extends SQLiteOpenHelper implements CourseDb {
	// ten CSDL
	private static final String DB_NAME = "team68v8.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 1;
	private static final String LOG = "CourseDbSqlite";
	SQLiteDatabase myDatabase;
	public CourseDbSqlite(Context context, SQLiteDatabase database) {
		super(context, DB_NAME, null, DB_VERSION);
		myDatabase = database;
	}

	@Override
	public Course getById(int id) {
		
		String sql = "SELECT * FROM Course WHERE id = " + id;
		LogUtil.LogD(LOG, sql);
		Cursor cur = myDatabase.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
		}
		Course course = new Course();
		course.setId(id);
		course.setNumberSlide(cur.getInt(2));
		course.setNumberQuestion(cur.getInt(3));
		course.setTitle(cur.getString(4));
		
		return course;
	}

	@Override
	public List<Course> getListCourse() {
		List<Course> listCourse = new ArrayList<Course>();
		String sql = "SELECT * FROM Course";
		
		LogUtil.LogD(LOG, sql);
		
		Cursor c = myDatabase.rawQuery(sql, null);
		
		if (c != null){
			c.moveToFirst();
			do{
				Course course = new Course();
				course.setId(c.getInt(c.getColumnIndex("id")));
				course.setNumberSlide(c.getInt(c.getColumnIndex("numberSlide")));
				course.setNumberQuestion(c.getInt(c.getColumnIndex("numberQuestion")));
				course.setTitle(c.getString(c.getColumnIndex("title")));
				listCourse.add(course);
			} while (c.moveToNext());
		}
		return listCourse;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}