package com.voidcitymc.plugins.SimplePolice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;

public class Database {
    private String filePath;
    private ArrayList<Object> data = new ArrayList<>();

    public Database(String databaseName) {
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
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Reader reader = new FileReader(databaseFile);
            data = gson.fromJson(reader, new TypeToken<ArrayList<Object>>(){}.getType());
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

    public ArrayList<Object> getData() {
        return data;
    }

    public void add(Object item) {
        this.data.add(item);
    }

    public void remove(Object item) {
        this.data.remove(item);
    }

}
