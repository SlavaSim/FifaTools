package ru.slavasim.util;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {

    public static final int BORDER_SIZE = 1;

    public static BufferedImage imageFromDataUrl(String imageDataUrl) throws IOException {
        byte[] imagedata = DatatypeConverter.parseBase64Binary(imageDataUrl.substring(imageDataUrl.indexOf(",") + 1));
        return ImageIO.read(new ByteArrayInputStream(imagedata));
    }

    public static BufferedImage imageFromShape(BufferedImage image, Rectangle shape) {
        return new ImageBuilder(image)
                .crop(shape, 1)
                .invert()
                .trim()
                .getImage();
//        return image.getSubimage(shape.x + BORDER_SIZE, shape.y + BORDER_SIZE, shape.width - 2 * BORDER_SIZE, shape.height - 2 * BORDER_SIZE);
//        return new ImageBuilder(image.getSubimage(shape.x + BORDER_SIZE, shape.y + BORDER_SIZE, shape.width - 2 * BORDER_SIZE, shape.height - 2 * BORDER_SIZE))
//                .invert()
//                .getImage();
    }

    public static List<BufferedImage> lettersFromImage(BufferedImage image, int count) {
        BufferedImage captcha = new ImageBuilder(image)
                .crop(new Rectangle(0, 0, image.getWidth(), 69), 0)
                .trim()
                .squeeze(5)
                .getImage();
        List<BufferedImage> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                ImageIO.write(captcha, "png", new File("captcha" + (i + 1) + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            int avgWidth = captcha.getWidth() / (count - i);
            BufferedImage letter = new ImageBuilder(captcha).letter(2, avgWidth, (int) (avgWidth * 1.5)).trim().getImage();
            captcha = new ImageBuilder(captcha)
                    .crop(new Rectangle(letter.getWidth(), 0, captcha.getWidth() - letter.getWidth(), captcha.getHeight()), 0)
                    .trim()
                    .getImage();
            result.add(letter);
            try {
                ImageIO.write(letter, "png", new File("letter" + (i + 1) + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static int bestMatch(BufferedImage letter, List<BufferedImage> candidates) {
        int minDiff = Integer.MAX_VALUE;
        int result = 0;
        for (int i = 0; i < candidates.size(); i++) {
            int diff = imageDiff(letter, candidates.get(i));
            if (diff < minDiff) {
                minDiff = diff;
                result = i;
            }
        }
        return result;
    }

    private static int imageDiff(BufferedImage letter, BufferedImage candidate) {
        int w = letter.getWidth();
        int h = letter.getHeight();
        int diff = 0;
        BufferedImage image = new ImageBuilder(candidate).resize(w, h).getImage();
        int[] pixels1 = letter.getData().getPixels(0, 0, w, h, (int[]) null);
        int[] pixels2 = image.getData().getPixels(0, 0, w, h, (int[]) null);
        for (int i = 0; i < pixels1.length; i++) {
            diff += pixels1[i] != pixels2[i] ? 1 : 0;
        }
        return diff;
    }

}
