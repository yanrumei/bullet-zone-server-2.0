/*    */ package org.springframework.boot.bind;
/*    */ 
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
/*    */ class StringToCharArrayConverter
/*    */   implements Converter<String, char[]>
/*    */ {
/*    */   public char[] convert(String source)
/*    */   {
/* 30 */     return source.toCharArray();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\StringToCharArrayConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */