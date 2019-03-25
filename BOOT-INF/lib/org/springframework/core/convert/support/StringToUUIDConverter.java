/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ final class StringToUUIDConverter
/*    */   implements Converter<String, UUID>
/*    */ {
/*    */   public UUID convert(String source)
/*    */   {
/* 35 */     return StringUtils.hasLength(source) ? UUID.fromString(source.trim()) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\StringToUUIDConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */