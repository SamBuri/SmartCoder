/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CLINICMASTER13
 */
public class Settings {

    private static Settings AppSettings;
    private static final String FILE_NAME = "AppSettings.properties";
    private static final Properties PROPERTIES = new Properties();
    private static int lineBreak;
    private static int miniLineBreak;

    public Settings() {
    }

    public Settings(int lineBreak, int miniLineBreak) {
        Settings.lineBreak = lineBreak;
        Settings.miniLineBreak = miniLineBreak;
    }

    public static Settings getAppSettings() {
        return AppSettings;
    }

    public static void setAppSettings(Settings aAppSettings) {
        AppSettings = aAppSettings;
    }

    public int getLineBreak() {
        return lineBreak;
    }

    public void setLineBreak(int lineBreak) {
        Settings.lineBreak = lineBreak;
    }

    public int getMiniLineBreak() {
        return miniLineBreak;
    }

    public void setMiniLineBreak(int miniLineBreak) {
        Settings.miniLineBreak = miniLineBreak;
    }

    public static void writeProperties(Settings settings) {
        FileWriter writer;
        try {
            writer = new FileWriter(FILE_NAME);
            PROPERTIES.setProperty("LineBreaker", String.valueOf(settings.getLineBreak()));
            PROPERTIES.setProperty("MinLineBreaker", String.valueOf(settings.getMiniLineBreak()));
            PROPERTIES.store(writer, null);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Settings readProperties() throws Exception {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            PROPERTIES.load(reader);
            lineBreak = Integer.parseInt(PROPERTIES.getOrDefault("LineBreaker", 15).toString());
            miniLineBreak = Integer.parseInt(PROPERTIES.getOrDefault("MinLineBreaker", 6).toString());
        }
        return new Settings(lineBreak, miniLineBreak);
    }

   

}
