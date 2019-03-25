/*     */ package ch.qos.logback.core.pattern;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.LayoutBase;
/*     */ import ch.qos.logback.core.pattern.parser.Node;
/*     */ import ch.qos.logback.core.pattern.parser.Parser;
/*     */ import ch.qos.logback.core.spi.ScanException;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class PatternLayoutBase<E>
/*     */   extends LayoutBase<E>
/*     */ {
/*     */   static final int INTIAL_STRING_BUILDER_SIZE = 256;
/*     */   Converter<E> head;
/*     */   String pattern;
/*     */   protected PostCompileProcessor<E> postCompileProcessor;
/*  36 */   Map<String, String> instanceConverterMap = new HashMap();
/*  37 */   protected boolean outputPatternAsHeader = false;
/*     */   
/*  39 */   StringBuilder recycledStringBuilder = new StringBuilder(256);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Map<String, String> getDefaultConverterMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getEffectiveConverterMap()
/*     */   {
/*  54 */     Map<String, String> effectiveMap = new HashMap();
/*     */     
/*     */ 
/*  57 */     Map<String, String> defaultMap = getDefaultConverterMap();
/*  58 */     if (defaultMap != null) {
/*  59 */       effectiveMap.putAll(defaultMap);
/*     */     }
/*     */     
/*     */ 
/*  63 */     Context context = getContext();
/*  64 */     if (context != null)
/*     */     {
/*  66 */       Map<String, String> contextMap = (Map)context.getObject("PATTERN_RULE_REGISTRY");
/*  67 */       if (contextMap != null) {
/*  68 */         effectiveMap.putAll(contextMap);
/*     */       }
/*     */     }
/*     */     
/*  72 */     effectiveMap.putAll(this.instanceConverterMap);
/*  73 */     return effectiveMap;
/*     */   }
/*     */   
/*     */   public void start() {
/*  77 */     if ((this.pattern == null) || (this.pattern.length() == 0)) {
/*  78 */       addError("Empty or null pattern.");
/*  79 */       return;
/*     */     }
/*     */     try {
/*  82 */       Parser<E> p = new Parser(this.pattern);
/*  83 */       if (getContext() != null) {
/*  84 */         p.setContext(getContext());
/*     */       }
/*  86 */       Node t = p.parse();
/*  87 */       this.head = p.compile(t, getEffectiveConverterMap());
/*  88 */       if (this.postCompileProcessor != null) {
/*  89 */         this.postCompileProcessor.process(this.context, this.head);
/*     */       }
/*  91 */       ConverterUtil.setContextForConverters(getContext(), this.head);
/*  92 */       ConverterUtil.startConverters(this.head);
/*  93 */       super.start();
/*     */     } catch (ScanException sce) {
/*  95 */       StatusManager sm = getContext().getStatusManager();
/*  96 */       sm.add(new ErrorStatus("Failed to parse pattern \"" + getPattern() + "\".", this, sce));
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPostCompileProcessor(PostCompileProcessor<E> postCompileProcessor) {
/* 101 */     this.postCompileProcessor = postCompileProcessor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void setContextForConverters(Converter<E> head)
/*     */   {
/* 111 */     ConverterUtil.setContextForConverters(getContext(), head);
/*     */   }
/*     */   
/*     */   protected String writeLoopOnConverters(E event) {
/* 115 */     StringBuilder strBuilder = new StringBuilder(256);
/* 116 */     Converter<E> c = this.head;
/* 117 */     while (c != null) {
/* 118 */       c.write(strBuilder, event);
/* 119 */       c = c.getNext();
/*     */     }
/* 121 */     return strBuilder.toString();
/*     */   }
/*     */   
/*     */   public String getPattern() {
/* 125 */     return this.pattern;
/*     */   }
/*     */   
/*     */   public void setPattern(String pattern) {
/* 129 */     this.pattern = pattern;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 133 */     return getClass().getName() + "(\"" + getPattern() + "\")";
/*     */   }
/*     */   
/*     */   public Map<String, String> getInstanceConverterMap() {
/* 137 */     return this.instanceConverterMap;
/*     */   }
/*     */   
/*     */   protected String getPresentationHeaderPrefix() {
/* 141 */     return "";
/*     */   }
/*     */   
/*     */   public boolean isOutputPatternAsHeader() {
/* 145 */     return this.outputPatternAsHeader;
/*     */   }
/*     */   
/*     */   public void setOutputPatternAsHeader(boolean outputPatternAsHeader) {
/* 149 */     this.outputPatternAsHeader = outputPatternAsHeader;
/*     */   }
/*     */   
/*     */   public String getPresentationHeader()
/*     */   {
/* 154 */     if (this.outputPatternAsHeader) {
/* 155 */       return getPresentationHeaderPrefix() + this.pattern;
/*     */     }
/* 157 */     return super.getPresentationHeader();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\pattern\PatternLayoutBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */