/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.dataacess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saburi.model.Model;
import com.saburi.utils.SearchColumn;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public abstract class DataAccess {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    protected String baseurl = "DataFiles/";
    protected String fileName;
    protected List<SearchColumn> searchColumns = new ArrayList<>();
    protected Model model;
    
    protected File createFile() throws IOException{
        try {
            File file = new File(this.fileName);
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

    public List<SearchColumn> getSearchColumns() {
        return this.searchColumns;
    }

    public Model getModel() {
        return model;
    }
  
    public abstract List<DataAccess> get() throws Exception;

    public abstract void remove(Model model) throws Exception;

   
    


    

}
