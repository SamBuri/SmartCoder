/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.vue3;

import com.saburi.dataacess.FieldDAO;
import com.saburi.smartcoder.FileModel;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;

/**
 *
 * @author samburiima
 */
public class Controller extends Vue3Utils {

    public Controller(FileModel fileModel) {
        super(fileModel);

    }

    private String imports() {
        return "import rootController from \"@/root/RootController\";\n"
                + "import " + this.objectVariableName + "Model from \"./" + this.objectName + "Model\";\n"
                + makeImports();
    }

    private boolean includeNavVariables(FieldDAO fd) {
        return fd.hasCrudTable() || fd.isSelect();
    }

    private String importLine(FieldDAO fd) {
        if (!fd.isReferance()) {
            return "";
        }

        String references = fd.getReferences();

        String referencesNav = references.concat("Nav");
        String referencesController = references.concat("Controller");
        String imp = "";
        if (includeNavVariables(fd)) {
            imp += String.format("import %s from \"%s\"\n", Utilities.getVariableName(referencesNav),
                    this.importPath(fd, referencesNav.concat(".js")));
        }

        if (fd.hasCrudTable()) {
            imp += String.format("import %s from \"%s\"\n", Utilities.getVariableName(referencesController),
                    this.importPath(fd, referencesController.concat(".js")));
        }
        imp += String.format("import {%s} from \"%s\"\n", defineStoreFunction(fd),
                this.importPath(fd, this.getStoreName(fd).concat(".js")));
        return imp;
    }

    private String makeImports() {

        String imports = "";
//        imports += fields.stream()
//                .filter(p -> p.getControlType().equals(UIControls.TableView))
//                .count() > 0 ? "import CrudTable from \"../../components/CrudTable.vue\";\n" : "";

        imports += fields.stream()
                .filter(p -> p.isReferance())
                .count() > 0 ? "import { onMounted } from \"vue\";\n" : "";

        imports += fields.stream()
                .filter(p -> p.getControlType().equals(Enums.UIControls.ImageView))
                .count() > 0 ? "import funcs from '../../utils/funcs'\n" : "";

        imports = fields.stream()
                .filter(p -> p.isReferance())
                .map(f -> importLine(f))
                .distinct()
                .reduce(imports, String::concat);
        return imports;
    }

    private String makeStoreLine(FieldDAO fieldDAO) {
        if (!fieldDAO.isReferance()) {
            return "";
        }

        String storeVariableName = getStoreVariableName(fieldDAO);

        String assignStore = String.format("controller.%s = %s;", storeVariableName, storeVariableName);

        return String.format("const %s = %s();\n%s\n", storeVariableName, super.defineStoreFunction(fieldDAO), assignStore);
    }

    private String assignControllerVariableName(FieldDAO fd) {
        if (!fd.hasCrudTable()) {
            return "";
        }
        String reference = fd.getReferences();

        String referenceController = reference.concat("Controller");
        String setDataVariableName = String.format("set%sData", reference);
        String setData = String.format("const {setData: %s}=%s();\n", setDataVariableName, Utilities.getVariableName(referenceController));
        String assignSetData = String.format("controller.%s=%s;\n", setDataVariableName, setDataVariableName);
        return setData.concat(assignSetData);
    }

    private String assignControllerVariableNames() {
        String lines = "";
        lines = this.fields.stream()
                .filter(f -> f.hasCrudTable())
                .map(f -> this.assignControllerVariableName(f))
                .distinct()
                .reduce(lines, String::concat);
        return lines;
    }

    private String assignVariableName(FieldDAO fd) {
        if (!includeNavVariables(fd)) {
            return "";
        }
        String reference = fd.getReferences();
        String navVariableName = Utilities.getVariableName(reference).concat("Nav");
        return String.format("controller.%s=%s;\n", navVariableName, navVariableName);

    }

    public String assignVariableNames() {
        String lines = "";
        lines = this.fields.stream()
                .filter(f -> includeNavVariables(f))
                .map(f -> this.assignVariableName(f))
                .distinct()
                .reduce(lines, String::concat);
        return lines;
    }

    public String makeDefineLines() {
        String lines = "";
        lines = this.fields.stream()
                .filter(f -> f.isReferance())
                .map(f -> this.makeStoreLine(f))
                .distinct()
                .reduce(lines, String::concat);
        return lines;
    }

    private String makeDialogOk(FieldDAO fieldDAO) {
        if (!fieldDAO.isSelect()) {
            return "";
        }
        String variableName= this.getVariableName(fieldDAO);
        String okMethodName =dialogOkMtdName(fieldDAO);
        String valuePath ="controller.model.value."+ variableName ;
        String dataDotId =  " data.id";
        String setValue = fieldDAO.isCollection()?String.format("%s.push(%s)", valuePath, dataDotId)
                :String.format("%s=%s", valuePath, dataDotId);
        return "const " + okMethodName + " = (data) => {\n"
                + "    if (data) {\n"
                + "     \n"
                + "      " + setValue + ";\n"
                + "\n"
                + "    }\n"
                + "  };\n"
                + "controller." + okMethodName + " = " + okMethodName + ";\n";
    }

    public String makeDiaogOks() {
        String dialogs = "";
        dialogs = this.fields.stream()
                .filter(f -> f.isSelect())
                .map(f -> this.makeDialogOk(f))
                .distinct()
                .reduce(dialogs, String::concat);
        return dialogs;
    }

    private String onMunted() {
        String lines = "";
        lines = this.fields.stream()
                .filter(f -> f.isReferance())
                .map(f -> this.callStoreAction(f))
                .distinct()
                .reduce(lines, String::concat);
        return lines.equals("") ? "" : String.format("onMounted(()=>{\n%s\n})\n", lines);
    }

    private String makeFunction() {
        return "export default function " + objectVariableName + "Controller(){\n"
                + "\n"
                + "  const controller = rootController(" + objectVariableName + "Model);\n"
                + makeDiaogOks()
                + makeDefineLines()
                + assignControllerVariableNames()
                + assignVariableNames()
                + onMunted()
                + "  return controller;\n"
                + "\n"
                + "}\n";
    }

    @Override
    public String create() {
        return this.imports().concat(this.makeFunction());
    }

    @Override
    protected String getFileName() {
        return this.objectName.concat("Controller");
    }

    @Override
    protected String getFileExtension() {
        return "js";
    }

}
