/*    */ package org.springframework.boot.autoconfigure.jmx;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.management.MalformedObjectNameException;
/*    */ import javax.management.ObjectName;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.ApplicationContextAware;
/*    */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*    */ import org.springframework.jmx.export.naming.MetadataNamingStrategy;
/*    */ import org.springframework.jmx.support.ObjectNameManager;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParentAwareNamingStrategy
/*    */   extends MetadataNamingStrategy
/*    */   implements ApplicationContextAware
/*    */ {
/*    */   private ApplicationContext applicationContext;
/*    */   private boolean ensureUniqueRuntimeObjectNames;
/*    */   
/*    */   public ParentAwareNamingStrategy(JmxAttributeSource attributeSource)
/*    */   {
/* 47 */     super(attributeSource);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setEnsureUniqueRuntimeObjectNames(boolean ensureUniqueRuntimeObjectNames)
/*    */   {
/* 56 */     this.ensureUniqueRuntimeObjectNames = ensureUniqueRuntimeObjectNames;
/*    */   }
/*    */   
/*    */   public ObjectName getObjectName(Object managedBean, String beanKey)
/*    */     throws MalformedObjectNameException
/*    */   {
/* 62 */     ObjectName name = super.getObjectName(managedBean, beanKey);
/* 63 */     Hashtable<String, String> properties = new Hashtable();
/* 64 */     properties.putAll(name.getKeyPropertyList());
/* 65 */     if (this.ensureUniqueRuntimeObjectNames) {
/* 66 */       properties.put("identity", ObjectUtils.getIdentityHexString(managedBean));
/*    */     }
/* 68 */     else if (parentContextContainsSameBean(this.applicationContext, beanKey)) {
/* 69 */       properties.put("context", 
/* 70 */         ObjectUtils.getIdentityHexString(this.applicationContext));
/*    */     }
/* 72 */     return ObjectNameManager.getInstance(name.getDomain(), properties);
/*    */   }
/*    */   
/*    */   public void setApplicationContext(ApplicationContext applicationContext)
/*    */     throws BeansException
/*    */   {
/* 78 */     this.applicationContext = applicationContext;
/*    */   }
/*    */   
/*    */   private boolean parentContextContainsSameBean(ApplicationContext context, String beanKey)
/*    */   {
/* 83 */     if (context.getParent() == null) {
/* 84 */       return false;
/*    */     }
/*    */     try {
/* 87 */       this.applicationContext.getParent().getBean(beanKey);
/* 88 */       return true;
/*    */     }
/*    */     catch (BeansException ex) {}
/* 91 */     return parentContextContainsSameBean(context.getParent(), beanKey);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jmx\ParentAwareNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */