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

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Composite for Tag, the getValue method gets the tags separated with Commas as
 * java.lang.String. The tags are sorted in there natural ordering
 *
 * @author Konrad Renner
 */
@AttributeDefinition(name = "tags")
public class Tags extends TreeSet<Tag> implements Attribute<String> {

    public Tags() {
        super();
    }
    
    public Tags(String value) {
        String[] split = value.split(",");
        for (String s : split) {
            add(new Tag(s));
        }
    }

    public Tags(Tag... c) {
        super(Arrays.asList(c));
    }

    public Tags(Collection<Tag> c) {
        super(c);
    }

    @Override
    public String getValue() {
        StringBuilder sb = new StringBuilder();

        this.stream().map((tag) -> {
            sb.append(tag.getValue());
            return tag;
        }).forEach((item) -> {
            sb.append(',');
        });

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

}
