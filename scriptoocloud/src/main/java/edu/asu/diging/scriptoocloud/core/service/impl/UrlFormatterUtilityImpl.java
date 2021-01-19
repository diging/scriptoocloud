package edu.asu.diging.scriptoocloud.core.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.UrlFormatterUtility;

/*
 * Performs actions related to converting url's to strings that can be safely read and wrote by system 
 * 
 * @author Jason Ormsby
 */

@Service
public class UrlFormatterUtilityImpl implements UrlFormatterUtility {

    /*
     * Converts remote git url's to folder names 
     * 
     * @param  url of format www.web.com/path/etc
     * @return url of format www_web_com_path_etc
     */
    @Override
    public String urlToFolderName(String url) throws MalformedURLException { 
        URL urlWrapper = new URL(url);
        
        Scanner scanner = new Scanner(urlWrapper.getHost()).useDelimiter("\\.");
        String folderName = scanner.next();
        while(scanner.hasNext()) {
            folderName += "_" + scanner.next();
        }

        scanner = new Scanner(urlWrapper.getPath()).useDelimiter("/");
        while(scanner.hasNext()) {
            folderName +=  "_" + scanner.next();
        }
        
        scanner.close();
        
        return folderName;
    }
}
