/*    */ package org.springframework.web.bind;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
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
/*    */ public class MissingPathVariableException
/*    */   extends ServletRequestBindingException
/*    */ {
/*    */   private final String variableName;
/*    */   private final MethodParameter parameter;
/*    */   
/*    */   public MissingPathVariableException(String variableName, MethodParameter parameter)
/*    */   {
/* 45 */     super("");
/* 46 */     this.variableName = variableName;
/* 47 */     this.parameter = parameter;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 53 */     return 
/* 54 */       "Missing URI template variable '" + this.variableName + "' for method parameter of type " + this.parameter.getParameterType().getSimpleName();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final String getVariableName()
/*    */   {
/* 61 */     return this.variableName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final MethodParameter getParameter()
/*    */   {
/* 68 */     return this.parameter;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\MissingPathVariableException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */