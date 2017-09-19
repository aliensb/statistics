package com.zsmarter.framework.statistics.upload.service.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.util.concurrent.atomic.AtomicLong;

class AsyncAppender {
    private final AsynchronousFileChannel channel;
    /**
     * Where new append operations are told to start writing.
     */
    private final AtomicLong projectedSize;

    AsyncAppender(AsynchronousFileChannel channel) throws IOException {
        this.channel = channel;
        this.projectedSize = new AtomicLong(channel.size());
    }

    public void append(ByteBuffer buf) {
        final int buflen = buf.remaining();
        long size;
        do {
            size = projectedSize.get();
        }
        while (!projectedSize.compareAndSet(size, size + buflen)) ;
        channel.write(buf, size, channel, new WriteOp(buf, size));

    }
}