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

/**
 *
 * @author Konrad Renner
 */
public class AttributeID implements Comparable<AttributeID> {

    private String name;
    private String namespace;

    AttributeID() {
    }

    AttributeID(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    public static WithName newInstance() {
        return new Builder();
    }


    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    @Override
    public int compareTo(AttributeID o) {
        return (namespace + name).compareTo(o.getNamespace() + o.getName());
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.namespace);
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
        final AttributeID other = (AttributeID) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.namespace, other.namespace)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AttributeID{" + "name=" + name + ", namespace=" + namespace + '}';
    }

    public interface WithName {

        WithNamespace name(String name);
    }

    public interface WithNamespace {

        AttributeIDBuilder namespace(String namespace);
    }

    public interface AttributeIDBuilder {

        AttributeID build();
    }

    static class Builder implements WithName, WithNamespace, AttributeIDBuilder {

        private final AttributeID item;

        private Builder() {
            this.item = new AttributeID();
        }

        @Override
        public Builder name(final String name) {
            this.item.name = name;
            return this;
        }

        @Override
        public Builder namespace(final String namespace) {
            this.item.namespace = namespace;
            return this;
        }

        @Override
        public AttributeID build() {
            Objects.requireNonNull(this.item.name, "name must not be null");
            Objects.requireNonNull(this.item.namespace, "namespace must not be null");
            return this.item;
        }
    }

}
