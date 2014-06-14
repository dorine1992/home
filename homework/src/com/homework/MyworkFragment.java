package com.homework;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tool.ActionItem;
import tool.QuickAction;


import tool.SpecialAdapter;

import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.AccessControl;
import com.renn.rennsdk.param.ListAlbumParam;
import com.renn.rennsdk.param.PutAlbumParam;
import com.renn.rennsdk.param.PutStatusParam;
import com.renn.rennsdk.param.UploadPhotoParam;
import com.umeng.analytics.MobclickAgent;

import data.Homework;
import data.Lesson;
import data.LessonList;
import data.WorkList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MyworkFragment extends Fragment{
	private static final int[] ITEM_DRAWABLES = {R.drawable.text,R.drawable.xiangce, 
		R.drawable.photo,R.drawable.yuyin};
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ListView todoList=null;
	private ListView doneList=null;
	SpecialAdapter adpter=null;
	private ArrayAdapter<String> dadpter;
	private List<Map<String, Object>> tlist=null;
	private ProgressDialog mProgressDialog;
	private ArrayList<String> dlist;
	private WorkList deal;
	private int mSelectedRow = 0;
	private static Long albumId;
	private Boolean isExist = false;//判断相册是否存在
	RennClient rennClient;
    
	public static Fragment newInstance(int sectionNumber) {
		MyworkFragment fragment = new MyworkFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyworkFragment() {
    	deal=new WorkList();
    	dlist=deal.getDoneName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	rennClient=RennClient.getInstance(getActivity());
        View rootView = inflater.inflate(R.layout.myhomework, container, false);
        
        todoList = (ListView) rootView.findViewById(R.id.todoList);
        doneList = (ListView) rootView.findViewById(R.id.doneList);
         tlist=getData();
         
         adpter = new SpecialAdapter(getActivity(),tlist,//数据源   
                 R.layout.worklistitem,
                 new String[] {"ItemTitle","ItemDead"},    
                 new int[] {R.id.worklistitemtitle,R.id.worklistitemDead}  
             );  
         
         todoList.setAdapter(adpter);
         dadpter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1 , dlist);
         doneList.setAdapter(dadpter);
         setListViewHeightBasedOnChildren(todoList);
         setListViewHeightBasedOnChildren(doneList);
         
         ActionItem delete = new ActionItem(1, "删除", getResources().getDrawable(R.drawable.delete));
         ActionItem look = new ActionItem(2, "查看详情", getResources().getDrawable(R.drawable.xiangqing));
         //ActionItem renren = new ActionItem(3, "分享到人人", getResources().getDrawable(R.drawable.renren));
         ActionItem hasdone=new ActionItem(4,"已完成", getResources().getDrawable(R.drawable.wancheng));
         final QuickAction mQuickAction 	= new QuickAction(getActivity());
 		mQuickAction.addActionItem(look);
 		mQuickAction.addActionItem(delete);
 		mQuickAction.addActionItem(hasdone);
 		//mQuickAction.addActionItem(renren);
 		//setup the action item click listener
 		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
 					@Override
 					public void onItemClick(QuickAction quickAction, int pos, int actionId) {
 						//ActionItem actionItem = quickAction.getActionItem(pos);
 						menuItemClick(actionId,mSelectedRow);
 					}
 				});
 		
 		 todoList.setOnItemClickListener(new OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> arg0, View v, int position,
 					long id) {
 				mSelectedRow = position;
 				mQuickAction.show(v);
 			}
 		});
        
        tool.RayMenu rayMenu = (tool.RayMenu) rootView.findViewById(R.id.ray_menu);
        final int itemCount = ITEM_DRAWABLES.length;
 		for (int i = 0; i < itemCount; i++) {
 			ImageView item = new ImageView(getActivity());
 			item.setImageResource(ITEM_DRAWABLES[i]);
 			rayMenu.addItem(item, new AddTextListener(i));
 		}
 		
         return rootView;
     }
    
     private List<Map<String, Object>> getData() {
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	if(deal.getToDoList().size()==0){
    		Map<String, Object> map = new HashMap<String, Object>();
     		map.put("ItemTitle", "没有任务");
     		map.put("ItemDead","");
     		list.add(map);
    	}else{
    		list.clear();
    		for(int i=0;i<deal.getToDoList().size();i++){
        		Homework work=deal.getToDoList().get(i);
        		Map<String, Object> map = new HashMap<String, Object>();
         		map.put("ItemTitle", work.getTitle());
         		map.put("ItemDead",work.getDeadline());
         		list.add(map);
        	}
    	}
		return list;
	}
     
    private void Share(String message) {
    	RennClient rennClient=RennClient.getInstance(getActivity());
    	PutStatusParam putStatusParam = new PutStatusParam();
        putStatusParam.setContent(message);
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(true);
            mProgressDialog.setTitle("请等待");
            mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
            mProgressDialog.setMessage("正在发布状态");
            mProgressDialog.show();
        }
        try {
            rennClient.getRennService().sendAsynRequest(putStatusParam, new CallBack() {    
                
                @Override
                public void onSuccess(RennResponse response) {
                  
                    Toast.makeText(getActivity(), "状态发布成功", Toast.LENGTH_SHORT).show();  
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }                           
                }
                
                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    
                    Toast.makeText(getActivity(), "状态发布失败"+errorMessage, Toast.LENGTH_SHORT).show();
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }                            
                }
            });
        } catch (RennException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
	}

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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
    @Override
   	public void onActivityResult(int requestCode, int resultCode, Intent data) {
       	switch (resultCode) {
       	case 10://增加
       		Bundle b=data.getExtras();  
       		String title=b.getString("workname");
       		String deadline=b.getString("deadline");
       		String lesson=b.getString("lesson");
       		String content=b.getString("content");
       		String imgUrl=b.getString("imgurl");
       		String yuyinUrl=b.getString("yuyinurl");
       		Map<String,Object> map=new HashMap<String, Object>();
       		map.put("ItemTitle", title);
       		map.put("ItemDead", deadline);
       		if(tlist.size()!=0){
       			if(tlist.get(0).get("ItemTitle").equals("没有任务"))
       				tlist.clear();
       		}
       		tlist.add(map);
       		adpter.notifyDataSetChanged();      		
       		setListViewHeightBasedOnChildren(todoList);
       		
       		Homework newwork=new Homework();
       		newwork.setContent(content);
       		newwork.setDeadline(deadline);
       		newwork.setLesson(lesson);
       		newwork.setTitle(title);
       		newwork.setImgPath(imgUrl);
       		newwork.setYuyinPath(yuyinUrl);
       		deal.addTodo(newwork);
       		//查找lesson是否存在，不存在则新增
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
       	        break;
       	case 1://编辑
       		b=data.getExtras();  
       		title=b.getString("workname");
       		deadline=b.getString("deadline");
       		int position=b.getInt("index");
       		lesson=b.getString("lesson");
       		content=b.getString("content");
       		imgUrl=b.getString("imgurl");
       		yuyinUrl=b.getString("yuyinurl");
       		map=tlist.get(position);
       		map.put("ItemTitle", title);
       		map.put("ItemDead", deadline);
       		adpter.notifyDataSetChanged();
       		
       		Homework doitem=deal.getToDoList().get(position);
       		doitem.setContent(content);
       		doitem.setDeadline(deadline);
       		doitem.setLesson(lesson);
       		doitem.setTitle(title);
       		doitem.setImgPath(imgUrl);
       		doitem.setYuyinPath(yuyinUrl);
       		deal.modify(doitem);
       		setListViewHeightBasedOnChildren(todoList);
       	        break;
       	default:
       	        break;
       	}   
       }
   
    private class AddTextListener implements OnClickListener{
    	int position=0;
    	public AddTextListener(int i){
    		position=i;
    	}

		@Override
		public void onClick(View arg0) {
				Intent intent=new Intent();
				LessonList deatwithlesson=new LessonList();
				CharSequence[] alllesson=deatwithlesson.getAll();
				intent.putExtra("alllesson", alllesson);
				intent.putExtra("type", position);
				intent.setClass(getActivity(), AddworkActivity.class);
				startActivityForResult(intent, 1);
				getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
    	
    }
		public boolean menuItemClick(int id,int position) {
			 // 获取问题内容
		      Map<String, Object> map = tlist.get(position);
		      String neirong=map.get("ItemTitle").toString();
		      String deadline=map.get("ItemDead").toString();
		      switch(id){
		      case 2:
		    	  //编辑,跳转
		    	  if(neirong.equals("没有任务")){
		    		  Toast.makeText(getActivity(),"当前没有任务，你不可以进行修改哦",Toast.LENGTH_SHORT).show();
		    	  }else{
		    	  	Intent intent=new Intent(getActivity(), WorkItemActivity.class);
					intent.putExtra("title", neirong);
					intent.putExtra("index", position);
					intent.putExtra("deadline", deadline);
					LessonList deatwithlesson=new LessonList();
					CharSequence[] alllesson=deatwithlesson.getAll();
					intent.putExtra("alllesson", alllesson);
					Homework doitem=deal.getToDoList().get(position);
					intent.putExtra("content", doitem.getContent());
					intent.putExtra("lesson", doitem.getLesson());
					intent.putExtra("imgurl", doitem.getImgPath());
					intent.putExtra("yuyinurl", doitem.getYuyinPath());
					startActivityForResult(intent, 0);
		    	  }
					break;
		      case 1:
		    	  //删除
		    	  if(neirong.equals("没有任务")){
		    		  Toast.makeText(getActivity(),"当前没有任务，你不可以进行删除哦",Toast.LENGTH_SHORT).show();
		    	  }else{
		    	  tlist.remove(position);
		    	  deal.deleteToDo(deal.getToDoList().get(position));
		    	  if(tlist.size()==0){
		    		map = new HashMap<String, Object>();
		       		map.put("ItemTitle", "没有任务");
		       		map.put("ItemDead","");
		       		tlist.add(map);
		    	  }
		    	  adpter.notifyDataSetChanged();
		    	  setListViewHeightBasedOnChildren(todoList);
		    	  }
		    	  break;
		      case 3://分享
		    	  RennClient ren=RennClient.getInstance(getActivity());
		    	  if(ren.isLogin()){
		    		  if(neirong.equals("没有任务")){
		    		  Toast.makeText(getActivity(),"当前没有任务，你不可以进行分享哦",Toast.LENGTH_SHORT).show();
		    		  }else{
		    			  Homework workitem=deal.getToDoList().get(position);
		    			  //分享分类，
		    			 String content=workitem.getContent();
		    			  if(content!=null){//文字分享
		    			  if(content.equals("")) content="无";
		    			  String messsage="新作业发布: 题目为："+workitem.getTitle()+ " ;科目为："+workitem.getLesson()
		    			  		+" deadline:"+deadline +"   具体内容为:"+content;
		    			  Share(messsage);
		    			  }
		    			  String imgpath=workitem.getImgPath();
		    			  if(imgpath!=null && !imgpath.equals("")){
		    				  //图片分享
//		    				  getAlbumNum();
		    				  String descrip="题目为："+workitem.getTitle()+ " ;科目为："+workitem.getLesson()
				    			  		+"; deadline为:"+deadline ;
		    				  uploadPhoto(imgpath, descrip);
		    			  }
		    			  String yuyinurl=workitem.getYuyinPath();
		    			  if(yuyinurl!=null && !yuyinurl.equals("")){
		    				  //语音分享
		    				  Toast.makeText(getActivity(), "暂时不支持语音分享哦", Toast.LENGTH_SHORT).show();
		    			  }
		    	  	}
		    	  }else{
		    		  Toast.makeText(getActivity(),"你还没有登录,不可以进行分享哦",Toast.LENGTH_SHORT).show();
		    	  }
		    	  break;
		      case 4://已完成
		    	Homework w=deal.getToDoList().get(position);
		    	tlist.remove(position);
		    	if(tlist.size()==0){
		    		map = new HashMap<String, Object>();
		       		map.put("ItemTitle", "没有任务");
		       		map.put("ItemDead","");
		       		tlist.add(map);
		    	  }
		    	adpter.notifyDataSetChanged();
		    	deal.hasdone(w);
		    	if(dlist.size()!=0){
	       			if(dlist.get(0).equals("没有已完成的任务"))
	       				dlist.clear();
	       		}
		        dlist.add(w.getTitle());
		        dadpter.notifyDataSetChanged();
		        setListViewHeightBasedOnChildren(todoList);
		        setListViewHeightBasedOnChildren(doneList);
		      break;
		      default:
		    	  break;
		      }
			return false;
		}
		
		public void onResume() {
		    super.onResume();
		    MobclickAgent.onPageStart("MainScreen"); //统计页面
		}
		public void onPause() {
		    super.onPause();
		    MobclickAgent.onPageEnd("MainScreen"); 
		}
		public void uploadPhoto(String path,String descrip){
				UploadPhotoParam upp = new UploadPhotoParam();
				upp.setFile(new File(path));
				upp.setDescription(descrip);
				//此处做了修改
				upp.setAlbumId(albumId);
				if (mProgressDialog == null) {
	                mProgressDialog = new ProgressDialog(getActivity());
	                mProgressDialog.setCancelable(true);
	                mProgressDialog.setTitle("请等待");
	                mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
	                mProgressDialog.setMessage("正在上传照片");
	                mProgressDialog.show();
	            }
	            try {
	                rennClient.getRennService().sendAsynRequest(upp, new CallBack() {    

	                    @Override
	                    public void onSuccess(RennResponse response) {
	                        Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT)
	                                .show();
	                        if (mProgressDialog != null) {
	                            mProgressDialog.dismiss();
	                            mProgressDialog = null;
	                        }
	                    }

	                    @Override
	                    public void onFailed(String errorCode, String errorMessage) {
	                        Toast.makeText(getActivity(), "上传失败"+errorMessage, Toast.LENGTH_SHORT)
	                                .show();
	                        if (mProgressDialog != null) {
	                            mProgressDialog.dismiss();
	                            mProgressDialog = null;
	                        }
	                    }
	                });
	            } catch (RennException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }

		}
		}

