/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.Base64Variants;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.io.CharTypes;
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class TextNode extends ValueNode
/*     */ {
/*  17 */   static final TextNode EMPTY_STRING_NODE = new TextNode("");
/*     */   protected final String _value;
/*     */   
/*     */   public TextNode(String v) {
/*  21 */     this._value = v;
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
/*     */   public static TextNode valueOf(String v)
/*     */   {
/*  34 */     if (v == null) {
/*  35 */       return null;
/*     */     }
/*  37 */     if (v.length() == 0) {
/*  38 */       return EMPTY_STRING_NODE;
/*     */     }
/*  40 */     return new TextNode(v);
/*     */   }
/*     */   
/*     */   public JsonNodeType getNodeType()
/*     */   {
/*  45 */     return JsonNodeType.STRING;
/*     */   }
/*     */   
/*  48 */   public JsonToken asToken() { return JsonToken.VALUE_STRING; }
/*     */   
/*     */   public String textValue()
/*     */   {
/*  52 */     return this._value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getBinaryValue(Base64Variant b64variant)
/*     */     throws IOException
/*     */   {
/*  63 */     ByteArrayBuilder builder = new ByteArrayBuilder(100);
/*  64 */     String str = this._value;
/*  65 */     int ptr = 0;
/*  66 */     int len = str.length();
/*     */     
/*     */ 
/*  69 */     while (ptr < len)
/*     */     {
/*     */       do
/*     */       {
/*  73 */         ch = str.charAt(ptr++);
/*  74 */         if (ptr >= len) {
/*     */           break;
/*     */         }
/*  77 */       } while (ch <= ' ');
/*  78 */       int bits = b64variant.decodeBase64Char(ch);
/*  79 */       if (bits < 0) {
/*  80 */         _reportInvalidBase64(b64variant, ch, 0);
/*     */       }
/*  82 */       int decodedData = bits;
/*     */       
/*  84 */       if (ptr >= len) {
/*  85 */         _reportBase64EOF();
/*     */       }
/*  87 */       char ch = str.charAt(ptr++);
/*  88 */       bits = b64variant.decodeBase64Char(ch);
/*  89 */       if (bits < 0) {
/*  90 */         _reportInvalidBase64(b64variant, ch, 1);
/*     */       }
/*  92 */       decodedData = decodedData << 6 | bits;
/*     */       
/*  94 */       if (ptr >= len)
/*     */       {
/*  96 */         if (!b64variant.usesPadding())
/*     */         {
/*  98 */           decodedData >>= 4;
/*  99 */           builder.append(decodedData);
/* 100 */           break;
/*     */         }
/* 102 */         _reportBase64EOF();
/*     */       }
/* 104 */       ch = str.charAt(ptr++);
/* 105 */       bits = b64variant.decodeBase64Char(ch);
/*     */       
/*     */ 
/* 108 */       if (bits < 0) {
/* 109 */         if (bits != -2) {
/* 110 */           _reportInvalidBase64(b64variant, ch, 2);
/*     */         }
/*     */         
/* 113 */         if (ptr >= len) {
/* 114 */           _reportBase64EOF();
/*     */         }
/* 116 */         ch = str.charAt(ptr++);
/* 117 */         if (!b64variant.usesPaddingChar(ch)) {
/* 118 */           _reportInvalidBase64(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*     */         }
/*     */         
/* 121 */         decodedData >>= 4;
/* 122 */         builder.append(decodedData);
/*     */       }
/*     */       else
/*     */       {
/* 126 */         decodedData = decodedData << 6 | bits;
/*     */         
/* 128 */         if (ptr >= len)
/*     */         {
/* 130 */           if (!b64variant.usesPadding()) {
/* 131 */             decodedData >>= 2;
/* 132 */             builder.appendTwoBytes(decodedData);
/* 133 */             break;
/*     */           }
/* 135 */           _reportBase64EOF();
/*     */         }
/* 137 */         ch = str.charAt(ptr++);
/* 138 */         bits = b64variant.decodeBase64Char(ch);
/* 139 */         if (bits < 0) {
/* 140 */           if (bits != -2) {
/* 141 */             _reportInvalidBase64(b64variant, ch, 3);
/*     */           }
/* 143 */           decodedData >>= 2;
/* 144 */           builder.appendTwoBytes(decodedData);
/*     */         }
/*     */         else {
/* 147 */           decodedData = decodedData << 6 | bits;
/* 148 */           builder.appendThreeBytes(decodedData);
/*     */         }
/*     */       } }
/* 151 */     return builder.toByteArray();
/*     */   }
/*     */   
/*     */   public byte[] binaryValue() throws IOException
/*     */   {
/* 156 */     return getBinaryValue(Base64Variants.getDefaultVariant());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String asText()
/*     */   {
/* 167 */     return this._value;
/*     */   }
/*     */   
/*     */   public String asText(String defaultValue)
/*     */   {
/* 172 */     return this._value == null ? defaultValue : this._value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean asBoolean(boolean defaultValue)
/*     */   {
/* 179 */     if (this._value != null) {
/* 180 */       String v = this._value.trim();
/* 181 */       if ("true".equals(v)) {
/* 182 */         return true;
/*     */       }
/* 184 */       if ("false".equals(v)) {
/* 185 */         return false;
/*     */       }
/*     */     }
/* 188 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public int asInt(int defaultValue)
/*     */   {
/* 193 */     return NumberInput.parseAsInt(this._value, defaultValue);
/*     */   }
/*     */   
/*     */   public long asLong(long defaultValue)
/*     */   {
/* 198 */     return NumberInput.parseAsLong(this._value, defaultValue);
/*     */   }
/*     */   
/*     */   public double asDouble(double defaultValue)
/*     */   {
/* 203 */     return NumberInput.parseAsDouble(this._value, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(JsonGenerator g, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 215 */     if (this._value == null) {
/* 216 */       g.writeNull();
/*     */     } else {
/* 218 */       g.writeString(this._value);
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
/*     */   public boolean equals(Object o)
/*     */   {
/* 231 */     if (o == this) return true;
/* 232 */     if (o == null) return false;
/* 233 */     if ((o instanceof TextNode)) {
/* 234 */       return ((TextNode)o)._value.equals(this._value);
/*     */     }
/* 236 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 240 */     return this._value.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 248 */     int len = this._value.length();
/* 249 */     len = len + 2 + (len >> 4);
/* 250 */     StringBuilder sb = new StringBuilder(len);
/* 251 */     appendQuoted(sb, this._value);
/* 252 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected static void appendQuoted(StringBuilder sb, String content)
/*     */   {
/* 257 */     sb.append('"');
/* 258 */     CharTypes.appendQuoted(sb, content);
/* 259 */     sb.append('"');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex)
/*     */     throws JsonParseException
/*     */   {
/* 271 */     _reportInvalidBase64(b64variant, ch, bindex, null);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void _reportInvalidBase64(Base64Variant b64variant, char ch, int bindex, String msg)
/*     */     throws JsonParseException
/*     */   {
/*     */     String base;
/*     */     
/*     */     String base;
/*     */     
/* 282 */     if (ch <= ' ') {
/* 283 */       base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units"; } else { String base;
/* 284 */       if (b64variant.usesPaddingChar(ch)) {
/* 285 */         base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character"; } else { String base;
/* 286 */         if ((!Character.isDefined(ch)) || (Character.isISOControl(ch)))
/*     */         {
/* 288 */           base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*     */         } else
/* 290 */           base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*     */       } }
/* 292 */     if (msg != null) {
/* 293 */       base = base + ": " + msg;
/*     */     }
/* 295 */     throw new JsonParseException(null, base, JsonLocation.NA);
/*     */   }
/*     */   
/*     */   protected void _reportBase64EOF() throws JsonParseException {
/* 299 */     throw new JsonParseException(null, "Unexpected end-of-String when base64 content");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\TextNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */