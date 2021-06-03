package mg.ny.adminui.data_model;

import java.util.ArrayList;

public class VolJsonDataModel {
    private String success;
    private ArrayList<FlightDataModel> data;
    public ArrayList<FlightDataModel> getData(){
        return data;
    }
    public String getSuccess(){
        return success;
    }
}
