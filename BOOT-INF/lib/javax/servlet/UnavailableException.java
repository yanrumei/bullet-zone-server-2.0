/*     */ package javax.servlet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnavailableException
/*     */   extends ServletException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Servlet servlet;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean permanent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int seconds;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public UnavailableException(Servlet servlet, String msg)
/*     */   {
/*  61 */     super(msg);
/*  62 */     this.servlet = servlet;
/*  63 */     this.permanent = true;
/*  64 */     this.seconds = 0;
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
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public UnavailableException(int seconds, Servlet servlet, String msg)
/*     */   {
/*  83 */     super(msg);
/*  84 */     this.servlet = servlet;
/*  85 */     if (seconds <= 0) {
/*  86 */       this.seconds = -1;
/*     */     } else
/*  88 */       this.seconds = seconds;
/*  89 */     this.permanent = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UnavailableException(String msg)
/*     */   {
/* 100 */     super(msg);
/* 101 */     this.seconds = 0;
/* 102 */     this.servlet = null;
/* 103 */     this.permanent = true;
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
/*     */   public UnavailableException(String msg, int seconds)
/*     */   {
/* 126 */     super(msg);
/*     */     
/* 128 */     if (seconds <= 0) {
/* 129 */       this.seconds = -1;
/*     */     } else
/* 131 */       this.seconds = seconds;
/* 132 */     this.servlet = null;
/* 133 */     this.permanent = false;
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
/*     */   public boolean isPermanent()
/*     */   {
/* 146 */     return this.permanent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public Servlet getServlet()
/*     */   {
/* 159 */     return this.servlet;
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
/*     */   public int getUnavailableSeconds()
/*     */   {
/* 176 */     return this.permanent ? -1 : this.seconds;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\UnavailableException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */