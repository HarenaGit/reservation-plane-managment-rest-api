package mg.ny.adminui.view_logics.flight_view.activity;

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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mg.ny.adminui.ApiCallConfig;
import mg.ny.adminui.R;
import mg.ny.adminui.TypeDoubleFormatter;
import mg.ny.adminui.apiCall.Avion;
import mg.ny.adminui.apiCall.Vol;
import mg.ny.adminui.data_model.AvionJsonDataModel;
import mg.ny.adminui.data_model.PostJsonDataModel;
import mg.ny.adminui.data_model.VolJsonDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.data.StaticDataGeneration;
import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.view_logics.flight_view.adapter.spinner_adapter.CustomSpinnerAdapter;
import mg.ny.adminui.view_logics.plane_view.activity.AddplaneActivity;
import mg.ny.adminui.view_logics.plane_view.activity.EditplaneActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFlightActivity extends AppCompatActivity {

    MaterialButton save;
    ImageButton backButton;
    EditText numVol;
    EditText dateTimeDepDate;
    EditText dateTimeArvDate;
    EditText depCity;
    EditText arvCity;
    EditText cost;
    Spinner plane;
    int planePosition;
    private RelativeLayout loading;
    private InputMethodManager imm;
    private ArrayList<AvionDataModel> planeList;
    private FlightDataModel currentFlightData;
    private Call<AvionJsonDataModel> callGetAvionDispo;
    private GetAvionDispo getAvionDispo;
    private PutVol putVol;
    private Call<VolJsonDataModel> call;

    private void requestsCancel(){
        if(callGetAvionDispo!=null) callGetAvionDispo.cancel();
        if(call!=null)call.cancel();
        if(putVol!=null)putVol.cancel(true);
        if(getAvionDispo!=null) getAvionDispo.cancel(true);
    }

    private void planeListReady(){

        plane = (Spinner) findViewById(R.id.editFlightPlane);
        plane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                planePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SpinnerAdapter dataAdapter = new CustomSpinnerAdapter(getApplicationContext(), planeList);
        plane.setAdapter(dataAdapter);
        planePosition = 0;
        plane.post(new Runnable() {
            @Override
            public void run() {
                plane.setSelection(planePosition);
            }
        });

        dateTimeDepDate = findViewById(R.id.editFlightDepDate);
        dateTimeArvDate = findViewById(R.id.editFlightArvDate);
        dateTimeArvDate.setInputType(InputType.TYPE_NULL);
        dateTimeDepDate.setInputType(InputType.TYPE_NULL);
        dateTimeArvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateTimeArvDate);
            }
        });
        dateTimeDepDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateTimeDepDate);
            }
        });
        dateTimeDepDate.setText(currentFlightData.getHeure_depart());
        dateTimeArvDate.setText(currentFlightData.getHeure_arrivee());

        numVol = findViewById(R.id.editFlightId);
        depCity = findViewById(R.id.editFlightDepCity);
        arvCity = findViewById(R.id.editFlightArvCity);
        cost = findViewById(R.id.editFlightCost);

        numVol.setText(String.valueOf(currentFlightData.getNum_vol()));
        depCity.setText(currentFlightData.getVille_depart());
        arvCity.setText(currentFlightData.getVille_arrivee());

        cost.setText(TypeDoubleFormatter.formatNumeric(currentFlightData.getFrais()));

        backButton = findViewById(R.id.backButtonFlight);
        save = findViewById(R.id.saveFlightButton);

        imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestsCancel();
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                if(!isEditTextValid()){
                    Toast.makeText(EditFlightActivity.this, "Veuiller verifier vos donn??es!", Toast.LENGTH_LONG).show();
                    return;
                }
                putVol = new PutVol();
                putVol.execute();
            }
        });
    }

    private Boolean isEditTextValid(){
        if(dateTimeArvDate.getText().toString().trim().length() > 0 && dateTimeDepDate.getText().toString().trim().length() > 0 && cost.getText().toString().trim().length() > 0 && depCity.getText().toString().trim().length() > 0 && arvCity.getText().toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

    private void updateViewEdit(){
        Intent intent=new Intent();
        intent.putExtra("data", currentFlightData);
        setResult(RequestCode.REQUEST_CODE_EDIT_FLIGHT,intent);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flight);
        currentFlightData = (FlightDataModel) getIntent().getParcelableExtra("data");
        loading = findViewById(R.id.editFlightLoading);
        getAvionDispo = new GetAvionDispo();
        getAvionDispo.execute();
    }

    private int getPlaneDataPosition(Integer id){
        for(int i=0; i<planeList.size();i++){
            if(planeList.get(i).getNum_avion().equals(id)) return i;
        }
        return -1;
    }

    private void showDateTimeDialog(EditText dateTimeDepDate) {
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
                        dateTimeDepDate.setText(simpleDateFormat.format(calendar.getTime()));


                    }
                };
                new TimePickerDialog(EditFlightActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(EditFlightActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private int sleepThreadTime = 1500;
    private class GetAvionDispo extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            loading.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Avion av = ApiCallConfig.retrofit.create(Avion.class);
                callGetAvionDispo = av.getAvionDispo();
                callGetAvionDispo.enqueue(new Callback<AvionJsonDataModel>() {
                    @Override
                    public void onResponse(Call<AvionJsonDataModel> call, Response<AvionJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(EditFlightActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        planeList = response.body().getData();
                        planeList.add(0, new AvionDataModel(currentFlightData.getNum_avion(), currentFlightData.getType(), currentFlightData.getNb_places(), currentFlightData.getNb_colonnes()));
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<AvionJsonDataModel> call, Throwable t) {
                        Toast.makeText(EditFlightActivity.this,"Veuiller verifier votre connection internet! SVP:(", Toast.LENGTH_LONG).show();
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
                planeListReady();
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
            loading.setVisibility(View.GONE);
        }
    }

    private class PutVol extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            loading.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Vol vol = ApiCallConfig.retrofit.create(Vol.class);
                call = vol.putVol(currentFlightData.getNum_vol(), planeList.get(planePosition).getNum_avion(), Double.valueOf(cost.getText().toString().trim()), depCity.getText().toString().trim(), arvCity.getText().toString().trim(), dateTimeDepDate.getText().toString().trim(), dateTimeArvDate.getText().toString().trim());
                call.enqueue(new Callback<VolJsonDataModel>() {
                    @Override
                    public void onResponse(Call<VolJsonDataModel> call, Response<VolJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(EditFlightActivity.this, "Veuiller verifier votre connexion internet ou les donnees que vous avez enregistrer!", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        currentFlightData = response.body().getData().get(0);
                        currentFlightData.setType(planeList.get(planePosition).getType());
                        currentFlightData.setNb_places(planeList.get(planePosition).getNb_places());
                        currentFlightData.setNb_colonnes(planeList.get(planePosition).getNb_colonnes());
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<VolJsonDataModel> call, Throwable t) {
                        Toast.makeText(EditFlightActivity.this, "Veuiller verifier votre connexion internet ou les donnees que vous avez enregistrer!", Toast.LENGTH_LONG).show();
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
            if(value[0]) updateViewEdit();
            loading.setVisibility(View.GONE);
        }
        @Override
        protected void onPostExecute(Void aVoid){
        }
    }


}