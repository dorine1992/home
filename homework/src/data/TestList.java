package data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.homework.MainActivity;

public class TestList {
	private String tablename="test";
	private ArrayList<Test> testList;
	
	
	public TestList(){
		initTest();
	}
	private void initTest(){
		testList=new ArrayList<Test>();
		
		Cursor cursor=MainActivity.helper.getReadableDatabase().query("test", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Test test=new Test();
			test.setDate(cursor.getString(cursor.getColumnIndex("date")));
			test.setId(cursor.getInt(cursor.getColumnIndex("tid")));
			test.setLesson(cursor.getString(cursor.getColumnIndex("lesson")));
			testList.add(test);
		}
	}
	
	public void add(Test w){
		testList.add(w);
		MainActivity.helper.getWritableDatabase().execSQL("insert into test values(null,?,?)"
							,new String[]{w.getLesson(),w.getDate()});
	}
	public void delete(Test w){
		testList.remove(w);
		SQLiteDatabase db=MainActivity.helper.getWritableDatabase();
		String where="tid = ?";
		String[] whereValue={w.getId()+""};
		db.delete(tablename, where, whereValue);
	}
	
	public void modify(Test w){
		SQLiteDatabase db=MainActivity.helper.getWritableDatabase();
		String where="tid = ?";
		String[] whereValue={w.getId()+""};
		ContentValues cv=new ContentValues();
		cv.put("date", w.getDate());
		cv.put("lesson", w.getLesson());
		db.update(tablename,cv, where, whereValue);
	}
	public ArrayList<Test> getTestList() {
		return testList;
	}
	public void setTestList(ArrayList<Test> testList) {
		this.testList = testList;
	}
	
}
