/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class CustomBooleanEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   public static final String VALUE_TRUE = "true";
/*     */   public static final String VALUE_FALSE = "false";
/*     */   public static final String VALUE_ON = "on";
/*     */   public static final String VALUE_OFF = "off";
/*     */   public static final String VALUE_YES = "yes";
/*     */   public static final String VALUE_NO = "no";
/*     */   public static final String VALUE_1 = "1";
/*     */   public static final String VALUE_0 = "0";
/*     */   private final String trueString;
/*     */   private final String falseString;
/*     */   private final boolean allowEmpty;
/*     */   
/*     */   public CustomBooleanEditor(boolean allowEmpty)
/*     */   {
/*  69 */     this(null, null, allowEmpty);
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
/*     */   public CustomBooleanEditor(String trueString, String falseString, boolean allowEmpty)
/*     */   {
/*  93 */     this.trueString = trueString;
/*  94 */     this.falseString = falseString;
/*  95 */     this.allowEmpty = allowEmpty;
/*     */   }
/*     */   
/*     */   public void setAsText(String text)
/*     */     throws IllegalArgumentException
/*     */   {
/* 101 */     String input = text != null ? text.trim() : null;
/* 102 */     if ((this.allowEmpty) && (!StringUtils.hasLength(input)))
/*     */     {
/* 104 */       setValue(null);
/*     */     }
/* 106 */     else if ((this.trueString != null) && (this.trueString.equalsIgnoreCase(input))) {
/* 107 */       setValue(Boolean.TRUE);
/*     */     }
/* 109 */     else if ((this.falseString != null) && (this.falseString.equalsIgnoreCase(input))) {
/* 110 */       setValue(Boolean.FALSE);
/*     */     }
/* 112 */     else if ((this.trueString == null) && (
/* 113 */       ("true".equalsIgnoreCase(input)) || ("on".equalsIgnoreCase(input)) || 
/* 114 */       ("yes".equalsIgnoreCase(input)) || ("1".equals(input)))) {
/* 115 */       setValue(Boolean.TRUE);
/*     */     }
/* 117 */     else if ((this.falseString == null) && (
/* 118 */       ("false".equalsIgnoreCase(input)) || ("off".equalsIgnoreCase(input)) || 
/* 119 */       ("no".equalsIgnoreCase(input)) || ("0".equals(input)))) {
/* 120 */       setValue(Boolean.FALSE);
/*     */     }
/*     */     else {
/* 123 */       throw new IllegalArgumentException("Invalid boolean value [" + text + "]");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getAsText()
/*     */   {
/* 129 */     if (Boolean.TRUE.equals(getValue())) {
/* 130 */       return this.trueString != null ? this.trueString : "true";
/*     */     }
/* 132 */     if (Boolean.FALSE.equals(getValue())) {
/* 133 */       return this.falseString != null ? this.falseString : "false";
/*     */     }
/*     */     
/* 136 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\propertyeditors\CustomBooleanEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */