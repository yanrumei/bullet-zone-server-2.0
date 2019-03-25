/*    */ package org.apache.tomcat.util.descriptor;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.tomcat.util.res.StringManager;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class XmlErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/* 31 */   private static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*    */   
/* 33 */   private final List<SAXParseException> errors = new ArrayList();
/*    */   
/* 35 */   private final List<SAXParseException> warnings = new ArrayList();
/*    */   
/*    */   public void error(SAXParseException exception)
/*    */     throws SAXException
/*    */   {
/* 40 */     this.errors.add(exception);
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException exception)
/*    */     throws SAXException
/*    */   {
/* 46 */     throw exception;
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException exception)
/*    */     throws SAXException
/*    */   {
/* 52 */     this.warnings.add(exception);
/*    */   }
/*    */   
/*    */   public List<SAXParseException> getErrors()
/*    */   {
/* 57 */     return this.errors;
/*    */   }
/*    */   
/*    */   public List<SAXParseException> getWarnings()
/*    */   {
/* 62 */     return this.warnings;
/*    */   }
/*    */   
/*    */   public void logFindings(Log log, String source) {
/* 66 */     for (SAXParseException e : getWarnings()) {
/* 67 */       log.warn(sm.getString("xmlErrorHandler.warning", new Object[] {e
/* 68 */         .getMessage(), source }));
/*    */     }
/* 70 */     for (SAXParseException e : getErrors()) {
/* 71 */       log.warn(sm.getString("xmlErrorHandler.error", new Object[] {e
/* 72 */         .getMessage(), source }));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\XmlErrorHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */