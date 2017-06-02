/*
 * Copyright (C) Kyle Fricilone
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.openrs.cache.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.FileStore;
import net.openrs.cache.region.Location;
import net.openrs.cache.region.Region;
import net.openrs.cache.sprite.Sprite;
import net.openrs.cache.sprite.Sprites;
import net.openrs.cache.sprite.Textures;
import net.openrs.cache.type.TypeListManager;
import net.openrs.cache.type.areas.AreaType;
import net.openrs.cache.type.objects.ObjectType;
import net.openrs.cache.type.overlays.OverlayType;
import net.openrs.cache.type.underlays.UnderlayType;
import net.openrs.cache.util.XTEAManager;

/**
 * Created by Kyle Fricilone on Sep 16, 2016.
 * Optimizations done by Adam
 */
public class MapImageDumper {

	private final List<Region> regions = new ArrayList<>();
	private final List<Integer> flags = new ArrayList<>();
	
	private final Map<Integer, Image> mapIcons = new HashMap<>();
	
	private Region lowestX;
	private Region lowestY;
	private Region highestX;
	private Region highestY;
	
	private static final int MAX_REGION = 32768;
	private static final int MAP_SCALE = 2;
	
	private static final boolean LABEL = true;
	private static final boolean OUTLINE = true;
	private static final boolean FILL = true;
	
	private void initialize(final Cache cache) throws IOException
	{
		TypeListManager.initialize(cache);
		Textures.initialize(cache);
		Sprites.initialize(cache);
		XTEAManager.touch();
		
		for (int i = 0; i < MAX_REGION; i++)
		{
			final Region region = new Region(i);
			
			int map = cache.getFileId(5, region.getTerrainIdentifier());
			int loc = cache.getFileId(5, region.getLocationsIdentifier());
			if (map == -1 && loc == -1)
				continue;
			
			if (map != -1)
			{
				region.loadTerrain(cache.read(5, map).getData());
			}
			
			if (loc != -1)
			{
				try
				{
					region.loadLocations(cache.read(5, loc, XTEAManager.lookupMap(i)).getData());
				}
				
				catch (Exception e)
				{
					flags.add(i);
				}
			}
			
			regions.add(region);

			if (lowestX == null || region.getBaseX() < lowestX.getBaseX())
			{
				lowestX = region;
			}

			if (highestX == null || region.getBaseX() > highestX.getBaseX())
			{
				highestX = region;
			}

			if (lowestY == null || region.getBaseY() < lowestY.getBaseY())
			{
				lowestY = region;
			}

			if (highestY == null || region.getBaseY() > highestY.getBaseY())
			{
				highestY = region;
			}
			
		}
		
		final Sprite mapscene = Sprites.getSprite("mapscene");
		
		for (int i = 0; i < mapscene.size(); i++)
		{
			mapIcons.put(i, mapscene.getFrame(i).getScaledInstance(5, 6, 0));
		}
	}
	
	private void draw() throws IOException
	{
		
		int minX = lowestX.getBaseX();
		int minY = lowestY.getBaseY();

		int maxX = highestX.getBaseX() + 64;
		int maxY = highestY.getBaseY() + 64;

		int dimX = maxX - minX;
		int dimY = maxY - minY;

		int boundX = dimX - 1;
		int boundY = dimY - 1;
		
		dimX *= MAP_SCALE;
		dimY *= MAP_SCALE;
		
		BufferedImage baseImage = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_RGB);
		BufferedImage fullImage = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_RGB);
		
		//Draw Underlay Map - Pass 1
		for (Region region : regions)
		{
			
			int baseX = region.getBaseX();
			int baseY = region.getBaseY();
			int drawBaseX = baseX - lowestX.getBaseX();
			int drawBaseY = highestY.getBaseY() - baseY;
			
			for (int x = 0; x < 64; ++x)
			{
				int drawX = drawBaseX + x;

				for (int y = 0; y < 64; ++y)
				{
					int drawY = drawBaseY + (63 - y);

					int overlayId = region.getOverlayId(0, x, y) - 1;
					int underlayId = region.getUnderlayId(0, x, y) - 1;
					int rgb = 0;
					
					if (overlayId > -1)
					{
						OverlayType overlay = TypeListManager.lookupOver(overlayId);
						if (!overlay.isHideUnderlay() && underlayId > -1)
						{
							UnderlayType underlay = TypeListManager.lookupUnder(underlayId);
							rgb = underlay.getRgbColor();
						}
						
						else
						{
							rgb = Color.CYAN.getRGB();
						}
					}
					
					else if (underlayId > -1)
					{
						UnderlayType underlay = TypeListManager.lookupUnder(underlayId);
						rgb = underlay.getRgbColor();
					}
					
					else
					{
						rgb = Color.CYAN.getRGB();
					}

					drawMapSquare(baseImage, drawX, drawY, rgb);
				}
			}
		}
		
		//Blend Underlay Map - Pass 2
		for (Region region : regions)
		{
			
			int baseX = region.getBaseX();
			int baseY = region.getBaseY();
			int drawBaseX = baseX - lowestX.getBaseX();
			int drawBaseY = highestY.getBaseY() - baseY;
			
			for (int x = 0; x < 64; ++x)
			{
				int drawX = drawBaseX + x;

				for (int y = 0; y < 64; ++y)
				{
					int drawY = drawBaseY + (63 - y);

					Color c = getMapSquare(baseImage, drawX, drawY);

					if (c.equals(Color.CYAN))
						continue;
					
					int tRed = 0, tGreen = 0, tBlue = 0;
					int count = 0;
					
					int maxDY = Math.min(boundY, drawY + 3);
					int maxDX = Math.min(boundX, drawX + 3);
					int minDY = Math.max(0, drawY - 3);
					int minDX = Math.max(0, drawX - 3);
					
					
					for (int dy = minDY; dy < maxDY; dy++)
					{		
						for (int dx = minDX; dx < maxDX; dx++)
						{
							c = getMapSquare(baseImage, dx, dy);

							if (c.equals(Color.CYAN))
								continue;

							tRed += c.getRed();
							tGreen += c.getGreen();
							tBlue += c.getBlue();
							count++;
						}
					}

					if (count > 0)
					{
						c = new Color(tRed / count, tGreen / count, tBlue / count);
						drawMapSquare(fullImage, drawX, drawY, c.getRGB());
					}
				}
			}
		}
		
		//Draw Overlay Map - Pass 3
		for (Region region : regions)
		{
			
			int baseX = region.getBaseX();
			int baseY = region.getBaseY();
			int drawBaseX = baseX - lowestX.getBaseX();
			int drawBaseY = highestY.getBaseY() - baseY;
			
			for (int x = 0; x < 64; ++x)
			{
				int drawX = drawBaseX + x;

				for (int y = 0; y < 64; ++y)
				{
					int drawY = drawBaseY + (63 - y);

					int overlayId = region.getOverlayId(0, x, y) - 1;
					int rgb = -1;
					
					if (overlayId > -1)
					{
						OverlayType overlay = TypeListManager.lookupOver(overlayId);
						if (overlay.isHideUnderlay())
						{
							rgb = overlay.getRgbColor();
						}

						
						if (overlay.getSecondaryRgbColor() > -1)
						{
							rgb = overlay.getSecondaryRgbColor();
						}

						
						if (overlay.getTexture() > -1)
						{
							rgb = Textures.getColors(overlay.getTexture());
						}

					}

					if (rgb > -1)
						drawMapSquare(fullImage, drawX, drawY, rgb);
				}
			}
		}
		
		//Draw Locations Map - Pass 4
		for (Region region : regions)
		{
			
			int baseX = region.getBaseX();
			int baseY = region.getBaseY();
			int drawBaseX = baseX - lowestX.getBaseX();
			int drawBaseY = highestY.getBaseY() - baseY;
			
			Graphics2D graphics = fullImage.createGraphics();
			
			for (Location location : region.getLocations())
			{
				if (location.getPosition().getHeight() != 0)
				{
				//	continue;
				}

				ObjectType objType = TypeListManager.lookupObject(location.getId());

				int localX = location.getPosition().getX() - region.getBaseX();
				int localY = location.getPosition().getY() - region.getBaseY();

				int drawX = drawBaseX + localX;
				int drawY = drawBaseY + (63 - localY);

				if (objType.getMapSceneID() != -1)
				{
					Image spriteImage = mapIcons.get(objType.getMapSceneID());
					graphics.drawImage(spriteImage, drawX * MAP_SCALE, drawY * MAP_SCALE, null);
				}
			}
			
			graphics.dispose();
		}
		
		//Draw Icons Map - Pass 5
		for (Region region : regions)
		{
			
			int baseX = region.getBaseX();
			int baseY = region.getBaseY();
			int drawBaseX = baseX - lowestX.getBaseX();
			int drawBaseY = highestY.getBaseY() - baseY;
			
			Graphics2D graphics = fullImage.createGraphics();
			
			for (Location location : region.getLocations())
			{
				if (location.getPosition().getHeight() != 0)
				{
				//	continue;
				}

				ObjectType objType = TypeListManager.lookupObject(location.getId());

				int localX = location.getPosition().getX() - region.getBaseX();
				int localY = location.getPosition().getY() - region.getBaseY();

				int drawX = drawBaseX + localX;
				int drawY = drawBaseY + (63 - localY);

				if (objType.getMapAreaId() != -1)
				{
					AreaType areaType = TypeListManager.lookupArea(objType.getMapAreaId());
					Image spriteImage = Sprites.getSprite(areaType.getSpriteId()).getFrame(0);
					graphics.drawImage(spriteImage, drawX * MAP_SCALE, drawY * MAP_SCALE, null);
				}
			}
			
			graphics.dispose();
		}
		
		//Label/Outline/Fill regions - Pass 6
		for (Region region : regions)
		{
		
			int baseX = region.getBaseX();
			int baseY = region.getBaseY();
			int drawBaseX = baseX - lowestX.getBaseX();
			int drawBaseY = highestY.getBaseY() - baseY;
			
			Graphics2D graphics = fullImage.createGraphics();

			if (LABEL)
			{
				graphics.setColor(Color.RED);
				graphics.drawString(String.valueOf(region.getRegionID()), drawBaseX * MAP_SCALE, drawBaseY * MAP_SCALE + graphics.getFontMetrics().getHeight());
			}

			if (OUTLINE)
			{
				graphics.setColor(Color.RED);
				graphics.drawRect(drawBaseX * MAP_SCALE, drawBaseY * MAP_SCALE, 64 * MAP_SCALE, 64 * MAP_SCALE);
			}
			
			if (FILL)
			{
				if (flags.contains(region.getRegionID()))
				{
					graphics.setColor(new Color(255, 0, 0, 80));
					graphics.fillRect(drawBaseX * MAP_SCALE, drawBaseY * MAP_SCALE, 64 * MAP_SCALE, 64 * MAP_SCALE);
				}
			}

			graphics.dispose();
			
		}
		
		ImageIO.write(baseImage, "png", new File("base_image.png"));
		ImageIO.write(fullImage, "png", new File("full_image.png"));
	}
	
	private void drawMapSquare(BufferedImage image, int x, int y, int rgb)
	{
		x *= MAP_SCALE;
		y *= MAP_SCALE;

		for (int dx = 0; dx < MAP_SCALE; ++dx)
		{
			for (int dy = 0; dy < MAP_SCALE; ++dy)
			{
				image.setRGB(x + dx, y + dy, rgb);
			}
		}
	}
	
	public Color getMapSquare(BufferedImage image, int x, int y)
	{
		x *= MAP_SCALE;
		y *= MAP_SCALE;
		
		return new Color(image.getRGB(x, y));
	}
	
	public static void main(String[] args)
	{
		long ms = System.currentTimeMillis();
		MapImageDumper dumper = new MapImageDumper();

		try (Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH)))
		{
			dumper.initialize(cache);
			dumper.draw();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - ms));
	}
	
}
