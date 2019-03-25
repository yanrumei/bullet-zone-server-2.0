/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NameTransformer
/*     */ {
/*  14 */   public static final NameTransformer NOP = new NopTransformer();
/*     */   
/*     */   protected static final class NopTransformer
/*     */     extends NameTransformer
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public String transform(String name)
/*     */     {
/*  24 */       return name;
/*     */     }
/*     */     
/*     */     public String reverse(String transformed)
/*     */     {
/*  29 */       return transformed;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NameTransformer simpleTransformer(String prefix, final String suffix)
/*     */   {
/*  41 */     boolean hasPrefix = (prefix != null) && (prefix.length() > 0);
/*  42 */     boolean hasSuffix = (suffix != null) && (suffix.length() > 0);
/*     */     
/*  44 */     if (hasPrefix) {
/*  45 */       if (hasSuffix) {
/*  46 */         new NameTransformer()
/*     */         {
/*  48 */           public String transform(String name) { return this.val$prefix + name + suffix; }
/*     */           
/*     */           public String reverse(String transformed) {
/*  51 */             if (transformed.startsWith(this.val$prefix)) {
/*  52 */               String str = transformed.substring(this.val$prefix.length());
/*  53 */               if (str.endsWith(suffix)) {
/*  54 */                 return str.substring(0, str.length() - suffix.length());
/*     */               }
/*     */             }
/*  57 */             return null;
/*     */           }
/*     */           
/*  60 */           public String toString() { return "[PreAndSuffixTransformer('" + this.val$prefix + "','" + suffix + "')]"; }
/*     */         };
/*     */       }
/*  63 */       new NameTransformer()
/*     */       {
/*  65 */         public String transform(String name) { return this.val$prefix + name; }
/*     */         
/*     */         public String reverse(String transformed) {
/*  68 */           if (transformed.startsWith(this.val$prefix)) {
/*  69 */             return transformed.substring(this.val$prefix.length());
/*     */           }
/*  71 */           return null;
/*     */         }
/*     */         
/*  74 */         public String toString() { return "[PrefixTransformer('" + this.val$prefix + "')]"; }
/*     */       };
/*     */     }
/*  77 */     if (hasSuffix) {
/*  78 */       new NameTransformer()
/*     */       {
/*  80 */         public String transform(String name) { return name + this.val$suffix; }
/*     */         
/*     */         public String reverse(String transformed) {
/*  83 */           if (transformed.endsWith(this.val$suffix)) {
/*  84 */             return transformed.substring(0, transformed.length() - this.val$suffix.length());
/*     */           }
/*  86 */           return null;
/*     */         }
/*     */         
/*  89 */         public String toString() { return "[SuffixTransformer('" + this.val$suffix + "')]"; }
/*     */       };
/*     */     }
/*  92 */     return NOP;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NameTransformer chainedTransformer(NameTransformer t1, NameTransformer t2)
/*     */   {
/* 101 */     return new Chained(t1, t2);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract String transform(String paramString);
/*     */   
/*     */ 
/*     */   public abstract String reverse(String paramString);
/*     */   
/*     */ 
/*     */   public static class Chained
/*     */     extends NameTransformer
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected final NameTransformer _t1;
/*     */     
/*     */     protected final NameTransformer _t2;
/*     */     
/*     */ 
/*     */     public Chained(NameTransformer t1, NameTransformer t2)
/*     */     {
/* 124 */       this._t1 = t1;
/* 125 */       this._t2 = t2;
/*     */     }
/*     */     
/*     */     public String transform(String name)
/*     */     {
/* 130 */       return this._t1.transform(this._t2.transform(name));
/*     */     }
/*     */     
/*     */     public String reverse(String transformed)
/*     */     {
/* 135 */       transformed = this._t1.reverse(transformed);
/* 136 */       if (transformed != null) {
/* 137 */         transformed = this._t2.reverse(transformed);
/*     */       }
/* 139 */       return transformed;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 143 */       return "[ChainedTransformer(" + this._t1 + ", " + this._t2 + ")]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\NameTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */