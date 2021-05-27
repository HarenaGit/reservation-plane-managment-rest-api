 package mg.ny.adminui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import mg.ny.adminui.data.StaticDataGeneration;
import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.PlaneDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.view_logics.dashboard_view.fragment.DashboardFragment;
import mg.ny.adminui.view_logics.flight_view.activity.SearchFlightActivity;
import mg.ny.adminui.view_logics.flight_view.fragment.FlightFragment;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.public_component_view.interfaces.RemoveItemCallBack;
import mg.ny.adminui.view_logics.plane_view.activity.SearchActivity;
import mg.ny.adminui.view_logics.plane_view.fragment.PlaneFragment;
import mg.ny.adminui.view_logics.reservation_view.fragment.ReservationFragment;
import mg.ny.adminui.view_logics.visualization_view.fragment.VisualisationFragment;

 public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private ChipNavigationBar bottomMenu;
    private FragmentManager  fragmentManager;
    private TextView activityTitle;
    private Fragment fragment;
    private Fragment planeFragment;
    private ProgressBar progressBar;
    private ImageButton searchButton;
    private ArrayList<PlaneDataModel> planeData;
    private ArrayList<StaticHorizentalListModel> planeItem;
    private ArrayList<FlightDataModel> flightData;
    private ArrayList<StaticHorizentalListModel> flightItem;
    private ArrayList<ReservationDataModel> reservationData;
    private ArrayList<StaticHorizentalListModel> reservationItem;
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
    private void changeCurrentBottomMenuItemSelected(final int id){
        this.searchButton.setVisibility(View.GONE);
        final ImageButton searchBtn = this.searchButton;
        progressBar.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        switch (id){
            case R.id.Dashboard:
                if(planeData == null || planeItem == null){
                    planeData = planeData();
                    planeItem = planeItem();
                }
                RemoveItemCallBack rem = (int p) -> {
                    planeData.remove(p);
                    planeItem.remove(p);
                };
                fragment = new DashboardFragment(StaticDataGeneration.getPlaneDashItem(), planeData, rem);
                this.activityTitle.setText("Tableau de bord");
                break;
            case R.id.Plane:
                if(planeData == null || planeItem == null){
                    planeData = planeData();
                    planeItem = planeItem();
                }
                RemoveItemCallBack removeItemCallBack = (int p) -> {
                   planeData.remove(p);
                   planeItem.remove(p);
                };
                fragment = new PlaneFragment(planeItem, planeData, removeItemCallBack);
                this.activityTitle.setText("Avion");
                break;
            case R.id.Flight:
                if(flightData == null || flightItem == null){
                    flightData = StaticDataGeneration.getFlightData();
                    flightItem = StaticDataGeneration.getFlightItem();
                }
                RemoveItemCallBack removeFlightData = (int p) -> {
                    flightData.remove(p);
                    flightItem.remove(p);
                };
                fragment = new FlightFragment(flightItem, flightData, removeFlightData);
                this.activityTitle.setText("Vol");
                break;
            case R.id.Reservation:
                if(reservationData == null || reservationItem == null){
                    reservationData = StaticDataGeneration.getReservationData();
                    reservationItem = StaticDataGeneration.getReservationItem();
                }
                RemoveItemCallBack removeReservationData = (int p) -> {
                    reservationData.remove(p);
                 };
                fragment = new ReservationFragment(reservationItem, reservationData, removeReservationData);
                this.activityTitle.setText("Reservation");
                break;
            case R.id.Visualization:
                if(reservationData == null || reservationItem == null){
                    reservationData = StaticDataGeneration.getReservationData();
                    reservationItem = StaticDataGeneration.getReservationItem();
                }
                RemoveItemCallBack rm = (int p) -> {
                    reservationData.remove(p);
                };
                fragment = new VisualisationFragment(reservationItem, reservationData, rm);
                this.activityTitle.setText("Visualisation");
                break;
        }
        
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                            @Override
                            public void run() {
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            }
                        });
                        try {
                           synchronized (this){
                               future.get();
                               progressBar.setVisibility(View.GONE);
                               if(id != R.id.Dashboard && id != R.id.Visualization) searchBtn.setVisibility(View.VISIBLE);
                               else searchBtn.setVisibility(View.GONE);
                           }

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }, 2000);
    }

     private int getFlightDataPosition(String id){
         for(int i=0; i<flightData.size();i++){
             if(flightData.get(i).getId().equals(id)) return i;
         }
         return -1;
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_CANCELED) return;
        switch (resultCode){
            case RequestCode.REQUEST_CODE_ADD_PLANE:
                PlaneDataModel currentPlaneData = (PlaneDataModel) data.getParcelableExtra("data");
                addPlaneData(currentPlaneData);
                addPlaneItem(currentPlaneData.getName());
                break;
            case RequestCode.REQUEST_CODE_EDIT_PLANE:
                PlaneDataModel currentData = (PlaneDataModel) data.getParcelableExtra("data");
                int currentPosition = getPlaneDataPosition(currentData.getId());
                if(currentPosition>=0){
                    setPlaneData(currentPosition, currentData);
                    setPlaneItem(currentPosition, new StaticHorizentalListModel(currentData.getName()));
                }
                break;
            case RequestCode.REQUEST_CODE_ADD_FLIGHT:
                FlightDataModel currentFlightData = (FlightDataModel) data.getParcelableExtra("data");
                flightData.add(0, currentFlightData);
                flightItem.add(0, new StaticHorizentalListModel(currentFlightData.getId()));
                break;
            case RequestCode.REQUEST_CODE_EDIT_FLIGHT:
                FlightDataModel currentFlData = (FlightDataModel) data.getParcelableExtra("data");
                int currentFlPos = getFlightDataPosition(currentFlData.getId());
                if(currentFlPos>=0){
                    flightData.set(currentFlPos, currentFlData);
                    flightItem.set(currentFlPos, new StaticHorizentalListModel(currentFlData.getId()));
                }
                break;
            default:
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addPlaneData(PlaneDataModel o){
        planeData.add(0, o);
    }
    private void addPlaneItem(String text){
        planeItem.add(0, new StaticHorizentalListModel(text));
    }
    private void setPlaneData(int position, PlaneDataModel o){
        planeData.set(position, o);
    }
    private void setPlaneItem(int position, StaticHorizentalListModel o){
        planeItem.set(position, o);
    }
    private int getPlaneDataPosition(String id){
        for(int i=0; i<planeData.size();i++){
            if(planeData.get(i).getId().equals(id)) return i;
        }
        return -1;
    }
    private ArrayList<PlaneDataModel>  planeData(){
        return StaticDataGeneration.getPlaneData();
    }
    private ArrayList<StaticHorizentalListModel> planeItem(){
        return StaticDataGeneration.getPlaneHorizentalItem();
    }




}