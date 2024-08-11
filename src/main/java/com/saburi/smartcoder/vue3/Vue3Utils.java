/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.vue3;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.ProjectFile;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import java.util.List;

/**
 *
 * @author samburiima
 */
public abstract class Vue3Utils extends ProjectFile{
    
    protected String objectName;
    protected String moduleName;
    protected List<FieldDAO> fields;
    protected String objectVariableName;
    protected String controller;
    protected String controllerVariableName;
    protected String store = "Store";
    protected Project project;

    public Vue3Utils(FileModel fileModel) {
        super(fileModel);
        this.project=fileModel.getProject();
        this.objectName = fileModel.getObjectName();
        this.moduleName = fileModel.getModuleName();
        this.fields = fileModel.getFields().stream().filter(f -> !f.getSaburiKey()
                .equalsIgnoreCase(Enums.Saburikeys.Query_Only.name())).toList();
        this.objectVariableName = Utilities.getVariableName(this.objectName);
        this.controller = this.objectName.concat("Controller");
        this.controllerVariableName = Utilities.getVariableName(this.controller);
    }
    
    public boolean forceReferences(FieldDAO fieldDAO) {
        String proiectName = fieldDAO.getProjectName();
        return fieldDAO.isReferance() && (proiectName.equalsIgnoreCase(this.project.getProjectName()) || isNullOrEmpty(proiectName));
    }

    public String getModuleName(FieldDAO fieldDAO) {
        String module = fieldDAO.getModuleName();
        return isNullOrEmpty(module) ? this.moduleName : module;

    }

    public boolean isSameModule(FieldDAO fieldDAO) {
        return this.moduleName.equalsIgnoreCase(this.getModuleName(fieldDAO));
    }

    public String getReferenceObjectName(FieldDAO fieldDAO) {
        if (!fieldDAO.isReferance()) {
            return "";
        }

        String prefix = fieldDAO.getEnumerated() ? getModuleName(fieldDAO) : fieldDAO.getReferences();
        return prefix.substring(0, 1).toUpperCase() + prefix.substring(1) + store;

    }

    public String getStoreName(FieldDAO fieldDAO) {
        if (!fieldDAO.isReferance()) {
            return "";
        }

        String prefix = fieldDAO.getEnumerated() ? getModuleName(fieldDAO) : fieldDAO.getReferences();
        return prefix.substring(0, 1).toUpperCase() + prefix.substring(1) + store;

    }

    public String getStoreVariableName(FieldDAO fieldDAO) {
        if (!fieldDAO.isReferance()) {
            return "";
        }
        return Utilities.getVariableName(getStoreName(fieldDAO));
    }

    public String defineStoreFunction(FieldDAO fieldDAO) {
        if (!fieldDAO.isReferance()) {
            return "";
        }
        return "define" + getStoreName(fieldDAO);
    }

    private String importPath(FieldDAO fieldDAO) {
        if (!fieldDAO.isReferance()) {
            return "";
        }
        String references = fieldDAO.getReferences();
        String objectFolder = fieldDAO.getEnumerated() ? "" : "/" + references;
        String path = String.format("@/%s%s", getModuleName(fieldDAO), objectFolder);
        System.out.println("Path " + path);
        return path.toLowerCase();

    }

    public String importPath(FieldDAO fieldDAO, String filename) {
        if (!fieldDAO.isReferance()) {
            return "";
        }
        return this.importPath(fieldDAO) + "/" + filename;

    }

    public String predictLookupObjectName(FieldDAO fieldDAO) {
        String references = fieldDAO.getReferences();
        String fieldName = fieldDAO.getFieldName();
        if (fieldDAO.getEnumerated()) {
            return references;
        }
        String lookupName = references.equalsIgnoreCase("LookupData") ? fieldName : references;

        int length = lookupName.length();
        if (lookupName.substring(length - 2, length).equalsIgnoreCase("id")) {
            lookupName = lookupName.substring(0, length - 2);
        }
        return Utilities.toPlural(lookupName);
    }

    public String getReferencingName(FieldDAO fieldDAO) {
        if (!fieldDAO.isReferance()) {
            return "";
        }
        return fieldDAO.getReferences().equalsIgnoreCase("LookupData") ? predictLookupObjectName(fieldDAO)
                : fieldDAO.getEnumerated() ? predictLookupObjectName(fieldDAO) : "Mini";
    }

    public String getStoreAction(FieldDAO fieldDAO) {
        return String.format("get%s();\n", getReferencingName(fieldDAO));
    }

    public String callStoreAction(FieldDAO fieldDAO) {
        return String.format("%s.%s\n", getStoreVariableName(fieldDAO), getStoreAction(fieldDAO));

    }

    public String callStoreDataVaribale(FieldDAO fieldDAO) {
        return String.format("controller.%s.%s", getStoreVariableName(fieldDAO),
                Utilities.getVariableName(getReferencingName(fieldDAO)));
    }

    public String callStoreDataVaribaleLoading(FieldDAO fieldDAO) {
        return String.format("controller.%s.%sLoading", getStoreVariableName(fieldDAO),
                Utilities.getVariableName(getReferencingName(fieldDAO)));
    }
    
    @Override
    protected String getBaseFolder() {
        return this.project.getBaseFolder().toLowerCase();

    }
    

    @Override
    protected String getFolderName() {
        return this.moduleName.concat(FILE_SEPARATOR).concat(objectName).toLowerCase();

    }
        
    
    
    
    

}
