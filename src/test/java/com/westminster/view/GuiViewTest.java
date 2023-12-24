package com.westminster.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuiViewTest {

    @Test
    void start() {
        assertDoesNotThrow(GuiView::start);
    }
}