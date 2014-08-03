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
import java.io.Serializable;
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
 UserAttributes. User the Types enum for predefined attributes.
 *
 * @author Konrad Renner
 */
public class UserAttributes implements Attributes {

    public static final String DEFAULT_NAMESPACE = "xdg";
    public static final String USER_NAMESPACE_PREFIX = "user";

    void getAttribute(Types types, Class<Tags> aClass) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

        private final AttributeID id;

        private Types(String name, String namespace) {
            this.id = new AttributeID(name, namespace);
        }

        public AttributeID getAttributeID() {
            return id;
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
                if (type.getAttributeID().getNamespace().equalsIgnoreCase(id.getNamespace()) && type.getAttributeID().getName().equalsIgnoreCase(id.getName())) {
                    return type.createInstance(value);
                }
            }

            return null;
        }
    }

    private final Path pathToFile;
    private final Comparator<Attribute<?>> comparator;
    private final Map<AttributeID, Attribute<?>> attributes;

    /**
     * Creates an instance of this class with a given comparator. This
     * comparator is used for sorting, when the getAttributes() method is called
     * Throws an IllegalStateException if the given path is invalid. Throws
     * NullpointerException if an argument is null.
     *
     * @throws IllegalStateException
     * @param pathToFile
     * @param comparator
     */
    UserAttributes(Path pathToFile, Comparator<Attribute<?>> comparator) {
        Objects.requireNonNull(pathToFile, "path to file must be not null");
        Objects.requireNonNull(comparator, "comparator must be not null");
        this.pathToFile = pathToFile;
        this.comparator = comparator;
        this.attributes = initAttributes();
    }

    /**
     * Gets all user attributes from the defined path, or an empty set if no
     * attributes are defined
     *
     * @return Set<Attribute<?>>
     */
    @Override
    public Set<Attribute<?>> getAttributes() {
        TreeSet<Attribute<?>> ret = new TreeSet<>(this.comparator);
        this.attributes.values().stream().forEach((attr) -> {
            ret.add(attr);
        });

        return ret;
    }

    /**
     * Returns the user attribute which is specified by the given ID. Throws a
     * ClassCastException if the given Class is incompatibel with the type of
     * the attribute.
     *
     * Returns null if no attribute is found
     *
     * @param <T>
     * @param id
     * @param clazz
     * @throws ClassCastException
     * @return T
     */
    @Override
    public <T> T getAttribute(AttributeID id, Class<T> clazz) {
        Attribute<?> attribute = getAttribute(id);
        return clazz.cast(attribute);
    }

    /**
     * Returns the user attribute which is specified by the given ID.
     *
     * Returns null if no attribute is found
     *
     * @param id
     * @return T
     */
    @Override
    public Attribute<?> getAttribute(AttributeID id) {
        return this.attributes.get(id);
    }

    /**
     * Updates or creates user attributes. This method does not remove any
     * attributes
     *
     * Throws an IllegalStateException if the given path is invalid.
     *
     * @throws IllegalStateException
     * @param attrs
     */
    @Override
    public void setAttributes(Attribute<?>... attrs) {
        try {
            for (Attribute<?> attr : attrs) {
                Files.setAttribute(pathToFile, USER_NAMESPACE_PREFIX + ":" + attr.getNamespace() + "." + attr.getName(), ByteBuffer.wrap(attr.getValue().toString().getBytes(StandardCharsets.UTF_8)), LinkOption.NOFOLLOW_LINKS);
                this.attributes.put(AttributeID.newInstance().name(attr.getName()).namespace(attr.getNamespace()).build(), attr);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to set Attributes: " + e);
        }
    }

    /**
     * Removes UserAttributes which match with the given ids
     *
     * Throws an IllegalStateException if the given path is invalid.
     *
     * @throws IllegalStateException
     * @param ids
     */
    @Override
    public void removeAttributes(AttributeID... ids) {
        UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(pathToFile, UserDefinedFileAttributeView.class);
        try {
            for (AttributeID id : ids) {
                fileAttributeView.delete(id.getNamespace() + "." + id.getName());
                this.attributes.remove(id);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to delete Attributes: " + e);
        }
    }

    /**
     * Returns the number of user attributes which are set
     *
     * @return int
     */
    @Override
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
     * Sorts the Elements depending on there namespace+name combination
     */
    public static class DefaultComparator implements Comparator<Attribute<?>>, Serializable {

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
