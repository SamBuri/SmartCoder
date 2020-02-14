/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.controller;

import static com.saburi.utils.FXUIUtils.errorMessage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 *
 * @author CLINICMASTER13
 */
public class ViewController extends AbstractViewController {

    @FXML
    protected TableView tblDBAccess;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.tableView = tblDBAccess;
            initSearchEvents();
        } catch (Exception e) {
            errorMessage(e);
        }
    }



}
