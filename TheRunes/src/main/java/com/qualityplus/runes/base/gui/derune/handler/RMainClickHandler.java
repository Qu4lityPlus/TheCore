package com.qualityplus.runes.base.gui.derune.handler;

import com.qualityplus.runes.base.gui.ClickHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class RMainClickHandler implements ClickHandler {
    private final ClickHandler normalHandler;
    private final ClickHandler shiftHandler;

    public RMainClickHandler(ClickHandler normalHandler, ClickHandler shiftHandler) {
        this.normalHandler = normalHandler;
        this.shiftHandler = shiftHandler;
    }

    public void handle(InventoryClickEvent event) {
        if (event.isShiftClick())
            shiftHandler.handle(event);
        else
            normalHandler.handle(event);
    }

    @Override
    public void handleOutSide(InventoryClickEvent event) {
        if (event.isShiftClick())
            shiftHandler.handleOutSide(event);
        else
            normalHandler.handleOutSide(event);
    }
}
