package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Place {
    private String detail;
    private String detailKey;
    private String name;
    private String nameKey;
    private String placeID;
    private String placeIDKey;

    @Nullable
    public final String getDetail() {
        return this.detail;
    }

    public final void setDetail(@Nullable String var1) {
        this.detail = var1;
    }

    @Nullable
    public final String getName() {
        return this.name;
    }

    public final void setName(@Nullable String var1) {
        this.name = var1;
    }

    @Nullable
    public final String getPlaceID() {
        return this.placeID;
    }

    public final void setPlaceID(@Nullable String var1) {
        this.placeID = var1;
    }

    @NotNull
    public final String getDetailKey() {
        return this.detailKey;
    }

    @NotNull
    public final String getNameKey() {
        return this.nameKey;
    }

    @NotNull
    public final String getPlaceIDKey() {
        return this.placeIDKey;
    }

    public boolean equals(@Nullable Object other) {
        if ((Place)this == other) {
            return true;
        } else if (Intrinsics.areEqual(this.getClass(), other != null ? other.getClass() : null) ^ true) {
            return false;
        } else if (other == null) {
            throw new TypeCastException("null cannot be cast to non-null type " +
                    "ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place");
        } else {
            return placeID == ((Place) other).placeID;
        }
    }

    public Place(String name) {
        super();
        this.name = name;
    }

    public Place(@NotNull String name, @NotNull String detail, @NotNull String placeID) {
        super();
        this.detail = detail;
        this.name = name;
        this.placeID = placeID;
    }
}
