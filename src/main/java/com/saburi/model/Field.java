/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.model;

import com.saburi.utils.Utilities;

/**
 *
 * @author ClinicMaster13
 */
public class Field {

    private String fieldName = "";
    private String caption = "";
    private String dataType = "";
    private String references = "";
    private String mapping = "";
    private String key = "";
    private String saburiKey = "";
    private int size = 100;
    private boolean nullable = false;
    private boolean enumerated = false;
    private String subFields = "";
    private String projectID = "";

    private String variableName;
    private String referencesID;
    private String display;
    private String referencesVariableID;
    private String displayVariableName;
    private final String displayDataType = "String";

    public Field() {
    }

    public Field(String fieldName, String caption, String dataType) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.enumerated = false;
        this.nullable = true;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.references = references;
        this.variableName = Utilities.getVariableName(fieldName);
        this.enumerated = false;
        this.nullable = true;
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references, String subFields) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.subFields = subFields;
        this.references = references;
        this.enumerated = false;
        this.nullable = true;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references, String subFields,
            String mapping) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.references = references;
        this.subFields = subFields;
        this.mapping = mapping;
        this.enumerated = false;
        this.nullable = true;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references, String subFields,
            String mapping, String key) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.references = references;
        this.subFields = subFields;
        this.mapping = mapping;
        this.key = key;
        this.enumerated = false;
        this.nullable = true;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references, String subFields,
            String mapping, String key, String saburiKey) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.references = references;
        this.subFields = subFields;
        this.mapping = mapping;
        this.key = key;
        this.saburiKey = saburiKey;
        this.enumerated = false;
        this.nullable = true;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references, String subFields,
            String mapping, String key, String saburiKey, int size) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.references = references;
        this.subFields = subFields;
        this.mapping = mapping;
        this.key = key;
        this.saburiKey = saburiKey;
        this.size = size;
        this.enumerated = false;
        this.nullable = true;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references, String subFields,
            String mapping, String key, String saburiKey, int size, boolean emumerated) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.references = references;
        this.subFields = subFields;
        this.mapping = mapping;
        this.key = key;
        this.saburiKey = saburiKey;
        this.size = size;
        this.enumerated = emumerated;
        this.nullable = true;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references, String subFields,
            String mapping, String key, String saburiKey, int size, boolean emumerated, boolean nullable) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.references = references;
        this.subFields = subFields;
        this.mapping = mapping;
        this.key = key;
        this.saburiKey = saburiKey;
        this.size = size;
        this.enumerated = emumerated;
        this.nullable = nullable;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public Field(String fieldName, String caption, String dataType, String references, String subFields,
            String mapping, String key, String saburiKey, int size, boolean emumerated, boolean nullable, String projectID) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.dataType = dataType;
        this.references = references;
        this.subFields = subFields;
        this.mapping = mapping;
        this.key = key;
        this.saburiKey = saburiKey;
        this.size = size;
        this.enumerated = emumerated;
        this.nullable = nullable;
        this.projectID = projectID;
        this.variableName = Utilities.getVariableName(fieldName);
        this.referencesID = this.fieldName.concat("ID");
        this.display = this.fieldName.concat("Display");
        this.referencesVariableID = Utilities.getVariableName(referencesID);
        this.displayVariableName = Utilities.getVariableName(display);

    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSaburiKey() {
        return saburiKey;
    }

    public void setSaburiKey(String saburiKey) {
        this.saburiKey = saburiKey;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isEnumerated() {
        return enumerated;
    }

    public void setEnumerated(boolean enumerated) {
        this.enumerated = enumerated;
    }

    public String getSubFields() {
        return subFields;
    }

    public void setSubFields(String subFields) {
        this.subFields = subFields;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getReferencesID() {
        return referencesID;
    }

    public void setReferencesID(String referencesID) {
        this.referencesID = referencesID;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getReferencesVariableID() {
        return referencesVariableID;
    }

    public void setReferencesVariableID(String referencesVariableID) {
        this.referencesVariableID = referencesVariableID;
    }

    public String getDisplayVariableName() {
        return displayVariableName;
    }

    public void setDisplayVariableName(String displayVariableName) {
        this.displayVariableName = displayVariableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Field)) {
            return false;
        }

        Field field = (Field) o;

        return this.getFieldName().equalsIgnoreCase(field.getFieldName());
    }

    @Override
    public int hashCode() {
        return getFieldName().hashCode();

    }

}
