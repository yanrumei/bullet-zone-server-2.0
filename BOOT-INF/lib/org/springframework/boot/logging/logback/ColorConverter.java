/*    */ package org.springframework.boot.logging.logback;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.pattern.CompositeConverter;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.boot.ansi.AnsiColor;
/*    */ import org.springframework.boot.ansi.AnsiElement;
/*    */ import org.springframework.boot.ansi.AnsiOutput;
/*    */ import org.springframework.boot.ansi.AnsiStyle;
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
/*    */ public class ColorConverter
/*    */   extends CompositeConverter<ILoggingEvent>
/*    */ {
/*    */   private static final Map<String, AnsiElement> ELEMENTS;
/*    */   private static final Map<Integer, AnsiElement> LEVELS;
/*    */   
/*    */   static
/*    */   {
/* 44 */     Map<String, AnsiElement> ansiElements = new HashMap();
/* 45 */     ansiElements.put("faint", AnsiStyle.FAINT);
/* 46 */     ansiElements.put("red", AnsiColor.RED);
/* 47 */     ansiElements.put("green", AnsiColor.GREEN);
/* 48 */     ansiElements.put("yellow", AnsiColor.YELLOW);
/* 49 */     ansiElements.put("blue", AnsiColor.BLUE);
/* 50 */     ansiElements.put("magenta", AnsiColor.MAGENTA);
/* 51 */     ansiElements.put("cyan", AnsiColor.CYAN);
/* 52 */     ELEMENTS = Collections.unmodifiableMap(ansiElements);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 58 */     Map<Integer, AnsiElement> ansiLevels = new HashMap();
/* 59 */     ansiLevels.put(Level.ERROR_INTEGER, AnsiColor.RED);
/* 60 */     ansiLevels.put(Level.WARN_INTEGER, AnsiColor.YELLOW);
/* 61 */     LEVELS = Collections.unmodifiableMap(ansiLevels);
/*    */   }
/*    */   
/*    */   protected String transform(ILoggingEvent event, String in)
/*    */   {
/* 66 */     AnsiElement element = (AnsiElement)ELEMENTS.get(getFirstOption());
/* 67 */     if (element == null)
/*    */     {
/* 69 */       element = (AnsiElement)LEVELS.get(event.getLevel().toInteger());
/* 70 */       element = element == null ? AnsiColor.GREEN : element;
/*    */     }
/* 72 */     return toAnsiString(in, element);
/*    */   }
/*    */   
/*    */   protected String toAnsiString(String in, AnsiElement element) {
/* 76 */     return AnsiOutput.toString(new Object[] { element, in });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\ColorConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */