package net.yzwlab.daap.graphics;

import java.awt.image.BufferedImage;

public class Bitmap {

	private BufferedImage image;

	public Bitmap(BufferedImage image) {
		if (image == null) {
			throw new IllegalArgumentException();
		}
		this.image = image;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public BufferedImage getAsImage() {
		return image;
	}

}
