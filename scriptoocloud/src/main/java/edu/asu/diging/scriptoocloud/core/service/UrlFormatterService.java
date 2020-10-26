package edu.asu.diging.scriptoocloud.core.service;

import java.net.MalformedURLException;

public interface UrlFormatterService {

    String urlToFolderName(String url) throws MalformedURLException;

}