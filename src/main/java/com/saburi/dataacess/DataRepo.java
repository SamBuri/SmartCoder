/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.dataacess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author samburiima
 */
public abstract class DataRepo<C, F, I> {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    protected String baseurl = System.getProperty("user.home").concat("/".concat("Smart Coder")).concat("/").concat("Data/");
    private String fileName;

    protected File createFile() throws IOException {
        try {
            File file = new File(this.baseurl.concat(this.fileName));
            file.getParentFile().mkdir();
            file.createNewFile();
            return file;
        } catch (IOException e) {
            throw e;
        }
    }

    protected void saveJson(List<C> models, C model) throws IOException, Exception {

        try ( FileWriter fileWriter = new FileWriter(createFile())) {
            if (models.contains(model)) {
                models.remove(model);
            }
            models.add(model);
            GSON.toJson(models, fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            throw e;
        }

    }

    protected void saveJson(List<C> models) throws IOException, Exception {

        try ( FileWriter fileWriter = new FileWriter(createFile())) {
            GSON.toJson(models, fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            throw e;
        }

    }

    protected abstract List<C> read() throws Exception;

}
