/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.springboot;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.CodeGenerator;
import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.JavaClass;
import com.saburi.utils.Enums;
import com.saburi.utils.Enums.EntityTypes;
import com.saburi.utils.Enums.keys;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import static com.saburi.utils.Utilities.makeAbstractMethod;
import static com.saburi.utils.Utilities.makeAnnotedAbstractMethod;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Hp
 */
public class Repository extends SpringbootUtils {


    public Repository(FileModel fileModel) {
        super(fileModel);
    }

   

    public String makeImports(Project project, EntityTypes entityTypes) throws Exception {
       
        String imp = "import " + commonProject.getBasePackage() + ".repositories.PagingAndSortingRepo;\n";
        imp += "import " + project.getBasePackage() + ".".concat((objectName).toLowerCase()).concat(".dtos.") + objectName.concat(Enums.WebFiles.Mini.name()).concat(";\n");
        imp += "import java.util.List;\n";

        if (entityTypes.equals(EntityTypes.Auto_ID_Gen)) {
            imp += "import java.util.Optional;".concat("\n")
                    .concat("import org.springframework.data.jpa.repository.Query;").concat("\n");
        }
//        List<String> references 
        imp = fields.stream().filter(f -> (f.referencesIN(project)
                && (f.getKey().equalsIgnoreCase(keys.Unique_Group.name())
                || f.getKey().equalsIgnoreCase(keys.Unique.name())
                || f.getSaburiKey().equalsIgnoreCase(Enums.Saburikeys.ID_Generator.name())))
        )
                .map(fd -> fd.getReferences())
                .distinct()
                .map(r -> "import " + project.getBasePackage()
                + "." + r.toLowerCase() + "." + r + ";\n").reduce(imp, String::concat);
//                .collect(Collectors.toList());
//
//        imp = references.stream().map(reference -> "import " + project.getBasePackage()
//                + "." + reference.toLowerCase() + "." + reference + ";\n").reduce(imp, String::concat);
        return imp;
    }

    public String makeProperties() {
        String properties = "";
        properties = fields.stream().map(field -> field.makeProperties(forceReferences(field))).reduce(properties, String::concat);
        return properties;
    }

    public String methods(EntityTypes entityTypes) {

        String mtds = "";
        String dotChar = "e";

        if (entityTypes.equals(EntityTypes.Auto_ID_Gen)) {
            String querry = "SELECT MAX(idHelper) from " + objectName + " " + dotChar + " ";
            String params = "";
            FieldDAO idGenerator = Utilities.getIDGenerator(fields);
            if (idGenerator != null) {
                params = idGenerator.getDeclaration(forceReferences(idGenerator), false);
                querry += "where " + dotChar + "." + idGenerator.getVariableName() + "=?1";
            }
            mtds += makeAnnotedAbstractMethod("\n @Query(\"" + querry + "\")", "public", "Optional<Integer>", "getMaxIdHelper", params);

        }
        mtds = fields.stream().filter((f -> f.getKey().equalsIgnoreCase(keys.Unique.name()))).map(fieldDAO -> makeAbstractMethod("\npublic", "List<" + objectName.concat(Enums.WebFiles.Mini.name()) + ">", "findBy" + fieldDAO.getFieldName(), fieldDAO.getDeclaration(true, false))).reduce(mtds, String::concat);

        List<FieldDAO> uniqueGroups = fields.stream().filter(f -> f.getKey()
                .equalsIgnoreCase(keys.Unique_Group.name())).collect(Collectors.toList());

        if (!uniqueGroups.isEmpty()) {
            String methodName = "findBy";
            String methodParams = "";
            int last = uniqueGroups.size() - 1;
            for (int i = 0; i <= last; i++) {
                FieldDAO fdao = uniqueGroups.get(i);
                methodParams += fdao.getDeclaration(forceReferences(fdao), false);
                methodName += "" + fdao.getFieldName();

                if (i != last) {
                    methodName += "And";
                    methodParams += ",";
                }
            }
            mtds += makeAbstractMethod("public", "List<" + objectName.concat(Enums.WebFiles.Mini.name()) + ">", methodName, methodParams);
        }

        String exm = "";

        exm = fields.stream().filter(p -> p.isExpose()).map(fieldDAO -> createExpose(fieldDAO)).reduce(exm, String::concat);
        mtds += exm;
        return mtds;

    }

    private String createExpose(FieldDAO fieldDAO) {
        boolean referencesIn = fieldDAO.referencesIN(project);
        String id = referencesIn ? "Id" : "";
        String param= fieldDAO.getUsableDataType(false).concat(" ")
                        .concat(referencesIn ? "id" : fieldDAO.getVariableName());
        
        return "public List<" + objectName.concat(Enums.WebFiles.Mini.name()) + "> findBy"
                .concat(fieldDAO.getFieldName()).concat(id).concat("(")
                .concat(param).concat(");\n");
    }

    @Override
    protected boolean isValid() throws Exception {
          CodeGenerator.validate(fields, project);
        return super.isValid(); 
    }
    
    

    public String makeClass(Project project, Enums.EntityTypes entityTypes) throws Exception {
      
        String className = objectName + "" + Enums.WebFiles.Repo.name();

        String methods = methods(entityTypes).concat("\npublic List<" + objectName.concat(Enums.WebFiles.Mini.name()) + "> findAllBy();\n");

        String dataType = primaryKeyFied == null ? Utilities.getIdWrapperDataType(entityTypes) : primaryKeyFied.getDataType();

        String packageName = project.getBasePackage() + "." + objectName.toLowerCase();
        JavaClass javaClass = new JavaClass(packageName, className, this.makeImports(project, entityTypes), methods);
        return javaClass.makeInterfaceExt("PagingAndSortingRepo<" + objectName + ", " + dataType + ">");
    }

    @Override
    protected String getFileName() {
         return repo;
    }

    @Override
    protected String create() throws Exception {
        return makeClass(project, entityType);
    }

}
