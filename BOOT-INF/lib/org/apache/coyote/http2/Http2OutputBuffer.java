/*    */ package org.apache.coyote.http2;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Http2OutputBuffer
/*    */   implements HttpOutputBuffer
/*    */ {
/*    */   private final Response coyoteResponse;
/*    */   private HttpOutputBuffer next;
/*    */   
/*    */   public void addFilter(OutputFilter filter)
/*    */   {
/* 44 */     filter.setBuffer(this.next);
/* 45 */     this.next = filter;
/*    */   }
/*    */   
/*    */   public Http2OutputBuffer(Response coyoteResponse, Stream.StreamOutputBuffer streamOutputBuffer)
/*    */   {
/* 50 */     this.coyoteResponse = coyoteResponse;
/* 51 */     this.next = streamOutputBuffer;
/*    */   }
/*    */   
/*    */   public int doWrite(ByteBuffer chunk)
/*    */     throws IOException
/*    */   {
/* 57 */     if (!this.coyoteResponse.isCommitted()) {
/* 58 */       this.coyoteResponse.sendHeaders();
/*    */     }
/* 60 */     return this.next.doWrite(chunk);
/*    */   }
/*    */   
/*    */ 
/*    */   public long getBytesWritten()
/*    */   {
/* 66 */     return this.next.getBytesWritten();
/*    */   }
/*    */   
/*    */   public void end()
/*    */     throws IOException
/*    */   {
/* 72 */     this.next.end();
/*    */   }
/*    */   
/*    */   public void flush()
/*    */     throws IOException
/*    */   {
/* 78 */     this.next.flush();
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public int doWrite(ByteChunk chunk)
/*    */     throws IOException
/*    */   {
/* 85 */     return this.next.doWrite(chunk);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Http2OutputBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */