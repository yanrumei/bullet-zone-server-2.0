/*     */ package org.apache.el.parser;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import javax.el.ELClass;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import javax.el.ImportHandler;
/*     */ import javax.el.MethodExpression;
/*     */ import javax.el.MethodInfo;
/*     */ import javax.el.MethodNotFoundException;
/*     */ import javax.el.PropertyNotFoundException;
/*     */ import javax.el.ValueExpression;
/*     */ import javax.el.ValueReference;
/*     */ import javax.el.VariableMapper;
/*     */ import org.apache.el.lang.EvaluationContext;
/*     */ import org.apache.el.util.MessageFactory;
/*     */ import org.apache.el.util.Validation;
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
/*     */ public final class AstIdentifier
/*     */   extends SimpleNode
/*     */ {
/*     */   public AstIdentifier(int id)
/*     */   {
/*  41 */     super(id);
/*     */   }
/*     */   
/*     */   public Class<?> getType(EvaluationContext ctx) throws ELException
/*     */   {
/*  46 */     VariableMapper varMapper = ctx.getVariableMapper();
/*  47 */     if (varMapper != null) {
/*  48 */       ValueExpression expr = varMapper.resolveVariable(this.image);
/*  49 */       if (expr != null) {
/*  50 */         return expr.getType(ctx.getELContext());
/*     */       }
/*     */     }
/*  53 */     ctx.setPropertyResolved(false);
/*  54 */     Class<?> result = ctx.getELResolver().getType(ctx, null, this.image);
/*  55 */     if (!ctx.isPropertyResolved()) {
/*  56 */       throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled.null", new Object[] { this.image }));
/*     */     }
/*     */     
/*  59 */     return result;
/*     */   }
/*     */   
/*     */   public Object getValue(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/*  65 */     if (ctx.isLambdaArgument(this.image)) {
/*  66 */       return ctx.getLambdaArgument(this.image);
/*     */     }
/*     */     
/*     */ 
/*  70 */     VariableMapper varMapper = ctx.getVariableMapper();
/*  71 */     if (varMapper != null) {
/*  72 */       ValueExpression expr = varMapper.resolveVariable(this.image);
/*  73 */       if (expr != null) {
/*  74 */         return expr.getValue(ctx.getELContext());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  79 */     ctx.setPropertyResolved(false);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */     if ((this.parent instanceof AstValue)) {
/*  89 */       ctx.putContext(getClass(), Boolean.FALSE);
/*     */     } else {
/*  91 */       ctx.putContext(getClass(), Boolean.TRUE);
/*     */     }
/*     */     try {
/*  94 */       result = ctx.getELResolver().getValue(ctx, null, this.image);
/*     */     }
/*     */     finally {
/*     */       Object result;
/*  98 */       ctx.putContext(getClass(), Boolean.FALSE);
/*     */     }
/*     */     
/* 101 */     if (ctx.isPropertyResolved()) {
/* 102 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 106 */     Object result = ctx.getImportHandler().resolveClass(this.image);
/* 107 */     if (result != null) {
/* 108 */       return new ELClass((Class)result);
/*     */     }
/* 110 */     result = ctx.getImportHandler().resolveStatic(this.image);
/* 111 */     if (result != null) {
/*     */       try {
/* 113 */         return ((Class)result).getField(this.image).get(null);
/*     */       }
/*     */       catch (IllegalArgumentException|IllegalAccessException|NoSuchFieldException|SecurityException e) {
/* 116 */         throw new ELException(e);
/*     */       }
/*     */     }
/*     */     
/* 120 */     throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled.null", new Object[] { this.image }));
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/* 126 */     VariableMapper varMapper = ctx.getVariableMapper();
/* 127 */     if (varMapper != null) {
/* 128 */       ValueExpression expr = varMapper.resolveVariable(this.image);
/* 129 */       if (expr != null) {
/* 130 */         return expr.isReadOnly(ctx.getELContext());
/*     */       }
/*     */     }
/* 133 */     ctx.setPropertyResolved(false);
/* 134 */     boolean result = ctx.getELResolver().isReadOnly(ctx, null, this.image);
/* 135 */     if (!ctx.isPropertyResolved()) {
/* 136 */       throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled.null", new Object[] { this.image }));
/*     */     }
/*     */     
/* 139 */     return result;
/*     */   }
/*     */   
/*     */   public void setValue(EvaluationContext ctx, Object value)
/*     */     throws ELException
/*     */   {
/* 145 */     VariableMapper varMapper = ctx.getVariableMapper();
/* 146 */     if (varMapper != null) {
/* 147 */       ValueExpression expr = varMapper.resolveVariable(this.image);
/* 148 */       if (expr != null) {
/* 149 */         expr.setValue(ctx.getELContext(), value);
/* 150 */         return;
/*     */       }
/*     */     }
/* 153 */     ctx.setPropertyResolved(false);
/* 154 */     ctx.getELResolver().setValue(ctx, null, this.image, value);
/* 155 */     if (!ctx.isPropertyResolved()) {
/* 156 */       throw new PropertyNotFoundException(MessageFactory.get("error.resolver.unhandled.null", new Object[] { this.image }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Object invoke(EvaluationContext ctx, Class<?>[] paramTypes, Object[] paramValues)
/*     */     throws ELException
/*     */   {
/* 164 */     return getMethodExpression(ctx).invoke(ctx.getELContext(), paramValues);
/*     */   }
/*     */   
/*     */ 
/*     */   public MethodInfo getMethodInfo(EvaluationContext ctx, Class<?>[] paramTypes)
/*     */     throws ELException
/*     */   {
/* 171 */     return getMethodExpression(ctx).getMethodInfo(ctx.getELContext());
/*     */   }
/*     */   
/*     */   public void setImage(String image)
/*     */   {
/* 176 */     if (!Validation.isIdentifier(image)) {
/* 177 */       throw new ELException(MessageFactory.get("error.identifier.notjava", new Object[] { image }));
/*     */     }
/*     */     
/* 180 */     this.image = image;
/*     */   }
/*     */   
/*     */ 
/*     */   public ValueReference getValueReference(EvaluationContext ctx)
/*     */   {
/* 186 */     VariableMapper varMapper = ctx.getVariableMapper();
/*     */     
/* 188 */     if (varMapper == null) {
/* 189 */       return null;
/*     */     }
/*     */     
/* 192 */     ValueExpression expr = varMapper.resolveVariable(this.image);
/*     */     
/* 194 */     if (expr == null) {
/* 195 */       return null;
/*     */     }
/*     */     
/* 198 */     return expr.getValueReference(ctx);
/*     */   }
/*     */   
/*     */   private final MethodExpression getMethodExpression(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/* 204 */     Object obj = null;
/*     */     
/*     */ 
/*     */ 
/* 208 */     VariableMapper varMapper = ctx.getVariableMapper();
/* 209 */     ValueExpression ve = null;
/* 210 */     if (varMapper != null) {
/* 211 */       ve = varMapper.resolveVariable(this.image);
/* 212 */       if (ve != null) {
/* 213 */         obj = ve.getValue(ctx);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 219 */     if (ve == null) {
/* 220 */       ctx.setPropertyResolved(false);
/* 221 */       obj = ctx.getELResolver().getValue(ctx, null, this.image);
/*     */     }
/*     */     
/*     */ 
/* 225 */     if ((obj instanceof MethodExpression))
/* 226 */       return (MethodExpression)obj;
/* 227 */     if (obj == null) {
/* 228 */       throw new MethodNotFoundException("Identity '" + this.image + "' was null and was unable to invoke");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */     throw new ELException("Identity '" + this.image + "' does not reference a MethodExpression instance, returned type: " + obj.getClass().getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstIdentifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */