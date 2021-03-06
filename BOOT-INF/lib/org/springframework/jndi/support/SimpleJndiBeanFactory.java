/*     */ package org.springframework.jndi.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.jndi.JndiLocatorSupport;
/*     */ import org.springframework.jndi.TypeMismatchNamingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleJndiBeanFactory
/*     */   extends JndiLocatorSupport
/*     */   implements BeanFactory
/*     */ {
/*  63 */   private final Set<String> shareableResources = new HashSet();
/*     */   
/*     */ 
/*  66 */   private final Map<String, Object> singletonObjects = new HashMap();
/*     */   
/*     */ 
/*  69 */   private final Map<String, Class<?>> resourceTypes = new HashMap();
/*     */   
/*     */   public SimpleJndiBeanFactory()
/*     */   {
/*  73 */     setResourceRef(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addShareableResource(String shareableResource)
/*     */   {
/*  84 */     this.shareableResources.add(shareableResource);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShareableResources(String... shareableResources)
/*     */   {
/*  94 */     this.shareableResources.addAll(Arrays.asList(shareableResources));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getBean(String name)
/*     */     throws BeansException
/*     */   {
/* 105 */     return getBean(name, Object.class);
/*     */   }
/*     */   
/*     */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException
/*     */   {
/*     */     try {
/* 111 */       if (isSingleton(name)) {
/* 112 */         return (T)doGetSingleton(name, requiredType);
/*     */       }
/*     */       
/* 115 */       return (T)lookup(name, requiredType);
/*     */     }
/*     */     catch (NameNotFoundException ex)
/*     */     {
/* 119 */       throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
/*     */     }
/*     */     catch (TypeMismatchNamingException ex) {
/* 122 */       throw new BeanNotOfRequiredTypeException(name, ex.getRequiredType(), ex.getActualType());
/*     */     }
/*     */     catch (NamingException ex) {
/* 125 */       throw new BeanDefinitionStoreException("JNDI environment", name, "JNDI lookup failed", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> T getBean(Class<T> requiredType) throws BeansException
/*     */   {
/* 131 */     return (T)getBean(requiredType.getSimpleName(), requiredType);
/*     */   }
/*     */   
/*     */   public Object getBean(String name, Object... args) throws BeansException
/*     */   {
/* 136 */     if (args != null) {
/* 137 */       throw new UnsupportedOperationException("SimpleJndiBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */     
/* 140 */     return getBean(name);
/*     */   }
/*     */   
/*     */   public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException
/*     */   {
/* 145 */     if (args != null) {
/* 146 */       throw new UnsupportedOperationException("SimpleJndiBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */     
/* 149 */     return (T)getBean(requiredType);
/*     */   }
/*     */   
/*     */   public boolean containsBean(String name)
/*     */   {
/* 154 */     if ((this.singletonObjects.containsKey(name)) || (this.resourceTypes.containsKey(name))) {
/* 155 */       return true;
/*     */     }
/*     */     try {
/* 158 */       doGetType(name);
/* 159 */       return true;
/*     */     }
/*     */     catch (NamingException ex) {}
/* 162 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSingleton(String name)
/*     */     throws NoSuchBeanDefinitionException
/*     */   {
/* 168 */     return this.shareableResources.contains(name);
/*     */   }
/*     */   
/*     */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException
/*     */   {
/* 173 */     return !this.shareableResources.contains(name);
/*     */   }
/*     */   
/*     */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException
/*     */   {
/* 178 */     Class<?> type = getType(name);
/* 179 */     return (type != null) && (typeToMatch.isAssignableFrom(type));
/*     */   }
/*     */   
/*     */   public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException
/*     */   {
/* 184 */     Class<?> type = getType(name);
/* 185 */     return (typeToMatch == null) || ((type != null) && (typeToMatch.isAssignableFrom(type)));
/*     */   }
/*     */   
/*     */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException
/*     */   {
/*     */     try {
/* 191 */       return doGetType(name);
/*     */     }
/*     */     catch (NameNotFoundException ex) {
/* 194 */       throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
/*     */     }
/*     */     catch (NamingException ex) {}
/* 197 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getAliases(String name)
/*     */   {
/* 203 */     return new String[0];
/*     */   }
/*     */   
/*     */   private <T> T doGetSingleton(String name, Class<T> requiredType)
/*     */     throws NamingException
/*     */   {
/* 209 */     synchronized (this.singletonObjects) {
/* 210 */       if (this.singletonObjects.containsKey(name)) {
/* 211 */         Object jndiObject = this.singletonObjects.get(name);
/* 212 */         if ((requiredType != null) && (!requiredType.isInstance(jndiObject)))
/*     */         {
/* 214 */           throw new TypeMismatchNamingException(convertJndiName(name), requiredType, jndiObject != null ? jndiObject.getClass() : null);
/*     */         }
/* 216 */         return (T)jndiObject;
/*     */       }
/* 218 */       T jndiObject = lookup(name, requiredType);
/* 219 */       this.singletonObjects.put(name, jndiObject);
/* 220 */       return jndiObject;
/*     */     }
/*     */   }
/*     */   
/*     */   private Class<?> doGetType(String name) throws NamingException {
/* 225 */     if (isSingleton(name)) {
/* 226 */       Object jndiObject = doGetSingleton(name, null);
/* 227 */       return jndiObject != null ? jndiObject.getClass() : null;
/*     */     }
/*     */     
/* 230 */     synchronized (this.resourceTypes) {
/* 231 */       if (this.resourceTypes.containsKey(name)) {
/* 232 */         return (Class)this.resourceTypes.get(name);
/*     */       }
/*     */       
/* 235 */       Object jndiObject = lookup(name, null);
/* 236 */       Class<?> type = jndiObject != null ? jndiObject.getClass() : null;
/* 237 */       this.resourceTypes.put(name, type);
/* 238 */       return type;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jndi\support\SimpleJndiBeanFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */