/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.EventListener;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletContextAttributeListener;
/*     */ import javax.servlet.ServletContextListener;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequestAttributeListener;
/*     */ import javax.servlet.ServletRequestListener;
/*     */ import javax.servlet.http.HttpSessionAttributeListener;
/*     */ import javax.servlet.http.HttpSessionListener;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class ServletListenerRegistrationBean<T extends EventListener>
/*     */   extends RegistrationBean
/*     */ {
/*  65 */   private static final Log logger = LogFactory.getLog(ServletListenerRegistrationBean.class);
/*     */   private static final Set<Class<?>> SUPPORTED_TYPES;
/*     */   private T listener;
/*     */   
/*     */   static {
/*  70 */     Set<Class<?>> types = new HashSet();
/*  71 */     types.add(ServletContextAttributeListener.class);
/*  72 */     types.add(ServletRequestListener.class);
/*  73 */     types.add(ServletRequestAttributeListener.class);
/*  74 */     types.add(HttpSessionAttributeListener.class);
/*  75 */     types.add(HttpSessionListener.class);
/*  76 */     types.add(ServletContextListener.class);
/*  77 */     SUPPORTED_TYPES = Collections.unmodifiableSet(types);
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
/*     */   public ServletListenerRegistrationBean(T listener)
/*     */   {
/*  93 */     Assert.notNull(listener, "Listener must not be null");
/*  94 */     Assert.isTrue(isSupportedType(listener), "Listener is not of a supported type");
/*  95 */     this.listener = listener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setListener(T listener)
/*     */   {
/* 103 */     Assert.notNull(listener, "Listener must not be null");
/* 104 */     Assert.isTrue(isSupportedType(listener), "Listener is not of a supported type");
/* 105 */     this.listener = listener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setName(String name)
/*     */   {
/* 116 */     super.setName(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setAsyncSupported(boolean asyncSupported)
/*     */   {
/* 128 */     super.setAsyncSupported(asyncSupported);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isAsyncSupported()
/*     */   {
/* 139 */     return super.isAsyncSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setInitParameters(Map<String, String> initParameters)
/*     */   {
/* 151 */     super.setInitParameters(initParameters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Map<String, String> getInitParameters()
/*     */   {
/* 162 */     return super.getInitParameters();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void addInitParameter(String name, String value)
/*     */   {
/* 174 */     super.addInitParameter(name, value);
/*     */   }
/*     */   
/*     */   public void onStartup(ServletContext servletContext) throws ServletException
/*     */   {
/* 179 */     if (!isEnabled()) {
/* 180 */       logger.info("Listener " + this.listener + " was not registered (disabled)");
/* 181 */       return;
/*     */     }
/*     */     try {
/* 184 */       servletContext.addListener(this.listener);
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 187 */       throw new IllegalStateException("Failed to add listener '" + this.listener + "' to servlet context", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public T getListener()
/*     */   {
/* 194 */     return this.listener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isSupportedType(EventListener listener)
/*     */   {
/* 203 */     for (Class<?> type : SUPPORTED_TYPES) {
/* 204 */       if (ClassUtils.isAssignableValue(type, listener)) {
/* 205 */         return true;
/*     */       }
/*     */     }
/* 208 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<Class<?>> getSupportedTypes()
/*     */   {
/* 216 */     return SUPPORTED_TYPES;
/*     */   }
/*     */   
/*     */   public ServletListenerRegistrationBean() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ServletListenerRegistrationBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */