package com.dcordero.ibadajoz.core.workers;


import android.content.Context;

import com.dcordero.ibadajoz.core.models.tubasa.BusStop;
import com.dcordero.ibadajoz.core.storage.Storage;

import java.util.ArrayList;

public class FavoritesWorker {

    private static final String FAVORITOS_STORAGE = "favorites_storage";

    public static void fetchFavorites(Context context, final WorkerCallback callback)
    {
        callback.success(loadFavoritesFromStorage(context));
    }

    public static void toggleFavorite(Context context, BusStop busStop, final WorkerCallback callback)
    {
        ArrayList<BusStop> favorites = loadFavoritesFromStorage(context);
        if (favorites.contains(busStop)) {
            removeFavorite(context, busStop, callback);
        }
        else {
            saveFavorite(context, busStop, callback);
        }
    }

    public static void saveFavorite(Context context, BusStop busStop, final WorkerCallback callback)
    {
        ArrayList<BusStop> favorites = loadFavoritesFromStorage(context);
        if (!favorites.contains(busStop)) {
            favorites.add(busStop);
        }
        Storage.saveObject(context, FAVORITOS_STORAGE, favorites);
        callback.success(favorites);
    }

    public static void removeFavorite(Context context, BusStop busStop, final WorkerCallback callback)
    {
        ArrayList<BusStop> favorites = loadFavoritesFromStorage(context);
        if (favorites.contains(busStop)) {
            favorites.remove(busStop);
        }
        Storage.saveObject(context, FAVORITOS_STORAGE, favorites);
        callback.success(favorites);
    }


    private static ArrayList<BusStop>loadFavoritesFromStorage(Context context)
    {
        ArrayList<BusStop> favorites = (ArrayList<BusStop>) Storage.readObject(context, FAVORITOS_STORAGE);
        if (favorites == null) {
            favorites = new ArrayList<BusStop>();
        }
        return favorites;
    }
}
