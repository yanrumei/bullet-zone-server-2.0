/*    */ package org.springframework.boot.autoconfigure.session;
/*    */ 
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.boot.autoconfigure.AbstractDatabaseInitializer;
/*    */ import org.springframework.core.io.ResourceLoader;
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
/*    */ public class JdbcSessionDatabaseInitializer
/*    */   extends AbstractDatabaseInitializer
/*    */ {
/*    */   private final SessionProperties.Jdbc properties;
/*    */   
/*    */   public JdbcSessionDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader, SessionProperties properties)
/*    */   {
/* 37 */     super(dataSource, resourceLoader);
/* 38 */     Assert.notNull(properties, "SessionProperties must not be null");
/* 39 */     this.properties = properties.getJdbc();
/*    */   }
/*    */   
/*    */   protected boolean isEnabled()
/*    */   {
/* 44 */     return this.properties.getInitializer().isEnabled();
/*    */   }
/*    */   
/*    */   protected String getSchemaLocation()
/*    */   {
/* 49 */     return this.properties.getSchema();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\JdbcSessionDatabaseInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */