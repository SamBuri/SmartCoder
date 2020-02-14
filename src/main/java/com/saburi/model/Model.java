/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.model;

/**
 *
 * @author Hp
 */
public  abstract class Model {
    protected transient Object id;
    protected transient String display;

    public Object getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }
    
    
  
}
