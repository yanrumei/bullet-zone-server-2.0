/*     */ package org.apache.tomcat.util.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerCookie
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   private final MessageBytes name = MessageBytes.newInstance();
/*  39 */   private final MessageBytes value = MessageBytes.newInstance();
/*     */   
/*  41 */   private final MessageBytes path = MessageBytes.newInstance();
/*  42 */   private final MessageBytes domain = MessageBytes.newInstance();
/*     */   
/*     */ 
/*  45 */   private final MessageBytes comment = MessageBytes.newInstance();
/*  46 */   private int version = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/*  60 */     this.name.recycle();
/*  61 */     this.value.recycle();
/*  62 */     this.comment.recycle();
/*  63 */     this.path.recycle();
/*  64 */     this.domain.recycle();
/*  65 */     this.version = 0;
/*     */   }
/*     */   
/*     */   public MessageBytes getComment() {
/*  69 */     return this.comment;
/*     */   }
/*     */   
/*     */   public MessageBytes getDomain() {
/*  73 */     return this.domain;
/*     */   }
/*     */   
/*     */   public MessageBytes getPath() {
/*  77 */     return this.path;
/*     */   }
/*     */   
/*     */   public MessageBytes getName() {
/*  81 */     return this.name;
/*     */   }
/*     */   
/*     */   public MessageBytes getValue() {
/*  85 */     return this.value;
/*     */   }
/*     */   
/*     */   public int getVersion() {
/*  89 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(int v) {
/*  93 */     this.version = v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 101 */     return 
/* 102 */       "Cookie " + getName() + "=" + getValue() + " ; " + getVersion() + " " + getPath() + " " + getDomain();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\ServerCookie.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */