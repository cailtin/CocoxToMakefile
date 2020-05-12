/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.satellitedna.utils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.satellitedna.ui.CompilerFlags;

/**
 *
 * @author clopez
 */
public class Converter {

    ArrayList<String> fileList = new ArrayList<>();
    ArrayList<String> searchDirectories = new ArrayList<>();

    private final PropertyManager properties;

    private static final Logger LOG = Logger.getLogger(Converter.class.getName());
    private final String makefile[] = {
        "BASE_DEBUG    =",
        "BASE          = ",
        "CC            = ",
        "CXX           = ",
        "LINK          = $(CXX)",
        "DEFINES       = ",
        "CFLAGS        =  $(DEFINES)",
        "CXXFLAGS      = $(DEFINES)",
        "INCPATH       =  ",
        "LFLAGS        = ",
        "LIBS          = $(SUBLIBS) ",
        "AR            = ar cqs",
        "RANLIB        = ",
        "QMAKE         = /usr/bin/qmake",
        "TAR           = tar -cf",
        "COMPRESS      = gzip -9f",
        "COPY          = cp -f",
        "SED           = sed",
        "COPY_FILE     = $(COPY)",
        "COPY_DIR      = $(COPY) -r",
        "STRIP         = strip",
        "INSTALL_FILE  = install -m 644 -p",
        "INSTALL_DIR   = $(COPY_DIR)",
        "INSTALL_PROGRAM = install -m 755 -p",
        "DEL_FILE      = rm -f",
        "SYMLINK       = ln -f -s",
        "DEL_DIR       = rmdir",
        "MOVE          = mv -f",
        "CHK_DIR_EXISTS= test -d",
        "MKDIR         = mkdir -p",
        "",
        "",
        "OBJECTS_DIR   = ./objs",
        "",
        "####### Files",
        "",
        "SOURCES       = ",
        "",
        "OBJECTS       = ",
        "",
        "DIST          = ",
        "",
        "QMAKE_TARGET  = ",
        "DESTDIR       = ",
        "TARGET        = ",
        "",
        "first: all",
        "####### Implicit rules",
        "",
        ".SUFFIXES: .o .c .cpp .cc .cxx .C",
        "",
        ".cpp.o:",
        "\t$(CXX) -c $(CXXFLAGS) $(INCPATH) -o \"$@\" \"$<\"",
        "",
        ".cc.o:",
        "\t$(CXX) -c $(CXXFLAGS) $(INCPATH) -o \"$@\" \"$<\"",
        "",
        ".cxx.o:",
        "\t$(CXX) -c $(CXXFLAGS) $(INCPATH) -o \"$@\" \"$<\"",
        "",
        ".C.o:",
        "\t$(CXX) -c $(CXXFLAGS) $(INCPATH) -o \"$@\" \"$<\"",
        "",
        ".c.o:",
        "\t$(CC) -c $(CFLAGS) $(INCPATH) -o \"$@\" \"$<\"",
        "",
        "####### Build rules",
        "",
        "all: make_directories  Makefile $(TARGET)",
        "",
        "$(TARGET): $(OBJECTS)  ",
        "\t$(LINK) $(LFLAGS) -o $(OBJECTS_DIR)/$(TARGET) $(OBJECTS) $(OBJCOMP) $(LIBS)",
        "\t{ test -n \"$(DESTDIR)\" && DESTDIR=\"$(DESTDIR)\" || DESTDIR=.; } && test $$(gdb --version | sed -e 's,[^0-9][^0-9]*\\([0-9]\\)\\.\\([0-9]\\).*,\\1\\2,;q') -gt 72 && gdb --nx --batch --quiet -ex 'set confirm off' -ex \"save gdb-index $$DESTDIR\" -ex quit '$(TARGET)' && test -f $(TARGET).gdb-index && objcopy --add-section '.gdb_index=$(TARGET).gdb-index' --set-section-flags '.gdb_index=readonly' '$(TARGET)' '$(TARGET)' && rm -f $(TARGET).gdb-index || true",
        "",
        "####### Compile",
        "",
        ".PHONY: clean",
        "clean: ",
        "\t-rm -rf $(OBJECTS_DIR)",
        "",
        "",
        "####### Sub-libraries",
        "",
        ".PHONY: make_directories",
        "make_directories: \n\t\t/usr/bin/mkdir -p $(OBJECTS_DIR)",
        "",
        "check: first",
        "",
        "",
        "",
        "",
        "",
        "####### Install",
        "",
        "install:   FORCE",
        "",
        "uninstall:   FORCE",
        "",
        "FORCE:",
        ""};

    public Converter(PropertyManager properties) {
        this.properties = properties;
    }

    private final ArrayList<Action> propertyList = new ArrayList<>();
    private final HashMap<String, ArrayList<Action>> actions = new HashMap<>();

    /**
     * add an external variable
     *
     * @param act <code>Action</code> to add
     */
    public void addExternalVariable(Action act) {
        propertyList.add(act);
    }

    /**
     * sees if a variable is defined in the coocox project
     *
     * @param variable the variable name
     * @return <code>true</code> if the variable exists
     */
    public boolean variableExists(String variable) {
        Iterator<Action> uap = propertyList.iterator();
        while (uap.hasNext()) {

            Action ac = uap.next();
            if (ac.getActed().equalsIgnoreCase(variable)) {

                return true;

            }
        }
        return false;
    }

    /**
     * gets a variable value
     *
     * @param variable the variable
     * @return the value
     * @throws TransformException on variable do not exists
     */
    public String getVariableValue(String variable) throws TransformException {
        Iterator<Action> uap = propertyList.iterator();
        while (uap.hasNext()) {

            Action ac = uap.next();
            if (ac.getActed().equalsIgnoreCase(variable)) {

                return ac.getActions().get(0).getActed();

            }
        }
        throw new TransformException("unable to find variable " + variable);
    }

    /**
     * complex method, gets the variables and applies the prefix,subfix and new
     * project location
     *
     * @param section the build section for example:"compile"
     * @param subSection the sub section for example: "compilerarg"
     * @param prefix if not null is added at the beginning of the string
     * @param subfix if not null is added to the end of the string
     * @param newProjectLocation if not null the file path is modified to use
     * this folder
     * @return list of variables with applies the prefix,subfix and new project
     * location applied
     * @throws TransformException if the section or sub section are not found
     */
    public ArrayList<String> getVariablesList(String section, String subSection, String prefix, String subfix, String newProjectLocation) throws TransformException {
        ArrayList<Action> act = actions.get(section);
        if (act == null) {
            throw new TransformException("Unable to find Section " + section);
        }
        ArrayList<String> values = new ArrayList<>();
        Iterator<Action> its = act.iterator();
        while (its.hasNext()) {
            Action u = its.next();

            if (u.getAction().equalsIgnoreCase(subSection)) {
                String text = u.getActed();
                if (newProjectLocation != null) {
                    text = ZipUtil.formatInclude(newProjectLocation, properties.getProperty(CompilerFlags.COOCOX_BASE_LOCATION.name()), text);
                }
                values.add(prefix + text);
            }

        }
        return values;
    }

    /**
     * complex method, gets the variables and applies the prefix,subfix and new
     * project location note that this method returns an string, a sample usage
     * is to generate the include path -Ipath -I path etc
     *
     * @param section the build section for example:"compile"
     * @param subSection the sub section for example: "compilerarg"
     * @param prefix if not null is added at the beginning of the string
     * @param subfix if not null is added to the end of the string
     * @param newProjectLocation if not null the file path is modified to use
     * this folder
     * @return list of variables as string with applies the prefix,subfix and
     * new project location applied
     * @throws TransformException if the section or sub section are not found
     */
    public String getVariables(String section, String subSection, String prefix, String subfix, String newProjectLocation) throws TransformException {
        ArrayList<Action> act = actions.get(section);
        if (act == null) {
            throw new TransformException("Unable to find Section " + section);
        }
        ArrayList<String> values = new ArrayList<>();
        Iterator<Action> its = act.iterator();
        while (its.hasNext()) {
            Action u = its.next();

            if (u.getAction().equalsIgnoreCase(subSection)) {
                String text = u.getActed();
                if (newProjectLocation != null) {
                    text = ZipUtil.formatInclude(newProjectLocation, properties.getProperty(CompilerFlags.COOCOX_BASE_LOCATION.name()), text);

                }

                values.add(prefix + text);
            }

        }
        String listString = String.join(subfix, values);

        return listString;
    }

    /**
     * gets all the variables
     *
     * @param section the build section for example:"compile"
     * @param subSection the sub section for example: "compilerarg"
     * @return list of variables
     * @throws TransformException if the section is not found
     */
    public ArrayList<String> getVariablesList(String section, String subSection) throws TransformException {
        ArrayList<Action> act = actions.get(section);
        if (act == null) {
            throw new TransformException("Unable to find Section " + section);
        }
        ArrayList<String> values = new ArrayList<>();
        Iterator<Action> its = act.iterator();
        while (its.hasNext()) {
            Action u = its.next();
            if (u.getAction().equalsIgnoreCase(subSection)) {
                values.add(u.getActed());
            }

        }
        return values;
    }

    /**
     * gets an item from the build.xml and returns the actions contained on the
     * node
     *
     * @param child the XML child
     * @return list of elements contained on the node
     */
    private ArrayList<Action> processCompile(Element child) {

        ArrayList<Action> actionList = new ArrayList<>();

        List<Element> elList = child.elements();
        int size = elList.size();
        for (int count = 0; count < size; count++) {
            Element e = elList.get(count);
            String name = e.getName();
            switch (name) {
                case "mkdir":
                    Action act = new Action(name, e.attributeValue("dir"));
                    actionList.add(act);
                    break;
                case "cc":
                    act = new Action(name, e.attributeValue("debug"));
                    act.addAction(new Action("debug", e.attributeValue("debug")));
                    act.addAction(new Action("incremental", e.attributeValue("incremental")));
                    act.addAction(new Action("objdir", e.attributeValue("objdir")));
                    act.addAction(new Action("outfile", e.attributeValue("outfile")));
                    act.addAction(new Action("outtype", e.attributeValue("outtype")));
                    act.addAction(new Action("subsystem", e.attributeValue("subsystem")));
                    actionList.add(act);

                    return processCompile(e);

                case "fileset":
                    act = new Action(name, e.attributeValue("file"));
                    actionList.add(act);
                    break;
                // <project outfile="${output.path}/${output.name}.elf" overwrite="true" type="xcode"/>
                case "project":
                    act = new Action(name, e.attributeValue("outfile"));
                    act.addAction(new Action("overwrite", e.attributeValue("outtype")));
                    actionList.add(act);
                    break;

                case "defineset":
                    Element chi = e.element("define");
                    if (chi != null) {
                        act = new Action("define", e.attributeValue("name"));
                        actionList.add(act);
                    }
                    break;
                case "includepath":
                    act = new Action(name, e.attributeValue("path"));
                    actionList.add(act);
                    break;
                case "compilerarg":
                case "linkerarg":
                    act = new Action(name, e.attributeValue("value"));
                    actionList.add(act);
                    break;
                case "delete":
                    act = new Action(name, e.attributeValue("dir"));
                    actionList.add(act);
                    break;
                case "exec":
                    act = new Action(name, e.attributeValue("executable"));
                    Iterator<Element> its = e.elementIterator();
                    while (its.hasNext()) {
                        Element next = its.next();
                        act.addAction(new Action("arg", next.attributeValue("value")));
                    }
                    actionList.add(act);
                    break;

            }

        }

        return actionList;
    }

    /**
     * processes the build.xml walks through the elements retrieving the items
     * into subsections
     *
     * @param inputFileName the build.xml file path
     * @throws Exception on error processing the file
     */
    public void process(String inputFileName) throws Exception {

        XPathQuery query = new XPathQuery();
        Document doc = query.open(inputFileName);
        Element root = doc.getRootElement();
        List<Element> elements = root.elements();
        Element child;
        for (int count = 0; count < elements.size(); count++) {
            child = elements.get(count);

            if (child.getName().equalsIgnoreCase("property")) {
                // <property name="project.name" value="lpc_1768_controller"/>
                Action acc = new Action("property", child.attributeValue("name"));
                acc.addAction(new Action("value", child.attributeValue("value")));

                propertyList.add(acc);
            }
            if (child.getName().equalsIgnoreCase("target")) {

                Attribute t = child.attribute("name");
                String name = t.getValue();
                switch (name) {
                    case "compile":
                    case "postbuild":
                    case "clean":
                        actions.put(name, processCompile(child));
                        break;

                }

            }

        }

    }

    /**
     * very complex method,based on the
     *
     * @param stringPrefix the string prefix for example -I can not be null
     * @param stringPrefixSubItem what to add after the stringPrefix for example
     * \t can be null
     * @param items the items to process can not be null
     * @param replace if not null what to replace in the string, it uses the
     * replace with to replace the value
     * @param replaceWith if replace is not null the replacement value
     * @param append if not null add to the end of the string
     * @param addTab if <code>true</code> a tab is added to each item
     * @param removePrefix if <code>true</code> removes the filename prefix for
     * example .c
     * @param newLocation the new project location
     * @param stringPrefix the new filename prefix
     * @return
     */
    private String replace(String stringPrefix, String stringPrefixSubItem, ArrayList<String> items,
            String replace, String replaceWith, String append, boolean addTab, boolean removePrefix,
            String newLocation, String prefix) {

        StringBuilder buffer = new StringBuilder(4000);
        buffer.append(prefix);

        String base = properties.getProperty(CompilerFlags.COOCOX_BASE_FILTERED.name());

        int size = items.size();
        for (int count = 0; count < size; count++) {

            if (stringPrefixSubItem != null) {

                buffer.append(stringPrefixSubItem);
            }
            if (addTab && count > 0) {
                buffer.append('\t');
            }

            String text = items.get(count);
            if (newLocation != null) {
                // CompilerFlags.COOCOX_BASE_FILTERED.name()
                // CompilerFlags.PROJECT_LOCATION.name()
                text = ZipUtil.formatProjectFile(base, newLocation, text);

            }
            if (removePrefix) {
                text = text.substring(text.lastIndexOf('/') + 1);
            }

            if (replace != null) {

                text = text.substring(0, text.lastIndexOf(replace)) + replaceWith;

            }

            if (prefix != null) {
                buffer.append(prefix);
            }

            buffer.append(text);
            if (count + 1 < size && append.isEmpty() == false) {
                buffer.append(append);
            }

        }
        String ret = buffer.toString();

        return ret;
    }

    private final ArrayList<String> outHolder = new ArrayList<>();

    /**
     * processes the build.xml and creates the makefile
     *
     * @param newProjectDirectory the new project location
     * @return array list holding the makefile lines
     * @throws TransformException on error processing the file
     */
    public ArrayList<String> createMakeFileFromBuildxml(String newProjectDirectory) throws TransformException {

        BufferedReader br = null;
        boolean processed = false;
        // the output 
        ArrayList<String> temp = new ArrayList<>();
        String n;
        String variable;

        String base = properties.getProperty(CompilerFlags.COOCOX_BASE_FILTERED.name());
        String compilerBase = properties.getProperty(CompilerFlags.COMPILER_BASE.name());

        try {

            outHolder.clear();
            outHolder.addAll(Arrays.asList(makefile));
            for (int count = 0; count < outHolder.size(); count++) {
                String line = outHolder.get(count);
                line = line.trim();

                if (line.startsWith("CXXFLAGS")) {
                    
                    if (line.contains("$(CXXFLAGS)")) {
                        continue;
                    }

                    /**
                     * creates the compiler options
                     * CXXFLAGS = -mcpu=cortex-m3
                     * 
                     * <compilerarg value="-mcpu=cortex-m3"/>
          
                     */
                    variable = getVariables("compile", "compilerarg", " ", "", null);

                    temp.add(line + variable + "\n");
                    // add the link instructions  
                    temp.add("LINK          = " + compilerBase + "/arm-none-eabi-ld");

                }
                
                if (line.startsWith("LFLAGS")) {

                    ArrayList<String> items = getVariablesList("compile", "linkerarg");
                    /*
                     
                    creates the      LFLAGS        = -Wl,-nostartfiles -Wl,-O0 -Wl,-g  -Wl,-Map=output.map -Wl,--gc-sections  -L${OBJECTS_DIR} -Tarm-gcc-link.ld

                       <linkerarg value="-mcpu=cortex-m3"/>
                    */

                    StringBuilder buffer = new StringBuilder(4000);
                    for (int loop = 0; loop < items.size(); loop++) {
                        String item = items.get(loop);
                        if (item.startsWith("-L")) {

                            item = "-L${OBJECTS_DIR}";
                            buffer.append(item);
                            buffer.append(" ");
                        } else if (item.startsWith("-T")) {

                            buffer.append("-Tarm-gcc-link.ld");
                            buffer.append(" ");
                        } else {

                            buffer.append(item);
                            buffer.append(" ");

                        }
                    }

                    temp.add(line + buffer.toString() + "\n");

                }

                if (line.contains("BASE_DEBUG")) {
                    if (newProjectDirectory == null) {
                        variable = getVariableValue("project.path");
                    } else {
                        variable = newProjectDirectory;
                    }
                    temp.add(line + variable + "\n");

                }
                if (line.contains("BASE")) {
                    if (line.contains("BASE_DEBUG")) {
                        continue;
                    }

                    if (newProjectDirectory == null) {
                        variable = getVariableValue("project.path");
                    } else {
                        variable = newProjectDirectory;
                    }
                    // add the base variable
                    temp.add(line + variable + "\n");

                }
                if (line.startsWith("CC")) {
                    variable = properties.getProperty(CompilerFlags.CC.name());
                    temp.add(line + variable + "\n");
                    // adds the c compiler
                }
                if (line.startsWith("CXX")) {
                    if (line.contains("CXXFLAGS")) {
                        continue;
                    }
                    // adds the C++ compiler
                    variable = properties.getProperty(CompilerFlags.CCX.name());
                    temp.add(line + variable + "\n");
                }
                if (line.startsWith("INCPATH")) {
                    variable = getVariables("compile", "includepath", "-I", " ", newProjectDirectory);
                    // adds the include path
                    temp.add(line + variable + "\n");
                }
                if (line.contains("SOURCES")) {
                    if (line.contains("--parents ")) {
                        continue;
                    }

                    ArrayList<String> items = getVariablesList("compile", "fileset");
                    /*
                     add the sources
                    SOURCES=./uart.c\
	./lpc17xx_lib/source/lpc17xx_nvic.c\
	./lpc17xx_lib/source/lpc17xx_pinsel.c\
	./cmsis_boot/startup/startup_LPC17xx.c\
                    */
                    n = replace("\n\nSOURCES=", null, items, null, null, "\\\n", true, false, newProjectDirectory, null);
                    temp.add(n);
                    temp.add("\n");

                }
                if (line.contains("OBJECTS")) {
                    if (line.contains("$(TARGET)") || line.contains("OBJECTS_DIR")) {
                        temp.add(line);
                        temp.add("\n");
                        continue;
                    }
                    /**
                     * adds the objects
                     * OBJECTS=uart.o\
                                lpc17xx_nvic.o\
                     */

                    ArrayList<String> items = getVariablesList("compile", "fileset");
                    n = replace("\nOBJECTS=", null, items, ".c", ".o", "\\\n", true, true, null, "./objs/");
                    temp.add(n);
                    temp.add("\n");
                }
                if (line.startsWith("TARGET")) {

                    if (line.contains("all: ") || line.contains("$(TARGET)") || line.contains("QMAKE_TARGET")) {
                        continue;
                    }

                    // add the target TARGET        = lpc_1768_controller
                    temp.add("\nTARGET        = " + getVariableValue("output.name").replace('\n', ' ') + "\n");

                }
                if (line.contains("clean:")) {
                    temp.add("\n\n");
                    int loop;
                    for (loop = count; loop < outHolder.size(); loop++) {
                        line = outHolder.get(loop);
                        temp.add(line + "\n");
                    }
                    count = loop;
                    // add the clean commands
                }

                if (line.contains("####### Build rules")) {

                       /*
                        adds   ####### Build rules
                          $(TARGET): $(OBJECTS)  
                        	$(LINK) $(LFLAGS) -o $(TARGET) $(OBJECTS) $(OBJCOMP) $(LIBS)
                            	{ test -n "$(DESTDIR)" && DESTDIR="$(DESTDIR)" || DESTDIR=.; } && test $$(gdb --version | sed -e 's,[^0-9][^0-9]*\([0-9]\)\.\([0-9]\).*,\1\2,;q') -gt 72 && gdb --nx --batch --quiet -ex 'set confirm off' -ex "save gdb-index $$DESTDIR" -ex quit '$(TARGET)' && test -f $(TARGET).gdb-index && objcopy --add-section '.gdb_index=$(TARGET).gdb-index' --set-section-flags '.gdb_index=readonly' '$(TARGET)' '$(TARGET)' && rm -f $(TARGET).gdb-index || true


                         */
                    
                   
                    int loop;
                    for (loop = count; loop < outHolder.size(); loop++) {
                        line = outHolder.get(loop);

                        if (line.contains("####### Compile")) {

                            break;
                        }

                        if (line.contains("####### Build rules")
                                || line.contains("all: Makefile $(TARGET)")) {
                            temp.add("\n" + line + "\n");
                        } else if (line.contains("$(TARGET): $(OBJECTS) ")
                                || line.contains("$(LINK) $(LFLAGS) -o $(TARGET) $(OBJECTS) $(OBJCOMP) $(LIBS)")) {
                            temp.add(line + "\n");
                        } else {
                            temp.add(line);
                        }

                     
                    }

                    count = loop - 1;

                }

                if (line.contains(".SUFFIXES:")) {
                    temp.add("\n" + line);
                    temp.add("\n");
                    /**
                     * add the sub-fixes
                     * 
                     * .SUFFIXES: .o .c .cpp .cc .cxx .C
                            .cpp.o:

                        	$(CXX) -c $(CXXFLAGS) $(INCPATH) -o "$@" "$<"

                     */
                    for (int loop = count + 1; loop < outHolder.size(); loop++) {
                        n = outHolder.get(loop);
                        if (n.contains("####### Build rules")) {

                            count = loop - 1;
                            break;
                        }

                        n = n.trim();

                        if (n.isEmpty() == false) {
                            if (n.startsWith(".")) {

                                temp.add(n + "\n");

                            } else {
                                temp.add("\t\t" + n + "\n");
                            }
                        }
                    }

                    temp.add("\n");
                }
                if (line.contains("####### Compile")) {
/**
 * adds the compile set
 * 
 * mcp23017.o:
		$(CC) -c $(CXXFLAGS) $(INCPATH) -o mcp23017.o ./i2c/mcp23017.c
 */
                    if (!processed) {
                        processed = true;
                    } else {
                        continue;
                    }
                    ArrayList<String> items = getVariablesList("compile", "fileset");

                    Iterator<String> its = items.iterator();
                    while (its.hasNext()) {
                        String original = its.next();

                        if (newProjectDirectory != null) {

                            // CompilerFlags.COOCOX_BASE_FILTERED.name()
                            // CompilerFlags.PROJECT_LOCATION.name()
                            original = ZipUtil.formatProjectFile(base, newProjectDirectory, original);

                        }

                        String modified = original.substring(0, original.lastIndexOf(".c")) + ".o";

                        String idStr = "./objs/" + modified.substring(modified.lastIndexOf('/') + 1);

                        temp.add(idStr + ":");
                        temp.add("\t\t$(CXX) -c $(CXXFLAGS) $(INCPATH) -o " + idStr + " " + original);
                        temp.add("\n\n");

                    }

                }

            }

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, e.toString());
                    throw new TransformException(e.toString(), e);
                }

            }
        }

        return temp;
    }

}
