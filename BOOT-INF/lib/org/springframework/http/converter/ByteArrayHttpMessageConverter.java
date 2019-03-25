/*    */ package org.springframework.http.converter;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.http.HttpOutputMessage;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.util.StreamUtils;
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
/*    */ 
/*    */ 
/*    */ public class ByteArrayHttpMessageConverter
/*    */   extends AbstractHttpMessageConverter<byte[]>
/*    */ {
/*    */   public ByteArrayHttpMessageConverter()
/*    */   {
/* 44 */     super(new MediaType[] { new MediaType("application", "octet-stream"), MediaType.ALL });
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean supports(Class<?> clazz)
/*    */   {
/* 50 */     return byte[].class == clazz;
/*    */   }
/*    */   
/*    */   public byte[] readInternal(Class<? extends byte[]> clazz, HttpInputMessage inputMessage) throws IOException
/*    */   {
/* 55 */     long contentLength = inputMessage.getHeaders().getContentLength();
/* 56 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(contentLength >= 0L ? (int)contentLength : 4096);
/*    */     
/* 58 */     StreamUtils.copy(inputMessage.getBody(), bos);
/* 59 */     return bos.toByteArray();
/*    */   }
/*    */   
/*    */   protected Long getContentLength(byte[] bytes, MediaType contentType)
/*    */   {
/* 64 */     return Long.valueOf(bytes.length);
/*    */   }
/*    */   
/*    */   protected void writeInternal(byte[] bytes, HttpOutputMessage outputMessage) throws IOException
/*    */   {
/* 69 */     StreamUtils.copy(bytes, outputMessage.getBody());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\ByteArrayHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */