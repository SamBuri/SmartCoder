/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.saburi.smartcoder;

import static com.saburi.utils.FXUIUtils.message;
import static com.saburi.utils.FXUIUtils.warningOK;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.writeFile;
import java.awt.Desktop;
import java.io.File;

/**
 *
 * @author samburiima
 */
public abstract class ProjectFile {

    protected FileModel fileModel;
    protected final String FILE_SEPARATOR="//";

    public ProjectFile(FileModel fileModel) {
        this.fileModel = fileModel;
    }
    
    
    
    protected abstract String getBaseFolder();

    protected abstract String getFolderName();

    protected abstract String getFileName();

    protected abstract String getFileExtension();

    protected String getFullFileName() {
        String baseFolderName = fileModel.isSaveToProject()?getBaseFolder():fileModel.getOutputFolder();
        String folder= String.format("%s%s%s", baseFolderName, FILE_SEPARATOR,getFolderName());
        Utilities.makeDirectory(folder);
        return String.format("%s%s%s.%s", folder, FILE_SEPARATOR, getFileName(), getFileExtension());
    }

    protected boolean isValid() throws Exception {
        return true;
    }

    protected abstract String create() throws Exception;

    public void generate() throws Exception {
        if (!this.isValid()) {
            return;
        }
        String fileName = getFullFileName();
        File file = new File(fileName);
        if (file.exists()) {
            if (!warningOK("File Exists", "The file with name " + file.getName() + " already exists.\n"
                    + "Do you want to replace it?")) {
                message("Operation Cancelled!");
                return;
            } else {

                if (!warningOK("Confirm Replace", """
                                                             Replacing this file may lead to potential loss of the code
                                                             Are you sure you want to continue?""")) {
                    message("Operation Cancelled!");
                    return;
                }

            }
        }

        writeFile(fileName, create());
        System.out.println("File Created with name: "+fileName);
        if (fileModel.isOpenFile()) {
            Desktop.getDesktop().open(file);

        }

       
    }

}
