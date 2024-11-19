package com.coms309.nutrifit.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The type Image utils.
 */
public class ImageUtils {

	/**
	 * Compress image byte [ ].
	 *
	 * @param image the image
	 *
	 * @return the byte [ ]
	 */
	public static byte[] compressImage(byte[] image) {
		Deflater deflater = new Deflater();
		deflater.setLevel(Deflater.BEST_COMPRESSION);
		deflater.setInput(image);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(image.length);
		byte[] buffer = new byte[4 * 1024];
		while (!deflater.finished())
		{
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);

		}
		try
		{
			outputStream.close();
		}
		catch (IOException e)
		{

		}
		return outputStream.toByteArray();
	}

	/**
	 * Decompress image byte [ ].
	 *
	 * @param image the image
	 *
	 * @return the byte [ ]
	 */
	public static byte[] decompressImage(byte[] image) {
		Inflater inflater = new Inflater();
		inflater.setInput(image);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(image.length);
		byte[] buffer = new byte[4 * 1024];
		try
		{
			while (!inflater.finished())
			{

				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
		}
		catch (Exception e)
		{

		}

		return outputStream.toByteArray();
	}

}
