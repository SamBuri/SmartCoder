/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.springboot;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.ProjectDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.FileModel;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import java.util.List;

/**
 *
 * @author samburiima
 */
public class Response extends DtoClass{

    public Response(FileModel fileModel) {
        super(fileModel);
    }

   

    private String makeImport(FieldDAO fieldDAO) {
        String enumPackage = (fieldDAO.referencesIN(project)) ? project.getBasePackage() : commonProject.getBasePackage();
        return "import " + enumPackage + ".enums." + fieldDAO.getReferences().concat(";\n");
    }

    private String makeImports() {
        String imp= "import "+this.commonProject.getBasePackage()+".dtos.ResponseData;\n";
        imp = this.fields.stream()
                .filter(p->p.getEnumerated())
                .map(f->makeImport(f))
                .distinct()
                .reduce(imp, String::concat);
        return imp;
    }

  

    private String makeField(FieldDAO fieldDAO) {
        if (forceReferences(fieldDAO) && !fieldDAO.getEnumerated()) {
            return "String " + fieldDAO.getVariableName().concat("Id, ".concat("String ").concat(fieldDAO.getVariableName()));
        }
        return fieldDAO.getDeclaration(false, false);
    }
    
    

    private String makeRecord() {

        String construtorLine = "";
        for (int i = 0; i < fields.size(); i++) {
            FieldDAO field = this.fields.get(i);

            if (i == 0) {
                construtorLine += makeField(field);
            } else {
                construtorLine += "," + makeField(field);
            }

        }

        return "public record " + this.objectName + "Response (\n" + construtorLine + ") implements ResponseData{\n"
                + "\n"
                + "}";
    }

    @Override
    public String create() {
        return "package ".concat(project.getBasePackage() + "." + objectName.toLowerCase().concat(".dtos;\n"))
                .concat(this.makeImports())
                .concat(this.makeRecord());
    }

    @Override
    protected String getFileName() {
       return objectName.concat("Response");
    }

}
