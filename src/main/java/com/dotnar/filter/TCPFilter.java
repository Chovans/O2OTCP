package com.dotnar.filter;

import hprose.common.HproseContext;
import hprose.common.HproseFilter;

import java.nio.ByteBuffer;

/**
 * Created by chovans on 15/7/20.
 */
public class TCPFilter implements HproseFilter {
    @Override
    public ByteBuffer inputFilter(ByteBuffer byteBuffer, HproseContext hproseContext) {
        return null;
    }

    @Override
    public ByteBuffer outputFilter(ByteBuffer byteBuffer, HproseContext hproseContext) {
        return null;
    }
}
