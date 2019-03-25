/*     */ package org.apache.tomcat.util.http;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class HttpMessages
/*     */ {
/*  36 */   private static final Map<Locale, HttpMessages> instances = new ConcurrentHashMap();
/*     */   
/*     */ 
/*  39 */   private static final HttpMessages DEFAULT = new HttpMessages(
/*  40 */     StringManager.getManager("org.apache.tomcat.util.http.res", 
/*  41 */     Locale.getDefault()));
/*     */   
/*     */ 
/*     */   private final StringManager sm;
/*     */   
/*     */ 
/*  47 */   private String st_200 = null;
/*  48 */   private String st_302 = null;
/*  49 */   private String st_400 = null;
/*  50 */   private String st_404 = null;
/*  51 */   private String st_500 = null;
/*     */   
/*     */   private HttpMessages(StringManager sm) {
/*  54 */     this.sm = sm;
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
/*     */   public String getMessage(int status)
/*     */   {
/*  72 */     switch (status) {
/*     */     case 200: 
/*  74 */       if (this.st_200 == null) {
/*  75 */         this.st_200 = this.sm.getString("sc.200");
/*     */       }
/*  77 */       return this.st_200;
/*     */     case 302: 
/*  79 */       if (this.st_302 == null) {
/*  80 */         this.st_302 = this.sm.getString("sc.302");
/*     */       }
/*  82 */       return this.st_302;
/*     */     case 400: 
/*  84 */       if (this.st_400 == null) {
/*  85 */         this.st_400 = this.sm.getString("sc.400");
/*     */       }
/*  87 */       return this.st_400;
/*     */     case 404: 
/*  89 */       if (this.st_404 == null) {
/*  90 */         this.st_404 = this.sm.getString("sc.404");
/*     */       }
/*  92 */       return this.st_404;
/*     */     case 500: 
/*  94 */       if (this.st_500 == null) {
/*  95 */         this.st_500 = this.sm.getString("sc.500");
/*     */       }
/*  97 */       return this.st_500;
/*     */     }
/*  99 */     return this.sm.getString("sc." + status);
/*     */   }
/*     */   
/*     */   public static HttpMessages getInstance(Locale locale)
/*     */   {
/* 104 */     HttpMessages result = (HttpMessages)instances.get(locale);
/* 105 */     if (result == null) {
/* 106 */       StringManager sm = StringManager.getManager("org.apache.tomcat.util.http.res", locale);
/*     */       
/* 108 */       if (Locale.getDefault().equals(sm.getLocale())) {
/* 109 */         result = DEFAULT;
/*     */       } else {
/* 111 */         result = new HttpMessages(sm);
/*     */       }
/* 113 */       instances.put(locale, result);
/*     */     }
/* 115 */     return result;
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
/*     */   public static boolean isSafeInHttpHeader(String msg)
/*     */   {
/* 130 */     if (msg == null) {
/* 131 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */     int len = msg.length();
/* 140 */     for (int i = 0; i < len; i++) {
/* 141 */       char c = msg.charAt(i);
/* 142 */       if (((' ' > c) || (c > '~')) && (('' > c) || (c > 'ÿ')) && (c != '\t'))
/*     */       {
/*     */ 
/* 145 */         return false;
/*     */       }
/*     */     }
/* 148 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\HttpMessages.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */