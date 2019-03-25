/*     */ package org.apache.tomcat.util.http.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map.Entry;
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
/*     */ public class MediaType
/*     */ {
/*     */   private final String type;
/*     */   private final String subtype;
/*     */   private final LinkedHashMap<String, String> parameters;
/*     */   private final String charset;
/*     */   private volatile String noCharset;
/*     */   private volatile String withCharset;
/*     */   
/*     */   protected MediaType(String type, String subtype, LinkedHashMap<String, String> parameters)
/*     */   {
/*  35 */     this.type = type;
/*  36 */     this.subtype = subtype;
/*  37 */     this.parameters = parameters;
/*     */     
/*  39 */     String cs = (String)parameters.get("charset");
/*  40 */     if ((cs != null) && (cs.length() > 0) && (cs.charAt(0) == '"')) {
/*  41 */       cs = HttpParser.unquote(cs);
/*     */     }
/*  43 */     this.charset = cs;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  47 */     return this.type;
/*     */   }
/*     */   
/*     */   public String getSubtype() {
/*  51 */     return this.subtype;
/*     */   }
/*     */   
/*     */   public String getCharset() {
/*  55 */     return this.charset;
/*     */   }
/*     */   
/*     */   public int getParameterCount() {
/*  59 */     return this.parameters.size();
/*     */   }
/*     */   
/*     */   public String getParameterValue(String parameter) {
/*  63 */     return (String)this.parameters.get(parameter.toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  68 */     if (this.withCharset == null) {
/*  69 */       synchronized (this) {
/*  70 */         if (this.withCharset == null) {
/*  71 */           StringBuilder result = new StringBuilder();
/*  72 */           result.append(this.type);
/*  73 */           result.append('/');
/*  74 */           result.append(this.subtype);
/*  75 */           for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
/*  76 */             String value = (String)entry.getValue();
/*  77 */             if ((value != null) && (value.length() != 0))
/*     */             {
/*     */ 
/*  80 */               result.append(';');
/*     */               
/*     */ 
/*     */ 
/*  84 */               result.append(' ');
/*  85 */               result.append((String)entry.getKey());
/*  86 */               result.append('=');
/*  87 */               result.append(value);
/*     */             }
/*     */           }
/*  90 */           this.withCharset = result.toString();
/*     */         }
/*     */       }
/*     */     }
/*  94 */     return this.withCharset;
/*     */   }
/*     */   
/*     */   public String toStringNoCharset() {
/*  98 */     if (this.noCharset == null) {
/*  99 */       synchronized (this) {
/* 100 */         if (this.noCharset == null) {
/* 101 */           StringBuilder result = new StringBuilder();
/* 102 */           result.append(this.type);
/* 103 */           result.append('/');
/* 104 */           result.append(this.subtype);
/* 105 */           for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
/* 106 */             if (!((String)entry.getKey()).equalsIgnoreCase("charset"))
/*     */             {
/*     */ 
/* 109 */               result.append(';');
/*     */               
/*     */ 
/*     */ 
/* 113 */               result.append(' ');
/* 114 */               result.append((String)entry.getKey());
/* 115 */               result.append('=');
/* 116 */               result.append((String)entry.getValue());
/*     */             }
/*     */           }
/* 119 */           this.noCharset = result.toString();
/*     */         }
/*     */       }
/*     */     }
/* 123 */     return this.noCharset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MediaType parseMediaType(StringReader input)
/*     */     throws IOException
/*     */   {
/* 136 */     String type = HttpParser.readToken(input);
/* 137 */     if ((type == null) || (type.length() == 0)) {
/* 138 */       return null;
/*     */     }
/*     */     
/* 141 */     if (HttpParser.skipConstant(input, "/") == SkipResult.NOT_FOUND) {
/* 142 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 146 */     String subtype = HttpParser.readToken(input);
/* 147 */     if ((subtype == null) || (subtype.length() == 0)) {
/* 148 */       return null;
/*     */     }
/*     */     
/* 151 */     LinkedHashMap<String, String> parameters = new LinkedHashMap();
/*     */     
/* 153 */     SkipResult lookForSemiColon = HttpParser.skipConstant(input, ";");
/* 154 */     if (lookForSemiColon == SkipResult.NOT_FOUND) {
/* 155 */       return null;
/*     */     }
/* 157 */     while (lookForSemiColon == SkipResult.FOUND) {
/* 158 */       String attribute = HttpParser.readToken(input);
/*     */       
/* 160 */       String value = "";
/* 161 */       if (HttpParser.skipConstant(input, "=") == SkipResult.FOUND) {
/* 162 */         value = HttpParser.readTokenOrQuotedString(input, true);
/*     */       }
/*     */       
/* 165 */       if (attribute != null) {
/* 166 */         parameters.put(attribute.toLowerCase(Locale.ENGLISH), value);
/*     */       }
/*     */       
/* 169 */       lookForSemiColon = HttpParser.skipConstant(input, ";");
/* 170 */       if (lookForSemiColon == SkipResult.NOT_FOUND) {
/* 171 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 175 */     return new MediaType(type, subtype, parameters);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\parser\MediaType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */