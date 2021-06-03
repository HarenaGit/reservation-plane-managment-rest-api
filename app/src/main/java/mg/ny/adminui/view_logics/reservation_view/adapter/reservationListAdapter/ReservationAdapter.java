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

        id.setText(p.getNum_reservation());
        place.setText(p.getNum_place());
       container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(detail.getText().equals("")) {
                    detail.setText("Reservé par : " + p.getNom_voayageur() + " le " + p.getDate_reservation());
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


}
