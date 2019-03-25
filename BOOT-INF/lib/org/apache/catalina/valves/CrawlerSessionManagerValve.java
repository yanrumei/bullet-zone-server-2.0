/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.http.HttpSessionBindingEvent;
/*     */ import javax.servlet.http.HttpSessionBindingListener;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
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
/*     */ public class CrawlerSessionManagerValve
/*     */   extends ValveBase
/*     */   implements HttpSessionBindingListener
/*     */ {
/*  45 */   private static final Log log = LogFactory.getLog(CrawlerSessionManagerValve.class);
/*     */   
/*  47 */   private final Map<String, String> clientIpSessionId = new ConcurrentHashMap();
/*  48 */   private final Map<String, String> sessionIdClientIp = new ConcurrentHashMap();
/*     */   
/*  50 */   private String crawlerUserAgents = ".*[bB]ot.*|.*Yahoo! Slurp.*|.*Feedfetcher-Google.*";
/*  51 */   private Pattern uaPattern = null;
/*     */   
/*  53 */   private String crawlerIps = null;
/*  54 */   private Pattern ipPattern = null;
/*     */   
/*  56 */   private int sessionInactiveInterval = 60;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CrawlerSessionManagerValve()
/*     */   {
/*  63 */     super(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCrawlerUserAgents(String crawlerUserAgents)
/*     */   {
/*  75 */     this.crawlerUserAgents = crawlerUserAgents;
/*  76 */     if ((crawlerUserAgents == null) || (crawlerUserAgents.length() == 0)) {
/*  77 */       this.uaPattern = null;
/*     */     } else {
/*  79 */       this.uaPattern = Pattern.compile(crawlerUserAgents);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCrawlerUserAgents()
/*     */   {
/*  88 */     return this.crawlerUserAgents;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCrawlerIps(String crawlerIps)
/*     */   {
/* 100 */     this.crawlerIps = crawlerIps;
/* 101 */     if ((crawlerIps == null) || (crawlerIps.length() == 0)) {
/* 102 */       this.ipPattern = null;
/*     */     } else {
/* 104 */       this.ipPattern = Pattern.compile(crawlerIps);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCrawlerIps()
/*     */   {
/* 113 */     return this.crawlerIps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSessionInactiveInterval(int sessionInactiveInterval)
/*     */   {
/* 124 */     this.sessionInactiveInterval = sessionInactiveInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSessionInactiveInterval()
/*     */   {
/* 132 */     return this.sessionInactiveInterval;
/*     */   }
/*     */   
/*     */   public Map<String, String> getClientIpSessionId()
/*     */   {
/* 137 */     return this.clientIpSessionId;
/*     */   }
/*     */   
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 143 */     super.initInternal();
/*     */     
/* 145 */     this.uaPattern = Pattern.compile(this.crawlerUserAgents);
/*     */   }
/*     */   
/*     */ 
/*     */   public void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 152 */     boolean isBot = false;
/* 153 */     String sessionId = null;
/* 154 */     String clientIp = request.getRemoteAddr();
/*     */     
/* 156 */     if (log.isDebugEnabled()) {
/* 157 */       log.debug(request.hashCode() + ": ClientIp=" + clientIp + ", RequestedSessionId=" + request
/* 158 */         .getRequestedSessionId());
/*     */     }
/*     */     
/*     */ 
/* 162 */     if (request.getSession(false) == null)
/*     */     {
/*     */ 
/* 165 */       Enumeration<String> uaHeaders = request.getHeaders("user-agent");
/* 166 */       String uaHeader = null;
/* 167 */       if (uaHeaders.hasMoreElements()) {
/* 168 */         uaHeader = (String)uaHeaders.nextElement();
/*     */       }
/*     */       
/*     */ 
/* 172 */       if ((uaHeader != null) && (!uaHeaders.hasMoreElements()))
/*     */       {
/* 174 */         if (log.isDebugEnabled()) {
/* 175 */           log.debug(request.hashCode() + ": UserAgent=" + uaHeader);
/*     */         }
/*     */         
/* 178 */         if (this.uaPattern.matcher(uaHeader).matches()) {
/* 179 */           isBot = true;
/*     */           
/* 181 */           if (log.isDebugEnabled()) {
/* 182 */             log.debug(request.hashCode() + ": Bot found. UserAgent=" + uaHeader);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 187 */       if ((this.ipPattern != null) && (this.ipPattern.matcher(clientIp).matches())) {
/* 188 */         isBot = true;
/*     */         
/* 190 */         if (log.isDebugEnabled()) {
/* 191 */           log.debug(request.hashCode() + ": Bot found. IP=" + clientIp);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 196 */       if (isBot) {
/* 197 */         sessionId = (String)this.clientIpSessionId.get(clientIp);
/* 198 */         if (sessionId != null) {
/* 199 */           request.setRequestedSessionId(sessionId);
/* 200 */           if (log.isDebugEnabled()) {
/* 201 */             log.debug(request.hashCode() + ": SessionID=" + sessionId);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 207 */     getNext().invoke(request, response);
/*     */     
/* 209 */     if (isBot) {
/* 210 */       if (sessionId == null)
/*     */       {
/* 212 */         HttpSession s = request.getSession(false);
/* 213 */         if (s != null) {
/* 214 */           this.clientIpSessionId.put(clientIp, s.getId());
/* 215 */           this.sessionIdClientIp.put(s.getId(), clientIp);
/*     */           
/* 217 */           s.setAttribute(getClass().getName(), this);
/* 218 */           s.setMaxInactiveInterval(this.sessionInactiveInterval);
/*     */           
/* 220 */           if (log.isDebugEnabled()) {
/* 221 */             log.debug(request.hashCode() + ": New bot session. SessionID=" + s.getId());
/*     */           }
/*     */         }
/*     */       }
/* 225 */       else if (log.isDebugEnabled()) {
/* 226 */         log.debug(request
/* 227 */           .hashCode() + ": Bot session accessed. SessionID=" + sessionId);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void valueBound(HttpSessionBindingEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void valueUnbound(HttpSessionBindingEvent event)
/*     */   {
/* 242 */     String clientIp = (String)this.sessionIdClientIp.remove(event.getSession().getId());
/* 243 */     if (clientIp != null) {
/* 244 */       this.clientIpSessionId.remove(clientIp);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\CrawlerSessionManagerValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */