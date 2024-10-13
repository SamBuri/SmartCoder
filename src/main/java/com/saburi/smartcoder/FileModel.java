/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Project;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.EntityTypes;
import com.saburi.utils.Enums.ProjectTypes;
import com.saburi.utils.Enums.ServiceTypes;
import com.saburi.utils.Utilities;
import java.util.List;

/**
 *
 * @author samburiima
 */
public class FileModel {
    private String outputFolder;
    private boolean openFile;
    private  String objectName;
    private  List<FieldDAO> fields;
    private  String objectVariableName;
    private  Project project;
    private  Enums.ProjectTypes projectType;
    private String moduleName;
    private String objectCaption;
    private boolean saveToProject;
    private EntityTypes entityType;
    private ServiceTypes serviceType;

    public FileModel(String objectName, List<FieldDAO> fields, Project project, ProjectTypes projectType, 
            String moduleName,String objectCaption,String outputFolder, boolean openFile,boolean saveToProject, 
            EntityTypes entityType, ServiceTypes serviceType) {
        this.objectName = objectName;
        this.fields = fields;
        this.objectVariableName = Utilities.getVariableName(objectName);
        this.project = project;
        this.projectType = projectType;
        this.moduleName = moduleName;
        this.objectCaption = objectCaption;
        this.outputFolder = outputFolder;
        this.openFile = openFile;
        this.saveToProject = saveToProject;
        this.entityType = entityType;
        this.serviceType = serviceType;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public boolean isOpenFile() {
        return openFile;
    }

    public void setOpenFile(boolean openFile) {
        this.openFile = openFile;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<FieldDAO> getFields() {
        return fields;
    }

    public void setFields(List<FieldDAO> fields) {
        this.fields = fields;
    }

    public String getObjectVariableName() {
        return objectVariableName;
    }

    public void setObjectVariableName(String objectVariableName) {
        this.objectVariableName = objectVariableName;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Enums.ProjectTypes getProjectType() {
        return projectType;
    }

    public void setProjectType(Enums.ProjectTypes projectType) {
        this.projectType = projectType;
    }
    
    

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getObjectCaption() {
        return objectCaption;
    }

    public void setObjectCaption(String objectCaption) {
        this.objectCaption = objectCaption;
    }

    public boolean isSaveToProject() {
        return saveToProject;
    }

    public void setSaveToProject(boolean saveToProject) {
        this.saveToProject = saveToProject;
    }

    public EntityTypes getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityTypes entityType) {
        this.entityType = entityType;
    }

    public ServiceTypes getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypes serviceType) {
        this.serviceType = serviceType;
    }

    
    
    

   
    
    
    
}
