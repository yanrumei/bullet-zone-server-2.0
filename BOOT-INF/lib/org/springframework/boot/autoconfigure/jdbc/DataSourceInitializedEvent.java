/*    */ package org.springframework.boot.autoconfigure.jdbc;
/*    */ 
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.context.ApplicationEvent;
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
/*    */ public class DataSourceInitializedEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   public DataSourceInitializedEvent(DataSource source)
/*    */   {
/* 40 */     super(source);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceInitializedEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */