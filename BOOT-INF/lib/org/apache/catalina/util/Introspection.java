/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.beans.Introspector;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.Loader;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Introspection
/*     */ {
/*  39 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.util");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getPropertyName(Method setter)
/*     */   {
/*  51 */     return Introspector.decapitalize(setter.getName().substring(3));
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
/*     */   public static boolean isValidSetter(Method method)
/*     */   {
/*  65 */     if ((method.getName().startsWith("set")) && 
/*  66 */       (method.getName().length() > 3) && 
/*  67 */       (method.getParameterTypes().length == 1) && 
/*  68 */       (method.getReturnType().getName().equals("void"))) {
/*  69 */       return true;
/*     */     }
/*  71 */     return false;
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
/*     */   public static boolean isValidLifecycleCallback(Method method)
/*     */   {
/*  84 */     if ((method.getParameterTypes().length != 0) || 
/*  85 */       (Modifier.isStatic(method.getModifiers())) || 
/*  86 */       (method.getExceptionTypes().length > 0) || 
/*  87 */       (!method.getReturnType().getName().equals("void"))) {
/*  88 */       return false;
/*     */     }
/*  90 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Field[] getDeclaredFields(Class<?> clazz)
/*     */   {
/* 100 */     Field[] fields = null;
/* 101 */     if (Globals.IS_SECURITY_ENABLED) {
/* 102 */       fields = (Field[])AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Field[] run()
/*     */         {
/* 106 */           return this.val$clazz.getDeclaredFields();
/*     */         }
/*     */       });
/*     */     } else {
/* 110 */       fields = clazz.getDeclaredFields();
/*     */     }
/* 112 */     return fields;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Method[] getDeclaredMethods(Class<?> clazz)
/*     */   {
/* 123 */     Method[] methods = null;
/* 124 */     if (Globals.IS_SECURITY_ENABLED) {
/* 125 */       methods = (Method[])AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Method[] run()
/*     */         {
/* 129 */           return this.val$clazz.getDeclaredMethods();
/*     */         }
/*     */       });
/*     */     } else {
/* 133 */       methods = clazz.getDeclaredMethods();
/*     */     }
/* 135 */     return methods;
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
/*     */   public static Class<?> loadClass(Context context, String className)
/*     */   {
/* 149 */     ClassLoader cl = context.getLoader().getClassLoader();
/* 150 */     Log log = context.getLogger();
/* 151 */     Class<?> clazz = null;
/*     */     try {
/* 153 */       clazz = cl.loadClass(className);
/*     */     } catch (ClassNotFoundException|NoClassDefFoundError|ClassFormatError e) {
/* 155 */       log.debug(sm.getString("introspection.classLoadFailed", new Object[] { className }), e);
/*     */     } catch (Throwable t) {
/* 157 */       ExceptionUtils.handleThrowable(t);
/* 158 */       log.debug(sm.getString("introspection.classLoadFailed", new Object[] { className }), t);
/*     */     }
/* 160 */     return clazz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> convertPrimitiveType(Class<?> clazz)
/*     */   {
/* 172 */     if (clazz.equals(Character.TYPE))
/* 173 */       return Character.class;
/* 174 */     if (clazz.equals(Integer.TYPE))
/* 175 */       return Integer.class;
/* 176 */     if (clazz.equals(Boolean.TYPE))
/* 177 */       return Boolean.class;
/* 178 */     if (clazz.equals(Double.TYPE))
/* 179 */       return Double.class;
/* 180 */     if (clazz.equals(Byte.TYPE))
/* 181 */       return Byte.class;
/* 182 */     if (clazz.equals(Short.TYPE))
/* 183 */       return Short.class;
/* 184 */     if (clazz.equals(Long.TYPE))
/* 185 */       return Long.class;
/* 186 */     if (clazz.equals(Float.TYPE)) {
/* 187 */       return Float.class;
/*     */     }
/* 189 */     return clazz;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\Introspection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */