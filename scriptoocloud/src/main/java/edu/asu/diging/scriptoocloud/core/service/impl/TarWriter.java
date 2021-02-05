package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarWriter {

    private FileOutputStream out;
    private TarArchiveOutputStream tOut;    
    
    public TarWriter() throws IOException{        
        out = new FileOutputStream("C:/mytar.tar");        
        tOut = new TarArchiveOutputStream(out);
    }

    public void writeEntry(File file) throws IOException{
        Path sourcePath = FileSystems.getDefault().getPath(file.getPath());
        byte[] sourceBytes = Files.readAllBytes(sourcePath);
       
        TarArchiveEntry tarEntry = new TarArchiveEntry(file);
        tarEntry.setSize(sourceBytes.length);
       
        tOut.putArchiveEntry(tarEntry);
        tOut.write(sourceBytes);
        tOut.closeArchiveEntry();
    }
    
    public void writeDir(File rootFile) throws IOException{    
        File[] fileTree = rootFile.listFiles();
        
        for( File file : fileTree ){
            System.out.println(file.getName());
            if(file.isDirectory()){
                writeDir(file);
                continue;
            }
            writeEntry(file);
        }       
    }
    
}
