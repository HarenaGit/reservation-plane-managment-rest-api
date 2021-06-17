 package mg.ny.adminui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

import mg.ny.adminui.apiCall.Avion;
import mg.ny.adminui.apiCall.Reservation;
import mg.ny.adminui.apiCall.Vol;
import mg.ny.adminui.data.StaticDataGeneration;
import mg.ny.adminui.data_model.AvionJsonDataModel;
import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.data_model.ReservVolJsonDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.data_model.VolJsonDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.view_logics.dashboard_view.fragment.DashboardFragment;
import mg.ny.adminui.view_logics.flight_view.activity.SearchFlightActivity;
import mg.ny.adminui.view_logics.flight_view.fragment.FlightFragment;
import mg.ny.adminui.data_model.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.public_component_view.interfaces.RemoveItemCallBack;
import mg.ny.adminui.view_logics.plane_view.activity.SearchActivity;
import mg.ny.adminui.view_logics.plane_view.fragment.PlaneFragment;
import mg.ny.adminui.view_logics.reservation_view.fragment.ReservationFragment;
import mg.ny.adminui.view_logics.visualization_view.fragment.VisualisationFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ChipNavigationBar bottomMenu;
    private FragmentManager  fragmentManager;
    private TextView activityTitle;
    private Fragment fragment;
    private Fragment planeFragment;
    private ProgressBar progressBar;
    private ImageButton searchButton;
    private ArrayList<AvionDataModel> planeData;
    private ArrayList<StaticHorizentalListModel> planeItem;
    private ArrayList<FlightDataModel> flightData;
    private ArrayList<StaticHorizentalListModel> flightItem;
    private ArrayList<ReservationDataModel> reservationData;
    private ArrayList<StaticHorizentalListModel> reservationItem;
    private Integer sleepThreadTime = 1500;
    private int currentFragramentId;
    private GetAvion getAvion;
    private Call<AvionJsonDataModel> callGetAvion;
    private GetVol getVol;
    private Call<VolJsonDataModel> callGetVol;
    private GetReservVol getReservVol;
    private Call<ReservVolJsonDataModel> callGetReservVol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        setContentView(R.layout.activity_main);
        this.progressBar = findViewById(R.id.spin_kit);
        this.activityTitle = findViewById(R.id.activity_title);
        this.bottomMenu = findViewById(R.id.bottom_menu);
        this.searchButton = findViewById(R.id.searchButton);
        if(savedInstanceState == null){
            this.bottomMenu.setItemSelected(R.id.Dashboard, true);
            fragmentManager = getSupportFragmentManager();
            if(planeData == null || planeItem == null){
                planeData = planeData();
                planeItem = planeItem();
            }
            RemoveItemCallBack rem = (int p) -> {
                planeData.remove(p);
                planeItem.remove(p);
            };
            fragment = new DashboardFragment(StaticDataGeneration.getPlaneDashItem(), planeData, rem);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            progressBar.setVisibility(View.GONE);
        }
        this.bottomMenu.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                changeCurrentBottomMenuItemSelected(id);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityTitle.getText().equals("Avion")){
                    Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                    searchActivity.putParcelableArrayListExtra("data", planeData);
                    startActivityForResult(searchActivity, RequestCode.REQUEST_CODE_SEARCH_PLANE);
                }
                if(activityTitle.getText().equals("Vol")){
                    Intent searchFAct = new Intent(getApplicationContext(), SearchFlightActivity.class);
                    searchFAct.putParcelableArrayListExtra("data", flightData);
                    startActivityForResult(searchFAct, RequestCode.REQUEST_CODE_SEARCH_PLANE);
                }

            }
        });

    }

    private void requestCancel(){
       if(callGetAvion != null) callGetAvion.cancel();
       if(getAvion!=null) getAvion.cancel(true);
       if(callGetVol!=null) callGetVol.cancel();
       if(getVol!=null) getVol.cancel(true);
       if(callGetReservVol != null) callGetReservVol.cancel();
       if(getReservVol !=null) getReservVol.cancel(true);
    }
    private void changeCurrentBottomMenuItemSelected(final int id){
        currentFragramentId = id;
        this.searchButton.setVisibility(View.GONE);
        final ImageButton searchBtn = this.searchButton;
        progressBar.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        requestCancel();
        switch (id){
            case R.id.Dashboard:
                this.activityTitle.setText("Tableau de bord");
                RemoveItemCallBack rem = (int p) -> {
                    planeData.remove(p);
                    planeItem.remove(p);
                };
                fragment = new DashboardFragment(StaticDataGeneration.getPlaneDashItem(), planeData, rem);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                progressBar.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                break;
            case R.id.Plane:
                this.activityTitle.setText("Avion");
               getAvion =  new GetAvion();
               getAvion.execute();
                return;
            case R.id.Flight:
                this.activityTitle.setText("Vol");
                getVol = new GetVol();
                getVol.execute();
                return;
            case R.id.Reservation:
                this.activityTitle.setText("Reservation");
                getReservVol = new GetReservVol();
                getReservVol.execute();
                return;
            case R.id.Visualization:
                this.activityTitle.setText("Visualisation");
                getReservVol = new GetReservVol();
                getReservVol.execute();
                break;
        }
    }
    private int getFlightDataPosition(Integer id){
         for(int i=0; i<flightData.size();i++){
             if(flightData.get(i).getNum_vol().equals(id)) return i;
         }
         return -1;
     }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_CANCELED) return;
        switch (resultCode){
            case RequestCode.REQUEST_CODE_ADD_PLANE:
                AvionDataModel currentPlaneData = (AvionDataModel) data.getParcelableExtra("data");
                addPlaneData(currentPlaneData);
                addPlaneItem(currentPlaneData.getType());
                break;
            case RequestCode.REQUEST_CODE_EDIT_PLANE:
                AvionDataModel currentData = (AvionDataModel) data.getParcelableExtra("data");
                int currentPosition = getPlaneDataPosition(currentData.getNum_avion());
                if(currentPosition>=0){
                    setPlaneData(currentPosition, currentData);
                    setPlaneItem(currentPosition, new StaticHorizentalListModel(currentData.getType()));
                }
                break;
            case RequestCode.REQUEST_CODE_ADD_FLIGHT:
                FlightDataModel currentFlightData = (FlightDataModel) data.getParcelableExtra("data");
                flightData.add(0, currentFlightData);
                flightItem.add(0, new StaticHorizentalListModel(currentFlightData));
                break;
            case RequestCode.REQUEST_CODE_EDIT_FLIGHT:
                FlightDataModel currentFlData = (FlightDataModel) data.getParcelableExtra("data");
                int currentFlPos = getFlightDataPosition(currentFlData.getNum_vol());
                if(currentFlPos>=0){
                    flightData.set(currentFlPos, currentFlData);
                    flightItem.set(currentFlPos, new StaticHorizentalListModel(currentFlData));
                }
                break;

            default:
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addPlaneData(AvionDataModel o){
        planeData.add(0, o);
    }
    private void addPlaneItem(String text){
        planeItem.add(0, new StaticHorizentalListModel(text));
    }
    private void setPlaneData(int position, AvionDataModel o){
        planeData.set(position, o);
    }
    private void setPlaneItem(int position, StaticHorizentalListModel o){
        planeItem.set(position, o);
    }
    private int getPlaneDataPosition(Integer id){
        for(int i=0; i<planeData.size();i++){
            if(planeData.get(i).getNum_avion().equals(id)) return i;
        }
        return -1;
    }
    private ArrayList<AvionDataModel>  planeData(){
        return StaticDataGeneration.getPlaneData();
    }
    private ArrayList<StaticHorizentalListModel> planeItem(){
        return StaticDataGeneration.getPlaneHorizentalItem();
    }


    private void updatePlaneView(){
        RemoveItemCallBack removeItemCallBack = (int p) -> {
            planeData.remove(p);
            planeItem.remove(p);
        };
        fragment = new PlaneFragment(planeItem, planeData, removeItemCallBack);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        if(currentFragramentId != R.id.Dashboard && currentFragramentId != R.id.Visualization) this.searchButton.setVisibility(View.VISIBLE);
        else this.searchButton.setVisibility(View.GONE);
    }

    private class GetAvion extends AsyncTask<Void, Boolean, Void>{

        @Override
        protected void onPreExecute(){
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Avion av = ApiCallConfig.retrofit.create(Avion.class);
                callGetAvion = av.getAvion();
                callGetAvion.enqueue(new Callback<AvionJsonDataModel>() {
                    @Override
                    public void onResponse(Call<AvionJsonDataModel> call, Response<AvionJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        planeData = new ArrayList<>();
                        planeItem = new ArrayList<>();
                        planeData = response.body().getData();
                        for(int i=0;i<planeData.size();i++){
                            planeItem.add(new StaticHorizentalListModel(planeData.get(i)));
                        }
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<AvionJsonDataModel> call, Throwable t) {
                        Toast.makeText(MainActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                        publishProgress(false);
                        return;
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Boolean... value){
            if(value[0]){
                updatePlaneView();
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
            progressBar.setVisibility(View.GONE);
        }
    }

     private void updateVolView(){

         RemoveItemCallBack removeFlightData = (int p) -> {
             flightData.remove(p);
             flightItem.remove(p);
         };
         fragment = new FlightFragment(flightItem, flightData, removeFlightData);
         fragmentManager = getSupportFragmentManager();
         fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
         if(currentFragramentId != R.id.Dashboard && currentFragramentId != R.id.Visualization) this.searchButton.setVisibility(View.VISIBLE);
         else this.searchButton.setVisibility(View.GONE);
     }

     private class GetVol extends AsyncTask<Void, Boolean, Void>{

         @Override
         protected void onPreExecute(){
             progressBar.setVisibility(View.VISIBLE);
         }
         @Override
         protected Void doInBackground(Void... voids) {
             try {
                 Thread.sleep(sleepThreadTime);
                 Vol vol = ApiCallConfig.retrofit.create(Vol.class);
                 callGetVol = vol.getVol();
                 callGetVol.enqueue(new Callback<VolJsonDataModel>() {
                     @Override
                     public void onResponse(Call<VolJsonDataModel> call, Response<VolJsonDataModel> response) {
                         if(!response.isSuccessful()){
                             Toast.makeText(MainActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                             publishProgress(false);
                             return;
                         }
                         flightData = new ArrayList<>();
                         flightItem = new ArrayList<>();
                         flightData = response.body().getData();
                         for(int i=0;i<flightData.size();i++){
                             flightItem.add(new StaticHorizentalListModel(flightData.get(i)));
                         }
                         publishProgress(true);
                     }

                     @Override
                     public void onFailure(Call<VolJsonDataModel> call, Throwable t) {
                         Toast.makeText(MainActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                         publishProgress(false);
                         return;
                     }
                 });

             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

             return null;
         }
         @Override
         protected void onProgressUpdate(Boolean... value){
             if(value[0]){
                 updateVolView();
             }

         }
         @Override
         protected void onPostExecute(Void aVoid){
             progressBar.setVisibility(View.GONE);
         }
     }

     private void updateReservAndVizView(){
         RemoveItemCallBack removeReservationData = (int p) -> {
         };
         switch (currentFragramentId){
             case R.id.Reservation:
                 fragment = new ReservationFragment(reservationItem, reservationData, removeReservationData);
                 break;
             case R.id.Visualization:
                 fragment = new VisualisationFragment(reservationItem, reservationData, removeReservationData);
                 break;
         }
         fragmentManager = getSupportFragmentManager();
         fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
         if(currentFragramentId != R.id.Dashboard && currentFragramentId != R.id.Visualization && currentFragramentId != R.id.Reservation) this.searchButton.setVisibility(View.VISIBLE);
         else this.searchButton.setVisibility(View.GONE);
     }

     private class GetReservVol extends AsyncTask<Void, Boolean, Void>{

         @Override
         protected void onPreExecute(){
             progressBar.setVisibility(View.VISIBLE);
         }
         @Override
         protected Void doInBackground(Void... voids) {
             try {
                 Thread.sleep(sleepThreadTime);
                 Reservation r = ApiCallConfig.retrofit.create(Reservation.class);
                 callGetReservVol = r.getReservationByVol();;
                 callGetReservVol.enqueue(new Callback<ReservVolJsonDataModel>() {
                     @Override
                     public void onResponse(Call<ReservVolJsonDataModel> call, Response<ReservVolJsonDataModel> response) {
                         if(!response.isSuccessful()){
                             Toast.makeText(MainActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                             publishProgress(false);
                             return;
                         }
                         reservationItem = new ArrayList<>();
                         for(int i=0;i<response.body().getData().size();i++){
                             reservationItem.add(new StaticHorizentalListModel(response.body().getData().get(i)));
                         }
                         publishProgress(true);
                     }

                     @Override
                     public void onFailure(Call<ReservVolJsonDataModel> call, Throwable t) {
                         Toast.makeText(MainActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                         publishProgress(false);
                         return;
                     }
                 });

             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

             return null;
         }
         @Override
         protected void onProgressUpdate(Boolean... value){
             if(value[0]){
                 updateReservAndVizView();
             }

         }
         @Override
         protected void onPostExecute(Void aVoid){
             progressBar.setVisibility(View.GONE);
         }
     }
}