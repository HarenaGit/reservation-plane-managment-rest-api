package mg.ny.adminui.apiCall;

import mg.ny.adminui.data_model.PlaceJsonDataModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Place {
    @GET("place/{id}")
    Call<PlaceJsonDataModel> getPlaceByNumVol(@Path("id") Integer num_vol);
}
