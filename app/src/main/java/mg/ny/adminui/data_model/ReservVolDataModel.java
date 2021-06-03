package mg.ny.adminui.data_model;

public class ReservVolDataModel {
    private Integer num_vol;
    private String type;
    private Integer nb_places;
    private Integer nb_colonnes;

    public ReservVolDataModel(Integer num_vol, String type, Integer nb_places, Integer nb_colonnes) {
        this.num_vol = num_vol;
        this.type = type;
        this.nb_places = nb_places;
        this.nb_colonnes = nb_colonnes;
    }

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

    public void setNum_vol(Integer num_vol) {
        this.num_vol = num_vol;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNb_places(Integer nb_places) {
        this.nb_places = nb_places;
    }

    public void setNb_colonnes(Integer nb_colonnes) {
        this.nb_colonnes = nb_colonnes;
    }
}
