/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Locale;
/*     */ import java.util.Scanner;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.util.ServerInfo;
/*     */ import org.apache.coyote.ActionCode;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.util.security.Escape;
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
/*     */ public class ErrorReportValve
/*     */   extends ValveBase
/*     */ {
/*  53 */   private boolean showReport = true;
/*     */   
/*  55 */   private boolean showServerInfo = true;
/*     */   
/*     */   public ErrorReportValve()
/*     */   {
/*  59 */     super(true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invoke(Request request, org.apache.catalina.connector.Response response)
/*     */     throws IOException, ServletException
/*     */   {
/*  81 */     getNext().invoke(request, response);
/*     */     
/*  83 */     if (response.isCommitted()) {
/*  84 */       if (response.setErrorReported())
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*  89 */           response.flushBuffer();
/*     */         } catch (Throwable t) {
/*  91 */           ExceptionUtils.handleThrowable(t);
/*     */         }
/*     */         
/*     */ 
/*  95 */         response.getCoyoteResponse().action(ActionCode.CLOSE_NOW, null);
/*     */       }
/*  97 */       return;
/*     */     }
/*     */     
/* 100 */     Throwable throwable = (Throwable)request.getAttribute("javax.servlet.error.exception");
/*     */     
/*     */ 
/*     */ 
/* 104 */     if ((request.isAsync()) && (!request.isAsyncCompleting())) {
/* 105 */       return;
/*     */     }
/*     */     
/* 108 */     if ((throwable != null) && (!response.isError()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */       response.reset();
/* 115 */       response.sendError(500);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 121 */     response.setSuspended(false);
/*     */     try
/*     */     {
/* 124 */       report(request, response, throwable);
/*     */     } catch (Throwable tt) {
/* 126 */       ExceptionUtils.handleThrowable(tt);
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
/*     */ 
/*     */ 
/*     */   protected void report(Request request, org.apache.catalina.connector.Response response, Throwable throwable)
/*     */   {
/* 144 */     int statusCode = response.getStatus();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */     if ((statusCode < 400) || (response.getContentWritten() > 0L) || (!response.setErrorReported())) {
/* 151 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 156 */     AtomicBoolean result = new AtomicBoolean(false);
/* 157 */     response.getCoyoteResponse().action(ActionCode.IS_IO_ALLOWED, result);
/* 158 */     if (!result.get()) {
/* 159 */       return;
/*     */     }
/*     */     
/* 162 */     String message = Escape.htmlElementContent(response.getMessage());
/* 163 */     if (message == null) {
/* 164 */       if (throwable != null) {
/* 165 */         String exceptionMessage = throwable.getMessage();
/* 166 */         if ((exceptionMessage != null) && (exceptionMessage.length() > 0)) {
/* 167 */           message = Escape.htmlElementContent(new Scanner(exceptionMessage).nextLine());
/*     */         }
/*     */       }
/* 170 */       if (message == null) {
/* 171 */         message = "";
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 177 */     String reason = null;
/* 178 */     String description = null;
/* 179 */     StringManager smClient = StringManager.getManager("org.apache.catalina.valves", request
/* 180 */       .getLocales());
/* 181 */     response.setLocale(smClient.getLocale());
/*     */     try {
/* 183 */       reason = smClient.getString("http." + statusCode + ".reason");
/* 184 */       description = smClient.getString("http." + statusCode + ".desc");
/*     */     } catch (Throwable t) {
/* 186 */       ExceptionUtils.handleThrowable(t);
/*     */     }
/* 188 */     if ((reason == null) || (description == null)) {
/* 189 */       if (message.isEmpty()) {
/* 190 */         return;
/*     */       }
/* 192 */       reason = smClient.getString("errorReportValve.unknownReason");
/* 193 */       description = smClient.getString("errorReportValve.noDescription");
/*     */     }
/*     */     
/*     */ 
/* 197 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 199 */     sb.append("<!doctype html><html lang=\"");
/* 200 */     sb.append(smClient.getLocale().getLanguage()).append("\">");
/* 201 */     sb.append("<head>");
/* 202 */     sb.append("<title>");
/* 203 */     sb.append(smClient.getString("errorReportValve.statusHeader", new Object[] {
/* 204 */       String.valueOf(statusCode), reason }));
/* 205 */     sb.append("</title>");
/* 206 */     sb.append("<style type=\"text/css\">");
/* 207 */     sb.append("h1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} h2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} h3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} body {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} b {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} p {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;} a {color:black;} a.name {color:black;} .line {height:1px;background-color:#525D76;border:none;}");
/* 208 */     sb.append("</style>");
/* 209 */     sb.append("</head><body>");
/* 210 */     sb.append("<h1>");
/* 211 */     sb.append(smClient.getString("errorReportValve.statusHeader", new Object[] {
/* 212 */       String.valueOf(statusCode), reason })).append("</h1>");
/* 213 */     if (isShowReport()) {
/* 214 */       sb.append("<hr class=\"line\" />");
/* 215 */       sb.append("<p><b>");
/* 216 */       sb.append(smClient.getString("errorReportValve.type"));
/* 217 */       sb.append("</b> ");
/* 218 */       if (throwable != null) {
/* 219 */         sb.append(smClient.getString("errorReportValve.exceptionReport"));
/*     */       } else {
/* 221 */         sb.append(smClient.getString("errorReportValve.statusReport"));
/*     */       }
/* 223 */       sb.append("</p>");
/* 224 */       if (!message.isEmpty()) {
/* 225 */         sb.append("<p><b>");
/* 226 */         sb.append(smClient.getString("errorReportValve.message"));
/* 227 */         sb.append("</b> ");
/* 228 */         sb.append(message).append("</p>");
/*     */       }
/* 230 */       sb.append("<p><b>");
/* 231 */       sb.append(smClient.getString("errorReportValve.description"));
/* 232 */       sb.append("</b> ");
/* 233 */       sb.append(description);
/* 234 */       sb.append("</p>");
/* 235 */       if (throwable != null) {
/* 236 */         String stackTrace = getPartialServletStackTrace(throwable);
/* 237 */         sb.append("<p><b>");
/* 238 */         sb.append(smClient.getString("errorReportValve.exception"));
/* 239 */         sb.append("</b></p><pre>");
/* 240 */         sb.append(Escape.htmlElementContent(stackTrace));
/* 241 */         sb.append("</pre>");
/*     */         
/* 243 */         int loops = 0;
/* 244 */         Throwable rootCause = throwable.getCause();
/* 245 */         while ((rootCause != null) && (loops < 10)) {
/* 246 */           stackTrace = getPartialServletStackTrace(rootCause);
/* 247 */           sb.append("<p><b>");
/* 248 */           sb.append(smClient.getString("errorReportValve.rootCause"));
/* 249 */           sb.append("</b></p><pre>");
/* 250 */           sb.append(Escape.htmlElementContent(stackTrace));
/* 251 */           sb.append("</pre>");
/*     */           
/* 253 */           rootCause = rootCause.getCause();
/* 254 */           loops++;
/*     */         }
/*     */         
/* 257 */         sb.append("<p><b>");
/* 258 */         sb.append(smClient.getString("errorReportValve.note"));
/* 259 */         sb.append("</b> ");
/* 260 */         sb.append(smClient.getString("errorReportValve.rootCauseInLogs"));
/* 261 */         sb.append("</p>");
/*     */       }
/*     */       
/* 264 */       sb.append("<hr class=\"line\" />");
/*     */     }
/* 266 */     if (isShowServerInfo()) {
/* 267 */       sb.append("<h3>").append(ServerInfo.getServerInfo()).append("</h3>");
/*     */     }
/* 269 */     sb.append("</body></html>");
/*     */     try
/*     */     {
/*     */       try {
/* 273 */         response.setContentType("text/html");
/* 274 */         response.setCharacterEncoding("utf-8");
/*     */       } catch (Throwable t) {
/* 276 */         ExceptionUtils.handleThrowable(t);
/* 277 */         if (this.container.getLogger().isDebugEnabled()) {
/* 278 */           this.container.getLogger().debug("status.setContentType", t);
/*     */         }
/*     */       }
/* 281 */       Writer writer = response.getReporter();
/* 282 */       if (writer != null)
/*     */       {
/*     */ 
/* 285 */         writer.write(sb.toString());
/* 286 */         response.finishResponse();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException) {}catch (IllegalStateException localIllegalStateException) {}
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
/*     */   protected String getPartialServletStackTrace(Throwable t)
/*     */   {
/* 304 */     StringBuilder trace = new StringBuilder();
/* 305 */     trace.append(t.toString()).append(System.lineSeparator());
/* 306 */     StackTraceElement[] elements = t.getStackTrace();
/* 307 */     int pos = elements.length;
/* 308 */     for (int i = elements.length - 1; i >= 0; i--)
/*     */     {
/* 310 */       if ((elements[i].getClassName().startsWith("org.apache.catalina.core.ApplicationFilterChain")) && 
/* 311 */         (elements[i].getMethodName().equals("internalDoFilter"))) {
/* 312 */         pos = i;
/* 313 */         break;
/*     */       }
/*     */     }
/* 316 */     for (int i = 0; i < pos; i++)
/*     */     {
/* 318 */       if (!elements[i].getClassName().startsWith("org.apache.catalina.core.")) {
/* 319 */         trace.append('\t').append(elements[i].toString()).append(System.lineSeparator());
/*     */       }
/*     */     }
/* 322 */     return trace.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShowReport(boolean showReport)
/*     */   {
/* 331 */     this.showReport = showReport;
/*     */   }
/*     */   
/*     */   public boolean isShowReport() {
/* 335 */     return this.showReport;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShowServerInfo(boolean showServerInfo)
/*     */   {
/* 344 */     this.showServerInfo = showServerInfo;
/*     */   }
/*     */   
/*     */   public boolean isShowServerInfo() {
/* 348 */     return this.showServerInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\ErrorReportValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */