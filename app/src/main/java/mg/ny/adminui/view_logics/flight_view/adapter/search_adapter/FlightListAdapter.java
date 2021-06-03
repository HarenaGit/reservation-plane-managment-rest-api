package mg.ny.adminui.view_logics.flight_view.adapter.search_adapter;

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
import mg.ny.adminui.data_model.FlightDataModel;



public class FlightListAdapter extends ArrayAdapter<FlightDataModel> {

private List<FlightDataModel> flight;
private Filter filter;



public FlightListAdapter(Context context, ArrayList<FlightDataModel> flight){
        super(context, 0, flight);
        this.flight = flight;
        }

private String detailText(String departureDate, String arrivalDate, String departureCity, String arrivalCity, Double cost){
    String txt = "Heure de départ : " + departureDate + "; heure d'arrivée : " + arrivalDate + "; Ville de départ : " + departureCity + "; Ville d'arrivée : " + arrivalCity + "; Frais : " + cost ;
    return txt;
}

public FlightDataModel getElement(int p){
    return flight.get(p);
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {

        FlightDataModel p = getItem(position);

        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search_flight, parent, false);
        }

        TextView plane = (TextView) convertView.findViewById(R.id.flightPlaneSearch);
        TextView id = (TextView) convertView.findViewById(R.id.flightIdSearch);
        TextView detailSearch = (TextView) convertView.findViewById(R.id.detailFlightSearch);

        plane.setText(p.getType());
        id.setText(p.getNum_vol());
        detailSearch.setText(detailText(p.getHeure_depart(), p.getHeure_arrivee(), p.getVille_depart(), p.getVille_arrivee(), p.getFrais()));

        return convertView;
        }

@Override
public Filter getFilter() {
        if (filter == null)
        filter = new AppFilter<FlightDataModel>(flight);
        return filter;
        }

private class AppFilter<T> extends Filter {

    private ArrayList<FlightDataModel> sourceObjects;

    public AppFilter(List<FlightDataModel> objects) {
        sourceObjects = new ArrayList<FlightDataModel>();
        synchronized (this) {
            sourceObjects.addAll(objects);
        }
    }

    @Override
    protected FilterResults performFiltering(CharSequence chars) {
        String filterSeq = chars.toString().toLowerCase();
        FilterResults result = new FilterResults();
        if (filterSeq != null && filterSeq.length() > 0) {
            ArrayList<FlightDataModel> filter = new ArrayList<FlightDataModel>();

            for (FlightDataModel object : sourceObjects) {
                if(String.valueOf(object.getNum_vol()).toLowerCase().contains(filterSeq) || String.valueOf(object.getType()).toLowerCase().contains(filterSeq) || String.valueOf(object.getFrais()).toLowerCase().contains(filterSeq) || object.getVille_depart().toLowerCase().contains(filterSeq) || object.getVille_arrivee().toLowerCase().contains(filterSeq) ||  object.getHeure_depart().toLowerCase().contains(filterSeq) ||  object.getHeure_arrivee().toLowerCase().contains(filterSeq) )
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
            add((FlightDataModel) filtered.get(i));
        notifyDataSetInvalidated();
    }
}


}
