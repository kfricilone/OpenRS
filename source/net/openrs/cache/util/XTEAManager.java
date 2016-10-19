/**
* Copyright (c) Kyle Fricilone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.openrs.cache.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.openrs.cache.Constants;

/**
 * @author Kyle Friz
 * 
 * @since Jun 30, 2015
 */
public class XTEAManager {

      private static final Map<Integer, int[]> maps = new HashMap<Integer, int[]>();
      private static final Map<Integer, int[]> tables = new HashMap<Integer, int[]>();

      public static final int[] NULL_KEYS = new int[4];

      public static final int[] lookupTable(int id) {
            int[] keys = tables.get(id);
            if (keys == null)
                  return NULL_KEYS;

            return keys;
      }

      public static final int[] lookupMap(int id) {
            int[] keys = maps.get(id);
            if (keys == null)
                  return NULL_KEYS;

            return keys;
      }

      static {
            try {

                  File xMapDir = new File(Constants.XMAP_PATH);

                  if (!xMapDir.exists()) {
                        xMapDir.mkdirs();
                  }

                  for (File file : xMapDir.listFiles()) {
                        if (file.getName().endsWith(".txt")) {
                              List<Integer> keys = new ArrayList<Integer>();
                              Integer regionID = Integer.valueOf(file.getName().substring(0,
                                          file.getName().indexOf(".txt")));

                              Files.lines(Paths.get(".")
                                          .resolve(Constants.XMAP_PATH + file.getName()))
                                          .forEach((String line) -> {
                                        	 // try {
                                        		  if (keys.size() < 4)
                                        			  keys.add(Integer.valueOf(line));
                                        	  //} catch (Exception e) { System.out.println(file.getName());}
                                          });

                              maps.put(regionID, listToArray(keys));
                        }
                  }

                  File xTableDir = new File(Constants.XTABLE_PATH);

                  if (!xTableDir.exists()) {
                        xTableDir.mkdirs();
                  }

                  for (File file : xTableDir.listFiles()) {
                        if (file.getName().endsWith(".txt")) {
                              List<Integer> keys = new ArrayList<Integer>();
                              Integer typeID = Integer.valueOf(file.getName().substring(0,
                                          file.getName().indexOf(".txt")));

                              Files.lines(Paths.get(".")
                                          .resolve(Constants.XTABLE_PATH + file.getName()))
                                          .forEach((String line) -> {
                                                keys.add(Integer.valueOf(line));
                                          });

                              tables.put(typeID, listToArray(keys));
                        }
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }
      
      private static int[] listToArray(List<Integer> list){
    	  int[] out = new int[list.size()];
    	  for(int i = 0; i < list.size(); i++){
    		  out[i] = list.get(i);
    	  }
    	  return out;
      }
      
      public static void touch() { };
      
}
