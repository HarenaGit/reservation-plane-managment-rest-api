package mg.ny.adminui.data_model;

import android.os.Parcel;
import android.os.Parcelable;

public class StaticHorizentalListModel implements Parcelable {
    private String text;
    private String detail;
    private String txt;

    private Integer num_vol;
    private String type;
    private Integer nb_places;
    private Integer nb_colonnes;

    public StaticHorizentalListModel(String text){
        this.text = text;
    }
    public StaticHorizentalListModel(String text, String detail){
        this.text = text + " / " + detail;
        this.detail = detail;
        this.txt = text;
    }
    public StaticHorizentalListModel(FlightDataModel f){
        this.text = String.valueOf(f.getNum_vol() + "/" + f.getType());
    }
    public StaticHorizentalListModel(AvionDataModel av){
        this.text = av.getType();
    }
    public StaticHorizentalListModel(ReservVolDataModel r){
        this.text = String.valueOf(r.getNum_vol()) + " / " + r.getType();
        this.detail = r.getType();
        this.txt = String.valueOf(r.getNum_vol());

        this.num_vol = r.getNum_vol();
        this.type = r.getType();
        this.nb_places = r.getNb_places();
        this.nb_colonnes = r.getNb_colonnes();
    }
    public String getText(){
        return text;
    }
    public String getTxt(){return txt;}
    public String getDetail(){return  detail;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }
    public StaticHorizentalListModel(Parcel source){
        text = source.readString();
    }
    public static final Creator<StaticHorizentalListModel> CREATOR = new Creator<StaticHorizentalListModel>() {
        @Override
        public StaticHorizentalListModel[] newArray(int size) {
            return new StaticHorizentalListModel[size];
        }
        @Override
        public StaticHorizentalListModel createFromParcel(Parcel source) {
            return new StaticHorizentalListModel(source);
        }

    };

    public Integer getNum_vol() {
        return num_vol;
    }

    public String getType() {
        return type;
    }

    public Integer getNb_places() {
        return nb_places;
    }

    public Integer getNb_colonnes() {
        return nb_colonnes;
    }
}

