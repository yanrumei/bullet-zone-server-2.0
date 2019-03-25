/*     */ package org.apache.el;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.FunctionMapper;
/*     */ import javax.el.PropertyNotFoundException;
/*     */ import javax.el.PropertyNotWritableException;
/*     */ import javax.el.ValueExpression;
/*     */ import javax.el.ValueReference;
/*     */ import javax.el.VariableMapper;
/*     */ import org.apache.el.lang.EvaluationContext;
/*     */ import org.apache.el.lang.ExpressionBuilder;
/*     */ import org.apache.el.parser.AstLiteralExpression;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ValueExpressionImpl
/*     */   extends ValueExpression
/*     */   implements Externalizable
/*     */ {
/*     */   private Class<?> expectedType;
/*     */   private String expr;
/*     */   private FunctionMapper fnMapper;
/*     */   private VariableMapper varMapper;
/*     */   private transient Node node;
/*     */   
/*     */   public ValueExpressionImpl() {}
/*     */   
/*     */   public ValueExpressionImpl(String expr, Node node, FunctionMapper fnMapper, VariableMapper varMapper, Class<?> expectedType)
/*     */   {
/* 107 */     this.expr = expr;
/* 108 */     this.node = node;
/* 109 */     this.fnMapper = fnMapper;
/* 110 */     this.varMapper = varMapper;
/* 111 */     this.expectedType = expectedType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 121 */     return ((obj instanceof ValueExpressionImpl)) && 
/* 122 */       (obj.hashCode() == hashCode());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getExpectedType()
/*     */   {
/* 132 */     return this.expectedType;
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
/*     */   public String getExpressionString()
/*     */   {
/* 147 */     return this.expr;
/*     */   }
/*     */   
/*     */   private Node getNode() throws ELException {
/* 151 */     if (this.node == null) {
/* 152 */       this.node = ExpressionBuilder.createNode(this.expr);
/*     */     }
/* 154 */     return this.node;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getType(ELContext context)
/*     */     throws PropertyNotFoundException, ELException
/*     */   {
/* 165 */     EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
/*     */     
/* 167 */     context.notifyBeforeEvaluation(getExpressionString());
/* 168 */     Class<?> result = getNode().getType(ctx);
/* 169 */     context.notifyAfterEvaluation(getExpressionString());
/* 170 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getValue(ELContext context)
/*     */     throws PropertyNotFoundException, ELException
/*     */   {
/* 181 */     EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
/*     */     
/* 183 */     context.notifyBeforeEvaluation(getExpressionString());
/* 184 */     Object value = getNode().getValue(ctx);
/* 185 */     if (this.expectedType != null) {
/* 186 */       value = context.convertToType(value, this.expectedType);
/*     */     }
/* 188 */     context.notifyAfterEvaluation(getExpressionString());
/* 189 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 199 */     return getNode().hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLiteralText()
/*     */   {
/*     */     try
/*     */     {
/* 210 */       return getNode() instanceof AstLiteralExpression;
/*     */     } catch (ELException ele) {}
/* 212 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReadOnly(ELContext context)
/*     */     throws PropertyNotFoundException, ELException
/*     */   {
/* 224 */     EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
/*     */     
/* 226 */     context.notifyBeforeEvaluation(getExpressionString());
/* 227 */     boolean result = getNode().isReadOnly(ctx);
/* 228 */     context.notifyAfterEvaluation(getExpressionString());
/* 229 */     return result;
/*     */   }
/*     */   
/*     */   public void readExternal(ObjectInput in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 235 */     this.expr = in.readUTF();
/* 236 */     String type = in.readUTF();
/* 237 */     if (!"".equals(type)) {
/* 238 */       this.expectedType = ReflectionUtil.forName(type);
/*     */     }
/* 240 */     this.fnMapper = ((FunctionMapper)in.readObject());
/* 241 */     this.varMapper = ((VariableMapper)in.readObject());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(ELContext context, Object value)
/*     */     throws PropertyNotFoundException, PropertyNotWritableException, ELException
/*     */   {
/* 254 */     EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
/*     */     
/* 256 */     context.notifyBeforeEvaluation(getExpressionString());
/* 257 */     getNode().setValue(ctx, value);
/* 258 */     context.notifyAfterEvaluation(getExpressionString());
/*     */   }
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException
/*     */   {
/* 263 */     out.writeUTF(this.expr);
/* 264 */     out.writeUTF(this.expectedType != null ? this.expectedType.getName() : "");
/*     */     
/* 266 */     out.writeObject(this.fnMapper);
/* 267 */     out.writeObject(this.varMapper);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 272 */     return "ValueExpression[" + this.expr + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueReference getValueReference(ELContext context)
/*     */   {
/* 280 */     EvaluationContext ctx = new EvaluationContext(context, this.fnMapper, this.varMapper);
/*     */     
/* 282 */     context.notifyBeforeEvaluation(getExpressionString());
/* 283 */     ValueReference result = getNode().getValueReference(ctx);
/* 284 */     context.notifyAfterEvaluation(getExpressionString());
/* 285 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\ValueExpressionImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */