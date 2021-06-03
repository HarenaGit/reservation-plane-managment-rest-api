package mg.ny.adminui.data_model;

import java.util.ArrayList;

public class ReservationJsonDataModel {
    private String success;
    private ArrayList<ReservationDataModel> data;
    public ArrayList<ReservationDataModel> getData(){
        return data;
    }
    public String getSuccess(){
        return success;
    }
}
