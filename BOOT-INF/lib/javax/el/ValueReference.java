/*    */ package javax.el;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class ValueReference
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final Object base;
/*    */   private final Object property;
/*    */   
/*    */   public ValueReference(Object base, Object property)
/*    */   {
/* 33 */     this.base = base;
/* 34 */     this.property = property;
/*    */   }
/*    */   
/*    */   public Object getBase() {
/* 38 */     return this.base;
/*    */   }
/*    */   
/*    */   public Object getProperty() {
/* 42 */     return this.property;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ValueReference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */