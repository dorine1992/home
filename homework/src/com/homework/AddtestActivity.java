package com.homework;


import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddtestActivity extends Activity{

	private Button save=null;
	private Spinner lesson=null;
	private DatePicker date=null;
	private TimePicker time=null;
	
	private ArrayAdapter<CharSequence> kechengAdapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtest);
        date=(DatePicker) super.findViewById(R.id.date);
        time=(TimePicker) super.findViewById(R.id.testtime);
 
        lesson=(Spinner) super.findViewById(R.id.testlesson);
        save=(Button) super.findViewById(R.id.savetest);
        save.setOnClickListener(new SaveListener());
        
        String[] list=getIntent().getStringArrayExtra("alllesson");
        kechengAdapter=new ArrayAdapter<CharSequence>(this, 
       		 android.R.layout.select_dialog_item, list);
        kechengAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        lesson.setAdapter(kechengAdapter);
        
       
    }
    
	private class SaveListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
		int iyear=0;
		int imonth=0;
		int iday=0;
		String sDate="";
		iyear=date.getYear();
		imonth=date.getMonth()+1;
		iday=date.getDayOfMonth();
		String selectLesson=lesson.getSelectedItem().toString();
		int ihour=time.getCurrentHour();
		int imu=time.getCurrentMinute();
		sDate=String.valueOf(iyear)+"年"+String.valueOf(imonth)+"月"+
		String.valueOf(iday)+"日";
		String stime=ihour+"时"+imu+"分";
		Intent intent=new Intent();
		intent.putExtra("lesson",selectLesson);
		intent.putExtra("day",sDate );
		intent.putExtra("time", stime);
		setResult(30, intent);
		Toast.makeText(getApplicationContext(), "新增成功", Toast.LENGTH_SHORT).show();
		finish();
		}
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
