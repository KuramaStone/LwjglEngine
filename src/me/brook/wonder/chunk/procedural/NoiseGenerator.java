package me.brook.wonder.chunk.procedural;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public abstract class NoiseGenerator {

	public abstract float getHeightAt(float x, float z);

	protected float interpolateLinear(float i, float j, float a) {
		return i + a * (j - i);
	}

	protected float interpolateCosine(float i, float j, float a) {
		float mu2 = (float) ((1 - Math.cos(a * Math.PI)) / 2);
		return (i * (1 - mu2) + j * mu2);
	}

	public BufferedImage drawImage(int width, int height, String file) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(int x = 0; x < width; x++) {
			for(int z = 0; z < width; z++) {
				float c = getHeightAt(x, z);
				Color color = new Color(c, c, c);
				image.setRGB(x, z, color.getRGB());
			}
		}

		File f = new File(file);

		try {
			f.getParentFile().mkdirs();
			ImageIO.write(image, "png", f);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return image;
	}

}
