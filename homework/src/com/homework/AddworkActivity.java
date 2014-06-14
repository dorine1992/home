package com.homework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddworkActivity extends  Activity{
         private DatePicker dptoday=null;
         private Button save=null;
         private EditText title=null;
         private EditText neirong=null;
         private ArrayAdapter<CharSequence> kechengAdapter=null;
         private ImageView add,xiangqing;
         private AutoCompleteTextView lesson=null;
         int type;
         private static int PHOTO=1;
         private static int XIANGCE=2;
         private static int LUYIN=3;
         String path;
         private static final String LOG_TAG = "AudioRecordTest";
         private static String mFileName = null;
         private RecordButton mRecordButton = null;
         private MediaRecorder mRecorder = null;
         private TextView luzhigxinxi;
         
         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.addwork);
             dptoday=(DatePicker) super.findViewById(R.id.today);
             title=(EditText) super.findViewById(R.id.title);
             neirong=(EditText) super.findViewById(R.id.neirong);
             lesson=(AutoCompleteTextView) super.findViewById(R.id.worklesson);
             add=(ImageView) super.findViewById(R.id.addw);
             xiangqing=(ImageView) super.findViewById(R.id.xiangqing);
             save=(Button) super.findViewById(R.id.save);
             save.setOnClickListener(new SaveListener());
             String[] list=getIntent().getStringArrayExtra("alllesson");
             type=getIntent().getIntExtra("type", 0);
             LinearLayout ll=(LinearLayout) super.findViewById(R.id.ll);
             mRecordButton = new RecordButton(this);
             ll.addView(mRecordButton,
                 new LinearLayout.LayoutParams(
                     ViewGroup.LayoutParams.WRAP_CONTENT,
                     ViewGroup.LayoutParams.WRAP_CONTENT,
                     0));
             mRecordButton.setVisibility(View.GONE);
             luzhigxinxi=new TextView(getApplicationContext());
             luzhigxinxi.setTextSize(22);
             luzhigxinxi.setVisibility(View.GONE);
             ll.addView(luzhigxinxi,
                     new LinearLayout.LayoutParams(
                         ViewGroup.LayoutParams.WRAP_CONTENT,
                         ViewGroup.LayoutParams.WRAP_CONTENT,
                         0));
             switch (type) {
			 case 1://相册
				add.setImageResource(R.drawable.xiangce);
				add.setVisibility(View.VISIBLE);
				add.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						 Intent localIntent = new Intent();
				          localIntent.setType("image/*");
				          localIntent.setAction("android.intent.action.GET_CONTENT");
				          Intent localIntent2 = Intent.createChooser(localIntent,
				                  "选择图片");
				          startActivityForResult(localIntent2,XIANGCE);
				          
					}
				});
				break;
			case 2:
				//拍摄
				add.setImageResource(R.drawable.photo);
				add.setVisibility(View.VISIBLE);
				add.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						try{
							Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, PHOTO);
							}catch (Exception e){
								e.printStackTrace();
							}	
					}
				});
				break;
			case 3:
				//语音
				mRecordButton.setVisibility(View.VISIBLE);
				luzhigxinxi.setVisibility(View.VISIBLE);
				break;
			default:
				neirong.setVisibility(View.VISIBLE);
				break;
			}
             kechengAdapter=new ArrayAdapter<CharSequence>(this, 
               		 android.R.layout.select_dialog_item, list);
             kechengAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
             lesson.setAdapter(kechengAdapter);
         }
         @Override  
 	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
 	        // TODO Auto-generated method stub  
 	        super.onActivityResult(requestCode, resultCode, data);
 	        if (resultCode == Activity.RESULT_OK) {  
 	        	if(requestCode==PHOTO){
 	        	dealwithPhoto(data);
 	        	}
 	        	if(requestCode==XIANGCE){
 	        	dealwithxiangce(data);
 	        	}
 	        	if(requestCode==LUYIN){
 	        	
 	        	}
 	        }  
 	    } 

         public void dealwithxiangce(Intent data){
        	 String sdStatus = Environment.getExternalStorageState();  
             if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
                 Log.i("TestFile",  
                         "SD card is not avaiable/writeable right now.");  
                 return;  
             } 
             
             if(data==null||"".equals(data)){
             	return;
             }
             //此处选取照片
         	 Uri selectedImage = data.getData();
             String[] filePathColumn = { MediaStore.Images.Media.DATA };
             Cursor cursor = getContentResolver().query(selectedImage,
                             filePathColumn, null, null, null);
             cursor.moveToFirst();
             int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             String picturePath = cursor.getString(columnIndex);
             cursor.close();
             
             Bitmap bitmap = BitmapFactory.decodeFile(picturePath);      
             xiangqing.setImageBitmap(bitmap);
             xiangqing.setVisibility(View.VISIBLE);
             //此处保存照片
            String f = getSDPath()+"/"+"myimage";
     		File file = new File(f);
     		if(!file.exists()){
     			file.mkdirs();
     		}
     		 //以当前时间命名
             String str = null;
             Date date = null;
             SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
             date = new Date();
             str = format.format(date);
             String filename = f+"/"+str+".jpg";
             path=filename;
             try{
             	FileOutputStream fos = new FileOutputStream(filename);
             	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
             	fos.flush();
         		fos.close();
             }catch(FileNotFoundException e){
             	e.printStackTrace();
             }catch(IOException e){
             		e.printStackTrace();
             	}
         }
         
         public void dealwithPhoto(Intent data){
        	 String sdStatus = Environment.getExternalStorageState();  
	            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
	                Log.i("TestFile",  
	                        "SD card is not avaiable/writeable right now.");  
	                return;  
	            }  
	            if(data==null||"".equals(data)){
	            	return;
	            	}
	            Bundle bundle = data.getExtras();  
	            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
	            xiangqing.setImageBitmap(bitmap);// 将图片显示在ImageView里   
	            xiangqing.setVisibility(View.VISIBLE);
	            //将照片存入file中
	            String s = getSDPath()+"/"+"myimage";
	            File file = new File(s); 
	            if(!file.exists()){
	            file.mkdirs();// 创建文件夹，名称为myimage 
	            }

	            //以当前时间命名
	            String str = null;
	            Date date = null;
	            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	            date = new Date();
	            str = format.format(date);
	            String filename = s+"/"+str+".jpg";
	            path=filename;
	            
	            try{
	            	FileOutputStream fos = new FileOutputStream(filename);
	            	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	            	fos.flush();
	        		fos.close();
	            }catch(FileNotFoundException e){
	            	e.printStackTrace();
	            }catch(IOException e){
	            		e.printStackTrace();
	            	}
         }
         public String getSDPath(){ 
  	       File sdDir = null; 
  	       boolean sdCardExist = Environment.getExternalStorageState()   
  	                           .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
  	       if(sdCardExist)   
  	       {                               
  	         sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
  	      }   
  	       return sdDir.toString(); 
         }
         
		private class SaveListener implements OnClickListener{
			@Override
			public void onClick(View arg0) {
			int iyear=0;
			int imonth=0;
			int iday=0;
			String sDate="";
			iyear=dptoday.getYear();
			imonth=dptoday.getMonth()+1;
			iday=dptoday.getDayOfMonth();
			String selectLesson=lesson.getText().toString();
			sDate=String.valueOf(iyear)+"年"+String.valueOf(imonth)+"月"+
			String.valueOf(iday)+"日";
			Intent intent=new Intent();
			String t=title.getText().toString();
			switch (type) {
			case 1://相册
				intent.putExtra("imgurl", path);
				if(t.equals("")){
					t="图片记录";
				}
				break;
			case 2:
				//拍摄
				intent.putExtra("imgurl", path);
				if(t.equals("")){
					t="图片记录";
				}
				break;
			case 3:
				//语音
				intent.putExtra("yuyinurl", path);
				if(t.equals("")){
					t="语音记录";
				}
				break;
			default:
				intent.putExtra("content", neirong.getText().toString());
				if(t.equals("")){
					t="文字记录";
				}
				break;
			}
			intent.putExtra("workname",t);
			intent.putExtra("deadline", sDate);
			if(selectLesson.equals("")){
				selectLesson="其他";
			}
			intent.putExtra("lesson", selectLesson);
			
			setResult(10, intent);
			Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
			finish();
			}
         }
			
		public void onResume() {
			super.onResume();
			MobclickAgent.onResume(this);
			}
		public void onPause() {
			super.onPause();
			 if (mRecorder != null) {
		            mRecorder.release();
		            mRecorder = null;
		        }
			MobclickAgent.onPause(this);
			}
		  class RecordButton extends Button {
		        boolean mStartRecording = true;
		        OnClickListener clicker = new OnClickListener() {
		            public void onClick(View v) {
		                onRecord(mStartRecording);
		                if (mStartRecording) {
		                    setText("停止录音");
		                    luzhigxinxi.setText("正在录音，再按一次停止录音");
		                } else {
		                    setText("开始录音");
		                    luzhigxinxi.setText("录音停止，再按一次重新录制");
		                }
		                mStartRecording = !mStartRecording;
		            }
		        };
		        public RecordButton(Context ctx) {
		            super(ctx);
		            setBackgroundResource(R.drawable.button);
		            setText("开始录音");
		            setOnClickListener(clicker);
		        }
		    }
		    
		    private void onRecord(boolean start) {
		        if (start) {
		            startRecording();
		        } else {
		            stopRecording();
		        }
		    }
		    
		    private void startRecording() {
		    	String sfile = getSDPath()+"/"+"mysound";
		    	File file = new File(sfile);
		    	if(!file.exists()){
		    		file.mkdirs();
		    	}
		    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		    	Date d = new Date();
		    	String s = format.format(d);
		    	mFileName = sfile+"/"+s+".amr";

		        mRecorder = new MediaRecorder();
		        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		        mRecorder.setOutputFile(mFileName);
		        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		        try {
		            mRecorder.prepare();
		        } catch (IOException e) {
		            Log.e(LOG_TAG, "prepare() failed");
		        }
		        mRecorder.start();
		        path=mFileName;
		    }
		    private void stopRecording() {
		        mRecorder.stop();
		        mRecorder.release();
		        mRecorder = null;
		    }
}
