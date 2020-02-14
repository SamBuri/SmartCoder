/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.utils;

import com.saburi.dataacess.ProjectDAO;
import com.saburi.smartcoder.App;
import com.saburi.utils.Enums.SearchItemTypes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.TreeItem;

/**
 *
 * @author CLINICMASTER13
 */
public class SearchTree {

    private final TreeItem<SearchItem> triProject = new TreeItem<>(new SearchItem("Project"));
    private final Class mainClass = App.class;
    protected List<TreeItem> treeItems = new ArrayList<>();
    protected List<SearchItem> searchObjects = new ArrayList<>();
    protected final TreeItem<SearchItem> triGeneral = new TreeItem<>(new SearchItem("General"));

    private final List<SearchItem> searchItems = Arrays.asList(
            new SearchItem(mainClass, new ProjectDAO(), "Project", "Project", true, triProject));

    protected void addTreeItem(SearchItem searchItem) {
        String objectName = searchItem.getUiName();
        if (objectName != null) {
            
        }
        TreeItem parent = searchItem.getParent();
        if (parent == null) {
            parent = triGeneral;
           
        }
        TreeItem<SearchItem> treeItem = new TreeItem(searchItem);
        treeItem.setExpanded(true);
        parent.getChildren().add(treeItem);

    }

    public List<SearchItem> getSearchItems() {
        return searchItems;
    }

    public List<TreeItem> getTreeItems() {
         triProject.setExpanded(true);
        searchItems.forEach(e -> addTreeItem(e));
        this.treeItems = Arrays.asList(triProject);
        return treeItems;
    }

}
