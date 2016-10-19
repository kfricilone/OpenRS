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
            	ByteBuffer table = cache.createChecksumTable().encode();
            	
                /* encode the checksum table into the 'update keys' format */
                ByteBuffer buf = ByteBuffer.allocate(table.limit() + 8);
                buf.put((byte) 0xFF);       /* type = 255 */
                buf.putShort((short) 0xFF); /* file = 255 */
                buf.put((byte) 0);          /* no compression */
                buf.putInt(table.limit());  /* length */
                buf.put(table);             /* the checksum table */
                buf.flip();

                /* finally print the 'update keys' */
                System.out.println("public static final int[] VERSION_TABLE = new int[] {");
                System.out.print("    ");
                for (int i = 0; i < buf.limit(); i++) {
                    String hex = Integer.toHexString(buf.get(i) & 0xFF).toUpperCase();
                    if (hex.length() == 1) {
                        hex = "0" + hex;
                    }

                    System.out.print("0x" + hex + ", ");
                    if ((i - 7) % 8 == 0) {
                        System.out.println();
                        if (i != buf.limit() - 1) {
                            System.out.print("    ");
                        }
                    }
                }
                System.out.println("};");               
            }            
      }
      
}
