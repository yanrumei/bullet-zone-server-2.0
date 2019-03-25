/*     */ package org.apache.tomcat.util;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Hashtable;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IntrospectionUtils
/*     */ {
/*  35 */   private static final Log log = LogFactory.getLog(IntrospectionUtils.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean setProperty(Object o, String name, String value)
/*     */   {
/*  47 */     return setProperty(o, name, value, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean setProperty(Object o, String name, String value, boolean invokeSetProperty)
/*     */   {
/*  53 */     if (log.isDebugEnabled()) {
/*  54 */       log.debug("IntrospectionUtils: setProperty(" + o
/*  55 */         .getClass() + " " + name + "=" + value + ")");
/*     */     }
/*  57 */     String setter = "set" + capitalize(name);
/*     */     try
/*     */     {
/*  60 */       Method[] methods = findMethods(o.getClass());
/*  61 */       Method setPropertyMethodVoid = null;
/*  62 */       Method setPropertyMethodBool = null;
/*     */       
/*     */ 
/*  65 */       for (int i = 0; i < methods.length; i++) {
/*  66 */         Class<?>[] paramT = methods[i].getParameterTypes();
/*  67 */         if ((setter.equals(methods[i].getName())) && (paramT.length == 1) && 
/*  68 */           ("java.lang.String".equals(paramT[0].getName())))
/*     */         {
/*  70 */           methods[i].invoke(o, new Object[] { value });
/*  71 */           return true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  76 */       for (int i = 0; i < methods.length; i++) {
/*  77 */         boolean ok = true;
/*  78 */         if ((setter.equals(methods[i].getName())) && 
/*  79 */           (methods[i].getParameterTypes().length == 1))
/*     */         {
/*     */ 
/*  82 */           Class<?> paramType = methods[i].getParameterTypes()[0];
/*  83 */           Object[] params = new Object[1];
/*     */           
/*     */ 
/*  86 */           if (("java.lang.Integer".equals(paramType.getName())) || 
/*  87 */             ("int".equals(paramType.getName()))) {
/*     */             try {
/*  89 */               params[0] = Integer.valueOf(value);
/*     */             } catch (NumberFormatException ex) {
/*  91 */               ok = false;
/*     */             }
/*     */             
/*  94 */           } else if (("java.lang.Long".equals(paramType.getName())) || 
/*  95 */             ("long".equals(paramType.getName()))) {
/*     */             try {
/*  97 */               params[0] = Long.valueOf(value);
/*     */             } catch (NumberFormatException ex) {
/*  99 */               ok = false;
/*     */             }
/*     */             
/*     */           }
/* 103 */           else if (("java.lang.Boolean".equals(paramType.getName())) || 
/* 104 */             ("boolean".equals(paramType.getName()))) {
/* 105 */             params[0] = Boolean.valueOf(value);
/*     */ 
/*     */           }
/* 108 */           else if ("java.net.InetAddress".equals(paramType
/* 109 */             .getName())) {
/*     */             try {
/* 111 */               params[0] = InetAddress.getByName(value);
/*     */             } catch (UnknownHostException exc) {
/* 113 */               if (log.isDebugEnabled())
/* 114 */                 log.debug("IntrospectionUtils: Unable to resolve host name:" + value);
/* 115 */               ok = false;
/*     */ 
/*     */             }
/*     */             
/*     */           }
/* 120 */           else if (log.isDebugEnabled()) {
/* 121 */             log.debug("IntrospectionUtils: Unknown type " + paramType
/* 122 */               .getName());
/*     */           }
/*     */           
/* 125 */           if (ok) {
/* 126 */             methods[i].invoke(o, params);
/* 127 */             return true;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 132 */         if ("setProperty".equals(methods[i].getName())) {
/* 133 */           if (methods[i].getReturnType() == Boolean.TYPE) {
/* 134 */             setPropertyMethodBool = methods[i];
/*     */           } else {
/* 136 */             setPropertyMethodVoid = methods[i];
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 143 */       if ((invokeSetProperty) && ((setPropertyMethodBool != null) || (setPropertyMethodVoid != null)))
/*     */       {
/* 145 */         Object[] params = new Object[2];
/* 146 */         params[0] = name;
/* 147 */         params[1] = value;
/* 148 */         if (setPropertyMethodBool != null) {
/*     */           try {
/* 150 */             return 
/* 151 */               ((Boolean)setPropertyMethodBool.invoke(o, params)).booleanValue();
/*     */           }
/*     */           catch (IllegalArgumentException biae)
/*     */           {
/* 155 */             if (setPropertyMethodVoid != null) {
/* 156 */               setPropertyMethodVoid.invoke(o, params);
/* 157 */               return true;
/*     */             }
/* 159 */             throw biae;
/*     */           }
/*     */         }
/*     */         
/* 163 */         setPropertyMethodVoid.invoke(o, params);
/* 164 */         return true;
/*     */       }
/*     */     }
/*     */     catch (IllegalArgumentException ex2)
/*     */     {
/* 169 */       log.warn("IAE " + o + " " + name + " " + value, ex2);
/*     */     } catch (SecurityException ex1) {
/* 171 */       log.warn("IntrospectionUtils: SecurityException for " + o
/* 172 */         .getClass() + " " + name + "=" + value + ")", ex1);
/*     */     } catch (IllegalAccessException iae) {
/* 174 */       log.warn("IntrospectionUtils: IllegalAccessException for " + o
/* 175 */         .getClass() + " " + name + "=" + value + ")", iae);
/*     */     } catch (InvocationTargetException ie) {
/* 177 */       ExceptionUtils.handleThrowable(ie.getCause());
/* 178 */       log.warn("IntrospectionUtils: InvocationTargetException for " + o
/* 179 */         .getClass() + " " + name + "=" + value + ")", ie);
/*     */     }
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   public static Object getProperty(Object o, String name) {
/* 185 */     String getter = "get" + capitalize(name);
/* 186 */     String isGetter = "is" + capitalize(name);
/*     */     try
/*     */     {
/* 189 */       Method[] methods = findMethods(o.getClass());
/* 190 */       Method getPropertyMethod = null;
/*     */       
/*     */ 
/* 193 */       for (int i = 0; i < methods.length; i++) {
/* 194 */         Class<?>[] paramT = methods[i].getParameterTypes();
/* 195 */         if ((getter.equals(methods[i].getName())) && (paramT.length == 0)) {
/* 196 */           return methods[i].invoke(o, (Object[])null);
/*     */         }
/* 198 */         if ((isGetter.equals(methods[i].getName())) && (paramT.length == 0)) {
/* 199 */           return methods[i].invoke(o, (Object[])null);
/*     */         }
/*     */         
/* 202 */         if ("getProperty".equals(methods[i].getName())) {
/* 203 */           getPropertyMethod = methods[i];
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 208 */       if (getPropertyMethod != null) {
/* 209 */         Object[] params = new Object[1];
/* 210 */         params[0] = name;
/* 211 */         return getPropertyMethod.invoke(o, params);
/*     */       }
/*     */     }
/*     */     catch (IllegalArgumentException ex2) {
/* 215 */       log.warn("IAE " + o + " " + name, ex2);
/*     */     } catch (SecurityException ex1) {
/* 217 */       log.warn("IntrospectionUtils: SecurityException for " + o
/* 218 */         .getClass() + " " + name + ")", ex1);
/*     */     } catch (IllegalAccessException iae) {
/* 220 */       log.warn("IntrospectionUtils: IllegalAccessException for " + o
/* 221 */         .getClass() + " " + name + ")", iae);
/*     */     } catch (InvocationTargetException ie) {
/* 223 */       if ((ie.getCause() instanceof NullPointerException))
/*     */       {
/* 225 */         return null;
/*     */       }
/* 227 */       ExceptionUtils.handleThrowable(ie.getCause());
/* 228 */       log.warn("IntrospectionUtils: InvocationTargetException for " + o
/* 229 */         .getClass() + " " + name + ")", ie);
/*     */     }
/* 231 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String replaceProperties(String value, Hashtable<Object, Object> staticProp, PropertySource[] dynamicProp)
/*     */   {
/* 243 */     if (value.indexOf('$') < 0) {
/* 244 */       return value;
/*     */     }
/* 246 */     StringBuilder sb = new StringBuilder();
/* 247 */     int prev = 0;
/*     */     
/*     */     int pos;
/* 250 */     while ((pos = value.indexOf('$', prev)) >= 0) {
/* 251 */       if (pos > 0) {
/* 252 */         sb.append(value.substring(prev, pos));
/*     */       }
/* 254 */       if (pos == value.length() - 1) {
/* 255 */         sb.append('$');
/* 256 */         prev = pos + 1;
/* 257 */       } else if (value.charAt(pos + 1) != '{') {
/* 258 */         sb.append('$');
/* 259 */         prev = pos + 1;
/*     */       } else {
/* 261 */         int endName = value.indexOf('}', pos);
/* 262 */         if (endName < 0) {
/* 263 */           sb.append(value.substring(pos));
/* 264 */           prev = value.length();
/*     */         }
/*     */         else {
/* 267 */           String n = value.substring(pos + 2, endName);
/* 268 */           String v = null;
/* 269 */           if (staticProp != null) {
/* 270 */             v = (String)staticProp.get(n);
/*     */           }
/* 272 */           if ((v == null) && (dynamicProp != null)) {
/* 273 */             for (int i = 0; i < dynamicProp.length; i++) {
/* 274 */               v = dynamicProp[i].getProperty(n);
/* 275 */               if (v != null) {
/*     */                 break;
/*     */               }
/*     */             }
/*     */           }
/* 280 */           if (v == null) {
/* 281 */             v = "${" + n + "}";
/*     */           }
/* 283 */           sb.append(v);
/* 284 */           prev = endName + 1;
/*     */         }
/*     */       } }
/* 287 */     if (prev < value.length())
/* 288 */       sb.append(value.substring(prev));
/* 289 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String capitalize(String name)
/*     */   {
/* 298 */     if ((name == null) || (name.length() == 0)) {
/* 299 */       return name;
/*     */     }
/* 301 */     char[] chars = name.toCharArray();
/* 302 */     chars[0] = Character.toUpperCase(chars[0]);
/* 303 */     return new String(chars);
/*     */   }
/*     */   
/*     */   public static void clear()
/*     */   {
/* 308 */     objectMethods.clear();
/*     */   }
/*     */   
/* 311 */   private static final Hashtable<Class<?>, Method[]> objectMethods = new Hashtable();
/*     */   
/*     */   public static Method[] findMethods(Class<?> c) {
/* 314 */     Method[] methods = (Method[])objectMethods.get(c);
/* 315 */     if (methods != null) {
/* 316 */       return methods;
/*     */     }
/* 318 */     methods = c.getMethods();
/* 319 */     objectMethods.put(c, methods);
/* 320 */     return methods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Method findMethod(Class<?> c, String name, Class<?>[] params)
/*     */   {
/* 327 */     Method[] methods = findMethods(c);
/* 328 */     if (methods == null)
/* 329 */       return null;
/* 330 */     for (int i = 0; i < methods.length; i++)
/* 331 */       if (methods[i].getName().equals(name)) {
/* 332 */         Class<?>[] methodParams = methods[i].getParameterTypes();
/* 333 */         if ((methodParams == null) && (
/* 334 */           (params == null) || (params.length == 0)))
/* 335 */           return methods[i];
/* 336 */         if ((params == null) && (
/* 337 */           (methodParams == null) || (methodParams.length == 0)))
/* 338 */           return methods[i];
/* 339 */         if (params.length == methodParams.length)
/*     */         {
/* 341 */           boolean found = true;
/* 342 */           for (int j = 0; j < params.length; j++) {
/* 343 */             if (params[j] != methodParams[j]) {
/* 344 */               found = false;
/* 345 */               break;
/*     */             }
/*     */           }
/* 348 */           if (found)
/* 349 */             return methods[i];
/*     */         }
/*     */       }
/* 352 */     return null;
/*     */   }
/*     */   
/*     */   public static Object callMethod1(Object target, String methodN, Object param1, String typeParam1, ClassLoader cl) throws Exception
/*     */   {
/* 357 */     if ((target == null) || (param1 == null)) {
/* 358 */       throw new IllegalArgumentException("IntrospectionUtils: Assert: Illegal params " + target + " " + param1);
/*     */     }
/*     */     
/*     */ 
/* 362 */     if (log.isDebugEnabled()) {
/* 363 */       log.debug("IntrospectionUtils: callMethod1 " + target
/* 364 */         .getClass().getName() + " " + param1
/* 365 */         .getClass().getName() + " " + typeParam1);
/*     */     }
/* 367 */     Class<?>[] params = new Class[1];
/* 368 */     if (typeParam1 == null) {
/* 369 */       params[0] = param1.getClass();
/*     */     } else
/* 371 */       params[0] = cl.loadClass(typeParam1);
/* 372 */     Method m = findMethod(target.getClass(), methodN, params);
/* 373 */     if (m == null) {
/* 374 */       throw new NoSuchMethodException(target.getClass().getName() + " " + methodN);
/*     */     }
/*     */     try {
/* 377 */       return m.invoke(target, new Object[] { param1 });
/*     */     } catch (InvocationTargetException ie) {
/* 379 */       ExceptionUtils.handleThrowable(ie.getCause());
/* 380 */       throw ie;
/*     */     }
/*     */   }
/*     */   
/*     */   public static Object callMethodN(Object target, String methodN, Object[] params, Class<?>[] typeParams) throws Exception
/*     */   {
/* 386 */     Method m = null;
/* 387 */     m = findMethod(target.getClass(), methodN, typeParams);
/* 388 */     if (m == null) {
/* 389 */       if (log.isDebugEnabled())
/* 390 */         log.debug("IntrospectionUtils: Can't find method " + methodN + " in " + target + " CLASS " + target
/* 391 */           .getClass());
/* 392 */       return null;
/*     */     }
/*     */     try {
/* 395 */       Object o = m.invoke(target, params);
/*     */       
/* 397 */       if (log.isDebugEnabled())
/*     */       {
/* 399 */         StringBuilder sb = new StringBuilder();
/* 400 */         sb.append(target.getClass().getName()).append('.')
/* 401 */           .append(methodN).append("( ");
/* 402 */         for (int i = 0; i < params.length; i++) {
/* 403 */           if (i > 0)
/* 404 */             sb.append(", ");
/* 405 */           sb.append(params[i]);
/*     */         }
/* 407 */         sb.append(")");
/* 408 */         log.debug("IntrospectionUtils:" + sb.toString());
/*     */       }
/* 410 */       return o;
/*     */     } catch (InvocationTargetException ie) {
/* 412 */       ExceptionUtils.handleThrowable(ie.getCause());
/* 413 */       throw ie;
/*     */     }
/*     */   }
/*     */   
/*     */   public static Object convert(String object, Class<?> paramType) {
/* 418 */     Object result = null;
/* 419 */     if ("java.lang.String".equals(paramType.getName())) {
/* 420 */       result = object;
/* 421 */     } else if (("java.lang.Integer".equals(paramType.getName())) || 
/* 422 */       ("int".equals(paramType.getName()))) {
/*     */       try {
/* 424 */         result = Integer.valueOf(object);
/*     */ 
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {}
/* 428 */     } else if (("java.lang.Boolean".equals(paramType.getName())) || 
/* 429 */       ("boolean".equals(paramType.getName()))) {
/* 430 */       result = Boolean.valueOf(object);
/*     */ 
/*     */     }
/* 433 */     else if ("java.net.InetAddress".equals(paramType
/* 434 */       .getName())) {
/*     */       try {
/* 436 */         result = InetAddress.getByName(object);
/*     */       } catch (UnknownHostException exc) {
/* 438 */         if (log.isDebugEnabled()) {
/* 439 */           log.debug("IntrospectionUtils: Unable to resolve host name:" + object);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/* 445 */     else if (log.isDebugEnabled()) {
/* 446 */       log.debug("IntrospectionUtils: Unknown type " + paramType
/* 447 */         .getName());
/*     */     }
/* 449 */     if (result == null) {
/* 450 */       throw new IllegalArgumentException("Can't convert argument: " + object);
/*     */     }
/* 452 */     return result;
/*     */   }
/*     */   
/*     */   public static abstract interface PropertySource
/*     */   {
/*     */     public abstract String getProperty(String paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\IntrospectionUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */