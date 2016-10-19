package net.openrs.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageUtils {

	public static BufferedImage horizontalFlip(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage flippedImage = new BufferedImage(w, h, img.getType());
		Graphics2D g = flippedImage.createGraphics();
		g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
		g.dispose();
		return flippedImage;
	}

	public static BufferedImage verticalFlip(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage flippedImage = new BufferedImage(w, h, img.getColorModel().getTransparency());
		Graphics2D g = flippedImage.createGraphics();
		g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
		g.dispose();
		return flippedImage;
	}

	public static BufferedImage getScaledImage(BufferedImage src, double factor) {
		int finalw = src.getWidth();
		int finalh = src.getHeight();
		if (src.getWidth() > src.getHeight())
		{
			factor = ((double) src.getHeight() / (double) src.getWidth());
			finalh = (int) (finalw * factor);
		}

		else
		{
			factor = ((double) src.getWidth() / (double) src.getHeight());
			finalw = (int) (finalh * factor);
		}

		BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(src, 0, 0, finalw, finalh, null);
		g2.dispose();
		return resizedImg;
	}
}