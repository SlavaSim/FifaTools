package ru.slavasim.FifaTools;

import com.google.common.io.Resources;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import ru.slavasim.model.MapArea;
import ru.slavasim.model.ResolveRequest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FifaToolsApplication.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
public class FifaToolsApplicationTests {
    private static final String API_ROOT = "http://localhost:8080/rest/resolve";

    @Test
    public void contextLoads() throws IOException {
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(getTestRequest())
                .post(API_ROOT);
        System.out.println(response);
    }

    private ResolveRequest getTestRequest() throws IOException {
        ResolveRequest req = new ResolveRequest();
        File file = new File(Resources.getResource("image.txt").getFile());
        String dataURL = FileUtils.readFileToString(file, Charset.defaultCharset());

        File shapesFile = new File(Resources.getResource("shapes.txt").getFile());
        List<String> lines = FileUtils.readLines(shapesFile, Charset.defaultCharset());
        List<MapArea> shapes = new ArrayList<>();
        for (String line : lines) {
            String[] coords = line.split(",");
            MapArea area = new MapArea();
            area.x1 = Integer.parseInt(coords[0]);
            area.y1 = Integer.parseInt(coords[1]);
            area.x2 = Integer.parseInt(coords[2]);
            area.y2 = Integer.parseInt(coords[3]);
            shapes.add(area);
        }

        req.setDataURL(dataURL);
        req.setShapes(shapes);
        req.setCount(4);
        return req;
    }

}
