/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.serializer.AnchorGenerator;
/*     */ import org.yaml.snakeyaml.serializer.NumberAnchorGenerator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DumperOptions
/*     */ {
/*     */   public static enum ScalarStyle
/*     */   {
/*  39 */     DOUBLE_QUOTED(Character.valueOf('"')),  SINGLE_QUOTED(Character.valueOf('\'')),  LITERAL(Character.valueOf('|')), 
/*  40 */     FOLDED(Character.valueOf('>')),  PLAIN(null);
/*     */     
/*     */     private Character styleChar;
/*     */     
/*  44 */     private ScalarStyle(Character style) { this.styleChar = style; }
/*     */     
/*     */     public Character getChar()
/*     */     {
/*  48 */       return this.styleChar;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  53 */       return "Scalar style: '" + this.styleChar + "'";
/*     */     }
/*     */     
/*     */     public static ScalarStyle createStyle(Character style) {
/*  57 */       if (style == null) {
/*  58 */         return PLAIN;
/*     */       }
/*  60 */       switch (style.charValue()) {
/*     */       case '"': 
/*  62 */         return DOUBLE_QUOTED;
/*     */       case '\'': 
/*  64 */         return SINGLE_QUOTED;
/*     */       case '|': 
/*  66 */         return LITERAL;
/*     */       case '>': 
/*  68 */         return FOLDED;
/*     */       }
/*  70 */       throw new YAMLException("Unknown scalar style character: " + style);
/*     */     }
/*     */   }
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
/*     */   public static enum FlowStyle
/*     */   {
/*  85 */     FLOW(Boolean.TRUE),  BLOCK(Boolean.FALSE),  AUTO(null);
/*     */     
/*     */     private Boolean styleBoolean;
/*     */     
/*     */     private FlowStyle(Boolean flowStyle) {
/*  90 */       this.styleBoolean = flowStyle;
/*     */     }
/*     */     
/*     */     public Boolean getStyleBoolean() {
/*  94 */       return this.styleBoolean;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  99 */       return "Flow style: '" + this.styleBoolean + "'";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum LineBreak
/*     */   {
/* 107 */     WIN("\r\n"),  MAC("\r"),  UNIX("\n");
/*     */     
/*     */     private String lineBreak;
/*     */     
/*     */     private LineBreak(String lineBreak) {
/* 112 */       this.lineBreak = lineBreak;
/*     */     }
/*     */     
/*     */     public String getString() {
/* 116 */       return this.lineBreak;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 121 */       return "Line break: " + name();
/*     */     }
/*     */     
/*     */     public static LineBreak getPlatformLineBreak() {
/* 125 */       String platformLineBreak = System.getProperty("line.separator");
/* 126 */       for (LineBreak lb : values()) {
/* 127 */         if (lb.lineBreak.equals(platformLineBreak)) {
/* 128 */           return lb;
/*     */         }
/*     */       }
/* 131 */       return UNIX;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Version
/*     */   {
/* 139 */     V1_0(new Integer[] { Integer.valueOf(1), Integer.valueOf(0) }),  V1_1(new Integer[] { Integer.valueOf(1), Integer.valueOf(1) });
/*     */     
/*     */     private Integer[] version;
/*     */     
/*     */     private Version(Integer[] version) {
/* 144 */       this.version = version;
/*     */     }
/*     */     
/* 147 */     public int major() { return this.version[0].intValue(); }
/* 148 */     public int minor() { return this.version[1].intValue(); }
/*     */     
/*     */     public String getRepresentation() {
/* 151 */       return this.version[0] + "." + this.version[1];
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 156 */       return "Version: " + getRepresentation();
/*     */     }
/*     */   }
/*     */   
/* 160 */   private ScalarStyle defaultStyle = ScalarStyle.PLAIN;
/* 161 */   private FlowStyle defaultFlowStyle = FlowStyle.AUTO;
/* 162 */   private boolean canonical = false;
/* 163 */   private boolean allowUnicode = true;
/* 164 */   private boolean allowReadOnlyProperties = false;
/* 165 */   private int indent = 2;
/* 166 */   private int indicatorIndent = 0;
/* 167 */   private int bestWidth = 80;
/* 168 */   private boolean splitLines = true;
/* 169 */   private LineBreak lineBreak = LineBreak.UNIX;
/* 170 */   private boolean explicitStart = false;
/* 171 */   private boolean explicitEnd = false;
/* 172 */   private TimeZone timeZone = null;
/*     */   
/* 174 */   private Version version = null;
/* 175 */   private Map<String, String> tags = null;
/* 176 */   private Boolean prettyFlow = Boolean.valueOf(false);
/* 177 */   private AnchorGenerator anchorGenerator = new NumberAnchorGenerator(0);
/*     */   
/*     */   public boolean isAllowUnicode() {
/* 180 */     return this.allowUnicode;
/*     */   }
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
/*     */   public void setAllowUnicode(boolean allowUnicode)
/*     */   {
/* 194 */     this.allowUnicode = allowUnicode;
/*     */   }
/*     */   
/*     */   public ScalarStyle getDefaultScalarStyle() {
/* 198 */     return this.defaultStyle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultScalarStyle(ScalarStyle defaultStyle)
/*     */   {
/* 209 */     if (defaultStyle == null) {
/* 210 */       throw new NullPointerException("Use ScalarStyle enum.");
/*     */     }
/* 212 */     this.defaultStyle = defaultStyle;
/*     */   }
/*     */   
/*     */   public void setIndent(int indent) {
/* 216 */     if (indent < 1) {
/* 217 */       throw new YAMLException("Indent must be at least 1");
/*     */     }
/* 219 */     if (indent > 10) {
/* 220 */       throw new YAMLException("Indent must be at most 10");
/*     */     }
/* 222 */     this.indent = indent;
/*     */   }
/*     */   
/*     */   public int getIndent() {
/* 226 */     return this.indent;
/*     */   }
/*     */   
/*     */   public void setIndicatorIndent(int indicatorIndent) {
/* 230 */     if (indicatorIndent < 0) {
/* 231 */       throw new YAMLException("Indicator indent must be non-negative.");
/*     */     }
/* 233 */     if (indicatorIndent > 9) {
/* 234 */       throw new YAMLException("Indicator indent must be at most Emitter.MAX_INDENT-1: 9");
/*     */     }
/* 236 */     this.indicatorIndent = indicatorIndent;
/*     */   }
/*     */   
/*     */   public int getIndicatorIndent() {
/* 240 */     return this.indicatorIndent;
/*     */   }
/*     */   
/*     */   public void setVersion(Version version) {
/* 244 */     this.version = version;
/*     */   }
/*     */   
/*     */   public Version getVersion() {
/* 248 */     return this.version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCanonical(boolean canonical)
/*     */   {
/* 258 */     this.canonical = canonical;
/*     */   }
/*     */   
/*     */   public boolean isCanonical() {
/* 262 */     return this.canonical;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPrettyFlow(boolean prettyFlow)
/*     */   {
/* 273 */     this.prettyFlow = Boolean.valueOf(prettyFlow);
/*     */   }
/*     */   
/*     */   public boolean isPrettyFlow() {
/* 277 */     return this.prettyFlow.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWidth(int bestWidth)
/*     */   {
/* 289 */     this.bestWidth = bestWidth;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 293 */     return this.bestWidth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSplitLines(boolean splitLines)
/*     */   {
/* 304 */     this.splitLines = splitLines;
/*     */   }
/*     */   
/*     */   public boolean getSplitLines() {
/* 308 */     return this.splitLines;
/*     */   }
/*     */   
/*     */   public LineBreak getLineBreak() {
/* 312 */     return this.lineBreak;
/*     */   }
/*     */   
/*     */   public void setDefaultFlowStyle(FlowStyle defaultFlowStyle) {
/* 316 */     if (defaultFlowStyle == null) {
/* 317 */       throw new NullPointerException("Use FlowStyle enum.");
/*     */     }
/* 319 */     this.defaultFlowStyle = defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public FlowStyle getDefaultFlowStyle() {
/* 323 */     return this.defaultFlowStyle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLineBreak(LineBreak lineBreak)
/*     */   {
/* 332 */     if (lineBreak == null) {
/* 333 */       throw new NullPointerException("Specify line break.");
/*     */     }
/* 335 */     this.lineBreak = lineBreak;
/*     */   }
/*     */   
/*     */   public boolean isExplicitStart() {
/* 339 */     return this.explicitStart;
/*     */   }
/*     */   
/*     */   public void setExplicitStart(boolean explicitStart) {
/* 343 */     this.explicitStart = explicitStart;
/*     */   }
/*     */   
/*     */   public boolean isExplicitEnd() {
/* 347 */     return this.explicitEnd;
/*     */   }
/*     */   
/*     */   public void setExplicitEnd(boolean explicitEnd) {
/* 351 */     this.explicitEnd = explicitEnd;
/*     */   }
/*     */   
/*     */   public Map<String, String> getTags() {
/* 355 */     return this.tags;
/*     */   }
/*     */   
/*     */   public void setTags(Map<String, String> tags)
/*     */   {
/* 360 */     this.tags = tags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAllowReadOnlyProperties()
/*     */   {
/* 370 */     return this.allowReadOnlyProperties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties)
/*     */   {
/* 382 */     this.allowReadOnlyProperties = allowReadOnlyProperties;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 386 */     return this.timeZone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeZone(TimeZone timeZone)
/*     */   {
/* 394 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */   public AnchorGenerator getAnchorGenerator()
/*     */   {
/* 399 */     return this.anchorGenerator;
/*     */   }
/*     */   
/*     */   public void setAnchorGenerator(AnchorGenerator anchorGenerator) {
/* 403 */     this.anchorGenerator = anchorGenerator;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\DumperOptions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */