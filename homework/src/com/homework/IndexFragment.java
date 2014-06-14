package com.homework;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.GetLoginUserParam;
import com.umeng.analytics.MobclickAgent;

import data.Homework;
import data.Lesson;
import data.LessonList;
import data.Test;
import data.TestList;
import data.WorkList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.media.Image;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class IndexFragment extends Fragment {
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ListView workList=null;
	private ListView testList=null;
	private ArrayList<String> wlist=new ArrayList<String>();
	private ArrayList<String> tlist=new ArrayList<String>();
	private TableLayout tableLayout;
	private ArrayAdapter<String> adapter,tad;
	private TextView name,myInfo;
	private LinearLayout info=null;
	private ImageView touxiang=null;
	private RennClient renrenClient=null;
	private ProgressDialog mProgressDialog;
	private WorkList work;
	private TestList test;
	private LessonList lesson;
	
	public static Fragment newInstance(int sectionNumber) {
		IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public IndexFragment() {
    	work=new WorkList();
    	test=new TestList();
    	lesson=new LessonList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) { 	
    	if(work.toDoList.size()==0){
    		wlist.add("无");
    	}else{
    		wlist=new ArrayList<String>();
    		for(int i=0;i<work.getToDoList().size();i++){
        		wlist.add(work.getToDoList().get(i).getTitle());
        	}
    	}
    	
    	if(test.getTestList().size()==0){
    		tlist.add("近期没有考试");
    	}else{
    		tlist=new ArrayList<String>();
    		for(int i=0;i<test.getTestList().size();i++){
        		tlist.add(test.getTestList().get(i).getLesson());
        	}
    	}
    	
        View rootView = inflater.inflate(R.layout.index, container, false);
         workList = (ListView) rootView.findViewById(R.id.workList);
         adapter=new ArrayAdapter<String>(getActivity(), R.layout.listview_item, R.id.list_item, wlist);
         testList = (ListView) rootView.findViewById(R.id.testList);
         workList.setAdapter(adapter);
         
         tad=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1 , tlist);
         testList.setAdapter(tad);
         setListViewHeightBasedOnChildren(testList);
         setListViewHeightBasedOnChildren(workList);
         tableLayout=(TableLayout)rootView.findViewById(R.id.table);
         if(lesson.getLessonList().size()==0){
        	 addRow("无","无","无");
         }else{
        	 int count=0;
        	 for(int i=0;i<lesson.getLessonList().size();i++){
        	 Lesson lessonitem=lesson.getLessonList().get(i);
     		 if(lessonitem.getDay().equals(lesson.tomorrowDay())){
     			addRow(lessonitem.getJieci(),lessonitem.getName(),lessonitem.getClassroom());
     			count++;
     		}
     	}
           if(count==0){
      		    addRow("无","无","无");
      		 }
         }
//         info=(LinearLayout) rootView.findViewById(R.id.infolist);
//         myInfo=(TextView) rootView.findViewById(R.id.myInfo);
//         name=(TextView) rootView.findViewById(R.id.myname);
//         touxiang=(ImageView) rootView.findViewById(R.id.mytouxiang);
//         renrenClient=RennClient.getInstance(getActivity());
//         if(renrenClient.isLogin()&&info.getVisibility()!=View.VISIBLE){
//        	 getloginInfo();
//        	 info.setVisibility(View.VISIBLE);
//        	 myInfo.setVisibility(View.VISIBLE);
//         }
         workList.setOnItemClickListener(new OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> parent, View view,
 					int position, long id) {
 				//单击查看
 				String title=wlist.get(position);
 				if(title.equals("无")){
 					Toast.makeText(getActivity(), "你没有未完成任务", Toast.LENGTH_SHORT);
 				}else{
 				Intent intent=new Intent(getActivity(), WorkItemActivity.class);
 				intent.putExtra("title", title);
 				intent.putExtra("index", position);
 				LessonList deatwithlesson=new LessonList();
 				CharSequence[] alllesson=deatwithlesson.getAll();
 				intent.putExtra("alllesson", alllesson);
 				Homework worki=work.getToDoList().get(position);
 				intent.putExtra("deadline",worki.getDeadline());
 				intent.putExtra("content", worki.getContent());
 				intent.putExtra("lesson", worki.getLesson());
 				intent.putExtra("imgurl", worki.getImgPath());
 				intent.putExtra("yuyinurl", worki.getYuyinPath());
 				startActivityForResult(intent, 0);
 				}
 			}
 		});
         testList.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
			@Override
			public void onCreateContextMenu(ContextMenu menu, View arg1,
					ContextMenuInfo arg2) {
				  menu.setHeaderTitle("处理该记录");//标题
	              menu.add(0, 1, 0, "删除");//下拉菜单
	              menu.add(0,2,0,"编辑");
	              
            }
         });
         workList.setOnTouchListener(new OnTouchListener() {
 			float x, y, upx, upy;
 			public boolean onTouch(View view, MotionEvent event) {
 				if (event.getAction() == MotionEvent.ACTION_DOWN) {
 					x = event.getX();
 					y = event.getY();
 				}
 				if (event.getAction() == MotionEvent.ACTION_UP) {
 					upx = event.getX();
 					upy = event.getY();
 					int position1 = ((ListView) view).pointToPosition((int) x, (int) y);
 					int position2 = ((ListView) view).pointToPosition((int) upx,(int) upy);
 					
 					if (position1 == position2 && Math.abs(x - upx) > 10) {
 						View v = ((ListView) view).getChildAt(position1);
 						
 						removeListItem(v,position1);
 						return true;
 					}
 				}
 				return false;
 			}
         });
        return rootView;
    }
    
    @Override
   	public void onActivityResult(int requestCode, int resultCode, Intent data) {
       	switch (resultCode) {
       	case 1://作业列表修改
       		Bundle b=data.getExtras();  
       		String title=b.getString("workname");
       		int position=b.getInt("position");
       		String lesson=b.getString("lesson");
       		String deadline=b.getString("deadline");
       		String content=b.getString("content");
       		wlist.set(position,title);
       		Homework worki=work.getToDoList().get(position);
       		worki.setContent(content);
       		worki.setDeadline(deadline);
       		worki.setLesson(lesson);
       		worki.setTitle(title);
       		work.modify(worki);
       		LessonList lessondeal=new LessonList();
       		Integer i=lessondeal.getIndex(lesson);
       		if(i==null){
       			Lesson l=new Lesson();
       			l.setName(lesson);
       			l.setDay("");
       			l.setClassroom("");
       			l.setJieci(" -- ");
       			l.setTeacher("");
       			lessondeal.add(l);
       		}
       		adapter.notifyDataSetChanged();
       	        break;
       	case 2://考试列表修改
       		b=data.getExtras();
       		title=b.getString("lesson");
       		position=b.getInt("index");
       		String datetime=b.getString("day")+" "+b.getString("time");
       		Test t=test.getTestList().get(position);
       		t.setDate(datetime);
       		t.setLesson(title);
       		tlist.set(position, title);
       		test.modify(t);
       		tad.notifyDataSetChanged();
       		break;
       	default:
       	        break;
       	}
      
       }
    
    
    public void getloginInfo(){
    	 GetLoginUserParam param5 = new GetLoginUserParam();
//         if (mProgressDialog == null) {
//             mProgressDialog = new ProgressDialog(getActivity());
//             mProgressDialog.setCancelable(true);
//             mProgressDialog.setTitle("请等待");
//             mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
//             mProgressDialog.setMessage("正在获取人人账号信息");
//             mProgressDialog.show();
//         }
         try {
             renrenClient.getRennService().sendAsynRequest(param5, new CallBack() {    
                 @Override
                 public void onSuccess(RennResponse response) {
                	 dealwithmyInfo(response);
                     //response.toString();
                    // Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_SHORT).show();  
                     if (mProgressDialog != null) {
                         mProgressDialog.dismiss();
                         mProgressDialog = null;
                     }                           
                 }
                 
				@Override
                 public void onFailed(String errorCode, String errorMessage) {
                
                     Toast.makeText(getActivity(), "获取失败"+errorMessage, Toast.LENGTH_SHORT).show();
                     if (mProgressDialog != null) {
                         mProgressDialog.dismiss();
                         mProgressDialog = null;
                     }                            
                 }
             });
         } catch (RennException e1) {
             e1.printStackTrace();
         }
    }
    
    private void dealwithmyInfo(RennResponse response) {
    	JSONObject user=null;
    	 try {
    		 	 user=response.getResponseObject();
	             //String id = user.getString("id");
	             String iname = user.getString("name");
	             JSONArray imagelist = (JSONArray) user.get("avatar");
	             name.setText(iname);
	             JSONObject image=(JSONObject)imagelist.get(0);
	             final String tinyurl=image.getString("url");
	             new DownloadImageTask().execute(tinyurl);
	             
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
	}
    public boolean onContextItemSelected(MenuItem item){//考试列表
     ContextMenuInfo info = item.getMenuInfo();
     AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) info;
     // 获取选中行位置
     int position = contextMenuInfo.position;
     String lessoni = tlist.get(position);
     if(item.getItemId()==2){
   	  //编辑,跳转
    	 if(lessoni.equals("近期没有考试")){
    		Toast.makeText(getActivity(),"你没有考试哦，不能修改", Toast.LENGTH_SHORT).show();
    	 }else{
    		 Test t=test.getTestList().get(position);
    	 Intent intent=new Intent(getActivity(), TestItemActivity.class);
    	 String[] datetime=t.getDate().split(" ");
    	 intent.putExtra("lesson", lessoni);
		 intent.putExtra("time", datetime[1]);
		 intent.putExtra("day", datetime[0]);
		 intent.putExtra("index", position);
		 CharSequence[] alllesson=lesson.getAll();
		 intent.putExtra("alllesson", alllesson);
		 int select=lesson.getIndex(lessoni);
		 intent.putExtra("select", select);
		 startActivityForResult(intent, 0);
    	 }
     }else{
   	  //删除
      if(lessoni.equals("近期没有考试")){
    	  Toast.makeText(getActivity(),"你没有考试哦，不能删除", Toast.LENGTH_SHORT).show();
      }else{
   	  tlist.remove(position);
   	  if(tlist.size()==0){
   		  tlist.add("近期没有考试");
   	  }
   	  adapter.notifyDataSetChanged();
   	  setListViewHeightBasedOnChildren(testList);
   	  test.delete(test.getTestList().get(position));//数据库删除
      }
     }
     return super.onContextItemSelected(item);
    }
       
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    
    private void addRow(String jieci,String name,String classroom){
    	TableRow tableRow=new TableRow(getActivity());
    	TextView time=new TextView(getActivity());
    	TextView kecheng=new TextView(getActivity());
    	TextView address=new TextView(getActivity());
    	time.setTextSize(20);
    	kecheng.setTextSize(20);
    	address.setTextSize(20);
    	time.setGravity(Gravity.CENTER);
    	kecheng.setGravity(Gravity.CENTER);
    	address.setGravity(Gravity.CENTER);
    	time.setText(jieci);
    	kecheng.setText(name);
    	address.setText(classroom);
    	tableRow.addView(time); 
    	tableRow.addView(kecheng);
    	tableRow.addView(address);
    	tableLayout.addView(tableRow,
    			new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    	}
    
    public void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  

        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  

        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);  
    } 
    /**
	 * 删除item，并播放动画
	 * @param rowView 播放动画的view
	 * @param positon 要删除的item位置
	 */
	protected void removeListItem(View rowView, final int positon) {
		
		final Animation animation = (Animation) AnimationUtils.loadAnimation(rowView.getContext(), R.layout.item_anim);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}

			public void onAnimationRepeat(Animation animation) {}

			public void onAnimationEnd(Animation animation) {
				if(!wlist.get(positon).equals("无")){
				wlist.remove(positon);
				if(wlist.size()==0){
					wlist.add("无");
				}
				adapter.notifyDataSetChanged();
				work.hasdone(work.getToDoList().get(positon));}
				animation.cancel();
				setListViewHeightBasedOnChildren(workList);
			}
		});
		rowView.startAnimation(animation);

	}
	private Drawable loadImageFromNetwork(String imageUrl)
	{
	 Drawable drawable = null;
	 try {
	  // 可以在这里通过文件名来判断，是否本地有此图片
	  drawable = Drawable.createFromStream(
	    new URL(imageUrl).openStream(), "image.jpg");
	 } catch (IOException e) {
	  Log.d("test", e.getMessage());
	 }
	 if (drawable == null) {
	  Log.d("test", "null drawable");
	 } else {
	  Log.d("test", "not null drawable");
	 }
	 
	 return drawable ;
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Drawable> 
    {
         
         protected Drawable doInBackground(String... urls) {
             return loadImageFromNetwork(urls[0]);
         }

         protected void onPostExecute(Drawable result) {
             touxiang.setImageDrawable(result);
         }
    }
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("MainScreen"); 
	}
}
