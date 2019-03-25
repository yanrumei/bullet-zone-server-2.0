/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.bind.JAXBException;
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
/*    */ public final class NewJaxbContext
/*    */   implements PrivilegedExceptionAction<JAXBContext>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   
/*    */   public static NewJaxbContext action(Class<?> clazz)
/*    */   {
/* 24 */     return new NewJaxbContext(clazz);
/*    */   }
/*    */   
/*    */   private NewJaxbContext(Class<?> clazz) {
/* 28 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public JAXBContext run() throws JAXBException
/*    */   {
/* 33 */     return JAXBContext.newInstance(new Class[] { this.clazz });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\NewJaxbContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */