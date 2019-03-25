/*     */ package org.apache.coyote.http11.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.coyote.InputBuffer;
/*     */ import org.apache.coyote.Request;
/*     */ import org.apache.coyote.http11.InputFilter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.net.ApplicationBufferHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VoidInputFilter
/*     */   implements InputFilter
/*     */ {
/*     */   protected static final String ENCODING_NAME = "void";
/*  40 */   protected static final ByteChunk ENCODING = new ByteChunk();
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  46 */     ENCODING.setBytes("void".getBytes(StandardCharsets.ISO_8859_1), 0, "void"
/*  47 */       .length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int doRead(ByteChunk chunk)
/*     */     throws IOException
/*     */   {
/*  60 */     return -1;
/*     */   }
/*     */   
/*     */   public int doRead(ApplicationBufferHandler handler) throws IOException
/*     */   {
/*  65 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteChunk getEncodingName()
/*     */   {
/* 104 */     return ENCODING;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long end()
/*     */     throws IOException
/*     */   {
/* 119 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */   public int available()
/*     */   {
/* 125 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFinished()
/*     */   {
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   public void setRequest(Request request) {}
/*     */   
/*     */   public void setBuffer(InputBuffer buffer) {}
/*     */   
/*     */   public void recycle() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\VoidInputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */