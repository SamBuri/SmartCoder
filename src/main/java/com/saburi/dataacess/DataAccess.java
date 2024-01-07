/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.dataacess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.saburi.model.Model;
import com.saburi.utils.SearchColumn;
import com.saburi.utils.Utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public abstract class DataAccess{

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    protected String baseurl = System.getProperty("user.home").concat("/".concat("Smart Coder")).concat("/").concat("Data/");
//    protected String fileName;
    protected List<SearchColumn> searchColumns = new ArrayList<>();
   
    protected File createFile() throws IOException{
        try {
            Utilities.makeDirectory(baseurl);
            File file = new File(this.baseurl.concat(this.getFileName()));
            file.getParentFile().mkdir();
            file.createNewFile();
            return file;
        } catch (IOException e) {
            throw  e;
        }
    }
    protected void saveJson(List<Model> models, Model model) throws IOException, Exception {

        try (FileWriter fileWriter = new FileWriter(createFile())) {
            if(models.contains(model)){models.remove(model);}
            models.add(model);
            models.removeIf(p->p.getDisplay()==null);
            GSON.toJson(models, fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            throw e;
        }

    }
    protected void saveJson(List models) throws IOException, Exception {

        try (FileWriter fileWriter = new FileWriter(createFile())) {
            GSON.toJson(models, fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            throw e;
        }

    }
    
    protected  abstract List<?extends Model> read() throws Exception;

    public List<SearchColumn> getSearchColumns() {
        return this.searchColumns;
    }

    public abstract Model getModel();
    
    
//        public List<? extends Model> read() throws Exception {
//        List<? extends Model> models = this.readJson();
//        return models != null ? models : new ArrayList<>();
//
//    }

    public void save(Model model) throws IOException, Exception {
        List<? extends Model> models = read();
        this.saveJson((List<Model>) models, model);
    }

    
    protected abstract String getFileName();
  
    public abstract List<DataAccess> get() throws Exception;

   
    public void remove(Model model) throws Exception {
         List<? extends Model> models = this.read();
        models.remove(model);
        this.saveJson(models);
    }
   
   public void clearSave(Model model) throws IOException, Exception {
        List<? extends Model> models = new ArrayList<>();
        this.saveJson((List<Model>) models, model);
    }
    


    

}
