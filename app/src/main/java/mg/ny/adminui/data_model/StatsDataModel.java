package mg.ny.adminui.data_model;

import mg.ny.adminui.TypeDoubleFormatter;

public class StatsDataModel {
    private Integer nb_reservation;
    private Integer nb_vol;
    private Double recette;

    public StatsDataModel(Integer nb_reservation, Integer nb_vol, Double recette) {
        this.nb_reservation = nb_reservation;
        this.nb_vol = nb_vol;
        this.recette = recette;
    }

    public Integer getNb_reservation() {
        return nb_reservation;
    }

    public void setNb_reservation(Integer nb_reservation) {
        this.nb_reservation = nb_reservation;
    }

    public Integer getNb_vol() {
        return nb_vol;
    }

    public void setNb_vol(Integer nb_vol) {
        this.nb_vol = nb_vol;
    }

    public Double getRecette() {
        return recette;
    }

    public String getFormatedRecette(){
       return TypeDoubleFormatter.format(recette);
    }

    public void setRecette(Double recette) {
        this.recette = recette;
    }
}
