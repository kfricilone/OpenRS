package net.openrs.cache.tools;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.openrs.cache.Constants;
import net.openrs.cache.Container;
import net.openrs.cache.FileStore;
import net.openrs.cache.ReferenceTable;

public final class CacheDefragmenter {

	public static void main(String[] args) throws IOException {
		try (FileStore in = FileStore.open(Constants.CACHE_PATH)) {
			try (FileStore out = FileStore.create(Constants.CACHETMP_PATH, in.getTypeCount())) {
				for (int type = 0; type < in.getTypeCount(); type++) {
					ByteBuffer buf = in.read(255, type);
					buf.mark();
					out.write(255, type, buf);
					buf.reset();

					ReferenceTable rt = ReferenceTable.decode(Container.decode(buf).getData());
					for (int file = 0; file < rt.capacity(); file++) {
						if (rt.getEntry(file) == null) {
							System.out.println(type + ", " + file);
							continue;
						}

						out.write(type, file, in.read(type, file));
					}
				}
			}
		}
	}

}