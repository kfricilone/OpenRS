package net.openrs.cache.track;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.openrs.cache.Cache;
import net.openrs.cache.Container;
import net.openrs.cache.ReferenceTable;
import net.openrs.cache.type.CacheIndex;
import net.openrs.cache.type.TypeList;

/**
 * The class that holds the collection of tracks
 * 
 * @author Freyr
 */
public class Tracks {
	
	private static Track[] track1s;	
	
	private static Track[] track2s;

	public static void initialize(Cache cache) {
		// track1s
		try {
			ReferenceTable table = cache.getReferenceTable(CacheIndex.TRACK1);

			track1s = new Track[table.capacity()];
			for (int i = 0; i < table.capacity(); i++) {
				
				if (table.getEntry(i) == null) {
					continue;
				}

				Container container = cache.read(CacheIndex.TRACK1, i);
				
				byte[] encoded = new byte[container.getData().limit()];				
				container.getData().get(encoded);
				
				Track track1 = new Track();				
				track1.decode(ByteBuffer.wrap(encoded));
				
				track1s[i] = track1;			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// tracks2s
		try {
			ReferenceTable table = cache.getReferenceTable(CacheIndex.TRACK2);
			
			track2s = new Track[table.capacity()];
			for (int i = 0; i < table.capacity(); i++) {
				
				if (table.getEntry(i) == null) {
					continue;
				}

				Container container = cache.read(CacheIndex.TRACK2, i);
				
				byte[] encoded = new byte[container.getData().limit()];				
				container.getData().get(encoded);
				
				Track track2 = new Track();					
				track2.decode(ByteBuffer.wrap(encoded));
				
				track2s[i] = track2;			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static Track getTrack1(int id) {
		return track1s[id];
	}
	
	public static int getTrack1Count() {
		return track1s.length;
	}
	
	public static Track getTrack2(int id) {
		return track2s[id];
	}
	
	public static int getTrack2Count() {
		return track2s.length;
	}

}

