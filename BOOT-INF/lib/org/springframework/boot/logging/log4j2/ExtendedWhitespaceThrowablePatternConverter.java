/*    */ package org.springframework.boot.logging.log4j2;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.impl.ThrowableFormatOptions;
/*    */ import org.apache.logging.log4j.core.pattern.ConverterKeys;
/*    */ import org.apache.logging.log4j.core.pattern.ExtendedThrowablePatternConverter;
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
/*    */ 
/*    */ 
/*    */ @Plugin(name="ExtendedWhitespaceThrowablePatternConverter", category="Converter")
/*    */ @ConverterKeys({"xwEx", "xwThrowable", "xwException"})
/*    */ public final class ExtendedWhitespaceThrowablePatternConverter
/*    */   extends ThrowablePatternConverter
/*    */ {
/*    */   private final ExtendedThrowablePatternConverter delegate;
/*    */   
/*    */   private ExtendedWhitespaceThrowablePatternConverter(String[] options)
/*    */   {
/* 42 */     super("WhitespaceExtendedThrowable", "throwable", options);
/* 43 */     this.delegate = ExtendedThrowablePatternConverter.newInstance(options);
/*    */   }
/*    */   
/*    */   public void format(LogEvent event, StringBuilder buffer)
/*    */   {
/* 48 */     if (event.getThrown() != null) {
/* 49 */       buffer.append(this.options.getSeparator());
/* 50 */       this.delegate.format(event, buffer);
/* 51 */       buffer.append(this.options.getSeparator());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ExtendedWhitespaceThrowablePatternConverter newInstance(String[] options)
/*    */   {
/* 63 */     return new ExtendedWhitespaceThrowablePatternConverter(options);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\log4j2\ExtendedWhitespaceThrowablePatternConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */