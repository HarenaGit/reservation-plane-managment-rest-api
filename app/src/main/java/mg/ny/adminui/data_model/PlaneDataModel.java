package mg.ny.adminui.data_model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaneDataModel implements Parcelable {
    private String id;
    private String name;
    private String planeSize;
    public PlaneDataModel(String id, String name, String planeSize){
        this.id = id;
        this.name = name;
        this.planeSize = planeSize;
    }
    public PlaneDataModel(Parcel source){
        id = source.readString();
        name = source.readString();
        planeSize = source.readString();
    }
    public String getId(){
        return  this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getPlaneSize(){
        return this.planeSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(planeSize);
    }

    public static final Creator<PlaneDataModel> CREATOR = new Creator<PlaneDataModel>() {
        @Override
        public PlaneDataModel[] newArray(int size) {
            return new PlaneDataModel[size];
        }
        @Override
        public PlaneDataModel createFromParcel(Parcel source) {
            return new PlaneDataModel(source);
        }

    };
}
