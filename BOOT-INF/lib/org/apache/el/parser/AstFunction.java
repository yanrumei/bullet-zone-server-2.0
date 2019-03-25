/*     */ package org.apache.el.parser;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.el.ELClass;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import javax.el.FunctionMapper;
/*     */ import javax.el.ImportHandler;
/*     */ import javax.el.LambdaExpression;
/*     */ import javax.el.ValueExpression;
/*     */ import javax.el.VariableMapper;
/*     */ import org.apache.el.lang.EvaluationContext;
/*     */ import org.apache.el.util.MessageFactory;
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
/*     */ public final class AstFunction
/*     */   extends SimpleNode
/*     */ {
/*  40 */   protected String localName = "";
/*     */   
/*  42 */   protected String prefix = "";
/*     */   
/*     */   public AstFunction(int id) {
/*  45 */     super(id);
/*     */   }
/*     */   
/*     */   public String getLocalName() {
/*  49 */     return this.localName;
/*     */   }
/*     */   
/*     */   public String getOutputName() {
/*  53 */     if (this.prefix == null) {
/*  54 */       return this.localName;
/*     */     }
/*  56 */     return this.prefix + ":" + this.localName;
/*     */   }
/*     */   
/*     */   public String getPrefix()
/*     */   {
/*  61 */     return this.prefix;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> getType(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/*  68 */     FunctionMapper fnMapper = ctx.getFunctionMapper();
/*     */     
/*     */ 
/*  71 */     if (fnMapper == null) {
/*  72 */       throw new ELException(MessageFactory.get("error.fnMapper.null"));
/*     */     }
/*  74 */     Method m = fnMapper.resolveFunction(this.prefix, this.localName);
/*  75 */     if (m == null) {
/*  76 */       throw new ELException(MessageFactory.get("error.fnMapper.method", new Object[] {
/*  77 */         getOutputName() }));
/*     */     }
/*  79 */     return m.getReturnType();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getValue(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/*  86 */     FunctionMapper fnMapper = ctx.getFunctionMapper();
/*     */     
/*     */ 
/*  89 */     if (fnMapper == null) {
/*  90 */       throw new ELException(MessageFactory.get("error.fnMapper.null"));
/*     */     }
/*  92 */     Method m = fnMapper.resolveFunction(this.prefix, this.localName);
/*     */     
/*  94 */     if ((m == null) && (this.prefix.length() == 0))
/*     */     {
/*     */ 
/*     */ 
/*  98 */       Object obj = null;
/*  99 */       if (ctx.isLambdaArgument(this.localName)) {
/* 100 */         obj = ctx.getLambdaArgument(this.localName);
/*     */       }
/* 102 */       if (obj == null) {
/* 103 */         VariableMapper varMapper = ctx.getVariableMapper();
/* 104 */         if (varMapper != null) {
/* 105 */           obj = varMapper.resolveVariable(this.localName);
/* 106 */           if ((obj instanceof ValueExpression))
/*     */           {
/* 108 */             obj = ((ValueExpression)obj).getValue(ctx);
/*     */           }
/*     */         }
/*     */       }
/* 112 */       if (obj == null) {
/* 113 */         obj = ctx.getELResolver().getValue(ctx, null, this.localName);
/*     */       }
/* 115 */       if ((obj instanceof LambdaExpression))
/*     */       {
/* 117 */         int i = 0;
/* 118 */         while (((obj instanceof LambdaExpression)) && 
/* 119 */           (i < jjtGetNumChildren())) {
/* 120 */           Node args = jjtGetChild(i);
/* 121 */           obj = ((LambdaExpression)obj).invoke(((AstMethodParameters)args)
/* 122 */             .getParameters(ctx));
/* 123 */           i++;
/*     */         }
/* 125 */         if (i < jjtGetNumChildren())
/*     */         {
/*     */ 
/* 128 */           throw new ELException(MessageFactory.get("error.lambda.tooManyMethodParameterSets"));
/*     */         }
/*     */         
/* 131 */         return obj;
/*     */       }
/*     */       
/*     */ 
/* 135 */       obj = ctx.getImportHandler().resolveClass(this.localName);
/* 136 */       if (obj != null) {
/* 137 */         return ctx.getELResolver().invoke(ctx, new ELClass((Class)obj), "<init>", null, ((AstMethodParameters)this.children[0])
/* 138 */           .getParameters(ctx));
/*     */       }
/* 140 */       obj = ctx.getImportHandler().resolveStatic(this.localName);
/* 141 */       if (obj != null) {
/* 142 */         return ctx.getELResolver().invoke(ctx, new ELClass((Class)obj), this.localName, null, ((AstMethodParameters)this.children[0])
/* 143 */           .getParameters(ctx));
/*     */       }
/*     */     }
/*     */     
/* 147 */     if (m == null) {
/* 148 */       throw new ELException(MessageFactory.get("error.fnMapper.method", new Object[] {
/* 149 */         getOutputName() }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 154 */     if (jjtGetNumChildren() != 1) {
/* 155 */       throw new ELException(MessageFactory.get("error.funciton.tooManyMethodParameterSets", new Object[] {
/*     */       
/* 157 */         getOutputName() }));
/*     */     }
/*     */     
/* 160 */     Node parameters = jjtGetChild(0);
/* 161 */     Class<?>[] paramTypes = m.getParameterTypes();
/* 162 */     Object[] params = null;
/* 163 */     Object result = null;
/* 164 */     int inputParameterCount = parameters.jjtGetNumChildren();
/* 165 */     int methodParameterCount = paramTypes.length;
/* 166 */     if ((inputParameterCount == 0) && (methodParameterCount == 1) && (m.isVarArgs())) {
/* 167 */       params = new Object[] { null };
/* 168 */     } else if (inputParameterCount > 0) {
/* 169 */       params = new Object[methodParameterCount];
/*     */       try {
/* 171 */         for (int i = 0; i < methodParameterCount; i++) {
/* 172 */           if ((m.isVarArgs()) && (i == methodParameterCount - 1)) {
/* 173 */             if (inputParameterCount < methodParameterCount) {
/* 174 */               params[i] = { null };
/* 175 */             } else if ((inputParameterCount == methodParameterCount) && 
/* 176 */               (paramTypes[i].isArray())) {
/* 177 */               params[i] = parameters.jjtGetChild(i).getValue(ctx);
/*     */             } else {
/* 179 */               Object[] varargs = new Object[inputParameterCount - methodParameterCount + 1];
/*     */               
/* 181 */               Class<?> target = paramTypes[i].getComponentType();
/* 182 */               for (int j = i; j < inputParameterCount; j++) {
/* 183 */                 varargs[(j - i)] = parameters.jjtGetChild(j).getValue(ctx);
/* 184 */                 varargs[(j - i)] = coerceToType(ctx, varargs[(j - i)], target);
/*     */               }
/* 186 */               params[i] = varargs;
/*     */             }
/*     */           } else {
/* 189 */             params[i] = parameters.jjtGetChild(i).getValue(ctx);
/*     */           }
/* 191 */           params[i] = coerceToType(ctx, params[i], paramTypes[i]);
/*     */         }
/*     */       } catch (ELException ele) {
/* 194 */         throw new ELException(MessageFactory.get("error.function", new Object[] {
/* 195 */           getOutputName() }), ele);
/*     */       }
/*     */     }
/*     */     try {
/* 199 */       result = m.invoke(null, params);
/*     */     } catch (IllegalAccessException iae) {
/* 201 */       throw new ELException(MessageFactory.get("error.function", new Object[] {
/* 202 */         getOutputName() }), iae);
/*     */     } catch (InvocationTargetException ite) {
/* 204 */       Throwable cause = ite.getCause();
/* 205 */       if ((cause instanceof ThreadDeath)) {
/* 206 */         throw ((ThreadDeath)cause);
/*     */       }
/* 208 */       if ((cause instanceof VirtualMachineError)) {
/* 209 */         throw ((VirtualMachineError)cause);
/*     */       }
/* 211 */       throw new ELException(MessageFactory.get("error.function", new Object[] {
/* 212 */         getOutputName() }), cause);
/*     */     }
/* 214 */     return result;
/*     */   }
/*     */   
/*     */   public void setLocalName(String localName) {
/* 218 */     this.localName = localName;
/*     */   }
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 222 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 229 */     return ELParserTreeConstants.jjtNodeName[this.id] + "[" + getOutputName() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstFunction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */