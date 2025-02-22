package com.qualityplus.dragon.base.events;

import com.qualityplus.dragon.api.event.DragonGameEvent;
import com.qualityplus.dragon.api.event.SwitchableEvents;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public final class SwitchableEventImpl implements SwitchableEvents {
    private final Map<Integer, DragonGameEvent> events;
    private DragonGameEvent currentEvent;

    @Override
    public DragonGameEvent getNext() {
        if (this.currentEvent == null) {
            this.currentEvent = this.events.getOrDefault(1, null);
        } else {
            final Optional<Integer> next = getCurrentPosEvent();

            next.ifPresent(this::setupNextEvent);
        }
        return this.currentEvent;
    }

    @Override
    public DragonGameEvent getCurrentEvent() {
        return currentEvent;
    }

    private Optional<Integer> getCurrentPosEvent() {
        return events.keySet()
                .stream()
                .filter(value -> this.events.get(value) == currentEvent)
                .findFirst();
    }

    private void setupNextEvent(final Integer currentEvent) {
        final int newValue = currentEvent + 1;

        if (this.events.containsKey(newValue)) {
            this.currentEvent = this.events.get(newValue);
        } else {
            this.currentEvent = this.events.getOrDefault(1, null);
        }
    }
}
