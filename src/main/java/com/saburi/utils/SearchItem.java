/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.utils;

import com.saburi.dataacess.DataAccess;
import com.saburi.utils.Enums.SearchItemTypes;
import java.util.List;
import javafx.scene.control.TreeItem;

/**
 *
 * @author CLINICMASTER13
 */
public class SearchItem {

    private DataAccess dbAccess;
    private List<DataAccess> dBAccesses;
    private List<SearchColumn> searchColumns;
    private SearchItemTypes searchItemTypes;
    private String uiName;
    private String title;
    private boolean constrainColumns;
    private TreeItem<SearchItem> parent;
    private Class mainClass;
    public SearchItem(String title) {
        this.title = title;
    }

    public SearchItem(SearchItemTypes searchItemTypes, String title) {
        this.searchItemTypes = searchItemTypes;
        this.title = title;
    }

    public SearchItem(Class mainClass, DataAccess dbAccess, String uiName, String title, boolean constrainColumns) {
        this.mainClass= mainClass;
        this.dbAccess = dbAccess;
        this.uiName = uiName;
        this.title = title;
        this.constrainColumns = constrainColumns;
    }

    public SearchItem(Class mainClass, DataAccess dbAccess, SearchItemTypes searchItemTypes, String uiName, String title, boolean constrainColumns) {
        this.mainClass =  mainClass;
        this.dbAccess = dbAccess;
        this.uiName = uiName;
        this.title = title;
        this.constrainColumns = constrainColumns;
        this.searchItemTypes = searchItemTypes;
    }

    public SearchItem(Class mainClass, DataAccess dbAccess, List<DataAccess> dBAccesses, List<SearchColumn> searchColumns, SearchItemTypes searchItemTypes, String uiName, String title, boolean constrainColumns) {
        this.mainClass = mainClass;
        this.dbAccess = dbAccess;
        this.dBAccesses = dBAccesses;
        this.searchColumns = searchColumns;
        this.searchItemTypes = searchItemTypes;
        this.uiName = uiName;
        this.title = title;
        this.constrainColumns = constrainColumns;
    }

    public SearchItem(Class mainClass, DataAccess dbAccess, List<DataAccess> dBAccesses, List<SearchColumn> searchColumns, String uiName, String title, boolean constrainColumns, TreeItem<SearchItem> parent) {
        this.mainClass = mainClass;
        this.dbAccess = dbAccess;
        this.dBAccesses = dBAccesses;
        this.searchColumns = searchColumns;
        this.uiName = uiName;
        this.title = title;
        this.constrainColumns = constrainColumns;
        this.parent = parent;
    }

    public SearchItem(Class mainClass, DataAccess dbAccess, String uiName, String title, boolean constrainColumns, TreeItem<SearchItem> parent) {
       this.mainClass = mainClass;
        this.dbAccess = dbAccess;
        this.uiName = uiName;
        this.title = title;
        this.constrainColumns = constrainColumns;
        this.parent = parent;
    }

    public SearchItem(Class mainClass, DataAccess dbAccess, SearchItemTypes searchItemTypes, String uiName, String title, boolean constrainColumns, TreeItem<SearchItem> parent) {
        this.mainClass = mainClass;
        this.dbAccess = dbAccess;
        this.searchItemTypes = searchItemTypes;
        this.uiName = uiName;
        this.title = title;
        this.constrainColumns = constrainColumns;
        this.parent = parent;
    }

    public SearchItem(Class mainClass, DataAccess dbAccess, List<DataAccess> dBAccesses, List<SearchColumn> searchColumns, SearchItemTypes searchItemTypes, String uiName, String title, boolean constrainColumns, TreeItem<SearchItem> parent) {
        this.mainClass = mainClass;
        this.dbAccess = dbAccess;
        this.dBAccesses = dBAccesses;
        this.searchColumns = searchColumns;
        this.searchItemTypes = searchItemTypes;
        this.uiName = uiName;
        this.title = title;
        this.constrainColumns = constrainColumns;
        this.parent = parent;
    }

    public Class getMainClass() {
        return mainClass;
    }

    public void setMainClass(Class mainClass) {
        this.mainClass = mainClass;
    }

    
    
    public DataAccess getDataAccess() {
        return dbAccess;
    }

    public void setDataAccess(DataAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    public List<DataAccess> getdBAccesses() {
        return dBAccesses;
    }

    public void setdBAccesses(List<DataAccess> dBAccesses) {
        this.dBAccesses = dBAccesses;
    }

    public List<SearchColumn> getSearchColumns() {
        return searchColumns;
    }

    public void setSearchColumns(List<SearchColumn> searchColumns) {
        this.searchColumns = searchColumns;
    }

    public SearchItemTypes getSearchItemTypes() {
        return searchItemTypes;
    }

    public void setSearchItemTypes(SearchItemTypes searchItemTypes) {
        this.searchItemTypes = searchItemTypes;
    }

    public String getUiName() {
        return uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isConstrainColumns() {
        return constrainColumns;
    }

    public void setConstrainColumns(boolean constrainColumns) {
        this.constrainColumns = constrainColumns;
    }

    public TreeItem<SearchItem> getParent() {
        return parent;
    }

    public void setParent(TreeItem<SearchItem> parent) {
        this.parent = parent;
    }

}
