/*     */ package org.springframework.beans.factory.groovy;
/*     */ 
/*     */ import groovy.lang.GroovyObjectSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GroovyBeanDefinitionWrapper
/*     */   extends GroovyObjectSupport
/*     */ {
/*     */   private static final String PARENT = "parent";
/*     */   private static final String AUTOWIRE = "autowire";
/*     */   private static final String CONSTRUCTOR_ARGS = "constructorArgs";
/*     */   private static final String FACTORY_BEAN = "factoryBean";
/*     */   private static final String FACTORY_METHOD = "factoryMethod";
/*     */   private static final String INIT_METHOD = "initMethod";
/*     */   private static final String DESTROY_METHOD = "destroyMethod";
/*     */   private static final String SINGLETON = "singleton";
/*  55 */   private static final List<String> dynamicProperties = new ArrayList(8);
/*     */   private String beanName;
/*     */   
/*  58 */   static { dynamicProperties.add("parent");
/*  59 */     dynamicProperties.add("autowire");
/*  60 */     dynamicProperties.add("constructorArgs");
/*  61 */     dynamicProperties.add("factoryBean");
/*  62 */     dynamicProperties.add("factoryMethod");
/*  63 */     dynamicProperties.add("initMethod");
/*  64 */     dynamicProperties.add("destroyMethod");
/*  65 */     dynamicProperties.add("singleton");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Class<?> clazz;
/*     */   
/*     */ 
/*     */   private Collection<?> constructorArgs;
/*     */   
/*     */   private AbstractBeanDefinition definition;
/*     */   
/*     */   private BeanWrapper definitionWrapper;
/*     */   
/*     */   private String parentName;
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName)
/*     */   {
/*  83 */     this.beanName = beanName;
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName, Class<?> clazz) {
/*  87 */     this.beanName = beanName;
/*  88 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName, Class<?> clazz, Collection<?> constructorArgs) {
/*  92 */     this.beanName = beanName;
/*  93 */     this.clazz = clazz;
/*  94 */     this.constructorArgs = constructorArgs;
/*     */   }
/*     */   
/*     */   public String getBeanName()
/*     */   {
/*  99 */     return this.beanName;
/*     */   }
/*     */   
/*     */   public void setBeanDefinition(AbstractBeanDefinition definition) {
/* 103 */     this.definition = definition;
/*     */   }
/*     */   
/*     */   public AbstractBeanDefinition getBeanDefinition() {
/* 107 */     if (this.definition == null) {
/* 108 */       this.definition = createBeanDefinition();
/*     */     }
/* 110 */     return this.definition;
/*     */   }
/*     */   
/*     */   protected AbstractBeanDefinition createBeanDefinition() {
/* 114 */     AbstractBeanDefinition bd = new GenericBeanDefinition();
/* 115 */     bd.setBeanClass(this.clazz);
/* 116 */     if (!CollectionUtils.isEmpty(this.constructorArgs)) {
/* 117 */       ConstructorArgumentValues cav = new ConstructorArgumentValues();
/* 118 */       for (Object constructorArg : this.constructorArgs) {
/* 119 */         cav.addGenericArgumentValue(constructorArg);
/*     */       }
/* 121 */       bd.setConstructorArgumentValues(cav);
/*     */     }
/* 123 */     if (this.parentName != null) {
/* 124 */       bd.setParentName(this.parentName);
/*     */     }
/* 126 */     this.definitionWrapper = new BeanWrapperImpl(bd);
/* 127 */     return bd;
/*     */   }
/*     */   
/*     */   public void setBeanDefinitionHolder(BeanDefinitionHolder holder) {
/* 131 */     this.definition = ((AbstractBeanDefinition)holder.getBeanDefinition());
/* 132 */     this.beanName = holder.getBeanName();
/*     */   }
/*     */   
/*     */   public BeanDefinitionHolder getBeanDefinitionHolder() {
/* 136 */     return new BeanDefinitionHolder(getBeanDefinition(), getBeanName());
/*     */   }
/*     */   
/*     */   public void setParent(Object obj) {
/* 140 */     if (obj == null) {
/* 141 */       throw new IllegalArgumentException("Parent bean cannot be set to a null runtime bean reference!");
/*     */     }
/* 143 */     if ((obj instanceof String)) {
/* 144 */       this.parentName = ((String)obj);
/*     */     }
/* 146 */     else if ((obj instanceof RuntimeBeanReference)) {
/* 147 */       this.parentName = ((RuntimeBeanReference)obj).getBeanName();
/*     */     }
/* 149 */     else if ((obj instanceof GroovyBeanDefinitionWrapper)) {
/* 150 */       this.parentName = ((GroovyBeanDefinitionWrapper)obj).getBeanName();
/*     */     }
/* 152 */     getBeanDefinition().setParentName(this.parentName);
/* 153 */     getBeanDefinition().setAbstract(false);
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper addProperty(String propertyName, Object propertyValue) {
/* 157 */     if ((propertyValue instanceof GroovyBeanDefinitionWrapper)) {
/* 158 */       propertyValue = ((GroovyBeanDefinitionWrapper)propertyValue).getBeanDefinition();
/*     */     }
/* 160 */     getBeanDefinition().getPropertyValues().add(propertyName, propertyValue);
/* 161 */     return this;
/*     */   }
/*     */   
/*     */   public Object getProperty(String property)
/*     */   {
/* 166 */     if (this.definitionWrapper.isReadableProperty(property)) {
/* 167 */       return this.definitionWrapper.getPropertyValue(property);
/*     */     }
/* 169 */     if (dynamicProperties.contains(property)) {
/* 170 */       return null;
/*     */     }
/* 172 */     return super.getProperty(property);
/*     */   }
/*     */   
/*     */   public void setProperty(String property, Object newValue) {
/* 176 */     if ("parent".equals(property)) {
/* 177 */       setParent(newValue);
/*     */     }
/*     */     else {
/* 180 */       AbstractBeanDefinition bd = getBeanDefinition();
/* 181 */       if ("autowire".equals(property)) {
/* 182 */         if ("byName".equals(newValue)) {
/* 183 */           bd.setAutowireMode(1);
/*     */         }
/* 185 */         else if ("byType".equals(newValue)) {
/* 186 */           bd.setAutowireMode(2);
/*     */         }
/* 188 */         else if ("constructor".equals(newValue)) {
/* 189 */           bd.setAutowireMode(3);
/*     */         }
/* 191 */         else if (Boolean.TRUE.equals(newValue)) {
/* 192 */           bd.setAutowireMode(1);
/*     */         }
/*     */         
/*     */       }
/* 196 */       else if (("constructorArgs".equals(property)) && ((newValue instanceof List))) {
/* 197 */         ConstructorArgumentValues cav = new ConstructorArgumentValues();
/* 198 */         List args = (List)newValue;
/* 199 */         for (Object arg : args) {
/* 200 */           cav.addGenericArgumentValue(arg);
/*     */         }
/* 202 */         bd.setConstructorArgumentValues(cav);
/*     */ 
/*     */       }
/* 205 */       else if ("factoryBean".equals(property)) {
/* 206 */         if (newValue != null) {
/* 207 */           bd.setFactoryBeanName(newValue.toString());
/*     */         }
/*     */         
/*     */       }
/* 211 */       else if ("factoryMethod".equals(property)) {
/* 212 */         if (newValue != null) {
/* 213 */           bd.setFactoryMethodName(newValue.toString());
/*     */         }
/*     */       }
/* 216 */       else if ("initMethod".equals(property)) {
/* 217 */         if (newValue != null) {
/* 218 */           bd.setInitMethodName(newValue.toString());
/*     */         }
/*     */         
/*     */       }
/* 222 */       else if ("destroyMethod".equals(property)) {
/* 223 */         if (newValue != null) {
/* 224 */           bd.setDestroyMethodName(newValue.toString());
/*     */         }
/*     */         
/*     */       }
/* 228 */       else if ("singleton".equals(property)) {
/* 229 */         bd.setScope(Boolean.TRUE.equals(newValue) ? "singleton" : "prototype");
/*     */ 
/*     */       }
/* 232 */       else if (this.definitionWrapper.isWritableProperty(property)) {
/* 233 */         this.definitionWrapper.setPropertyValue(property, newValue);
/*     */       }
/*     */       else {
/* 236 */         super.setProperty(property, newValue);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\groovy\GroovyBeanDefinitionWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */