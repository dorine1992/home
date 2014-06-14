package com.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ShareFragment extends Fragment{
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ListView list=null;
	private SimpleAdapter adapter=null;
	public static List<Map<String,Object>> maplist=null;
	
	public static Fragment newInstance(int sectionNumber) {
		ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ShareFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.share, container, false);
    	list=(ListView) rootView.findViewById(R.id.myShare);
    	maplist=getData();
    	adapter=new SimpleAdapter(getActivity(), maplist, R.layout.shareitem,
    			 new String[] {"ItemTitle","ItemDate","ItemNum"},    
                 new int[] {R.id.shareItemtitle,R.id.sharedate,R.id.commentnum});
    	list.setAdapter(adapter);
    	return rootView;
    }
    private List<Map<String, Object>> getData() {
    	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		return list;
	}
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
