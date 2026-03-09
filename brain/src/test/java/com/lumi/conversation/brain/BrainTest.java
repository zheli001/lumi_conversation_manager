/*
 * Lumi Conversation Manager — Brain Module
 * Copyright (C) 2024 Jeff Li and Lumi Conversation Manager Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
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
