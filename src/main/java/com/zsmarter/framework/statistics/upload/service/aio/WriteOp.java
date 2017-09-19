package com.zsmarter.framework.statistics.upload.service.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;

class WriteOp implements CompletionHandler<Integer, AsynchronousFileChannel> {
    private final ByteBuffer buf;
    private long position;

    WriteOp(ByteBuffer buf, long position) {
        this.buf = buf;
        this.position = position;
    }

    public void completed(Integer result, AsynchronousFileChannel channel) {
        if ( buf.hasRemaining() ) { // incomplete write
            position += result;
            channel.write( buf, position, channel, this );
        }
        else {
            cleanUp(channel);
        }
    }

    public void failed(Throwable ex, AsynchronousFileChannel channel) {
        System.out.println("文件写入失败");
        // ?
    }

    private void cleanUp(AsynchronousFileChannel channel){
        if(channel!=null){
            try {
                channel.close();
            } catch (IOException e) {
                System.out.println("关闭资源失败！");
                e.printStackTrace();
            }
        }
    }
}