/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.time.ZoneId;
/*    */ import java.util.TimeZone;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ 
/*    */ @UsesJava8
/*    */ final class ZoneIdToTimeZoneConverter
/*    */   implements Converter<ZoneId, TimeZone>
/*    */ {
/*    */   public TimeZone convert(ZoneId source)
/*    */   {
/* 43 */     return TimeZone.getTimeZone(source);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\convert\support\ZoneIdToTimeZoneConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */