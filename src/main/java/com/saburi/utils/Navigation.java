/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.utils;

import com.saburi.controller.EditController;
import com.saburi.controller.SearchController;
import com.saburi.smartcoder.App;
import static com.saburi.utils.FXUIUtils.errorMessage;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Hp
 */
public class Navigation {

    private static final Class TYPE = App.class;
    public static String styleControls = App.class.getResource("StyleControls.css").toExternalForm();

    public static FXMLLoader getUILoader(String ui) {
        try {
            return new FXMLLoader(TYPE.getResource(ui + ".fxml"));
        } catch (Exception e) {
            throw e;
        }
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TYPE.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void showDialog(Parent root, String title, Node node) throws IOException {
        try {

            Scene scene = new Scene(root);
            scene.getStylesheets().add(styleControls);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.initOwner(node.getScene().getWindow());
            stage.setMaximized(false);
            stage.showAndWait();

        } catch (Exception e) {
            throw e;
        }

    }

    public static void loadUI(Node node, String ui, boolean resize) {
        try {
            Parent settingsRoot = loadFXML(ui);
            Scene scene = new Scene(settingsRoot);
            scene.getStylesheets().add(styleControls);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.resizableProperty().setValue(resize);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.initOwner(node.getScene().getWindow());
            stage.show();

        } catch (IOException ex) {
            errorMessage(ex);
        }

    }

    public static void loadUI(Node node, MenuItem menuItem, String ui, boolean resize) {
        menuItem.setOnAction(e -> {

            loadUI(node, ui, resize);
        });
    }

    public static void loadEditUi(Node node, MenuItem menuItem, String ui, String title, boolean resize) {
        menuItem.setOnAction(e -> {

            try {
                loadEditUI(node, ui, title, resize);
            } catch (IOException ex) {
                errorMessage(ex);
            }
        });
    }

    public static void loadSearchEngine(MenuItem menuItem, List<TreeItem> treeItems, boolean maximised) throws IOException {
        String searchUI = "Search";
        menuItem.setOnAction(e -> {
            try {
                FXMLLoader loader = getUILoader(searchUI);
                Parent root = loader.load();
                SearchController controller = loader.<SearchController>getController();
                controller.setTreeItems(treeItems);
                Scene scene = new Scene(root);
                scene.getStylesheets().add(styleControls);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                errorMessage(ex);
            }
        });

    }

    public static void loadEditUI(Node node, String uiName, String title, boolean resize) throws IOException {
        try {

            FXMLLoader loader = getUILoader(uiName);
            Parent root = loader.load();
            EditController controller = loader.<EditController>getController();
            controller.init();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(styleControls);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.resizableProperty().setValue(resize);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.initOwner(node.getScene().getWindow());

            stage.show();
        } catch (IOException ex) {
            errorMessage(ex);

        }

    }

    public static void loadEditUI(Node node,String uiName, String title, Object objectID, TableView tableView, boolean isPopup, boolean resize) throws IOException {
        try {

            FXMLLoader loader = getUILoader(uiName);
            Parent root = loader.load();
            EditController controller = loader.<EditController>getController();
            controller.init();
            controller.setObjectID(objectID);
            controller.setTableView(tableView);

            if (isPopup) {
                showDialog(root, title, tableView);
            } else {
                loadUI(node, uiName, resize);

            }
        } catch (IOException ex) {
            errorMessage(ex);

        }

    }

}
