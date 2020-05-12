package org.satellitedna.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class BOMUtil
{
 private static final int NONE = -1;
 

  private static final byte[] UTF32BEBOMBYTES = new byte[]
    { (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, };
  private static final byte[] UTF32LEBOMBYTES = new byte[]
    { (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, };
  private static final byte[] UTF16BEBOMBYTES = new byte[]
    { (byte) 0xFE, (byte) 0xFF, };
  private static final byte[] UTF16LEBOMBYTES = new byte[]
    { (byte) 0xFF, (byte) 0xFE, };
  private static final byte[] UTF8BOMBYTES = new byte[]
    { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, };

  private static final byte[][] BOMBYTES = new byte[][]
    { UTF32BEBOMBYTES, UTF32LEBOMBYTES, UTF16BEBOMBYTES, UTF16LEBOMBYTES, 
      UTF8BOMBYTES, };

  private static final int MAXBOMBYTES = 
    4; //no bom sequence is longer than 4 byte

  public static int getBOMType(byte[] _bomBytes)
  {
    return getBOMType(_bomBytes, _bomBytes.length);
  }

  public static int getBOMType(byte[] _bomBytes, int _length)
  {
    for (int i = 0; i < BOMBYTES.length; i++)
    {
      for (int j = 0; j < _length && j < BOMBYTES[i].length; j++)
      {
        if (_bomBytes[j] != BOMBYTES[i][j])
          break;
        if (_bomBytes[j] == BOMBYTES[i][j] && j == BOMBYTES[i].length - 1)
          return i;
      }
    }
    return NONE;
  }

  public static int getBOMType(File _f)
    throws IOException
  {
      int BOMType;
     try (FileInputStream fIn = new FileInputStream(_f)) {
         byte[] buff = new byte[MAXBOMBYTES];
         int read = fIn.read(buff);
         BOMType = getBOMType(buff, read);
     }
    return BOMType;
  }

  public static int getSkipBytes(int BOMType)
  {
    if (BOMType < 0 || BOMType >= BOMBYTES.length)
      return 0;
    return BOMBYTES[BOMType].length;
  }

  /**
   * Just reads necessary bytes from the stream
   * 
     * @param _f
     * @param encoding
     * @return 
     * @throws java.io.IOException 
   */
  public static Reader getReader(File _f, String encoding)
    throws IOException
  {
    int BOMType = getBOMType(_f);
    int skipBytes = getSkipBytes(BOMType);
    FileInputStream fIn = new FileInputStream(_f);
    long read= fIn.skip(skipBytes);
    
    Reader reader = new InputStreamReader(fIn, encoding);
    return reader;
  }
}


