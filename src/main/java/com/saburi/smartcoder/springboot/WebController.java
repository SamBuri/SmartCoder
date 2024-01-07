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
import com.saburi.utils.Enums.EntityTypes;
import com.saburi.utils.Enums.ServiceTypes;
import com.saburi.utils.Utilities;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Hp
 */
public class WebController extends CodeGenerator {

    private final String objectName;
    private final List<FieldDAO> fields;
    private String primaryKeyVariableName;
    private final FieldDAO primaryKeyFied;
    private final String controllerType;
    private final String objectRequest;
    private final String objectservice;
    private final String serviceVariableName;
    private final ProjectDAO oProjectDAO = new ProjectDAO();
    private final Project commonProject;
    private final ServiceTypes serviceTypes;
    private final String baseService;
    private final Project project;
    private final boolean hasMultipart;

    public WebController(String objectName, Project project, List<FieldDAO> fields, ServiceTypes serviceTypes) throws Exception {
        this.objectName = objectName;
        this.project = project;
        this.fields = fields.stream()
                .filter(p -> !(p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.UI_Only.name())
                || p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Query_Only.name())
                || p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Read_Only.name())))
                .collect(Collectors.toList());
        this.serviceTypes = serviceTypes;

        primaryKeyFied = Utilities.getPrimaryKey(fields);
        if (primaryKeyFied != null) {
            this.primaryKeyVariableName = primaryKeyFied.getVariableName();
        }
        this.hasMultipart = Utilities.hasMultipart(fields);
        this.controllerType = Utilities.getControllerType(serviceTypes, hasMultipart);
        this.objectRequest = objectName.concat(Enums.WebFiles.Request.name());
        this.objectservice = objectName.concat(Enums.WebFiles.Service.name());
        this.serviceVariableName = Utilities.getVariableName(objectservice);
        this.commonProject = oProjectDAO.get(project.getCommonProjectName());
        this.baseService = Utilities.getParentService(serviceTypes);
    }

    public String makeImports(Project project) {

        String imp = "import " + project.getBasePackage() + "." + objectName.toLowerCase() + ".dtos." + objectRequest + ";\n"
                + "import " + commonProject.getBasePackage() + ".controllers." + controllerType + ";\n"
                + "import " + commonProject.getBasePackage() + ".services." + baseService + ";\n"
                + "import org.springframework.beans.factory.annotation.Autowired;\n"
                + "import org.springframework.web.bind.annotation.RequestMapping;\n"
                + "import org.springframework.web.bind.annotation.RestController;\n";

        if (fields.parallelStream().filter(p -> p.isExpose()).count() > 0) {
            imp += "import org.springframework.web.bind.annotation.GetMapping;\n"
                    + "import org.springframework.web.bind.annotation.PathVariable;\n"
                    + "import "+commonProject.getBasePackage() + ".dtos.ResponseData;\n"
                    + "import java.util.List;\n";

        }

        return imp;
    }

    private String makeAnnotedFields() {

        return """
               @Autowired
                   private """.concat(" ") + objectservice + " " + serviceVariableName + ";";

    }

    public String makeProperties(EntityTypes entityTypes) {
        String dataType = primaryKeyFied == null ? Utilities.getIdWrapperDataType(entityTypes) : primaryKeyFied.getDataType();
        return "@Override\n"
                + "    protected " + baseService + "<" + objectName + ", " + objectRequest + ", " + dataType + "> getBaseService() {\n"
                + "        return " + serviceVariableName + ";\n"
                + "    }";
    }

    private String createExpose(FieldDAO fieldDAO) {
        boolean referencesIn = fieldDAO.referencesIN(project);
        String id = referencesIn ? "Id" : "";
        String param = fieldDAO.getUsableDataType(false).concat(" ")
                .concat(referencesIn ? "id" : fieldDAO.getVariableName());
        return "@GetMapping(\"/" + fieldDAO.getFieldName().toLowerCase() + "/{" + (referencesIn ? "id" : fieldDAO.getVariableName()) + "}\")\npublic List<? extends ResponseData> getBy"
                .concat(fieldDAO.getFieldName()).concat(id).concat("(")
                .concat("@PathVariable ")
                .concat(param)
                .concat("){\n")
                .concat("return " + serviceVariableName + ".getBy" + fieldDAO.getFieldName().concat(id) + "(" + (referencesIn ? "id" : fieldDAO.getVariableName()) + ");")
                .concat("}\n");
    }

    public String makeClass(Project project, Enums.EntityTypes entityTypes) throws Exception {
        validate(fields, project);
        String className = objectName + "" + Enums.WebFiles.Controller.name();

        String dataType = primaryKeyFied == null ? Utilities.getIdWrapperDataType(entityTypes) : primaryKeyFied.getDataType();
        String annotation = "@RestController\n"
                + "@RequestMapping(\"" + Utilities.toPlural(objectName).toLowerCase() + "\")";

        String exm = "";

        exm = fields.stream().filter(p -> p.isExpose()).map(fieldDAO -> createExpose(fieldDAO)).reduce(exm, String::concat);

        String packageName = project.getBasePackage() + "." + objectName.toLowerCase();
        JavaClass javaClass = new JavaClass(packageName, className, this.makeImports(project), makeAnnotedFields() + makeProperties(entityTypes) + exm);
        return javaClass.makeClass(controllerType + "<" + objectName + ", " + objectRequest + ", " + dataType + ">", annotation);
    }
}
