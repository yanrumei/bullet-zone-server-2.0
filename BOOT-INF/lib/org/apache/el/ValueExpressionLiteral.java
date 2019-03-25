/*     */ package org.apache.el;
/*     */ 
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.PropertyNotWritableException;
/*     */ import javax.el.ValueExpression;
/*     */ import org.apache.el.util.MessageFactory;
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
/*     */ public final class ValueExpressionLiteral
/*     */   extends ValueExpression
/*     */   implements Externalizable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private Object value;
/*     */   private String valueString;
/*     */   private Class<?> expectedType;
/*     */   
/*     */   public ValueExpressionLiteral() {}
/*     */   
/*     */   public ValueExpressionLiteral(Object value, Class<?> expectedType)
/*     */   {
/*  47 */     this.value = value;
/*  48 */     this.expectedType = expectedType;
/*     */   }
/*     */   
/*     */   public Object getValue(ELContext context)
/*     */   {
/*  53 */     context.notifyBeforeEvaluation(getExpressionString());
/*     */     Object result;
/*  55 */     Object result; if (this.expectedType != null) {
/*  56 */       result = context.convertToType(this.value, this.expectedType);
/*     */     } else {
/*  58 */       result = this.value;
/*     */     }
/*  60 */     context.notifyAfterEvaluation(getExpressionString());
/*  61 */     return result;
/*     */   }
/*     */   
/*     */   public void setValue(ELContext context, Object value)
/*     */   {
/*  66 */     context.notifyBeforeEvaluation(getExpressionString());
/*  67 */     throw new PropertyNotWritableException(MessageFactory.get("error.value.literal.write", new Object[] { this.value }));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isReadOnly(ELContext context)
/*     */   {
/*  73 */     context.notifyBeforeEvaluation(getExpressionString());
/*  74 */     context.notifyAfterEvaluation(getExpressionString());
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context)
/*     */   {
/*  80 */     context.notifyBeforeEvaluation(getExpressionString());
/*  81 */     Class<?> result = this.value != null ? this.value.getClass() : null;
/*  82 */     context.notifyAfterEvaluation(getExpressionString());
/*  83 */     return result;
/*     */   }
/*     */   
/*     */   public Class<?> getExpectedType()
/*     */   {
/*  88 */     return this.expectedType;
/*     */   }
/*     */   
/*     */   public String getExpressionString()
/*     */   {
/*  93 */     if (this.valueString == null) {
/*  94 */       this.valueString = (this.value != null ? this.value.toString() : null);
/*     */     }
/*  96 */     return this.valueString;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 101 */     return ((obj instanceof ValueExpressionLiteral)) && 
/* 102 */       (equals((ValueExpressionLiteral)obj));
/*     */   }
/*     */   
/*     */   public boolean equals(ValueExpressionLiteral ve) {
/* 106 */     return (ve != null) && (this.value != null) && (ve.value != null) && ((this.value == ve.value) || 
/* 107 */       (this.value.equals(ve.value)));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 112 */     return this.value != null ? this.value.hashCode() : 0;
/*     */   }
/*     */   
/*     */   public boolean isLiteralText()
/*     */   {
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException
/*     */   {
/* 122 */     out.writeObject(this.value);
/* 123 */     out.writeUTF(this.expectedType != null ? this.expectedType.getName() : "");
/*     */   }
/*     */   
/*     */ 
/*     */   public void readExternal(ObjectInput in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 130 */     this.value = in.readObject();
/* 131 */     String type = in.readUTF();
/* 132 */     if (!"".equals(type)) {
/* 133 */       this.expectedType = ReflectionUtil.forName(type);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\ValueExpressionLiteral.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */