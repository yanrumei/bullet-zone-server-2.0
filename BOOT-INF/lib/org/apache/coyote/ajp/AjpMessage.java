/*     */ package org.apache.coyote.ajp;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.CharChunk;
/*     */ import org.apache.tomcat.util.buf.HexUtils;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
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
/*     */ public class AjpMessage
/*     */ {
/*  45 */   private static final Log log = LogFactory.getLog(AjpMessage.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   protected static final StringManager sm = StringManager.getManager(AjpMessage.class);
/*     */   protected final byte[] buf;
/*     */   protected int pos;
/*     */   protected int len;
/*     */   
/*     */   public AjpMessage(int packetSize)
/*     */   {
/*  57 */     this.buf = new byte[packetSize];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/*  94 */     this.len = 4;
/*  95 */     this.pos = 4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void end()
/*     */   {
/* 105 */     this.len = this.pos;
/* 106 */     int dLen = this.len - 4;
/*     */     
/* 108 */     this.buf[0] = 65;
/* 109 */     this.buf[1] = 66;
/* 110 */     this.buf[2] = ((byte)(dLen >>> 8 & 0xFF));
/* 111 */     this.buf[3] = ((byte)(dLen & 0xFF));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getBuffer()
/*     */   {
/* 121 */     return this.buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLen()
/*     */   {
/* 133 */     return this.len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendInt(int val)
/*     */   {
/* 143 */     this.buf[(this.pos++)] = ((byte)(val >>> 8 & 0xFF));
/* 144 */     this.buf[(this.pos++)] = ((byte)(val & 0xFF));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendByte(int val)
/*     */   {
/* 154 */     this.buf[(this.pos++)] = ((byte)val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendBytes(MessageBytes mb)
/*     */   {
/* 165 */     if (mb == null) {
/* 166 */       log.error(sm.getString("ajpmessage.null"), new NullPointerException());
/*     */       
/* 168 */       appendInt(0);
/* 169 */       appendByte(0);
/* 170 */       return;
/*     */     }
/* 172 */     if (mb.getType() != 2) {
/* 173 */       mb.toBytes();
/* 174 */       ByteChunk bc = mb.getByteChunk();
/*     */       
/*     */ 
/*     */ 
/* 178 */       byte[] buffer = bc.getBuffer();
/* 179 */       for (int i = bc.getOffset(); i < bc.getLength(); i++)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 185 */         if (((buffer[i] > -1) && (buffer[i] <= 31) && (buffer[i] != 9)) || (buffer[i] == Byte.MAX_VALUE))
/*     */         {
/* 187 */           buffer[i] = 32;
/*     */         }
/*     */       }
/*     */     }
/* 191 */     appendByteChunk(mb.getByteChunk());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendByteChunk(ByteChunk bc)
/*     */   {
/* 202 */     if (bc == null) {
/* 203 */       log.error(sm.getString("ajpmessage.null"), new NullPointerException());
/*     */       
/* 205 */       appendInt(0);
/* 206 */       appendByte(0);
/* 207 */       return;
/*     */     }
/* 209 */     appendBytes(bc.getBytes(), bc.getStart(), bc.getLength());
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
/*     */   public void appendBytes(byte[] b, int off, int numBytes)
/*     */   {
/* 225 */     if (checkOverflow(numBytes)) {
/* 226 */       return;
/*     */     }
/* 228 */     appendInt(numBytes);
/* 229 */     System.arraycopy(b, off, this.buf, this.pos, numBytes);
/* 230 */     this.pos += numBytes;
/* 231 */     appendByte(0);
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
/*     */   public void appendBytes(ByteBuffer b)
/*     */   {
/* 245 */     int numBytes = b.remaining();
/* 246 */     if (checkOverflow(numBytes)) {
/* 247 */       return;
/*     */     }
/* 249 */     appendInt(numBytes);
/* 250 */     b.get(this.buf, this.pos, numBytes);
/* 251 */     this.pos += numBytes;
/* 252 */     appendByte(0);
/*     */   }
/*     */   
/*     */   private boolean checkOverflow(int numBytes)
/*     */   {
/* 257 */     if (this.pos + numBytes + 3 > this.buf.length) {
/* 258 */       log.error(sm.getString("ajpmessage.overflow", new Object[] { "" + numBytes, "" + this.pos }), new ArrayIndexOutOfBoundsException());
/*     */       
/* 260 */       if (log.isDebugEnabled()) {
/* 261 */         dump("Overflow/coBytes");
/*     */       }
/* 263 */       return true;
/*     */     }
/* 265 */     return false;
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
/*     */   public int getInt()
/*     */   {
/* 278 */     int b1 = this.buf[(this.pos++)] & 0xFF;
/* 279 */     int b2 = this.buf[(this.pos++)] & 0xFF;
/* 280 */     validatePos(this.pos);
/* 281 */     return (b1 << 8) + b2;
/*     */   }
/*     */   
/*     */   public int peekInt()
/*     */   {
/* 286 */     validatePos(this.pos + 2);
/* 287 */     int b1 = this.buf[this.pos] & 0xFF;
/* 288 */     int b2 = this.buf[(this.pos + 1)] & 0xFF;
/* 289 */     return (b1 << 8) + b2;
/*     */   }
/*     */   
/*     */   public byte getByte()
/*     */   {
/* 294 */     byte res = this.buf[(this.pos++)];
/* 295 */     validatePos(this.pos);
/* 296 */     return res;
/*     */   }
/*     */   
/*     */   public void getBytes(MessageBytes mb)
/*     */   {
/* 301 */     doGetBytes(mb, true);
/*     */   }
/*     */   
/*     */   public void getBodyBytes(MessageBytes mb) {
/* 305 */     doGetBytes(mb, false);
/*     */   }
/*     */   
/*     */   private void doGetBytes(MessageBytes mb, boolean terminated) {
/* 309 */     int length = getInt();
/* 310 */     if ((length == 65535) || (length == -1)) {
/* 311 */       mb.recycle();
/* 312 */       return;
/*     */     }
/* 314 */     if (terminated) {
/* 315 */       validatePos(this.pos + length + 1);
/*     */     } else {
/* 317 */       validatePos(this.pos + length);
/*     */     }
/* 319 */     mb.setBytes(this.buf, this.pos, length);
/* 320 */     mb.getCharChunk().recycle();
/* 321 */     this.pos += length;
/* 322 */     if (terminated) {
/* 323 */       this.pos += 1;
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
/*     */   public int getLongInt()
/*     */   {
/* 337 */     int b1 = this.buf[(this.pos++)] & 0xFF;
/* 338 */     b1 <<= 8;
/* 339 */     b1 |= this.buf[(this.pos++)] & 0xFF;
/* 340 */     b1 <<= 8;
/* 341 */     b1 |= this.buf[(this.pos++)] & 0xFF;
/* 342 */     b1 <<= 8;
/* 343 */     b1 |= this.buf[(this.pos++)] & 0xFF;
/* 344 */     validatePos(this.pos);
/* 345 */     return b1;
/*     */   }
/*     */   
/*     */   public int processHeader(boolean toContainer)
/*     */   {
/* 350 */     this.pos = 0;
/* 351 */     int mark = getInt();
/* 352 */     this.len = getInt();
/*     */     
/* 354 */     if (((toContainer) && (mark != 4660)) || ((!toContainer) && (mark != 16706)))
/*     */     {
/* 356 */       log.error(sm.getString("ajpmessage.invalid", new Object[] { "" + mark }));
/* 357 */       if (log.isDebugEnabled()) {
/* 358 */         dump("In");
/*     */       }
/* 360 */       return -1;
/*     */     }
/* 362 */     if (log.isDebugEnabled()) {
/* 363 */       log.debug("Received " + this.len + " " + this.buf[0]);
/*     */     }
/* 365 */     return this.len;
/*     */   }
/*     */   
/*     */   private void dump(String prefix)
/*     */   {
/* 370 */     if (log.isDebugEnabled()) {
/* 371 */       log.debug(prefix + ": " + HexUtils.toHexString(this.buf) + " " + this.pos + "/" + (this.len + 4));
/*     */     }
/* 373 */     int max = this.pos;
/* 374 */     if (this.len + 4 > this.pos)
/* 375 */       max = this.len + 4;
/* 376 */     if (max > 1000)
/* 377 */       max = 1000;
/* 378 */     if (log.isDebugEnabled()) {
/* 379 */       for (int j = 0; j < max; j += 16) {
/* 380 */         log.debug(hexLine(this.buf, j, this.len));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void validatePos(int posToTest)
/*     */   {
/* 387 */     if (posToTest > this.len + 4)
/*     */     {
/* 389 */       throw new ArrayIndexOutOfBoundsException(sm.getString("ajpMessage.invalidPos", new Object[] {
/* 390 */         Integer.valueOf(posToTest) }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected static String hexLine(byte[] buf, int start, int len)
/*     */   {
/* 397 */     StringBuilder sb = new StringBuilder();
/* 398 */     for (int i = start; i < start + 16; i++) {
/* 399 */       if (i < len + 4) {
/* 400 */         sb.append(hex(buf[i]) + " ");
/*     */       } else {
/* 402 */         sb.append("   ");
/*     */       }
/*     */     }
/* 405 */     sb.append(" | ");
/* 406 */     for (int i = start; (i < start + 16) && (i < len + 4); i++) {
/* 407 */       if (!Character.isISOControl((char)buf[i])) {
/* 408 */         sb.append(Character.valueOf((char)buf[i]));
/*     */       } else {
/* 410 */         sb.append(".");
/*     */       }
/*     */     }
/* 413 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected static String hex(int x)
/*     */   {
/* 418 */     String h = Integer.toHexString(x);
/* 419 */     if (h.length() == 1) {
/* 420 */       h = "0" + h;
/*     */     }
/* 422 */     return h.substring(h.length() - 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ajp\AjpMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */