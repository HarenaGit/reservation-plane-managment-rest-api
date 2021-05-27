package mg.ny.adminui.view_logics.reservation_view.adapter.spiner_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mg.ny.adminui.R;


public class NumPlaceSpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Integer> items;

    public NumPlaceSpinnerAdapter(Context context, ArrayList<Integer> items){
        this.context = context;
        this.items = items;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }
        TextView text = convertView.findViewById(R.id.spinnerTextItem);
        text.setText(String.valueOf(items.get(position)));
        return convertView;
    }
}
