/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.vuejs;

import com.saburi.utils.Utilities;

/**
 *
 * @author samburiima
 */
public class Search {

    private final String objectName;
    private final String navName;
    private final String navVariableName;

    public Search(String objectName) {
        this.objectName = objectName;
        this.navName = objectName.concat("Nav");
        this.navVariableName = Utilities.getVariableName(navName);

    }

    private String template = "<template>\n"
            + "  <search :menu=\"menu\" :menuItems =\"menuItems\"  @itemSelected=\"itemSelected\"></search>\n"
            + "</template>\n";

    private String imports() {
        return "import " + this.navVariableName + " from './" + this.navName + "';";
    }

    private String data() {
        return " data: () => ({\n"
                + "  \n"
                + "     menu: " + this.navVariableName + ".menu,\n"
                + "     menuItems: [\n"
                + "      { title: \"Add\", icon: \"mdi-plus\" },\n"
                + "      { title: \"Edit\", icon: \"mdi-pencil\" },\n"
                + "      { title: \"Delete\", icon: \"mdi-delete\" },\n"
                 + "     { title: \"Re-post\", icon: \"mdi-send\" },\n"
                + "    ],\n"
                + "  \n"
                + "  }),\n";
    }

    String methods() {
        String selected = " itemSelected(selectedItem, items) {\n"
                + "      switch (selectedItem) {\n"
                + "        case 0:\n"
                + "          this.saveClicked();\n"
                + "          break;\n"
                + "        case 1:\n"
                + "          this.editClicked(items);\n"
                + "          break;\n"
                + "        case 2:\n"
                + "          this.dialogDelete = true;\n"
                + "          break;\n"
                + "          case 3:\n"
                + "          this.republishClicked(items);\n"
                + "          break;\n"
                + "      }\n"
                + "    },\n";

        return "methods: {\n" + selected + " "
                + "},\n";

    }
    
       private String script() {
        return "<script>\n"
                + imports()
                + "export default {\n"
                + "  name: \"" + objectName + "\",\n"
                + data()
                
                + "\n"
               
                + methods()
                + "};\n"
                + "</script>";
    }

    public String create() {
        return this.template.concat(this.script());
    }

}
