/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.naming.StringManager;
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
/*     */ public final class PasswdUserDatabase
/*     */   implements UserDatabase
/*     */ {
/*  36 */   private static final Log log = LogFactory.getLog(PasswdUserDatabase.class);
/*  37 */   private static final StringManager sm = StringManager.getManager(PasswdUserDatabase.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String PASSWORD_FILE = "/etc/passwd";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private final Hashtable<String, String> homes = new Hashtable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private UserConfig userConfig = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UserConfig getUserConfig()
/*     */   {
/*  62 */     return this.userConfig;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserConfig(UserConfig userConfig)
/*     */   {
/*  73 */     this.userConfig = userConfig;
/*  74 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHome(String user)
/*     */   {
/*  85 */     return (String)this.homes.get(user);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getUsers()
/*     */   {
/*  94 */     return this.homes.keys();
/*     */   }
/*     */   
/*     */ 
/*     */   private void init()
/*     */   {
/*     */     try
/*     */     {
/* 102 */       BufferedReader reader = new BufferedReader(new FileReader("/etc/passwd"));Throwable localThrowable3 = null;
/* 103 */       try { String line = reader.readLine();
/* 104 */         while (line != null) {
/* 105 */           String[] tokens = line.split(":");
/*     */           
/* 107 */           if ((tokens.length > 5) && (tokens[0].length() > 0) && (tokens[5].length() > 0))
/*     */           {
/* 109 */             this.homes.put(tokens[0], tokens[5]);
/*     */           }
/* 111 */           line = reader.readLine();
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 102 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 113 */         if (reader != null) if (localThrowable3 != null) try { reader.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else reader.close();
/* 114 */       } } catch (Exception e) { log.warn(sm.getString("passwdUserDatabase.readFail"), e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\PasswdUserDatabase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */