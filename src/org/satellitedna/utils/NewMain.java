/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
/**
 *
 * @author clopez
 */
public class NewMain {

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

}
