package edu.asu.diging.scriptoocloud.core.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.UrlFormatterService;

@Service
public class UrlFormatterServiceImpl implements UrlFormatterService {

    @Override
    public String urlToFolderName(String url) throws MalformedURLException{ 
        URL urlWrapper = new URL(url);
        String folderName  = urlWrapper.getHost();
        Scanner scanner = new Scanner(urlWrapper.getPath()).useDelimiter("/");
        while(scanner.hasNext()){
            folderName += "_" + scanner.next();
        }
        scanner.close();
        return folderName;
    }

}
