package mg.ny.adminui.view_logics.dashboard_view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import mg.ny.adminui.ApiCallConfig;
import mg.ny.adminui.MainActivity;
import mg.ny.adminui.R;
import mg.ny.adminui.TypeDoubleFormatter;
import mg.ny.adminui.apiCall.Avion;
import mg.ny.adminui.apiCall.Stat;
import mg.ny.adminui.data.StaticDataGeneration;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.data_model.AvionJsonDataModel;
import mg.ny.adminui.data_model.StatsDataModel;
import mg.ny.adminui.data_model.StatsJsonDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.view_logics.flight_view.activity.AddFlightActivity;
import mg.ny.adminui.view_logics.flight_view.adapter.spinner_adapter.CustomSpinnerAdapter;
import mg.ny.adminui.view_logics.flight_view.fragment.FlightFragment;
import mg.ny.adminui.view_logics.public_component_view.horizentalList.StaticHorizentalListAdapter;
import mg.ny.adminui.data_model.StaticHorizentalListModel;
import mg.ny.adminui.view_logics.public_component_view.interfaces.HorizentalListCallBack;
import mg.ny.adminui.view_logics.public_component_view.interfaces.RemoveItemCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private Fragment fragment;
    private FragmentManager  fragmentManager;
    private ArrayList<StaticHorizentalListModel> item ;
    private ArrayList<AvionDataModel> data;
    private TextView planeNumber;
    private Dialog dialog;
    private RemoveItemCallBack removeItemCallBack;
    private LinearLayout contentDialog;
    private RelativeLayout loadingDialog;
   private RecyclerView recyclerView;
    private StaticHorizentalListAdapter horizentalListAdapter;
    private RelativeLayout currentMonthLoader;
    private RelativeLayout currentAvionLoader;
    private RelativeLayout linearChartLoader;
    private RelativeLayout currentYearLoader;
    private TextView currentMonthValue;
    private TextView currentYearValue;
    private Double monthValue;
    private Double yearValue;
    private Call<AvionJsonDataModel> callGetAvion;
    private View view;
    private  ArrayList<Entry> chartData = new ArrayList<>();
    private GetAvion getAvion;
    private Call<StatsJsonDataModel> callGetStats;
    private GetLinearChartValue getLinearChartValue;
    private String currentYear = new SimpleDateFormat("yyyy").format(new Date());
    private Boolean isFirstCall = true;
    public Button yearPickerButton;
    public MonthPickerDialog.Builder builder ;
    private Spinner numAvionRecette;
    private EditText dateDebut;
    private EditText dateFin;
    private TextView valeurRecette;
    private TextView nombreReservation;
    private TextView nombreVol;
    private MaterialButton calculer;
    private FloatingActionButton floatingActionButton;
    private ArrayList<AvionDataModel> planeList;
    private Integer planePosition = 0;
    private Integer chartAvionPosition = 0;

    private void cancelRequests(){
        if(callGetStats !=null) callGetStats.cancel();
        if(getLinearChartValue !=null) getLinearChartValue.cancel(true);
        if(callGetAvion!=null) callGetAvion.cancel();
        if(getAvion!=null) getAvion.cancel(true);
    }


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        this.view = view;

        currentMonthLoader = view.findViewById(R.id.currentMonthLoader);
        currentAvionLoader = view.findViewById(R.id.currentAvionLoader);
        linearChartLoader = view.findViewById(R.id.linearChartLoader);
        currentYearLoader = view.findViewById(R.id.currentYearLoader);
        currentYearValue = view.findViewById(R.id.currentYearValue);
        currentMonthValue = view.findViewById(R.id.currentMonthValue);
        recyclerView = view.findViewById(R.id.rv_plane);



        yearPickerButton = view.findViewById(R.id.yearPickerButton);
        yearPickerButton.setText(currentYear);
        TextView m = view.findViewById(R.id.monthLabel);
        TextView y = view.findViewById(R.id.yearLabel);
        m.setText(new SimpleDateFormat("MMMM").format(new Date()) + ", " + new SimpleDateFormat("yyyy").format(new Date()));
        y.setText("Année, " + currentYear);

        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.recette_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        calculer = dialog.findViewById(R.id.calculerRecette);
        numAvionRecette = dialog.findViewById(R.id.avionRecette);
        dateDebut = dialog.findViewById(R.id.dateDebut);
        dateFin = dialog.findViewById(R.id.dateFin);
        valeurRecette = dialog.findViewById(R.id.recetteValue);
        nombreReservation = dialog.findViewById(R.id.nombreReserv);
        nombreVol = dialog.findViewById(R.id.nombreVol);

        calculer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditTextValid()){
                    Toast.makeText(context, "Veuiller remplir les champs", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(planePosition == null || planePosition == 0){
                    planePosition = 0;
                }

                new CalculerRecette(item.get(planePosition+ 1).getNum_avion()).execute();
            }
        });

        dateDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateDebut);
            }
        });
        dateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateFin);
            }
        });

        floatingActionButton = view.findViewById(R.id.recetteBoutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

       /* builder = new MonthPickerDialog.Builder(activity, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {

            }
        }, Calendar.YEAR, Calendar.MONTH);

        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(1990)
                .setActivatedYear(2017)
                .setMaxYear(2030)
                .setMinMonth(Calendar.FEBRUARY)
                .setTitle("Selectionner une année")
                .setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
                .setMaxMonth(Calendar.OCTOBER)
                .setYearRange(1890, 1890)
                .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int selectedYear) {
                        currentYear = String.valueOf(selectedYear);
                        yearPickerButton.setText(currentYear);
                    } })
                 .build();

        yearPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.showYearOnly();
            }
        });
      */

        GetMonthlyValue getMonthlyValue = new GetMonthlyValue();
        getMonthlyValue.execute();
        getAvion = new GetAvion();
        getAvion.execute();
    }

    private void showDateTimeDialog(EditText dateTimeDepDate) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateTimeDepDate.setText(simpleDateFormat.format(calendar.getTime()));
                }
        };
        new DatePickerDialog(context, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private ArrayList<AvionDataModel> createAvionList(){
        ArrayList<AvionDataModel> a = new ArrayList<>();
        for(int i=1;i<item.size();i++){
            a.add(item.get(i).getAv());
        }
        return  a;
    }

    private void updateChartView(){

        if(fragment !=null){
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }

        fragment = new LineChartFragment(chartData);
        getFragmentManager().beginTransaction().replace(R.id.fragment_chart, fragment).commit();

        if(isFirstCall){


            float d = 0;
            for(int i=0; i<chartData.size();i++){

                    d = d + chartData.get(i).getY();
                }
            yearValue = (double)d;
            currentYearValue.setText(TypeDoubleFormatter.format(yearValue));
            isFirstCall = false;
            currentYearLoader.setVisibility(View.GONE);

            numAvionRecette.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    planePosition = position;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            planeList = createAvionList();
            SpinnerAdapter dataAdapter = new CustomSpinnerAdapter(context, planeList);
            numAvionRecette.setAdapter(dataAdapter);

            floatingActionButton.setVisibility(View.VISIBLE);

        }

    }

    private Boolean isEditTextValid(){
        if(dateDebut.getText().toString().trim().length() > 0 && dateFin.getText().toString().trim().length() > 0 ) {
            if (planeList.size() > 0) {
                return true;
            }
        }
        return false;
    }

    private void getBilanByYear(){
        if(callGetStats!=null) callGetStats.cancel();
        if(getLinearChartValue!=null) getLinearChartValue.cancel(true);

        if(item.get(chartAvionPosition).getText().equals("Tout")){
            getLinearChartValue = new GetLinearChartValue();
            getLinearChartValue.execute();
        }
        else{
            Integer num_avion = item.get(chartAvionPosition).getNum_avion();
            getLinearChartValue = new GetLinearChartValue(num_avion);
            getLinearChartValue.execute();
        }

    }

    private void updatePlaneView(){
        HorizentalListCallBack<RecyclerView.ViewHolder, Integer, Boolean, Integer> callbackHorizentalList = (RecyclerView.ViewHolder holder, Integer position, Boolean isFirstClicked) -> {
            chartAvionPosition = position;
            getBilanByYear();
            return 0;
        };
        recyclerView.setLayoutManager(new LinearLayoutManager( view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizentalListAdapter = new StaticHorizentalListAdapter(recyclerView, activity, item, callbackHorizentalList );
        recyclerView.setAdapter(horizentalListAdapter);
        planeNumber = view.findViewById(R.id.planeNumber);
        planeNumber.setText(String.valueOf(data.size()));
        horizentalListAdapter.setRow_index(0);
        horizentalListAdapter.notifyDataSetChanged();
        getLinearChartValue = new GetLinearChartValue();
        getLinearChartValue.execute();
    }



    private Integer sleepThreadTime = 1500;

    private class GetMonthlyValue extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            currentMonthLoader.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Stat st = ApiCallConfig.retrofit.create(Stat.class);
                String month = new SimpleDateFormat("MM").format(new Date());
                Call<StatsJsonDataModel> callGetCurrentValue = st.getBilanByMY(month, currentYear);
                callGetCurrentValue.enqueue(new Callback<StatsJsonDataModel>() {
                    @Override
                    public void onResponse(Call<StatsJsonDataModel> call, Response<StatsJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        monthValue = response.body().getData().get(0).getRecette();
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<StatsJsonDataModel> call, Throwable t) {
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
                if(monthValue == null) monthValue = 0.00;
                currentMonthValue.setText(TypeDoubleFormatter.format(monthValue));
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
            currentMonthLoader.setVisibility(View.GONE);
        }
    }

    private class GetAvion extends AsyncTask<Void, Boolean, Void>{

        @Override
        protected void onPreExecute(){
            currentAvionLoader.setVisibility(View.VISIBLE);
            linearChartLoader.setVisibility(View.VISIBLE);
            currentYearLoader.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {

            Avion av = ApiCallConfig.retrofit.create(Avion.class);
            callGetAvion = av.getAvion();
            callGetAvion.enqueue(new Callback<AvionJsonDataModel>() {
                @Override
                public void onResponse(Call<AvionJsonDataModel> call, Response<AvionJsonDataModel> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                        publishProgress(false);
                        return;
                    }
                    data = new ArrayList<>();
                    item = new ArrayList<>();
                    data = response.body().getData();
                    for(int i=0;i<data.size();i++){
                        item.add(new StaticHorizentalListModel(data.get(i)));
                    }
                    item.add(0, new StaticHorizentalListModel("Tout", "", "tout"));
                    publishProgress(true);
                }

                @Override
                public void onFailure(Call<AvionJsonDataModel> call, Throwable t) {
                    Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                    publishProgress(false);
                    return;
                }
            });

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
            currentAvionLoader.setVisibility(View.GONE);

        }
    }
    private class GetLinearChartValue extends AsyncTask<Void, Boolean, Void> {

        private Integer num_avion;
        private ArrayList<StatsDataModel> stData = new ArrayList<>();
        public GetLinearChartValue(){
            Stat st = ApiCallConfig.retrofit.create(Stat.class);
            callGetStats = st.getBilanByYear(currentYear);
        }
        public GetLinearChartValue(Integer num_avion){
            this.num_avion = num_avion;
            Stat st = ApiCallConfig.retrofit.create(Stat.class);
            callGetStats = st.getBilanAvionByYear(num_avion, currentYear);
        }
        @Override
        protected void onPreExecute(){
            linearChartLoader.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                callGetStats.enqueue(new Callback<StatsJsonDataModel>() {
                    @Override
                    public void onResponse(Call<StatsJsonDataModel> call, Response<StatsJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            chartData = new ArrayList<>();
                            publishProgress(false);
                            return;
                        }

                        chartData = new ArrayList<>();
                        stData = response.body().getData();
                        for(int i=0;i<stData.size();i++){
                            Double d = stData.get(i).getRecette();
                            float value;
                            if(d == null) value = 0;
                            else {
                                double dl = stData.get(i).getRecette();
                                value = (float)dl;
                            };
                            chartData.add(new Entry(i+1, value));
                        }
                        publishProgress(true);

                    }

                    @Override
                    public void onFailure(Call<StatsJsonDataModel> call, Throwable t) {
                        Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                        chartData = new ArrayList<>();
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
                updateChartView();

            }
            linearChartLoader.setVisibility(View.GONE);
        }
        @Override
        protected void onPostExecute(Void aVoid){

        }
    }

    private class CalculerRecette extends AsyncTask<Void, Boolean, Void> {

        private Integer num_avion;
       private Double value;
       private Integer nbR;
       private Integer nbV;


        public CalculerRecette(Integer num_avion){
            this.num_avion = num_avion;

        }
        @Override
        protected void onPreExecute(){
            calculer.setEnabled(false);
            valeurRecette.setText("chargement...");
            nombreReservation.setText("Réservations: ...");
            nombreVol.setText("Vol: ...");
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Thread.sleep(sleepThreadTime);

                Stat st = ApiCallConfig.retrofit.create(Stat.class);
                Call<StatsJsonDataModel> c = st.getBilanEntre2Date(num_avion, dateDebut.getText().toString().trim(), dateFin.getText().toString().trim());

                c.enqueue(new Callback<StatsJsonDataModel>() {
                    @Override
                    public void onResponse(Call<StatsJsonDataModel> call, Response<StatsJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        value = response.body().getData().get(0).getRecette();
                        nbR = response.body().getData().get(0).getNb_reservation();
                        nbV = response.body().getData().get(0).getNb_vol();
                        publishProgress(true);

                    }

                    @Override
                    public void onFailure(Call<StatsJsonDataModel> call, Throwable t) {
                        Toast.makeText(context,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                        Log.d("Test", String.valueOf(t));
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
                valeurRecette.setText(TypeDoubleFormatter.format(this.value));
                nombreReservation.setText("Réservations: " + nbR);
                nombreVol.setText("Vol: " + nbV);
            }
            else{
                valeurRecette.setText("Réservations : Erreur");
                nombreReservation.setText("...");
                nombreVol.setText("...");
            }
            calculer.setEnabled(true);
        }
        @Override
        protected void onPostExecute(Void aVoid){

        }
    }


}