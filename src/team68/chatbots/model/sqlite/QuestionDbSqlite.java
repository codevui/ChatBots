package team68.chatbots.model.sqlite;

import java.util.ArrayList;
import java.util.List;

import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.QuestionDb;
import team68.chatbots.model.entity.Question;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestionDbSqlite extends SQLiteOpenHelper implements QuestionDb {

	// ten CSDL
	private static final String DB_NAME = "team68v8.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 1;
	private static final String LOG = "QuestionDbSqlite";
	Context context;
	SQLiteDatabase myDatabase;
	public QuestionDbSqlite(Context context, SQLiteDatabase database) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
		myDatabase = database;
	}

	@Override
	public Question getById(int id) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM Question WHERE id = " + id;
		LogUtil.LogD(LOG, sql);
		Cursor cur = myDatabase.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
		}
		Question question = new Question();
		question.setId(cur.getInt(cur.getColumnIndex("id")));
		question.setId_course(cur.getInt(cur.getColumnIndex("id_course")));
		question.setType(cur.getInt(cur.getColumnIndex("type")));
		question.setContent(cur.getString(cur.getColumnIndex("content")));
		question.setImage(cur.getString(cur.getColumnIndex("image")));
		
		return question;
	}

	@Override
	public List<Question> getListQuestion() {
		List<Question> listQuestion = new ArrayList<Question>();
		String sql = "SELECT * FROM Question";
		
		LogUtil.LogD(LOG, sql);
		
		Cursor cur = myDatabase.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
			do{
				Question question = new Question();
				question.setId(cur.getInt(cur.getColumnIndex("id")));
				question.setId_course(cur.getInt(cur.getColumnIndex("id_course")));
				question.setType(cur.getInt(cur.getColumnIndex("type")));
				question.setContent(cur.getString(cur.getColumnIndex("content")));
				question.setImage(cur.getString(cur.getColumnIndex("image")));
				listQuestion.add(question);
			} while (cur.moveToNext());
		}
		return listQuestion;
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
	public List<Question> getListQuestionByCourseId(int id) {
		List<Question> listQuestion = new ArrayList<Question>();
		String sql = "SELECT * FROM Question WHERE id_course = " + id;
		
		LogUtil.LogD(LOG, sql);
		
		Cursor cur = myDatabase.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
			do{
				Question question = new Question();
				question.setId(cur.getInt(cur.getColumnIndex("id")));
				question.setId_course(cur.getInt(cur.getColumnIndex("id_course")));
				question.setType(cur.getInt(cur.getColumnIndex("type")));
				question.setContent(cur.getString(cur.getColumnIndex("content")));
				question.setImage(cur.getString(cur.getColumnIndex("image")));
				listQuestion.add(question);
			} while (cur.moveToNext());
		}
		return listQuestion;
	}

}