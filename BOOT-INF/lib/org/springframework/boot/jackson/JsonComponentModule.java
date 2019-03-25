/*     */ package org.springframework.boot.jackson;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ import javax.annotation.PostConstruct;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.HierarchicalBeanFactory;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.core.ResolvableType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonComponentModule
/*     */   extends SimpleModule
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/*  50 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   public void registerJsonComponents() {
/*  55 */     BeanFactory beanFactory = this.beanFactory;
/*  56 */     while (beanFactory != null) {
/*  57 */       if ((beanFactory instanceof ListableBeanFactory)) {
/*  58 */         addJsonBeans((ListableBeanFactory)beanFactory);
/*     */       }
/*     */       
/*  61 */       beanFactory = (beanFactory instanceof HierarchicalBeanFactory) ? ((HierarchicalBeanFactory)beanFactory).getParentBeanFactory() : null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void addJsonBeans(ListableBeanFactory beanFactory)
/*     */   {
/*  68 */     Map<String, Object> beans = beanFactory.getBeansWithAnnotation(JsonComponent.class);
/*  69 */     for (Object bean : beans.values()) {
/*  70 */       addJsonBean(bean);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addJsonBean(Object bean) {
/*  75 */     if ((bean instanceof JsonSerializer)) {
/*  76 */       addSerializerWithDeducedType((JsonSerializer)bean);
/*     */     }
/*  78 */     if ((bean instanceof JsonDeserializer)) {
/*  79 */       addDeserializerWithDeducedType((JsonDeserializer)bean);
/*     */     }
/*  81 */     for (Class<?> innerClass : bean.getClass().getDeclaredClasses()) {
/*  82 */       if ((!Modifier.isAbstract(innerClass.getModifiers())) && (
/*  83 */         (JsonSerializer.class.isAssignableFrom(innerClass)) || 
/*  84 */         (JsonDeserializer.class.isAssignableFrom(innerClass)))) {
/*     */         try {
/*  86 */           addJsonBean(innerClass.newInstance());
/*     */         }
/*     */         catch (Exception ex) {
/*  89 */           throw new IllegalStateException(ex);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private <T> void addSerializerWithDeducedType(JsonSerializer<T> serializer)
/*     */   {
/*  97 */     ResolvableType type = ResolvableType.forClass(JsonSerializer.class, serializer
/*  98 */       .getClass());
/*  99 */     addSerializer(type.resolveGeneric(new int[0]), serializer);
/*     */   }
/*     */   
/*     */   private <T> void addDeserializerWithDeducedType(JsonDeserializer<T> deserializer)
/*     */   {
/* 104 */     ResolvableType type = ResolvableType.forClass(JsonDeserializer.class, deserializer
/* 105 */       .getClass());
/* 106 */     addDeserializer(type.resolveGeneric(new int[0]), deserializer);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jackson\JsonComponentModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */