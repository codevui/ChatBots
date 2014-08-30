package team68.chatbots.model.sqlite;

import team68.chatbots.controller.LogUtil;
import team68.chatbots.model.dao.db.SignsDb;
import team68.chatbots.model.entity.Signs;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SignsDbSqlite extends SQLiteOpenHelper implements SignsDb {
	// ten CSDL
	private static final String DB_NAME = "team68v8.db";
	// phien ban cua CSDL
	private static final int DB_VERSION = 1;
	private static final String LOG = "SignsDbSqlite";
	SQLiteDatabase myDatabase;
	public SignsDbSqlite(Context context, SQLiteDatabase database) {
		super(context, DB_NAME, null, DB_VERSION);
		myDatabase = database;
	}

	@Override
	public Signs getById(int id) {
		
		String sql = "SELECT * FROM signs WHERE id = " + id;
		LogUtil.LogD(LOG, sql);
		Cursor cur = myDatabase.rawQuery(sql, null);
		
		if (cur != null){
			cur.moveToFirst();
		}
		Signs sign = new Signs();
		sign.setId(id);
		sign.setImg(cur.getString(cur.getColumnIndex("img")));
		sign.setTitle(cur.getString(cur.getColumnIndex("title")));
		sign.setText(cur.getString(cur.getColumnIndex("text")));
		return sign;
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