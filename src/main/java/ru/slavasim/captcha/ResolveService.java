package ru.slavasim.captcha;

import org.springframework.stereotype.Service;
import ru.slavasim.util.ImageBuilder;
import ru.slavasim.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResolveService {
    public List<Point> resolveCaptchaPoints(String imageDataUrl, List<Rectangle> shapes, int count) {
        List<Integer> nums = resolveCaptchaNums(imageDataUrl, shapes, count);
        return nums.stream()
                .map(i -> new Point((int) shapes.get(i).getCenterX(), (int) shapes.get(i).getCenterY()))
                .collect(Collectors.toList());
    }

    public List<Integer> resolveCaptchaNums(String imageDataUrl, List<Rectangle> shapes, int count) {
        try {
            BufferedImage src = ImageUtils.imageFromDataUrl(imageDataUrl);
            BufferedImage image = new ImageBuilder(src).toBlackWhite().getImage();
            try {
                ImageIO.write(image, "png", new File("image.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<BufferedImage> letters = ImageUtils.lettersFromImage(image, count);
            List<BufferedImage> candidates = shapes.stream()
                    .map(s -> ImageUtils.imageFromShape(image, s))
                    .collect(Collectors.toList());
            List<Integer> nums = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                nums.add(ImageUtils.bestMatch(letters.get(i), candidates));
            }
            return nums;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
