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

import org.jxattr.GenericAttribute;
import org.jxattr.AttributeID;
import org.jxattr.Attributes;
import org.jxattr.Attribute;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Konrad Renner
 */
public class UserAttributesTest {

    private Attributes attributes;
    private Path path;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        URL resource = getClass().getResource("testfile.txt");
        path = Paths.get(resource.toURI());
        Files.setAttribute(path, "user:xdg.comment", ByteBuffer.wrap("Halllo".getBytes(StandardCharsets.UTF_8)), LinkOption.NOFOLLOW_LINKS);
        Files.setAttribute(path, "user:xdg.tags", ByteBuffer.wrap("Eins,Zwei".getBytes(StandardCharsets.UTF_8)), LinkOption.NOFOLLOW_LINKS);
        attributes = Attributes.loadUserAttributes(path);
    }

    @Test
    public void testGetAttributes() {
        assertThat(attributes.size(), is(2));
    }

    @Test
    public void testGetAttributeNotFound() {
        assertThat(attributes.getAttribute(AttributeID.newInstance().name("rating").namespace("baloo").build()), is(nullValue()));
    }

    @Test
    public void testGetAttributeFoundName() {
        Attribute<?> attribute = attributes.getAttribute(AttributeID.newInstance().name("comment").namespace("xdg").build());
        assertThat(attribute, is(new Comment("Halllo")));
    }
    
    @Test
    public void testGetAttributeFoundNameAndClass() {
        assertThat(attributes.getAttribute(AttributeID.newInstance().name("tags").namespace("xdg").build(), Tags.class), is(new Tags(new Tag("Eins"), new Tag("Zwei")))
        );
    }

    @Test
    public void testSetAttributes() throws IOException {
        Tags expectedTags = new Tags(new Tag("One"), new Tag("Two"));
        Comment expectedComment = new Comment("Hello");
        attributes.setAttributes(expectedTags, expectedComment);

        Tags actualTags = new Tags(readAttribute("xdg.tags"));
        Comment actualComment = new Comment(readAttribute("xdg.comment"));
        
        assertThat(actualTags, is(expectedTags));
        assertThat(actualComment, is(expectedComment));
    }

    @Test
    public void testRemoveAttributes() throws IOException {
        Files.setAttribute(path, "user:jxattr.removeTest", ByteBuffer.wrap("Removing".getBytes(StandardCharsets.UTF_8)), LinkOption.NOFOLLOW_LINKS);
        attributes = Attributes.loadUserAttributes(path);

        AttributeID attrId = AttributeID.newInstance().name("removeTest").namespace("jxattr").build();
        GenericAttribute myAttr = GenericAttribute.newInstance(attrId).value("Removing").build();
        assertThat(attributes.getAttribute(attrId), is(myAttr));

        attributes.removeAttributes(attrId);

        assertThat(attributes.getAttribute(attrId, GenericAttribute.class), is(nullValue()));
    }

    private String readAttribute(String name) throws IOException {
        ByteBuffer read = ByteBuffer.allocate(Files.getFileAttributeView(path, UserDefinedFileAttributeView.class).size(name));

        UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);

        fileAttributeView.read(name, read);

        read.rewind();
        return StandardCharsets.UTF_8.decode(read).toString();
    }

    @Test
    public void userAttributesInAction() {
        Attributes userAttributes = Attributes.loadUserAttributes(path);
        //Read Tags an display it
        Tags tags = userAttributes.getAttribute(UserAttributes.Types.TAGS.getAttributeID(), Tags.class);

        tags.stream().forEach((tag) -> {
            System.out.println(tag);
        });

        //Set a comment and some other attribute into the file
        AttributeID myID = AttributeID.newInstance().name("someTest").namespace("jxattr").build();
        GenericAttribute myAttribute = GenericAttribute.newInstance(myID).value("Hello World").build();
        userAttributes.setAttributes(UserAttributes.Types.COMMENT.createInstance("Some comment"), myAttribute);

        //Remove the other attribute
        userAttributes.removeAttributes(myID);
    }
}
