package mg.ny.adminui.view_logics.dashboard_view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mg.ny.adminui.MainActivity;
import mg.ny.adminui.R;
import mg.ny.adminui.TypeDoubleFormatter;


public class LineChartFragment extends Fragment {

    private LineChart lineChart;
    private ArrayList<Entry> data;
    private TextView lineChartCurrentValue;


    public LineChartFragment(ArrayList<Entry> data){
        this.data = data;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_line_chart, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        lineChart = view.findViewById(R.id.lineChart);
        lineChartCurrentValue = view.findViewById(R.id.lineChartCurrentValue);
        formatXAxis();

        setLineChart(data);
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

                String val = month + " : "  + TypeDoubleFormatter.format((double)e.getY()) + " ar";
                if(!val.equals(""))  lineChartCurrentValue.setText(val);
                else lineChartCurrentValue.setText("--- ar");

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    private void formatXAxis(){
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
        lineChart.animateX(1000);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
    }

    private void setLineChart(ArrayList<Entry> yvals){

        Collections.sort(yvals, new EntryXComparator());

        LineData d = new LineData(createLineDataSet(yvals));
        d.notifyDataChanged();
        lineChart.setData(d);
        d.notifyDataChanged();
        lineChart.getLineData().notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
        lineChart.moveViewToX(d.getEntryCount() );

    }

    private LineDataSet createLineDataSet(ArrayList<Entry> yvals){
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
        return set;
    }



}