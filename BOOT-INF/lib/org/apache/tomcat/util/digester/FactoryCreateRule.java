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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FactoryCreateRule
/*     */   extends Rule
/*     */ {
/*     */   private boolean ignoreCreateExceptions;
/*     */   private ArrayStack<Boolean> exceptionIgnoredStack;
/*     */   
/*     */   public FactoryCreateRule(ObjectCreationFactory creationFactory, boolean ignoreCreateExceptions)
/*     */   {
/*  59 */     this.creationFactory = creationFactory;
/*  60 */     this.ignoreCreateExceptions = ignoreCreateExceptions;
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
/*  71 */   protected ObjectCreationFactory creationFactory = null;
/*     */   
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
/*  85 */     if (this.ignoreCreateExceptions)
/*     */     {
/*  87 */       if (this.exceptionIgnoredStack == null) {
/*  88 */         this.exceptionIgnoredStack = new ArrayStack();
/*     */       }
/*     */       try
/*     */       {
/*  92 */         Object instance = this.creationFactory.createObject(attributes);
/*     */         
/*  94 */         if (this.digester.log.isDebugEnabled()) {
/*  95 */           this.digester.log.debug("[FactoryCreateRule]{" + this.digester.match + "} New " + instance
/*  96 */             .getClass().getName());
/*     */         }
/*  98 */         this.digester.push(instance);
/*  99 */         this.exceptionIgnoredStack.push(Boolean.FALSE);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 103 */         if (this.digester.log.isInfoEnabled()) {
/* 104 */           this.digester.log.info("[FactoryCreateRule] Create exception ignored: " + (e
/* 105 */             .getMessage() == null ? e.getClass().getName() : e.getMessage()));
/* 106 */           if (this.digester.log.isDebugEnabled()) {
/* 107 */             this.digester.log.debug("[FactoryCreateRule] Ignored exception:", e);
/*     */           }
/*     */         }
/* 110 */         this.exceptionIgnoredStack.push(Boolean.TRUE);
/*     */       }
/*     */     }
/*     */     else {
/* 114 */       Object instance = this.creationFactory.createObject(attributes);
/*     */       
/* 116 */       if (this.digester.log.isDebugEnabled()) {
/* 117 */         this.digester.log.debug("[FactoryCreateRule]{" + this.digester.match + "} New " + instance
/* 118 */           .getClass().getName());
/*     */       }
/* 120 */       this.digester.push(instance);
/*     */     }
/*     */   }
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
/* 133 */     if ((this.ignoreCreateExceptions) && (this.exceptionIgnoredStack != null))
/*     */     {
/*     */ 
/* 136 */       if (!this.exceptionIgnoredStack.empty())
/*     */       {
/* 138 */         if (((Boolean)this.exceptionIgnoredStack.pop()).booleanValue())
/*     */         {
/*     */ 
/* 141 */           if (this.digester.log.isTraceEnabled()) {
/* 142 */             this.digester.log.trace("[FactoryCreateRule] No creation so no push so no pop");
/*     */           }
/* 144 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 148 */     Object top = this.digester.pop();
/* 149 */     if (this.digester.log.isDebugEnabled()) {
/* 150 */       this.digester.log.debug("[FactoryCreateRule]{" + this.digester.match + "} Pop " + top
/* 151 */         .getClass().getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finish()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 172 */     StringBuilder sb = new StringBuilder("FactoryCreateRule[");
/* 173 */     if (this.creationFactory != null) {
/* 174 */       sb.append("creationFactory=");
/* 175 */       sb.append(this.creationFactory);
/*     */     }
/* 177 */     sb.append("]");
/* 178 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\FactoryCreateRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */