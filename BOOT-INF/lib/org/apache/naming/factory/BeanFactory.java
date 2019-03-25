/*     */ package org.apache.naming.factory;
/*     */ 
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ import org.apache.naming.ResourceRef;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanFactory
/*     */   implements ObjectFactory
/*     */ {
/*     */   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
/*     */     throws NamingException
/*     */   {
/* 119 */     if ((obj instanceof ResourceRef))
/*     */     {
/*     */       try
/*     */       {
/* 123 */         Reference ref = (Reference)obj;
/* 124 */         String beanClassName = ref.getClassName();
/* 125 */         Class<?> beanClass = null;
/*     */         
/* 127 */         ClassLoader tcl = Thread.currentThread().getContextClassLoader();
/* 128 */         if (tcl != null) {
/*     */           try {
/* 130 */             beanClass = tcl.loadClass(beanClassName);
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException1) {}
/*     */         } else {
/*     */           try {
/* 135 */             beanClass = Class.forName(beanClassName);
/*     */           } catch (ClassNotFoundException e) {
/* 137 */             e.printStackTrace();
/*     */           }
/*     */         }
/* 140 */         if (beanClass == null) {
/* 141 */           throw new NamingException("Class not found: " + beanClassName);
/*     */         }
/*     */         
/*     */ 
/* 145 */         BeanInfo bi = Introspector.getBeanInfo(beanClass);
/* 146 */         PropertyDescriptor[] pda = bi.getPropertyDescriptors();
/*     */         
/* 148 */         Object bean = beanClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */         
/*     */ 
/* 151 */         RefAddr ra = ref.get("forceString");
/* 152 */         Map<String, Method> forced = new HashMap();
/*     */         
/*     */ 
/* 155 */         if (ra != null) {
/* 156 */           String value = (String)ra.getContent();
/* 157 */           Class<?>[] paramTypes = new Class[1];
/* 158 */           paramTypes[0] = String.class;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 163 */           for (String param : value.split(",")) {
/* 164 */             param = param.trim();
/*     */             
/*     */ 
/*     */ 
/* 168 */             int index = param.indexOf('=');
/* 169 */             String setterName; if (index >= 0) {
/* 170 */               String setterName = param.substring(index + 1).trim();
/* 171 */               param = param.substring(0, index).trim();
/*     */             }
/*     */             else
/*     */             {
/* 175 */               setterName = "set" + param.substring(0, 1).toUpperCase(Locale.ENGLISH) + param.substring(1);
/*     */             }
/*     */             try {
/* 178 */               forced.put(param, beanClass
/* 179 */                 .getMethod(setterName, paramTypes));
/*     */             } catch (NoSuchMethodException|SecurityException ex) {
/* 181 */               throw new NamingException("Forced String setter " + setterName + " not found for property " + param);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 188 */         Enumeration<RefAddr> e = ref.getAll();
/*     */         
/* 190 */         while (e.hasMoreElements())
/*     */         {
/* 192 */           ra = (RefAddr)e.nextElement();
/* 193 */           String propName = ra.getType();
/*     */           
/* 195 */           if ((!propName.equals("factory")) && 
/* 196 */             (!propName.equals("scope")) && (!propName.equals("auth")) && 
/* 197 */             (!propName.equals("forceString")) && 
/* 198 */             (!propName.equals("singleton")))
/*     */           {
/*     */ 
/*     */ 
/* 202 */             String value = (String)ra.getContent();
/*     */             
/* 204 */             Object[] valueArray = new Object[1];
/*     */             
/*     */ 
/* 207 */             Method method = (Method)forced.get(propName);
/* 208 */             if (method != null) {
/* 209 */               valueArray[0] = value;
/*     */               try {
/* 211 */                 method.invoke(bean, valueArray);
/*     */ 
/*     */               }
/*     */               catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException ex)
/*     */               {
/* 216 */                 throw new NamingException("Forced String setter " + method.getName() + " threw exception for property " + propName);
/*     */               }
/*     */               
/*     */             }
/*     */             else
/*     */             {
/* 222 */               int i = 0;
/* 223 */               for (i = 0; i < pda.length; i++)
/*     */               {
/* 225 */                 if (pda[i].getName().equals(propName))
/*     */                 {
/* 227 */                   Object propType = pda[i].getPropertyType();
/*     */                   
/* 229 */                   if (propType.equals(String.class)) {
/* 230 */                     valueArray[0] = value;
/* 231 */                   } else if ((propType.equals(Character.class)) || 
/* 232 */                     (propType.equals(Character.TYPE)))
/*     */                   {
/* 234 */                     valueArray[0] = Character.valueOf(value.charAt(0));
/* 235 */                   } else if ((propType.equals(Byte.class)) || 
/* 236 */                     (propType.equals(Byte.TYPE))) {
/* 237 */                     valueArray[0] = Byte.valueOf(value);
/* 238 */                   } else if ((propType.equals(Short.class)) || 
/* 239 */                     (propType.equals(Short.TYPE))) {
/* 240 */                     valueArray[0] = Short.valueOf(value);
/* 241 */                   } else if ((propType.equals(Integer.class)) || 
/* 242 */                     (propType.equals(Integer.TYPE))) {
/* 243 */                     valueArray[0] = Integer.valueOf(value);
/* 244 */                   } else if ((propType.equals(Long.class)) || 
/* 245 */                     (propType.equals(Long.TYPE))) {
/* 246 */                     valueArray[0] = Long.valueOf(value);
/* 247 */                   } else if ((propType.equals(Float.class)) || 
/* 248 */                     (propType.equals(Float.TYPE))) {
/* 249 */                     valueArray[0] = Float.valueOf(value);
/* 250 */                   } else if ((propType.equals(Double.class)) || 
/* 251 */                     (propType.equals(Double.TYPE))) {
/* 252 */                     valueArray[0] = Double.valueOf(value);
/* 253 */                   } else if ((propType.equals(Boolean.class)) || 
/* 254 */                     (propType.equals(Boolean.TYPE))) {
/* 255 */                     valueArray[0] = Boolean.valueOf(value);
/*     */                   }
/*     */                   else
/*     */                   {
/* 259 */                     throw new NamingException("String conversion for property " + propName + " of type '" + ((Class)propType).getName() + "' not available");
/*     */                   }
/*     */                   
/*     */ 
/* 263 */                   Method setProp = pda[i].getWriteMethod();
/* 264 */                   if (setProp != null) {
/* 265 */                     setProp.invoke(bean, valueArray); break;
/*     */                   }
/* 267 */                   throw new NamingException("Write not allowed for property: " + propName);
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 278 */               if (i == pda.length) {
/* 279 */                 throw new NamingException("No set method found for property: " + propName);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 285 */         return bean;
/*     */       }
/*     */       catch (IntrospectionException ie) {
/* 288 */         NamingException ne = new NamingException(ie.getMessage());
/* 289 */         ne.setRootCause(ie);
/* 290 */         throw ne;
/*     */       } catch (ReflectiveOperationException e) {
/* 292 */         Throwable cause = e.getCause();
/* 293 */         if ((cause instanceof ThreadDeath)) {
/* 294 */           throw ((ThreadDeath)cause);
/*     */         }
/* 296 */         if ((cause instanceof VirtualMachineError)) {
/* 297 */           throw ((VirtualMachineError)cause);
/*     */         }
/* 299 */         NamingException ne = new NamingException(e.getMessage());
/* 300 */         ne.setRootCause(e);
/* 301 */         throw ne;
/*     */       }
/*     */     }
/*     */     
/* 305 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\factory\BeanFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */