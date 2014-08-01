jxattr
======

Java library for accessing extended file attributes (comments, tags, etc), defined by the freedesktop project.

HowTo use the org.freedesktop.Attributes class for manipulating/read user attributes:

```java
//Initialize with java.nio.file.Path
Attributes userAttributes = new Attributes(path);
//Read Tags an display it
Tags tags = userAttributes.getAttribute(Attributes.Types.TAGS.getAttributeID(), Tags.class);

//Java 8 Lambda
tags.stream().forEach((tag) -> {
    System.out.println(tag);
});

//Set a comment and some other attribute into the file
AttributeID myID = AttributeID.newInstance().name("someTest").namespace("jxattr").build();
GenericAttribute myAttribute = GenericAttribute.newInstance(myID).value("Hello World").build();
userAttributes.setAttributes(Attributes.Types.COMMENT.createInstance("Some comment"), myAttribute);

//Remove the other attribute
userAttributes.removeAttributes(myID);
```

Travis:
[![Build Status](https://secure.travis-ci.org/konradrenner/jxattr.png?branch=master)](http://travis-ci.org/konradrenner/jxattr)

Coverity Scan:
[![Coverity Scan Build Status](https://scan.coverity.com/projects/2692/badge.svg)](https://scan.coverity.com/projects/2692)
