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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    protected FieldDAO primaryKeyFied;
    protected String primaryKeyVariableName;
    protected FieldDAO idGenerator;
    protected boolean hasGenerator;
    protected String requestObject;
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
            this.mini = this.objectName.concat("Mini");
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

    public String referecesImports(FieldDAO fieldDAO) throws Exception {

        if (!fieldDAO.isReference()) {
            return "";
        }

        String references = fieldDAO.getReferences();

        if (fieldDAO.getEnumerated()) {
            String enumPackage = (fieldDAO.referencesIN(project)) ? project.getBasePackage() : commonProject.getBasePackage();
            return "import " + enumPackage + ".enums." + references;

        }
        if (!forceReferences(fieldDAO)) {
            return "";
        }

        return "import " + project.getBasePackage() + "." + references.toLowerCase().concat(".").concat(references);

    }

    public String nullValidationImport(FieldDAO fieldDAO) throws Exception {

        if (fieldDAO.isPrimaryKey() || !fieldDAO.getNullable()) {
            return "import jakarta.validation.constraints.NotNull";
        }

        return "";
    }

    public String sizeValidationImport(FieldDAO fieldDAO) throws Exception {

        if (fieldDAO.isCollection()) {
            return "";
        }
        if (fieldDAO.getUsableDataType(forceReferences(fieldDAO)).equalsIgnoreCase("String")) {
            return "import jakarta.validation.constraints.Size";
        }

        return "";
    }

    public String enumeratedImport(FieldDAO fieldDAO) {
        if (!fieldDAO.getEnumerated()) {
            return "";
        }
        return "import jakarta.persistence.Enumerated;\nimport jakarta.persistence.EnumType";
    }
    
      public String joinColumImport(FieldDAO fieldDAO) {
        if (!fieldDAO.isForeignKey(forceReferences(fieldDAO))) {
            return "";
        }
        return "import jakarta.persistence.JoinColumn;\nimport jakarta.persistence.ForeignKey";
    }

    private String fixMapping(FieldDAO fieldDAO) {
        String mapping = fieldDAO.getMapping();
        if (!fieldDAO.isForeignKey(forceReferences(fieldDAO))) {
            return "";
        }
        if (!isNullOrEmpty(mapping)) {
            return mapping;
        }

        if (fieldDAO.isCollection()) {
            return "OneToMany";
        }
        return "OneToOne";

    }

    public String mappingImports(FieldDAO fieldDAO) throws Exception {

        String mapping = fixMapping(fieldDAO);
        if (isNullOrEmpty(mapping)) {
            return "";
        }
        return "import jakarta.persistence." + mapping;

    }

    public String cascadeImport(FieldDAO fieldDAO) {
        if (fieldDAO.isCollection() && fieldDAO.isForeignKey(forceReferences(fieldDAO))) {
            return "import jakarta.persistence.CascadeType";
        }
        return "";
    }

    public String jacksonImports(FieldDAO fieldDAO) {

        if (!fieldDAO.isCollection()) {
            return "";
        }
        if (fieldDAO.isForeignKey(forceReferences(fieldDAO))) {
            return "import com.fasterxml.jackson.annotation.JsonIgnoreProperties";
        }
        return "";

    }
    
      protected List<String> getImports(FieldDAO fieldDAO) throws Exception {
        return List.of(referecesImports(fieldDAO),
                nullValidationImport(fieldDAO),
               fieldDAO.getDataTypeImps(),
                fieldDAO.getGenericDataTypeImps());
    }

 

    protected String getImports() throws Exception {

        List<String> imports = new ArrayList<>();

        for (FieldDAO fieldDAO : fields) {
            imports.addAll(getImports(fieldDAO));
        }

        return imports.stream().distinct().sorted().filter(f->!isNullOrEmpty(f))
                .map(f -> f + ";\n").collect(Collectors.joining());

    }
    
    
    

    protected boolean forceReferences(FieldDAO fieldDAO) {
        String proiectName = fieldDAO.getProjectName();
        return proiectName.equalsIgnoreCase(this.project.getProjectName()) || isNullOrEmpty(proiectName);
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
