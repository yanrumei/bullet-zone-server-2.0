/*    */ package org.springframework.remoting.jaxws;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.SOAPFault;
/*    */ import javax.xml.ws.soap.SOAPFaultException;
/*    */ import org.springframework.remoting.soap.SoapFaultException;
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
/*    */ public class JaxWsSoapFaultException
/*    */   extends SoapFaultException
/*    */ {
/*    */   public JaxWsSoapFaultException(SOAPFaultException original)
/*    */   {
/* 40 */     super(original.getMessage(), original);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final SOAPFault getFault()
/*    */   {
/* 47 */     return ((SOAPFaultException)getCause()).getFault();
/*    */   }
/*    */   
/*    */ 
/*    */   public String getFaultCode()
/*    */   {
/* 53 */     return getFault().getFaultCode();
/*    */   }
/*    */   
/*    */   public QName getFaultCodeAsQName()
/*    */   {
/* 58 */     return getFault().getFaultCodeAsQName();
/*    */   }
/*    */   
/*    */   public String getFaultString()
/*    */   {
/* 63 */     return getFault().getFaultString();
/*    */   }
/*    */   
/*    */   public String getFaultActor()
/*    */   {
/* 68 */     return getFault().getFaultActor();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\jaxws\JaxWsSoapFaultException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */