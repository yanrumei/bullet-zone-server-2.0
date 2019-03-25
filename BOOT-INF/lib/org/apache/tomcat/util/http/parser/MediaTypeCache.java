/*    */ package org.apache.tomcat.util.http.parser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.StringReader;
/*    */ import org.apache.tomcat.util.collections.ConcurrentCache;
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
/*    */ public class MediaTypeCache
/*    */ {
/*    */   private final ConcurrentCache<String, String[]> cache;
/*    */   
/*    */   public MediaTypeCache(int size)
/*    */   {
/* 32 */     this.cache = new ConcurrentCache(size);
/*    */   }
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
/*    */   public String[] parse(String input)
/*    */   {
/* 46 */     String[] result = (String[])this.cache.get(input);
/*    */     
/* 48 */     if (result != null) {
/* 49 */       return result;
/*    */     }
/*    */     
/* 52 */     MediaType m = null;
/*    */     try {
/* 54 */       m = MediaType.parseMediaType(new StringReader(input));
/*    */     }
/*    */     catch (IOException localIOException) {}
/*    */     
/* 58 */     if (m != null) {
/* 59 */       result = new String[] { m.toStringNoCharset(), m.getCharset() };
/* 60 */       this.cache.put(input, result);
/*    */     }
/*    */     
/* 63 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\parser\MediaTypeCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */