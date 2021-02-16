package edu.asu.diging.scriptoocloud.core.service;

import java.net.MalformedURLException;

/**
 * An interface which provides method(s) to convert a remote Git URL to a folder name.
 */
public interface UrlFormatterUtility {

    /**
     * Converts remote git url's to folder names
     *
     * @param url of format www.web.com/path/etc
     * @return url of format www_web_com_path_etc
     */
    String urlToFolderName(String url) throws MalformedURLException;

}