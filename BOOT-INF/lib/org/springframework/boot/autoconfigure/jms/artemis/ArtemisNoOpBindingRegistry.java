/*    */ package org.springframework.boot.autoconfigure.jms.artemis;
/*    */ 
/*    */ import org.apache.activemq.artemis.spi.core.naming.BindingRegistry;
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
/*    */ public class ArtemisNoOpBindingRegistry
/*    */   implements BindingRegistry
/*    */ {
/*    */   public Object lookup(String s)
/*    */   {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   public boolean bind(String s, Object o)
/*    */   {
/* 37 */     return false;
/*    */   }
/*    */   
/*    */   public void unbind(String s) {}
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\artemis\ArtemisNoOpBindingRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */