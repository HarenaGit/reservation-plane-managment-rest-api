package mg.ny.adminui.data_model;

import android.os.Parcel;
import android.os.Parcelable;

public class AvionDataModel implements Parcelable {
    private Integer num_avion;
    private String type;
    private Integer nb_places;
    private Integer nb_colonnes;
    public AvionDataModel(Integer num_avion, String type, Integer nb_places){
        this.num_avion = num_avion;
        this.type = type;
        this.nb_places = nb_places;
    }
    public AvionDataModel(Integer num_avion, String type, Integer nb_places, Integer nb_colonnes){
        this.num_avion = num_avion;
        this.type = type;
        this.nb_places = nb_places;
        this.nb_colonnes = nb_colonnes;
    }
    public AvionDataModel(Parcel source){
        num_avion = source.readInt();
        type = source.readString();
        nb_places = source.readInt();
        nb_colonnes = source.readInt();
    }
    public Integer getNum_avion(){
        return  this.num_avion;
    }
    public String getType(){
        return this.type;
    }
    public Integer getNb_places(){
        return this.nb_places;
    }
    public Integer getNb_colonnes() { return nb_colonnes; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(num_avion);
        dest.writeString(type);
        dest.writeInt(nb_places);
        dest.writeInt(nb_colonnes);
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
