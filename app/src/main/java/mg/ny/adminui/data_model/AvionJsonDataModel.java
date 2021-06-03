package mg.ny.adminui.data_model;

import java.util.ArrayList;

public class AvionJsonDataModel {
    private String success;
    private ArrayList<AvionDataModel> data;
    public ArrayList<AvionDataModel> getData(){
        return data;
    }
    public String getSuccess(){
        return success;
    }
}

