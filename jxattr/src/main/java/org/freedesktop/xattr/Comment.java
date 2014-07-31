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
@AttributeDefinition(name = "comment")
public class Comment extends SimpleValue<String> implements Attribute<String> {

    private final String value;

    public Comment(String value) {
        Objects.requireNonNull(value, "value must not be null");
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(getName());
        hash = 31 * hash + Objects.hashCode(getNamespace());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (!Objects.equals(getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(getNamespace(), other.getNamespace())) {
            return false;
        }
        return true;
    }
}
