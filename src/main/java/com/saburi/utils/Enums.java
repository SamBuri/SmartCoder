/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.utils;

/**
 *
 * @author CLINICMASTER13
 */
public class Enums {

    public static enum keys {
        Primary, Primary_Auto, Foreign, Unique, Unique_Group, None
    };

    public static enum Saburikeys {
        ID_Helper, ID_Generator, Display, None
    };

    public static enum FXControls {
        Label, TextField, ComboBox, DatePicker, CheckBox, TextArea, ImageView, TableView
    }

    public static enum RelationMappping {
        OneToOne, OneToMany, ManyToOne, ManyToMany
    };
    
    public static enum MenuTypes {
        Menu, SplitButton
    };
    
    public static enum FormMode {
        Save, Update
    }
    
    public enum SearchItemTypes {
        Entinty, Revision
    }
    
    public static enum PackageTypes {Entity, DBAcess, Controller, Util};

}
