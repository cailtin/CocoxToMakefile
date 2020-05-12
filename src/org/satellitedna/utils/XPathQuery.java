package org.satellitedna.utils;

import java.io.File;
import java.io.FileWriter;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


public class XPathQuery
{

  private Document document = null;

  public XPathQuery()
  {
  }


  public void write(String outFileName)
    throws Exception
  {
    try
    {
      // lets write to a file
      if ( document == null)
      {
         return;
      }
      
      XMLWriter writer = new XMLWriter(new FileWriter(outFileName));
      writer.write(document);
      writer.flush();
      writer.close();
      /*
      document = null;
      File file = new File(outFileName);
      file.delete();
      
      file = new File(outFileName+".bak");
      file.renameTo(new File(outFileName));
      file.delete();
      */
/*

      // Pretty print the document to System.out
      OutputFormat format = OutputFormat.createPrettyPrint();
      writer = new XMLWriter(System.out, format);
      writer.write(document);

      // Compact format to System.out
      format = OutputFormat.createCompactFormat();
      writer = new XMLWriter(System.out, format);
      writer.write(document);
      */
    }
    catch (java.io.IOException e)
    {
      throw new Exception(e);
    }
  }

  public Document open(String fileName)
    throws Exception
  {
    if (document == null)
    {
      document = parse(fileName);
    }
    return document;
  }

  public void close()
  {
    document = null;
  }

  private Document parse(String fileName)
    throws Exception
  {
    try
    {
      SAXReader reader = new SAXReader();
      return reader.read(new File(fileName));
    }
    catch (DocumentException e)
    {
      throw new Exception(e);
    }

  }

  public Node getRoot()
    throws Exception
  {
    if (document == null)
    {
      throw new Exception("unable to complete request, document was not open");
    }

    return document.getRootElement();


  }

  public Node getElement(String xpath)
    throws Exception
  {
    if (document == null)
    {
      throw new Exception("unable to complete request, document was not open");
    }
    return document.selectSingleNode(xpath);

  }

  public List getElements(String xpath)
    throws Exception
  {
    if (document == null)
    {
      throw new Exception("unable to complete request, document was not open");
    }
    return document.selectNodes(xpath);


  }

   public List getElements(String xpath,Element children)
    throws Exception
  {
   
    if (document == null)
    {
      throw new Exception("unable to complete request, document was not open");
    }
    return  children.selectNodes(xpath);
      
   


  }
 
  public Element getNextElement(String name,Element element)
  {
      
       List children = element.elements();
      for (int count =0; count < children.size(); count++)
      {
          Element child  = (Element)children.get(count);
          String text = child.getText();
          
          
          if ( child.getText().equalsIgnoreCase(name))
          {
              return (Element)children.get(count+1);
          }
      }
      
      return null;  
  }
   
   
  public Element getElement(String name,Element element)
  {
      List children = element.elements();
      for (int count =0; count < children.size(); count++)
      {
          Element child  = (Element)children.get(count);
          if ( child.getText().equalsIgnoreCase(name))
          {
              return child;
          }
      }
      
      return null;
  }
  
   public Element getElementWithName(String name,Element element)
  {
      List children = element.elements();
      for (int count =0; count < children.size(); count++)
      {
          Element child  = (Element)children.get(count);
          if ( child.getName().equalsIgnoreCase(name))
          {
              return child;
          }
      }
      
      return null;
  }
   
   

 
}






