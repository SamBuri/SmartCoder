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
public class Store extends Vue3Utils{

    public Store(FileModel fileModel) {
        super(fileModel);
    }

 
    
    private String imports() {
        return "import { defineStore } from \"pinia\";\n"
                + "import " + objectVariableName + "Nav from \"./" + objectName + "Nav\";\n"
                + "import {defineRootStore} from \"@/root/RootStore\";\n";
    }

    private String state() {
        return "state: () => ({\n"
                + "path: " + objectVariableName + "Nav.menu.path,\n"
                + "    mini: [],\n"
                + "    miniLoading: false,\n"
                + "  }),\n";
    }

  

    private String getMini() {
        return " getMini() {\n"
                + "\n"
                + " if(this.mini.length>0) return this.mini;\n"
                + "      const rootStore = defineRootStore();\n"
                + "       let data = rootStore.fetch(`${this.path}/mini`,\n"
                + "       ()=>{\n"
                + "        this.miniLoading =true\n"
                + "        this.mini = [];\n"
                + "\n"
                + "      },\n"
                + "\n"
                + "       res=>this.mini = res.data,\n"
                + "\n"
                + "       ()=>this.miniLoading = false); \n"
                + "       return data;\n"
                + "\n"
                + "     },\n";
    }

    private String getActions() {
        return this.getMini();
    }

    private String actions() {
        return String.format("actions: {\n%s\n}", getActions());
    }

    private String makeFunction() {
        return "   export const define" + objectName + "Store = defineStore(\"" + objectVariableName + "\", {\n"
                + " \n"
                        .concat(state())
                        .concat(actions())
                + "});\n";
    }

    @Override
    public String create() {
        return this.imports().concat(this.makeFunction());
    }

    @Override
    protected String getFileName() {
       return objectName.concat(store);
    }

    @Override
    protected String getFileExtension() {
        return "js";
    }

}
