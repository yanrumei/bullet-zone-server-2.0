/*    */ package org.apache.el.lang;
/*    */ 
/*    */ import java.io.Externalizable;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInput;
/*    */ import java.io.ObjectOutput;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.el.ValueExpression;
/*    */ import javax.el.VariableMapper;
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
/*    */ public class VariableMapperImpl
/*    */   extends VariableMapper
/*    */   implements Externalizable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 34 */   private Map<String, ValueExpression> vars = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ValueExpression resolveVariable(String variable)
/*    */   {
/* 42 */     return (ValueExpression)this.vars.get(variable);
/*    */   }
/*    */   
/*    */ 
/*    */   public ValueExpression setVariable(String variable, ValueExpression expression)
/*    */   {
/* 48 */     if (expression == null) {
/* 49 */       return (ValueExpression)this.vars.remove(variable);
/*    */     }
/* 51 */     return (ValueExpression)this.vars.put(variable, expression);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readExternal(ObjectInput in)
/*    */     throws IOException, ClassNotFoundException
/*    */   {
/* 59 */     this.vars = ((Map)in.readObject());
/*    */   }
/*    */   
/*    */   public void writeExternal(ObjectOutput out) throws IOException
/*    */   {
/* 64 */     out.writeObject(this.vars);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\lang\VariableMapperImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */