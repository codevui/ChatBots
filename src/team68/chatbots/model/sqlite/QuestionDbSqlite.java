package team68.chatbots.model.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.QuestionDb;
import team68.chatbots.model.entity.Course;
import team68.chatbots.model.entity.Question;

public class QuestionDbSqlite extends SQLiteOpenHelper implements QuestionDb {

	// ten CSDL
	private static final String DB_NAME = "team68.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 1;
	private static final String LOG = "QuestionDbSqlite";

	public QuestionDbSqlite(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public Question getById(int id) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = this.getReadableDatabase();
		String sql = "SELECT * FROM Question WHERE id = " + id;
		LogUtil.LogD(LOG, sql);
		Cursor cur = database.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
		}
		Question question = new Question();
		question.setId(cur.getInt(1));
		question.setId_course(cur.getInt(2));
		question.setType(cur.getInt(3));
		question.setContent(cur.getString(4));
		question.setImage(cur.getString(5));
		
		return question;
	}

	@Override
	public List<Question> getListQuestion() {
		List<Question> listQuestion = new ArrayList<Question>();
		SQLiteDatabase database = this.getReadableDatabase();
		String sql = "SELECT * FROM Question";
		
		LogUtil.LogD(LOG, sql);
		
		Cursor cur = database.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
			do{
				Question question = new Question();
				question.setId(cur.getInt(1));
				question.setId_course(cur.getInt(2));
				question.setType(cur.getInt(3));
				question.setContent(cur.getString(4));
				question.setImage(cur.getString(5));
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

}