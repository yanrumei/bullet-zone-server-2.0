/*     */ package org.apache.el;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.MethodExpression;
/*     */ import javax.el.MethodInfo;
/*     */ import org.apache.el.util.ReflectionUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodExpressionLiteral
/*     */   extends MethodExpression
/*     */   implements Externalizable
/*     */ {
/*     */   private Class<?> expectedType;
/*     */   private String expr;
/*     */   private Class<?>[] paramTypes;
/*     */   
/*     */   public MethodExpressionLiteral() {}
/*     */   
/*     */   public MethodExpressionLiteral(String expr, Class<?> expectedType, Class<?>[] paramTypes)
/*     */   {
/*  47 */     this.expr = expr;
/*  48 */     this.expectedType = expectedType;
/*  49 */     this.paramTypes = paramTypes;
/*     */   }
/*     */   
/*     */   public MethodInfo getMethodInfo(ELContext context) throws ELException
/*     */   {
/*  54 */     context.notifyBeforeEvaluation(getExpressionString());
/*  55 */     MethodInfo result = new MethodInfo(this.expr, this.expectedType, this.paramTypes);
/*     */     
/*  57 */     context.notifyAfterEvaluation(getExpressionString());
/*  58 */     return result;
/*     */   }
/*     */   
/*     */   public Object invoke(ELContext context, Object[] params) throws ELException
/*     */   {
/*  63 */     context.notifyBeforeEvaluation(getExpressionString());
/*     */     Object result;
/*  65 */     Object result; if (this.expectedType != null) {
/*  66 */       result = context.convertToType(this.expr, this.expectedType);
/*     */     } else {
/*  68 */       result = this.expr;
/*     */     }
/*  70 */     context.notifyAfterEvaluation(getExpressionString());
/*  71 */     return result;
/*     */   }
/*     */   
/*     */   public String getExpressionString()
/*     */   {
/*  76 */     return this.expr;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  81 */     return ((obj instanceof MethodExpressionLiteral)) && (hashCode() == obj.hashCode());
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  86 */     return this.expr.hashCode();
/*     */   }
/*     */   
/*     */   public boolean isLiteralText()
/*     */   {
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
/*     */   {
/*  96 */     this.expr = in.readUTF();
/*  97 */     String type = in.readUTF();
/*  98 */     if (!"".equals(type)) {
/*  99 */       this.expectedType = ReflectionUtil.forName(type);
/*     */     }
/* 101 */     this.paramTypes = ReflectionUtil.toTypeArray(
/* 102 */       (String[])in.readObject());
/*     */   }
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException
/*     */   {
/* 107 */     out.writeUTF(this.expr);
/* 108 */     out.writeUTF(this.expectedType != null ? this.expectedType.getName() : "");
/*     */     
/* 110 */     out.writeObject(ReflectionUtil.toTypeNameArray(this.paramTypes));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\MethodExpressionLiteral.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */