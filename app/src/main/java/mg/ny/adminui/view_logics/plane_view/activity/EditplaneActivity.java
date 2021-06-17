package mg.ny.adminui.view_logics.plane_view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import mg.ny.adminui.ApiCallConfig;
import mg.ny.adminui.apiCall.Avion;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.R;
import mg.ny.adminui.data_model.AvionJsonDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditplaneActivity extends AppCompatActivity {

    private AvionDataModel data;
    private ImageButton backButton;
    private EditText id;
    private EditText name;
    private EditText placeCount;
    private EditText colonne;
    private PutAvion putAvion;
    private Call<AvionJsonDataModel> call;
    private MaterialButton save;
    private RelativeLayout loading;
    private int sleepThreadTime = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplane);
        data = (AvionDataModel) getIntent().getParcelableExtra("data");
        backButton = findViewById(R.id.backButtonEdit);
        id = findViewById(R.id.editPlaneNumber);
        name = findViewById(R.id.editPlaneName);
        placeCount = findViewById(R.id.editPlacePlane);
        colonne = findViewById(R.id.editColonnePlane);

        id.setText(String.valueOf(data.getNum_avion()));
        id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                id.setText(data.getNum_avion());
                return false;
            }
        });
        name.setText(data.getType());
        name.requestFocus();
        placeCount.setText(String.valueOf(data.getNb_places()));
        colonne.setText(String.valueOf(data.getNb_colonnes()));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                cancelRequests();
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
        loading = findViewById(R.id.editPlaneLoading);
        save = findViewById(R.id.saveEditPlane);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                if(!isDataValid()){
                    Toast.makeText(EditplaneActivity.this, "Veuiller verifier vos données!", Toast.LENGTH_LONG).show();
                    return;
                }
                cancelRequests();
                putAvion = new PutAvion();
                putAvion.execute();
            }
        });
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void putAvionChange(){
        Intent intent = new Intent();
        intent.putExtra("data", data);
        setResult(RequestCode.REQUEST_CODE_EDIT_PLANE, intent);
        finish();
    }

    private Boolean isDataValid(){
        if(name.getText().toString().trim().length() > 0 && placeCount.getText().toString().trim().length() > 0 && colonne.getText().toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

    private void cancelRequests(){
        if(call!=null) call.cancel();
        if(putAvion!=null) putAvion.cancel(true);
    }

    private class PutAvion extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            loading.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Avion av = ApiCallConfig.retrofit.create(Avion.class);
                AvionDataModel d = new AvionDataModel(data.getNum_avion(), name.getText().toString().trim(), Integer.parseInt(placeCount.getText().toString().trim()), Integer.parseInt(colonne.getText().toString().trim()));
                call = av.putAvion(data.getNum_avion(), d);

                call.enqueue(new Callback<AvionJsonDataModel>() {
                    @Override
                    public void onResponse(Call<AvionJsonDataModel> call, Response<AvionJsonDataModel> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(EditplaneActivity.this, "Veuiller verifier votre connexion internet ou les donnees que vous avez enregistrer!", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        data = response.body().getData().get(0);
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<AvionJsonDataModel> call, Throwable t) {
                        Toast.makeText(EditplaneActivity.this, "Veuiller verifier votre connexion internet ou les donnees que vous avez enregistrer!", Toast.LENGTH_LONG).show();
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
                putAvionChange();
            }
            loading.setVisibility(View.GONE);
        }
        @Override
        protected void onPostExecute(Void aVoid){

        }
    }
}