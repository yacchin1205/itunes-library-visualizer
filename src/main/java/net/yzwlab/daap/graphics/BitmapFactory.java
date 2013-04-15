package net.yzwlab.daap.graphics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class BitmapFactory {

	public static Bitmap decodeByteArray(byte[] data, int offset, int length)
			throws IOException {
		if (data == null) {
			throw new IllegalArgumentException();
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		bout.write(data, offset, length);

		return new Bitmap(ImageIO.read(new ByteArrayInputStream(bout
				.toByteArray())));
	}

	private BitmapFactory() {
		;
	}

}
