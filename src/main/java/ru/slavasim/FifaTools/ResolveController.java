package ru.slavasim.FifaTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slavasim.captcha.ResolveService;
import ru.slavasim.model.MapArea;
import ru.slavasim.model.ResolveRequest;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/resolve")
public class ResolveController {
    @Autowired
    private ResolveService resolveService;

    @PostMapping
    public List<Integer> resolve(@RequestBody ResolveRequest resolveRequest) {
        List<Rectangle> rectangles = resolveRequest.getShapes().stream()
                .map(MapArea::asRect)
                .collect(Collectors.toList());
        return resolveService.resolveCaptchaNums(resolveRequest.getDataURL(), rectangles, resolveRequest.getCount());
    }
}
