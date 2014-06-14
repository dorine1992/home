package data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.homework.MainActivity;

public class LessonList {
	private String tablename="lesson";
	private ArrayList<Lesson> lessonList;
	
	
	public LessonList(){
		initLesson();
	}
	private void initLesson(){
		lessonList=new ArrayList<Lesson>();
		
		Cursor cursor=MainActivity.helper.getReadableDatabase().query("lesson", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Lesson lesson=new Lesson();
			lesson.setClassroom(cursor.getString(cursor.getColumnIndex("classroom")));
			lesson.setDay(cursor.getString(cursor.getColumnIndex("day")));
			lesson.setId(cursor.getInt(cursor.getColumnIndex("lid")));
			lesson.setJieci(cursor.getString(cursor.getColumnIndex("jieci")));
			lesson.setName(cursor.getString(cursor.getColumnIndex("name")));
			lesson.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
			lessonList.add(lesson);
		}
	}
	
	public void add(Lesson w){
		lessonList.add(w);
		MainActivity.helper.getWritableDatabase().execSQL("insert into lesson values(null,?,?,?,?,?)"
							,new String[]{w.getDay(),w.getJieci(),w.getName(),w.getTeacher(),w.getClassroom()});
	}
	public void delete(Lesson w){
		lessonList.remove(w);
		SQLiteDatabase db=MainActivity.helper.getWritableDatabase();
		String where="lid = ?";
		String[] whereValue={w.getId()+""};
		db.delete(tablename, where, whereValue);
	}
	
	public void modify(Lesson w){
		SQLiteDatabase db=MainActivity.helper.getWritableDatabase();
		String where="lid = ?";
		String[] whereValue={w.getId()+""};
		ContentValues cv=new ContentValues();
		cv.put("day", w.getDay());
		cv.put("jieci", w.getJieci());
		cv.put("name",w.getName());
		cv.put("teacher", w.getTeacher());
		cv.put("classroom", w.getClassroom());
		db.update(tablename,cv, where, whereValue);
	}
	public ArrayList<Lesson> getLessonList() {
		return lessonList;
	}
	public void setLessonList(ArrayList<Lesson> lessonList) {
		this.lessonList = lessonList;
	}
	public String tomorrowDay(){
		  Calendar c = Calendar.getInstance();
		  c.setTime(new Date(System.currentTimeMillis()));
		  int index = c.get(Calendar.DAY_OF_WEEK);
		  String[] day={"周一","周二","周三","周四","周五","周六","周日"};
		  return day[index-1];
	}
	public CharSequence[] getAll(){
		String[] str;
		if(lessonList.size()!=0){
		str=new String[lessonList.size()+1];
		for(int i=0;i<lessonList.size();i++){
			str[i]=lessonList.get(i).getName();
		}
		str[lessonList.size()]="其他";
		}
		else{ 
		str=new String[]{"其他"};
		}
		return str;
	}
	public Integer getIndex(String s){
		CharSequence[] str=getAll();
		for(int i=0;i<str.length;i++){
			if(str[i].equals(s))
				return i;
		}
		return null;
	}
}
