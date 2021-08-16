package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;

public class Data {
    public static boolean writeToJSONP(ArrayList<Proizvod> list,String path){   //po dve metoda ( jedan par za proizvodi.json a drugi za kategorije.json
        try {
            Writer writer=new FileWriter(path);
            Gson gson = new Gson();
            gson.toJson(list,writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Proizvod> readFromJsonP(String path){
        if(!new File(path).exists()){
            return new ArrayList<>();
        }
        try {
            JsonReader reader=new JsonReader(new FileReader(path));
            Gson gson = new Gson();
            return gson.fromJson(reader,new TypeToken<ArrayList<Proizvod>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static boolean writeToJSONK(ArrayList<Kategorija> list,String path){
        try {
            Writer writer=new FileWriter(path);
            Gson gson = new Gson();
            gson.toJson(list,writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Kategorija> readFromJsonK(String path){
        if(!new File(path).exists()){
            return new ArrayList<>();
        }
        try {
            JsonReader reader=new JsonReader(new FileReader(path));
            Gson gson = new Gson();
            return gson.fromJson(reader,new TypeToken<ArrayList<Kategorija>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
