package ru.slavasim.FifaTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.slavasim.FifaTools.model.ResolveAnswer;
import ru.slavasim.captcha.ResolveService;
import ru.slavasim.model.MapArea;
import ru.slavasim.model.ResolveRequest;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/resolve")
public class ResolveController {
    @Autowired
    private ResolveService resolveService;

    @PostMapping
    @CrossOrigin
    public List<ResolveAnswer> resolve(@RequestBody ResolveRequest resolveRequest) {
        List<Rectangle> rectangles = resolveRequest.getShapes().stream()
                .map(MapArea::asRect)
                .collect(Collectors.toList());
        List<Integer> nums = resolveService.resolveCaptchaNums(resolveRequest.getDataURL(), rectangles, resolveRequest.getCount());
        List<ResolveAnswer> answers = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            Integer num = nums.get(i);
            Point point = new Point((int) rectangles.get(num).getCenterX(), (int) rectangles.get(num).getCenterY());
            String code = resolveRequest.getShapes().get(num).code;
            answers.add(new ResolveAnswer(code, point, num));
        }
        return answers;
    }
}
