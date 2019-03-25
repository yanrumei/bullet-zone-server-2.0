/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InjectionMetadata
/*     */ {
/*  49 */   private static final Log logger = LogFactory.getLog(InjectionMetadata.class);
/*     */   
/*     */   private final Class<?> targetClass;
/*     */   
/*     */   private final Collection<InjectedElement> injectedElements;
/*     */   
/*     */   private volatile Set<InjectedElement> checkedElements;
/*     */   
/*     */   public InjectionMetadata(Class<?> targetClass, Collection<InjectedElement> elements)
/*     */   {
/*  59 */     this.targetClass = targetClass;
/*  60 */     this.injectedElements = elements;
/*     */   }
/*     */   
/*     */   public void checkConfigMembers(RootBeanDefinition beanDefinition)
/*     */   {
/*  65 */     Set<InjectedElement> checkedElements = new LinkedHashSet(this.injectedElements.size());
/*  66 */     for (InjectedElement element : this.injectedElements) {
/*  67 */       Member member = element.getMember();
/*  68 */       if (!beanDefinition.isExternallyManagedConfigMember(member)) {
/*  69 */         beanDefinition.registerExternallyManagedConfigMember(member);
/*  70 */         checkedElements.add(element);
/*  71 */         if (logger.isDebugEnabled()) {
/*  72 */           logger.debug("Registered injected element on class [" + this.targetClass.getName() + "]: " + element);
/*     */         }
/*     */       }
/*     */     }
/*  76 */     this.checkedElements = checkedElements;
/*     */   }
/*     */   
/*     */   public void inject(Object target, String beanName, PropertyValues pvs) throws Throwable {
/*  80 */     Collection<InjectedElement> elementsToIterate = this.checkedElements != null ? this.checkedElements : this.injectedElements;
/*     */     boolean debug;
/*  82 */     if (!elementsToIterate.isEmpty()) {
/*  83 */       debug = logger.isDebugEnabled();
/*  84 */       for (InjectedElement element : elementsToIterate) {
/*  85 */         if (debug) {
/*  86 */           logger.debug("Processing injected element of bean '" + beanName + "': " + element);
/*     */         }
/*  88 */         element.inject(target, beanName, pvs);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear(PropertyValues pvs)
/*     */   {
/*  97 */     Collection<InjectedElement> elementsToIterate = this.checkedElements != null ? this.checkedElements : this.injectedElements;
/*     */     
/*  99 */     if (!elementsToIterate.isEmpty()) {
/* 100 */       for (InjectedElement element : elementsToIterate) {
/* 101 */         element.clearPropertySkipping(pvs);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean needsRefresh(InjectionMetadata metadata, Class<?> clazz)
/*     */   {
/* 108 */     return (metadata == null) || (metadata.targetClass != clazz);
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract class InjectedElement
/*     */   {
/*     */     protected final Member member;
/*     */     
/*     */     protected final boolean isField;
/*     */     
/*     */     protected final PropertyDescriptor pd;
/*     */     protected volatile Boolean skip;
/*     */     
/*     */     protected InjectedElement(Member member, PropertyDescriptor pd)
/*     */     {
/* 123 */       this.member = member;
/* 124 */       this.isField = (member instanceof Field);
/* 125 */       this.pd = pd;
/*     */     }
/*     */     
/*     */     public final Member getMember() {
/* 129 */       return this.member;
/*     */     }
/*     */     
/*     */     protected final Class<?> getResourceType() {
/* 133 */       if (this.isField) {
/* 134 */         return ((Field)this.member).getType();
/*     */       }
/* 136 */       if (this.pd != null) {
/* 137 */         return this.pd.getPropertyType();
/*     */       }
/*     */       
/* 140 */       return ((Method)this.member).getParameterTypes()[0];
/*     */     }
/*     */     
/*     */     protected final void checkResourceType(Class<?> resourceType)
/*     */     {
/* 145 */       if (this.isField) {
/* 146 */         Class<?> fieldType = ((Field)this.member).getType();
/* 147 */         if ((!resourceType.isAssignableFrom(fieldType)) && (!fieldType.isAssignableFrom(resourceType)))
/*     */         {
/* 149 */           throw new IllegalStateException("Specified field type [" + fieldType + "] is incompatible with resource type [" + resourceType.getName() + "]");
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 154 */         Class<?> paramType = this.pd != null ? this.pd.getPropertyType() : ((Method)this.member).getParameterTypes()[0];
/* 155 */         if ((!resourceType.isAssignableFrom(paramType)) && (!paramType.isAssignableFrom(resourceType)))
/*     */         {
/* 157 */           throw new IllegalStateException("Specified parameter type [" + paramType + "] is incompatible with resource type [" + resourceType.getName() + "]");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     protected void inject(Object target, String requestingBeanName, PropertyValues pvs)
/*     */       throws Throwable
/*     */     {
/* 166 */       if (this.isField) {
/* 167 */         Field field = (Field)this.member;
/* 168 */         ReflectionUtils.makeAccessible(field);
/* 169 */         field.set(target, getResourceToInject(target, requestingBeanName));
/*     */       }
/*     */       else {
/* 172 */         if (checkPropertySkipping(pvs)) {
/* 173 */           return;
/*     */         }
/*     */         try {
/* 176 */           Method method = (Method)this.member;
/* 177 */           ReflectionUtils.makeAccessible(method);
/* 178 */           method.invoke(target, new Object[] { getResourceToInject(target, requestingBeanName) });
/*     */         }
/*     */         catch (InvocationTargetException ex) {
/* 181 */           throw ex.getTargetException();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean checkPropertySkipping(PropertyValues pvs)
/*     */     {
/* 192 */       if (this.skip != null) {
/* 193 */         return this.skip.booleanValue();
/*     */       }
/* 195 */       if (pvs == null) {
/* 196 */         this.skip = Boolean.valueOf(false);
/* 197 */         return false;
/*     */       }
/* 199 */       synchronized (pvs) {
/* 200 */         if (this.skip != null) {
/* 201 */           return this.skip.booleanValue();
/*     */         }
/* 203 */         if (this.pd != null) {
/* 204 */           if (pvs.contains(this.pd.getName()))
/*     */           {
/* 206 */             this.skip = Boolean.valueOf(true);
/* 207 */             return true;
/*     */           }
/* 209 */           if ((pvs instanceof MutablePropertyValues)) {
/* 210 */             ((MutablePropertyValues)pvs).registerProcessedProperty(this.pd.getName());
/*     */           }
/*     */         }
/* 213 */         this.skip = Boolean.valueOf(false);
/* 214 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void clearPropertySkipping(PropertyValues pvs)
/*     */     {
/* 222 */       if (pvs == null) {
/* 223 */         return;
/*     */       }
/* 225 */       synchronized (pvs) {
/* 226 */         if ((Boolean.FALSE.equals(this.skip)) && (this.pd != null) && ((pvs instanceof MutablePropertyValues))) {
/* 227 */           ((MutablePropertyValues)pvs).clearProcessedProperty(this.pd.getName());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected Object getResourceToInject(Object target, String requestingBeanName)
/*     */     {
/* 236 */       return null;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 241 */       if (this == other) {
/* 242 */         return true;
/*     */       }
/* 244 */       if (!(other instanceof InjectedElement)) {
/* 245 */         return false;
/*     */       }
/* 247 */       InjectedElement otherElement = (InjectedElement)other;
/* 248 */       return this.member.equals(otherElement.member);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 253 */       return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 258 */       return getClass().getSimpleName() + " for " + this.member;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\annotation\InjectionMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */