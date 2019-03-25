/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonParserSequence
/*     */   extends JsonParserDelegate
/*     */ {
/*     */   protected final JsonParser[] _parsers;
/*     */   protected final boolean _checkForExistingToken;
/*     */   protected int _nextParserIndex;
/*     */   protected boolean _hasToken;
/*     */   
/*     */   @Deprecated
/*     */   protected JsonParserSequence(JsonParser[] parsers)
/*     */   {
/*  60 */     this(false, parsers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonParserSequence(boolean checkForExistingToken, JsonParser[] parsers)
/*     */   {
/*  68 */     super(parsers[0]);
/*  69 */     this._checkForExistingToken = checkForExistingToken;
/*  70 */     this._hasToken = ((checkForExistingToken) && (this.delegate.hasCurrentToken()));
/*  71 */     this._parsers = parsers;
/*  72 */     this._nextParserIndex = 1;
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
/*     */   public static JsonParserSequence createFlattened(boolean checkForExistingToken, JsonParser first, JsonParser second)
/*     */   {
/*  87 */     if ((!(first instanceof JsonParserSequence)) && (!(second instanceof JsonParserSequence))) {
/*  88 */       return new JsonParserSequence(checkForExistingToken, new JsonParser[] { first, second });
/*     */     }
/*     */     
/*  91 */     ArrayList<JsonParser> p = new ArrayList();
/*  92 */     if ((first instanceof JsonParserSequence)) {
/*  93 */       ((JsonParserSequence)first).addFlattenedActiveParsers(p);
/*     */     } else {
/*  95 */       p.add(first);
/*     */     }
/*  97 */     if ((second instanceof JsonParserSequence)) {
/*  98 */       ((JsonParserSequence)second).addFlattenedActiveParsers(p);
/*     */     } else {
/* 100 */       p.add(second);
/*     */     }
/* 102 */     return new JsonParserSequence(checkForExistingToken, (JsonParser[])p.toArray(new JsonParser[p.size()]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static JsonParserSequence createFlattened(JsonParser first, JsonParser second)
/*     */   {
/* 112 */     return createFlattened(false, first, second);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addFlattenedActiveParsers(List<JsonParser> listToAddIn)
/*     */   {
/* 118 */     int i = this._nextParserIndex - 1; for (int len = this._parsers.length; i < len; i++) {
/* 119 */       JsonParser p = this._parsers[i];
/* 120 */       if ((p instanceof JsonParserSequence)) {
/* 121 */         ((JsonParserSequence)p).addFlattenedActiveParsers(listToAddIn);
/*     */       } else {
/* 123 */         listToAddIn.add(p);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     do
/*     */     {
/* 137 */       this.delegate.close(); } while (switchToNext());
/*     */   }
/*     */   
/*     */   public JsonToken nextToken()
/*     */     throws IOException
/*     */   {
/* 143 */     if (this.delegate == null) {
/* 144 */       return null;
/*     */     }
/* 146 */     if (this._hasToken) {
/* 147 */       this._hasToken = false;
/* 148 */       return this.delegate.currentToken();
/*     */     }
/* 150 */     JsonToken t = this.delegate.nextToken();
/* 151 */     if (t == null) {
/* 152 */       return switchAndReturnNext();
/*     */     }
/* 154 */     return t;
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
/*     */   public int containedParsersCount()
/*     */   {
/* 169 */     return this._parsers.length;
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
/*     */   protected boolean switchToNext()
/*     */   {
/* 189 */     if (this._nextParserIndex < this._parsers.length) {
/* 190 */       this.delegate = this._parsers[(this._nextParserIndex++)];
/* 191 */       return true;
/*     */     }
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   protected JsonToken switchAndReturnNext() throws IOException
/*     */   {
/* 198 */     while (this._nextParserIndex < this._parsers.length) {
/* 199 */       this.delegate = this._parsers[(this._nextParserIndex++)];
/* 200 */       if ((this._checkForExistingToken) && (this.delegate.hasCurrentToken())) {
/* 201 */         return this.delegate.getCurrentToken();
/*     */       }
/* 203 */       JsonToken t = this.delegate.nextToken();
/* 204 */       if (t != null) {
/* 205 */         return t;
/*     */       }
/*     */     }
/* 208 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\cor\\util\JsonParserSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */