/*    */ package org.springframework.cglib.core.internal;
/*    */ 
/*    */ import java.util.concurrent.FutureTask;
/*    */ 
/*    */ public class LoadingCache<K, KK, V>
/*    */ {
/*    */   protected final java.util.concurrent.ConcurrentMap<KK, Object> map;
/*    */   protected final Function<K, V> loader;
/*    */   protected final Function<K, KK> keyMapper;
/* 10 */   public static final Function IDENTITY = new Function() {
/*    */     public Object apply(Object key) {
/* 12 */       return key;
/*    */     }
/*    */   };
/*    */   
/*    */   public LoadingCache(Function<K, KK> keyMapper, Function<K, V> loader) {
/* 17 */     this.keyMapper = keyMapper;
/* 18 */     this.loader = loader;
/* 19 */     this.map = new java.util.concurrent.ConcurrentHashMap();
/*    */   }
/*    */   
/*    */   public static <K> Function<K, K> identity()
/*    */   {
/* 24 */     return IDENTITY;
/*    */   }
/*    */   
/*    */   public V get(K key) {
/* 28 */     KK cacheKey = this.keyMapper.apply(key);
/* 29 */     Object v = this.map.get(cacheKey);
/* 30 */     if ((v != null) && (!(v instanceof FutureTask))) {
/* 31 */       return (V)v;
/*    */     }
/*    */     
/* 34 */     return (V)createEntry(key, cacheKey, v);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected V createEntry(final K key, KK cacheKey, Object v)
/*    */   {
/* 47 */     boolean creator = false;
/* 48 */     FutureTask<V> task; FutureTask<V> task; if (v != null)
/*    */     {
/* 50 */       task = (FutureTask)v;
/*    */     } else {
/* 52 */       task = new FutureTask(new java.util.concurrent.Callable() {
/*    */         public V call() throws Exception {
/* 54 */           return (V)LoadingCache.this.loader.apply(key);
/*    */         }
/* 56 */       });
/* 57 */       Object prevTask = this.map.putIfAbsent(cacheKey, task);
/* 58 */       if (prevTask == null)
/*    */       {
/* 60 */         creator = true;
/* 61 */         task.run();
/* 62 */       } else if ((prevTask instanceof FutureTask)) {
/* 63 */         task = (FutureTask)prevTask;
/*    */       } else {
/* 65 */         return (V)prevTask;
/*    */       }
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 71 */       result = task.get();
/*    */     } catch (InterruptedException e) { V result;
/* 73 */       throw new IllegalStateException("Interrupted while loading cache item", e);
/*    */     } catch (java.util.concurrent.ExecutionException e) {
/* 75 */       Throwable cause = e.getCause();
/* 76 */       if ((cause instanceof RuntimeException)) {
/* 77 */         throw ((RuntimeException)cause);
/*    */       }
/* 79 */       throw new IllegalStateException("Unable to load cache item", cause); }
/*    */     V result;
/* 81 */     if (creator) {
/* 82 */       this.map.put(cacheKey, result);
/*    */     }
/* 84 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\internal\LoadingCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */