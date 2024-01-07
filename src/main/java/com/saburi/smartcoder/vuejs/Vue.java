/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.vuejs;

import com.saburi.dataacess.FieldDAO;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.UIControls;
import static com.saburi.utils.Enums.UIControls.SNumberInput;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import static com.saburi.utils.Utilities.toPlural;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author samburiima
 */
public class Vue {

    private final String objectName;
    private final List<FieldDAO> fields;
    private final String objectNameVariable;
    private final String modelName;
    private final String objectCaption;
    private final String moduleName;
    private final boolean hasMuiltipart;

    public Vue(String objectName, String moduleName, String objectCaption, List<FieldDAO> fields) {
        this.objectName = objectName;
        this.moduleName = moduleName;
        this.objectCaption = objectCaption;
        this.objectNameVariable = Utilities.getVariableName(this.objectName);
        this.fields = fields;
        this.modelName = this.objectNameVariable.concat("Model");
        this.hasMuiltipart = Utilities.hasMultipart(fields);
    }

    private boolean forceReferences(FieldDAO fieldDAO) {
        String mName = fieldDAO.getModuleName();
        return fieldDAO.isReferance() && (mName.equalsIgnoreCase(this.moduleName) || isNullOrEmpty(mName));
    }

    private UIControls getUIControl(FieldDAO fieldDAO) {
        UIControls uIControl = fieldDAO.getControlType();
        if (uIControl.equals(UIControls.TextArea) && fieldDAO.isCollection()) {
            return UIControls.MultiSelectCombo;
        } else if (uIControl.equals(UIControls.TextField) && fieldDAO.isNumeric()) {
            return UIControls.SNumberInput;
        } else if (uIControl.equals(UIControls.ComboBox) && fieldDAO.isSelect()) {
            return UIControls.SelectField;
        }
        return uIControl;
    }

    private String makeControl(FieldDAO fieldDAO) {
        UIControls uIControl = getUIControl(fieldDAO);

        return switch (uIControl) {
            case ComboBox ->
                "<v-select".concat(comboProperties(fieldDAO)).concat("></v-select>\n");
            case MultiSelectCombo ->
                "<v-select".concat(multiSelectComboProperties(fieldDAO)).concat("></v-select>\n");
            case CheckBox ->
                "<v-checkbox".concat(noValidationProps(fieldDAO)).concat("></v-checkbox>\n");
            case TextArea ->
                "<v-textarea\n" + textArea(fieldDAO) + "></v-textarea>\n";
            case TableView ->
                this.crudTable(fieldDAO);
            case DatePicker ->
                " <s-date-picker" + this.noValidationProps(fieldDAO)
                .concat(" :rules=\"" + fieldDAO.getVariableName() + "Rules\"\n") + "/>";
            case ImageView ->
                "<s-file-input\n" + fileInput(fieldDAO) + "></s-file-input>\n";

            case SNumberInput ->
                "<s-number-input\n" + basicProperties(fieldDAO) + "></s-number-input>\n";

            case SelectField ->
                "<v-text-field\n" + selectField(fieldDAO) + "></v-text-field>\n";

            default ->
                "<v-text-field\n" + basicProperties(fieldDAO) + "></v-text-field>\n";
        };

    }

    private String makeColumnScales(FieldDAO fieldDAO) {
        return fieldDAO.getControlType().equals(UIControls.TableView) ? "<v-col cols=\"12\">\n" : "<v-col :cols=\"cols\" :sm=\"sm\" :md=\"md\">\n";
    }

    private String controlColumn(FieldDAO fieldDAO) {
        return makeColumnScales(fieldDAO).concat(this.makeControl(fieldDAO)).concat("</v-col>\n");
    }

    private String noValidationProps(FieldDAO fieldDAO) {
        String variableName = fieldDAO.getVariableName(); 
        return " id=\""+variableName+"\" label=\"" + fieldDAO.getCaption() + "\"\n"
                + "          v-model=\"" + this.objectNameVariable + "." + variableName + "\"\n";

    }

    private String basicProperties(FieldDAO fieldDAO) {

        String properties = noValidationProps(fieldDAO)
                .concat(disableControl(fieldDAO));
        properties += " :rules=\"" + fieldDAO.getVariableName() + "Rules\"\n";
        properties += ":counter=\"" + fieldDAO.getSize() + "\"\n";
        return properties;

    }

    private String selectField(FieldDAO fieldDAO) {

        return this.basicProperties(fieldDAO)
                .concat("\n")
                .concat("append-icon=\"mdi-view-list\" @click:append=\"show" + fieldDAO.getFieldName() + "Dialog\"");

    }

    public String dialogModelVariableName(FieldDAO fieldDAO) {
        return fieldDAO.getVariableName() + "Dialog";
    }

    public String dialogModelDataVariable(FieldDAO fieldDAO) {
        return dialogModelVariableName(fieldDAO) + ": false,\n";
    }

    public String dialogDataObjectDataName(FieldDAO fieldDAO) {
        return fieldDAO.getVariableName() + "Data";
    }

    public String dialogDataObject(FieldDAO fieldDAO) {
        return dialogDataObjectDataName(fieldDAO) + ": null,\n";
    }

    public String showDialogDataVariables() {
        return fields.stream()
                .filter(f -> f.isSelect())
                .map(f -> this.dialogModelDataVariable(f))
                .collect(Collectors.joining());
    }

    public String dialogDataObjects() {
        String dObject = fields.stream()
                .filter(f -> f.isSelect())
                .map(f -> this.dialogDataObject(f))
                .collect(Collectors.joining());

        return dObject.equals("") ? "" : dObject.concat("mtdsProvided: true,\n");
    }

    public String dialogDataWatcher(FieldDAO fieldDAO) {
        String dialogDataObjectName = dialogDataObjectDataName(fieldDAO);
        String body = "if(this." + dialogDataObjectName + "){\n"
                + "        this." + objectNameVariable + "." + fieldDAO.getVariableName() + " = this." + dialogDataObjectName + ".id;\n"
                + "      }\n";
        return Utilities.makeMethodJs("", "", dialogDataObjectName,
                "", body);
    }

    public String dialogDataWatchers() {
        return fields.stream()
                .filter(f -> f.isSelect())
                .map(f -> this.dialogDataWatcher(f))
                .collect(Collectors.joining());
    }

    private String dialog(FieldDAO fieldDAO) {
        String variable = fieldDAO.getVariableName();
        return "<v-dialog v-model=\"" + dialogModelVariableName(fieldDAO) + "\" persistent :max-with=\"600\">\n"
                + "        <search-mini :mtdsProvided=\"mtdsProvided\" @ok=\"" + variable + "Ok\" @close=\"" + variable + "Close\" :items=\"$store.state." + getStateVariable(fieldDAO) + "\"\n"
                + "          :headers=\"" + variable + "Nav.menu.miniHeaders\" />\n"
                + "      </v-dialog>\n";
    }

    public String dialogs() {
        return fields.stream()
                .filter(f -> f.isSelect())
                .map(f -> this.dialog(f))
                .collect(Collectors.joining());
    }

    private String dialogMethods(FieldDAO fieldDAO) {

        String dialogModelVariableName = dialogModelVariableName(fieldDAO);
        String variableName = fieldDAO.getVariableName();
        return variableName + "Ok(data){\n"
                + "      this." + this.dialogDataObjectDataName(fieldDAO) + " = data\n"
                + "    },\n"
                + variableName + "Close(){\n"
                + "      this." + dialogModelVariableName + " = false;\n"
                + "    },\n"
                + "\n"
                + "    show" + fieldDAO.getFieldName() + "Dialog(){\n"
                + "      this." + dialogModelVariableName + " = true;\n"
                + "    },\n";
    }

    private String dialogMethods() {
        return fields.stream()
                .filter(f -> f.isSelect())
                .map(f -> this.dialogMethods(f))
                .collect(Collectors.joining());
    }

    private String referenceComputed(FieldDAO f) {

        if (!forceReferences(f)) {
            return "";
        }
        return f.getVariableName()+"(){\n"
                + "      return this."+this.objectNameVariable + "." + f.getVariableName()+";\n"
                + "    },\n";
    }
    
        private String referenceWatcher(FieldDAO f) {

        if (!forceReferences(f)) {
            return "";
        }
        String variableName = f.getVariableName();
        return variableName+"(){\n"
                +"if(this."+variableName+"){"
                + "       this."+this.objectNameVariable + "." + variableName+"Id =this." + variableName+".id;\n"
                + "    }\n"
                + "},\n";
    }

    private boolean isSameModule(FieldDAO fieldDAO) {
        return this.moduleName.equalsIgnoreCase(this.getModuleName(fieldDAO));
    }

    private String getModuleName(FieldDAO fieldDAO) {
        String module = fieldDAO.getModuleName();
        return isNullOrEmpty(module) ? this.moduleName : module;

    }

    private String textValueProperties(FieldDAO fieldDAO) {
        return fieldDAO.getEnumerated() ? "" : "item-text=\"" + Utilities.getVariableName(fieldDAO.getReferences()) + "Name\"\n"
                + "          item-value=\"id\"\n";
    }

    private String comboProperties(FieldDAO fieldDAO) {
        return this.basicProperties(fieldDAO)
                .concat(":items=\"$store.state." + getStateVariable(fieldDAO) + "\"\n"
                        .concat(":loading=\"$store.state." + getStateVariable(fieldDAO).concat("Loading") + "\"\n")
                        + textValueProperties(fieldDAO)
                        + "          ").concat(comboReturnObject(fieldDAO));

    }

    private String comboReturnObject(FieldDAO fieldDAO) {
        return this.forceReferences(fieldDAO) ? "return-object\n" : "";
    }

    private String multiSelectComboProperties(FieldDAO fieldDAO) {
        return this.comboProperties(fieldDAO).concat("multiple").concat("\n");
    }

    private String disableControl(FieldDAO fieldDAO) {
        return fieldDAO.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.Read_Only.name()) ? "disabled\n" : "";

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
                + "            v-model=\"" + objectNameVariable + "." + fieldDAO.getVariableName() + "\"\n"
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
                + "       :headers =\"" + referenceVariableName + "Nav.menu.editHeaders\"\n"
                + "       :items=\"" + objectNameVariable + "." + fieldDAO.getVariableName() + "\"\n"
                + "       :component=\"" + referenceVariableName + "Nav.menu.component\"\n"
                + "       maxWidth=\"700px\"\n"
                + "      />\n";
    }

    private String fileInput(FieldDAO fieldDAO) {
        return this.noValidationProps(fieldDAO);
    }

    private String getLengthRule(FieldDAO fieldDAO) {
        return (fieldDAO.getDataType().equalsIgnoreCase("String") && !fieldDAO.isReferance())
                ? "(v) => v.length < " + fieldDAO.getSize() + " || \"" + fieldDAO.getCaption() + " length must be "
                + "less or equal to " + fieldDAO.getSize() + "\"," : "";
    }

    private String rules(FieldDAO fieldDAO) {
        if (fieldDAO.getControlType().equals(UIControls.CheckBox)) {
            return "";
        }
        String rules = fieldDAO.getVariableName() + "Rules: [(v) => !!v || \"" + fieldDAO.getCaption() + " is required\",\n";
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

    private String data() {
        int sm = 6;
        int md = 4;
        int width = 700;
        int size = this.fields.size();
        if (size > 8) {
            width = 1000;
            md = 3;
        }

        return """
               data: () => ({
                   cols: 12,
                   sm:""" + sm + ",\n" + ""
                + "       md: " + md + ",\n" + ""
                + "       maxWidth:" + width + ",\n"
                + "path: " + this.modelName + ".path,\n"
                + "    " + this.objectNameVariable + ": " + this.modelName + "." + this.objectNameVariable + ",\n"
                + dataNavLines()
                + makeRules()
                + urlLines()
                + showDialogDataVariables()
                + dialogDataObjects()
                + "showPrintPrompt: true,"
                + "  }),\n";
    }

    private String predictLookupObjectName(FieldDAO fieldDAO) {
        String references = fieldDAO.getReferences();
        String fieldName = fieldDAO.getFieldName();
        if (fieldDAO.getEnumerated()) {
            return references;
        }
        String lookupName = references.equalsIgnoreCase("LookupData") ? fieldName : references;

        int length = lookupName.length();
        if (lookupName.substring(length - 2, length).equalsIgnoreCase("id")) {
            lookupName = lookupName.substring(0, length - 2);
        }
        return lookupName;
    }

    private String getReferencingName(FieldDAO fieldDAO) {
        return fieldDAO.getReferences().equalsIgnoreCase("LookupData") ? toPlural(predictLookupObjectName(fieldDAO))
                : fieldDAO.getEnumerated() ? predictLookupObjectName(fieldDAO) : "Mini";
    }

    private String getStateVariable(FieldDAO fieldDAO) {
        return storePath(fieldDAO, ".") + Utilities.getVariableName(this.getReferencingName(fieldDAO));

    }

    private String storePath(FieldDAO fieldDAO, String literal) {
        String mName = this.getModuleName(fieldDAO).toLowerCase();
        String rV = fieldDAO.getEnumerated() ? "" : fieldDAO.getReferences().toLowerCase().concat(literal);
        return mName.concat(literal) + rV;
    }

    private String makeImports() {

        String imports = "import " + this.modelName + " from \"./" + this.objectName + "Model\";\n";
        imports += fields.stream()
                .filter(p -> p.getControlType().equals(UIControls.TableView))
                .count() > 0 ? "import CrudTable from \"../../components/CrudTable.vue\";\n" : "";

        imports += fields.stream()
                .filter(p -> p.getControlType().equals(UIControls.ImageView))
                .count() > 0 ? "import funcs from '../../utils/funcs'\n" : "";

        imports = fields.stream()
                .filter(p -> p.getControlType().equals(UIControls.TableView)
                || p.isSelect())
                .distinct().map(f -> importLine(f)).reduce(imports, String::concat);
        return imports;
    }

    private String importLine(FieldDAO fieldDAO) {
        String mName = isSameModule(fieldDAO) ? "" : getModuleName(fieldDAO).concat("/");
        String references = fieldDAO.getReferences();
        String referencesNav = references.concat("Nav");
        return "import " + Utilities.getVariableName(referencesNav) + " from '../" + mName + "" + references.toLowerCase() + "/" + referencesNav + ".js';\n";

    }

    private String makeComponents() {
        String crudTable = fields.stream()
                .filter(p -> p.getControlType().equals(UIControls.TableView))
                .count() > 0 ? "CrudTable" : "";
        return crudTable;
    }

    private String dataNavLine(FieldDAO fieldDAO) {
        String referenceVariableName = Utilities.getVariableName(fieldDAO.getReferences()).concat("Nav");
        return referenceVariableName.concat(":").concat(referenceVariableName).concat(",\n");

    }

    private String dataNavLines() {
        String lines = "";
        lines = this.fields.stream()
                .filter(p -> p.getControlType().equals(UIControls.TableView)
                || p.isSelect())
                .map(f -> dataNavLine(f)).reduce(lines, String::concat);
        return lines;
    }

    private String created() {

        String body = "";

        body = fields.stream()
                .filter((p) -> p.isReferance())
                .filter((p) -> !p.isCollection())
                .map(fieldDAO -> "this.$store.dispatch(\"".concat(this.storePath(fieldDAO, "/")) + "get" + this.getReferencingName(fieldDAO) + "\");\n")
                .reduce(body, String::concat);
        return " created() {" + body + "},\n";

    }

    public String makeFormDataLine(FieldDAO fieldDAO, String variable) {
        return variable + ".append(\"" + fieldDAO.getVariableName() + "\", this." + objectNameVariable + "." + fieldDAO.getVariableName() + ");\n";

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
                + "      this." + objectNameVariable + "." + fieldDAO.getVariableName() + " = file;\n"
                + "\n"
                + "      } else {\n"
                + "        this." + fieldDAO.getVariableName() + "Url = null;\n"
                + "      }\n"
                + "    },\n";
    }

    String callSetFile(FieldDAO fieldDAO) {
        return " funcs.createFileFromBytes(obj." + fieldDAO.getVariableName() + ").then(e=>{\n"
                + "        this." + objectNameVariable + "." + fieldDAO.getVariableName() + " =e;\n"
                + "\n"
                + "      }).catch(error=> console.log(error));\n";
    }

    private String makePrintLine(FieldDAO fieldDAO) {

        return "data.push({ text: \"" + fieldDAO.getCaption() + "\", value: this." + objectNameVariable + "." + fieldDAO.getVariableName() + " });\n";
    }

    private String print() {

        String printLines = "";
        printLines = this.fields.stream()
                .filter(f -> !f.getDataType().equalsIgnoreCase("boolean"))
                .map(f -> this.makePrintLine(f))
                .reduce(printLines, String::concat);
        String printData = "let data = [];\n" + printLines + "\n";
        String mtdBody = printData + " let options = {\n"
                + "        data: data,\n"
                + "        startXPos: 10,\n"
                + "        startYPos: 25,\n"
                + "        lineBreak: 4,\n"
                + "        hSpace: 50,\n"
                + "        vSpace: 10,\n"
                + "        title: \"" + objectCaption + "\"\n"
                + "\n"
                + "      }\n"
                + "\n"
                + "      this.makePDFDocument(options);";

        return "print(){" + mtdBody + "},\n";
    }

    private String watch() {
        
        String referenceWatcher = "";
        referenceWatcher = this.fields
                .stream()
                .filter(f->this.forceReferences(f))
                .map(f->this.referenceWatcher(f))
                .distinct()
                .reduce(referenceWatcher, String::concat)
                .concat("\n");

        return "watch: {\n"
                + dialogDataWatchers()
                +referenceWatcher
                + "},\n";

    }
    
     private String computed() {
        
        String referenceComputed = "";
        referenceComputed = this.fields
                .stream()
                .filter(f->this.forceReferences(f))
                .map(f->this.referenceComputed(f))
                .distinct()
                .reduce(referenceComputed, String::concat)
                .concat("\n");

        return "computed: {\n"
                 +referenceComputed
                + "},\n";

    }

    private String methods() {
        String theBody = hasMuiltipart ? "getFormData()" : objectNameVariable;

        String onFileChangedMtds = "";
//        onFileChangedMtds = this.fields
//                .stream().filter(p -> p.getDataTypeImps().equalsIgnoreCase("Image"))
//                .map(f -> onFileChanged(f))
//                .reduce(onFileChangedMtds, String::concat);

        String setImageFileNameMtds = "";
        setImageFileNameMtds = this.fields
                .stream().filter(p -> p.getDataType().equalsIgnoreCase("Image"))
                .map(f -> setImageFileName(f))
                .reduce(setImageFileNameMtds, String::concat);

        String callSetFiles = "";

        callSetFiles = this.fields
                .stream().filter(p -> p.getDataType().equalsIgnoreCase("Image"))
                .map(f -> callSetFile(f))
                .reduce(callSetFiles, String::concat);

        String save = """
                      save() {
                            this.$store.dispatch("post", { path: this.path, body: this.""" + theBody + "});\n"
                + "    },\n";

        String update = """
                        update() {
                              this.$store.dispatch("put", { path: `${this.path}/${this.""" + objectNameVariable + ".id}`,body: this." + theBody + "});\n"
                + "    },\n";

        String updateDialg = "updateDialog() {\n"
                + "      var obj = this.$store.state.search.selectedData[0].value;\n"
                + "      this.setDialog(obj);\n"
                + "    },\n";

        String search = "async search() {\n"
                + "      var obj = this.$store.state.obj;\n"
                + "      this." + objectNameVariable + " = Object.assign({}, obj);\n"
                + "      this.setObjects(obj);\n"
                + "    },\n";

        String reset = "reset() {\n"
                + "      this." + objectNameVariable + ".clear();\n"
                + "    },\n";

        String setObject = "setObjects(obj){\nconsole.log(obj);\n" + callSetFiles + "},";

        String setDialog = "setDialog(obj) {\n"
                + "this." + this.objectNameVariable + " = Object.assign({}, obj);\n"
                + "this.setObjects(obj);\n"
                + "},\n";

        String done = "done() {\n"
                + "      this.$store.commit(\n"
                + "        \"crudtable/data\",\n"
                + "        Object.assign({}, this." + this.objectNameVariable + ")\n"
                + "      );\n"
                + "    },\n";

        String updateCrudTableDialog = "updateCrudTableDialog() {\n"
                + "      this.setDialog(this.$store.state.crudtable.data);\n"
                + "    },\n";

        String resetCrudTableDialog = "resetCrudTableDialog() {\n"
                + "      this.reset();\n"
                + "    },\n";

        return "methods: {" + save.concat(update).concat(updateDialg).concat(search).concat(reset).
                concat(setObject)
                .concat(setDialog)
                .concat(done)
                .concat(updateCrudTableDialog)
                .concat(resetCrudTableDialog)
                .concat(getFormDataMtd())
                .concat(onFileChangedMtds)
                .concat(setImageFileNameMtds)
                .concat(this.print())
                .concat(dialogMethods())
                .concat("}");

    }

    private String templete() {
        return "<template>\n"
                + "  <crud-form\n"
                + "    @save=\"save\"\n"
                + "    @update=\"update\"\n"
                + "    @search=\"search\"\n"
                + "    @updateDialog=\"updateDialog\"\n"
                + "    @reset=\"reset\"\n"
                + "    @done=\"done\"\n"
                + "    @updateCrudTableDialog=\"updateCrudTableDialog\"\n"
                + "    @resetCrudTableDialog=\"resetCrudTableDialog\"\n"
                + "    :path=\"path\"\n"
                + "    :maxWidth=\"maxWidth\"\n"
                + "    :showPrintPrompt=\"showPrintPrompt\""
                + "    @print=\"print\""
                + "  >\n"
                + "    <template slot=\"heading\">" + this.objectCaption + "</template>\n"
                + "\n"
                + "    <template slot=\"form-data\">\n"
                + this.makeControlColumns()
                + this.dialogs()
                + "    </template>\n"
                + "  </crud-form>\n"
                + "</template>\n";
    }

    private String script() {
        return "<script>\n"
                + makeImports()
                + "export default {\n"
                + "  components: { " + makeComponents() + " },\n"
                + "  name: \"" + objectName + "\",\n"
               
                + data()
                + created()
                + computed()
               
                + "\n"
                + watch()
                + methods()
                + "};\n"
                + "</script>";
    }

    public String create() {
        return this.templete().concat(this.script());
    }

}
