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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author CLINICMASTER13
 */
public class DBAcess extends CodeGenerator {

    private String objectName;
    private List<FieldDAO> fields;
//    private final List<Field> allFields;
    private String objectNameDA;
    private final String objectVariableName;
    private final FieldDAO primaryKey;
    private final String primaryKeyVariableName;

    private final String objectNameDAVariableName;
    private final String superClass = "DBAccess";
    private final FilteredList<FieldDAO> collectionList;
    private final ProjectDAO oProjectDAO = new ProjectDAO();

    public DBAcess(String objectName, List<FieldDAO> fields) {
        this.objectName = objectName;
        this.fields = fields;
//        this.allFields = fields;
//        this.allFields.addAll(defaulFields);
        this.objectNameDA = objectName.concat("DA");
        this.objectVariableName = Utilities.getVariableName(objectName);
        this.primaryKey = Utilities.getPrimaryKey(fields);
        this.primaryKeyVariableName = primaryKey.getVariableName();
        this.objectNameDAVariableName = Utilities.getVariableName(objectNameDA);
        collectionList = new FilteredList<>(FXCollections.observableList(fields), e -> true);
        collectionList.setPredicate((p) -> p.isCollection());
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
        this.objectNameDA = objectName.concat("DA");
    }

    public List<FieldDAO> getFields() {
        return fields;
    }

    public void setFields(List<FieldDAO> fields) {
        this.fields = fields;
    }

    public String getObjectNameDA() {
        return objectNameDA;
    }

    public String makeImports(Project currentProject) {
        Project commonProject = oProjectDAO.find(currentProject.getCommonProjectID());
        String imp = "import " + currentProject.getEntityPackage() + "." + objectName + ";"
                + "import javafx.beans.property.*;\n"
                + "import java.util.ArrayList;\n"
                + "import " + commonProject.getEntityPackage() + ".AppRevisionEntity;\n"
                + "import java.util.List;\n"
                + "import " + commonProject.getUtilPackage() + ".SearchColumn;\n"
                + "import " + commonProject.getUtilPackage() + ".SearchColumn.SearchDataTypes;\n"
                + "import org.hibernate.envers.RevisionType;\n";
        if (commonProject.getProjectID() != currentProject.getProjectID()) {
            imp += "import " + oProjectDAO.find(currentProject.getCommonProjectID()).getDBAccessPackage() + ".DBAccess;\n";
        }
        List<String> imports = new ArrayList();
        fields.forEach((t) -> {
            t.daImports(currentProject).forEach(i -> addIfNotExists(imports, i));

        });
        for (String impo : imports) {
            imp += impo + ";\n";
        }
        return imp;
    }

    public String makeProperties() {
        String properties = "private " + this.objectName.concat(" ").concat(objectVariableName).concat("= new " + this.objectName + "();\n");
        for (FieldDAO field : fields) {
            properties += field.daPropertyLine();
        }
        return properties;
    }

    private String callNextIDHelper() throws Exception {
        FieldDAO idHelperObject = Utilities.getIDHelper(fields);
        FieldDAO idGeneratorObject = Utilities.getIDGenerator(fields);
        if (idGeneratorObject != null && idHelperObject == null) {
            throw new Exception("The ID generator does not have a helper column");
        }
        if (idGeneratorObject == null && idHelperObject == null) {
            return "";
        }
        if (idGeneratorObject == null) {
            return "getNext" + idHelperObject.getFieldName() + "()";
        } else {

            String idGeneratorVariableName = idGeneratorObject.getVariableName();
            if (idGeneratorObject.isReferance()) {
                idGeneratorVariableName = idGeneratorVariableName;
            }

            return " getNext" + idHelperObject.getFieldName() + "(" + idGeneratorVariableName + ")";
        }

    }

    private String makeConstructor() {

        String initialisers = "initialseProprties();\ncreateSearchColumns();";

        String noArg = makeMethod("public", "", objectNameDA, "", "createSearchColumns();");

        String puConstructor = makeMethod("public", "", objectNameDA, "String persistenceUnit", "super(persistenceUnit);");

        String readLineSimple = objectName.concat(" ").concat(objectVariableName);
        String readInitialSimple = "this." + objectVariableName + " = " + objectVariableName.concat(";\n").concat(initialisers);
        String readConstructorSimple = makeMethod("public", "", objectNameDA, readLineSimple, readInitialSimple);

        String readLine = "String persistenceUnit, " + objectName.concat(" ").concat(objectVariableName);
        String readInitial = "super(persistenceUnit);\n".concat("this." + objectVariableName + " = " + objectVariableName).concat(";\n").concat(initialisers);
        String readConstructor = makeMethod("public", "", objectNameDA, readLine, readInitial);

        String construtorLine = "";

        List<FieldDAO> constructorFields = fields.stream()
                .filter((p) -> !p.isCollection())
                .filter((p) -> !p.isHelper()).collect(Collectors.toList());

        for (int i = 0; i < constructorFields.size(); i++) {
            FieldDAO field = constructorFields.get(i);
//            if (field.isReferance() && !field.getEnumerated()) {
//                if (i == 0) {
//                    construtorLine += field.getReferencesDA() + " " + field.getVariableNameDA();
//                } else {
//                    construtorLine += ", " + field.getReferencesDA() + " " + field.getVariableNameDA();
//                }
//            } else {

            if (i == 0) {
                construtorLine += field.getDeclaration(true, false);

            } else {
                construtorLine += ", " + field.getDeclaration(true, false);
            }

//            }
        }

        String makeInitials = "";
        for (int i = 0; i < fields.size(); i++) {
            FieldDAO field = this.fields.get(i);
            if (field.isCollection()) {
//                construtorLine += field.getDeclaration(false, false);
//                makeInitials += field.getVariableName();
//            } else if (field.isReferance() && !field.getEnumerated()) {
//                if (i == 0) {
//                    makeInitials += field.getVariableNameDA() + "!= null ? (" + field.getReferences() + ") " + field.getVariableNameDA() + ".getDBEntity() : null";
//                } else {
//
////                makeInitials += "(" + field.getReferences() + ")" + Utilities.getVariableName(field.getFieldName() + "DA ") + ".getDBEntity()";
//                    makeInitials += ", " + field.getVariableNameDA() + "!= null ? (" + field.getReferences() + ") " + field.getVariableNameDA() + ".getDBEntity() : null";
//                }
            } else {
                if (field.isHelper()) {
                    try {
                        if (i == 0) {
                            makeInitials += callNextIDHelper();
                        } else {
                            makeInitials += ", " + callNextIDHelper();
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(DBAcess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    {
                        if (i == 0) {
                            makeInitials += field.getVariableName();
                        } else {
                            makeInitials += ", " + field.getVariableName();
                        }

                    }
                }

            }

        }

        String construtorInitials = "this." + objectVariableName + " = new " + objectName + "(";

        String simpleCreate = construtorInitials += makeInitials + ");".concat(initialisers);
        String createConstructorSimple = makeMethod("public", "", objectNameDA, construtorLine, simpleCreate);

        String createLine = "String persistenceUnit, " + construtorLine;
        String createInitial = "super(persistenceUnit);\n".concat(simpleCreate);
        String createConstructor = makeMethod("public", "", objectNameDA, createLine, createInitial);

        return noArg + puConstructor + readConstructorSimple + readConstructor + createConstructorSimple + createConstructor;
    }

    private String makeSearchColumn() {
        String searchColumnBody = "";
        searchColumnBody = fields.parallelStream().map((field) -> field.makeSearchColumn()).reduce(searchColumnBody, String::concat);
        searchColumnBody += "this.searchColumns.addAll(this.getDefaultSearchColumns());";

        return Utilities.makeMethod("private", "void", "createSearchColumns", "", searchColumnBody);
    }

    private String initialiseProperties() {
        String constructorBody = "this.dBEntity = " + objectVariableName.concat(";\n");
        constructorBody = fields.stream().map((field) -> field.daInitialiseProperties(objectVariableName)).reduce(constructorBody, String::concat);
        constructorBody += "initCommonProprties();";
        return Utilities.makeMethod("private", "void", "initialseProprties", "", constructorBody);
    }

    public String makeDAProperties() {
        String properties = "";
        properties = fields.stream().map((field) -> field.makeDAProperties(objectName)).reduce(properties, String::concat);

        return properties;
    }

//    public String makeSetters() {
//        String setters = "";
//        for (Field field : fields) {
//            setters += field.makeDaSetter(objectName);
//        }
//        return setters;
//    }
    private String getNextIDHelper() throws Exception {
        FieldDAO idHelperObject = Utilities.getIDHelper(fields);
        FieldDAO idGeneratorObject = Utilities.getIDGenerator(fields);

        if (idGeneratorObject != null && idHelperObject == null) {
            throw new Exception("The ID generator does not have a helper column");
        }
        if (idGeneratorObject == null && idHelperObject == null) {
            return "";
        }
        if (idGeneratorObject == null) {
            return "public final int getNext" + idHelperObject.getFieldName() + "() {\n"
                    + "        return this.getMax(\"" + idHelperObject.getVariableName() + "\") + 1;\n"
                    + "    }\n";
        } else {

            String idGeneratorVariableName = idGeneratorObject.getVariableName();
            String idHelperVariableName = idHelperObject.getVariableName();
            if (idGeneratorObject.isReferance() && !idGeneratorObject.getEnumerated()) {
                return "public final int getNext" + idHelperObject.getFieldName() + "(" + idGeneratorObject.getReferences() + " " + idGeneratorVariableName + ") {\n"
                        + "        return this.getMax(\"" + idHelperVariableName + "\",\"" + idGeneratorVariableName + "\", " + idGeneratorVariableName + ")+1;\n"
                        + "    }\n";
            } else {
                return "public final int getNext" + idHelperObject.getFieldName() + "(Object " + idGeneratorVariableName + ") {\n"
                        + "        return this.getMax( \"" + idHelperVariableName + "\",\"" + idGeneratorVariableName + "\", " + idGeneratorVariableName + ")+1;\n"
                        + "    }\n";
            }

        }

    }

    private String getNextIDColumn() throws Exception {
        FieldDAO idHelperObject = Utilities.getIDHelper(fields);
        FieldDAO idGeneratorObject = Utilities.getIDGenerator(fields);
        FieldDAO primaryKeyObject = Utilities.getPrimaryKey(fields);
        if (idGeneratorObject != null && idHelperObject == null) {
            throw new Exception("The ID generator does not have a helper column");
        }
        if (idGeneratorObject == null && idHelperObject == null) {
            return "";
        }
        if (idGeneratorObject == null) {
            String idHelperVariableName = idHelperObject.getVariableName();
            return "public String getNext" + primaryKeyObject.getFieldName() + "(int " + idHelperVariableName + " ) {\n"
                    + "        return new IDGeneratorDA().getToAppendString(" + objectName + ".class.getSimpleName(), " + idHelperVariableName + ");\n"
                    + "    }\n";
        } else {

            String idGeneratorVariableName = idGeneratorObject.getVariableName();
            String idHelperVariableName = idHelperObject.getVariableName();
            String references = idGeneratorObject.getReferences();
            if (idGeneratorObject.isReferance() && idGeneratorObject.getEnumerated()) {
                return "public String getNext" + primaryKeyObject.getFieldName() + "(int " + idHelperVariableName + ", " + references + " " + idGeneratorVariableName + ") {\n"
                        + "  return new IDGeneratorDA().getToAppendString(" + objectName + ".class.getSimpleName(), (" + idGeneratorVariableName + ".ordinal()+1), " + idHelperVariableName + ");\n"
                        + "    }\n";
            } else {
                return "public String getNext" + primaryKeyObject.getFieldName() + "(int " + idHelperVariableName + ", " + idGeneratorObject.getDataType() + " " + idGeneratorVariableName + ") {\n"
                        + "          return new IDGeneratorDA().getToAppendString(" + objectName + ".class.getSimpleName()," + idGeneratorVariableName + ", " + idHelperVariableName + ");"
                        + "    }\n";
            }

        }

    }

    public String getObjectFromDB() {
        List<String> collectionVariableNames = new ArrayList<>();
        collectionList.forEach(f -> collectionVariableNames.add(f.getVariableName()));

        String body;
        if (collectionVariableNames.isEmpty()) {

            body = "return (" + objectName + ") super.find(" + objectName + ".class, " + primaryKeyVariableName + ");";
        } else if (collectionVariableNames.size() == 1) {

            body = "return (" + objectName + ") super.findJoin(" + objectName + ".class, " + primaryKeyVariableName + ",\"" + collectionVariableNames.get(0) + "\");";
        } else {
            String joinString = "List<String> associatedEntities = new ArrayList();";

            for (String associatedEntity : collectionVariableNames) {
                joinString += "associatedEntities.add(\"" + associatedEntity + "\");";
            }
            body = joinString;
            body += "return (" + objectName + ") super.findJoin(" + objectName + ".class, " + primaryKeyVariableName + ", associatedEntities);";
        }
        return makeMethod("public", objectName, "get" + objectName, primaryKey.getDeclaration(true, false), body);
    }

    private String makeMethods() {
        try {
            String setObject = "public void set" + objectName + "(" + objectName + " " + objectVariableName + ") {\n"
                    + "        this." + objectVariableName + " = " + objectVariableName + ";\n"
                    + "        this.initialseProprties();\n"
                    + "        createSearchColumns();\n"
                    + "    }";

            String equals = "         @Override\n"
                    + "    public boolean equals(Object o) {\n"
                    + "        if (this == o) {\n"
                    + "            return true;\n"
                    + "        }\n"
                    + "        if (!(o instanceof " + objectNameDA + ")) {\n"
                    + "            return false;\n"
                    + "        }\n"
                    + "        \n"
                    + "        " + objectNameDA + " " + objectNameDAVariableName + " = (" + objectNameDA + ") o;\n"
                    + "        \n"
                    + "        return this." + objectVariableName + ".equals(" + objectNameDAVariableName + ".get" + objectName + "());"
                    + "    }";

            String hashCode = " @Override\n"
                    + "            public int hashCode\n"
                    + "            \n"
                    + "                () {\n"
                    + "        return " + objectVariableName + ".hashCode();\n"
                    + "            }";

            String objectNameGet = "public " + objectName + " get" + objectName + "(){\n"
                    + "        return this." + objectVariableName + ";\n"
                    + "    }";

            String dagetSearchColumns = " @Override\n"
                    + "    public List<SearchColumn> getSearchColumns() {\n"
                    + "       return this.searchColumns;\n"
                    + "    }\n";

            String getID = " @Override\n"
                    + "    public Object getId() {\n"
                    + "        return this." + objectVariableName + ".getId();\n"
                    + "    }";

            String getDisplayKey = "@Override\n"
                    + "    public String getDisplayKey() {\n"
                    + "        return this." + objectVariableName + ".getDisplayKey();\n"
                    + "    }";

            String daGetValue = " public List<" + objectNameDA + "> get(String columName, Object value) {\n"
                    + "        List<" + objectNameDA + "> list = new ArrayList<>();\n"
                    + "        super.selectQuery(" + objectName + ".class, columName, value).forEach(da -> list.add(new " + objectNameDA + "((" + objectName + ") da)));\n"
                    + "        if (entityManager != null) {\n"
                    + "            entityManager.close();\n"
                    + "        }\n"
                    + "        return list;\n"
                    + "    }";

            String mConvert = " public static List<" + objectName + "> get" + objectName + "List(List<" + objectNameDA + "> " + Utilities.getVariableName(objectNameDA) + "s){\n"
                    + "       List<" + objectName + "> " + objectVariableName + "s = new ArrayList<>();\n"
                    + "       " + Utilities.getVariableName(objectNameDA) + "s.forEach(a->" + objectVariableName + "s.add(a." + objectVariableName + "));\n"
                    + "       return " + objectVariableName + "s;\n"
                    + "    }";

            String dConvert = " public static List<" + objectNameDA + "> get" + objectNameDA + "s(List<" + objectName + "> " + objectVariableName + "s) {\n"
                    + "        List<" + objectNameDA + "> list = new ArrayList<>();\n"
                    + "        " + objectVariableName + "s.forEach((" + objectVariableName + ") -> {\n"
                    + "            list.add(new " + objectNameDA + "(" + objectVariableName + "));\n"
                    + "        });\n"
                    + "        return list;\n"
                    + "    }";

            String daSave = "public boolean save() throws Exception{\n"
                    + "if (!isValid()) {\n"
                    + "            return false;\n"
                    + "        }\n"
                    + " return super.persist(this." + objectVariableName + ");\n"
                    + "\n"
                    + "}\n"
                    + "";
            String daUpdate = " public boolean update() throws Exception{\n"
                    + "if (!isValid()) {\n"
                    + "            return false;\n"
                    + "        }\n"
                    + "return super.merge(this." + objectVariableName + ");\n"
                    + "\n"
                    + "}\n"
                    + "";

            String daDelete = " public boolean delete() {\n"
                    + "return super.remove(this." + objectVariableName + ");\n"
                    + "\n"
                    + "}\n"
                    + "";

            String dagetAllObject = "public List<" + objectName + "> get" + objectName + "s() {\n"
                    + "return super.find(" + objectName + ".class);\n"
                    + "}";

            String getObjectName = getObjectFromDB();

            String dagetAll = " @Override\n"
                    + "    public List<" + superClass + "> get() {\n"
                    + "        List<" + superClass + "> list = new ArrayList<>();\n"
                    + "        selectQuery(" + objectName + ".class).forEach(o -> list.add(new " + objectNameDA + "((" + objectName + ") o)));\n"
                    + "        if (entityManager != null) {\n"
                    + "            entityManager.close();\n"
                    + "        }\n"
                    + "        return list;\n"
                    + "    }";

            String daget = "public " + objectNameDA + " get(" + primaryKey.getDeclaration(true, false) + ") throws Exception {\n"
                    + "       " + objectName + " o" + objectName + " = get" + objectName + "(" + primaryKeyVariableName + ");\n"
                    + "       if(o" + objectName + " == null) throw new Exception(\"No Record with id: \"+" + primaryKeyVariableName + ");\n"
                    + "        return new " + objectNameDA + "(o" + objectName + ");\n"
                    + "    }\n";

            String getByColName = "public List<" + objectName + "> get" + objectName + "s(String columName, Object value) {\n"
                    + "        return  super.find(" + objectName + ".class, columName, value);}\n";

            String daMax = " public int getMax(String columnName) {\n"
                    + "return super.getMax(" + objectName + ".class, columnName);\n"
                    + "}\n";

            String daMax1 = "public int getMax(String columnName, String comparatorColumn, Object comparatorVaue) {\n"
                    + "        return super.getMax(" + objectName + ".class, columnName, comparatorColumn, comparatorVaue);\n"
                    + "    }\n";

            String todaList = "public List<" + objectNameDA + "> toDaList(List<" + objectName + "> " + objectVariableName + "s) {\n"
                    + "        List<" + objectNameDA + "> " + objectNameDAVariableName + "s = new ArrayList<>();\n"
                    + "        " + objectVariableName + "s.forEach(s -> " + objectNameDAVariableName + "s.add(new " + objectNameDA + "(s)));\n"
                    + "        return " + objectNameDAVariableName + "s;\n"
                    + "    }";

            String toDBAccesList = "public List<DBAccess> toDBAccessList(List<" + objectName + "> " + objectVariableName + "s) {\n"
                    + "        List<DBAccess> dbAccesses = new ArrayList<>();\n"
                    + "        " + objectVariableName + "s.forEach(s -> dbAccesses.add(new " + objectNameDA + "(s)));\n"
                    + "        return dbAccesses;\n"
                    + "    }";
            String revisions = " @Override\n"
                    + "    public List<DBAccess> getRevisions() {\n"
                    + "        try {\n"
                    + "\n"
                    + "            List<Object[]> list = getEntityRevisions(" + objectName + ".class);\n"
                    + "            List<DBAccess> dBAccesses = new ArrayList<>();\n"
                    + "            list.forEach(e -> {\n"
                    + "\n"
                    + "                " + objectNameDA + " " + objectNameDAVariableName + " = new " + objectNameDA + "((" + objectName + ") e[0]);\n"
                    + "                " + objectNameDAVariableName + ".revisionEntity = (AppRevisionEntity) e[1];\n"
                    + "                " + objectNameDAVariableName + ".oRevisionType = (RevisionType) e[2];\n"
                    + "                " + objectNameDAVariableName + ".initRevProprties();\n"
                    + "                " + objectNameDAVariableName + ".searchColumns.addAll(" + objectNameDAVariableName + ".getRevSearchColumns());\n"
                    + "                dBAccesses.add(" + objectNameDAVariableName + ");\n"
                    + "\n"
                    + "            });\n"
                    + "\n"
                    + "            return dBAccesses;\n"
                    + "        } catch (Exception e) {\n"
                    + "            throw e;\n"
                    + "        } finally {\n"
                    + "            if (entityManager == null) {\n"
                    + "                entityManager.close();\n"
                    + "            }\n"
                    + "        }\n"
                    + "\n"
                    + "    }";

            List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                    .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());

            List<FieldDAO> unique = fields.stream().filter((p) -> p.getKey()
                    .equalsIgnoreCase(Enums.keys.Unique.name())).collect(Collectors.toList());
            String validateBody = "";
            if (!uniqueGroups.isEmpty()) {
                String message = "\"The record with ";
                String uniqueGroupValidate = "List<SearchColumn> lSearchColumns = new ArrayList<>();";
                int last = uniqueGroups.size() - 1;
                for (int i = 0; i <= last; i++) {
                    FieldDAO fdao = uniqueGroups.get(i);
                    uniqueGroupValidate += "lSearchColumns.add(new SearchColumn(\"" + fdao.getVariableName() + "\", " + objectVariableName + "." + fdao.getCall() + ", SearchColumn.SearchType.Equal));";
                    message += "" + fdao.getCaption() + ": \"+" + objectVariableName + "." + fdao.getCall() + fdao.getReferenceDisplayText() + "+";

                    if (i != last) {
                        message += " \" and ";
                    }
                }
                message += "\"already exists\"";
                uniqueGroupValidate += "List<" + objectName + "> l" + objectName + " = super.find(" + objectName + ".class, lSearchColumns);\n"
                        + "l" + objectName + ".removeIf((p)->p.getId().equals(" + objectVariableName + ".getId()));\n"
                        + "        if (l" + objectName + " .size() > 0) {\n"
                        + "            throw new Exception(" + message + ");\n"
                        + "        }";
                validateBody += uniqueGroupValidate;
            }

            String uniqueValidate = "";
            uniqueValidate = unique.stream().map((fdao) -> "List<" + objectName + " > " + objectVariableName + fdao.getFieldName() + "s"
                    + "= this.get" + objectName + "s(\"" + fdao.getVariableName() + "\", " + objectVariableName + "." + fdao.getCall() + ");\n"
                    + "        " + objectVariableName + fdao.getFieldName() + "s.removeIf((p)->p.getId().equals(" + objectVariableName + ".getId()));\n"
                    + "        if (" + objectVariableName + fdao.getFieldName() + "s.size() > 0) {\n"
                    + "            throw new Exception(\"There is already an existing record  with the " + fdao.getCaption() + ": \" + " + objectVariableName + "." + fdao.getCall() + fdao.getReferenceDisplayText() + ");\n"
                    + "        }\n").reduce(uniqueValidate, String::concat);
            validateBody += uniqueValidate;
            validateBody += "return true;";
            String validateMethod = Utilities.makeThrowsMethod("public", "boolean", "isValid", "", validateBody);

            String daNextIDHelper = getNextIDHelper();
            String daNextPrimaryKey = getNextIDColumn();
            return setObject + equals + hashCode + initialiseProperties() + makeSearchColumn() + dagetSearchColumns + getID + getDisplayKey + dConvert + mConvert + daSave + daUpdate + daDelete + getObjectName + objectNameGet + dagetAllObject + dagetAll
                    + daget + daGetValue + todaList + toDBAccesList + daMax + daMax1 + daNextIDHelper + daNextPrimaryKey + getByColName + validateMethod + revisions + "\n";
        } catch (Exception ex) {
            Logger.getLogger(DBAcess.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public String makeClass(Project currentProject) throws Exception {
        validate(fields);
        JavaClass javaClass = new JavaClass(currentProject.getDBAccessPackage(), objectNameDA, this.makeImports(currentProject),
                this.makeProperties(), this.makeConstructor(), this.makeDAProperties(), makeMethods());
        return javaClass.makeClass("DBAccess");
    }

}
