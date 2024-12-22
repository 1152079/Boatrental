package com.example.boatrental.controllers;

import com.example.boatrental.dtos.ActionDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Map;
import java.util.stream.Collectors;

public class CustomResponse<T> {
    private final T data;
    private final Map<String, ActionDto> actions;
    private final Map<String, String> links;
    public CustomResponse(EntityModel<T> resource) {
        this.data = resource.getContent();
        this.actions = resource.getLinks().stream()
                .filter(link -> link.getRel().value().equals("update") || link.getRel().value().equals("delete"))
                .collect(Collectors.toMap(link -> link.getRel().value(), link -> {
                    if (link.getRel().value().equals("delete")) {
                        return new ActionDto(link.getHref(), "DELETE", null);
                    } else {
                        return new ActionDto(link.getHref(), "PUT", "application/json");
                    }
                }));
        this.links = resource.getLinks().stream()
                .filter(link -> !link.getRel().value().equals("update") && !link.getRel().value().equals("delete"))
                .collect(Collectors.toMap(link -> link.getRel().value(), Link::getHref));
    }
    public T getData() {
        return data;
    }
    public Map<String, ActionDto> getActions() {
        return actions;
    }
    public Map<String, String> getLinks() {
        return links;
    }
}
