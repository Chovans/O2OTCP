package com.dotnar.filter;

import hprose.common.HproseContext;
import hprose.common.HproseFilter;

import java.nio.ByteBuffer;

/**
 * @author chovans on 15/8/23.
 */
public class MongoFilter implements HproseFilter{
    @Override
    public ByteBuffer inputFilter(ByteBuffer byteBuffer, HproseContext hproseContext) {
        return byteBuffer;
    }

    @Override
    public ByteBuffer outputFilter(ByteBuffer byteBuffer, HproseContext hproseContext) {
        return byteBuffer;
    }
}
