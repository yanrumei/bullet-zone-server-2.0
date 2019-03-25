/*     */ package org.springframework.boot.json;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class BasicJsonParser
/*     */   implements JsonParser
/*     */ {
/*     */   public Map<String, Object> parseMap(String json)
/*     */   {
/*  41 */     if (json != null) {
/*  42 */       json = json.trim();
/*  43 */       if (json.startsWith("{")) {
/*  44 */         return parseMapInternal(json);
/*     */       }
/*     */     }
/*  47 */     throw new IllegalArgumentException("Cannot parse JSON");
/*     */   }
/*     */   
/*     */   public List<Object> parseList(String json)
/*     */   {
/*  52 */     if (json != null) {
/*  53 */       json = json.trim();
/*  54 */       if (json.startsWith("[")) {
/*  55 */         return parseListInternal(json);
/*     */       }
/*     */     }
/*  58 */     throw new IllegalArgumentException("Cannot parse JSON");
/*     */   }
/*     */   
/*     */   private List<Object> parseListInternal(String json) {
/*  62 */     List<Object> list = new ArrayList();
/*  63 */     json = trimLeadingCharacter(trimTrailingCharacter(json, ']'), '[');
/*  64 */     for (String value : tokenize(json)) {
/*  65 */       list.add(parseInternal(value));
/*     */     }
/*  67 */     return list;
/*     */   }
/*     */   
/*     */   private Object parseInternal(String json) {
/*  71 */     if (json.startsWith("[")) {
/*  72 */       return parseListInternal(json);
/*     */     }
/*  74 */     if (json.startsWith("{")) {
/*  75 */       return parseMapInternal(json);
/*     */     }
/*  77 */     if (json.startsWith("\"")) {
/*  78 */       return trimTrailingCharacter(trimLeadingCharacter(json, '"'), '"');
/*     */     }
/*     */     try {
/*  81 */       return Long.valueOf(json);
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException)
/*     */     {
/*     */       try
/*     */       {
/*  87 */         return Double.valueOf(json);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException1) {}
/*     */     }
/*     */     
/*  92 */     return json;
/*     */   }
/*     */   
/*     */   private static String trimTrailingCharacter(String string, char c) {
/*  96 */     if ((string.length() > 0) && (string.charAt(string.length() - 1) == c)) {
/*  97 */       return string.substring(0, string.length() - 1);
/*     */     }
/*  99 */     return string;
/*     */   }
/*     */   
/*     */   private static String trimLeadingCharacter(String string, char c) {
/* 103 */     if ((string.length() > 0) && (string.charAt(0) == c)) {
/* 104 */       return string.substring(1);
/*     */     }
/* 106 */     return string;
/*     */   }
/*     */   
/*     */   private Map<String, Object> parseMapInternal(String json) {
/* 110 */     Map<String, Object> map = new LinkedHashMap();
/* 111 */     json = trimLeadingCharacter(trimTrailingCharacter(json, '}'), '{');
/* 112 */     for (String pair : tokenize(json)) {
/* 113 */       String[] values = StringUtils.trimArrayElements(StringUtils.split(pair, ":"));
/* 114 */       String key = trimLeadingCharacter(trimTrailingCharacter(values[0], '"'), '"');
/* 115 */       Object value = null;
/* 116 */       if (values.length > 0) {
/* 117 */         String string = trimLeadingCharacter(
/* 118 */           trimTrailingCharacter(values[1], '"'), '"');
/* 119 */         value = parseInternal(string);
/*     */       }
/* 121 */       map.put(key, value);
/*     */     }
/* 123 */     return map;
/*     */   }
/*     */   
/*     */   private List<String> tokenize(String json) {
/* 127 */     List<String> list = new ArrayList();
/* 128 */     int index = 0;
/* 129 */     int inObject = 0;
/* 130 */     int inList = 0;
/* 131 */     StringBuilder build = new StringBuilder();
/* 132 */     while (index < json.length()) {
/* 133 */       char current = json.charAt(index);
/* 134 */       if (current == '{') {
/* 135 */         inObject++;
/*     */       }
/* 137 */       if (current == '}') {
/* 138 */         inObject--;
/*     */       }
/* 140 */       if (current == '[') {
/* 141 */         inList++;
/*     */       }
/* 143 */       if (current == ']') {
/* 144 */         inList--;
/*     */       }
/* 146 */       if ((current == ',') && (inObject == 0) && (inList == 0)) {
/* 147 */         list.add(build.toString());
/* 148 */         build.setLength(0);
/*     */       }
/*     */       else {
/* 151 */         build.append(current);
/*     */       }
/* 153 */       index++;
/*     */     }
/* 155 */     if (build.length() > 0) {
/* 156 */       list.add(build.toString());
/*     */     }
/* 158 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\json\BasicJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */