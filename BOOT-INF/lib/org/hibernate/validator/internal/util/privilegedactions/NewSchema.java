/*    */ package org.hibernate.validator.internal.util.privilegedactions;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ import javax.xml.validation.Schema;
/*    */ import javax.xml.validation.SchemaFactory;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public final class NewSchema
/*    */   implements PrivilegedExceptionAction<Schema>
/*    */ {
/*    */   private final SchemaFactory schemaFactory;
/*    */   private final URL url;
/*    */   
/*    */   public static NewSchema action(SchemaFactory schemaFactory, URL url)
/*    */   {
/* 28 */     return new NewSchema(schemaFactory, url);
/*    */   }
/*    */   
/*    */   public NewSchema(SchemaFactory schemaFactory, URL url) {
/* 32 */     this.schemaFactory = schemaFactory;
/* 33 */     this.url = url;
/*    */   }
/*    */   
/*    */   public Schema run() throws SAXException
/*    */   {
/* 38 */     return this.schemaFactory.newSchema(this.url);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\NewSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */