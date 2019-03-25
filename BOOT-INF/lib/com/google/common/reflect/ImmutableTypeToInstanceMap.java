/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.collect.ForwardingMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMap.Builder;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class ImmutableTypeToInstanceMap<B>
/*     */   extends ForwardingMap<TypeToken<? extends B>, B>
/*     */   implements TypeToInstanceMap<B>
/*     */ {
/*     */   private final ImmutableMap<TypeToken<? extends B>, B> delegate;
/*     */   
/*     */   public static <B> ImmutableTypeToInstanceMap<B> of()
/*     */   {
/*  36 */     return new ImmutableTypeToInstanceMap(ImmutableMap.of());
/*     */   }
/*     */   
/*     */   public static <B> Builder<B> builder()
/*     */   {
/*  41 */     return new Builder(null);
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
/*     */   @Beta
/*     */   public static final class Builder<B>
/*     */   {
/*  62 */     private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> put(Class<T> key, T value)
/*     */     {
/*  72 */       this.mapBuilder.put(TypeToken.of(key), value);
/*  73 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> put(TypeToken<T> key, T value)
/*     */     {
/*  82 */       this.mapBuilder.put(key.rejectTypeVariables(), value);
/*  83 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ImmutableTypeToInstanceMap<B> build()
/*     */     {
/*  92 */       return new ImmutableTypeToInstanceMap(this.mapBuilder.build(), null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ImmutableTypeToInstanceMap(ImmutableMap<TypeToken<? extends B>, B> delegate)
/*     */   {
/*  99 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   public <T extends B> T getInstance(TypeToken<T> type)
/*     */   {
/* 104 */     return (T)trustedGet(type.rejectTypeVariables());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(TypeToken<T> type, T value)
/*     */   {
/* 117 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public <T extends B> T getInstance(Class<T> type)
/*     */   {
/* 122 */     return (T)trustedGet(TypeToken.of(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(Class<T> type, T value)
/*     */   {
/* 135 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public B put(TypeToken<? extends B> key, B value)
/*     */   {
/* 148 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map)
/*     */   {
/* 160 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected Map<TypeToken<? extends B>, B> delegate()
/*     */   {
/* 165 */     return this.delegate;
/*     */   }
/*     */   
/*     */   private <T extends B> T trustedGet(TypeToken<T> type)
/*     */   {
/* 170 */     return (T)this.delegate.get(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\reflect\ImmutableTypeToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */