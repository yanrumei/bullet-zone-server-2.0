/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.Registration.Dynamic;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class RegistrationBean
/*     */   implements ServletContextInitializer, Ordered
/*     */ {
/*     */   private String name;
/*  42 */   private int order = Integer.MAX_VALUE;
/*     */   
/*  44 */   private boolean asyncSupported = true;
/*     */   
/*  46 */   private boolean enabled = true;
/*     */   
/*  48 */   private Map<String, String> initParameters = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/*  55 */     Assert.hasLength(name, "Name must not be empty");
/*  56 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAsyncSupported(boolean asyncSupported)
/*     */   {
/*  65 */     this.asyncSupported = asyncSupported;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAsyncSupported()
/*     */   {
/*  73 */     return this.asyncSupported;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnabled(boolean enabled)
/*     */   {
/*  81 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/*  89 */     return this.enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInitParameters(Map<String, String> initParameters)
/*     */   {
/* 100 */     Assert.notNull(initParameters, "InitParameters must not be null");
/* 101 */     this.initParameters = new LinkedHashMap(initParameters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getInitParameters()
/*     */   {
/* 109 */     return this.initParameters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInitParameter(String name, String value)
/*     */   {
/* 118 */     Assert.notNull(name, "Name must not be null");
/* 119 */     this.initParameters.put(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String getOrDeduceName(Object value)
/*     */   {
/* 129 */     return this.name != null ? this.name : Conventions.getVariableName(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configure(Registration.Dynamic registration)
/*     */   {
/* 137 */     Assert.state(registration != null, "Registration is null. Was something already registered for name=[" + this.name + "]?");
/*     */     
/*     */ 
/* 140 */     registration.setAsyncSupported(this.asyncSupported);
/* 141 */     if (!this.initParameters.isEmpty()) {
/* 142 */       registration.setInitParameters(this.initParameters);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/* 151 */     this.order = order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 160 */     return this.order;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\RegistrationBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */