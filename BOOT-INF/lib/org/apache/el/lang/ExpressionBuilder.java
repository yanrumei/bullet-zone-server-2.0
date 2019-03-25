/*     */ package org.apache.el.lang;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.FunctionMapper;
/*     */ import javax.el.MethodExpression;
/*     */ import javax.el.ValueExpression;
/*     */ import javax.el.VariableMapper;
/*     */ import org.apache.el.MethodExpressionImpl;
/*     */ import org.apache.el.MethodExpressionLiteral;
/*     */ import org.apache.el.ValueExpressionImpl;
/*     */ import org.apache.el.parser.AstDeferredExpression;
/*     */ import org.apache.el.parser.AstDynamicExpression;
/*     */ import org.apache.el.parser.AstFunction;
/*     */ import org.apache.el.parser.AstIdentifier;
/*     */ import org.apache.el.parser.AstLiteralExpression;
/*     */ import org.apache.el.parser.AstValue;
/*     */ import org.apache.el.parser.ELParser;
/*     */ import org.apache.el.parser.Node;
/*     */ import org.apache.el.parser.NodeVisitor;
/*     */ import org.apache.el.util.ConcurrentCache;
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
/*     */ public final class ExpressionBuilder
/*     */   implements NodeVisitor
/*     */ {
/*  51 */   private static final SynchronizedStack<ELParser> parserCache = new SynchronizedStack();
/*     */   private static final int CACHE_SIZE;
/*     */   private static final String CACHE_SIZE_PROP = "org.apache.el.ExpressionBuilder.CACHE_SIZE";
/*     */   
/*     */   static
/*     */   {
/*     */     String cacheSizeStr;
/*     */     String cacheSizeStr;
/*  59 */     if (System.getSecurityManager() == null) {
/*  60 */       cacheSizeStr = System.getProperty("org.apache.el.ExpressionBuilder.CACHE_SIZE", "5000");
/*     */     } else {
/*  62 */       cacheSizeStr = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */ 
/*     */         public String run()
/*     */         {
/*  67 */           return System.getProperty("org.apache.el.ExpressionBuilder.CACHE_SIZE", "5000");
/*     */         }
/*     */       });
/*     */     }
/*  71 */     CACHE_SIZE = Integer.parseInt(cacheSizeStr);
/*     */   }
/*     */   
/*  74 */   private static final ConcurrentCache<String, Node> expressionCache = new ConcurrentCache(CACHE_SIZE);
/*     */   
/*     */   private FunctionMapper fnMapper;
/*     */   
/*     */   private VariableMapper varMapper;
/*     */   
/*     */   private final String expression;
/*     */   
/*     */   public ExpressionBuilder(String expression, ELContext ctx)
/*     */     throws ELException
/*     */   {
/*  85 */     this.expression = expression;
/*     */     
/*  87 */     FunctionMapper ctxFn = ctx.getFunctionMapper();
/*  88 */     VariableMapper ctxVar = ctx.getVariableMapper();
/*     */     
/*  90 */     if (ctxFn != null) {
/*  91 */       this.fnMapper = new FunctionMapperFactory(ctxFn);
/*     */     }
/*  93 */     if (ctxVar != null) {
/*  94 */       this.varMapper = new VariableMapperFactory(ctxVar);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final Node createNode(String expr) throws ELException {
/*  99 */     Node n = createNodeInternal(expr);
/* 100 */     return n;
/*     */   }
/*     */   
/*     */   private static final Node createNodeInternal(String expr) throws ELException
/*     */   {
/* 105 */     if (expr == null) {
/* 106 */       throw new ELException(MessageFactory.get("error.null"));
/*     */     }
/*     */     
/* 109 */     Node n = (Node)expressionCache.get(expr);
/* 110 */     if (n == null) {
/* 111 */       ELParser parser = (ELParser)parserCache.pop();
/*     */       try {
/* 113 */         if (parser == null) {
/* 114 */           parser = new ELParser(new StringReader(expr));
/*     */         } else {
/* 116 */           parser.ReInit(new StringReader(expr));
/*     */         }
/* 118 */         n = parser.CompositeExpression();
/*     */         
/*     */ 
/* 121 */         int numChildren = n.jjtGetNumChildren();
/* 122 */         if (numChildren == 1) {
/* 123 */           n = n.jjtGetChild(0);
/*     */         } else {
/* 125 */           Class<?> type = null;
/* 126 */           Node child = null;
/* 127 */           for (int i = 0; i < numChildren; i++) {
/* 128 */             child = n.jjtGetChild(i);
/* 129 */             if (!(child instanceof AstLiteralExpression))
/*     */             {
/* 131 */               if (type == null) {
/* 132 */                 type = child.getClass();
/*     */               }
/* 134 */               else if (!type.equals(child.getClass())) {
/* 135 */                 throw new ELException(MessageFactory.get("error.mixed", new Object[] { expr }));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 142 */         if (((n instanceof AstDeferredExpression)) || ((n instanceof AstDynamicExpression)))
/*     */         {
/* 144 */           n = n.jjtGetChild(0);
/*     */         }
/* 146 */         expressionCache.put(expr, n);
/*     */       }
/*     */       catch (Exception e) {
/* 149 */         throw new ELException(MessageFactory.get("error.parseFail", new Object[] { expr }), e);
/*     */       } finally {
/* 151 */         if (parser != null) {
/* 152 */           parserCache.push(parser);
/*     */         }
/*     */       }
/*     */     }
/* 156 */     return n;
/*     */   }
/*     */   
/*     */   private void prepare(Node node) throws ELException {
/*     */     try {
/* 161 */       node.accept(this);
/*     */     } catch (Exception e) {
/* 163 */       if ((e instanceof ELException)) {
/* 164 */         throw ((ELException)e);
/*     */       }
/* 166 */       throw new ELException(e);
/*     */     }
/*     */     
/* 169 */     if ((this.fnMapper instanceof FunctionMapperFactory)) {
/* 170 */       this.fnMapper = ((FunctionMapperFactory)this.fnMapper).create();
/*     */     }
/* 172 */     if ((this.varMapper instanceof VariableMapperFactory)) {
/* 173 */       this.varMapper = ((VariableMapperFactory)this.varMapper).create();
/*     */     }
/*     */   }
/*     */   
/*     */   private Node build() throws ELException {
/* 178 */     Node n = createNodeInternal(this.expression);
/* 179 */     prepare(n);
/* 180 */     if (((n instanceof AstDeferredExpression)) || ((n instanceof AstDynamicExpression)))
/*     */     {
/* 182 */       n = n.jjtGetChild(0);
/*     */     }
/* 184 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void visit(Node node)
/*     */     throws ELException
/*     */   {
/* 194 */     if ((node instanceof AstFunction))
/*     */     {
/* 196 */       AstFunction funcNode = (AstFunction)node;
/*     */       
/* 198 */       Method m = null;
/*     */       
/* 200 */       if (this.fnMapper != null) {
/* 201 */         m = this.fnMapper.resolveFunction(funcNode.getPrefix(), funcNode
/* 202 */           .getLocalName());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 208 */       if ((m == null) && (this.varMapper != null) && 
/* 209 */         (funcNode.getPrefix().length() == 0)) {
/* 210 */         this.varMapper.resolveVariable(funcNode.getLocalName());
/* 211 */         return;
/*     */       }
/*     */       
/* 214 */       if (this.fnMapper == null) {
/* 215 */         throw new ELException(MessageFactory.get("error.fnMapper.null"));
/*     */       }
/*     */       
/* 218 */       if (m == null) {
/* 219 */         throw new ELException(MessageFactory.get("error.fnMapper.method", new Object[] {funcNode
/* 220 */           .getOutputName() }));
/*     */       }
/*     */       
/* 223 */       int methodParameterCount = m.getParameterTypes().length;
/*     */       
/* 225 */       int inputParameterCount = node.jjtGetChild(0).jjtGetNumChildren();
/* 226 */       if (((m.isVarArgs()) && (inputParameterCount < methodParameterCount - 1)) || (
/* 227 */         (!m.isVarArgs()) && (inputParameterCount != methodParameterCount))) {
/* 228 */         throw new ELException(MessageFactory.get("error.fnMapper.paramcount", new Object[] {funcNode
/* 229 */           .getOutputName(), "" + methodParameterCount, "" + node
/* 230 */           .jjtGetChild(0).jjtGetNumChildren() }));
/*     */       }
/* 232 */     } else if (((node instanceof AstIdentifier)) && (this.varMapper != null)) {
/* 233 */       String variable = ((AstIdentifier)node).getImage();
/*     */       
/*     */ 
/* 236 */       this.varMapper.resolveVariable(variable);
/*     */     }
/*     */   }
/*     */   
/*     */   public ValueExpression createValueExpression(Class<?> expectedType) throws ELException
/*     */   {
/* 242 */     Node n = build();
/* 243 */     return new ValueExpressionImpl(this.expression, n, this.fnMapper, this.varMapper, expectedType);
/*     */   }
/*     */   
/*     */   public MethodExpression createMethodExpression(Class<?> expectedReturnType, Class<?>[] expectedParamTypes)
/*     */     throws ELException
/*     */   {
/* 249 */     Node n = build();
/* 250 */     if ((!n.isParametersProvided()) && (expectedParamTypes == null))
/*     */     {
/* 252 */       throw new NullPointerException(MessageFactory.get("error.method.nullParms"));
/*     */     }
/* 254 */     if (((n instanceof AstValue)) || ((n instanceof AstIdentifier))) {
/* 255 */       return new MethodExpressionImpl(this.expression, n, this.fnMapper, this.varMapper, expectedReturnType, expectedParamTypes);
/*     */     }
/* 257 */     if ((n instanceof AstLiteralExpression)) {
/* 258 */       return new MethodExpressionLiteral(this.expression, expectedReturnType, expectedParamTypes);
/*     */     }
/*     */     
/* 261 */     throw new ELException("Not a Valid Method Expression: " + this.expression);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class SynchronizedStack<T>
/*     */   {
/*     */     public static final int DEFAULT_SIZE = 128;
/*     */     
/*     */ 
/*     */ 
/*     */     private static final int DEFAULT_LIMIT = -1;
/*     */     
/*     */ 
/*     */     private int size;
/*     */     
/*     */ 
/*     */     private final int limit;
/*     */     
/*     */ 
/* 282 */     private int index = -1;
/*     */     
/*     */     private Object[] stack;
/*     */     
/*     */     public SynchronizedStack()
/*     */     {
/* 288 */       this(128, -1);
/*     */     }
/*     */     
/*     */     public SynchronizedStack(int size, int limit) {
/* 292 */       this.size = size;
/* 293 */       this.limit = limit;
/* 294 */       this.stack = new Object[size];
/*     */     }
/*     */     
/*     */     public synchronized boolean push(T obj)
/*     */     {
/* 299 */       this.index += 1;
/* 300 */       if (this.index == this.size) {
/* 301 */         if ((this.limit == -1) || (this.size < this.limit)) {
/* 302 */           expand();
/*     */         } else {
/* 304 */           this.index -= 1;
/* 305 */           return false;
/*     */         }
/*     */       }
/* 308 */       this.stack[this.index] = obj;
/* 309 */       return true;
/*     */     }
/*     */     
/*     */     public synchronized T pop()
/*     */     {
/* 314 */       if (this.index == -1) {
/* 315 */         return null;
/*     */       }
/* 317 */       T result = this.stack[this.index];
/* 318 */       this.stack[(this.index--)] = null;
/* 319 */       return result;
/*     */     }
/*     */     
/*     */     private void expand() {
/* 323 */       int newSize = this.size * 2;
/* 324 */       if ((this.limit != -1) && (newSize > this.limit)) {
/* 325 */         newSize = this.limit;
/*     */       }
/* 327 */       Object[] newStack = new Object[newSize];
/* 328 */       System.arraycopy(this.stack, 0, newStack, 0, this.size);
/*     */       
/*     */ 
/*     */ 
/* 332 */       this.stack = newStack;
/* 333 */       this.size = newSize;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\lang\ExpressionBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */