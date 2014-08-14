package team68.chatbots.model.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.PlanAnswerDb;
import team68.chatbots.model.entity.Course;
import team68.chatbots.model.entity.PlanAnswer;

public class PlanAnswerDbSqlite extends SQLiteOpenHelper implements PlanAnswerDb{

	// ten CSDL
	private static final String DB_NAME = "team68.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 1;
	private static final String LOG = "PlanAnswerDbSqlite";

	public PlanAnswerDbSqlite(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public PlanAnswer getById(int id) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = this.getReadableDatabase();
		String sql = "SELECT * FROM PlanAnswer WHERE id = " + id;
		LogUtil.LogD(LOG, sql);
		Cursor cur = database.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
		}
		PlanAnswer planAnswer = new PlanAnswer();
		planAnswer.setId(cur.getInt(1));
		planAnswer.setIdQuestion(cur.getInt(2));
		planAnswer.setContent(cur.getString(3));
		planAnswer.setIsTrue(cur.getInt(4) == 1);
		
		
		return planAnswer;
	}

	@Override
	public List<PlanAnswer> getListAnswer() {
		List<PlanAnswer> listPlanAnswer = new ArrayList<PlanAnswer>();
		SQLiteDatabase database = this.getReadableDatabase();
		String sql = "SELECT * FROM PlanAnswer";
		
		LogUtil.LogD(LOG, sql);
		
		Cursor cur = database.rawQuery(sql, null);
		
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
}
