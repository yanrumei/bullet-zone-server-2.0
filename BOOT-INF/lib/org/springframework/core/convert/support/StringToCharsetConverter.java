/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ class StringToCharsetConverter
/*    */   implements Converter<String, Charset>
/*    */ {
/*    */   public Charset convert(String source)
/*    */   {
/* 33 */     return Charset.forName(source);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\StringToCharsetConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */