package mg.ny.adminui.data_model;

import android.os.Parcel;
import android.os.Parcelable;

public class AvionDataModel implements Parcelable {
    private String num_avion;
    private String type;
    private String nb_places;
    private String nb_colonnes;
    public AvionDataModel(String num_avion, String type, String nb_places){
        this.num_avion = num_avion;
        this.type = type;
        this.nb_places = nb_places;
    }
    public AvionDataModel(String num_avion, String type, String nb_places, String nb_colonnes){
        this.num_avion = num_avion;
        this.type = type;
        this.nb_places = nb_places;
        this.nb_colonnes = nb_colonnes;
    }
    public AvionDataModel(Parcel source){
        num_avion = source.readString();
        type = source.readString();
        nb_places = source.readString();
    }
    public String getNum_avion(){
        return  this.num_avion;
    }
    public String getType(){
        return this.type;
    }
    public String getNb_places(){
        return this.nb_places;
    }
    public String getNb_colonnes() { return nb_colonnes; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(num_avion);
        dest.writeString(type);
        dest.writeString(nb_places);
        dest.writeString(nb_colonnes);
    }

    public static final Creator<AvionDataModel> CREATOR = new Creator<AvionDataModel>() {
        @Override
        public AvionDataModel[] newArray(int size) {
            return new AvionDataModel[size];
        }
        @Override
        public AvionDataModel createFromParcel(Parcel source) {
            return new AvionDataModel(source);
        }

    };


}
