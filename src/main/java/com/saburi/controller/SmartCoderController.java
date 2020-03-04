/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.controller;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.utils.FXUIUtils;
import static com.saburi.utils.FXUIUtils.browse;
import static com.saburi.utils.FXUIUtils.errorMessage;
import static com.saburi.utils.FXUIUtils.message;
import static com.saburi.utils.FXUIUtils.setTableEditable;
import static com.saburi.utils.Utilities.makeDirectory;
import static com.saburi.utils.Utilities.openFile;
import static com.saburi.utils.Utilities.writeFile;
import com.saburi.model.Field;
import com.saburi.model.Project;
import com.saburi.smartcoder.CodeGenerator;
import com.saburi.smartcoder.Controller;
import com.saburi.smartcoder.DBAcess;
import com.saburi.smartcoder.Entity;
import com.saburi.smartcoder.GenMenu;
import com.saburi.smartcoder.GenViewController;
import com.saburi.smartcoder.SQLFile;
import com.saburi.smartcoder.UIEdit;
import com.saburi.smartcoder.UIView;
import com.saburi.utils.EditCell;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.MenuTypes;
import com.saburi.utils.Enums.RelationMappping;
import com.saburi.utils.Enums.Saburikeys;
import com.saburi.utils.Enums.keys;
import static com.saburi.utils.FXUIUtils.loadProjects;
import static com.saburi.utils.FXUIUtils.warningOK;
import com.saburi.utils.Utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 *
 * @author ClinicMaster13
 */
public class SmartCoderController implements Initializable {

    private enum FXControls {
        Label, TextField, ComboBox, DatePicker, CheckBox
    }

    private final ObservableList dataTypes = FXCollections.observableArrayList(
            "String", "List", "Set", "Image", "LocalDate", "LocalDateTime", "LocalTime", "boolean", "int", "float", "double"
    );

//    private final ObservableList enumClasses = FXCollections.observableArrayList("CommonEnums", "Enums");
    private final ObservableList mapppings = FXCollections.observableArrayList(RelationMappping.OneToOne.name(),
            RelationMappping.OneToMany.name(), RelationMappping.ManyToOne.name(), RelationMappping.ManyToMany.name());

    private final ObservableList keyses = FXCollections.observableArrayList(keys.Primary.name(), Enums.keys.Foreign.name(), Enums.keys.Unique.name(), Enums.keys.None.name());

    private final ObservableList saburiKeys = FXCollections.observableArrayList(Saburikeys.ID_Helper.name(),
            Saburikeys.ID_Generator.name(), Saburikeys.Display.name(), Saburikeys.None.name());

    private final ObservableList togenerateFiles = FXCollections.observableArrayList(
            "", "Entity", "DBAcess", "Controller", "View Controller", "FXML", "FXML View", "Menu", "SQL"
    );

    private final ObservableList menuTypes = FXCollections.observableArrayList(Enums.MenuTypes.values());

    @FXML
    TableView<FieldDAO> tblSaburiTools;
    @FXML
    private TableColumn<FieldDAO, String> tbcFieldName, tbcCaption, tbcDataType, tbcReferences, tbcKey,
            tbcSaburiKey, tbcMapping, tbcSubFields, tbcProjectID;
    @FXML
    private TableColumn<FieldDAO, Integer> tbcSize;

    @FXML
    private TableColumn<FieldDAO, Boolean> tbcNullable;

    @FXML
    private TableColumn<FieldDAO, Boolean> tbcEnumerated;

    @FXML
    private TextField txtFileName, txtObjectName, txtObjectCaption, txtOutputDirectory, txtParentMenuID;
    @FXML

    private Button btnBrowse, btnOutputDirectory;
    @FXML
    private Button btnGenerate;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnImport, btnSave;
    @FXML
    private ComboBox cboFiles;
    @FXML
    private ComboBox<Project> cboProject;
    @FXML
    private ComboBox<MenuTypes> cboMenuType;

    @FXML
    private CheckBox chkOpenFile, chkSaveToProject, chkGenerateMenus, chkGenerateViewUI;
    private final ProjectDAO oProjectDAO = new ProjectDAO();
    private final FieldDAO oFieldDAO = new FieldDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            btnBrowse.setOnAction(e -> {
                String fileName = (browse(txtFileName));
//                String tokens = fileName.substring(0, fileName.length() - 4);
                String objectName = fileName.substring(0, fileName.length() - 4);
                txtObjectName.setText(objectName);
                txtObjectCaption.setText(objectName);
                tblSaburiTools.getItems().clear();
            });
            btnImport.setOnAction(e -> {
                this.loadTable();
                String path = txtFileName.getText();
                File file = new File(path);
                String fileName = file.getName();
                String objectName = fileName.substring(0, fileName.length() - 4);
                txtObjectName.setText(objectName);
                txtObjectCaption.setText(objectName);
                btnSave.visibleProperty().bind(Bindings.size(tblSaburiTools.getItems()).greaterThan(0));

            });

            btnSave.setOnAction(e -> {

                try {
                    String path = txtFileName.getText();

                    String contents = "";
                    int index = 0;
                    int size = tblSaburiTools.getItems().size();
                    List<FieldDAO> fieldDAOs = tblSaburiTools.getItems();
                    fieldDAOs.removeIf(p -> p.getFieldName().isBlank());
                    for (FieldDAO field : fieldDAOs) {
                        index++;
                        contents += field.toString();
                        if (index < size) {
                            contents += "\n";
                        }
                    }
                    writeFile(path, contents);
                    message("Saved Successfully");
                } catch (Exception ex) {
                    errorMessage(ex);
                }

            });

            btnOutputDirectory.setOnAction(e -> FXUIUtils.browseDirectory(txtOutputDirectory));
            btnGenerate.setOnAction(e -> this.generateCode());
            btnClose.setOnAction(e -> this.close());

            this.tblSaburiTools.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            cboFiles.setItems(togenerateFiles);
            cboMenuType.setItems(menuTypes);
            cboMenuType.setValue(MenuTypes.SplitButton);
            txtParentMenuID.editableProperty().bindBidirectional(chkGenerateMenus.selectedProperty());
            loadProjects(oProjectDAO.read(), cboProject);
            initTable();
        } catch (Exception e) {
            errorMessage(e);
        }

    }

    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void initTable() {
        try {
            setTableEditable(tblSaburiTools);

            final Callback<TableColumn<FieldDAO, Boolean>, TableCell<FieldDAO, Boolean>> cellFactory = CheckBoxTableCell.forTableColumn(tbcNullable);
            tbcNullable.setCellFactory((TableColumn<FieldDAO, Boolean> param) -> {
                TableCell<FieldDAO, Boolean> cell = cellFactory.call(param);
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            tbcNullable.setCellFactory(cellFactory);

            final Callback<TableColumn<FieldDAO, Boolean>, TableCell<FieldDAO, Boolean>> enumeratedCellFactory = CheckBoxTableCell.forTableColumn(tbcEnumerated);
            tbcEnumerated.setCellFactory((TableColumn<FieldDAO, Boolean> param) -> {
                TableCell<FieldDAO, Boolean> cell = enumeratedCellFactory.call(param);
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            tbcEnumerated.setCellFactory(enumeratedCellFactory);

            tbcNullable.setCellValueFactory((TableColumn.CellDataFeatures<FieldDAO, Boolean> param) -> {
                FieldDAO field = param.getValue();
                SimpleBooleanProperty p = field.getNullableProperty();
                p.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    field.setNullable(newValue);
                });
                return p;
            });

            tbcEnumerated.setCellValueFactory((TableColumn.CellDataFeatures<FieldDAO, Boolean> param) -> {
                FieldDAO field = param.getValue();

                SimpleBooleanProperty p = field.getEnumeratedProperty();
                p.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    field.setEnumerated(newValue);
                });
                return p;
            });
            editFieldName();
            editCaption();
            editReferences();
            editSubFields();
            editDataType();
            editMapping();
            editKey();
            editSaburiKey();
            editProjectID();
            editSize();
            tbcDataType.setCellFactory(ComboBoxTableCell.forTableColumn(dataTypes));
            tbcKey.setCellFactory(ComboBoxTableCell.forTableColumn(keyses));
            tbcSaburiKey.setCellFactory(ComboBoxTableCell.forTableColumn(saburiKeys));
            tbcMapping.setCellFactory(ComboBoxTableCell.forTableColumn(mapppings));

            ObservableList projectIds = FXCollections.observableList(oProjectDAO.read()
                    .stream().map(Project::getProjectID).collect(Collectors.toList()));
            tbcProjectID.setCellFactory(ComboBoxTableCell.forTableColumn(projectIds));
            addRow(tblSaburiTools, 0);

        } catch (Exception e) {
            errorMessage(e);
        }

    }

    private void loadTable() {
        try {

            tblSaburiTools.setItems(FXCollections.observableList(FieldDAO.getFieldDAOs(this.readFile(txtFileName.getText(), ","))));
            addRow(tblSaburiTools, tblSaburiTools.getItems().size() - 1);
        } catch (Exception e) {
            errorMessage(e);
        }
    }

    private ObservableList<Field> readFile(String fileName, String seperator) {

        ObservableList<Field> fields = FXCollections.observableArrayList();
        if (!(fileName.isEmpty())) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String line;

                while ((line = reader.readLine()) != null) {
                    Field field = Utilities.getFields(line, seperator);
                    if (field != null) {
                        fields.add(field);
                    }
                }

            } catch (IOException e) {
                errorMessage(e);
            } catch (Exception e) {
                errorMessage(e);
            }
        }

        return fields;

    }

    private void editFieldName() {

        tbcFieldName.setCellFactory(EditCell.forTableColumn());

        tbcFieldName.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setFieldName(value);
            tblSaburiTools.refresh();
            addRow(tblSaburiTools, event.getTablePosition().getRow());

        });
    }

    private void editCaption() {

        tbcCaption.setCellFactory(EditCell.forTableColumn());

        tbcCaption.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setCaption(value);
            tblSaburiTools.refresh();
        });
    }

    private void editReferences() {

        tbcReferences.setCellFactory(EditCell.forTableColumn());

        tbcReferences.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setReferences(value);
            tblSaburiTools.refresh();
        });
    }

    private void editSubFields() {
        tbcSubFields.setCellFactory(EditCell.forTableColumn());
        tbcSubFields.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setSubFields(value);
            tblSaburiTools.refresh();
        });
    }

    private void editDataType() {

        tbcDataType.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setDataType(value);
            tblSaburiTools.refresh();
        });
    }

    private void editMapping() {

        tbcMapping.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setMapping(value);
            tblSaburiTools.refresh();
        });
    }

    private void editKey() {

        tbcKey.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setKey(value);
            tblSaburiTools.refresh();
        });
    }

    private void editSaburiKey() {

        tbcSaburiKey.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setSaburiKey(value);
            tblSaburiTools.refresh();
        });
    }

    private void editProjectID() {

        tbcProjectID.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setProjectID(value);
            tblSaburiTools.refresh();
        });
    }

    private void editSize() {
        try {
            tbcSize.setCellFactory(EditCell.forIntTableColumn());
            tbcSize.setOnEditCommit(event -> {
                final int value = event.getNewValue() != null ? event.getNewValue()
                        : event.getOldValue();
                ((FieldDAO) event.getTableView().getItems()
                        .get(event.getTablePosition().getRow()))
                        .setSize(value);
                tblSaburiTools.refresh();
            });
        } catch (Exception e) {
            errorMessage(e);
        }
    }

//*************************************************************************************
//*************************************************************************************
//Generate code
    private void generateCode() {
        try {
            String objectName = txtObjectName.getText();
            String objectCaption = txtObjectCaption.getText();
            String outPutDirecory = txtOutputDirectory.getText();
            Project project = cboProject.getValue();
            if (project == null) {
                message("Please select the project!");
                return;
            }
            if (objectName.isBlank()) {
                message("Must enter Object Name!");
                return;
            }

            if (outPutDirecory.isBlank()) {
                message("Must enter Out Put Direcory!");
                return;
            }

            List<FieldDAO> fieldDAOs = tblSaburiTools.getItems();
            fieldDAOs.removeIf(p -> p.getFieldName().isBlank());

            CodeGenerator codeGenerator = new CodeGenerator();
            codeGenerator.validate(fieldDAOs);

            String entityFolder = outPutDirecory + "\\Entities";
            String controllerFolder = outPutDirecory + "\\Controllers";
            String fxmlFolder = outPutDirecory + "\\UIs";

            String dbAccessFolder = outPutDirecory + "\\DBAccess";
            String menusFolder = outPutDirecory + "\\Menus";
            String sqlFolder = outPutDirecory + "\\SQL";

            makeDirectory(entityFolder);
            makeDirectory(dbAccessFolder);
            makeDirectory(controllerFolder);
            makeDirectory(fxmlFolder);
            makeDirectory(dbAccessFolder);
            makeDirectory(menusFolder);
            makeDirectory(sqlFolder);

            String daObjectName = objectName + "DA";
            String entityFileName = entityFolder + "\\" + objectName + ".java";
            String daFileName = dbAccessFolder + "\\" + daObjectName + ".java";
            String fxmlFileName = fxmlFolder + "\\" + objectName + ".fxml";
            String fxmlTBFileName = fxmlFolder + "\\" + objectName + "View.fxml";
            String controllerFileName = controllerFolder + "\\" + objectName + "Controller.java";
            String vwcontrollerFileName = controllerFolder + "\\" + objectName + "ViewController.java";
            String menuFileName = menusFolder + "\\" + objectName + "Menu.txt";
            String sqlFileName = sqlFolder + "\\" + objectName + "SQLFile.txt";

            String entityFileContents = new Entity(objectName, fieldDAOs).makeClass(project);
            String daFileContents = new DBAcess(objectName, fieldDAOs).makeClass(project);
            String controllerFileContent = new Controller(objectName, fieldDAOs).makeClass(project);
            String vwcontrollerFileContent = new GenViewController(objectName).makeClass();
            String fxmlFileContent = new UIEdit(objectName, fieldDAOs).create(project);
            String fxmlTBFileContent = new UIView(objectName, fieldDAOs).create(project);
            String menuFIleContents = GenMenu.makeMenu(objectName, objectCaption, cboMenuType.getValue(), txtParentMenuID.getText());
            String sqlFileContents = SQLFile.callEditAccessObject(objectName, objectCaption);

            if (Utilities.hasHelper(fieldDAOs)) {
                sqlFileContents += SQLFile.callEditIDGenerator(objectName);
            }

            List<Pair<String, String>> lstPairs = new ArrayList<>();

            Pair<String, String> fxlPair = new Pair(fxmlFileName, fxmlFileContent);
            Pair<String, String> fxlTBPair = new Pair(fxmlTBFileName, fxmlTBFileContent);
            Pair<String, String> controllerPair = new Pair(controllerFileName, controllerFileContent);
            Pair<String, String> vwcontrollerPair = new Pair(vwcontrollerFileName, vwcontrollerFileContent);
            Pair<String, String> daFilePair = new Pair(daFileName, daFileContents);
            Pair<String, String> entityPair = new Pair(entityFileName, entityFileContents);
            Pair<String, String> menuPair = new Pair(menuFileName, menuFIleContents);
            Pair<String, String> sqlPair = new Pair(sqlFileName, sqlFileContents);

            if (chkGenerateMenus.isSelected()) {
                menuFIleContents += "\n\n\n\n\n";
                menuFIleContents += "\n\n\n\n\n";
                lstPairs.add(new Pair(menuFileName, menuFIleContents));

            }

            if (chkGenerateViewUI.isSelected()) {
                lstPairs.add(fxlTBPair);
                lstPairs.add(vwcontrollerPair);

            }

//          Writing the created files
            Pair<String, String> pairs[];

            String toGenerateFile = "";
            if (cboFiles.getSelectionModel().getSelectedIndex() > -1) {
                toGenerateFile = cboFiles.getValue().toString();
            }

            switch (toGenerateFile) {
                case "Entity":
                    pairs = new Pair[]{entityPair};
                    break;
                case "DBAcess":
                    pairs = new Pair[]{daFilePair};
                    break;
                case "Controller":
                    pairs = new Pair[]{controllerPair};
                    break;

                case "View Controller":
                    pairs = new Pair[]{vwcontrollerPair};
                    break;
                case "FXML":
                    pairs = new Pair[]{fxlPair};
                    break;
                case "FXML View":
                    pairs = new Pair[]{fxlTBPair};
                    break;
                case "Menu":
                    pairs = new Pair[]{menuPair};
                    break;
                case "SQL":
                    pairs = new Pair[]{sqlPair};
                    break;
                default:
                    pairs = new Pair[]{entityPair, daFilePair, controllerPair, fxlPair, menuPair, sqlPair};

                    break;

            }
            lstPairs.addAll(Arrays.asList(pairs));
            List<Pair<String, String>> projectFiles = new ArrayList<>();
            if (chkSaveToProject.isSelected()) {

                String entityFileNameProj = project.getEntityFolder() + "\\" + objectName + ".java";
                String daFileNameProj = project.getDBAcessFolder() + "\\" + daObjectName + ".java";
                String fxmlFileNameProj = project.getResourceFolder() + "\\" + objectName + ".fxml";
                String controllerFileNameProj = project.getControllerFolder() + "\\" + objectName + "Controller.java";

                Pair<String, String> fxlPairProj = new Pair(fxmlFileNameProj, fxmlFileContent);
                Pair<String, String> controllerPairProj = new Pair(controllerFileNameProj, controllerFileContent);
                Pair<String, String> daFilePairProj = new Pair(daFileNameProj, daFileContents);
                Pair<String, String> entityPairProj = new Pair(entityFileNameProj, entityFileContents);
                switch (toGenerateFile) {
                    case "Entity":
                        pairs = new Pair[]{entityPairProj};
                        break;
                    case "DBAcess":
                        pairs = new Pair[]{daFilePairProj};
                        break;
                    case "Controller":
                        pairs = new Pair[]{controllerPairProj};
                        break;

                    case "FXML":
                        pairs = new Pair[]{fxlPairProj};
                        break;

                    default:
                        pairs = new Pair[]{entityPairProj, daFilePairProj, controllerPairProj, fxlPairProj};

                        break;

                }
                projectFiles.addAll(Arrays.asList(pairs));
                projectFiles.forEach(a -> {
                    String fileName = a.getKey();
                    File file = new File(fileName);
                    if (file.exists()) {
                        if (warningOK("File Exists", "The file with name " + file.getName() + " already exists.\n"
                                + "Do you want to replace it?")) {
                            if (warningOK("Confirm Replace",
                                    "Replacing this file may lead to potential loss of the code you previously wrote\n"
                                    + "Are you sure you want to continue?")) {
                                writeFile(fileName, a.getValue());
                            }
                        }
                    } else {
                        writeFile(fileName, a.getValue());
                    }
                });
            }

            lstPairs.forEach(a -> writeFile(a.getKey(), a.getValue()));

            if (!chkOpenFile.isSelected()) {
                message("Operation Successful");
                return;
            }

            lstPairs.forEach(a -> openFile(a.getKey()));

        } catch (Exception e) {
            errorMessage(e);
        }

    }

    private void addRow(TableView tableView, int rowIndex) {

        List<FieldDAO> fieldDAOs = tableView.getItems();
        int size = fieldDAOs.size();

        if (size == 0) {
            tableView.getItems().add(new FieldDAO());
        } else {
            int lastIndex = size - 1;
            if (rowIndex == lastIndex) {
                FieldDAO lastFieldDAO = fieldDAOs.get(lastIndex);
                if (!lastFieldDAO.getFieldName().isBlank()) {
                    tableView.getItems().add(new FieldDAO());
                }
            }
        }
    }
}
