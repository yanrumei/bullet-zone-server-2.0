/*     */ package javax.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StaticFieldELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  35 */     Objects.requireNonNull(context);
/*     */     
/*  37 */     if (((base instanceof ELClass)) && ((property instanceof String))) {
/*  38 */       context.setPropertyResolved(base, property);
/*     */       
/*  40 */       Class<?> clazz = ((ELClass)base).getKlass();
/*  41 */       String name = (String)property;
/*  42 */       Exception exception = null;
/*     */       try {
/*  44 */         Field field = clazz.getField(name);
/*  45 */         int modifiers = field.getModifiers();
/*  46 */         if ((Modifier.isStatic(modifiers)) && 
/*  47 */           (Modifier.isPublic(modifiers))) {
/*  48 */           return field.get(null);
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException|IllegalAccessException|NoSuchFieldException|SecurityException e) {
/*  52 */         exception = e;
/*     */       }
/*  54 */       String msg = Util.message(context, "staticFieldELResolver.notFound", new Object[] { name, clazz
/*  55 */         .getName() });
/*  56 */       if (exception == null) {
/*  57 */         throw new PropertyNotFoundException(msg);
/*     */       }
/*  59 */       throw new PropertyNotFoundException(msg, exception);
/*     */     }
/*     */     
/*  62 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/*  69 */     Objects.requireNonNull(context);
/*     */     
/*  71 */     if (((base instanceof ELClass)) && ((property instanceof String))) {
/*  72 */       Class<?> clazz = ((ELClass)base).getKlass();
/*  73 */       String name = (String)property;
/*     */       
/*  75 */       throw new PropertyNotWritableException(Util.message(context, "staticFieldELResolver.notWriteable", new Object[] { name, clazz
/*     */       
/*  77 */         .getName() }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params)
/*     */   {
/*  85 */     Objects.requireNonNull(context);
/*     */     
/*  87 */     if (((base instanceof ELClass)) && ((method instanceof String))) {
/*  88 */       context.setPropertyResolved(base, method);
/*     */       
/*  90 */       Class<?> clazz = ((ELClass)base).getKlass();
/*  91 */       String methodName = (String)method;
/*     */       
/*  93 */       if ("<init>".equals(methodName))
/*     */       {
/*  95 */         Constructor<?> match = Util.findConstructor(clazz, paramTypes, params);
/*     */         
/*  97 */         Object[] parameters = Util.buildParameters(match
/*  98 */           .getParameterTypes(), match.isVarArgs(), params);
/*     */         
/* 100 */         Object result = null;
/*     */         try
/*     */         {
/* 103 */           result = match.newInstance(parameters);
/*     */         }
/*     */         catch (IllegalArgumentException|IllegalAccessException|InstantiationException e) {
/* 106 */           throw new ELException(e);
/*     */         } catch (InvocationTargetException e) {
/* 108 */           Throwable cause = e.getCause();
/* 109 */           Util.handleThrowable(cause);
/* 110 */           throw new ELException(cause);
/*     */         }
/* 112 */         return result;
/*     */       }
/*     */       
/*     */ 
/* 116 */       Method match = Util.findMethod(clazz, methodName, paramTypes, params);
/*     */       
/* 118 */       int modifiers = match.getModifiers();
/* 119 */       if (!Modifier.isStatic(modifiers)) {
/* 120 */         throw new MethodNotFoundException(Util.message(context, "staticFieldELResolver.methodNotFound", new Object[] { methodName, clazz
/*     */         
/* 122 */           .getName() }));
/*     */       }
/*     */       
/* 125 */       Object[] parameters = Util.buildParameters(match
/* 126 */         .getParameterTypes(), match.isVarArgs(), params);
/*     */       
/* 128 */       Object result = null;
/*     */       try {
/* 130 */         result = match.invoke(null, parameters);
/*     */       } catch (IllegalArgumentException|IllegalAccessException e) {
/* 132 */         throw new ELException(e);
/*     */       } catch (InvocationTargetException e) {
/* 134 */         Throwable cause = e.getCause();
/* 135 */         Util.handleThrowable(cause);
/* 136 */         throw new ELException(cause);
/*     */       }
/* 138 */       return result;
/*     */     }
/*     */     
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/* 146 */     Objects.requireNonNull(context);
/*     */     
/* 148 */     if (((base instanceof ELClass)) && ((property instanceof String))) {
/* 149 */       context.setPropertyResolved(base, property);
/*     */       
/* 151 */       Class<?> clazz = ((ELClass)base).getKlass();
/* 152 */       String name = (String)property;
/* 153 */       Exception exception = null;
/*     */       try {
/* 155 */         Field field = clazz.getField(name);
/* 156 */         int modifiers = field.getModifiers();
/* 157 */         if ((Modifier.isStatic(modifiers)) && 
/* 158 */           (Modifier.isPublic(modifiers))) {
/* 159 */           return field.getType();
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException|NoSuchFieldException|SecurityException e) {
/* 163 */         exception = e;
/*     */       }
/* 165 */       String msg = Util.message(context, "staticFieldELResolver.notFound", new Object[] { name, clazz
/* 166 */         .getName() });
/* 167 */       if (exception == null) {
/* 168 */         throw new PropertyNotFoundException(msg);
/*     */       }
/* 170 */       throw new PropertyNotFoundException(msg, exception);
/*     */     }
/*     */     
/* 173 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/* 179 */     Objects.requireNonNull(context);
/*     */     
/* 181 */     if (((base instanceof ELClass)) && ((property instanceof String))) {
/* 182 */       context.setPropertyResolved(base, property);
/*     */     }
/* 184 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/* 194 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/* 202 */     return String.class;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\StaticFieldELResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */