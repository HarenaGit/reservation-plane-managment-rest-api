package mg.ny.adminui.view_logics.reservation_view.activity;

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
import mg.ny.adminui.data.StaticDataGeneration;
import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import mg.ny.adminui.view_logics.reservation_view.adapter.spiner_adapter.NumPlaceSpinnerAdapter;
import mg.ny.adminui.view_logics.reservation_view.adapter.spiner_adapter.NumVolSpinnerAdapter;

public class AddReservationActivity extends AppCompatActivity {

    MaterialButton save;
    ImageButton backButton;
    EditText numReserv;
    Spinner numVol;
    Spinner numPlace;
    EditText passengerName;
    EditText dateTimeReserv;
    int numVolPosition;
    int numPlacePosition;

    private RelativeLayout loading;
    private InputMethodManager imm;
    private ArrayList<FlightDataModel> flightData;
    private ArrayList<Integer> placeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
        numVol = (Spinner) findViewById(R.id.addReservVol);
        numPlace = (Spinner) findViewById(R.id.addReservPlace);

        flightData = StaticDataGeneration.getFlightData();
        numVol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numVolPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SpinnerAdapter dataAdapter = new NumVolSpinnerAdapter(getApplicationContext(), flightData);
        numVol.setAdapter(dataAdapter);

        placeData = new ArrayList<>();
        for(int i=1;i<=100; i++) placeData.add(i);
        numPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numPlacePosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SpinnerAdapter placeDataAdapter = new NumPlaceSpinnerAdapter(getApplicationContext(), placeData);
        numPlace.setAdapter(placeDataAdapter);


        dateTimeReserv = findViewById(R.id.addReservDate);
        dateTimeReserv.setInputType(InputType.TYPE_NULL);

        dateTimeReserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateTimeReserv);
            }
        });

        numReserv = findViewById(R.id.addReservId);
        passengerName = findViewById(R.id.addReservName);

        backButton = findViewById(R.id.backButtonFlight);
        save = findViewById(R.id.saveFlightButton);
        loading = findViewById(R.id.addFlightLoading);
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
                        intent.putExtra("data", new ReservationDataModel(numReserv.getText().toString().trim(), flightData.get(numVolPosition).getId(), flightData.get(numVolPosition).getPlaneId(), String.valueOf(placeData.get(numPlacePosition)),flightData.get(numVolPosition).getPlane(), dateTimeReserv.getText().toString().trim(), passengerName.getText().toString().trim()));
                        setResult(RequestCode.REQUEST_CODE_ADD_RESERV,intent);
                        finish();
                    }
                }, 2000);
            }
        });

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
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        d.setText(simpleDateFormat.format(calendar.getTime()));


                    }
                };
                new TimePickerDialog(AddReservationActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(AddReservationActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}