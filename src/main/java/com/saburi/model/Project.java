/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.model;

import java.util.Objects;

/**
 *
 * @author Hp
 */
public class Project extends Model {

    private int projectID;
    private String projectName;
    private int commonProjectID;

    private String entityPackage;

    private String dBAccessPackage;

    private String contollerPackage;

    private String utilPackage;

    private String enumClass;

    private String objectNameClass;

    private String entityFolder;

    private String dBAcessFolder;

    private String controllerFolde;

    private String resourceFolder;

    public Project() {
    }

    public Project(int projectID, String projectName, int commonProjectID, String entityPackage, String dBAccessPackage, String contollerPackage, String utilPackage, String enumClass, String objectNameClass, String entityFolder, String dBAcessFolder, String controllerFolde, String resourceFolder) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.commonProjectID = commonProjectID;
        this.entityPackage = entityPackage;
        this.dBAccessPackage = dBAccessPackage;
        this.contollerPackage = contollerPackage;
        this.utilPackage = utilPackage;
        this.enumClass = enumClass;
        this.objectNameClass = objectNameClass;
        this.entityFolder = entityFolder;
        this.dBAcessFolder = dBAcessFolder;
        this.controllerFolde = controllerFolde;
        this.resourceFolder = resourceFolder;

    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getCommonProjectID() {
        return commonProjectID;
    }

    public void setCommonProjectID(int commonProjectID) {
        this.commonProjectID = commonProjectID;
    }

    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String getDBAccessPackage() {
        return dBAccessPackage;
    }

    public void setDBAccessPackage(String dBAccessPackage) {
        this.dBAccessPackage = dBAccessPackage;
    }

    public String getContollerPackage() {
        return contollerPackage;
    }

    public void setContollerPackage(String contollerPackage) {
        this.contollerPackage = contollerPackage;
    }

    public String getUtilPackage() {
        return utilPackage;
    }

    public void setUtilPackage(String utilPackage) {
        this.utilPackage = utilPackage;
    }

    public String getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(String enumClass) {
        this.enumClass = enumClass;
    }

    public String getObjectNameClass() {
        return objectNameClass;
    }

    public void setObjectNameClass(String objectNameClass) {
        this.objectNameClass = objectNameClass;
    }

    public String getEntityFolder() {
        return entityFolder;
    }

    public void setEntityFolder(String entityFolder) {
        this.entityFolder = entityFolder;
    }

    public String getDBAcessFolder() {
        return dBAcessFolder;
    }

    public void setDBAcessFolder(String dBAcessFolder) {
        this.dBAcessFolder = dBAcessFolder;
    }

    public String getControllerFolde() {
        return controllerFolde;
    }

    public void setControllerFolde(String controllerFolde) {
        this.controllerFolde = controllerFolde;
    }

    public String getResourceFolder() {
        return resourceFolder;
    }

    public void setResourceFolder(String resourceFolder) {
        this.resourceFolder = resourceFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }

        Project project = (Project) o;

        return this.getProjectID() == project.getProjectID();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.projectID);

    }

    @Override
    public Object getId() {
        return this.projectID;
    }

    @Override
    public String getDisplay() {
        return this.projectName;
    }

}
