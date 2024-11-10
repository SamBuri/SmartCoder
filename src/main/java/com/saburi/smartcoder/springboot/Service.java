/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.springboot;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.CodeGenerator;
import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.JavaClass;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.ServiceTypes;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.makeMethod;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Hp
 */
public class Service extends SpringbootUtils{

    public Service(FileModel fileModel) {
        super(fileModel);    
    }

    public String makeImports(Project project, Enums.ServiceTypes serviceTypes)  {
        
        String imp = "import " + commonProject.getBasePackage() + ".repositories.PagingAndSortingRepo;\n";
        imp += "import " + project.getBasePackage() + "." + objectName.concat(".dtos").toLowerCase() + "." + requestObject + ";\n"
                + "import org.springframework.beans.factory.annotation.Autowired;"
                + "import org.springframework.stereotype.Service;\n"
                + "import " + commonProject.getBasePackage() + ".services.".concat(Utilities.getParentService(serviceTypes)).concat(";\n"
                + "import " + commonProject.getBasePackage() + ".dtos.ResponseData;\n"
                + "import " + commonProject.getBasePackage() + ".utils.KnownException;\n"
                + "import java.util.List;");

        if (serviceTypes.equals(Enums.ServiceTypes.ID_Gen)) {
            if (idGenerator != null) {
                if (idGenerator.isReference()) {
                    imp += "import " + project.getBasePackage() + "." + idGenerator.getReferences().toLowerCase() + "." + idGenerator.getReferences() + ";\n";
                }
            }
        }

        if (fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique.name()) || p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).count() > 0) {
            imp += "import java.util.Objects;\n";
        }

        List<FieldDAO> collections = fields.stream().filter((p) -> p.isCollection())
                .distinct().collect(Collectors.toList());

        for (FieldDAO f : collections) {
            imp += "import " + project.getBasePackage() + "." + f.getReferences().toLowerCase() + "." + f.getReferences() + ";\n";
            imp += "import java.util.ArrayList;\n";
        }

      
           String ref ="";
           ref= fields.stream()
                .filter(f -> f.isForeignKey(forceReferences(f)))
                .map(f -> getPackageImport(f))
                .distinct()
                .reduce(ref, String::concat);
           imp+=ref;

        if (this.createModify()) {
            imp += "import " + commonProject.getBasePackage() + ".utils.SpringUtil;\n";
        }

        return imp;
    }

    private String getPackageImport(FieldDAO f) {
        if (forceReferences(f)) {
            String references = f.getReferences();

            String imp = "import " + project.getBasePackage() + "." + references.toLowerCase()
                    .concat(".")
                    .concat(references);
            return imp.concat(";\n").concat(imp.concat("Repo;\n"));
        }

        return "";

    }

    private boolean createModify() {
        return this.fields
                .parallelStream()
                .filter((p) -> p.referencesLookupExt(project) || p.referencesAccountExt(project))
                .count() > 0;
    }

    private String makeClassFields(ServiceTypes serviceTypes) {
        String resourceID = serviceTypes.equals(ServiceTypes.ID_Gen) ? "private static final int RESOURCE_ID = 1;\n" : "";

        String properties = resourceID
                + "    private static final String ENTITY_CAPTION = \"" + this.fileModel.getObjectCaption() + "\";\n"
                + "    @Autowired\n"
                + "    private " + objectName + "Repo " + objectVariableName + "Repo;\n";

        String prop = "";
        prop = this.fields.stream()
                .filter(f -> f.isForeignKey(forceReferences(f)))
                .map(f -> this.makeRepoProps(f, forceReferences(f)))
                .distinct()
                .reduce(prop, String::concat);
        properties += prop;

        if (this.createModify()) {
            properties += "    @Autowired\n"
                    + "    private SpringUtil springUtil;\n";
        }
        return properties;
    }

    private String getReferenceRepoVariableName(FieldDAO f) {
        return Utilities.getVariableName(f.getReferences()).concat("Repo");
    }

    private String makeRepoProps(FieldDAO f, boolean forceReference) {
        if (f.isForeignKey(forceReference)) {
            return "@Autowired\nprivate " + f.getReferences() + "Repo  " + getReferenceRepoVariableName(f) + ";\n";
        }
        return "";
    }

    private String refSetVariable(FieldDAO fieldDAO) {
        if (!fieldDAO.isForeignKey(forceReferences(fieldDAO))) {
            return "";
        }
        String ref = fieldDAO.getVariableName();
        String refReq = fieldDAO.getReqFieldName(forceReferences(fieldDAO));
        String refReqLine = "req.get" + refReq + "()";

        return fieldDAO.getReferences() + " " + ref + " = " + getReferenceRepoVariableName(fieldDAO) + "\n"
                + "                .findById(" + refReqLine + ")\n"
                + "                .orElseThrow(()->new KnownException(\"No " + fieldDAO.getCaption() + " found with id: \"+" + refReqLine + "));";
    }

//     private String makeRepo(FieldDAO f, boolean forceReference) {
//        if (f.isForeignKey(forceReference)) {
//            return f.getFieldName() + "Repo = " + f.getVariableName() + "Repo;";
//        }
//        return "";
//    }
    private String validateMethod(String firstChar) {

        List<FieldDAO> uniqueGroups = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique_Group.name())).collect(Collectors.toList());

        List<FieldDAO> unique = fields.stream().filter((p) -> p.getKey()
                .equalsIgnoreCase(Enums.keys.Unique.name())).collect(Collectors.toList());
        String validateBody = "";
        if (!uniqueGroups.isEmpty()) {
            String message = "\"The record with ";
            String uniqueGroupValidate = "";
            int last = uniqueGroups.size() - 1;
            String methodName = "findBy";
            String methodParams = "";
            for (int i = 0; i <= last; i++) {
                FieldDAO fdao = uniqueGroups.get(i);
                methodParams += objectVariableName.concat(".") + fdao.getCall();
                methodName += "" + fdao.getFieldName();

                if (i != last) {
                    methodName += "And";
                    methodParams += ",";
                }

                message += "" + fdao.getCaption() + ": \"+" + objectVariableName + "." + fdao.getCall() + fdao.getReferenceDisplayText(this.forceReferences(fdao)) + "+";

                if (i != last) {
                    message += " \" and ";
                }
            }
            message += "\" already exists\"";
            uniqueGroupValidate += "if (" + repoVariableName + "." + methodName + "(" + methodParams + ")\n"
                    + "                .stream().filter(" + firstChar + " -> !Objects.equals(" + firstChar + ".getId(), " + objectVariableName + ".getId())).count() > 0) {\n"
                    + "            throw new KnownException(" + message + ");\n"
                    + "        }";

            validateBody += uniqueGroupValidate;
        }

        String uniqueValidate = "";

        for (FieldDAO fieldDAO : unique) {
            uniqueValidate += "if (" + repoVariableName + ".findBy" + fieldDAO.getFieldName() + "(" + objectVariableName + "." + fieldDAO.getCall() + ")\n"
                    + "                .stream().filter(" + firstChar + " -> !Objects.equals(" + firstChar + ".getId(), " + objectVariableName + ".getId())).count() > 0) {\n"
                    + "            throw new KnownException(\"A record with name: \" + " + objectVariableName + "." + fieldDAO.getCall() + "+ \" already exists\");\n"
                    + "        }";
        }

        validateBody += uniqueValidate;
        validateBody += "return super.isValid(" + objectVariableName + ");";
        String params = objectName.concat(" ").concat(objectVariableName);
        String validateMethod = Utilities.makeThrowsMethod("@Override\npublic", "boolean", "isValid", params, validateBody);
        return validateMethod;
    }

    private String predictModifyMtdName(FieldDAO fiedDAO) {
        String lookupName = fiedDAO.getFieldName();
        int length = lookupName.length();
        if (lookupName.substring(length - 2, length).equalsIgnoreCase("id")) {
            lookupName = lookupName.substring(0, length - 2);
        }
        return lookupName;
    }

    private String modifyLine(FieldDAO fieldDAO, String ov) {
        if (fieldDAO.referencesLookupExt(this.project)) {
            return ov + ".set" + this.predictModifyMtdName(fieldDAO) + "(springUtil.getLookupDataName(" + ov + "." + fieldDAO.getCall() + ", \"" + fieldDAO.getCaption() + "\"));\n";
        } else if (fieldDAO.referencesAccountExt(this.project)) {
            return ov + ".set" + this.predictModifyMtdName(fieldDAO) + "(springUtil.getAccountName(" + ov + "." + fieldDAO.getCall() + "));\n";
        }
        return "";
    }

    private String makeModifyMethod() {
        String modify = "";
        String ov = objectVariableName.substring(0, 1);
        modify = fields.stream()
                .filter((p) -> p.referencesLookupExt(this.project)
                || p.referencesAccountExt(this.project))
                .map(fieldDAO -> modifyLine(fieldDAO, ov)).reduce(modify, String::concat);

        if (!modify.isBlank()) {
            return "@Override\n"
                    + "    protected " + objectName + " modify(" + objectName + " " + ov + ") throws Exception {\n"
                    + modify
                    + "return " + ov + ";\n"
                    + "}\n";

        }

        return modify;
    }

    private String setLine(FieldDAO fieldDAO) {
        if (fieldDAO.isCollection()) {
            return "  List<" + fieldDAO.getReferences() + "> " + fieldDAO.getVariableName() + " = new ArrayList<>();\n"
                    + "        req." + fieldDAO.getCall() + ".forEach(t->{\n"
                    + "        t.set" + objectName + "(e);\n"
                    + "        " + fieldDAO.getVariableName() + ".add(t);\n"
                    + "        });\n"
                    + "        e.set" + fieldDAO.getFieldName() + "(" + fieldDAO.getVariableName() + ");";
        }

        String body = this.refSetVariable(fieldDAO);
        String getbytes = fieldDAO.getDataType().equalsIgnoreCase("Image") ? ".getBytes()" : "";
        String call = fieldDAO.isForeignKey(forceReferences(fieldDAO))
                ? fieldDAO.getVariableName() : "req." + fieldDAO.getCall() + getbytes;
        body += "e.set" + fieldDAO.getFieldName() + "(" + call + ");\n";
        return body;
    }

    

    private String createExpose(FieldDAO fieldDAO) {
        boolean referencesIn = fieldDAO.referencesIN(project);
        String id = referencesIn ? "Id" : "";

        String param = fieldDAO.getUsableDataType(false).concat(" ")
                .concat(referencesIn ? "id" : fieldDAO.getVariableName());

        return "public List<? extends ResponseData> getBy"
                .concat(fieldDAO.getFieldName()).concat(id).concat("(")
                .concat(param)
                .concat("){\n")
                .concat("return " + repoVariableName + ".findBy" + fieldDAO.getFieldName().concat(id) + "(" + (referencesIn ? "id" : fieldDAO.getVariableName()) + ");")
                .concat("}\n");
    }

    private String methods(ServiceTypes serviceTypes) {

        String params = "";
        String firstChar = this.objectName.substring(0, 1).toLowerCase();

        String mtds = makeMethod("@Override\npublic", "String", "getEntityCaption", "", "return ENTITY_CAPTION;");
        mtds += makeMethod("@Override\npublic", "PagingAndSortingRepo<" + this.objectName + ", " + idDataType + ">", "getRepository", "", "return " + objectVariableName.concat("Repo") + ";");

        mtds += makeMethod("@Override\npublic", "List<? extends ResponseData>", 
                "getMiniData", "", "return " + repoVariableName + ".findAllBy();");
        if (serviceTypes.equals(ServiceTypes.ID_Gen)) {
            mtds += makeMethod("@Override\npublic", "int", "getResourceID", "", "return RESOURCE_ID;");
            String cast = "";

            if (idGenerator != null) {
                params = idGenerator.getDeclaration(true, false);

                cast = idGenerator.isReference() ? idGenerator.getReferences() : idGenerator.getDataType();
                cast = "(" + cast + ")object";
            }

            mtds += """
                    @Override
                        protected int getNextIdHelper(Object object) {
                            return """.concat(" ") + (repoVariableName) + ".getMaxIdHelper(" + cast + ").orElse(0) + 1;\n"
                    + "    }\n";

            mtds += " @Override\n"
                    + "    protected " + objectName + " modifyToSave(" + objectName + " " + objectVariableName + ") throws  Exception {\n"
                    + "       \n"
                    + "        if (" + objectVariableName + ".getId() == null) {\n"
                    + "            " + objectVariableName + ".setIdHelper(this.getNextIdHelper(" + objectVariableName + "));\n"
                    + "            " + objectVariableName + ".setId(this.getNextID(" + objectVariableName + "));\n"
                    + "        }\n"
                    + "        return " + objectVariableName + ";\n"
                    + "    }";
        }
        mtds += makeModifyMethod();

        String exm = "";

        exm = fields.stream().filter(p -> p.isExpose()).map(fieldDAO -> createExpose(fieldDAO)).reduce(exm, String::concat);
        mtds += exm;

        mtds += validateMethod(firstChar);

        String requestVaiableName = Utilities.getVariableName(requestObject);

        String createMethodBody = "return new " + objectName + "();";

        mtds += Utilities.makeMethod("@Override\npublic", objectName, "instantiate", "", createMethodBody);

        String setEntityBody = "";
//        setEntityBody += this.fields.stream()
//                .filter(f -> f.isForeignKey(forceReferences(f)))
//                .map(field -> this.refSetVariable(field))
//                .reduce(setEntityBody, String::concat);

        setEntityBody += this.fields.stream().map(field -> this.setLine(field))
                .reduce(setEntityBody, String::concat);
        setEntityBody += "return e;\n";
        mtds += "@Override\n"
                + "    public " + objectName + " setEntity(" + objectName + " e, " + requestObject + " req) throws Exception{\n"
                + "" + setEntityBody + "\n}\n";

        return mtds;

    }

    public String makeClass(Project project, Enums.ServiceTypes serviceTypes) throws Exception  {
        CodeGenerator.validate(fields, project);
        String className = objectName + "" + Enums.WebFiles.Service.name();

        String methods = methods(serviceTypes);

        String packageName = project.getBasePackage() + "." + objectName.toLowerCase();
        JavaClass javaClass = new JavaClass(packageName, className, this.makeImports(project, serviceTypes), this.makeClassFields(serviceTypes) + methods);
        return javaClass.makeClass(Utilities.getParentService(serviceTypes) + "<" + objectName + "," + requestObject + ", " + idDataType + ">", "@Service");
    }

    @Override
    protected boolean isValid() throws Exception {
        CodeGenerator.validate(fields, project);
        if(Utilities.isNullOrEmpty(entityType.name())) throw  new Exception("Entity Type is required!");
        if(Utilities.isNullOrEmpty(fileModel.getServiceType().name())) throw  new Exception("Service Type is required!");
        return super.isValid(); 
    }
    
    

    @Override
    protected String getFileName() {
        return this.service;
    }


    @Override
    protected String create() throws Exception{
       return this.makeClass(this.project, this.fileModel.getServiceType());
    }

}
