/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author clopez
 */
public class FileCopier implements Runnable {

    private static final Logger LOG = Logger.getLogger(FileCopier.class.getName());
    private final Object mutex = new Object();
    private boolean canRun = true;
    private ArrayList<String> original;
    private ArrayList<String> newDestination;

    private final String projectFile;
    private final String destination;
    public volatile boolean finished = false;

    public FileCopier(String project, String destination) {
        this.projectFile = project;
        this.destination = destination;

    }

    public void process(ArrayList<String> original, ArrayList<String> newDestination) throws Exception {

        this.original = original;
        this.newDestination = newDestination;
        if (this.original.size() != newDestination.size()) {
            throw new Exception("source or destination do not have same number of file");
        }

        synchronized (mutex) {
            mutex.notifyAll();
        }

    }

    public void process(String original, String target) throws java.io.IOException {

        File srcDir = new File(original);
        File destDir = new File(target);
        FileUtils.copyDirectory(srcDir, destDir);

    }

    public void stopProcess() {
        this.canRun = false;
        synchronized (mutex) {
            mutex.notifyAll();
        }
    }

    private void copyFile(String source, String destination, String projectFile) throws IOException {

        ZipUtil.copyFile(source, destination, projectFile);

    }

    @Override
    public void run() {

        while (canRun) {
            try {
                synchronized (mutex) {
                    mutex.wait();
                }
            } catch (InterruptedException e) {

            }
            if (!canRun) {
                break;
            }

            int size = original.size();
            for (int count = 0; count < size; count++) {
                String originalFileName = original.get(count);
                String newLocation = newDestination.get(count);
                try {

                    originalFileName = ZipUtil.formatCopyFilePath(projectFile, originalFileName);

                    copyFile(originalFileName, newLocation, destination);
                } catch (java.io.IOException e) {
                    LOG.log(Level.SEVERE, e.toString());
                }

            }
            finished = true;

        }

    }

}
