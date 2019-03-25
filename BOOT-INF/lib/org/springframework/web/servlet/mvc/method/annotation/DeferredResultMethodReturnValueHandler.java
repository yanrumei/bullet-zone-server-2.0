/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.function.BiFunction;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureCallback;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.async.DeferredResult;
/*     */ import org.springframework.web.context.request.async.WebAsyncManager;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*     */ public class DeferredResultMethodReturnValueHandler
/*     */   implements AsyncHandlerMethodReturnValueHandler
/*     */ {
/*     */   private final Map<Class<?>, DeferredResultAdapter> adapterMap;
/*     */   
/*     */   public DeferredResultMethodReturnValueHandler()
/*     */   {
/*  51 */     this.adapterMap = new HashMap(5);
/*  52 */     this.adapterMap.put(DeferredResult.class, new SimpleDeferredResultAdapter(null));
/*  53 */     this.adapterMap.put(ListenableFuture.class, new ListenableFutureAdapter(null));
/*  54 */     if (ClassUtils.isPresent("java.util.concurrent.CompletionStage", getClass().getClassLoader())) {
/*  55 */       this.adapterMap.put(CompletionStage.class, new CompletionStageAdapter(null));
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
/*     */   @Deprecated
/*     */   public Map<Class<?>, DeferredResultAdapter> getAdapterMap()
/*     */   {
/*  69 */     return this.adapterMap;
/*     */   }
/*     */   
/*     */   private DeferredResultAdapter getAdapterFor(Class<?> type) {
/*  73 */     for (Class<?> adapteeType : getAdapterMap().keySet()) {
/*  74 */       if (adapteeType.isAssignableFrom(type)) {
/*  75 */         return (DeferredResultAdapter)getAdapterMap().get(adapteeType);
/*     */       }
/*     */     }
/*  78 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean supportsReturnType(MethodParameter returnType)
/*     */   {
/*  84 */     return getAdapterFor(returnType.getParameterType()) != null;
/*     */   }
/*     */   
/*     */   public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType)
/*     */   {
/*  89 */     return (returnValue != null) && (getAdapterFor(returnValue.getClass()) != null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/*  96 */     if (returnValue == null) {
/*  97 */       mavContainer.setRequestHandled(true);
/*  98 */       return;
/*     */     }
/*     */     
/* 101 */     DeferredResultAdapter adapter = getAdapterFor(returnValue.getClass());
/* 102 */     if (adapter == null)
/*     */     {
/* 104 */       throw new IllegalStateException("Could not find DeferredResultAdapter for return value type: " + returnValue.getClass());
/*     */     }
/* 106 */     DeferredResult<?> result = adapter.adaptToDeferredResult(returnValue);
/* 107 */     WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(result, new Object[] { mavContainer });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class SimpleDeferredResultAdapter
/*     */     implements DeferredResultAdapter
/*     */   {
/*     */     public DeferredResult<?> adaptToDeferredResult(Object returnValue)
/*     */     {
/* 118 */       Assert.isInstanceOf(DeferredResult.class, returnValue, "DeferredResult expected");
/* 119 */       return (DeferredResult)returnValue;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ListenableFutureAdapter
/*     */     implements DeferredResultAdapter
/*     */   {
/*     */     public DeferredResult<?> adaptToDeferredResult(Object returnValue)
/*     */     {
/* 131 */       Assert.isInstanceOf(ListenableFuture.class, returnValue, "ListenableFuture expected");
/* 132 */       final DeferredResult<Object> result = new DeferredResult();
/* 133 */       ((ListenableFuture)returnValue).addCallback(new ListenableFutureCallback()
/*     */       {
/*     */         public void onSuccess(Object value) {
/* 136 */           result.setResult(value);
/*     */         }
/*     */         
/*     */         public void onFailure(Throwable ex) {
/* 140 */           result.setErrorResult(ex);
/*     */         }
/* 142 */       });
/* 143 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @UsesJava8
/*     */   private static class CompletionStageAdapter
/*     */     implements DeferredResultAdapter
/*     */   {
/*     */     public DeferredResult<?> adaptToDeferredResult(Object returnValue)
/*     */     {
/* 156 */       Assert.isInstanceOf(CompletionStage.class, returnValue, "CompletionStage expected");
/* 157 */       final DeferredResult<Object> result = new DeferredResult();
/*     */       
/* 159 */       CompletionStage<?> future = (CompletionStage)returnValue;
/* 160 */       future.handle(new BiFunction()
/*     */       {
/*     */         public Object apply(Object value, Throwable ex) {
/* 163 */           if (ex != null) {
/* 164 */             result.setErrorResult(ex);
/*     */           }
/*     */           else {
/* 167 */             result.setResult(value);
/*     */           }
/* 169 */           return null;
/*     */         }
/* 171 */       });
/* 172 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\DeferredResultMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */