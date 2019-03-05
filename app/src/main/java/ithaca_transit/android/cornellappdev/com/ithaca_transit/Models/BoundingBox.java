package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

public class BoundingBox {
    private Double maxLat;
    private Double minLat;
    private Double maxLong;
    private Double minLong;

    public BoundingBox(Double maxLat, Double minLat, Double maxLong, Double minLong) {
        this.maxLat = maxLat;
        this.minLat = minLat;
        this.maxLong = maxLong;
        this.minLong = minLong;
    }

    public Double getMaxLat() {
        return maxLat;
    }

    public Double getMinLat() {
        return minLat;
    }

    public Double getMaxLong() {
        return maxLong;
    }

    public Double getMinLong() {
        return minLong;
    }
}
