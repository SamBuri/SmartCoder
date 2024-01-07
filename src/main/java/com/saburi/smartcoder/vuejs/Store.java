/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.vuejs;

import com.saburi.utils.Utilities;

/**
 *
 * @author samburiima
 */
public class Store {

    private final String objectName;
    private String objectNameNav;
    private String objectNameNavVariableName;

    public Store(String objectName) {
        this.objectName = objectName;
        this.objectNameNav = objectName.concat("Nav");
        this.objectNameNavVariableName = Utilities.getVariableName(this.objectNameNav);
    }

    private String imports() {
        return "import httmMethods from '../../utils/HttpMethods'\n"
                + "import " + this.objectNameNavVariableName + " from './" + this.objectNameNav + "';\n";
    }
    

    private String state = "state: {\n"
            + "        mini:[],\n"
            + "        miniLoading: false,\n"
            + "        \n"
            + "    },\n";

    private String mutations = "mutations: {\n"
            + "\n"
            + "        mini(state, mini) {\n"
            + "            state.mini = mini;\n"
            + "        },\n"
            + "         miniLoading(state, miniLoading) {\n"
            + "            state.miniLoading = miniLoading;\n"
            + "        },\n"
            + "    },\n";

    private String actions() {
        return " actions: {      getMini(context) {\n"
                + "            if (context.mini) { return }\n"
                + "             context.state.miniLoading= true;\n"
                + "            httmMethods.get(`${" + this.objectNameNavVariableName + ".menu.path}/mini`)\n"
                + "                .then(response => {\n"
                + "                    context.state.mini= response.data;\n"
                + "                    context.state.miniLoading= false;\n"
                + "                }).catch(e => {\n"
                + "                    context.state.mini= [];\n"
                + "                   console.log(e);\n"
                + "                    context.state.miniLoading= false;\n"
                + "                })\n"
                + "\n"
                + "        },\n"
                + "}";

    }

    public String create() {
        return imports() + "export default {\n"
                + "namespaced: true,\n"
                        .concat(state)
                        .concat(actions())
                + "}";
    }

}
