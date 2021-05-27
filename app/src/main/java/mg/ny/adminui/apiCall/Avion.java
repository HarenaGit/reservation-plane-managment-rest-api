package mg.ny.adminui.apiCall;

import java.util.ArrayList;

import mg.ny.adminui.data_model.AvionDataModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Avion {
    @GET("avion")
    Call<ArrayList<AvionDataModel>> getAvion();
}
