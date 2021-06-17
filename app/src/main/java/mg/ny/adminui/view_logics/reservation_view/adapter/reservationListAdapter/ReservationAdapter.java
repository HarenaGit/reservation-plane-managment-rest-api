package mg.ny.adminui.view_logics.reservation_view.adapter.reservationListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mg.ny.adminui.R;
import mg.ny.adminui.data_model.ReservationDataModel;

public class ReservationAdapter extends ArrayAdapter<ReservationDataModel> {

    private List<ReservationDataModel> reservation;
    private Filter filter;

    public ReservationAdapter(Context context, ArrayList<ReservationDataModel> reservation){
        super(context, 0, reservation);
        this.reservation =reservation;
    }

    public ReservationDataModel getElement(int p){
        return  this.reservation.get(p);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ReservationDataModel p = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_list_item, parent, false);
        }

        TextView id = (TextView) convertView.findViewById(R.id.reservationId);
        TextView place = (TextView) convertView.findViewById(R.id.reservationPlace);
        TextView detail = (TextView) convertView.findViewById(R.id.reservationDetail);
        ImageView container = (ImageView) convertView.findViewById(R.id.reservationContainer);

        String numRsv = "Rsv-"+p.getNum_reservation();
        id.setText(numRsv);
        place.setText(String.valueOf(p.getNum_place()));
       container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(detail.getText().equals("")) {
                    detail.setText("Reservé par : " + p.getNom_voyageur() + ", le " + p.getDate_reservation());
                    container.setImageResource(R.drawable.ic_arrow_up);
                }
                else {
                    detail.setText("");
                    container.setImageResource(R.drawable.ic_arrow_down);
                }

            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<ReservationDataModel>(reservation);
        return filter;
    }

    private class AppFilter<T> extends Filter {

        private ArrayList<ReservationDataModel> sourceObjects;

        public AppFilter(List<ReservationDataModel> objects) {
            sourceObjects = new ArrayList<ReservationDataModel>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<ReservationDataModel> filter = new ArrayList<ReservationDataModel>();

                for (ReservationDataModel object : sourceObjects) {
                   if(String.valueOf(object.getNum_reservation()).contains(filterSeq) || object.getNom_voyageur().toLowerCase().contains(filterSeq) || String.valueOf(object.getNum_place()).contains(filterSeq) || object.getDate_reservation().toLowerCase().contains(filterSeq))
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
                add((ReservationDataModel) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }




}
