package mg.ny.adminui.data_model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReservationDataModel implements Parcelable {
    private String id;
    private String flightId;
    private String planeId;
    private String planeName;
    private String placeNumber;
    private String reservationDate;
    private String passengerName;


    public ReservationDataModel(String id, String flightId, String planeId, String planeName, String placeNumber, String reservationDate, String passengerName) {
        this.id = id;
        this.flightId = flightId;
        this.planeId = planeId;
        this.planeName = planeName;
        this.placeNumber = placeNumber;
        this.reservationDate = reservationDate;
        this.passengerName = passengerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public String getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(String placeNumber) {
        this.placeNumber = placeNumber;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(flightId);
        dest.writeString(planeId);
        dest.writeString(planeName);
        dest.writeString(placeNumber);
        dest.writeString(reservationDate);
        dest.writeString(passengerName);
    }
    public ReservationDataModel(Parcel source){
        id = source.readString();
        flightId = source.readString();
        planeId = source.readString();
        planeName = source.readString();
        placeNumber = source.readString();
        reservationDate = source.readString();
        passengerName = source.readString();
    }
    public static final Creator<ReservationDataModel> CREATOR = new Creator<ReservationDataModel>() {
        @Override
        public ReservationDataModel[] newArray(int size) {
            return new ReservationDataModel[size];
        }
        @Override
        public ReservationDataModel createFromParcel(Parcel source) {
            return new ReservationDataModel(source);
        }

    };
}
