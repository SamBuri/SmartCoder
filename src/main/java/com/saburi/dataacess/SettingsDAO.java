/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.dataacess;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import static com.saburi.dataacess.DataAccess.GSON;
import com.saburi.model.Model;
import com.saburi.model.Settings;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author samburiima
 */
public class SettingsDAO extends DataAccess{
     private transient final String currentFile = "Settings.json";
     private Settings settings =  new Settings();
     private final SimpleIntegerProperty lineBreak =  new SimpleIntegerProperty(this,"lineBreak");
    private final SimpleIntegerProperty miniLineBreak  =  new SimpleIntegerProperty(this,"miniLineBreak");

    public SettingsDAO(Settings settings) {
        this.settings = settings;
        this.initialseProprties();
    }

    public SettingsDAO() {
        
    }

     
      private void initialseProprties() {
          this.lineBreak.set(settings.getLineBreak());
          this.miniLineBreak.set(settings.getMiniLineBreak());
          
      }

    public Settings getSettings() {
        return settings;
    }

    public SimpleIntegerProperty getLineBreak() {
        return lineBreak;
    }

    public SimpleIntegerProperty getMiniLineBreak() {
        return miniLineBreak;
    }
      
      
     
     
     @Override
     public List<Settings> read() throws Exception {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(createFile()));
            return GSON.fromJson(bufferedReader,
                    new TypeToken<List<Settings>>() {
                    }.getType());
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            throw e;
        }
    }



    @Override
    public List<DataAccess> get() throws Exception {
       try {
            List<DataAccess> dataAccesses = new ArrayList<>();
            this.read().forEach(proj -> dataAccesses.add(new SettingsDAO((Settings) proj)));
            return dataAccesses;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Optional<Settings> getOne() throws Exception{
          return (Optional<Settings>) this.read().parallelStream().findAny();
          
    }
   

    @Override
    protected String getFileName() {
        return this.currentFile;
    }

    @Override
    public Model getModel() {
        return this.settings;
    }
    
}
