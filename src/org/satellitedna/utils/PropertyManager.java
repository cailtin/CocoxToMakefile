/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author clopez
 */
public class PropertyManager extends Properties {
    
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(PropertyManager.class.getName());

   
    
    public boolean saveToClassLoader()
    {
     // TODO change this
        boolean bRet = false;
        
        String userFolder = System.getProperty("user.home") +"/cocoxToMakefile.properties";
       
        
        
        try(FileOutputStream out = new FileOutputStream(new File(userFolder)))
        {
             store( out,"");
            bRet = true;
            
        }
        catch(Exception e)
        {
         LOG.log(Level.SEVERE, e.toString());
        }
        return bRet;
        
        
    }
    
    
    public boolean loadFromClassLoader() 
    {
        
        boolean bRet = false;
          String userFolder = System.getProperty("user.home") +"/cocoxToMakefile.properties";
      
        try ( FileInputStream in = new FileInputStream(new File(userFolder)) )
        {
            if ( in != null)
            {
            
             load(in);
             bRet = true;
            }
        }
        catch(java.io.IOException e)
        {
           LOG.log(Level.SEVERE, e.toString());
        }
      
        return bRet;
        
    }
    
    
    
    
    
    
    @Override
    public void store(OutputStream out, String comments) throws IOException {
        super.store(out, comments); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public synchronized Object setProperty(String key, String value) {
        return super.setProperty(key, value); //To change body of generated methods, choose Tools | Templates.
    }
    
}
