package com.hg.bethunger.controller;

import com.hg.bethunger.service.EventService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
}
