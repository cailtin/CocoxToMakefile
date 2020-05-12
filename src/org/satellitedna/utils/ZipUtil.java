package org.satellitedna.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FilenameUtils;

/**
 * copywrite 2006 clr
 */
public final class ZipUtil {

    public static final String SEPARATOR = "/";
    private static final Logger LOG = Logger.getLogger(ZipUtil.class.getName());

    public ZipUtil() {

    }

    
    
    
    public static boolean canCopyFile(String projectFile, String fileName) {
      

        // /home/clopez/projects/ARM/test/COX/src/COX/COX_Peripheral
        // /home/clopez/projects/ARM/test
        int projectSize = projectFile.length();
        int fileNameSize = fileName.length();

        return fileNameSize >= projectSize;

    }

    private static void copyFileImp(File source, File dest) throws IOException {

        byte array[] = new byte[500];
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);
            while (true) {
                int r = in.read(array);
                if (r == -1) {
                    break;
                }
                out.write(array);

            }

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, e.toString());
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, e.toString());
                }

            }

        }

    }

    private static void copyDir(String sourceDir, String destination) throws IOException {

        String sourceFile;
        String dest;
        try {

            File src = new File(sourceDir);
            if (src.isDirectory()) {

                File u = new File(destination);
                if (!u.mkdirs()) {
                  //  System.out.println("failed to make dirs " + destination);
                }
            }

            if (src.isDirectory()) {
                File fs[] = src.listFiles();

                for (File f : fs) {
                    if (f.isDirectory()) {
                     //   System.out.println("1");
                        copyDir(f.getAbsolutePath(), destination);
                    } else {
                        sourceFile = f.getPath();
                        String ext = FilenameUtils.getName(sourceFile);
                        dest = destination + "/" + ext;
                        File dst = new File(dest);
                        if (dst.exists()) {
                            continue;
                        }
                       // System.out.println("Copy " + f.getPath() + "\n   to" + dest);
///home/clopez/projects/ARM/test/COX/src/COX/COX_Peripheral/cox_adc.h (Not a directory)
                        copyFileImp(f, dst);
                    }
                }
            } else {

                String ns = FilenameUtils.getFullPath(destination);
                File u = new File(ns);
                if (!u.mkdirs()) {
              //      System.out.println("unable to create " + ns);
                }

                copyFileImp(new File(sourceDir), new File(destination));
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.toString());
            throw e;
        }

    }

    public static void copyFile(String source, String dest, String projectFile) throws IOException {

        if (canCopyFile(projectFile, dest) == false) {
            return;
        }

        try {

            copyDir(source, dest);

        } catch (java.io.IOException e) {
            LOG.log(Level.SEVERE, e.toString());
            throw e;

        }
    }

    public static String getPrefix(String name) {
        String projectPrefix = FilenameUtils.getPrefix(name);
        if (projectPrefix.length() == 1) {

            String tmp = name.substring(1, name.length());
            String t = projectPrefix + tmp.substring(0, tmp.indexOf("/")) + "/";

            return t;

        }
        return projectPrefix;
    }

    public static String formatT(String newProject, String target) {
        String tu = getPrefix(target);

      //  System.out.println(tu);
        String t = FilenameUtils.getName(target);
        return newProject + "/" + t;

    }

    public static void initializePath(String p) {

    }

    /**
     * Formats the file
     *
     * @param originalProject "/windows/projects/arm_projects/updated_lpc1768";
     * @param newProject "/home/clopez/projects/ARM/test/"
     * @param fileName
     * "/windows/projects/arm_projects/updated_lpc1768/cmsis_boot/startup/startup_LPC17xx.c";
     * @return
     */
    public static String formatProjectFile(String originalProject, String newProject, String fileName) {

        StringBuilder buffer = new StringBuilder(originalProject.length());
        buffer.append(newProject);
        if (newProject.endsWith("/") == false) {
            buffer.append("/");
        }

        ArrayList<String> l = ZipUtil.split(originalProject);

        ArrayList<String> u = ZipUtil.split(fileName);
        for (int count = l.size(); count < u.size(); count++) {

            buffer.append(u.get(count));
            if (count + 1 < u.size()) {
                buffer.append("/");
            }
        }
        return buffer.toString();
    }

    public static String formatCopyFilePath(String projectFile, String fileName) {

        ArrayList<String> fin = new ArrayList<>();

        ArrayList<String> projectList = split(projectFile);

        ArrayList<String> filenameList = split(fileName);

        String us = FilenameUtils.getPrefix(projectFile);

        fin.add(us);

        fin.add(projectList.get(0));
        fin.add("/");

        for (int count = 1; count < filenameList.size(); count++) {
            fin.add(filenameList.get(count));
            if (count + 1 < filenameList.size()) {
                fin.add("/");
            }
        }

        StringBuilder buffer = new StringBuilder(projectFile.length());
        Iterator<String> its = fin.iterator();
        while (its.hasNext()) {
            buffer.append(its.next());
        }
        return buffer.toString();

    }

    public static ArrayList<String> split(String n) {

        StringTokenizer tokenizer = new StringTokenizer(n, "/");
        ArrayList<String> list = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.isEmpty() == false) {
                list.add(token);
            }
        }
        return list;
    }

    public static String formatInclude(String newProject, String projectFile, String includeFile) {

        ArrayList<String> fin = new ArrayList<>();

        String prefix = FilenameUtils.getPrefix(includeFile);

        int start = 0;
        ArrayList<String> list = split(projectFile);
        ArrayList<String> inc = split(includeFile);
        if (prefix != null) {

            start = 1;
        }
        fin.add(newProject);
        fin.add("/");
        int count;
        for (count = start; count < inc.size(); count++) {
            String n = list.get(count);
            String o = inc.get(count);

            if (n.equalsIgnoreCase(o) == false) {
                break;
            }

        }
        if (count >= inc.size()) {
            return includeFile;
        }

        for (int loop = count; loop < inc.size(); loop++) {
            fin.add(inc.get(loop));
            if (loop + 1 < inc.size()) {
                fin.add("/");
            }

        }
        StringBuilder buffer = new StringBuilder(444);
        Iterator<String> its = fin.iterator();
        while (its.hasNext()) {
            buffer.append(its.next());
        }
        return buffer.toString();

    }

    /**
     *
     * This method uses the provided user coocox project path to retrieve the
     * coocox main project file (this file is needed because it has the gcc
     * parameters, linker, etc)
     *
     * @param coocoxPath the coocox project path
     * ../../../../CooCox/CoIDE/configuration/ProgramData/lpc_1768_controller"
     * @param projectPath the original project location
     * ../../../../CooCox/CoIDE/configuration/ProgramData/lpc_1768_controller
     * @return the path to the coocox project
     */
    public static String formatPath(String coocoxPath, String projectPath) {
        ArrayList<String> tokens = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(projectPath, "/");
        while (tokenizer.hasMoreTokens()) {
            String tok = tokenizer.nextToken();
            if (tok.contains("..") == false) {
                tokens.add(tok);
            }
        }
        StringTokenizer tos = new StringTokenizer(coocoxPath, "/");

        StringBuilder buffer = new StringBuilder(400);
        buffer.append("/");
        if (tos.hasMoreTokens()) {
            buffer.append(tos.nextToken());
            buffer.append("/");
        }
        for (int count = 0; count < tokens.size(); count++) {
            buffer.append(tokens.get(count));
            if (count + 1 < tokens.size()) {
                buffer.append("/");
            }
        }
        return buffer.toString();

    }

    public static String formatCoocoxPath(String projectLoction, String base) {

        String tok[] = projectLoction.split("/");
        String sep[] = base.split("/");
        StringBuilder buffer = new StringBuilder(base.length());
        int count = 0;

        buffer.append("/");

        for (; count < tok.length; count++) {
            String t = tok[count];

            if (t.isEmpty()) {
                continue;
            }
            if (t.equalsIgnoreCase("..")) {
                buffer.append(sep[count + 1]);
                buffer.append("/");
            } else {
                break;
            }

        }
        for (; count < tok.length; count++) {
            buffer.append(tok[count]);
            if (count + 1 < tok.length) {
                buffer.append("/");
            }
        }
        buffer.append("/build.xml");

        return buffer.toString();
    }

    public void appendToZip(String fileName, String newBaseDir,
            String[] files, int compressionLevel)
            throws Exception {
        try {
            String baseDir = getBaseDir(fileName);
            File file = new File(baseDir + SEPARATOR + "temp");
            file.mkdir();

            unzip(baseDir + SEPARATOR + "temp", fileName);
            String list[] = file.list();
            String fileList[] = new String[list.length + files.length];
            System.arraycopy(files, 0, fileList, 0, files.length);
            System.arraycopy(list, 0, fileList, files.length, list.length);
            zip(fileName, newBaseDir, fileList, compressionLevel);

        } catch (Exception e) {

            throw new Exception(e);
        }

    }

    public String getFileName(String filePath) {
        //return TextUtil.getFileName(TextUtil.formatDosFileName(file));
        return getFileNameIS(filePath.replace('\\', '/'));
    }

    public String getFileNameIS(String filePath) {
        int pos = filePath.lastIndexOf("/");
        if (pos != -1) {
            String ret = filePath.substring(pos + 1, filePath.length());
            return ret;

        }

        return filePath;
    }

    public String escapeDosSlash(String file) {
        String str = file.replace('\\', '/');
        return str;

    }

    public String getBaseDir(String file) {
        String ret = "/temp";
        String s = file.replace('\\', '/');
        int pos = s.lastIndexOf("/");
        if (pos != -1) {
            ret = s.substring(0, pos);
        }
        return ret;
    }

    public void zip(String fileName, String baseDir, String[] files,
            int compressionLevel)
            throws Exception {
        FileInputStream fileIn = null;
        ZipOutputStream outFile = null;
        try {
            byte buffer[] = new byte[35000];

            outFile = new ZipOutputStream(new FileOutputStream(fileName));
            outFile.setLevel(compressionLevel);
            for (String name : files) {
                if (baseDir != null) {
                    String fullPath = baseDir + SEPARATOR + getFileName(name);
                    name = fullPath;
                }
                fileIn = new FileInputStream(name);
                ZipEntry entry = new ZipEntry(getFileName(name));
                outFile.putNextEntry(entry);
                int length;
                while ((length = fileIn.read(buffer)) != -1) {
                    outFile.write(buffer, 0, length);
                }
                fileIn.close();
                fileIn = null;
                outFile.closeEntry();
            }

        } catch (IOException e) {

            throw new Exception(e);
        } finally {
            if (outFile != null) {
                try {
                    outFile.flush();
                    outFile.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }
            }
            if (fileIn != null) {
                try {
                    fileIn.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }

            }

        }

    }

    /**
     * zips (compresses and packs) one or more files
     *
     * @param fileName the zip file name
     * @param files one or more files to be compresses
     * @param compressionLevel
     * @throws java.lang.Exception
     */
    public void zip(String fileName, String[] files, int compressionLevel)
            throws Exception {
        ZipOutputStream outFile = null;
        FileInputStream fileIn = null;
        try {
            byte buffer[] = new byte[35000];

            outFile = new ZipOutputStream(new FileOutputStream(fileName));
            outFile.setLevel(compressionLevel);
            for (String file : files) {
                fileIn = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(getFileName(file));
                outFile.putNextEntry(entry);
                int length;
                while ((length = fileIn.read(buffer)) != -1) {
                    outFile.write(buffer, 0, length);
                }
                fileIn.close();
                fileIn = null;
                outFile.closeEntry();
            }

        } catch (IOException e) {

            throw new Exception(e);
        } finally {
            if (outFile != null) {
                try {
                    outFile.flush();
                    outFile.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }
            }
            if (fileIn != null) {
                try {
                    fileIn.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }

            }

        }

    }

    /**
     * uncompress a zip file
     *
     * @param fileName the zip file to be uncompress
     * @return
     * @throws java.lang.Exception
     */
    public String[] unzip(String fileName)
            throws Exception {
        return unzip(null, fileName);
    }

    /**
     * uzips a zip (compressed file)
     *
     * @param baseDir the directory where uncompress the files if
     * <code>null</code> the files are uncompressed in the same directory as the
     * zip file
     * @param fileName the zip file name
     * @return
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public String[] unzip(String baseDir, String fileName)
            throws Exception {
        ArrayList list = new ArrayList();
        ZipInputStream inFile = null;
        FileOutputStream outFile = null;
        try {
            inFile = new ZipInputStream(new FileInputStream(fileName));

            byte buffer[] = new byte[30500];

            ZipEntry entry = inFile.getNextEntry();
            while (entry != null) {
                String name = entry.getName();
                name = name.trim();
                if (name.endsWith("/")) {
                    File f = new File(baseDir + SEPARATOR + name);
                    f.mkdir();
                    entry = inFile.getNextEntry();
                    continue;
                }
                if (baseDir != null) {
                    String fullPath = baseDir + SEPARATOR + getFileName(name);
                    name = fullPath;
                }
                list.add(name);

                outFile = new FileOutputStream(name);
                int len;

                while ((len = inFile.read(buffer)) != -1) {

                    outFile.write(buffer, 0, len);

                }
                outFile.flush();
                outFile.close();
                outFile = null;
                inFile.closeEntry();
                entry = inFile.getNextEntry();

            }

        } catch (IOException e) {
            throw new Exception(e);
        } finally {
            if (outFile != null) {
                try {
                    outFile.flush();
                    outFile.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }
            }
            if (inFile != null) {
                try {
                    inFile.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }

            }

        }
        String ret[] = new String[list.size()];
        list.toArray(ret);
        return ret;

    }

    /**
     * uncompresses a file in zip format, if the <code>inFileName</code> has a
     * <b>gzip</b> extension the extension is removed the remainder of the name
     * is the output file name
     *
     * @param inFileName the input fileName, it must be terminated with a
     * <b>gzip</b> extension
     * @throws java.lang.Exception
     */
    public void uncompress(String inFileName)
            throws Exception {
        int pos = inFileName.indexOf(".gzip");
        if (pos != -1) {
            String outFileName = inFileName.substring(0, pos);
            uncompress(inFileName, outFileName);
        }

    }

    /**
     * uncompresses a file
     *
     * @param inFileName the input file name
     * @param outFileName the output file name
     * @throws java.lang.Exception
     */
    public void uncompress(String inFileName, String outFileName)
            throws Exception {
        GZIPInputStream inFile = null;
        OutputStream outFile = null;
        try {
            // Open the compressed file

            inFile = new GZIPInputStream(new FileInputStream(inFileName));

            // Open the output file
            outFile = new FileOutputStream(outFileName);

            // Transfer bytes from the compressed file to the output file
            byte[] buf = new byte[7000];
            int len;
            while ((len = inFile.read(buf)) > 0) {
                outFile.write(buf, 0, len);
            }

            // Close the file and stream
        } catch (IOException e) {

            throw new Exception(e);
        } finally {
            if (outFile != null) {
                try {
                    outFile.flush();
                    outFile.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }
            }
            if (inFile != null) {
                try {
                    inFile.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }

            }

        }
    }

    /**
     * compresses a file, the output file is the file name plus <b>gzip</b>
     * extension
     *
     * @param fileName the input file name
     * @throws java.lang.Exception
     */
    public void compress(String fileName)
            throws Exception {
        String outFileName = fileName + ".gzip";
        compress(fileName, outFileName);
    }

    /**
     * compresses a input file using gzip
     *
     * @param infileName the input file name
     * @param outFileName the output file name
     * @throws java.lang.Exception
     */
    public void compress(String infileName, String outFileName)
            throws Exception {
        BufferedInputStream inFile = null;
        GZIPOutputStream outFile = null;
        try {
            File file = new File(infileName);

            inFile = new BufferedInputStream(new FileInputStream(file), 4048);

            outFile = new GZIPOutputStream(new FileOutputStream(outFileName));

            // Transfer bytes from the input file to the GZIP output stream
            byte[] buf = new byte[4024];
            int len;
            while ((len = inFile.read(buf)) > 0) {
                outFile.write(buf, 0, len);
            }
            inFile.close();

            // Complete the GZIP file
            outFile.finish();
            outFile.close();

        } catch (IOException e) {

            throw new Exception(e);
        } finally {
            if (outFile != null) {
                try {
                    outFile.flush();
                    outFile.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }
            }
            if (inFile != null) {
                try {
                    inFile.close();

                } catch (IOException e) {
                    throw new Exception(e);

                }

            }

        }
    }

    public static void main(String args[]) {

        //    ZipUtil.fixPath(source, source, true)
    }

}
