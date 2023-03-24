package com.st1580.diploma.collector.service.dto;

import java.util.Map;
import java.util.Objects;

public class GraphLinkDto {
    private final LinkEndDto fromGraphEntityDto;
    private final LinkEndDto toGraphEntityDto;
    private final Map<String, String> payload;

    public GraphLinkDto(LinkEndDto fromGraphEntityDto,
                        LinkEndDto toGraphEntityDto,
                        Map<String, String> payload) {
        this.fromGraphEntityDto = fromGraphEntityDto;
        this.toGraphEntityDto = toGraphEntityDto;
        this.payload = payload;
    }

    public LinkEndDto getFromGraphEntity() {
        return fromGraphEntityDto;
    }

    public LinkEndDto getToGraphEntity() {
        return toGraphEntityDto;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GraphLinkDto that = (GraphLinkDto) o;
        return Objects.equals(fromGraphEntityDto, that.fromGraphEntityDto) && Objects.equals(toGraphEntityDto,
                that.toGraphEntityDto) && Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromGraphEntityDto, toGraphEntityDto, payload);
    }
}
