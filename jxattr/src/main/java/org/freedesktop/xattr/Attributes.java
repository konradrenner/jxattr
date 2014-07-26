/*
 * Copyright (C) 2014 Konrad Renner.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package org.freedesktop.xattr;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Konrad Renner
 */
public class Attributes {

    private final Path pathToFile;

    public Attributes(Path pathToFile) {
        Objects.requireNonNull(pathToFile, "path to file must be not null");
        this.pathToFile = pathToFile;
    }


    public <T> Map<String, Attribute<T>> getAttributes() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <T> Attribute<T> getAttribute(String name, Class<T> clazz) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <T> Attribute<T> getAttribute(String name) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void setAttributes(Map<String, Attribute<?>> attr) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void setAttribute(Attribute<?> attr) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
