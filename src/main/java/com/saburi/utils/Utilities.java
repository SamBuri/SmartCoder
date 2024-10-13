/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.utils;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.model.Field;
import com.saburi.model.Project;
import static com.saburi.utils.FXUIUtils.errorMessage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.saburi.smartcoder.FieldPredicates;
import com.saburi.utils.Enums.EntityTypes;
import com.saburi.utils.Enums.Saburikeys;
import com.saburi.utils.Enums.ServiceTypes;

/**
 *
 * @author ClinicMaster13
 */
public class Utilities {

    public static String RESPONSE_INERFACE = "ResponseData";

    public static List readFile(String fileName, String seperator) {
        List<List> bigList = new ArrayList<>();
        try {

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    Vector innerList = new Vector();
                    String[] values = line.split(seperator);
//                display(values);
                    innerList.addAll(Arrays.asList(values));
                    bigList.add(innerList);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            errorMessage(ex);
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            errorMessage(ex);
        }

        return bigList;
    }

    public static void writeFile(String fileName, Object[] value) {

        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (Object s : value) {
                    writer.append(s.toString());
                    writer.newLine();
                }
            }
        } catch (FileNotFoundException ex) {
            errorMessage(ex);
        } catch (IOException ex) {
            errorMessage(ex);
        }

    }

    public static void writeFile(String fileName, String content) {

        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

                writer.write(content);
                writer.newLine();
            }
        } catch (FileNotFoundException ex) {
            errorMessage(ex);
        } catch (IOException ex) {
            errorMessage(ex);
        }

    }

    public boolean fileExists(String fileName) {
        try {
            File file = new File(fileName);
            return file.exists();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void makeDirectory(String dir) {

        File f = new File(dir);
        f.setExecutable(true, true);
        f.setWritable(true, true);
        f.setReadable(true, true);

        if (!f.exists()) {
            f.mkdirs();

        }

    }

    public static void writeFileAppend(String fileName, String content) throws FileNotFoundException, IOException {

        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.append(content);
                writer.newLine();
            }
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        }

    }

    public static List addIfNotExists(List list, Object o) {
        if (!list.contains(o)) {
            list.add(o);
        }
        return list;
    }

    public static String makeMethod(String visibility, String returnType, String methodName, String parameters,
            String body) {

        return visibility + " " + returnType + " " + methodName + "(" + parameters + "){\n"
                + " " + body + "\n"
                + "}\n";
    }

    public static String makeMethodJs(String visibility, String returnType, String methodName, String parameters,
            String body) {

        return visibility + " " + returnType + " " + methodName + "(" + parameters + "){\n"
                + " " + body + "\n"
                + "},\n";
    }

    public static String makeThrowsMethod(String visibility, String returnType, String methodName, String parameters,
            String body) {

        return visibility + " " + returnType + " " + methodName + "(" + parameters + ")throws Exception{\n"
                + " " + body + "\n"
                + "}\n";
    }

    public static String makeTryMethod(String visibility, String returnType, String methodName, String parameters,
            String body) {

        return visibility + " " + returnType + " " + methodName + "(" + parameters + "){\n"
                + "try{\n " + body + ""
                + "}catch(Exception e){"
                + "errorMessage(e);"
                + "}finally{}\n"
                + "}\n";
    }

    public static String makeTryMethod(String visibility, String returnType, String methodName, String parameters,
            String body, String exceptionType) {

        return visibility + " " + returnType + " " + methodName + "(" + parameters + "){\n"
                + "try{\n " + body + ""
                + "}catch(" + exceptionType + " e){"
                + "errorMessage(e);"
                + "}finally{}\n"
                + "}\n";
    }

    public static String makeAbstractMethod(String visibility, String returnType, String methodName, String parameters) {

        return "\n" + visibility + " " + returnType + " " + methodName + "(" + parameters + ");\n";
    }

    public static String makeAnnotedAbstractMethod(String Annotations, String visibility, String returnType, String methodName, String parameters) {

        return Annotations + "\n" + visibility + " " + returnType + " " + methodName + "(" + parameters + ");\n";
    }

    public static Field getFields(String line, String seperator) throws Exception {

        if (!(line.isEmpty())) {
            try {

                String[] values = line.split(seperator);
                int length = values.length;

                switch (length) {
                    case 3 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim());
                    }
                    case 4 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim());

                    }
                    case 5 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(),
                                values[4].trim());

                    }
                    case 6 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(), values[5].trim());

                    }
                    case 7 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(), values[5].trim(), values[6].trim());

                    }
                    case 8 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(), values[5].trim(), values[6].trim(), values[7].trim());

                    }
                    case 9 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(),
                                Integer.valueOf(values[8].trim()));

                    }
                    case 10 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()), Boolean.valueOf(values[9].trim()));

                    }

                    case 11 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()), Boolean.valueOf(values[9].trim()), Boolean.valueOf(values[10].trim()));

                    }
                    case 12 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()), Boolean.valueOf(values[9].trim()), Boolean.valueOf(values[10].trim()), values[11].trim());

                    }

                    case 13 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()),
                                Boolean.valueOf(values[9].trim()), Boolean.valueOf(values[10].trim()), values[11].trim(),
                                Boolean.valueOf(values[12].trim()));

                    }

                    case 14 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()),
                                Boolean.valueOf(values[9].trim()), Boolean.valueOf(values[10].trim()), values[11].trim(),
                                Boolean.valueOf(values[12].trim()), values[13].trim());

                    }

                    case 15 -> {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()),
                                Boolean.valueOf(values[9].trim()), Boolean.valueOf(values[10].trim()), values[11].trim(),
                                Boolean.valueOf(values[12].trim()), values[13].trim(),
                                Boolean.valueOf(values[14].trim()));

                    }
                    default ->
                        throw new Exception("Invalid file content (" + line + "). Required: 3 to 14 fields, Found :" + length);

                }
            } catch (IOException e) {
                throw e;

            } catch (Exception e) {
                throw e;
            }
        }
        return null;

    }

    public static boolean hasHelper(List<FieldDAO> fields) {
//        FilteredList<FieldDAO> helperList = new FilteredList<>(FXCollections.observableList(fields), e -> true);
//        helperList.setPredicate(FieldPredicates.isHelper());
        return fields.stream().filter(FieldPredicates.isHelper()).count() > 0;
//        return helperList.size() > 0;

    }

    public static boolean hasMultipart(List<FieldDAO> fields) {
        return fields.stream()
                .filter(p -> p.getDataType().equalsIgnoreCase("Image"))
                .count() > 0;

    }

    public static String getVariableName(String fieldName) {
        return fieldName.trim().substring(0, 1).toLowerCase().concat(fieldName.substring(1, fieldName.length()));

    }

    public static FieldDAO getPrimaryKey(List<FieldDAO> fields) {
        for (FieldDAO field : fields) {
            if (field.getKey().equalsIgnoreCase(Enums.keys.Primary.name())) {
                return field;
            } else if (field.getKey().equalsIgnoreCase(Enums.keys.Primary_Auto.name())) {
                return field;
            }
        }
        return null;
    }

    public static FieldDAO getIDHelper(List<FieldDAO> fields) {
        for (FieldDAO field : fields) {
            if (field.getSaburiKey().equalsIgnoreCase(Saburikeys.ID_Helper.name())) {
                return field;
            }
        }
        return null;
    }

    public static FieldDAO getIDGenerator(List<FieldDAO> fields) {
        for (FieldDAO field : fields) {
            if (field.getSaburiKey().equalsIgnoreCase(Saburikeys.ID_Generator.name())) {
                return field;
            }
        }
        return null;
    }

    public static String makeTable(String objectName, String tableColumn, String contextMenu) {
        return "<TableView fx:id=\"tbl" + objectName + "\" id = \"" + objectName + "\" VBox.vgrow=\"ALWAYS\" minHeight=\"300\" minWidth=\"450\">\n"
                + "<columns>".concat(tableColumn).concat("</columns>\n").concat(contextMenu)
                + "</TableView>\n";
    }

    public static boolean isInteger(Object o) {
        if (o == null) {

            return false;
        } else {
            try {
                Integer.parseInt(o.toString());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    public static int getInt(String projectID) {
        if (isInteger(projectID)) {
            return Integer.parseInt(projectID);
        } else {
            return 0;
        }
    }

    public static boolean isNullOrEmpty(String string) {
        if (string == null) {
            return true;
        }
        return string.isEmpty() || string.equals("");
    }

    public static int defortInteger(String number) {

        return Integer.parseInt(number.replaceAll("(?<=\\d),(?=\\d)|\\$", ""));

    }

    public static String toPlural(String string) {

//        List<String> plurals = List.of("info", "information", "s");
//        if (plurals.stream().filter(s -> string.toLowerCase().endsWith(s)).count() > 0) {
//            return string;
//        } else if (string.endsWith("y")) {
//            int lastIndex = string.length() - 1;
//            return string.substring(0, lastIndex).concat("ies");
//        } else {
//            return string.concat("s");
//        }
        return Pluralizer.pluralize(string);

    }

    public static String toSingular(String string) {

        return Pluralizer.singularize(string);

    }

//    public static String getTableName(String tableName){
//    }
    public static String getParentEntity(EntityTypes entityType) {
        return entityType.equals(Enums.EntityTypes.Auto_ID_Int) ? "DBEntityIncID"
                : entityType.equals(Enums.EntityTypes.Auto_ID_Long) ? "DBEntityIncID"
                : entityType.equals(Enums.EntityTypes.Auto_ID_Gen) ? "DBEntityGenID" : "DBEntity";
    }

    public static String getFullParentEntity(EntityTypes entityType) {
        return entityType.equals(Enums.EntityTypes.Auto_ID_Int) ? "DBEntityIncID<Integer>"
                : entityType.equals(Enums.EntityTypes.Auto_ID_Long) ? "DBEntityIncID<Long>"
                : entityType.equals(Enums.EntityTypes.Auto_ID_Gen) ? "DBEntityGenID" : "DBEntity<String>";
    }

    public static String getIdWrapperDataType(EntityTypes entityType) {
        return entityType.equals(Enums.EntityTypes.Auto_ID_Int) ? "Integer"
                : entityType.equals(Enums.EntityTypes.Auto_ID_Long) ? "Long"
                : entityType.equals(Enums.EntityTypes.Auto_ID_Gen) ? "String" : "String";
    }

    public static String getParentService(ServiceTypes serviceTypes) {
        return serviceTypes.equals(ServiceTypes.Base) ? "BaseService"
                : serviceTypes.equals(ServiceTypes.ID_Gen) ? "BaseServiceIDGen"
                : serviceTypes.equals(ServiceTypes.Read_Only) ? "ReadOnlyService" : "BaseService";
    }

    public static String getControllerType(ServiceTypes serviceTypes, boolean hasMultipart) {
        if (hasMultipart) {
            return "FormDataController";
        }
        return serviceTypes.equals(Enums.ServiceTypes.Read_Only) ? "ReadOnlyController" : "BaseController";
    }

    public static String makeResponseImport(Project project) throws Exception {
        Project parentProject = new ProjectDAO().get(project.getCommonProjectName());
        parentProject = parentProject == null ? project : parentProject;
        return "import " + parentProject.getBasePackage() + ".dtos." + RESPONSE_INERFACE + ";\n";

    }

    public static String getDataTypeImps(FieldDAO fieldDAO) {
        String dataType = fieldDAO.getDataType();
        if (dataType.equalsIgnoreCase("LocalDate")) {
            return "import java.time.LocalDate;\n";
        }
        if (dataType.equalsIgnoreCase("LocalDateTime")) {
            return "import java.time.LocalDateTime;\n";
        }
        if (dataType.equalsIgnoreCase("Date")) {
            return "import java.util.Date;\n";
        }
        if (dataType.equalsIgnoreCase("DateTime;\n")) {
            return "import java.util.DateTime;\n";
        }
        return "";
    }

    public static String getNonPrimitiveDataTypeImports(List<FieldDAO> fieldDAOs) {
        String v = "";
        v = fieldDAOs
                .stream()
                .map(f -> getDataTypeImps(f))
                .distinct()
                .reduce(v, String::concat);
        return v;
    }

    public static String getCaption(String string) {
        if (isNullOrEmpty(string)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(Character.toUpperCase(string.charAt(0)));

        for (char c : string.substring(1, string.length()).toCharArray()) {
            if (Character.isLowerCase(c)) {
                builder.append(c);
            } else {
                builder.append(" ").append(c);
            }

        }
        return builder.toString();
    }

}
