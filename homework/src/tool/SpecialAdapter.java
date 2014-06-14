package tool;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class SpecialAdapter extends SimpleAdapter {
    private int[] colors = new int[] {0x3045B97C,0x300000FF,0x30FF0000};

    public SpecialAdapter(Context context, List<Map<String, Object>> tlist, int resource, String[] from, int[] to) {
        super(context, tlist, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = super.getView(position, convertView, parent);
      int colorPos = position % colors.length;
      view.setBackgroundColor(colors[colorPos]);
      return view;
    }
}