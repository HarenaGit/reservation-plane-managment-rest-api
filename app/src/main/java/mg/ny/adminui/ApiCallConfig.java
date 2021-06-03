package mg.ny.adminui;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCallConfig {
    public static String baseUrl = "http://192.168.43.26:3000/api/";
    public static Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
}
