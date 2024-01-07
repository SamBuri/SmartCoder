/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder;

import com.saburi.utils.FXUIUtils;
import static com.saburi.utils.FXUIUtils.errorMessage;
import static com.saburi.utils.Navigation.loadEditUi;
import static com.saburi.utils.Navigation.loadFXML;
import static com.saburi.utils.Navigation.loadSearchEngine;
import static com.saburi.utils.Navigation.loadUI;
import com.saburi.utils.SearchTree;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    Button btnPrint;
    @FXML
    private MenuItem mniSetting, mniView, spmProjectAdd, spmProjectView;
    @FXML
    private Label lblStatusBar;

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
            
            loadUI(stpMain, mniSetting, "Settings", false);
            loadEditUi(stpMain, spmProjectAdd, "Project", "Project", false);
            loadSearchEngine(mniView, new SearchTree().getTreeItems(), false);
            btnPrint.setOnAction((ActionEvent e) -> {
//                ObservableSet<Printer> printers = Printer.getAllPrinters();
//                printers.forEach((printer) -> {
//                    FXUIUtils.message(printer.getName());
//                });

//                Printer printer = Printer.getDefaultPrinter();
//                if (printer != null) {
//                    FXUIUtils.message(printer.getName());
//                }
                lblStatusBar.textProperty().unbind();
                PrinterJob printerJob = PrinterJob.createPrinterJob();
                if (printerJob != null) {
                    lblStatusBar.textProperty().bind(printerJob.jobStatusProperty().asString());
                    if (printerJob.showPageSetupDialog(btnPrint.getScene().getWindow())) {
                        if (printerJob.printPage(root)) {
                            printerJob.endJob();
                        } else {
                            lblStatusBar.textProperty().unbind();
                        }
                    }
                } else {
                    FXUIUtils.message("Error creating a print job");
                }
            });

//            stpMain.getScene().getWindow().setOnCloseRequest(e -> System.exit(0));
        } catch (IOException e) {
            errorMessage(e);
        }
    }
    
}
