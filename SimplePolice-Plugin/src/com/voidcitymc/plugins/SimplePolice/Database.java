package com.voidcitymc.plugins.SimplePolice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;

public class Database<T> {
    private String filePath;
    private ArrayList<T> data = new ArrayList<>();
    private TypeToken typeToken;

    public Database(String databaseName, TypeToken<ArrayList<T>> typeToken) {
        this.typeToken = typeToken;
        this.filePath = SimplePolice.getPluginFolderPath()+File.separator+"Database"+File.separator+databaseName+".json";
    }

    public boolean load() {
        File databaseFile = new File(filePath);
        if (!databaseFile.exists()) {
            try {
                if (databaseFile.createNewFile()) {
                    save();
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }


        try {
            Gson gson = new GsonBuilder().create();
            Reader reader = new FileReader(databaseFile);
            data = gson.fromJson(reader, typeToken.getType());
            if (data == null) {
                data = new ArrayList<>();
            }
            reader.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean save() {
        File databaseFile = new File(filePath);
        if (!databaseFile.exists()) {
            try {
                if (!databaseFile.createNewFile()) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }

        try {
            Writer writer = new FileWriter(databaseFile);
            Gson gson = new GsonBuilder().create();
            gson.toJson(data, writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void add(T item) {
        this.data.add(item);
    }

    public void remove(T item) {
        this.data.remove(item);
    }

}
