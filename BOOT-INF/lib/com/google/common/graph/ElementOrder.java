/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.MoreObjects.ToStringHelper;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @Beta
/*     */ public final class ElementOrder<T>
/*     */ {
/*     */   private final Type type;
/*     */   @Nullable
/*     */   private final Comparator<T> comparator;
/*     */   
/*     */   public static enum Type
/*     */   {
/*  66 */     UNORDERED, 
/*  67 */     INSERTION, 
/*  68 */     SORTED;
/*     */     
/*     */     private Type() {} }
/*     */   
/*  72 */   private ElementOrder(Type type, @Nullable Comparator<T> comparator) { this.type = ((Type)Preconditions.checkNotNull(type));
/*  73 */     this.comparator = comparator;
/*  74 */     Preconditions.checkState((type == Type.SORTED ? 1 : 0) == (comparator != null ? 1 : 0));
/*     */   }
/*     */   
/*     */   public static <S> ElementOrder<S> unordered()
/*     */   {
/*  79 */     return new ElementOrder(Type.UNORDERED, null);
/*     */   }
/*     */   
/*     */   public static <S> ElementOrder<S> insertion()
/*     */   {
/*  84 */     return new ElementOrder(Type.INSERTION, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <S extends Comparable<? super S>> ElementOrder<S> natural()
/*     */   {
/*  91 */     return new ElementOrder(Type.SORTED, Ordering.natural());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <S> ElementOrder<S> sorted(Comparator<S> comparator)
/*     */   {
/*  99 */     return new ElementOrder(Type.SORTED, comparator);
/*     */   }
/*     */   
/*     */   public Type type()
/*     */   {
/* 104 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Comparator<T> comparator()
/*     */   {
/* 113 */     if (this.comparator != null) {
/* 114 */       return this.comparator;
/*     */     }
/* 116 */     throw new UnsupportedOperationException("This ordering does not define a comparator.");
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object obj)
/*     */   {
/* 121 */     if (obj == this) {
/* 122 */       return true;
/*     */     }
/* 124 */     if (!(obj instanceof ElementOrder)) {
/* 125 */       return false;
/*     */     }
/*     */     
/* 128 */     ElementOrder<?> other = (ElementOrder)obj;
/* 129 */     return (this.type == other.type) && (Objects.equal(this.comparator, other.comparator));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 134 */     return Objects.hashCode(new Object[] { this.type, this.comparator });
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 139 */     MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", this.type);
/* 140 */     if (this.comparator != null) {
/* 141 */       helper.add("comparator", this.comparator);
/*     */     }
/* 143 */     return helper.toString();
/*     */   }
/*     */   
/*     */   <K extends T, V> Map<K, V> createMap(int expectedSize)
/*     */   {
/* 148 */     switch (this.type) {
/*     */     case UNORDERED: 
/* 150 */       return Maps.newHashMapWithExpectedSize(expectedSize);
/*     */     case INSERTION: 
/* 152 */       return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
/*     */     case SORTED: 
/* 154 */       return Maps.newTreeMap(comparator());
/*     */     }
/* 156 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */ 
/*     */   <T1 extends T> ElementOrder<T1> cast()
/*     */   {
/* 162 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\ElementOrder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */