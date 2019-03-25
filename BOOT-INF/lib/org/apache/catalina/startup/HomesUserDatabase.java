/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
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
/*     */ 
/*     */ 
/*     */ public final class HomesUserDatabase
/*     */   implements UserDatabase
/*     */ {
/*  57 */   private final Hashtable<String, String> homes = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private UserConfig userConfig = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UserConfig getUserConfig()
/*     */   {
/*  75 */     return this.userConfig;
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
/*     */   public void setUserConfig(UserConfig userConfig)
/*     */   {
/*  88 */     this.userConfig = userConfig;
/*  89 */     init();
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
/*     */   public String getHome(String user)
/*     */   {
/* 105 */     return (String)this.homes.get(user);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getUsers()
/*     */   {
/* 116 */     return this.homes.keys();
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
/*     */   private void init()
/*     */   {
/* 129 */     String homeBase = this.userConfig.getHomeBase();
/* 130 */     File homeBaseDir = new File(homeBase);
/* 131 */     if ((!homeBaseDir.exists()) || (!homeBaseDir.isDirectory()))
/* 132 */       return;
/* 133 */     String[] homeBaseFiles = homeBaseDir.list();
/* 134 */     if (homeBaseFiles == null) {
/* 135 */       return;
/*     */     }
/*     */     
/* 138 */     for (int i = 0; i < homeBaseFiles.length; i++) {
/* 139 */       File homeDir = new File(homeBaseDir, homeBaseFiles[i]);
/* 140 */       if ((homeDir.isDirectory()) && (homeDir.canRead()))
/*     */       {
/* 142 */         this.homes.put(homeBaseFiles[i], homeDir.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\HomesUserDatabase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */