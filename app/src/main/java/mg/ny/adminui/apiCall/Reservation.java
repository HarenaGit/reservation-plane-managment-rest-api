package mg.ny.adminui.apiCall;

import mg.ny.adminui.data_model.PostJsonDataModel;
import mg.ny.adminui.data_model.ReservVolJsonDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.data_model.ReservationJsonDataModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Reservation {
    @GET("reservation/vol")
    Call<ReservVolJsonDataModel> getReservationByVol();

    @GET("reservation/{id}")
    Call<ReservationJsonDataModel>  getReservation(@Path("id") Integer num_vol);

    @POST("reservation")
    Call<PostJsonDataModel> postReservation(@Body ReservationDataModel r);

    @DELETE("reservation/{id}")
    Call<Void> deleteReservation(@Path("id") Integer num_reservation);

    @PUT("reservation/{id}")
    Call<ReservationJsonDataModel> putReservation(@Path("id") Integer num_reservation, @Body ReservationDataModel r);
}
