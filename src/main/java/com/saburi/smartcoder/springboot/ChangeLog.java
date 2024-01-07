/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.springboot;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.model.Field;
import com.saburi.model.Project;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;
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
public class ChangeLog {

    private final String objectName;
    private final List<FieldDAO> fields;
    private String primaryKeyVariableName;
    private FieldDAO primaryKeyFied;
    private final String objectVariableName;
    private final Project project;
    private final Enums.ProjectTypes projectType;
    private final ProjectDAO oProjectDAO = new ProjectDAO();
    private final Enums.EntityTypes entityType;
    private final String idDataType;

    public ChangeLog(String objectName, Project project, List<FieldDAO> fields, Enums.EntityTypes entityType) {
        this.objectName = objectName;
        this.project = project;
        this.projectType = this.project.getProjectType();
        this.entityType = entityType;

        this.fields = fields.stream()
                .filter(p -> !p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.UI_Only.name()))
                .collect(Collectors.toList());

        primaryKeyFied = Utilities.getPrimaryKey(fields);
        this.primaryKeyVariableName = primaryKeyFied == null ? "id" : primaryKeyFied.getVariableName();
        this.idDataType = primaryKeyFied == null ? Utilities.getIdWrapperDataType(entityType) : primaryKeyFied.getDataType();
        this.objectVariableName = Utilities.getVariableName(objectName);

    }

    public List<FieldDAO> constantFields() throws Exception {

        return List.of(
                new FieldDAO(new Field("ClientId", "Client Id", "String")),
                new FieldDAO(new Field("CreatedBy", "Created By", "String")),
                new FieldDAO(new Field("ModifiedBy", "Modified By", "String")),
                new FieldDAO(new Field("CreationDate", "Creation Date", "long")),
                new FieldDAO(new Field("LastModifiedDate", "Last Modified Date", "long")),
                new FieldDAO(new Field("Branch", "Branch", "String")),
                new FieldDAO(new Field("BranchId", "Branch Id", "String"))
        );
    }

    public List<FieldDAO> auditFields() {
        List<FieldDAO> revFields = new ArrayList<>();
        try {

            Field field = new Field("Rev", "Rev", "int", "RevInfo");
            field.setKey(Enums.keys.Primary.name());
            revFields.add(new FieldDAO(field));
            revFields.add(new FieldDAO(new Field("Revtype", "Rev Type", "short")));

        } catch (Exception ex) {
            Logger.getLogger(ChangeLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return revFields;
    }

    private boolean forceReferences(FieldDAO fieldDAO) {
        String proiectName = fieldDAO.getProjectName();
        return this.projectType.equals(Enums.ProjectTypes.Springboot_API) ? proiectName.equalsIgnoreCase(this.project.getProjectName()) || isNullOrEmpty(proiectName) : true;
    }

    private String makeColumnName(FieldDAO fieldDo) {

        String name = "";

        for (char c : fieldDo.getDBColumnName(this.forceReferences(fieldDo)).toCharArray()) {
            String cs = String.valueOf(c);
            if (cs.toUpperCase().equals(cs)) {
                name += "_" + cs.toLowerCase();
            } else {
                name += c;
            }
        }

        return name;

    }

    private String makeColumn(FieldDAO fieldDo, String prefix) {
        String columnName = this.makeColumnName(fieldDo);

        String nameType = "name=\"" + columnName + "\" type=\"" + this.getDataType(fieldDo) + "\"";
        String autoIncrement = "";
        boolean isAutoIdByEntityType = (this.entityType == Enums.EntityTypes.Auto_ID_Int
                || this.entityType == Enums.EntityTypes.Auto_ID_Long);
        if (!isAudit(prefix) &&   (fieldDo.isPrimaryKeyAuto() || (fieldDo.isPrimaryKey() && isAutoIdByEntityType))) {
            autoIncrement = " autoIncrement=\"true\"";
        }

        String column = "<column  "
                .concat(nameType)
                .concat(autoIncrement)
                .concat(">\n")
                .concat(this.makeColumnConstraints(fieldDo, prefix))
                .concat("</column>");

        return column;

    }

    private String makePrimaryKeyFiled(String prefix) {
        if (this.primaryKeyFied != null) {
            return "";
        }
        try {

            Field field = new Field("Id", "Id", idDataType);
            field.setKey(Enums.keys.Primary.name());
            FieldDAO fdao = new FieldDAO(field);
            return this.makeColumn(fdao, prefix);
        } catch (Exception e) {
            e.printStackTrace();
            return "";

        }

    }
    
     private String makeIdHelperField(String prefix) {
        
       
        
          boolean isIdGen =  this.entityType.equals(Enums.EntityTypes.Auto_ID_Gen);
          
          if(!isIdGen) return "";
        try {

            Field field = new Field("IdHelepr", "Id Helper", "int");
            field.setSaburiKey(Enums.Saburikeys.ID_Helper.name());
            FieldDAO fdao = new FieldDAO(field);
            return this.makeColumn(fdao, prefix);
        } catch (Exception e) {
            e.printStackTrace();
            return "";

        }

    }

    private String makeConstantColums(String prefix) {
        String columns = "";
        try {
            columns = this.constantFields()
                    .stream()
                    .map(f -> makeColumn(f, prefix))
                    .reduce(columns, String::concat);
        } catch (Exception ex) {
            Logger.getLogger(ChangeLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return columns;
    }

    private String tableName(String prefix) {
        return Utilities.toPlural(objectName).concat(prefix).toLowerCase();
    }
    
    private boolean isAudit(String prefix){
     return  prefix.equalsIgnoreCase("_aud") || prefix.equalsIgnoreCase("_rev");
    }

    private String makeColumnConstraints(FieldDAO fieldDAO, String prefix) {
        boolean isPrimary = fieldDAO.isPrimaryKey() || fieldDAO.isPrimaryKeyAuto();
        boolean isForeign = fieldDAO.isForeignKey(this.forceReferences(fieldDAO));
        boolean isUnique = fieldDAO.getKey().equalsIgnoreCase(Enums.keys.Unique.name());
        boolean isNullable = fieldDAO.getNullable();

        if (isNullable && !isForeign && !isUnique && !isPrimary) {
            return "";
        }

        String columnName = makeColumnName(fieldDAO);
        String tableName = tableName(prefix);
        String constraintName = tableName + "_" + columnName;
       

        String pk = "";
        if (isPrimary) {
            pk = "primaryKey=\"true\" primaryKeyName=\"pk_" + constraintName + "\" ";
        }

        String fk = "";
        if (isForeign) {
            String referenceTableName = Utilities.toPlural(fieldDAO.getReferences()).toLowerCase();
             String referencedColumnName=  referenceTableName.equalsIgnoreCase("revinfo")?"rev":"id";

            fk = "foreignKeyName=\"fk_" + constraintName + "\"  referencedTableName=\"" + referenceTableName + "\" "
                    + "referencedColumnNames=\"" + referencedColumnName + "\" ";
        }

        String uk = "";
        boolean isRevision = isAudit(prefix);
        if (isUnique && !isRevision) {
            uk = " unique=\"true\" uniqueConstraintName=\"uk_" + constraintName + "\" ";
        }

        String nullable = isRevision ? "" : isNullable ? "" : " nullable=\"false\"";
        return "<constraints " + pk + fk + uk + nullable + "/>\n";
    }

    private String getDataType(FieldDAO fieldDo) {
        String dataType = fieldDo.getDataType();
        if (dataType.equalsIgnoreCase("String")) {

            return "VARCHAR(" + fieldDo.getSize() + ")";
        } else if (dataType.equalsIgnoreCase("Image") || dataType.equalsIgnoreCase("File")) {
            return "LONGBLOB";
        } else if (dataType.equalsIgnoreCase("int") || dataType.equalsIgnoreCase("Integer")) {
            return "INT";
        } else if (dataType.equalsIgnoreCase("long") || dataType.equalsIgnoreCase("Long")) {
            return "BIGINT";
        } else if (dataType.equalsIgnoreCase("short")) {
            return "TINYINT(3)";
        } 
         else if (dataType.equalsIgnoreCase("LocalDate")
                 ||dataType.equalsIgnoreCase("Date")) {
            return "DATE";
        }
        
        else {
            return dataType.toUpperCase();
        }
    }

    public String makeChangeSet() {
        String tableName = Utilities.toPlural(objectName).toLowerCase();
        String author = "Sam Buriima";
        String id = project.getProjectName() + "_create_" + tableName;
        String prefix = "";
        String columns = this.makePrimaryKeyFiled(prefix);
        columns+=makeIdHelperField(prefix);
        columns = fields.stream().map(f -> makeColumn(f, prefix))
                .reduce(columns, String::concat);

        columns += makeConstantColums(prefix);

        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());

        String uniqueColumnGroupsConstraints = "";
        if (!uniqueGroups.isEmpty()) {
            FieldDAO fdao = uniqueGroups.get(0);
            String fColName = makeColumnName(fdao);
            String columnNames = makeColumnName(fdao);
            String constraintName = "uk_" + tableName + "_" + fColName;
            for (int i = 1; i < uniqueGroups.size(); i++) {

                FieldDAO fieldDAO = uniqueGroups.get(i);
                String colName = makeColumnName(fieldDAO);
                columnNames += "," + colName;
                constraintName += "_" + colName;
            }

            uniqueColumnGroupsConstraints = " <changeSet id=\"add_" + constraintName + "\" author=\"Sam Buriima\">\n"
                    + "        <addUniqueConstraint tableName=\"" + tableName + "\" columnNames=\"" + columnNames + "\" constraintName=\"" + constraintName + "\"/>\n"
                    + "    </changeSet>\n";
        }

        return "<changeSet author=\"" + author + "\" id=\"" + id + "\">\n"
                + "    <createTable tableName=\"" + tableName + "\">\n"
                + columns
                + "    </createTable>\n"
                + "  <rollback>\n"
                + "            <dropTable tableName=\"" + tableName + "\"/>\n"
                + "        </rollback>"
                + "</changeSet>\n"
                + uniqueColumnGroupsConstraints
                + "";
    }

    public String makeAuditChangeSet() {
        String prefix = "_aud";
        String tableName = tableName(prefix);
        String author = "Sam Buriima";
        String id = project.getProjectName() + "_create_" + tableName;

        String columns = this.makePrimaryKeyFiled(prefix);
        String revColums = "";

        revColums = auditFields().stream().map(f -> makeColumn(f, prefix))
                .reduce(revColums, String::concat);
        columns += revColums;
        columns+=makeIdHelperField(prefix);
        columns = fields
                 .stream()
                 .map(f -> makeColumn(f, prefix))
                  .reduce(columns, String::concat);

        columns += makeConstantColums(prefix);

//        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
//                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());

//        String uniqueColumnGroupsConstraints = "";
//        if (!uniqueGroups.isEmpty()) {
//            FieldDAO fdao = uniqueGroups.get(0);
//            String fColName = makeColumnName(fdao);
//            String columnNames = "";
//            String constraintName = "uk_" + tableName + "_" + fColName;
//            for (int i = 1; i < uniqueGroups.size(); i++) {
//
//                FieldDAO fieldDAO = uniqueGroups.get(i);
//                String colName = makeColumnName(fieldDAO);
//                columnNames += "," + colName;
//                constraintName += "_" + colName;
//            }
//
//            uniqueColumnGroupsConstraints = " <changeSet id=\"add_" + constraintName + "\" author=\"Sam Buriima\">\n"
//                    + "        <addUniqueConstraint tableName=\"" + tableName + "\" columnNames=\"" + columnNames + "\" constraintName=\"" + constraintName + "\"/>\n"
//                    + "    </changeSet>\n";
//        }

        return "<changeSet author=\"" + author + "\" id=\"" + id + "\">\n"
                + "    <createTable tableName=\"" + tableName + "\">\n"
                + columns
                + "    </createTable>\n"
                + "  <rollback>\n"
                + "            <dropTable tableName=\"" + tableName + "\"/>\n"
                + "        </rollback>"
                + "</changeSet>\n"
//                + uniqueColumnGroupsConstraints
                + "";
    }

    public String makeDocument() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<databaseChangeLog\n"
                + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "        xmlns:ext=\"http://www.liquibase.org/xml/ns/dbchangelog-ext\"\n"
                + "        xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"\n"
                + "        xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd\n"
                + "    http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd\">\n"
                        .concat(makeChangeSet())
                        .concat("\n")
                        .concat(makeAuditChangeSet())
                        .concat("\n")
                        .concat("</databaseChangeLog>");

    }

}
