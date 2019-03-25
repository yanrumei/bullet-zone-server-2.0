/*     */ package org.apache.tomcat.util.digester;
/*     */ 
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.IntrospectionUtils;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CallMethodRule
/*     */   extends Rule
/*     */ {
/*     */   public CallMethodRule(String methodName, int paramCount)
/*     */   {
/*  81 */     this(0, methodName, paramCount);
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
/*     */   public CallMethodRule(int targetOffset, String methodName, int paramCount)
/*     */   {
/* 100 */     this.targetOffset = targetOffset;
/* 101 */     this.methodName = methodName;
/* 102 */     this.paramCount = paramCount;
/* 103 */     if (paramCount == 0) {
/* 104 */       this.paramTypes = new Class[] { String.class };
/*     */     } else {
/* 106 */       this.paramTypes = new Class[paramCount];
/* 107 */       for (int i = 0; i < this.paramTypes.length; i++) {
/* 108 */         this.paramTypes[i] = String.class;
/*     */       }
/*     */     }
/* 111 */     this.paramClassNames = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallMethodRule(String methodName)
/*     */   {
/* 122 */     this(0, methodName, 0, (Class[])null);
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
/*     */   public CallMethodRule(int targetOffset, String methodName, int paramCount, Class<?>[] paramTypes)
/*     */   {
/* 152 */     this.targetOffset = targetOffset;
/* 153 */     this.methodName = methodName;
/* 154 */     this.paramCount = paramCount;
/* 155 */     if (paramTypes == null) {
/* 156 */       this.paramTypes = new Class[paramCount];
/* 157 */       for (int i = 0; i < this.paramTypes.length; i++) {
/* 158 */         this.paramTypes[i] = String.class;
/*     */       }
/*     */     } else {
/* 161 */       this.paramTypes = new Class[paramTypes.length];
/* 162 */       for (int i = 0; i < this.paramTypes.length; i++) {
/* 163 */         this.paramTypes[i] = paramTypes[i];
/*     */       }
/*     */     }
/* 166 */     this.paramClassNames = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 176 */   protected String bodyText = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int targetOffset;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String methodName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int paramCount;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 203 */   protected Class<?>[] paramTypes = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected final String[] paramClassNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */   protected boolean useExactMatch = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getUseExactMatch()
/*     */   {
/* 227 */     return this.useExactMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseExactMatch(boolean useExactMatch)
/*     */   {
/* 237 */     this.useExactMatch = useExactMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDigester(Digester digester)
/*     */   {
/* 248 */     super.setDigester(digester);
/*     */     
/* 250 */     if (this.paramClassNames != null) {
/* 251 */       this.paramTypes = new Class[this.paramClassNames.length];
/* 252 */       for (int i = 0; i < this.paramClassNames.length; i++) {
/*     */         try
/*     */         {
/* 255 */           this.paramTypes[i] = digester.getClassLoader().loadClass(this.paramClassNames[i]);
/*     */         }
/*     */         catch (ClassNotFoundException e) {
/* 258 */           digester.getLogger().error("(CallMethodRule) Cannot load class " + this.paramClassNames[i], e);
/* 259 */           this.paramTypes[i] = null;
/*     */         }
/*     */       }
/*     */     }
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
/*     */   public void begin(String namespace, String name, Attributes attributes)
/*     */     throws Exception
/*     */   {
/* 280 */     if (this.paramCount > 0) {
/* 281 */       Object[] parameters = new Object[this.paramCount];
/* 282 */       for (int i = 0; i < parameters.length; i++) {
/* 283 */         parameters[i] = null;
/*     */       }
/* 285 */       this.digester.pushParams(parameters);
/*     */     }
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
/*     */   public void body(String namespace, String name, String bodyText)
/*     */     throws Exception
/*     */   {
/* 305 */     if (this.paramCount == 0) {
/* 306 */       this.bodyText = bodyText.trim();
/*     */     }
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
/*     */   public void end(String namespace, String name)
/*     */     throws Exception
/*     */   {
/* 326 */     Object[] parameters = null;
/* 327 */     if (this.paramCount > 0)
/*     */     {
/* 329 */       parameters = (Object[])this.digester.popParams();
/*     */       
/* 331 */       if (this.digester.log.isTraceEnabled()) {
/* 332 */         int i = 0; for (int size = parameters.length; i < size; i++) {
/* 333 */           this.digester.log.trace("[CallMethodRule](" + i + ")" + parameters[i]);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 341 */       if ((this.paramCount != 1) || (parameters[0] != null)) {}
/*     */ 
/*     */ 
/*     */     }
/* 345 */     else if ((this.paramTypes != null) && (this.paramTypes.length != 0))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 351 */       if (this.bodyText == null) {
/* 352 */         return;
/*     */       }
/*     */       
/* 355 */       parameters = new Object[1];
/* 356 */       parameters[0] = this.bodyText;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 362 */     Object[] paramValues = new Object[this.paramTypes.length];
/* 363 */     for (int i = 0; i < this.paramTypes.length; i++)
/*     */     {
/*     */ 
/* 366 */       if (parameters[i] != null) { if ((parameters[i] instanceof String))
/*     */         {
/*     */ 
/* 369 */           if (String.class.isAssignableFrom(this.paramTypes[i])) {}
/*     */         }
/*     */       } else {
/* 372 */         paramValues[i] = IntrospectionUtils.convert((String)parameters[i], this.paramTypes[i]); continue;
/*     */       }
/* 374 */       paramValues[i] = parameters[i];
/*     */     }
/*     */     
/*     */     Object target;
/*     */     
/*     */     Object target;
/* 380 */     if (this.targetOffset >= 0) {
/* 381 */       target = this.digester.peek(this.targetOffset);
/*     */     } else {
/* 383 */       target = this.digester.peek(this.digester.getCount() + this.targetOffset);
/*     */     }
/*     */     
/* 386 */     if (target == null) {
/* 387 */       StringBuilder sb = new StringBuilder();
/* 388 */       sb.append("[CallMethodRule]{");
/* 389 */       sb.append(this.digester.match);
/* 390 */       sb.append("} Call target is null (");
/* 391 */       sb.append("targetOffset=");
/* 392 */       sb.append(this.targetOffset);
/* 393 */       sb.append(",stackdepth=");
/* 394 */       sb.append(this.digester.getCount());
/* 395 */       sb.append(")");
/* 396 */       throw new SAXException(sb.toString());
/*     */     }
/*     */     
/*     */ 
/* 400 */     if (this.digester.log.isDebugEnabled()) {
/* 401 */       StringBuilder sb = new StringBuilder("[CallMethodRule]{");
/* 402 */       sb.append(this.digester.match);
/* 403 */       sb.append("} Call ");
/* 404 */       sb.append(target.getClass().getName());
/* 405 */       sb.append(".");
/* 406 */       sb.append(this.methodName);
/* 407 */       sb.append("(");
/* 408 */       for (int i = 0; i < paramValues.length; i++) {
/* 409 */         if (i > 0) {
/* 410 */           sb.append(",");
/*     */         }
/* 412 */         if (paramValues[i] == null) {
/* 413 */           sb.append("null");
/*     */         } else {
/* 415 */           sb.append(paramValues[i].toString());
/*     */         }
/* 417 */         sb.append("/");
/* 418 */         if (this.paramTypes[i] == null) {
/* 419 */           sb.append("null");
/*     */         } else {
/* 421 */           sb.append(this.paramTypes[i].getName());
/*     */         }
/*     */       }
/* 424 */       sb.append(")");
/* 425 */       this.digester.log.debug(sb.toString());
/*     */     }
/* 427 */     Object result = IntrospectionUtils.callMethodN(target, this.methodName, paramValues, this.paramTypes);
/*     */     
/* 429 */     processMethodCallResult(result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finish()
/*     */     throws Exception
/*     */   {
/* 439 */     this.bodyText = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void processMethodCallResult(Object result) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 459 */     StringBuilder sb = new StringBuilder("CallMethodRule[");
/* 460 */     sb.append("methodName=");
/* 461 */     sb.append(this.methodName);
/* 462 */     sb.append(", paramCount=");
/* 463 */     sb.append(this.paramCount);
/* 464 */     sb.append(", paramTypes={");
/* 465 */     if (this.paramTypes != null) {
/* 466 */       for (int i = 0; i < this.paramTypes.length; i++) {
/* 467 */         if (i > 0) {
/* 468 */           sb.append(", ");
/*     */         }
/* 470 */         sb.append(this.paramTypes[i].getName());
/*     */       }
/*     */     }
/* 473 */     sb.append("}");
/* 474 */     sb.append("]");
/* 475 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\CallMethodRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */