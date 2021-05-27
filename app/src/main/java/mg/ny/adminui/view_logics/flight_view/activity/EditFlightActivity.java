package mg.ny.adminui.view_logics.flight_view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mg.ny.adminui.R;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.data.StaticDataGeneration;
import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.view_logics.flight_view.adapter.spinner_adapter.CustomSpinnerAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flight);
        currentFlightData = (FlightDataModel) getIntent().getParcelableExtra("data");
        plane = (Spinner) findViewById(R.id.editFlightPlane);
        planeList = StaticDataGeneration.getPlaneData();
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
        planePosition = getPlaneDataPosition(currentFlightData.getPlaneId());
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
        dateTimeDepDate.setText(currentFlightData.getDepartureDate());
        dateTimeArvDate.setText(currentFlightData.getArrivalDate());

        numVol = findViewById(R.id.editFlightId);
        depCity = findViewById(R.id.editFlightDepCity);
        arvCity = findViewById(R.id.editFlightArvCity);
        cost = findViewById(R.id.editFlightCost);

        numVol.setText(currentFlightData.getId());
        depCity.setText(currentFlightData.getDepartureCity());
        arvCity.setText(currentFlightData.getArrivalCity());
        cost.setText(currentFlightData.getCost());

        backButton = findViewById(R.id.backButtonFlight);
        save = findViewById(R.id.saveFlightButton);
        loading = findViewById(R.id.editFlightLoading);
        imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                loading.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent();
                        intent.putExtra("data", new FlightDataModel(numVol.getText().toString(), planeList.get(planePosition).getType(), planeList.get(planePosition).getNum_avion(), cost.getText().toString(), depCity.getText().toString(), arvCity.getText().toString(), dateTimeDepDate.getText().toString(), dateTimeArvDate.getText().toString()));
                        setResult(RequestCode.REQUEST_CODE_EDIT_FLIGHT,intent);
                        finish();
                    }
                }, 2000);
            }
        });

    }

    private int getPlaneDataPosition(String id){
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
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        dateTimeDepDate.setText(simpleDateFormat.format(calendar.getTime()));


                    }
                };
                new TimePickerDialog(EditFlightActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(EditFlightActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}