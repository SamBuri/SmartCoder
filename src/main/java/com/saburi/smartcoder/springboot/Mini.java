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
public class Mini extends CodeGenerator {

    private final String objectName;
    private final List<FieldDAO> fields;
    private final FieldDAO primaryKeyFied;
    private final String pkDataType;
    private final boolean isPKNull;
    Project project;

    public Mini(String objectName, Project project, List<FieldDAO> fields, Enums.EntityTypes entityTypes) {
        this.objectName = objectName;
        this.fields = fields.stream().filter(f -> f.isPrimaryKey() || f.isDisplayKey()).collect(Collectors.toList());

        primaryKeyFied = Utilities.getPrimaryKey(fields);
        isPKNull = primaryKeyFied == null;

        pkDataType = isPKNull ? Utilities.getIdWrapperDataType(entityTypes) : primaryKeyFied.getDataType();
        this.project=project;
    }
    
     private boolean forceReferences(FieldDAO fieldDAO) {
        String projectName = fieldDAO.getProjectName();
        return this.project.getProjectType().equals(Enums.ProjectTypes.Springboot_API) ? projectName.equalsIgnoreCase(this.project.getProjectName()) || isNullOrEmpty(projectName) : true;
    }

    private String makeImports() throws Exception {

        String imp = "";
        imp+=Utilities.makeResponseImport(project);

        List<String> imports = new ArrayList();
        for(FieldDAO t: this.fields) {
            t.miniImports(project).forEach(i -> addIfNotExists(imports, i));

        }
        for (String impo : imports) {
            imp += impo + ";\n";
        }
        return imp;
    }

    private String makeAFields() {
        String annotedFields = isPKNull ? pkDataType.concat(" ").concat("get").concat("Id();").concat("\n") : "";

        for (FieldDAO field : this.fields) {

            annotedFields += field.getUsableDataType(this.forceReferences(field)) + field.getCall().concat(";").concat("\n");

        }
        return annotedFields;
    }

    public String makeClass() throws Exception {
        validate(fields, project);
        String className = objectName + "" + Enums.WebFiles.Mini.name();

        String packageName = project.getBasePackage() + "." + objectName.toLowerCase().concat(".dtos");
        JavaClass javaClass = new JavaClass(packageName, className, this.makeImports(), this.makeAFields());
        return javaClass.makeInterfaceExt(Utilities.RESPONSE_INERFACE);
    }

}
