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
import com.saburi.utils.Utilities;

/**
 *
 * @author Hp
 */
public class ServiceTest extends TestClass{

    private static final int OBJECT_NUMBER = 3;

    public ServiceTest(FileModel fileModel) {
        super(fileModel);
    }

  
    public String makeImports(Project project, Enums.ServiceTypes serviceTypes) throws Exception {
        String dataTypes = Utilities.getNonPrimitiveDataTypeImports(fields);
        return "import " + project.getBasePackage() + "." + objectName.toLowerCase() + ".dtos." + this.objectName + "Request;\n"
                +dataTypes
                + "import org.junit.jupiter.api.Assertions;\n"
                + "import org.junit.jupiter.api.BeforeEach;\n"
                + "import org.junit.jupiter.api.Test;\n"
                + "import org.springframework.beans.factory.annotation.Autowired;\n"
                + "import org.springframework.boot.test.context.SpringBootTest;\n"
                + "import org.springframework.boot.testcontainers.service.connection.ServiceConnection;\n"
                + "import org.springframework.test.context.ActiveProfiles;\n"
                + "import org.testcontainers.containers.MySQLContainer;\n"
                + "import org.testcontainers.junit.jupiter.Container;\n"
                + "import org.testcontainers.junit.jupiter.Testcontainers;\n"
                + "import org.testcontainers.utility.DockerImageName;\n";
    }

    private String makeClassFields() {

        return " @Container\n"
                + "    @ServiceConnection\n"
                + "    static MySQLContainer mySQLContainer = new MySQLContainer<>(\n"
                + "            DockerImageName.parse(\"mysql:8.0-debian\"));\n"
                + "\n"
                + "    \n"
                + "    @Autowired\n"
                + "    private " + objectName + "Service " + objectVariableName + "Service;\n"
                + "   ";
    }

 
    private String buildField(FieldDAO fieldDAO, String count) {
        String dataType = fieldDAO.getUsableDataType(this.forceReferences(fieldDAO));
        boolean referencesIn = fieldDAO.referencesIN(project);
        String id = referencesIn ? "Id" : "";

        String value = "";
        if (fieldDAO.isPrimitiveNumber()) {
            value = "1000";
        } 
        
        else if (fieldDAO.isBigDecimal()) {
            value = "new BigDecimal(\"1000\")";
        }
        else if (fieldDAO.isDate()) {
            value = "LocalDate.now()";
        } else if (fieldDAO.isDateTime()) {
            value = "LocalDateTime.now()";
        } else if (dataType.equalsIgnoreCase("String")) {
            count = count.equals("") ? "" : " +" + count;
            value = "\"" + fieldDAO.getCaption() + "\"" + count;
        } else if (dataType.equalsIgnoreCase("boolean")) {

            value = "false";
        } else {
            value = "";
        }

        return value.equals("") ? "" : "." + Utilities.getVariableName(fieldDAO.getDBColumnName(referencesIn))+ "(" + value + ")\n";
    }

    public String buildObject() {
        String builderParam = "";
        for (FieldDAO f : this.fields) {
            builderParam += this.buildField(f, "");
        }
        return objectName + "Request.builder()\n" + builderParam + ".build()";

    }

    public String beforeEach() {
        String builderParam = "";
        for (FieldDAO f : this.fields) {
            builderParam += this.buildField(f, "i");
        }
        return "    @BeforeEach\n"
                + "    public void setup() throws Exception {\n"
                + "        for (int i = 0; i < " + OBJECT_NUMBER + "; i++) {\n"
                + "            " + objectVariableName + "Service.save(" + objectName + "Request.builder()\n"
                + "                    " + builderParam + ".build()\n"
                + "            );\n"
                + "        }\n"
                + "\n"
                + "    }\n";
    }

    public String testSave() {
        return "@Test\n"
                + "    public void should" + objectName + "() throws Exception {\n"
                + "        try {\n"
                + "\n"
                + "           \n"
                + "           \n"
                + "            \n"
                + "             " + objectName + " " + objectVariableName + " =" + objectVariableName + "Service.save(" + this.buildObject() + "\n"
                + "            ).getEntity();\n"
                + "\n"
                + "            System.out.println(\"Returned id: \" + " + objectVariableName + ".getId());\n"
                + "            Assertions.assertNotNull(" + objectVariableName + ", \"The returned object must not be null\");\n"
                + "            Assertions.assertNotNull(" + objectVariableName + ".getId(), \"Id should be not be null\");\n"
                + "            long count = " + objectVariableName + "Service.count();\n"
                + "            Assertions.assertEquals(4, count, \"Returned records should be equal to 4\");\n"
                + "        } catch (Exception e) {\n"
                + "        }\n"
                + "\n"
                + "    }\n";
    }

    private String methods() {

        return beforeEach() + testSave();

    }
    
    private String className(){
        return objectName + "" + Enums.WebFiles.ServiceTest.name();
    }

    public String makeClass(Project project, Enums.ServiceTypes serviceTypes) throws Exception {
        CodeGenerator.validate(fields, project);
       
        String methods = methods();
        String annotation = "@SpringBootTest\n"
                + "@Testcontainers\n"
                + "@ActiveProfiles(\"test\")\n";

        String packageName = project.getBasePackage() + "." + objectName.toLowerCase();
        JavaClass javaClass = new JavaClass(packageName, className(), this.makeImports(project, serviceTypes), this.makeClassFields() + methods);
        return javaClass.makeClass("", annotation);
    }

    @Override
    protected String getFileName() {
        return className();
    }

    @Override
    protected String create() throws Exception {
        return makeClass(project, fileModel.getServiceType());
    }

}
