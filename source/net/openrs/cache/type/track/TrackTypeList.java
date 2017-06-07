package net.openrs.cache.type.track;

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
public class TrackTypeList implements TypeList<TrackType> {
	
	private TrackType[] track1s;	
	
	private TrackType[] track2s;

	@Override
	public void initialize(Cache cache) {
		// track1s
		try {
			ReferenceTable table = ReferenceTable.decode(Container.decode(cache.getStore().read(255, CacheIndex.TRACK1.getID())).getData());
			
			track1s = new TrackType[table.capacity()];
			
			for (int i = 0; i < table.capacity(); i++) {
				
				if (table.getEntry(i) == null) {
					continue;
				}

				Container container = cache.read(CacheIndex.TRACK1, i);
				
				byte[] encoded = new byte[container.getData().limit()];				
				container.getData().get(encoded);
				
				TrackType track1 = new TrackType();				
				track1.decode(ByteBuffer.wrap(encoded));
				
				track1s[i] = track1;			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// tracks2s
		try {
			ReferenceTable table = ReferenceTable.decode(Container.decode(cache.getStore().read(255, CacheIndex.TRACK2.getID())).getData());
			
			track2s = new TrackType[table.capacity()];
			
			for (int i = 0; i < table.capacity(); i++) {
				
				if (table.getEntry(i) == null) {
					continue;
				}

				Container container = cache.read(CacheIndex.TRACK2, i);
				
				byte[] encoded = new byte[container.getData().limit()];				
				container.getData().get(encoded);
				
				TrackType track2 = new TrackType();					
				track2.decode(ByteBuffer.wrap(encoded));
				
				track2s[i] = track2;			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public TrackType list(int id) {
		throw new UnsupportedOperationException("This method is not supported, use #getTrack1s or #getTrack2s");
	}

	@Override
	public void print() {
		
	}

	@Override
	public int size() {
		return 0;
	}

	public TrackType[] getTrack1s() {
		return track1s;
	}
	
	public TrackType[] getTrack2s() {
		return track2s;
	}

}

