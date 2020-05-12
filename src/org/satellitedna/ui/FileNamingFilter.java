package org.satellitedna.ui;

import java.io.File;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.filechooser.FileFilter;

public final class FileNamingFilter
  extends FileFilter
{


  private HashMap filters = null;
  private String description = null;
  private String fullDescription = null;
  private boolean useExtensionsInDescription = true;

  public FileNamingFilter()
  {
    this.filters = new HashMap();
  }


  public FileNamingFilter(String extension)
  {

    this(extension, null);
    this.filters = new HashMap();
  }


  public FileNamingFilter(String extension, String description)
  {
    this();
    this.filters = new HashMap();
    if (extension != null)
      addExtension(extension);
    if (description != null)
      setDescription(description);
  }

  public FileNamingFilter(String[] filters)
  {
    this(filters, null);
    this.filters = new HashMap();
  }

  public FileNamingFilter(String[] filters, String description)
  {
    this();
      for (String filter : filters) {
          // add filters one by one
          addExtension(filter);
      }
    if (description != null)
      setDescription(description);
  }

  @Override
  public boolean accept(File f)
  {
    if (f != null)
    {
      if (f.isDirectory())
      {
        return true;
      }
      String extension = getExtension(f);
      if (extension != null && filters.get(getExtension(f)) != null)
      {
        return true;
      }
      
    }
    return false;
  }

  public String getExtension(File f)
  {
    if (f != null)
    {
      String filename = f.getName();
      int i = filename.lastIndexOf('.');
      if (i > 0 && i < filename.length() - 1)
      {
        return filename.substring(i + 1).toLowerCase();
      }
     
    }
    return null;
  }
  @SuppressWarnings("unchecked")
  public void addExtension(String extension)
  {
    if (filters == null)
    {
      filters = new HashMap(5);
    }
    filters.put(extension.toLowerCase(), this);
    fullDescription = null;
  }

  @Override
  public String getDescription()
  {
    if (fullDescription == null)
    {
      if (description == null || isExtensionListInDescription())
      {
        fullDescription = description == null? "(": description + " (";
        // build the description from the extension list
        Iterator extensions = filters.keySet().iterator();
        if (extensions != null)
        {
          fullDescription += "." + (String) extensions.next();
          while (extensions.hasNext())
          {
            fullDescription += ", ." + (String) extensions.next();
          }

        }

        fullDescription += ")";

      }
      else
      {

        fullDescription = description;
      
      }

    }

    return fullDescription;

  }

  public void setDescription(String description)
  {
    this.description = description;
    fullDescription = null;
  }

  public void setExtensionListInDescription(boolean b)
  {
    useExtensionsInDescription = b;
    fullDescription = null;
  }

  public boolean isExtensionListInDescription()
  {
    return useExtensionsInDescription;
  }

}
