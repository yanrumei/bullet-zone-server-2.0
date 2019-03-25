/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.TimeZone;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ @UsesJava8
/*    */ class StringToTimeZoneConverter
/*    */   implements Converter<String, TimeZone>
/*    */ {
/*    */   public TimeZone convert(String source)
/*    */   {
/* 36 */     return StringUtils.parseTimeZoneString(source);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\StringToTimeZoneConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */