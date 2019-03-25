/*     */ package org.apache.el;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.FunctionMapper;
/*     */ import javax.el.MethodExpression;
/*     */ import javax.el.MethodInfo;
/*     */ import javax.el.MethodNotFoundException;
/*     */ import javax.el.PropertyNotFoundException;
/*     */ import javax.el.VariableMapper;
/*     */ import org.apache.el.lang.EvaluationContext;
/*     */ import org.apache.el.lang.ExpressionBuilder;
/*     */ import org.apache.el.parser.Node;
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
/*     */ public final class MethodExpressionImpl
/*     */   extends MethodExpression
/*     */   implements Externalizable
/*     */ {
/*     */   private Class<?> expectedType;
/*     */   private String expr;
/*     */   private FunctionMapper fnMapper;
/*     */   private VariableMapper varMapper;
/*     */   private transient Node node;
/*     */   private Class<?>[] paramTypes;
/*     */   
/*     */   public MethodExpressionImpl() {}
/*     */   
/*     */   public MethodExpressionImpl(String expr, Node node, FunctionMapper fnMapper, VariableMapper varMapper, Class<?> expectedType, Class<?>[] paramTypes)
/*     */   {
/*  99 */     this.expr = expr;
/* 100 */     this.node = node;
/* 101 */     this.fnMapper = fnMapper;
/* 102 */     this.varMapper = varMapper;
/* 103 */     this.expectedType = expectedType;
/* 104 */     this.paramTypes = paramTypes;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 136 */     return ((obj instanceof MethodExpressionImpl)) && 
/* 137 */       (obj.hashCode() == hashCode());
/*     */   }
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
/*     */   public String getExpressionString()
/*     */   {
/* 163 */     return this.expr;
/*     */   }
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
/*     */ 
/*     */   public MethodInfo getMethodInfo(ELContext context)
/*     */     throws PropertyNotFoundException, MethodNotFoundException, ELException
/*     */   {
/* 192 */     Node n = getNode();
/* 193 */     EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
/*     */     
/* 195 */     ctx.notifyBeforeEvaluation(getExpressionString());
/* 196 */     MethodInfo result = n.getMethodInfo(ctx, this.paramTypes);
/* 197 */     ctx.notifyAfterEvaluation(getExpressionString());
/* 198 */     return result;
/*     */   }
/*     */   
/*     */   private Node getNode() throws ELException {
/* 202 */     if (this.node == null) {
/* 203 */       this.node = ExpressionBuilder.createNode(this.expr);
/*     */     }
/* 205 */     return this.node;
/*     */   }
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
/*     */   public int hashCode()
/*     */   {
/* 227 */     return this.expr.hashCode();
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object invoke(ELContext context, Object[] params)
/*     */     throws PropertyNotFoundException, MethodNotFoundException, ELException
/*     */   {
/* 264 */     EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
/*     */     
/* 266 */     ctx.notifyBeforeEvaluation(getExpressionString());
/* 267 */     Object result = getNode().invoke(ctx, this.paramTypes, params);
/* 268 */     ctx.notifyAfterEvaluation(getExpressionString());
/* 269 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readExternal(ObjectInput in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 280 */     this.expr = in.readUTF();
/* 281 */     String type = in.readUTF();
/* 282 */     if (!"".equals(type)) {
/* 283 */       this.expectedType = ReflectionUtil.forName(type);
/*     */     }
/* 285 */     this.paramTypes = ReflectionUtil.toTypeArray(
/* 286 */       (String[])in.readObject());
/* 287 */     this.fnMapper = ((FunctionMapper)in.readObject());
/* 288 */     this.varMapper = ((VariableMapper)in.readObject());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeExternal(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 298 */     out.writeUTF(this.expr);
/* 299 */     out.writeUTF(this.expectedType != null ? this.expectedType.getName() : "");
/*     */     
/* 301 */     out.writeObject(ReflectionUtil.toTypeNameArray(this.paramTypes));
/* 302 */     out.writeObject(this.fnMapper);
/* 303 */     out.writeObject(this.varMapper);
/*     */   }
/*     */   
/*     */   public boolean isLiteralText()
/*     */   {
/* 308 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isParametersProvided()
/*     */   {
/* 317 */     return getNode().isParametersProvided();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isParmetersProvided()
/*     */   {
/* 328 */     return getNode().isParametersProvided();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\MethodExpressionImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */