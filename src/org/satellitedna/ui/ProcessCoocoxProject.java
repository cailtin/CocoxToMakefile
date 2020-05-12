/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.ui;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Element;
import org.satellitedna.utils.XPathQuery;

/**
 *
 * @author clopez
 */
public class ProcessCoocoxProject implements Runnable {

    private static final Logger LOG = Logger.getLogger(ProcessCoocoxProject.class.getName());
    private String fileName;

    private String coocoxLocation;

    public String getCoocoxLocation() {
        return coocoxLocation;
    }

    public void setCoocoxProjectLocation(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {

        while (true) {

            try {

                XPathQuery query = new XPathQuery();
                query.open(fileName);

                Element r = (Element) query.getElement("//*[text()='projectRoot']");

                if (r != null) {

                    List<Element> elements = r.getParent().elements();

                    boolean foundProject = false;

                    Iterator<Element> its = elements.iterator();

                    while (its.hasNext()) {
                        Element e = its.next();

                      

                        if (e.getName().equalsIgnoreCase("key")) {
                            if (e.getText().equalsIgnoreCase("projectRoot")) {
                                foundProject = true;
                                continue;
                            }
                        }
                        if (foundProject) {
                            coocoxLocation = e.getText();
                            break;
                        }

                    }
                    break;
                }

            } catch (Exception e) {

                LOG.log(Level.SEVERE, e.toString());

            }
        }

    }

    public static void main(String args[]) {
        try {
            ProcessCoocoxProject e = new ProcessCoocoxProject();
            e.setCoocoxProjectLocation("/run/media/clopez/1264185364183C43/projects/arm_projects/updated_lpc1768/lpc_1768_controller/lpc_1768_controller.elf.xcodeproj/project.pbxproj");
            e.run();

        } catch (Exception e) {
         LOG.log(Level.SEVERE, e.toString());
        }

    }

}
