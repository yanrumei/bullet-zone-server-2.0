/*     */ package org.apache.el.util;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
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
/*     */ public class Validation
/*     */ {
/*  26 */   private static final String[] invalidIdentifiers = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while" };
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
/*  37 */   private static final boolean IS_SECURITY_ENABLED = System.getSecurityManager() != null;
/*     */   private static final boolean SKIP_IDENTIFIER_CHECK;
/*     */   
/*     */   static {
/*     */     String skipIdentifierCheckStr;
/*     */     String skipIdentifierCheckStr;
/*  43 */     if (IS_SECURITY_ENABLED) {
/*  44 */       skipIdentifierCheckStr = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run()
/*     */         {
/*  48 */           return System.getProperty("org.apache.el.parser.SKIP_IDENTIFIER_CHECK", "false");
/*     */         }
/*     */         
/*     */ 
/*     */       });
/*     */     }
/*     */     else {
/*  55 */       skipIdentifierCheckStr = System.getProperty("org.apache.el.parser.SKIP_IDENTIFIER_CHECK", "false");
/*     */     }
/*     */     
/*  58 */     SKIP_IDENTIFIER_CHECK = Boolean.parseBoolean(skipIdentifierCheckStr);
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
/*     */   public static boolean isIdentifier(String key)
/*     */   {
/*  78 */     if (SKIP_IDENTIFIER_CHECK) {
/*  79 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  83 */     if ((key == null) || (key.length() == 0)) {
/*  84 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  88 */     int i = 0;
/*  89 */     int j = invalidIdentifiers.length;
/*  90 */     while (i < j) {
/*  91 */       int k = i + j >>> 1;
/*  92 */       int result = invalidIdentifiers[k].compareTo(key);
/*  93 */       if (result == 0) {
/*  94 */         return false;
/*     */       }
/*  96 */       if (result < 0) {
/*  97 */         i = k + 1;
/*     */       } else {
/*  99 */         j = k;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 104 */     if (!Character.isJavaIdentifierStart(key.charAt(0))) {
/* 105 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 109 */     for (int idx = 1; idx < key.length(); idx++) {
/* 110 */       if (!Character.isJavaIdentifierPart(key.charAt(idx))) {
/* 111 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 115 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\e\\util\Validation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */