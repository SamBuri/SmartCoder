/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder.springboot;

import com.saburi.smartcoder.FileModel;

/**
 *
 * @author samburiima
 */
public abstract class  TestClass extends SpringbootUtils{

    public TestClass(FileModel fileModel) {
        super(fileModel);
    }

    @Override
    protected String getBaseFolder() {
        return project.getTestFolder();
    }

   
    
}
