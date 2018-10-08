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

import net.openrs.cache.Cache;
import net.openrs.cache.Constants;
import net.openrs.cache.Container;
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
import net.openrs.util.BigBufferedImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kyle Fricilone on Sep 16, 2016.
 * Optimizations done by Adam
 * Updated by Explv on October 02, 2018
 */
public class MapImageDumper {

    private static int[][] TILE_SHAPES = new int[][]{
            {
                    1, 1, 1, 1,
                    1, 1, 1, 1,
                    1, 1, 1, 1,
                    1, 1, 1, 1
            },
            {
                    1, 0, 0, 0,
                    1, 1, 0, 0,
                    1, 1, 1, 0,
                    1, 1, 1, 1
            },

            {
                    1, 1, 0, 0,
                    1, 1, 0, 0,
                    1, 0, 0, 0,
                    1, 0, 0, 0
            },

            {
                    0, 0, 1, 1,
                    0, 0, 1, 1,
                    0, 0, 0, 1,
                    0, 0, 0, 1,
            },
            {
                    0, 1, 1, 1,
                    0, 1, 1, 1,
                    1, 1, 1, 1,
                    1, 1, 1, 1
            },
            {
                    1, 1, 1, 0,
                    1, 1, 1, 0,
                    1, 1, 1, 1,
                    1, 1, 1, 1
            },
            {
                    1, 1, 0, 0,
                    1, 1, 0, 0,
                    1, 1, 0, 0,
                    1, 1, 0, 0
            },
            {
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    1, 0, 0, 0,
                    1, 1, 0, 0
            },
            {
                    1, 1, 1, 1,
                    1, 1, 1, 1,
                    0, 1, 1, 1,
                    0, 0, 1, 1
            },
            {
                    1, 1, 1, 1,
                    1, 1, 0, 0,
                    1, 0, 0, 0,
                    1, 0, 0, 0
            },

            {
                    0, 0, 0, 0,
                    0, 0, 1, 1,
                    0, 1, 1, 1,
                    0, 1, 1, 1
            },

            {
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 1, 1, 0,
                    1, 1, 1, 1
            }
    };

    private static int[][] TILE_ROTATIONS = new int[][]{
            {
                    0, 1, 2, 3,
                    4, 5, 6, 7,
                    8, 9, 10, 11,
                    12, 13, 14, 15
            },

            {
                    12, 8, 4, 0,
                    13, 9, 5, 1,
                    14, 10, 6, 2,
                    15, 11, 7, 3
            },

            {
                    15, 14, 13, 12,
                    11, 10, 9, 8,
                    7, 6, 5, 4,
                    3, 2, 1, 0
            },

            {
                    3, 7, 11, 15,
                    2, 6, 10, 14,
                    1, 5, 9, 13,
                    0, 4, 8, 12
            }
    };

    private final List<Region> regions = new ArrayList<>();
    private final List<Integer> flags = new ArrayList<>();

    private final Map<Integer, Image> mapIcons = new HashMap<>();

    private Region lowestX;
    private Region lowestY;
    private Region highestX;
    private Region highestY;

    private static final int MAX_REGION = 32768;
    private static final int PIXELS_PER_TILE = 4;

    private static final boolean DRAW_WALLS = true;
    private static final boolean DRAW_ICONS = true;
    private static final boolean DRAW_REGIONS = true;
    private static final boolean LABEL = true;
    private static final boolean OUTLINE = true;
    private static final boolean FILL = true;

    private static final int Z_MIN = 0, Z_MAX = 3;

    private void initialize(final Cache cache) throws IOException {
        TypeListManager.initialize(cache);
        Textures.initialize(cache);
        Sprites.initialize(cache);
        XTEAManager.touch();

        for (int i = 0; i < MAX_REGION; i++) {
            final Region region = new Region(i);

            int map = cache.getFileId(5, region.getTerrainIdentifier());
            int loc = cache.getFileId(5, region.getLocationsIdentifier());

            if (map == -1 && loc == -1) {
                continue;
            }

            if (map != -1) {
                region.loadTerrain(cache.read(5, map).getData());
            }

            if (loc != -1) {
                ByteBuffer buffer = cache.getStore().read(5, loc);
                try {
                    region.loadLocations(Container.decode(buffer, XTEAManager.lookupMap(i)).getData());
                } catch (Exception e) {
                    if (buffer.limit() != 32) {
                        flags.add(i);
                    }
                }
            }

            regions.add(region);

            if (lowestX == null || region.getBaseX() < lowestX.getBaseX()) {
                lowestX = region;
            }

            if (highestX == null || region.getBaseX() > highestX.getBaseX()) {
                highestX = region;
            }

            if (lowestY == null || region.getBaseY() < lowestY.getBaseY()) {
                lowestY = region;
            }

            if (highestY == null || region.getBaseY() > highestY.getBaseY()) {
                highestY = region;
            }

        }

        final Sprite mapscene = Sprites.getSprite("mapscene");

        for (int i = 0; i < mapscene.size(); i++) {
            mapIcons.put(i, mapscene.getFrame(i));
        }
    }

    private void draw() throws IOException {
        int minX = lowestX.getBaseX();
        int minY = lowestY.getBaseY();

        int maxX = highestX.getBaseX() + Region.WIDTH;
        int maxY = highestY.getBaseY() + Region.HEIGHT;

        int dimX = maxX - minX;
        int dimY = maxY - minY;

        int boundX = dimX - 1;
        int boundY = dimY - 1;

        dimX *= PIXELS_PER_TILE;
        dimY *= PIXELS_PER_TILE;

        for (int z = Z_MIN; z <= Z_MAX; z++) {
            System.out.println("Generating map images for z = " + z);

            BufferedImage baseImage = BigBufferedImage.create(dimX, dimY, BufferedImage.TYPE_INT_RGB);
            BufferedImage fullImage = BigBufferedImage.create(dimX, dimY, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics = fullImage.createGraphics();

            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            System.out.println("Drawing underlay");
            drawUnderlay(z, baseImage);

            System.out.println("Blending underlay");
            blendUnderlay(z, baseImage, fullImage, boundX, boundY);

            System.out.println("Drawing overlay");
            drawOverlay(z, fullImage);

            System.out.println("Drawing locations");
            drawLocations(z, graphics);

            if (DRAW_WALLS) {
                System.out.println("Drawing walls");
                drawWalls(z, graphics);
            }

            if (DRAW_ICONS) {
                System.out.println("Drawing icons");
                drawIcons(z, graphics);
            }

            if (DRAW_REGIONS) {
                System.out.println("Drawing regions");
                drawRegions(z, graphics);
            }

            graphics.dispose();

            System.out.println("Writing to files");
            ImageIO.write(baseImage, "png", new File("base_image_" + z + ".png"));
            ImageIO.write(fullImage, "png", new File("full_image_" + z + ".png"));
        }
    }

    private void drawUnderlay(int z, BufferedImage image) {
        for (Region region : regions) {
            int drawBaseX = region.getBaseX() - lowestX.getBaseX();
            int drawBaseY = highestY.getBaseY() - region.getBaseY();

            for (int x = 0; x < Region.WIDTH; ++x) {
                int drawX = drawBaseX + x;

                for (int y = 0; y < Region.HEIGHT; ++y) {
                    int drawY = drawBaseY + ((Region.HEIGHT - 1) - y);

                    int underlayId = region.getUnderlayId(z, x, y) - 1;

                    int rgb = Color.CYAN.getRGB();

                    if (underlayId > -1) {
                        UnderlayType underlay = TypeListManager.lookupUnder(underlayId);
                        rgb = underlay.getRgbColor();
                    }

                    drawMapSquare(image, drawX, drawY, rgb, -1, -1);
                }
            }
        }
    }

    private void blendUnderlay(int z, BufferedImage baseImage, BufferedImage fullImage, int boundX, int boundY) {
        for (Region region : regions) {
            int drawBaseX = region.getBaseX() - lowestX.getBaseX();
            int drawBaseY = highestY.getBaseY() - region.getBaseY();

            for (int x = 0; x < Region.WIDTH; ++x) {
                int drawX = drawBaseX + x;

                for (int y = 0; y < Region.HEIGHT; ++y) {
                    int drawY = drawBaseY + ((Region.HEIGHT - 1) - y);

                    Color c = getMapSquare(baseImage, drawX, drawY);

                    if (c.equals(Color.CYAN)) {
                        continue;
                    }

                    int tRed = 0, tGreen = 0, tBlue = 0;
                    int count = 0;

                    int maxDY = Math.min(boundY, drawY + 3);
                    int maxDX = Math.min(boundX, drawX + 3);
                    int minDY = Math.max(0, drawY - 3);
                    int minDX = Math.max(0, drawX - 3);


                    for (int dy = minDY; dy < maxDY; dy++) {
                        for (int dx = minDX; dx < maxDX; dx++) {
                            c = getMapSquare(baseImage, dx, dy);

                            if (c.equals(Color.CYAN)) {
                                continue;
                            }

                            tRed += c.getRed();
                            tGreen += c.getGreen();
                            tBlue += c.getBlue();
                            count++;
                        }
                    }

                    if (count > 0) {
                        c = new Color(tRed / count, tGreen / count, tBlue / count);
                        drawMapSquare(fullImage, drawX, drawY, c.getRGB(), -1, -1);
                    }
                }
            }
        }
    }

    private void drawOverlay(int z, BufferedImage image) {
        for (Region region : regions) {
            int drawBaseX = region.getBaseX() - lowestX.getBaseX();
            int drawBaseY = highestY.getBaseY() - region.getBaseY();

            for (int x = 0; x < Region.WIDTH; ++x) {
                int drawX = drawBaseX + x;

                for (int y = 0; y < Region.HEIGHT; ++y) {
                    int drawY = drawBaseY + ((Region.HEIGHT - 1) - y);

                    if (z == 0 || (!region.isLinkedBelow(z, x, y) && !region.isVisibleBelow(z, x, y))) {
                        int overlayId = region.getOverlayId(z, x, y) - 1;
                        if (overlayId > -1) {
                            int rgb = getOverLayColour(overlayId);
                            drawMapSquare(image, drawX, drawY, rgb, region.getOverlayPath(z, x, y), region.getOverlayRotation(z, x, y));
                        }
                    }

                    if (z < 3 && (region.isLinkedBelow(z + 1, x, y) || region.isVisibleBelow(z + 1, x, y))) {
                        int overlayAboveId = region.getOverlayId(z + 1, x, y) - 1;
                        if (overlayAboveId > -1) {
                            int rgb = getOverLayColour(overlayAboveId);
                            drawMapSquare(image, drawX, drawY, rgb, region.getOverlayPath(z + 1, x, y), region.getOverlayRotation(z + 1, x, y));
                        }
                    }
                }
            }
        }
    }

    private int getOverLayColour(int overlayID) {
        OverlayType overlay = TypeListManager.lookupOver(overlayID);

        int rgb = 0;

        if (overlay.isHideUnderlay()) {
            rgb = overlay.getRgbColor();
        }


        if (overlay.getSecondaryRgbColor() > -1) {
            rgb = overlay.getSecondaryRgbColor();
        }


        if (overlay.getTexture() > -1) {
            rgb = Textures.getColors(overlay.getTexture());
        }

        return rgb;
    }

    private void drawLocations(int z, Graphics2D graphics) {
        for (Region region : regions) {
            int drawBaseX = region.getBaseX() - lowestX.getBaseX();
            int drawBaseY = highestY.getBaseY() - region.getBaseY();

            for (Location location : region.getLocations()) {
                int localX = location.getPosition().getX() - region.getBaseX();
                int localY = location.getPosition().getY() - region.getBaseY();

                if (!canDrawLocation(region, location, z, localX, localY)) {
                    continue;
                }

                ObjectType objType = TypeListManager.lookupObject(location.getId());

                int drawX = drawBaseX + localX;
                int drawY = drawBaseY + ((Region.HEIGHT - 1) - localY);

                if (objType.getMapSceneID() != -1) {
                    Image spriteImage = mapIcons.get(objType.getMapSceneID());
                    graphics.drawImage(spriteImage, drawX * PIXELS_PER_TILE, drawY * PIXELS_PER_TILE, null);
                }
            }
        }
    }

    private void drawWalls(int z, Graphics2D graphics) {
        for (Region region : regions) {
            int drawBaseX = region.getBaseX() - lowestX.getBaseX();
            int drawBaseY = highestY.getBaseY() - region.getBaseY();

            for (Location location : region.getLocations()) {
                graphics.setColor(Color.WHITE);

                int localX = location.getPosition().getX() - region.getBaseX();
                int localY = location.getPosition().getY() - region.getBaseY();

                if (!canDrawLocation(region, location, z, localX, localY)) {
                    continue;
                }

                ObjectType objType = TypeListManager.lookupObject(location.getId());

                // Don't draw walls on water
                if (objType.getMapSceneID() == 22) {
                    continue;
                }

                String objName = objType.getName().toLowerCase();

                if (objName.contains("door") || objName.contains("gate")) {
                    graphics.setColor(Color.RED);
                }

                int drawX = drawBaseX + localX;
                int drawY = drawBaseY + ((Region.HEIGHT - 1) - localY);

                drawX *= PIXELS_PER_TILE;
                drawY *= PIXELS_PER_TILE;

                if (location.getType() == 0) { // Straight walls
                    if (location.getOrientation() == 0) { // West
                        graphics.drawLine(drawX, drawY, drawX, drawY + PIXELS_PER_TILE);
                    } else if (location.getOrientation() == 1) { // South
                        graphics.drawLine(drawX, drawY, drawX + PIXELS_PER_TILE, drawY);
                    } else if (location.getOrientation() == 2) { // East
                        graphics.drawLine(drawX + PIXELS_PER_TILE, drawY, drawX + PIXELS_PER_TILE, drawY + PIXELS_PER_TILE);
                    } else if (location.getOrientation() == 3) { // North
                        graphics.drawLine(drawX, drawY + PIXELS_PER_TILE, drawX + PIXELS_PER_TILE, drawY + PIXELS_PER_TILE);
                    }
                } else if (location.getType() == 2) { // Corner walls
                    if (location.getOrientation() == 0) { // West & South
                        graphics.drawLine(drawX, drawY, drawX, drawY + PIXELS_PER_TILE);
                        graphics.drawLine(drawX, drawY, drawX + PIXELS_PER_TILE, drawY);
                    } else if (location.getOrientation() == 1) { // South & East
                        graphics.drawLine(drawX, drawY, drawX + PIXELS_PER_TILE, drawY);
                        graphics.drawLine(drawX + PIXELS_PER_TILE, drawY, drawX + PIXELS_PER_TILE, drawY + PIXELS_PER_TILE);
                    } else if (location.getOrientation() == 2) { // East & North
                        graphics.drawLine(drawX + PIXELS_PER_TILE, drawY, drawX + PIXELS_PER_TILE, drawY + PIXELS_PER_TILE);
                        graphics.drawLine(drawX, drawY + PIXELS_PER_TILE, drawX + PIXELS_PER_TILE, drawY + PIXELS_PER_TILE);
                    } else if (location.getOrientation() == 3) { // North & West
                        graphics.drawLine(drawX, drawY + PIXELS_PER_TILE, drawX + PIXELS_PER_TILE, drawY + PIXELS_PER_TILE);
                        graphics.drawLine(drawX, drawY, drawX, drawY + PIXELS_PER_TILE);
                    }
                } else if (location.getType() == 3) { // Single points
                    if (location.getOrientation() == 0) { // West
                        graphics.drawLine(drawX, drawY + 1, drawX, drawY + 1);
                    } else if (location.getOrientation() == 1) { // South
                        graphics.drawLine(drawX + 3, drawY + 1, drawX + 3, drawY + 1);
                    } else if (location.getOrientation() == 2) { // East
                        graphics.drawLine(drawX + 3, drawY + 4, drawX + 3, drawY + 4);
                    } else if (location.getOrientation() == 3) { // North
                        graphics.drawLine(drawX, drawY + 3, drawX, drawY + 3);
                    }
                } else if (location.getType() == 9) { // Diagonal walls
                    if (location.getOrientation() == 0 || location.getOrientation() == 2) { // West or East
                        graphics.drawLine(drawX, drawY + PIXELS_PER_TILE, drawX + PIXELS_PER_TILE, drawY);
                    } else if (location.getOrientation() == 1 || location.getOrientation() == 3) { // South or South
                        graphics.drawLine(drawX, drawY, drawX + PIXELS_PER_TILE, drawY + PIXELS_PER_TILE);
                    }
                }
            }
        }
    }

    private void drawIcons(int z, Graphics2D graphics) {
        for (Region region : regions) {
            int drawBaseX = region.getBaseX() - lowestX.getBaseX();
            int drawBaseY = highestY.getBaseY() - region.getBaseY();

            for (Location location : region.getLocations()) {
                int localX = location.getPosition().getX() - region.getBaseX();
                int localY = location.getPosition().getY() - region.getBaseY();

                if (!canDrawLocation(region, location, z, localX, localY)) {
                    continue;
                }

                ObjectType objType = TypeListManager.lookupObject(location.getId());

                int drawX = drawBaseX + localX;
                int drawY = drawBaseY + (63 - localY);

                if (objType.getMapAreaId() != -1) {
                    AreaType areaType = TypeListManager.lookupArea(objType.getMapAreaId());
                    Image spriteImage = Sprites.getSprite(areaType.getSpriteId()).getFrame(0);
                    graphics.drawImage(spriteImage, (drawX - 1) * PIXELS_PER_TILE, (drawY - 1) * PIXELS_PER_TILE, null);
                }
            }
        }
    }

    private boolean canDrawLocation(Region region, Location location, int z, int x, int y) {
        if (region.isLinkedBelow(z, x, y) || region.isVisibleBelow(z, x, y)) {
            return false;
        }

        if (location.getPosition().getHeight() == z + 1 && (region.isLinkedBelow(z + 1, x, y) || region.isVisibleBelow(z + 1, x, y))) {
            return true;
        }

        return z == location.getPosition().getHeight();
    }

    private void drawRegions(int z, Graphics2D graphics) {
        for (Region region : regions) {
            int baseX = region.getBaseX();
            int baseY = region.getBaseY();
            int drawBaseX = baseX - lowestX.getBaseX();
            int drawBaseY = highestY.getBaseY() - baseY;

            if (LABEL) {
                graphics.setColor(Color.RED);
                graphics.drawString(String.valueOf(region.getRegionID()), drawBaseX * PIXELS_PER_TILE, drawBaseY * PIXELS_PER_TILE + graphics.getFontMetrics().getHeight());
            }

            if (OUTLINE) {
                graphics.setColor(Color.RED);
                graphics.drawRect(drawBaseX * PIXELS_PER_TILE, drawBaseY * PIXELS_PER_TILE, 64 * PIXELS_PER_TILE, 64 * PIXELS_PER_TILE);
            }

            if (FILL) {
                if (flags.contains(region.getRegionID())) {
                    graphics.setColor(new Color(255, 0, 0, 80));
                    graphics.fillRect(drawBaseX * PIXELS_PER_TILE, drawBaseY * PIXELS_PER_TILE, 64 * PIXELS_PER_TILE, 64 * PIXELS_PER_TILE);
                }
            }
        }
    }

    private void drawMapSquare(BufferedImage image, int x, int y, int overlayRGB, int shape, int rotation) {
        if (shape > -1) {
            int[] shapeMatrix = TILE_SHAPES[shape];
            int[] rotationMatrix = TILE_ROTATIONS[rotation & 0x3];
            int shapeIndex = 0;
            for (int tilePixelY = 0; tilePixelY < PIXELS_PER_TILE; tilePixelY++) {
                for (int tilePixelX = 0; tilePixelX < PIXELS_PER_TILE; tilePixelX++) {
                    int drawx = x * PIXELS_PER_TILE + tilePixelX;
                    int drawy = y * PIXELS_PER_TILE + tilePixelY;

                    if (shapeMatrix[rotationMatrix[shapeIndex++]] != 0) {
                        image.setRGB(drawx, drawy, overlayRGB);
                    }
                }
            }
        } else {
            for (int tilePixelY = 0; tilePixelY < PIXELS_PER_TILE; tilePixelY++) {
                for (int tilePixelX = 0; tilePixelX < PIXELS_PER_TILE; tilePixelX++) {
                    int drawx = x * PIXELS_PER_TILE + tilePixelX;
                    int drawy = y * PIXELS_PER_TILE + tilePixelY;
                    image.setRGB(drawx, drawy, overlayRGB);
                }
            }
        }
    }

    public Color getMapSquare(BufferedImage image, int x, int y) {
        x *= PIXELS_PER_TILE;
        y *= PIXELS_PER_TILE;

        return new Color(image.getRGB(x, y));
    }

    public static void main(String[] args) {
        long ms = System.currentTimeMillis();
        MapImageDumper dumper = new MapImageDumper();

        try (Cache cache = new Cache(FileStore.open(Constants.CACHE_PATH))) {
            dumper.initialize(cache);
            dumper.draw();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Time taken: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - ms) + "s");
    }

}
