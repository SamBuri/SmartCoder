/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.controller;

import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.ProjectFile;
import com.saburi.smartcoder.springboot.SpringBoot;
import com.saburi.smartcoder.vue3.Vue3;
import com.saburi.utils.Enums.ProjectTypes;
import java.util.Map;

/**
 *
 * @author samburiima
 */
public class ProjectTypeRegistry {

    public static Map<String, ProjectFile> register(FileModel fileModel) {
        if (fileModel.getProjectType().equals(ProjectTypes.Vue3)) {
           return  new Vue3().register(fileModel);
        }
        if (fileModel.getProjectType().equals(ProjectTypes.Springboot_API)) {
           return new SpringBoot().register(fileModel);
        }

       return null;

    }

}
