/*    */ package javax.el;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
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
/*    */ public class ELManager
/*    */ {
/* 27 */   private StandardELContext context = null;
/*    */   
/*    */   public static ExpressionFactory getExpressionFactory() {
/* 30 */     return Util.getExpressionFactory();
/*    */   }
/*    */   
/*    */   public StandardELContext getELContext() {
/* 34 */     if (this.context == null) {
/* 35 */       this.context = new StandardELContext(getExpressionFactory());
/*    */     }
/*    */     
/* 38 */     return this.context;
/*    */   }
/*    */   
/*    */   public ELContext setELContext(ELContext context) {
/* 42 */     StandardELContext oldContext = this.context;
/* 43 */     this.context = new StandardELContext(context);
/* 44 */     return oldContext;
/*    */   }
/*    */   
/*    */   public void addBeanNameResolver(BeanNameResolver beanNameResolver) {
/* 48 */     getELContext().addELResolver(new BeanNameELResolver(beanNameResolver));
/*    */   }
/*    */   
/*    */   public void addELResolver(ELResolver resolver) {
/* 52 */     getELContext().addELResolver(resolver);
/*    */   }
/*    */   
/*    */   public void mapFunction(String prefix, String function, Method method) {
/* 56 */     getELContext().getFunctionMapper().mapFunction(prefix, function, method);
/*    */   }
/*    */   
/*    */   public void setVariable(String variable, ValueExpression expression)
/*    */   {
/* 61 */     getELContext().getVariableMapper().setVariable(variable, expression);
/*    */   }
/*    */   
/*    */   public void importStatic(String staticMemberName) throws ELException
/*    */   {
/* 66 */     getELContext().getImportHandler().importStatic(staticMemberName);
/*    */   }
/*    */   
/*    */   public void importClass(String className) throws ELException {
/* 70 */     getELContext().getImportHandler().importClass(className);
/*    */   }
/*    */   
/*    */   public void importPackage(String packageName) {
/* 74 */     getELContext().getImportHandler().importPackage(packageName);
/*    */   }
/*    */   
/*    */   public Object defineBean(String name, Object bean) {
/* 78 */     Map<String, Object> localBeans = getELContext().getLocalBeans();
/*    */     
/* 80 */     if (bean == null) {
/* 81 */       return localBeans.remove(name);
/*    */     }
/* 83 */     return localBeans.put(name, bean);
/*    */   }
/*    */   
/*    */   public void addEvaluationListener(EvaluationListener listener)
/*    */   {
/* 88 */     getELContext().addEvaluationListener(listener);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ELManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */