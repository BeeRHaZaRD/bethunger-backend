package com.hg.bethunger.scheduler;

import com.hg.bethunger.model.PlannedEvent;
import com.hg.bethunger.model.enums.PlannedEventStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class StartEventTask implements Runnable {
    private final PlannedEvent plannedEvent;
    private final Map<Long, ScheduledFuture<?>> scheduledEvents;

    public StartEventTask(PlannedEvent plannedEvent, Map<Long, ScheduledFuture<?>> scheduledEvents) {
        this.plannedEvent = plannedEvent;
        this.scheduledEvents = scheduledEvents;
    }

    @Override
    public void run() {
        plannedEvent.setStatus(PlannedEventStatus.REQUESTED);
        System.out.println("Start event: " + plannedEvent.getEventType().getName() + " at " + LocalDateTime.now());
        scheduledEvents.remove(plannedEvent.getId());
    }
}
