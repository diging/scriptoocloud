package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;

/*
 * Deletes files and directories from the system using standard java libraries
 * 
 * @author Jason Ormsby
 */

@Service
public class DeleteFilesServiceImpl implements DeleteFilesService {


    /*
     * Recursively deletes a folder and it's contents, or just deletes a file/directory
     * 
     * @param   file    Directory or file to be deleted 
     */
    @Override
    public void deleteDirectoryContents(File file) {
        if(file.isDirectory()) {
            File directorycontents[] = file.listFiles();
            for (File fileItem : directorycontents) {
                deleteDirectoryContents(fileItem);     
            }
            file.delete();
        } else {
            file.delete();
        }
    }    
 
}
