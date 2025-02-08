/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.dataacess;

import com.saburi.model.Field;
import com.saburi.model.Project;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.UIControls;
import com.saburi.utils.Enums.Saburikeys;
import static com.saburi.utils.Enums.UIControls.CheckBox;
import static com.saburi.utils.Enums.UIControls.ComboBox;
import com.saburi.utils.Enums.keys;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.addIfNotExists;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.*;

/**
 *
 * @author ClinicMaster13
 */
public class FieldDAO {

    private Field field = new Field();
    private SimpleStringProperty fieldName = new SimpleStringProperty(this, "fieldName", "");
    private SimpleStringProperty caption = new SimpleStringProperty(this, "caption", "");
    private SimpleStringProperty dataType = new SimpleStringProperty(this, "dataType", "");
    private SimpleStringProperty references = new SimpleStringProperty(this, "references", "");
    private SimpleStringProperty mapping = new SimpleStringProperty(this, "mapping", "");
    private SimpleStringProperty key = new SimpleStringProperty(this, "key", "");
    private SimpleStringProperty saburiKey = new SimpleStringProperty(this, "saburiKey", "");
    private SimpleIntegerProperty size = new SimpleIntegerProperty(this, "size", 100);
    private SimpleBooleanProperty nullable = new SimpleBooleanProperty(this, "nullable", true);
    private SimpleBooleanProperty enumerated = new SimpleBooleanProperty(this, "enumerated", false);
    private SimpleStringProperty subFields = new SimpleStringProperty(this, "subFields", "");
    private SimpleStringProperty projectName = new SimpleStringProperty(this, "projectName", "");
    private SimpleBooleanProperty expose = new SimpleBooleanProperty(this, "expose", false);
    private SimpleStringProperty moduleName = new SimpleStringProperty(this, "moduleName", "");
    private SimpleBooleanProperty select = new SimpleBooleanProperty(this, "select", false);

    private String variableName;
    private String referencesID;
    private String display;
    private String referencesVariableID;
    private String displayVariableName;
    private final String displayDataType = "String";
    private ProjectDAO oProjectDAO = new ProjectDAO();
    private Project project;
    private Project commonProject;
    private String dialogName;
    private String dialogVariableName;

    public FieldDAO() {
    }

    public FieldDAO(Field field) throws Exception {
        this.field = field;
        initialseProprties();
    }

    private void initialseProprties() throws Exception {
        this.fieldName = new SimpleStringProperty(field.getFieldName());
        this.caption = new SimpleStringProperty(field.getCaption());
        this.dataType = new SimpleStringProperty(field.getDataType());
        this.references = new SimpleStringProperty(field.getReferences());
        this.subFields = new SimpleStringProperty(field.getSubFields());
        this.mapping = new SimpleStringProperty(field.getMapping());
        this.key = new SimpleStringProperty(field.getKey());
        this.saburiKey = new SimpleStringProperty(field.getSaburiKey());
        this.size = new SimpleIntegerProperty(field.getSize());
        this.enumerated = new SimpleBooleanProperty(field.isEnumerated());
        this.projectName = new SimpleStringProperty(field.getProjectName());
        this.nullable = new SimpleBooleanProperty(field.isNullable());
        this.variableName = Utilities.getVariableName(this.fieldName.get());
        this.referencesID = this.fieldName.get().concat("ID");
        this.display = this.fieldName.get().concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);
        this.project = oProjectDAO.get(field.getProjectName());
        this.expose.set(field.isExpose());
        this.moduleName.set(field.getModuleName());
        this.select.set(field.isSelect());
        this.dialogName = this.fieldName.get().concat("Dialog");
        this.dialogVariableName = Utilities.getVariableName(dialogName);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldDAO)) {
            return false;
        }

        FieldDAO fieldDAO = (FieldDAO) o;

        return this.getFieldName().equalsIgnoreCase(fieldDAO.getFieldName());
    }

    @Override
    public int hashCode() {
        return getFieldName().hashCode();

    }

    public static List<FieldDAO> getFieldDAOs(List<Field> fields) {
        List<FieldDAO> fieldDAOs = new ArrayList<>();
        fields.forEach(fd -> {
            try {
                fieldDAOs.add(new FieldDAO(fd));
            } catch (Exception ex) {
                Logger.getLogger(FieldDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return fieldDAOs;

    }

    public void setFieldName(String fieldName) {
        this.fieldName.set(fieldName);
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public void setDataType(String dataType) {
        this.dataType.set(dataType);
    }

    public void setReferences(String references) {
        this.references.set(references);
    }

    public void setMapping(String mapping) {
        this.mapping.set(mapping);
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public void setSaburiKey(String saburiKey) {
        this.saburiKey.set(saburiKey);
    }

    public void setSubFields(String subFields) {
        this.subFields.set(subFields);
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public void setNullable(boolean nullable) {
        this.nullable.set(nullable);
    }

    public void setEnumerated(boolean enumerated) {
        this.enumerated.set(enumerated);
    }

    public String getFieldName() {
        return fieldName.get();
    }

    public String getCaption() {
        return caption.get();
    }

    public String getDataType() {
        return dataType.get();
    }

    public String getReferences() {
        return references.get();
    }

    public String getMapping() {
        return mapping.get();
    }

    public String getKey() {
        return key.get();
    }

    public String getSaburiKey() {
        return saburiKey.get();
    }

    public String getSubFields() {
        return subFields.get();
    }

    public int getSize() {
        return size.get();
    }

    public boolean getNullable() {
        return nullable.get();
    }

    public boolean getEnumerated() {
        return enumerated.get();
    }

    public String getProjectName() {
        return projectName.get();
    }

    public void setProjectName(String projectName) {
        this.projectName.set(projectName);
    }

    public boolean isExpose() {
        return this.expose.get();
    }

    public void setExpose(boolean expose) {
        this.expose.set(expose);
    }

    public boolean isSelect() {
        return this.select.get();
    }

    public void setSelect(boolean select) {
        this.select.set(select);
    }

    public SimpleBooleanProperty getSelect() {
        return select;
    }

    public void setSelect(SimpleBooleanProperty select) {
        this.select = select;
    }

    public String getModuleName() {
        return moduleName.get();
    }

    public void setModuleName(String moduleName) {
        this.moduleName.set(moduleName);
    }

    public SimpleBooleanProperty getExpose() {
        return expose;
    }

    public void setExpose(SimpleBooleanProperty expose) {
        this.expose = expose;
    }

    public SimpleBooleanProperty getNullableProperty() {
        return this.nullable;
    }

    public SimpleBooleanProperty getEnumeratedProperty() {
        return this.enumerated;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getReferencesID() {
        return referencesID;
    }

    public String getReferencesVariableID() {
        return referencesVariableID;
    }

    public String getDisplay() {
        return display;
    }

    public String getDisplayVariableName() {
        return displayVariableName;
    }

    public String getDisplayDataType() {
        return displayDataType;
    }

    public Project getProject() {
        return project;
    }

    public Project getCommonProject() {
        return commonProject;
    }

    public Project getFieldLineProject(Project lineProject) throws Exception {
        if (this.getProjectName().isBlank()) {
            return lineProject;
        } else {
            return oProjectDAO.get(getProjectName());
        }
    }

    public String getColumnName(String custom) {
        if (isReference() && !this.getEnumerated()) {
            return "tbc" + custom + getFieldName() + "ID";
        } else {
            return "tbc" + custom + getFieldName();
        }
    }

    public String getColumnName() {
        return "tbc" + getFieldName();
    }
    
    public String addIdPrefix(String variableName){
       int length = variableName.length();
       if(length<2)return variableName;
       return variableName.substring(length-2, length);
    }

    public String getDBColumnName(boolean forceReferences) {
        if (isForeignKey(forceReferences)
                && !getReferences().equalsIgnoreCase("RevInfo")) {

            return addIdPrefix(variableName).equalsIgnoreCase("id") ? variableName : variableName.concat(forceReferences ? "Id" : "");
        }
        return this.getVariableName();
    }

    public String getReqVariableName(boolean forceReferences) {
        if (isForeignKey(forceReferences)
                && !getReferences().equalsIgnoreCase("RevInfo")) {
            return this.getVariableName().concat(forceReferences ? "Id" : "");
        }
        return this.getVariableName();
    }

    public String getReqFieldName(boolean forceReferences) {
        if (isForeignKey(forceReferences) && !getReferences().equalsIgnoreCase("RevInfo")) {
            return this.getFieldName().concat(forceReferences ? "Id" : "");
        }
        return this.getFieldName();
    }

    public boolean hasDisplay() {
        return this.getDataType().equalsIgnoreCase("Double") || this.getDataType().equalsIgnoreCase("float")
                || this.getDataType().equalsIgnoreCase("BigDecimal")
                || this.getDataType().equalsIgnoreCase("int") || this.getDataType().equalsIgnoreCase("Integer")
                || this.getDataType().equalsIgnoreCase("Date") || this.getDataType().equalsIgnoreCase("DateTime")
                || this.getDataType().equalsIgnoreCase("LocalDate") || this.getDataType().equalsIgnoreCase("LocalDateTime");
    }

    private String getToDisplayValue() {
        if (hasDisplay()) {
            return displayVariableName + ".get(), ";
        }
        return "";
    }

    public boolean isCollection() {
        return this.getDataType().equalsIgnoreCase("List") || this.getDataType().equalsIgnoreCase("Set");
    }

    public boolean makeEditableTable() {
        return !getSubFields().isBlank();
    }

    public String getDialogName() {
        return dialogName;
    }

    public String getDialogVariableName() {
        return dialogVariableName;
    }

    @Override
    public String toString() {
        return fieldName.get() + ", " + caption.get() + ", " + dataType.get() + ", "
                + "" + references.get() + "," + subFields.get() + ", "
                + mapping.get() + ", " + key.get() + ", " + saburiKey.get() + ", "
                + size.get() + ", " + enumerated.get() + ", " + nullable.get() + ", "
                + projectName.get() + ", "
                + expose.get() + ", " + moduleName.get();
    }

    public boolean isReference() {
        return !(this.getReferences().isBlank() || this.getReferences().equalsIgnoreCase("None"));
    }

    public boolean isForeignKey(boolean forceReference) {
        return this.isReference() && forceReference && !getEnumerated();
    }

    public boolean isPrimaryKey() {
        return this.getKey().equalsIgnoreCase(keys.Primary.name());
    }

    public boolean isPrimaryKeyAuto() {
        return this.getKey().equalsIgnoreCase(keys.Primary_Auto.name());
    }

    public boolean isHelper() {
        return this.getSaburiKey().equalsIgnoreCase(Saburikeys.ID_Helper.name());
    }

    public boolean isIDGenerator() {
        return this.getSaburiKey().equalsIgnoreCase(Saburikeys.ID_Generator.name());
    }

    public boolean isDisplayKey() {
        return this.getSaburiKey().equalsIgnoreCase(Saburikeys.Display.name());
    }

    public String getReferenceDisplayText() {
        if (isReference() && !getEnumerated()) {
            return ".getDisplayKey()";
        }
        return "";
    }

    public String getReferenceDisplayText(boolean forceReferences) {
        if (isReference() && forceReferences && !getEnumerated()) {
            return ".getDisplayKey()";
        }
        return "";
    }

    public boolean referencesLookup() {
        return this.getReferences().equalsIgnoreCase("LookupData");
    }

    public boolean referencesLookupExt(Project project) {
        return this.referencesLookup() && !this.getProjectName().equalsIgnoreCase(project.getProjectName());
    }

    public boolean referencesAccount() {
        return this.getReferences().equalsIgnoreCase("Account");
    }

    public boolean referencesAccountExt(Project project) {
        return this.referencesAccount() && !this.getProjectName().equalsIgnoreCase(project.getProjectName());
    }

    public boolean referencesIN(Project project) {
        return this.isReference() && (this.getProjectName().equalsIgnoreCase(project.getProjectName()) || Utilities.isNullOrEmpty(this.getProjectName()));
    }

    public String getDeclaration(boolean forceReferences, boolean newLine) {
        if (newLine) {

            if (isCollection()) {
                if (getDataType().equalsIgnoreCase("List")) {
                    return "List<" + getReferences() + "> " + getVariableName() + " = new ArrayList<>();\n";
                } else if (getDataType().equalsIgnoreCase("Set")) {
                    return "Set<" + getReferences() + "> " + getVariableName() + " = new HashSet<>();\n";
                } else {
                    return "";
                }
            } else {
                return this.getUsableDataType(forceReferences).concat(" ").concat(variableName).concat(";\n");
            }
        }
        return this.getUsableDataType(forceReferences).concat(" ").concat(variableName);

    }

    public String getReferencesDA() {
        if (!isReference()) {
            return "";
        }
        return getReferences().concat("DA");
    }

    public String getVariableNameDA() {

        return getVariableName().concat("DA");
    }

    public String getSearchDataType() {
        String type = this.dataType.get();
        if (type.equalsIgnoreCase("String") || type.equalsIgnoreCase("File")) {
            return "SearchDataTypes.STRING";
        } else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
            return "SearchDataTypes.INTEGER";
        } else if (type.equalsIgnoreCase("float")) {
            return "SearchDataTypes.NUMBER";
        } else if (type.equalsIgnoreCase("BigDecimal")) {
            return "SearchDataTypes.NUMBER";
        } else if (type.equalsIgnoreCase("double")) {
            return "SearchDataTypes.MONEY";
        } else if (type.equalsIgnoreCase("LocalDate") || type.equalsIgnoreCase("LocalDateTime") || type.equalsIgnoreCase("Date") || type.equalsIgnoreCase("Time")) {
            return "SearchDataTypes.DATE";
        } else if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("Boolean")) {
            return "SearchDataTypes.BOOLEAN";
        } else {
            return "SearchDataTypes.STRING";
        }

    }
    
     public boolean isPrimitiveNumber() {
        String type = this.dataType.get();
        return type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")
                || type.equalsIgnoreCase("float")
                || type.equalsIgnoreCase("double");
    }

    public boolean isNumeric() {
        String type = this.dataType.get();
        return isPrimitiveNumber()
                || type.equalsIgnoreCase("BigDecimal");
    }

    public boolean isDate() {
        return this.getDataType().equalsIgnoreCase("Date") || this.getDataType().equalsIgnoreCase("LocalDate");
    }
    
    public boolean isBigDecimal(){
        return this.dataType.get().equalsIgnoreCase("BigDecimal");
    }

    public boolean isDateTime() {
        return this.getDataType().equalsIgnoreCase("DateTime") || this.getDataType().equalsIgnoreCase("LocalDateTime");
    }

    public String getDataTypeWrapper() {
        String type = this.dataType.get();
        if (type.equalsIgnoreCase("String") || type.equalsIgnoreCase("File")) {
            return "String";
        } else if (type.equalsIgnoreCase("int")) {
            return "Integer";
        } else if (type.equalsIgnoreCase("float")) {
            return "Float";
        } else if (type.equalsIgnoreCase("double")) {
            return "Double";
        } else if (type.equalsIgnoreCase("BigDecimal")) {
            return "BigDecimal";
        }
        else if (type.equalsIgnoreCase("Date")) {
            return "Date";
        } else if (type.equalsIgnoreCase("LocalDate")) {
            return "LocalDate";
        } else if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("Boolean")) {
            return "Boolean";
        } else {
            return "String";
        }

    }

    public UIControls getControlType() {
        if (isReference() && !this.isCollection()) {
            return UIControls.ComboBox;
        }

        if (this.getDataType().equalsIgnoreCase("Date") || this.getDataType().equalsIgnoreCase("DateTime")
                || this.getDataType().equalsIgnoreCase("LocalDate") || this.getDataType().equalsIgnoreCase("LocalDateTime")) {
            return UIControls.DatePicker;
        }

        if (this.getDataType().equalsIgnoreCase("bool") || this.getDataType().equalsIgnoreCase("boolean")) {
            return UIControls.CheckBox;
        }

        if (this.getDataType().equalsIgnoreCase("Image")) {
            return UIControls.ImageView;
        }

        if (this.getDataType().equalsIgnoreCase("File")) {
            return UIControls.FileBrowser;
        }

        if (this.isCollection()) {
            if (makeEditableTable()) {
                return UIControls.TableView;
            } else {
                return UIControls.TextArea;
            }
        }

        if (this.getDataType().equalsIgnoreCase("String") || this.getDataType().equalsIgnoreCase("File")) {
            if (this.getSize() > 100) {
                return UIControls.TextArea;
            }
            return UIControls.TextField;
        } else {
            return UIControls.TextField;
        }
    }

    public UIControls getPrimaryControlType() {
        if (isReference() && !this.isCollection()) {
            return UIControls.ComboBox;
        }

        if (this.getDataType().equalsIgnoreCase("Date") || this.getDataType().equalsIgnoreCase("DateTime")
                || this.getDataType().equalsIgnoreCase("LocalDate") || this.getDataType().equalsIgnoreCase("LocalDateTime")) {
            return UIControls.DatePicker;
        }

        if (this.getDataType().equalsIgnoreCase("bool") || this.getDataType().equalsIgnoreCase("boolean")) {
            return UIControls.CheckBox;
        }

        if (this.getDataType().equalsIgnoreCase("Image")) {
            return UIControls.ImageView;
        }

        if (this.getDataType().equalsIgnoreCase("File")) {
            return UIControls.TextArea;
        }

        if (this.isCollection()) {
            if (makeEditableTable()) {
                return UIControls.TableView;
            } else {
                return UIControls.TextArea;
            }
        }

        if (this.getDataType().equalsIgnoreCase("String") || this.getDataType().equalsIgnoreCase("File")) {
            if (this.getSize() > 100) {
                return UIControls.TextArea;
            }
            return UIControls.TextField;
        } else {
            return UIControls.TextField;
        }
    }

    public boolean hasCrudTable() {
        return this.getControlType().equals(Enums.UIControls.TableView);
    }

    public String getCall() {

        return switch (getControlType()) {
            case ComboBox ->
                " get" + fieldName.get() + "()";
            case CheckBox ->
                " is" + fieldName.get() + "()";
            default ->
                " get" + fieldName.get() + "()";
        };
    }
    
    public  String getDataTypeImps() {
        String dType = this.getDataType();
        if (dType.equalsIgnoreCase("LocalDate")) {
            return "import java.time.LocalDate\n";
        }
        if (dType.equalsIgnoreCase("LocalDateTime")) {
            return "import java.time.LocalDateTime";
        }
        if (dType.equalsIgnoreCase("Date")) {
            return "import java.util.Date";
        }
        if (dType.equalsIgnoreCase("DateTime")) {
            return "import java.util.Date";
        }
       
        if (dType.equalsIgnoreCase("BigDecimal")) {
            return "import java.math.BigDecimal";
        }
        
        if (dType.equalsIgnoreCase("List")) {
            return "import java.util.ArrayList";
        }
        
        if (dType.equalsIgnoreCase("Set")) {
            return "import java.util.HashSet";
        }
        return "";
    }
    
    public  String getGenericDataTypeImps() {
        String dType = this.getDataType();
        if (dType.equalsIgnoreCase("List")) {
            return "import java.util.List";
        }
        
        if (dType.equalsIgnoreCase("Set")) {
            return "import java.util. Set";
        }
        return "";
    }

    public String getReqCall() {
        String fName = getReqFieldName(false);

        return switch (getControlType()) {
            case ComboBox ->
                " get" + fName + "()";
            case CheckBox ->
                " is" + fName + "()";
            default ->
                " get" + fName + "()";
        };
    }

    public String makeGetter(boolean forceReferences) {
        String type = this.getDataType();
        if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) {
            return Utilities.makeMethod("public", getUsableDataType(false), "is" + this.getFieldName(), "", "return " + this.variableName + ";");
        } else {
            return Utilities.makeMethod("public", getUsableDataType(forceReferences), "get" + this.getFieldName(), "", "return " + this.variableName + ";");

        }
    }

    public String makeSetter(boolean forceReferences) {
        return "public ".concat("void").concat(" ").
                concat("set").concat(getFieldName()).concat("(" + getDeclaration(forceReferences, false) + "){\n").concat("this.").
                concat(getVariableName()).concat(" = ").concat(getVariableName()).concat(";\n}");
    }

    public String setCall(String objectName) {
        return Utilities.getVariableName(objectName).concat(".set").concat(getFieldName()).concat("(" + variableName + ");\n");
    }

    public String makeProperties(boolean forceReferences) {
        return this.makeGetter(forceReferences) + this.makeSetter(forceReferences);
    }

    public List entityImports(Project currentProject, boolean considerReferences) throws Exception {
        List list = new ArrayList();
        if (getProjectName().isBlank()) {
            project = currentProject;
        } else {
            project = oProjectDAO.get(getProjectName());
        }

        this.commonProject = oProjectDAO.get(project.getCommonProjectName());
        if (isPrimaryKey()) {
            addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
            if (isReference()) {
                if (getEnumerated()) {
                } else {
                    addIfNotExists(list, "import jakarta.persistence.MapsId");
                }
            }
        }

        if (isIDGenerator()) {
            addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
        }
        if (isPrimaryKeyAuto()) {
            addIfNotExists(list, "import jakarta.persistence.GeneratedValue");
            addIfNotExists(list, "import jakarta.persistence.GenerationType");
        } else if (isReference()) {
            if (!nullable.get()) {
                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
            }

            if (isCollection()) {
                addIfNotExists(list, "import java.util." + getDataType());
                addIfNotExists(list, "import jakarta.persistence.CascadeType");
                if (project.getProjectType().equals(Enums.ProjectTypes.Springboot_API)) {
                    addIfNotExists(list, "import com.fasterxml.jackson.annotation.JsonIgnoreProperties");
                }
                if (getDataType().equalsIgnoreCase("List")) {
                    addIfNotExists(list, "import java.util.ArrayList");
                } else if (getDataType().equalsIgnoreCase("Set")) {
                    addIfNotExists(list, "import java.util.HashSet");
                }
                if (mapping.get().isBlank()) {
                    mapping.set("OneToMany");
                }
                addIfNotExists(list, "import jakarta.persistence." + mapping.get());

            }

            if (enumerated.get()) {
                if (project.getProjectType().equals(Enums.ProjectTypes.Desktop)) {
                    addIfNotExists(list, "import " + project.getUtilPackage() + "." + project.getEnumClass() + "." + references.get());
                }
                if (project.getProjectType().equals(Enums.ProjectTypes.Springboot_API)) {

                    String enumPackage = (referencesIN(project)) ? project.getBasePackage() : commonProject.getBasePackage();
                    addIfNotExists(list, "import " + enumPackage + ".enums." + references.get());

                }
                addIfNotExists(list, "import jakarta.persistence.Enumerated");
                addIfNotExists(list, "import jakarta.persistence.EnumType");

            } else {
                if (considerReferences) {
                    if (mapping.get().isBlank() && !isCollection()) {
                        mapping.set("OneToOne");
                    }

                    if (key.get().isBlank()) {
                        key.set("Foreign");

                    }
                    addIfNotExists(list, "import jakarta.persistence.JoinColumn");
                    addIfNotExists(list, "import jakarta.persistence.ForeignKey");
                    addIfNotExists(list, "import jakarta.persistence." + mapping.get());

                    if (project.getProjectType().equals(Enums.ProjectTypes.Desktop)) {
                        if (!project.getProjectName().equalsIgnoreCase(currentProject.getProjectName())) {
                            addIfNotExists(list, "import " + project.getEntityPackage() + "." + references.get());
                        }
                    }
                    addIfNotExists(list, "import " + project.getBasePackage() + "." + getReferences().toLowerCase().concat(".").concat(getReferences()));

                }
            }
        } else if (dataType.get().equalsIgnoreCase("String")) {
            addIfNotExists(list, "import jakarta.validation.constraints.Size");
            if (key.get().equalsIgnoreCase("Unique")) {

            }

            if (!nullable.get()) {
                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
            }

        } else {
            if (!nullable.get()) {
                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
            }
            if (dataType.get().equalsIgnoreCase("LocalDate")) {
                addIfNotExists(list, "import java.time.LocalDate");
            } else if (dataType.get().equalsIgnoreCase("LocalDateTime")) {
                addIfNotExists(list, "import java.time.LocalDateTime");
            } 
            else if (dataType.get().equalsIgnoreCase("BigDecimal")) {
                addIfNotExists(list, "import java.math.BigDecimal");
            }
            else if (dataType.get().equalsIgnoreCase("Image")) {
                addIfNotExists(list, "import jakarta.persistence.Lob");

            }

        }
        return list;
    }

    public String entityAnnotation(String objectName, String primaryKeyVariableName, boolean consideredReferences) {
        String fieldAnnotation = "";
        if (saburiKey.get().equalsIgnoreCase(Saburikeys.ID_Generator.name())) {
            nullable.set(false);
        }
        String column = "";
        if (key.get().equalsIgnoreCase(keys.Primary.name())) {
            if (isReference()) {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
                if (enumerated.get()) {
                    fieldAnnotation += "@Id\n";
                    fieldAnnotation += "@Size(max =  " + getSize() + ", message =  \"The field: " + getCaption() + " size cannot be greater than " + getSize() + "\")\n";
                    fieldAnnotation += "@Enumerated(EnumType.STRING)\n";
                    fieldAnnotation += "@Column(length = " + getSize() + ")";

                } else {
                    if (consideredReferences) {
                        fieldAnnotation += "@MapsId(\"" + getReferences().concat("Id") + "\")\n"
                                + "    @OneToOne\n"
                                + "    @JoinColumn(name = \"" + objectName.concat("Id") + "\", referencedColumnName = \"" + getReferences().concat("ID") + "\", insertable = true)";
                    }
                }
            } else {
                fieldAnnotation += "@Id\n";
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
                if (getDataType().equalsIgnoreCase("String")) {
                    fieldAnnotation += "@Size(max =  " + getSize() + ", message =  \"The field: " + getCaption() + " size cannot be greater than " + getSize() + "\")\n";
                    column += "@Column(length = " + getSize() + ", updatable = false)";
                } else {
                    column += "@Column(updatable = false)";
                }
            }
        } else if (key.get().equalsIgnoreCase(keys.Primary_Auto.name())) {
            fieldAnnotation += "@Id\n"
                    + "    @GeneratedValue(strategy = GenerationType.AUTO)\n"
                    + "    @Column(updatable = false, nullable = false)\n";
        } else if (isCollection()) {

            if (mapping.get().isBlank()) {
                mapping.set("OneToMany");
            }
            fieldAnnotation += "@Builder.Default\n";
            fieldAnnotation += "@" + mapping.get() + "(cascade = CascadeType.MERGE, mappedBy = \"" + Utilities.getVariableName(objectName) + "\")\n";
            if (isReference()) {
                fieldAnnotation += "@JsonIgnoreProperties(\"" + Utilities.getVariableName(objectName) + "\")";
//                fieldAnnotation += "@JoinTable(name = \"" + objectName + getFieldName() + "\",\n"
//                        + "            joinColumns = {\n"
//                        + "                @JoinColumn(name = \"" + primaryKeyVariableName + "\", nullable = false)},\n"
//                        + "            inverseJoinColumns = {\n"
//                        + "                @JoinColumn(name = \"" + Utilities.getVariableName(getReferences()) + "ID\", nullable = false)})";
            }

        } else if (isReference()
                && !isCollection()) {
            if (!nullable.get()) {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
            }
            if (enumerated.get()) {
                fieldAnnotation += "@Enumerated(EnumType.STRING)\n";
                fieldAnnotation += "@Column(length = " + getSize() + ")";

            } else {
                if (consideredReferences) {
                    if (mapping.get().isBlank()) {
                        mapping.set("OneToOne");
                    }

                    if (key.get().isBlank()) {
                        key.set("Foreign");

                    }

                    column = "@JoinColumn(name = \"" + variableName.concat("Id") + "\"";
                    column += ",foreignKey = @ForeignKey(name = \"fk" + fieldName.get().concat("Id") + objectName + "\")";

                    if (key.get().equalsIgnoreCase("Unique")) {
                        column += ",unique = true";
                    }
                    column += ")";
                    fieldAnnotation += "@" + mapping.get() + "\n";
                }

            }
        } else if (saburiKey.get().equalsIgnoreCase(Saburikeys.ID_Helper.name())) {
            column = "@Column(updatable = false)";
        } else if (dataType.get().equalsIgnoreCase("String")) {
            fieldAnnotation += "@Size(max =  " + getSize() + ", message =  \"The field: " + getCaption() + " size cannot be greater than " + getSize() + "\")\n";

            column += "@Column(";
            if (!isReference()) {
                column += "length =  " + getSize();
            }

            if (key.get().equalsIgnoreCase("Unique")) {
                column += ", unique = true";
            }
            column += ")\n";
            if (!nullable.get()) {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
            }

        } else if (dataType.get().equalsIgnoreCase("LocalDate") || dataType.get().equalsIgnoreCase("LocalDateTime")) {

            if (!nullable.get()) {
                column += "@Column(name = \"" + variableName + "\",nullable = false)";
                fieldAnnotation += "@NotNull(message =\"The field: " + getCaption() + " cannot be null\")\n";
            }
        } else if (dataType.get().equalsIgnoreCase("int") || dataType.get().equalsIgnoreCase("Integer")
                || dataType.get().equalsIgnoreCase("float") || dataType.get().equalsIgnoreCase("double")) {
            column += "@Column(name = \"" + variableName + "\"";
            if (!nullable.get()) {
                column += ",nullable = false";
                fieldAnnotation += "@NotNull(message =\"The field: " + getCaption() + " cannot be null\")\n";
            }
            if (getKey().equalsIgnoreCase("Unique")) {
                column += ",unique = false";
                fieldAnnotation += "@NotNull(message =\"The field: " + getCaption() + " cannot be null\")\n";
            }
            column += ")";
        } else if (dataType.get().equalsIgnoreCase("Image")) {
            fieldAnnotation += "@Lob\n";

            if (!nullable.get()) {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
            }

        }

        fieldAnnotation += column;
        return fieldAnnotation;
    }

    public String requestAnnotation(String objectName, String primaryKey, boolean consideredReferences) {
        String fieldAnnotation = "";
        if (saburiKey.get().equalsIgnoreCase(Saburikeys.ID_Generator.name())) {
            nullable.set(false);
        }

        if (getKey().equalsIgnoreCase(keys.Primary.name())) {
            if (isReference()) {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";

            } else {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
                if (getDataType().equalsIgnoreCase("String")) {
                    fieldAnnotation += "@Size(max =  " + getSize() + ", message =  \"The field: " + getCaption() + " size cannot be greater than " + getSize() + "\")\n";

                } else {

                }
            }
        } else if (getKey().equalsIgnoreCase(keys.Primary_Auto.name())) {

        } else if (isCollection()) {
            fieldAnnotation += "@Builder.Default ";

        } else if (isReference()
                && !isCollection()) {
            if (!nullable.get()) {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
            }

        } else if (saburiKey.get().equalsIgnoreCase(Saburikeys.ID_Helper.name())) {

        } else if (dataType.get().equalsIgnoreCase("String")) {
            fieldAnnotation += "@Size(max =  " + getSize() + ", message =  \"The field: " + getCaption() + " size cannot be greater than " + getSize() + "\")\n";

            if (!nullable.get()) {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
            }

        } else if (dataType.get().equalsIgnoreCase("LocalDate") || dataType.get().equalsIgnoreCase("LocalDateTime")) {

            if (!nullable.get()) {

                fieldAnnotation += "@NotNull(message =\"The field: " + getCaption() + " cannot be null\")\n";
            }
        } else if (dataType.get().equalsIgnoreCase("int") || dataType.get().equalsIgnoreCase("Integer")
                || dataType.get().equalsIgnoreCase("float") || dataType.get().equalsIgnoreCase("double")) {

            if (!nullable.get()) {
                fieldAnnotation += "@NotNull(message =\"The field: " + getCaption() + " cannot be null\")\n";
            }
            if (getKey().equalsIgnoreCase(keys.Unique.name())) {
                fieldAnnotation += "@NotNull(message =\"The field: " + getCaption() + " cannot be null\")\n";
            }

        } else if (dataType.get().equalsIgnoreCase("Image")) {

            if (!nullable.get()) {
                fieldAnnotation += "@NotNull(message =  \"The field: " + getCaption() + " cannot be null\")\n";
            }

        }

        return fieldAnnotation;
    }

    public List miniImports(Project currentProject) throws Exception {
        List list = new ArrayList();
        if (getProjectName().isBlank()) {
            project = currentProject;
        }

        this.commonProject = oProjectDAO.get(project.getCommonProjectName());

        if (isCollection()) {
            addIfNotExists(list, "import java.util." + getDataType());
            if (getDataType().equalsIgnoreCase("List")) {
                addIfNotExists(list, "import java.util.ArrayList");
            } else if (getDataType().equalsIgnoreCase("Set")) {
                addIfNotExists(list, "import java.util.HashSet");
            }

        } else if (isReference()) {

            if (enumerated.get()) {
                addIfNotExists(list, "import " + project.getUtilPackage() + "." + project.getEnumClass() + "." + references.get());

            } else {
                if (project.getProjectName().equalsIgnoreCase(currentProject.getProjectName())) {
                    addIfNotExists(list, "import " + project.getBasePackage() + "." + getReferences().toLowerCase().concat(".").concat(getReferences()));
                }

            }
        } else {

            if (dataType.get().equalsIgnoreCase("LocalDate")) {
                addIfNotExists(list, "import java.time.LocalDate");
            } else if (dataType.get().equalsIgnoreCase("LocalDateTime")) {
                addIfNotExists(list, "import java.time.LocalDateTime");
            }

        }
        return list;
    }

    public List daImports(Project currentProject) throws Exception {
        List list = new ArrayList();
        if (getProjectName().isBlank()) {
            project = currentProject;
        }
        this.commonProject = oProjectDAO.get(project.getCommonProjectName());

        if (!currentProject.getProjectName().equalsIgnoreCase(commonProject.getProjectName()) && isHelper()) {
            addIfNotExists(list, "import " + commonProject.getDBAccessPackage() + ".IDGeneratorDA");
        }
        if (isCollection()) {
            if (isReference()) {
                if (enumerated.get()) {
                    addIfNotExists(list, "import " + project.getUtilPackage() + "." + project.getEnumClass() + "." + references.get());
                } else {
                    if (getReferences().equalsIgnoreCase("LookupData")) {
                        addIfNotExists(list, "import " + commonProject.getEntityPackage() + "." + references.get());
                        if (!commonProject.getProjectName().equalsIgnoreCase(currentProject.getProjectName())) {
                            addIfNotExists(list, "import " + commonProject.getDBAccessPackage() + "." + references.get() + "DA");
                        }
                    } else {

                        addIfNotExists(list, "import " + project.getEntityPackage() + "." + references.get());
                        if (project.getProjectName().equalsIgnoreCase(currentProject.getProjectName())) {
                            addIfNotExists(list, "import " + project.getDBAccessPackage() + "." + references.get() + "DA");
                        }
                    }

                }
            }
            if (getDataType().equalsIgnoreCase("Set")) {
                addIfNotExists(list, "import java.util.HashSet");
            }
        } else if (isReference()) {
            if (isReference()) {
                if (enumerated.get()) {

                    addIfNotExists(list, "import " + project.getUtilPackage() + "." + project.getEnumClass() + "." + references.get());

                } else {
                    if (getReferences().equalsIgnoreCase("LookupData")) {
                        addIfNotExists(list, "import " + commonProject.getEntityPackage() + "." + references.get());
                        if (!currentProject.getProjectName().equalsIgnoreCase(commonProject.getProjectName())) {
                            addIfNotExists(list, "import " + commonProject.getDBAccessPackage() + "." + references.get() + "DA");
                        }
                    } else {

                        addIfNotExists(list, "import " + project.getEntityPackage() + "." + references.get());
                        if (!currentProject.getProjectName().equalsIgnoreCase(project.getProjectName())) {
                            addIfNotExists(list, "import " + project.getDBAccessPackage() + "." + references.get() + "DA");
                        }
                    }
                }
            }
        } else if (dataType.get().equalsIgnoreCase("LocalDate")) {
            addIfNotExists(list, "import java.time.LocalDate");
            addIfNotExists(list, "import static " + commonProject.getUtilPackage() + ".Utilities.formatDate");
        } else if (dataType.get().equalsIgnoreCase("LocalDateTime")) {
            addIfNotExists(list, "import java.time.LocalDateTime");
            addIfNotExists(list, "import static " + commonProject.getUtilPackage() + ".Utilities.formatDateTime");

        } else if (dataType.get().equalsIgnoreCase("double") || dataType.get().equalsIgnoreCase("float")) {
            addIfNotExists(list, "import static " + commonProject.getUtilPackage() + ".Utilities.formatNumber");

        } else if (dataType.get().equalsIgnoreCase("int") || dataType.get().equalsIgnoreCase("Integer")) {
            addIfNotExists(list, "import static " + commonProject.getUtilPackage() + ".Utilities.formatInteger");

        } else if (dataType.get().equalsIgnoreCase("Image")) {
            addIfNotExists(list, "import javafx.scene.image.ImageView");
            addIfNotExists(list, "import " + commonProject.getUtilPackage() + ".FXUIUtils");

        }

        return list;
    }

    public String getControlName() {
        UIControls controlType = this.getControlType();
        String prefix;
        prefix = switch (controlType) {
            case DatePicker ->
                "dtp";
            case CheckBox ->
                "chk";
            case ComboBox ->
                "cbo";
            case Label ->
                "lbl";
            case TextArea ->
                "txa";
            case ImageView ->
                "imv";
            case FileBrowser ->
                "txa";
            case TableView ->
                "tbl";
            default ->
                "txt";
        };

        return prefix.concat(this.getFieldName());
    }

    public String getUsableDataType(boolean forceReferences) {
        if (isCollection()) {
            return getDataType() + "<" + getReferences() + ">";
        } else if (forceReferences && isReference()) {
            return this.references.get();
        } else if (enumerated.get()) {
            return references.get();
        } else if (this.getDataType().equalsIgnoreCase("bool")
                || this.getDataType().equalsIgnoreCase("boolean")) {
            return "boolean";
        } else if (this.getDataType().equalsIgnoreCase("Image")) {
            return "byte[]";
        } else {
            return this.getDataType();
        }
    }

//    DB Access specific
    public String daPropertyLine() {
        String type = this.dataType.get();
        if (isCollection()) {
            if (getDataType().equalsIgnoreCase("List")) {
                return " private List<" + getReferences() + "> " + getVariableName() + " = new ArrayList<>();\n";
            } else if (getDataType().equalsIgnoreCase("Set")) {
                return " private Set<" + getReferences() + "> " + getVariableName() + " = new HashSet<>();\n";
            } else {
                return "";
            }
        } else if (isReference()) {
            if (enumerated.get()) {
                return "private final SimpleObjectProperty " + variableName + " =  new SimpleObjectProperty(this,\"" + variableName + "\");\n";
            } else {
                return "private final SimpleStringProperty " + displayVariableName + " =  new SimpleStringProperty(this,\"" + displayVariableName + "\");\n"
                        + "private final SimpleObjectProperty " + referencesVariableID + " =  new SimpleObjectProperty(this,\"" + referencesVariableID + "\");\n"
                        + "private " + references.get() + " " + variableName + ";\n";
            }
        } else {

            if (type.equalsIgnoreCase("Date") || type.equalsIgnoreCase("DateTime")
                    || type.equalsIgnoreCase("LocalDate") || type.equalsIgnoreCase("LocalDateTime")
                    || type.equalsIgnoreCase("Object")) {
                return "private final SimpleObjectProperty " + variableName + " =  new SimpleObjectProperty(this,\"" + variableName + "\");\n"
                        + "private final SimpleStringProperty " + displayVariableName + " =  new SimpleStringProperty(this,\"" + displayVariableName + "\");\n";
            } else if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) {
                return "private final SimpleBooleanProperty " + variableName + " =  new SimpleBooleanProperty(this,\"" + variableName + "\");\n";
            } else if (type.equalsIgnoreCase("String")) {
                return "private final SimpleStringProperty " + variableName + " =  new SimpleStringProperty(this,\"" + variableName + "\");\n";
            } else if (type.equalsIgnoreCase("int")) {
                return "private final SimpleIntegerProperty " + variableName + " =  new SimpleIntegerProperty(this,\"" + variableName + "\");\n"
                        + "private final SimpleStringProperty " + displayVariableName + " =  new SimpleStringProperty(this,\"" + displayVariableName + "\");\n";
            } else if (type.equalsIgnoreCase("float")) {
                return "private final SimpleFloatProperty " + variableName + " =  new SimpleFloatProperty(this,\"" + variableName + "\");\n"
                        + "private final SimpleStringProperty " + displayVariableName + " =  new SimpleStringProperty(this,\"" + displayVariableName + "\");\n";
            } else if (type.equalsIgnoreCase("double")) {
                return "private final SimpleDoubleProperty " + variableName + " =  new SimpleDoubleProperty(this,\"" + variableName + "\");\n"
                        + "private final SimpleStringProperty " + displayVariableName + " =  new SimpleStringProperty(this,\"" + displayVariableName + "\");\n";

            } else if (type.equalsIgnoreCase("Image")) {
                return "private byte[] " + variableName + ";\n"
                        + "private ImageView " + getControlName() + " = new ImageView();\n";
            } else {
                return "private final SimpleStringProperty " + variableName + " =  new SimpleObjectProperty(this,\"" + variableName + "\");\n";
            }
        }
    }

    public String makeSearchColumn() {

        if (this.isHelper() || this.getControlType().equals(UIControls.ImageView) || isCollection()) {
            return "";
        }

        if (this.isReference()) {
            if (this.getEnumerated()) {
                return "this.searchColumns.add(new SearchColumn(\"" + this.getVariableName() + "\", \"" + this.getCaption() + "\", this." + this.getVariableName() + ".get(), " + getToDisplayValue() + this.getSearchDataType() + ", SearchColumn.SearchType.Equal));\n";
            } else {
                return "this.searchColumns.add(new SearchColumn(\"" + this.getReferencesVariableID() + "\", \"" + this.getCaption() + " ID\", this." + getReferencesVariableID() + ".get(), " + getSearchDataType() + ", SearchColumn.SearchType.Equal,false));\n"
                        + "this.searchColumns.add(new SearchColumn(\"" + displayVariableName + "\", \"" + this.getCaption() + "\", this." + displayVariableName + ".get(), " + getSearchDataType() + "));\n";
            }
        } else {
            return "this.searchColumns.add(new SearchColumn(\"" + this.getVariableName() + "\", \"" + this.getCaption() + "\", this." + getVariableName() + ".get(), " + getToDisplayValue() + getSearchDataType() + "));\n";
        }

    }

//    controller Specific
    public String getControlLibary() {
        if (getControlType().equals(UIControls.ImageView)) {
            return "import javafx.scene.image.";
        } else {
            return "import javafx.scene.control.";
        }

    }

    public List<Field> getSubFieldList() throws Exception {
        List<Field> subFieldList = new ArrayList<>();
        if (isCollection()) {
            if (makeEditableTable()) {
                String[] subFieldsArray = getSubFields().split("#");
                for (String st : subFieldsArray) {
                    subFieldList.add(Utilities.getFields(st, ">"));

                }
            }
        }
        return subFieldList;
    }

    public List<FieldDAO> getSubFieldListDAO() throws Exception {
        return getFieldDAOs(getSubFieldList());
    }

    public List ControllerImports(String objectName, Project currentProject) throws Exception {
        String controlLiberay = getControlLibary();

        if (getProjectName().isBlank()) {
            project = currentProject;
        }
        this.commonProject = oProjectDAO.get(project.getCommonProjectName());
        List list = new ArrayList();
        addIfNotExists(list, controlLiberay.concat(this.getControlType().name()));
        if (isCollection()) {
            addIfNotExists(list, "import java.util." + getDataType());
            if (!(field.getReferences().equalsIgnoreCase("LookupData") || getReferences().equalsIgnoreCase(objectName))) {
                addIfNotExists(list, "import " + project.getDBAccessPackage() + "." + getReferencesDA());
            }
            addIfNotExists(list, "import javafx.collections.FXCollections");
            if (makeEditableTable()) {
                addIfNotExists(list, "import " + project.getEntityPackage() + "." + objectName);
                addIfNotExists(list, "import javafx.scene.control.MenuItem");
                addIfNotExists(list, "import javafx.scene.control.TableColumn");
                addIfNotExists(list, "import javafx.scene.control.TablePosition");
                addIfNotExists(list, "import javafx.collections.ObservableList");
                addIfNotExists(list, "import " + commonProject.getUtilPackage() + ".EditCell");
                try {
                    this.getSubFieldListDAO().forEach(d -> {
                        if ((d.getDataType().equalsIgnoreCase("float"))) {
                            addIfNotExists(list, "import static " + commonProject.getUtilPackage() + ".Utilities.defortFloat");
                        }

                        if ((d.getDataType().equalsIgnoreCase("float") || d.getDataType().equalsIgnoreCase("double"))) {
                            addIfNotExists(list, "import static " + commonProject.getUtilPackage() + ".Utilities.defortNumberOptional");
                        }

                        if ((d.getDataType().equalsIgnoreCase("int") || d.getDataType().equalsIgnoreCase("Integer"))) {
                            addIfNotExists(list, "import static " + commonProject.getUtilPackage() + ".Utilities.getInteger");
                        }
                        if (d.getDataType().equalsIgnoreCase("boolean")) {
                            addIfNotExists(list, "import javafx.beans.property.SimpleBooleanProperty");
                            addIfNotExists(list, "import javafx.beans.value.ObservableValue");
                            addIfNotExists(list, "import javafx.scene.control.TableCell");
                            addIfNotExists(list, "import javafx.scene.control.cell.CheckBoxTableCell");
                            addIfNotExists(list, "import javafx.util.Callback");
                        }
                        if (d.isReference() && !getEnumerated()) {
                            if (d.getReferences().equalsIgnoreCase("LookupData")) {
                                addIfNotExists(list, "import " + commonProject.getEntityPackage() + "." + d.getReferences());
                                addIfNotExists(list, "import " + commonProject.getDBAccessPackage() + "." + d.getReferencesDA());

                            } else {
                                if (!(field.getReferences().equalsIgnoreCase("LookupData") || getReferences().equalsIgnoreCase(objectName))) {
                                    addIfNotExists(list, "import " + project.getEntityPackage() + "." + d.getReferences());
                                    addIfNotExists(list, "import " + project.getDBAccessPackage() + "." + d.getReferencesDA());
                                }
                            }
                            addIfNotExists(list, "import java.util.stream.Collectors");
                            addIfNotExists(list, "import javafx.application.Platform");
                        }
                    });
                } catch (Exception ex) {
                    throw ex;
                }
            }
        } else if (isReference()) {
            if (enumerated.get()) {

                addIfNotExists(list, "import " + project.getUtilPackage() + "." + project.getEnumClass() + "." + references.get());

                addIfNotExists(list, "import javafx.collections.FXCollections");

            } else {
                addIfNotExists(list, "import javafx.scene.control.MenuItem");

                if (references.get().equalsIgnoreCase("LookupData")) {
                    addIfNotExists(list, "import " + project.getUtilPackage() + "." + project.getObjectNameClass());
                    addIfNotExists(list, "import  " + commonProject.getEntityPackage() + "." + getReferences());
                } else {
                    if (!getReferences().equalsIgnoreCase(objectName)) {
                        addIfNotExists(list, "import " + project.getDBAccessPackage() + "." + getReferencesDA());
                        addIfNotExists(list, "import  " + project.getEntityPackage() + "." + getReferences());
                    }
                    addIfNotExists(list, "import  " + project.getUtilPackage() + "." + project.getNavigationClass());
                }

            }
        } else if (dataType.get().equalsIgnoreCase("LocalDate")) {
            addIfNotExists(list, "import java.time.LocalDate");
        } else if (dataType.get().equalsIgnoreCase("LocalDateTime")) {
            addIfNotExists(list, "import java.time.LocalDateTime");
        } else if (dataType.get().equalsIgnoreCase("float") || dataType.get().equalsIgnoreCase("double")) {
            addIfNotExists(list, "import static " + commonProject.getUtilPackage() + ".Utilities.formatNumber");
        } else if (dataType.get().equalsIgnoreCase("Image")) {
            addIfNotExists(list, "import javafx.scene.control.Button");
        }

//        ***********************************************************************************************
        if (getSaburiKey().equalsIgnoreCase(Saburikeys.ID_Generator.name())) {
            if (isReference() && !getEnumerated()) {
                addIfNotExists(list, "import " + project.getDBAccessPackage() + "." + getReferencesDA());
            }
        }
        return list;
    }

    public String editTableColumnMethod(FieldDAO field) {
        String type = this.getDataType();
        String body = "";
        if (this.isReference()) {
            if (this.getEnumerated()) {
                body += getColumnName(field.getReferences()) + ".setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(" + getReferences() + ".values())));\n"
                        + "        " + getColumnName(field.getReferences()) + ".setOnEditCommit(event -> {\n"
                        + "            final Object value = event.getNewValue() != null ? event.getNewValue()\n"
                        + "                    : event.getOldValue();\n"
                        + "            \n"
                        + "            ((" + field.getReferencesDA() + ") event.getTableView().getItems()\n"
                        + "                    .get(event.getTablePosition().getRow()))\n"
                        + "                    .set" + getFieldName() + "((" + getReferences() + ")value);\n"
                        + "            " + field.getControlName() + ".refresh();\n"
                        + "            addRowOnce(" + field.getControlName() + ",new " + field.getReferencesDA() + "());\n"
                        + "        });";
            } else {
                body += getColumnName(field.getReferences()) + ".setCellFactory(EditCell." + getDataTypeWrapper() + "TableColumn());";
                body += getColumnName(field.getReferences()) + ".setOnEditCommit(event -> {\n"
                        + "final " + getDataType() + " value = event.getNewValue() != null ? event.getNewValue()\n"
                        + "                    : event.getOldValue();\n"
                        + getReferences() + " " + variableName + " = o" + getReferencesDA() + ".get" + getReferences() + "(value);\n"
                        + "            if (" + variableName + " == null) {\n"
                        + "                Platform.runLater(() -> {\n"
                        + "                    message(\"No Item with Id \" + value + \" found\");\n"
                        + "                    " + field.getControlName() + ".getItems().set(event.getTablePosition().getRow(), new " + field.getReferencesDA() + "());\n"
                        + "                    " + field.getControlName() + ".refresh();\n"
                        + "                });\n"
                        + "                return;\n"
                        + "            }\n"
                        + "\n"
                        + "            if (" + field.getControlName() + ".getItems()\n"
                        + "                    .parallelStream()\n"
                        + "                    .filter((p) -> p.get" + getFieldName() + "ID() != null)\n"
                        + "                    .filter(p -> p.get" + getFieldName() + "ID().equals(value))\n"
                        + "                    .collect(Collectors.toList())\n"
                        + "                    .size() > 1) {\n"
                        + "                Platform.runLater(() -> {\n"
                        + "                    message(\"" + getCaption() + " : \" + " + variableName + ".getDisplayKey() + \" is already selected\");\n"
                        + "                    " + field.getControlName() + ".getItems().set(event.getTablePosition().getRow(), new " + field.getReferencesDA() + "());\n"
                        + "                    " + field.getControlName() + ".refresh();\n"
                        + "                });\n"
                        + "                return;\n"
                        + "\n"
                        + "            }\n"
                        + "            \n"
                        + "            \n"
                        + "            ((" + field.getReferencesDA() + ") event.getTableView().getItems()\n"
                        + "                    .get(event.getTablePosition().getRow()))\n"
                        + "                    .set" + getReferences() + "(" + variableName + ");\n"
                        + "            " + field.getControlName() + ".refresh();\n"
                        + "            addRowOnce(" + field.getControlName() + ", new " + field.getReferencesDA() + "());\n"
                        + "        });";

            }
        } else if (type.equalsIgnoreCase("float")) {
            body += getColumnName(field.getReferences()) + ".setCellFactory(EditCell.StringTableColumn());";
            body += getColumnName(field.getReferences()) + ".setOnEditCommit(event -> {\n"
                    + "            final String value = event.getNewValue() != null ? event.getNewValue()\n"
                    + "                    : event.getOldValue();\n"
                    + "            ((" + field.getReferencesDA() + ") event.getTableView().getItems()\n"
                    + "                    .get(event.getTablePosition().getRow()))\n"
                    + "                    .set" + getFieldName() + "(defortFloat(value));\n"
                    + "            " + field.getControlName() + ".refresh();\n"
                    + "            addRowOnce(" + field.getControlName() + ",new " + field.getReferencesDA() + "());\n"
                    + "        });";
        } else if (type.equalsIgnoreCase("double")) {
            body += getColumnName(field.getReferences()) + ".setCellFactory(EditCell.StringTableColumn());";
            body += getColumnName(field.getReferences()) + ".setOnEditCommit(event -> {\n"
                    + "            final String value = event.getNewValue() != null ? event.getNewValue()\n"
                    + "                    : event.getOldValue();\n"
                    + "            ((" + field.getReferencesDA() + ") event.getTableView().getItems()\n"
                    + "                    .get(event.getTablePosition().getRow()))\n"
                    + "                    .set" + getFieldName() + "(defortNumberOptional(value));\n"
                    + "            " + field.getControlName() + ".refresh();\n"
                    + "            addRowOnce(" + field.getControlName() + ",new " + field.getReferencesDA() + "());\n"
                    + "        });";
        } else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
            body += getColumnName(field.getReferences()) + ".setCellFactory(EditCell.StringTableColumn());";
            body += getColumnName(field.getReferences()) + ".setOnEditCommit(event -> {\n"
                    + "            final String value = event.getNewValue() != null ? event.getNewValue()\n"
                    + "                    : event.getOldValue();\n"
                    + "            ((" + field.getReferencesDA() + ") event.getTableView().getItems()\n"
                    + "                    .get(event.getTablePosition().getRow()))\n"
                    + "                    .set" + getFieldName() + "(getInt(value));\n"
                    + "            " + field.getControlName() + ".refresh();\n"
                    + "            addRowOnce(" + field.getControlName() + ",new " + field.getReferencesDA() + "());\n"
                    + "        });";
        } else {
            body += getColumnName(field.getReferences()) + ".setCellFactory(EditCell." + getDataTypeWrapper() + "TableColumn());";
            body += getColumnName(field.getReferences()) + ".setOnEditCommit(event -> {\n"
                    + "            final " + getDataType() + " value = event.getNewValue() != null ? event.getNewValue()\n"
                    + "                    : event.getOldValue();\n"
                    + "            ((" + field.getReferencesDA() + ") event.getTableView().getItems()\n"
                    + "                    .get(event.getTablePosition().getRow()))\n"
                    + "                    .set" + getFieldName() + "(value);\n"
                    + "            " + field.getControlName() + ".refresh();\n"
                    + "            addRowOnce(" + field.getControlName() + ",new " + field.getReferencesDA() + "());\n"
                    + "        });";
        }

        return Utilities.makeMethod("private", "void", "set" + field.getReferences() + getFieldName(), "", body);

    }

    public String controllerAnnotatedFields() {
        String annotatedField = "";
        if (!isHelper()) {
            annotatedField = "@FXML private " + getControlType().name() + " " + getControlName() + ";\n";
            if (getDataType().equalsIgnoreCase("image")) {
                annotatedField += "@FXML private btnBrowse" + getControlName() + ", btnCapture" + getControlName() + ", btnClear" + getControlName() + "\n";
            }
        }
        return annotatedField;
    }

    public String initialseSavableVariable() {
        if (isHelper()) {
            return "";
        }
        String controlName = this.getControlName();
        if (isCollection()) {

            return "List<" + getReferencesDA() + "> " + variableName + "DAs = " + controlName + ".getItems();\n"
                    + variableName + "DAs.removeIf((p) -> p.get" + getFieldName() + "() == null);\n";

        }
        if (isReference()) {
            if (enumerated.get()) {
                return references.get() + " " + this.variableName + " =  (" + references.get() + ")getSelectedValue(" + controlName + ", \"" + caption.get() + "\");\n";
            }
            return getReferences() + " " + this.variableName + " =(" + getReferences() + ") getEntity(" + controlName + ", \"" + caption.get() + "\");\n";
        }

        if (dataType.get().equalsIgnoreCase("Date") || dataType.get().equalsIgnoreCase("DateTime")
                || dataType.get().equalsIgnoreCase("LocalDate") || dataType.get().equalsIgnoreCase("LocalDateTime")) {
            return "LocalDate " + this.variableName + " = getDate(" + controlName + ", \"" + caption.get() + "\");\n";
        }

        if (dataType.get().equalsIgnoreCase("bool") || dataType.get().equalsIgnoreCase("boolean")) {
            return "boolean " + this.variableName + " = " + controlName + ".isSelected();\n";
        }

        if (dataType.get().equalsIgnoreCase("String")) {
            return "String " + this.variableName + " =  getText(" + controlName + ", \"" + caption.get() + "\");\n";
        }
        if (dataType.get().equalsIgnoreCase("int")) {
            return "int " + this.variableName + " =  getInt(" + controlName + ", \"" + caption.get() + "\");\n";
        }
        if (dataType.get().equalsIgnoreCase("float")) {
            return "float " + this.variableName + " =  getFloat(" + controlName + ", \"" + caption.get() + "\");\n";
        }
        if (dataType.get().equalsIgnoreCase("double")) {
            return "double " + this.variableName + " =  getDouble(" + controlName + ", \"" + caption.get() + "\");\n";
        }

        if (dataType.get().equalsIgnoreCase("Image")) {
            return "byte[] " + this.variableName + " = getBytes(" + controlName + ", \"" + caption.get() + "\");\n";
        } else {
            return "String " + this.variableName + " =  getText(" + controlName + ", \"" + caption.get() + "\");\n";
        }
    }

    public String initialseOptianalSavableVariable() {
        if (isHelper()) {
            return "";
        }
        String controlName = this.getControlName();
        if (isCollection()) {

            return "List<" + getReferencesDA() + "> " + variableName + "DAs = " + controlName + ".getItems();\n"
                    + variableName + "DAs.removeIf((p) -> p.get" + getFieldName() + "() == null);\n";

        }
        if (isReference()) {
            if (enumerated.get()) {
                return references.get() + " " + this.variableName + " =  (" + references.get() + ")getSelectedValue(" + controlName + ", \"" + caption.get() + "\");\n";
            }
            return getReferences() + " " + this.variableName + " =(" + getReferences() + ") getEntity(" + controlName + ");\n";
        }

        if (dataType.get().equalsIgnoreCase("Date") || dataType.get().equalsIgnoreCase("DateTime")
                || dataType.get().equalsIgnoreCase("LocalDate") || dataType.get().equalsIgnoreCase("LocalDateTime")) {
            return "LocalDate " + this.variableName + " = getDate(" + controlName + ");\n";
        }

        if (dataType.get().equalsIgnoreCase("bool") || dataType.get().equalsIgnoreCase("boolean")) {
            return "boolean " + this.variableName + " = " + controlName + ".isSelected();\n";
        }

        if (dataType.get().equalsIgnoreCase("String")) {
            return "String " + this.variableName + " =  getText(" + controlName + ");\n";
        }
        if (dataType.get().equalsIgnoreCase("int")) {
            return "int " + this.variableName + " =  getInt(" + controlName + ");\n";
        }
        if (dataType.get().equalsIgnoreCase("float")) {
            return "float " + this.variableName + " =  getFloat(" + controlName + ");\n";
        }
        if (dataType.get().equalsIgnoreCase("double")) {
            return "double " + this.variableName + " =  getDouble(" + controlName + ");\n";
        }

        if (dataType.get().equalsIgnoreCase("Image")) {
            return "byte[] " + this.variableName + " = getBytes(" + controlName + ");\n";
        } else {
            return "String " + this.variableName + " =  getText(" + controlName + ");\n";
        }
    }

    public String daInitialiseProperties(String objectVariableName) {
        String propertInitialised = "";
        if (isCollection()) {
            propertInitialised = "";
        } else if (isReference()) {
            if (getEnumerated()) {
                propertInitialised = "this." + variableName + ".set(" + objectVariableName + "." + getCall() + ");\n";
            } else {
                propertInitialised = "this." + variableName + "= " + objectVariableName + "." + getCall() + ";\n";
                propertInitialised += "if(this." + variableName + "!= null){";
                propertInitialised += "this." + getReferencesVariableID() + ".set(" + variableName + ".getId());\n";
                propertInitialised += "this." + displayVariableName + ".set(" + variableName + ".getDisplayKey());\n}\n";
            }
        } else if (dataType.get().equalsIgnoreCase("Image")) {
            propertInitialised += "this." + variableName + " = " + objectVariableName + "." + getCall() + ";\n"
                    + "this." + getControlName() + " = FXUIUtils.setTableSizeImage(" + getControlName() + ", " + objectVariableName + "." + getCall() + ");";
        } else {
            propertInitialised = "this." + variableName + ".set(" + objectVariableName + "." + getCall() + ");\n";
        }

        if (getDataType().equalsIgnoreCase("float") || getDataType().equalsIgnoreCase("double")) {
            propertInitialised += "this." + displayVariableName + ".set(formatNumber(" + objectVariableName + "." + getCall() + "));\n";

        }
        if (getDataType().equalsIgnoreCase("int") || getDataType().equalsIgnoreCase("Integer")) {
            propertInitialised += "this." + displayVariableName + ".set(formatInteger(" + objectVariableName + "." + getCall() + "));\n";

        } else if (dataType.get().equalsIgnoreCase("Date") || dataType.get().equalsIgnoreCase("LocalDate")) {
            propertInitialised += "this." + displayVariableName + ".set(formatDate(" + objectVariableName + "." + getCall() + "));\n";
        } else if (dataType.get().equalsIgnoreCase("DateTime") || dataType.get().equalsIgnoreCase("LocalDateTime")) {
            propertInitialised += "this." + displayVariableName + ".set(formatDateTime(" + objectVariableName + "." + getCall() + "));\n";
        }
        return propertInitialised;
    }

    private String daProperty() {
        String type = this.getDataType();
        if (type.equalsIgnoreCase("Date") || type.equalsIgnoreCase("DateTime")
                || type.equalsIgnoreCase("LocalDate") || type.equalsIgnoreCase("LocalDateTime") || type.equalsIgnoreCase("Object")) {
            return "SimpleObjectProperty ";
        } else if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) {
            return "SimpleBooleanProperty ";
        } else if (type.equalsIgnoreCase("String")) {
            return "SimpleStringProperty ";
        } else if (type.equalsIgnoreCase("int")) {
            return "SimpleIntegerProperty ";
        } else if (type.equalsIgnoreCase("float")) {
            return "SimpleFloatProperty ";
        } else if (type.equalsIgnoreCase("double")) {
            return "SimpleDoubleProperty ";
        } else {
            return "SimpleStringProperty ";
        }
    }

    public String daProperty(String type) {
        if (type.equalsIgnoreCase("Date") || type.equalsIgnoreCase("DateTime")
                || type.equalsIgnoreCase("LocalDate") || type.equalsIgnoreCase("LocalDateTime") || type.equalsIgnoreCase("Object")) {
            return "SimpleObjectProperty ";
        } else if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) {
            return "SimpleBooleanProperty ";
        } else if (type.equalsIgnoreCase("String")) {
            return "SimpleStringProperty ";
        } else if (type.equalsIgnoreCase("int")) {
            return "SimpleIntegerProperty ";
        } else if (type.equalsIgnoreCase("float")) {
            return "SimpleFloatProperty ";
        } else if (type.equalsIgnoreCase("double")) {
            return "SimpleDoubleProperty ";
        } else {
            return "SimpleStringProperty ";
        }
    }

    public String afterClearing() {
        if (isHelper()) {
            return "";
        }
        switch (getControlType()) {

            case TableView:

                return "addRow(" + getControlName() + ", new " + getReferencesDA() + "());\n";
            default:
                return "";
        }
    }

    public String setControlText(String value) {
        if (isHelper()) {
            return "";
        }
        switch (getControlType()) {
            case ComboBox -> {
                return getControlName() + ".setValue(" + value + ");\n";
            }
            case DatePicker -> {
                return getControlName() + ".setValue((" + getDataType() + ")" + value + ");\n";
            }
            case CheckBox -> {
                return getControlName() + ".setSelected(" + value + ");\n";
            }
            case ImageView -> {
                return "setImage(" + getControlName() + ", " + value + ");\n";
            }
            default -> {
                if (getDataType().equalsIgnoreCase("int") || getDataType().equalsIgnoreCase("Integer")) {
                    return getControlName() + ".setText(String.valueOf(" + value + "));\n";
                } else if (getDataType().equalsIgnoreCase("float") || getDataType().equalsIgnoreCase("double")) {
                    return getControlName() + ".setText(formatNumber(" + value + "));\n";
                } else {
                    return getControlName() + ".setText(" + value + ");\n";
                }
            }
        }
    }

    public String makeUIFXMLEditLine(String id, int rowIndex, int columnIndex) {
        if (isPrimaryKey() || isPrimaryKeyAuto()) {
            return makePrimaryKeyControl(id, rowIndex, columnIndex);
        }
        if (isHelper()) {
            return "";
        }
        String line = " <Label id=\"" + id + "\" fx:id=\"lbl" + getFieldName() + "\" "
                + "minWidth=\"100\" text=\"" + getCaption() + "\" GridPane.columnIndex=\"" + columnIndex + "\" GridPane.rowIndex=\"" + rowIndex + "\" />\n";

        line += "<" + getControlType() + " fx:id = \"" + getControlName() + "\" id = \"" + id + "\"  GridPane.rowIndex = \"" + rowIndex + "\" "
                + "GridPane.columnIndex = \"" + (columnIndex + 1) + "\" ";

        switch (getControlType()) {
            case DatePicker:
                line += "minWidth=\"185.0\"/>";
                break;
            case CheckBox:
                line += "/>";
                break;
            case ComboBox:
                line += "promptText = \"Select " + getCaption() + "\" minWidth=\"185.0\"";
                if (getEnumerated()) {
                    line += "/>";
                } else {
                    line += ">\n<contextMenu>\n"
                            + "  <ContextMenu fx:id =\"cmuSelect" + getFieldName() + "\" id = \"" + id + "\">\n"
                            + " <items>\n"
                            + "<MenuItem mnemonicParsing=\"false\" fx:id =\"cmiSelect" + getFieldName() + "\" id = \"" + id + "\" text=\"Select " + getCaption() + "\" />\n"
                            + "   </items>\n"
                            + "</ContextMenu>\n"
                            + " </contextMenu>\n"
                            + "</" + getControlType() + "> ";
                }
                break;
            case ImageView:
                line = " <Label id=\"" + id + "\" fx:id=\"lbl" + getFieldName() + "\" "
                        + "minWidth=\"100\" text=\"" + getCaption() + "\" GridPane.columnIndex=\"" + columnIndex + "\" GridPane.rowIndex=\"" + rowIndex + "\" />\n";
                line += " <HBox alignment=\"center\" spacing=\"4\" GridPane.columnIndex=\"" + (columnIndex + 1) + "\" GridPane.rowIndex=\"" + rowIndex + "\""
                        + " fx:id=\"hbx" + getFieldName() + "\" id=\"" + id + "\">\n"
                        + "               \n"
                        + "               <VBox alignment=\"center\" prefHeight=\"150.0\" prefWidth=\"150.0\" styleClass=\"image-pane\" "
                        + "fx:id=\"vbx" + getFieldName() + "\" id=\"" + id + "\">\n"
                        + "                    <ImageView id=\"" + id + "\" fx:id=\"" + getControlName() + "\" fitHeight=\"100\" fitWidth=\"100.0\" pickOnBounds=\"true\" preserveRatio=\"true\" style=\"-fx-opacity: 45;\" />\n"
                        + "                \n"
                        + "                </VBox>\n"
                        + "                <VBox alignment=\"center\" spacing=\"10\" styleClass=\"image-buttons\">\n"
                        + "                    <Button fx:id=\"btnBrowse" + getFieldName() + "\" id=\"" + id + "\" minWidth=\"60\" text=\"Browse\" /> \n"
                        + "                    <Button fx:id=\"btnCapture" + getFieldName() + "\" id=\"" + id + "\" minWidth=\"60\" text=\"Capture\" />\n"
                        + "                    <Button fx:id=\"btnClear" + getFieldName() + "\" id=\"" + id + "\" minWidth=\"60\" text=\"Clear\" /> \n"
                        + "                </VBox>\n"
                        + "            </HBox>";
                break;
            case FileBrowser:
                line = " <Label id=\"" + id + "\" fx:id=\"lbl" + getFieldName() + "\" "
                        + "minWidth=\"100\" text=\"" + getCaption() + "\" GridPane.columnIndex=\"" + columnIndex + "\" GridPane.rowIndex=\"" + rowIndex + "\" />\n";
                line += "  <HBox spacing=\"2\" GridPane.columnIndex=\"" + (columnIndex + 1) + "\" GridPane.rowIndex=\"" + rowIndex + "\">\n"
                        + "                <TextArea fx:id = \"" + getControlName() + "\" id = \"Project\"  prefWidth=\"250\" prefHeight=\"50\" promptText = \"Enter " + getCaption() + "\"/> \n"
                        + "            <Button fx:id=\"btn" + getFieldName() + "\" prefWidth=\"60.0\" text=\"Browse\" />\n"
                        + "            </HBox>";
                break;
            case Label:
                line += " text = \"" + getCaption() + "\"/>";
                break;
            case TextField:
                line += "minWidth=\"100\" promptText = \"Enter " + getCaption() + "\"/>";
                break;
            case TextArea:
                line += "prefWidth=\"100\" prefHeight=\"50\" promptText = \"Enter " + getCaption() + "\"/>";
                break;
            case TableView:
                return "";

            default:
                line += "minWidth=\"100\" promptText = \"Enter " + getCaption() + "\"/>";
                break;
        }

        return line;
    }

    public String makePrimaryKeyControl(String id, int rowIndex, int columnIndex) {
        if (isHelper()) {
            return "";
        }
        String line = " <Label id=\"" + id + "\" fx:id=\"lbl" + getFieldName() + "\" "
                + "minWidth=\"100\" text=\"" + getCaption() + "\" GridPane.columnIndex=\"" + columnIndex + "\" GridPane.rowIndex=\"" + rowIndex + "\" />\n";

        line += "<" + getControlType() + " fx:id = \"" + getControlName() + "\" id = \"" + id + "\"  GridPane.rowIndex = \"" + rowIndex + "\" "
                + "GridPane.columnIndex = \"" + (columnIndex + 1) + "\" ";

        switch (getControlType()) {
            case DatePicker ->
                line += "minWidth=\"185.0\"/>";
            case CheckBox ->
                line += "/>";
            case ComboBox -> {
                line += "promptText = \"Select " + getCaption() + "\" minWidth=\"185.0\"";
                line += ">\n<contextMenu>\n"
                        + "  <ContextMenu fx:id =\"cmuSelect" + getFieldName() + "\" id = \"" + id + "\">\n"
                        + " <items>\n"
                        + "<MenuItem mnemonicParsing=\"false\" fx:id =\"cmiLoad\" id = \"" + id + "\" text=\"Load\" />"
                        + "<MenuItem mnemonicParsing=\"false\" fx:id =\"cmiSelect" + getFieldName() + "\" id = \"" + id + "\" text=\"Select " + getCaption() + "\" />\n"
                        + "   </items>\n"
                        + "</ContextMenu>\n"
                        + " </contextMenu>\n"
                        + "</" + getControlType() + "> ";
            }
            case ImageView -> {
                line = " <Label id=\"" + id + "\" fx:id=\"lbl" + getFieldName() + "\" "
                        + "minWidth=\"100\" text=\"" + getCaption() + "\" GridPane.columnIndex=\"" + columnIndex + "\" GridPane.rowIndex=\"" + rowIndex + "\" />\n";
                line += " <HBox alignment=\"center\" spacing=\"10\" GridPane.columnIndex=\"" + (columnIndex + 1) + "\" GridPane.rowIndex=\"" + rowIndex + "\""
                        + " fx:id=\"hbx" + getFieldName() + "\" id=\"" + id + "\">\n"
                        + "               \n"
                        + "               <VBox alignment=\"center\" prefHeight=\"150.0\" prefWidth=\"150.0\" styleClass=\"image-pane\" "
                        + "fx:id=\"vbx" + getFieldName() + "\" id=\"" + id + "\">\n"
                        + "                    <ImageView id=\"" + id + "\" fx:id=\"" + getControlName() + "\" fitHeight=\"150\" fitWidth=\"150.0\" pickOnBounds=\"true\" preserveRatio=\"true\" style=\"-fx-opacity: 45;\" />\n"
                        + "                \n"
                        + "                </VBox>\n"
                        + "                <VBox alignment=\"center\" spacing=\"10\">\n"
                        + "                    <Button fx:id=\"btnBrowse" + getFieldName() + "\" id=\"" + id + "\" minWidth=\"100\" text=\"Browse\" /> \n"
                        + "                    <Button fx:id=\"btnCapture" + getFieldName() + "\" id=\"" + id + "\" minWidth=\"100\" text=\"Capture\" />\n"
                        + "                    <Button fx:id=\"btnClear" + getFieldName() + "\" id=\"" + id + "\" minWidth=\"100\" text=\"Clear\" /> \n"
                        + "                </VBox>\n"
                        + "            </HBox>";
            }
            case Label ->
                line += " minWidth=\"100\"  text = \"" + getCaption() + "\"/>";
            case TextField -> {
                line += " minWidth=\"100\" promptText = \"Enter " + getCaption() + "\">";
                line += "\n<contextMenu>\n"
                        + "  <ContextMenu fx:id =\"cmuSelect" + getFieldName() + "\" id = \"" + id + "\">\n"
                        + " <items>\n"
                        + "<MenuItem mnemonicParsing=\"false\" fx:id =\"cmiLoad\" id = \"" + id + "\" text=\"Load\" />"
                        + "   </items>\n"
                        + "</ContextMenu>\n"
                        + " </contextMenu>\n"
                        + "</" + getControlType() + "> ";
            }
            case TextArea -> {
                line += " minWidth=\"100\" promptText = \"Enter " + getCaption() + "\">";
                line += "\n<contextMenu>\n"
                        + "  <ContextMenu fx:id =\"cmuSelect" + getFieldName() + "\" id = \"" + id + "\">\n"
                        + " <items>\n"
                        + "<MenuItem mnemonicParsing=\"false\" fx:id =\"cmiLoad\" id = \"" + id + "\" text=\"Load\" />"
                        + "   </items>\n"
                        + "</ContextMenu>\n"
                        + " </contextMenu>\n"
                        + "</" + getControlType() + "> ";
            }
            case TableView -> {
                return "";
            }

            default -> {
            }
        }

        return line;
    }

    public String NumberValidator() {
        String type = getDataType();
        if (isHelper()) {
            return "";
        }
        if (type.equalsIgnoreCase("int")) {
            return "validateIteger(" + getControlName() + ");\n";
        } else if (type.equalsIgnoreCase("float") || type.equalsIgnoreCase("double")) {
            return "validateNumber(" + getControlName() + ");\n";
        } else {
            return "";
        }
    }

    public String NumberFormatter() {
        String type = getDataType();
        if (isHelper()) {
            return "";
        }
        if (type.equalsIgnoreCase("int")) {
            return "formatInteger(" + getControlName() + ");\n";
        } else if (type.equalsIgnoreCase("float") || type.equalsIgnoreCase("double")) {
            return "formatValue(" + getControlName() + ");\n";
        } else if (type.equalsIgnoreCase("LocalDate") || type.equalsIgnoreCase("LocalDateTime")) {
            return "formatDatePicker(" + getControlName() + ");\n";
        } else {
            return "";
        }
    }

    public String ImageButtonsActions() {
        String type = getDataType();

        if (type.equalsIgnoreCase("Image")) {
            return " btnBrowse" + getFieldName() + ".setOnAction(e -> browseImage(" + getControlName() + "));\n"
                    + "btnCapture" + getFieldName() + ".setOnAction(e->setCapturedImage(" + getControlName() + "));"
                    + "btnClear" + getFieldName() + ".setOnAction(e->" + getControlName() + ".setImage(null));\n";
        } else {
            return "";
        }
    }

    public String annotedImageButtons() {
        String type = getDataType();

        if (type.equalsIgnoreCase("Image")) {
            return "@FXML private Button btnBrowse" + getFieldName() + ", btnCapture" + getFieldName() + ", btnClear" + getFieldName() + ";\n";

        } else {
            return "";
        }
    }

    public String getTableColumn(String objectName, String editable) {
        String tableColumn = "";

        if (!(isHelper() || isCollection())) {
            if (isReference() && !getEnumerated()) {
                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + getReferencesID() + "\" text=\"" + getCaption() + " ID\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"" + getReferencesID() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";

                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + getDisplay() + "\" text=\"" + getCaption() + "\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"" + getDisplay() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";

            } else if (hasDisplay()) {
                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + getFieldName() + "\" text=\"" + getCaption() + "\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"" + getDisplay() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";

            } else if (getDataType().equals("Image")) {
                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + getFieldName() + "\" text=\"" + getCaption() + "\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"Imv" + getFieldName() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";

            } else {
                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + getFieldName() + "\" text=\"" + getCaption() + "\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"" + getFieldName() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";
            }

        }
        return tableColumn;
    }

    public String getTableColumn(String objectName, String custom, String editable) {
        String tableColumn = "";

        if (!(isHelper() || isCollection())) {
            if (isReference() && !getEnumerated()) {
                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + custom + getReferencesID() + "\" text=\"" + getCaption() + " ID\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"" + getReferencesID() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";

                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + custom + getDisplay() + "\" text=\"" + getCaption() + "\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"" + getDisplay() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";

            } else if (hasDisplay()) {
                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + custom + getFieldName() + "\" text=\"" + getCaption() + "\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"" + getDisplay() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";

            } else if (getDataType().equals("Image")) {
                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + custom + getFieldName() + "\" text=\"" + getCaption() + "\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"Imv" + getFieldName() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";

            } else {
                tableColumn += "<TableColumn id=\"" + objectName + "\" fx:id=\"tbc" + custom + getFieldName() + "\" text=\"" + getCaption() + "\" editable=\"" + editable + "\">\n"
                        + "<cellValueFactory><PropertyValueFactory property=\"" + getFieldName() + "\" />\n"
                        + "</cellValueFactory>\n"
                        + "</TableColumn>\n";
            }

        }
        return tableColumn;
    }

    public String makeLoadCollections(Project currentProject) {
        if (!makeEditableTable()) {
            return "";
        }

        if (getProjectName().isBlank()) {
            project = currentProject;
        }
        try {
            this.commonProject = oProjectDAO.get(project.getCommonProjectName());
        } catch (Exception ex) {
            Logger.getLogger(FieldDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String mainClass;
        if (getReferences().equalsIgnoreCase("LookupData")) {
            mainClass = commonProject.getNavigationClass() + ".MAIN_CLASS";
        } else {
            mainClass = project.getNavigationClass() + ".MAIN_CLASS";
        }

        return " private void load" + getFieldName() + "() {\n"
                + "        try {\n"
                + "            ObservableList<" + getReferencesDA() + "> selectedItems = tbl" + getFieldName() + ".getSelectionModel().getSelectedItems();\n"
                + "            if (selectedItems.isEmpty() || selectedItems.size() > 1) {\n"
                + "                return;\n"
                + "            }\n"
                + "            " + getReferencesDA() + " " + getVariableName() + "DA = (" + getReferencesDA() + ") getSelectedItem(" + mainClass + ", new " + getReferencesDA() + "(), \"" + getReferences() + "\", \"" + getCaption() + "\", 400, 450, " + getControlName() + ", false);\n"
                + "\n"
                + "            if (" + getVariableName() + "DA == null) {\n"
                + "                return;\n"
                + "            }\n"
                + "            \n"
                + "             if (" + getControlName() + ".getItems().contains(" + getVariableName() + "DA)) {\n"
                + "                    throw new Exception(\"The record with id: \" + " + getVariableName() + "DA.getId() + \" is already selected\");\n"
                + "            }\n"
                + "\n"
                + "            final TablePosition<" + getReferencesDA() + ", String> focusedCell = " + getControlName() + "\n"
                + "                    .focusModelProperty().get().focusedCellProperty().get();\n"
                + "            " + getControlName() + ".getItems().set(focusedCell.getRow(), " + getVariableName() + "DA);\n"
                + "            " + getControlName() + ".refresh();\n"
                + "            addRow(" + getControlName() + ", new " + getReferencesDA() + "());"
                + "\n"
                + "        } catch (Exception e) {\n"
                + "            errorMessage(e);\n"
                + "        }\n"
                + "    }\n"
                + "";

    }
}
