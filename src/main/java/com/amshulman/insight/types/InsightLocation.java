package com.amshulman.insight.types;

import lombok.Value;

@Value
public class InsightLocation {

    int x;
    int y;
    int z;
    String world;
}
