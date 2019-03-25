/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
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
/*     */ public final class SSIFsize
/*     */   implements SSICommand
/*     */ {
/*     */   static final int ONE_KILOBYTE = 1024;
/*     */   static final int ONE_MEGABYTE = 1048576;
/*     */   
/*     */   public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer)
/*     */   {
/*  42 */     long lastModified = 0L;
/*  43 */     String configErrMsg = ssiMediator.getConfigErrMsg();
/*  44 */     for (int i = 0; i < paramNames.length; i++) {
/*  45 */       String paramName = paramNames[i];
/*  46 */       String paramValue = paramValues[i];
/*     */       
/*  48 */       String substitutedValue = ssiMediator.substituteVariables(paramValue);
/*     */       try {
/*  50 */         if ((paramName.equalsIgnoreCase("file")) || 
/*  51 */           (paramName.equalsIgnoreCase("virtual"))) {
/*  52 */           boolean virtual = paramName.equalsIgnoreCase("virtual");
/*  53 */           lastModified = ssiMediator.getFileLastModified(substitutedValue, virtual);
/*     */           
/*  55 */           long size = ssiMediator.getFileSize(substitutedValue, virtual);
/*     */           
/*  57 */           String configSizeFmt = ssiMediator.getConfigSizeFmt();
/*  58 */           writer.write(formatSize(size, configSizeFmt));
/*     */         } else {
/*  60 */           ssiMediator.log("#fsize--Invalid attribute: " + paramName);
/*  61 */           writer.write(configErrMsg);
/*     */         }
/*     */       } catch (IOException e) {
/*  64 */         ssiMediator.log("#fsize--Couldn't get size for file: " + substitutedValue, e);
/*     */         
/*  66 */         writer.write(configErrMsg);
/*     */       }
/*     */     }
/*  69 */     return lastModified;
/*     */   }
/*     */   
/*     */   public String repeat(char aChar, int numChars)
/*     */   {
/*  74 */     if (numChars < 0) {
/*  75 */       throw new IllegalArgumentException("Num chars can't be negative");
/*     */     }
/*  77 */     StringBuilder buf = new StringBuilder();
/*  78 */     for (int i = 0; i < numChars; i++) {
/*  79 */       buf.append(aChar);
/*     */     }
/*  81 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String padLeft(String str, int maxChars)
/*     */   {
/*  86 */     String result = str;
/*  87 */     int charsToAdd = maxChars - str.length();
/*  88 */     if (charsToAdd > 0) {
/*  89 */       result = repeat(' ', charsToAdd) + str;
/*     */     }
/*  91 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String formatSize(long size, String format)
/*     */   {
/*  98 */     String retString = "";
/*  99 */     if (format.equalsIgnoreCase("bytes")) {
/* 100 */       DecimalFormat decimalFormat = new DecimalFormat("#,##0");
/* 101 */       retString = decimalFormat.format(size);
/*     */     } else {
/* 103 */       if (size == 0L) {
/* 104 */         retString = "0k";
/* 105 */       } else if (size < 1024L) {
/* 106 */         retString = "1k";
/* 107 */       } else if (size < 1048576L) {
/* 108 */         retString = Long.toString((size + 512L) / 1024L);
/* 109 */         retString = retString + "k";
/* 110 */       } else if (size < 103809024L) {
/* 111 */         DecimalFormat decimalFormat = new DecimalFormat("0.0M");
/* 112 */         retString = decimalFormat.format(size / 1048576.0D);
/*     */       } else {
/* 114 */         retString = Long.toString((size + 541696L) / 1048576L);
/*     */         
/* 116 */         retString = retString + "M";
/*     */       }
/* 118 */       retString = padLeft(retString, 5);
/*     */     }
/* 120 */     return retString;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIFsize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */