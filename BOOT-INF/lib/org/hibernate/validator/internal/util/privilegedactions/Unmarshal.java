/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ import javax.xml.bind.JAXBElement;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.Unmarshaller;
/*    */ import javax.xml.stream.XMLEventReader;
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
/*    */ public final class Unmarshal<T>
/*    */   implements PrivilegedExceptionAction<JAXBElement<T>>
/*    */ {
/*    */   private final Unmarshaller unmarshaller;
/*    */   private final XMLEventReader xmlEventReader;
/*    */   private final Class<T> clazz;
/*    */   
/*    */   public static <T> Unmarshal<T> action(Unmarshaller unmarshaller, XMLEventReader xmlEventReader, Class<T> clazz)
/*    */   {
/* 27 */     return new Unmarshal(unmarshaller, xmlEventReader, clazz);
/*    */   }
/*    */   
/*    */   private Unmarshal(Unmarshaller unmarshaller, XMLEventReader xmlEventReader, Class<T> clazz) {
/* 31 */     this.unmarshaller = unmarshaller;
/* 32 */     this.xmlEventReader = xmlEventReader;
/* 33 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public JAXBElement<T> run() throws JAXBException
/*    */   {
/* 38 */     return this.unmarshaller.unmarshal(this.xmlEventReader, this.clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\Unmarshal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */