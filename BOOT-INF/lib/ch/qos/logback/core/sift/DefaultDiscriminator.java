/*    */ package ch.qos.logback.core.sift;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultDiscriminator<E>
/*    */   extends AbstractDiscriminator<E>
/*    */ {
/*    */   public static final String DEFAULT = "default";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDiscriminatingValue(E e)
/*    */   {
/* 24 */     return "default";
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 28 */     return "default";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\sift\DefaultDiscriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */