/*     */ package ch.qos.logback.core.net.ssl;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import ch.qos.logback.core.util.StringCollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public class SSLParametersConfiguration
/*     */   extends ContextAwareBase
/*     */ {
/*     */   private String includedProtocols;
/*     */   private String excludedProtocols;
/*     */   private String includedCipherSuites;
/*     */   private String excludedCipherSuites;
/*     */   private Boolean needClientAuth;
/*     */   private Boolean wantClientAuth;
/*     */   private String[] enabledProtocols;
/*     */   private String[] enabledCipherSuites;
/*     */   
/*     */   public void configure(SSLConfigurable socket)
/*     */   {
/*  49 */     socket.setEnabledProtocols(enabledProtocols(socket.getSupportedProtocols(), socket.getDefaultProtocols()));
/*  50 */     socket.setEnabledCipherSuites(enabledCipherSuites(socket.getSupportedCipherSuites(), socket.getDefaultCipherSuites()));
/*  51 */     if (isNeedClientAuth() != null) {
/*  52 */       socket.setNeedClientAuth(isNeedClientAuth().booleanValue());
/*     */     }
/*  54 */     if (isWantClientAuth() != null) {
/*  55 */       socket.setWantClientAuth(isWantClientAuth().booleanValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] enabledProtocols(String[] supportedProtocols, String[] defaultProtocols)
/*     */   {
/*  66 */     if (this.enabledProtocols == null)
/*     */     {
/*     */ 
/*  69 */       if ((OptionHelper.isEmpty(getIncludedProtocols())) && (OptionHelper.isEmpty(getExcludedProtocols()))) {
/*  70 */         this.enabledProtocols = ((String[])Arrays.copyOf(defaultProtocols, defaultProtocols.length));
/*     */       } else {
/*  72 */         this.enabledProtocols = includedStrings(supportedProtocols, getIncludedProtocols(), getExcludedProtocols());
/*     */       }
/*  74 */       for (String protocol : this.enabledProtocols) {
/*  75 */         addInfo("enabled protocol: " + protocol);
/*     */       }
/*     */     }
/*  78 */     return this.enabledProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] enabledCipherSuites(String[] supportedCipherSuites, String[] defaultCipherSuites)
/*     */   {
/*  88 */     if (this.enabledCipherSuites == null)
/*     */     {
/*     */ 
/*  91 */       if ((OptionHelper.isEmpty(getIncludedCipherSuites())) && (OptionHelper.isEmpty(getExcludedCipherSuites()))) {
/*  92 */         this.enabledCipherSuites = ((String[])Arrays.copyOf(defaultCipherSuites, defaultCipherSuites.length));
/*     */       } else {
/*  94 */         this.enabledCipherSuites = includedStrings(supportedCipherSuites, getIncludedCipherSuites(), getExcludedCipherSuites());
/*     */       }
/*  96 */       for (String cipherSuite : this.enabledCipherSuites) {
/*  97 */         addInfo("enabled cipher suite: " + cipherSuite);
/*     */       }
/*     */     }
/* 100 */     return this.enabledCipherSuites;
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
/*     */   private String[] includedStrings(String[] defaults, String included, String excluded)
/*     */   {
/* 114 */     List<String> values = new ArrayList(defaults.length);
/* 115 */     values.addAll(Arrays.asList(defaults));
/* 116 */     if (included != null) {
/* 117 */       StringCollectionUtil.retainMatching(values, stringToArray(included));
/*     */     }
/* 119 */     if (excluded != null) {
/* 120 */       StringCollectionUtil.removeMatching(values, stringToArray(excluded));
/*     */     }
/* 122 */     return (String[])values.toArray(new String[values.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] stringToArray(String s)
/*     */   {
/* 131 */     return s.split("\\s*,\\s*");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIncludedProtocols()
/*     */   {
/* 140 */     return this.includedProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIncludedProtocols(String protocols)
/*     */   {
/* 150 */     this.includedProtocols = protocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExcludedProtocols()
/*     */   {
/* 159 */     return this.excludedProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExcludedProtocols(String protocols)
/*     */   {
/* 169 */     this.excludedProtocols = protocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIncludedCipherSuites()
/*     */   {
/* 178 */     return this.includedCipherSuites;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIncludedCipherSuites(String cipherSuites)
/*     */   {
/* 188 */     this.includedCipherSuites = cipherSuites;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExcludedCipherSuites()
/*     */   {
/* 197 */     return this.excludedCipherSuites;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExcludedCipherSuites(String cipherSuites)
/*     */   {
/* 207 */     this.excludedCipherSuites = cipherSuites;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean isNeedClientAuth()
/*     */   {
/* 215 */     return this.needClientAuth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNeedClientAuth(Boolean needClientAuth)
/*     */   {
/* 223 */     this.needClientAuth = needClientAuth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean isWantClientAuth()
/*     */   {
/* 231 */     return this.wantClientAuth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWantClientAuth(Boolean wantClientAuth)
/*     */   {
/* 239 */     this.wantClientAuth = wantClientAuth;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\ssl\SSLParametersConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */