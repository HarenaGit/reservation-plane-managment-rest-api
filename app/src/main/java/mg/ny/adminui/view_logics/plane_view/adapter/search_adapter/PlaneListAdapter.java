package mg.ny.adminui.view_logics.plane_view.adapter.search_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mg.ny.adminui.R;
import mg.ny.adminui.data_model.PlaneDataModel;

public class PlaneListAdapter extends ArrayAdapter<PlaneDataModel> {

    private List<PlaneDataModel> plane;
    private Filter filter;

    public PlaneListAdapter(Context context, ArrayList<PlaneDataModel> plane){
        super(context, 0, plane);
        this.plane = plane;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlaneDataModel p = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search_plane, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.planeNameSearch);
        TextView id = (TextView) convertView.findViewById(R.id.planeIdSearch);
        TextView placeCount = (TextView) convertView.findViewById(R.id.planePlaceCountSearch);

        name.setText(p.getName());
        id.setText(p.getId());
        placeCount.setText("Nombres de place : " + p.getPlaneSize());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<PlaneDataModel>(plane);
        return filter;
    }

    private class AppFilter<T> extends Filter {

        private ArrayList<PlaneDataModel> sourceObjects;

        public AppFilter(List<PlaneDataModel> objects) {
            sourceObjects = new ArrayList<PlaneDataModel>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<PlaneDataModel> filter = new ArrayList<PlaneDataModel>();

                for (PlaneDataModel object : sourceObjects) {
                    if(object.getId().toLowerCase().contains(filterSeq) || object.getName().toLowerCase().contains(filterSeq) || object.getPlaneSize().toLowerCase().contains(filterSeq))
                       filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {

                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((PlaneDataModel) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }


}
