/*     */ package org.springframework.boot.ansi;
/*     */ 
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AnsiOutput
/*     */ {
/*     */   private static final String ENCODE_JOIN = ";";
/*  31 */   private static Enabled enabled = Enabled.DETECT;
/*     */   
/*     */   private static Boolean consoleAvailable;
/*     */   
/*     */   private static Boolean ansiCapable;
/*     */   
/*  37 */   private static final String OPERATING_SYSTEM_NAME = System.getProperty("os.name")
/*  38 */     .toLowerCase();
/*     */   
/*     */   private static final String ENCODE_START = "\033[";
/*     */   
/*     */   private static final String ENCODE_END = "m";
/*     */   
/*  44 */   private static final String RESET = "0;" + AnsiColor.DEFAULT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setEnabled(Enabled enabled)
/*     */   {
/*  51 */     Assert.notNull(enabled, "Enabled must not be null");
/*  52 */     enabled = enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setConsoleAvailable(Boolean consoleAvailable)
/*     */   {
/*  61 */     consoleAvailable = consoleAvailable;
/*     */   }
/*     */   
/*     */   static Enabled getEnabled() {
/*  65 */     return enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String encode(AnsiElement element)
/*     */   {
/*  74 */     if (isEnabled()) {
/*  75 */       return "\033[" + element + "m";
/*     */     }
/*  77 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Object... elements)
/*     */   {
/*  87 */     StringBuilder sb = new StringBuilder();
/*  88 */     if (isEnabled()) {
/*  89 */       buildEnabled(sb, elements);
/*     */     }
/*     */     else {
/*  92 */       buildDisabled(sb, elements);
/*     */     }
/*  94 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static void buildEnabled(StringBuilder sb, Object[] elements) {
/*  98 */     boolean writingAnsi = false;
/*  99 */     boolean containsEncoding = false;
/* 100 */     for (Object element : elements) {
/* 101 */       if ((element instanceof AnsiElement)) {
/* 102 */         containsEncoding = true;
/* 103 */         if (!writingAnsi) {
/* 104 */           sb.append("\033[");
/* 105 */           writingAnsi = true;
/*     */         }
/*     */         else {
/* 108 */           sb.append(";");
/*     */         }
/*     */         
/*     */       }
/* 112 */       else if (writingAnsi) {
/* 113 */         sb.append("m");
/* 114 */         writingAnsi = false;
/*     */       }
/*     */       
/* 117 */       sb.append(element);
/*     */     }
/* 119 */     if (containsEncoding) {
/* 120 */       sb.append(writingAnsi ? ";" : "\033[");
/* 121 */       sb.append(RESET);
/* 122 */       sb.append("m");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void buildDisabled(StringBuilder sb, Object[] elements) {
/* 127 */     for (Object element : elements) {
/* 128 */       if ((!(element instanceof AnsiElement)) && (element != null)) {
/* 129 */         sb.append(element);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isEnabled() {
/* 135 */     if (enabled == Enabled.DETECT) {
/* 136 */       if (ansiCapable == null) {
/* 137 */         ansiCapable = Boolean.valueOf(detectIfAnsiCapable());
/*     */       }
/* 139 */       return ansiCapable.booleanValue();
/*     */     }
/* 141 */     return enabled == Enabled.ALWAYS;
/*     */   }
/*     */   
/*     */   private static boolean detectIfAnsiCapable() {
/*     */     try {
/* 146 */       if (Boolean.FALSE.equals(consoleAvailable)) {
/* 147 */         return false;
/*     */       }
/* 149 */       if ((consoleAvailable == null) && (System.console() == null)) {
/* 150 */         return false;
/*     */       }
/* 152 */       return OPERATING_SYSTEM_NAME.indexOf("win") < 0;
/*     */     }
/*     */     catch (Throwable ex) {}
/* 155 */     return false;
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
/*     */   public static enum Enabled
/*     */   {
/* 169 */     DETECT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 174 */     ALWAYS, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 179 */     NEVER;
/*     */     
/*     */     private Enabled() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ansi\AnsiOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */