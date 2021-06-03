package mg.ny.adminui.data_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationDataModel implements Parcelable {
    private Integer num_reservation;
    private Integer num_vol;
    private Integer num_place;
    private String date_reservation;
    private String nom_voayageur;


    public ReservationDataModel(Integer num_reservation, Integer num_vol, Integer num_place, String date_reservation, String nom_voayageur) {
        this.num_reservation = num_reservation;
        this.num_vol = num_vol;
        this.num_place = num_place;
        this.date_reservation = date_reservation;
        this.nom_voayageur = nom_voayageur;
    }
    public ReservationDataModel(){
        this.num_reservation = null;
        this.num_vol = null;
        this.num_place = null;
        this.date_reservation = "";
        this.nom_voayageur = "";
    }

    public Integer getNum_reservation() {
        return num_reservation;
    }

    public void setNum_reservation(Integer num_reservation) {
        this.num_reservation = num_reservation;
    }

    public Integer getNum_vol() {
        return num_vol;
    }

    public void setNum_vol(Integer num_vol) {
        this.num_vol = num_vol;
    }


    public Integer getNum_place() {
        return num_place;
    }

    public void setNum_place(Integer num_place) {
        this.num_place = num_place;
    }

    public String getDate_reservation() {
        return formatDate(date_reservation);
    }

    public void setDate_reservation(String date_reservation) {
        this.date_reservation = date_reservation;
    }

    public String formatDate(String d){
        SimpleDateFormat enterFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = enterFormat.parse(d);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return d;
        }
    }

    public String getNom_voayageur() {
        return nom_voayageur;
    }

    public void setNom_voayageur(String nom_voayageur) {
        this.nom_voayageur = nom_voayageur;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num_reservation);
        dest.writeInt(num_vol);
        dest.writeInt(num_place);
        dest.writeString(date_reservation);
        dest.writeString(nom_voayageur);
    }
    public ReservationDataModel(Parcel source){
        num_reservation = source.readInt();
        num_vol = source.readInt();
        num_place = source.readInt();
        date_reservation = source.readString();
        nom_voayageur = source.readString();
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
