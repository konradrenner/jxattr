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

package org.jxattr;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import org.jxattr.Attribute;
import org.freedesktop.xattr.UserAttributes;

/**
 * Central object for handling attributes
 *
 * @author Konrad Renner
 */
public interface Attributes {

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
    <T> T getAttribute(AttributeID id, Class<T> clazz);

    /**
     * Returns the user attribute which is specified by the given ID.
     *
     * Returns null if no attribute is found
     *
     * @param id
     * @return T
     */
    Attribute<?> getAttribute(AttributeID id);

    /**
     * Gets all user attributes from the defined path, or an empty set if no
     * attributes are defined
     *
     * @return Set<Attribute<?>>
     */
    Set<Attribute<?>> getAttributes();

    /**
     * Removes UserAttributes which match with the given ids
    Throws an IllegalStateException if the given path is invalid.
     *
     * @throws IllegalStateException
     * @param ids
     */
    void removeAttributes(AttributeID... ids);

    /**
     * Updates or creates user attributes. This method does not remove any
     * attributes
     *
     * Throws an IllegalStateException if the given path is invalid.
     *
     * @throws IllegalStateException
     * @param attrs
     */
    void setAttributes(Attribute<?>... attrs);

    /**
     * Returns the number of user attributes which are set
     *
     * @return int
     */
    int size();

    /**
     * Creates an new instance for reading/manipulating user attributes. Throws
     * an IllegalStateException if the given path is invalid. Throws
     * NullpointerException if an argument is null.
     *
     * @return Attributes implementation for attributes in the user space
     * @throws IllegalStateException
     * @throws NullPointerException
     * @param pathToFile
     */
    public static Attributes loadUserAttributes(Path pathToFile) {
        return loadUserAttributes(pathToFile, new UserAttributes.DefaultComparator());
    }

    /**
     * Creates an instance of this class with a given comparator. This
     * comparator is used for sorting, when the getAttributes() method is called
     * Throws an IllegalStateException if the given path is invalid. Throws
     * NullpointerException if an argument is null.
     *
     * @return Attributes implementation for attributes in the user space
     * @throws IllegalStateException
     * @param pathToFile
     * @param comparator
     */
    public static Attributes loadUserAttributes(Path pathToFile, Comparator<Attribute<?>> comparator) {
        return new UserAttributes(pathToFile, comparator);
    }
}
