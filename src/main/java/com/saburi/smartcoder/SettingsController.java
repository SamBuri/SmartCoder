/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder;

import com.saburi.dataacess.SettingsDAO;
import com.saburi.model.Model;
import com.saburi.model.Settings;
import static com.saburi.utils.FXUIUtils.errorMessage;
import static com.saburi.utils.FXUIUtils.getInt;
import static com.saburi.utils.FXUIUtils.message;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author CLINICMASTER13
 */
public class SettingsController implements Initializable {

    
    @FXML
    private TextField txtBreakLine, txtMiniBreakLine;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClose;
    private SettingsDAO settingsDAO =  new SettingsDAO();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       loadSettings();
       btnSave.setOnAction(e->this.save());
       btnClose.setOnAction(e->((Node)e.getSource()).getScene().getWindow().hide());
    }    
    
    private void loadSettings(){
        try {
            
            Optional<? extends Model> settings =settingsDAO.read().parallelStream().findAny();
            
          if(settings.isPresent()){
           Settings s =  (Settings) settings.get();
            txtBreakLine.setText(String.valueOf(s.getLineBreak()));
            txtMiniBreakLine.setText(String.valueOf(s.getMiniLineBreak()));
        }
          
        } catch (IOException ex) {
            errorMessage(ex);
        } catch (Exception ex) {
            errorMessage(ex);
        }
    }

    private void save() {
        try {
            int lineBreak = getInt(txtBreakLine, "Break Line");
            int miLineBreak = getInt(txtMiniBreakLine, "Break Line");
            Settings settings = new Settings(lineBreak, miLineBreak);
            SettingsDAO sdao =  new SettingsDAO(settings);
            sdao.clearSave(settings);
//            settings.xmlEncode();
            message("Operation Successful");
        } catch (Exception e) {
            errorMessage(e);
        }
    }
    
}
