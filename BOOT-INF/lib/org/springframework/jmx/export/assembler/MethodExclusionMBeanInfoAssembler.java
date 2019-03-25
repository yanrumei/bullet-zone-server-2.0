/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class MethodExclusionMBeanInfoAssembler
/*     */   extends AbstractConfigurableMBeanInfoAssembler
/*     */ {
/*     */   private Set<String> ignoredMethods;
/*     */   private Map<String, Set<String>> ignoredMethodMappings;
/*     */   
/*     */   public void setIgnoredMethods(String... ignoredMethodNames)
/*     */   {
/*  72 */     this.ignoredMethods = new HashSet(Arrays.asList(ignoredMethodNames));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoredMethodMappings(Properties mappings)
/*     */   {
/*  83 */     this.ignoredMethodMappings = new HashMap();
/*  84 */     for (Enumeration<?> en = mappings.keys(); en.hasMoreElements();) {
/*  85 */       String beanKey = (String)en.nextElement();
/*  86 */       String[] methodNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/*  87 */       this.ignoredMethodMappings.put(beanKey, new HashSet(Arrays.asList(methodNames)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean includeReadAttribute(Method method, String beanKey)
/*     */   {
/*  94 */     return isNotIgnored(method, beanKey);
/*     */   }
/*     */   
/*     */   protected boolean includeWriteAttribute(Method method, String beanKey)
/*     */   {
/*  99 */     return isNotIgnored(method, beanKey);
/*     */   }
/*     */   
/*     */   protected boolean includeOperation(Method method, String beanKey)
/*     */   {
/* 104 */     return isNotIgnored(method, beanKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isNotIgnored(Method method, String beanKey)
/*     */   {
/* 115 */     if (this.ignoredMethodMappings != null) {
/* 116 */       Set<String> methodNames = (Set)this.ignoredMethodMappings.get(beanKey);
/* 117 */       if (methodNames != null) {
/* 118 */         return !methodNames.contains(method.getName());
/*     */       }
/*     */     }
/* 121 */     if (this.ignoredMethods != null) {
/* 122 */       return !this.ignoredMethods.contains(method.getName());
/*     */     }
/* 124 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\assembler\MethodExclusionMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */