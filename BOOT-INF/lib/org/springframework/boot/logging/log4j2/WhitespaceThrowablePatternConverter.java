/*    */ package org.springframework.boot.logging.log4j2;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.impl.ThrowableFormatOptions;
/*    */ import org.apache.logging.log4j.core.pattern.ConverterKeys;
/*    */ import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
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
/*    */ @Plugin(name="WhitespaceThrowablePatternConverter", category="Converter")
/*    */ @ConverterKeys({"wEx", "wThrowable", "wException"})
/*    */ public final class WhitespaceThrowablePatternConverter
/*    */   extends ThrowablePatternConverter
/*    */ {
/*    */   private WhitespaceThrowablePatternConverter(String[] options)
/*    */   {
/* 37 */     super("WhitespaceThrowable", "throwable", options);
/*    */   }
/*    */   
/*    */   public void format(LogEvent event, StringBuilder buffer)
/*    */   {
/* 42 */     if (event.getThrown() != null) {
/* 43 */       buffer.append(this.options.getSeparator());
/* 44 */       super.format(event, buffer);
/* 45 */       buffer.append(this.options.getSeparator());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static WhitespaceThrowablePatternConverter newInstance(String[] options)
/*    */   {
/* 56 */     return new WhitespaceThrowablePatternConverter(options);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\log4j2\WhitespaceThrowablePatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */