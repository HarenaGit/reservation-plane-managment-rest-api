package mg.ny.adminui.data_model;

import java.util.ArrayList;

public class StatsJsonDataModel {
    private String success;
    private ArrayList<StatsDataModel> data;
    public ArrayList<StatsDataModel> getData(){
        return data;
    }
    public String getSuccess(){
        return success;
    }
}
