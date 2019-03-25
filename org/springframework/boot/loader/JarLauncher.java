/*    */ package org.springframework.boot.loader;
/*    */ 
/*    */ import org.springframework.boot.loader.archive.Archive;
/*    */ import org.springframework.boot.loader.archive.Archive.Entry;
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
/*    */ public class JarLauncher
/*    */   extends ExecutableArchiveLauncher
/*    */ {
/*    */   static final String BOOT_INF_CLASSES = "BOOT-INF/classes/";
/*    */   static final String BOOT_INF_LIB = "BOOT-INF/lib/";
/*    */   
/*    */   public JarLauncher() {}
/*    */   
/*    */   protected JarLauncher(Archive archive)
/*    */   {
/* 39 */     super(archive);
/*    */   }
/*    */   
/*    */   protected boolean isNestedArchive(Archive.Entry entry)
/*    */   {
/* 44 */     if (entry.isDirectory()) {
/* 45 */       return entry.getName().equals("BOOT-INF/classes/");
/*    */     }
/* 47 */     return entry.getName().startsWith("BOOT-INF/lib/");
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 51 */     new JarLauncher().launch(args);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\JarLauncher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */