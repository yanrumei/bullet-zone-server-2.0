/*     */ package org.apache.tomcat.util.digester;
/*     */ 
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.IntrospectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SetNextRule
/*     */   extends Rule
/*     */ {
/*     */   public SetNextRule(String methodName, String paramType)
/*     */   {
/*  51 */     this.methodName = methodName;
/*  52 */     this.paramType = paramType;
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
/*  63 */   protected String methodName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   protected String paramType = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   protected boolean useExactMatch = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isExactMatch()
/*     */   {
/* 101 */     return this.useExactMatch;
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
/*     */   public void setExactMatch(boolean useExactMatch)
/*     */   {
/* 114 */     this.useExactMatch = useExactMatch;
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
/*     */   public void end(String namespace, String name)
/*     */     throws Exception
/*     */   {
/* 130 */     Object child = this.digester.peek(0);
/* 131 */     Object parent = this.digester.peek(1);
/* 132 */     if (this.digester.log.isDebugEnabled()) {
/* 133 */       if (parent == null) {
/* 134 */         this.digester.log.debug("[SetNextRule]{" + this.digester.match + "} Call [NULL PARENT]." + this.methodName + "(" + child + ")");
/*     */       }
/*     */       else
/*     */       {
/* 138 */         this.digester.log.debug("[SetNextRule]{" + this.digester.match + "} Call " + parent
/* 139 */           .getClass().getName() + "." + this.methodName + "(" + child + ")");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 145 */     IntrospectionUtils.callMethod1(parent, this.methodName, child, this.paramType, this.digester
/* 146 */       .getClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 157 */     StringBuilder sb = new StringBuilder("SetNextRule[");
/* 158 */     sb.append("methodName=");
/* 159 */     sb.append(this.methodName);
/* 160 */     sb.append(", paramType=");
/* 161 */     sb.append(this.paramType);
/* 162 */     sb.append("]");
/* 163 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\SetNextRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */