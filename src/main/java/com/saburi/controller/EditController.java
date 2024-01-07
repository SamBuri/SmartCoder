/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.controller;

import com.saburi.dataacess.DataAccess;
import com.saburi.utils.FXUIUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Hp
 */
public abstract class EditController implements Initializable {

    @FXML
    protected Button btnSave, btnSearch, btnDelete, btnClose;
    @FXML
    protected VBox mVBox;
    protected TableView tableView;
    protected Object objectID;
    protected boolean editSuccessful = false;
    protected DataAccess dbAccess;
    protected Control primaryKeyControl;
    protected String title;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void init() {
        btnClose.setOnAction(e -> ((Node) e.getSource()).getScene().getWindow().hide());
        btnSave.setOnAction(e -> {
            this.save();
            if (tableView != null && editSuccessful) {

                tableView.getItems().set(tableView.getSelectionModel().selectedIndexProperty().get(), dbAccess);
                tableView.refresh();

                btnClose.fire();
            }
        });
    }

    protected abstract void save();

    protected abstract void loadData();

    public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }

    public Object getObjectID() {
        return objectID;
    }

    public void setObjectID(Object objectID) {
        this.objectID = objectID;
        if (primaryKeyControl instanceof TextField) {
            TextField textField = (TextField) primaryKeyControl;
            textField.setText(objectID.toString());
        } else if (primaryKeyControl instanceof ComboBox) {
            ComboBox textField = (ComboBox) primaryKeyControl;
            textField.setValue(objectID.toString());
        }
        this.loadData();
    }

    protected void clear() {
        FXUIUtils.clear(mVBox);
    }

    protected void clear(List<Node> exclusions) {
        FXUIUtils.clear(mVBox, exclusions);
    }
}
