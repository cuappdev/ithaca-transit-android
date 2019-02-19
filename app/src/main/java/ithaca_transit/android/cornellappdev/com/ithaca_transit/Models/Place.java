package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

public final class Place extends LocationObject{
    private String detail;
    private String detailKey;
    private String name;
    private String nameKey;
    private String placeID;
    private String placeIDKey;

    public Place(String name) {
        super();
        this.name = name;
    }

    public Place(String name, String detail, String placeID) {
        super();
        this.detail = detail;
        this.name = name;
        this.placeID = placeID;
    }

    public final String getDetail() {
        return this.detail;
    }

    public final void setDetail(String var1) {
        this.detail = var1;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String var1) {
        this.name = var1;
    }

    public final String getPlaceID() {
        return this.placeID;
    }

    public final void setPlaceID( String var1) {
        this.placeID = var1;
    }

    public final String getDetailKey() {
        return this.detailKey;
    }

    public final String getNameKey() {
        return this.nameKey;
    }

    public final String getPlaceIDKey() {
        return this.placeIDKey;
    }
}
