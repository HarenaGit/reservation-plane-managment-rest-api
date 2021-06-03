package mg.ny.adminui.view_logics.plane_view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

import mg.ny.adminui.ApiCallConfig;
import mg.ny.adminui.apiCall.Avion;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.view_logics.plane_view.adapter.search_adapter.PlaneListAdapter;
import mg.ny.adminui.R;
import mg.ny.adminui.view_logics.RequestCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ImageButton backButton;
    private ArrayList<AvionDataModel> data;
    private PlaneListAdapter adapter;
    private SwipeMenuListView listView;
    private InputMethodManager imm;
    private LinearLayout contentDialog;
    private RelativeLayout loadingDialog;
    private Dialog dialog;
    private DeleteAvion deleteAvion;
    private Call<Void> call;
    private  int sleepThreadTime = 1500;
    private int p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = findViewById(R.id.searchListItem);
        listView.setVisibility(View.GONE);
        searchView = findViewById(R.id.searchItem);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) searchView.findViewById(id); backButton = findViewById(R.id.backButton);
        Typeface ft = ResourcesCompat.getFont(this, R.font.segeo_ui);
        searchText.setTypeface(ft);
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



        data = getIntent().getParcelableArrayListExtra("data");

        adapter = new PlaneListAdapter(this, data);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());

                editItem.setBackground(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
                editItem.setWidth(130);
                editItem.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.backgroundColor)));
                deleteItem.setWidth(100);
                deleteItem.setIcon(R.drawable.ic_delete_swipe);

                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.remove_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button yes =  dialog.findViewById(R.id.acceptRemove);
        Button no = dialog.findViewById(R.id.declineRemove);
        contentDialog = dialog.findViewById(R.id.removeDialogContent);
        loadingDialog = dialog.findViewById(R.id.removeDialogLoading);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequests();
                deleteAvion = new DeleteAvion();
                deleteAvion.execute();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                p = position;
                switch (index) {
                    case 0:
                        Intent editActivity = new Intent(getApplicationContext(), EditplaneActivity.class);
                        AvionDataModel currentPlaneData = adapter.getElement(position);
                        editActivity.putExtra("data", currentPlaneData);
                        startActivityForResult(editActivity, RequestCode.REQUEST_CODE_EDIT_PLANE);
                        break;
                    case 1:
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        TextView textDialog = dialog.findViewById(R.id.planeRemoveId);
                        textDialog.setText("Avion numéro : " +  adapter.getElement(position).getNum_avion());
                        dialog.show();
                        break;
                }

                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equals("")) listView.setVisibility(View.VISIBLE);
                else listView.setVisibility(View.GONE);
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_CANCELED){
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            return;
        }
        if(resultCode == RequestCode.REQUEST_CODE_EDIT_PLANE)
        {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            Intent intent = new Intent();
            AvionDataModel currentData = (AvionDataModel) data.getParcelableExtra("data");
            intent.putExtra("data", currentData);
            setResult(RequestCode.REQUEST_CODE_EDIT_PLANE, intent);
            finish();
        }
    }

    private void deleteAvionChange(){
        Intent intent = new Intent();
        AvionDataModel currentPlaneData = adapter.getElement(p);
        intent.putExtra("data", currentPlaneData);
        setResult(RequestCode.REQUEST_CODE_REMOVE_PLANE, intent);
        finish();
    }

    private void cancelRequests(){
        if(call!=null) call.cancel();
        if(deleteAvion!=null) deleteAvion.cancel(true);
    }

    private class DeleteAvion extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute(){
            contentDialog.setVisibility(View.GONE);
            loadingDialog.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(sleepThreadTime);
                Avion av = ApiCallConfig.retrofit.create(Avion.class);
                call = av.deleteAvion(adapter.getElement(p).getNum_avion());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(SearchActivity.this, "Erreur, Veuiller verifier votre connexion internet!", Toast.LENGTH_LONG).show();
                            publishProgress(false);
                            return;
                        }
                        publishProgress(true);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(SearchActivity.this, "Erreur, Veuiller verifier votre connexion internet!", Toast.LENGTH_LONG).show();
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
                deleteAvionChange();
            }

        }
        @Override
        protected void onPostExecute(Void aVoid){
            contentDialog.setVisibility(View.VISIBLE);
            loadingDialog.setVisibility(View.GONE);
            dialog.dismiss();
        }
    }
}