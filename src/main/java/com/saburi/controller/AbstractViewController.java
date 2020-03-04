/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.controller;

import com.saburi.dataacess.DataAccess;
import com.saburi.model.Model;
import static com.saburi.utils.FXUIUtils.errorMessage;
import static com.saburi.utils.FXUIUtils.getText;
import static com.saburi.utils.FXUIUtils.loadSearchColumnCombo;
import static com.saburi.utils.FXUIUtils.searchColumnSelected;
import static com.saburi.utils.FXUIUtils.setSearch;
import static com.saburi.utils.FXUIUtils.warningOk;
import com.saburi.utils.Navigation;
import com.saburi.utils.SearchColumn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 *
 * @author CLINICMASTER13
 */
public abstract class AbstractViewController implements Initializable {
    
    protected boolean popUp, constrainColumns;
    protected DataAccess selectedItem;
    protected DataAccess oDBAccess;
    
    protected FilteredList filteredList;
    protected List<? extends DataAccess> list = new ArrayList<>();
    protected String objectName, uiEdit, title;
    protected Class mainClass;
    protected float editUIWidth, EditUIHeight;
    protected ObservableList data;
    protected FilteredList fList;
    protected TableView tableView;
    protected boolean editSuccessful = false;
    protected DataAccess dbAccess;
    
    @FXML
    protected TextField txtSearch, txtSecondValue;
    @FXML
    private MenuItem cmiUpdate, cmiDelete, cmiSelectAll;
    @FXML
    private ComboBox<SearchColumn> cboSearchColumn;
    @FXML
    private ComboBox<SearchColumn.SearchType> cboSearchType;
    @FXML
    protected TableColumn tbcUserID;
    @FXML
    protected TableColumn tbcUserFullName;
    @FXML
    protected TableColumn tbcClientMachine;
    @FXML
    protected TableColumn tbcRecordDateTime;
    @FXML
    protected TableColumn tbcLastUpdateDateTime;
    @FXML
    protected ContextMenu cmuView;
    
    private List<SearchColumn> searchColumns;
    List<SearchColumn> defaultSearchColumns = new ArrayList<>();
    protected ObservableList selectedItems;
    protected Node editNode;
    
    public void setPopUp(boolean popUp) {
        this.popUp = popUp;
        
    }
    
    public boolean isPopUp() {
        return this.popUp;
    }
    
    public DataAccess getSelectedItem() {
        return selectedItem;
    }
    
    public void initSearchEvents() {
        if (this.tableView == null) {
            
        }
        this.tableView.setItems(FXCollections.observableArrayList());
        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
       
        cmiUpdate.setOnAction(e -> setUpdate(this.uiEdit, this.title));
        cmiDelete.setOnAction(e -> this.delete());
        cmiSelectAll.setOnAction(e -> tableView.getSelectionModel().selectAll());
        
        this.cboSearchColumn.setOnAction(e -> searchColumnSelected(this.cboSearchColumn, this.cboSearchType));
        
        cboSearchType.setOnAction(e -> {
            String searchType = getText(cboSearchType);
            this.txtSecondValue.setVisible(searchType.equals(SearchColumn.SearchType.Between.name()));
        });
        
        this.txtSearch.setOnKeyPressed(e -> {
            
            if (e.getCode() == KeyCode.ENTER) {
                
                setSearch(filteredList, cboSearchType, cboSearchColumn, txtSearch, txtSecondValue, tableView);
            }
        });
        this.txtSecondValue.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                setSearch(filteredList, cboSearchType, cboSearchColumn, txtSearch, txtSecondValue, tableView);
            }
        });
        
        tableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && popUp) {
                this.selectedItem = (DataAccess) tableView.getSelectionModel().getSelectedItem();
                if (this.editNode != null) {
                    if (editNode instanceof ComboBox) {
                        ComboBox box = (ComboBox) editNode;
                        Model entity = selectedItem.getModel();
                        if (!box.getItems().contains(entity)) {
                            box.getItems().add(0, entity);
                            
                        }
                        
                        box.setValue(entity);
                    } else if (editNode instanceof TextField) {
                        TextField field = (TextField) editNode;
                        field.setText(selectedItem.getModel().getId().toString());
                        
                    }
                    this.tableView.getScene().getWindow().hide();
                } else {
                    this.tableView.getScene().getWindow().hide();
                }
                
            }
            
        });
        
    }
    
    public void loadTable() {
        
        this.data = FXCollections.observableArrayList(this.list);
        filteredList = new FilteredList<>(data, e -> true);
        
        FilteredList<SearchColumn> filteredSearchColumns = new FilteredList<>(FXCollections.observableArrayList(searchColumns), e -> true);
        filteredSearchColumns.setPredicate((P) -> P.isVisible());
        for (int i = 0; i < filteredSearchColumns.size(); i++) {
            final int x = i;
            SearchColumn sc = filteredSearchColumns.get(x);
            TableColumn<DataAccess, Object> tableColumn = new TableColumn(sc.getDisplayValue());
            tableColumn.setCellValueFactory(param
                    -> new ReadOnlyObjectWrapper<>(param.getValue().getSearchColumns().get(param.getValue().getSearchColumns().lastIndexOf(sc)).getValue())
            );
            tableView.getColumns().add(tableColumn);
        }
        filteredSearchColumns.forEach(sc -> {
            
        });
        
        tableView.setItems(FXCollections.observableArrayList(data));
        if (constrainColumns) {
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        
        if (!data.isEmpty()) {
            
            loadSearchColumnCombo(FXCollections.observableArrayList(filteredSearchColumns), cboSearchColumn);
        }
        
        this.selectedItems = tableView.getSelectionModel().getSelectedItems();
//        cmiDelete.disableProperty().bind(Bindings.size(selectedItems).lessThan(1));
//        cmiUpdate.disableProperty().bind(Bindings.size(selectedItems).isNotEqualTo(1));
        cmiSelectAll.disableProperty().bind(Bindings.size(tableView.getItems()).lessThan(1));
        
        cmuView.setOnShowing((event) -> {
            this.viewContextMenuShowing();
        });
        
    }
    
    protected void setUpdate(String ui, String title) {

        try {

           
                DataAccess dBAccess = (DataAccess) tableView.getSelectionModel().getSelectedItem();
                Object recordID = dBAccess.getModel().getId();
                Navigation.loadEditUI(this.editNode, ui, title, recordID, tableView, true, false);
            

        } catch (IOException ex) {
            errorMessage(ex);
        }
    }
    
    protected void delete() {
        try {
            List<DataAccess> selectedList = tableView.getSelectionModel().getSelectedItems();
            if (!warningOk("Confirm Delete", "You are about to delete " + selectedList.size() + "record(s) Are you sure you want to continue?", "Remember this action cannot be un done")) {
                return;
            }
            selectedList.forEach(e -> {
                DataAccess dBAccess = (DataAccess) e;
                try {
                    dBAccess.remove(dBAccess.getModel());
                } catch (Exception ex) {
                    errorMessage(ex);
                }
                tableView.getItems().remove(e);
            });
            
        } catch (Exception e) {
            errorMessage(e);
        }
        
    }
    
    public void setInitData(Class type, DataAccess oDBAccess, String objectName, float editUIWidth, float EditUIHeight,
            boolean constrainColumns) {
        try {
            this.constrainColumns = constrainColumns;
            this.oDBAccess = oDBAccess;
            this.list = oDBAccess.get();
            this.objectName = objectName;
            this.uiEdit = objectName;
            this.mainClass = type;
            this.title = objectName;
            this.editUIWidth = editUIWidth;
            this.EditUIHeight = EditUIHeight;
            loadTable();
        } catch (Exception e) {
            errorMessage(e);
        }
        
    }
    
    public void setInitData(Class type, DataAccess oDBAccess, String objectName, boolean constrainColumns) {
        try {
            this.constrainColumns = constrainColumns;
            this.oDBAccess = oDBAccess;
            this.list = oDBAccess.get();
            this.searchColumns = this.oDBAccess.getSearchColumns();
            this.objectName = objectName;
            this.uiEdit = objectName;
            this.mainClass = type;
            this.title = objectName;
            
            loadTable();
        } catch (Exception e) {
            errorMessage(e);
        }
        
    }
    
    public void setInitData(Class type, DataAccess oDBAccess, String objectName, boolean constrainColumns, boolean popup) {
        try {
            this.constrainColumns = constrainColumns;
            this.oDBAccess = oDBAccess;
            this.list = oDBAccess.get();
            this.searchColumns = this.oDBAccess.getSearchColumns();
            this.objectName = objectName;
            this.uiEdit = objectName;
            this.mainClass = type;
            this.title = objectName;
            loadTable();
        } catch (Exception e) {
            errorMessage(e);
        }
        
    }
    
    public void setInitData(Class type, DataAccess oDBAccess, String objectName, Node editNode, boolean constrainColumns, boolean popup) {
        try {
            this.constrainColumns = constrainColumns;
            this.oDBAccess = oDBAccess;
            this.list = oDBAccess.get();
            this.searchColumns = this.oDBAccess.getSearchColumns();
            this.popUp = popup;
            this.objectName = objectName;
            this.uiEdit = objectName;
            this.mainClass = type;
            this.title = objectName;
            this.editNode = editNode;
            loadTable();
        } catch (Exception e) {
            errorMessage(e);
        }
        
    }
    
    public void setInitData(Class type, DataAccess oDBAccess, List<? extends DataAccess> dBAccesses, String objectName, boolean constrainColumns, boolean popup) {
        this.constrainColumns = constrainColumns;
        this.oDBAccess = oDBAccess;
        this.list = dBAccesses;
        this.searchColumns = oDBAccess.getSearchColumns();
        
        this.popUp = popup;
        
        this.objectName = objectName;
        this.uiEdit = objectName;
        this.mainClass = type;
        this.title = objectName;
        loadTable();
        
    }
    
    public void setInitData(Class type, DataAccess oDBAccess, List<? extends DataAccess> dBAccesses, String objectName, Node editNode, boolean constrainColumns, boolean popup) {
        this.constrainColumns = constrainColumns;
        this.oDBAccess = oDBAccess;
        this.list = dBAccesses;
        this.searchColumns = oDBAccess.getSearchColumns();
        
        this.popUp = popup;
        
        this.objectName = objectName;
        this.uiEdit = objectName;
        this.mainClass = type;
        this.title = objectName;
        this.editNode = editNode;
        loadTable();
        
    }
    
    public void setInitData(Class type, DataAccess oDBAccess, List<? extends DataAccess> dBAccesses,
            List<SearchColumn> searchColumns, String objectName, boolean constrainColumns) {
        try {
            this.constrainColumns = constrainColumns;
            this.oDBAccess = oDBAccess;
            this.list = dBAccesses;
            this.searchColumns = searchColumns;
            this.objectName = objectName;
            this.uiEdit = objectName;
            this.mainClass = type;
            this.title = objectName;
            loadTable();
        } catch (Exception e) {
            throw e;
        }
        
    }
    
    protected void viewContextMenuShowing() {
//        if (selectedItems.size() >= 1) {
//            applyRights(objectName, cmiDelete);
//        } else {
//            cmiDelete.disableProperty().set(true);
//        }
//        if (selectedItems.size() == 1) {
//            applyRights(objectName, cmiUpdate);
//        } else {
//            cmiUpdate.disableProperty().set(true);
//        }
//        applyRights(objectName, cmiNew);
    }
    
}
