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
import static com.saburi.utils.Utilities.writeFile;
import com.saburi.model.Field;
import com.saburi.model.Project;
import com.saburi.smartcoder.CodeGenerator;
import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.ProjectFile;
import com.saburi.smartcoder.vue3.Vue3;
import com.saburi.utils.EditCell;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.DesktopFiles;
import com.saburi.utils.Enums.EntityTypes;
import com.saburi.utils.Enums.MenuTypes;
import com.saburi.utils.Enums.RelationMappping;
import com.saburi.utils.Enums.Saburikeys;
import com.saburi.utils.Enums.keys;
import com.saburi.utils.Enums.ProjectTypes;
import static com.saburi.utils.Enums.ProjectTypes.Vue;
import com.saburi.utils.Enums.ServiceTypes;
import com.saburi.utils.Enums.Vue3Files;
import com.saburi.utils.Enums.VueFiles;
import com.saburi.utils.Enums.WebFiles;
import static com.saburi.utils.FXUIUtils.loadProjects;
import static com.saburi.utils.FXUIUtils.warningOK;
import com.saburi.utils.Utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author ClinicMaster13
 */
public class SmartCoderController implements Initializable {

    private enum FXControls {
        Label, TextField, ComboBox, DatePicker, CheckBox
    }

    private final ObservableList dataTypes = FXCollections.observableArrayList(
            "String", "List", "Set", "Image", "LocalDate", "LocalDateTime", "LocalTime", "boolean", "int", "long", "float", "double","BigDecimal", "File"
    );

    private final ObservableList mapppings = FXCollections.observableArrayList(RelationMappping.OneToOne.name(),
            RelationMappping.OneToMany.name(), RelationMappping.ManyToOne.name(), RelationMappping.ManyToMany.name());

//    private final ObservableList keyses = FXCollections.observableArrayList(keys.values());
    private final ObservableList menuTypes = FXCollections.observableArrayList(Enums.MenuTypes.values());

    @FXML
    TableView<FieldDAO> tblSaburiTools;
    @FXML
    private TableColumn<FieldDAO, String> tbcFieldName, tbcCaption, tbcDataType, tbcReferences,
            tbcMapping, tbcSubFields, tbcProjectName, tbcModuleName;
    @FXML
    private TableColumn<FieldDAO, Saburikeys> tbcSaburiKey;
    @FXML
    private TableColumn<FieldDAO, keys> tbcKey;
    @FXML
    private TableColumn<FieldDAO, Integer> tbcSize;

    @FXML
    private TableColumn<FieldDAO, Boolean> tbcNullable;

    @FXML
    private TableColumn<FieldDAO, Boolean> tbcEnumerated, tbcExpose, tbcSelect;

    @FXML
    private TextField txtFileName, txtObjectName, txtObjectCaption, txtOutputDirectory, txtParentMenuID, txtModule;
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
    private ComboBox<ProjectTypes> cboProjectType;
    @FXML
    private ComboBox<Project> cboProject;
    @FXML
    private ComboBox<MenuTypes> cboMenuType;
    @FXML
    private Label lblRowCount;
    @FXML
    private ComboBox<EntityTypes> cboEntityType;
    @FXML
    private ComboBox<ServiceTypes> cboServiceType;

    @FXML
    private CheckBox chkOpenFile, chkSaveToProject, chkGenerateMenus, chkGenerateViewUI;
    private final ProjectDAO oProjectDAO = new ProjectDAO();
    private List<Project> projects = new ArrayList<>();
    private List<String> projectNames;
    private Vue3 vue3= new Vue3();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            btnBrowse.setOnAction(e -> {
                String fileName = (browse(txtFileName));
                if (Utilities.isNullOrEmpty(fileName)) {
                    return;
                }
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
                txtObjectCaption.setText(Utilities.getCaption(objectName));
                btnSave.visibleProperty().bind(Bindings.size(tblSaburiTools.getItems()).greaterThan(0));

            });

            btnSave.setOnAction(e -> {

                try {
                    String path = txtFileName.getText();

                    String contents = "";
                    int index = 0;
                    int size = tblSaburiTools.getItems().size();
                    List<FieldDAO> fieldDAOs = tblSaburiTools.getItems().filtered(p -> !p.getFieldName().isBlank());

                    for (FieldDAO field : fieldDAOs) {
                        index++;
                        contents += field.toString();
                        if (index < size) {
                            contents += "\n";
                        }
                    }
                    File f = new File(path);
                    if (f.exists()) {
                        if (!warningOK("Overwrite", "You are about to overwrite a file: " + f.getName() + " are you sure you want to continue")) {
                            return;
                        }
                    }
                    writeFile(path, contents);
                    message("Saved Successfully");
                } catch (Exception ex) {
                    errorMessage(ex);
                }

            });

            String dir = System.getProperty("user.home").concat("/".concat("Desktop")).concat("/").concat("SmartCoder");
            txtOutputDirectory.setText(dir);

            btnOutputDirectory.setOnAction(e -> FXUIUtils.browseDirectory(txtOutputDirectory));
            btnGenerate.setOnAction(e -> this.generateCode());
            btnClose.setOnAction(e -> this.close());

            this.tblSaburiTools.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//            cboFiles.setItems(togenerateFiles);
            cboMenuType.setItems(menuTypes);
            cboMenuType.setValue(MenuTypes.SplitButton);

            cboProjectType.setItems(FXCollections.observableArrayList(ProjectTypes.values()));

            txtParentMenuID.editableProperty().bindBidirectional(chkGenerateMenus.selectedProperty());

            cboProjectType.setOnAction(e -> {
                ProjectTypes projectType = cboProjectType.getValue();
                switch (projectType) {
                    case Springboot_API ->
                        cboFiles.setItems(FXCollections.observableArrayList(WebFiles.values()));

                    case Vue ->
                        cboFiles.setItems(FXCollections.observableArrayList(VueFiles.values()));
                        case Vue3 ->
                        cboFiles.setItems(FXCollections.observableArrayList(Vue3Files.values()));


                    case Desktop -> {
                        cboFiles.setItems(FXCollections.observableArrayList(DesktopFiles.values()));
                        cboEntityType.setValue(EntityTypes.DB_Entity);
                    }

                    default ->
                        cboFiles.getItems().clear();
                }
                
                ProjectTypes ptype = cboProjectType.getValue();

                showProjectTypeControls(ptype.equals(ProjectTypes.Springboot_API));
                txtModule.setVisible(ptype.equals(ProjectTypes.Vue)||ptype.equals(ProjectTypes.Vue3));
                hideTableColumns(ptype.equals(ProjectTypes.Vue)||ptype.equals(ProjectTypes.Vue3));

                try {
                    projects = oProjectDAO.read().stream()
                            .filter((p) -> p.getProjectType().equals(projectType))
                            .collect(Collectors.toList());

                    loadProjects(projects, cboProject);
                    projectNames = projects.stream().map(Project::getDisplay).collect(Collectors.toList());
                    projectNames.add(0, "");
                    tbcProjectName.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableList(projectNames)));

                } catch (Exception ex) {
                    errorMessage(ex);
                }

            });
            cboEntityType.setItems(FXCollections.observableArrayList(Enums.EntityTypes.values()));
            cboServiceType.setItems(FXCollections.observableArrayList(Enums.ServiceTypes.values()));
            cboEntityType.setOnAction(e -> {
                EntityTypes entityType = cboEntityType.getValue();
                if (entityType.equals(EntityTypes.Auto_ID_Gen)) {
                    cboServiceType.setValue(ServiceTypes.ID_Gen);
                    cboServiceType.disableProperty().set(true);
                } else {
                    cboServiceType.setValue(ServiceTypes.Base);
                    cboServiceType.disableProperty().set(false);
                }

            });

            initTable();
        } catch (Exception e) {
            errorMessage(e);
        }

    }

    private void showProjectTypeControls(boolean visible) {
        cboEntityType.visibleProperty().set(visible);
        cboServiceType.visibleProperty().set(visible);

    }

    private void hideTableColumns(boolean visible) {
        tbcModuleName.visibleProperty().set(visible);
        tbcSelect.visibleProperty().set(visible);
        tbcProjectName.visibleProperty().set(!visible);
    }

    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void initTable() {
        try {
            setTableEditable(tblSaburiTools);

            final Callback<TableColumn<FieldDAO, Boolean>, TableCell<FieldDAO, Boolean>> enumeratedCellFactory = CheckBoxTableCell.forTableColumn(tbcEnumerated);
            tbcEnumerated.setCellFactory((TableColumn<FieldDAO, Boolean> param) -> {
                TableCell<FieldDAO, Boolean> cell = enumeratedCellFactory.call(param);
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            tbcEnumerated.setCellFactory(enumeratedCellFactory);

            tbcEnumerated.setCellValueFactory((TableColumn.CellDataFeatures<FieldDAO, Boolean> param) -> {
                FieldDAO field = param.getValue();

                SimpleBooleanProperty p = field.getEnumeratedProperty();
                p.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    field.setEnumerated(newValue);
                });
                return p;
            });

            final Callback<TableColumn<FieldDAO, Boolean>, TableCell<FieldDAO, Boolean>> cellFactory = CheckBoxTableCell.forTableColumn(tbcNullable);
            tbcNullable.setCellFactory((TableColumn<FieldDAO, Boolean> param) -> {
                TableCell<FieldDAO, Boolean> cell = cellFactory.call(param);
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            tbcNullable.setCellFactory(cellFactory);

            tbcNullable.setCellValueFactory((TableColumn.CellDataFeatures<FieldDAO, Boolean> param) -> {
                FieldDAO field = param.getValue();
                SimpleBooleanProperty p = field.getNullableProperty();
                p.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    field.setNullable(newValue);
                });
                return p;
            });

            final Callback<TableColumn<FieldDAO, Boolean>, TableCell<FieldDAO, Boolean>> exposeCellFactory = CheckBoxTableCell.forTableColumn(tbcExpose);
            tbcExpose.setCellFactory((TableColumn<FieldDAO, Boolean> param) -> {
                TableCell<FieldDAO, Boolean> cell = exposeCellFactory.call(param);
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            tbcExpose.setCellFactory(exposeCellFactory);

            tbcExpose.setCellValueFactory((TableColumn.CellDataFeatures<FieldDAO, Boolean> param) -> {
                FieldDAO field = param.getValue();
                SimpleBooleanProperty p = field.getExpose();
                p.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    field.setExpose(newValue);
                });
                return p;
            });

            final Callback<TableColumn<FieldDAO, Boolean>, TableCell<FieldDAO, Boolean>> selectCellFactory = CheckBoxTableCell.forTableColumn(tbcSelect);
            tbcSelect.setCellFactory((TableColumn<FieldDAO, Boolean> param) -> {
                TableCell<FieldDAO, Boolean> cell = selectCellFactory.call(param);
                cell.setAlignment(Pos.CENTER);
                return cell;
            });
            tbcSelect.setCellFactory(selectCellFactory);

            tbcSelect.setCellValueFactory((TableColumn.CellDataFeatures<FieldDAO, Boolean> param) -> {
                FieldDAO field = param.getValue();
                SimpleBooleanProperty p = field.getSelect();
                p.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    field.setSelect(newValue);
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
            editModuleName();
            tbcDataType.setCellFactory(ComboBoxTableCell.forTableColumn(dataTypes));
            tbcKey.setCellFactory(ComboBoxTableCell.forTableColumn(keys.values()));
            tbcSaburiKey.setCellFactory(ComboBoxTableCell.forTableColumn(Saburikeys.values()));
            tbcMapping.setCellFactory(ComboBoxTableCell.forTableColumn(mapppings));

            addRow(tblSaburiTools, 0);

        } catch (Exception e) {
            errorMessage(e);
        }

    }

    private void loadTable() {
        try {

            tblSaburiTools.setItems(FXCollections
                    .observableList(FieldDAO.getFieldDAOs(
                            this.readFile(txtFileName.getText(), ","))));
            lblRowCount.setText("" + tblSaburiTools.getItems().size() + " row(s)");
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
                    .setFieldName(value.trim());
            tblSaburiTools.refresh();
            lblRowCount.setText("" + (tblSaburiTools.getItems().size() - 1) + " row(s)");
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
                    .setCaption(value.trim());
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
                    .setReferences(value.trim());
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
                    .setSubFields(value.trim());
            tblSaburiTools.refresh();
        });
    }

    private void editDataType() {

        tbcDataType.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setDataType(value.trim());
            tblSaburiTools.refresh();
        });
    }

    private void editMapping() {

        tbcMapping.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setMapping(value.trim());
            tblSaburiTools.refresh();
        });
    }

    private void editKey() {

        tbcKey.setOnEditCommit(event -> {
            final keys value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setKey(value.name().trim());
            tblSaburiTools.refresh();
        });
    }

    private void editSaburiKey() {

        tbcSaburiKey.setOnEditCommit(event -> {
            final Saburikeys value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setSaburiKey(value == null ? "" : value.name().trim());
            tblSaburiTools.refresh();
        });
    }

    private void editProjectID() {

        tbcProjectName.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setProjectName(value.trim());
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

    private void editModuleName() {
        tbcModuleName.setCellFactory(EditCell.forTableColumn());
        tbcModuleName.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();
            ((FieldDAO) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setModuleName(value.trim());
            tblSaburiTools.refresh();
        });
    }

//*************************************************************************************
//*************************************************************************************
    private String getFileExtension(String selectedFile) {
        if (selectedFile.equalsIgnoreCase(DesktopFiles.FXML.name()) || selectedFile.equalsIgnoreCase(DesktopFiles.FXML_View.name())) {
            return ".fxml";
        }
        if (selectedFile.equalsIgnoreCase(DesktopFiles.SQL.name()) || selectedFile.equalsIgnoreCase(DesktopFiles.Menu.name())) {
            return ".txt";
        }

        if (selectedFile.equalsIgnoreCase(VueFiles.Model.name()) 
                || selectedFile.equalsIgnoreCase(VueFiles.Store.name())
                || selectedFile.equalsIgnoreCase(VueFiles.Nav.name())
                || selectedFile.equalsIgnoreCase(Vue3Files.Controller.name())) {
            return ".js";
        }
        if (selectedFile.equalsIgnoreCase(VueFiles.View.name())
                || selectedFile.equalsIgnoreCase(VueFiles.Search.name())) {
            return ".vue";
        }

        if (selectedFile.equalsIgnoreCase(WebFiles.Change_Log.name())) {
            return ".xml";
        }
        return ".java";
    }

    private String getFileFullName(String objectName, Project project, String selectedFile) {
        String fileName = (selectedFile.equalsIgnoreCase(DesktopFiles.Entity.name())
                || selectedFile.equalsIgnoreCase(VueFiles.View.name()))
                || selectedFile.equalsIgnoreCase(DesktopFiles.FXML.name()) ? objectName
                : selectedFile.equalsIgnoreCase(VueFiles.Store.name())
                && project.getProjectType().equals(ProjectTypes.Vue)
                ? objectName.toLowerCase()
                
                 : selectedFile.equalsIgnoreCase(Vue3Files.Store.name())
                && project.getProjectType().equals(ProjectTypes.Vue3)
                ? objectName.concat(Vue3Files.Store.name())
                : selectedFile.equalsIgnoreCase(VueFiles.Search.name()) ? Utilities.toPlural(objectName)
                : selectedFile.equalsIgnoreCase(DesktopFiles.DBAcess.name()) ? objectName.concat("DA")
                : selectedFile.equalsIgnoreCase(DesktopFiles.FXML_View.name()) ? objectName.concat("View")
                : selectedFile.equalsIgnoreCase(WebFiles.Change_Log.name()) ? "create_" + Utilities.toPlural(objectName).toLowerCase()
                 : selectedFile.equalsIgnoreCase(Vue3Files.Controller.name()) ? objectName.concat("Controller")
                
                : objectName.concat(selectedFile.replace("-", ""));
//
//        fileName = (selectedFile.equalsIgnoreCase(VueFiles.Store.name())
//                && project.getProjectType().equals(ProjectTypes.Vue)
//                ? fileName.toLowerCase() : fileName);
//        
//        fileName = (selectedFile.equalsIgnoreCase(VueFiles.Store.name())
//                && project.getProjectType().equals(ProjectTypes.Vue3)
//                ? fileName.concat("Store") : fileName);
        String baseFolder = getBaseFolder(selectedFile, objectName.toLowerCase(), project);
        return baseFolder + "//" + fileName.concat(this.getFileExtension(selectedFile));
    }

    private String getBaseFolder(String selectedFile, String objectName, Project project) {
        String outputFolder;
        String outPutDirector = txtOutputDirectory.getText();
        boolean saveProject = chkSaveToProject.isSelected();
        switch (project.getProjectType()) {
            case Desktop -> {
                outputFolder = saveProject ? getDesktopBaseFolderName(project, selectedFile, outPutDirector) : outPutDirector;
            }
            case Vue -> {
                String module = txtModule.getText();
                String lastSection = Utilities.isNullOrEmpty(module) ? "//".concat(objectName.toLowerCase()) : "//".concat(module).concat("//").concat(objectName);
                outputFolder = saveProject ? project.getBaseFolder().concat(lastSection) : outPutDirector;

//                outputFolder = outPutDir.concat("//" + (selectedFile.equalsIgnoreCase(VueFiles.Store.name()) ? objectName.toLowerCase() : selectedFile.replace("-", "")));
            }
            case Vue3 -> {
                String module = txtModule.getText();
                String lastSection = Utilities.isNullOrEmpty(module) ? "//".concat(objectName.toLowerCase())
                        : "//".concat(module).concat("//").concat(objectName);
                outputFolder = saveProject ? project.getBaseFolder().concat(lastSection) : outPutDirector;

//                outputFolder = outPutDir.concat("//" + (selectedFile.equalsIgnoreCase(VueFiles.Store.name()) ? objectName.toLowerCase() : selectedFile.replace("-", "")));
            }
            default -> {
                String outPutDir = saveProject ? project.getBaseFolder() : outPutDirector;
                outputFolder = outPutDir.concat("//" + objectName.toLowerCase());
                if (selectedFile.equalsIgnoreCase(WebFiles.Request.name())
                        || selectedFile.equalsIgnoreCase(WebFiles.Mini.name()) //                        || selectedFile.equalsIgnoreCase(WebFiles.Response.name())
                        ) {
                    outputFolder = outputFolder.concat("//").concat("dtos");
                } else if (selectedFile.equalsIgnoreCase(WebFiles.Change_Log.name())) {
                    outputFolder = project.getResourceFolder().concat("//changelogs//")
                            .concat(objectName);
                } else if (selectedFile.endsWith("Test")) {
                    outputFolder = project.getTestFolder().concat("//").concat(objectName);
                }
            }
        }

        makeDirectory(outputFolder);
        return outputFolder;
    }

    private String getDesktopBaseFolderName(Project project, String selectedFile, String outPutDir) {
        if (selectedFile.equalsIgnoreCase(DesktopFiles.Entity.name())) {
            return project.getEntityFolder();
        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.DBAcess.name())) {
            return project.getDBAcessFolder();
        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.Controller.name())
                || selectedFile.equalsIgnoreCase(DesktopFiles.ViewController.name())) {
            return project.getControllerFolder();
        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.FXML.name())
                || selectedFile.equalsIgnoreCase(DesktopFiles.FXML_View.name())) {
            return project.getResourceFolder();
        } else {
            return outPutDir;
        }

    }

//    private String getFileContents(String selectedFile, String objectName, String objectCaption, String moduleName,
//            Project project, List<FieldDAO> fieldDAOs, EntityTypes entiityType, ServiceTypes serviceTypes) throws Exception {
//
//        if(project.getProjectType().equals(ProjectTypes.Vue3)) return vue3.getFileContents(selectedFile, objectName, objectCaption, moduleName, project, fieldDAOs);
//        if (selectedFile.equalsIgnoreCase(DesktopFiles.Entity.name())) {
//            return new Entity(objectName, project, fieldDAOs).makeClass(project, entiityType);
//        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.DBAcess.name())) {
//            return new DBAcess(objectName, fieldDAOs).makeClass(project);
//        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.Controller.name()) && project.getProjectType().equals(ProjectTypes.Desktop)) {
//            return new Controller(objectName, fieldDAOs).makeClass(project);
//        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.FXML.name())) {
//            return new UIEdit(objectName, fieldDAOs).create(project);
//        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.FXML_View.name())) {
//            return new UIView(objectName, fieldDAOs).create(project);
//        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.ViewController.name())) {
//            return new GenViewController(objectName).makeClass();
//        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.Menu.name())) {
//            return GenMenu.makeMenu(objectName, objectCaption, cboMenuType.getValue(), txtParentMenuID.getText());
//        } else if (selectedFile.equalsIgnoreCase(DesktopFiles.SQL.name())) {
//            return SQLFile.callEditAccessObject(objectName, objectCaption);
//        } else if (selectedFile.equalsIgnoreCase(WebFiles.Request.name())) {
//            return new Request(objectName, project, fieldDAOs).makeClass(project);
//        } else if (selectedFile.equalsIgnoreCase(WebFiles.Repo.name())) {
//            return new Repository(objectName, project, fieldDAOs).makeClass(project, entiityType);
//        } else if (selectedFile.equalsIgnoreCase(WebFiles.Service.name())) {
//            return new Service(objectName, objectCaption, project, fieldDAOs, entiityType).makeClass(project, serviceTypes);
//        } else if (selectedFile.equalsIgnoreCase(WebFiles.Controller.name()) && project.getProjectType().equals(ProjectTypes.Springboot_API)) {
//            return new WebController(objectName, project, fieldDAOs, serviceTypes).makeClass(project, entiityType);
//        } else if (selectedFile.equalsIgnoreCase(WebFiles.ServiceTest.name())) {
//            return new ServiceTest(objectName, objectCaption, project, fieldDAOs, entiityType).makeClass(project, serviceTypes);
//        } else if (selectedFile.equalsIgnoreCase(WebFiles.ControllerTest.name())) {
//            return new ControllerTest(objectName, objectCaption, project, fieldDAOs, entiityType).makeClass(project, serviceTypes);
//        } else if (selectedFile.equalsIgnoreCase(WebFiles.Mini.name())) {
//            return new Mini(objectName, project, fieldDAOs, entiityType).makeClass();
//        } else if (selectedFile.equalsIgnoreCase(WebFiles.Change_Log.name())) {
//            return new ChangeLog(objectName, project, fieldDAOs, entiityType).makeDocument();
//        } //          else if (selectedFile.equalsIgnoreCase(WebFiles.Response.name())) {
//        //            return new Response(objectName, project, fieldDAOs).create();}
//        else if (selectedFile.equalsIgnoreCase(VueFiles.Model.name())) {
//            return new VueModel(objectName, project, fieldDAOs).create();
//        } else if (selectedFile.equalsIgnoreCase(VueFiles.View.name())) {
//            return new Vue(objectName, moduleName, objectCaption, fieldDAOs).create();
//        } else if (selectedFile.equalsIgnoreCase(VueFiles.Store.name())) {
//            return new Store(objectName).create();
//        } else if (selectedFile.equalsIgnoreCase(VueFiles.Nav.name())) {
//            return new VueNav(objectName, objectCaption, moduleName, fieldDAOs).create();
//        } else if (selectedFile.equalsIgnoreCase(VueFiles.Search.name())) {
//            return new Search(objectName).create();
//        }
//
//        throw new Exception("Un recorginsed file type: " + selectedFile);
//    }
//    
//  

    private Predicate filterFileName(String selectedFile) {
        return (p) -> selectedFile.equalsIgnoreCase(DesktopFiles.All.name()) ? !p.toString().equalsIgnoreCase(selectedFile) : p.toString().equalsIgnoreCase(selectedFile);
    }

    private Predicate filterViewUI(boolean generateView, String selectedFile) {
        boolean isViewFile = DesktopFiles.FXML_View.name().equalsIgnoreCase(selectedFile) || DesktopFiles.ViewController.name().equalsIgnoreCase(selectedFile);
        return (p) -> isViewFile ? true : this.generateFXMLView(generateView, p.toString());
    }

    private boolean generateFXMLView(boolean selected, String fname) {
        return (DesktopFiles.FXML_View.name().equalsIgnoreCase(fname)
                || DesktopFiles.ViewController.name().equalsIgnoreCase(fname)) ? selected : true;
    }

//Generate code
    private void generateCode() {
        try {
            String objectName = txtObjectName.getText();
            String objectCaption = txtObjectCaption.getText();
            String outPutDirectory = txtOutputDirectory.getText();
            Project project = cboProject.getValue();
            Object selectedFileo = cboFiles.getValue();
            ProjectTypes projectType = cboProjectType.getValue();
            EntityTypes entityTypes = cboEntityType.getValue();
            ServiceTypes serviceTypes = cboServiceType.getValue();
            String moduleName = txtModule.getText();
            if (project == null) {
                message("Please select the project!");
                return;
            }
            if (objectName.isBlank()) {
                message("Must enter Object Name!");
                return;
            }

            if (outPutDirectory.isBlank()) {
                message("Must enter Out Put Direcory!");
                return;
            }

            if (projectType == null) {
                message("Please select the Project Type!");
                return;
            }

            if (project.getProjectType().equals(ProjectTypes.Springboot_API)) {
                if (entityTypes == null) {
                    message("Please select the Entity Type!");
                    return;
                }
                if (serviceTypes == null) {
                    message("Please select the Service Type!");
                    return;
                }
            }

            if (project.getProjectType().equals(ProjectTypes.Vue)) {
                if (Utilities.isNullOrEmpty(moduleName)) {
                    message("Please enter module name!");
                    return;
                }

            }

            String selectedFile = selectedFileo == null ? "ALL" : selectedFileo.toString();

            List<FieldDAO> fieldDAOs = tblSaburiTools.getItems().stream().filter(p -> !p.getFieldName().isBlank()).toList();

//            CodeGenerator codeGenerator = new CodeGenerator();
            CodeGenerator.validate(fieldDAOs, project);
            List items = cboFiles.getItems().stream()
                    .filter(filterFileName(selectedFile))
                    .filter(this.filterViewUI(chkGenerateViewUI.isSelected(), selectedFile))
                    .toList();
            
            FileModel fileModel = new FileModel(objectName, fieldDAOs, project, projectType, moduleName, objectCaption, 
                    outPutDirectory, chkOpenFile.isSelected(), chkSaveToProject.isSelected(), 
                    entityTypes, serviceTypes);
              Map<String, ProjectFile> map  = ProjectTypeRegistry.register(fileModel);
//             Map<String, ProjectFile> map=  projectFilesMap.get(projectType);
             if(map==null) throw new Exception("Project Type not registered in the registry");
            for (Object item : items) {
                
              ProjectFile pj = map.get(item.toString());
              if(pj!=null) pj.generate();


//                String fileName = getFileFullName(objectName, project, item.toString());
//                String fileContentent = getFileContents(item.toString(), objectName, objectCaption, moduleName, project, fieldDAOs, entityTypes, serviceTypes);
//                File file = new File(fileName);
//                if (file.exists()) {
//                    if (!warningOK("File Exists", "The file with name " + file.getName() + " already exists.\n"
//                            + "Do you want to replace it?")) {
//                        message("Operation Cancelled!");
//                        return;
//                    } else {
//
//                        if (!warningOK("Confirm Replace", """
//                                                             Replacing this file may lead to potential loss of the code
//                                                             Are you sure you want to continue?""")) {
//                            message("Operation Cancelled!");
//                            return;
//                        }
//
//                    }
//                }
//                writeFile(fileName, fileContentent);
//                if (chkOpenFile.isSelected()) {
//                    Desktop.getDesktop().open(file);
//
//                }

            }

            if (!chkOpenFile.isSelected()) {
                message("Operation Successful");
            }

        } catch (IOException ex) {
            errorMessage(ex);
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
