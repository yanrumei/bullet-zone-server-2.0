/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.MethodFilter;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectiveMethodResolver
/*     */   implements MethodResolver
/*     */ {
/*     */   private final boolean useDistance;
/*     */   private Map<Class<?>, MethodFilter> filters;
/*     */   
/*     */   public ReflectiveMethodResolver()
/*     */   {
/*  66 */     this.useDistance = true;
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
/*     */   public ReflectiveMethodResolver(boolean useDistance)
/*     */   {
/*  80 */     this.useDistance = useDistance;
/*     */   }
/*     */   
/*     */   public void registerMethodFilter(Class<?> type, MethodFilter filter)
/*     */   {
/*  85 */     if (this.filters == null) {
/*  86 */       this.filters = new HashMap();
/*     */     }
/*  88 */     if (filter != null) {
/*  89 */       this.filters.put(type, filter);
/*     */     }
/*     */     else {
/*  92 */       this.filters.remove(type);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> argumentTypes)
/*     */     throws AccessException
/*     */   {
/*     */     try
/*     */     {
/* 111 */       TypeConverter typeConverter = context.getTypeConverter();
/* 112 */       Class<?> type = (targetObject instanceof Class) ? (Class)targetObject : targetObject.getClass();
/* 113 */       List<Method> methods = new ArrayList(getMethods(type, targetObject));
/*     */       
/*     */ 
/* 116 */       MethodFilter filter = this.filters != null ? (MethodFilter)this.filters.get(type) : null;
/* 117 */       if (filter != null) {
/* 118 */         List<Method> filtered = filter.filter(methods);
/* 119 */         methods = (filtered instanceof ArrayList) ? filtered : new ArrayList(filtered);
/*     */       }
/*     */       
/*     */ 
/* 123 */       if (methods.size() > 1) {
/* 124 */         Collections.sort(methods, new Comparator()
/*     */         {
/*     */           public int compare(Method m1, Method m2) {
/* 127 */             int m1pl = m1.getParameterTypes().length;
/* 128 */             int m2pl = m2.getParameterTypes().length;
/*     */             
/* 130 */             if (m1pl == m2pl) {
/* 131 */               if ((!m1.isVarArgs()) && (m2.isVarArgs())) {
/* 132 */                 return -1;
/*     */               }
/* 134 */               if ((m1.isVarArgs()) && (!m2.isVarArgs())) {
/* 135 */                 return 1;
/*     */               }
/*     */               
/* 138 */               return 0;
/*     */             }
/*     */             
/* 141 */             return m1pl > m2pl ? 1 : m1pl < m2pl ? -1 : 0;
/*     */           }
/*     */         });
/*     */       }
/*     */       
/*     */ 
/* 147 */       for (int i = 0; i < methods.size(); i++) {
/* 148 */         methods.set(i, BridgeMethodResolver.findBridgedMethod((Method)methods.get(i)));
/*     */       }
/*     */       
/*     */ 
/* 152 */       Set<Method> methodsToIterate = new LinkedHashSet(methods);
/*     */       
/* 154 */       Method closeMatch = null;
/* 155 */       int closeMatchDistance = Integer.MAX_VALUE;
/* 156 */       Method matchRequiringConversion = null;
/* 157 */       boolean multipleOptions = false;
/*     */       
/* 159 */       for (Method method : methodsToIterate) {
/* 160 */         if (method.getName().equals(name)) {
/* 161 */           Class<?>[] paramTypes = method.getParameterTypes();
/* 162 */           List<TypeDescriptor> paramDescriptors = new ArrayList(paramTypes.length);
/* 163 */           for (int i = 0; i < paramTypes.length; i++) {
/* 164 */             paramDescriptors.add(new TypeDescriptor(new MethodParameter(method, i)));
/*     */           }
/* 166 */           ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
/* 167 */           if ((method.isVarArgs()) && (argumentTypes.size() >= paramTypes.length - 1))
/*     */           {
/* 169 */             matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
/*     */           }
/* 171 */           else if (paramTypes.length == argumentTypes.size())
/*     */           {
/* 173 */             matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
/*     */           }
/* 175 */           if (matchInfo != null) {
/* 176 */             if (matchInfo.isExactMatch()) {
/* 177 */               return new ReflectiveMethodExecutor(method);
/*     */             }
/* 179 */             if (matchInfo.isCloseMatch()) {
/* 180 */               if (this.useDistance) {
/* 181 */                 int matchDistance = ReflectionHelper.getTypeDifferenceWeight(paramDescriptors, argumentTypes);
/* 182 */                 if ((closeMatch == null) || (matchDistance < closeMatchDistance))
/*     */                 {
/* 184 */                   closeMatch = method;
/* 185 */                   closeMatchDistance = matchDistance;
/*     */                 }
/*     */                 
/*     */ 
/*     */               }
/* 190 */               else if (closeMatch == null) {
/* 191 */                 closeMatch = method;
/*     */               }
/*     */               
/*     */             }
/* 195 */             else if (matchInfo.isMatchRequiringConversion()) {
/* 196 */               if (matchRequiringConversion != null) {
/* 197 */                 multipleOptions = true;
/*     */               }
/* 199 */               matchRequiringConversion = method;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 204 */       if (closeMatch != null) {
/* 205 */         return new ReflectiveMethodExecutor(closeMatch);
/*     */       }
/* 207 */       if (matchRequiringConversion != null) {
/* 208 */         if (multipleOptions) {
/* 209 */           throw new SpelEvaluationException(SpelMessage.MULTIPLE_POSSIBLE_METHODS, new Object[] { name });
/*     */         }
/* 211 */         return new ReflectiveMethodExecutor(matchRequiringConversion);
/*     */       }
/*     */       
/* 214 */       return null;
/*     */     }
/*     */     catch (EvaluationException ex)
/*     */     {
/* 218 */       throw new AccessException("Failed to resolve method", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private Collection<Method> getMethods(Class<?> type, Object targetObject) { Method[] methods;
/* 223 */     if ((targetObject instanceof Class)) {
/* 224 */       Set<Method> result = new LinkedHashSet();
/*     */       
/* 226 */       methods = getMethods(type);
/* 227 */       for (Method method : methods) {
/* 228 */         if (Modifier.isStatic(method.getModifiers())) {
/* 229 */           result.add(method);
/*     */         }
/*     */       }
/*     */       
/* 233 */       result.addAll(Arrays.asList(getMethods(Class.class)));
/* 234 */       return result;
/*     */     }
/* 236 */     if (Proxy.isProxyClass(type)) {
/* 237 */       Set<Method> result = new LinkedHashSet();
/*     */       
/* 239 */       for (Object ifc : type.getInterfaces()) {
/* 240 */         result.addAll(Arrays.asList(getMethods((Class)ifc)));
/*     */       }
/* 242 */       return result;
/*     */     }
/*     */     
/* 245 */     return Arrays.asList(getMethods(type));
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
/*     */   protected Method[] getMethods(Class<?> type)
/*     */   {
/* 258 */     return type.getMethods();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\support\ReflectiveMethodResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */