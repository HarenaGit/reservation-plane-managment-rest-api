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
import mg.ny.adminui.data_model.AvionDataModel;

public class PlaneListAdapter extends ArrayAdapter<AvionDataModel> {

    private List<AvionDataModel> plane;
    private Filter filter;

    public PlaneListAdapter(Context context, ArrayList<AvionDataModel> plane){
        super(context, 0, plane);
        this.plane = plane;
    }


    public AvionDataModel getElement(int p){
       return plane.get(p);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AvionDataModel p = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search_plane, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.planeNameSearch);
        TextView id = (TextView) convertView.findViewById(R.id.planeIdSearch);
        TextView placeCount = (TextView) convertView.findViewById(R.id.planePlaceCountSearch);

        name.setText(String.valueOf(p.getType()));
        id.setText(String.valueOf(p.getNum_avion()));
        placeCount.setText("Places / Colonnes : " + String.valueOf(p.getNb_places()) + " / " + String.valueOf(p.getNb_colonnes()));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<AvionDataModel>(plane);
        return filter;
    }

    private class AppFilter<T> extends Filter {

        private ArrayList<AvionDataModel> sourceObjects;

        public AppFilter(List<AvionDataModel> objects) {
            sourceObjects = new ArrayList<AvionDataModel>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<AvionDataModel> filter = new ArrayList<AvionDataModel>();

                for (AvionDataModel object : sourceObjects) {
                    if(String.valueOf(object.getNum_avion()).contains(filterSeq) || object.getType().toLowerCase().contains(filterSeq) || String.valueOf(object.getNb_places()).contains(filterSeq) || String.valueOf(object.getNb_colonnes()).equals(filterSeq))
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
                add((AvionDataModel) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }


}
