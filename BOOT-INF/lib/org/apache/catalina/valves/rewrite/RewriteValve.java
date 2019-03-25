/*     */ package org.apache.catalina.valves.rewrite;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.Lifecycle;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.valves.ValveBase;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class RewriteValve
/*     */   extends ValveBase
/*     */ {
/*  62 */   protected RewriteRule[] rules = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */   protected ThreadLocal<Boolean> invoked = new ThreadLocal();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */   protected String resourcePath = "rewrite.config";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   protected boolean context = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   protected boolean enabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  93 */   protected Map<String, RewriteMap> maps = new Hashtable();
/*     */   
/*     */   public RewriteValve()
/*     */   {
/*  97 */     super(true);
/*     */   }
/*     */   
/*     */   public boolean getEnabled()
/*     */   {
/* 102 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 106 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 112 */     super.initInternal();
/* 113 */     this.containerLog = LogFactory.getLog(getContainer().getLogName() + ".rewrite");
/*     */   }
/*     */   
/*     */ 
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 120 */     super.startInternal();
/*     */     
/* 122 */     InputStream is = null;
/*     */     
/*     */     File file;
/* 125 */     if ((getContainer() instanceof Context)) {
/* 126 */       this.context = true;
/*     */       
/* 128 */       is = ((Context)getContainer()).getServletContext().getResourceAsStream("/WEB-INF/" + this.resourcePath);
/* 129 */       if (this.containerLog.isDebugEnabled()) {
/* 130 */         if (is == null) {
/* 131 */           this.containerLog.debug("No configuration resource found: /WEB-INF/" + this.resourcePath);
/*     */         } else {
/* 133 */           this.containerLog.debug("Read configuration from: /WEB-INF/" + this.resourcePath);
/*     */         }
/*     */       }
/* 136 */     } else if ((getContainer() instanceof Host)) {
/* 137 */       String resourceName = getHostConfigPath(this.resourcePath);
/* 138 */       file = new File(getConfigBase(), resourceName);
/*     */       try {
/* 140 */         if (!file.exists())
/*     */         {
/*     */ 
/* 143 */           is = getClass().getClassLoader().getResourceAsStream(resourceName);
/* 144 */           if ((is != null) && (this.containerLog.isDebugEnabled())) {
/* 145 */             this.containerLog.debug("Read configuration from CL at " + resourceName);
/*     */           }
/*     */         } else {
/* 148 */           if (this.containerLog.isDebugEnabled()) {
/* 149 */             this.containerLog.debug("Read configuration from " + file.getAbsolutePath());
/*     */           }
/* 151 */           is = new FileInputStream(file);
/*     */         }
/* 153 */         if ((is == null) && (this.containerLog.isDebugEnabled())) {
/* 154 */           this.containerLog.debug("No configuration resource found: " + resourceName + " in " + 
/* 155 */             getConfigBase() + " or in the classloader");
/*     */         }
/*     */       } catch (Exception e) {
/* 158 */         this.containerLog.error("Error opening configuration", e);
/*     */       }
/*     */     }
/*     */     
/* 162 */     if (is == null)
/*     */     {
/* 164 */       return;
/*     */     }
/*     */     try {
/* 167 */       InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);file = null;
/* 168 */       try { BufferedReader reader = new BufferedReader(isr);Throwable localThrowable6 = null;
/* 169 */         try { parse(reader);
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 167 */           localThrowable6 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { file = localThrowable4;throw localThrowable4;
/*     */       }
/*     */       finally {
/* 170 */         if (isr != null) if (file != null) try { isr.close(); } catch (Throwable localThrowable5) { file.addSuppressed(localThrowable5); } else isr.close(); }
/*     */       return; } catch (IOException ioe) { this.containerLog.error("Error closing configuration", ioe);
/*     */     } finally {
/*     */       try {
/* 174 */         is.close();
/*     */       } catch (IOException e) {
/* 176 */         this.containerLog.error("Error closing configuration", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setConfiguration(String configuration)
/*     */     throws Exception
/*     */   {
/* 184 */     if (this.containerLog == null) {
/* 185 */       this.containerLog = LogFactory.getLog(getContainer().getLogName() + ".rewrite");
/*     */     }
/* 187 */     this.maps.clear();
/* 188 */     parse(new BufferedReader(new StringReader(configuration)));
/*     */   }
/*     */   
/*     */   public String getConfiguration() {
/* 192 */     StringBuffer buffer = new StringBuffer();
/*     */     
/* 194 */     for (int i = 0; i < this.rules.length; i++) {
/* 195 */       for (int j = 0; j < this.rules[i].getConditions().length; j++) {
/* 196 */         buffer.append(this.rules[i].getConditions()[j].toString()).append("\r\n");
/*     */       }
/* 198 */       buffer.append(this.rules[i].toString()).append("\r\n").append("\r\n");
/*     */     }
/* 200 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   protected void parse(BufferedReader reader) throws LifecycleException {
/* 204 */     ArrayList<RewriteRule> rules = new ArrayList();
/* 205 */     ArrayList<RewriteCond> conditions = new ArrayList();
/*     */     try {
/*     */       for (;;) {
/* 208 */         String line = reader.readLine();
/* 209 */         if (line == null) {
/*     */           break;
/*     */         }
/* 212 */         Object result = parse(line);
/* 213 */         if ((result instanceof RewriteRule)) {
/* 214 */           RewriteRule rule = (RewriteRule)result;
/* 215 */           if (this.containerLog.isDebugEnabled()) {
/* 216 */             this.containerLog.debug("Add rule with pattern " + rule.getPatternString() + " and substitution " + rule
/* 217 */               .getSubstitutionString());
/*     */           }
/* 219 */           for (int i = conditions.size() - 1; i > 0; i--) {
/* 220 */             if (((RewriteCond)conditions.get(i - 1)).isOrnext()) {
/* 221 */               ((RewriteCond)conditions.get(i)).setOrnext(true);
/*     */             }
/*     */           }
/* 224 */           for (int i = 0; i < conditions.size(); i++) {
/* 225 */             if (this.containerLog.isDebugEnabled()) {
/* 226 */               RewriteCond cond = (RewriteCond)conditions.get(i);
/* 227 */               this.containerLog.debug("Add condition " + cond.getCondPattern() + " test " + cond
/* 228 */                 .getTestString() + " to rule with pattern " + rule
/* 229 */                 .getPatternString() + " and substitution " + rule
/* 230 */                 .getSubstitutionString() + (cond.isOrnext() ? " [OR]" : "") + (cond
/* 231 */                 .isNocase() ? " [NC]" : ""));
/*     */             }
/* 233 */             rule.addCondition((RewriteCond)conditions.get(i));
/*     */           }
/* 235 */           conditions.clear();
/* 236 */           rules.add(rule);
/* 237 */         } else if ((result instanceof RewriteCond)) {
/* 238 */           conditions.add((RewriteCond)result);
/* 239 */         } else if ((result instanceof Object[])) {
/* 240 */           String mapName = (String)((Object[])(Object[])result)[0];
/* 241 */           RewriteMap map = (RewriteMap)((Object[])(Object[])result)[1];
/* 242 */           this.maps.put(mapName, map);
/* 243 */           if ((map instanceof Lifecycle))
/* 244 */             ((Lifecycle)map).start();
/*     */         }
/*     */       }
/*     */     } catch (IOException e) {
/* 248 */       this.containerLog.error("Error reading configuration", e);
/*     */     }
/*     */     
/* 251 */     this.rules = ((RewriteRule[])rules.toArray(new RewriteRule[0]));
/*     */     
/*     */ 
/* 254 */     for (int i = 0; i < this.rules.length; i++) {
/* 255 */       this.rules[i].parse(this.maps);
/*     */     }
/*     */   }
/*     */   
/*     */   protected synchronized void stopInternal() throws LifecycleException
/*     */   {
/* 261 */     super.stopInternal();
/* 262 */     Iterator<RewriteMap> values = this.maps.values().iterator();
/* 263 */     while (values.hasNext()) {
/* 264 */       RewriteMap map = (RewriteMap)values.next();
/* 265 */       if ((map instanceof Lifecycle)) {
/* 266 */         ((Lifecycle)map).stop();
/*     */       }
/*     */     }
/* 269 */     this.maps.clear();
/* 270 */     this.rules = null;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void invoke(org.apache.catalina.connector.Request request, org.apache.catalina.connector.Response response)
/*     */     throws IOException, javax.servlet.ServletException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 119	org/apache/catalina/valves/rewrite/RewriteValve:getEnabled	()Z
/*     */     //   4: ifeq +18 -> 22
/*     */     //   7: aload_0
/*     */     //   8: getfield 2	org/apache/catalina/valves/rewrite/RewriteValve:rules	[Lorg/apache/catalina/valves/rewrite/RewriteRule;
/*     */     //   11: ifnull +11 -> 22
/*     */     //   14: aload_0
/*     */     //   15: getfield 2	org/apache/catalina/valves/rewrite/RewriteValve:rules	[Lorg/apache/catalina/valves/rewrite/RewriteRule;
/*     */     //   18: arraylength
/*     */     //   19: ifne +15 -> 34
/*     */     //   22: aload_0
/*     */     //   23: invokevirtual 120	org/apache/catalina/valves/rewrite/RewriteValve:getNext	()Lorg/apache/catalina/Valve;
/*     */     //   26: aload_1
/*     */     //   27: aload_2
/*     */     //   28: invokeinterface 121 3 0
/*     */     //   33: return
/*     */     //   34: getstatic 122	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
/*     */     //   37: aload_0
/*     */     //   38: getfield 5	org/apache/catalina/valves/rewrite/RewriteValve:invoked	Ljava/lang/ThreadLocal;
/*     */     //   41: invokevirtual 123	java/lang/ThreadLocal:get	()Ljava/lang/Object;
/*     */     //   44: invokevirtual 124	java/lang/Boolean:equals	(Ljava/lang/Object;)Z
/*     */     //   47: ifeq +37 -> 84
/*     */     //   50: aload_0
/*     */     //   51: invokevirtual 120	org/apache/catalina/valves/rewrite/RewriteValve:getNext	()Lorg/apache/catalina/Valve;
/*     */     //   54: aload_1
/*     */     //   55: aload_2
/*     */     //   56: invokeinterface 121 3 0
/*     */     //   61: aload_0
/*     */     //   62: getfield 5	org/apache/catalina/valves/rewrite/RewriteValve:invoked	Ljava/lang/ThreadLocal;
/*     */     //   65: aconst_null
/*     */     //   66: invokevirtual 125	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   69: goto +14 -> 83
/*     */     //   72: astore_3
/*     */     //   73: aload_0
/*     */     //   74: getfield 5	org/apache/catalina/valves/rewrite/RewriteValve:invoked	Ljava/lang/ThreadLocal;
/*     */     //   77: aconst_null
/*     */     //   78: invokevirtual 125	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   81: aload_3
/*     */     //   82: athrow
/*     */     //   83: return
/*     */     //   84: new 126	org/apache/catalina/valves/rewrite/ResolverImpl
/*     */     //   87: dup
/*     */     //   88: aload_1
/*     */     //   89: invokespecial 127	org/apache/catalina/valves/rewrite/ResolverImpl:<init>	(Lorg/apache/catalina/connector/Request;)V
/*     */     //   92: astore_3
/*     */     //   93: aload_0
/*     */     //   94: getfield 5	org/apache/catalina/valves/rewrite/RewriteValve:invoked	Ljava/lang/ThreadLocal;
/*     */     //   97: getstatic 122	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
/*     */     //   100: invokevirtual 125	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   103: aload_1
/*     */     //   104: invokevirtual 128	org/apache/catalina/connector/Request:getConnector	()Lorg/apache/catalina/connector/Connector;
/*     */     //   107: invokevirtual 129	org/apache/catalina/connector/Connector:getURICharset	()Ljava/nio/charset/Charset;
/*     */     //   110: astore 4
/*     */     //   112: aload_1
/*     */     //   113: invokevirtual 130	org/apache/catalina/connector/Request:getQueryString	()Ljava/lang/String;
/*     */     //   116: astore 5
/*     */     //   118: aload_0
/*     */     //   119: getfield 8	org/apache/catalina/valves/rewrite/RewriteValve:context	Z
/*     */     //   122: ifeq +10 -> 132
/*     */     //   125: aload_1
/*     */     //   126: invokevirtual 131	org/apache/catalina/connector/Request:getRequestPathMB	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   129: goto +7 -> 136
/*     */     //   132: aload_1
/*     */     //   133: invokevirtual 132	org/apache/catalina/connector/Request:getDecodedRequestURIMB	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   136: astore 6
/*     */     //   138: aload 6
/*     */     //   140: invokevirtual 133	org/apache/tomcat/util/buf/MessageBytes:toChars	()V
/*     */     //   143: aload 6
/*     */     //   145: invokevirtual 134	org/apache/tomcat/util/buf/MessageBytes:getCharChunk	()Lorg/apache/tomcat/util/buf/CharChunk;
/*     */     //   148: astore 7
/*     */     //   150: aload_1
/*     */     //   151: invokevirtual 135	org/apache/catalina/connector/Request:getServerName	()Ljava/lang/String;
/*     */     //   154: astore 8
/*     */     //   156: iconst_0
/*     */     //   157: istore 9
/*     */     //   159: iconst_0
/*     */     //   160: istore 10
/*     */     //   162: iconst_0
/*     */     //   163: istore 11
/*     */     //   165: iconst_0
/*     */     //   166: istore 12
/*     */     //   168: iload 12
/*     */     //   170: aload_0
/*     */     //   171: getfield 2	org/apache/catalina/valves/rewrite/RewriteValve:rules	[Lorg/apache/catalina/valves/rewrite/RewriteRule;
/*     */     //   174: arraylength
/*     */     //   175: if_icmpge +828 -> 1003
/*     */     //   178: aload_0
/*     */     //   179: getfield 2	org/apache/catalina/valves/rewrite/RewriteValve:rules	[Lorg/apache/catalina/valves/rewrite/RewriteRule;
/*     */     //   182: iload 12
/*     */     //   184: aaload
/*     */     //   185: astore 13
/*     */     //   187: aload 13
/*     */     //   189: invokevirtual 136	org/apache/catalina/valves/rewrite/RewriteRule:isHost	()Z
/*     */     //   192: ifeq +8 -> 200
/*     */     //   195: aload 8
/*     */     //   197: goto +5 -> 202
/*     */     //   200: aload 7
/*     */     //   202: astore 14
/*     */     //   204: aload 13
/*     */     //   206: aload 14
/*     */     //   208: aload_3
/*     */     //   209: invokevirtual 137	org/apache/catalina/valves/rewrite/RewriteRule:evaluate	(Ljava/lang/CharSequence;Lorg/apache/catalina/valves/rewrite/Resolver;)Ljava/lang/CharSequence;
/*     */     //   212: astore 15
/*     */     //   214: aload 15
/*     */     //   216: ifnull +104 -> 320
/*     */     //   219: aload 14
/*     */     //   221: aload 15
/*     */     //   223: invokeinterface 138 1 0
/*     */     //   228: invokevirtual 139	java/lang/Object:equals	(Ljava/lang/Object;)Z
/*     */     //   231: ifne +89 -> 320
/*     */     //   234: aload_0
/*     */     //   235: getfield 22	org/apache/catalina/valves/rewrite/RewriteValve:containerLog	Lorg/apache/juli/logging/Log;
/*     */     //   238: invokeinterface 28 1 0
/*     */     //   243: ifeq +55 -> 298
/*     */     //   246: aload_0
/*     */     //   247: getfield 22	org/apache/catalina/valves/rewrite/RewriteValve:containerLog	Lorg/apache/juli/logging/Log;
/*     */     //   250: new 14	java/lang/StringBuilder
/*     */     //   253: dup
/*     */     //   254: invokespecial 15	java/lang/StringBuilder:<init>	()V
/*     */     //   257: ldc -116
/*     */     //   259: invokevirtual 18	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   262: aload 14
/*     */     //   264: invokevirtual 48	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   267: ldc -115
/*     */     //   269: invokevirtual 18	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   272: aload 15
/*     */     //   274: invokevirtual 48	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   277: ldc -114
/*     */     //   279: invokevirtual 18	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   282: aload 13
/*     */     //   284: invokevirtual 83	org/apache/catalina/valves/rewrite/RewriteRule:getPatternString	()Ljava/lang/String;
/*     */     //   287: invokevirtual 18	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   290: invokevirtual 20	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   293: invokeinterface 30 2 0
/*     */     //   298: aload 13
/*     */     //   300: invokevirtual 136	org/apache/catalina/valves/rewrite/RewriteRule:isHost	()Z
/*     */     //   303: ifeq +10 -> 313
/*     */     //   306: aload 15
/*     */     //   308: astore 8
/*     */     //   310: goto +7 -> 317
/*     */     //   313: aload 15
/*     */     //   315: astore 7
/*     */     //   317: iconst_1
/*     */     //   318: istore 9
/*     */     //   320: iload 11
/*     */     //   322: ifne +19 -> 341
/*     */     //   325: aload 15
/*     */     //   327: ifnull +14 -> 341
/*     */     //   330: aload 13
/*     */     //   332: invokevirtual 143	org/apache/catalina/valves/rewrite/RewriteRule:isQsappend	()Z
/*     */     //   335: ifeq +6 -> 341
/*     */     //   338: iconst_1
/*     */     //   339: istore 11
/*     */     //   341: aload 13
/*     */     //   343: invokevirtual 144	org/apache/catalina/valves/rewrite/RewriteRule:isForbidden	()Z
/*     */     //   346: ifeq +21 -> 367
/*     */     //   349: aload 15
/*     */     //   351: ifnull +16 -> 367
/*     */     //   354: aload_2
/*     */     //   355: sipush 403
/*     */     //   358: invokevirtual 146	org/apache/catalina/connector/Response:sendError	(I)V
/*     */     //   361: iconst_1
/*     */     //   362: istore 10
/*     */     //   364: goto +639 -> 1003
/*     */     //   367: aload 13
/*     */     //   369: invokevirtual 147	org/apache/catalina/valves/rewrite/RewriteRule:isGone	()Z
/*     */     //   372: ifeq +21 -> 393
/*     */     //   375: aload 15
/*     */     //   377: ifnull +16 -> 393
/*     */     //   380: aload_2
/*     */     //   381: sipush 410
/*     */     //   384: invokevirtual 146	org/apache/catalina/connector/Response:sendError	(I)V
/*     */     //   387: iconst_1
/*     */     //   388: istore 10
/*     */     //   390: goto +613 -> 1003
/*     */     //   393: aload 13
/*     */     //   395: invokevirtual 148	org/apache/catalina/valves/rewrite/RewriteRule:isRedirect	()Z
/*     */     //   398: ifeq +333 -> 731
/*     */     //   401: aload 15
/*     */     //   403: ifnull +328 -> 731
/*     */     //   406: aload 7
/*     */     //   408: invokeinterface 138 1 0
/*     */     //   413: astore 16
/*     */     //   415: aload 16
/*     */     //   417: ldc -107
/*     */     //   419: invokevirtual 150	java/lang/String:indexOf	(Ljava/lang/String;)I
/*     */     //   422: istore 17
/*     */     //   424: iload 17
/*     */     //   426: iconst_m1
/*     */     //   427: if_icmpne +9 -> 436
/*     */     //   430: aconst_null
/*     */     //   431: astore 18
/*     */     //   433: goto +24 -> 457
/*     */     //   436: aload 16
/*     */     //   438: iload 17
/*     */     //   440: iconst_1
/*     */     //   441: iadd
/*     */     //   442: invokevirtual 151	java/lang/String:substring	(I)Ljava/lang/String;
/*     */     //   445: astore 18
/*     */     //   447: aload 16
/*     */     //   449: iconst_0
/*     */     //   450: iload 17
/*     */     //   452: invokevirtual 152	java/lang/String:substring	(II)Ljava/lang/String;
/*     */     //   455: astore 16
/*     */     //   457: new 69	java/lang/StringBuffer
/*     */     //   460: dup
/*     */     //   461: getstatic 153	org/apache/catalina/util/URLEncoder:DEFAULT	Lorg/apache/catalina/util/URLEncoder;
/*     */     //   464: aload 16
/*     */     //   466: aload 4
/*     */     //   468: invokevirtual 154	org/apache/catalina/util/URLEncoder:encode	(Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   471: invokespecial 155	java/lang/StringBuffer:<init>	(Ljava/lang/String;)V
/*     */     //   474: astore 19
/*     */     //   476: aload 5
/*     */     //   478: ifnull +133 -> 611
/*     */     //   481: aload 5
/*     */     //   483: invokevirtual 156	java/lang/String:length	()I
/*     */     //   486: ifle +125 -> 611
/*     */     //   489: aload 18
/*     */     //   491: ifnonnull +22 -> 513
/*     */     //   494: aload 19
/*     */     //   496: bipush 63
/*     */     //   498: invokevirtual 157	java/lang/StringBuffer:append	(C)Ljava/lang/StringBuffer;
/*     */     //   501: pop
/*     */     //   502: aload 19
/*     */     //   504: aload 5
/*     */     //   506: invokevirtual 73	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*     */     //   509: pop
/*     */     //   510: goto +130 -> 640
/*     */     //   513: iload 11
/*     */     //   515: ifeq +46 -> 561
/*     */     //   518: aload 19
/*     */     //   520: bipush 63
/*     */     //   522: invokevirtual 157	java/lang/StringBuffer:append	(C)Ljava/lang/StringBuffer;
/*     */     //   525: pop
/*     */     //   526: aload 19
/*     */     //   528: getstatic 158	org/apache/catalina/util/URLEncoder:QUERY	Lorg/apache/catalina/util/URLEncoder;
/*     */     //   531: aload 18
/*     */     //   533: aload 4
/*     */     //   535: invokevirtual 154	org/apache/catalina/util/URLEncoder:encode	(Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   538: invokevirtual 73	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*     */     //   541: pop
/*     */     //   542: aload 19
/*     */     //   544: bipush 38
/*     */     //   546: invokevirtual 157	java/lang/StringBuffer:append	(C)Ljava/lang/StringBuffer;
/*     */     //   549: pop
/*     */     //   550: aload 19
/*     */     //   552: aload 5
/*     */     //   554: invokevirtual 73	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*     */     //   557: pop
/*     */     //   558: goto +82 -> 640
/*     */     //   561: iload 17
/*     */     //   563: aload 19
/*     */     //   565: invokevirtual 159	java/lang/StringBuffer:length	()I
/*     */     //   568: iconst_1
/*     */     //   569: isub
/*     */     //   570: if_icmpne +14 -> 584
/*     */     //   573: aload 19
/*     */     //   575: iload 17
/*     */     //   577: invokevirtual 160	java/lang/StringBuffer:deleteCharAt	(I)Ljava/lang/StringBuffer;
/*     */     //   580: pop
/*     */     //   581: goto +59 -> 640
/*     */     //   584: aload 19
/*     */     //   586: bipush 63
/*     */     //   588: invokevirtual 157	java/lang/StringBuffer:append	(C)Ljava/lang/StringBuffer;
/*     */     //   591: pop
/*     */     //   592: aload 19
/*     */     //   594: getstatic 158	org/apache/catalina/util/URLEncoder:QUERY	Lorg/apache/catalina/util/URLEncoder;
/*     */     //   597: aload 18
/*     */     //   599: aload 4
/*     */     //   601: invokevirtual 154	org/apache/catalina/util/URLEncoder:encode	(Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   604: invokevirtual 73	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*     */     //   607: pop
/*     */     //   608: goto +32 -> 640
/*     */     //   611: aload 18
/*     */     //   613: ifnull +27 -> 640
/*     */     //   616: aload 19
/*     */     //   618: bipush 63
/*     */     //   620: invokevirtual 157	java/lang/StringBuffer:append	(C)Ljava/lang/StringBuffer;
/*     */     //   623: pop
/*     */     //   624: aload 19
/*     */     //   626: getstatic 158	org/apache/catalina/util/URLEncoder:QUERY	Lorg/apache/catalina/util/URLEncoder;
/*     */     //   629: aload 18
/*     */     //   631: aload 4
/*     */     //   633: invokevirtual 154	org/apache/catalina/util/URLEncoder:encode	(Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   636: invokevirtual 73	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*     */     //   639: pop
/*     */     //   640: aload_0
/*     */     //   641: getfield 8	org/apache/catalina/valves/rewrite/RewriteValve:context	Z
/*     */     //   644: ifeq +38 -> 682
/*     */     //   647: aload 19
/*     */     //   649: iconst_0
/*     */     //   650: invokevirtual 161	java/lang/StringBuffer:charAt	(I)C
/*     */     //   653: bipush 47
/*     */     //   655: if_icmpne +27 -> 682
/*     */     //   658: aload 19
/*     */     //   660: invokestatic 162	org/apache/tomcat/util/buf/UriUtil:hasScheme	(Ljava/lang/CharSequence;)Z
/*     */     //   663: ifne +19 -> 682
/*     */     //   666: aload 19
/*     */     //   668: iconst_0
/*     */     //   669: aload_1
/*     */     //   670: invokevirtual 163	org/apache/catalina/connector/Request:getContext	()Lorg/apache/catalina/Context;
/*     */     //   673: invokeinterface 164 1 0
/*     */     //   678: invokevirtual 165	java/lang/StringBuffer:insert	(ILjava/lang/String;)Ljava/lang/StringBuffer;
/*     */     //   681: pop
/*     */     //   682: aload 13
/*     */     //   684: invokevirtual 166	org/apache/catalina/valves/rewrite/RewriteRule:isNoescape	()Z
/*     */     //   687: ifeq +20 -> 707
/*     */     //   690: aload_2
/*     */     //   691: aload 19
/*     */     //   693: invokevirtual 76	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*     */     //   696: aload 4
/*     */     //   698: invokestatic 167	org/apache/tomcat/util/buf/UDecoder:URLDecode	(Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   701: invokevirtual 168	org/apache/catalina/connector/Response:sendRedirect	(Ljava/lang/String;)V
/*     */     //   704: goto +12 -> 716
/*     */     //   707: aload_2
/*     */     //   708: aload 19
/*     */     //   710: invokevirtual 76	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*     */     //   713: invokevirtual 168	org/apache/catalina/connector/Response:sendRedirect	(Ljava/lang/String;)V
/*     */     //   716: aload_2
/*     */     //   717: aload 13
/*     */     //   719: invokevirtual 169	org/apache/catalina/valves/rewrite/RewriteRule:getRedirectCode	()I
/*     */     //   722: invokevirtual 170	org/apache/catalina/connector/Response:setStatus	(I)V
/*     */     //   725: iconst_1
/*     */     //   726: istore 10
/*     */     //   728: goto +275 -> 1003
/*     */     //   731: aload 13
/*     */     //   733: invokevirtual 171	org/apache/catalina/valves/rewrite/RewriteRule:isCookie	()Z
/*     */     //   736: ifeq +83 -> 819
/*     */     //   739: aload 15
/*     */     //   741: ifnull +78 -> 819
/*     */     //   744: new 172	javax/servlet/http/Cookie
/*     */     //   747: dup
/*     */     //   748: aload 13
/*     */     //   750: invokevirtual 173	org/apache/catalina/valves/rewrite/RewriteRule:getCookieName	()Ljava/lang/String;
/*     */     //   753: aload 13
/*     */     //   755: invokevirtual 174	org/apache/catalina/valves/rewrite/RewriteRule:getCookieResult	()Ljava/lang/String;
/*     */     //   758: invokespecial 175	javax/servlet/http/Cookie:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   761: astore 16
/*     */     //   763: aload 16
/*     */     //   765: aload 13
/*     */     //   767: invokevirtual 176	org/apache/catalina/valves/rewrite/RewriteRule:getCookieDomain	()Ljava/lang/String;
/*     */     //   770: invokevirtual 177	javax/servlet/http/Cookie:setDomain	(Ljava/lang/String;)V
/*     */     //   773: aload 16
/*     */     //   775: aload 13
/*     */     //   777: invokevirtual 178	org/apache/catalina/valves/rewrite/RewriteRule:getCookieLifetime	()I
/*     */     //   780: invokevirtual 179	javax/servlet/http/Cookie:setMaxAge	(I)V
/*     */     //   783: aload 16
/*     */     //   785: aload 13
/*     */     //   787: invokevirtual 180	org/apache/catalina/valves/rewrite/RewriteRule:getCookiePath	()Ljava/lang/String;
/*     */     //   790: invokevirtual 181	javax/servlet/http/Cookie:setPath	(Ljava/lang/String;)V
/*     */     //   793: aload 16
/*     */     //   795: aload 13
/*     */     //   797: invokevirtual 182	org/apache/catalina/valves/rewrite/RewriteRule:isCookieSecure	()Z
/*     */     //   800: invokevirtual 183	javax/servlet/http/Cookie:setSecure	(Z)V
/*     */     //   803: aload 16
/*     */     //   805: aload 13
/*     */     //   807: invokevirtual 184	org/apache/catalina/valves/rewrite/RewriteRule:isCookieHttpOnly	()Z
/*     */     //   810: invokevirtual 185	javax/servlet/http/Cookie:setHttpOnly	(Z)V
/*     */     //   813: aload_2
/*     */     //   814: aload 16
/*     */     //   816: invokevirtual 186	org/apache/catalina/connector/Response:addCookie	(Ljavax/servlet/http/Cookie;)V
/*     */     //   819: aload 13
/*     */     //   821: invokevirtual 187	org/apache/catalina/valves/rewrite/RewriteRule:isEnv	()Z
/*     */     //   824: ifeq +45 -> 869
/*     */     //   827: aload 15
/*     */     //   829: ifnull +40 -> 869
/*     */     //   832: iconst_0
/*     */     //   833: istore 16
/*     */     //   835: iload 16
/*     */     //   837: aload 13
/*     */     //   839: invokevirtual 188	org/apache/catalina/valves/rewrite/RewriteRule:getEnvSize	()I
/*     */     //   842: if_icmpge +27 -> 869
/*     */     //   845: aload_1
/*     */     //   846: aload 13
/*     */     //   848: iload 16
/*     */     //   850: invokevirtual 189	org/apache/catalina/valves/rewrite/RewriteRule:getEnvName	(I)Ljava/lang/String;
/*     */     //   853: aload 13
/*     */     //   855: iload 16
/*     */     //   857: invokevirtual 190	org/apache/catalina/valves/rewrite/RewriteRule:getEnvResult	(I)Ljava/lang/String;
/*     */     //   860: invokevirtual 191	org/apache/catalina/connector/Request:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
/*     */     //   863: iinc 16 1
/*     */     //   866: goto -31 -> 835
/*     */     //   869: aload 13
/*     */     //   871: invokevirtual 192	org/apache/catalina/valves/rewrite/RewriteRule:isType	()Z
/*     */     //   874: ifeq +17 -> 891
/*     */     //   877: aload 15
/*     */     //   879: ifnull +12 -> 891
/*     */     //   882: aload_1
/*     */     //   883: aload 13
/*     */     //   885: invokevirtual 193	org/apache/catalina/valves/rewrite/RewriteRule:getTypeValue	()Ljava/lang/String;
/*     */     //   888: invokevirtual 194	org/apache/catalina/connector/Request:setContentType	(Ljava/lang/String;)V
/*     */     //   891: aload 13
/*     */     //   893: invokevirtual 195	org/apache/catalina/valves/rewrite/RewriteRule:isChain	()Z
/*     */     //   896: ifeq +51 -> 947
/*     */     //   899: aload 15
/*     */     //   901: ifnonnull +46 -> 947
/*     */     //   904: iload 12
/*     */     //   906: istore 16
/*     */     //   908: iload 16
/*     */     //   910: aload_0
/*     */     //   911: getfield 2	org/apache/catalina/valves/rewrite/RewriteValve:rules	[Lorg/apache/catalina/valves/rewrite/RewriteRule;
/*     */     //   914: arraylength
/*     */     //   915: if_icmpge +29 -> 944
/*     */     //   918: aload_0
/*     */     //   919: getfield 2	org/apache/catalina/valves/rewrite/RewriteValve:rules	[Lorg/apache/catalina/valves/rewrite/RewriteRule;
/*     */     //   922: iload 16
/*     */     //   924: aaload
/*     */     //   925: invokevirtual 195	org/apache/catalina/valves/rewrite/RewriteRule:isChain	()Z
/*     */     //   928: ifne +10 -> 938
/*     */     //   931: iload 16
/*     */     //   933: istore 12
/*     */     //   935: goto +9 -> 944
/*     */     //   938: iinc 16 1
/*     */     //   941: goto -33 -> 908
/*     */     //   944: goto +53 -> 997
/*     */     //   947: aload 13
/*     */     //   949: invokevirtual 196	org/apache/catalina/valves/rewrite/RewriteRule:isLast	()Z
/*     */     //   952: ifeq +11 -> 963
/*     */     //   955: aload 15
/*     */     //   957: ifnull +6 -> 963
/*     */     //   960: goto +43 -> 1003
/*     */     //   963: aload 13
/*     */     //   965: invokevirtual 197	org/apache/catalina/valves/rewrite/RewriteRule:isNext	()Z
/*     */     //   968: ifeq +14 -> 982
/*     */     //   971: aload 15
/*     */     //   973: ifnull +9 -> 982
/*     */     //   976: iconst_0
/*     */     //   977: istore 12
/*     */     //   979: goto +18 -> 997
/*     */     //   982: aload 15
/*     */     //   984: ifnull +13 -> 997
/*     */     //   987: iload 12
/*     */     //   989: aload 13
/*     */     //   991: invokevirtual 198	org/apache/catalina/valves/rewrite/RewriteRule:getSkip	()I
/*     */     //   994: iadd
/*     */     //   995: istore 12
/*     */     //   997: iinc 12 1
/*     */     //   1000: goto -832 -> 168
/*     */     //   1003: iload 9
/*     */     //   1005: ifeq +470 -> 1475
/*     */     //   1008: iload 10
/*     */     //   1010: ifne +476 -> 1486
/*     */     //   1013: aload 7
/*     */     //   1015: invokeinterface 138 1 0
/*     */     //   1020: astore 12
/*     */     //   1022: aconst_null
/*     */     //   1023: astore 13
/*     */     //   1025: aload 12
/*     */     //   1027: bipush 63
/*     */     //   1029: invokevirtual 199	java/lang/String:indexOf	(I)I
/*     */     //   1032: istore 14
/*     */     //   1034: iload 14
/*     */     //   1036: iconst_m1
/*     */     //   1037: if_icmpeq +24 -> 1061
/*     */     //   1040: aload 12
/*     */     //   1042: iload 14
/*     */     //   1044: iconst_1
/*     */     //   1045: iadd
/*     */     //   1046: invokevirtual 151	java/lang/String:substring	(I)Ljava/lang/String;
/*     */     //   1049: astore 13
/*     */     //   1051: aload 12
/*     */     //   1053: iconst_0
/*     */     //   1054: iload 14
/*     */     //   1056: invokevirtual 152	java/lang/String:substring	(II)Ljava/lang/String;
/*     */     //   1059: astore 12
/*     */     //   1061: aconst_null
/*     */     //   1062: astore 15
/*     */     //   1064: aload_0
/*     */     //   1065: getfield 8	org/apache/catalina/valves/rewrite/RewriteValve:context	Z
/*     */     //   1068: ifeq +9 -> 1077
/*     */     //   1071: aload_1
/*     */     //   1072: invokevirtual 200	org/apache/catalina/connector/Request:getContextPath	()Ljava/lang/String;
/*     */     //   1075: astore 15
/*     */     //   1077: aload_1
/*     */     //   1078: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1081: invokevirtual 202	org/apache/coyote/Request:requestURI	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1084: aconst_null
/*     */     //   1085: invokevirtual 203	org/apache/tomcat/util/buf/MessageBytes:setString	(Ljava/lang/String;)V
/*     */     //   1088: aload_1
/*     */     //   1089: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1092: invokevirtual 202	org/apache/coyote/Request:requestURI	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1095: invokevirtual 134	org/apache/tomcat/util/buf/MessageBytes:getCharChunk	()Lorg/apache/tomcat/util/buf/CharChunk;
/*     */     //   1098: astore 16
/*     */     //   1100: aload 16
/*     */     //   1102: invokevirtual 204	org/apache/tomcat/util/buf/CharChunk:recycle	()V
/*     */     //   1105: aload_0
/*     */     //   1106: getfield 8	org/apache/catalina/valves/rewrite/RewriteValve:context	Z
/*     */     //   1109: ifeq +10 -> 1119
/*     */     //   1112: aload 16
/*     */     //   1114: aload 15
/*     */     //   1116: invokevirtual 205	org/apache/tomcat/util/buf/CharChunk:append	(Ljava/lang/String;)V
/*     */     //   1119: aload 16
/*     */     //   1121: getstatic 153	org/apache/catalina/util/URLEncoder:DEFAULT	Lorg/apache/catalina/util/URLEncoder;
/*     */     //   1124: aload 12
/*     */     //   1126: aload 4
/*     */     //   1128: invokevirtual 154	org/apache/catalina/util/URLEncoder:encode	(Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   1131: invokevirtual 205	org/apache/tomcat/util/buf/CharChunk:append	(Ljava/lang/String;)V
/*     */     //   1134: aload_1
/*     */     //   1135: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1138: invokevirtual 202	org/apache/coyote/Request:requestURI	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1141: invokevirtual 133	org/apache/tomcat/util/buf/MessageBytes:toChars	()V
/*     */     //   1144: aload 12
/*     */     //   1146: invokestatic 206	org/apache/tomcat/util/http/RequestUtil:normalize	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   1149: astore 12
/*     */     //   1151: aload_1
/*     */     //   1152: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1155: invokevirtual 207	org/apache/coyote/Request:decodedURI	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1158: aconst_null
/*     */     //   1159: invokevirtual 203	org/apache/tomcat/util/buf/MessageBytes:setString	(Ljava/lang/String;)V
/*     */     //   1162: aload_1
/*     */     //   1163: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1166: invokevirtual 207	org/apache/coyote/Request:decodedURI	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1169: invokevirtual 134	org/apache/tomcat/util/buf/MessageBytes:getCharChunk	()Lorg/apache/tomcat/util/buf/CharChunk;
/*     */     //   1172: astore 16
/*     */     //   1174: aload 16
/*     */     //   1176: invokevirtual 204	org/apache/tomcat/util/buf/CharChunk:recycle	()V
/*     */     //   1179: aload_0
/*     */     //   1180: getfield 8	org/apache/catalina/valves/rewrite/RewriteValve:context	Z
/*     */     //   1183: ifeq +17 -> 1200
/*     */     //   1186: aload 16
/*     */     //   1188: aload_1
/*     */     //   1189: invokevirtual 208	org/apache/catalina/connector/Request:getServletContext	()Ljavax/servlet/ServletContext;
/*     */     //   1192: invokeinterface 209 1 0
/*     */     //   1197: invokevirtual 205	org/apache/tomcat/util/buf/CharChunk:append	(Ljava/lang/String;)V
/*     */     //   1200: aload 16
/*     */     //   1202: aload 12
/*     */     //   1204: invokevirtual 205	org/apache/tomcat/util/buf/CharChunk:append	(Ljava/lang/String;)V
/*     */     //   1207: aload_1
/*     */     //   1208: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1211: invokevirtual 207	org/apache/coyote/Request:decodedURI	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1214: invokevirtual 133	org/apache/tomcat/util/buf/MessageBytes:toChars	()V
/*     */     //   1217: aload 13
/*     */     //   1219: ifnull +96 -> 1315
/*     */     //   1222: aload_1
/*     */     //   1223: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1226: invokevirtual 210	org/apache/coyote/Request:queryString	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1229: aconst_null
/*     */     //   1230: invokevirtual 203	org/apache/tomcat/util/buf/MessageBytes:setString	(Ljava/lang/String;)V
/*     */     //   1233: aload_1
/*     */     //   1234: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1237: invokevirtual 210	org/apache/coyote/Request:queryString	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1240: invokevirtual 134	org/apache/tomcat/util/buf/MessageBytes:getCharChunk	()Lorg/apache/tomcat/util/buf/CharChunk;
/*     */     //   1243: astore 16
/*     */     //   1245: aload 16
/*     */     //   1247: invokevirtual 204	org/apache/tomcat/util/buf/CharChunk:recycle	()V
/*     */     //   1250: aload 16
/*     */     //   1252: getstatic 158	org/apache/catalina/util/URLEncoder:QUERY	Lorg/apache/catalina/util/URLEncoder;
/*     */     //   1255: aload 13
/*     */     //   1257: aload 4
/*     */     //   1259: invokevirtual 154	org/apache/catalina/util/URLEncoder:encode	(Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   1262: invokevirtual 205	org/apache/tomcat/util/buf/CharChunk:append	(Ljava/lang/String;)V
/*     */     //   1265: iload 11
/*     */     //   1267: ifeq +30 -> 1297
/*     */     //   1270: aload 5
/*     */     //   1272: ifnull +25 -> 1297
/*     */     //   1275: aload 5
/*     */     //   1277: invokevirtual 156	java/lang/String:length	()I
/*     */     //   1280: ifle +17 -> 1297
/*     */     //   1283: aload 16
/*     */     //   1285: bipush 38
/*     */     //   1287: invokevirtual 211	org/apache/tomcat/util/buf/CharChunk:append	(C)V
/*     */     //   1290: aload 16
/*     */     //   1292: aload 5
/*     */     //   1294: invokevirtual 205	org/apache/tomcat/util/buf/CharChunk:append	(Ljava/lang/String;)V
/*     */     //   1297: aload 16
/*     */     //   1299: invokevirtual 212	org/apache/tomcat/util/buf/CharChunk:isNull	()Z
/*     */     //   1302: ifne +13 -> 1315
/*     */     //   1305: aload_1
/*     */     //   1306: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1309: invokevirtual 210	org/apache/coyote/Request:queryString	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1312: invokevirtual 133	org/apache/tomcat/util/buf/MessageBytes:toChars	()V
/*     */     //   1315: aload 8
/*     */     //   1317: aload_1
/*     */     //   1318: invokevirtual 135	org/apache/catalina/connector/Request:getServerName	()Ljava/lang/String;
/*     */     //   1321: invokevirtual 139	java/lang/Object:equals	(Ljava/lang/Object;)Z
/*     */     //   1324: ifne +53 -> 1377
/*     */     //   1327: aload_1
/*     */     //   1328: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1331: invokevirtual 213	org/apache/coyote/Request:serverName	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1334: aconst_null
/*     */     //   1335: invokevirtual 203	org/apache/tomcat/util/buf/MessageBytes:setString	(Ljava/lang/String;)V
/*     */     //   1338: aload_1
/*     */     //   1339: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1342: invokevirtual 213	org/apache/coyote/Request:serverName	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1345: invokevirtual 134	org/apache/tomcat/util/buf/MessageBytes:getCharChunk	()Lorg/apache/tomcat/util/buf/CharChunk;
/*     */     //   1348: astore 16
/*     */     //   1350: aload 16
/*     */     //   1352: invokevirtual 204	org/apache/tomcat/util/buf/CharChunk:recycle	()V
/*     */     //   1355: aload 16
/*     */     //   1357: aload 8
/*     */     //   1359: invokeinterface 138 1 0
/*     */     //   1364: invokevirtual 205	org/apache/tomcat/util/buf/CharChunk:append	(Ljava/lang/String;)V
/*     */     //   1367: aload_1
/*     */     //   1368: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1371: invokevirtual 213	org/apache/coyote/Request:serverName	()Lorg/apache/tomcat/util/buf/MessageBytes;
/*     */     //   1374: invokevirtual 133	org/apache/tomcat/util/buf/MessageBytes:toChars	()V
/*     */     //   1377: aload_1
/*     */     //   1378: invokevirtual 214	org/apache/catalina/connector/Request:getMappingData	()Lorg/apache/catalina/mapper/MappingData;
/*     */     //   1381: invokevirtual 215	org/apache/catalina/mapper/MappingData:recycle	()V
/*     */     //   1384: aload_1
/*     */     //   1385: invokevirtual 128	org/apache/catalina/connector/Request:getConnector	()Lorg/apache/catalina/connector/Connector;
/*     */     //   1388: astore 17
/*     */     //   1390: aload 17
/*     */     //   1392: invokevirtual 216	org/apache/catalina/connector/Connector:getProtocolHandler	()Lorg/apache/coyote/ProtocolHandler;
/*     */     //   1395: invokeinterface 217 1 0
/*     */     //   1400: aload_1
/*     */     //   1401: invokevirtual 201	org/apache/catalina/connector/Request:getCoyoteRequest	()Lorg/apache/coyote/Request;
/*     */     //   1404: aload_2
/*     */     //   1405: invokevirtual 218	org/apache/catalina/connector/Response:getCoyoteResponse	()Lorg/apache/coyote/Response;
/*     */     //   1408: invokeinterface 219 3 0
/*     */     //   1413: ifne +12 -> 1425
/*     */     //   1416: aload_0
/*     */     //   1417: getfield 5	org/apache/catalina/valves/rewrite/RewriteValve:invoked	Ljava/lang/ThreadLocal;
/*     */     //   1420: aconst_null
/*     */     //   1421: invokevirtual 125	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   1424: return
/*     */     //   1425: aload 17
/*     */     //   1427: invokevirtual 220	org/apache/catalina/connector/Connector:getService	()Lorg/apache/catalina/Service;
/*     */     //   1430: invokeinterface 221 1 0
/*     */     //   1435: invokeinterface 222 1 0
/*     */     //   1440: astore 18
/*     */     //   1442: aload_1
/*     */     //   1443: aload 18
/*     */     //   1445: invokeinterface 223 1 0
/*     */     //   1450: invokevirtual 224	org/apache/catalina/connector/Request:setAsyncSupported	(Z)V
/*     */     //   1453: aload 18
/*     */     //   1455: invokeinterface 225 1 0
/*     */     //   1460: aload_1
/*     */     //   1461: aload_2
/*     */     //   1462: invokeinterface 121 3 0
/*     */     //   1467: goto +5 -> 1472
/*     */     //   1470: astore 17
/*     */     //   1472: goto +14 -> 1486
/*     */     //   1475: aload_0
/*     */     //   1476: invokevirtual 120	org/apache/catalina/valves/rewrite/RewriteValve:getNext	()Lorg/apache/catalina/Valve;
/*     */     //   1479: aload_1
/*     */     //   1480: aload_2
/*     */     //   1481: invokeinterface 121 3 0
/*     */     //   1486: aload_0
/*     */     //   1487: getfield 5	org/apache/catalina/valves/rewrite/RewriteValve:invoked	Ljava/lang/ThreadLocal;
/*     */     //   1490: aconst_null
/*     */     //   1491: invokevirtual 125	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   1494: goto +16 -> 1510
/*     */     //   1497: astore 20
/*     */     //   1499: aload_0
/*     */     //   1500: getfield 5	org/apache/catalina/valves/rewrite/RewriteValve:invoked	Ljava/lang/ThreadLocal;
/*     */     //   1503: aconst_null
/*     */     //   1504: invokevirtual 125	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   1507: aload 20
/*     */     //   1509: athrow
/*     */     //   1510: return
/*     */     // Line number table:
/*     */     //   Java source line #278	-> byte code offset #0
/*     */     //   Java source line #279	-> byte code offset #22
/*     */     //   Java source line #280	-> byte code offset #33
/*     */     //   Java source line #283	-> byte code offset #34
/*     */     //   Java source line #285	-> byte code offset #50
/*     */     //   Java source line #287	-> byte code offset #61
/*     */     //   Java source line #288	-> byte code offset #69
/*     */     //   Java source line #287	-> byte code offset #72
/*     */     //   Java source line #289	-> byte code offset #83
/*     */     //   Java source line #294	-> byte code offset #84
/*     */     //   Java source line #296	-> byte code offset #93
/*     */     //   Java source line #300	-> byte code offset #103
/*     */     //   Java source line #301	-> byte code offset #112
/*     */     //   Java source line #302	-> byte code offset #118
/*     */     //   Java source line #303	-> byte code offset #126
/*     */     //   Java source line #304	-> byte code offset #138
/*     */     //   Java source line #305	-> byte code offset #143
/*     */     //   Java source line #306	-> byte code offset #150
/*     */     //   Java source line #307	-> byte code offset #156
/*     */     //   Java source line #308	-> byte code offset #159
/*     */     //   Java source line #309	-> byte code offset #162
/*     */     //   Java source line #310	-> byte code offset #165
/*     */     //   Java source line #311	-> byte code offset #178
/*     */     //   Java source line #312	-> byte code offset #187
/*     */     //   Java source line #313	-> byte code offset #204
/*     */     //   Java source line #314	-> byte code offset #214
/*     */     //   Java source line #315	-> byte code offset #234
/*     */     //   Java source line #316	-> byte code offset #246
/*     */     //   Java source line #317	-> byte code offset #284
/*     */     //   Java source line #316	-> byte code offset #293
/*     */     //   Java source line #319	-> byte code offset #298
/*     */     //   Java source line #320	-> byte code offset #306
/*     */     //   Java source line #322	-> byte code offset #313
/*     */     //   Java source line #324	-> byte code offset #317
/*     */     //   Java source line #328	-> byte code offset #320
/*     */     //   Java source line #331	-> byte code offset #338
/*     */     //   Java source line #337	-> byte code offset #341
/*     */     //   Java source line #338	-> byte code offset #354
/*     */     //   Java source line #339	-> byte code offset #361
/*     */     //   Java source line #340	-> byte code offset #364
/*     */     //   Java source line #343	-> byte code offset #367
/*     */     //   Java source line #344	-> byte code offset #380
/*     */     //   Java source line #345	-> byte code offset #387
/*     */     //   Java source line #346	-> byte code offset #390
/*     */     //   Java source line #350	-> byte code offset #393
/*     */     //   Java source line #353	-> byte code offset #406
/*     */     //   Java source line #354	-> byte code offset #415
/*     */     //   Java source line #356	-> byte code offset #424
/*     */     //   Java source line #357	-> byte code offset #430
/*     */     //   Java source line #359	-> byte code offset #436
/*     */     //   Java source line #360	-> byte code offset #447
/*     */     //   Java source line #363	-> byte code offset #457
/*     */     //   Java source line #364	-> byte code offset #468
/*     */     //   Java source line #365	-> byte code offset #476
/*     */     //   Java source line #366	-> byte code offset #483
/*     */     //   Java source line #367	-> byte code offset #489
/*     */     //   Java source line #368	-> byte code offset #494
/*     */     //   Java source line #369	-> byte code offset #502
/*     */     //   Java source line #371	-> byte code offset #513
/*     */     //   Java source line #373	-> byte code offset #518
/*     */     //   Java source line #374	-> byte code offset #526
/*     */     //   Java source line #376	-> byte code offset #542
/*     */     //   Java source line #377	-> byte code offset #550
/*     */     //   Java source line #378	-> byte code offset #561
/*     */     //   Java source line #381	-> byte code offset #573
/*     */     //   Java source line #383	-> byte code offset #584
/*     */     //   Java source line #384	-> byte code offset #592
/*     */     //   Java source line #388	-> byte code offset #611
/*     */     //   Java source line #389	-> byte code offset #616
/*     */     //   Java source line #390	-> byte code offset #624
/*     */     //   Java source line #391	-> byte code offset #633
/*     */     //   Java source line #390	-> byte code offset #636
/*     */     //   Java source line #398	-> byte code offset #640
/*     */     //   Java source line #399	-> byte code offset #660
/*     */     //   Java source line #400	-> byte code offset #666
/*     */     //   Java source line #402	-> byte code offset #682
/*     */     //   Java source line #403	-> byte code offset #690
/*     */     //   Java source line #404	-> byte code offset #693
/*     */     //   Java source line #403	-> byte code offset #701
/*     */     //   Java source line #406	-> byte code offset #707
/*     */     //   Java source line #408	-> byte code offset #716
/*     */     //   Java source line #409	-> byte code offset #725
/*     */     //   Java source line #410	-> byte code offset #728
/*     */     //   Java source line #416	-> byte code offset #731
/*     */     //   Java source line #417	-> byte code offset #744
/*     */     //   Java source line #418	-> byte code offset #755
/*     */     //   Java source line #419	-> byte code offset #763
/*     */     //   Java source line #420	-> byte code offset #773
/*     */     //   Java source line #421	-> byte code offset #783
/*     */     //   Java source line #422	-> byte code offset #793
/*     */     //   Java source line #423	-> byte code offset #803
/*     */     //   Java source line #424	-> byte code offset #813
/*     */     //   Java source line #427	-> byte code offset #819
/*     */     //   Java source line #428	-> byte code offset #832
/*     */     //   Java source line #429	-> byte code offset #845
/*     */     //   Java source line #428	-> byte code offset #863
/*     */     //   Java source line #434	-> byte code offset #869
/*     */     //   Java source line #435	-> byte code offset #882
/*     */     //   Java source line #441	-> byte code offset #891
/*     */     //   Java source line #442	-> byte code offset #904
/*     */     //   Java source line #443	-> byte code offset #918
/*     */     //   Java source line #444	-> byte code offset #931
/*     */     //   Java source line #445	-> byte code offset #935
/*     */     //   Java source line #442	-> byte code offset #938
/*     */     //   Java source line #448	-> byte code offset #944
/*     */     //   Java source line #451	-> byte code offset #947
/*     */     //   Java source line #452	-> byte code offset #960
/*     */     //   Java source line #455	-> byte code offset #963
/*     */     //   Java source line #456	-> byte code offset #976
/*     */     //   Java source line #457	-> byte code offset #979
/*     */     //   Java source line #460	-> byte code offset #982
/*     */     //   Java source line #461	-> byte code offset #987
/*     */     //   Java source line #310	-> byte code offset #997
/*     */     //   Java source line #466	-> byte code offset #1003
/*     */     //   Java source line #467	-> byte code offset #1008
/*     */     //   Java source line #469	-> byte code offset #1013
/*     */     //   Java source line #470	-> byte code offset #1022
/*     */     //   Java source line #471	-> byte code offset #1025
/*     */     //   Java source line #472	-> byte code offset #1034
/*     */     //   Java source line #473	-> byte code offset #1040
/*     */     //   Java source line #474	-> byte code offset #1051
/*     */     //   Java source line #477	-> byte code offset #1061
/*     */     //   Java source line #478	-> byte code offset #1064
/*     */     //   Java source line #479	-> byte code offset #1071
/*     */     //   Java source line #482	-> byte code offset #1077
/*     */     //   Java source line #483	-> byte code offset #1088
/*     */     //   Java source line #484	-> byte code offset #1100
/*     */     //   Java source line #485	-> byte code offset #1105
/*     */     //   Java source line #487	-> byte code offset #1112
/*     */     //   Java source line #489	-> byte code offset #1119
/*     */     //   Java source line #490	-> byte code offset #1134
/*     */     //   Java source line #493	-> byte code offset #1144
/*     */     //   Java source line #494	-> byte code offset #1151
/*     */     //   Java source line #495	-> byte code offset #1162
/*     */     //   Java source line #496	-> byte code offset #1174
/*     */     //   Java source line #497	-> byte code offset #1179
/*     */     //   Java source line #499	-> byte code offset #1186
/*     */     //   Java source line #501	-> byte code offset #1200
/*     */     //   Java source line #502	-> byte code offset #1207
/*     */     //   Java source line #504	-> byte code offset #1217
/*     */     //   Java source line #505	-> byte code offset #1222
/*     */     //   Java source line #506	-> byte code offset #1233
/*     */     //   Java source line #507	-> byte code offset #1245
/*     */     //   Java source line #508	-> byte code offset #1250
/*     */     //   Java source line #509	-> byte code offset #1265
/*     */     //   Java source line #510	-> byte code offset #1277
/*     */     //   Java source line #511	-> byte code offset #1283
/*     */     //   Java source line #512	-> byte code offset #1290
/*     */     //   Java source line #514	-> byte code offset #1297
/*     */     //   Java source line #515	-> byte code offset #1305
/*     */     //   Java source line #519	-> byte code offset #1315
/*     */     //   Java source line #520	-> byte code offset #1327
/*     */     //   Java source line #521	-> byte code offset #1338
/*     */     //   Java source line #522	-> byte code offset #1350
/*     */     //   Java source line #523	-> byte code offset #1355
/*     */     //   Java source line #524	-> byte code offset #1367
/*     */     //   Java source line #526	-> byte code offset #1377
/*     */     //   Java source line #529	-> byte code offset #1384
/*     */     //   Java source line #530	-> byte code offset #1390
/*     */     //   Java source line #531	-> byte code offset #1401
/*     */     //   Java source line #530	-> byte code offset #1408
/*     */     //   Java source line #546	-> byte code offset #1416
/*     */     //   Java source line #532	-> byte code offset #1424
/*     */     //   Java source line #534	-> byte code offset #1425
/*     */     //   Java source line #535	-> byte code offset #1442
/*     */     //   Java source line #536	-> byte code offset #1453
/*     */     //   Java source line #539	-> byte code offset #1467
/*     */     //   Java source line #537	-> byte code offset #1470
/*     */     //   Java source line #540	-> byte code offset #1472
/*     */     //   Java source line #542	-> byte code offset #1475
/*     */     //   Java source line #546	-> byte code offset #1486
/*     */     //   Java source line #547	-> byte code offset #1494
/*     */     //   Java source line #546	-> byte code offset #1497
/*     */     //   Java source line #549	-> byte code offset #1510
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	1511	0	this	RewriteValve
/*     */     //   0	1511	1	request	org.apache.catalina.connector.Request
/*     */     //   0	1511	2	response	org.apache.catalina.connector.Response
/*     */     //   72	10	3	localObject1	Object
/*     */     //   92	117	3	resolver	Object
/*     */     //   110	1148	4	uriCharset	java.nio.charset.Charset
/*     */     //   116	1177	5	originalQueryStringEncoded	String
/*     */     //   136	8	6	urlMB	org.apache.tomcat.util.buf.MessageBytes
/*     */     //   148	866	7	urlDecoded	CharSequence
/*     */     //   154	1204	8	host	CharSequence
/*     */     //   157	847	9	rewritten	boolean
/*     */     //   160	849	10	done	boolean
/*     */     //   163	1103	11	qsa	boolean
/*     */     //   166	832	12	i	int
/*     */     //   1020	183	12	urlStringDecoded	String
/*     */     //   185	805	13	rule	RewriteRule
/*     */     //   1023	233	13	queryStringDecoded	String
/*     */     //   202	61	14	test	CharSequence
/*     */     //   1032	23	14	queryIndex	int
/*     */     //   212	771	15	newtest	CharSequence
/*     */     //   1062	53	15	contextPath	String
/*     */     //   413	52	16	urlStringDecoded	String
/*     */     //   761	54	16	cookie	javax.servlet.http.Cookie
/*     */     //   833	31	16	j	int
/*     */     //   906	33	16	j	int
/*     */     //   1098	258	16	chunk	org.apache.tomcat.util.buf.CharChunk
/*     */     //   422	154	17	index	int
/*     */     //   1388	38	17	connector	org.apache.catalina.connector.Connector
/*     */     //   1470	1	17	localException	Exception
/*     */     //   431	3	18	rewrittenQueryStringDecoded	String
/*     */     //   445	185	18	rewrittenQueryStringDecoded	String
/*     */     //   1440	14	18	pipeline	org.apache.catalina.Pipeline
/*     */     //   474	235	19	urlStringEncoded	StringBuffer
/*     */     //   1497	11	20	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   50	61	72	finally
/*     */     //   1384	1416	1470	java/lang/Exception
/*     */     //   1425	1467	1470	java/lang/Exception
/*     */     //   84	1416	1497	finally
/*     */     //   1425	1486	1497	finally
/*     */     //   1497	1499	1497	finally
/*     */   }
/*     */   
/*     */   protected File getConfigBase()
/*     */   {
/* 557 */     File configBase = new File(System.getProperty("catalina.base"), "conf");
/* 558 */     if (!configBase.exists()) {
/* 559 */       return null;
/*     */     }
/* 561 */     return configBase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHostConfigPath(String resourceName)
/*     */   {
/* 573 */     StringBuffer result = new StringBuffer();
/* 574 */     Container container = getContainer();
/* 575 */     Container host = null;
/* 576 */     Container engine = null;
/* 577 */     while (container != null) {
/* 578 */       if ((container instanceof Host))
/* 579 */         host = container;
/* 580 */       if ((container instanceof Engine))
/* 581 */         engine = container;
/* 582 */       container = container.getParent();
/*     */     }
/* 584 */     if (engine != null) {
/* 585 */       result.append(engine.getName()).append('/');
/*     */     }
/* 587 */     if (host != null) {
/* 588 */       result.append(host.getName()).append('/');
/*     */     }
/* 590 */     result.append(resourceName);
/* 591 */     return result.toString();
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
/*     */   public static Object parse(String line)
/*     */   {
/* 605 */     StringTokenizer tokenizer = new StringTokenizer(line);
/* 606 */     if (tokenizer.hasMoreTokens()) {
/* 607 */       String token = tokenizer.nextToken();
/* 608 */       if (token.equals("RewriteCond"))
/*     */       {
/* 610 */         RewriteCond condition = new RewriteCond();
/* 611 */         if (tokenizer.countTokens() < 2) {
/* 612 */           throw new IllegalArgumentException("Invalid line: " + line);
/*     */         }
/* 614 */         condition.setTestString(tokenizer.nextToken());
/* 615 */         condition.setCondPattern(tokenizer.nextToken());
/* 616 */         if (tokenizer.hasMoreTokens()) {
/* 617 */           String flags = tokenizer.nextToken();
/* 618 */           if ((flags.startsWith("[")) && (flags.endsWith("]"))) {
/* 619 */             flags = flags.substring(1, flags.length() - 1);
/*     */           }
/* 621 */           StringTokenizer flagsTokenizer = new StringTokenizer(flags, ",");
/* 622 */           while (flagsTokenizer.hasMoreElements()) {
/* 623 */             parseCondFlag(line, condition, flagsTokenizer.nextToken());
/*     */           }
/*     */         }
/* 626 */         return condition; }
/* 627 */       if (token.equals("RewriteRule"))
/*     */       {
/* 629 */         RewriteRule rule = new RewriteRule();
/* 630 */         if (tokenizer.countTokens() < 2) {
/* 631 */           throw new IllegalArgumentException("Invalid line: " + line);
/*     */         }
/* 633 */         rule.setPatternString(tokenizer.nextToken());
/* 634 */         rule.setSubstitutionString(tokenizer.nextToken());
/* 635 */         if (tokenizer.hasMoreTokens()) {
/* 636 */           String flags = tokenizer.nextToken();
/* 637 */           if ((flags.startsWith("[")) && (flags.endsWith("]"))) {
/* 638 */             flags = flags.substring(1, flags.length() - 1);
/*     */           }
/* 640 */           StringTokenizer flagsTokenizer = new StringTokenizer(flags, ",");
/* 641 */           while (flagsTokenizer.hasMoreElements()) {
/* 642 */             parseRuleFlag(line, rule, flagsTokenizer.nextToken());
/*     */           }
/*     */         }
/* 645 */         return rule; }
/* 646 */       if (token.equals("RewriteMap"))
/*     */       {
/* 648 */         if (tokenizer.countTokens() < 2) {
/* 649 */           throw new IllegalArgumentException("Invalid line: " + line);
/*     */         }
/* 651 */         String name = tokenizer.nextToken();
/* 652 */         String rewriteMapClassName = tokenizer.nextToken();
/* 653 */         RewriteMap map = null;
/*     */         try
/*     */         {
/* 656 */           map = (RewriteMap)Class.forName(rewriteMapClassName).getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */         } catch (Exception e) {
/* 658 */           throw new IllegalArgumentException("Invalid map className: " + line);
/*     */         }
/* 660 */         if (tokenizer.hasMoreTokens()) {
/* 661 */           map.setParameters(tokenizer.nextToken());
/*     */         }
/* 663 */         Object[] result = new Object[2];
/* 664 */         result[0] = name;
/* 665 */         result[1] = map;
/* 666 */         return result; }
/* 667 */       if (!token.startsWith("#"))
/*     */       {
/*     */ 
/* 670 */         throw new IllegalArgumentException("Invalid line: " + line);
/*     */       }
/*     */     }
/* 673 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void parseCondFlag(String line, RewriteCond condition, String flag)
/*     */   {
/* 684 */     if ((flag.equals("NC")) || (flag.equals("nocase"))) {
/* 685 */       condition.setNocase(true);
/* 686 */     } else if ((flag.equals("OR")) || (flag.equals("ornext"))) {
/* 687 */       condition.setOrnext(true);
/*     */     } else {
/* 689 */       throw new IllegalArgumentException("Invalid flag in: " + line + " flags: " + flag);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void parseRuleFlag(String line, RewriteRule rule, String flag)
/*     */   {
/* 701 */     if (flag.equals("B")) {
/* 702 */       rule.setEscapeBackReferences(true);
/* 703 */     } else if ((flag.equals("chain")) || (flag.equals("C"))) {
/* 704 */       rule.setChain(true);
/* 705 */     } else if ((flag.startsWith("cookie=")) || (flag.startsWith("CO="))) {
/* 706 */       rule.setCookie(true);
/* 707 */       if (flag.startsWith("cookie")) {
/* 708 */         flag = flag.substring("cookie=".length());
/* 709 */       } else if (flag.startsWith("CO=")) {
/* 710 */         flag = flag.substring("CO=".length());
/*     */       }
/* 712 */       StringTokenizer tokenizer = new StringTokenizer(flag, ":");
/* 713 */       if (tokenizer.countTokens() < 2) {
/* 714 */         throw new IllegalArgumentException("Invalid flag in: " + line);
/*     */       }
/* 716 */       rule.setCookieName(tokenizer.nextToken());
/* 717 */       rule.setCookieValue(tokenizer.nextToken());
/* 718 */       if (tokenizer.hasMoreTokens()) {
/* 719 */         rule.setCookieDomain(tokenizer.nextToken());
/*     */       }
/* 721 */       if (tokenizer.hasMoreTokens()) {
/*     */         try {
/* 723 */           rule.setCookieLifetime(Integer.parseInt(tokenizer.nextToken()));
/*     */         } catch (NumberFormatException ???) {
/* 725 */           throw new IllegalArgumentException("Invalid flag in: " + line, ???);
/*     */         }
/*     */       }
/* 728 */       if (tokenizer.hasMoreTokens()) {
/* 729 */         rule.setCookiePath(tokenizer.nextToken());
/*     */       }
/* 731 */       if (tokenizer.hasMoreTokens()) {
/* 732 */         rule.setCookieSecure(Boolean.parseBoolean(tokenizer.nextToken()));
/*     */       }
/* 734 */       if (tokenizer.hasMoreTokens()) {
/* 735 */         rule.setCookieHttpOnly(Boolean.parseBoolean(tokenizer.nextToken()));
/*     */       }
/* 737 */     } else if ((flag.startsWith("env=")) || (flag.startsWith("E="))) {
/* 738 */       rule.setEnv(true);
/* 739 */       if (flag.startsWith("env=")) {
/* 740 */         flag = flag.substring("env=".length());
/* 741 */       } else if (flag.startsWith("E=")) {
/* 742 */         flag = flag.substring("E=".length());
/*     */       }
/* 744 */       int pos = flag.indexOf(':');
/* 745 */       if ((pos == -1) || (pos + 1 == flag.length())) {
/* 746 */         throw new IllegalArgumentException("Invalid flag in: " + line);
/*     */       }
/* 748 */       rule.addEnvName(flag.substring(0, pos));
/* 749 */       rule.addEnvValue(flag.substring(pos + 1));
/* 750 */     } else if ((flag.startsWith("forbidden")) || (flag.startsWith("F"))) {
/* 751 */       rule.setForbidden(true);
/* 752 */     } else if ((flag.startsWith("gone")) || (flag.startsWith("G"))) {
/* 753 */       rule.setGone(true);
/* 754 */     } else if ((flag.startsWith("host")) || (flag.startsWith("H"))) {
/* 755 */       rule.setHost(true);
/* 756 */     } else if ((flag.startsWith("last")) || (flag.startsWith("L"))) {
/* 757 */       rule.setLast(true);
/* 758 */     } else if ((flag.startsWith("nocase")) || (flag.startsWith("NC"))) {
/* 759 */       rule.setNocase(true);
/* 760 */     } else if ((flag.startsWith("noescape")) || (flag.startsWith("NE"))) {
/* 761 */       rule.setNoescape(true);
/* 762 */     } else if ((flag.startsWith("next")) || (flag.startsWith("N"))) {
/* 763 */       rule.setNext(true);
/*     */ 
/*     */     }
/* 766 */     else if ((flag.startsWith("qsappend")) || (flag.startsWith("QSA"))) {
/* 767 */       rule.setQsappend(true);
/* 768 */     } else if ((flag.startsWith("redirect")) || (flag.startsWith("R"))) {
/* 769 */       rule.setRedirect(true);
/* 770 */       int redirectCode = 302;
/* 771 */       if ((flag.startsWith("redirect=")) || (flag.startsWith("R="))) {
/* 772 */         if (flag.startsWith("redirect=")) {
/* 773 */           flag = flag.substring("redirect=".length());
/* 774 */         } else if (flag.startsWith("R=")) {
/* 775 */           flag = flag.substring("R=".length());
/*     */         }
/* 777 */         switch (flag) {
/*     */         case "temp": 
/* 779 */           redirectCode = 302;
/* 780 */           break;
/*     */         case "permanent": 
/* 782 */           redirectCode = 301;
/* 783 */           break;
/*     */         case "seeother": 
/* 785 */           redirectCode = 303;
/* 786 */           break;
/*     */         default: 
/* 788 */           redirectCode = Integer.parseInt(flag);
/*     */         }
/*     */         
/*     */       }
/* 792 */       rule.setRedirectCode(redirectCode);
/* 793 */     } else if ((flag.startsWith("skip")) || (flag.startsWith("S"))) {
/* 794 */       if (flag.startsWith("skip=")) {
/* 795 */         flag = flag.substring("skip=".length());
/* 796 */       } else if (flag.startsWith("S=")) {
/* 797 */         flag = flag.substring("S=".length());
/*     */       }
/* 799 */       rule.setSkip(Integer.parseInt(flag));
/* 800 */     } else if ((flag.startsWith("type")) || (flag.startsWith("T"))) {
/* 801 */       if (flag.startsWith("type=")) {
/* 802 */         flag = flag.substring("type=".length());
/* 803 */       } else if (flag.startsWith("T=")) {
/* 804 */         flag = flag.substring("T=".length());
/*     */       }
/* 806 */       rule.setType(true);
/* 807 */       rule.setTypeValue(flag);
/*     */     } else {
/* 809 */       throw new IllegalArgumentException("Invalid flag in: " + line + " flag: " + flag);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\rewrite\RewriteValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */