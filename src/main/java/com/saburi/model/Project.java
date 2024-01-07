/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.model;

import com.saburi.utils.Enums.ProjectTypes;
import java.util.Objects;

/**
 *
 * @author Hp
 */
public class Project extends Model {

    private ProjectTypes projectType;
    private String projectName;
    private String basePackage;
    private String baseFolder;
    private String testFolder;

    private String commonProjectName;
    private String entityPackage;
    private String dBAccessPackage;
    private String contollerPackage;
    private String utilPackage;
    private String enumClass;
    private String objectNameClass;
    private String navigationClass;
    private String entityFolder;
    private String dBAcessFolder;
    private String controllerFolder;
    private String resourceFolder;
    private String menuControllerFile;
    private String searchTreeFile;
    private String menuUIFile;
    private String sQLFile;

    public Project() {
    }

    public Project(ProjectTypes projectType, String projectName, String basePackage, String baseFolder, String testFolder,
            String commonProjectName, String entityPackage, String dBAccessPackage, String contollerPackage, String utilPackage,
            String enumClass, String objectNameClass, String navigationClass, String entityFolder, String dBAcessFolder,
            String controllerFolder, String resourceFolder, String menuControllerFile, String searchTreeFile, String menuUIFile, String sQLFile) {
        this.projectType = projectType;
        this.projectName = projectName;
        this.basePackage = basePackage;
        this.baseFolder = baseFolder;
        this.testFolder = testFolder;
        this.commonProjectName = commonProjectName;
        this.entityPackage = entityPackage;
        this.dBAccessPackage = dBAccessPackage;
        this.contollerPackage = contollerPackage;
        this.utilPackage = utilPackage;
        this.enumClass = enumClass;
        this.objectNameClass = objectNameClass;
        this.navigationClass = navigationClass;
        this.entityFolder = entityFolder;
        this.dBAcessFolder = dBAcessFolder;
        this.controllerFolder = controllerFolder;
        this.resourceFolder = resourceFolder;
        this.menuControllerFile = menuControllerFile;
        this.searchTreeFile = searchTreeFile;
        this.menuUIFile = menuUIFile;
        this.sQLFile = sQLFile;

    }

    public ProjectTypes getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectTypes projectType) {
        this.projectType = projectType;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    public String getTestFolder() {
        return testFolder;
    }

    public void setTestFolder(String testFolder) {
        this.testFolder = testFolder;
    }
    
    

    public String getdBAccessPackage() {
        return dBAccessPackage;
    }

    public void setdBAccessPackage(String dBAccessPackage) {
        this.dBAccessPackage = dBAccessPackage;
    }

    public String getdBAcessFolder() {
        return dBAcessFolder;
    }

    public void setdBAcessFolder(String dBAcessFolder) {
        this.dBAcessFolder = dBAcessFolder;
    }

    public String getsQLFile() {
        return sQLFile;
    }

    public void setsQLFile(String sQLFile) {
        this.sQLFile = sQLFile;
    }

    public String getProjectIDDisplay() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCommonProjectName() {
        return commonProjectName;
    }

    public void setCommonProjectID(String commonProjectName) {
        this.commonProjectName = commonProjectName;
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

    public String getNavigationClass() {
        return navigationClass;
    }

    public void setNavigationClass(String navigationClass) {
        this.navigationClass = navigationClass;
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

    public String getControllerFolder() {
        return controllerFolder;
    }

    public void setControllerFolder(String controllerFolder) {
        this.controllerFolder = controllerFolder;
    }

    public String getMenuControllerFile() {
        return menuControllerFile;
    }

    public void setMenuControllerFile(String menuControllerFile) {
        this.menuControllerFile = menuControllerFile;
    }

    public String getSearchTreeFile() {
        return searchTreeFile;
    }

    public void setSearchTreeFile(String searchTreeFile) {
        this.searchTreeFile = searchTreeFile;
    }

    public String getMenuUIFile() {
        return menuUIFile;
    }

    public void setMenuUIFile(String menuUIFile) {
        this.menuUIFile = menuUIFile;
    }

    public String getSQLFile() {
        return sQLFile;
    }

    public void setSQLFile(String sQLFile) {
        this.sQLFile = sQLFile;
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

        return this.getProjectName().equalsIgnoreCase(project.getProjectName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.projectName);

    }

    @Override
    public Object getId() {
        return this.projectName;
    }

    @Override
    public String getDisplay() {
        return this.projectName;
    }

}
