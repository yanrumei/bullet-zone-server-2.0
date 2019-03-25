/*    */ package org.springframework.core.convert.support;
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
/*    */ 
/*    */ final class ObjectToStringConverter
/*    */   implements Converter<Object, String>
/*    */ {
/*    */   public String convert(Object source)
/*    */   {
/* 31 */     return source.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\ObjectToStringConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */