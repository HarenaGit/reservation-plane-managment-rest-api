package mg.ny.adminui.view_logics.plane_view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;

import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.R;
import mg.ny.adminui.view_logics.RequestCode;

public class AddplaneActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText id;
    private EditText name;
    private EditText place;
    private RelativeLayout loading;
    private MaterialButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplane);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        backButton = findViewById(R.id.backButtonAdd);
        id = findViewById(R.id.addPlaneNumber);
        name = findViewById(R.id.addPlaneName);
        place = findViewById(R.id.addPlacePlane);
        loading = findViewById(R.id.addPlaneLoading);
        save = findViewById(R.id.savePlaneButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                loading.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent();
                        intent.putExtra("data", new AvionDataModel(id.getText().toString(), name.getText().toString(), place.getText().toString()));
                        setResult(RequestCode.REQUEST_CODE_ADD_PLANE,intent);
                        finish();
                    }
                }, 2000);
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
}