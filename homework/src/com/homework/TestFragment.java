package com.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import tool.SpecialAdapter;
import data.LessonList;
import data.Test;
import data.TestList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TestFragment extends Fragment{
	
	private static final String ARG_SECTION_NUMBER = "section_number";

	private ListView testList=null;
	private ImageButton add=null;
	private List<Map<String, Object>> tlist=null;
	private SpecialAdapter adpter=null;
	private TestList deal;
	public static Fragment newInstance(int sectionNumber) {
		TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TestFragment() {
    	deal=new TestList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mytest, container, false);
        testList=(ListView) rootView.findViewById(R.id.testArray);
        add=(ImageButton) rootView.findViewById(R.id.addTest);
        
        add.setOnClickListener(new AddListener());
        tlist=init();
        adpter = new SpecialAdapter(getActivity(),tlist,//数据源   
                R.layout.testlistitem,
                new String[] {"ItemTime","ItemDay","ItemLesson"},    
                new int[] {R.id.testlisttime,R.id.testlistitemday,R.id.testlistitemlesson}  
            );  
        //adpter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1 , tlist);
        testList.setAdapter(adpter);

        testList.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
 			@Override
 			public void onCreateContextMenu(ContextMenu menu, View arg1,
 					ContextMenuInfo arg2) {
 				  menu.setHeaderTitle("处理该记录");//标题
 	              menu.add(0, 1, 0, "删除");//下拉菜单
 	              menu.add(0,2,0,"编辑");
             }
          });
         return rootView;
     }
     

	private List<Map<String, Object>> init() {
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		if(deal.getTestList().size()==0){
			Map<String, Object> map = new HashMap<String, Object>();
	 		map.put("ItemLesson", "近期没有考试");
	 		map.put("ItemTime","");
	 		map.put("ItemDay", "");
	 		list.add(map);
		}else{
			list.clear();
			for(int i=0;i<deal.getTestList().size();i++){
				Test te=deal.getTestList().get(i);
				Map<String, Object> map = new HashMap<String, Object>();
		 		map.put("ItemLesson", te.getLesson());
		 		String[] datetime=te.getDate().split(" ");
		 		map.put("ItemTime",datetime[1]);
		 		map.put("ItemDay",datetime[0]);
		 		list.add(map);
			}
		}
		return list;
	}

	public boolean onContextItemSelected(MenuItem item){

      ContextMenuInfo info = item.getMenuInfo();
      AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) info;
      // 获取选中行位置
      int position = contextMenuInfo.position;
      // 获取问题内容
      Map<String, Object> map = tlist.get(position);
      String neirong=map.get("ItemLesson").toString();
      String day=map.get("ItemDay").toString();
      String time=map.get("ItemTime").toString();
      
      if(item.getItemId()==2){
    	  //编辑,
    	  if(neirong.equals("近期没有考试")){
    		  Toast.makeText(getActivity(),"你还没有考试，不能进行修改", Toast.LENGTH_SHORT).show();
    	  }else{
    	  	Intent intent=new Intent(getActivity(), TestItemActivity.class);
			intent.putExtra("lesson", neirong);
			intent.putExtra("time", time);
			intent.putExtra("day", day);
			intent.putExtra("index", position);		
			LessonList dealwithlesson=new LessonList();
			CharSequence[] alllesson=dealwithlesson.getAll();
			intent.putExtra("alllesson", alllesson);
			int select=dealwithlesson.getIndex(neirong);
			intent.putExtra("select", select);
			startActivityForResult(intent, 0);
    	  }
      }else{
    	  //删除
    	  if(neirong.equals("近期没有考试")){
    		  Toast.makeText(getActivity(),"你还没有考试，不能进行删除",Toast.LENGTH_SHORT).show();
    	  }else{
    	  tlist.remove(position);
    	  if(tlist.size()==0){
    		map = new HashMap<String, Object>();
  	 		map.put("ItemLesson", "近期没有考试");
  	 		map.put("ItemTime","");
  	 		map.put("ItemDay", "");
  	 		tlist.add(map);
    	  }
    	  adpter.notifyDataSetChanged();
    	  Test t=deal.getTestList().get(position);
    	  deal.delete(t);
    	  Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
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
    
    @Override
   	public void onActivityResult(int requestCode, int resultCode, Intent data) {
       	switch (resultCode) {
       	case 30://增加
       		Bundle b=data.getExtras();  
       		String lesson=b.getString("lesson");
      		 String time=b.getString("time");
      		 String day=b.getString("day");
       		 Map<String,Object> map=new HashMap<String, Object>();
       		 map.put("ItemLesson", lesson);
      		 map.put("ItemDay", day);
      		 map.put("ItemTime", time);      		
      		if(tlist.size()!=0){
				if(tlist.get(0).get("ItemLesson").equals("近期没有考试")){
						tlist.clear();
				}
      		}
       		tlist.add(map);
       		adpter.notifyDataSetChanged();
       		Test t=new Test();
       		t.setDate(day+" "+time);
       		t.setLesson(lesson);
       		deal.add(t);	
       	        break;
       	case 2: //编辑
       		 b=data.getExtras();  
       		 lesson=b.getString("lesson");
       		 int position=b.getInt("index");
       		 time=b.getString("time");
       		 day=b.getString("day");
       		 map=tlist.get(position);
       		 map.put("ItemLesson", lesson);
       		 map.put("ItemDay", day);
       		 map.put("ItemTime", time);
       		 adpter.notifyDataSetChanged();
       		 Test te=deal.getTestList().get(position);
       		 te.setDate(day+" "+time);
       		 te.setLesson(lesson);
       		 deal.modify(te);
       		 Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
       	default:
       	        break;
       	}
      
       }
    
    
    private class AddListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity().getApplicationContext(),"增加一个考试安排",Toast.LENGTH_SHORT).show();
			Intent intent=new Intent();
			intent.setClass(getActivity(), AddtestActivity.class);
			LessonList deatwithlesson=new LessonList();
			CharSequence[] alllesson=deatwithlesson.getAll();
			intent.putExtra("alllesson", alllesson);
			startActivityForResult(intent, 2);
			getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
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
