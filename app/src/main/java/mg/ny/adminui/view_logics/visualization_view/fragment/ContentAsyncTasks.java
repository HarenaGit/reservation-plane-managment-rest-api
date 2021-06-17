package mg.ny.adminui.view_logics.visualization_view.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mg.ny.adminui.ApiCallConfig;
import mg.ny.adminui.R;
import mg.ny.adminui.apiCall.Reservation;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.data_model.ReservationJsonDataModel;
import mg.ny.adminui.data_model.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.visualization_view.adapter.visualizationGridAdapter.VisualisationGridAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentAsyncTasks extends AsyncTask<Void, Boolean, Void> {

    private Context context;
    private GridView gridView;
    private VisualisationGridAdapter adapter;
    private RelativeLayout loader;
    private ArrayList<ReservationDataModel> data;
    private  Integer num_vol;
    private int availablePlace = 0;
    private int takenPlace = 0;
    private Integer nb_colonnes;
    private Integer nb_places;
    private View planed;
    private Call<ReservationJsonDataModel> call;

    public void cancelRequests(){
       if(call!=null) call.cancel();
    }

    public ContentAsyncTasks(Context context, ArrayList<ReservationDataModel> data, StaticHorizentalListModel h, View planed, VisualisationGridAdapter adapter, GridView gridView, RelativeLayout loader){
        this.context = context;
        this.gridView = gridView;
        this.adapter = adapter;
        this.loader = loader;
        this.data = data;
        this.num_vol = h.getNum_vol();
        this.nb_places = h.getNb_places();
        this.nb_colonnes = h.getNb_colonnes();
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
            Reservation r = ApiCallConfig.retrofit.create(Reservation.class);
            call = r.getReservation(num_vol);
            call.enqueue(new Callback<ReservationJsonDataModel>() {
                @Override
                public void onResponse(Call<ReservationJsonDataModel> call, Response<ReservationJsonDataModel> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                        publishProgress(false);
                        return;
                    }
                    publishProgress(true);
                    ArrayList<ReservationDataModel> currentReservData = response.body().getData();
                    currentReservData = setVisualisationData(currentReservData, nb_places);
                    adapter = new VisualisationGridAdapter(context, currentReservData);
                }

                @Override
                public void onFailure(Call<ReservationJsonDataModel> call, Throwable t) {
                    Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                    publishProgress(false);
                    return;
                }
            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ReservationDataModel getItem(int position){
        return adapter.getElement(position);
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        if(values[0]){
           TextView avPlace = planed.findViewById(R.id.availablePlace);
           TextView tkPlace = planed.findViewById(R.id.takenPlace);
           avPlace.setText("Place disponible : " + String.valueOf(availablePlace));
           tkPlace.setText("Place occupée : " + String.valueOf(takenPlace));
           gridView.setNumColumns(nb_colonnes);
            gridView.setAdapter(adapter);
            loader.setVisibility(View.GONE);
        }
        else{
            loader.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {

    }



    private ArrayList<ReservationDataModel> setVisualisationData(ArrayList<ReservationDataModel> d, int numberOfPlace){
        ArrayList<ReservationDataModel> r = new ArrayList<>();
        for(int i =0;i<numberOfPlace;i++){
            boolean isElement = false;
            for(ReservationDataModel obj : d){
                if(obj.getNum_place() == i+1){
                    r.add(obj);
                    takenPlace++;
                    isElement = true;
                }
            }
            if(!isElement){
                r.add(new ReservationDataModel());
                availablePlace++;
            }
        }
        return r;
    }

}
