package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import com.google.android.gms.maps.model.LatLng;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LocationObject {
    @Nullable
    private String id;
    @Nullable
    private String name;
    @Nullable
    private Double latitude;
    @Nullable
    private Double longitude;

    @Nullable
    public final String getId() {
        return this.id;
    }

    public final void setId(@Nullable String var1) {
        this.id = var1;
    }

    @Nullable
    public final String getName() {
        return this.name;
    }

    public final void setName(@Nullable String var1) {
        this.name = var1;
    }

    @Nullable
    public final Double getLatitude() {
        return this.latitude;
    }

    public final void setLatitude(@Nullable Double var1) {
        this.latitude = var1;
    }

    @Nullable
    public final Double getLongitude() {
        return this.longitude;
    }

    public final void setLongitude(@Nullable Double var1) {
        this.longitude = var1;
    }

    @NotNull
    public final LatLng coordinates() {
        return new LatLng(latitude, longitude);
    }

    public LocationObject(@NotNull String name, @NotNull String id, double latitude, double longitude) {
        super();
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationObject() {
        this("", "", 0.0D, 0.0D);
    }

    public LocationObject(@NotNull String name) {
        this(name, "", 0.0D, 0.0D);
    }

    public LocationObject(@NotNull String name, double latitude, double longitude) {
        this(name, "", latitude, longitude);
    }
}
