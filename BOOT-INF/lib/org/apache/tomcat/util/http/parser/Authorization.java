/*     */ package org.apache.tomcat.util.http.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ public class Authorization
/*     */ {
/*  32 */   private static final StringManager sm = StringManager.getManager(Authorization.class);
/*     */   
/*     */ 
/*  35 */   private static final Integer FIELD_TYPE_TOKEN = Integer.valueOf(0);
/*  36 */   private static final Integer FIELD_TYPE_QUOTED_STRING = Integer.valueOf(1);
/*  37 */   private static final Integer FIELD_TYPE_TOKEN_OR_QUOTED_STRING = Integer.valueOf(2);
/*  38 */   private static final Integer FIELD_TYPE_LHEX = Integer.valueOf(3);
/*  39 */   private static final Integer FIELD_TYPE_QUOTED_TOKEN = Integer.valueOf(4);
/*     */   
/*  41 */   private static final Map<String, Integer> fieldTypes = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  48 */     fieldTypes.put("username", FIELD_TYPE_QUOTED_STRING);
/*  49 */     fieldTypes.put("realm", FIELD_TYPE_QUOTED_STRING);
/*  50 */     fieldTypes.put("nonce", FIELD_TYPE_QUOTED_STRING);
/*  51 */     fieldTypes.put("digest-uri", FIELD_TYPE_QUOTED_STRING);
/*     */     
/*  53 */     fieldTypes.put("response", FIELD_TYPE_LHEX);
/*     */     
/*  55 */     fieldTypes.put("algorithm", FIELD_TYPE_QUOTED_TOKEN);
/*  56 */     fieldTypes.put("cnonce", FIELD_TYPE_QUOTED_STRING);
/*  57 */     fieldTypes.put("opaque", FIELD_TYPE_QUOTED_STRING);
/*     */     
/*  59 */     fieldTypes.put("qop", FIELD_TYPE_QUOTED_TOKEN);
/*     */     
/*  61 */     fieldTypes.put("nc", FIELD_TYPE_LHEX);
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
/*     */   public static Map<String, String> parseAuthorizationDigest(StringReader input)
/*     */     throws IllegalArgumentException, IOException
/*     */   {
/*  83 */     Map<String, String> result = new HashMap();
/*     */     
/*  85 */     if (HttpParser.skipConstant(input, "Digest") != SkipResult.FOUND) {
/*  86 */       return null;
/*     */     }
/*     */     
/*  89 */     String field = HttpParser.readToken(input);
/*  90 */     if (field == null) {
/*  91 */       return null;
/*     */     }
/*  93 */     while (!field.equals("")) {
/*  94 */       if (HttpParser.skipConstant(input, "=") != SkipResult.FOUND) {
/*  95 */         return null;
/*     */       }
/*     */       
/*  98 */       Integer type = (Integer)fieldTypes.get(field.toLowerCase(Locale.ENGLISH));
/*  99 */       if (type == null)
/*     */       {
/* 101 */         type = FIELD_TYPE_TOKEN_OR_QUOTED_STRING; }
/*     */       String value;
/* 103 */       String value; String value; String value; String value; switch (type.intValue())
/*     */       {
/*     */       case 0: 
/* 106 */         value = HttpParser.readToken(input);
/* 107 */         break;
/*     */       
/*     */       case 1: 
/* 110 */         value = HttpParser.readQuotedString(input, false);
/* 111 */         break;
/*     */       
/*     */       case 2: 
/* 114 */         value = HttpParser.readTokenOrQuotedString(input, false);
/* 115 */         break;
/*     */       
/*     */       case 3: 
/* 118 */         value = HttpParser.readLhex(input);
/* 119 */         break;
/*     */       
/*     */       case 4: 
/* 122 */         value = HttpParser.readQuotedToken(input);
/* 123 */         break;
/*     */       
/*     */ 
/*     */       default: 
/* 127 */         throw new IllegalArgumentException(sm.getString("authorization.unknownType", new Object[] { type }));
/*     */       }
/*     */       String value;
/* 130 */       if (value == null) {
/* 131 */         return null;
/*     */       }
/* 133 */       result.put(field, value);
/*     */       
/* 135 */       if (HttpParser.skipConstant(input, ",") == SkipResult.NOT_FOUND) {
/* 136 */         return null;
/*     */       }
/* 138 */       field = HttpParser.readToken(input);
/* 139 */       if (field == null) {
/* 140 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 144 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\parser\Authorization.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */