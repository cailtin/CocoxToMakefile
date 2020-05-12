package org.satellitedna.utils;

import java.util.StringTokenizer;



import org.jdom2.Attribute;
import org.jdom2.Element;


public final class XMLUtil
{
  public XMLUtil()
  {
  }


  public String getAttribute(Element element, String attribute)
  {
    String ret = null;
    Attribute att = element.getAttribute(attribute);
    if (att != null)
    {
      ret = att.getValue();
    }
    return ret;


  }

  public Element findElement(Element root, String path)
  {
    StringTokenizer tokenizer = new StringTokenizer(path, "/");
    if (tokenizer.hasMoreTokens())
    {
      Element elem = root.getChild(tokenizer.nextToken());
      if (elem == null)
      {
        return null;
      }
      while (tokenizer.hasMoreTokens())
      {
        if (elem != null)
        {
          elem = elem.getChild(tokenizer.nextToken());
        }
      }
      return elem;

    }

    return null;

  }

  public   boolean findElementWithAttribute(Element root, String elementName, 
                                          String atributeName, 
                                          String value)
  {
    boolean bRet = false;
      for (Element child : root.getChildren()) {
          if (child.getName().equalsIgnoreCase(elementName))
          {
              Attribute attribute = child.getAttribute(atributeName);
              if (attribute != null)
              {
                  if (attribute.getValue().equalsIgnoreCase(value))
                  {
                      bRet = true;
                      break;
                  }
                  
              }
              
          }
      }

    return bRet;
  }


}
