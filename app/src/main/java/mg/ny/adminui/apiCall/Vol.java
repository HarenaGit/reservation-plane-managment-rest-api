package mg.ny.adminui.apiCall;

import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.PostJsonDataModel;
import mg.ny.adminui.data_model.VolJsonDataModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Vol {
    @GET("vol")
    Call<VolJsonDataModel> getVol();

    @FormUrlEncoded
    @POST("vol")
    Call<PostJsonDataModel> postVol(@Field("num_vol") Integer num_vol,
                                    @Field("num_avion") Integer num_avion,
                                    @Field("frais") Double frais,
                                    @Field("ville_depart") String ville_depart,
                                    @Field("ville_arrivee") String ville_arrivee,
                                    @Field("heure_depart") String heure_depart,
                                    @Field("heure_arrivee") String heure_arrivee);
    @FormUrlEncoded
    @PUT("vol/{id}")
    Call<VolJsonDataModel> putVol(@Path("id") Integer num_vol,
                                  @Field("num_avion") Integer num_avion,
                                  @Field("frais") Double frais,
                                  @Field("ville_depart") String ville_depart,
                                  @Field("ville_arrivee") String ville_arrivee,
                                  @Field("heure_depart") String heure_depart,
                                  @Field("heure_arrivee") String heure_arrivee);
    @DELETE("vol/{id}")
    Call<Void> deleteVol(@Path("id") Integer num_vol);


}
