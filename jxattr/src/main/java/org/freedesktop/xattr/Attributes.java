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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Operations for reading and modifying user attributes from Extended File
 * Attributes
 *
 * @author Konrad Renner
 */
public class Attributes {

    public static final String DEFAULT_NAMESPACE = "xdg";
    public static final String USER_NAMESPACE_PREFIX = "user";

    public enum Types {

        TAGS("tags", DEFAULT_NAMESPACE) {

                    @Override
                    public Tags createInstance(String value) {
                        return new Tags(value);
                    }

                },
        COMMENT("comment", DEFAULT_NAMESPACE) {

                    @Override
                    public Comment createInstance(String value) {
                        return new Comment(value);
                    }

                },
        RATING_BALOO("rating", "baloo") {

                    @Override
                    public BalooRating createInstance(String value) {
                        return new BalooRating(Integer.valueOf(value));
                    }

                };

        private final String name;
        private final String namespace;

        private Types(String name, String namespace) {
            this.name = name;
            this.namespace = namespace;
        }

        public String getName() {
            return name;
        }

        public String getNamespace() {
            return namespace;
        }

        public abstract Attribute<?> createInstance(String value);

        /**
         * Creates an instance of an attribute which matches with the given ID
         * (case insensitive!). If nothing matches, null is returned
         *
         * @param id
         * @param value
         * @return Attribute<?>
         */
        public static Attribute<?> createInstance(AttributeID id, String value) {
            for (Types type : values()) {
                if (type.getNamespace().equalsIgnoreCase(id.getNamespace()) && type.getName().equalsIgnoreCase(id.getName())) {
                    return type.createInstance(value);
                }
            }

            return null;
        }
    }

    private final Path pathToFile;
    private final Comparator<Attribute<?>> comparator;
    private final Map<AttributeID, Attribute<?>> attributes;

    public Attributes(Path pathToFile) {
        this(pathToFile, new DefaultComparator());
    }

    public Attributes(Path pathToFile, Comparator<Attribute<?>> comparator) {
        Objects.requireNonNull(pathToFile, "path to file must be not null");
        Objects.requireNonNull(comparator, "comparator must be not null");
        this.pathToFile = pathToFile;
        this.comparator = comparator;
        this.attributes = initAttributes();
    }


    public Set<Attribute<?>> getAttributes() {
        TreeSet<Attribute<?>> ret = new TreeSet<>(this.comparator);
        this.attributes.values().stream().forEach((attr) -> {
            ret.add(attr);
        });

        return ret;
    }

    public <T> T getAttribute(AttributeID id, Class<T> clazz) {
        return clazz.cast(getAttribute(id));
    }

    public Attribute<?> getAttribute(AttributeID id) {
        return this.attributes.get(id);
    }

    public void setAttributes(Attribute<?>... attrs) throws IOException {
        for (Attribute<?> attr : attrs) {
            Files.setAttribute(pathToFile, USER_NAMESPACE_PREFIX + ":" + attr.getNamespace() + "." + attr.getName(), ByteBuffer.wrap(attr.getValue().toString().getBytes(StandardCharsets.UTF_8)), LinkOption.NOFOLLOW_LINKS);
        }
    }

    public int size() {
        return this.attributes.size();
    }

    private Map<AttributeID, Attribute<?>> initAttributes() {
        Map<AttributeID, Attribute<?>> map = new HashMap<>();

        UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(pathToFile, UserDefinedFileAttributeView.class);

        try {
            List<String> allUserAttributes = fileAttributeView.list();

            for (String attribute : allUserAttributes) {
                ByteBuffer read = ByteBuffer.allocate(Files.getFileAttributeView(pathToFile, UserDefinedFileAttributeView.class).size(attribute));

                fileAttributeView.read(attribute, read);

                read.rewind();

                String value = StandardCharsets.UTF_8.decode(read).toString();

                AttributeID createAttributeID = createAttributeID(attribute);

                Attribute<?> instance = Types.createInstance(createAttributeID, value);
                if (instance == null) {
                    instance = GenericAttribute.newInstance().name(createAttributeID.getName()).namespace(createAttributeID.getNamespace()).value(value).build();
                }

                map.put(createAttributeID, instance);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read Attributes: " + ex);
        }

        return map;
    }

    private AttributeID createAttributeID(String concatedValue) {
        int lastIndexOfPoint = concatedValue.lastIndexOf('.');
        String namespace, name;
        if (lastIndexOfPoint == -1 || lastIndexOfPoint == concatedValue.length() - 1) {
            namespace = concatedValue;
            name = concatedValue;
        } else {
            namespace = concatedValue.substring(0, lastIndexOfPoint);
            name = concatedValue.substring(lastIndexOfPoint + 1);
        }

        return AttributeID.newInstance().name(name).namespace(namespace).build();
    }

    /**
     * Sorts the Elements depending on there namespace/name combination
     */
    public static class DefaultComparator implements Comparator<Attribute<?>> {

        @Override
        public int compare(Attribute<?> o1, Attribute<?> o2) {
            if (o1 == null || o2 == null) {
                return 1;
            }
            if (o1.equals(o2)) {
                return 0;
            }

            return (o1.getNamespace() + o1.getName()).compareTo(o2.getNamespace() + o2.getName());
        }

    }
}
