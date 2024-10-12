/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.vue3;

import com.saburi.dataacess.FieldDAO;
import com.saburi.smartcoder.FileModel;
import com.saburi.utils.Enums;
import static com.saburi.utils.Utilities.toPlural;

/**
 *
 * @author samburiima
 */
public class VueModel extends Vue3Utils{
    
//    private final String objectCaption;
    private final String modelName;
//    private final Project project;
    
    public VueModel(FileModel fileModel) {
       super(fileModel);
       
        this.modelName = this.objectVariableName.concat("Model");
    }
    
//    private boolean forceReferences(FieldDAO fieldDAO) {
//        String proiectName = fieldDAO.getProjectName();
//        return fieldDAO.isReferance() && (proiectName.equalsIgnoreCase(this.project.getProjectName()) || isNullOrEmpty(proiectName));
//    }
    
    private String makeLine(FieldDAO fieldDAO, String separater, String begin, String endLiteral) {
        String variableName = getVariableName(fieldDAO);
        if (fieldDAO.getDataType().equalsIgnoreCase("boolean") || fieldDAO.getDataType().equalsIgnoreCase("bool")) {
            return begin + variableName.concat(separater).concat("false").concat(endLiteral);
        } else if (fieldDAO.getDataType().equalsIgnoreCase("Image")||fieldDAO.isDate()) {
            return begin + variableName.concat(separater).concat("null").concat(endLiteral);
        } else if (fieldDAO.isCollection()) {
            return begin + fieldDAO.getVariableName().concat(separater).concat("[]").concat(endLiteral);
        } 
        return begin + variableName.concat(separater).concat("\"\"").concat(endLiteral);
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
    
   
      private String makeCoyLine(FieldDAO fieldDAO){
         
        return String.format("this.%s = obj.%s;", getVariableName(fieldDAO), getFieldPath(fieldDAO));
    };
    
         private String getFieldPathDisplay(FieldDAO f){
      if(f.isReferance() && forceReferences(f)) return f.getVariableName().concat(".displayKey");
      return f.getVariableName();
    }
    private String makeCopyMethod() {
        String copyLines = "this.id=obj.id;\n";
       
        copyLines = fields.stream()
                .filter(p -> !p.getSaburiKey()
                .equalsIgnoreCase(Enums.Saburikeys.Query_Only.name()))
                .map(fieldDAO -> makeCoyLine(fieldDAO)
                .concat("\n"))
                .reduce(copyLines, String::concat);
        return String.format("copy(obj){\n%s\n},", copyLines);
    }
    
     private String makePrintLine(FieldDAO fieldDAO) {

        return "data.push({ text: \"" + fieldDAO.getCaption() + "\", value: this." + objectVariableName + "." + getFieldPathDisplay(fieldDAO) + " });\n";
    }
    
     private String printOptions() {

        String printLines = "";
        printLines = this.fields.stream()
                .filter(f -> !f.getDataType().equalsIgnoreCase("boolean"))
                .map(f -> this.makePrintLine(f))
                .reduce(printLines, String::concat);
        String printData = "let data = [];\n" + printLines + "\n";
        String mtdBody = printData + " return {\n"
                + "        data: data,\n"
                + "        startXPos: 10,\n"
                + "        startYPos: 25,\n"
                + "        lineBreak: 4,\n"
                + "        hSpace: 50,\n"
                + "        vSpace: 10,\n"
                + "        title: \"" + fileModel.getObjectCaption() + "\"\n"
                + "\n"
                + "      };\n";

        return "printOptions(){" + mtdBody + "},\n";
    }
     
      public String makeFormDataLine(FieldDAO fieldDAO, String variable) {
        return variable + ".append(\"" + getVariableName(fieldDAO)+ "\", this." + getVariableName(fieldDAO) + ");\n";

    }

    private String makeFormDataLines(String variable) {
        String lines = "";
        lines = this.fields.stream()
                .filter(p -> !p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Query_Only.name()))
                .map(f -> makeFormDataLine(f, variable)).reduce(lines, String::concat);
        return lines;
    }

    public String getFormDataMtd() {
        return this.hasMuiltipart ? "getFormData() {\n"
                + "      var data = new FormData();\n" + makeFormDataLines("data") + "\n"
                + "      return data;\n"
                + "    }," : "";
    }
    

    
    @Override
    public String create() {
        return "const " + this.modelName + " = {\n "
                + "model: {\n" + makeInitialLines().concat("\n")
                        .concat("clear(){\n").concat(makeClearLines()).concat("},\n")
                        .concat(makeCopyMethod()).concat("\n")
                        .concat(printOptions()).concat("\n")
                        .concat(getFormDataMtd()).concat("\n")
                        .concat("},\n")
                        .concat("path:").concat("\"" + toPlural(objectName).toLowerCase() + "\"")
                        .concat(",\n")
                        .concat("rules: {\n" + makeRules() + "\n}\n")
                        .concat("}\n")
                        .concat("\n")
                        .concat("export default ").concat(modelName).concat(";");
        
    }
    
    private String getLengthRule(FieldDAO fieldDAO) {
        return (fieldDAO.getDataType().equalsIgnoreCase("String") && !fieldDAO.isReferance())
                ? "(v) => v.length < " + fieldDAO.getSize() + " || \"" + fieldDAO.getCaption() + " length must be "
                + "less or equal to " + fieldDAO.getSize() + "\"," : "";
    }
    
    private String rules(FieldDAO fieldDAO) {
        if (fieldDAO.getControlType().equals(Enums.UIControls.CheckBox)) {
            return "";
        }
        String rules = getVariableName(fieldDAO) + ":[(v) => !!v || \"" + fieldDAO.getCaption() + " is required\",\n";
        if (fieldDAO.getDataType().equalsIgnoreCase("String")) {
            rules += getLengthRule(fieldDAO);
        }
        
        rules += " ],";
        
        return rules;
    }
    
    private String makeRules() {
        String rules = "";
        rules = fields.stream().
                filter(p -> !p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Query_Only.name()))
                .map(fieldDAO -> rules(fieldDAO)).reduce(rules, String::concat);
        return rules.concat("\n");
    }

   

    @Override
    protected String getFileName() {
       return this.objectName.concat("Model");
    }

    @Override
    protected String getFileExtension() {
        return "js";
    }
    
}
