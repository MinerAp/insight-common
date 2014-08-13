package com.amshulman.insight.types;

import lombok.Value;

@Value
public final class InsightMaterial {

    public static final short UNSPECIFIED_SUBTYPE = -1;

    String namespace;
    String name;
    short subtype;
}
