/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder;

import static com.saburi.utils.FXUIUtils.errorMessage;
import static com.saburi.utils.Navigation.loadEditUi;
import static com.saburi.utils.Navigation.loadFXML;
import static com.saburi.utils.Navigation.loadSearchEngine;
import static com.saburi.utils.Navigation.loadUI;
import com.saburi.utils.SearchTree;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author ClinicMaster13
 */
public class SceneController implements Initializable {

    @FXML
    private StackPane stpMain;
    @FXML
    Button btnGenerate;
    @FXML
    private MenuItem mniSetting, mniProject, mniView;

    /**
     * Initializes the controller class.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Parent root = loadFXML("SmartCoder");
            stpMain.getChildren().add(root);

            loadUI(mniSetting, "Settings", false);
            loadEditUi(mniProject, "Project", "Project", false);
            loadSearchEngine(mniView, new SearchTree().getTreeItems(), false);

//            stpMain.getScene().getWindow().setOnCloseRequest(e -> System.exit(0));

        } catch (IOException e) {
            errorMessage(e);
        }
    }

}
