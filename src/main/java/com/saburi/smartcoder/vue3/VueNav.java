/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.vue3;

import com.saburi.dataacess.FieldDAO;
import com.saburi.smartcoder.FileModel;
import com.saburi.utils.Utilities;

/**
 *
 * @author samburiima
 */
public class VueNav extends Vue3Utils{

    private final String searchObjectName;

    public VueNav(FileModel fileModel) {
        super(fileModel);
        this.searchObjectName = Utilities.toPlural(objectName);
       
    }
    
    private String imports(){
        return "import "+objectName+" from './"+objectName+".vue'\n "
                + "import "+searchObjectName+" from './"+searchObjectName+".vue'\n";
    }

    private String objectHead() {
          int width = 700;
        int size = this.fields.size();
        if (size > 8) {
            width = 1000;
         
        }
        return "id: \"" + this.moduleName.toLowerCase() + "." + this.objectName.toLowerCase() + "\",\n"
                + "        title: \"" + Utilities.toPlural(this.fileModel.getObjectCaption()) + "\",\n"
                + "        component: " + this.objectName + ",\n"
                + "        path: \"" + Utilities.toPlural(this.objectName.toLowerCase()) + "\",\n"
                + "        width: \""+width+"px\",\n";

    }

    private String headerValue(FieldDAO fieldDAO) {
        return fieldDAO.isReferance() && !fieldDAO.getEnumerated() ? fieldDAO.getVariableName() : fieldDAO.getVariableName();
    }

    private String header(FieldDAO fieldDAO) {
        String numeric = " ";
        if(fieldDAO.isNumeric()){
            numeric = " ,isNumeric: true";
        }
        
        String isDate = " ";
        if(fieldDAO.getDataType().equalsIgnoreCase("Date")||fieldDAO.getDataType().equalsIgnoreCase("LocalDate")){
            isDate = ", isDate: true";
        }
        
        String isDateTime = " ";
        if(fieldDAO.getDataType().equalsIgnoreCase("DateTime")||fieldDAO.getDataType().equalsIgnoreCase("LocalDateTime")){
            isDateTime = ", isDateTime: true";
        }
        return "{ title: \"" + fieldDAO.getCaption() + "\", key: \""+ headerValue(fieldDAO)+"\"" +numeric+" "+isDate+""+isDateTime+"},\n";

    }

    private String headers() {
        String headers = "{\n"
                + "                title: \"Id\",\n"
                + "                align: \"start\",\n"
                + "                // sortable: false,\n"
                + "                key: \"id\",\n"
                + "            },\n";

        headers = fields.stream().map(fieldDAO -> this.header(fieldDAO)).reduce(headers, String::concat);
        headers = headers.concat(this.constantHeaders());
        headers = "headers: [" + headers + "],\n";

        return headers;

    }
    
    String constantHeaders(){
     return " { title: \"Branch\", key: \"branch\",},\n"
             + "{ title: \"Creation Date\", key: \"creationDate\",  label: \"Creation Date\", field: \"creationDate\", isDateTime: true },\n" +
"                { title: \"Last Modified Date\", key: \"lastModifiedDate\", isDateTime: true},\n" +
"                { title: \"Created By\", key: \"createdBy\", },\n" +
"                { title: \"Modified By\", key: \"modifiedBy\", }";
    }
    
    private String editHeaders() {
        String headers = "";

        headers = fields.stream().map(fieldDAO -> this.header(fieldDAO)).reduce(headers, String::concat);
        headers+="{title: \"Actions\", key: \"actions\"}";
        headers = "editHeaders: [" + headers + "],";

        return headers;

    }

    private String subMenus() {
        String lowername = objectName.toLowerCase();
        return "children: ["
                 + createSubMenu("View", Utilities.toPlural(lowername), "").concat(",\n")
                + createSubMenu("New", lowername, "params: {mode:0}").concat(",\n")
                 + createSubMenu("Edit", lowername, "params: {mode:1}").concat(",\n")
                 + createSubMenu("History", lowername, "params: {mode:2}").concat(",\n")
                .concat("]");
    }

    private String createSubMenu(String name,String path, String params) {
        String to = " to:{ name: \""+path+"\", "+params+"}";
        return "{ id: \"" + this.moduleName + "." + objectVariableName + "." 
                + name.replaceAll(" ", "").toLowerCase() + "\", title: \"" + name + "\", "
                + to.concat("}");
    }
    
    private String routes(){
        String editPath = objectName.toLowerCase();
        String searchPath = this.searchObjectName.toLowerCase();
        
        return " routes:[\n" +
"        {\n" +
"                path: '/" + editPath + "/:mode',\n" +
"                name: '" + editPath + "',\n" +
"                component: " + objectName + ",\n" +
"                meta: { auth: true },\n" +
"         },\n" +
"         {\n" +
"                path: '/"+searchPath+"',\n" +
"                name: '"+searchPath+"',\n" +
"                component: "+searchObjectName+",\n" +
"                meta: { auth: true },\n" +
"         },\n" +
"        \n" +
"        ],\n";
        
    }
    

    @Override
    public String create() {
        String objectNav = Utilities.getVariableName(this.objectName).concat("Nav");
        return this.imports()+" const "
                .concat(objectNav)
                .concat("=")
                .concat("{")
                .concat(routes())
                .concat("menu:{")
                .concat(this.objectHead())
                .concat(this.editHeaders())
                .concat(this.headers())
                .concat(this.subMenus())
                .concat("}\n}\n")
                .concat("export default ".concat(objectNav).concat(";"));

    }

    @Override
    protected String getFileName() {
       return objectName.concat("Nav");
    }

    @Override
    protected String getFileExtension() {
        return "js";
    }

}
