package mg.ny.adminui.view_logics.dashboard_view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mg.ny.adminui.R;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListAdapter;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.public_component_view.interfaces.HorizentalListCallBack;
import mg.ny.adminui.view_logics.public_component_view.interfaces.RemoveItemCallBack;

public class DashboardFragment extends Fragment {


    private ArrayList<StaticHorizentalListModel> item ;
    private ArrayList<AvionDataModel> data;
    private Integer currentPosition = null;
    private TextView currentId;
    private TextView currentName;
    private TextView currentPlaceCount;
    private TextView planeNumber;
    private Dialog dialog;
    private RemoveItemCallBack removeItemCallBack;
    private LinearLayout contentDialog;
    private RelativeLayout loadingDialog;
    private LineChart lineChart;
    private TextView lineChartCurrentValue;
    public DashboardFragment(ArrayList<StaticHorizentalListModel> item, ArrayList<AvionDataModel> data, RemoveItemCallBack removeItemCallBack){
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
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    private RelativeLayout planeContent;
    private View selectionIcon;
    private View planed;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        recyclerView = view.findViewById(R.id.rv_plane);
        HorizentalListCallBack<RecyclerView.ViewHolder, Integer, Boolean, Integer> callbackHorizentalList = (RecyclerView.ViewHolder holder, Integer position, Boolean isFirstClicked) -> {
            if(isFirstClicked) {


            }

            return 0;
        };

        recyclerView.setLayoutManager(new LinearLayoutManager( view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizentalListAdapter = new StaticHorizentalListAdapter(recyclerView, activity, item, callbackHorizentalList );
        recyclerView.setAdapter(horizentalListAdapter);
        planeNumber = view.findViewById(R.id.planeNumber);
        planeNumber.setText(String.valueOf(data.size()));
        horizentalListAdapter.setRow_index(0);
        horizentalListAdapter.notifyDataSetChanged();

        lineChart = view.findViewById(R.id.lineChart);
        lineChartCurrentValue = view.findViewById(R.id.lineChartCurrentValue);

        setLineChart(setLineChartData(12, 60));

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String month = "";
                switch ((int) e.getX()){
                    case 1: month = "Janvier";break;
                    case 2: month = "Février";break;
                    case 3: month = "Mars";break;
                    case 4: month = "Avril";break;
                    case 5: month = "Mai";break;
                    case 6: month = "Juin";break;
                    case 7: month = "Juillet";break;
                    case 8: month = "Aout";break;
                    case 9: month = "Septembre";break;
                    case 10: month = "Octobre";break;
                    case 11: month = "Novembre";break;
                    case 12: month = "Décembre";break;

                }

                String val = month + " : "  + String.valueOf((int) e.getY()) + " ar";
                if(!val.equals(""))  lineChartCurrentValue.setText(val);
                else lineChartCurrentValue.setText("--- ar");

            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private ArrayList<Entry> setLineChartData(int count, int range){
        ArrayList<Entry> yvals = new ArrayList<>();
        for(int i=1; i<=count;i++){
            float val = (float) (Math.random()*range) + 50;
            yvals.add(new Entry(i, val));
        }
        return yvals;
    }

    private void setLineChart(ArrayList<Entry> yvals){

        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Jan", "Feb", "Mar", "Avr", "Mai", "Jun","Jul", "Aou", "Sept", "Oct", "Nov", "Dec"));

        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setExtraLeftOffset(15);
        lineChart.setExtraRightOffset(15);

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setGridColor(getResources().getColor(R.color.light_grey));
        leftYAxis.setTypeface(ResourcesCompat.getFont(this.context, R.font.arial));
        XAxis topXAxis = lineChart.getXAxis();
        topXAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTypeface(ResourcesCompat.getFont(this.context, R.font.segeouibold));
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(getResources().getColor(R.color.light_grey));

        lineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

        LineDataSet set;

        set = new LineDataSet(yvals, "recette des avions");
        set.setColor(getResources().getColor(R.color.blue));
        set.setValueTextColor(getResources().getColor(R.color.dark_grey));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setDrawValues(false);
        set.setCircleHoleColor(getResources().getColor(R.color.white));
        set.setCircleColor(getResources().getColor(R.color.teal));

        LineData d = new LineData(set);
        this.lineChart.setData(d);

        lineChart.animateX(1000);
        lineChart.invalidate();
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
    }




}