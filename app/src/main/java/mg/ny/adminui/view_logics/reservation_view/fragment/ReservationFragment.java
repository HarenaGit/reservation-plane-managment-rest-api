package mg.ny.adminui.view_logics.reservation_view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

import mg.ny.adminui.ApiCallConfig;
import mg.ny.adminui.MainActivity;
import mg.ny.adminui.R;
import mg.ny.adminui.apiCall.Reservation;
import mg.ny.adminui.data_model.ReservVolJsonDataModel;
import mg.ny.adminui.data_model.ReservationJsonDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.ItemViewHolder;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListAdapter;
import mg.ny.adminui.data_model.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.public_component_view.interfaces.HorizentalListCallBack;
import mg.ny.adminui.view_logics.public_component_view.interfaces.RemoveItemCallBack;
import mg.ny.adminui.view_logics.reservation_view.activity.AddReservationActivity;
import mg.ny.adminui.view_logics.reservation_view.activity.EditReservationActivity;
import mg.ny.adminui.view_logics.reservation_view.adapter.reservationListAdapter.ReservationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  ReservationFragment extends Fragment {
    private ArrayList<StaticHorizentalListModel> item ;
    private ArrayList<ReservationDataModel> data;
    private Integer currentPosition = null;
    private TextView numberOfItem;
    private RemoveItemCallBack removeItemCallBack;
    private SwipeMenuListView listView;
    private ReservationAdapter adapter;
    private Dialog dialog;
    private LinearLayout contentDialog;
    private RelativeLayout loadingDialog;
    private GetReservByVol getReservByVol;
    private Call<ReservationJsonDataModel> callGetReservByVol;
    private Integer currentFlightId;
    private RelativeLayout progressBar;
    private TextView numberOfReserv;
    public ReservationFragment(ArrayList<StaticHorizentalListModel> item, ArrayList<ReservationDataModel> data, RemoveItemCallBack removeItemCallBack){
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
    private ArrayList<ReservationDataModel> currentReservData;
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
                Intent addActivity = new Intent(context, AddReservationActivity.class);
                startActivityForResult(addActivity, RequestCode.REQUEST_CODE_ADD_RESERV);
            }
        });
        planeFragement.addView(planePage);
        RelativeLayout planeList = view.findViewById(R.id.planeHList);
        View planeHorizentalList = inflater.inflate(R.layout.plane_horizental_list, container, false);
        planeList.addView(planeHorizentalList);
        planeContent = view.findViewById(R.id.planeContent);
        selectionIcon = inflater.inflate(R.layout.selection_icon, container, false);
        planeContent.addView(selectionIcon);
        planed = inflater.inflate(R.layout.reservation_content, container, false);
        progressBar = planed.findViewById(R.id.loadingReservation);
        recyclerView = view.findViewById(R.id.rv_plane);
        HorizentalListCallBack<RecyclerView.ViewHolder, Integer, Boolean, Integer> callbackHorizentalList = (RecyclerView.ViewHolder holder, Integer position, Boolean isFirstClicked) -> {
            if(isFirstClicked) {
                planeContent.removeView(selectionIcon);
                planeContent.addView(planed);
            }
            this.currentPosition = position;
            if(holder instanceof ItemViewHolder){
                ItemViewHolder viewHolder = (ItemViewHolder) holder;
                currentFlightId = item.get(position).getNum_vol();

                if(getReservByVol != null){
                    callGetReservByVol.cancel();
                    getReservByVol.cancel(true);
                }
                getReservByVol = new GetReservByVol();
                getReservByVol.execute();

            }
            return 0;
        };
        recyclerView.setLayoutManager(new LinearLayoutManager( view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizentalListAdapter = new StaticHorizentalListAdapter(recyclerView, activity, item, callbackHorizentalList );
        recyclerView.setAdapter(horizentalListAdapter);
        numberOfItem = view.findViewById(R.id.planeNumber);
        numberOfItem.setText(String.valueOf(item.size()));
        /*dialog = new Dialog(activity);
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
                contentDialog.setVisibility(View.GONE);
                loadingDialog.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeItemCallBack.removeItem(currentPosition);
                        planeNumber.setText(String.valueOf(item.size()));
                        horizentalListAdapter.setIsFirstClicked(true);
                        horizentalListAdapter.setRow_index(-1);
                        horizentalListAdapter.notifyDataSetChanged();
                        planeContent.removeView(planed);
                        planeContent.addView(selectionIcon);
                        dialog.dismiss();
                        contentDialog.setVisibility(View.VISIBLE);
                        loadingDialog.setVisibility(View.GONE);
                    }
                }, 2000);
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
        }); */
    }


    private void updateReservationView(){
        numberOfReserv = planed.findViewById(R.id.numberReservation);
        numberOfReserv.setText("Nombre : " + currentReservData.size());
        listView = planed.findViewById(R.id.reservationListItem);
        adapter = new ReservationAdapter(context, currentReservData);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem editItem = new SwipeMenuItem(context);

                editItem.setBackground(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
                editItem.setWidth(130);
                editItem.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
                deleteItem.setWidth(100);
                deleteItem.setIcon(R.drawable.ic_delete_swipe);

                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        Intent editActivity = new Intent(context, EditReservationActivity.class);
                        ReservationDataModel cRData = adapter.getElement(position);
                        editActivity.putExtra("data", cRData);
                        startActivityForResult(editActivity, RequestCode.REQUEST_CODE_EDIT_RESERV);
                        break;
                    case 1:

                              /*
                              imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                              TextView textDialog = dialog.findViewById(R.id.planeRemoveId);
                                textDialog.setText("Vol numéro : " +  data.get(position).getId());
                                dialog.show(); */
                        break;
                }

                return false;
            }
        });
    }



    private int getPlaneDataPosition(Integer id){
        for(int i=0; i<data.size();i++){
            if(data.get(i).getNum_reservation().equals(id)) return i;
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
                numberOfItem.setText(String.valueOf(item.size()));
                Toast.makeText(context, "Avion ajouter avec succés", Toast.LENGTH_LONG).show();
                break;
            case RequestCode.REQUEST_CODE_EDIT_PLANE:
                AvionDataModel currentD = (AvionDataModel) d.getParcelableExtra("data");
                horizentalListAdapter.notifyDataSetChanged();
                int p = getPlaneDataPosition(currentD.getNum_avion());
                if(currentPosition != null && currentPosition == p){
                    currentPlaneData = currentD;


                }
                numberOfItem.setText(String.valueOf(item.size()));
                Toast.makeText(context, "Données modifier avec succés", Toast.LENGTH_LONG).show();
                break;
            case RequestCode.REQUEST_CODE_REMOVE_PLANE:
                AvionDataModel rmData = (AvionDataModel) d.getParcelableExtra("data");
                int pos = getPlaneDataPosition(rmData.getNum_avion());
                removeItemCallBack.removeItem(pos);
                numberOfItem.setText(String.valueOf(item.size()));
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


    private Integer sleepThreadTime = 1500;
    private class GetReservByVol extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Reservation r = ApiCallConfig.retrofit.create(Reservation.class);
                callGetReservByVol = r.getReservation(currentFlightId);
                callGetReservByVol.enqueue(new Callback<ReservationJsonDataModel>() {
                    @Override
                    public void onResponse(Call<ReservationJsonDataModel> call, Response<ReservationJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        currentReservData = response.body().getData();
                        publishProgress(true);
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
        @Override
        protected void onProgressUpdate(Boolean... value){
            if(value[0]){
               updateReservationView();
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
            progressBar.setVisibility(View.GONE);
        }
    }
}