package team68.chatbots.model.sqlite;

import java.util.ArrayList;
import java.util.List;

import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.PlanAnswerDb;
import team68.chatbots.model.entity.PlanAnswer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlanAnswerDbSqlite extends SQLiteOpenHelper implements PlanAnswerDb{

	// ten CSDL
	private static final String DB_NAME = "team68v8.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 1;
	private static final String LOG = "PlanAnswerDbSqlite";
	
	Context context;
	SQLiteDatabase myDatabase;
	
	public PlanAnswerDbSqlite(Context context,SQLiteDatabase database) {
		super(context, DB_NAME, null, DB_VERSION);
		myDatabase = database;
		this.context = context;
	}
	
	@Override
	public PlanAnswer getById(int id) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM PlanAnswer WHERE id = " + id;
		LogUtil.LogD(LOG, sql);
		Cursor cur = myDatabase.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
		}
		PlanAnswer planAnswer = new PlanAnswer();
		planAnswer.setId(cur.getInt(cur.getColumnIndex("id")));
		planAnswer.setIdQuestion(cur.getInt(cur.getColumnIndex("id_question")));
		planAnswer.setContent(cur.getString(cur.getColumnIndex("content")));
		planAnswer.setIsTrue(cur.getInt(cur.getColumnIndex("isTrue")) == 1);
		
		
		return planAnswer;
	}

	@Override
	public List<PlanAnswer> getListAnswer() {
		List<PlanAnswer> listPlanAnswer = new ArrayList<PlanAnswer>();
		String sql = "SELECT * FROM PlanAnswer";
		
		LogUtil.LogD(LOG, sql);
		
		Cursor cur = myDatabase.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
			do{
				PlanAnswer course = new PlanAnswer();
				PlanAnswer planAnswer = new PlanAnswer();
				planAnswer.setId(cur.getInt(1));
				planAnswer.setIdQuestion(cur.getInt(2));
				planAnswer.setContent(cur.getString(3));
				planAnswer.setIsTrue(cur.getInt(4) == 1);
				listPlanAnswer.add(course);
			} while (cur.moveToNext());
		}
		return listPlanAnswer;
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
	public List<PlanAnswer> getAnswerByQuestionId(int id) {
		List<PlanAnswer> listPlanAnswer = new ArrayList<PlanAnswer>();
		String sql = "SELECT * FROM PlanAnswer WHERE id_question = " + id;
		
		LogUtil.LogD(LOG, sql);
		
		Cursor cur = myDatabase.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
			do{
				PlanAnswer planAnswer = new PlanAnswer();
				planAnswer.setId(cur.getInt(0));
				planAnswer.setIdQuestion(cur.getInt(1));
				planAnswer.setContent(cur.getString(2));
				planAnswer.setIsTrue(cur.getInt(3) == 1);
				listPlanAnswer.add(planAnswer);
			} while (cur.moveToNext());
		}
		return listPlanAnswer;
	}
}
