package mg.ny.adminui.view_logics.plane_view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;

import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.R;
import mg.ny.adminui.view_logics.RequestCode;

public class EditplaneActivity extends AppCompatActivity {

    private AvionDataModel data;
    private ImageButton backButton;
    private EditText id;
    private EditText name;
    private EditText placeCount;
    private MaterialButton save;
    private RelativeLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplane);
        data = (AvionDataModel) getIntent().getParcelableExtra("data");
        backButton = findViewById(R.id.backButtonEdit);
        id = findViewById(R.id.editPlaneNumber);
        name = findViewById(R.id.editPlaneName);
        placeCount = findViewById(R.id.editPlacePlane);

        id.setText(data.getNum_avion());
        id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                id.setText(data.getNum_avion());
                return false;
            }
        });
        name.setText(data.getType());
        name.requestFocus();
        placeCount.setText(data.getNb_places());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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
                loading.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("data", new AvionDataModel(id.getText().toString(), name.getText().toString(), placeCount.getText().toString()));
                        setResult(RequestCode.REQUEST_CODE_EDIT_PLANE, intent);
                        finish();
                    }
                }, 2000);
            }
        });
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}