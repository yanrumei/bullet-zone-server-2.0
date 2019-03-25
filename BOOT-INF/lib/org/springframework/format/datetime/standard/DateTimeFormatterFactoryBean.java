/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
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
/*    */ 
/*    */ public class DateTimeFormatterFactoryBean
/*    */   extends DateTimeFormatterFactory
/*    */   implements FactoryBean<DateTimeFormatter>, InitializingBean
/*    */ {
/*    */   private DateTimeFormatter dateTimeFormatter;
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 44 */     this.dateTimeFormatter = createDateTimeFormatter();
/*    */   }
/*    */   
/*    */   public DateTimeFormatter getObject()
/*    */   {
/* 49 */     return this.dateTimeFormatter;
/*    */   }
/*    */   
/*    */   public Class<?> getObjectType()
/*    */   {
/* 54 */     return DateTimeFormatter.class;
/*    */   }
/*    */   
/*    */   public boolean isSingleton()
/*    */   {
/* 59 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\standard\DateTimeFormatterFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */