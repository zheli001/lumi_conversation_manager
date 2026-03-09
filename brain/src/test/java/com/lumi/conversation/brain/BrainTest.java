package com.lumi.conversation.brain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrainTest {

    @Test
    void processReturnsInput() {
        Brain brain = new Brain();
        assertEquals("hello", brain.process("hello"));
    }
}
