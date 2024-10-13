/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.springboot;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.ProjectFile;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.getVariableName;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samburiima
 */
public abstract class SpringbootUtils extends ProjectFile {

    protected String objectName;
    protected String moduleName;
    protected List<FieldDAO> fields;
    protected String objectVariableName;
    protected String controller;
    protected String controllerVariableName;
    protected String service;
    protected String serviceVaribaleName;
    protected String repo;
    protected String repoVariableName;
    protected String mini;
    protected Project project;
    protected  FieldDAO primaryKeyFied;
    protected String primaryKeyVariableName;
    protected  FieldDAO idGenerator;
    protected  boolean hasGenerator;
    protected  String requestObject;
    protected String idDataType;
    protected Enums.EntityTypes entityType;
    private final ProjectDAO oProjectDAO = new ProjectDAO();
    protected Project commonProject;

    public SpringbootUtils(FileModel fileModel) {
        super(fileModel);
        try {
            this.project = fileModel.getProject();
            this.objectName = fileModel.getObjectName();
            this.moduleName = fileModel.getModuleName();
            this.fields = fileModel.getFields().stream().filter(f -> !f.getSaburiKey()
                    .equalsIgnoreCase(Enums.Saburikeys.UI_Only.name())).toList();
            this.repo = this.objectName.concat("Repo");
            this.repoVariableName = getVariableName(repo);
            this.service = objectName.concat("Service");
            this.serviceVaribaleName = getVariableName(service);
            this.mini= this.objectName.concat("Mini");
            this.objectVariableName = getVariableName(this.objectName);
            this.controller = this.objectName.concat("Controller");
            this.controllerVariableName = getVariableName(this.controller);
            
            primaryKeyFied = Utilities.getPrimaryKey(this.fields);
            if (primaryKeyFied != null) {
                this.primaryKeyVariableName = primaryKeyFied.getVariableName();
            }
            
            this.idGenerator = Utilities.getIDGenerator(fields);
            this.hasGenerator = idGenerator != null;
            requestObject = objectName.concat(Enums.WebFiles.Request.name());
            
            this.entityType = fileModel.getEntityType();
            this.idDataType = primaryKeyFied == null ? Utilities.getIdWrapperDataType(entityType) : primaryKeyFied.getDataType();
            this.commonProject = oProjectDAO.get(project.getCommonProjectName());
        } catch (Exception ex) {
            Logger.getLogger(SpringbootUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     protected boolean forceReferences(FieldDAO fieldDAO) {
        String proiectName = fieldDAO.getProjectName();
        return  proiectName.equalsIgnoreCase(this.project.getProjectName()) || isNullOrEmpty(proiectName);
    }

    @Override
    protected String getBaseFolder() {
        return this.project.getBaseFolder();

    }

    @Override
    protected String getFolderName() {
        return this.objectName.toLowerCase();

    }
    
    @Override
    protected String getFileExtension() {
        return "java";
    }

}
