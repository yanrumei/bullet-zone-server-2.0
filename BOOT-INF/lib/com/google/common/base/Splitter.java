/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
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
/*     */ @GwtCompatible(emulated=true)
/*     */ public final class Splitter
/*     */ {
/*     */   private final CharMatcher trimmer;
/*     */   private final boolean omitEmptyStrings;
/*     */   private final Strategy strategy;
/*     */   private final int limit;
/*     */   
/*     */   private Splitter(Strategy strategy)
/*     */   {
/* 100 */     this(strategy, false, CharMatcher.none(), Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
/* 104 */     this.strategy = strategy;
/* 105 */     this.omitEmptyStrings = omitEmptyStrings;
/* 106 */     this.trimmer = trimmer;
/* 107 */     this.limit = limit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Splitter on(char separator)
/*     */   {
/* 119 */     return on(CharMatcher.is(separator));
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
/*     */   public static Splitter on(CharMatcher separatorMatcher)
/*     */   {
/* 133 */     Preconditions.checkNotNull(separatorMatcher);
/*     */     
/* 135 */     new Splitter(new Strategy()
/*     */     {
/*     */       public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */       {
/* 139 */         new Splitter.SplittingIterator(splitter, toSplit)
/*     */         {
/*     */           int separatorStart(int start) {
/* 142 */             return Splitter.1.this.val$separatorMatcher.indexIn(this.toSplit, start);
/*     */           }
/*     */           
/*     */           int separatorEnd(int separatorPosition)
/*     */           {
/* 147 */             return separatorPosition + 1;
/*     */           }
/*     */         };
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Splitter on(String separator)
/*     */   {
/* 163 */     Preconditions.checkArgument(separator.length() != 0, "The separator may not be the empty string.");
/* 164 */     if (separator.length() == 1) {
/* 165 */       return on(separator.charAt(0));
/*     */     }
/* 167 */     new Splitter(new Strategy()
/*     */     {
/*     */       public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */       {
/* 171 */         new Splitter.SplittingIterator(splitter, toSplit)
/*     */         {
/*     */           public int separatorStart(int start) {
/* 174 */             int separatorLength = Splitter.2.this.val$separator.length();
/*     */             
/*     */ 
/* 177 */             int p = start; label80: for (int last = this.toSplit.length() - separatorLength; p <= last; p++) {
/* 178 */               for (int i = 0; i < separatorLength; i++) {
/* 179 */                 if (this.toSplit.charAt(i + p) != Splitter.2.this.val$separator.charAt(i)) {
/*     */                   break label80;
/*     */                 }
/*     */               }
/* 183 */               return p;
/*     */             }
/* 185 */             return -1;
/*     */           }
/*     */           
/*     */           public int separatorEnd(int separatorPosition)
/*     */           {
/* 190 */             return separatorPosition + Splitter.2.this.val$separator.length();
/*     */           }
/*     */         };
/*     */       }
/*     */     });
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
/*     */   @GwtIncompatible
/*     */   public static Splitter on(Pattern separatorPattern)
/*     */   {
/* 209 */     return on(new JdkPattern(separatorPattern));
/*     */   }
/*     */   
/*     */   private static Splitter on(CommonPattern separatorPattern) {
/* 213 */     Preconditions.checkArgument(
/* 214 */       !separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", separatorPattern);
/*     */     
/*     */ 
/*     */ 
/* 218 */     new Splitter(new Strategy()
/*     */     {
/*     */       public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */       {
/* 222 */         final CommonMatcher matcher = this.val$separatorPattern.matcher(toSplit);
/* 223 */         new Splitter.SplittingIterator(splitter, toSplit)
/*     */         {
/*     */           public int separatorStart(int start) {
/* 226 */             return matcher.find(start) ? matcher.start() : -1;
/*     */           }
/*     */           
/*     */           public int separatorEnd(int separatorPosition)
/*     */           {
/* 231 */             return matcher.end();
/*     */           }
/*     */         };
/*     */       }
/*     */     });
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
/*     */   @GwtIncompatible
/*     */   public static Splitter onPattern(String separatorPattern)
/*     */   {
/* 252 */     return on(Platform.compilePattern(separatorPattern));
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
/*     */   public static Splitter fixedLength(int length)
/*     */   {
/* 273 */     Preconditions.checkArgument(length > 0, "The length may not be less than 1");
/*     */     
/* 275 */     new Splitter(new Strategy()
/*     */     {
/*     */       public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit)
/*     */       {
/* 279 */         new Splitter.SplittingIterator(splitter, toSplit)
/*     */         {
/*     */           public int separatorStart(int start) {
/* 282 */             int nextChunkStart = start + Splitter.4.this.val$length;
/* 283 */             return nextChunkStart < this.toSplit.length() ? nextChunkStart : -1;
/*     */           }
/*     */           
/*     */           public int separatorEnd(int separatorPosition)
/*     */           {
/* 288 */             return separatorPosition;
/*     */           }
/*     */         };
/*     */       }
/*     */     });
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
/*     */   public Splitter omitEmptyStrings()
/*     */   {
/* 312 */     return new Splitter(this.strategy, true, this.trimmer, this.limit);
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
/*     */   public Splitter limit(int limit)
/*     */   {
/* 333 */     Preconditions.checkArgument(limit > 0, "must be greater than zero: %s", limit);
/* 334 */     return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, limit);
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
/*     */   public Splitter trimResults()
/*     */   {
/* 347 */     return trimResults(CharMatcher.whitespace());
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
/*     */   public Splitter trimResults(CharMatcher trimmer)
/*     */   {
/* 363 */     Preconditions.checkNotNull(trimmer);
/* 364 */     return new Splitter(this.strategy, this.omitEmptyStrings, trimmer, this.limit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterable<String> split(final CharSequence sequence)
/*     */   {
/* 376 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 378 */     new Iterable()
/*     */     {
/*     */       public Iterator<String> iterator() {
/* 381 */         return Splitter.this.splittingIterator(sequence);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 386 */         return 
/* 387 */           ']';
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   private Iterator<String> splittingIterator(CharSequence sequence)
/*     */   {
/* 395 */     return this.strategy.iterator(this, sequence);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public List<String> splitToList(CharSequence sequence)
/*     */   {
/* 408 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 410 */     Iterator<String> iterator = splittingIterator(sequence);
/* 411 */     List<String> result = new ArrayList();
/*     */     
/* 413 */     while (iterator.hasNext()) {
/* 414 */       result.add(iterator.next());
/*     */     }
/*     */     
/* 417 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(String separator)
/*     */   {
/* 428 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(char separator)
/*     */   {
/* 439 */     return withKeyValueSeparator(on(separator));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter)
/*     */   {
/* 450 */     return new MapSplitter(this, keyValueSplitter, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static final class MapSplitter
/*     */   {
/*     */     private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
/*     */     
/*     */ 
/*     */     private final Splitter outerSplitter;
/*     */     
/*     */     private final Splitter entrySplitter;
/*     */     
/*     */ 
/*     */     private MapSplitter(Splitter outerSplitter, Splitter entrySplitter)
/*     */     {
/* 468 */       this.outerSplitter = outerSplitter;
/* 469 */       this.entrySplitter = ((Splitter)Preconditions.checkNotNull(entrySplitter));
/*     */     }
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
/*     */     public Map<String, String> split(CharSequence sequence)
/*     */     {
/* 484 */       Map<String, String> map = new LinkedHashMap();
/* 485 */       for (String entry : this.outerSplitter.split(sequence)) {
/* 486 */         Iterator<String> entryFields = this.entrySplitter.splittingIterator(entry);
/*     */         
/* 488 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 489 */         String key = (String)entryFields.next();
/* 490 */         Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", key);
/*     */         
/* 492 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/* 493 */         String value = (String)entryFields.next();
/* 494 */         map.put(key, value);
/*     */         
/* 496 */         Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", entry);
/*     */       }
/* 498 */       return Collections.unmodifiableMap(map);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static abstract interface Strategy
/*     */   {
/*     */     public abstract Iterator<String> iterator(Splitter paramSplitter, CharSequence paramCharSequence);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static abstract class SplittingIterator
/*     */     extends AbstractIterator<String>
/*     */   {
/*     */     final CharSequence toSplit;
/*     */     
/*     */     final CharMatcher trimmer;
/*     */     
/*     */     final boolean omitEmptyStrings;
/*     */     
/*     */ 
/*     */     abstract int separatorStart(int paramInt);
/*     */     
/*     */ 
/* 524 */     int offset = 0;
/*     */     
/*     */     abstract int separatorEnd(int paramInt);
/*     */     
/* 528 */     protected SplittingIterator(Splitter splitter, CharSequence toSplit) { this.trimmer = splitter.trimmer;
/* 529 */       this.omitEmptyStrings = splitter.omitEmptyStrings;
/* 530 */       this.limit = splitter.limit;
/* 531 */       this.toSplit = toSplit;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     int limit;
/*     */     
/*     */ 
/*     */     protected String computeNext()
/*     */     {
/* 541 */       int nextStart = this.offset;
/* 542 */       while (this.offset != -1) {
/* 543 */         int start = nextStart;
/*     */         
/*     */ 
/* 546 */         int separatorPosition = separatorStart(this.offset);
/* 547 */         int end; if (separatorPosition == -1) {
/* 548 */           int end = this.toSplit.length();
/* 549 */           this.offset = -1;
/*     */         } else {
/* 551 */           end = separatorPosition;
/* 552 */           this.offset = separatorEnd(separatorPosition);
/*     */         }
/* 554 */         if (this.offset == nextStart)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 561 */           this.offset += 1;
/* 562 */           if (this.offset > this.toSplit.length()) {
/* 563 */             this.offset = -1;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 568 */           while ((start < end) && (this.trimmer.matches(this.toSplit.charAt(start)))) {
/* 569 */             start++;
/*     */           }
/* 571 */           while ((end > start) && (this.trimmer.matches(this.toSplit.charAt(end - 1)))) {
/* 572 */             end--;
/*     */           }
/*     */           
/* 575 */           if ((this.omitEmptyStrings) && (start == end))
/*     */           {
/* 577 */             nextStart = this.offset;
/*     */           }
/*     */           else
/*     */           {
/* 581 */             if (this.limit == 1)
/*     */             {
/*     */ 
/*     */ 
/* 585 */               end = this.toSplit.length();
/* 586 */               this.offset = -1;
/*     */               
/* 588 */               while ((end > start) && (this.trimmer.matches(this.toSplit.charAt(end - 1)))) {
/* 589 */                 end--;
/*     */               }
/*     */             }
/* 592 */             this.limit -= 1;
/*     */             
/*     */ 
/* 595 */             return this.toSplit.subSequence(start, end).toString();
/*     */           } } }
/* 597 */       return (String)endOfData();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Splitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */