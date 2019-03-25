/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.nio.ByteBuffer;
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
/*     */ final class Hpack
/*     */ {
/*  25 */   private static final StringManager sm = StringManager.getManager(Hpack.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final byte LOWER_DIFF = 32;
/*     */   
/*     */ 
/*     */ 
/*     */   static final int DEFAULT_TABLE_SIZE = 4096;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MAX_INTEGER_OCTETS = 8;
/*     */   
/*     */ 
/*     */ 
/*  42 */   private static final int[] PREFIX_TABLE = new int[32]; static final HeaderField[] STATIC_TABLE;
/*  43 */   static { for (int i = 0; i < 32; i++) {
/*  44 */       int n = 0;
/*  45 */       for (int j = 0; j < i; j++) {
/*  46 */         n <<= 1;
/*  47 */         n |= 0x1;
/*     */       }
/*  49 */       PREFIX_TABLE[i] = n;
/*     */     }
/*     */     
/*  52 */     HeaderField[] fields = new HeaderField[62];
/*     */     
/*  54 */     fields[1] = new HeaderField(":authority", null);
/*  55 */     fields[2] = new HeaderField(":method", "GET");
/*  56 */     fields[3] = new HeaderField(":method", "POST");
/*  57 */     fields[4] = new HeaderField(":path", "/");
/*  58 */     fields[5] = new HeaderField(":path", "/index.html");
/*  59 */     fields[6] = new HeaderField(":scheme", "http");
/*  60 */     fields[7] = new HeaderField(":scheme", "https");
/*  61 */     fields[8] = new HeaderField(":status", "200");
/*  62 */     fields[9] = new HeaderField(":status", "204");
/*  63 */     fields[10] = new HeaderField(":status", "206");
/*  64 */     fields[11] = new HeaderField(":status", "304");
/*  65 */     fields[12] = new HeaderField(":status", "400");
/*  66 */     fields[13] = new HeaderField(":status", "404");
/*  67 */     fields[14] = new HeaderField(":status", "500");
/*  68 */     fields[15] = new HeaderField("accept-charset", null);
/*  69 */     fields[16] = new HeaderField("accept-encoding", "gzip, deflate");
/*  70 */     fields[17] = new HeaderField("accept-language", null);
/*  71 */     fields[18] = new HeaderField("accept-ranges", null);
/*  72 */     fields[19] = new HeaderField("accept", null);
/*  73 */     fields[20] = new HeaderField("access-control-allow-origin", null);
/*  74 */     fields[21] = new HeaderField("age", null);
/*  75 */     fields[22] = new HeaderField("allow", null);
/*  76 */     fields[23] = new HeaderField("authorization", null);
/*  77 */     fields[24] = new HeaderField("cache-control", null);
/*  78 */     fields[25] = new HeaderField("content-disposition", null);
/*  79 */     fields[26] = new HeaderField("content-encoding", null);
/*  80 */     fields[27] = new HeaderField("content-language", null);
/*  81 */     fields[28] = new HeaderField("content-length", null);
/*  82 */     fields[29] = new HeaderField("content-location", null);
/*  83 */     fields[30] = new HeaderField("content-range", null);
/*  84 */     fields[31] = new HeaderField("content-type", null);
/*  85 */     fields[32] = new HeaderField("cookie", null);
/*  86 */     fields[33] = new HeaderField("date", null);
/*  87 */     fields[34] = new HeaderField("etag", null);
/*  88 */     fields[35] = new HeaderField("expect", null);
/*  89 */     fields[36] = new HeaderField("expires", null);
/*  90 */     fields[37] = new HeaderField("from", null);
/*  91 */     fields[38] = new HeaderField("host", null);
/*  92 */     fields[39] = new HeaderField("if-match", null);
/*  93 */     fields[40] = new HeaderField("if-modified-since", null);
/*  94 */     fields[41] = new HeaderField("if-none-match", null);
/*  95 */     fields[42] = new HeaderField("if-range", null);
/*  96 */     fields[43] = new HeaderField("if-unmodified-since", null);
/*  97 */     fields[44] = new HeaderField("last-modified", null);
/*  98 */     fields[45] = new HeaderField("link", null);
/*  99 */     fields[46] = new HeaderField("location", null);
/* 100 */     fields[47] = new HeaderField("max-forwards", null);
/* 101 */     fields[48] = new HeaderField("proxy-authenticate", null);
/* 102 */     fields[49] = new HeaderField("proxy-authorization", null);
/* 103 */     fields[50] = new HeaderField("range", null);
/* 104 */     fields[51] = new HeaderField("referer", null);
/* 105 */     fields[52] = new HeaderField("refresh", null);
/* 106 */     fields[53] = new HeaderField("retry-after", null);
/* 107 */     fields[54] = new HeaderField("server", null);
/* 108 */     fields[55] = new HeaderField("set-cookie", null);
/* 109 */     fields[56] = new HeaderField("strict-transport-security", null);
/* 110 */     fields[57] = new HeaderField("transfer-encoding", null);
/* 111 */     fields[58] = new HeaderField("user-agent", null);
/* 112 */     fields[59] = new HeaderField("vary", null);
/* 113 */     fields[60] = new HeaderField("via", null);
/* 114 */     fields[61] = new HeaderField("www-authenticate", null);
/* 115 */     STATIC_TABLE = fields; }
/* 116 */   static final int STATIC_TABLE_LENGTH = STATIC_TABLE.length - 1;
/*     */   
/*     */   static class HeaderField
/*     */   {
/*     */     final String name;
/*     */     final String value;
/*     */     final int size;
/*     */     
/*     */     HeaderField(String name, String value) {
/* 125 */       this.name = name;
/* 126 */       this.value = value;
/* 127 */       if (value != null) {
/* 128 */         this.size = (32 + name.length() + value.length());
/*     */       } else {
/* 130 */         this.size = -1;
/*     */       }
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
/*     */ 
/*     */ 
/*     */   static int decodeInteger(ByteBuffer source, int n)
/*     */     throws HpackException
/*     */   {
/* 147 */     if (source.remaining() == 0) {
/* 148 */       return -1;
/*     */     }
/* 150 */     int count = 1;
/* 151 */     int sp = source.position();
/* 152 */     int mask = PREFIX_TABLE[n];
/*     */     
/* 154 */     int i = mask & source.get();
/*     */     
/* 156 */     if (i < PREFIX_TABLE[n]) {
/* 157 */       return i;
/*     */     }
/* 159 */     int m = 0;
/*     */     int b;
/* 161 */     do { if (count++ > 8) {
/* 162 */         throw new HpackException(sm.getString("hpack.integerEncodedOverTooManyOctets", new Object[] {
/* 163 */           Integer.valueOf(8) }));
/*     */       }
/* 165 */       if (source.remaining() == 0)
/*     */       {
/*     */ 
/* 168 */         source.position(sp);
/* 169 */         return -1;
/*     */       }
/* 171 */       b = source.get();
/* 172 */       i += (b & 0x7F) * (PREFIX_TABLE[m] + 1);
/* 173 */       m += 7;
/* 174 */     } while ((b & 0x80) == 128);
/*     */     
/* 176 */     return i;
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
/*     */   static void encodeInteger(ByteBuffer source, int value, int n)
/*     */   {
/* 191 */     int twoNminus1 = PREFIX_TABLE[n];
/* 192 */     int pos = source.position() - 1;
/* 193 */     if (value < twoNminus1) {
/* 194 */       source.put(pos, (byte)(source.get(pos) | value));
/*     */     } else {
/* 196 */       source.put(pos, (byte)(source.get(pos) | twoNminus1));
/* 197 */       value -= twoNminus1;
/* 198 */       while (value >= 128) {
/* 199 */         source.put((byte)(value % 128 + 128));
/* 200 */         value /= 128;
/*     */       }
/* 202 */       source.put((byte)value);
/*     */     }
/*     */   }
/*     */   
/*     */   static char toLower(char c)
/*     */   {
/* 208 */     if ((c >= 'A') && (c <= 'Z')) {
/* 209 */       return (char)(c + ' ');
/*     */     }
/* 211 */     return c;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Hpack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */