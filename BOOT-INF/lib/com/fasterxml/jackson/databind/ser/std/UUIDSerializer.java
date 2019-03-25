/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.UUID;
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
/*     */ public class UUIDSerializer
/*     */   extends StdScalarSerializer<UUID>
/*     */ {
/*  21 */   static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
/*     */   
/*  23 */   public UUIDSerializer() { super(UUID.class); }
/*     */   
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider prov, UUID value)
/*     */   {
/*  28 */     if (value == null) {
/*  29 */       return true;
/*     */     }
/*     */     
/*  32 */     if ((value.getLeastSignificantBits() == 0L) && (value.getMostSignificantBits() == 0L))
/*     */     {
/*  34 */       return true;
/*     */     }
/*  36 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serialize(UUID value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  44 */     if (gen.canWriteBinaryNatively())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */       if (!(gen instanceof TokenBuffer)) {
/*  51 */         gen.writeBinary(_asBytes(value));
/*  52 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  59 */     char[] ch = new char[36];
/*  60 */     long msb = value.getMostSignificantBits();
/*  61 */     _appendInt((int)(msb >> 32), ch, 0);
/*  62 */     ch[8] = '-';
/*  63 */     int i = (int)msb;
/*  64 */     _appendShort(i >>> 16, ch, 9);
/*  65 */     ch[13] = '-';
/*  66 */     _appendShort(i, ch, 14);
/*  67 */     ch[18] = '-';
/*     */     
/*  69 */     long lsb = value.getLeastSignificantBits();
/*  70 */     _appendShort((int)(lsb >>> 48), ch, 19);
/*  71 */     ch[23] = '-';
/*  72 */     _appendShort((int)(lsb >>> 32), ch, 24);
/*  73 */     _appendInt((int)lsb, ch, 28);
/*     */     
/*  75 */     gen.writeString(ch, 0, 36);
/*     */   }
/*     */   
/*     */   private static void _appendInt(int bits, char[] ch, int offset)
/*     */   {
/*  80 */     _appendShort(bits >> 16, ch, offset);
/*  81 */     _appendShort(bits, ch, offset + 4);
/*     */   }
/*     */   
/*     */   private static void _appendShort(int bits, char[] ch, int offset)
/*     */   {
/*  86 */     ch[offset] = HEX_CHARS[(bits >> 12 & 0xF)];
/*  87 */     ch[(++offset)] = HEX_CHARS[(bits >> 8 & 0xF)];
/*  88 */     ch[(++offset)] = HEX_CHARS[(bits >> 4 & 0xF)];
/*  89 */     ch[(++offset)] = HEX_CHARS[(bits & 0xF)];
/*     */   }
/*     */   
/*     */ 
/*     */   private static final byte[] _asBytes(UUID uuid)
/*     */   {
/*  95 */     byte[] buffer = new byte[16];
/*  96 */     long hi = uuid.getMostSignificantBits();
/*  97 */     long lo = uuid.getLeastSignificantBits();
/*  98 */     _appendInt((int)(hi >> 32), buffer, 0);
/*  99 */     _appendInt((int)hi, buffer, 4);
/* 100 */     _appendInt((int)(lo >> 32), buffer, 8);
/* 101 */     _appendInt((int)lo, buffer, 12);
/* 102 */     return buffer;
/*     */   }
/*     */   
/*     */   private static final void _appendInt(int value, byte[] buffer, int offset)
/*     */   {
/* 107 */     buffer[offset] = ((byte)(value >> 24));
/* 108 */     buffer[(++offset)] = ((byte)(value >> 16));
/* 109 */     buffer[(++offset)] = ((byte)(value >> 8));
/* 110 */     buffer[(++offset)] = ((byte)value);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\UUIDSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */