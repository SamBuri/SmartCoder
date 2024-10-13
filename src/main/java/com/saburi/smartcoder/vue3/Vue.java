/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.vue3;

import com.saburi.dataacess.FieldDAO;
import com.saburi.smartcoder.FileModel;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.UIControls;
import static com.saburi.utils.Enums.UIControls.SNumberInput;
import static com.saburi.utils.Enums.UIControls.SelectField;
import com.saburi.utils.Utilities;

/**
 *
 * @author samburiima
 */
public class Vue extends Vue3Utils {

    private final String modelName;

    

    public Vue(FileModel fileModel) {
        super(fileModel);

        this.modelName = objectVariableName.concat("Model");
       
    }

  

    private UIControls getUIControl(FieldDAO fieldDAO) {
        UIControls uIControl = fieldDAO.getControlType();
        if (uIControl.equals(UIControls.TextArea) && fieldDAO.isCollection()&!fieldDAO.isSelect()) {
            return UIControls.MultiSelectCombo;
        }
        
        if (uIControl.equals(UIControls.TextArea) && fieldDAO.isCollection()&fieldDAO.isSelect()) {
            return UIControls.MultiSelectField;
        }
        else if (uIControl.equals(UIControls.TextField) && fieldDAO.isNumeric()) {
            return UIControls.SNumberInput;
        } else if (uIControl.equals(UIControls.ComboBox) && fieldDAO.isSelect()) {
            System.out.println("Field Name: "+fieldDAO.getColumnName());
            return UIControls.SelectField;
        }
        
        return uIControl;
    }

    private String makeControl(FieldDAO fieldDAO) {
        UIControls uIControl = getUIControl(fieldDAO);

        return switch (uIControl) {
            case ComboBox ->
                "<s-autocomplete".concat(comboProperties(fieldDAO)).concat("></s-autocomplete>\n");
            case MultiSelectCombo ->
                "<v-autocomplete".concat(multiSelectComboProperties(fieldDAO)).concat("></v-autocomplete>\n");
            case CheckBox ->
                "<v-checkbox".concat(noValidationProps(fieldDAO)).concat("></v-checkbox>\n");
            case TextArea ->
                "<s-textarea\n" + textArea(fieldDAO) + "></s-textarea>\n";
            case TableView ->
                this.crudTable(fieldDAO);
            case DatePicker ->
                " <s-date-picker" + this.basicNoCounterProperties(fieldDAO)+ "/>";
            case ImageView ->
                "<s-file-input\n" + fileInput(fieldDAO) + "></s-file-input>\n";

            case SNumberInput ->
                "<s-number-input\n" + basicProperties(fieldDAO) + "></s-number-input>\n";

            case SelectField ->
                "<s-select-field\n" + selectField(fieldDAO) + "></s-select-field>\n";
                
                 case MultiSelectField ->
                "<s-multi-select-field\n" + selectField(fieldDAO) + "></s-multi-select-field>\n";

            default ->
                "<s-text-field\n" + basicProperties(fieldDAO) + "></s-text-field>\n";
        };

    }

    private String makeColumnScales(FieldDAO fieldDAO) {
        return fieldDAO.getControlType().equals(UIControls.TableView) ? "<v-col cols=\"12\">\n" : "<v-col :cols=\"cols\" :sm=\"sm\" :md=\"md\">\n";
    }

    private String controlColumn(FieldDAO fieldDAO) {
        return makeColumnScales(fieldDAO).concat(this.makeControl(fieldDAO)).concat("</v-col>\n");
    }

    private String noValidationProps(FieldDAO fieldDAO) {
        String variableName = this.getVariableName(fieldDAO);
        return " id=\"" + variableName + "\" label=\"" + fieldDAO.getCaption() + "\"\n"
                + "          v-model=\"" + "model." + variableName + "\"\n";

    }

    private String basicProperties(FieldDAO fieldDAO) {

        String properties = noValidationProps(fieldDAO)
                .concat(disableControl(fieldDAO));
        properties += " :rules=\"rules." + this.getVariableName(fieldDAO) + "\"\n";
        properties += ":counter=\"" + fieldDAO.getSize() + "\"\n";
        return properties;

    }
    
    private String basicNoCounterProperties(FieldDAO fieldDAO) {

        String properties = noValidationProps(fieldDAO)
                .concat(disableControl(fieldDAO));
        properties += " :rules=\"rules." + getVariableName(fieldDAO) + "\"\n";
       return properties;

    }


    private String selectField(FieldDAO fieldDAO) {
 return basicProperties(fieldDAO)+
         "@ok=\"controller."+dialogOkMtdName(fieldDAO)+"\"\n" +
"          :items=\"controller."+getStoreVariableName(fieldDAO)+".mini\"\n" +
"          :headers=\"controller."+Utilities.getVariableName(fieldDAO.getReferences())+"Nav.menu.miniHeaders\"\n" +
"        ";
        
        

    }
    
   

//    public String dialogModelVariableName(FieldDAO fieldDAO) {
//        return fieldDAO.getVariableName() + "Dialog";
//    }
//
//    public String dialogModelDataVariable(FieldDAO fieldDAO) {
//        return dialogModelVariableName(fieldDAO) + ": false,\n";
//    }
//
//    public String dialogDataObjectDataName(FieldDAO fieldDAO) {
//        return fieldDAO.getVariableName() + "Data";
//    }
//
//    public String dialogDataObject(FieldDAO fieldDAO) {
//        return dialogDataObjectDataName(fieldDAO) + ": null,\n";
//    }
//
//    public String showDialogDataVariables() {
//        return fields.stream()
//                .filter(f -> f.isSelect())
//                .map(f -> this.dialogModelDataVariable(f))
//                .collect(Collectors.joining());
//    }
//
//    public String dialogDataObjects() {
//        String dObject = fields.stream()
//                .filter(f -> f.isSelect())
//                .map(f -> this.dialogDataObject(f))
//                .collect(Collectors.joining());
//
//        return dObject.equals("") ? "" : dObject.concat("mtdsProvided: true,\n");
//    }
//
//    public String dialogDataWatcher(FieldDAO fieldDAO) {
//        String dialogDataObjectName = dialogDataObjectDataName(fieldDAO);
//        String body = "if(this." + dialogDataObjectName + "){\n"
//                + "        this." + objectVariableName + "." + fieldDAO.getVariableName() + " = this." + dialogDataObjectName + ".id;\n"
//                + "      }\n";
//        return Utilities.makeMethodJs("", "", dialogDataObjectName,
//                "", body);
//    }
//
//    public String dialogDataWatchers() {
//        return fields.stream()
//                .filter(f -> f.isSelect())
//                .map(f -> this.dialogDataWatcher(f))
//                .collect(Collectors.joining());
//    }

 

  

 
  
    private String textValueProperties(FieldDAO fieldDAO) {
        return fieldDAO.getEnumerated() ? "" : "item-title=\"" + Utilities.getVariableName(fieldDAO.getReferences()) + "Name\"\n"
                + "          item-value=\"id\"\n";
    }

    private String comboProperties(FieldDAO fieldDAO) {
        return this.basicNoCounterProperties(fieldDAO)
                .concat(":items=\"" + callStoreDataVaribale(fieldDAO) + "\"\n"
                        .concat(":loading=\"" + callStoreDataVaribaleLoading(fieldDAO) + "\"\n")
                        + textValueProperties(fieldDAO)   + "          ");

    }

   

    private String multiSelectComboProperties(FieldDAO fieldDAO) {
        return this.comboProperties(fieldDAO).concat("multiple").concat("\n");
    }

    private String disableControl(FieldDAO fieldDAO) {
        return fieldDAO.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Read_Only.name())||fieldDAO.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.UI_Only.name()) ? "disabled\n" : "";

    }

    private String textArea(FieldDAO fieldDAO) {
        return basicProperties(fieldDAO).concat("rows=\"1\"\n"
                + "           auto-grow");

    }

    private String datePicker(FieldDAO fieldDAO) {
        return "<v-menu\n"
                + "          ref=\"menu\"\n"
                + "          v-model=\"menu\"\n"
                + "          :close-on-content-click=\"true\"\n"
                + "          transition=\"scale-transition\"\n"
                + "          offset-y\n"
                + "          min-width=\"auto\"\n"
                + "        >\n"
                + "          <template v-slot:activator=\"{ on, attrs }\">\n"
                + "            <v-text-field\n"
                + noValidationProps(fieldDAO)
                + "              prepend-icon=\"mdi-calendar\"\n"
                + "              readonly\n"
                + "              v-bind=\"attrs\"\n"
                + "              v-on=\"on\"\n"
                + "            ></v-text-field>\n"
                + "          </template>\n"
                + "          <v-date-picker\n"
                + "            v-model=\"" + objectVariableName + "." + fieldDAO.getVariableName() + "\"\n"
                + "            :max=\"\n"
                + "              new Date(Date.now() - new Date().getTimezoneOffset() * 60000)\n"
                + "                .toISOString()\n"
                + "                .substr(0, 10)\n"
                + "            \"\n"
                + "            min=\"1950-01-01\"\n"
                + "          ></v-date-picker>\n"
                + "        </v-menu>";
    }

    String crudTable(FieldDAO fieldDAO) {
        String referenceVariableName = Utilities.getVariableName(fieldDAO.getReferences());
        return "<crud-table \n"
                + "      title=\"" + fieldDAO.getCaption() + "\"\n"
                + "       :headers =\"controller." + referenceVariableName + "Nav.menu.editHeaders\"\n"
                + "       :items=\"model." + fieldDAO.getVariableName() + "\"\n"
                + "       :component=\"controller." + referenceVariableName + "Nav.menu.component\"\n"
                + "       maxWidth=\"700px\"\n"
                + "      />\n";
    }

    private String fileInput(FieldDAO fieldDAO) {
        return this.noValidationProps(fieldDAO);
    }

//    private String getLengthRule(FieldDAO fieldDAO) {
//        return (fieldDAO.getDataType().equalsIgnoreCase("String") && !fieldDAO.isReferance())
//                ? "(v) => v.length < " + fieldDAO.getSize() + " || \"" + fieldDAO.getCaption() + " length must be "
//                + "less or equal to " + fieldDAO.getSize() + "\"," : "";
//    }

//    private String rules(FieldDAO fieldDAO) {
//        if (fieldDAO.getControlType().equals(UIControls.CheckBox)) {
//            return "";
//        }
//        String rules = fieldDAO.getVariableName() + "Rules: [(v) => !!v || \"" + fieldDAO.getCaption() + " is required\",\n";
//        if (fieldDAO.getDataType().equalsIgnoreCase("String")) {
//            rules += getLengthRule(fieldDAO);
//        }
//
//        rules += " ],";
//
//        return rules;
//    }



    private String makeControlColumns() {
        String controls = "";
        controls = fields.stream()
                .filter(p -> !p.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Query_Only.name()))
                .map(fieldDAO -> controlColumn(fieldDAO)).reduce(controls, String::concat);
        return controls;
    }

    private String urlLine(FieldDAO fieldDAO) {
        return fieldDAO.getVariableName() + "Url: null,\n";
    }

    private String urlLines() {
        String urlLines = "";
        urlLines = this.fields
                .stream().filter(p -> p.getDataType().equalsIgnoreCase("Image"))
                .map(f -> urlLine(f))
                .reduce(urlLines, String::concat);
        return urlLines.concat("\n");
    }

 

    private String makeImports() {

        String imports = "import " + this.controllerVariableName + " from \"./" + this.controller + "\";\n";
//        imports += fields.stream()
//                .filter(p -> p.getControlType().equals(UIControls.TableView))
//                .count() > 0 ? "import CrudTable from \"../../components/CrudTable.vue\";\n" : "";

        imports += fields.stream()
                .filter(p -> p.getControlType().equals(UIControls.ImageView))
                .count() > 0 ? "import funcs from '../../utils/funcs'\n" : "";
        
        if(changeFormSize())imports+="import rootOptions from '@/root/RootOptions';\n";

        imports = fields.stream()
                .filter(p -> p.getControlType().equals(UIControls.TableView)
                || p.isSelect())
                .map(f -> importLine(f))
                .distinct()
                .reduce(imports, String::concat);
        return imports;
    }

    private String importLine(FieldDAO fd) {
        if (!fd.isReferance()) {
            return "";
        }


        String imp = "";
       

        return imp;
    }

    private String dataNavLine(FieldDAO fieldDAO) {
        String referenceVariableName = Utilities.getVariableName(fieldDAO.getReferences()).concat("Nav");
        return referenceVariableName.concat(":").concat(referenceVariableName).concat(",\n");

    }

//    private String dataNavLines() {
//        String lines = "";
//        lines = this.fields.stream()
//                .filter(p -> p.getControlType().equals(UIControls.TableView)
//                || p.isSelect())
//                .map(f -> dataNavLine(f)).reduce(lines, String::concat);
//        return lines;
//    }


    public String makeFormDataLine(FieldDAO fieldDAO, String variable) {
        return variable + ".append(\"" + fieldDAO.getVariableName() + "\", this." + objectVariableName + "." + fieldDAO.getVariableName() + ");\n";

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

//    private String onFileChanged(FieldDAO fieldDAO) {
//        return "on" + fieldDAO.getFieldName() + "FileChange(e) {\n"
//                + "      if (e) {\n"
//                + "        this." + fieldDAO.getVariableName() + "Url = URL.createObjectURL(e);\n"
//                + "      } else {\n"
//                + "        this." + fieldDAO.getVariableName() + "Url = null;\n"
//                + "      }\n"
//                + "    },\n";
//    }
    private String setImageFileName(FieldDAO fieldDAO) {
        return " async set" + fieldDAO.getFieldName() + "File(obj) {\n"
                + "      let " + fieldDAO.getVariableName() + " = obj." + fieldDAO.getVariableName() + ";\n"
                + "      if (" + fieldDAO.getVariableName() + ") {\n"
                + "        this." + fieldDAO.getVariableName() + "Url = \"data:image/png;base64,\" + " + fieldDAO.getVariableName() + ";\n"
                + "        const getUrlExtension = (url) => {\n"
                + "        return url.split(/[#?]/)[0].split(\".\").pop().trim();\n"
                + "      };\n"
                + "      var imgExt = getUrlExtension(this." + fieldDAO.getVariableName() + "Url);\n"
                + "\n"
                + "        const response = await fetch(this." + fieldDAO.getVariableName() + "Url);\n"
                + "        const blob = await response.blob();\n"
                + "        console.log(\"Blob: \", blob);\n"
                + "        const file = new File([blob], \"logo.\" + imgExt, {\n"
                + "          type: blob.type,\n"
                + "        });\n"
                + "      this." + objectVariableName + "." + fieldDAO.getVariableName() + " = file;\n"
                + "\n"
                + "      } else {\n"
                + "        this." + fieldDAO.getVariableName() + "Url = null;\n"
                + "      }\n"
                + "    },\n";
    }

    String callSetFile(FieldDAO fieldDAO) {
        return " funcs.createFileFromBytes(obj." + fieldDAO.getVariableName() + ").then(e=>{\n"
                + "        this." + objectVariableName + "." + fieldDAO.getVariableName() + " =e;\n"
                + "\n"
                + "      }).catch(error=> console.log(error));\n";
    }

  

 
    private boolean changeFormSize(){
      return this.fields.stream().filter(f->f.hasCrudTable()).count()>0 ||this.fields.size()>11;
    }
    private String templete() {
        return "<template>\n"
                + "  <crud-form\n"
                + "    :controller=\"controller\"\n>\n"
                + "    <template #heading>" + this.fileModel.getObjectCaption() + "</template>\n"
                + "\n"
                + "    <template #form-data>\n"
                + this.makeControlColumns()
                + "    </template>\n"
                + "  </crud-form>\n"
                + "</template>\n";
    }

    private  String breakPoints (){
    if(!changeFormSize())
     return "const cols = 12;\nconst sm = 6;\n const md = 6;\n";
    
    return "const cols = 12;\nconst sm = 4;\n const md = 4;\n".concat("rootOptions.maxWidth=1000;\n");
    }



    private String initialiseController() {
        return "const controller= " + controllerVariableName + "();\n"
                + "\n"
                + "const model =  controller.model;\n"
                + "const rules= controller.rules;\n";
    }

    private String script() {
        return "<script setup>\n"
                + makeImports()
                + breakPoints()
                + initialiseController()
                + "</script>";
    }

    @Override
    public String create() {
        return this.script().concat(this.templete());
    }

    @Override
    protected String getFileName() {
       return objectName;
    }

    @Override
    protected String getFileExtension() {
       return "vue";
    }

}
