package mg.ny.adminui.view_logics.plane_view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import mg.ny.adminui.data_model.PostJsonDataModel;
import mg.ny.adminui.view_logics.RequestCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddplaneActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText name;
    private EditText place;
    private EditText colonne;
    private RelativeLayout loading;
    private MaterialButton save;
    private int sleepThreadTime = 1500;
    private PostAvion postAvion;
    private AvionDataModel currentAvionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplane);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        backButton = findViewById(R.id.backButtonAdd);
        name = findViewById(R.id.addPlaneName);
        place = findViewById(R.id.addPlacePlane);
        colonne = findViewById(R.id.addColonnePlane);
        loading = findViewById(R.id.addPlaneLoading);
        save = findViewById(R.id.savePlaneButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                if(!isDataValid()){
                    Toast.makeText(AddplaneActivity.this, "Veuiller verifier vos données!", Toast.LENGTH_LONG).show();
                    return;
                }
                postAvion = new PostAvion();
                postAvion.execute();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void addChangeAvion(){
        Intent intent=new Intent();
        intent.putExtra("data", currentAvionData);
        setResult(RequestCode.REQUEST_CODE_ADD_PLANE,intent);
        if(postAvion !=null) postAvion.cancel(true);
        finish();
    }



    private Boolean isDataValid(){
        if(name.getText().toString().trim().length() > 0 && place.getText().toString().trim().length() > 0 && colonne.getText().toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

    private class PostAvion extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            loading.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Avion av = ApiCallConfig.retrofit.create(Avion.class);

                    AvionDataModel avModel = new AvionDataModel(null, name.getText().toString(), Integer.parseInt(place.getText().toString()), Integer.parseInt(colonne.getText().toString()));
                    Call<PostJsonDataModel> p = av.createPost(avModel);
                    p.enqueue(new Callback<PostJsonDataModel>() {
                        @Override
                        public void onResponse(Call<PostJsonDataModel> call, Response<PostJsonDataModel> response) {
                            if(!response.isSuccessful()){
                                Toast.makeText(AddplaneActivity.this, "Veuiller verifier votre connexion internet ou les donnees que vous avez enregistrer!", Toast.LENGTH_LONG).show();
                                publishProgress(false);
                                return;
                            }
                            Integer id = response.body().getData().get(0);
                            currentAvionData = new AvionDataModel(id, name.getText().toString(), Integer.parseInt(place.getText().toString()), Integer.parseInt(colonne.getText().toString()));
                            publishProgress(true);
                        }

                        @Override
                        public void onFailure(Call<PostJsonDataModel> call, Throwable t) {
                            Toast.makeText(AddplaneActivity.this, "Veuiller verifier votre connexion internet ou les donnees que vous avez enregistrer!", Toast.LENGTH_LONG).show();
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
            if(value[0]) addChangeAvion();
        }
        @Override
        protected void onPostExecute(Void aVoid){

            loading.setVisibility(View.GONE);
        }
    }

}