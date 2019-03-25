/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringCache
/*     */ {
/*  36 */   private static final Log log = LogFactory.getLog(StringCache.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */   protected static boolean byteEnabled = "true".equals(System.getProperty("tomcat.util.buf.StringCache.byte.enabled", "false"));
/*     */   
/*     */ 
/*     */ 
/*  49 */   protected static boolean charEnabled = "true".equals(System.getProperty("tomcat.util.buf.StringCache.char.enabled", "false"));
/*     */   
/*     */ 
/*     */ 
/*  53 */   protected static int trainThreshold = Integer.parseInt(System.getProperty("tomcat.util.buf.StringCache.trainThreshold", "20000"));
/*     */   
/*     */ 
/*     */ 
/*  57 */   protected static int cacheSize = Integer.parseInt(System.getProperty("tomcat.util.buf.StringCache.cacheSize", "200"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   protected static final int maxStringSize = Integer.parseInt(System.getProperty("tomcat.util.buf.StringCache.maxStringSize", "128"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   protected static final HashMap<ByteEntry, int[]> bcStats = new HashMap(cacheSize);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */   protected static int bcCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   protected static ByteEntry[] bcCache = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   protected static final HashMap<CharEntry, int[]> ccStats = new HashMap(cacheSize);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */   protected static int ccCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */   protected static CharEntry[] ccCache = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */   protected static int accessCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */   protected static int hitCount = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCacheSize()
/*     */   {
/* 123 */     return cacheSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheSize(int cacheSize)
/*     */   {
/* 131 */     cacheSize = cacheSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getByteEnabled()
/*     */   {
/* 139 */     return byteEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setByteEnabled(boolean byteEnabled)
/*     */   {
/* 147 */     byteEnabled = byteEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCharEnabled()
/*     */   {
/* 155 */     return charEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharEnabled(boolean charEnabled)
/*     */   {
/* 163 */     charEnabled = charEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTrainThreshold()
/*     */   {
/* 171 */     return trainThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTrainThreshold(int trainThreshold)
/*     */   {
/* 179 */     trainThreshold = trainThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getAccessCount()
/*     */   {
/* 187 */     return accessCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHitCount()
/*     */   {
/* 195 */     return hitCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 203 */     hitCount = 0;
/* 204 */     accessCount = 0;
/* 205 */     synchronized (bcStats) {
/* 206 */       bcCache = null;
/* 207 */       bcCount = 0;
/*     */     }
/* 209 */     synchronized (ccStats) {
/* 210 */       ccCache = null;
/* 211 */       ccCount = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(ByteChunk bc)
/*     */   {
/* 220 */     if (bcCache == null) {
/* 221 */       String value = bc.toStringInternal();
/* 222 */       if ((byteEnabled) && (value.length() < maxStringSize))
/*     */       {
/* 224 */         synchronized (bcStats)
/*     */         {
/*     */ 
/*     */ 
/* 228 */           if (bcCache != null) {
/* 229 */             return value;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 234 */           if (bcCount > trainThreshold) {
/* 235 */             long t1 = System.currentTimeMillis();
/*     */             
/* 237 */             TreeMap<Integer, ArrayList<ByteEntry>> tempMap = new TreeMap();
/*     */             
/* 239 */             for (Map.Entry<ByteEntry, int[]> item : bcStats.entrySet()) {
/* 240 */               ByteEntry entry = (ByteEntry)item.getKey();
/* 241 */               int[] countA = (int[])item.getValue();
/* 242 */               Integer count = Integer.valueOf(countA[0]);
/*     */               
/* 244 */               ArrayList<ByteEntry> list = (ArrayList)tempMap.get(count);
/* 245 */               if (list == null)
/*     */               {
/* 247 */                 list = new ArrayList();
/* 248 */                 tempMap.put(count, list);
/*     */               }
/* 250 */               list.add(entry);
/*     */             }
/*     */             
/* 253 */             int size = bcStats.size();
/* 254 */             if (size > cacheSize) {
/* 255 */               size = cacheSize;
/*     */             }
/* 257 */             ByteEntry[] tempbcCache = new ByteEntry[size];
/*     */             
/*     */ 
/* 260 */             ByteChunk tempChunk = new ByteChunk();
/* 261 */             int n = 0;
/* 262 */             while (n < size) {
/* 263 */               Object key = tempMap.lastKey();
/* 264 */               ArrayList<ByteEntry> list = (ArrayList)tempMap.get(key);
/* 265 */               for (int i = 0; (i < list.size()) && (n < size); i++) {
/* 266 */                 ByteEntry entry = (ByteEntry)list.get(i);
/* 267 */                 tempChunk.setBytes(entry.name, 0, 
/* 268 */                   entry.name.length);
/* 269 */                 int insertPos = findClosest(tempChunk, tempbcCache, n);
/*     */                 
/* 271 */                 if (insertPos == n) {
/* 272 */                   tempbcCache[(n + 1)] = entry;
/*     */                 } else {
/* 274 */                   System.arraycopy(tempbcCache, insertPos + 1, tempbcCache, insertPos + 2, n - insertPos - 1);
/*     */                   
/*     */ 
/* 277 */                   tempbcCache[(insertPos + 1)] = entry;
/*     */                 }
/* 279 */                 n++;
/*     */               }
/* 281 */               tempMap.remove(key);
/*     */             }
/* 283 */             bcCount = 0;
/* 284 */             bcStats.clear();
/* 285 */             bcCache = tempbcCache;
/* 286 */             if (log.isDebugEnabled()) {
/* 287 */               long t2 = System.currentTimeMillis();
/* 288 */               log.debug("ByteCache generation time: " + (t2 - t1) + "ms");
/*     */             }
/*     */           }
/*     */           else {
/* 292 */             bcCount += 1;
/*     */             
/* 294 */             ByteEntry entry = new ByteEntry(null);
/* 295 */             entry.value = value;
/* 296 */             int[] count = (int[])bcStats.get(entry);
/* 297 */             if (count == null) {
/* 298 */               int end = bc.getEnd();
/* 299 */               int start = bc.getStart();
/*     */               
/* 301 */               entry.name = new byte[bc.getLength()];
/* 302 */               System.arraycopy(bc.getBuffer(), start, entry.name, 0, end - start);
/*     */               
/*     */ 
/* 305 */               entry.charset = bc.getCharset();
/*     */               
/* 307 */               count = new int[1];
/* 308 */               count[0] = 1;
/*     */               
/* 310 */               bcStats.put(entry, count);
/*     */             } else {
/* 312 */               count[0] += 1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 317 */       return value;
/*     */     }
/* 319 */     accessCount += 1;
/*     */     
/* 321 */     String result = find(bc);
/* 322 */     if (result == null) {
/* 323 */       return bc.toStringInternal();
/*     */     }
/*     */     
/* 326 */     hitCount += 1;
/* 327 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(CharChunk cc)
/*     */   {
/* 337 */     if (ccCache == null) {
/* 338 */       String value = cc.toStringInternal();
/* 339 */       if ((charEnabled) && (value.length() < maxStringSize))
/*     */       {
/* 341 */         synchronized (ccStats)
/*     */         {
/*     */ 
/*     */ 
/* 345 */           if (ccCache != null) {
/* 346 */             return value;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 351 */           if (ccCount > trainThreshold) {
/* 352 */             long t1 = System.currentTimeMillis();
/*     */             
/* 354 */             TreeMap<Integer, ArrayList<CharEntry>> tempMap = new TreeMap();
/*     */             
/* 356 */             for (Map.Entry<CharEntry, int[]> item : ccStats.entrySet()) {
/* 357 */               CharEntry entry = (CharEntry)item.getKey();
/* 358 */               int[] countA = (int[])item.getValue();
/* 359 */               Integer count = Integer.valueOf(countA[0]);
/*     */               
/* 361 */               ArrayList<CharEntry> list = (ArrayList)tempMap.get(count);
/* 362 */               if (list == null)
/*     */               {
/* 364 */                 list = new ArrayList();
/* 365 */                 tempMap.put(count, list);
/*     */               }
/* 367 */               list.add(entry);
/*     */             }
/*     */             
/* 370 */             int size = ccStats.size();
/* 371 */             if (size > cacheSize) {
/* 372 */               size = cacheSize;
/*     */             }
/* 374 */             CharEntry[] tempccCache = new CharEntry[size];
/*     */             
/*     */ 
/* 377 */             CharChunk tempChunk = new CharChunk();
/* 378 */             int n = 0;
/* 379 */             while (n < size) {
/* 380 */               Object key = tempMap.lastKey();
/* 381 */               ArrayList<CharEntry> list = (ArrayList)tempMap.get(key);
/* 382 */               for (int i = 0; (i < list.size()) && (n < size); i++) {
/* 383 */                 CharEntry entry = (CharEntry)list.get(i);
/* 384 */                 tempChunk.setChars(entry.name, 0, 
/* 385 */                   entry.name.length);
/* 386 */                 int insertPos = findClosest(tempChunk, tempccCache, n);
/*     */                 
/* 388 */                 if (insertPos == n) {
/* 389 */                   tempccCache[(n + 1)] = entry;
/*     */                 } else {
/* 391 */                   System.arraycopy(tempccCache, insertPos + 1, tempccCache, insertPos + 2, n - insertPos - 1);
/*     */                   
/*     */ 
/* 394 */                   tempccCache[(insertPos + 1)] = entry;
/*     */                 }
/* 396 */                 n++;
/*     */               }
/* 398 */               tempMap.remove(key);
/*     */             }
/* 400 */             ccCount = 0;
/* 401 */             ccStats.clear();
/* 402 */             ccCache = tempccCache;
/* 403 */             if (log.isDebugEnabled()) {
/* 404 */               long t2 = System.currentTimeMillis();
/* 405 */               log.debug("CharCache generation time: " + (t2 - t1) + "ms");
/*     */             }
/*     */           }
/*     */           else {
/* 409 */             ccCount += 1;
/*     */             
/* 411 */             CharEntry entry = new CharEntry(null);
/* 412 */             entry.value = value;
/* 413 */             int[] count = (int[])ccStats.get(entry);
/* 414 */             if (count == null) {
/* 415 */               int end = cc.getEnd();
/* 416 */               int start = cc.getStart();
/*     */               
/* 418 */               entry.name = new char[cc.getLength()];
/* 419 */               System.arraycopy(cc.getBuffer(), start, entry.name, 0, end - start);
/*     */               
/*     */ 
/* 422 */               count = new int[1];
/* 423 */               count[0] = 1;
/*     */               
/* 425 */               ccStats.put(entry, count);
/*     */             } else {
/* 427 */               count[0] += 1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 432 */       return value;
/*     */     }
/* 434 */     accessCount += 1;
/*     */     
/* 436 */     String result = find(cc);
/* 437 */     if (result == null) {
/* 438 */       return cc.toStringInternal();
/*     */     }
/*     */     
/* 441 */     hitCount += 1;
/* 442 */     return result;
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
/*     */   protected static final int compare(ByteChunk name, byte[] compareTo)
/*     */   {
/* 458 */     int result = 0;
/*     */     
/* 460 */     byte[] b = name.getBuffer();
/* 461 */     int start = name.getStart();
/* 462 */     int end = name.getEnd();
/* 463 */     int len = compareTo.length;
/*     */     
/* 465 */     if (end - start < len) {
/* 466 */       len = end - start;
/*     */     }
/* 468 */     for (int i = 0; (i < len) && (result == 0); i++) {
/* 469 */       if (b[(i + start)] > compareTo[i]) {
/* 470 */         result = 1;
/* 471 */       } else if (b[(i + start)] < compareTo[i]) {
/* 472 */         result = -1;
/*     */       }
/*     */     }
/* 475 */     if (result == 0) {
/* 476 */       if (compareTo.length > end - start) {
/* 477 */         result = -1;
/* 478 */       } else if (compareTo.length < end - start) {
/* 479 */         result = 1;
/*     */       }
/*     */     }
/* 482 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String find(ByteChunk name)
/*     */   {
/* 493 */     int pos = findClosest(name, bcCache, bcCache.length);
/* 494 */     if ((pos < 0) || (compare(name, bcCache[pos].name) != 0) || 
/* 495 */       (!name.getCharset().equals(bcCache[pos].charset))) {
/* 496 */       return null;
/*     */     }
/* 498 */     return bcCache[pos].value;
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
/*     */   protected static final int findClosest(ByteChunk name, ByteEntry[] array, int len)
/*     */   {
/* 515 */     int a = 0;
/* 516 */     int b = len - 1;
/*     */     
/*     */ 
/* 519 */     if (b == -1) {
/* 520 */       return -1;
/*     */     }
/*     */     
/* 523 */     if (compare(name, array[0].name) < 0) {
/* 524 */       return -1;
/*     */     }
/* 526 */     if (b == 0) {
/* 527 */       return 0;
/*     */     }
/*     */     
/* 530 */     int i = 0;
/*     */     for (;;) {
/* 532 */       i = b + a >>> 1;
/* 533 */       int result = compare(name, array[i].name);
/* 534 */       if (result == 1) {
/* 535 */         a = i;
/* 536 */       } else { if (result == 0) {
/* 537 */           return i;
/*     */         }
/* 539 */         b = i;
/*     */       }
/* 541 */       if (b - a == 1) {
/* 542 */         int result2 = compare(name, array[b].name);
/* 543 */         if (result2 < 0) {
/* 544 */           return a;
/*     */         }
/* 546 */         return b;
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
/*     */   protected static final int compare(CharChunk name, char[] compareTo)
/*     */   {
/* 561 */     int result = 0;
/*     */     
/* 563 */     char[] c = name.getBuffer();
/* 564 */     int start = name.getStart();
/* 565 */     int end = name.getEnd();
/* 566 */     int len = compareTo.length;
/*     */     
/* 568 */     if (end - start < len) {
/* 569 */       len = end - start;
/*     */     }
/* 571 */     for (int i = 0; (i < len) && (result == 0); i++) {
/* 572 */       if (c[(i + start)] > compareTo[i]) {
/* 573 */         result = 1;
/* 574 */       } else if (c[(i + start)] < compareTo[i]) {
/* 575 */         result = -1;
/*     */       }
/*     */     }
/* 578 */     if (result == 0) {
/* 579 */       if (compareTo.length > end - start) {
/* 580 */         result = -1;
/* 581 */       } else if (compareTo.length < end - start) {
/* 582 */         result = 1;
/*     */       }
/*     */     }
/* 585 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String find(CharChunk name)
/*     */   {
/* 596 */     int pos = findClosest(name, ccCache, ccCache.length);
/* 597 */     if ((pos < 0) || (compare(name, ccCache[pos].name) != 0)) {
/* 598 */       return null;
/*     */     }
/* 600 */     return ccCache[pos].value;
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
/*     */   protected static final int findClosest(CharChunk name, CharEntry[] array, int len)
/*     */   {
/* 617 */     int a = 0;
/* 618 */     int b = len - 1;
/*     */     
/*     */ 
/* 621 */     if (b == -1) {
/* 622 */       return -1;
/*     */     }
/*     */     
/* 625 */     if (compare(name, array[0].name) < 0) {
/* 626 */       return -1;
/*     */     }
/* 628 */     if (b == 0) {
/* 629 */       return 0;
/*     */     }
/*     */     
/* 632 */     int i = 0;
/*     */     for (;;) {
/* 634 */       i = b + a >>> 1;
/* 635 */       int result = compare(name, array[i].name);
/* 636 */       if (result == 1) {
/* 637 */         a = i;
/* 638 */       } else { if (result == 0) {
/* 639 */           return i;
/*     */         }
/* 641 */         b = i;
/*     */       }
/* 643 */       if (b - a == 1) {
/* 644 */         int result2 = compare(name, array[b].name);
/* 645 */         if (result2 < 0) {
/* 646 */           return a;
/*     */         }
/* 648 */         return b;
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
/*     */   private static class ByteEntry
/*     */   {
/* 661 */     private byte[] name = null;
/* 662 */     private Charset charset = null;
/* 663 */     private String value = null;
/*     */     
/*     */     public String toString()
/*     */     {
/* 667 */       return this.value;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 671 */       return this.value.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 675 */       if ((obj instanceof ByteEntry)) {
/* 676 */         return this.value.equals(((ByteEntry)obj).value);
/*     */       }
/* 678 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class CharEntry
/*     */   {
/* 689 */     private char[] name = null;
/* 690 */     private String value = null;
/*     */     
/*     */     public String toString()
/*     */     {
/* 694 */       return this.value;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 698 */       return this.value.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 702 */       if ((obj instanceof CharEntry)) {
/* 703 */         return this.value.equals(((CharEntry)obj).value);
/*     */       }
/* 705 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\StringCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */