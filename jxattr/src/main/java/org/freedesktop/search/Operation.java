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

package org.freedesktop.search;

import org.jxattr.Attribute;

/**
 * Operations to specfiy how the search should be done
 *
 * @author Konrad Renner
 */
public enum Operation {

    EXCAT("="),
    LIKE("="),
    NOT("!="),
    GREATER(">"),
    LESSER("<");

    private final String operator;

    private Operation(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    /**
     * The given attribute must match excatly
     *
     * @param attr
     * @return Operation
     */
    public static Operation containigExcatly(Attribute<?> attr) {
        return EXCAT;
    }

    /**
     * The value must be a at least a part of the attribute from the searched
     * item     * item
     *
     * @param attr
     * @return Operation
     */
    public static Operation containig(Attribute<?> attr) {
        return LIKE;
    }

    /**
     * The given attribute is not allowed to match
     *
     * @param attr
     * @return Operation
     */
    public static Operation containigNot(Attribute<?> attr) {
        return NOT;
    }

    /**
     * The value must be greater than the attribute-value from the searched
     * item.
     *
     * @param attr
     * @return Operation
     */
    public static Operation containingGreaterThan(Attribute<?> attr) {
        return GREATER;
    }

    /**
     * The value must be lesser than the attribute-value from the searched item.
     *
     * @param attr
     * @return Operation
     */
    public static Operation containingLesserThan(Attribute<?> attr) {
        return LESSER;
    }

}
