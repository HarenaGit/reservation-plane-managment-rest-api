package mg.ny.adminui.view_logics.flight_view.adapter.spinner_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mg.ny.adminui.R;
import mg.ny.adminui.data_model.PlaneDataModel;


public class CustomSpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PlaneDataModel> items;

    public CustomSpinnerAdapter(Context context, ArrayList<PlaneDataModel> items){
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
        text.setText(items.get(position).getId() + "/" + items.get(position).getName());
        return convertView;
    }
}
