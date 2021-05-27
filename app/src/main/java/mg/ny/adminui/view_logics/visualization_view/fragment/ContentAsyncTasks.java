package mg.ny.adminui.view_logics.visualization_view.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mg.ny.adminui.R;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.view_logics.visualization_view.adapter.visualizationGridAdapter.VisualisationGridAdapter;

public class ContentAsyncTasks extends AsyncTask<Void, Boolean, Void> {

    private Context context;
    private GridView gridView;
    private VisualisationGridAdapter adapter;
    private RelativeLayout loader;
    private ArrayList<ReservationDataModel> data;
    private  String currentFlightId;
    private int availablePlace = 0;
    private int takenPlace = 0;
    private int numColumns = 6;
    private int numPlace = 100;
    private View planed;


    public ContentAsyncTasks(Context context, ArrayList<ReservationDataModel> data, String currentFlightId, View planed, VisualisationGridAdapter adapter,  GridView gridView,  RelativeLayout loader){
        this.context = context;
        this.gridView = gridView;
        this.adapter = adapter;
        this.loader = loader;
        this.data = data;
        this.currentFlightId = currentFlightId;
        this.planed = planed;
    }

    @Override
    protected void onPreExecute() {
        loader.setVisibility(View.VISIBLE);
    }



    @Override
    protected Void doInBackground(Void... voids) {

        try {
            Thread.sleep(1500);
            ArrayList<ReservationDataModel> currentReservData = getFilteredDataByFlightId(currentFlightId);
            currentReservData = setVisualisationData(currentReservData, numPlace);
            adapter = new VisualisationGridAdapter(this.context, currentReservData);
            publishProgress(true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ReservationDataModel getItem(int position){
        return adapter.getItem(position);
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        if(values[0]){
           TextView avPlace = planed.findViewById(R.id.availablePlace);
           TextView tkPlace = planed.findViewById(R.id.takenPlace);
           avPlace.setText("Place disponible : " + String.valueOf(availablePlace));
           tkPlace.setText("Place occupée : " + String.valueOf(takenPlace));
           gridView.setNumColumns(numColumns);
            gridView.setAdapter(adapter);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        loader.setVisibility(View.GONE);
    }

    private ArrayList<ReservationDataModel> getFilteredDataByFlightId(String flightId){
        flightId = flightId.toLowerCase();
        ArrayList<ReservationDataModel> filtered = new ArrayList<>();
        for(ReservationDataModel object : data){
            if(object.getFlightId().toLowerCase().contains(flightId)) filtered.add(object);
        }
        return filtered;
    }

    private ArrayList<ReservationDataModel> setVisualisationData(ArrayList<ReservationDataModel> d, int numberOfPlace){
        ArrayList<ReservationDataModel> r = new ArrayList<>();
        for(int i =0;i<numberOfPlace;i++){
            boolean isElement = false;
            for(ReservationDataModel obj : d){
                if(obj.getPlaceNumber().equals(String.valueOf(i+1))){
                    r.add(obj);
                    takenPlace++;
                    isElement = true;
                }
            }
            if(!isElement){
                r.add(new ReservationDataModel("", "", "","", "", "", ""));
                availablePlace++;
            }
        }
        return r;
    }

}
