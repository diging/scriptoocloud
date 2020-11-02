package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;

@Service
public class DeleteFilesServiceImpl implements DeleteFilesService{

    @Override
    public void deleteDirectoryContents(File file){
        if(file.isDirectory()){
            File directorycontents[] = file.listFiles();
            for(File fileItem : directorycontents){
                deleteDirectoryContents(fileItem);     
            }
            file.delete();
        }
        else{
            file.delete();
        }
    }    
 
}
