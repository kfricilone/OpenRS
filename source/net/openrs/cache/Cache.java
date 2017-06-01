/**
 * Copyright (c) 2014 RSE Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.openrs.cache;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

import net.openrs.cache.ReferenceTable.Entry;
import net.openrs.cache.type.CacheIndex;
import net.openrs.cache.type.ConfigArchive;
import net.openrs.cache.util.XTEAManager;
import net.openrs.util.ByteBufferUtils;
import net.openrs.util.crypto.Djb2;
import net.openrs.util.crypto.Whirlpool;

/**
 * The {@link Cache} class provides a unified, high-level API for modifying the
 * cache of a Jagex game.
 * 
 * @author Graham
 * @author `Discardedx2
 */
public final class Cache implements Closeable {

	private static final Map<String, Integer> identifiers = new HashMap<>();
	
	/**
	 * The file store that backs this cache.
	 */
	private final FileStore store;

	/**
	 * The list of reference tables for this cache
	 */
	private Map<Integer, ReferenceTable> references;

	/**
	 * Creates a new {@link Cache} backed by the specified {@link FileStore}.
	 * 
	 * @param store
	 *            The {@link FileStore} that backs this {@link Cache}.
	 * @throws IOException
	 */
	public Cache(FileStore store) throws IOException {
		this.store = store;

		this.references = new HashMap<>(store.getTypeCount());
		
		for (int type = 0; type < store.getTypeCount(); type++) {
			ByteBuffer buf = store.read(255, type);
			if (buf != null && buf.limit() > 0) {
				this.references.put(type, ReferenceTable.decode(Container.decode(buf, XTEAManager.lookupTable(type)).getData()));
			}
		}
	}

	public void close() throws IOException {
		store.close();
	}

	public final ReferenceTable getReferenceTable(int type) {
		return references.get(type);
	}

	public final boolean hasReferenceTable(int type) {
		return references.containsKey(type);
	}

	/**
	 * Computes the {@link ChecksumTable} for this cache. The checksum table
	 * forms part of the so-called "update keys".
	 * 
	 * @return The {@link ChecksumTable}.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public ChecksumTable createChecksumTable() throws IOException {
		/* create the checksum table */
		int size = store.getTypeCount();
		ChecksumTable table = new ChecksumTable(size);

		/*
		 * loop through all the reference tables and get their CRC and versions
		 */
		for (int i = 0; i < size; i++) {
			int crc = 0;
			int version = 0;
			int files = 0;
			int archiveSize = 0;
			byte[] whirlpool = new byte[64];

			if (store.hasData()) {
				/*
				 * if there is actually a reference table, calculate the CRC,
				 * version and whirlpool hash
				 */
				ByteBuffer buf = store.read(255, i);
				if (buf != null && buf.limit() > 0) {
					ReferenceTable ref = ReferenceTable.decode(Container.decode(buf).getData());
					crc = ByteBufferUtils.getCrcChecksum(buf);
					version = ref.getVersion();
					files = ref.capacity();
					archiveSize = ref.getArchiveSize();
					buf.position(0);
					whirlpool = ByteBufferUtils.getWhirlpoolDigest(buf);
				}
			}

			table.setEntry(i, new ChecksumTable.Entry(crc, version, files, archiveSize, whirlpool));
		}

		/* return the table */
		return table;
	}

	/**
	 * Gets the number of files of the specified type.
	 * 
	 * @param type
	 *            The type.
	 * @return The number of files.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public int getFileCount(int type) throws IOException {
		return store.getFileCount(type);
	}

	/**
	 * Gets the {@link FileStore} that backs this {@link Cache}.
	 * 
	 * @return The underlying file store.
	 */
	public FileStore getStore() {
		return store;
	}

	/**
	 * Gets the number of index files, not including the meta index file.
	 * 
	 * @return The number of index files.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public int getTypeCount() throws IOException {
		return store.getTypeCount();
	}

	/**
	 * Reads a file from the cache.
	 * 
	 * @param type
	 *            The type of file.
	 * @param file
	 *            The file id.
	 * @return The file.
	 * @throws IOException
	 *             if an I/O error occurred.
	 */
	public Container read(CacheIndex index, ConfigArchive archive) throws IOException {
		return read(index.getID(), archive.getID());
	}

	/**
	 * Reads a file from the cache.
	 * 
	 * @param type
	 *            The type of file.
	 * @param file
	 *            The file id.
	 * @return The file.
	 * @throws IOException
	 *             if an I/O error occurred.
	 */
	public Container read(CacheIndex index, int file) throws IOException {
		return read(index.getID(), file);
	}

	/**
	 * Reads a file from the cache.
	 * 
	 * @param type
	 *            The type of file.
	 * @param file
	 *            The file id.
	 * @return The file.
	 * @throws IOException
	 *             if an I/O error occurred.
	 */
	public Container read(int type, int file) throws IOException {
		/* we don't want people reading/manipulating these manually */
		if (type == 255)
			throw new IOException("Reference tables can only be read with the low level FileStore API!");

		/* delegate the call to the file store then decode the container */
		return Container.decode(store.read(type, file));
	}

	/**
	 * Reads a file from the cache.
	 * 
	 * @param type
	 *            The type of file.
	 * @param file
	 *            The file id.
	 * @param keys
	 *            The decryption keys.
	 * @return The file.
	 * @throws IOException
	 *             if an I/O error occurred.
	 */
	public Container read(int type, int file, int[] keys) throws IOException {
		/* we don't want people reading/manipulating these manually */
		if (type == 255)
			throw new IOException("Reference tables can only be read with the low level FileStore API!");

		/* delegate the call to the file store then decode the container */
		return Container.decode(store.read(type, file), keys);
	}

	/**
	 * Reads a file contained in an archive in the cache.
	 * 
	 * @param type
	 *            The type of the file.
	 * @param file
	 *            The archive id.
	 * @param file
	 *            The file within the archive.
	 * @return The file.
	 * @throws IOException
	 *             if an I/O error occurred.
	 */
	public ByteBuffer read(int type, int file, int member) throws IOException {
		/* grab the container and the reference table */
		Container container = read(type, file);
		Container tableContainer = Container.decode(store.read(255, type));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		/* check if the file/member are valid */
		ReferenceTable.Entry entry = table.getEntry(file);
		if (entry == null || member < 0 || member >= entry.capacity())
			throw new FileNotFoundException();

		/* extract the entry from the archive */
		Archive archive = Archive.decode(container.getData(), entry.capacity());
		return archive.getEntry(member);
	}

	/**
	 * Gets a file id from the cache by name
	 * 
	 * @param type
	 *            The type of file.
	 * @param name
	 *            The name of the file
	 * @return The file id.
	 * @throws java.io.IOException
	 */
	public int getFileId(int type, String name) throws IOException {
		if (!identifiers.containsKey(name)) {
			int identifier = Djb2.hash(name);
			Container container = Container.decode(store.read(255, type));
			ReferenceTable table = ReferenceTable.decode(container.getData());
			for (int i = 0; i <= table.capacity(); i++) {
				Entry e = table.getEntry(i);
				if (e == null)
					continue;

				if (e.getIdentifier() == identifier) {
					identifiers.put(name, i);
					break;
				}
			}
		}
		
		Object i = identifiers.get(name);
		return i == null ? -1 : (int) i;
	}

	/**
	 * Writes a file to the cache and updates the {@link ReferenceTable} that it
	 * is associated with.
	 * 
	 * @param type
	 *            The type of file.
	 * @param file
	 *            The file id.
	 * @param container
	 *            The {@link Container} to write.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(int type, int file, Container container) throws IOException {
		write(type, file, container, XTEAManager.NULL_KEYS);
	}
	
	/**
	 * Writes a file to the cache and updates the {@link ReferenceTable} that it
	 * is associated with.
	 * 
	 * @param type
	 *            The type of file.
	 * @param file
	 *            The file id.
	 * @param container
	 *            The {@link Container} to write.
	 * @param keys
	 *            The encryption keys.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(int type, int file, Container container, int[] keys) throws IOException {
		/* we don't want people reading/manipulating these manually */
		if (type == 255)
			throw new IOException("Reference tables can only be modified with the low level FileStore API!");

		/* increment the container's version */
		container.setVersion(container.getVersion()/* + 1 */);

		/* decode the reference table for this index */
		Container tableContainer = Container.decode(store.read(255, type));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		/* grab the bytes we need for the checksum */
		ByteBuffer buffer = container.encode(keys);
		byte[] bytes = new byte[buffer.limit() - 2]; // last two bytes are the
														// version and shouldn't
														// be included
		buffer.mark();
		try {
			buffer.position(0);
			buffer.get(bytes, 0, bytes.length);
		} finally {
			buffer.reset();
		}

		/* calculate the new CRC checksum */
		CRC32 crc = new CRC32();
		crc.update(bytes, 0, bytes.length);

		/* update the version and checksum for this file */
		ReferenceTable.Entry entry = table.getEntry(file);
		if (entry == null) {
			/* create a new entry for the file */
			entry = new ReferenceTable.Entry(file);
			table.putEntry(file, entry);
		}
		entry.setVersion(container.getVersion());
		entry.setCrc((int) crc.getValue());

		/* calculate and update the whirlpool digest if we need to */
		if ((table.getFlags() & ReferenceTable.FLAG_WHIRLPOOL) != 0) {
			byte[] whirlpool = Whirlpool.whirlpool(bytes, 0, bytes.length);
			entry.setWhirlpool(whirlpool);
		}

		/* update the reference table version */
		table.setVersion(table.getVersion()/* + 1 */);

		/* save the reference table */
		tableContainer = new Container(tableContainer.getType(), table.encode());
		store.write(255, type, tableContainer.encode());

		/* save the file itself */
		store.write(type, file, buffer);
	}
	
	/**
	 * Writes a file contained in an archive to the cache.
	 * 
	 * @param type
	 *            The type of file.
	 * @param file
	 *            The id of the archive.
	 * @param member
	 *            The file within the archive.
	 * @param data
	 *            The data to write.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(int type, int file, int member, ByteBuffer data) throws IOException {
		write(type, file, member, data, XTEAManager.NULL_KEYS);
	}

	/**
	 * Writes a file contained in an archive to the cache.
	 * 
	 * @param type
	 *            The type of file.
	 * @param file
	 *            The id of the archive.
	 * @param member
	 *            The file within the archive.
	 * @param data
	 *            The data to write.
	 * @param keys
	 *            The encryption keys.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(int type, int file, int member, ByteBuffer data, int[] keys) throws IOException {
		/* grab the reference table */
		Container tableContainer = Container.decode(store.read(255, type));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		/* create a new entry if necessary */
		ReferenceTable.Entry entry = table.getEntry(file);
		int oldArchiveSize = -1;
		if (entry == null) {
			entry = new ReferenceTable.Entry(file);
			table.putEntry(file, entry);
		} else {
			oldArchiveSize = entry.capacity();
		}

		/* add a child entry if one does not exist */
		ReferenceTable.ChildEntry child = entry.getEntry(member);
		if (child == null) {
			child = new ReferenceTable.ChildEntry(member);
			entry.putEntry(member, child);
		}

		/* extract the current archive into memory so we can modify it */
		Archive archive;
		int containerType, containerVersion;
		if (file < store.getFileCount(type) && oldArchiveSize != -1) {
			Container container = read(type, file);
			containerType = container.getType();
			containerVersion = container.getVersion();
			archive = Archive.decode(container.getData(), oldArchiveSize);
		} else {
			containerType = Container.COMPRESSION_GZIP;
			containerVersion = 1;
			archive = new Archive(member + 1);
		}

		/* expand the archive if it is not large enough */
		if (member >= archive.size()) {
			Archive newArchive = new Archive(member + 1);
			for (int id = 0; id < archive.size(); id++) {
				newArchive.putEntry(id, archive.getEntry(id));
			}
			archive = newArchive;
		}

		/* put the member into the archive */
		archive.putEntry(member, data);

		/* create 'dummy' entries */
		for (int id = 0; id < archive.size(); id++) {
			if (archive.getEntry(id) == null) {
				entry.putEntry(id, new ReferenceTable.ChildEntry(id));
				archive.putEntry(id, ByteBuffer.allocate(1));
			}
		}

		/* write the reference table out again */
		tableContainer = new Container(tableContainer.getType(), table.encode());
		store.write(255, type, tableContainer.encode());

		/* and write the archive back to memory */
		Container container = new Container(containerType, archive.encode(), containerVersion);
		write(type, file, container, keys);
	}
}
