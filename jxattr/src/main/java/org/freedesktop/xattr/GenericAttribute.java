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
public class GenericAttribute implements Attribute<String> {

    private String value;
    private String name;
    private String namespace;

    public static WithName newInstance() {
        return new Builder();
    }

    public static WithValue newInstance(AttributeID id) {
        Builder builder = new Builder();
        builder.name(id.getName()).namespace(id.getNamespace());
        return builder;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.name);
        hash = 31 * hash + Objects.hashCode(this.namespace);
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
        final GenericAttribute other = (GenericAttribute) obj;
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
        return "GenericAttribute{" + "value=" + value + ", name=" + name + ", namespace=" + namespace + '}';
    }

    public interface WithName {
        WithNamespace name(String val);
    }

    public interface WithNamespace {

        WithValue namespace(String val);
    }

    public interface WithValue {

        GenericAttributeBuilder value(String val);
    }

    public interface GenericAttributeBuilder {

        GenericAttribute build();
    }

    static class Builder implements WithName, WithNamespace, WithValue, GenericAttributeBuilder {

        private final GenericAttribute item;

        private Builder() {
            this.item = new GenericAttribute();
        }

        @Override
        public Builder value(final String value) {
            Objects.requireNonNull(value, "value must not be null");
            this.item.value = value;
            return this;
        }

        @Override
        public Builder name(final String name) {
            Objects.requireNonNull(name, "name must not be null");
            this.item.name = name;
            return this;
        }

        @Override
        public Builder namespace(final String namespace) {
            Objects.requireNonNull(namespace, "namespace must not be null");
            this.item.namespace = namespace;
            return this;
        }

        @Override
        public GenericAttribute build() {
            return this.item;
        }
    }

}
