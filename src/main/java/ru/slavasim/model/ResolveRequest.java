package ru.slavasim.model;

import lombok.Data;

import java.util.List;

@Data
public class ResolveRequest {
    private String dataURL;
    private List<MapArea> shapes;
    private int count;
}
