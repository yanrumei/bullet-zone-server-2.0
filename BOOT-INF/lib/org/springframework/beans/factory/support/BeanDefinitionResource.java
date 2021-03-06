/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.core.io.AbstractResource;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class BeanDefinitionResource
/*    */   extends AbstractResource
/*    */ {
/*    */   private final BeanDefinition beanDefinition;
/*    */   
/*    */   public BeanDefinitionResource(BeanDefinition beanDefinition)
/*    */   {
/* 45 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/* 46 */     this.beanDefinition = beanDefinition;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final BeanDefinition getBeanDefinition()
/*    */   {
/* 53 */     return this.beanDefinition;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean exists()
/*    */   {
/* 59 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isReadable()
/*    */   {
/* 64 */     return false;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream()
/*    */     throws IOException
/*    */   {
/* 70 */     throw new FileNotFoundException("Resource cannot be opened because it points to " + getDescription());
/*    */   }
/*    */   
/*    */   public String getDescription()
/*    */   {
/* 75 */     return "BeanDefinition defined in " + this.beanDefinition.getResourceDescription();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 84 */     if (obj != this) if (!(obj instanceof BeanDefinitionResource)) break label33; label33: return ((BeanDefinitionResource)obj).beanDefinition
/*    */     
/* 86 */       .equals(this.beanDefinition);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 94 */     return this.beanDefinition.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\BeanDefinitionResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */