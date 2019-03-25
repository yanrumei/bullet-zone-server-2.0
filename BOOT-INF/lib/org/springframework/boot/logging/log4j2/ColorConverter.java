/*     */ package org.springframework.boot.logging.log4j2;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.pattern.ConverterKeys;
/*     */ import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.springframework.boot.ansi.AnsiColor;
/*     */ import org.springframework.boot.ansi.AnsiElement;
/*     */ import org.springframework.boot.ansi.AnsiOutput;
/*     */ import org.springframework.boot.ansi.AnsiStyle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name="color", category="Converter")
/*     */ @ConverterKeys({"clr", "color"})
/*     */ public final class ColorConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private static final Map<String, AnsiElement> ELEMENTS;
/*     */   private static final Map<Integer, AnsiElement> LEVELS;
/*     */   private final List<PatternFormatter> formatters;
/*     */   private final AnsiElement styling;
/*     */   
/*     */   static
/*     */   {
/*  55 */     Map<String, AnsiElement> ansiElements = new HashMap();
/*  56 */     ansiElements.put("faint", AnsiStyle.FAINT);
/*  57 */     ansiElements.put("red", AnsiColor.RED);
/*  58 */     ansiElements.put("green", AnsiColor.GREEN);
/*  59 */     ansiElements.put("yellow", AnsiColor.YELLOW);
/*  60 */     ansiElements.put("blue", AnsiColor.BLUE);
/*  61 */     ansiElements.put("magenta", AnsiColor.MAGENTA);
/*  62 */     ansiElements.put("cyan", AnsiColor.CYAN);
/*  63 */     ELEMENTS = Collections.unmodifiableMap(ansiElements);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */     Map<Integer, AnsiElement> ansiLevels = new HashMap();
/*  70 */     ansiLevels.put(Integer.valueOf(Level.FATAL.intLevel()), AnsiColor.RED);
/*  71 */     ansiLevels.put(Integer.valueOf(Level.ERROR.intLevel()), AnsiColor.RED);
/*  72 */     ansiLevels.put(Integer.valueOf(Level.WARN.intLevel()), AnsiColor.YELLOW);
/*  73 */     LEVELS = Collections.unmodifiableMap(ansiLevels);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ColorConverter(List<PatternFormatter> formatters, AnsiElement styling)
/*     */   {
/*  81 */     super("style", "style");
/*  82 */     this.formatters = formatters;
/*  83 */     this.styling = styling;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ColorConverter newInstance(Configuration config, String[] options)
/*     */   {
/*  93 */     if (options.length < 1) {
/*  94 */       LOGGER.error("Incorrect number of options on style. Expected at least 1, received {}", 
/*  95 */         Integer.valueOf(options.length));
/*  96 */       return null;
/*     */     }
/*  98 */     if (options[0] == null) {
/*  99 */       LOGGER.error("No pattern supplied on style");
/* 100 */       return null;
/*     */     }
/* 102 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 103 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/* 104 */     AnsiElement element = options.length == 1 ? null : (AnsiElement)ELEMENTS.get(options[1]);
/* 105 */     return new ColorConverter(formatters, element);
/*     */   }
/*     */   
/*     */   public boolean handlesThrowable()
/*     */   {
/* 110 */     for (PatternFormatter formatter : this.formatters) {
/* 111 */       if (formatter.handlesThrowable()) {
/* 112 */         return true;
/*     */       }
/*     */     }
/* 115 */     return super.handlesThrowable();
/*     */   }
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo)
/*     */   {
/* 120 */     StringBuilder buf = new StringBuilder();
/* 121 */     for (PatternFormatter formatter : this.formatters) {
/* 122 */       formatter.format(event, buf);
/*     */     }
/* 124 */     if (buf.length() > 0) {
/* 125 */       AnsiElement element = this.styling;
/* 126 */       if (element == null)
/*     */       {
/* 128 */         element = (AnsiElement)LEVELS.get(Integer.valueOf(event.getLevel().intLevel()));
/* 129 */         element = element == null ? AnsiColor.GREEN : element;
/*     */       }
/* 131 */       appendAnsiString(toAppendTo, buf.toString(), element);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void appendAnsiString(StringBuilder toAppendTo, String in, AnsiElement element)
/*     */   {
/* 137 */     toAppendTo.append(AnsiOutput.toString(new Object[] { element, in }));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\log4j2\ColorConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */