/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.javafx;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.FieldPredicates;
import com.saburi.smartcoder.JavaClass;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.addIfNotExists;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author Sam Buriima
 */
public class Controller {

    private final String objectName;
    private final List<FieldDAO> fields;
    private final String objectNameDA;
    private final String objectNameController;
    private final String primaryKeyVariableName;
    private final String daGlobalVariable;
    private final String daVariableName;
    private final FieldDAO primaryKey;
    FilteredList<FieldDAO> subListFields;
    private final ProjectDAO oProjectDAO = new ProjectDAO();

    public Controller(String objectName, List<FieldDAO> fields) {
        this.objectName = objectName;
        this.fields = fields;
        this.objectNameDA = objectName.concat("DA");
        this.objectNameController = objectName.concat("Controller");
        this.primaryKeyVariableName = Utilities.getPrimaryKey(fields).getVariableName();
        this.daGlobalVariable = "o".concat(objectNameDA);
        this.daVariableName = Utilities.getVariableName(objectNameDA);
        this.primaryKey = Utilities.getPrimaryKey(fields);
        subListFields = new FilteredList<>(FXCollections.observableList(fields), e -> true);
        subListFields.setPredicate(FieldPredicates.hasSubFields());
    }

    private String imports(Project currentProject) throws Exception {
        Project commonProject = oProjectDAO.get(currentProject.getCommonProjectName());
        String imp = "import java.net.URL;\n"
                + "import java.util.ResourceBundle;\n"
                + "import javafx.fxml.FXML;\n"
                + "import static " + commonProject.getUtilPackage() + ".FXUIUtils.*;\n"
                + "import " + commonProject.getUtilPackage() + ".Utilities.FormMode;\n"
                + "import static " + commonProject.getUtilPackage() + ".FXUIUtils.warningOk;\n"
                + "import " + currentProject.getDBAccessPackage() + "." + objectNameDA + ";\n";
        if (!commonProject.getProjectName().equalsIgnoreCase(currentProject.getProjectName())) {
            imp += "import " + oProjectDAO.get(currentProject.getCommonProjectName()).getContollerPackage() + ".EditController;\n";
        }
        for (FieldDAO field : subListFields) {
            List<FieldDAO> subFields = field.getSubFieldListDAO();

            for (FieldDAO subField : subFields) {

                if (subField.getDataType().equalsIgnoreCase("boolean")) {
                    imp += "import javafx.beans.property.SimpleBooleanProperty;\n"
                            + "import javafx.beans.value.ObservableValue;\n"
                            + "import javafx.scene.control.TableCell;\n"
                            + "import javafx.scene.control.cell.CheckBoxTableCell;\n"
                            + "import javafx.util.Callback;\n";
                }
            }
        }

        List<String> imports = new ArrayList();
        this.fields.forEach((t) -> {
            try {
                t.ControllerImports(this.objectName, currentProject).forEach(i -> addIfNotExists(imports, i));
            } catch (Exception ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        imp = imports.stream().map((impo) -> impo + ";\n").reduce(imp, String::concat);
        return imp;

    }

    private String annotedControllers() throws Exception {

        String properties = "";
        properties += " private final " + objectNameDA + " " + daGlobalVariable + " = new " + objectNameDA + "();\n";
        List<String> globalRefObjects = new ArrayList<>();
        for (FieldDAO field : fields) {
            if (!field.isHelper()) {
                if (field.isCollection()) {
                    properties += "@FXML private " + field.getControlType().name() + "<" + field.getReferences() + "DA> " + field.getControlName() + ";\n";
                    if (field.makeEditableTable()) {
                        properties += "@FXML private MenuItem cmiSelect" + field.getFieldName() + ";\n";
                    }
                } else {
                    properties += "@FXML private " + field.getControlType().name() + " " + field.getControlName() + ";\n";
                    properties += field.annotedImageButtons();
                    if (field.isReferance() && !field.getEnumerated()) {
                        properties += "@FXML private MenuItem cmiSelect" + field.getFieldName() + ";\n";
                        if (!(field.getReferences().equalsIgnoreCase("LookupData") || field.getReferences().equalsIgnoreCase(objectName))) {
                            addIfNotExists(globalRefObjects, field.getReferencesDA());
                        }
                    }
                }
            }
        }
        properties = globalRefObjects.stream().map((string) -> "private final " + string + " o" + string + " = new " + string + "();").reduce(properties, String::concat);

        String tableColumns = "";
        for (FieldDAO field : subListFields) {

            String custom = field.getReferences();
            List<FieldDAO> subFields = field.getSubFieldListDAO();

            for (FieldDAO subField : subFields) {
                if (subField.isReferance()) {
                    if (!field.getReferences().equalsIgnoreCase(objectName)) {
                        tableColumns += subField.getReferencesDA() + " o" + subField.getReferencesDA() + " = new " + subField.getReferencesDA() + "();\n";
                    }
                }
                String type = subField.getDataType();
                if (type.equalsIgnoreCase("float") || type.equalsIgnoreCase("double")
                        || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
                    tableColumns += "@FXML private TableColumn<" + custom + "DA, String> " + subField.getColumnName(custom) + ";\n";
                } else {
                    tableColumns += "@FXML private TableColumn<" + custom + "DA, " + subField.getDataTypeWrapper() + "> " + subField.getColumnName(custom) + ";\n";
                }

            }

        }
        return properties + tableColumns;
    }

    public String setControlIDInInitialiser() {

        FieldDAO IDHelperObject = Utilities.getIDHelper(fields);
        FieldDAO idGeneratorObject = Utilities.getIDGenerator(fields);
        if (IDHelperObject == null) {
            return "";
        } else if (idGeneratorObject != null) {
            if (idGeneratorObject.isReferance()) {
                return idGeneratorObject.getControlName() + ".setOnAction(e->" + getNextIDLineCall() + ");";
            } else {
                return idGeneratorObject.getControlName() + ".focusedProperty().addListener((ov, t, t1) -> {\n"
                        + "                if (t) {\n" + getNextIDCall() + "\n}\n"
                        + "            });";
            }
        } else {
            return getNextIDCall();
        }
    }

    private String getNextIDCall() {
        return "this.setNext" + primaryKey.getFieldName() + "();\n";
    }

    private String getNextIDLineCall() {
        return "this.setNext" + primaryKey.getFieldName() + "()";
    }

    private String initMethod(Project currentProject) throws Exception {
        String initProperties = "this.primaryKeyControl = " + primaryKey.getControlName() + ";\n"
                + "          this.dbAccess = " + daGlobalVariable + ";\n"
                + "          this.restrainColumnConstraint = false;\n"
                + " //this.prefSize = 300;\n";
        String lookupDataLoadings = "";
        String comboLoadings = "";
        String numberValidator = "";
        String numberFormator = "";
        String imageButtonActions = "";
        String editableTable = "";
        String menuLoadCalls = "";
        String hiddenCheckBox = "";
        for (FieldDAO field : fields) {
            Project project;
            if (field.getProjectName().isBlank()) {
                project = currentProject;
            } else {
                project = field.getProject();
            }

            if (field.isReferance() && !field.isCollection()) {

                if (field.getEnumerated()) {
                    comboLoadings += field.getControlName() + ".setItems(FXCollections.observableArrayList(" + field.getReferences() + ".values()));";
                } else {

                    if (field.getReferences().equalsIgnoreCase("LookupData")) {
                        lookupDataLoadings += " loadLookupData(" + field.getControlName() + ", " + project.getObjectNameClass() + "." + field.getFieldName().toUpperCase() + ");\n";
                        menuLoadCalls += "selectLookupData(cmiSelect" + field.getFieldName() + ", " + project.getObjectNameClass() + "." + field.getFieldName().toUpperCase() + ",  \"" + field.getCaption() + "\", " + field.getControlName() + ", false);";
                    } else {
                        comboLoadings += "loadDBEntities(o" + field.getReferencesDA() + ".get" + field.getReferences() + "s(), " + field.getControlName() + ");\n";
                        menuLoadCalls += "selectItem(" + project.getNavigationClass() + ".MAIN_CLASS, cmiSelect" + field.getFieldName() + ", o" + field.getReferencesDA() + ", \"" + field.getReferences() + "\", \"" + field.getCaption() + "\", " + field.getControlName() + ", true);";

                    }

                }

            } else {
                if (field.makeEditableTable()) {
                    editableTable += field.getControlName() + ".setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);";
                    editableTable += "setTableEditable(" + field.getControlName() + ");";
                    editableTable += "addRow(" + field.getControlName() + ", new " + field.getReferencesDA() + "());";
                    editableTable += "cmiSelect" + field.getFieldName() + ".setOnAction(e->load" + field.getFieldName() + "());";

                }

            }
            if (field.getControlName().equalsIgnoreCase("chkHidden")) {
                hiddenCheckBox += "chkHidden.disableProperty().bind(btnSave.textProperty().isEqualToIgnoreCase(FormMode.Save.name()));";
            }

            imageButtonActions += field.ImageButtonsActions();
            numberValidator += field.NumberValidator();
            numberFormator += field.NumberFormatter();
        }

        String methodBody = lookupDataLoadings + comboLoadings + "\n"
                + numberValidator + numberFormator + editableTable;

        String editColumnMethodCall = "";
        String booleanTableColumnEdit = "";
        for (FieldDAO field : subListFields) {
            List<FieldDAO> subFields = field.getSubFieldListDAO();
            String custom = field.getReferences();
            String refereceDA = field.getReferencesDA().trim();
            String referenceVariableName = Utilities.getVariableName(refereceDA);
            for (FieldDAO subField : subFields) {
                String customColumnName = subField.getColumnName(custom);
                editColumnMethodCall += "set" + field.getReferences() + subField.getFieldName() + "();\n";
                if (subField.getDataType().equalsIgnoreCase("boolean")) {
                    booleanTableColumnEdit += "final Callback<TableColumn<" + refereceDA + ", Boolean>, TableCell<" + refereceDA + ", Boolean>> \n"
                            + "                    " + subField.getVariableName() + "Factory = CheckBoxTableCell.forTableColumn(" + customColumnName + ");\n"
                            + "            " + customColumnName + ".setCellValueFactory((TableColumn.CellDataFeatures<" + refereceDA + ", Boolean> param) -> {\n"
                            + "                " + refereceDA + " " + referenceVariableName + " = param.getValue();\n"
                            + "                SimpleBooleanProperty valueBooleanProperty = new SimpleBooleanProperty(" + referenceVariableName + "." + subField.getCall() + ");\n"
                            + "                valueBooleanProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {\n"
                            + "                    " + referenceVariableName + ".set" + subField.getFieldName() + "(newValue);\n"
                            + "                });\n"
                            + "                return valueBooleanProperty;\n"
                            + "            });\n"
                            + "            " + customColumnName + ".setCellFactory(" + subField.getVariableName() + "Factory);";
                }
            }
        }

        methodBody += initProperties + imageButtonActions + setControlIDInInitialiser() + editColumnMethodCall + booleanTableColumnEdit + menuLoadCalls + hiddenCheckBox;
        return " @Override\n" + Utilities.makeTryMethod("public", "void", "initialize", "URL url, ResourceBundle rb", methodBody);
    }

    private String save() {
        String makeInitials = "";
        String saveVariables = "this.editSuccessful = false;";

        List<FieldDAO> conField = fields.stream()
                .filter((p) -> !p.isCollection())
                .filter((p) -> !p.isHelper())
                .collect(Collectors.toList());
        for (int i = 0; i < conField.size(); i++) {
            FieldDAO field = conField.get(i);

            makeInitials += field.getVariableName();

            if (i < conField.size() - 1) {
                makeInitials += ",";
            }
        }

        for (int i = 0; i < fields.size(); i++) {
            FieldDAO field = this.fields.get(i);
            if (!field.isHelper()) {

                saveVariables += field.initialseSavableVariable();

            }

        }
        String body = saveVariables.concat("\n");
        body += objectNameDA + " " + daVariableName + "= new " + objectNameDA + "(" + makeInitials + ");\n";

        if (!subListFields.isEmpty()) {
            for (FieldDAO field : fields) {
                if (field.isCollection()) {
                    body += field.getVariableName() + "DAs.forEach(e->{\n"
                            + "                e.set" + objectName + "(" + daVariableName + ".get" + objectName + "());\n"
                            + "            });\n"
                            + "            " + daVariableName + ".set" + field.getFieldName() + "DAs(" + field.getVariableName() + "DAs);";
                }

            }
        }

        body += "String buttonText =btnSave.getText();\n"
                + "if (buttonText.equalsIgnoreCase(FormMode.Save.name())) {\n"
                + "                " + daVariableName + ".save();\n"
                + "                message(\"Saved Successfully\");\n"
                + "                clear();\n" + afterClearing()
                + "            } else if (buttonText.equalsIgnoreCase(FormMode.Update.name())) {\n"
                + "                " + daVariableName + ".update();\n"
                + "                message(\"Updated Successfully\");\n"
                + "            }\n"
                + "this.dbAccess = " + daVariableName + ";\n"
                + "this.editSuccessful = true;\n";

        return Utilities.makeTryMethod("@Override\nprotected", "void", "save", "", body);
    }

    private String loadData() {
        String updateBody = "";
        for (FieldDAO field : fields) {
            if (field.isCollection()) {
                updateBody += field.getControlName() + ".setItems(FXCollections.observableArrayList(" + daVariableName + ".get" + field.getFieldName() + "DAs()));";
                updateBody += "addRow(" + field.getControlName() + ", new " + field.getReferencesDA() + "());\n";

            } else if (field.isReferance() && !field.getEnumerated()) {
                updateBody += field.setControlText(daVariableName.concat(".get" + field.getFieldName() + "()"));
            } else {
                updateBody += field.setControlText(daVariableName.concat(".").concat(field.getCall()));
            }
        }
        String updateMethod = "@Override\npublic void loadData() {\n"
                + "        try {\n"
                + primaryKey.initialseSavableVariable() + "\n"
                + objectNameDA + " " + daVariableName + " = " + daGlobalVariable + " .get(" + primaryKeyVariableName + ");\n"
                + updateBody + "\n"
                + "        } catch (Exception e) {\n"
                + "            errorMessage(e);\n"
                + "        }\n"
                + "\n"
                + "    }\n"
                + "";

        return updateMethod;

    }

    private String afterClearing() {
        String afterClearing = "";
        for (FieldDAO field : fields) {
            afterClearing += field.afterClearing();
        }
        FieldDAO idGeneratorObject = Utilities.getIDGenerator(fields);
        FieldDAO IDHelperObject = Utilities.getIDHelper(fields);

        if (idGeneratorObject == null) {
            if (IDHelperObject != null) {
                afterClearing += "this.setNext" + primaryKey.getFieldName() + "();\n";
            }
        }
        return afterClearing;
    }

    private String getSetIDControl() throws Exception {

        String body;
        FieldDAO idHelperObject = Utilities.getIDHelper(fields);
        FieldDAO idGeneratorObject = Utilities.getIDGenerator(fields);
        if (idGeneratorObject != null && idHelperObject == null) {
            throw new Exception("The ID generator does not have a helper column");
        }
        if (idGeneratorObject == null && idHelperObject == null) {
            return "";
        }
        if (idGeneratorObject == null) {
            body = primaryKey.getControlName() + ".setText(" + daGlobalVariable + ".getNext" + primaryKey.getFieldName() + ""
                    + "(" + daGlobalVariable + ".getNext" + idHelperObject.getFieldName() + "()));\n";
        } else {

            String idGeneratorVariableName = idGeneratorObject.getVariableName();
            String references = idGeneratorObject.getReferences();
            if (idGeneratorObject.isReferance()) {

                if (idGeneratorObject.getEnumerated()) {
                    body = references + " " + idGeneratorVariableName + " = (" + references + ") "
                            + "cbo" + idGeneratorObject.getFieldName() + ".getValue();\n"
                            + "        if (" + idGeneratorVariableName + " == null) {\n"
                            + "            return;\n"
                            + "        }\n"
                            + primaryKey.getControlName() + ".setText(" + daGlobalVariable + ".getNext" + primaryKey.getFieldName() + ""
                            + "(" + daGlobalVariable + ".getNext" + idHelperObject.getFieldName() + "(" + idGeneratorVariableName + "), " + idGeneratorVariableName + "));\n";

                } else {
                    body = idGeneratorObject.initialseOptianalSavableVariable() + ""
                            + "if(" + idGeneratorVariableName + " == null){return;}"
                            + primaryKey.getControlName() + ".setText(" + daGlobalVariable + ".getNext" + primaryKey.getFieldName() + ""
                            + "(" + daGlobalVariable + ".getNext" + idHelperObject.getFieldName() + "(" + idGeneratorVariableName + "), " + idGeneratorVariableName + ".getId().toString()));\n";
                }

            } else {
                body = "String " + idGeneratorVariableName + " = "
                        + idGeneratorObject.getControlName() + ".getText();\n"
                        + "        if (" + idGeneratorVariableName + ".isBlank()) {\n"
                        + "            return;\n"
                        + "        }\n"
                        + primaryKey.getControlName() + ".setText(" + daGlobalVariable + ".getNext" + primaryKey.getFieldName() + ""
                        + "(" + daGlobalVariable + ".getNext" + idHelperObject.getFieldName() + "(" + idGeneratorVariableName + "), " + idGeneratorVariableName + "));\n";

            }

        }

        String genCondition = "if(btnSave.getText ().equalsIgnoreCase(FormMode.Save.name())){\n";
        String bBody = genCondition + body.concat("}");
        String tryBody = "";
        String methodName = "setNext".concat(primaryKey.getFieldName());
        if (!bBody.equals("")) {
            tryBody = "try{" + bBody + "}catch(Exception e){errorMessage(e);}";
            bBody = tryBody;

        }

        return Utilities.makeMethod("private", "void", methodName, "", bBody);

    }

    private String methods(Project currentProject) throws Exception {


            String cDelete = "@Override\nprotected void delete(){\n"
                    + "        try {\n"
                    + Utilities.getPrimaryKey(fields).initialseSavableVariable()
                    + objectNameDA + " " + daVariableName + " = " + daGlobalVariable + " .get(" + primaryKeyVariableName + ");\n"
                    + " if(!warningOk(\"Confirm Delete\", \"You are about to delete a record with ID: \"+" + primaryKeyVariableName + "+\" Are you sure you want to continue?\", \"Remember this action cannot be un done\"))return;\n"
                    + "            if(" + daVariableName + ".delete()){\n"
                    + "                message(\"Deleted Successfully\");\n"
                    + "            }\n"
                    + "        } catch (Exception e) {\n"
                    + "            errorMessage(e);\n"
                    + "        }\n"
                    + "        }\n";
            
            String editColumn = "";
            for (FieldDAO field : subListFields) {
                List<FieldDAO> subFields = field.getSubFieldListDAO();
                
                for (FieldDAO subField : subFields) {
                    editColumn += subField.editTableColumnMethod(field);
                }
            }
            
            String loadMethods = "";
            loadMethods = subListFields.stream().map((field) -> field.makeLoadCollections(currentProject)).reduce(loadMethods, String::concat);
            return initMethod(currentProject) + save() + cDelete + loadData() + getSetIDControl() + editColumn + loadMethods;
        
    }

    public String makeClass(Project currentProject) throws Exception {
        JavaClass javaClass = new JavaClass(currentProject.getContollerPackage(), objectNameController, this.imports(currentProject),
                this.annotedControllers(), "", "", methods(currentProject));
        return javaClass.makeClass("EditController");
    }

}
