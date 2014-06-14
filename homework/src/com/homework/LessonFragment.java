package com.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import tool.SpecialAdapter;
import data.Lesson;
import data.LessonList;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LessonFragment extends Fragment{
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ListView tablelist;
	private Button addLesson;
	private SpecialAdapter adapter;
	private List<Map<String, Object>> tlist=null;
	private LessonList deal;
	
	public static Fragment newInstance(int sectionNumber) {
		LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LessonFragment() {
    	deal=new LessonList();
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.mylesson, container, false);    
         tablelist=(ListView) rootView.findViewById(R.id.lesson_list);
         tlist=init();
         adapter=new SpecialAdapter(getActivity(),tlist ,R.layout.lessonitem,
        		 new String[] {"lessonName","lessonAddress","lessonXingqi","lessonJieci","teacher"},    
                 new int[] {R.id.lessonname,R.id.lessonclass,R.id.lessonitemxingqi,
        	 					R.id.lessonitemjieci,R.id.lessonitemteacher});
         tablelist.setAdapter(adapter);
         addLesson=(Button) rootView.findViewById(R.id.addLesson);
         addLesson.setOnClickListener(new AddListener());
         setListViewHeightBasedOnChildren(tablelist);
         tablelist.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
  			@Override
  			public void onCreateContextMenu(ContextMenu menu, View arg1,
  					ContextMenuInfo arg2) {
  				  menu.setHeaderTitle("处理该记录");//标题
  	              menu.add(0,1, 0, "修改");//下拉菜单
  	              menu.add(0,2,0,"删除");
              }
           });
        return rootView;
    }
    private List<Map<String, Object>> init() {
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	if(deal.getLessonList().size()==0){
    		Map<String, Object> map = new HashMap<String, Object>();
     		map.put("lessonName", "无");
     		map.put("lessonAddress","");
     		map.put("lessonXingqi", "");
     		map.put("lessonJieci", "");
     		map.put("teacher", "");
     		list.add(map);
    	}else{
    		list.clear();
    		for(int i=0;i<deal.getLessonList().size();i++){
    			Lesson lesson=deal.getLessonList().get(i);
    			Map<String, Object> map = new HashMap<String, Object>();
         		map.put("lessonName", lesson.getName());
         		map.put("lessonAddress",lesson.getClassroom());
         		map.put("lessonXingqi", lesson.getDay());
         		map.put("lessonJieci", lesson.getJieci());
         		map.put("teacher",lesson.getTeacher());
         		list.add(map);
    		}
    	}
		return list;
	}
    @Override
	public boolean onContextItemSelected(MenuItem item){

      ContextMenuInfo info = item.getMenuInfo();
      AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) info;
      // 获取选中行位置
      int position = contextMenuInfo.position;
      // 获取问题内容
      Map<String, Object> map=tlist.get(position);
      String xingqi=map.get("lessonXingqi").toString();
      String teacher=map.get("teacher").toString();
      String jieci=map.get("lessonJieci").toString();
      String address=map.get("lessonAddress").toString();
      String name=map.get("lessonName").toString();
      switch(item.getItemId()){
      case 1:
    	  //编辑,跳转
    	  if(name.equals("无")){
    		  Toast.makeText(getActivity(),"你没有课程哦，不能进行修改",Toast.LENGTH_SHORT).show();
    	  }else{
    	  	Intent intent=new Intent(getActivity(), LessonItemActivity.class);
			intent.putExtra("xingqi", xingqi);
			intent.putExtra("index", position);
			String[] jiecizu=jieci.split("--");
			intent.putExtra("jieci1", jiecizu[0]);
			intent.putExtra("jieci2", jiecizu[1]);
			intent.putExtra("name", name);
			intent.putExtra("address", address);
			intent.putExtra("teacher", teacher);
			startActivityForResult(intent, 0);
    	  }
			break;
      case 2:
    	  //删除
    	  if(name.equals("无")){
    		  Toast.makeText(getActivity(),"你没有课程哦，不能进行删除",Toast.LENGTH_SHORT).show();
    	  }else{
    	  Lesson l=deal.getLessonList().get(position);
    	  tlist.remove(position);
    	  if(tlist.size()==0){
    		map = new HashMap<String, Object>();
       		map.put("lessonName", "无");
       		map.put("lessonAddress","");
       		map.put("lessonXingqi", "");
       		map.put("lessonJieci", "");
       		map.put("teacher", "");
       		tlist.add(map);
    	  }
    	  adapter.notifyDataSetChanged();
    	  setListViewHeightBasedOnChildren(tablelist);
    	  deal.delete(l);
    	  Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
    	  }
    	  break;
      default:
    	  break;
      }
     
      return super.onContextItemSelected(item);
     }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
   
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (resultCode) {
    	case 20://新增
    		Bundle b=data.getExtras();  //data为B中回传的Intent
    		String jieci=b.getString("jieci");
    		String xingqi=b.getString("xingqi");
    		String name=b.getString("name");
    		String address=b.getString("address");
    		String teacher=b.getString("teacher");
    		if(name.equals("")){
    			name="未命名";
    		}
    		Map<String, Object> map=new HashMap<String, Object>();
    		map.put("lessonName", name);
    		map.put("lessonAddress", address);
    		map.put("lessonXingqi", xingqi);
    		map.put("lessonJieci", jieci);
    		map.put("teacher", teacher);
    		if(tlist.size()!=0){
    				if(tlist.get(0).get("lessonName").equals("无")){
    						tlist.clear();
    				}
    		}
    		tlist.add(map);
    		adapter.notifyDataSetChanged();
    		setListViewHeightBasedOnChildren(tablelist);
    		Lesson l=new Lesson();
    		l.setClassroom(address);
    		l.setDay(xingqi);
    		l.setJieci(jieci);
    		l.setName(name);
    		l.setTeacher(teacher);
    		deal.add(l);
    	        break;
    	case 5://修改
    		b=data.getExtras();  //data为B中回传的Intent
    		jieci=b.getString("jieci");
    		xingqi=b.getString("xingqi");
    		name=b.getString("name");
    		address=b.getString("address");
    		teacher=b.getString("teacher");
    		int position=b.getInt("index");
    		map=tlist.get(position);
    		map.put("lessonName", name);
    		map.put("lessonAddress", address);
    		map.put("lessonXingqi", xingqi);
    		map.put("lessonJieci", jieci);
    		map.put("teacher", teacher);
    		tlist.set(position,map);
    		adapter.notifyDataSetChanged();
    		l=deal.getLessonList().get(position);
    		l.setClassroom(address);
    		l.setDay(xingqi);
    		l.setJieci(jieci);
    		l.setName(name);
    		l.setTeacher(teacher);
    		deal.modify(l);
    		break;
    	default:
    	        break;
    	}
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen"); 
    } 
    class AddListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			Intent bintent = new Intent(getActivity(), AddlessonActivity.class);
			startActivityForResult(bintent,0); 
			getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
    
    }
    }
    
