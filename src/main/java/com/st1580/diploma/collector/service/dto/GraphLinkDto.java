package com.st1580.diploma.collector.service.dto;

import java.util.Objects;

public class GraphLinkDto {
    private final GraphEntityDto fromGraphEntityDto;

    private final GraphEntityDto toGraphEntityDto;

    public GraphLinkDto(GraphEntityDto fromGraphEntityDto, GraphEntityDto toGraphEntityDto) {
        this.fromGraphEntityDto = fromGraphEntityDto;
        this.toGraphEntityDto = toGraphEntityDto;
    }

    public GraphEntityDto getFromGraphEntity() {
        return fromGraphEntityDto;
    }

    public GraphEntityDto getToGraphEntity() {
        return toGraphEntityDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphLinkDto graphLinkDto = (GraphLinkDto) o;
        return Objects.equals(fromGraphEntityDto, graphLinkDto.fromGraphEntityDto) && Objects.equals(toGraphEntityDto, graphLinkDto.toGraphEntityDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromGraphEntityDto, toGraphEntityDto);
    }
}
