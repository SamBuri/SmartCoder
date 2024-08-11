/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.springboot.SpringbootUtils;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.EntityTypes;
import com.saburi.utils.Enums.ProjectTypes;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.addIfNotExists;
import static com.saburi.utils.Utilities.getParentEntity;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import static com.saburi.utils.Utilities.makeMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author CLINICMASTER13
 */
public class Entity extends SpringbootUtils {


    public Entity(FileModel fileModel) {
        super(fileModel);
        
    }

   

    public String makeEntityImports(Project project, EntityTypes entityTypes) throws Exception {
       String idImp = project.getProjectType().equals(ProjectTypes.Desktop) ? """
                            import jakarta.persistence.Id;
                             """ : "";
        String imp = """
                     import java.util.Objects;
                     import jakarta.persistence.Column;
                     import jakarta.persistence.Entity;
                     import jakarta.persistence.Table;
                     import org.hibernate.envers.Audited;
                     import org.hibernate.envers.RelationTargetAuditMode;
                     import lombok.Builder;
                     import lombok.AllArgsConstructor;
                     import lombok.Getter;
                     import lombok.Setter;
                     """ + idImp;
        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());
        if (!uniqueGroups.isEmpty()) {
            imp += """
                    import jakarta.persistence.UniqueConstraint;
                   """;
        }
        if (project.getProjectType().equals(ProjectTypes.Springboot_API)) {
            if (!commonProject.getProjectName().equalsIgnoreCase(project.getProjectName())) {
                imp += "import " + commonProject.getBasePackage() + ".entities." + getParentEntity(entityTypes) + ";\n";
                imp += "import " + commonProject.getBasePackage() + ".dtos.ResponseData;\n";
            }
        }
        List<String> imports = new ArrayList();
        for (FieldDAO t : this.fields) {
            t.entityImports(project, forceReferences(t)).forEach(i -> addIfNotExists(imports, i));

        };
        for (String impo : imports) {
            imp += impo + ";\n";
        }
        return imp;
    }

    public String makeAnnotedFields() {
        String annotedFields = "";
        for (FieldDAO field : this.fields) {
            if (field.isReferance() && field.isPrimaryKey()) {
                annotedFields += """
                                  @Id
                                  @Column(length = """ + field.getSize() + ", updatable = false)";
                annotedFields += "private String" + objectName.concat(this.forceReferences(field) ? "Id" : "");
            }

            annotedFields += field.entityAnnotation(objectName, primaryKeyVariableName, forceReferences(field));
            annotedFields += "private " + field.getDeclaration(forceReferences(field), true);

        }
        return annotedFields;
    }



    public String makeProperties() {
        String properties = "";
        for (FieldDAO field : fields) {
            properties += field.makeProperties(forceReferences(field));
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
    public String overriddenID(Project project) {
        if (project.getProjectType().equals(ProjectTypes.Springboot_API)) {
            return "";
        }

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

    public String otherMethods(Project project, EntityTypes entityTypes) {

        boolean isAutoPK;
        if (project.getProjectType().equals(ProjectTypes.Desktop)) {
            isAutoPK = primaryKeyFied.isPrimaryKeyAuto();
        } else {
            isAutoPK = entityTypes.equals(EntityTypes.Auto_ID_Int) || entityTypes.equals(EntityTypes.Auto_ID_Long);
        }

        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());

        String body = """
                      if (this == o) {
                                  return true;
                              }
                              if (o == null) {
                                  return false;
                              }
                              if (getClass() != o.getClass()) {
                                  return false;
                              }
                              final """ + " " + this.objectName + " " + objectVariableName + " = (" + this.objectName + ") o;";
        if (isAutoPK && !uniqueGroups.isEmpty()) {
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
        if (isAutoPK && !uniqueGroups.isEmpty()) {
            int last = uniqueGroups.size() - 1;
            List<FieldDAO> middleUniqueFileds = new ArrayList<>(uniqueGroups);
            middleUniqueFileds.remove(last);
            hashBody = middleUniqueFileds.stream().map((fieldDAO) -> " Objects.hashCode(this." + fieldDAO.getVariableName() + ")+")
                    .reduce(hashBody, String::concat);
            FieldDAO fieldDAO = uniqueGroups.get(last);
            hashBody += "Objects.hashCode(this." + fieldDAO.getVariableName() + ");";
            hashBody = "return " + hashBody;

        } else {
            hashBody = " return Objects.hashCode(this." + (project.getProjectType().equals(ProjectTypes.Desktop) ? primaryKeyVariableName : "id") + ");\n";

        }

        String objectHashCode = makeMethod("@Override\npublic", "int", "hashCode", "", hashBody);
        String idValueMessage = "";
        if (project.getProjectType().equals(ProjectTypes.Springboot_API)) {
            if (entityTypes.equals(EntityTypes.Auto_ID_Gen)) {

                FieldDAO idgenerator = Utilities.getIDGenerator(fields);
                if (idgenerator != null) {
                    String generatorValue = idgenerator.getVariableName();

                    if (idgenerator.isReferance()) {
                        if (idgenerator.getEnumerated()) {
                            generatorValue = idgenerator.getVariableName();
                        } else {
                            generatorValue = idgenerator.getVariableName() + ".getId()";
                        }
                    }

                    idValueMessage += "@Override\n"
                            + "    public String getIdGenerateValue() {\n"
                            + "       return  String.valueOf(this." + generatorValue + ");\n"
                            + "    }";
                }
            }
        }

        return objectEquals + objectHashCode + idValueMessage;

    }

    public String makeClass(Project project, EntityTypes entityTypes) throws Exception {
        
        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());
        String uniqueColumnGroupsConstraints = "";
        if (!uniqueGroups.isEmpty()) {
            FieldDAO fdao = uniqueGroups.get(0);
            String columnNames = "\"" + fdao.getDBColumnName(this.forceReferences(fdao)) + "\"";
            String constraintName = "uq" + fdao.getFieldName();
            for (int i = 1; i < uniqueGroups.size(); i++) {
                FieldDAO fieldDAO = uniqueGroups.get(i);
                columnNames += "," + "\"" + fieldDAO.getDBColumnName(this.forceReferences(fieldDAO)) + "\"";
                constraintName += fieldDAO.getFieldName();
            }

            uniqueColumnGroupsConstraints = " ,uniqueConstraints = @UniqueConstraint(columnNames = {" + columnNames + "}, "
                    + "name = \"" + constraintName + "\")\n";
        }
        String constructor = new JavaClass(objectName).makeNoArgConstructor().concat("\n");
        String methods = otherMethods(project, entityTypes) + this.overriddenID(project) + this.overriddenDisplayKey();

        String entityPackage = project.getEntityPackage();
        String packageName = isNullOrEmpty(entityPackage)
                ? project.getBasePackage() + "." + objectName.toLowerCase() : entityPackage;
        String imp = project.getProjectType().equals(ProjectTypes.Springboot_API) ? "implements ResponseData" : "";

        JavaClass javaClass = new JavaClass(packageName, objectName, this.makeEntityImports(project, entityTypes),
                this.makeAnnotedFields(), constructor, "", methods);

        String entityAnnotation = "@Entity\n@Builder\n@AllArgsConstructor\n"
                + "@Getter\n"
                + "@Setter\n";
        String tableName = Utilities.toPlural(objectName).toLowerCase();
        String tableAnnotation = "@Table(name = \"" + tableName + "\"" + uniqueColumnGroupsConstraints + ")";
        String auditAnnotation = "@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)";

        String topAnnotations = entityAnnotation.concat("\n").concat("\n").concat(tableAnnotation).concat("\n").concat(auditAnnotation);
        return javaClass.makeClass(entityTypes, imp, topAnnotations);
    }

    @Override
    protected boolean isValid() throws Exception {
        CodeGenerator.validate(fields, project);
        return super.isValid(); 
    }
    
    

    @Override
    protected String getFileName() {
       return objectName;
    }

    @Override
    protected String create() throws Exception {
        return makeClass(project, entityType);
    }

}
