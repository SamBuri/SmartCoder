/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.springboot;

import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.ProjectFile;
import com.saburi.utils.Enums.WebFiles;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author samburiima
 */
public class SpringBoot {

    public Map<String, ProjectFile> register(FileModel fileModel) {
        Map<String, ProjectFile> map = new HashMap<>();
        map.put(WebFiles.Entity.name(), new Entity(fileModel));
        map.put(WebFiles.Request.name(), new Request(fileModel));
        map.put(WebFiles.Mini.name(), new Mini(fileModel));
        map.put(WebFiles.Repo.name(), new Repository(fileModel));
        map.put(WebFiles.Service.name(), new Service(fileModel));
        map.put(WebFiles.Controller.name(), new WebController(fileModel));
        map.put(WebFiles.ControllerTest.name(), new ControllerTest(fileModel));
        map.put(WebFiles.ServiceTest.name(), new ServiceTest(fileModel));
        map.put(WebFiles.Change_Log.name(), new ChangeLog(fileModel));
        return map;
    }

}
