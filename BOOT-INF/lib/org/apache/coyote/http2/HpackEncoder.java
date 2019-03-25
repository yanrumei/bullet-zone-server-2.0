/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
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
/*     */ public class HpackEncoder
/*     */ {
/*  39 */   private static final Log log = LogFactory.getLog(HpackEncoder.class);
/*  40 */   private static final StringManager sm = StringManager.getManager(HpackEncoder.class);
/*     */   
/*  42 */   public static final HpackHeaderFunction DEFAULT_HEADER_FUNCTION = new HpackHeaderFunction()
/*     */   {
/*     */ 
/*     */     public boolean shouldUseIndexing(String headerName, String value)
/*     */     {
/*  47 */       return (!headerName.equals("content-length")) && (!headerName.equals("date"));
/*     */     }
/*     */     
/*     */     public boolean shouldUseHuffman(String header, String value)
/*     */     {
/*  52 */       return value.length() > 5;
/*     */     }
/*     */     
/*     */     public boolean shouldUseHuffman(String header)
/*     */     {
/*  57 */       return header.length() > 5;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*  63 */   private int headersIterator = -1;
/*  64 */   private boolean firstPass = true;
/*     */   
/*     */   private MimeHeaders currentHeaders;
/*     */   
/*     */   private int entryPositionCounter;
/*     */   
/*  70 */   private int newMaxHeaderSize = -1;
/*  71 */   private int minNewMaxHeaderSize = -1;
/*     */   
/*     */   private static final Map<String, TableEntry[]> ENCODING_STATIC_TABLE;
/*     */   
/*  75 */   private final Deque<TableEntry> evictionQueue = new ArrayDeque();
/*  76 */   private final Map<String, List<TableEntry>> dynamicTable = new HashMap();
/*     */   
/*     */   static {
/*  79 */     Map<String, TableEntry[]> map = new HashMap();
/*  80 */     for (int i = 1; i < Hpack.STATIC_TABLE.length; i++) {
/*  81 */       Hpack.HeaderField m = Hpack.STATIC_TABLE[i];
/*  82 */       TableEntry[] existing = (TableEntry[])map.get(m.name);
/*  83 */       if (existing == null) {
/*  84 */         map.put(m.name, new TableEntry[] { new TableEntry(m.name, m.value, i) });
/*     */       } else {
/*  86 */         TableEntry[] newEntry = new TableEntry[existing.length + 1];
/*  87 */         System.arraycopy(existing, 0, newEntry, 0, existing.length);
/*  88 */         newEntry[existing.length] = new TableEntry(m.name, m.value, i);
/*  89 */         map.put(m.name, newEntry);
/*     */       }
/*     */     }
/*  92 */     ENCODING_STATIC_TABLE = Collections.unmodifiableMap(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private int maxTableSize = 4096;
/*     */   
/*     */ 
/*     */   private int currentTableSize;
/*     */   
/*     */   private final HpackHeaderFunction hpackHeaderFunction;
/*     */   
/*     */ 
/*     */   HpackEncoder()
/*     */   {
/* 108 */     this.hpackHeaderFunction = DEFAULT_HEADER_FUNCTION;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public State encode(MimeHeaders headers, ByteBuffer target)
/*     */   {
/* 120 */     int it = this.headersIterator;
/* 121 */     if (this.headersIterator == -1) {
/* 122 */       handleTableSizeChange(target);
/*     */       
/* 124 */       it = 0;
/* 125 */       this.currentHeaders = headers;
/*     */     }
/* 127 */     else if (headers != this.currentHeaders) {
/* 128 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 131 */     while (it < this.currentHeaders.size())
/*     */     {
/* 133 */       String headerName = headers.getName(it).toString().toLowerCase(Locale.US);
/* 134 */       boolean skip = false;
/* 135 */       if (this.firstPass) {
/* 136 */         if (headerName.charAt(0) != ':') {
/* 137 */           skip = true;
/*     */         }
/*     */       }
/* 140 */       else if (headerName.charAt(0) == ':') {
/* 141 */         skip = true;
/*     */       }
/*     */       
/* 144 */       if (!skip) {
/* 145 */         String val = headers.getValue(it).toString();
/*     */         
/* 147 */         if (log.isDebugEnabled()) {
/* 148 */           log.debug(sm.getString("hpackEncoder.encodeHeader", new Object[] { headerName, val }));
/*     */         }
/* 150 */         TableEntry tableEntry = findInTable(headerName, val);
/*     */         
/*     */ 
/*     */ 
/* 154 */         int required = 11 + headerName.length() + 1 + val.length();
/*     */         
/* 156 */         if (target.remaining() < required) {
/* 157 */           this.headersIterator = it;
/* 158 */           return State.UNDERFLOW;
/*     */         }
/*     */         
/*     */ 
/* 162 */         boolean canIndex = (this.hpackHeaderFunction.shouldUseIndexing(headerName, val)) && (headerName.length() + val.length() + 32 < this.maxTableSize);
/* 163 */         if ((tableEntry == null) && (canIndex))
/*     */         {
/* 165 */           target.put((byte)64);
/* 166 */           writeHuffmanEncodableName(target, headerName);
/* 167 */           writeHuffmanEncodableValue(target, headerName, val);
/* 168 */           addToDynamicTable(headerName, val);
/* 169 */         } else if (tableEntry == null)
/*     */         {
/* 171 */           target.put((byte)16);
/* 172 */           writeHuffmanEncodableName(target, headerName);
/* 173 */           writeHuffmanEncodableValue(target, headerName, val);
/*     */ 
/*     */         }
/* 176 */         else if (val.equals(tableEntry.value))
/*     */         {
/* 178 */           target.put((byte)Byte.MIN_VALUE);
/* 179 */           Hpack.encodeInteger(target, tableEntry.getPosition(), 7);
/*     */         }
/* 181 */         else if (canIndex)
/*     */         {
/* 183 */           target.put((byte)64);
/* 184 */           Hpack.encodeInteger(target, tableEntry.getPosition(), 6);
/* 185 */           writeHuffmanEncodableValue(target, headerName, val);
/* 186 */           addToDynamicTable(headerName, val);
/*     */         }
/*     */         else {
/* 189 */           target.put((byte)16);
/* 190 */           Hpack.encodeInteger(target, tableEntry.getPosition(), 4);
/* 191 */           writeHuffmanEncodableValue(target, headerName, val);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 197 */       it++; if ((it == this.currentHeaders.size()) && (this.firstPass)) {
/* 198 */         this.firstPass = false;
/* 199 */         it = 0;
/*     */       }
/*     */     }
/* 202 */     this.headersIterator = -1;
/* 203 */     this.firstPass = true;
/* 204 */     return State.COMPLETE;
/*     */   }
/*     */   
/*     */   private void writeHuffmanEncodableName(ByteBuffer target, String headerName) {
/* 208 */     if ((this.hpackHeaderFunction.shouldUseHuffman(headerName)) && 
/* 209 */       (HPackHuffman.encode(target, headerName, true))) {
/* 210 */       return;
/*     */     }
/*     */     
/* 213 */     target.put((byte)0);
/* 214 */     Hpack.encodeInteger(target, headerName.length(), 7);
/* 215 */     for (int j = 0; j < headerName.length(); j++) {
/* 216 */       target.put((byte)Hpack.toLower(headerName.charAt(j)));
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeHuffmanEncodableValue(ByteBuffer target, String headerName, String val)
/*     */   {
/* 222 */     if (this.hpackHeaderFunction.shouldUseHuffman(headerName, val)) {
/* 223 */       if (!HPackHuffman.encode(target, val, false)) {
/* 224 */         writeValueString(target, val);
/*     */       }
/*     */     } else {
/* 227 */       writeValueString(target, val);
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeValueString(ByteBuffer target, String val) {
/* 232 */     target.put((byte)0);
/* 233 */     Hpack.encodeInteger(target, val.length(), 7);
/* 234 */     for (int j = 0; j < val.length(); j++) {
/* 235 */       target.put((byte)val.charAt(j));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addToDynamicTable(String headerName, String val) {
/* 240 */     int pos = this.entryPositionCounter++;
/* 241 */     DynamicTableEntry d = new DynamicTableEntry(headerName, val, -pos);
/* 242 */     List<TableEntry> existing = (List)this.dynamicTable.get(headerName);
/* 243 */     if (existing == null) {
/* 244 */       this.dynamicTable.put(headerName, existing = new ArrayList(1));
/*     */     }
/* 246 */     existing.add(d);
/* 247 */     this.evictionQueue.add(d);
/* 248 */     this.currentTableSize += d.size;
/* 249 */     runEvictionIfRequired();
/* 250 */     if (this.entryPositionCounter == Integer.MAX_VALUE)
/*     */     {
/* 252 */       preventPositionRollover();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void preventPositionRollover()
/*     */   {
/* 261 */     for (Map.Entry<String, List<TableEntry>> entry : this.dynamicTable.entrySet()) {
/* 262 */       for (TableEntry t : (List)entry.getValue()) {
/* 263 */         t.position = t.getPosition();
/*     */       }
/*     */     }
/* 266 */     this.entryPositionCounter = 0;
/*     */   }
/*     */   
/*     */   private void runEvictionIfRequired()
/*     */   {
/* 271 */     while (this.currentTableSize > this.maxTableSize) {
/* 272 */       TableEntry next = (TableEntry)this.evictionQueue.poll();
/* 273 */       if (next == null) {
/* 274 */         return;
/*     */       }
/* 276 */       this.currentTableSize -= next.size;
/* 277 */       List<TableEntry> list = (List)this.dynamicTable.get(next.name);
/* 278 */       list.remove(next);
/* 279 */       if (list.isEmpty()) {
/* 280 */         this.dynamicTable.remove(next.name);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private TableEntry findInTable(String headerName, String value) {
/* 286 */     TableEntry[] staticTable = (TableEntry[])ENCODING_STATIC_TABLE.get(headerName);
/* 287 */     if (staticTable != null) {
/* 288 */       for (TableEntry st : staticTable) {
/* 289 */         if ((st.value != null) && (st.value.equals(value))) {
/* 290 */           return st;
/*     */         }
/*     */       }
/*     */     }
/* 294 */     Object dynamic = (List)this.dynamicTable.get(headerName);
/* 295 */     if (dynamic != null) {
/* 296 */       for (TableEntry st : (List)dynamic) {
/* 297 */         if (st.value.equals(value)) {
/* 298 */           return st;
/*     */         }
/*     */       }
/*     */     }
/* 302 */     if (staticTable != null) {
/* 303 */       return staticTable[0];
/*     */     }
/* 305 */     return null;
/*     */   }
/*     */   
/*     */   public void setMaxTableSize(int newSize) {
/* 309 */     this.newMaxHeaderSize = newSize;
/* 310 */     if (this.minNewMaxHeaderSize == -1) {
/* 311 */       this.minNewMaxHeaderSize = newSize;
/*     */     } else {
/* 313 */       this.minNewMaxHeaderSize = Math.min(newSize, this.minNewMaxHeaderSize);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleTableSizeChange(ByteBuffer target) {
/* 318 */     if (this.newMaxHeaderSize == -1) {
/* 319 */       return;
/*     */     }
/* 321 */     if (this.minNewMaxHeaderSize != this.newMaxHeaderSize) {
/* 322 */       target.put((byte)32);
/* 323 */       Hpack.encodeInteger(target, this.minNewMaxHeaderSize, 5);
/*     */     }
/* 325 */     target.put((byte)32);
/* 326 */     Hpack.encodeInteger(target, this.newMaxHeaderSize, 5);
/* 327 */     this.maxTableSize = this.newMaxHeaderSize;
/* 328 */     runEvictionIfRequired();
/* 329 */     this.newMaxHeaderSize = -1;
/* 330 */     this.minNewMaxHeaderSize = -1;
/*     */   }
/*     */   
/*     */   public static enum State {
/* 334 */     COMPLETE, 
/* 335 */     UNDERFLOW;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */   
/*     */   static class TableEntry {
/*     */     final String name;
/*     */     final String value;
/*     */     final int size;
/*     */     int position;
/*     */     
/* 346 */     TableEntry(String name, String value, int position) { this.name = name;
/* 347 */       this.value = value;
/* 348 */       this.position = position;
/* 349 */       if (value != null) {
/* 350 */         this.size = (32 + name.length() + value.length());
/*     */       } else {
/* 352 */         this.size = -1;
/*     */       }
/*     */     }
/*     */     
/*     */     public int getPosition() {
/* 357 */       return this.position;
/*     */     }
/*     */   }
/*     */   
/*     */   class DynamicTableEntry extends HpackEncoder.TableEntry
/*     */   {
/*     */     DynamicTableEntry(String name, String value, int position) {
/* 364 */       super(value, position);
/*     */     }
/*     */     
/*     */     public int getPosition()
/*     */     {
/* 369 */       return super.getPosition() + HpackEncoder.this.entryPositionCounter + Hpack.STATIC_TABLE_LENGTH;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface HpackHeaderFunction
/*     */   {
/*     */     public abstract boolean shouldUseIndexing(String paramString1, String paramString2);
/*     */     
/*     */     public abstract boolean shouldUseHuffman(String paramString1, String paramString2);
/*     */     
/*     */     public abstract boolean shouldUseHuffman(String paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\HpackEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */