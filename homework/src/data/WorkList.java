package data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.homework.MainActivity;

public class WorkList {
	private String tablename="homework";
	public ArrayList<Homework> toDoList;
	public ArrayList<Homework> donelist;
	
	public WorkList(){
		initHomework();
	}
	private void initHomework(){
		toDoList=new ArrayList<Homework>();
		donelist=new ArrayList<Homework>();
		SQLiteDatabase db=MainActivity.helper.getReadableDatabase();
		System.out.println(db);
		Cursor cursor=db.query("homework", null, null, null, null, null,"deadline");
		while(cursor.moveToNext()){
			Homework work=new Homework();
			work.setContent(cursor.getString(cursor.getColumnIndex("content")));
			work.setDeadline(cursor.getString(cursor.getColumnIndex("deadline")));
			work.setDone(cursor.getInt(cursor.getColumnIndex("done")));
			work.setId(cursor.getInt(cursor.getColumnIndex("hid")));
			work.setLesson(cursor.getString(cursor.getColumnIndex("lesson")));
			work.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			work.setImgPath(cursor.getString(cursor.getColumnIndex("imgurl")));
			work.setYuyinPath(cursor.getString(cursor.getColumnIndex("yuyinurl")));
			if(work.getDone()==1){
				donelist.add(work);
			}else{
				toDoList.add(work);
			}
		}
	}
	
	public void addTodo(Homework w){
		toDoList.add(w);
		MainActivity.helper.getWritableDatabase().execSQL("insert into homework values(null,?,?,?,?,?,?,?)"
							,new String[]{w.getLesson(),w.getDeadline(),"0",w.getContent(),w.getTitle(),w.getImgPath(),w.getYuyinPath()});
	}
	public void deleteToDo(Homework w){
		toDoList.remove(w);
		SQLiteDatabase db=MainActivity.helper.getWritableDatabase();
		String where="hid = ?";
		String[] whereValue={w.getId()+""};
		db.delete(tablename, where, whereValue);
	}
	public void hasdone(Homework w){
		toDoList.remove(w);
		donelist.add(w);
		w.setDone(1);
		modify(w);
	}
	public void modify(Homework w){
		SQLiteDatabase db=MainActivity.helper.getWritableDatabase();
		String where="hid = ?";
		String[] whereValue={String.valueOf(w.getId())};
		ContentValues cv=new ContentValues();
		cv.put("lesson", w.getLesson());
		cv.put("deadline", w.getDeadline());
		cv.put("content", w.getContent());
		cv.put("title", w.getTitle());
		cv.put("done", w.getDone());
		cv.put("imgurl", w.getImgPath());
		cv.put("yuyinurl", w.getYuyinPath());
		db.update(tablename,cv, where, whereValue);
	}
	public ArrayList<Homework> getToDoList() {
		return toDoList;
	}
	public void setToDoList(ArrayList<Homework> toDoList) {
		this.toDoList = toDoList;
	}
	public ArrayList<Homework> getDonelist() {
		return donelist;
	}
	public void setDonelist(ArrayList<Homework> donelist) {
		this.donelist = donelist;
	}
	public ArrayList<String> getDoneName(){
		ArrayList<String> list=new ArrayList<String>();
		if(donelist.size()!=0){
		for(int i=0;i<donelist.size();i++){
			String title=donelist.get(i).getTitle();
			list.add(title);
		}
		}
		else{ 
		list.add("没有已完成的任务 ");
		}
		return list;
	}
}
