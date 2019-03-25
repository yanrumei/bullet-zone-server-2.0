/*     */ package org.springframework.boot.context.embedded.tomcat;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ final class TldSkipPatterns
/*     */ {
/*     */   private static final Set<String> TOMCAT;
/*     */   private static final Set<String> ADDITIONAL;
/*     */   static final Set<String> DEFAULT;
/*     */   
/*     */   static
/*     */   {
/*  34 */     Set<String> patterns = new LinkedHashSet();
/*  35 */     patterns.add("ant-*.jar");
/*  36 */     patterns.add("aspectj*.jar");
/*  37 */     patterns.add("commons-beanutils*.jar");
/*  38 */     patterns.add("commons-codec*.jar");
/*  39 */     patterns.add("commons-collections*.jar");
/*  40 */     patterns.add("commons-dbcp*.jar");
/*  41 */     patterns.add("commons-digester*.jar");
/*  42 */     patterns.add("commons-fileupload*.jar");
/*  43 */     patterns.add("commons-httpclient*.jar");
/*  44 */     patterns.add("commons-io*.jar");
/*  45 */     patterns.add("commons-lang*.jar");
/*  46 */     patterns.add("commons-logging*.jar");
/*  47 */     patterns.add("commons-math*.jar");
/*  48 */     patterns.add("commons-pool*.jar");
/*  49 */     patterns.add("geronimo-spec-jaxrpc*.jar");
/*  50 */     patterns.add("h2*.jar");
/*  51 */     patterns.add("hamcrest*.jar");
/*  52 */     patterns.add("hibernate*.jar");
/*  53 */     patterns.add("jmx*.jar");
/*  54 */     patterns.add("jmx-tools-*.jar");
/*  55 */     patterns.add("jta*.jar");
/*  56 */     patterns.add("junit-*.jar");
/*  57 */     patterns.add("httpclient*.jar");
/*  58 */     patterns.add("log4j-*.jar");
/*  59 */     patterns.add("mail*.jar");
/*  60 */     patterns.add("org.hamcrest*.jar");
/*  61 */     patterns.add("slf4j*.jar");
/*  62 */     patterns.add("tomcat-embed-core-*.jar");
/*  63 */     patterns.add("tomcat-embed-logging-*.jar");
/*  64 */     patterns.add("tomcat-jdbc-*.jar");
/*  65 */     patterns.add("tomcat-juli-*.jar");
/*  66 */     patterns.add("tools.jar");
/*  67 */     patterns.add("wsdl4j*.jar");
/*  68 */     patterns.add("xercesImpl-*.jar");
/*  69 */     patterns.add("xmlParserAPIs-*.jar");
/*  70 */     patterns.add("xml-apis-*.jar");
/*  71 */     TOMCAT = Collections.unmodifiableSet(patterns);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */     Set<String> patterns = new LinkedHashSet();
/*  79 */     patterns.add("antlr-*.jar");
/*  80 */     patterns.add("aopalliance-*.jar");
/*  81 */     patterns.add("aspectjrt-*.jar");
/*  82 */     patterns.add("aspectjweaver-*.jar");
/*  83 */     patterns.add("classmate-*.jar");
/*  84 */     patterns.add("dom4j-*.jar");
/*  85 */     patterns.add("ecj-*.jar");
/*  86 */     patterns.add("ehcache-core-*.jar");
/*  87 */     patterns.add("hibernate-core-*.jar");
/*  88 */     patterns.add("hibernate-commons-annotations-*.jar");
/*  89 */     patterns.add("hibernate-entitymanager-*.jar");
/*  90 */     patterns.add("hibernate-jpa-2.1-api-*.jar");
/*  91 */     patterns.add("hibernate-validator-*.jar");
/*  92 */     patterns.add("hsqldb-*.jar");
/*  93 */     patterns.add("jackson-annotations-*.jar");
/*  94 */     patterns.add("jackson-core-*.jar");
/*  95 */     patterns.add("jackson-databind-*.jar");
/*  96 */     patterns.add("jandex-*.jar");
/*  97 */     patterns.add("javassist-*.jar");
/*  98 */     patterns.add("jboss-logging-*.jar");
/*  99 */     patterns.add("jboss-transaction-api_*.jar");
/* 100 */     patterns.add("jcl-over-slf4j-*.jar");
/* 101 */     patterns.add("jdom-*.jar");
/* 102 */     patterns.add("jul-to-slf4j-*.jar");
/* 103 */     patterns.add("log4j-over-slf4j-*.jar");
/* 104 */     patterns.add("logback-classic-*.jar");
/* 105 */     patterns.add("logback-core-*.jar");
/* 106 */     patterns.add("rome-*.jar");
/* 107 */     patterns.add("slf4j-api-*.jar");
/* 108 */     patterns.add("spring-aop-*.jar");
/* 109 */     patterns.add("spring-aspects-*.jar");
/* 110 */     patterns.add("spring-beans-*.jar");
/* 111 */     patterns.add("spring-boot-*.jar");
/* 112 */     patterns.add("spring-core-*.jar");
/* 113 */     patterns.add("spring-context-*.jar");
/* 114 */     patterns.add("spring-data-*.jar");
/* 115 */     patterns.add("spring-expression-*.jar");
/* 116 */     patterns.add("spring-jdbc-*.jar,");
/* 117 */     patterns.add("spring-orm-*.jar");
/* 118 */     patterns.add("spring-oxm-*.jar");
/* 119 */     patterns.add("spring-tx-*.jar");
/* 120 */     patterns.add("snakeyaml-*.jar");
/* 121 */     patterns.add("tomcat-embed-el-*.jar");
/* 122 */     patterns.add("validation-api-*.jar");
/* 123 */     patterns.add("xml-apis-*.jar");
/* 124 */     ADDITIONAL = Collections.unmodifiableSet(patterns);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */     Set<String> patterns = new LinkedHashSet();
/* 131 */     patterns.addAll(TOMCAT);
/* 132 */     patterns.addAll(ADDITIONAL);
/* 133 */     DEFAULT = Collections.unmodifiableSet(patterns);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TldSkipPatterns.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */