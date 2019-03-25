/*    */ package org.springframework.boot;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.lang.management.RuntimeMXBean;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ public class ApplicationPid
/*    */ {
/*    */   private final String pid;
/*    */   
/*    */   public ApplicationPid()
/*    */   {
/* 37 */     this.pid = getPid();
/*    */   }
/*    */   
/*    */   protected ApplicationPid(String pid) {
/* 41 */     this.pid = pid;
/*    */   }
/*    */   
/*    */   private String getPid() {
/*    */     try {
/* 46 */       String jvmName = ManagementFactory.getRuntimeMXBean().getName();
/* 47 */       return jvmName.split("@")[0];
/*    */     }
/*    */     catch (Throwable ex) {}
/* 50 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 56 */     return this.pid == null ? "???" : this.pid;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 61 */     return ObjectUtils.nullSafeHashCode(this.pid);
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 66 */     if (obj == this) {
/* 67 */       return true;
/*    */     }
/* 69 */     if ((obj != null) && ((obj instanceof ApplicationPid))) {
/* 70 */       return ObjectUtils.nullSafeEquals(this.pid, ((ApplicationPid)obj).pid);
/*    */     }
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void write(File file)
/*    */     throws java.io.IOException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 3	org/springframework/boot/ApplicationPid:pid	Ljava/lang/String;
/*    */     //   4: ifnull +7 -> 11
/*    */     //   7: iconst_1
/*    */     //   8: goto +4 -> 12
/*    */     //   11: iconst_0
/*    */     //   12: ldc 13
/*    */     //   14: invokestatic 14	org/springframework/util/Assert:state	(ZLjava/lang/String;)V
/*    */     //   17: aload_0
/*    */     //   18: aload_1
/*    */     //   19: invokespecial 15	org/springframework/boot/ApplicationPid:createParentFolder	(Ljava/io/File;)V
/*    */     //   22: new 16	java/io/FileWriter
/*    */     //   25: dup
/*    */     //   26: aload_1
/*    */     //   27: invokespecial 17	java/io/FileWriter:<init>	(Ljava/io/File;)V
/*    */     //   30: astore_2
/*    */     //   31: aload_2
/*    */     //   32: aload_0
/*    */     //   33: getfield 3	org/springframework/boot/ApplicationPid:pid	Ljava/lang/String;
/*    */     //   36: invokevirtual 18	java/io/FileWriter:append	(Ljava/lang/CharSequence;)Ljava/io/Writer;
/*    */     //   39: pop
/*    */     //   40: aload_2
/*    */     //   41: invokevirtual 19	java/io/FileWriter:close	()V
/*    */     //   44: goto +10 -> 54
/*    */     //   47: astore_3
/*    */     //   48: aload_2
/*    */     //   49: invokevirtual 19	java/io/FileWriter:close	()V
/*    */     //   52: aload_3
/*    */     //   53: athrow
/*    */     //   54: return
/*    */     // Line number table:
/*    */     //   Java source line #82	-> byte code offset #0
/*    */     //   Java source line #83	-> byte code offset #17
/*    */     //   Java source line #84	-> byte code offset #22
/*    */     //   Java source line #86	-> byte code offset #31
/*    */     //   Java source line #89	-> byte code offset #40
/*    */     //   Java source line #90	-> byte code offset #44
/*    */     //   Java source line #89	-> byte code offset #47
/*    */     //   Java source line #91	-> byte code offset #54
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	55	0	this	ApplicationPid
/*    */     //   0	55	1	file	File
/*    */     //   30	19	2	writer	java.io.FileWriter
/*    */     //   47	6	3	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   31	40	47	finally
/*    */   }
/*    */   
/*    */   private void createParentFolder(File file)
/*    */   {
/* 94 */     File parent = file.getParentFile();
/* 95 */     if (parent != null) {
/* 96 */       parent.mkdirs();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ApplicationPid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */