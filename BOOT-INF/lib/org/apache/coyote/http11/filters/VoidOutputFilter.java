/*    */ package org.apache.coyote.http11.filters;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.coyote.Response;
/*    */ import org.apache.coyote.http11.HttpOutputBuffer;
/*    */ import org.apache.coyote.http11.OutputFilter;
/*    */ import org.apache.tomcat.util.buf.ByteChunk;
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
/*    */ public class VoidOutputFilter
/*    */   implements OutputFilter
/*    */ {
/* 35 */   private HttpOutputBuffer buffer = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public int doWrite(ByteChunk chunk)
/*    */     throws IOException
/*    */   {
/* 47 */     return chunk.getLength();
/*    */   }
/*    */   
/*    */   public int doWrite(ByteBuffer chunk)
/*    */     throws IOException
/*    */   {
/* 53 */     return chunk.remaining();
/*    */   }
/*    */   
/*    */ 
/*    */   public long getBytesWritten()
/*    */   {
/* 59 */     return 0L;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setResponse(Response response) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setBuffer(HttpOutputBuffer buffer)
/*    */   {
/* 73 */     this.buffer = buffer;
/*    */   }
/*    */   
/*    */   public void flush()
/*    */     throws IOException
/*    */   {
/* 79 */     this.buffer.flush();
/*    */   }
/*    */   
/*    */ 
/*    */   public void recycle()
/*    */   {
/* 85 */     this.buffer = null;
/*    */   }
/*    */   
/*    */   public void end()
/*    */     throws IOException
/*    */   {
/* 91 */     this.buffer.end();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\VoidOutputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */