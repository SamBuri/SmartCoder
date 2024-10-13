/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.vue3;

import com.saburi.smartcoder.FileModel;
import com.saburi.utils.Utilities;

/**
 *
 * @author samburiima
 */
public class Search extends Vue3Utils {

    private final String navName;
    private final String navVariableName;

    public Search(FileModel fileModel) {
        super(fileModel);
        this.navName = fileModel.getObjectName().concat("Nav");
        this.navVariableName = Utilities.getVariableName(navName);

    }

    private String template = "<template>\n"
            + "  <search :menu=\"menu\" :menuItems =\"menuItems\"></search>\n"
            + "</template>\n";

    private String imports() {
        return "import " + this.navVariableName + " from './" + this.navName + "';\n"
                + "import " + objectVariableName + "Controller from \"./" + objectName + "Controller\";\n"
                + "import searchMenuController from \"@/search/SearchMenuController\";\n";
    }

    private String script() {

        return "<script setup>\n" + imports()
                + "const controller= " + objectVariableName + "Controller();\n"
                + "const {save, edit, deleteItem, republish}=searchMenuController(controller)\n"
                + "const menu= " + navVariableName + ".menu;\n"
                + "const menuItems = [\n"
                + "      { title: \"Add\", icon: \"mdi-plus\", click: (options)=>save(options)},\n"
                + "      { title: \"Edit\", icon: \"mdi-pencil\",click: (options)=>edit(options) },\n"
                + "      { title: \"Delete\", icon: \"mdi-delete\",click:(options)=>deleteItem(options) },\n"
                + "      { title: \"Re-post\", icon: \"mdi-send\",click: (options)=>republish(options) },\n"
                + "    ]\n"
                + "\n"
                + "    \n"
                + "</script>\n";

    }

    @Override
    public String create() {
        return this.script().concat(this.template);
    }

    @Override
    protected String getFileName() {
        return Utilities.toPlural(this.objectName);
    }

    @Override
    protected String getFileExtension() {
        return "vue";
    }

}
