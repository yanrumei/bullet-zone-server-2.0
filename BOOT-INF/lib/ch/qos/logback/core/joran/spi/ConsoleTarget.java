/*     */ package ch.qos.logback.core.joran.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ConsoleTarget
/*     */ {
/*  29 */   SystemOut("System.out", new OutputStream()), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   SystemErr("System.err", new OutputStream());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final OutputStream stream;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConsoleTarget findByName(String name)
/*     */   {
/*  74 */     for (ConsoleTarget target : ) {
/*  75 */       if (target.name.equalsIgnoreCase(name)) {
/*  76 */         return target;
/*     */       }
/*     */     }
/*  79 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ConsoleTarget(String name, OutputStream stream)
/*     */   {
/*  86 */     this.name = name;
/*  87 */     this.stream = stream;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  91 */     return this.name;
/*     */   }
/*     */   
/*     */   public OutputStream getStream() {
/*  95 */     return this.stream;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 100 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\spi\ConsoleTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */