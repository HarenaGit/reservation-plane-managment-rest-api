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
    private Integer num_avion;
    private AvionDataModel av;

    public StaticHorizentalListModel(String text){
        this.text = text;
    }
    public StaticHorizentalListModel(String text, String detail){
        this.text = text + " / " + detail;
        this.detail = detail;
        this.txt = text;
    }
    public StaticHorizentalListModel(String text, String detail, String txt){
        this.text = text;
        this.detail = detail;
        this.txt = txt;
    }
    public StaticHorizentalListModel(FlightDataModel f){
        this.text = String.valueOf("Vol-" + f.getNum_vol());
    }
    public StaticHorizentalListModel(AvionDataModel av){
        this.num_avion = av.getNum_avion();
        this.text = av.getType();
        this.av = av;
    }
    public AvionDataModel getAv(){
        return this.av;
    }
    public Integer getNum_avion(){
        return this.num_avion;
    }
    public StaticHorizentalListModel(ReservVolDataModel r){
        this.text = String.valueOf("Vol-" + r.getNum_vol()) + " / " + r.getType();
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

