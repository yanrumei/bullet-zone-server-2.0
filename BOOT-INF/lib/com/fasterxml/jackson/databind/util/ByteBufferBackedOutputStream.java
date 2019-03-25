/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ 
/*    */ public class ByteBufferBackedOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   protected final ByteBuffer _b;
/*    */   
/* 13 */   public ByteBufferBackedOutputStream(ByteBuffer buf) { this._b = buf; }
/*    */   
/* 15 */   public void write(int b) throws IOException { this._b.put((byte)b); }
/* 16 */   public void write(byte[] bytes, int off, int len) throws IOException { this._b.put(bytes, off, len); }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\ByteBufferBackedOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */