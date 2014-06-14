package com.homework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.umeng.analytics.MobclickAgent;

import tool.GetImage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorkItemActivity extends Activity{
   private EditText title,deadline,content;
   private AutoCompleteTextView lesson;
   private Button save,back,edit;
   private DatePicker dp;
   private ArrayAdapter<CharSequence> kechengAdapter=null;
   String  stitle;
   int position;
   boolean b = false;
	private ImageView image,addShare;
    private PlayButton   mPlayButton = null;
	int type;
	int input;
	String path;
	Intent intent;
	private MediaPlayer   mPlayer = null;
	private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private TextView bofangxinxi;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.workitem);
       title=(EditText) super.findViewById(R.id.checkworktitle);
       deadline=(EditText) super.findViewById(R.id.checkworkdeadline);
       content=(EditText) super.findViewById(R.id.checkworkcontent);
       lesson=(AutoCompleteTextView) super.findViewById(R.id.checkworklesson);
       edit=(Button) super.findViewById(R.id.editcheckwork);
       back=(Button) super.findViewById(R.id.backcheckwork);
       save=(Button) super.findViewById(R.id.saveEditwork);
       dp=(DatePicker) super.findViewById(R.id.editworkdate);
       image=(ImageView) super.findViewById(R.id.workImage);
       LinearLayout ll=(LinearLayout) super.findViewById(R.id.lout);
       mPlayButton = new PlayButton(getApplicationContext());
       ll.addView(mPlayButton,
           new LinearLayout.LayoutParams(
               ViewGroup.LayoutParams.WRAP_CONTENT,
               ViewGroup.LayoutParams.WRAP_CONTENT,
               0));
      
       bofangxinxi=new TextView(getApplicationContext());
       bofangxinxi.setTextSize(22);
       bofangxinxi.setVisibility(View.GONE);
       ll.addView(bofangxinxi,
               new LinearLayout.LayoutParams(
                   ViewGroup.LayoutParams.WRAP_CONTENT,
                   ViewGroup.LayoutParams.WRAP_CONTENT,
                   0));
       mPlayButton.setVisibility(View.GONE);
       addShare=(ImageView) super.findViewById(R.id.addShare);
       input=title.getInputType();
       title.setInputType(InputType.TYPE_NULL);
       deadline.setInputType(InputType.TYPE_NULL);
       content.setInputType(InputType.TYPE_NULL);
       lesson.setInputType(InputType.TYPE_NULL);
       intent=getIntent();
       stitle=intent.getStringExtra("title");
       title.setText(stitle);
       String dl=intent.getStringExtra("deadline");
       deadline.setText(dl);
       position=intent.getIntExtra("index", 0);
       String con=intent.getStringExtra("content");
       if(con!=null){
           content.setText(con); 
           content.setVisibility(View.VISIBLE);
           type=0;
       }
       String slesson=intent.getStringExtra("lesson");
       lesson.setText(slesson);
       String im=intent.getStringExtra("imgurl");
       if(im!=null&& !im.equals("")){
    	   Bitmap bmp=GetImage.get(im);
    	   if(bmp!=null){
    	   image.setImageBitmap(bmp);
    	   image.setVisibility(View.VISIBLE);
    	   type=1;
    	   }
       }
       final String bourl=intent.getStringExtra("yuyinurl");//获取
       if(bourl!=null&& !bourl.equals("")){
    	   mPlayButton.setVisibility(View.VISIBLE);
    	   bofangxinxi.setVisibility(View.VISIBLE);
    	   mFileName=bourl;
    	   path=bourl;
    	   type=2;
       }
       
       final String[] list=getIntent().getStringArrayExtra("alllesson");
       kechengAdapter=new ArrayAdapter<CharSequence>(this, 
         		 android.R.layout.select_dialog_item, list);
       kechengAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
       lesson.setAdapter(kechengAdapter);
   }
   public void save(View v){
	   //获取值进行存储
	   int iyear=0;
		int imonth=0;
		int iday=0;
		String sDate="";
		iyear=dp.getYear();
		imonth=dp.getMonth()+1;
		iday=dp.getDayOfMonth();
		sDate=String.valueOf(iyear)+"年"+String.valueOf(imonth)+"月"+
				String.valueOf(iday)+"日";

	Intent aintent=new Intent();
	
	aintent.putExtra("index", position);
	aintent.putExtra("deadline", sDate);
	aintent.putExtra("lesson",lesson.getText().toString() );
	String t=title.getText().toString();
	switch (type) {
	case 1://相册
		aintent.putExtra("imgurl", path);
		if(t.equals("")){
			t="图片记录";
		}
		break;
	case 2:
		//拍摄
		aintent.putExtra("imgurl", path);
		if(t.equals("")){
			t="图片记录";
		}
		break;
	case 3:
		//语音
		aintent.putExtra("yuyinurl", path);
		if(t.equals("")){
			t="语音记录";
		}
		break;
	default:
		aintent.putExtra("content", content.getText().toString());
		if(t.equals("")){
			t="文字记录";
		}
		break;
	}
	aintent.putExtra("workname",t);
	setResult(1, aintent);
	Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
	finish();
   }
   
   public void back(View v){
	   finish();
   }
   public void edit(View v){
	   title.setInputType(input);
	   content.setInputType(input);
	   lesson.setInputType(input);
	   title.setEnabled(true);
	   deadline.setVisibility(View.GONE);
	   content.setEnabled(true);
	   edit.setVisibility(View.GONE);
	   save.setVisibility(View.VISIBLE);
	   dp.setVisibility(View.VISIBLE);
	   lesson.setEnabled(true);
	   addShare.setVisibility(View.GONE);
	   switch (type) {
	   case 1://图片
		   addShare.setBackgroundResource(R.drawable.xiangce);
		   break;
	   case 2://音频
		   addShare.setBackgroundResource(R.drawable.yuyin);
		   break;
	   default:
		   addShare.setVisibility(View.GONE);
		break;
	}
   }
   @Override
   public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
   @Override
	public void onPause() {
		super.onPause();
		 if (mPlayer != null) {
	            mPlayer.release();
	            mPlayer = null;
	        }
		MobclickAgent.onPause(this);
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
     	 Bitmap bitmap=null;
     	 if(selectedImage==null){
     		Bundle extras = data.getExtras();
     	    if (extras != null) {
     	    	bitmap = extras.getParcelable("data");
     	    }
     	 }else{
         String[] filePathColumn = { MediaStore.Images.Media.DATA };
         Cursor cursor = getContentResolver().query(selectedImage,
                         filePathColumn, null, null, null);
         cursor.moveToFirst();
         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
         String picturePath = cursor.getString(columnIndex);
         cursor.close();
         
         bitmap = BitmapFactory.decodeFile(picturePath);   
         }   
         image.setImageBitmap(bitmap);
         image.setVisibility(View.VISIBLE);
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
	 class PlayButton extends Button {
	        boolean mStartPlaying = true;
	        OnClickListener clicker = new OnClickListener() {
	            public void onClick(View v) {
	                onPlay(mStartPlaying);
	                if (mStartPlaying) {
	                	setText("停止播放");
	                    bofangxinxi.setText("正在播放，再按一次停止播放");;
	                } else {
	                	setText("开始播放");
	                	bofangxinxi.setText("播放停止，按一次开始播放");
	                }
	                mStartPlaying = !mStartPlaying;
	            }
	        };
	        public PlayButton(Context ctx) {
	            super(ctx);
	            setText("开始播放");
	            setTextColor(Color.BLACK);
	            setBackgroundResource(R.drawable.button);
	            setOnClickListener(clicker);
	        }
	    }
	 private void onPlay(boolean start) {
	        if (start) {
	            startPlaying();
	        } else {
	            stopPlaying();
	        }
	    }
	    private void startPlaying() {
	        mPlayer = new MediaPlayer();
	        try {
	            mPlayer.setDataSource(mFileName);
	            mPlayer.prepare();
	            mPlayer.start();
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "prepare() failed");
	        }
	    }
	    private void stopPlaying() {
	        mPlayer.release();
	        mPlayer = null;
	    }
}
