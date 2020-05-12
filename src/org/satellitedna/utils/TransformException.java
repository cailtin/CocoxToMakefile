/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.utils;

/**
 *
 * @author clopez
 */
public class TransformException extends Exception{

    private static final long serialVersionUID = 1L;

    public TransformException(String message) {
        super(message);
    }

    public TransformException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
