package com.cocoawerks.mosaic.router.client;

import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<String, Widget> views = new HashMap<>();

    static final Router INSTANCE = new Router();

    public static Router get() {
        return INSTANCE;
    }

    private Router() {}

    public void addRoute(String path, Widget view) {
        views.put(path, view);
    }

    public Widget getViewForRoute(String path) {
        return views.get(path);
    }
}
