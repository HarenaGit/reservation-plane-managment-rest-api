package mg.ny.adminui.data_model;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightDataModel implements Parcelable{
    private String id;
    private String plane;
    private String planeId;
    private String cost;
    private String departureCity;
    private String arrivalCity;
    private String departureDate;
    private String arrivalDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlane() {
        return plane;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(plane);
        dest.writeString(planeId);
        dest.writeString(cost);
        dest.writeString(departureCity);
        dest.writeString(arrivalCity);
        dest.writeString(departureDate);
        dest.writeString(arrivalDate);

    }
    public FlightDataModel(String id, String plane, String planeId, String cost, String departureCity, String arrivalCity, String departureDate, String arrivalDate) {
        this.id = id;
        this.plane = plane;
        this.planeId = planeId;
        this.cost = cost;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }
    public FlightDataModel(Parcel source){
        id = source.readString();
        plane = source.readString();
        planeId = source.readString();
        cost = source.readString();
        departureCity = source.readString();
        arrivalCity = source.readString();
        departureDate = source.readString();
        arrivalDate = source.readString();
    }
    public static final Creator<FlightDataModel> CREATOR = new Creator<FlightDataModel>() {
        @Override
        public FlightDataModel[] newArray(int size) {
            return new FlightDataModel[size];
        }
        @Override
        public FlightDataModel createFromParcel(Parcel source) {
            return new FlightDataModel(source);
        }

    };
}
