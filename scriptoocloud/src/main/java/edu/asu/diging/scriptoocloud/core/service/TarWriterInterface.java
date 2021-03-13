package edu.asu.diging.scriptoocloud.core.service;

import java.io.File;
import java.io.IOException;

public interface TarWriterInterface {

    void writeDir(File rootFile) throws IOException;

}