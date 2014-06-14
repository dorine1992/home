package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

       final String CREATE_HOMEWORK_=
    		   "create table homework("
    		   + "hid integer primary key autoincrement,"
    		   + "lesson varchar(40),"
    		   + "deadline varchar(20),"
    		   + "done integer,"
    		   + "content varchar(250),"
    		   + "title varchar(40),"
    		   + "imgurl varchar(100),"
    		   + "yuyinurl varchar(100))";
       final String CREATE_LESSON=
       		"create table lesson("
       		 + "lid integer primary key autoincrement,"
       		 + "day var(20),"
       		 + "jieci var(20),"
       		 + "name var(50),"
       		 + "teacher var(50),"
       		 + "classroom var(50))";
       final String CREATE_TEST=
    		   "create table test("
    		   + "tid integer primary key autoincrement,"
    		   + "lesson var(50),"
    		   + "date varchar(20))";
    
   
	public DatabaseHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_HOMEWORK_);
		db.execSQL(CREATE_LESSON);
		db.execSQL(CREATE_TEST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("------onUpdate Called"
				+oldVersion+"---->"+newVersion);		
	}
}
