package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class ImageToAsciiConverter implements TextGraphicsConverter {

    private int width;
    private int height;
    private double maxRatio;
    private TextColorSchema schema = new AsciiColorSchema();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        double ratio = (double) img.getWidth() / img.getHeight();
        if (maxRatio > 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        double scale = 1.0;

        if (width > 0 && newWidth > width) {
            scale = Math.min(scale, (double) width / newWidth);
        }
        if (height > 0 && newHeight > height) {
            scale = Math.min(scale, (double) height / newHeight);
        }

        if (scale < 1.0) {
            newWidth = (int) Math.round(newWidth * scale);
            newHeight = (int) Math.round(newHeight * scale);
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder sb = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                sb.append(c).append(c);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
