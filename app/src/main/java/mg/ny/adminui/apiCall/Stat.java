package mg.ny.adminui.apiCall;

import mg.ny.adminui.data_model.StatsJsonDataModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Stat {
    @GET("stat/bilan/{month}/{year}")
    Call<StatsJsonDataModel> getBilanByMY(@Path("month") String month, @Path("year") String year);

    @GET("stat/bilan/{num_avion}/{month}/{year}")
    Call<StatsJsonDataModel> getBilanAvionByMY(@Path("num_avion") Integer num_avion, @Path("month") String month, @Path("year") String year);

    @GET("stat/bilan/{year}")
    Call<StatsJsonDataModel> getBilanByYear(@Path("year") String year);

    @GET("stat/bilanbyavion/{num_avion}/{year}")
    Call<StatsJsonDataModel> getBilanAvionByYear(@Path("num_avion") Integer num_avion, @Path("year") String year);

    @GET("stat/reservation/avion/{num_avion}/{start_date}/{end_date}")
    Call<StatsJsonDataModel> getBilanEntre2Date(@Path("num_avion") Integer num_avion, @Path("start_date") String startDate, @Path("end_date") String endDate);

}
