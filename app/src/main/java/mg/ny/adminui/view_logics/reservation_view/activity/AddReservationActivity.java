package mg.ny.adminui.view_logics.reservation_view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mg.ny.adminui.ApiCallConfig;
import mg.ny.adminui.MainActivity;
import mg.ny.adminui.R;
import mg.ny.adminui.apiCall.Place;
import mg.ny.adminui.apiCall.Reservation;
import mg.ny.adminui.apiCall.Vol;
import mg.ny.adminui.data.StaticDataGeneration;
import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.PlaceDataModel;
import mg.ny.adminui.data_model.PlaceJsonDataModel;
import mg.ny.adminui.data_model.PostJsonDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.data_model.StaticHorizentalListModel;
import mg.ny.adminui.data_model.VolJsonDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.view_logics.plane_view.activity.AddplaneActivity;
import mg.ny.adminui.view_logics.reservation_view.adapter.spiner_adapter.NumPlaceSpinnerAdapter;
import mg.ny.adminui.view_logics.reservation_view.adapter.spiner_adapter.NumVolSpinnerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReservationActivity extends AppCompatActivity {

    MaterialButton save;
    ImageButton backButton;
    Spinner numVol;
    Spinner numPlace;
    EditText passengerName;
    EditText dateTimeReserv;
    int numVolPosition = 0;
    int numPlacePosition = 0;
    private Call<PostJsonDataModel> callPostReserv;
    private PostReserv postReserv;
    private RelativeLayout loading;
    private RelativeLayout loadingPlace;
    private InputMethodManager imm;
    private ArrayList<FlightDataModel> flightData;
    private ArrayList<Integer> placeData;
    private  Call<VolJsonDataModel> callGetVol;
    private GetVol getVol;
    private GetPlace getPlace;
    private Call<PlaceJsonDataModel> callGetPlace;
    private ArrayList<PlaceDataModel> placeList;
    private Integer nb_places;
    private ReservationDataModel currentReservationData;
    public static class Occupation{
        public static final Integer TRUE = 1;
        public static final Integer FALSE = 0;
    }

    private void placeDataReady(){
        SpinnerAdapter placeDataAdapter = new NumPlaceSpinnerAdapter(getApplicationContext(), placeData);
        numPlace.setAdapter(placeDataAdapter);
    }

    private void flightDatReady(){
        numVol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numVolPosition = position;
                getPlace = new GetPlace();
                getPlace.execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        numPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numPlacePosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SpinnerAdapter dataAdapter = new NumVolSpinnerAdapter(getApplicationContext(), flightData);
        numVol.setAdapter(dataAdapter);

        dateTimeReserv = findViewById(R.id.addReservDate);
        dateTimeReserv.setInputType(InputType.TYPE_NULL);

        dateTimeReserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateTimeReserv);
            }
        });


        passengerName = findViewById(R.id.addReservName);

        backButton = findViewById(R.id.backButtonFlight);
        save = findViewById(R.id.saveFlightButton);

        imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequests();
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequests();
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                if(!isDataValid()){
                    Toast.makeText(AddReservationActivity.this, "Veuiller verifier vos donn??es!", Toast.LENGTH_LONG).show();
                    return;
                }
                postReserv = new PostReserv();
                postReserv.execute();
            }
        });
    }

    private Boolean isDataValid(){
        if(dateTimeReserv.getText().toString().trim().length() > 0 && passengerName.getText().toString().trim().length() > 0 && placeData.size() > 0 && flightData.size() > 0) {
            return true;
        }
        return false;
    }


    private void addReservation(){
        Intent intent=new Intent();
        intent.putExtra("data", currentReservationData);
        setResult(RequestCode.REQUEST_CODE_ADD_RESERV,intent);
        finish();
    }
    private void cancelRequests(){
        if(callGetVol !=null) callGetVol.cancel();
        if(getVol != null) getVol.cancel(true);
        if(callGetPlace!=null) callGetPlace.cancel();
        if(getPlace != null) getPlace.cancel(true);
        if(callPostReserv!=null) callPostReserv.cancel();
        if(postReserv !=null)postReserv.cancel(true);
    }
    private void showDateTimeDialog(EditText d) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        d.setText(simpleDateFormat.format(calendar.getTime()));


                    }
                };
                new TimePickerDialog(AddReservationActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(AddReservationActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
        numVol = (Spinner) findViewById(R.id.addReservVol);
        numPlace = (Spinner) findViewById(R.id.addReservPlace);
        loading = findViewById(R.id.addFlightLoading);
        loadingPlace = findViewById(R.id.loadingPlace);
        getVol = new GetVol();
        getVol.execute();

    }

    private ArrayList<Integer> generatePlaceData(){
        ArrayList<Integer> l = new ArrayList<>();
        for(int i=1;i<=nb_places;i++){
            Boolean isInsertion = true;
            for(PlaceDataModel object: placeList){
                if(object.getNum_place() == i && object.getOccupation() == Occupation.TRUE) isInsertion = false;
            }
            if(isInsertion) l.add(i);
        }
        return l;
    }

    private Integer sleepThreadTime = 1500;


    private class GetVol extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            loading.setVisibility(View.VISIBLE);
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
                            Toast.makeText(AddReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        flightData = response.body().getData();
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<VolJsonDataModel> call, Throwable t) {
                        Toast.makeText(AddReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
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
                flightDatReady();
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
           loading.setVisibility(View.GONE);
        }
    }

    private class GetPlace extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            loadingPlace.setVisibility(View.VISIBLE);
            placeData = new ArrayList<>();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                nb_places = flightData.get(numVolPosition).getNb_places();
                Place p = ApiCallConfig.retrofit.create(Place.class);
                callGetPlace = p.getPlaceByNumVol(flightData.get(numVolPosition).getNum_vol());
                callGetPlace.enqueue(new Callback<PlaceJsonDataModel>() {
                    @Override
                    public void onResponse(Call<PlaceJsonDataModel> call, Response<PlaceJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(AddReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        placeList = response.body().getData();
                        placeData = generatePlaceData();
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<PlaceJsonDataModel> call, Throwable t) {
                        Toast.makeText(AddReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
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
                placeDataReady();
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
            loadingPlace.setVisibility(View.GONE);
        }


    }
    private class PostReserv extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            loading.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Reservation r = ApiCallConfig.retrofit.create(Reservation.class);

                callPostReserv = r.postReservation(new ReservationDataModel(null, flightData.get(numVolPosition).getNum_vol(), placeData.get(numPlacePosition), dateTimeReserv.getText().toString().trim(), passengerName.getText().toString().trim()));
                callPostReserv.enqueue(new Callback<PostJsonDataModel>() {
                    @Override
                    public void onResponse(Call<PostJsonDataModel> call, Response<PostJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(AddReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        currentReservationData =  new ReservationDataModel(response.body().getData().get(0), flightData.get(numVolPosition).getNum_vol(), placeData.get(numPlacePosition), dateTimeReserv.getText().toString().trim(), passengerName.getText().toString().trim());
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<PostJsonDataModel> call, Throwable t) {
                        Toast.makeText(AddReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
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
                addReservation();
            }
            loading.setVisibility(View.GONE);
        }
        @Override
        protected void onPostExecute(Void aVoid){

        }
    }

}