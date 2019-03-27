package ithaca_transit.android.cornellappdev.com.ithaca_transit.Models;

import java.util.HashMap;

public class SectionedRoutes {
    private static final String SECTION_OPTIMAL = "optimal";
    private static final String SECTION_FROM_STOPS = "fromStops";
    private static final String SECTION_BOARDING_SOON = "boardingSoon";
    private static final String SECTION_WALKING = "walking";

    private Route[] fromStop;
    private Route[] boardingSoon;
    private Route[] walking;

    /* Represented as a list (of size 1) so that it can be made into a section in the Routes Options
       Sectioned Recycler View
    */
    private Route optimal;

    // Keeps track of a route and its original section
    private HashMap<Route, String> map;

    public SectionedRoutes(Route[] fromStop, Route[] boardingSoon, Route[] byWalking) {
        this.fromStop = fromStop;
        this.boardingSoon = boardingSoon;
        this.walking = byWalking;
    }

    public Route[] getFromStop() {
        return fromStop;
    }

    public Route[] getBoardingSoon() {
        return boardingSoon;
    }

    public Route[] getWalking() {
        return walking;
    }

    public Route getOptRoute() {

        if (fromStop.length > 0) {
            optimal = getFromStop()[0];
        } else if (boardingSoon.length > 0) {
            optimal = getBoardingSoon()[0];
        } else {
            // In this case the only route is the walking route, so we set the optimal section to
            // the
            // sole walking route and make the walking list null
            optimal = walking[0];
        }

        return optimal;
    }


}
