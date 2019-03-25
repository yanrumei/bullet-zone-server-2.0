/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.HandlerMethod.HandlerMethodParameter;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*     */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.util.NestedServletException;
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
/*     */ public class ServletInvocableHandlerMethod
/*     */   extends InvocableHandlerMethod
/*     */ {
/*  58 */   private static final Method CALLABLE_METHOD = ClassUtils.getMethod(Callable.class, "call", new Class[0]);
/*     */   
/*     */ 
/*     */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*     */   
/*     */ 
/*     */ 
/*     */   public ServletInvocableHandlerMethod(Object handler, Method method)
/*     */   {
/*  67 */     super(handler, method);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ServletInvocableHandlerMethod(HandlerMethod handlerMethod)
/*     */   {
/*  74 */     super(handlerMethod);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers)
/*     */   {
/*  83 */     this.returnValueHandlers = returnValueHandlers;
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
/*     */   public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer, Object... providedArgs)
/*     */     throws Exception
/*     */   {
/*  97 */     Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
/*  98 */     setResponseStatus(webRequest);
/*     */     
/* 100 */     if (returnValue == null) {
/* 101 */       if ((isRequestNotModified(webRequest)) || (getResponseStatus() != null) || (mavContainer.isRequestHandled())) {
/* 102 */         mavContainer.setRequestHandled(true);
/*     */       }
/*     */       
/*     */     }
/* 106 */     else if (StringUtils.hasText(getResponseStatusReason())) {
/* 107 */       mavContainer.setRequestHandled(true);
/* 108 */       return;
/*     */     }
/*     */     
/* 111 */     mavContainer.setRequestHandled(false);
/*     */     try {
/* 113 */       this.returnValueHandlers.handleReturnValue(returnValue, 
/* 114 */         getReturnValueType(returnValue), mavContainer, webRequest);
/*     */     }
/*     */     catch (Exception ex) {
/* 117 */       if (this.logger.isTraceEnabled()) {
/* 118 */         this.logger.trace(getReturnValueHandlingErrorMessage("Error handling return value", returnValue), ex);
/*     */       }
/* 120 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void setResponseStatus(ServletWebRequest webRequest)
/*     */     throws IOException
/*     */   {
/* 128 */     HttpStatus status = getResponseStatus();
/* 129 */     if (status == null) {
/* 130 */       return;
/*     */     }
/*     */     
/* 133 */     String reason = getResponseStatusReason();
/* 134 */     if (StringUtils.hasText(reason)) {
/* 135 */       webRequest.getResponse().sendError(status.value(), reason);
/*     */     }
/*     */     else {
/* 138 */       webRequest.getResponse().setStatus(status.value());
/*     */     }
/*     */     
/*     */ 
/* 142 */     webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, status);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isRequestNotModified(ServletWebRequest webRequest)
/*     */   {
/* 151 */     return webRequest.isNotModified();
/*     */   }
/*     */   
/*     */   private String getReturnValueHandlingErrorMessage(String message, Object returnValue) {
/* 155 */     StringBuilder sb = new StringBuilder(message);
/* 156 */     if (returnValue != null) {
/* 157 */       sb.append(" [type=").append(returnValue.getClass().getName()).append("]");
/*     */     }
/* 159 */     sb.append(" [value=").append(returnValue).append("]");
/* 160 */     return getDetailedErrorMessage(sb.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ServletInvocableHandlerMethod wrapConcurrentResult(Object result)
/*     */   {
/* 170 */     return new ConcurrentResultHandlerMethod(result, new ConcurrentResultMethodParameter(result));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private class ConcurrentResultHandlerMethod
/*     */     extends ServletInvocableHandlerMethod
/*     */   {
/*     */     private final MethodParameter returnType;
/*     */     
/*     */ 
/*     */ 
/*     */     public ConcurrentResultHandlerMethod(final Object result, ServletInvocableHandlerMethod.ConcurrentResultMethodParameter returnType)
/*     */     {
/* 185 */       super(
/*     */       {
/*     */         public Object call() throws Exception {
/* 188 */           if ((result instanceof Exception)) {
/* 189 */             throw ((Exception)result);
/*     */           }
/* 191 */           if ((result instanceof Throwable)) {
/* 192 */             throw new NestedServletException("Async processing failed", (Throwable)result);
/*     */           }
/* 194 */           return result;
/*     */         }
/* 196 */       });
/*     */       
/* 198 */       setHandlerMethodReturnValueHandlers(ServletInvocableHandlerMethod.this.returnValueHandlers);
/* 199 */       this.returnType = returnType;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Class<?> getBeanType()
/*     */     {
/* 207 */       return ServletInvocableHandlerMethod.this.getBeanType();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MethodParameter getReturnValueType(Object returnValue)
/*     */     {
/* 216 */       return this.returnType;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType)
/*     */     {
/* 224 */       return ServletInvocableHandlerMethod.this.getMethodAnnotation(annotationType);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType)
/*     */     {
/* 232 */       return ServletInvocableHandlerMethod.this.hasMethodAnnotation(annotationType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class ConcurrentResultMethodParameter
/*     */     extends HandlerMethod.HandlerMethodParameter
/*     */   {
/*     */     private final Object returnValue;
/*     */     
/*     */ 
/*     */     private final ResolvableType returnType;
/*     */     
/*     */ 
/*     */     public ConcurrentResultMethodParameter(Object returnValue)
/*     */     {
/* 249 */       super(-1);
/* 250 */       this.returnValue = returnValue;
/* 251 */       this.returnType = ResolvableType.forType(super.getGenericParameterType()).getGeneric(new int[] { 0 });
/*     */     }
/*     */     
/*     */     public ConcurrentResultMethodParameter(ConcurrentResultMethodParameter original) {
/* 255 */       super(original);
/* 256 */       this.returnValue = original.returnValue;
/* 257 */       this.returnType = original.returnType;
/*     */     }
/*     */     
/*     */     public Class<?> getParameterType()
/*     */     {
/* 262 */       if (this.returnValue != null) {
/* 263 */         return this.returnValue.getClass();
/*     */       }
/* 265 */       if (!ResolvableType.NONE.equals(this.returnType)) {
/* 266 */         return this.returnType.resolve(Object.class);
/*     */       }
/* 268 */       return super.getParameterType();
/*     */     }
/*     */     
/*     */     public Type getGenericParameterType()
/*     */     {
/* 273 */       return this.returnType.getType();
/*     */     }
/*     */     
/*     */     public ConcurrentResultMethodParameter clone()
/*     */     {
/* 278 */       return new ConcurrentResultMethodParameter(ServletInvocableHandlerMethod.this, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ServletInvocableHandlerMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */