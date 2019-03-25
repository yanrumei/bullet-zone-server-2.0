/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.BitSet;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
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
/*     */ public class URLEncoder
/*     */   implements Cloneable
/*     */ {
/*  42 */   private static final char[] hexadecimal = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */   
/*     */ 
/*  45 */   public static final URLEncoder DEFAULT = new URLEncoder();
/*  46 */   public static final URLEncoder QUERY = new URLEncoder();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final BitSet safeCharacters;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  61 */     DEFAULT.addSafeCharacter('-');
/*  62 */     DEFAULT.addSafeCharacter('.');
/*  63 */     DEFAULT.addSafeCharacter('_');
/*  64 */     DEFAULT.addSafeCharacter('~');
/*     */     
/*  66 */     DEFAULT.addSafeCharacter('!');
/*  67 */     DEFAULT.addSafeCharacter('$');
/*  68 */     DEFAULT.addSafeCharacter('&');
/*  69 */     DEFAULT.addSafeCharacter('\'');
/*  70 */     DEFAULT.addSafeCharacter('(');
/*  71 */     DEFAULT.addSafeCharacter(')');
/*  72 */     DEFAULT.addSafeCharacter('*');
/*  73 */     DEFAULT.addSafeCharacter('+');
/*  74 */     DEFAULT.addSafeCharacter(',');
/*  75 */     DEFAULT.addSafeCharacter(';');
/*  76 */     DEFAULT.addSafeCharacter('=');
/*     */     
/*  78 */     DEFAULT.addSafeCharacter(':');
/*  79 */     DEFAULT.addSafeCharacter('@');
/*     */     
/*  81 */     DEFAULT.addSafeCharacter('/');
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
/*  93 */     QUERY.setEncodeSpaceAsPlus(true);
/*     */     
/*     */ 
/*  96 */     QUERY.addSafeCharacter('*');
/*  97 */     QUERY.addSafeCharacter('-');
/*  98 */     QUERY.addSafeCharacter('.');
/*  99 */     QUERY.addSafeCharacter('_');
/* 100 */     QUERY.addSafeCharacter('=');
/* 101 */     QUERY.addSafeCharacter('&');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 107 */   private boolean encodeSpaceAsPlus = false;
/*     */   
/*     */   public URLEncoder()
/*     */   {
/* 111 */     this(new BitSet(256));
/*     */     
/* 113 */     for (char i = 'a'; i <= 'z'; i = (char)(i + '\001')) {
/* 114 */       addSafeCharacter(i);
/*     */     }
/* 116 */     for (char i = 'A'; i <= 'Z'; i = (char)(i + '\001')) {
/* 117 */       addSafeCharacter(i);
/*     */     }
/* 119 */     for (char i = '0'; i <= '9'; i = (char)(i + '\001')) {
/* 120 */       addSafeCharacter(i);
/*     */     }
/*     */   }
/*     */   
/*     */   private URLEncoder(BitSet safeCharacters)
/*     */   {
/* 126 */     this.safeCharacters = safeCharacters;
/*     */   }
/*     */   
/*     */   public void addSafeCharacter(char c)
/*     */   {
/* 131 */     this.safeCharacters.set(c);
/*     */   }
/*     */   
/*     */   public void removeSafeCharacter(char c)
/*     */   {
/* 136 */     this.safeCharacters.clear(c);
/*     */   }
/*     */   
/*     */   public void setEncodeSpaceAsPlus(boolean encodeSpaceAsPlus)
/*     */   {
/* 141 */     this.encodeSpaceAsPlus = encodeSpaceAsPlus;
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
/*     */   @Deprecated
/*     */   public String encode(String path)
/*     */   {
/* 156 */     return encode(path, "UTF-8");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String encode(String path, String encoding)
/*     */   {
/*     */     Charset charset;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 174 */       charset = B2CConverter.getCharset(encoding);
/*     */     } catch (UnsupportedEncodingException e) { Charset charset;
/* 176 */       charset = Charset.defaultCharset();
/*     */     }
/* 178 */     return encode(path, charset);
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
/*     */   public String encode(String path, Charset charset)
/*     */   {
/* 192 */     int maxBytesPerChar = 10;
/* 193 */     StringBuilder rewrittenPath = new StringBuilder(path.length());
/* 194 */     ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
/* 195 */     OutputStreamWriter writer = new OutputStreamWriter(buf, charset);
/*     */     
/* 197 */     for (int i = 0; i < path.length(); i++) {
/* 198 */       int c = path.charAt(i);
/* 199 */       if (this.safeCharacters.get(c)) {
/* 200 */         rewrittenPath.append((char)c);
/* 201 */       } else if ((this.encodeSpaceAsPlus) && (c == 32)) {
/* 202 */         rewrittenPath.append('+');
/*     */       }
/*     */       else {
/*     */         try {
/* 206 */           writer.write((char)c);
/* 207 */           writer.flush();
/*     */         } catch (IOException e) {
/* 209 */           buf.reset();
/* 210 */           continue;
/*     */         }
/* 212 */         byte[] ba = buf.toByteArray();
/* 213 */         for (int j = 0; j < ba.length; j++)
/*     */         {
/* 215 */           byte toEncode = ba[j];
/* 216 */           rewrittenPath.append('%');
/* 217 */           int low = toEncode & 0xF;
/* 218 */           int high = (toEncode & 0xF0) >> 4;
/* 219 */           rewrittenPath.append(hexadecimal[high]);
/* 220 */           rewrittenPath.append(hexadecimal[low]);
/*     */         }
/* 222 */         buf.reset();
/*     */       }
/*     */     }
/* 225 */     return rewrittenPath.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 231 */     URLEncoder result = new URLEncoder((BitSet)this.safeCharacters.clone());
/* 232 */     result.setEncodeSpaceAsPlus(this.encodeSpaceAsPlus);
/* 233 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\URLEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */