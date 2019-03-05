package ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;

public class LocationAutocomplete implements SearchSuggestion {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public LocationAutocomplete createFromParcel(Parcel in) {
            return new LocationAutocomplete(in);
        }

        public LocationAutocomplete[] newArray(int size) {
            return new LocationAutocomplete[size];
        }
    };

    private Place data;
    private String desc;

    public LocationAutocomplete(Place data){
        this.data = data;
        desc = data.getName();
    }

    public LocationAutocomplete(Parcel in){
        this.desc = in.readString();
    }

    @Override
    public String getBody() {
        return desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(desc);
    }
}