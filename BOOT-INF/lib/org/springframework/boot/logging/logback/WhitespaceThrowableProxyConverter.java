/*    */ package org.springframework.boot.logging.logback;
/*    */ 
/*    */ import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
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
/*    */ public class WhitespaceThrowableProxyConverter
/*    */   extends ThrowableProxyConverter
/*    */ {
/*    */   protected String throwableProxyToString(IThrowableProxy tp)
/*    */   {
/* 33 */     return CoreConstants.LINE_SEPARATOR + super.throwableProxyToString(tp) + CoreConstants.LINE_SEPARATOR;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\WhitespaceThrowableProxyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */