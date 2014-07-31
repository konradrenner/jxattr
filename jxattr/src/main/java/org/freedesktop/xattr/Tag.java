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

import java.util.Objects;
import org.freedesktop.SimpleValue;

/**
 * Represents a comment as defined by freedesktop.org
 *
 * @author Konrad Renner
 */
@AttributeDefinition(name = "tags")
public class Tag extends SimpleValue<String> implements Attribute<String> {

    private final String value;

    public Tag(String val) {
        Objects.requireNonNull(val, "value must not be null");
        this.value = val;
    }

    @Override
    public String getValue() {
        return value;
    }

}
