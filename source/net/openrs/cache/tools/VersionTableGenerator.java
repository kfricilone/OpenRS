package net.openrs.cache.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.FileStore;

/**
 * Generates the version table and prints it out.
 * 
 * @author Seven | Discardedx2 & Graham wrote the method.
 * 
 */
public final class VersionTableGenerator {

      public static void main(String[] args) throws FileNotFoundException, IOException {
            try(Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH))) {                  
                  generateVersionTable(cache);                  
            }            
      }

      /**
       * Generates the "Update Keys" by encoding the checksum table into "Update Key" format and prints them out.
       * 
       * @param cache
       *    The cache to create the checksum table from.
       */
      public static void generateVersionTable(Cache cache) throws IOException {
            ByteBuffer table = cache.createChecksumTable().encode();
            /* encode the checksum table into the 'update keys' format */
            ByteBuffer updateKeys = ByteBuffer.allocate(table.limit() + 8);
            updateKeys.put((byte) 0xFF);       /* type = 255 */
            updateKeys.putShort((short) 0xFF); /* file = 255 */
            updateKeys.put((byte) 0);          /* no compression */
            updateKeys.putInt(table.limit());  /* length */
            updateKeys.put(table);             /* the checksum table */
            updateKeys.flip();

            /* finally print the 'update keys' */
            System.out.println("public static final int[] VERSION_TABLE = new int[] {");
            System.out.print("    ");
            for (int i = 0; i < updateKeys.limit(); i++) {
                String hex = Integer.toHexString(updateKeys.get(i) & 0xFF).toUpperCase();
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }

                System.out.print("0x" + hex + ", ");
                if ((i - 7) % 8 == 0) {
                    System.out.println();
                    if (i != updateKeys.limit() - 1) {
                        System.out.print("    ");
                    }
                }
            }
            System.out.println("};");
        }
      
}
