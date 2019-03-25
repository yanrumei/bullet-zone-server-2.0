/*    */ package ch.qos.logback.core.rolling;
/*    */ 
/*    */ import ch.qos.logback.core.util.FileSize;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SizeAndTimeBasedRollingPolicy<E>
/*    */   extends TimeBasedRollingPolicy<E>
/*    */ {
/*    */   FileSize maxFileSize;
/*    */   
/*    */   public void start()
/*    */   {
/* 14 */     SizeAndTimeBasedFNATP<E> sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP(SizeAndTimeBasedFNATP.Usage.EMBEDDED);
/* 15 */     if (this.maxFileSize == null) {
/* 16 */       addError("maxFileSize property is mandatory.");
/* 17 */       return;
/*    */     }
/* 19 */     addInfo("Archive files will be limited to [" + this.maxFileSize + "] each.");
/*    */     
/*    */ 
/* 22 */     sizeAndTimeBasedFNATP.setMaxFileSize(this.maxFileSize);
/* 23 */     this.timeBasedFileNamingAndTriggeringPolicy = sizeAndTimeBasedFNATP;
/*    */     
/* 25 */     if ((!isUnboundedTotalSizeCap()) && (this.totalSizeCap.getSize() < this.maxFileSize.getSize())) {
/* 26 */       addError("totalSizeCap of [" + this.totalSizeCap + "] is smaller than maxFileSize [" + this.maxFileSize + "] which is non-sensical");
/* 27 */       return;
/*    */     }
/*    */     
/*    */ 
/* 31 */     super.start();
/*    */   }
/*    */   
/*    */   public void setMaxFileSize(FileSize aMaxFileSize)
/*    */   {
/* 36 */     this.maxFileSize = aMaxFileSize;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 41 */     return "c.q.l.core.rolling.SizeAndTimeBasedRollingPolicy@" + hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\rolling\SizeAndTimeBasedRollingPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */