/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionInfo;
/*     */ import org.springframework.core.Ordered;
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
/*     */ public class DefaultIntroductionAdvisor
/*     */   implements IntroductionAdvisor, ClassFilter, Ordered, Serializable
/*     */ {
/*     */   private final Advice advice;
/*  46 */   private final Set<Class<?>> interfaces = new LinkedHashSet();
/*     */   
/*  48 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultIntroductionAdvisor(Advice advice)
/*     */   {
/*  58 */     this(advice, (advice instanceof IntroductionInfo) ? (IntroductionInfo)advice : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultIntroductionAdvisor(Advice advice, IntroductionInfo introductionInfo)
/*     */   {
/*  68 */     Assert.notNull(advice, "Advice must not be null");
/*  69 */     this.advice = advice;
/*  70 */     if (introductionInfo != null) {
/*  71 */       Class<?>[] introducedInterfaces = introductionInfo.getInterfaces();
/*  72 */       if (introducedInterfaces.length == 0) {
/*  73 */         throw new IllegalArgumentException("IntroductionAdviceSupport implements no interfaces");
/*     */       }
/*  75 */       for (Class<?> ifc : introducedInterfaces) {
/*  76 */         addInterface(ifc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultIntroductionAdvisor(DynamicIntroductionAdvice advice, Class<?> intf)
/*     */   {
/*  87 */     Assert.notNull(advice, "Advice must not be null");
/*  88 */     this.advice = advice;
/*  89 */     addInterface(intf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInterface(Class<?> intf)
/*     */   {
/*  98 */     Assert.notNull(intf, "Interface must not be null");
/*  99 */     if (!intf.isInterface()) {
/* 100 */       throw new IllegalArgumentException("Specified class [" + intf.getName() + "] must be an interface");
/*     */     }
/* 102 */     this.interfaces.add(intf);
/*     */   }
/*     */   
/*     */   public Class<?>[] getInterfaces()
/*     */   {
/* 107 */     return (Class[])this.interfaces.toArray(new Class[this.interfaces.size()]);
/*     */   }
/*     */   
/*     */   public void validateInterfaces() throws IllegalArgumentException
/*     */   {
/* 112 */     for (Class<?> ifc : this.interfaces) {
/* 113 */       if (((this.advice instanceof DynamicIntroductionAdvice)) && 
/* 114 */         (!((DynamicIntroductionAdvice)this.advice).implementsInterface(ifc)))
/*     */       {
/* 116 */         throw new IllegalArgumentException("DynamicIntroductionAdvice [" + this.advice + "] does not implement interface [" + ifc.getName() + "] specified for introduction");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setOrder(int order)
/*     */   {
/* 123 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 128 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */   public Advice getAdvice()
/*     */   {
/* 134 */     return this.advice;
/*     */   }
/*     */   
/*     */   public boolean isPerInstance()
/*     */   {
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   public ClassFilter getClassFilter()
/*     */   {
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public boolean matches(Class<?> clazz)
/*     */   {
/* 149 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 155 */     if (this == other) {
/* 156 */       return true;
/*     */     }
/* 158 */     if (!(other instanceof DefaultIntroductionAdvisor)) {
/* 159 */       return false;
/*     */     }
/* 161 */     DefaultIntroductionAdvisor otherAdvisor = (DefaultIntroductionAdvisor)other;
/* 162 */     return (this.advice.equals(otherAdvisor.advice)) && (this.interfaces.equals(otherAdvisor.interfaces));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 167 */     return this.advice.hashCode() * 13 + this.interfaces.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 172 */     return 
/* 173 */       ClassUtils.getShortName(getClass()) + ": advice [" + this.advice + "]; interfaces " + ClassUtils.classNamesToString(this.interfaces);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\support\DefaultIntroductionAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */