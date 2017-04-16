package com.dcordero.ibadajoz.core.storage;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Storage {

    public static void saveObject(Context context, String storageName, Object object) {
        try {
            FileOutputStream fos = context.openFileOutput(storageName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        }
        catch (IOException e) {};
    }

    public static Object readObject(Context context, String storageName) {
        try {
            FileInputStream fis = context.openFileInput(storageName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return object;
        }
        catch (IOException e) {}
        catch (ClassNotFoundException e) {}
        return null;
    }
}
