package com.homework;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddlessonActivity extends  Activity{
	 private Button save=null;
	 private Button fanhui=null;
	 private EditText mingcheng=null;
	 private EditText teacher=null;
	 private EditText classroom=null;
	 private EditText jieci1,jieci2;
	 private Spinner xingqi;
	 final String[] list={"周一","周二","周三","周四","周五","周六","周日"};
	 @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.addlesson);
         fanhui=(Button) super.findViewById(R.id.lessonfanhui);
         mingcheng=(EditText) super.findViewById(R.id.lessonName);
         teacher=(EditText) super.findViewById(R.id.teacher);
         classroom=(EditText) super.findViewById(R.id.classroom);
         jieci1=(EditText) super.findViewById(R.id.jieci1);
         jieci2=(EditText) super.findViewById(R.id.jieci2);
         xingqi=(Spinner) super.findViewById(R.id.xingqi);
         save=(Button) super.findViewById(R.id.saveLesson);
        ArrayAdapter<CharSequence> ad= new ArrayAdapter<CharSequence>(this, 
        		 android.R.layout.select_dialog_item, list);
        ad.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line);
         xingqi.setAdapter(ad);
         
         save.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aintent = new Intent();
				aintent.putExtra("name", mingcheng.getText().toString());
				aintent.putExtra("address", classroom.getText().toString());
				aintent.putExtra("jieci", jieci1.getText().toString()+"--"+jieci2.getText().toString());
				aintent.putExtra("xingqi",xingqi.getSelectedItem().toString());
				aintent.putExtra("teacher", teacher.getText().toString());
				setResult(20,aintent);
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
         fanhui.setOnClickListener(new OnClickListener() {
        	 @Override
			public void onClick(View arg0) {
				finish();
			}
		});
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
