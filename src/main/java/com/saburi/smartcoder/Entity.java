/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.model.Project;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.addIfNotExists;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author CLINICMASTER13
 */
public class Entity extends CodeGenerator {

    String objectName;
    List<FieldDAO> fields;
    String objectNameDA;
    String objectNameController;
    String objectNameViewController;
    String objectNameEdit;
    String objectNameView;
    String primaryKeyVariableName;
    String objectVariableName;
    private final ProjectDAO oProjectDAO =  new ProjectDAO();

    public Entity(String objectName, List<FieldDAO> fields) {
        this.objectName = objectName;
        this.fields = fields;
        this.objectNameDA = objectName.concat("DA");
        this.objectNameController = objectName.concat("Controller");
        this.objectNameViewController = objectName.concat("ViewController");
        this.objectNameEdit = objectName.concat("Edit");
        this.objectNameView = objectName.concat("View");
        this.primaryKeyVariableName = Utilities.getPrimaryKey(fields).getVariableName();
        this.objectVariableName = Utilities.getVariableName(objectName);
    }

    public String makeEntityImports(Project currentProject) {
        Project commonProject = oProjectDAO.find(currentProject.getCommonProjectID());
        String imp = "import javax.persistence.Entity;\n"
                + "import javax.persistence.Id;\n"
                + "import org.hibernate.envers.Audited;\n"
                + "import org.hibernate.envers.RelationTargetAuditMode;\n";
        if (commonProject.getProjectID() != currentProject.getProjectID()) {
            imp += "import " + oProjectDAO.find(currentProject.getCommonProjectID()).getEntityPackage() + ".DBEntity;\n";
        }
        List<String> imports = new ArrayList();
        this.fields.forEach((t) -> {
            t.entityImports(currentProject).forEach(i -> addIfNotExists(imports, i));

        });
        for (String impo : imports) {
            imp += impo + ";\n";
        }
        return imp;
    }

    public String makeAnnotedFields() {
        String annotedFields = "";
        for (FieldDAO field : this.fields) {
            annotedFields += field.fiedAnnotations(objectName, primaryKeyVariableName);
            annotedFields += "private " + field.getDeclaration(true, true);
        }
        return annotedFields;
    }

    public String makeConstructor() {

        String construtorLine = "";
        String construtorInitials = "";
        for (int i = 0; i < fields.size(); i++) {
            FieldDAO field = this.fields.get(i);

            if (!field.isCollection()) {

                if (i == 0) {
                    construtorLine += field.getDeclaration(true, false);
                } else {
                    construtorLine += "," + field.getDeclaration(true, false);
                }
                construtorInitials += "this." + field.getVariableName() + " = " + field.getVariableName() + ";\n";
            }
        }

        return Utilities.makeMethod("public", "", objectName, construtorLine, construtorInitials);
    }

    public String makeProperties() {
        String properties = "";
        for (FieldDAO field : fields) {
            properties += field.makeProperties();
        }
        return properties;
    }

//    public String makeSetters() {
//        String setters = "";
//        for (Field field : fields) {
//            setters += field.makeSetter();
//        }
//        return setters;
//    }
    public String overriddenID() {

        String primaryKey = "";
        for (FieldDAO field : fields) {
            if (field.getKey().equalsIgnoreCase(Enums.keys.Primary.name())) {
                primaryKey = field.getVariableName();
            }
        }

        if (primaryKey.equalsIgnoreCase("id")) {
            return "";
        }
        String method = " @Override\npublic Object getId(){\n";
        if (!primaryKey.isBlank()) {
            method += "return this." + primaryKey + ";\n";
        } else {
            method += " throw new UnsupportedOperationException(\"Not supported yet.\");\n";
        }
        method += "}\n";
        return method;
    }

    public String overriddenDisplayKey() {
        String displayKey = "";
        String displayVariableName = "";

        List<FieldDAO> displayKeys = fields.stream().filter((p) -> p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Display.name())).collect(Collectors.toList());

        for (FieldDAO field : displayKeys) {
            if (field.isReferance()) {
                displayVariableName = field.getVariableName().concat(".getDisplayKey()");
            } else {
                displayVariableName = field.getVariableName();
            }

            if (displayKeys.indexOf(field) == 0) {
                displayKey += displayVariableName;
            } else {
                displayKey += "+\" \"+this." + displayVariableName;
            }

        }

        if (displayKey.equalsIgnoreCase("displayKey")) {
            return "";
        }
        String method = " @Override\npublic String getDisplayKey(){\n";
        if (!displayKey.isBlank()) {
            method += "return this." + displayKey + ";\n";
        } else {
            method += " throw new UnsupportedOperationException(\"Not supported yet.\");\n";
        }
        method += "}\n";
        return method;
    }

    public String otherMethods() {
        String objectEquals = " @Override\n"
                + "    public boolean equals(Object o) {\n"
                + "        if (this == o) {\n"
                + "            return true;\n"
                + "        }\n"
                + "        if (!(o instanceof " + this.objectName + ")) {\n"
                + "            return false;\n"
                + "        }\n"
                + "\n"
                + "        " + this.objectName + " " + objectVariableName + " = (" + objectName + ") o;\n"
                + "\n"
                + "        return this.getId().equals(" + objectVariableName + ".getId());\n"
                + "    }\n";

        String objectHashCode = "    @Override\n"
                + "    public int hashCode() {\n"
                + "        return this." + primaryKeyVariableName + ".hashCode();\n"
                + "\n"
                + "    }";

        return objectEquals + objectHashCode;

    }

    public String makeClass(Project currentProject) throws Exception {
        validate(fields);
        String constructor = new JavaClass(objectName).makeNoArgConstructor().concat("\n") + this.makeConstructor();
        String methods = otherMethods() + this.overriddenID() + this.overriddenDisplayKey();
        JavaClass javaClass = new JavaClass(currentProject.getEntityPackage(), objectName, this.makeEntityImports(currentProject),
                this.makeAnnotedFields(), constructor, this.makeProperties(), methods);
        return javaClass.makeClass("DBEntity", "@Entity\n@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)");
    }

}
