/*    */ package ch.qos.logback.core.db;
/*    */ 
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.joran.util.PropertySetter;
/*    */ import ch.qos.logback.core.joran.util.beans.BeanDescriptionCache;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.sql.DataSource;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class BindDataSourceToJNDIAction
/*    */   extends Action
/*    */ {
/*    */   static final String DATA_SOURCE_CLASS = "dataSourceClass";
/*    */   static final String URL = "url";
/*    */   static final String USER = "user";
/*    */   static final String PASSWORD = "password";
/*    */   private final BeanDescriptionCache beanDescriptionCache;
/*    */   
/*    */   public BindDataSourceToJNDIAction(BeanDescriptionCache beanDescriptionCache)
/*    */   {
/* 42 */     this.beanDescriptionCache = beanDescriptionCache;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*    */   {
/* 50 */     String dsClassName = ec.getProperty("dataSourceClass");
/*    */     
/* 52 */     if (OptionHelper.isEmpty(dsClassName)) {
/* 53 */       addWarn("dsClassName is a required parameter");
/* 54 */       ec.addError("dsClassName is a required parameter");
/*    */       
/* 56 */       return;
/*    */     }
/*    */     
/* 59 */     String urlStr = ec.getProperty("url");
/* 60 */     String userStr = ec.getProperty("user");
/* 61 */     String passwordStr = ec.getProperty("password");
/*    */     try
/*    */     {
/* 64 */       DataSource ds = (DataSource)OptionHelper.instantiateByClassName(dsClassName, DataSource.class, this.context);
/*    */       
/* 66 */       PropertySetter setter = new PropertySetter(this.beanDescriptionCache, ds);
/* 67 */       setter.setContext(this.context);
/*    */       
/* 69 */       if (!OptionHelper.isEmpty(urlStr)) {
/* 70 */         setter.setProperty("url", urlStr);
/*    */       }
/*    */       
/* 73 */       if (!OptionHelper.isEmpty(userStr)) {
/* 74 */         setter.setProperty("user", userStr);
/*    */       }
/*    */       
/* 77 */       if (!OptionHelper.isEmpty(passwordStr)) {
/* 78 */         setter.setProperty("password", passwordStr);
/*    */       }
/*    */       
/* 81 */       Context ctx = new InitialContext();
/* 82 */       ctx.rebind("dataSource", ds);
/*    */     } catch (Exception oops) {
/* 84 */       addError("Could not bind  datasource. Reported error follows.", oops);
/* 85 */       ec.addError("Could not not bind  datasource of type [" + dsClassName + "].");
/*    */     }
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String name) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\BindDataSourceToJNDIAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */