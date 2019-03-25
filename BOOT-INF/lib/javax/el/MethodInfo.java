/*    */ package javax.el;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodInfo
/*    */ {
/*    */   private final String name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private final Class<?>[] paramTypes;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private final Class<?> returnType;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MethodInfo(String name, Class<?> returnType, Class<?>[] paramTypes)
/*    */   {
/* 28 */     this.name = name;
/* 29 */     this.returnType = returnType;
/* 30 */     this.paramTypes = paramTypes;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 34 */     return this.name;
/*    */   }
/*    */   
/*    */   public Class<?> getReturnType() {
/* 38 */     return this.returnType;
/*    */   }
/*    */   
/*    */   public Class<?>[] getParamTypes() {
/* 42 */     return this.paramTypes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\MethodInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */