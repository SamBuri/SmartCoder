/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.java;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Project;
import com.saburi.utils.Enums;
import static com.saburi.utils.Utilities.addIfNotExists;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author samburiima
 */
public class JavaFile {
    
     public static String getDataTypeImps(FieldDAO fieldDAO) {
        String dataType = fieldDAO.getDataType();
        if (dataType.equalsIgnoreCase("LocalDate")) {
            return "import java.time.LocalDate;\n";
        }
        if (dataType.equalsIgnoreCase("LocalDateTime")) {
            return "import java.time.LocalDateTime;\n";
        }
        if (dataType.equalsIgnoreCase("Date")) {
            return "import java.util.Date;\n";
        }
        if (dataType.equalsIgnoreCase("DateTime;\n")) {
            return "import java.util.Date;\n";
        }
       
        if (dataType.equalsIgnoreCase("BigDecimal;\n")) {
            return "import java.math.BigDecimal;\n";
        }
        return "";
    }

    public static String getNonPrimitiveDataTypeImports(List<FieldDAO> fieldDAOs) {
        String v = "";
        v = fieldDAOs
                .stream()
                .map(f -> getDataTypeImps(f))
                .distinct()
                .reduce(v, String::concat);
        return v;
    }
    
    
//     public List entityImports(FieldDAO fieldDAO, Project currentProject, boolean considerReferences) throws Exception {
//        List list = new ArrayList();
//        String projectName = fieldDAO.getProjectName();
//        if (projectName.isBlank()) {
//            project = currentProject;
//        } else {
//            project = oProjectDAO.get(getProjectName());
//        }
//
//        this.commonProject = oProjectDAO.get(project.getCommonProjectName());
//        if (isPrimaryKey()) {
//            addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
//            if (isReference()) {
//                if (getEnumerated()) {
//                } else {
//                    addIfNotExists(list, "import jakarta.persistence.MapsId");
//                }
//            }
//        }
//
//        if (isIDGenerator()) {
//            addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
//        }
//        if (isPrimaryKeyAuto()) {
//            addIfNotExists(list, "import jakarta.persistence.GeneratedValue");
//            addIfNotExists(list, "import jakarta.persistence.GenerationType");
//        } else if (isReference()) {
//            if (!nullable.get()) {
//                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
//            }
//
//            if (isCollection()) {
//                addIfNotExists(list, "import java.util." + getDataType());
//                addIfNotExists(list, "import jakarta.persistence.CascadeType");
//                if (project.getProjectType().equals(Enums.ProjectTypes.Springboot_API)) {
//                    addIfNotExists(list, "import com.fasterxml.jackson.annotation.JsonIgnoreProperties");
//                }
//                if (getDataType().equalsIgnoreCase("List")) {
//                    addIfNotExists(list, "import java.util.ArrayList");
//                } else if (getDataType().equalsIgnoreCase("Set")) {
//                    addIfNotExists(list, "import java.util.HashSet");
//                }
//                if (mapping.get().isBlank()) {
//                    mapping.set("OneToMany");
//                }
//                addIfNotExists(list, "import jakarta.persistence." + mapping.get());
//
//            }
//
//            if (enumerated.get()) {
//                if (project.getProjectType().equals(Enums.ProjectTypes.Desktop)) {
//                    addIfNotExists(list, "import " + project.getUtilPackage() + "." + project.getEnumClass() + "." + references.get());
//                }
//                if (project.getProjectType().equals(Enums.ProjectTypes.Springboot_API)) {
//
//                    String enumPackage = (referencesIN(project)) ? project.getBasePackage() : commonProject.getBasePackage();
//                    addIfNotExists(list, "import " + enumPackage + ".enums." + references.get());
//
//                }
//                addIfNotExists(list, "import jakarta.persistence.Enumerated");
//                addIfNotExists(list, "import jakarta.persistence.EnumType");
//
//            } else {
//                if (considerReferences) {
//                    if (mapping.get().isBlank() && !isCollection()) {
//                        mapping.set("OneToOne");
//                    }
//
//                    if (key.get().isBlank()) {
//                        key.set("Foreign");
//
//                    }
//                    addIfNotExists(list, "import jakarta.persistence.JoinColumn");
//                    addIfNotExists(list, "import jakarta.persistence.ForeignKey");
//                    addIfNotExists(list, "import jakarta.persistence." + mapping.get());
//
//                    if (project.getProjectType().equals(Enums.ProjectTypes.Desktop)) {
//                        if (!project.getProjectName().equalsIgnoreCase(currentProject.getProjectName())) {
//                            addIfNotExists(list, "import " + project.getEntityPackage() + "." + references.get());
//                        }
//                    }
//                    addIfNotExists(list, "import " + project.getBasePackage() + "." + getReferences().toLowerCase().concat(".").concat(getReferences()));
//
//                }
//            }
//        } else if (dataType.get().equalsIgnoreCase("String")) {
//            addIfNotExists(list, "import jakarta.validation.constraints.Size");
//            if (key.get().equalsIgnoreCase("Unique")) {
//
//            }
//
//            if (!nullable.get()) {
//                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
//            }
//
//        } else {
//            if (!nullable.get()) {
//                addIfNotExists(list, "import jakarta.validation.constraints.NotNull");
//            }
//            if (dataType.get().equalsIgnoreCase("LocalDate")) {
//                addIfNotExists(list, "import java.time.LocalDate");
//            } else if (dataType.get().equalsIgnoreCase("LocalDateTime")) {
//                addIfNotExists(list, "import java.time.LocalDateTime");
//            } 
//            else if (dataType.get().equalsIgnoreCase("BigDecimal")) {
//                addIfNotExists(list, "import java.math.BigDecimal");
//            }
//            else if (dataType.get().equalsIgnoreCase("Image")) {
//                addIfNotExists(list, "import jakarta.persistence.Lob");
//
//            }
//
//        }
//        return list;
//    }

    
}
