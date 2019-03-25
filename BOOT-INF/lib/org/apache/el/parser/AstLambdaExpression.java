/*     */ package org.apache.el.parser;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.el.ELException;
/*     */ import javax.el.LambdaExpression;
/*     */ import org.apache.el.ValueExpressionImpl;
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
/*     */ public class AstLambdaExpression
/*     */   extends SimpleNode
/*     */ {
/*  32 */   private NestedState nestedState = null;
/*     */   
/*     */   public AstLambdaExpression(int id) {
/*  35 */     super(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getValue(EvaluationContext ctx)
/*     */     throws ELException
/*     */   {
/*  43 */     NestedState state = getNestedState();
/*     */     
/*     */ 
/*     */ 
/*  47 */     int methodParameterSetCount = jjtGetNumChildren() - 2;
/*  48 */     if (methodParameterSetCount > state.getNestingCount()) {
/*  49 */       throw new ELException(MessageFactory.get("error.lambda.tooManyMethodParameterSets"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  54 */     AstLambdaParameters formalParametersNode = (AstLambdaParameters)this.children[0];
/*     */     
/*  56 */     Node[] formalParamNodes = formalParametersNode.children;
/*     */     
/*     */ 
/*     */ 
/*  60 */     ValueExpressionImpl ve = new ValueExpressionImpl("", this.children[1], ctx.getFunctionMapper(), ctx.getVariableMapper(), null);
/*     */     
/*     */ 
/*  63 */     List<String> formalParameters = new ArrayList();
/*  64 */     if (formalParamNodes != null) {
/*  65 */       for (Node formalParamNode : formalParamNodes) {
/*  66 */         formalParameters.add(formalParamNode.getImage());
/*     */       }
/*     */     }
/*  69 */     LambdaExpression le = new LambdaExpression(formalParameters, ve);
/*  70 */     le.setELContext(ctx);
/*     */     
/*  72 */     if (jjtGetNumChildren() == 2)
/*     */     {
/*     */ 
/*     */ 
/*  76 */       if (state.getHasFormalParameters()) {
/*  77 */         return le;
/*     */       }
/*  79 */       return le.invoke(ctx, (Object[])null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     int methodParameterIndex = 2;
/*  97 */     Object result = le.invoke(((AstMethodParameters)this.children[methodParameterIndex])
/*  98 */       .getParameters(ctx));
/*  99 */     methodParameterIndex++;
/*     */     
/* 101 */     while (((result instanceof LambdaExpression)) && 
/* 102 */       (methodParameterIndex < jjtGetNumChildren())) {
/* 103 */       result = ((LambdaExpression)result).invoke(((AstMethodParameters)this.children[methodParameterIndex])
/* 104 */         .getParameters(ctx));
/* 105 */       methodParameterIndex++;
/*     */     }
/*     */     
/* 108 */     return result;
/*     */   }
/*     */   
/*     */   private NestedState getNestedState()
/*     */   {
/* 113 */     if (this.nestedState == null) {
/* 114 */       setNestedState(new NestedState(null));
/*     */     }
/* 116 */     return this.nestedState;
/*     */   }
/*     */   
/*     */   private void setNestedState(NestedState nestedState)
/*     */   {
/* 121 */     if (this.nestedState != null)
/*     */     {
/* 123 */       throw new IllegalStateException("nestedState may only be set once");
/*     */     }
/* 125 */     this.nestedState = nestedState;
/*     */     
/*     */ 
/* 128 */     nestedState.incrementNestingCount();
/*     */     
/* 130 */     if (jjtGetNumChildren() > 1) {
/* 131 */       Node firstChild = jjtGetChild(0);
/* 132 */       if ((firstChild instanceof AstLambdaParameters)) {
/* 133 */         if (firstChild.jjtGetNumChildren() > 0) {
/* 134 */           nestedState.setHasFormalParameters();
/*     */         }
/*     */       }
/*     */       else {
/* 138 */         return;
/*     */       }
/* 140 */       Node secondChild = jjtGetChild(1);
/* 141 */       if ((secondChild instanceof AstLambdaExpression)) {
/* 142 */         ((AstLambdaExpression)secondChild).setNestedState(nestedState);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 152 */     StringBuilder result = new StringBuilder();
/* 153 */     for (Node n : this.children) {
/* 154 */       result.append(n.toString());
/*     */     }
/* 156 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   private static class NestedState
/*     */   {
/* 162 */     private int nestingCount = 0;
/* 163 */     private boolean hasFormalParameters = false;
/*     */     
/*     */     private void incrementNestingCount() {
/* 166 */       this.nestingCount += 1;
/*     */     }
/*     */     
/*     */     private int getNestingCount() {
/* 170 */       return this.nestingCount;
/*     */     }
/*     */     
/*     */     private void setHasFormalParameters() {
/* 174 */       this.hasFormalParameters = true;
/*     */     }
/*     */     
/*     */     private boolean getHasFormalParameters() {
/* 178 */       return this.hasFormalParameters;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstLambdaExpression.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */