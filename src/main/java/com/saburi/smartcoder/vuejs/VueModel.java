/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.vuejs;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Project;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import static com.saburi.utils.Utilities.toPlural;
import java.util.List;

/**
 *
 * @author samburiima
 */
public class VueModel {

    private final String objectName;
    private final List<FieldDAO> fields;
    private final String objectNameVariable;
    private final String modelName;
    private final Project project;

    public VueModel(String objectName, Project project, List<FieldDAO> fields) {
        this.objectName = objectName;
        this.project = project;
        this.objectNameVariable = Utilities.getVariableName(this.objectName);
        this.fields = fields;
        this.modelName = this.objectNameVariable.concat("Model");
    }

    private boolean forceReferences(FieldDAO fieldDAO) {
        String proiectName = fieldDAO.getProjectName();
        return fieldDAO.isReferance() && (proiectName.equalsIgnoreCase(this.project.getProjectName()) || isNullOrEmpty(proiectName));
    }

    private String makeLine(FieldDAO fieldDAO, String separater, String begin, String endLiteral) {
        if (fieldDAO.getDataType().equalsIgnoreCase("boolean") || fieldDAO.getDataType().equalsIgnoreCase("bool")) {
            return begin + fieldDAO.getVariableName().concat(separater).concat("false").concat(endLiteral);
        } else if (fieldDAO.getDataType().equalsIgnoreCase("Image")) {
            return begin + fieldDAO.getVariableName().concat(separater).concat("null").concat(endLiteral);
        } else if (fieldDAO.isCollection()) {
            return begin + fieldDAO.getVariableName().concat(separater).concat("[]").concat(endLiteral);
        } else if (this.forceReferences(fieldDAO) && !fieldDAO.getEnumerated()) {
            return begin + fieldDAO.getVariableName().concat("Id").concat(separater).concat("\"\"").concat(endLiteral)
                    .concat("\n")
                    .concat(begin + fieldDAO.getVariableName().concat(separater).concat("null").concat(endLiteral));
        }
        return begin + fieldDAO.getVariableName().concat(separater).concat("\"\"").concat(endLiteral);
    }

    private String makeInitialLines() {
        String intials = "";
        String separater = ":";
        String begin = "";
        String endLiteral = ",";
        intials = fields.stream()
                .filter(p -> !p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Query_Only.name()))
                .map(fieldDAO -> makeLine(fieldDAO, separater, begin, endLiteral)
                .concat("\n"))
                .reduce(intials, String::concat);
        return intials;
    }

    private String makeClearLines() {
        String intials = "";
        String separater = "=";
        String begin = "this.";
        String endLiteral = ";";
        intials = fields.stream()
                .filter(p -> !p.getSaburiKey()
                .equalsIgnoreCase(Enums.Saburikeys.Query_Only.name()))
                .map(fieldDAO -> makeLine(fieldDAO, separater, begin, endLiteral)
                .concat("\n"))
                .reduce(intials, String::concat);
        return intials;
    }

    public String create() {
        return "const " + this.modelName + " = {\n "
                + objectNameVariable + ": {\n" + makeInitialLines().concat("\n")
                        .concat("clear(){\n").concat(makeClearLines()).concat("}\n")
                        .concat("},\n")
                        .concat("path:").concat("\"" + toPlural(objectName).toLowerCase() + "\"")
                        .concat("}\n")
                        .concat("\n")
                        .concat("export default ").concat(modelName).concat(";");

    }

}
