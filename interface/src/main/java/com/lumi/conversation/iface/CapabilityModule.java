/*
 * Lumi Conversation Manager — Capability Interface Module
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
package com.lumi.conversation.iface;

/**
 * Defines the contract for a Lumi capability module.
 * All binary/WASM modules must implement this interface.
 */
public interface CapabilityModule {

    String getName();

    String getVersion();

    String[] getPermissions();

    String execute(String input);
}
