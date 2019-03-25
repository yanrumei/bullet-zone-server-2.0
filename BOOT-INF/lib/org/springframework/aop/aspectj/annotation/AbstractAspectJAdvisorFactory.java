/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.aspectj.lang.annotation.After;
/*     */ import org.aspectj.lang.annotation.AfterReturning;
/*     */ import org.aspectj.lang.annotation.AfterThrowing;
/*     */ import org.aspectj.lang.annotation.Around;
/*     */ import org.aspectj.lang.annotation.Aspect;
/*     */ import org.aspectj.lang.annotation.Before;
/*     */ import org.aspectj.lang.annotation.Pointcut;
/*     */ import org.aspectj.lang.reflect.AjType;
/*     */ import org.aspectj.lang.reflect.AjTypeSystem;
/*     */ import org.aspectj.lang.reflect.PerClause;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
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
/*     */ public abstract class AbstractAspectJAdvisorFactory
/*     */   implements AspectJAdvisorFactory
/*     */ {
/*     */   private static final String AJC_MAGIC = "ajc$";
/*  64 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  66 */   protected final ParameterNameDiscoverer parameterNameDiscoverer = new AspectJAnnotationParameterNameDiscoverer(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAspect(Class<?> clazz)
/*     */   {
/*  77 */     return (hasAspectAnnotation(clazz)) && (!compiledByAjc(clazz));
/*     */   }
/*     */   
/*     */   private boolean hasAspectAnnotation(Class<?> clazz) {
/*  81 */     return AnnotationUtils.findAnnotation(clazz, Aspect.class) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean compiledByAjc(Class<?> clazz)
/*     */   {
/*  92 */     for (Field field : clazz.getDeclaredFields()) {
/*  93 */       if (field.getName().startsWith("ajc$")) {
/*  94 */         return true;
/*     */       }
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/*     */   public void validate(Class<?> aspectClass)
/*     */     throws AopConfigException
/*     */   {
/* 103 */     if ((aspectClass.getSuperclass().getAnnotation(Aspect.class) != null) && 
/* 104 */       (!Modifier.isAbstract(aspectClass.getSuperclass().getModifiers())))
/*     */     {
/* 106 */       throw new AopConfigException("[" + aspectClass.getName() + "] cannot extend concrete aspect [" + aspectClass.getSuperclass().getName() + "]");
/*     */     }
/*     */     
/* 109 */     AjType<?> ajType = AjTypeSystem.getAjType(aspectClass);
/* 110 */     if (!ajType.isAspect()) {
/* 111 */       throw new NotAnAtAspectException(aspectClass);
/*     */     }
/* 113 */     if (ajType.getPerClause().getKind() == PerClauseKind.PERCFLOW) {
/* 114 */       throw new AopConfigException(aspectClass.getName() + " uses percflow instantiation model: This is not supported in Spring AOP.");
/*     */     }
/*     */     
/* 117 */     if (ajType.getPerClause().getKind() == PerClauseKind.PERCFLOWBELOW) {
/* 118 */       throw new AopConfigException(aspectClass.getName() + " uses percflowbelow instantiation model: This is not supported in Spring AOP.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static AspectJAnnotation<?> findAspectJAnnotationOnMethod(Method method)
/*     */   {
/* 129 */     Class<?>[] classesToLookFor = { Before.class, Around.class, After.class, AfterReturning.class, AfterThrowing.class, Pointcut.class };
/*     */     
/* 131 */     for (Class<?> c : classesToLookFor) {
/* 132 */       AspectJAnnotation<?> foundAnnotation = findAnnotation(method, c);
/* 133 */       if (foundAnnotation != null) {
/* 134 */         return foundAnnotation;
/*     */       }
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */   
/*     */   private static <A extends Annotation> AspectJAnnotation<A> findAnnotation(Method method, Class<A> toLookFor) {
/* 141 */     A result = AnnotationUtils.findAnnotation(method, toLookFor);
/* 142 */     if (result != null) {
/* 143 */       return new AspectJAnnotation(result);
/*     */     }
/*     */     
/* 146 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static enum AspectJAnnotationType
/*     */   {
/* 153 */     AtPointcut, 
/* 154 */     AtBefore, 
/* 155 */     AtAfter, 
/* 156 */     AtAfterReturning, 
/* 157 */     AtAfterThrowing, 
/* 158 */     AtAround;
/*     */     
/*     */ 
/*     */ 
/*     */     private AspectJAnnotationType() {}
/*     */   }
/*     */   
/*     */ 
/*     */   protected static class AspectJAnnotation<A extends Annotation>
/*     */   {
/* 168 */     private static final String[] EXPRESSION_PROPERTIES = { "value", "pointcut" };
/*     */     
/* 170 */     private static Map<Class<?>, AbstractAspectJAdvisorFactory.AspectJAnnotationType> annotationTypes = new HashMap();
/*     */     private final A annotation;
/*     */     private final AbstractAspectJAdvisorFactory.AspectJAnnotationType annotationType;
/*     */     
/* 174 */     static { annotationTypes.put(Pointcut.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtPointcut);
/* 175 */       annotationTypes.put(After.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfter);
/* 176 */       annotationTypes.put(AfterReturning.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfterReturning);
/* 177 */       annotationTypes.put(AfterThrowing.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAfterThrowing);
/* 178 */       annotationTypes.put(Around.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtAround);
/* 179 */       annotationTypes.put(Before.class, AbstractAspectJAdvisorFactory.AspectJAnnotationType.AtBefore);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private final String pointcutExpression;
/*     */     
/*     */ 
/*     */     private final String argumentNames;
/*     */     
/*     */     public AspectJAnnotation(A annotation)
/*     */     {
/* 191 */       this.annotation = annotation;
/* 192 */       this.annotationType = determineAnnotationType(annotation);
/*     */       
/*     */       try
/*     */       {
/* 196 */         this.pointcutExpression = resolveExpression(annotation);
/* 197 */         this.argumentNames = ((String)annotation.getClass().getMethod("argNames", new Class[0]).invoke(annotation, new Object[0]));
/*     */       }
/*     */       catch (Exception ex) {
/* 200 */         throw new IllegalArgumentException(annotation + " cannot be an AspectJ annotation", ex);
/*     */       }
/*     */     }
/*     */     
/*     */     private AbstractAspectJAdvisorFactory.AspectJAnnotationType determineAnnotationType(A annotation) {
/* 205 */       for (Class<?> type : annotationTypes.keySet()) {
/* 206 */         if (type.isInstance(annotation)) {
/* 207 */           return (AbstractAspectJAdvisorFactory.AspectJAnnotationType)annotationTypes.get(type);
/*     */         }
/*     */       }
/* 210 */       throw new IllegalStateException("Unknown annotation type: " + annotation.toString());
/*     */     }
/*     */     
/*     */     private String resolveExpression(A annotation) throws Exception {
/* 214 */       String expression = null;
/* 215 */       for (String methodName : EXPRESSION_PROPERTIES) {
/*     */         Method method;
/*     */         try {
/* 218 */           method = annotation.getClass().getDeclaredMethod(methodName, new Class[0]);
/*     */         } catch (NoSuchMethodException ex) {
/*     */           Method method;
/* 221 */           method = null;
/*     */         }
/* 223 */         if (method != null) {
/* 224 */           String candidate = (String)method.invoke(annotation, new Object[0]);
/* 225 */           if (StringUtils.hasText(candidate)) {
/* 226 */             expression = candidate;
/*     */           }
/*     */         }
/*     */       }
/* 230 */       return expression;
/*     */     }
/*     */     
/*     */     public AbstractAspectJAdvisorFactory.AspectJAnnotationType getAnnotationType() {
/* 234 */       return this.annotationType;
/*     */     }
/*     */     
/*     */     public A getAnnotation() {
/* 238 */       return this.annotation;
/*     */     }
/*     */     
/*     */     public String getPointcutExpression() {
/* 242 */       return this.pointcutExpression;
/*     */     }
/*     */     
/*     */     public String getArgumentNames() {
/* 246 */       return this.argumentNames;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 251 */       return this.annotation.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class AspectJAnnotationParameterNameDiscoverer
/*     */     implements ParameterNameDiscoverer
/*     */   {
/*     */     public String[] getParameterNames(Method method)
/*     */     {
/* 264 */       if (method.getParameterTypes().length == 0) {
/* 265 */         return new String[0];
/*     */       }
/* 267 */       AbstractAspectJAdvisorFactory.AspectJAnnotation<?> annotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
/* 268 */       if (annotation == null) {
/* 269 */         return null;
/*     */       }
/* 271 */       StringTokenizer strTok = new StringTokenizer(annotation.getArgumentNames(), ",");
/* 272 */       if (strTok.countTokens() > 0) {
/* 273 */         String[] names = new String[strTok.countTokens()];
/* 274 */         for (int i = 0; i < names.length; i++) {
/* 275 */           names[i] = strTok.nextToken();
/*     */         }
/* 277 */         return names;
/*     */       }
/*     */       
/* 280 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     public String[] getParameterNames(Constructor<?> ctor)
/*     */     {
/* 286 */       throw new UnsupportedOperationException("Spring AOP cannot handle constructor advice");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\annotation\AbstractAspectJAdvisorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */