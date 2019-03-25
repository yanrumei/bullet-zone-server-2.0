/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public final class UriUtil
/*     */ {
/*  29 */   private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */   
/*     */ 
/*  32 */   private static final Pattern PATTERN_EXCLAMATION_MARK = Pattern.compile("!/");
/*  33 */   private static final Pattern PATTERN_CARET = Pattern.compile("\\^/");
/*  34 */   private static final Pattern PATTERN_ASTERISK = Pattern.compile("\\*/");
/*     */   private static final Pattern PATTERN_CUSTOM;
/*     */   private static final String REPLACE_CUSTOM;
/*     */   private static final String WAR_SEPARATOR;
/*     */   
/*     */   static
/*     */   {
/*  41 */     String custom = System.getProperty("org.apache.tomcat.util.buf.UriUtil.WAR_SEPARATOR");
/*  42 */     if (custom == null) {
/*  43 */       WAR_SEPARATOR = "*/";
/*  44 */       PATTERN_CUSTOM = null;
/*  45 */       REPLACE_CUSTOM = null;
/*     */     } else {
/*  47 */       WAR_SEPARATOR = custom + "/";
/*  48 */       PATTERN_CUSTOM = Pattern.compile(Pattern.quote(WAR_SEPARATOR));
/*  49 */       StringBuffer sb = new StringBuffer(custom.length() * 3);
/*     */       
/*  51 */       byte[] ba = custom.getBytes();
/*  52 */       for (int j = 0; j < ba.length; j++)
/*     */       {
/*  54 */         byte toEncode = ba[j];
/*  55 */         sb.append('%');
/*  56 */         int low = toEncode & 0xF;
/*  57 */         int high = (toEncode & 0xF0) >> 4;
/*  58 */         sb.append(HEX[high]);
/*  59 */         sb.append(HEX[low]);
/*     */       }
/*  61 */       REPLACE_CUSTOM = sb.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isSchemeChar(char c)
/*     */   {
/*  81 */     return (Character.isLetterOrDigit(c)) || (c == '+') || (c == '-') || (c == '.');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasScheme(CharSequence uri)
/*     */   {
/*  93 */     int len = uri.length();
/*  94 */     for (int i = 0; i < len; i++) {
/*  95 */       char c = uri.charAt(i);
/*  96 */       if (c == ':')
/*  97 */         return i > 0;
/*  98 */       if (!isSchemeChar(c)) {
/*  99 */         return false;
/*     */       }
/*     */     }
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   public static URL buildJarUrl(File jarFile) throws MalformedURLException
/*     */   {
/* 107 */     return buildJarUrl(jarFile, null);
/*     */   }
/*     */   
/*     */   public static URL buildJarUrl(File jarFile, String entryPath) throws MalformedURLException
/*     */   {
/* 112 */     return buildJarUrl(jarFile.toURI().toString(), entryPath);
/*     */   }
/*     */   
/*     */   public static URL buildJarUrl(String fileUrlString) throws MalformedURLException
/*     */   {
/* 117 */     return buildJarUrl(fileUrlString, null);
/*     */   }
/*     */   
/*     */   public static URL buildJarUrl(String fileUrlString, String entryPath) throws MalformedURLException
/*     */   {
/* 122 */     String safeString = makeSafeForJarUrl(fileUrlString);
/* 123 */     StringBuilder sb = new StringBuilder();
/* 124 */     sb.append(safeString);
/* 125 */     sb.append("!/");
/* 126 */     if (entryPath != null) {
/* 127 */       sb.append(makeSafeForJarUrl(entryPath));
/*     */     }
/* 129 */     return new URL("jar", null, -1, sb.toString());
/*     */   }
/*     */   
/*     */   public static URL buildJarSafeUrl(File file) throws MalformedURLException
/*     */   {
/* 134 */     String safe = makeSafeForJarUrl(file.toURI().toString());
/* 135 */     return new URL(safe);
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
/*     */   private static String makeSafeForJarUrl(String input)
/*     */   {
/* 158 */     String tmp = PATTERN_EXCLAMATION_MARK.matcher(input).replaceAll("%21/");
/*     */     
/* 160 */     tmp = PATTERN_CARET.matcher(tmp).replaceAll("%5e/");
/* 161 */     tmp = PATTERN_ASTERISK.matcher(tmp).replaceAll("%2a/");
/* 162 */     if (PATTERN_CUSTOM != null) {
/* 163 */       tmp = PATTERN_CUSTOM.matcher(tmp).replaceAll(REPLACE_CUSTOM);
/*     */     }
/* 165 */     return tmp;
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
/*     */   public static URL warToJar(URL warUrl)
/*     */     throws MalformedURLException
/*     */   {
/* 181 */     String file = warUrl.getFile();
/* 182 */     if (file.contains("*/")) {
/* 183 */       file = file.replaceFirst("\\*/", "!/");
/* 184 */     } else if (file.contains("^/")) {
/* 185 */       file = file.replaceFirst("\\^/", "!/");
/* 186 */     } else if (PATTERN_CUSTOM != null) {
/* 187 */       file = file.replaceFirst(PATTERN_CUSTOM.pattern(), "!/");
/*     */     }
/*     */     
/* 190 */     return new URL("jar", warUrl.getHost(), warUrl.getPort(), file);
/*     */   }
/*     */   
/*     */   public static String getWarSeparator()
/*     */   {
/* 195 */     return WAR_SEPARATOR;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\UriUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */