/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.utils;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Field;
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
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import com.saburi.smartcoder.FieldPredicates;

/**
 *
 * @author ClinicMaster13
 */
public class Utilities {

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
    
    public boolean fileExists(String fileName){
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

    public static void writeFileAppend(String fileName, String content) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.append(content);
            writer.newLine();

            writer.close();
        } catch (FileNotFoundException ex) {
            errorMessage(ex);
        } catch (IOException ex) {
            errorMessage(ex);
        }

    }

    public static void openFile(String fileName) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + fileName);
        } catch (IOException ex) {
            errorMessage(ex);
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

    public static Field getFields(String line, String seperator) throws Exception {

        if (!(line.isEmpty())) {
            try {

                String[] values = line.split(seperator);
                int length = values.length;

                switch (length) {
                    case 3: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim());
                    }
                    case 4: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim());

                    }
                    case 5: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(),
                                values[4].trim());

                    }
                    case 6: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(), values[5].trim());

                    }
                    case 7: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(), values[5].trim(), values[6].trim());

                    }
                    case 8: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(), values[5].trim(), values[6].trim(), values[7].trim());

                    }
                    case 9: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(),
                                Integer.valueOf(values[8].trim()));

                    }
                    case 10: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()), Boolean.valueOf(values[9].trim()));

                    }

                    case 11: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()), Boolean.valueOf(values[9].trim()), Boolean.valueOf(values[10].trim()));

                    }
                    case 12: {
                        return new Field(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim(), values[4].trim(),
                                values[5].trim(), values[6].trim(), values[7].trim(), Integer.valueOf(values[8].trim()), Boolean.valueOf(values[9].trim()), Boolean.valueOf(values[10].trim()), values[11].trim());

                    }
                    default:
                        throw new Exception("Invalid file content (" + line + "). Required: 3 to 11 fields, Found :" + length);

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
        FilteredList<FieldDAO> helperList = new FilteredList<>(FXCollections.observableList(fields), e -> true);
        helperList.setPredicate(FieldPredicates.isHelper());
        return helperList.size() > 0;

    }

    public static String getVariableName(String fieldName) {
        return fieldName.substring(0, 1).toLowerCase().concat(fieldName.substring(1, fieldName.length()));
    }

    public static FieldDAO getPrimaryKey(List<FieldDAO> fields) {
        for (FieldDAO field : fields) {
            if (field.getKey().equalsIgnoreCase(Enums.keys.Primary.name())) {
                return field;
            }
        }
        return null;
    }

    public static FieldDAO getIDHelper(List<FieldDAO> fields) {
        for (FieldDAO field : fields) {
            if (field.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.ID_Helper.name())) {
                return field;
            }
        }
        return null;
    }

    public static FieldDAO getIDGenerator(List<FieldDAO> fields) {
        for (FieldDAO field : fields) {
            if (field.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.ID_Generator.name())) {
                return field;
            }
        }
        return null;
    }

    public static String makeTable(String objectName, String tableColumn, String contextMenu) {
        return "<TableView fx:id=\"tbl" + objectName + "\" id = \"" + objectName + "\" VBox.vgrow=\"ALWAYS\" minHeight=\"300\" minWidth=\"450\">\n"
                + "<padding>\n"
                + "<Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" top=\"10.0\" />\n"
                + "</padding>\n"
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

}
