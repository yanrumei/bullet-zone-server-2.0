/*     */ package org.springframework.web;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContainerInitializer;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.annotation.HandlesTypes;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
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
/*     */ @HandlesTypes({WebApplicationInitializer.class})
/*     */ public class SpringServletContainerInitializer
/*     */   implements ServletContainerInitializer
/*     */ {
/*     */   public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
/*     */     throws ServletException
/*     */   {
/* 143 */     List<WebApplicationInitializer> initializers = new LinkedList();
/*     */     
/* 145 */     if (webAppInitializerClasses != null) {
/* 146 */       for (Class<?> waiClass : webAppInitializerClasses)
/*     */       {
/*     */ 
/* 149 */         if ((!waiClass.isInterface()) && (!Modifier.isAbstract(waiClass.getModifiers())) && 
/* 150 */           (WebApplicationInitializer.class.isAssignableFrom(waiClass))) {
/*     */           try {
/* 152 */             initializers.add((WebApplicationInitializer)waiClass.newInstance());
/*     */           }
/*     */           catch (Throwable ex) {
/* 155 */             throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 161 */     if (initializers.isEmpty()) {
/* 162 */       servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
/* 163 */       return;
/*     */     }
/*     */     
/* 166 */     servletContext.log(initializers.size() + " Spring WebApplicationInitializers detected on classpath");
/* 167 */     AnnotationAwareOrderComparator.sort(initializers);
/* 168 */     for (WebApplicationInitializer initializer : initializers) {
/* 169 */       initializer.onStartup(servletContext);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\SpringServletContainerInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */