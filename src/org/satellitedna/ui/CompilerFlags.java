/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.ui;

/**
 *
 * @author clopez
 */
public enum CompilerFlags {


    CC("CC"),
    CCX("CCX"),
    CFLAGS("CFLAGS"),
    INCPATH("INCPATH"),
    LFLAGS("LFLAGS"),
    LIBS("LIBS"),
    COMPILER_BASE("COMPILER_BASE"),
    PROJECT_LOCATION("PROJECT_LOCATION"),
    COOCOX_BASE_LOCATION("COOCOX_BASE_LOCATION"),
    COOCOX_BASE_FILTERED("COOCOX_BASE_LOCATION_FILTERED"),
    COOCOX_RESOURCES("COOX_RESOURCES");

    String item;

    private CompilerFlags(String s) {
        item = s;
    }

}
