/*     */ package org.apache.tomcat.util.digester;
/*     */ 
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class CallParamRule
/*     */   extends Rule
/*     */ {
/*     */   protected final String attributeName;
/*     */   protected final int paramIndex;
/*     */   protected final boolean fromStack;
/*     */   protected final int stackIndex;
/*     */   protected ArrayStack<String> bodyTextStack;
/*     */   
/*     */   public CallParamRule(int paramIndex)
/*     */   {
/*  44 */     this(paramIndex, null);
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
/*     */   public CallParamRule(int paramIndex, String attributeName)
/*     */   {
/*  57 */     this(attributeName, paramIndex, 0, false);
/*     */   }
/*     */   
/*     */ 
/*     */   private CallParamRule(String attributeName, int paramIndex, int stackIndex, boolean fromStack)
/*     */   {
/*  63 */     this.attributeName = attributeName;
/*  64 */     this.paramIndex = paramIndex;
/*  65 */     this.stackIndex = stackIndex;
/*  66 */     this.fromStack = fromStack;
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
/*     */   public void begin(String namespace, String name, Attributes attributes)
/*     */     throws Exception
/*     */   {
/* 118 */     Object param = null;
/*     */     
/* 120 */     if (this.attributeName != null)
/*     */     {
/* 122 */       param = attributes.getValue(this.attributeName);
/*     */     }
/* 124 */     else if (this.fromStack)
/*     */     {
/* 126 */       param = this.digester.peek(this.stackIndex);
/*     */       
/* 128 */       if (this.digester.log.isDebugEnabled())
/*     */       {
/* 130 */         StringBuilder sb = new StringBuilder("[CallParamRule]{");
/* 131 */         sb.append(this.digester.match);
/* 132 */         sb.append("} Save from stack; from stack?").append(this.fromStack);
/* 133 */         sb.append("; object=").append(param);
/* 134 */         this.digester.log.debug(sb.toString());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */     if (param != null) {
/* 145 */       Object[] parameters = (Object[])this.digester.peekParams();
/* 146 */       parameters[this.paramIndex] = param;
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
/*     */   public void body(String namespace, String name, String bodyText)
/*     */     throws Exception
/*     */   {
/* 165 */     if ((this.attributeName == null) && (!this.fromStack))
/*     */     {
/*     */ 
/*     */ 
/* 169 */       if (this.bodyTextStack == null) {
/* 170 */         this.bodyTextStack = new ArrayStack();
/*     */       }
/* 172 */       this.bodyTextStack.push(bodyText.trim());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void end(String namespace, String name)
/*     */   {
/* 182 */     if ((this.bodyTextStack != null) && (!this.bodyTextStack.empty()))
/*     */     {
/* 184 */       Object[] parameters = (Object[])this.digester.peekParams();
/* 185 */       parameters[this.paramIndex] = this.bodyTextStack.pop();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 195 */     StringBuilder sb = new StringBuilder("CallParamRule[");
/* 196 */     sb.append("paramIndex=");
/* 197 */     sb.append(this.paramIndex);
/* 198 */     sb.append(", attributeName=");
/* 199 */     sb.append(this.attributeName);
/* 200 */     sb.append(", from stack=");
/* 201 */     sb.append(this.fromStack);
/* 202 */     sb.append("]");
/* 203 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\CallParamRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */