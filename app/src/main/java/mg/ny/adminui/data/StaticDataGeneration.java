package mg.ny.adminui.data;

import java.util.ArrayList;

import mg.ny.adminui.data_model.FlightDataModel;
import mg.ny.adminui.data_model.AvionDataModel;
import mg.ny.adminui.data_model.ReservationDataModel;
import mg.ny.adminui.data_model.StaticHorizentalListModel;

public class  StaticDataGeneration {
    public static ArrayList<AvionDataModel> getPlaneData(){
        ArrayList<AvionDataModel> data = new ArrayList<>();
        data.add(new AvionDataModel(1, "Jet Privée", 56, 4));
        data.add(new AvionDataModel(2, "AIR261-45", 23, 6));
        data.add(new AvionDataModel(3, "Bus2", 14, 8));
        data.add(new AvionDataModel(4, "AIR265-85", 67, 12));
        data.add(new AvionDataModel(5, "AIR234-78", 45, 7));
        data.add(new AvionDataModel(6, "Jet Privée xoxo", 23, 6));

        return data;
    }
    public static ArrayList<StaticHorizentalListModel> getPlaneHorizentalItem(){
        ArrayList<StaticHorizentalListModel> item = new ArrayList<>();
        item.add(new StaticHorizentalListModel("Jet Privée"));
        item.add(new StaticHorizentalListModel("AIR261-45"));
        item.add(new StaticHorizentalListModel("Bus2"));
        item.add(new StaticHorizentalListModel("AIR265-85"));
        item.add(new StaticHorizentalListModel("AIR234-78"));
        item.add(new StaticHorizentalListModel("Jet Privée xoxo"));

        return item;
    }

    public static ArrayList<StaticHorizentalListModel> getPlaneDashItem(){
        ArrayList<StaticHorizentalListModel> item = new ArrayList<>();
        item.add(new StaticHorizentalListModel("Tout"));
        item.add(new StaticHorizentalListModel("Jet Privée"));
        item.add(new StaticHorizentalListModel("AIR261-45"));
        item.add(new StaticHorizentalListModel("Bus2"));
        item.add(new StaticHorizentalListModel("AIR265-85"));
        item.add(new StaticHorizentalListModel("AIR234-78"));
        item.add(new StaticHorizentalListModel("Jet Privée xoxo"));

        return item;
    }
    public static ArrayList<FlightDataModel> getFlightData(){
        ArrayList<FlightDataModel> data = new ArrayList<>();

        return data;
    }
    public static ArrayList<StaticHorizentalListModel> getFlightItem(){
        ArrayList<StaticHorizentalListModel> item = new ArrayList<>();
        item.add(new StaticHorizentalListModel("Vol-0001"));
        item.add(new StaticHorizentalListModel("Vol-0002"));
        item.add(new StaticHorizentalListModel("Vol-0003"));
        item.add(new StaticHorizentalListModel("Vol-0004"));
        item.add(new StaticHorizentalListModel("Vol-0005"));
        return item;
    }
    public static  ArrayList<ReservationDataModel> getReservationData(){
        ArrayList<ReservationDataModel> data = new ArrayList<>();

    /*    data.add(new ReservationDataModel("Rsv-0001", "Vol-0001", "AV-0001", "Jet Privée", "4", "11-05-2021 à 19:30", "Ny Harena Fitahiantsoa"));
        data.add(new ReservationDataModel("Rsv-0002", "Vol-0001", "AV-0001", "Jet Privée", "3", "12-05-2021 à 15:30", "Kantoniaina Sandra"));

        data.add(new ReservationDataModel("Rsv-0003", "Vol-0002", "AV-0002", "AIR261-45", "7", "13-05-2021 à 19:30", "Hassler Esperee"));
        data.add(new ReservationDataModel("Rsv-0004", "Vol-0002", "AV-0002", "AIR261-45", "9", "15-05-2021 à 15:30", "Jenny Rabarijaona"));

        data.add(new ReservationDataModel("Rsv-0005", "Vol-0003", "AV-0003", "Bus2", "17", "12-05-2021 à 19:30", "Amboara Fehizoro"));
        data.add(new ReservationDataModel("Rsv-0006", "Vol-0003", "AV-0003", "Bus2", "19", "18-05-2021 à 15:30", "Nekena Fiderana"));

        data.add(new ReservationDataModel("Rsv-0007", "Vol-0004", "AV-0004", "AIR265-85", "27", "12-05-2021 à 19:30", "Linah Tianaharivony"));
        data.add(new ReservationDataModel("Rsv-0008", "Vol-0004", "AV-0004", "AIR265-85", "29", "18-05-2021 à 15:30", "Yves Rasoloherimiantra"));

        data.add(new ReservationDataModel("Rsv-0009", "Vol-0005", "AV-0005", "AIR234-78", "37", "12-05-2021 à 19:30", "Ny Hasina Luc"));
        data.add(new ReservationDataModel("Rsv-00010", "Vol-0005", "AV-0005", "AIR234-78", "39", "18-05-2021 à 15:30", "Ny Hafaliana Sofie Stephanie"));
     */
        return data;
    }
    public static ArrayList<StaticHorizentalListModel> getReservationItem(){
        ArrayList<StaticHorizentalListModel> item = new ArrayList<>();
        item.add(new StaticHorizentalListModel("Vol-0001", "Jet Privée"));
        item.add(new StaticHorizentalListModel("Vol-0002", "AIR261-45"));
        item.add(new StaticHorizentalListModel("Vol-0003", "Bus2"));
        item.add(new StaticHorizentalListModel("Vol-0004", "AIR265-85"));
        item.add(new StaticHorizentalListModel("Vol-0005", "AIR234-78"));
        return item;
    }

}
