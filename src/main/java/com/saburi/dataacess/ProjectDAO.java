/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.dataacess;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.saburi.model.Model;
import com.saburi.model.Project;
import com.saburi.utils.SearchColumn;
import com.saburi.utils.SearchColumn.SearchDataTypes;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Hp
 */
public class ProjectDAO extends DataAccess {

    private Project project = new Project();
    private transient final String currentFile = "Projects.json";
    private final SimpleIntegerProperty projectID = new SimpleIntegerProperty(this, "projectID");
    private final SimpleStringProperty projectName = new SimpleStringProperty(this, "projectName");
    private final SimpleIntegerProperty commonProjectID = new SimpleIntegerProperty(this, "commonProjectID");
    private final SimpleStringProperty entityPackage = new SimpleStringProperty(this, "entityPackage");
    private final SimpleStringProperty dBAccessPackage = new SimpleStringProperty(this, "dBAccessPackage");
    private final SimpleStringProperty contollerPackage = new SimpleStringProperty(this, "contollerPackage");
    private final SimpleStringProperty utilPackage = new SimpleStringProperty(this, "utilPackage");
    private final SimpleStringProperty enumClass = new SimpleStringProperty(this, "enumClass");
    private final SimpleStringProperty objectNameClass = new SimpleStringProperty(this, "objectNameClass");
    private final SimpleStringProperty entityFolder = new SimpleStringProperty(this, "entityFolder");
    private final SimpleStringProperty dBAcessFolder = new SimpleStringProperty(this, "dBAcessFolder");
    private final SimpleStringProperty controllerFolde = new SimpleStringProperty(this, "controllerFolde");
    private final SimpleStringProperty resourceFolder = new SimpleStringProperty(this, "resourceFolder");

    public ProjectDAO() {
        this.fileName = super.baseurl.concat(currentFile);
        createSearchColumns();
    }

    public ProjectDAO(Project project) {
        this.fileName = super.baseurl.concat(currentFile);
        this.project = project;
        initialseProprties();
        createSearchColumns();
        this.model = project;
    }

    private void initialseProprties() {
        this.model = project;
        this.projectID.set(project.getProjectID());
        this.projectName.set(project.getProjectName());
        this.commonProjectID.set(project.getCommonProjectID());
        this.entityPackage.set(project.getEntityPackage());
        this.dBAccessPackage.set(project.getDBAccessPackage());
        this.contollerPackage.set(project.getContollerPackage());
        this.utilPackage.set(project.getUtilPackage());
        this.enumClass.set(project.getEnumClass());
        this.objectNameClass.set(project.getObjectNameClass());
        this.entityFolder.set(project.getEntityFolder());
        this.dBAcessFolder.set(project.getDBAcessFolder());
        this.controllerFolde.set(project.getControllerFolde());
        this.resourceFolder.set(project.getResourceFolder());
    }

    private void createSearchColumns() {
        this.searchColumns.add(new SearchColumn("projectID", "Project ID", this.projectID.get(), SearchDataTypes.NUMBER));
        this.searchColumns.add(new SearchColumn("projectName", "Project Name", this.projectName.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("commonProjectID", "Common Project ID", this.commonProjectID.get(), SearchDataTypes.NUMBER));
        this.searchColumns.add(new SearchColumn("entityPackage", "Entity Package", this.entityPackage.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("dBAccessPackage", "DB Access Package", this.dBAccessPackage.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("contollerPackage", "Controller Package", this.contollerPackage.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("utilPackage", "Util Package", this.utilPackage.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("enumClass", "Enum Class", this.enumClass.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("objectNameClass", "Object Name Class", this.objectNameClass.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("entityFolder", "Entity Folder", this.entityFolder.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("dBAcessFolder", "DB Access Folder", this.dBAcessFolder.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("controllerFolde", "Controller Folder", this.controllerFolde.get(), SearchDataTypes.STRING));
        this.searchColumns.add(new SearchColumn("resourceFolder", "Resource Folder", this.resourceFolder.get(), SearchDataTypes.STRING));
    }

    protected List<Project> readJson() throws Exception {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(createFile()));
            return GSON.fromJson(bufferedReader,
                    new TypeToken<List<Project>>() {
                    }.getType());
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            throw e;
        }
    }

    public List<Project> read() throws Exception {
        List<Project> projects = this.readJson();
        return projects != null ? projects : new ArrayList<>();

    }

    public void save() throws IOException, Exception {
        List<? extends Model> projects = read();
        this.saveJson((List<Model>) projects, project);
    }

    public Project getProject() {
        return project;
    }

    @Override
    public List<DataAccess> get() throws Exception {
        try {
            List<DataAccess> dataAccesses = new ArrayList<>();
            this.read().forEach(proj -> dataAccesses.add(new ProjectDAO(proj)));
            return dataAccesses;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void remove(Model model) throws Exception {
        List<Project> projects = this.read();
        projects.remove((Project) model);
        this.saveJson(projects);
    }

}
