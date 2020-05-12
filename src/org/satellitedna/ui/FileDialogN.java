/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.ui;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author clopez
 */
public class FileDialogN {

    public String getFile(FileNamingFilter filter, String location, boolean dirsOnly, boolean save) {
        JFileChooser fc = new JFileChooser();

        if (dirsOnly == true) {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        if (filter != null) {
            fc.setFileFilter(filter);
        }
        if (location != null) {
            fc.setCurrentDirectory(new File(location));
        }
        int returnVal = 0;
        if (save) {
            returnVal = fc.showSaveDialog(null);
        } else {

            //In response to a button click:  
            returnVal = fc.showOpenDialog(null);
        }
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file.getAbsolutePath();

        }
        return null;
    }

}
