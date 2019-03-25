/*     */ package ch.qos.logback.classic;
/*     */ 
/*     */ import ch.qos.logback.classic.pattern.CallerDataConverter;
/*     */ import ch.qos.logback.classic.pattern.ClassOfCallerConverter;
/*     */ import ch.qos.logback.classic.pattern.ContextNameConverter;
/*     */ import ch.qos.logback.classic.pattern.DateConverter;
/*     */ import ch.qos.logback.classic.pattern.EnsureExceptionHandling;
/*     */ import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
/*     */ import ch.qos.logback.classic.pattern.FileOfCallerConverter;
/*     */ import ch.qos.logback.classic.pattern.LevelConverter;
/*     */ import ch.qos.logback.classic.pattern.LineOfCallerConverter;
/*     */ import ch.qos.logback.classic.pattern.LineSeparatorConverter;
/*     */ import ch.qos.logback.classic.pattern.LocalSequenceNumberConverter;
/*     */ import ch.qos.logback.classic.pattern.LoggerConverter;
/*     */ import ch.qos.logback.classic.pattern.MDCConverter;
/*     */ import ch.qos.logback.classic.pattern.MarkerConverter;
/*     */ import ch.qos.logback.classic.pattern.MessageConverter;
/*     */ import ch.qos.logback.classic.pattern.MethodOfCallerConverter;
/*     */ import ch.qos.logback.classic.pattern.NopThrowableInformationConverter;
/*     */ import ch.qos.logback.classic.pattern.PropertyConverter;
/*     */ import ch.qos.logback.classic.pattern.RelativeTimeConverter;
/*     */ import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
/*     */ import ch.qos.logback.classic.pattern.ThreadConverter;
/*     */ import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.BoldBlueCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.BoldMagentaCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.BoldWhiteCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.BoldYellowCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.CyanCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.GreenCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.MagentaCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.RedCompositeConverter;
/*     */ import ch.qos.logback.core.pattern.color.YellowCompositeConverter;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class PatternLayout extends ch.qos.logback.core.pattern.PatternLayoutBase<ILoggingEvent>
/*     */ {
/*  41 */   public static final Map<String, String> defaultConverterMap = new HashMap();
/*     */   public static final String HEADER_PREFIX = "#logback.classic pattern: ";
/*     */   
/*     */   static {
/*  45 */     defaultConverterMap.putAll(ch.qos.logback.core.pattern.parser.Parser.DEFAULT_COMPOSITE_CONVERTER_MAP);
/*     */     
/*  47 */     defaultConverterMap.put("d", DateConverter.class.getName());
/*  48 */     defaultConverterMap.put("date", DateConverter.class.getName());
/*     */     
/*  50 */     defaultConverterMap.put("r", RelativeTimeConverter.class.getName());
/*  51 */     defaultConverterMap.put("relative", RelativeTimeConverter.class.getName());
/*     */     
/*  53 */     defaultConverterMap.put("level", LevelConverter.class.getName());
/*  54 */     defaultConverterMap.put("le", LevelConverter.class.getName());
/*  55 */     defaultConverterMap.put("p", LevelConverter.class.getName());
/*     */     
/*  57 */     defaultConverterMap.put("t", ThreadConverter.class.getName());
/*  58 */     defaultConverterMap.put("thread", ThreadConverter.class.getName());
/*     */     
/*  60 */     defaultConverterMap.put("lo", LoggerConverter.class.getName());
/*  61 */     defaultConverterMap.put("logger", LoggerConverter.class.getName());
/*  62 */     defaultConverterMap.put("c", LoggerConverter.class.getName());
/*     */     
/*  64 */     defaultConverterMap.put("m", MessageConverter.class.getName());
/*  65 */     defaultConverterMap.put("msg", MessageConverter.class.getName());
/*  66 */     defaultConverterMap.put("message", MessageConverter.class.getName());
/*     */     
/*  68 */     defaultConverterMap.put("C", ClassOfCallerConverter.class.getName());
/*  69 */     defaultConverterMap.put("class", ClassOfCallerConverter.class.getName());
/*     */     
/*  71 */     defaultConverterMap.put("M", MethodOfCallerConverter.class.getName());
/*  72 */     defaultConverterMap.put("method", MethodOfCallerConverter.class.getName());
/*     */     
/*  74 */     defaultConverterMap.put("L", LineOfCallerConverter.class.getName());
/*  75 */     defaultConverterMap.put("line", LineOfCallerConverter.class.getName());
/*     */     
/*  77 */     defaultConverterMap.put("F", FileOfCallerConverter.class.getName());
/*  78 */     defaultConverterMap.put("file", FileOfCallerConverter.class.getName());
/*     */     
/*  80 */     defaultConverterMap.put("X", MDCConverter.class.getName());
/*  81 */     defaultConverterMap.put("mdc", MDCConverter.class.getName());
/*     */     
/*  83 */     defaultConverterMap.put("ex", ThrowableProxyConverter.class.getName());
/*  84 */     defaultConverterMap.put("exception", ThrowableProxyConverter.class.getName());
/*  85 */     defaultConverterMap.put("rEx", RootCauseFirstThrowableProxyConverter.class.getName());
/*  86 */     defaultConverterMap.put("rootException", RootCauseFirstThrowableProxyConverter.class.getName());
/*  87 */     defaultConverterMap.put("throwable", ThrowableProxyConverter.class.getName());
/*     */     
/*  89 */     defaultConverterMap.put("xEx", ExtendedThrowableProxyConverter.class.getName());
/*  90 */     defaultConverterMap.put("xException", ExtendedThrowableProxyConverter.class.getName());
/*  91 */     defaultConverterMap.put("xThrowable", ExtendedThrowableProxyConverter.class.getName());
/*     */     
/*  93 */     defaultConverterMap.put("nopex", NopThrowableInformationConverter.class.getName());
/*  94 */     defaultConverterMap.put("nopexception", NopThrowableInformationConverter.class.getName());
/*     */     
/*  96 */     defaultConverterMap.put("cn", ContextNameConverter.class.getName());
/*  97 */     defaultConverterMap.put("contextName", ContextNameConverter.class.getName());
/*     */     
/*  99 */     defaultConverterMap.put("caller", CallerDataConverter.class.getName());
/*     */     
/* 101 */     defaultConverterMap.put("marker", MarkerConverter.class.getName());
/*     */     
/* 103 */     defaultConverterMap.put("property", PropertyConverter.class.getName());
/*     */     
/* 105 */     defaultConverterMap.put("n", LineSeparatorConverter.class.getName());
/*     */     
/* 107 */     defaultConverterMap.put("black", BlackCompositeConverter.class.getName());
/* 108 */     defaultConverterMap.put("red", RedCompositeConverter.class.getName());
/* 109 */     defaultConverterMap.put("green", GreenCompositeConverter.class.getName());
/* 110 */     defaultConverterMap.put("yellow", YellowCompositeConverter.class.getName());
/* 111 */     defaultConverterMap.put("blue", ch.qos.logback.core.pattern.color.BlueCompositeConverter.class.getName());
/* 112 */     defaultConverterMap.put("magenta", MagentaCompositeConverter.class.getName());
/* 113 */     defaultConverterMap.put("cyan", CyanCompositeConverter.class.getName());
/* 114 */     defaultConverterMap.put("white", ch.qos.logback.core.pattern.color.WhiteCompositeConverter.class.getName());
/* 115 */     defaultConverterMap.put("gray", ch.qos.logback.core.pattern.color.GrayCompositeConverter.class.getName());
/* 116 */     defaultConverterMap.put("boldRed", ch.qos.logback.core.pattern.color.BoldRedCompositeConverter.class.getName());
/* 117 */     defaultConverterMap.put("boldGreen", ch.qos.logback.core.pattern.color.BoldGreenCompositeConverter.class.getName());
/* 118 */     defaultConverterMap.put("boldYellow", BoldYellowCompositeConverter.class.getName());
/* 119 */     defaultConverterMap.put("boldBlue", BoldBlueCompositeConverter.class.getName());
/* 120 */     defaultConverterMap.put("boldMagenta", BoldMagentaCompositeConverter.class.getName());
/* 121 */     defaultConverterMap.put("boldCyan", ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter.class.getName());
/* 122 */     defaultConverterMap.put("boldWhite", BoldWhiteCompositeConverter.class.getName());
/* 123 */     defaultConverterMap.put("highlight", ch.qos.logback.classic.pattern.color.HighlightingCompositeConverter.class.getName());
/*     */     
/* 125 */     defaultConverterMap.put("lsn", LocalSequenceNumberConverter.class.getName());
/*     */   }
/*     */   
/*     */   public PatternLayout()
/*     */   {
/* 130 */     this.postCompileProcessor = new EnsureExceptionHandling();
/*     */   }
/*     */   
/*     */   public Map<String, String> getDefaultConverterMap() {
/* 134 */     return defaultConverterMap;
/*     */   }
/*     */   
/*     */   public String doLayout(ILoggingEvent event) {
/* 138 */     if (!isStarted()) {
/* 139 */       return "";
/*     */     }
/* 141 */     return writeLoopOnConverters(event);
/*     */   }
/*     */   
/*     */   protected String getPresentationHeaderPrefix()
/*     */   {
/* 146 */     return "#logback.classic pattern: ";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\PatternLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */