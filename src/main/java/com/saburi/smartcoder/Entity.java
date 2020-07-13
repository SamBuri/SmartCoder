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
import static com.saburi.utils.Utilities.makeMethod;
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
    FieldDAO primaryKeyFied;
    String objectVariableName;
    private final ProjectDAO oProjectDAO = new ProjectDAO();

    public Entity(String objectName, List<FieldDAO> fields) {
        this.objectName = objectName;
        this.fields = fields;
        this.objectNameDA = objectName.concat("DA");
        this.objectNameController = objectName.concat("Controller");
        this.objectNameViewController = objectName.concat("ViewController");
        this.objectNameEdit = objectName.concat("Edit");
        this.objectNameView = objectName.concat("View");
        primaryKeyFied = Utilities.getPrimaryKey(fields);
        this.primaryKeyVariableName = primaryKeyFied.getVariableName();
        this.objectVariableName = Utilities.getVariableName(objectName);
    }

    public String makeEntityImports(Project currentProject) {
        Project commonProject = oProjectDAO.find(currentProject.getCommonProjectID());
        String imp = "import java.util.Objects;\n"
                + "import javax.persistence.Column;\n"
                + "import javax.persistence.Entity;\n"
                + "import javax.persistence.Id;\n"
                + "import org.hibernate.envers.Audited;\n"
                + "import org.hibernate.envers.RelationTargetAuditMode;\n";
        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());
        if (!uniqueGroups.isEmpty()) {
            imp += "import javax.persistence.Table;\n"
                    + "import javax.persistence.UniqueConstraint;\n";
        }
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
            if (field.isReferance() && field.isPrimaryKey()) {
                annotedFields += "@Id\n"
                        + "    @Column(length = " + field.getSize() + ", updatable = false)";
                annotedFields += "private String" + objectName.concat("ID");
            }
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
            if (field.getKey().equalsIgnoreCase(Enums.keys.Primary.name()) || field.getKey().equalsIgnoreCase(Enums.keys.Primary_Auto.name())) {
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
            if (field.isReferance() && !field.getEnumerated()) {
                displayVariableName = field.getVariableName().concat(".getDisplayKey()");
            } else if (field.isReferance() && field.getEnumerated()) {
                displayVariableName = field.getVariableName().concat(".name()");
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
        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());

        String body = "if (this == o) {\n"
                + "            return true;\n"
                + "        }\n"
                + "        if (o == null) {\n"
                + "            return false;\n"
                + "        }\n"
                + "        if (getClass() != o.getClass()) {\n"
                + "            return false;\n"
                + "        }\n"
                + "        final " + this.objectName + " " + objectVariableName + " = (" + this.objectName + ") o;";
        if (primaryKeyFied.isPrimaryKeyAuto() && !uniqueGroups.isEmpty()) {
            int last = uniqueGroups.size() - 1;
            List<FieldDAO> middleUniqueFileds = new ArrayList<>(uniqueGroups);
            middleUniqueFileds.remove(last);
            body = middleUniqueFileds.stream().map((fieldDAO) -> " if (!Objects.equals(this." + fieldDAO.getVariableName() + ", " + objectVariableName + "." + fieldDAO.getVariableName() + ")) {\n"
                    + "            return false;\n"
                    + "        }").reduce(body, String::concat);
            FieldDAO fieldDAO = uniqueGroups.get(last);
            body += "return Objects.equals(this." + fieldDAO.getVariableName() + ", " + objectVariableName + "." + fieldDAO.getVariableName() + ");";

        } else {
            body += " if (this.getId() == null || " + objectVariableName + ".getId() == null) {\n"
                    + "            return false;\n"
                    + "        }\nreturn this.getId().equals(" + objectVariableName + ".getId());\n";

        }
        String objectEquals = makeMethod("@Override\npublic", "boolean", "equals", "Object o", body);

        String hashBody = "";
        if (primaryKeyFied.isPrimaryKeyAuto() && !uniqueGroups.isEmpty()) {
            int last = uniqueGroups.size() - 1;
            List<FieldDAO> middleUniqueFileds = new ArrayList<>(uniqueGroups);
            middleUniqueFileds.remove(last);
            hashBody = middleUniqueFileds.stream().map((fieldDAO) -> " Objects.hashCode(this." + fieldDAO.getVariableName() + ")+")
                    .reduce(hashBody, String::concat);
            FieldDAO fieldDAO = uniqueGroups.get(last);
            hashBody += "Objects.hashCode(this." + fieldDAO.getVariableName() + ");";
            hashBody = "return " + hashBody;

        } else {
            hashBody = " return Objects.hashCode(this." + primaryKeyVariableName + ");\n";

        }

        String objectHashCode = makeMethod("@Override\npublic", "int", "hashCode", "", hashBody);

        return objectEquals + objectHashCode;

    }

    public String makeClass(Project currentProject) throws Exception {
        validate(fields);
        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());
        String tableAnnotation = "";
        if (!uniqueGroups.isEmpty()) {
            FieldDAO fdao = uniqueGroups.get(0);
            String columnNames = "\"" + fdao.getDBColumnName() + "\"";
            String constraintName = "uq" + fdao.getFieldName();
            for (int i = 1; i < uniqueGroups.size(); i++) {
                FieldDAO fieldDAO = uniqueGroups.get(i);
                columnNames += "," + "\"" + fieldDAO.getDBColumnName() + "\"";
                constraintName += fieldDAO.getFieldName();
            }

            tableAnnotation = "\n@Table(\n"
                    + "        uniqueConstraints = @UniqueConstraint(columnNames = {" + columnNames + "}, "
                    + "name = \"" + constraintName + "\")\n"
                    + ")";
        }
        String constructor = new JavaClass(objectName).makeNoArgConstructor().concat("\n") + this.makeConstructor();
        String methods = otherMethods() + this.overriddenID() + this.overriddenDisplayKey();
        JavaClass javaClass = new JavaClass(currentProject.getEntityPackage(), objectName, this.makeEntityImports(currentProject),
                this.makeAnnotedFields(), constructor, this.makeProperties(), methods);
        return javaClass.makeClass("DBEntity", "@Entity\n@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)" + tableAnnotation);
    }

}
