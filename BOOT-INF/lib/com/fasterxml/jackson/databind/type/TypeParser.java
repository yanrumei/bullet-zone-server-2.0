/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeParser
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final TypeFactory _factory;
/*     */   
/*     */   public TypeParser(TypeFactory f)
/*     */   {
/*  19 */     this._factory = f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeParser withFactory(TypeFactory f)
/*     */   {
/*  26 */     return f == this._factory ? this : new TypeParser(f);
/*     */   }
/*     */   
/*     */   public JavaType parse(String canonical) throws IllegalArgumentException
/*     */   {
/*  31 */     canonical = canonical.trim();
/*  32 */     MyTokenizer tokens = new MyTokenizer(canonical);
/*  33 */     JavaType type = parseType(tokens);
/*     */     
/*  35 */     if (tokens.hasMoreTokens()) {
/*  36 */       throw _problem(tokens, "Unexpected tokens after complete type");
/*     */     }
/*  38 */     return type;
/*     */   }
/*     */   
/*     */   protected JavaType parseType(MyTokenizer tokens)
/*     */     throws IllegalArgumentException
/*     */   {
/*  44 */     if (!tokens.hasMoreTokens()) {
/*  45 */       throw _problem(tokens, "Unexpected end-of-string");
/*     */     }
/*  47 */     Class<?> base = findClass(tokens.nextToken(), tokens);
/*     */     
/*     */ 
/*  50 */     if (tokens.hasMoreTokens()) {
/*  51 */       String token = tokens.nextToken();
/*  52 */       if ("<".equals(token)) {
/*  53 */         List<JavaType> parameterTypes = parseTypes(tokens);
/*  54 */         TypeBindings b = TypeBindings.create(base, parameterTypes);
/*  55 */         return this._factory._fromClass(null, base, b);
/*     */       }
/*     */       
/*  58 */       tokens.pushBack(token);
/*     */     }
/*  60 */     return this._factory._fromClass(null, base, null);
/*     */   }
/*     */   
/*     */   protected List<JavaType> parseTypes(MyTokenizer tokens)
/*     */     throws IllegalArgumentException
/*     */   {
/*  66 */     ArrayList<JavaType> types = new ArrayList();
/*  67 */     while (tokens.hasMoreTokens()) {
/*  68 */       types.add(parseType(tokens));
/*  69 */       if (!tokens.hasMoreTokens()) break;
/*  70 */       String token = tokens.nextToken();
/*  71 */       if (">".equals(token)) return types;
/*  72 */       if (!",".equals(token)) {
/*  73 */         throw _problem(tokens, "Unexpected token '" + token + "', expected ',' or '>')");
/*     */       }
/*     */     }
/*  76 */     throw _problem(tokens, "Unexpected end-of-string");
/*     */   }
/*     */   
/*     */   protected Class<?> findClass(String className, MyTokenizer tokens)
/*     */   {
/*     */     try {
/*  82 */       return this._factory.findClass(className);
/*     */     } catch (Exception e) {
/*  84 */       if ((e instanceof RuntimeException)) {
/*  85 */         throw ((RuntimeException)e);
/*     */       }
/*  87 */       throw _problem(tokens, "Can not locate class '" + className + "', problem: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   protected IllegalArgumentException _problem(MyTokenizer tokens, String msg)
/*     */   {
/*  93 */     return new IllegalArgumentException("Failed to parse type '" + tokens.getAllInput() + "' (remaining: '" + tokens.getRemainingInput() + "'): " + msg);
/*     */   }
/*     */   
/*     */ 
/*     */   static final class MyTokenizer
/*     */     extends StringTokenizer
/*     */   {
/*     */     protected final String _input;
/*     */     
/*     */     protected int _index;
/*     */     protected String _pushbackToken;
/*     */     
/*     */     public MyTokenizer(String str)
/*     */     {
/* 107 */       super("<,>", true);
/* 108 */       this._input = str;
/*     */     }
/*     */     
/*     */     public boolean hasMoreTokens()
/*     */     {
/* 113 */       return (this._pushbackToken != null) || (super.hasMoreTokens());
/*     */     }
/*     */     
/*     */     public String nextToken()
/*     */     {
/*     */       String token;
/* 119 */       if (this._pushbackToken != null) {
/* 120 */         String token = this._pushbackToken;
/* 121 */         this._pushbackToken = null;
/*     */       } else {
/* 123 */         token = super.nextToken();
/*     */       }
/* 125 */       this._index += token.length();
/* 126 */       return token;
/*     */     }
/*     */     
/*     */     public void pushBack(String token) {
/* 130 */       this._pushbackToken = token;
/* 131 */       this._index -= token.length();
/*     */     }
/*     */     
/* 134 */     public String getAllInput() { return this._input; }
/* 135 */     public String getUsedInput() { return this._input.substring(0, this._index); }
/* 136 */     public String getRemainingInput() { return this._input.substring(this._index); }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\type\TypeParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */