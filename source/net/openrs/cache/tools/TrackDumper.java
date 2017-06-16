package net.openrs.cache.tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.FileStore;
import net.openrs.cache.type.track.TrackType;
import net.openrs.cache.type.track.TrackTypeList;

public final class TrackDumper {
	
	private TrackDumper() {
		
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {		
		try (Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH))) {
			
			File track1Dir = new File(Constants.TRACK1_PATH);			
			
			if (!track1Dir.exists()) {
				track1Dir.mkdirs();
			}
			
			File track2Dir = new File(Constants.TRACK2_PATH);			
			
			if (!track2Dir.exists()) {
				track2Dir.mkdirs();
			}
			
			TrackTypeList list = new TrackTypeList();
			list.initialize(cache);
			
			for (int i = 0; i < list.getTrack1s().length; i++) {
				
				TrackType track1 = list.getTrack1s()[i];
				
				if (track1 == null) {
					continue;
				}
				
				try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(track1Dir, i + ".midi")))) {
					dos.write(track1.getDecoded());
				}
				
				double progress = (double) (i + 1) / list.getTrack1s().length * 100;
				
				System.out.printf("dumping track1 %d out of %d %.2f%s\n", (i + 1), list.getTrack1s().length, progress, "%");				
				
			}
			
			for (int i = 0; i < list.getTrack2s().length; i++) {
				
				TrackType track2 = list.getTrack2s()[i];				
				
				if (track2 == null) {
					continue;
				}
				
				try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(track2Dir, i + ".midi")))) {
					dos.write(track2.getDecoded());
				}
				
				double progress = (double) (i + 1) / list.getTrack2s().length * 100;
				
				System.out.printf("dumping track2 %d out of %d %.2f%s\n", (i + 1), list.getTrack2s().length, progress, "%");				
				
			}

			
		}
	}

}
