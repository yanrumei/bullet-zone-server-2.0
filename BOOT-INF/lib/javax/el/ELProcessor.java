/*     */ package javax.el;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public class ELProcessor
/*     */ {
/*  29 */   private static final Set<String> PRIMITIVES = new HashSet();
/*     */   
/*  31 */   static { PRIMITIVES.add("boolean");
/*  32 */     PRIMITIVES.add("byte");
/*  33 */     PRIMITIVES.add("char");
/*  34 */     PRIMITIVES.add("double");
/*  35 */     PRIMITIVES.add("float");
/*  36 */     PRIMITIVES.add("int");
/*  37 */     PRIMITIVES.add("long");
/*  38 */     PRIMITIVES.add("short");
/*     */   }
/*     */   
/*  41 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */   
/*  43 */   private final ELManager manager = new ELManager();
/*  44 */   private final ELContext context = this.manager.getELContext();
/*  45 */   private final ExpressionFactory factory = ELManager.getExpressionFactory();
/*     */   
/*     */   public ELManager getELManager()
/*     */   {
/*  49 */     return this.manager;
/*     */   }
/*     */   
/*     */   public Object eval(String expression)
/*     */   {
/*  54 */     return getValue(expression, Object.class);
/*     */   }
/*     */   
/*     */   public Object getValue(String expression, Class<?> expectedType)
/*     */   {
/*  59 */     ValueExpression ve = this.factory.createValueExpression(this.context, 
/*  60 */       bracket(expression), expectedType);
/*  61 */     return ve.getValue(this.context);
/*     */   }
/*     */   
/*     */   public void setValue(String expression, Object value)
/*     */   {
/*  66 */     ValueExpression ve = this.factory.createValueExpression(this.context, 
/*  67 */       bracket(expression), Object.class);
/*  68 */     ve.setValue(this.context, value);
/*     */   }
/*     */   
/*     */   public void setVariable(String variable, String expression)
/*     */   {
/*  73 */     if (expression == null) {
/*  74 */       this.manager.setVariable(variable, null);
/*     */     } else {
/*  76 */       ValueExpression ve = this.factory.createValueExpression(this.context, 
/*  77 */         bracket(expression), Object.class);
/*  78 */       this.manager.setVariable(variable, ve);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void defineFunction(String prefix, String function, String className, String methodName)
/*     */     throws ClassNotFoundException, NoSuchMethodException
/*     */   {
/*  87 */     if ((prefix == null) || (function == null) || (className == null) || (methodName == null))
/*     */     {
/*  89 */       throw new NullPointerException(Util.message(this.context, "elProcessor.defineFunctionNullParams", new Object[0]));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  94 */     Class<?> clazz = this.context.getImportHandler().resolveClass(className);
/*     */     
/*  96 */     if (clazz == null) {
/*  97 */       clazz = Class.forName(className, true, 
/*  98 */         Thread.currentThread().getContextClassLoader());
/*     */     }
/*     */     
/* 101 */     if (!Modifier.isPublic(clazz.getModifiers())) {
/* 102 */       throw new ClassNotFoundException(Util.message(this.context, "elProcessor.defineFunctionInvalidClass", new Object[] { className }));
/*     */     }
/*     */     
/*     */ 
/* 106 */     MethodSignature sig = new MethodSignature(this.context, methodName, className);
/*     */     
/*     */ 
/* 109 */     if (function.length() == 0) {
/* 110 */       function = sig.getName();
/*     */     }
/*     */     
/* 113 */     Method[] methods = clazz.getMethods();
/* 114 */     for (Method method : methods) {
/* 115 */       if (Modifier.isStatic(method.getModifiers()))
/*     */       {
/*     */ 
/* 118 */         if (method.getName().equals(sig.getName())) {
/* 119 */           if (sig.getParamTypeNames() == null)
/*     */           {
/*     */ 
/* 122 */             this.manager.mapFunction(prefix, function, method);
/* 123 */             return;
/*     */           }
/* 125 */           if (sig.getParamTypeNames().length == method.getParameterTypes().length)
/*     */           {
/*     */ 
/* 128 */             if (sig.getParamTypeNames().length == 0) {
/* 129 */               this.manager.mapFunction(prefix, function, method);
/* 130 */               return;
/*     */             }
/* 132 */             Class<?>[] types = method.getParameterTypes();
/* 133 */             String[] typeNames = sig.getParamTypeNames();
/* 134 */             if (types.length == typeNames.length) {
/* 135 */               boolean match = true;
/* 136 */               for (int i = 0; i < types.length; i++) {
/* 137 */                 if ((i == types.length - 1) && (method.isVarArgs())) {
/* 138 */                   String typeName = typeNames[i];
/* 139 */                   if (typeName.endsWith("...")) {
/* 140 */                     typeName = typeName.substring(0, typeName.length() - 3);
/* 141 */                     if (!typeName.equals(types[i].getName())) {
/* 142 */                       match = false;
/*     */                     }
/*     */                   } else {
/* 145 */                     match = false;
/*     */                   }
/* 147 */                 } else if (!types[i].getName().equals(typeNames[i])) {
/* 148 */                   match = false;
/* 149 */                   break;
/*     */                 }
/*     */               }
/* 152 */               if (match) {
/* 153 */                 this.manager.mapFunction(prefix, function, method);
/* 154 */                 return;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 161 */     throw new NoSuchMethodException(Util.message(this.context, "elProcessor.defineFunctionNoMethod", new Object[] { methodName, className }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void defineFunction(String prefix, String function, Method method)
/*     */     throws NoSuchMethodException
/*     */   {
/* 181 */     if ((prefix == null) || (function == null) || (method == null)) {
/* 182 */       throw new NullPointerException(Util.message(this.context, "elProcessor.defineFunctionNullParams", new Object[0]));
/*     */     }
/*     */     
/*     */ 
/* 186 */     int modifiers = method.getModifiers();
/*     */     
/*     */ 
/* 189 */     if ((!Modifier.isStatic(modifiers)) || (!Modifier.isPublic(modifiers))) {
/* 190 */       throw new NoSuchMethodException(Util.message(this.context, "elProcessor.defineFunctionInvalidMethod", new Object[] {method
/* 191 */         .getName(), method
/* 192 */         .getDeclaringClass().getName() }));
/*     */     }
/*     */     
/* 195 */     this.manager.mapFunction(prefix, function, method);
/*     */   }
/*     */   
/*     */   public void defineBean(String name, Object bean)
/*     */   {
/* 200 */     this.manager.defineBean(name, bean);
/*     */   }
/*     */   
/*     */   private static String bracket(String expression)
/*     */   {
/* 205 */     return "${" + expression + "}";
/*     */   }
/*     */   
/*     */   private static class MethodSignature
/*     */   {
/*     */     private final String name;
/*     */     private final String[] parameterTypeNames;
/*     */     
/*     */     public MethodSignature(ELContext context, String methodName, String className)
/*     */       throws NoSuchMethodException
/*     */     {
/* 216 */       int paramIndex = methodName.indexOf('(');
/*     */       
/* 218 */       if (paramIndex == -1) {
/* 219 */         this.name = methodName.trim();
/* 220 */         this.parameterTypeNames = null;
/*     */       } else {
/* 222 */         String returnTypeAndName = methodName.substring(0, paramIndex).trim();
/*     */         
/*     */ 
/*     */ 
/* 226 */         int wsPos = -1;
/* 227 */         for (int i = 0; i < returnTypeAndName.length(); i++) {
/* 228 */           if (Character.isWhitespace(returnTypeAndName.charAt(i))) {
/* 229 */             wsPos = i;
/* 230 */             break;
/*     */           }
/*     */         }
/* 233 */         if (wsPos == -1) {
/* 234 */           throw new NoSuchMethodException();
/*     */         }
/* 236 */         this.name = returnTypeAndName.substring(wsPos).trim();
/*     */         
/* 238 */         String paramString = methodName.substring(paramIndex).trim();
/*     */         
/* 240 */         if (!paramString.endsWith(")")) {
/* 241 */           throw new NoSuchMethodException(Util.message(context, "elProcessor.defineFunctionInvalidParameterList", new Object[] { paramString, methodName, className }));
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 246 */         paramString = paramString.substring(1, paramString.length() - 1).trim();
/* 247 */         if (paramString.length() == 0) {
/* 248 */           this.parameterTypeNames = ELProcessor.EMPTY_STRING_ARRAY;
/*     */         } else {
/* 250 */           this.parameterTypeNames = paramString.split(",");
/* 251 */           ImportHandler importHandler = context.getImportHandler();
/* 252 */           for (int i = 0; i < this.parameterTypeNames.length; i++) {
/* 253 */             String parameterTypeName = this.parameterTypeNames[i].trim();
/* 254 */             int dimension = 0;
/* 255 */             int bracketPos = parameterTypeName.indexOf('[');
/* 256 */             if (bracketPos > -1)
/*     */             {
/* 258 */               String parameterTypeNameOnly = parameterTypeName.substring(0, bracketPos).trim();
/* 259 */               while (bracketPos > -1) {
/* 260 */                 dimension++;
/* 261 */                 bracketPos = parameterTypeName.indexOf('[', bracketPos + 1);
/*     */               }
/* 263 */               parameterTypeName = parameterTypeNameOnly;
/*     */             }
/* 265 */             boolean varArgs = false;
/* 266 */             if (parameterTypeName.endsWith("...")) {
/* 267 */               varArgs = true;
/* 268 */               dimension = 1;
/*     */               
/* 270 */               parameterTypeName = parameterTypeName.substring(0, parameterTypeName.length() - 3).trim();
/*     */             }
/* 272 */             boolean isPrimitive = ELProcessor.PRIMITIVES.contains(parameterTypeName);
/* 273 */             if ((isPrimitive) && (dimension > 0))
/*     */             {
/* 275 */               switch (parameterTypeName)
/*     */               {
/*     */               case "boolean": 
/* 278 */                 parameterTypeName = "Z";
/* 279 */                 break;
/*     */               case "byte": 
/* 281 */                 parameterTypeName = "B";
/* 282 */                 break;
/*     */               case "char": 
/* 284 */                 parameterTypeName = "C";
/* 285 */                 break;
/*     */               case "double": 
/* 287 */                 parameterTypeName = "D";
/* 288 */                 break;
/*     */               case "float": 
/* 290 */                 parameterTypeName = "F";
/* 291 */                 break;
/*     */               case "int": 
/* 293 */                 parameterTypeName = "I";
/* 294 */                 break;
/*     */               case "long": 
/* 296 */                 parameterTypeName = "J";
/* 297 */                 break;
/*     */               case "short": 
/* 299 */                 parameterTypeName = "S";
/* 300 */                 break;
/*     */               
/*     */               }
/*     */               
/*     */             }
/* 305 */             else if ((!isPrimitive) && 
/* 306 */               (!parameterTypeName.contains("."))) {
/* 307 */               Object clazz = importHandler.resolveClass(parameterTypeName);
/*     */               
/* 309 */               if (clazz == null) {
/* 310 */                 throw new NoSuchMethodException(Util.message(context, "elProcessor.defineFunctionInvalidParameterTypeName", new Object[] { this.parameterTypeNames[i], methodName, className }));
/*     */               }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 316 */               parameterTypeName = ((Class)clazz).getName();
/*     */             }
/* 318 */             if (dimension > 0)
/*     */             {
/* 320 */               StringBuilder sb = new StringBuilder();
/* 321 */               for (int j = 0; j < dimension; j++) {
/* 322 */                 sb.append('[');
/*     */               }
/* 324 */               if (!isPrimitive) {
/* 325 */                 sb.append('L');
/*     */               }
/* 327 */               sb.append(parameterTypeName);
/* 328 */               if (!isPrimitive) {
/* 329 */                 sb.append(';');
/*     */               }
/* 331 */               parameterTypeName = sb.toString();
/*     */             }
/* 333 */             if (varArgs) {
/* 334 */               parameterTypeName = parameterTypeName + "...";
/*     */             }
/* 336 */             this.parameterTypeNames[i] = parameterTypeName;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 344 */       return this.name;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String[] getParamTypeNames()
/*     */     {
/* 353 */       return this.parameterTypeNames;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ELProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */