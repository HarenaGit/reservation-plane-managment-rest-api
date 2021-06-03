package mg.ny.adminui.data_model;

import java.util.ArrayList;

public class ReservVolJsonDataModel {
    private String success;
    private ArrayList<ReservVolDataModel> data;
    public ArrayList<ReservVolDataModel> getData(){
        return data;
    }
    public String getSuccess(){
        return success;
    }
}
