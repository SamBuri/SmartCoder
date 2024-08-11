/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.vue3;

import com.saburi.smartcoder.FileModel;
import com.saburi.smartcoder.ProjectFile;
import com.saburi.utils.Enums.Vue3Files;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author samburiima
 */
public class Vue3 {

    public Map<String, ProjectFile> register(FileModel fileModel) {
        Map<String, ProjectFile> map = new HashMap<>();
        map.put(Vue3Files.Model.name(), new VueModel(fileModel));
        map.put(Vue3Files.View.name(), new Vue(fileModel));
        map.put(Vue3Files.Store.name(), new Store(fileModel));
        map.put(Vue3Files.Nav.name(), new VueNav(fileModel));
        map.put(Vue3Files.Search.name(), new Search(fileModel));
        map.put(Vue3Files.Controller.name(), new Controller(fileModel));
        return map;
    }

}
