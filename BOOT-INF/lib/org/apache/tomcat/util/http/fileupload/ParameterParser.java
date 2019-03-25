/*     */ package org.apache.tomcat.util.http.fileupload;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.tomcat.util.http.fileupload.util.mime.MimeUtility;
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
/*     */ public class ParameterParser
/*     */ {
/*  42 */   private char[] chars = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private int pos = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private int len = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private int i1 = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   private int i2 = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private boolean lowerCaseNames = false;
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
/*     */   private boolean hasChar()
/*     */   {
/*  83 */     return this.pos < this.len;
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
/*     */   private String getToken(boolean quoted)
/*     */   {
/*  97 */     while ((this.i1 < this.i2) && (Character.isWhitespace(this.chars[this.i1]))) {
/*  98 */       this.i1 += 1;
/*     */     }
/*     */     
/* 101 */     while ((this.i2 > this.i1) && (Character.isWhitespace(this.chars[(this.i2 - 1)]))) {
/* 102 */       this.i2 -= 1;
/*     */     }
/*     */     
/* 105 */     if ((quoted) && (this.i2 - this.i1 >= 2) && (this.chars[this.i1] == '"') && (this.chars[(this.i2 - 1)] == '"'))
/*     */     {
/*     */ 
/*     */ 
/* 109 */       this.i1 += 1;
/* 110 */       this.i2 -= 1;
/*     */     }
/* 112 */     String result = null;
/* 113 */     if (this.i2 > this.i1) {
/* 114 */       result = new String(this.chars, this.i1, this.i2 - this.i1);
/*     */     }
/* 116 */     return result;
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
/*     */   private boolean isOneOf(char ch, char[] charray)
/*     */   {
/* 129 */     boolean result = false;
/* 130 */     for (char element : charray) {
/* 131 */       if (ch == element) {
/* 132 */         result = true;
/* 133 */         break;
/*     */       }
/*     */     }
/* 136 */     return result;
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
/*     */   private String parseToken(char[] terminators)
/*     */   {
/* 150 */     this.i1 = this.pos;
/* 151 */     this.i2 = this.pos;
/* 152 */     while (hasChar()) {
/* 153 */       char ch = this.chars[this.pos];
/* 154 */       if (isOneOf(ch, terminators)) {
/*     */         break;
/*     */       }
/* 157 */       this.i2 += 1;
/* 158 */       this.pos += 1;
/*     */     }
/* 160 */     return getToken(false);
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
/*     */   private String parseQuotedToken(char[] terminators)
/*     */   {
/* 175 */     this.i1 = this.pos;
/* 176 */     this.i2 = this.pos;
/* 177 */     boolean quoted = false;
/* 178 */     boolean charEscaped = false;
/* 179 */     while (hasChar()) {
/* 180 */       char ch = this.chars[this.pos];
/* 181 */       if ((!quoted) && (isOneOf(ch, terminators))) {
/*     */         break;
/*     */       }
/* 184 */       if ((!charEscaped) && (ch == '"')) {
/* 185 */         quoted = !quoted;
/*     */       }
/* 187 */       charEscaped = (!charEscaped) && (ch == '\\');
/* 188 */       this.i2 += 1;
/* 189 */       this.pos += 1;
/*     */     }
/*     */     
/* 192 */     return getToken(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLowerCaseNames()
/*     */   {
/* 204 */     return this.lowerCaseNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLowerCaseNames(boolean b)
/*     */   {
/* 216 */     this.lowerCaseNames = b;
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
/*     */   public Map<String, String> parse(String str, char[] separators)
/*     */   {
/* 230 */     if ((separators == null) || (separators.length == 0)) {
/* 231 */       return new HashMap();
/*     */     }
/* 233 */     char separator = separators[0];
/* 234 */     if (str != null) {
/* 235 */       int idx = str.length();
/* 236 */       for (char separator2 : separators) {
/* 237 */         int tmp = str.indexOf(separator2);
/* 238 */         if ((tmp != -1) && (tmp < idx)) {
/* 239 */           idx = tmp;
/* 240 */           separator = separator2;
/*     */         }
/*     */       }
/*     */     }
/* 244 */     return parse(str, separator);
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
/*     */   public Map<String, String> parse(String str, char separator)
/*     */   {
/* 257 */     if (str == null) {
/* 258 */       return new HashMap();
/*     */     }
/* 260 */     return parse(str.toCharArray(), separator);
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
/*     */   public Map<String, String> parse(char[] charArray, char separator)
/*     */   {
/* 274 */     if (charArray == null) {
/* 275 */       return new HashMap();
/*     */     }
/* 277 */     return parse(charArray, 0, charArray.length, separator);
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
/*     */   public Map<String, String> parse(char[] charArray, int offset, int length, char separator)
/*     */   {
/* 298 */     if (charArray == null) {
/* 299 */       return new HashMap();
/*     */     }
/* 301 */     HashMap<String, String> params = new HashMap();
/* 302 */     this.chars = charArray;
/* 303 */     this.pos = offset;
/* 304 */     this.len = length;
/*     */     
/* 306 */     String paramName = null;
/* 307 */     String paramValue = null;
/* 308 */     while (hasChar()) {
/* 309 */       paramName = parseToken(new char[] { '=', separator });
/*     */       
/* 311 */       paramValue = null;
/* 312 */       if ((hasChar()) && (charArray[this.pos] == '=')) {
/* 313 */         this.pos += 1;
/* 314 */         paramValue = parseQuotedToken(new char[] { separator });
/*     */         
/*     */ 
/* 317 */         if (paramValue != null) {
/*     */           try {
/* 319 */             paramValue = MimeUtility.decodeText(paramValue);
/*     */           }
/*     */           catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
/*     */         }
/*     */       }
/*     */       
/* 325 */       if ((hasChar()) && (charArray[this.pos] == separator)) {
/* 326 */         this.pos += 1;
/*     */       }
/* 328 */       if ((paramName != null) && (paramName.length() > 0)) {
/* 329 */         if (this.lowerCaseNames) {
/* 330 */           paramName = paramName.toLowerCase(Locale.ENGLISH);
/*     */         }
/*     */         
/* 333 */         params.put(paramName, paramValue);
/*     */       }
/*     */     }
/* 336 */     return params;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\ParameterParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */