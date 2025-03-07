/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.springboot;

import com.saburi.dataacess.FieldDAO;
import com.saburi.smartcoder.CodeGenerator;
import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.JavaClass;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;
import java.util.List;

/**
 *
 * @author Hp
 */
public class Mini extends DtoClass {

    private final boolean isPKNull;

    public Mini(FileModel fileModel) {
        super(fileModel);

        isPKNull = this.primaryKeyFied == null;

    }

    @Override
    protected List<String> getImports(FieldDAO fieldDAO) throws Exception {
        return List.of(referecesImports(fieldDAO),
                fieldDAO.getDataTypeImps(),
                fieldDAO.getGenericDataTypeImps());
    }

    private String makeImports() throws Exception {

        String imp = "";
        imp += Utilities.makeResponseImport(project);

//        List<String> imports = new ArrayList();
//        for (FieldDAO t : this.fields) {
//            t.miniImports(project).forEach(i -> addIfNotExists(imports, i));
//
//        }
//        for (String impo : imports) {
//            imp += impo + ";\n";
//        }
        imp += super.getImports();
        return imp;
    }

    private String makeAFields() {
        String annotedFields = isPKNull ? idDataType.concat(" ").concat("get").concat("Id();").concat("\n") : "";

        for (FieldDAO field : this.fields) {

            annotedFields += field.getUsableDataType(this.forceReferences(field)) + field.getCall().concat(";").concat("\n");

        }
        return annotedFields;
    }

    public String makeClass() throws Exception {

        String className = objectName + "" + Enums.WebFiles.Mini.name();

        String packageName = project.getBasePackage() + "." + objectName.toLowerCase().concat(".dtos");
        JavaClass javaClass = new JavaClass(packageName, className, this.makeImports(), this.makeAFields());
        return javaClass.makeInterfaceExt(Utilities.RESPONSE_INERFACE);
    }

    @Override
    protected boolean isValid() throws Exception {
        CodeGenerator.validate(fields, project);
        return super.isValid();
    }

    @Override
    protected String getFileName() {
        return mini;
    }

    @Override
    protected String create() throws Exception {
        return this.makeClass();
    }

}
