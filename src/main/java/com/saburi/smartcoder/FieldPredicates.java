/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder;

import com.saburi.dataacess.FieldDAO;
import com.saburi.model.Field;
import java.util.function.Predicate;

/**
 *
 * @author CLINICMASTER13
 */
public class FieldPredicates {
  
    public static Predicate<FieldDAO> isHelper(){
     return (p) -> p.isHelper();

    }
    
    public static Predicate<FieldDAO> isPrimaryKey(){
     return (p) -> p.isPrimaryKey();

    }
    
    public static Predicate<FieldDAO> isIDGenerator(){
     return (p) -> p.isIDGenerator();

    }
    
    public static Predicate<FieldDAO> isDIsplayKey(){
     return (p) -> p.isDisplayKey();

    }
    
    public static Predicate<FieldDAO> hasSubFields(){
     return (p) -> p.makeEditableTable();

    }
    
    
}
