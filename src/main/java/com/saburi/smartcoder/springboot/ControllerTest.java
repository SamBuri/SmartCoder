/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.springboot;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Project;
import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.JavaClass;
import com.saburi.utils.Enums;
import com.saburi.utils.Utilities;

/**
 *
 * @author Hp
 */
public class ControllerTest extends TestClass{


    public ControllerTest(FileModel fileModel){
        super(fileModel);
        
    }

    public String makeImports(Project project, Enums.ServiceTypes serviceTypes) throws Exception {
        String dataTypes = Utilities.getNonPrimitiveDataTypeImports(fields);
        String imp = "import com.fasterxml.jackson.databind.ObjectMapper;\n"
                + "import com.saburi.root.dtos.ResponseObj;\n"
                + "import com.saburi.root.search.SearchCriterion;\n"
                + "import com.saburi.root.search.SearchOperations;\n"
                + "import com.saburi.root.search.SearchOptions;\n"
                + "import "+this.getPackageName() + ".dtos." + objectName + "Request;\n"
                +dataTypes
                + "import java.util.List;\n"
                + "import java.util.Optional;\n"
                + "import org.junit.jupiter.api.BeforeEach;\n"
                + "import org.junit.jupiter.api.Test;\n"
                + "import org.mockito.Mockito;\n"
                + "import static org.mockito.Mockito.when;\n"
                + "import org.springframework.beans.factory.annotation.Autowired;\n"
                + "import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;\n"
                + "import org.springframework.boot.test.context.SpringBootTest;\n"
                + "import org.springframework.boot.test.mock.mockito.MockBean;\n"
                + "import org.springframework.data.domain.Page;\n"
                + "import org.springframework.http.HttpStatus;\n"
                + "import org.springframework.http.MediaType;\n"
                + "import org.springframework.security.test.context.support.WithMockUser;\n"
                + "import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;\n"
                + "import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;\n"
                + "import org.springframework.test.web.servlet.MockMvc;\n"
                + "import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;\n"
                + "import org.springframework.test.web.servlet.result.MockMvcResultHandlers;\n"
                + "import org.springframework.test.web.servlet.result.MockMvcResultMatchers;\n"
                + "import org.springframework.test.web.servlet.setup.MockMvcBuilders;\n"
                + "import org.springframework.web.context.WebApplicationContext;";
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

    private String makeClassFields() {
        return " private MockMvc mockMvc;\n"
                + "\n"
                + "    @MockBean\n"
                + "    private " + objectName + "Service " + objectVariableName + "Service;\n"
                + "\n"
                + "    @Autowired\n"
                + "    private WebApplicationContext context;\n"
                + "\n"
                + "    private final ObjectMapper objectMapper = new ObjectMapper();\n"
                + "private " + objectName + " " + objectVariableName + ";\n";

    }

   

    private String buildField(FieldDAO fieldDAO) {
        String dataType = fieldDAO.getUsableDataType(this.forceReferences(fieldDAO));

        String value = "";
        if (fieldDAO.isNumeric()) {
            value = "1000";
        } else if (fieldDAO.isDate()) {
            value = "LocalDate.now()";
        } else if (fieldDAO.isDateTime()) {
            value = "LocalDateTime.now()";
        } else if (dataType.equalsIgnoreCase("String")) {
            value = "\"" + fieldDAO.getCaption() + "\"";
        } else if (dataType.equalsIgnoreCase("boolean")) {

            value = "false";
        } else {
            value = "";
        }

        return value.equals("") ? "" : "." + fieldDAO.getVariableName() + "(" + value + ")\n";
    }

    public String buildObject() {
        String builderParam = "";
        for (FieldDAO f : this.fields) {
            builderParam += this.buildField(f);
        }
        return objectName + ".builder()\n" + builderParam + ".build()";

    }

    private String methods() {
        String toPlural = Utilities.toPlural(this.objectName);
        String path = toPlural.toLowerCase();
        String role = this.objectName.toLowerCase() + "_all";
      String id =this.idDataType.equalsIgnoreCase("String")?"\"1\"":"1";
        String beforeEach = "@BeforeEach\n"
                + "    public void setup() {\n"
                + "        this.mockMvc = MockMvcBuilders\n"
                + "                .webAppContextSetup(context)\n"
                + "                .apply(springSecurity())\n"
                + "                .alwaysDo(MockMvcResultHandlers.print()).build();\n"
                + " "
                + "this." + this.objectVariableName + "=" + this.buildObject() + ";\n"
                + "}\n";

        String getById = " @Test\n"
                + "    @WithMockUser\n"
                + "    public void shouldReturnOkWhenGivenId() throws Exception {\n"
                + "\n"
                + "        when(" + this.objectVariableName + "Service.get("+id+")).thenReturn(Optional.of(" + this.objectVariableName + "));\n"
                + "        this.mockMvc.perform(MockMvcRequestBuilders.get(\"/" + path + "/1\"))\n"
                + "                .andExpect(MockMvcResultMatchers.status().isOk());\n"
                + "\n"
                + "    }\n";

        String page = "  @Test\n"
                + "    @WithMockUser(roles = \"" + role + "\")\n"
                + "    public void shouldReturnPageOf" + toPlural + "() throws Exception {\n"
                + "\n"
                + "        Page<" + objectName + "> page = Mockito.mock(Page.class);\n"
                + "        when(" + this.objectVariableName + "Service.getAll(0, Optional.of(10))).thenReturn(page);\n"
                + "        this.mockMvc.perform(MockMvcRequestBuilders.get(\"/" + path + "\")\n"
                + "                .queryParam(\"page\", \"0\"))\n"
                + "                .andExpect(MockMvcResultMatchers.status().isOk());\n"
                + "\n"
                + "    }\n";

        String pdsc = "  @Test\n"
                + "    @WithMockUser(roles = \"" + role + "\")\n"
                + "    public void shouldReturnPageDataGivenSearchCriteria() throws Exception {\n"
                + "\n"
                + "        Page<" + objectName + "> page = Mockito.mock(Page.class);\n"
                + "        SearchOptions searchOptions = new SearchOptions(0, 10,\n"
                + "                List.of(\n"
                + "                        SearchCriterion\n"
                + "                                .builder()\n"
                + "                                .key(\"id\")\n"
                + "                                .operation(SearchOperations.EQUAL)\n"
                + "                                .value(\"1\")\n"
                + "                                .build())\n"
                + "        );\n"
                + "\n"
                + "\n"
                + "        when(" + objectVariableName + "Service.getAll(searchOptions))\n"
                + "                .thenReturn(page);\n"
                + "        this.mockMvc.perform(MockMvcRequestBuilders\n"
                + "                .post(\"/" + path + "/search\")\n"
                + "                .with(csrf())\n"
                + "                .contentType(MediaType.APPLICATION_JSON)\n"
                + "                .content(objectMapper.writeValueAsString(searchOptions)))\n"
                + "                .andExpect(MockMvcResultMatchers.status().isOk());\n"
                + "\n"
                + "    }\n";

        String save = "@Test\n"
                + "    @WithMockUser(roles = \"" + role + "\")\n"
                + "    public void shouldSave" + objectName + "() throws Exception {\n"
                + "\n"
                + "        " + objectName + "Request " + objectVariableName + "Request = Mockito.mock(" + objectName + "Request.class);\n"
                + "        ResponseObj<" + objectName + "> res = new ResponseObj<>(true, \"Saved Successfuly\",\n"
                + "                " + objectVariableName + ", HttpStatus.OK);\n"
                + "\n"
                + "        when(" + objectVariableName + "Service.save(" + objectVariableName + "Request))\n"
                + "                .thenReturn(res);\n"
                + "        this.mockMvc.perform(MockMvcRequestBuilders\n"
                + "                .post(\"/" + path + "\")\n"
                + "                .with(csrf())\n"
                + "                .contentType(MediaType.APPLICATION_JSON)\n"
                + "                .content(objectMapper.writeValueAsString(" + objectVariableName + "))\n"
                + "        )\n"
                + "                .andExpect(MockMvcResultMatchers.status().isOk());\n"
                + "\n"
                + "    }\n";

        String delete = " @Test\n"
                + "    @WithMockUser\n"
                + "    public void shouldDeleteAndReturnOk() throws Exception {\n"
                + "        ResponseObj<" + objectName + "> res = new ResponseObj<>(true, \"Operation Successful\", HttpStatus.OK);\n"
                + "\n"
                + "        when(" + objectVariableName + "Service.delete("+id+")).thenReturn(res);\n"
                + "        this.mockMvc.perform(MockMvcRequestBuilders.delete(\"/" + path + "/1\"));\n"
                + "\n"
                + "    }\n";

        String update = "@Test\n"
                + "    @WithMockUser\n"
                + "    public void shouldUpdateAndReturnOk() throws Exception {\n"
                + "        ResponseObj<" + objectName + "> res = new ResponseObj<>(true, \"Operation Successful\", HttpStatus.OK);\n"
                + "        " + objectName + "Request " + objectVariableName + "Request = Mockito.mock(" + objectName + "Request.class);\n"
                + "\n"
                + "        when(" + objectVariableName + "Service.update(" + objectVariableName + "Request, "+id+")).thenReturn(res);\n"
                + "        this.mockMvc.perform(MockMvcRequestBuilders.put(\"/" + role + "/1\"));\n"
                + "\n"
                + "    }\n";

        return beforeEach + getById + page + pdsc + save + delete + update;

    }
    
    private String className(){return objectName + "" + Enums.WebFiles.ControllerTest.name();}

    public String makeClass(Project project, Enums.ServiceTypes serviceTypes) throws Exception {



        String methods = methods();

        String annotations = "@SpringBootTest\n"
                + "@AutoConfigureMockMvc";

        String packageName = this.getPackageName();
        JavaClass javaClass = new JavaClass(packageName, className(), this.makeImports(project, serviceTypes), this.makeClassFields() + methods);
        return javaClass.makeClass("", annotations);
    }

    private String getPackageName() {
        return project.getBasePackage() + "." + objectName.toLowerCase();
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
