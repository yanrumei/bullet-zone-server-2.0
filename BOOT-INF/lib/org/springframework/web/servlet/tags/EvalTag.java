/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.JspWriter;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.el.VariableResolver;
/*     */ import org.springframework.context.expression.BeanFactoryResolver;
/*     */ import org.springframework.context.expression.EnvironmentAccessor;
/*     */ import org.springframework.context.expression.MapAccessor;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.ExpressionParser;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.expression.spel.support.StandardTypeConverter;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.util.JavaScriptUtils;
/*     */ import org.springframework.web.util.TagUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EvalTag
/*     */   extends HtmlEscapingAwareTag
/*     */ {
/*     */   private static final String EVALUATION_CONTEXT_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.EVALUATION_CONTEXT";
/*  59 */   private final ExpressionParser expressionParser = new SpelExpressionParser();
/*     */   
/*     */   private Expression expression;
/*     */   
/*     */   private String var;
/*     */   
/*  65 */   private int scope = 1;
/*     */   
/*  67 */   private boolean javaScriptEscape = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExpression(String expression)
/*     */   {
/*  74 */     this.expression = this.expressionParser.parseExpression(expression);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVar(String var)
/*     */   {
/*  82 */     this.var = var;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScope(String scope)
/*     */   {
/*  90 */     this.scope = TagUtils.getScope(scope);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setJavaScriptEscape(boolean javaScriptEscape)
/*     */     throws JspException
/*     */   {
/*  98 */     this.javaScriptEscape = javaScriptEscape;
/*     */   }
/*     */   
/*     */   public int doStartTagInternal()
/*     */     throws JspException
/*     */   {
/* 104 */     return 1;
/*     */   }
/*     */   
/*     */   public int doEndTag()
/*     */     throws JspException
/*     */   {
/* 110 */     EvaluationContext evaluationContext = (EvaluationContext)this.pageContext.getAttribute("org.springframework.web.servlet.tags.EVALUATION_CONTEXT");
/* 111 */     if (evaluationContext == null) {
/* 112 */       evaluationContext = createEvaluationContext(this.pageContext);
/* 113 */       this.pageContext.setAttribute("org.springframework.web.servlet.tags.EVALUATION_CONTEXT", evaluationContext);
/*     */     }
/* 115 */     if (this.var != null) {
/* 116 */       Object result = this.expression.getValue(evaluationContext);
/* 117 */       this.pageContext.setAttribute(this.var, result, this.scope);
/*     */     }
/*     */     else {
/*     */       try {
/* 121 */         String result = (String)this.expression.getValue(evaluationContext, String.class);
/* 122 */         result = ObjectUtils.getDisplayString(result);
/* 123 */         result = htmlEscape(result);
/* 124 */         result = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(result) : result;
/* 125 */         this.pageContext.getOut().print(result);
/*     */       }
/*     */       catch (IOException ex) {
/* 128 */         throw new JspException(ex);
/*     */       }
/*     */     }
/* 131 */     return 6;
/*     */   }
/*     */   
/*     */   private EvaluationContext createEvaluationContext(PageContext pageContext) {
/* 135 */     StandardEvaluationContext context = new StandardEvaluationContext();
/* 136 */     context.addPropertyAccessor(new JspPropertyAccessor(pageContext));
/* 137 */     context.addPropertyAccessor(new MapAccessor());
/* 138 */     context.addPropertyAccessor(new EnvironmentAccessor());
/* 139 */     context.setBeanResolver(new BeanFactoryResolver(getRequestContext().getWebApplicationContext()));
/* 140 */     ConversionService conversionService = getConversionService(pageContext);
/* 141 */     if (conversionService != null) {
/* 142 */       context.setTypeConverter(new StandardTypeConverter(conversionService));
/*     */     }
/* 144 */     return context;
/*     */   }
/*     */   
/*     */   private ConversionService getConversionService(PageContext pageContext) {
/* 148 */     return (ConversionService)pageContext.getRequest().getAttribute(ConversionService.class.getName());
/*     */   }
/*     */   
/*     */ 
/*     */   private static class JspPropertyAccessor
/*     */     implements PropertyAccessor
/*     */   {
/*     */     private final PageContext pageContext;
/*     */     private final VariableResolver variableResolver;
/*     */     
/*     */     public JspPropertyAccessor(PageContext pageContext)
/*     */     {
/* 160 */       this.pageContext = pageContext;
/* 161 */       this.variableResolver = pageContext.getVariableResolver();
/*     */     }
/*     */     
/*     */     public Class<?>[] getSpecificTargetClasses()
/*     */     {
/* 166 */       return null;
/*     */     }
/*     */     
/*     */     public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException
/*     */     {
/* 171 */       return (target == null) && (
/* 172 */         (resolveImplicitVariable(name) != null) || (this.pageContext.findAttribute(name) != null));
/*     */     }
/*     */     
/*     */     public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException
/*     */     {
/* 177 */       Object implicitVar = resolveImplicitVariable(name);
/* 178 */       if (implicitVar != null) {
/* 179 */         return new TypedValue(implicitVar);
/*     */       }
/* 181 */       return new TypedValue(this.pageContext.findAttribute(name));
/*     */     }
/*     */     
/*     */     public boolean canWrite(EvaluationContext context, Object target, String name)
/*     */     {
/* 186 */       return false;
/*     */     }
/*     */     
/*     */     public void write(EvaluationContext context, Object target, String name, Object newValue)
/*     */     {
/* 191 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     private Object resolveImplicitVariable(String name) throws AccessException {
/* 195 */       if (this.variableResolver == null) {
/* 196 */         return null;
/*     */       }
/*     */       try {
/* 199 */         return this.variableResolver.resolveVariable(name);
/*     */       }
/*     */       catch (Exception ex) {
/* 202 */         throw new AccessException("Unexpected exception occurred accessing '" + name + "' as an implicit variable", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\EvalTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */