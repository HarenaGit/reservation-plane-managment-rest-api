package mg.ny.adminui.data_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightDataModel implements Parcelable{
    private Integer num_vol;
    private String type;
    private Integer num_avion;
    private Double frais;
    private String ville_depart;
    private String ville_arrivee;
    private String heure_depart;
    private String heure_arrivee;
    private Integer nb_places;
    private Integer nb_colonnes;

    public Integer getNum_vol() {
        return num_vol;
    }

    public void setNum_vol(Integer num_vol) {
        this.num_vol = num_vol;
    }

    public String getType() {
        if(type == null) return "";
        return type;
    }

    public Integer getNb_places(){
        return this.nb_places;
    }
    public Integer getNb_colonnes(){
        return this.nb_colonnes;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNum_avion() {
        return num_avion;
    }

    public void setNum_avion(Integer num_avion) {
        this.num_avion = num_avion;
    }

    public Double getFrais() {
        return frais;
    }

    public void setFrais(Double frais) {
        this.frais = frais;
    }

    public String getVille_depart() {
        return ville_depart;
    }

    public void setVille_depart(String ville_depart) {
        this.ville_depart = ville_depart;
    }

    public String getVille_arrivee() {
        return ville_arrivee;
    }

    public void setVille_arrivee(String ville_arrivee) {
        this.ville_arrivee = ville_arrivee;
    }

    public String getHeure_depart() {
        return formatDate(heure_depart);
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

    public void setHeure_depart(String heure_depart) {
        this.heure_depart = heure_depart;
    }

    public String getHeure_arrivee() {
        return formatDate(heure_arrivee);
    }

    public void setHeure_arrivee(String heure_arrivee) {
        this.heure_arrivee = heure_arrivee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num_vol);
        dest.writeString(type);
        dest.writeInt(num_avion);
        dest.writeDouble(frais);
        dest.writeString(ville_depart);
        dest.writeString(ville_arrivee);
        dest.writeString(heure_depart);
        dest.writeString(heure_arrivee);
        dest.writeInt(nb_places);
        dest.writeInt(nb_colonnes);
    }
    public FlightDataModel(Integer num_vol, Integer num_avion, Double frais, String ville_depart, String ville_arrivee, String heure_depart, String heure_arrivee) {
        this.num_vol = num_vol;
        this.type = "";
        this.num_avion = num_avion;
        this.frais = frais;
        this.ville_depart = ville_depart;
        this.ville_arrivee = ville_arrivee;
        this.heure_depart = heure_depart;
        this.heure_arrivee = heure_arrivee;
    }
    public FlightDataModel(Integer num_vol, String type, Integer num_avion, Double frais, String ville_depart, String ville_arrivee, String heure_depart, String heure_arrivee) {
        this.num_vol = num_vol;
        this.type = type;
        this.num_avion = num_avion;
        this.frais = frais;
        this.ville_depart = ville_depart;
        this.ville_arrivee = ville_arrivee;
        this.heure_depart = heure_depart;
        this.heure_arrivee = heure_arrivee;
    }
    public FlightDataModel(Parcel source){
        num_vol = source.readInt();
        type = source.readString();
        num_avion = source.readInt();
        frais = source.readDouble();
        ville_depart = source.readString();
        ville_arrivee = source.readString();
        heure_depart = source.readString();
        heure_arrivee = source.readString();
        nb_places = source.readInt();
        nb_colonnes = source.readInt();
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
