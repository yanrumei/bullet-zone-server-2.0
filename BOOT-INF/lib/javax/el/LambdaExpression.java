/*    */ package javax.el;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
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
/*    */ public class LambdaExpression
/*    */ {
/*    */   private final List<String> formalParameters;
/*    */   private final ValueExpression expression;
/* 28 */   private final Map<String, Object> nestedArguments = new HashMap();
/* 29 */   private ELContext context = null;
/*    */   
/*    */   public LambdaExpression(List<String> formalParameters, ValueExpression expression)
/*    */   {
/* 33 */     this.formalParameters = formalParameters;
/* 34 */     this.expression = expression;
/*    */   }
/*    */   
/*    */   public void setELContext(ELContext context)
/*    */   {
/* 39 */     this.context = context;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object invoke(ELContext context, Object... args)
/*    */     throws ELException
/*    */   {
/* 46 */     Objects.requireNonNull(context);
/*    */     
/* 48 */     int formalParamCount = 0;
/* 49 */     if (this.formalParameters != null) {
/* 50 */       formalParamCount = this.formalParameters.size();
/*    */     }
/*    */     
/* 53 */     int argCount = 0;
/* 54 */     if (args != null) {
/* 55 */       argCount = args.length;
/*    */     }
/*    */     
/* 58 */     if (formalParamCount > argCount) {
/* 59 */       throw new ELException(Util.message(context, "lambdaExpression.tooFewArgs", new Object[] {
/*    */       
/* 61 */         Integer.valueOf(argCount), 
/* 62 */         Integer.valueOf(formalParamCount) }));
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 68 */     Map<String, Object> lambdaArguments = new HashMap();
/* 69 */     lambdaArguments.putAll(this.nestedArguments);
/* 70 */     for (int i = 0; i < formalParamCount; i++) {
/* 71 */       lambdaArguments.put(this.formalParameters.get(i), args[i]);
/*    */     }
/*    */     
/* 74 */     context.enterLambdaScope(lambdaArguments);
/*    */     try
/*    */     {
/* 77 */       Object result = this.expression.getValue(context);
/*    */       
/*    */ 
/* 80 */       if ((result instanceof LambdaExpression)) {
/* 81 */         ((LambdaExpression)result).nestedArguments.putAll(lambdaArguments);
/*    */       }
/*    */       
/* 84 */       return result;
/*    */     } finally {
/* 86 */       context.exitLambdaScope();
/*    */     }
/*    */   }
/*    */   
/*    */   public Object invoke(Object... args) {
/* 91 */     return invoke(this.context, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\LambdaExpression.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */