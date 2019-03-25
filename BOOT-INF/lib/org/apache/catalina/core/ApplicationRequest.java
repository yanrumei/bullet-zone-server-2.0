/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletRequestWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ApplicationRequest
/*     */   extends ServletRequestWrapper
/*     */ {
/*  54 */   protected static final String[] specials = { "javax.servlet.include.request_uri", "javax.servlet.include.context_path", "javax.servlet.include.servlet_path", "javax.servlet.include.path_info", "javax.servlet.include.query_string", "javax.servlet.forward.request_uri", "javax.servlet.forward.context_path", "javax.servlet.forward.servlet_path", "javax.servlet.forward.path_info", "javax.servlet.forward.query_string" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplicationRequest(ServletRequest request)
/*     */   {
/*  77 */     super(request);
/*  78 */     setRequest(request);
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
/*  90 */   protected final HashMap<String, Object> attributes = new HashMap();
/*     */   
/*     */   /* Error */
/*     */   public Object getAttribute(String name)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/catalina/core/ApplicationRequest:attributes	Ljava/util/HashMap;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/catalina/core/ApplicationRequest:attributes	Ljava/util/HashMap;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 6	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   15: aload_2
/*     */     //   16: monitorexit
/*     */     //   17: areturn
/*     */     //   18: astore_3
/*     */     //   19: aload_2
/*     */     //   20: monitorexit
/*     */     //   21: aload_3
/*     */     //   22: athrow
/*     */     // Line number table:
/*     */     //   Java source line #104	-> byte code offset #0
/*     */     //   Java source line #105	-> byte code offset #7
/*     */     //   Java source line #106	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	23	0	this	ApplicationRequest
/*     */     //   0	23	1	name	String
/*     */     //   5	15	2	Ljava/lang/Object;	Object
/*     */     //   18	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	17	18	finally
/*     */     //   18	21	18	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Enumeration<String> getAttributeNames()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/catalina/core/ApplicationRequest:attributes	Ljava/util/HashMap;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	org/apache/catalina/core/ApplicationRequest:attributes	Ljava/util/HashMap;
/*     */     //   11: invokevirtual 7	java/util/HashMap:keySet	()Ljava/util/Set;
/*     */     //   14: invokestatic 8	java/util/Collections:enumeration	(Ljava/util/Collection;)Ljava/util/Enumeration;
/*     */     //   17: aload_1
/*     */     //   18: monitorexit
/*     */     //   19: areturn
/*     */     //   20: astore_2
/*     */     //   21: aload_1
/*     */     //   22: monitorexit
/*     */     //   23: aload_2
/*     */     //   24: athrow
/*     */     // Line number table:
/*     */     //   Java source line #118	-> byte code offset #0
/*     */     //   Java source line #119	-> byte code offset #7
/*     */     //   Java source line #120	-> byte code offset #20
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	25	0	this	ApplicationRequest
/*     */     //   5	17	1	Ljava/lang/Object;	Object
/*     */     //   20	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	19	20	finally
/*     */     //   20	23	20	finally
/*     */   }
/*     */   
/*     */   public void removeAttribute(String name)
/*     */   {
/* 134 */     synchronized (this.attributes) {
/* 135 */       this.attributes.remove(name);
/* 136 */       if (!isSpecial(name)) {
/* 137 */         getRequest().removeAttribute(name);
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
/*     */   public void setAttribute(String name, Object value)
/*     */   {
/* 153 */     synchronized (this.attributes) {
/* 154 */       this.attributes.put(name, value);
/* 155 */       if (!isSpecial(name)) {
/* 156 */         getRequest().setAttribute(name, value);
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
/*     */   public void setRequest(ServletRequest request)
/*     */   {
/* 173 */     super.setRequest(request);
/*     */     
/*     */ 
/* 176 */     synchronized (this.attributes) {
/* 177 */       this.attributes.clear();
/* 178 */       Enumeration<String> names = request.getAttributeNames();
/* 179 */       while (names.hasMoreElements()) {
/* 180 */         String name = (String)names.nextElement();
/* 181 */         Object value = request.getAttribute(name);
/* 182 */         this.attributes.put(name, value);
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
/*     */   protected boolean isSpecial(String name)
/*     */   {
/* 200 */     for (int i = 0; i < specials.length; i++) {
/* 201 */       if (specials[i].equals(name))
/* 202 */         return true;
/*     */     }
/* 204 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */