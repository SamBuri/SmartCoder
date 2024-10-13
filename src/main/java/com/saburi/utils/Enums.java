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
        ID_Helper, ID_Generator, Display, UI_Only, Query_Only, Read_Only, None
    };

    public static enum UIControls {
        Label, TextField, ComboBox, DatePicker, CheckBox, TextArea, 
        MultiSelectCombo, ImageView, TableView, FileBrowser, SNumberInput, SelectField, MultiSelectField
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

    public static enum PackageTypes {
        Entity, DBAcess, Controller, Util
    };

    public enum ProjectTypes {
        Springboot_API, Vue, Vue3, Desktop
    };

    public enum WebFiles {
        All, Entity, Repo, Service, Controller, Request, Mini, Change_Log, ServiceTest, ControllerTest //, SecurityConfig
    };
    
    public enum VueFiles {
        All, Model, View, Store, Nav,Search
    };
    
     public enum Vue3Files {
        All, Model,Controller, View, Store, Nav,Search
    };

    public enum DesktopFiles {
        All, Entity, DBAcess, Controller, ViewController, FXML, FXML_View, Menu, SQL
    }

    public enum EntityTypes {
        Auto_ID_Int, Auto_ID_Long, Auto_ID_Gen, DB_Entity
    }

    public enum ServiceTypes {
        Base, Read_Only, ID_Gen
    }

    public enum ControllerTypes {
        Base, Read_Only
    }
   
}
