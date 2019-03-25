/*    */ package com.fasterxml.jackson.core.io;
/*    */ 
/*    */ import com.fasterxml.jackson.core.util.BufferRecycler;
/*    */ import com.fasterxml.jackson.core.util.TextBuffer;
/*    */ import java.io.Writer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SegmentedStringWriter
/*    */   extends Writer
/*    */ {
/*    */   private final TextBuffer _buffer;
/*    */   
/*    */   public SegmentedStringWriter(BufferRecycler br)
/*    */   {
/* 22 */     this._buffer = new TextBuffer(br);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Writer append(char c)
/*    */   {
/* 33 */     write(c);
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public Writer append(CharSequence csq)
/*    */   {
/* 39 */     String str = csq.toString();
/* 40 */     this._buffer.append(str, 0, str.length());
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public Writer append(CharSequence csq, int start, int end)
/*    */   {
/* 46 */     String str = csq.subSequence(start, end).toString();
/* 47 */     this._buffer.append(str, 0, str.length());
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */   
/*    */   public void flush() {}
/*    */   
/* 55 */   public void write(char[] cbuf) { this._buffer.append(cbuf, 0, cbuf.length); }
/*    */   
/*    */   public void write(char[] cbuf, int off, int len) {
/* 58 */     this._buffer.append(cbuf, off, len);
/*    */   }
/*    */   
/* 61 */   public void write(int c) { this._buffer.append((char)c); }
/*    */   
/*    */   public void write(String str) {
/* 64 */     this._buffer.append(str, 0, str.length());
/*    */   }
/*    */   
/* 67 */   public void write(String str, int off, int len) { this._buffer.append(str, off, len); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getAndClear()
/*    */   {
/* 83 */     String result = this._buffer.contentsAsString();
/* 84 */     this._buffer.releaseBuffers();
/* 85 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\io\SegmentedStringWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */