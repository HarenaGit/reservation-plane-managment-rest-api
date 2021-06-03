package mg.ny.adminui.view_logics.visualization_view.adapter.visualizationGridAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mg.ny.adminui.R;
import mg.ny.adminui.data_model.ReservationDataModel;

public class VisualisationGridAdapter extends ArrayAdapter<ReservationDataModel> {

    private ArrayList<ReservationDataModel> reservation;
    private Filter filter;

    public VisualisationGridAdapter(Context context, ArrayList<ReservationDataModel> reservation){
        super(context, 0, reservation);
        this.reservation =reservation;
    }

    public ReservationDataModel getElement(int position){
        return reservation.get(position);
    }

     public class Holder{
         TextView place;
         RelativeLayout container;
     }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.visualisation_list_item, parent, false);
        holder.place = (TextView) convertView.findViewById(R.id.textItem);
        holder.container = (RelativeLayout) convertView.findViewById(R.id.containerGrid);
        holder.place.setText(String.valueOf(position+1));
        ReservationDataModel p = reservation.get(position);
        if (p.getNum_place() == position+1) {
                holder.container.setBackgroundResource(R.drawable.box_container_active);
                holder.place.setTextColor(holder.place.getResources().getColor(R.color.white));
        }
        return convertView;
    }


}
