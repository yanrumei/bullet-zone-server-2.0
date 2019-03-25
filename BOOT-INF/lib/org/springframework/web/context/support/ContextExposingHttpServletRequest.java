/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletRequestWrapper;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.WebApplicationContext;
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
/*    */ public class ContextExposingHttpServletRequest
/*    */   extends HttpServletRequestWrapper
/*    */ {
/*    */   private final WebApplicationContext webApplicationContext;
/*    */   private final Set<String> exposedContextBeanNames;
/*    */   private Set<String> explicitAttributes;
/*    */   
/*    */   public ContextExposingHttpServletRequest(HttpServletRequest originalRequest, WebApplicationContext context)
/*    */   {
/* 50 */     this(originalRequest, context, null);
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
/*    */ 
/*    */   public ContextExposingHttpServletRequest(HttpServletRequest originalRequest, WebApplicationContext context, Set<String> exposedContextBeanNames)
/*    */   {
/* 64 */     super(originalRequest);
/* 65 */     Assert.notNull(context, "WebApplicationContext must not be null");
/* 66 */     this.webApplicationContext = context;
/* 67 */     this.exposedContextBeanNames = exposedContextBeanNames;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final WebApplicationContext getWebApplicationContext()
/*    */   {
/* 75 */     return this.webApplicationContext;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getAttribute(String name)
/*    */   {
/* 81 */     if (((this.explicitAttributes == null) || (!this.explicitAttributes.contains(name))) && ((this.exposedContextBeanNames == null) || 
/* 82 */       (this.exposedContextBeanNames.contains(name))) && 
/* 83 */       (this.webApplicationContext.containsBean(name))) {
/* 84 */       return this.webApplicationContext.getBean(name);
/*    */     }
/*    */     
/* 87 */     return super.getAttribute(name);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setAttribute(String name, Object value)
/*    */   {
/* 93 */     super.setAttribute(name, value);
/* 94 */     if (this.explicitAttributes == null) {
/* 95 */       this.explicitAttributes = new HashSet(8);
/*    */     }
/* 97 */     this.explicitAttributes.add(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\support\ContextExposingHttpServletRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */