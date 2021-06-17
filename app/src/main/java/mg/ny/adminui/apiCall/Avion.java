package mg.ny.adminui.apiCall;

import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.data_model.AvionJsonDataModel;
import mg.ny.adminui.data_model.PostJsonDataModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Avion {
    @GET("avion")
    Call<AvionJsonDataModel> getAvion();

    @POST("avion")
    Call<PostJsonDataModel> createPost(@Body AvionDataModel avion);

    @PUT("avion/{id}")
    Call<AvionJsonDataModel> putAvion(@Path("id") int id,@Body AvionDataModel avion);

    @DELETE("avion/{id}")
    Call<Void> deleteAvion(@Path("id") int id);

    @GET("avion/aviondispo")
    Call<AvionJsonDataModel> getAvionDispo();
}
