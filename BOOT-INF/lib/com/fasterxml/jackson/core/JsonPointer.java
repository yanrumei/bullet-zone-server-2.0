/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
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
/*     */ public class JsonPointer
/*     */ {
/*  27 */   protected static final JsonPointer EMPTY = new JsonPointer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonPointer _nextSegment;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected volatile JsonPointer _head;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _asString;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _matchingPropertyName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int _matchingElementIndex;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonPointer()
/*     */   {
/*  71 */     this._nextSegment = null;
/*  72 */     this._matchingPropertyName = "";
/*  73 */     this._matchingElementIndex = -1;
/*  74 */     this._asString = "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonPointer(String fullString, String segment, JsonPointer next)
/*     */   {
/*  81 */     this._asString = fullString;
/*  82 */     this._nextSegment = next;
/*     */     
/*  84 */     this._matchingPropertyName = segment;
/*     */     
/*  86 */     this._matchingElementIndex = _parseIndex(segment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonPointer(String fullString, String segment, int matchIndex, JsonPointer next)
/*     */   {
/*  93 */     this._asString = fullString;
/*  94 */     this._nextSegment = next;
/*  95 */     this._matchingPropertyName = segment;
/*  96 */     this._matchingElementIndex = matchIndex;
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
/*     */   public static JsonPointer compile(String input)
/*     */     throws IllegalArgumentException
/*     */   {
/* 117 */     if ((input == null) || (input.length() == 0)) {
/* 118 */       return EMPTY;
/*     */     }
/*     */     
/* 121 */     if (input.charAt(0) != '/') {
/* 122 */       throw new IllegalArgumentException("Invalid input: JSON Pointer expression must start with '/': \"" + input + "\"");
/*     */     }
/* 124 */     return _parseTail(input);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonPointer valueOf(String input)
/*     */   {
/* 131 */     return compile(input);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */   public boolean matches() { return this._nextSegment == null; }
/* 162 */   public String getMatchingProperty() { return this._matchingPropertyName; }
/* 163 */   public int getMatchingIndex() { return this._matchingElementIndex; }
/* 164 */   public boolean mayMatchProperty() { return this._matchingPropertyName != null; }
/* 165 */   public boolean mayMatchElement() { return this._matchingElementIndex >= 0; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonPointer last()
/*     */   {
/* 174 */     JsonPointer current = this;
/* 175 */     if (current == EMPTY) {
/* 176 */       return null;
/*     */     }
/*     */     JsonPointer next;
/* 179 */     while ((next = current._nextSegment) != EMPTY) {
/* 180 */       current = next;
/*     */     }
/* 182 */     return current;
/*     */   }
/*     */   
/*     */   public JsonPointer append(JsonPointer tail) {
/* 186 */     if (this == EMPTY) {
/* 187 */       return tail;
/*     */     }
/* 189 */     if (tail == EMPTY) {
/* 190 */       return this;
/*     */     }
/* 192 */     String currentJsonPointer = this._asString;
/* 193 */     if (currentJsonPointer.endsWith("/"))
/*     */     {
/* 195 */       currentJsonPointer = currentJsonPointer.substring(0, currentJsonPointer.length() - 1);
/*     */     }
/* 197 */     return compile(currentJsonPointer + tail._asString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matchesProperty(String name)
/*     */   {
/* 207 */     return (this._nextSegment != null) && (this._matchingPropertyName.equals(name));
/*     */   }
/*     */   
/*     */   public JsonPointer matchProperty(String name) {
/* 211 */     if ((this._nextSegment != null) && (this._matchingPropertyName.equals(name))) {
/* 212 */       return this._nextSegment;
/*     */     }
/* 214 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matchesElement(int index)
/*     */   {
/* 224 */     return (index == this._matchingElementIndex) && (index >= 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonPointer matchElement(int index)
/*     */   {
/* 231 */     if ((index != this._matchingElementIndex) || (index < 0)) {
/* 232 */       return null;
/*     */     }
/* 234 */     return this._nextSegment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonPointer tail()
/*     */   {
/* 243 */     return this._nextSegment;
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
/*     */   public JsonPointer head()
/*     */   {
/* 257 */     JsonPointer h = this._head;
/* 258 */     if (h == null) {
/* 259 */       if (this != EMPTY) {
/* 260 */         h = _constructHead();
/*     */       }
/* 262 */       this._head = h;
/*     */     }
/* 264 */     return h;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 273 */   public String toString() { return this._asString; }
/* 274 */   public int hashCode() { return this._asString.hashCode(); }
/*     */   
/*     */   public boolean equals(Object o) {
/* 277 */     if (o == this) return true;
/* 278 */     if (o == null) return false;
/* 279 */     if (!(o instanceof JsonPointer)) return false;
/* 280 */     return this._asString.equals(((JsonPointer)o)._asString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int _parseIndex(String str)
/*     */   {
/* 290 */     int len = str.length();
/*     */     
/*     */ 
/* 293 */     if ((len == 0) || (len > 10)) {
/* 294 */       return -1;
/*     */     }
/*     */     
/* 297 */     char c = str.charAt(0);
/* 298 */     if (c <= '0') {
/* 299 */       return (len == 1) && (c == '0') ? 0 : -1;
/*     */     }
/* 301 */     if (c > '9') {
/* 302 */       return -1;
/*     */     }
/* 304 */     for (int i = 1; i < len; i++) {
/* 305 */       c = str.charAt(i);
/* 306 */       if ((c > '9') || (c < '0')) {
/* 307 */         return -1;
/*     */       }
/*     */     }
/* 310 */     if (len == 10) {
/* 311 */       long l = NumberInput.parseLong(str);
/* 312 */       if (l > 2147483647L) {
/* 313 */         return -1;
/*     */       }
/*     */     }
/* 316 */     return NumberInput.parseInt(str);
/*     */   }
/*     */   
/*     */   protected static JsonPointer _parseTail(String input) {
/* 320 */     int end = input.length();
/*     */     
/*     */ 
/* 323 */     for (int i = 1; i < end;) {
/* 324 */       char c = input.charAt(i);
/* 325 */       if (c == '/') {
/* 326 */         return new JsonPointer(input, input.substring(1, i), _parseTail(input.substring(i)));
/*     */       }
/*     */       
/* 329 */       i++;
/*     */       
/* 331 */       if ((c == '~') && (i < end)) {
/* 332 */         return _parseQuotedTail(input, i);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 337 */     return new JsonPointer(input, input.substring(1), EMPTY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static JsonPointer _parseQuotedTail(String input, int i)
/*     */   {
/* 348 */     int end = input.length();
/* 349 */     StringBuilder sb = new StringBuilder(Math.max(16, end));
/* 350 */     if (i > 2) {
/* 351 */       sb.append(input, 1, i - 1);
/*     */     }
/* 353 */     _appendEscape(sb, input.charAt(i++));
/* 354 */     while (i < end) {
/* 355 */       char c = input.charAt(i);
/* 356 */       if (c == '/') {
/* 357 */         return new JsonPointer(input, sb.toString(), _parseTail(input.substring(i)));
/*     */       }
/*     */       
/* 360 */       i++;
/* 361 */       if ((c == '~') && (i < end)) {
/* 362 */         _appendEscape(sb, input.charAt(i++));
/*     */       }
/*     */       else {
/* 365 */         sb.append(c);
/*     */       }
/*     */     }
/* 368 */     return new JsonPointer(input, sb.toString(), EMPTY);
/*     */   }
/*     */   
/*     */ 
/*     */   protected JsonPointer _constructHead()
/*     */   {
/* 374 */     JsonPointer last = last();
/* 375 */     if (last == this) {
/* 376 */       return EMPTY;
/*     */     }
/*     */     
/* 379 */     int suffixLength = last._asString.length();
/* 380 */     JsonPointer next = this._nextSegment;
/* 381 */     return new JsonPointer(this._asString.substring(0, this._asString.length() - suffixLength), this._matchingPropertyName, this._matchingElementIndex, next._constructHead(suffixLength, last));
/*     */   }
/*     */   
/*     */ 
/*     */   protected JsonPointer _constructHead(int suffixLength, JsonPointer last)
/*     */   {
/* 387 */     if (this == last) {
/* 388 */       return EMPTY;
/*     */     }
/* 390 */     JsonPointer next = this._nextSegment;
/* 391 */     String str = this._asString;
/* 392 */     return new JsonPointer(str.substring(0, str.length() - suffixLength), this._matchingPropertyName, this._matchingElementIndex, next._constructHead(suffixLength, last));
/*     */   }
/*     */   
/*     */   private static void _appendEscape(StringBuilder sb, char c)
/*     */   {
/* 397 */     if (c == '0') {
/* 398 */       c = '~';
/* 399 */     } else if (c == '1') {
/* 400 */       c = '/';
/*     */     } else {
/* 402 */       sb.append('~');
/*     */     }
/* 404 */     sb.append(c);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonPointer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */