package mg.ny.adminui.data_model;

import java.util.ArrayList;

public class PlaceJsonDataModel {
    private String success;
    private ArrayList<PlaceDataModel> data;
    public ArrayList<PlaceDataModel> getData(){
        return data;
    }
    public String getSuccess(){
        return success;
    }
}
