/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Field;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author CLINICMASTER13
 */
public class CodeGenerator {

    protected List<FieldDAO> defaulFields = Arrays.asList(new FieldDAO(new Field("UserID", "User ID", "String")),
            new FieldDAO(new Field("UserFullName", "User Full Name", "String")),
            new FieldDAO(new Field("ClientMachine", "Client Machine", "String")),
            new FieldDAO(new Field("RecordDateTime", "Record Date Time", "LocalDateTime")),
            new FieldDAO(new Field("LastUpdateDateTime", "Last Update", "LocalDateTime")));

    public boolean validate(List<FieldDAO> fields) throws Exception {
        try {

            FilteredList<FieldDAO> primaryKeyList = new FilteredList<>(FXCollections.observableList(fields), e -> true);
            primaryKeyList.setPredicate(FieldPredicates.isPrimaryKey().or(FieldPredicates.isPrimaryKeyAuto()));

            if (fields.isEmpty()) {
                throw new Exception("Must include atleast one item to continue");
            }
            if (primaryKeyList.isEmpty()) {
                throw new Exception("You must enter a primary key");
            }
            if (primaryKeyList.size() > 1) {
                throw new Exception("Composite primary key is not allowed. Please select a primary key");
            }
            
            
            for (FieldDAO pk: primaryKeyList){
                if(pk.isPrimaryKeyAuto()&& !(pk.getDataType().toLowerCase().contains("int"))){
                throw new Exception("Un suppported data type" + pk.getDataType() + " auto generated Id. Please use int instead");
                }
            }

            FilteredList<FieldDAO> displayList = new FilteredList<>(FXCollections.observableList(fields), e -> true);
            displayList.setPredicate(FieldPredicates.isDIsplayKey());
            if (displayList.isEmpty()) {
                throw new Exception("You must enter atleast one display key");
            }

            FilteredList<FieldDAO> helperList = new FilteredList<>(FXCollections.observableList(fields), e -> true);
            helperList.setPredicate(FieldPredicates.isHelper());
            if (!helperList.isEmpty()) {
                if (helperList.size() > 1) {
                    throw new Exception("You cannot have more than 1 ID Helpers");
                } else {
                    FieldDAO idhelperField = helperList.get(0);
                    if (!(idhelperField.getDataType().equalsIgnoreCase("int") || idhelperField.getDataType().equalsIgnoreCase("integer"))) {
                        throw new Exception("Un suppported data type" + idhelperField + " for ID Helper. Please use int instead");
                    }
                }
            }

            FilteredList<FieldDAO> generatorList = new FilteredList<>(FXCollections.observableList(fields), e -> true);
            generatorList.setPredicate(FieldPredicates.isIDGenerator());
            if (!generatorList.isEmpty()) {
                if (generatorList.size() > 1) {
                    throw new Exception("You cannot have more than 1 ID Generators");
                } else {

                    if (helperList.isEmpty()) {
                        throw new Exception("You cannot have ID Generator with ID Helper");
                    }
                }
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

}
