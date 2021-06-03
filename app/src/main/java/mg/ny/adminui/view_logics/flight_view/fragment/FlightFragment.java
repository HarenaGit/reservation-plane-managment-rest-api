package mg.ny.adminui.view_logics.flight_view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import mg.ny.adminui.ApiCallConfig;
import mg.ny.adminui.R;
import mg.ny.adminui.apiCall.Vol;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.view_logics.flight_view.activity.AddFlightActivity;
import mg.ny.adminui.view_logics.flight_view.activity.EditFlightActivity;
import mg.ny.adminui.view_logics.flight_view.activity.SearchFlightActivity;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListAdapter;
import mg.ny.adminui.data_model.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.public_component_view.interfaces.HorizentalListCallBack;
import mg.ny.adminui.view_logics.public_component_view.interfaces.RemoveItemCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  FlightFragment extends Fragment {

    private ArrayList<StaticHorizentalListModel> item ;
    private ArrayList<FlightDataModel> data;
    private Integer currentPosition = null;
    private TextView currentId;
    private TextView currentPlaneName;
    private TextView currentCost;
    private TextView currentDepartureDate;
    private TextView currentDepartureCity;
    private TextView currentArrivalDate;
    private TextView currentArrivalCity;

    private TextView flighNumber;
    private Dialog dialog;
    private RemoveItemCallBack removeItemCallBack;
    private LinearLayout contentDialog;
    private RelativeLayout loadingDialog;

    private DeleteVol deleteVol;
    private Call<Void> call;
    public FlightFragment(ArrayList<StaticHorizentalListModel> item, ArrayList<FlightDataModel> data, RemoveItemCallBack removeItemCallBack){
        this.item = item;
        this.data = data;
        this.removeItemCallBack = removeItemCallBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Context context;
    private Activity activity;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.context = activity;
        this.activity = activity;
    }



    private RecyclerView recyclerView;
    private StaticHorizentalListAdapter horizentalListAdapter;
    private LayoutInflater inflater;
    private ViewGroup container;
    private FlightDataModel currentFlightData;
    private View planeDetail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        return inflater.inflate(R.layout.fragment_plane, container, false);

    }

    private RelativeLayout planeContent;
    private View selectionIcon;
    private View planed;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        RelativeLayout planeFragement = view.findViewById(R.id.planeFragement);

        View planePage = inflater.inflate(R.layout.plane_page, container, false);
        planePage.findViewById(R.id.addPlaneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addActivity = new Intent(context, AddFlightActivity.class);
                startActivityForResult(addActivity, RequestCode.REQUEST_CODE_ADD_FLIGHT);
            }
        });
        planeFragement.addView(planePage);
        RelativeLayout planeList = view.findViewById(R.id.planeHList);
        View planeHorizentalList = inflater.inflate(R.layout.plane_horizental_list, container, false);
        planeList.addView(planeHorizentalList);
        planeContent = view.findViewById(R.id.planeContent);
        selectionIcon = inflater.inflate(R.layout.selection_icon, container, false);
        planeContent.addView(selectionIcon);
        planed = inflater.inflate(R.layout.flight_content, container, false);
        planed.findViewById(R.id.editCurrentPlane).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editActivity = new Intent(context, EditFlightActivity.class);
                editActivity.putExtra("data", currentFlightData);
                startActivityForResult(editActivity, RequestCode.REQUEST_CODE_EDIT_FLIGHT);
            }
        });
        recyclerView = view.findViewById(R.id.rv_plane);
        HorizentalListCallBack<RecyclerView.ViewHolder, Integer, Boolean, Integer> callbackHorizentalList = (RecyclerView.ViewHolder holder, Integer position, Boolean isFirstClicked) -> {
            if(isFirstClicked) {
                planeContent.removeView(selectionIcon);
                planeContent.addView(planed);
            }
            this.currentPosition = position;
            currentFlightData = data.get(position);
            currentPlaneName = view.findViewById(R.id.flightPlaneName);
            currentDepartureCity = view.findViewById(R.id.departureCity);
            currentDepartureDate = view.findViewById(R.id.departureDate);
            currentArrivalCity = view.findViewById(R.id.arrivalCity);
            currentArrivalDate = view.findViewById(R.id.arrivalDate);
            currentCost = view.findViewById(R.id.cost);

            currentCost.setText(String.valueOf(currentFlightData.getFrais()));
            currentDepartureDate.setText(currentFlightData.getHeure_depart());
            currentDepartureCity.setText(currentFlightData.getVille_depart());
            currentArrivalDate.setText(currentFlightData.getHeure_arrivee());
            currentArrivalCity.setText(currentFlightData.getVille_arrivee());
            currentPlaneName.setText(currentFlightData.getType());

            return 0;
        };
        recyclerView.setLayoutManager(new LinearLayoutManager( view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizentalListAdapter = new StaticHorizentalListAdapter(recyclerView, activity, item, callbackHorizentalList );
        recyclerView.setAdapter(horizentalListAdapter);
        flighNumber = view.findViewById(R.id.planeNumber);
        flighNumber.setText(String.valueOf(data.size()));
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.remove_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        MaterialButton yes = dialog.findViewById(R.id.acceptRemove);
        MaterialButton no = dialog.findViewById(R.id.declineRemove);
        contentDialog = dialog.findViewById(R.id.removeDialogContent);
        loadingDialog = dialog.findViewById(R.id.removeDialogLoading);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        planed.findViewById(R.id.removeCurrentPlane).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textDialog = dialog.findViewById(R.id.planeRemoveId);
                textDialog.setText("Vol numéro : " + currentFlightData.getNum_vol());
                dialog.show();
            }
        });
    }


    private int getFlightDataPosition(Integer id){
        for(int i=0; i<data.size();i++){
            if(data.get(i).getNum_vol().equals(id)) return i;
        }
        return -1;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent d)
    {
        super.onActivityResult(requestCode, resultCode, d);
        if(resultCode == Activity.RESULT_CANCELED) return;
        switch (resultCode){
            case RequestCode.REQUEST_CODE_ADD_FLIGHT:
                FlightDataModel fl = (FlightDataModel) d.getParcelableExtra("data");
                if(currentPosition != null && currentPosition == 0) horizentalListAdapter.setRow_index(1);
                horizentalListAdapter.notifyDataSetChanged();
                flighNumber.setText(String.valueOf(data.size()));
                Toast.makeText(context, "Vol ajouter avec succés", Toast.LENGTH_LONG).show();
                break;
            case RequestCode.REQUEST_CODE_EDIT_FLIGHT:
                FlightDataModel currentD = (FlightDataModel) d.getParcelableExtra("data");
                horizentalListAdapter.notifyDataSetChanged();
                int p = getFlightDataPosition(currentD.getNum_vol());
                if(currentPosition != null && currentPosition == p){
                   currentFlightData = currentD;
                   currentPlaneName.setText(currentFlightData.getType());
                   currentDepartureDate.setText(currentFlightData.getHeure_depart());
                   currentArrivalDate.setText(currentFlightData.getHeure_arrivee());
                   currentDepartureCity.setText(currentFlightData.getVille_depart());
                   currentArrivalCity.setText(currentFlightData.getVille_arrivee());
                   currentCost.setText(String.valueOf(currentFlightData.getFrais()));
                }
                flighNumber.setText(String.valueOf(data.size()));
                Toast.makeText(context, "Données modifier avec succés", Toast.LENGTH_LONG).show();
                break;
            case RequestCode.REQUEST_CODE_REMOVE_FLIGHT:
                FlightDataModel rmData = (FlightDataModel) d.getParcelableExtra("data");
                int pos = getFlightDataPosition(rmData.getNum_vol());
                removeItemCallBack.removeItem(pos);
                flighNumber.setText(String.valueOf(data.size()));
                if(currentPosition == null){
                    horizentalListAdapter.notifyDataSetChanged();
                    Toast.makeText(context, "Données supprimer avec succés", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pos == currentPosition){
                    horizentalListAdapter.setIsFirstClicked(true);
                    horizentalListAdapter.setRow_index(-1);
                    planeContent.removeView(planed);
                    planeContent.addView(selectionIcon);
                }
                if(pos < currentPosition){
                    horizentalListAdapter.setRow_index(currentPosition-1);
                }
                horizentalListAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Données supprimer avec succés", Toast.LENGTH_LONG).show();
                break;
            default:

        }

    }

    private void deleteVolChange(){
        removeItemCallBack.removeItem(currentPosition);
        flighNumber.setText(String.valueOf(data.size()));
        horizentalListAdapter.setIsFirstClicked(true);
        horizentalListAdapter.setRow_index(-1);
        horizentalListAdapter.notifyDataSetChanged();
        planeContent.removeView(planed);
        planeContent.addView(selectionIcon);
    }

    private void cancelRequests(){
        if(call!=null) call.cancel();
        if(deleteVol !=null) deleteVol.cancel(true);
    }

    private int sleepThreadTime = 1500;
    private class DeleteVol extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            contentDialog.setVisibility(View.GONE);
            loadingDialog.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Vol v = ApiCallConfig.retrofit.create(Vol.class);
                call = v.deleteVol(currentFlightData.getNum_vol());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context, "Erreur, Veuiller verifier votre connexion internet!", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Erreur, Veuiller verifier votre connexion internet!", Toast.LENGTH_LONG).show();
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
                deleteVolChange();
            }
        }
        @Override
        protected void onPostExecute(Void aVoid){
            contentDialog.setVisibility(View.VISIBLE);
            loadingDialog.setVisibility(View.GONE);
            dialog.dismiss();
        }
    }


}