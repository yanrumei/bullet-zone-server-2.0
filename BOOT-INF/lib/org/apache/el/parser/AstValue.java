/*     */ package org.apache.el.parser;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import javax.el.LambdaExpression;
/*     */ import javax.el.MethodInfo;
/*     */ import javax.el.PropertyNotFoundException;
/*     */ import javax.el.ValueReference;
/*     */ import org.apache.el.lang.ELSupport;
/*     */ import org.apache.el.lang.EvaluationContext;
/*     */ import org.apache.el.stream.Optional;
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
/*     */ 
/*     */ 
/*     */ public final class AstValue
/*     */   extends SimpleNode
/*     */ {
/*  44 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AstValue(int id)
/*     */   {
/*  53 */     super(id);
/*     */   }
/*     */   
/*     */   public Class<?> getType(EvaluationContext ctx) throws ELException
/*     */   {
/*  58 */     Target t = getTarget(ctx);
/*  59 */     ctx.setPropertyResolved(false);
/*  60 */     Class<?> result = ctx.getELResolver().getType(ctx, t.base, t.property);
/*  61 */     if (!ctx.isPropertyResolved()) {
/*  62 */       throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled", new Object[] { t.base, t.property }));
/*     */     }
/*     */     
/*  65 */     return result;
/*     */   }
/*     */   
/*     */   private final Target getTarget(EvaluationContext ctx) throws ELException
/*     */   {
/*  70 */     Object base = this.children[0].getValue(ctx);
/*     */     
/*     */ 
/*  73 */     if (base == null) {
/*  74 */       throw new PropertyNotFoundException(MessageFactory.get("error.unreachable.base", new Object[] {this.children[0]
/*  75 */         .getImage() }));
/*     */     }
/*     */     
/*     */ 
/*  79 */     Object property = null;
/*  80 */     int propCount = jjtGetNumChildren();
/*     */     
/*  82 */     int i = 1;
/*     */     
/*  84 */     ELResolver resolver = ctx.getELResolver();
/*  85 */     while (i < propCount) {
/*  86 */       if ((i + 2 < propCount) && ((this.children[(i + 1)] instanceof AstMethodParameters)))
/*     */       {
/*     */ 
/*  89 */         base = resolver.invoke(ctx, base, this.children[i]
/*  90 */           .getValue(ctx), null, ((AstMethodParameters)this.children[(i + 1)])
/*     */           
/*  92 */           .getParameters(ctx));
/*  93 */         i += 2;
/*  94 */       } else if ((i + 2 == propCount) && ((this.children[(i + 1)] instanceof AstMethodParameters)))
/*     */       {
/*     */ 
/*  97 */         ctx.setPropertyResolved(false);
/*  98 */         property = this.children[i].getValue(ctx);
/*  99 */         i += 2;
/*     */         
/* 101 */         if (property == null) {
/* 102 */           throw new PropertyNotFoundException(MessageFactory.get("error.unreachable.property", new Object[] { property }));
/*     */         }
/*     */       }
/* 105 */       else if (i + 1 < propCount)
/*     */       {
/* 107 */         property = this.children[i].getValue(ctx);
/* 108 */         ctx.setPropertyResolved(false);
/* 109 */         base = resolver.getValue(ctx, base, property);
/* 110 */         i++;
/*     */       }
/*     */       else
/*     */       {
/* 114 */         ctx.setPropertyResolved(false);
/* 115 */         property = this.children[i].getValue(ctx);
/* 116 */         i++;
/*     */         
/* 118 */         if (property == null) {
/* 119 */           throw new PropertyNotFoundException(MessageFactory.get("error.unreachable.property", new Object[] { property }));
/*     */         }
/*     */       }
/*     */       
/* 123 */       if (base == null) {
/* 124 */         throw new PropertyNotFoundException(MessageFactory.get("error.unreachable.property", new Object[] { property }));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 129 */     Target t = new Target();
/* 130 */     t.base = base;
/* 131 */     t.property = property;
/* 132 */     return t;
/*     */   }
/*     */   
/*     */   public Object getValue(EvaluationContext ctx) throws ELException
/*     */   {
/* 137 */     Object base = this.children[0].getValue(ctx);
/* 138 */     int propCount = jjtGetNumChildren();
/* 139 */     int i = 1;
/* 140 */     Object suffix = null;
/* 141 */     ELResolver resolver = ctx.getELResolver();
/* 142 */     while ((base != null) && (i < propCount)) {
/* 143 */       suffix = this.children[i].getValue(ctx);
/* 144 */       if ((i + 1 < propCount) && ((this.children[(i + 1)] instanceof AstMethodParameters)))
/*     */       {
/* 146 */         AstMethodParameters mps = (AstMethodParameters)this.children[(i + 1)];
/*     */         
/* 148 */         if (((base instanceof Optional)) && ("orElseGet".equals(suffix)) && 
/* 149 */           (mps.jjtGetNumChildren() == 1)) {
/* 150 */           Node paramFoOptional = mps.jjtGetChild(0);
/* 151 */           if ((!(paramFoOptional instanceof AstLambdaExpression)) && (!(paramFoOptional instanceof LambdaExpression)))
/*     */           {
/* 153 */             throw new ELException(MessageFactory.get("stream.optional.paramNotLambda", new Object[] { suffix }));
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 158 */         Object[] paramValues = mps.getParameters(ctx);
/* 159 */         base = resolver.invoke(ctx, base, suffix, 
/* 160 */           getTypesFromValues(paramValues), paramValues);
/* 161 */         i += 2;
/*     */       }
/*     */       else {
/* 164 */         if (suffix == null) {
/* 165 */           return null;
/*     */         }
/*     */         
/* 168 */         ctx.setPropertyResolved(false);
/* 169 */         base = resolver.getValue(ctx, base, suffix);
/* 170 */         i++;
/*     */       }
/*     */     }
/* 173 */     if (!ctx.isPropertyResolved()) {
/* 174 */       throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled", new Object[] { base, suffix }));
/*     */     }
/*     */     
/* 177 */     return base;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(EvaluationContext ctx) throws ELException
/*     */   {
/* 182 */     Target t = getTarget(ctx);
/* 183 */     ctx.setPropertyResolved(false);
/*     */     
/* 185 */     boolean result = ctx.getELResolver().isReadOnly(ctx, t.base, t.property);
/* 186 */     if (!ctx.isPropertyResolved()) {
/* 187 */       throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled", new Object[] { t.base, t.property }));
/*     */     }
/*     */     
/* 190 */     return result;
/*     */   }
/*     */   
/*     */   public void setValue(EvaluationContext ctx, Object value)
/*     */     throws ELException
/*     */   {
/* 196 */     Target t = getTarget(ctx);
/* 197 */     ctx.setPropertyResolved(false);
/* 198 */     ELResolver resolver = ctx.getELResolver();
/*     */     
/*     */ 
/* 201 */     Class<?> targetClass = resolver.getType(ctx, t.base, t.property);
/* 202 */     resolver.setValue(ctx, t.base, t.property, 
/* 203 */       ELSupport.coerceToType(ctx, value, targetClass));
/* 204 */     if (!ctx.isPropertyResolved()) {
/* 205 */       throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled", new Object[] { t.base, t.property }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodInfo getMethodInfo(EvaluationContext ctx, Class[] paramTypes)
/*     */     throws ELException
/*     */   {
/* 215 */     Target t = getTarget(ctx);
/* 216 */     Method m = ReflectionUtil.getMethod(ctx, t.base, t.property, paramTypes, null);
/*     */     
/* 218 */     return new MethodInfo(m.getName(), m.getReturnType(), m
/* 219 */       .getParameterTypes());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues)
/*     */     throws ELException
/*     */   {
/* 228 */     Target t = getTarget(ctx);
/* 229 */     Method m = null;
/* 230 */     Object[] values = null;
/* 231 */     Class<?>[] types = null;
/* 232 */     if (isParametersProvided())
/*     */     {
/* 234 */       values = ((AstMethodParameters)jjtGetChild(jjtGetNumChildren() - 1)).getParameters(ctx);
/* 235 */       types = getTypesFromValues(values);
/*     */     } else {
/* 237 */       values = paramValues;
/* 238 */       types = paramTypes;
/*     */     }
/* 240 */     m = ReflectionUtil.getMethod(ctx, t.base, t.property, types, values);
/*     */     
/*     */ 
/* 243 */     values = convertArgs(ctx, values, m);
/*     */     
/* 245 */     Object result = null;
/*     */     try {
/* 247 */       result = m.invoke(t.base, values);
/*     */     } catch (IllegalAccessException iae) {
/* 249 */       throw new ELException(iae);
/*     */     } catch (IllegalArgumentException iae) {
/* 251 */       throw new ELException(iae);
/*     */     } catch (InvocationTargetException ite) {
/* 253 */       Throwable cause = ite.getCause();
/* 254 */       if ((cause instanceof ThreadDeath)) {
/* 255 */         throw ((ThreadDeath)cause);
/*     */       }
/* 257 */       if ((cause instanceof VirtualMachineError)) {
/* 258 */         throw ((VirtualMachineError)cause);
/*     */       }
/* 260 */       throw new ELException(cause);
/*     */     }
/* 262 */     return result;
/*     */   }
/*     */   
/*     */   private Object[] convertArgs(EvaluationContext ctx, Object[] src, Method m) {
/* 266 */     Class<?>[] types = m.getParameterTypes();
/* 267 */     if (types.length == 0)
/*     */     {
/* 269 */       return EMPTY_ARRAY;
/*     */     }
/*     */     
/* 272 */     int paramCount = types.length;
/*     */     
/* 274 */     if (((m.isVarArgs()) && (paramCount > 1) && ((src == null) || (paramCount > src.length))) || (
/* 275 */       (!m.isVarArgs()) && (((paramCount > 0) && (src == null)) || ((src != null) && (src.length != paramCount)))))
/*     */     {
/* 277 */       String srcCount = null;
/* 278 */       if (src != null)
/* 279 */         srcCount = Integer.toString(src.length);
/*     */       String msg;
/*     */       String msg;
/* 282 */       if (m.isVarArgs()) {
/* 283 */         msg = MessageFactory.get("error.invoke.tooFewParams", new Object[] {m
/* 284 */           .getName(), srcCount, Integer.toString(paramCount) });
/*     */       } else {
/* 286 */         msg = MessageFactory.get("error.invoke.wrongParams", new Object[] {m
/* 287 */           .getName(), srcCount, Integer.toString(paramCount) });
/*     */       }
/* 289 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */     
/* 292 */     if (src == null)
/*     */     {
/*     */ 
/*     */ 
/* 296 */       return new Object[1];
/*     */     }
/*     */     
/* 299 */     Object[] dest = new Object[paramCount];
/*     */     
/* 301 */     for (int i = 0; i < paramCount - 1; i++) {
/* 302 */       dest[i] = ELSupport.coerceToType(ctx, src[i], types[i]);
/*     */     }
/*     */     
/* 305 */     if (m.isVarArgs()) {
/* 306 */       Class<?> varArgType = m.getParameterTypes()[(paramCount - 1)].getComponentType();
/*     */       
/* 308 */       Object[] varArgs = (Object[])Array.newInstance(varArgType, src.length - (paramCount - 1));
/* 309 */       for (int i = 0; i < src.length - (paramCount - 1); i++) {
/* 310 */         varArgs[i] = ELSupport.coerceToType(ctx, src[(paramCount - 1 + i)], varArgType);
/*     */       }
/* 312 */       dest[(paramCount - 1)] = varArgs;
/*     */     } else {
/* 314 */       dest[(paramCount - 1)] = ELSupport.coerceToType(ctx, src[(paramCount - 1)], types[(paramCount - 1)]);
/*     */     }
/*     */     
/*     */ 
/* 318 */     return dest;
/*     */   }
/*     */   
/*     */   private Class<?>[] getTypesFromValues(Object[] values) {
/* 322 */     if (values == null) {
/* 323 */       return null;
/*     */     }
/*     */     
/* 326 */     Class<?>[] result = new Class[values.length];
/* 327 */     for (int i = 0; i < values.length; i++) {
/* 328 */       if (values[i] == null) {
/* 329 */         result[i] = null;
/*     */       } else {
/* 331 */         result[i] = values[i].getClass();
/*     */       }
/*     */     }
/* 334 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueReference getValueReference(EvaluationContext ctx)
/*     */   {
/* 344 */     if ((this.children.length > 2) && 
/* 345 */       ((jjtGetChild(2) instanceof AstMethodParameters)))
/*     */     {
/* 347 */       return null;
/*     */     }
/* 349 */     Target t = getTarget(ctx);
/* 350 */     return new ValueReference(t.base, t.property);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isParametersProvided()
/*     */   {
/* 361 */     int len = this.children.length;
/* 362 */     if ((len > 2) && 
/* 363 */       ((jjtGetChild(len - 1) instanceof AstMethodParameters))) {
/* 364 */       return true;
/*     */     }
/*     */     
/* 367 */     return false;
/*     */   }
/*     */   
/*     */   protected static class Target
/*     */   {
/*     */     protected Object base;
/*     */     protected Object property;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */