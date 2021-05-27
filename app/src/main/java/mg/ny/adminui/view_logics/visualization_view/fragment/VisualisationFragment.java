package mg.ny.adminui.view_logics.visualization_view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import mg.ny.adminui.R;
import mg.ny.adminui.data_model.PlaneDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.ItemViewHolder;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListAdapter;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.public_component_view.interfaces.HorizentalListCallBack;
import mg.ny.adminui.view_logics.public_component_view.interfaces.RemoveItemCallBack;
import mg.ny.adminui.view_logics.visualization_view.adapter.visualizationGridAdapter.VisualisationGridAdapter;

public class  VisualisationFragment extends Fragment {
    private ArrayList<StaticHorizentalListModel> item ;
    private ArrayList<ReservationDataModel> data;
    private Integer currentPosition = null;
    private TextView numberOfItem;
    private RemoveItemCallBack removeItemCallBack;
    private GridView gridView;
    private VisualisationGridAdapter adapter;
    private Dialog dialog;
    private LinearLayout contentDialog;
    private RelativeLayout loadingDialog;
    private RelativeLayout loader;
    private ContentAsyncTasks tasks;
    public VisualisationFragment(ArrayList<StaticHorizentalListModel> item, ArrayList<ReservationDataModel> data, RemoveItemCallBack removeItemCallBack){
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
    private PlaneDataModel currentPlaneData;
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
        planePage.findViewById(R.id.addPlaneButton).setVisibility(View.GONE);
        planeFragement.addView(planePage);
        RelativeLayout planeList = view.findViewById(R.id.planeHList);
        View planeHorizentalList = inflater.inflate(R.layout.plane_horizental_list, container, false);
        planeList.addView(planeHorizentalList);
        planeContent = view.findViewById(R.id.planeContent);
        selectionIcon = inflater.inflate(R.layout.selection_icon, container, false);
        planeContent.addView(selectionIcon);
        planed = inflater.inflate(R.layout.visualisation_content, container, false);
        loader = planed.findViewById(R.id.visualizationContentLoader);

        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.detail_visualisation_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        MaterialButton ok = dialog.findViewById(R.id.dismissDialog);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        recyclerView = view.findViewById(R.id.rv_plane);
        HorizentalListCallBack<RecyclerView.ViewHolder, Integer, Boolean, Integer> callbackHorizentalList = (RecyclerView.ViewHolder holder, Integer position, Boolean isFirstClicked) -> {

            if(isFirstClicked) {
                planeContent.removeView(selectionIcon);
                planeContent.addView(planed);
            }
            this.currentPosition = position;
            if(holder instanceof ItemViewHolder){
                ItemViewHolder viewHolder = (ItemViewHolder) holder;
                String currentFlightId = item.get(position).getTxt();

               if(tasks != null){
                   tasks.cancel(true);
               }

                gridView = planed.findViewById(R.id.simpleGridView);
                tasks = new ContentAsyncTasks(context, data, currentFlightId, planed, adapter, gridView, loader);
                tasks.execute();
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ReservationDataModel r = tasks.getItem(position);
                        if( r.getFlightId().equals(currentFlightId) && !r.getPlaceNumber().equals("")){
                           TextView t = dialog.findViewById(R.id.dialogId);
                           t.setText(r.getId());
                           TextView d = dialog.findViewById(R.id.detailDialog);
                           String detail = r.getPassengerName() + ", réservé le " + r.getReservationDate() + ", place numéro " + r.getPlaceNumber();
                           d.setText(detail);
                           dialog.show();
                        }
                    }
                });
            }
            return 0;
        };
        recyclerView.setLayoutManager(new LinearLayoutManager( view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizentalListAdapter = new StaticHorizentalListAdapter(recyclerView, activity, item, callbackHorizentalList );
        recyclerView.setAdapter(horizentalListAdapter);
        numberOfItem = view.findViewById(R.id.planeNumber);
        numberOfItem.setText(String.valueOf(item.size()));

    }


    public void setItem(ArrayList<StaticHorizentalListModel> item) {
        this.item = item;
    }


}