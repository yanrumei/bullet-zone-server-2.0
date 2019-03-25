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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HpackDecoder
/*     */ {
/*  28 */   protected static final StringManager sm = StringManager.getManager(HpackDecoder.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int DEFAULT_RING_BUFFER_SIZE = 10;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private HeaderEmitter headerEmitter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Hpack.HeaderField[] headerTable;
/*     */   
/*     */ 
/*     */ 
/*  47 */   private int firstSlotPosition = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private int filledTableSlots = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private int currentMemorySize = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   private int maxMemorySizeHard;
/*     */   
/*     */ 
/*     */ 
/*     */   private int maxMemorySizeSoft;
/*     */   
/*     */ 
/*  68 */   private int maxHeaderCount = 100;
/*  69 */   private int maxHeaderSize = 8192;
/*     */   
/*  71 */   private volatile int headerCount = 0;
/*     */   private volatile boolean countedCookie;
/*  73 */   private volatile int headerSize = 0;
/*     */   
/*  75 */   private final StringBuilder stringBuilder = new StringBuilder();
/*     */   
/*     */   public HpackDecoder(int maxMemorySize) {
/*  78 */     this.maxMemorySizeHard = maxMemorySize;
/*  79 */     this.maxMemorySizeSoft = maxMemorySize;
/*  80 */     this.headerTable = new Hpack.HeaderField[10];
/*     */   }
/*     */   
/*     */   public HpackDecoder() {
/*  84 */     this(4096);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void decode(ByteBuffer buffer)
/*     */     throws HpackException
/*     */   {
/*  97 */     while (buffer.hasRemaining()) {
/*  98 */       int originalPos = buffer.position();
/*  99 */       byte b = buffer.get();
/* 100 */       if ((b & 0x80) != 0)
/*     */       {
/* 102 */         buffer.position(buffer.position() - 1);
/* 103 */         int index = Hpack.decodeInteger(buffer, 7);
/* 104 */         if (index == -1) {
/* 105 */           buffer.position(originalPos);
/* 106 */           return; }
/* 107 */         if (index == 0)
/*     */         {
/* 109 */           throw new HpackException(sm.getString("hpackdecoder.zeroNotValidHeaderTableIndex"));
/*     */         }
/* 111 */         handleIndex(index);
/* 112 */       } else if ((b & 0x40) != 0)
/*     */       {
/* 114 */         String headerName = readHeaderName(buffer, 6);
/* 115 */         if (headerName == null) {
/* 116 */           buffer.position(originalPos);
/* 117 */           return;
/*     */         }
/* 119 */         String headerValue = readHpackString(buffer);
/* 120 */         if (headerValue == null) {
/* 121 */           buffer.position(originalPos);
/* 122 */           return;
/*     */         }
/* 124 */         emitHeader(headerName, headerValue);
/* 125 */         addEntryToHeaderTable(new Hpack.HeaderField(headerName, headerValue));
/* 126 */       } else if ((b & 0xF0) == 0)
/*     */       {
/* 128 */         String headerName = readHeaderName(buffer, 4);
/* 129 */         if (headerName == null) {
/* 130 */           buffer.position(originalPos);
/* 131 */           return;
/*     */         }
/* 133 */         String headerValue = readHpackString(buffer);
/* 134 */         if (headerValue == null) {
/* 135 */           buffer.position(originalPos);
/* 136 */           return;
/*     */         }
/* 138 */         emitHeader(headerName, headerValue);
/* 139 */       } else if ((b & 0xF0) == 16)
/*     */       {
/* 141 */         String headerName = readHeaderName(buffer, 4);
/* 142 */         if (headerName == null) {
/* 143 */           buffer.position(originalPos);
/* 144 */           return;
/*     */         }
/* 146 */         String headerValue = readHpackString(buffer);
/* 147 */         if (headerValue == null) {
/* 148 */           buffer.position(originalPos);
/* 149 */           return;
/*     */         }
/* 151 */         emitHeader(headerName, headerValue);
/* 152 */       } else if ((b & 0xE0) == 32)
/*     */       {
/* 154 */         if (handleMaxMemorySizeChange(buffer, originalPos)) {}
/*     */       }
/*     */       else
/*     */       {
/* 158 */         throw new RuntimeException("Not yet implemented");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean handleMaxMemorySizeChange(ByteBuffer buffer, int originalPos) throws HpackException {
/* 164 */     if (this.headerCount != 0) {
/* 165 */       throw new HpackException(sm.getString("hpackdecoder.tableSizeUpdateNotAtStart"));
/*     */     }
/* 167 */     buffer.position(buffer.position() - 1);
/* 168 */     int size = Hpack.decodeInteger(buffer, 5);
/* 169 */     if (size == -1) {
/* 170 */       buffer.position(originalPos);
/* 171 */       return false;
/*     */     }
/* 173 */     if (size > this.maxMemorySizeHard) {
/* 174 */       throw new HpackException();
/*     */     }
/* 176 */     this.maxMemorySizeSoft = size;
/* 177 */     if (this.currentMemorySize > this.maxMemorySizeSoft) {
/* 178 */       int newTableSlots = this.filledTableSlots;
/* 179 */       int tableLength = this.headerTable.length;
/* 180 */       int newSize = this.currentMemorySize;
/* 181 */       while (newSize > this.maxMemorySizeSoft) {
/* 182 */         int clearIndex = this.firstSlotPosition;
/* 183 */         this.firstSlotPosition += 1;
/* 184 */         if (this.firstSlotPosition == tableLength) {
/* 185 */           this.firstSlotPosition = 0;
/*     */         }
/* 187 */         Hpack.HeaderField oldData = this.headerTable[clearIndex];
/* 188 */         this.headerTable[clearIndex] = null;
/* 189 */         newSize -= oldData.size;
/* 190 */         newTableSlots--;
/*     */       }
/* 192 */       this.filledTableSlots = newTableSlots;
/* 193 */       this.currentMemorySize = newSize;
/*     */     }
/* 195 */     return true;
/*     */   }
/*     */   
/*     */   private String readHeaderName(ByteBuffer buffer, int prefixLength) throws HpackException {
/* 199 */     buffer.position(buffer.position() - 1);
/* 200 */     int index = Hpack.decodeInteger(buffer, prefixLength);
/* 201 */     if (index == -1)
/* 202 */       return null;
/* 203 */     if (index != 0) {
/* 204 */       return handleIndexedHeaderName(index);
/*     */     }
/* 206 */     return readHpackString(buffer);
/*     */   }
/*     */   
/*     */   private String readHpackString(ByteBuffer buffer) throws HpackException
/*     */   {
/* 211 */     if (!buffer.hasRemaining()) {
/* 212 */       return null;
/*     */     }
/* 214 */     byte data = buffer.get(buffer.position());
/*     */     
/* 216 */     int length = Hpack.decodeInteger(buffer, 7);
/* 217 */     if (buffer.remaining() < length) {
/* 218 */       return null;
/*     */     }
/* 220 */     boolean huffman = (data & 0x80) != 0;
/* 221 */     if (huffman) {
/* 222 */       return readHuffmanString(length, buffer);
/*     */     }
/* 224 */     for (int i = 0; i < length; i++) {
/* 225 */       this.stringBuilder.append((char)buffer.get());
/*     */     }
/* 227 */     String ret = this.stringBuilder.toString();
/* 228 */     this.stringBuilder.setLength(0);
/* 229 */     return ret;
/*     */   }
/*     */   
/*     */   private String readHuffmanString(int length, ByteBuffer buffer) throws HpackException {
/* 233 */     HPackHuffman.decode(buffer, length, this.stringBuilder);
/* 234 */     String ret = this.stringBuilder.toString();
/* 235 */     this.stringBuilder.setLength(0);
/* 236 */     return ret;
/*     */   }
/*     */   
/*     */   private String handleIndexedHeaderName(int index) throws HpackException {
/* 240 */     if (index <= Hpack.STATIC_TABLE_LENGTH) {
/* 241 */       return Hpack.STATIC_TABLE[index].name;
/*     */     }
/*     */     
/* 244 */     if (index > Hpack.STATIC_TABLE_LENGTH + this.filledTableSlots) {
/* 245 */       throw new HpackException(sm.getString("hpackdecoder.headerTableIndexInvalid", new Object[] {
/* 246 */         Integer.valueOf(index), Integer.valueOf(Hpack.STATIC_TABLE_LENGTH), 
/* 247 */         Integer.valueOf(this.filledTableSlots) }));
/*     */     }
/* 249 */     int adjustedIndex = getRealIndex(index - Hpack.STATIC_TABLE_LENGTH);
/* 250 */     Hpack.HeaderField res = this.headerTable[adjustedIndex];
/* 251 */     if (res == null) {
/* 252 */       throw new HpackException();
/*     */     }
/* 254 */     return res.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void handleIndex(int index)
/*     */     throws HpackException
/*     */   {
/* 265 */     if (index <= Hpack.STATIC_TABLE_LENGTH) {
/* 266 */       addStaticTableEntry(index);
/*     */     } else {
/* 268 */       int adjustedIndex = getRealIndex(index - Hpack.STATIC_TABLE_LENGTH);
/* 269 */       Hpack.HeaderField headerField = this.headerTable[adjustedIndex];
/* 270 */       emitHeader(headerField.name, headerField.value);
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
/*     */ 
/*     */ 
/*     */   int getRealIndex(int index)
/*     */   {
/* 287 */     return (this.firstSlotPosition + (this.filledTableSlots - index)) % this.headerTable.length;
/*     */   }
/*     */   
/*     */   private void addStaticTableEntry(int index)
/*     */     throws HpackException
/*     */   {
/* 293 */     Hpack.HeaderField entry = Hpack.STATIC_TABLE[index];
/* 294 */     if (entry.value == null) {
/* 295 */       throw new HpackException();
/*     */     }
/* 297 */     emitHeader(entry.name, entry.value);
/*     */   }
/*     */   
/*     */   private void addEntryToHeaderTable(Hpack.HeaderField entry) {
/* 301 */     if (entry.size > this.maxMemorySizeSoft)
/*     */     {
/* 303 */       while (this.filledTableSlots > 0) {
/* 304 */         this.headerTable[this.firstSlotPosition] = null;
/* 305 */         this.firstSlotPosition += 1;
/* 306 */         if (this.firstSlotPosition == this.headerTable.length) {
/* 307 */           this.firstSlotPosition = 0;
/*     */         }
/* 309 */         this.filledTableSlots -= 1;
/*     */       }
/* 311 */       this.currentMemorySize = 0;
/* 312 */       return;
/*     */     }
/* 314 */     resizeIfRequired();
/* 315 */     int newTableSlots = this.filledTableSlots + 1;
/* 316 */     int tableLength = this.headerTable.length;
/* 317 */     int index = (this.firstSlotPosition + this.filledTableSlots) % tableLength;
/* 318 */     this.headerTable[index] = entry;
/* 319 */     int newSize = this.currentMemorySize + entry.size;
/* 320 */     while (newSize > this.maxMemorySizeSoft) {
/* 321 */       int clearIndex = this.firstSlotPosition;
/* 322 */       this.firstSlotPosition += 1;
/* 323 */       if (this.firstSlotPosition == tableLength) {
/* 324 */         this.firstSlotPosition = 0;
/*     */       }
/* 326 */       Hpack.HeaderField oldData = this.headerTable[clearIndex];
/* 327 */       this.headerTable[clearIndex] = null;
/* 328 */       newSize -= oldData.size;
/* 329 */       newTableSlots--;
/*     */     }
/* 331 */     this.filledTableSlots = newTableSlots;
/* 332 */     this.currentMemorySize = newSize;
/*     */   }
/*     */   
/*     */   private void resizeIfRequired() {
/* 336 */     if (this.filledTableSlots == this.headerTable.length) {
/* 337 */       Hpack.HeaderField[] newArray = new Hpack.HeaderField[this.headerTable.length + 10];
/* 338 */       for (int i = 0; i < this.headerTable.length; i++) {
/* 339 */         newArray[i] = this.headerTable[((this.firstSlotPosition + i) % this.headerTable.length)];
/*     */       }
/* 341 */       this.firstSlotPosition = 0;
/* 342 */       this.headerTable = newArray;
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
/*     */   public HeaderEmitter getHeaderEmitter()
/*     */   {
/* 387 */     return this.headerEmitter;
/*     */   }
/*     */   
/*     */   void setHeaderEmitter(HeaderEmitter headerEmitter)
/*     */   {
/* 392 */     this.headerEmitter = headerEmitter;
/*     */     
/* 394 */     this.headerCount = 0;
/* 395 */     this.countedCookie = false;
/* 396 */     this.headerSize = 0;
/*     */   }
/*     */   
/*     */   void setMaxHeaderCount(int maxHeaderCount)
/*     */   {
/* 401 */     this.maxHeaderCount = maxHeaderCount;
/*     */   }
/*     */   
/*     */   void setMaxHeaderSize(int maxHeaderSize)
/*     */   {
/* 406 */     this.maxHeaderSize = maxHeaderSize;
/*     */   }
/*     */   
/*     */   private void emitHeader(String name, String value)
/*     */     throws HpackException
/*     */   {
/* 412 */     if ("cookie".equals(name))
/*     */     {
/*     */ 
/* 415 */       if (!this.countedCookie) {
/* 416 */         this.headerCount += 1;
/* 417 */         this.countedCookie = true;
/*     */       }
/*     */     } else {
/* 420 */       this.headerCount += 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 425 */     int inc = 3 + name.length() + value.length();
/* 426 */     this.headerSize += inc;
/* 427 */     if ((!isHeaderCountExceeded()) && (!isHeaderSizeExceeded(0))) {
/* 428 */       this.headerEmitter.emitHeader(name, value);
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isHeaderCountExceeded()
/*     */   {
/* 434 */     if (this.maxHeaderCount < 0) {
/* 435 */       return false;
/*     */     }
/* 437 */     return this.headerCount > this.maxHeaderCount;
/*     */   }
/*     */   
/*     */   boolean isHeaderSizeExceeded(int unreadSize)
/*     */   {
/* 442 */     if (this.maxHeaderSize < 0) {
/* 443 */       return false;
/*     */     }
/* 445 */     return this.headerSize + unreadSize > this.maxHeaderSize;
/*     */   }
/*     */   
/*     */   boolean isHeaderSwallowSizeExceeded(int unreadSize)
/*     */   {
/* 450 */     if (this.maxHeaderSize < 0) {
/* 451 */       return false;
/*     */     }
/*     */     
/* 454 */     return this.headerSize + unreadSize > 2 * this.maxHeaderSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int getFirstSlotPosition()
/*     */   {
/* 461 */     return this.firstSlotPosition;
/*     */   }
/*     */   
/*     */   Hpack.HeaderField[] getHeaderTable() {
/* 465 */     return this.headerTable;
/*     */   }
/*     */   
/*     */   int getFilledTableSlots() {
/* 469 */     return this.filledTableSlots;
/*     */   }
/*     */   
/*     */   int getCurrentMemorySize() {
/* 473 */     return this.currentMemorySize;
/*     */   }
/*     */   
/*     */   int getMaxMemorySizeSoft() {
/* 477 */     return this.maxMemorySizeSoft;
/*     */   }
/*     */   
/*     */   static abstract interface HeaderEmitter
/*     */   {
/*     */     public abstract void emitHeader(String paramString1, String paramString2)
/*     */       throws HpackException;
/*     */     
/*     */     public abstract void setHeaderException(StreamException paramStreamException);
/*     */     
/*     */     public abstract void validateHeaders()
/*     */       throws StreamException;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\HpackDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */