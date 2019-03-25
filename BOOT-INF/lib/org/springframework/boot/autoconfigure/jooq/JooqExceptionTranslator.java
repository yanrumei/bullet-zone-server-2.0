/*    */ package org.springframework.boot.autoconfigure.jooq;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.jooq.Configuration;
/*    */ import org.jooq.ExecuteContext;
/*    */ import org.jooq.SQLDialect;
/*    */ import org.jooq.SQLDialect.ThirdParty;
/*    */ import org.jooq.impl.DefaultExecuteListener;
/*    */ import org.springframework.dao.DataAccessException;
/*    */ import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
/*    */ import org.springframework.jdbc.support.SQLExceptionTranslator;
/*    */ import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
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
/*    */ public class JooqExceptionTranslator
/*    */   extends DefaultExecuteListener
/*    */ {
/* 46 */   private static final Log logger = LogFactory.getLog(JooqExceptionTranslator.class);
/*    */   
/*    */   public void exception(ExecuteContext context)
/*    */   {
/* 50 */     SQLExceptionTranslator translator = getTranslator(context);
/*    */     
/*    */ 
/* 53 */     SQLException exception = context.sqlException();
/* 54 */     while (exception != null) {
/* 55 */       handle(context, translator, exception);
/* 56 */       exception = exception.getNextException();
/*    */     }
/*    */   }
/*    */   
/*    */   private SQLExceptionTranslator getTranslator(ExecuteContext context) {
/* 61 */     SQLDialect dialect = context.configuration().dialect();
/* 62 */     if ((dialect != null) && (dialect.thirdParty() != null)) {
/* 63 */       String dbName = dialect.thirdParty().springDbName();
/* 64 */       if (dbName != null) {
/* 65 */         return new SQLErrorCodeSQLExceptionTranslator(dbName);
/*    */       }
/*    */     }
/* 68 */     return new SQLStateSQLExceptionTranslator();
/*    */   }
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
/*    */   private void handle(ExecuteContext context, SQLExceptionTranslator translator, SQLException exception)
/*    */   {
/* 82 */     DataAccessException translated = translate(context, translator, exception);
/* 83 */     if (exception.getNextException() == null) {
/* 84 */       context.exception(translated);
/*    */     }
/*    */     else {
/* 87 */       logger.error("Execution of SQL statement failed.", translated);
/*    */     }
/*    */   }
/*    */   
/*    */   private DataAccessException translate(ExecuteContext context, SQLExceptionTranslator translator, SQLException exception)
/*    */   {
/* 93 */     return translator.translate("jOOQ", context.sql(), exception);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jooq\JooqExceptionTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */