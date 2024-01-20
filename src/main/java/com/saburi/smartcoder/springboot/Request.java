/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.springboot;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.CodeGenerator;
import com.saburi.smartcoder.JavaClass;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.Saburikeys;
import com.saburi.utils.Enums.WebFiles;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.addIfNotExists;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Hp
 */
public class Request extends CodeGenerator {

    private final String objectName;
    private final List<FieldDAO> fields;
    private String primaryKeyVariableName;
    private FieldDAO primaryKeyFied;
    private final Project project;
    ProjectDAO oProjectDAO = new ProjectDAO();

    public Request(String objectName, Project project, List<FieldDAO> fields) {
        this.objectName = objectName;
        this.fields = fields.stream()
                .filter(p -> !(p.getSaburiKey().equalsIgnoreCase(Saburikeys.UI_Only.name())
                || p.getSaburiKey().equalsIgnoreCase(Saburikeys.Query_Only.name())
                || p.getSaburiKey().equalsIgnoreCase(Saburikeys.Read_Only.name())))
                .collect(Collectors.toList());

        if (project.getProjectType().equals(Enums.ProjectTypes.Desktop)) {
            primaryKeyFied = Utilities.getPrimaryKey(fields);
            this.primaryKeyVariableName = primaryKeyFied.getVariableName();
        }
        this.project = project;
    }

    private boolean forceReferences(FieldDAO fieldDAO) {
        String proiectName = fieldDAO.getProjectName();
        return this.project.getProjectType().equals(Enums.ProjectTypes.Springboot_API)
                ? proiectName.equalsIgnoreCase(this.project.getProjectName())
                || isNullOrEmpty(proiectName) : true;
    }

    public List requestImports(Project currentProject, boolean considerReferences, FieldDAO fieldDAO) throws Exception {
        List list = new ArrayList();
//        Project lineProject = fieldDAO.getFieldLineProject(currentProject);

        Project commonProject = oProjectDAO.get(project.getCommonProjectName());
        String references = fieldDAO.getReferences();
        String dataType = fieldDAO.getDataType();
        boolean isNull = fieldDAO.getNullable();
        if (fieldDAO.isPrimaryKey()) {
            addIfNotExists(list, "import jakarta.validation.constraints.NotNull");

        }
        if (fieldDAO.isReferance()) {
            if (!fieldDAO.getNullable()) {
                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
            }

            if (fieldDAO.isCollection()) {
                addIfNotExists(list, "import java.util." + fieldDAO.getDataType());
                if (fieldDAO.getDataType().equalsIgnoreCase("List")) {
                    addIfNotExists(list, "import java.util.ArrayList");
                } else if (fieldDAO.getDataType().equalsIgnoreCase("Set")) {
                    addIfNotExists(list, "import java.util.HashSet");
                }

            }
            if (fieldDAO.getEnumerated()) {
                String enumPackage = (fieldDAO.referencesIN(project)) ? project.getBasePackage() : commonProject.getBasePackage();
                addIfNotExists(list, "import " + enumPackage + ".enums." + fieldDAO.getReferences());

            } else {

//                if (considerReferences) {
//
//                    if (project.getProjectName().equalsIgnoreCase(currentProject.getProjectName())) {
//                        addIfNotExists(list, "import " + project.getBasePackage() + "." + references.toLowerCase().concat(".").concat(references));
//                    }
//
//                }
            }
        } else if (dataType.equalsIgnoreCase("String")) {
            addIfNotExists(list, "import jakarta.validation.constraints.Size");

            if (!isNull) {
                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
            }

        } else {
            if (!isNull) {
                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
            }
            if (dataType.equalsIgnoreCase("LocalDate")) {
                addIfNotExists(list, "import java.time.LocalDate");
            } else if (dataType.equalsIgnoreCase("LocalDateTime")) {
                addIfNotExists(list, "import java.time.LocalDateTime");
            } else if (dataType.equalsIgnoreCase("Image")) {
                addIfNotExists(list, "import org.springframework.web.multipart.MultipartFile");
            }

        }
        return list;
    }

    private String makeImports(Project project) throws Exception {

        String imp = "import lombok.Builder;\nimport lombok.Data;\n";

        List<String> imports = new ArrayList();
        for (FieldDAO t : this.fields) {
            this.requestImports(project, forceReferences(t), t).forEach(i -> addIfNotExists(imports, i));

        }
        for (String impo : imports) {
            imp += impo + ";\n";
        }
        return imp;
    }

    private String makeAnnotedFields() {
        String annotedFields = "";
        for (FieldDAO field : this.fields) {

            annotedFields += field.requestAnnotation(objectName, primaryKeyVariableName, forceReferences(field));
            annotedFields += "private " + getDeclaration(field, this.forceReferences(field), true);

        }
        return annotedFields;
    }

    public String getDeclaration(FieldDAO f, boolean forceReferences, boolean newLine) {
        String newline = newLine ? ";\n" : "";

        if (f.getDataType().equalsIgnoreCase("Image")) {
            return "MultipartFile ".concat(this.getVariableName(f))
                    .concat(newline);
        }
        if (f.isReferance()) {
            return f.getDataType() + " " + this.getVariableName(f).concat(newline);
        }
        return f.getDeclaration(forceReferences(f), newLine);

    }

    public String getVariableName(FieldDAO f) {

        return f.getDBColumnName(forceReferences(f));
    }
    
     public String getFieldName(FieldDAO f) {

        return f.getDBColumnName(forceReferences(f));
    }

    public String getUsableDataType(FieldDAO fieldDAO, boolean forceReferences) {

        if (fieldDAO.getDataType().equalsIgnoreCase("Image")) {
            return "MultipartFile";
        } else {
            return fieldDAO.getUsableDataType(false);
        }
    }

    private String makeConstructor() {

        String construtorLine = "";
        String construtorInitials = "";
        for (int i = 0; i < fields.size(); i++) {
            FieldDAO field = this.fields.get(i);

            if (!field.isCollection()) {

                if (i == 0) {
                    construtorLine += this.getDeclaration(field, forceReferences(field), false);
                } else {
                    construtorLine += "," + this.getDeclaration(field, forceReferences(field), false);
                }
                construtorInitials += "this." + this.getVariableName(field) + " = " + this.getVariableName(field) + ";\n";
            }
        }

        return Utilities.makeMethod("public", "", objectName.concat(WebFiles.Request.name()), construtorLine, construtorInitials);
    }

    public String makeGetter(FieldDAO f) {
        boolean forceReferences  = this.forceReferences(f);
        String type = f.getDataType();
        if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) {
            return Utilities.makeMethod("public", getUsableDataType(f, false), "is" + f.getReqFieldName(forceReferences), "", "return " + getVariableName(f) + ";");
        } else {
            return Utilities.makeMethod("public", getUsableDataType(f, forceReferences), "get" + f.getReqFieldName(forceReferences), "", "return " + getVariableName(f) + ";");

        }
    }

    public String makeSetter(FieldDAO f) {
        boolean forceReferences  = this.forceReferences(f);
        
        return "public ".concat("void").concat(" ").
                concat("set").concat(f.getReqFieldName(forceReferences))
                .concat("(" + getDeclaration(f, forceReferences, false) + "){\n").concat("this.").
                concat(this.getVariableName(f)).concat(" = ").concat(this.getVariableName(f)).concat(";\n}");
    }

    public String makeProperties(FieldDAO f) {

        return this.makeGetter(f) + this.makeSetter(f);
    }

    private String makeProperties() {
        String properties = "";
        for (FieldDAO field : fields) {
            properties += makeProperties(field);
        }
        return properties;
    }

    public String makeClass(Project project) throws Exception {
        validate(fields, project);
        String className = objectName + "" + WebFiles.Request.name();
        String constructor = new JavaClass(className).makeNoArgConstructor().concat("\n") + this.makeConstructor();
        String methods = "";

        String entityPackage = project.getEntityPackage();
        String packageName = isNullOrEmpty(entityPackage)
                ? project.getBasePackage() + "." + objectName.toLowerCase().concat(".dtos") : entityPackage;
        JavaClass javaClass = new JavaClass(packageName, className, this.makeImports(project),
                this.makeAnnotedFields(), "", "", methods);
        
        return javaClass.makeClass("", "@Builder\n@Data\n");
    }

}
