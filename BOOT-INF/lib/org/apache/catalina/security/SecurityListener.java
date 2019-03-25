/*     */ package org.apache.catalina.security;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.StringUtils;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class SecurityListener
/*     */   implements LifecycleListener
/*     */ {
/*  33 */   private static final Log log = LogFactory.getLog(SecurityListener.class);
/*     */   
/*     */ 
/*  36 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.security");
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String UMASK_PROPERTY_NAME = "org.apache.catalina.security.SecurityListener.UMASK";
/*     */   
/*     */ 
/*     */   private static final String UMASK_FORMAT = "%04o";
/*     */   
/*     */ 
/*  46 */   private final Set<String> checkedOsUsers = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private Integer minimumUmask = Integer.valueOf(7);
/*     */   
/*     */   public SecurityListener()
/*     */   {
/*  56 */     this.checkedOsUsers.add("root");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/*  63 */     if (event.getType().equals("before_init")) {
/*  64 */       doChecks();
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
/*     */   public void setCheckedOsUsers(String userNameList)
/*     */   {
/*  80 */     if ((userNameList == null) || (userNameList.length() == 0)) {
/*  81 */       this.checkedOsUsers.clear();
/*     */     } else {
/*  83 */       String[] userNames = userNameList.split(",");
/*  84 */       for (String userName : userNames) {
/*  85 */         if (userName.length() > 0) {
/*  86 */           this.checkedOsUsers.add(userName.toLowerCase(Locale.getDefault()));
/*     */         }
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
/*     */   public String getCheckedOsUsers()
/*     */   {
/* 100 */     return StringUtils.join(this.checkedOsUsers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMinimumUmask(String umask)
/*     */   {
/* 110 */     if ((umask == null) || (umask.length() == 0)) {
/* 111 */       this.minimumUmask = Integer.valueOf(0);
/*     */     } else {
/* 113 */       this.minimumUmask = Integer.valueOf(umask, 8);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMinimumUmask()
/*     */   {
/* 124 */     return String.format("%04o", new Object[] { this.minimumUmask });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doChecks()
/*     */   {
/* 132 */     checkOsUser();
/* 133 */     checkUmask();
/*     */   }
/*     */   
/*     */   protected void checkOsUser()
/*     */   {
/* 138 */     String userName = System.getProperty("user.name");
/* 139 */     if (userName != null) {
/* 140 */       String userNameLC = userName.toLowerCase(Locale.getDefault());
/*     */       
/* 142 */       if (this.checkedOsUsers.contains(userNameLC))
/*     */       {
/* 144 */         throw new Error(sm.getString("SecurityListener.checkUserWarning", new Object[] { userName }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void checkUmask()
/*     */   {
/* 152 */     String prop = System.getProperty("org.apache.catalina.security.SecurityListener.UMASK");
/* 153 */     Integer umask = null;
/* 154 */     if (prop != null) {
/*     */       try {
/* 156 */         umask = Integer.valueOf(prop, 8);
/*     */       } catch (NumberFormatException nfe) {
/* 158 */         log.warn(sm.getString("SecurityListener.checkUmaskParseFail", new Object[] { prop }));
/*     */       }
/*     */     }
/*     */     
/* 162 */     if (umask == null) {
/* 163 */       if ("\r\n".equals(System.lineSeparator()))
/*     */       {
/* 165 */         if (log.isDebugEnabled()) {
/* 166 */           log.debug(sm.getString("SecurityListener.checkUmaskSkip"));
/*     */         }
/* 168 */         return;
/*     */       }
/* 170 */       if (this.minimumUmask.intValue() > 0) {
/* 171 */         log.warn(sm.getString("SecurityListener.checkUmaskNone", new Object[] { "org.apache.catalina.security.SecurityListener.UMASK", 
/*     */         
/* 173 */           getMinimumUmask() }));
/*     */       }
/* 175 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 180 */     if ((umask.intValue() & this.minimumUmask.intValue()) != this.minimumUmask.intValue()) {
/* 181 */       throw new Error(sm.getString("SecurityListener.checkUmaskFail", new Object[] {
/* 182 */         String.format("%04o", new Object[] { umask }), getMinimumUmask() }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\security\SecurityListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */