/*    */ package org.springframework.web.context.request.async;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public abstract interface CallableProcessingInterceptor
/*    */ {
/* 53 */   public static final Object RESULT_NONE = new Object();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 60 */   public static final Object RESPONSE_HANDLED = new Object();
/*    */   
/*    */   public abstract <T> void beforeConcurrentHandling(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable)
/*    */     throws Exception;
/*    */   
/*    */   public abstract <T> void preProcess(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable)
/*    */     throws Exception;
/*    */   
/*    */   public abstract <T> void postProcess(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable, Object paramObject)
/*    */     throws Exception;
/*    */   
/*    */   public abstract <T> Object handleTimeout(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable)
/*    */     throws Exception;
/*    */   
/*    */   public abstract <T> void afterCompletion(NativeWebRequest paramNativeWebRequest, Callable<T> paramCallable)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\async\CallableProcessingInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */