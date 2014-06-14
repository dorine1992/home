package com.homework;

import java.util.ArrayList;
import java.util.Calendar;

import com.umeng.analytics.MobclickAgent;

import data.Lesson;
import data.LessonList;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class TestItemActivity extends Activity{
	   private EditText date,time,lesson;
	   private Button save,back,edit;
	   private DatePicker dp;
	   private TimePicker tp;
	   private Spinner spin;
	   private final static int DATE_DIALOG = 0; 
	   private final static int TIME_DIALOG =1;
	   private ArrayAdapter<CharSequence> kechengAdapter=null;
	   private CharSequence[] list;
	   String  slesson;
	   int position;
	   boolean b = false;
	   
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.testitem);
	       Intent intent=getIntent();
	       
	       lesson=(EditText) super.findViewById(R.id.testitemlesson);
	       date=(EditText) super.findViewById(R.id.testitemday);
	       time=(EditText) super.findViewById(R.id.testitemtime);
	       date.setInputType(InputType.TYPE_NULL); 
	       time.setInputType(InputType.TYPE_NULL);
	       back=(Button) super.findViewById(R.id.backtestitem);
	       save=(Button) super.findViewById(R.id.savetestitem);
	       
	       spin=(Spinner) super.findViewById(R.id.spinTestitem);
	       list=intent.getStringArrayExtra("alllesson");
	    		   
	       kechengAdapter=new ArrayAdapter<CharSequence>(this, 
             		 android.R.layout.select_dialog_item, list);
           kechengAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	       spin.setAdapter(kechengAdapter);
	       int select=intent.getIntExtra("select", 0);
	       spin.setSelection(select);
	       slesson=intent.getStringExtra("lesson");
	       lesson.setText(slesson);
	       time.setText(intent.getStringExtra("time"));
	       date.setText(intent.getStringExtra("day"));
	       position=intent.getIntExtra("index", 0);
	       	       
	       spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(b){
				 String ke=list[position].toString();
				 lesson.setText(ke);
				 }
				b=true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	   }
	   
	   
	   public void save(View v){
		   //获取值进行存储
		Intent intent=new Intent();
		intent.putExtra("lesson",lesson.getText().toString());
		intent.putExtra("index", position);
		intent.putExtra("day",date.getText().toString());
		intent.putExtra("time", time.getText().toString());
		setResult(2, intent);
		finish();
	   }
	   
	   public void back(View v){
		   finish();
	   }
	   public void chooseDate(View v){
		   showDialog(DATE_DIALOG);
	   }
	   public void chooseTime(View v){
		 showDialog(TIME_DIALOG);      
	   }
	   @Override
	   protected Dialog onCreateDialog(int id) { 
		   Dialog dialog = null; 
		   switch(id){
		   case DATE_DIALOG:
			   Calendar  c = Calendar.getInstance();               
			   dialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {   
			        public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {   
				 date.setText(year + "年" + (month+1) + "月" + dayOfMonth + "日");                      
				 }    
				}, c.get(Calendar.YEAR), // 传入年份                 
				  c.get(Calendar.MONTH), // 传入月份                  
				  c.get(Calendar.DAY_OF_MONTH) // 传入天数             
						 );  
				 break;
		   case TIME_DIALOG:
			   c=Calendar.getInstance();
			   dialog =new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){ 
				   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {    
						 time.setText(hourOfDay+"时"+minute+"分");                      
						 }    
			   },c.get(Calendar.HOUR_OF_DAY),
			   		c.get(Calendar.MINUTE),false);
			   break;
		   }
		   return dialog;
	   }
	   public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			}
		public void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
			}
}
