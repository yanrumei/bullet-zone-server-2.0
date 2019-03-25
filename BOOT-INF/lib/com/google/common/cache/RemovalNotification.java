/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.AbstractMap.SimpleImmutableEntry;
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ @GwtCompatible
/*    */ public final class RemovalNotification<K, V>
/*    */   extends AbstractMap.SimpleImmutableEntry<K, V>
/*    */ {
/*    */   private final RemovalCause cause;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <K, V> RemovalNotification<K, V> create(@Nullable K key, @Nullable V value, RemovalCause cause)
/*    */   {
/* 47 */     return new RemovalNotification(key, value, cause);
/*    */   }
/*    */   
/*    */   private RemovalNotification(@Nullable K key, @Nullable V value, RemovalCause cause) {
/* 51 */     super(key, value);
/* 52 */     this.cause = ((RemovalCause)Preconditions.checkNotNull(cause));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public RemovalCause getCause()
/*    */   {
/* 59 */     return this.cause;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean wasEvicted()
/*    */   {
/* 67 */     return this.cause.wasEvicted();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\cache\RemovalNotification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */