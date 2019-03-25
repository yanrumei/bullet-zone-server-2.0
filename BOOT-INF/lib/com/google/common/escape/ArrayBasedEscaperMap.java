/*    */ package com.google.common.escape;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public final class ArrayBasedEscaperMap
/*    */ {
/*    */   private final char[][] replacementArray;
/*    */   
/*    */   public static ArrayBasedEscaperMap create(Map<Character, String> replacements)
/*    */   {
/* 49 */     return new ArrayBasedEscaperMap(createReplacementArray(replacements));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private ArrayBasedEscaperMap(char[][] replacementArray)
/*    */   {
/* 57 */     this.replacementArray = replacementArray;
/*    */   }
/*    */   
/*    */   char[][] getReplacementArray()
/*    */   {
/* 62 */     return this.replacementArray;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @VisibleForTesting
/*    */   static char[][] createReplacementArray(Map<Character, String> map)
/*    */   {
/* 70 */     Preconditions.checkNotNull(map);
/* 71 */     if (map.isEmpty()) {
/* 72 */       return EMPTY_REPLACEMENT_ARRAY;
/*    */     }
/* 74 */     char max = ((Character)Collections.max(map.keySet())).charValue();
/* 75 */     char[][] replacements = new char[max + '\001'][];
/* 76 */     for (Iterator localIterator = map.keySet().iterator(); localIterator.hasNext();) { char c = ((Character)localIterator.next()).charValue();
/* 77 */       replacements[c] = ((String)map.get(Character.valueOf(c))).toCharArray();
/*    */     }
/* 79 */     return replacements;
/*    */   }
/*    */   
/*    */ 
/* 83 */   private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\escape\ArrayBasedEscaperMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */