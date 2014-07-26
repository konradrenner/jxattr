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

import org.freedesktop.SimpleValue;

/**
 * Represents ratings how baloo uses it (https://community.kde.org/Baloo)
 *
 * @author Konrad Renner
 */
@AttributeDefinition(name = "rating", namespace = "user.baloo")
public class BalooRating extends SimpleValue<Integer> implements Attribute<Integer> {

    private final Integer rating;
    
    public BalooRating(int rating) {
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("rating must be between 0 and 10");
        }
        this.rating = rating;
    }
    

    @Override
    public Integer getValue() {
        return rating;
    }
}
