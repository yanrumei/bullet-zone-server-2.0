/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanExpressionException;
/*     */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*     */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.ExpressionParser;
/*     */ import org.springframework.expression.ParserContext;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.expression.spel.support.StandardTypeConverter;
/*     */ import org.springframework.expression.spel.support.StandardTypeLocator;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardBeanExpressionResolver
/*     */   implements BeanExpressionResolver
/*     */ {
/*     */   public static final String DEFAULT_EXPRESSION_PREFIX = "#{";
/*     */   public static final String DEFAULT_EXPRESSION_SUFFIX = "}";
/*  58 */   private String expressionPrefix = "#{";
/*     */   
/*  60 */   private String expressionSuffix = "}";
/*     */   
/*     */   private ExpressionParser expressionParser;
/*     */   
/*  64 */   private final Map<String, Expression> expressionCache = new ConcurrentHashMap(256);
/*     */   
/*  66 */   private final Map<BeanExpressionContext, StandardEvaluationContext> evaluationCache = new ConcurrentHashMap(8);
/*     */   
/*     */ 
/*  69 */   private final ParserContext beanExpressionParserContext = new ParserContext()
/*     */   {
/*     */     public boolean isTemplate() {
/*  72 */       return true;
/*     */     }
/*     */     
/*     */     public String getExpressionPrefix() {
/*  76 */       return StandardBeanExpressionResolver.this.expressionPrefix;
/*     */     }
/*     */     
/*     */     public String getExpressionSuffix() {
/*  80 */       return StandardBeanExpressionResolver.this.expressionSuffix;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardBeanExpressionResolver()
/*     */   {
/*  89 */     this.expressionParser = new SpelExpressionParser();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardBeanExpressionResolver(ClassLoader beanClassLoader)
/*     */   {
/*  98 */     this.expressionParser = new SpelExpressionParser(new SpelParserConfiguration(null, beanClassLoader));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExpressionPrefix(String expressionPrefix)
/*     */   {
/* 108 */     Assert.hasText(expressionPrefix, "Expression prefix must not be empty");
/* 109 */     this.expressionPrefix = expressionPrefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExpressionSuffix(String expressionSuffix)
/*     */   {
/* 118 */     Assert.hasText(expressionSuffix, "Expression suffix must not be empty");
/* 119 */     this.expressionSuffix = expressionSuffix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExpressionParser(ExpressionParser expressionParser)
/*     */   {
/* 128 */     Assert.notNull(expressionParser, "ExpressionParser must not be null");
/* 129 */     this.expressionParser = expressionParser;
/*     */   }
/*     */   
/*     */   public Object evaluate(String value, BeanExpressionContext evalContext)
/*     */     throws BeansException
/*     */   {
/* 135 */     if (!StringUtils.hasLength(value)) {
/* 136 */       return value;
/*     */     }
/*     */     try {
/* 139 */       Expression expr = (Expression)this.expressionCache.get(value);
/* 140 */       if (expr == null) {
/* 141 */         expr = this.expressionParser.parseExpression(value, this.beanExpressionParserContext);
/* 142 */         this.expressionCache.put(value, expr);
/*     */       }
/* 144 */       StandardEvaluationContext sec = (StandardEvaluationContext)this.evaluationCache.get(evalContext);
/* 145 */       if (sec == null) {
/* 146 */         sec = new StandardEvaluationContext();
/* 147 */         sec.setRootObject(evalContext);
/* 148 */         sec.addPropertyAccessor(new BeanExpressionContextAccessor());
/* 149 */         sec.addPropertyAccessor(new BeanFactoryAccessor());
/* 150 */         sec.addPropertyAccessor(new MapAccessor());
/* 151 */         sec.addPropertyAccessor(new EnvironmentAccessor());
/* 152 */         sec.setBeanResolver(new BeanFactoryResolver(evalContext.getBeanFactory()));
/* 153 */         sec.setTypeLocator(new StandardTypeLocator(evalContext.getBeanFactory().getBeanClassLoader()));
/* 154 */         ConversionService conversionService = evalContext.getBeanFactory().getConversionService();
/* 155 */         if (conversionService != null) {
/* 156 */           sec.setTypeConverter(new StandardTypeConverter(conversionService));
/*     */         }
/* 158 */         customizeEvaluationContext(sec);
/* 159 */         this.evaluationCache.put(evalContext, sec);
/*     */       }
/* 161 */       return expr.getValue(sec);
/*     */     }
/*     */     catch (Throwable ex) {
/* 164 */       throw new BeanExpressionException("Expression parsing failed", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void customizeEvaluationContext(StandardEvaluationContext evalContext) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\expression\StandardBeanExpressionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */