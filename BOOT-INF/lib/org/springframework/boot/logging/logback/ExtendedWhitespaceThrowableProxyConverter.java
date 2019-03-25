/*    */ package org.springframework.boot.logging.logback;
/*    */ 
/*    */ import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
/*    */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*    */ import ch.qos.logback.core.CoreConstants;
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
/*    */ public class ExtendedWhitespaceThrowableProxyConverter
/*    */   extends ExtendedThrowableProxyConverter
/*    */ {
/*    */   protected String throwableProxyToString(IThrowableProxy tp)
/*    */   {
/* 35 */     return CoreConstants.LINE_SEPARATOR + super.throwableProxyToString(tp) + CoreConstants.LINE_SEPARATOR;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\ExtendedWhitespaceThrowableProxyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */