/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileSize
/*    */ {
/*    */   private static final String LENGTH_PART = "([0-9]+)";
/*    */   private static final int DOUBLE_GROUP = 1;
/*    */   private static final String UNIT_PART = "(|kb|mb|gb)s?";
/*    */   private static final int UNIT_GROUP = 2;
/* 40 */   private static final Pattern FILE_SIZE_PATTERN = Pattern.compile("([0-9]+)\\s*(|kb|mb|gb)s?", 2);
/*    */   
/*    */   public static final long KB_COEFFICIENT = 1024L;
/*    */   public static final long MB_COEFFICIENT = 1048576L;
/*    */   public static final long GB_COEFFICIENT = 1073741824L;
/*    */   final long size;
/*    */   
/*    */   public FileSize(long size)
/*    */   {
/* 49 */     this.size = size;
/*    */   }
/*    */   
/*    */   public long getSize() {
/* 53 */     return this.size;
/*    */   }
/*    */   
/*    */   public static FileSize valueOf(String fileSizeStr) {
/* 57 */     Matcher matcher = FILE_SIZE_PATTERN.matcher(fileSizeStr);
/*    */     
/*    */ 
/* 60 */     if (matcher.matches()) {
/* 61 */       String lenStr = matcher.group(1);
/* 62 */       String unitStr = matcher.group(2);
/*    */       
/* 64 */       long lenValue = Long.valueOf(lenStr).longValue();
/* 65 */       long coefficient; if (unitStr.equalsIgnoreCase("")) {
/* 66 */         coefficient = 1L; } else { long coefficient;
/* 67 */         if (unitStr.equalsIgnoreCase("kb")) {
/* 68 */           coefficient = 1024L; } else { long coefficient;
/* 69 */           if (unitStr.equalsIgnoreCase("mb")) {
/* 70 */             coefficient = 1048576L; } else { long coefficient;
/* 71 */             if (unitStr.equalsIgnoreCase("gb")) {
/* 72 */               coefficient = 1073741824L;
/*    */             } else
/* 74 */               throw new IllegalStateException("Unexpected " + unitStr); } } }
/*    */       long coefficient;
/* 76 */       return new FileSize(lenValue * coefficient);
/*    */     }
/* 78 */     throw new IllegalArgumentException("String value [" + fileSizeStr + "] is not in the expected format.");
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 84 */     long inKB = this.size / 1024L;
/*    */     
/* 86 */     if (inKB == 0L) {
/* 87 */       return this.size + " Bytes";
/*    */     }
/* 89 */     long inMB = this.size / 1048576L;
/* 90 */     if (inMB == 0L) {
/* 91 */       return inKB + " KB";
/*    */     }
/*    */     
/* 94 */     return inMB + " MB";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\cor\\util\FileSize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */