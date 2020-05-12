package org.satellitedna.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

/**
 * class is not currently used
 */
public final class XML2HTML
{
  private Document doc;
  private final OutputFormat outFormat;


  public XML2HTML()
  {
    this.outFormat = OutputFormat.createPrettyPrint();
    this.outFormat.setXHTML(true);

    this.outFormat.setNewlines(true);
    this.outFormat.setNewLineAfterNTags(1);
    this.outFormat.setExpandEmptyElements(true);


  }

  public XML2HTML(OutputFormat outFormat)
  {
    this.outFormat = outFormat;
  }

  @SuppressWarnings("unchecked")
  public String convertToHtml(String html)
    throws Exception
  {
    org.dom4j.io.HTMLWriter writer = null;
    try
    {
      StringWriter sw = new StringWriter();

      writer = new org.dom4j.io.HTMLWriter(sw, this.outFormat);
      Set current = writer.getPreformattedTags();
      current.add("IFRAME");
      current.add("BR");

      writer.setPreformattedTags(current);


      writer.write(html);


      writer.flush();


      return sw.toString();
    }
    catch (IOException e)
    {
      throw new Exception(e);
    }
    finally
    {
      if (writer != null)
      {
        try
        {
          writer.close();
        }
        catch (IOException e)
        {
             throw new Exception(e);
        }
      }

    }
  }

//  public void convertToHtml(String inFile, String outFile)
//    throws Exception
//  {
//    BufferedOutputStream out = null;
//    try
//    {
//      String str = this.openFile(inFile);
//      String outStr = TextUtil.formatText(str);
//      out = 
//          new BufferedOutputStream(new FileOutputStream(outFile), outStr.length());
//      out.write(outStr.getBytes());
//      out.flush();
//      out.close();
//      out = null;
//
//
//    }
//    catch (Exception e)
//    {
//      throw new Exception(e);
//    }
//    finally
//    {
//      if (out != null)
//      {
//        try
//        {
//          out.close();
//        }
//        catch (Exception e)
//        {
//          throw new Exception(e);
//        }
//
//
//      }
//
//
//    }
//
//  }

  public String writeAsHTML()
    throws Exception
  {
    StringWriter sw = new StringWriter();


    HTMLWriter writer = new HTMLWriter(sw, this.outFormat);
    writer.write(this.doc);
    writer.flush();
    return sw.toString();

  }

  public void parseWithSAX(File aFile)
    throws Exception
  {
    try
    {
      SAXReader xmlReader = new SAXReader();
      this.doc = xmlReader.read(aFile);
    }
    catch (DocumentException e)
    {
      throw new Exception(e);
    }
  }

  public String openFile(String fileName)
    throws Exception
  {

    BufferedReader in = null;
    StringBuilder out = new StringBuilder(40000);
    try
    {


      in = 
          new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
      String t = in.readLine();
      do
      {
        out.append(t);
        out.append("\n");
        t = in.readLine();

      }
      while (t != null);
    }
    catch (IOException e)
    {
      throw new Exception(e);
    }

    finally
    {
        if ( in != null)
        {
      try
      {
        in.close();
      }
      catch (IOException e)
      {

        throw new Exception(e);
      }
        }
    }


    return out.toString();
  }


 


}
