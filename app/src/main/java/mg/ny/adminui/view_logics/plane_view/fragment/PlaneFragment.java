package mg.ny.adminui.view_logics.plane_view.fragment;

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
import mg.ny.adminui.apiCall.Avion;
import mg.ny.adminui.view_logics.plane_view.activity.SearchActivity;
import mg.ny.adminui.view_logics.public_component_view.interfaces.HorizentalListCallBack;
import mg.ny.adminui.R;
import mg.ny.adminui.view_logics.public_component_view.interfaces.RemoveItemCallBack;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListAdapter;
import mg.ny.adminui.data_model.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.plane_view.activity.AddplaneActivity;
import mg.ny.adminui.view_logics.plane_view.activity.EditplaneActivity;
import mg.ny.adminui.data_model.AvionDataModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  PlaneFragment extends Fragment {

   private ArrayList<StaticHorizentalListModel> item ;
   private ArrayList<AvionDataModel> data;
   private Integer currentPosition = null;
   private TextView currentId;
   private TextView currentName;
   private TextView currentPlaceCount;
   private TextView currentColumnCount;
   private TextView planeNumber;
   private Dialog dialog;
   private RemoveItemCallBack removeItemCallBack;
   private LinearLayout contentDialog;
   private RelativeLayout loadingDialog;
   private DeleteAvion deleteAvion;
   private Call<Void> call;
    public PlaneFragment(ArrayList<StaticHorizentalListModel> item, ArrayList<AvionDataModel> data, RemoveItemCallBack removeItemCallBack){
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
    private AvionDataModel currentPlaneData;
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
                        Intent addActivity = new Intent(context, AddplaneActivity.class);
                        startActivityForResult(addActivity, RequestCode.REQUEST_CODE_ADD_PLANE);
                    }
                });
                planeFragement.addView(planePage);
                RelativeLayout planeList = view.findViewById(R.id.planeHList);
                View planeHorizentalList = inflater.inflate(R.layout.plane_horizental_list, container, false);
                planeList.addView(planeHorizentalList);
                 planeContent = view.findViewById(R.id.planeContent);
                 selectionIcon = inflater.inflate(R.layout.selection_icon, container, false);
                planeContent.addView(selectionIcon);
                planed = inflater.inflate(R.layout.plane_content, container, false);
                planed.findViewById(R.id.editCurrentPlane).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent editActivity = new Intent(context, EditplaneActivity.class);
                       editActivity.putExtra("data", currentPlaneData);
                       startActivityForResult(editActivity, RequestCode.REQUEST_CODE_EDIT_PLANE);
                    }
                });
                recyclerView = view.findViewById(R.id.rv_plane);
                HorizentalListCallBack<RecyclerView.ViewHolder, Integer, Boolean, Integer> callbackHorizentalList = (RecyclerView.ViewHolder holder, Integer position, Boolean isFirstClicked) -> {
                    if(isFirstClicked) {
                        planeContent.removeView(selectionIcon);
                        planeContent.addView(planed);
                    }
                    this.currentPosition = position;
                    currentPlaneData = data.get(position);
                    currentId = view.findViewById(R.id.planeId);
                    currentName = view.findViewById(R.id.planeName);
                    currentPlaceCount = view.findViewById(R.id.planePlaceCount);
                    currentColumnCount = view.findViewById(R.id.planeColumCount);
                    currentId.setText(String.valueOf(currentPlaneData.getNum_avion()));
                    currentName.setText(currentPlaneData.getType());
                    currentPlaceCount.setText(String.valueOf(currentPlaneData.getNb_places()));
                    currentColumnCount.setText(String.valueOf(currentPlaneData.getNb_colonnes()));
                    return 0;
                };
                recyclerView.setLayoutManager(new LinearLayoutManager( view.getContext(), LinearLayoutManager.HORIZONTAL, false));
                horizentalListAdapter = new StaticHorizentalListAdapter(recyclerView, activity, item, callbackHorizentalList );
                recyclerView.setAdapter(horizentalListAdapter);
                 planeNumber = view.findViewById(R.id.planeNumber);
                planeNumber.setText(String.valueOf(data.size()));
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
                      cancelRequests();
                      deleteAvion = new DeleteAvion();
                      deleteAvion.execute();
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
                        textDialog.setText("Avion numéro : " + currentId.getText());
                        dialog.show();
                    }
                });
    }


    private int getPlaneDataPosition(Integer id){
        for(int i=0; i<data.size();i++){
            if(data.get(i).getNum_avion().equals(id)) return i;
        }
        return -1;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent d)
    {
        super.onActivityResult(requestCode, resultCode, d);
        if(resultCode == Activity.RESULT_CANCELED) return;
        switch (resultCode){
            case RequestCode.REQUEST_CODE_ADD_PLANE:
                AvionDataModel pl = (AvionDataModel) d.getParcelableExtra("data");
                if(currentPosition != null && currentPosition == 0) horizentalListAdapter.setRow_index(1);
                horizentalListAdapter.notifyDataSetChanged();
                planeNumber.setText(String.valueOf(data.size()));
                Toast.makeText(context, "Avion ajouter avec succés", Toast.LENGTH_LONG).show();
                break;
            case RequestCode.REQUEST_CODE_EDIT_PLANE:
                AvionDataModel currentD = (AvionDataModel) d.getParcelableExtra("data");
                horizentalListAdapter.notifyDataSetChanged();
                int p = getPlaneDataPosition(currentD.getNum_avion());
                if(currentPosition != null && currentPosition == p){
                    currentPlaneData = currentD;
                    currentId.setText(String.valueOf(currentD.getNum_avion()));
                    currentName.setText(currentD.getType());
                    currentPlaceCount.setText(String.valueOf(currentD.getNb_places()));
                    currentColumnCount.setText(String.valueOf(currentD.getNb_colonnes()));
                }
                planeNumber.setText(String.valueOf(data.size()));
                Toast.makeText(context, "Données modifier avec succés", Toast.LENGTH_LONG).show();
                break;
            case RequestCode.REQUEST_CODE_REMOVE_PLANE:
                AvionDataModel rmData = (AvionDataModel) d.getParcelableExtra("data");
                int pos = getPlaneDataPosition(rmData.getNum_avion());
                removeItemCallBack.removeItem(pos);
                planeNumber.setText(String.valueOf(data.size()));
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

    public void setItem(ArrayList<StaticHorizentalListModel> item) {
        this.item = item;
    }

    private int sleepThreadTime = 1500;
    private void cancelRequests(){
        if(call!=null) call.cancel();
        if(deleteAvion!=null)deleteAvion.cancel(true);
    }
    private void deleteAvionChange(){
        removeItemCallBack.removeItem(currentPosition);
        planeNumber.setText(String.valueOf(data.size()));
        horizentalListAdapter.setIsFirstClicked(true);
        horizentalListAdapter.setRow_index(-1);
        horizentalListAdapter.notifyDataSetChanged();
        planeContent.removeView(planed);
        planeContent.addView(selectionIcon);
        dialog.dismiss();
        contentDialog.setVisibility(View.VISIBLE);
        loadingDialog.setVisibility(View.GONE);
    }
    private class DeleteAvion extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            contentDialog.setVisibility(View.GONE);
            loadingDialog.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Avion av = ApiCallConfig.retrofit.create(Avion.class);
                call = av.deleteAvion(Integer.parseInt(currentId.getText().toString().trim()));
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
                deleteAvionChange();
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
            loadingDialog.setVisibility(View.GONE);
            contentDialog.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }
}