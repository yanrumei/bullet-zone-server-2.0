/*     */ package org.apache.tomcat.util.modeler.modules;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.modeler.AttributeInfo;
/*     */ import org.apache.tomcat.util.modeler.ManagedBean;
/*     */ import org.apache.tomcat.util.modeler.OperationInfo;
/*     */ import org.apache.tomcat.util.modeler.ParameterInfo;
/*     */ import org.apache.tomcat.util.modeler.Registry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MbeansDescriptorsIntrospectionSource
/*     */   extends ModelerSource
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(MbeansDescriptorsIntrospectionSource.class);
/*     */   
/*     */   private Registry registry;
/*     */   private String type;
/*  46 */   private final List<ObjectName> mbeans = new ArrayList();
/*     */   
/*     */   public void setRegistry(Registry reg) {
/*  49 */     this.registry = reg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setType(String type)
/*     */   {
/*  58 */     this.type = type;
/*     */   }
/*     */   
/*     */   public void setSource(Object source) {
/*  62 */     this.source = source;
/*     */   }
/*     */   
/*     */   public List<ObjectName> loadDescriptors(Registry registry, String type, Object source)
/*     */     throws Exception
/*     */   {
/*  68 */     setRegistry(registry);
/*  69 */     setType(type);
/*  70 */     setSource(source);
/*  71 */     execute();
/*  72 */     return this.mbeans;
/*     */   }
/*     */   
/*     */   public void execute() throws Exception {
/*  76 */     if (this.registry == null) this.registry = Registry.getRegistry(null, null);
/*     */     try {
/*  78 */       ManagedBean managed = createManagedBean(this.registry, null, (Class)this.source, this.type);
/*     */       
/*  80 */       if (managed == null) return;
/*  81 */       managed.setName(this.type);
/*     */       
/*  83 */       this.registry.addManagedBean(managed);
/*     */     }
/*     */     catch (Exception ex) {
/*  86 */       log.error("Error reading descriptors ", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private static final Hashtable<String, String> specialMethods = new Hashtable();
/*     */   
/*     */   static {
/*  97 */     specialMethods.put("preDeregister", "");
/*  98 */     specialMethods.put("postDeregister", "");
/*     */   }
/*     */   
/* 101 */   private static final Class<?>[] supportedTypes = { Boolean.class, Boolean.TYPE, Byte.class, Byte.TYPE, Character.class, Character.TYPE, Short.class, Short.TYPE, Integer.class, Integer.TYPE, Long.class, Long.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE, String.class, String[].class, BigDecimal.class, BigInteger.class, ObjectName.class, Object[].class, File.class };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean supportedType(Class<?> ret)
/*     */   {
/* 135 */     for (int i = 0; i < supportedTypes.length; i++) {
/* 136 */       if (ret == supportedTypes[i]) {
/* 137 */         return true;
/*     */       }
/*     */     }
/* 140 */     if (isBeanCompatible(ret)) {
/* 141 */       return true;
/*     */     }
/* 143 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isBeanCompatible(Class<?> javaType)
/*     */   {
/* 155 */     if ((javaType.isArray()) || (javaType.isPrimitive())) {
/* 156 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 161 */     if ((javaType.getName().startsWith("java.")) || 
/* 162 */       (javaType.getName().startsWith("javax."))) {
/* 163 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 167 */       javaType.getConstructor(new Class[0]);
/*     */     } catch (NoSuchMethodException e) {
/* 169 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 173 */     Class<?> superClass = javaType.getSuperclass();
/* 174 */     if ((superClass != null) && (superClass != Object.class) && (superClass != Exception.class) && (superClass != Throwable.class))
/*     */     {
/*     */ 
/*     */ 
/* 178 */       if (!isBeanCompatible(superClass)) {
/* 179 */         return false;
/*     */       }
/*     */     }
/* 182 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initMethods(Class<?> realClass, Method[] methods, Hashtable<String, Method> attMap, Hashtable<String, Method> getAttMap, Hashtable<String, Method> setAttMap, Hashtable<String, Method> invokeAttMap)
/*     */   {
/* 202 */     for (int j = 0; j < methods.length; j++) {
/* 203 */       String name = methods[j].getName();
/*     */       
/* 205 */       if (!Modifier.isStatic(methods[j].getModifiers()))
/*     */       {
/* 207 */         if (!Modifier.isPublic(methods[j].getModifiers())) {
/* 208 */           if (log.isDebugEnabled()) {
/* 209 */             log.debug("Not public " + methods[j]);
/*     */           }
/*     */         }
/* 212 */         else if (methods[j].getDeclaringClass() != Object.class)
/*     */         {
/* 214 */           Class<?>[] params = methods[j].getParameterTypes();
/*     */           
/* 216 */           if ((name.startsWith("get")) && (params.length == 0)) {
/* 217 */             Class<?> ret = methods[j].getReturnType();
/* 218 */             if (!supportedType(ret)) {
/* 219 */               if (log.isDebugEnabled()) {
/* 220 */                 log.debug("Unsupported type " + methods[j]);
/*     */               }
/*     */             } else {
/* 223 */               name = unCapitalize(name.substring(3));
/*     */               
/* 225 */               getAttMap.put(name, methods[j]);
/*     */               
/* 227 */               attMap.put(name, methods[j]);
/* 228 */             } } else if ((name.startsWith("is")) && (params.length == 0)) {
/* 229 */             Class<?> ret = methods[j].getReturnType();
/* 230 */             if (Boolean.TYPE != ret) {
/* 231 */               if (log.isDebugEnabled()) {
/* 232 */                 log.debug("Unsupported type " + methods[j] + " " + ret);
/*     */               }
/*     */             } else {
/* 235 */               name = unCapitalize(name.substring(2));
/*     */               
/* 237 */               getAttMap.put(name, methods[j]);
/*     */               
/* 239 */               attMap.put(name, methods[j]);
/*     */             }
/* 241 */           } else if ((name.startsWith("set")) && (params.length == 1)) {
/* 242 */             if (!supportedType(params[0])) {
/* 243 */               if (log.isDebugEnabled()) {
/* 244 */                 log.debug("Unsupported type " + methods[j] + " " + params[0]);
/*     */               }
/*     */             } else {
/* 247 */               name = unCapitalize(name.substring(3));
/* 248 */               setAttMap.put(name, methods[j]);
/* 249 */               attMap.put(name, methods[j]);
/*     */             }
/* 251 */           } else if (params.length == 0) {
/* 252 */             if (specialMethods.get(methods[j].getName()) == null)
/*     */             {
/* 254 */               invokeAttMap.put(name, methods[j]); }
/*     */           } else {
/* 256 */             boolean supported = true;
/* 257 */             for (int i = 0; i < params.length; i++) {
/* 258 */               if (!supportedType(params[i])) {
/* 259 */                 supported = false;
/* 260 */                 break;
/*     */               }
/*     */             }
/* 263 */             if (supported) {
/* 264 */               invokeAttMap.put(name, methods[j]);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
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
/*     */ 
/*     */ 
/*     */   public ManagedBean createManagedBean(Registry registry, String domain, Class<?> realClass, String type)
/*     */   {
/* 286 */     ManagedBean mbean = new ManagedBean();
/*     */     
/* 288 */     Method[] methods = null;
/*     */     
/* 290 */     Hashtable<String, Method> attMap = new Hashtable();
/*     */     
/* 292 */     Hashtable<String, Method> getAttMap = new Hashtable();
/*     */     
/* 294 */     Hashtable<String, Method> setAttMap = new Hashtable();
/*     */     
/* 296 */     Hashtable<String, Method> invokeAttMap = new Hashtable();
/*     */     
/* 298 */     methods = realClass.getMethods();
/*     */     
/* 300 */     initMethods(realClass, methods, attMap, getAttMap, setAttMap, invokeAttMap);
/*     */     
/*     */     try
/*     */     {
/* 304 */       Enumeration<String> en = attMap.keys();
/* 305 */       String name; while (en.hasMoreElements()) {
/* 306 */         name = (String)en.nextElement();
/* 307 */         AttributeInfo ai = new AttributeInfo();
/* 308 */         ai.setName(name);
/* 309 */         Method gm = (Method)getAttMap.get(name);
/* 310 */         if (gm != null)
/*     */         {
/* 312 */           ai.setGetMethod(gm.getName());
/* 313 */           Class<?> t = gm.getReturnType();
/* 314 */           if (t != null)
/* 315 */             ai.setType(t.getName());
/*     */         }
/* 317 */         Method sm = (Method)setAttMap.get(name);
/* 318 */         if (sm != null)
/*     */         {
/* 320 */           Class<?> t = sm.getParameterTypes()[0];
/* 321 */           if (t != null)
/* 322 */             ai.setType(t.getName());
/* 323 */           ai.setSetMethod(sm.getName());
/*     */         }
/* 325 */         ai.setDescription("Introspected attribute " + name);
/* 326 */         if (log.isDebugEnabled()) { log.debug("Introspected attribute " + name + " " + gm + " " + sm);
/*     */         }
/* 328 */         if (gm == null)
/* 329 */           ai.setReadable(false);
/* 330 */         if (sm == null)
/* 331 */           ai.setWriteable(false);
/* 332 */         if ((sm != null) || (gm != null)) {
/* 333 */           mbean.addAttribute(ai);
/*     */         }
/*     */       }
/* 336 */       for (Map.Entry<String, Method> entry : invokeAttMap.entrySet()) {
/* 337 */         String name = (String)entry.getKey();
/* 338 */         Method m = (Method)entry.getValue();
/* 339 */         if (m != null) {
/* 340 */           OperationInfo op = new OperationInfo();
/* 341 */           op.setName(name);
/* 342 */           op.setReturnType(m.getReturnType().getName());
/* 343 */           op.setDescription("Introspected operation " + name);
/* 344 */           Class<?>[] parms = m.getParameterTypes();
/* 345 */           for (int i = 0; i < parms.length; i++) {
/* 346 */             ParameterInfo pi = new ParameterInfo();
/* 347 */             pi.setType(parms[i].getName());
/* 348 */             pi.setName("param" + i);
/* 349 */             pi.setDescription("Introspected parameter param" + i);
/* 350 */             op.addParameter(pi);
/*     */           }
/* 352 */           mbean.addOperation(op);
/*     */         } else {
/* 354 */           log.error("Null arg method for [" + name + "]");
/*     */         }
/*     */       }
/*     */       
/* 358 */       if (log.isDebugEnabled())
/* 359 */         log.debug("Setting name: " + type);
/* 360 */       mbean.setName(type);
/*     */       
/* 362 */       return mbean;
/*     */     } catch (Exception ex) {
/* 364 */       ex.printStackTrace(); }
/* 365 */     return null;
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
/*     */   private static String unCapitalize(String name)
/*     */   {
/* 379 */     if ((name == null) || (name.length() == 0)) {
/* 380 */       return name;
/*     */     }
/* 382 */     char[] chars = name.toCharArray();
/* 383 */     chars[0] = Character.toLowerCase(chars[0]);
/* 384 */     return new String(chars);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\modules\MbeansDescriptorsIntrospectionSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */