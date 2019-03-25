/*     */ package org.apache.naming;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NamingContextBindingsEnumeration
/*     */   implements NamingEnumeration<Binding>
/*     */ {
/*     */   protected final Iterator<NamingEntry> iterator;
/*     */   private final Context ctx;
/*     */   
/*     */   public NamingContextBindingsEnumeration(Iterator<NamingEntry> entries, Context ctx)
/*     */   {
/*  43 */     this.iterator = entries;
/*  44 */     this.ctx = ctx;
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
/*     */   public Binding next()
/*     */     throws NamingException
/*     */   {
/*  71 */     return nextElementInternal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasMore()
/*     */     throws NamingException
/*     */   {
/*  81 */     return this.iterator.hasNext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws NamingException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/*  96 */     return this.iterator.hasNext();
/*     */   }
/*     */   
/*     */   public Binding nextElement()
/*     */   {
/*     */     try
/*     */     {
/* 103 */       return nextElementInternal();
/*     */     } catch (NamingException e) {
/* 105 */       throw new RuntimeException(e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   private Binding nextElementInternal() throws NamingException {
/* 110 */     NamingEntry entry = (NamingEntry)this.iterator.next();
/*     */     
/*     */     Object value;
/*     */     
/* 114 */     if ((entry.type == 2) || (entry.type == 1)) {
/*     */       try
/*     */       {
/* 117 */         value = this.ctx.lookup(new CompositeName(entry.name));
/*     */       } catch (NamingException e) { Object value;
/* 119 */         throw e;
/*     */       } catch (Exception e) {
/* 121 */         NamingException ne = new NamingException(e.getMessage());
/* 122 */         ne.initCause(e);
/* 123 */         throw ne;
/*     */       }
/*     */     } else {
/* 126 */       value = entry.value;
/*     */     }
/*     */     
/* 129 */     return new Binding(entry.name, value.getClass().getName(), value, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\NamingContextBindingsEnumeration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */