package edu.asu.diging.scriptoocloud.core.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.UrlFormatterUtility;

/**
 * Service class which performs actions related to converting URLs to strings that can be safely
 * read and wrote by system.
 *
 * @author Jason Ormsby
 */

@Service
public class UrlFormatterUtilityImpl implements UrlFormatterUtility {


    @Override
    public String urlToFolderName(String url) throws MalformedURLException {
        URL urlWrapper = new URL(url);

        Scanner scanner = new Scanner(urlWrapper.getHost()).useDelimiter("\\.");
        String folderName = scanner.next();
        while (scanner.hasNext()) {
            folderName += "_" + scanner.next();
        }

        scanner = new Scanner(urlWrapper.getPath()).useDelimiter("/");
        while (scanner.hasNext()) {
            folderName += "_" + scanner.next();
        }

        scanner.close();

        return folderName;
    }
}
