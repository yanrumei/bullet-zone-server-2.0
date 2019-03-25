/*    */ package org.apache.catalina;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ContainerEvent
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final Object data;
/*    */   private final String type;
/*    */   
/*    */   public ContainerEvent(Container container, String type, Object data)
/*    */   {
/* 51 */     super(container);
/* 52 */     this.type = type;
/* 53 */     this.data = data;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object getData()
/*    */   {
/* 63 */     return this.data;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Container getContainer()
/*    */   {
/* 73 */     return (Container)getSource();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getType()
/*    */   {
/* 85 */     return this.type;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 94 */     return 
/* 95 */       "ContainerEvent['" + getContainer() + "','" + getType() + "','" + getData() + "']";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ContainerEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */