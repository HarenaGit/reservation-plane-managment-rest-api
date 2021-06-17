package mg.ny.adminui.view_logics.reservation_view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import mg.ny.adminui.R;
import mg.ny.adminui.apiCall.Place;
import mg.ny.adminui.apiCall.Reservation;
import mg.ny.adminui.apiCall.Vol;
import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.PlaceDataModel;
import mg.ny.adminui.data_model.PlaceJsonDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.data_model.ReservationJsonDataModel;
import mg.ny.adminui.data_model.VolJsonDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.view_logics.reservation_view.adapter.spiner_adapter.NumPlaceSpinnerAdapter;
import mg.ny.adminui.view_logics.reservation_view.adapter.spiner_adapter.NumVolSpinnerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReservationActivity extends AppCompatActivity {

    MaterialButton save;
    ImageButton backButton;
    EditText numReserv;
    Spinner numVol;
    Spinner numPlace;
    EditText passengerName;
    EditText dateTimeReserv;
    int numVolPosition;
    int numPlacePosition;
    private Call<ReservationJsonDataModel> callPutReserv;
    private PutReserv putReserv;
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
        numReserv.setText(String.valueOf(currentReservationData.getNum_reservation()));
        dateTimeReserv.setText(currentReservationData.getDate_reservation());
        passengerName.setText(currentReservationData.getNom_voyageur());
    }

    private Integer getNumVolPosition(){
        for(int i=0; i<flightData.size(); i++ ){
           if(flightData.get(i).getNum_vol() == currentReservationData.getNum_vol()) return i;
        }
        return null;
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
        numVol.post(new Runnable() {
            @Override
            public void run() {
                numVolPosition = getNumVolPosition();
                numVol.setSelection(numVolPosition);
                getPlace = new GetPlace();
                getPlace.execute();
            }
        });

        dateTimeReserv = findViewById(R.id.editReservDate);
        dateTimeReserv.setInputType(InputType.TYPE_NULL);

        dateTimeReserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateTimeReserv);
            }
        });


        passengerName = findViewById(R.id.editReservName);

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
                    Toast.makeText(EditReservationActivity.this, "Veuiller verifier vos données!", Toast.LENGTH_LONG).show();
                    return;
                }
                putReserv = new PutReserv();
                putReserv.execute();
            }
        });
    }

    private Boolean isDataValid(){
        if(dateTimeReserv.getText().toString().trim().length() > 0 && passengerName.getText().toString().trim().length() > 0 && placeData.size() > 0 && flightData.size() > 0) {
            return true;
        }
        return false;
    }


    private void editReservation(){
        Intent intent=new Intent();
        intent.putExtra("data", currentReservationData);
        setResult(RequestCode.REQUEST_CODE_EDIT_RESERV,intent);
        finish();
    }
    private void cancelRequests(){
        if(callGetVol !=null) callGetVol.cancel();
        if(getVol != null) getVol.cancel(true);
        if(callGetPlace!=null) callGetPlace.cancel();
        if(getPlace != null) getPlace.cancel(true);
        if(callPutReserv !=null) callPutReserv.cancel();
        if(putReserv !=null) putReserv.cancel(true);
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
                new TimePickerDialog(EditReservationActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(EditReservationActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);
        numReserv = findViewById(R.id.editReservId);
        numVol = (Spinner) findViewById(R.id.editReservVol);
        numPlace = (Spinner) findViewById(R.id.editReservPlace);
        loading = findViewById(R.id.editFlightLoading);
        loadingPlace = findViewById(R.id.editLoadingPlace);
        currentReservationData = (ReservationDataModel) getIntent().getParcelableExtra("data");
        getVol = new GetVol();
        getVol.execute();

    }

    private ArrayList<Integer> generatePlaceData(){
        ArrayList<Integer> l = new ArrayList<>();
        if(flightData.get(numVolPosition).getNum_vol() == currentReservationData.getNum_vol()){
            l.add(currentReservationData.getNum_place());
        }
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
                            Toast.makeText(EditReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        flightData = response.body().getData();
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<VolJsonDataModel> call, Throwable t) {
                        Toast.makeText(EditReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(EditReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        placeList = response.body().getData();
                        placeData = new ArrayList<>();
                        placeData = generatePlaceData();
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<PlaceJsonDataModel> call, Throwable t) {
                        Toast.makeText(EditReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
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
    private class PutReserv extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            loading.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Reservation r = ApiCallConfig.retrofit.create(Reservation.class);

                callPutReserv = r.putReservation(currentReservationData.getNum_reservation(), new ReservationDataModel(currentReservationData.getNum_reservation(), flightData.get(numVolPosition).getNum_vol(), placeData.get(numPlacePosition), dateTimeReserv.getText().toString().trim(), passengerName.getText().toString().trim()));
                callPutReserv.enqueue(new Callback<ReservationJsonDataModel>() {
                    @Override
                    public void onResponse(Call<ReservationJsonDataModel> call, Response<ReservationJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(EditReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        currentReservationData = response.body().getData().get(0);
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<ReservationJsonDataModel> call, Throwable t) {
                        Toast.makeText(EditReservationActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
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
                editReservation();
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
            loading.setVisibility(View.GONE);
        }
    }

}