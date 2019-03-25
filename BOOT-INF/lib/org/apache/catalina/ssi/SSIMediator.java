/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.catalina.util.Strftime;
/*     */ import org.apache.catalina.util.URLEncoder;
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
/*     */ public class SSIMediator
/*     */ {
/*     */   protected static final String DEFAULT_CONFIG_ERR_MSG = "[an error occurred while processing this directive]";
/*     */   protected static final String DEFAULT_CONFIG_TIME_FMT = "%A, %d-%b-%Y %T %Z";
/*     */   protected static final String DEFAULT_CONFIG_SIZE_FMT = "abbrev";
/*  47 */   protected String configErrMsg = "[an error occurred while processing this directive]";
/*  48 */   protected String configTimeFmt = "%A, %d-%b-%Y %T %Z";
/*  49 */   protected String configSizeFmt = "abbrev";
/*  50 */   protected final String className = getClass().getName();
/*     */   protected final SSIExternalResolver ssiExternalResolver;
/*     */   protected final long lastModifiedDate;
/*     */   protected Strftime strftime;
/*  54 */   protected final SSIConditionalState conditionalState = new SSIConditionalState();
/*     */   
/*     */ 
/*     */   public SSIMediator(SSIExternalResolver ssiExternalResolver, long lastModifiedDate)
/*     */   {
/*  59 */     this.ssiExternalResolver = ssiExternalResolver;
/*  60 */     this.lastModifiedDate = lastModifiedDate;
/*  61 */     setConfigTimeFmt("%A, %d-%b-%Y %T %Z", true);
/*     */   }
/*     */   
/*     */   public void setConfigErrMsg(String configErrMsg)
/*     */   {
/*  66 */     this.configErrMsg = configErrMsg;
/*     */   }
/*     */   
/*     */   public void setConfigTimeFmt(String configTimeFmt)
/*     */   {
/*  71 */     setConfigTimeFmt(configTimeFmt, false);
/*     */   }
/*     */   
/*     */   public void setConfigTimeFmt(String configTimeFmt, boolean fromConstructor)
/*     */   {
/*  76 */     this.configTimeFmt = configTimeFmt;
/*  77 */     this.strftime = new Strftime(configTimeFmt, Locale.US);
/*     */     
/*     */ 
/*     */ 
/*  81 */     setDateVariables(fromConstructor);
/*     */   }
/*     */   
/*     */   public void setConfigSizeFmt(String configSizeFmt)
/*     */   {
/*  86 */     this.configSizeFmt = configSizeFmt;
/*     */   }
/*     */   
/*     */   public String getConfigErrMsg()
/*     */   {
/*  91 */     return this.configErrMsg;
/*     */   }
/*     */   
/*     */   public String getConfigTimeFmt()
/*     */   {
/*  96 */     return this.configTimeFmt;
/*     */   }
/*     */   
/*     */   public String getConfigSizeFmt()
/*     */   {
/* 101 */     return this.configSizeFmt;
/*     */   }
/*     */   
/*     */   public SSIConditionalState getConditionalState()
/*     */   {
/* 106 */     return this.conditionalState;
/*     */   }
/*     */   
/*     */   public Collection<String> getVariableNames()
/*     */   {
/* 111 */     Set<String> variableNames = new HashSet();
/*     */     
/*     */ 
/*     */ 
/* 115 */     variableNames.add("DATE_GMT");
/* 116 */     variableNames.add("DATE_LOCAL");
/* 117 */     variableNames.add("LAST_MODIFIED");
/* 118 */     this.ssiExternalResolver.addVariableNames(variableNames);
/*     */     
/* 120 */     Iterator<String> iter = variableNames.iterator();
/* 121 */     while (iter.hasNext()) {
/* 122 */       String name = (String)iter.next();
/* 123 */       if (isNameReserved(name)) {
/* 124 */         iter.remove();
/*     */       }
/*     */     }
/* 127 */     return variableNames;
/*     */   }
/*     */   
/*     */   public long getFileSize(String path, boolean virtual) throws IOException
/*     */   {
/* 132 */     return this.ssiExternalResolver.getFileSize(path, virtual);
/*     */   }
/*     */   
/*     */   public long getFileLastModified(String path, boolean virtual)
/*     */     throws IOException
/*     */   {
/* 138 */     return this.ssiExternalResolver.getFileLastModified(path, virtual);
/*     */   }
/*     */   
/*     */   public String getFileText(String path, boolean virtual) throws IOException
/*     */   {
/* 143 */     return this.ssiExternalResolver.getFileText(path, virtual);
/*     */   }
/*     */   
/*     */   protected boolean isNameReserved(String name)
/*     */   {
/* 148 */     return name.startsWith(this.className + ".");
/*     */   }
/*     */   
/*     */   public String getVariableValue(String variableName)
/*     */   {
/* 153 */     return getVariableValue(variableName, "none");
/*     */   }
/*     */   
/*     */   public void setVariableValue(String variableName, String variableValue)
/*     */   {
/* 158 */     if (!isNameReserved(variableName)) {
/* 159 */       this.ssiExternalResolver.setVariableValue(variableName, variableValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getVariableValue(String variableName, String encoding)
/*     */   {
/* 165 */     String lowerCaseVariableName = variableName.toLowerCase(Locale.ENGLISH);
/* 166 */     String variableValue = null;
/* 167 */     if (!isNameReserved(lowerCaseVariableName))
/*     */     {
/*     */ 
/*     */ 
/* 171 */       variableValue = this.ssiExternalResolver.getVariableValue(variableName);
/* 172 */       if (variableValue == null) {
/* 173 */         variableName = variableName.toUpperCase(Locale.ENGLISH);
/*     */         
/* 175 */         variableValue = this.ssiExternalResolver.getVariableValue(this.className + "." + variableName);
/*     */       }
/* 177 */       if (variableValue != null) {
/* 178 */         variableValue = encode(variableValue, encoding);
/*     */       }
/*     */     }
/* 181 */     return variableValue;
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
/*     */   public String substituteVariables(String val)
/*     */   {
/* 194 */     if ((val.indexOf('$') < 0) && (val.indexOf('&') < 0)) { return val;
/*     */     }
/*     */     
/* 197 */     val = val.replace("&lt;", "<");
/* 198 */     val = val.replace("&gt;", ">");
/* 199 */     val = val.replace("&quot;", "\"");
/* 200 */     val = val.replace("&amp;", "&");
/*     */     
/* 202 */     StringBuilder sb = new StringBuilder(val);
/* 203 */     int charStart = sb.indexOf("&#");
/* 204 */     while (charStart > -1) {
/* 205 */       int charEnd = sb.indexOf(";", charStart);
/* 206 */       if (charEnd <= -1) break;
/* 207 */       char c = (char)Integer.parseInt(sb
/* 208 */         .substring(charStart + 2, charEnd));
/* 209 */       sb.delete(charStart, charEnd + 1);
/* 210 */       sb.insert(charStart, c);
/* 211 */       charStart = sb.indexOf("&#");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 217 */     for (int i = 0; i < sb.length();)
/*     */     {
/* 219 */       for (; i < sb.length(); i++) {
/* 220 */         if (sb.charAt(i) == '$') {
/* 221 */           i++;
/* 222 */           break;
/*     */         }
/*     */       }
/* 225 */       if (i == sb.length())
/*     */         break;
/* 227 */       if ((i > 1) && (sb.charAt(i - 2) == '\\')) {
/* 228 */         sb.deleteCharAt(i - 2);
/* 229 */         i--;
/*     */       }
/*     */       else {
/* 232 */         int nameStart = i;
/* 233 */         int start = i - 1;
/* 234 */         int end = -1;
/* 235 */         int nameEnd = -1;
/* 236 */         char endChar = ' ';
/*     */         
/* 238 */         if (sb.charAt(i) == '{') {
/* 239 */           nameStart++;
/* 240 */           endChar = '}';
/*     */         }
/* 243 */         for (; 
/* 243 */             i < sb.length(); i++)
/* 244 */           if (sb.charAt(i) == endChar)
/*     */             break;
/* 246 */         end = i;
/* 247 */         nameEnd = end;
/* 248 */         if (endChar == '}') { end++;
/*     */         }
/* 250 */         String varName = sb.substring(nameStart, nameEnd);
/* 251 */         String value = getVariableValue(varName);
/* 252 */         if (value == null) { value = "";
/*     */         }
/* 254 */         sb.replace(start, end, value);
/*     */         
/*     */ 
/* 257 */         i = start + value.length();
/*     */       } }
/* 259 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected String formatDate(Date date, TimeZone timeZone)
/*     */   {
/*     */     String retVal;
/* 265 */     if (timeZone != null)
/*     */     {
/*     */ 
/*     */ 
/* 269 */       TimeZone oldTimeZone = this.strftime.getTimeZone();
/* 270 */       this.strftime.setTimeZone(timeZone);
/* 271 */       String retVal = this.strftime.format(date);
/* 272 */       this.strftime.setTimeZone(oldTimeZone);
/*     */     } else {
/* 274 */       retVal = this.strftime.format(date);
/*     */     }
/* 276 */     return retVal;
/*     */   }
/*     */   
/*     */   protected String encode(String value, String encoding)
/*     */   {
/* 281 */     String retVal = null;
/* 282 */     if (encoding.equalsIgnoreCase("url")) {
/* 283 */       retVal = URLEncoder.DEFAULT.encode(value, StandardCharsets.UTF_8);
/* 284 */     } else if (encoding.equalsIgnoreCase("none")) {
/* 285 */       retVal = value;
/* 286 */     } else if (encoding.equalsIgnoreCase("entity")) {
/* 287 */       retVal = Escape.htmlElementContent(value);
/*     */     }
/*     */     else {
/* 290 */       throw new IllegalArgumentException("Unknown encoding: " + encoding);
/*     */     }
/* 292 */     return retVal;
/*     */   }
/*     */   
/*     */   public void log(String message)
/*     */   {
/* 297 */     this.ssiExternalResolver.log(message, null);
/*     */   }
/*     */   
/*     */   public void log(String message, Throwable throwable)
/*     */   {
/* 302 */     this.ssiExternalResolver.log(message, throwable);
/*     */   }
/*     */   
/*     */   protected void setDateVariables(boolean fromConstructor)
/*     */   {
/* 307 */     boolean alreadySet = this.ssiExternalResolver.getVariableValue(this.className + ".alreadyset") != null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 312 */     if ((!fromConstructor) || (!alreadySet)) {
/* 313 */       this.ssiExternalResolver.setVariableValue(this.className + ".alreadyset", "true");
/*     */       
/* 315 */       Date date = new Date();
/* 316 */       TimeZone timeZone = TimeZone.getTimeZone("GMT");
/* 317 */       String retVal = formatDate(date, timeZone);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 322 */       setVariableValue("DATE_GMT", null);
/* 323 */       this.ssiExternalResolver.setVariableValue(this.className + ".DATE_GMT", retVal);
/*     */       
/* 325 */       retVal = formatDate(date, null);
/* 326 */       setVariableValue("DATE_LOCAL", null);
/* 327 */       this.ssiExternalResolver.setVariableValue(this.className + ".DATE_LOCAL", retVal);
/*     */       
/* 329 */       retVal = formatDate(new Date(this.lastModifiedDate), null);
/* 330 */       setVariableValue("LAST_MODIFIED", null);
/* 331 */       this.ssiExternalResolver.setVariableValue(this.className + ".LAST_MODIFIED", retVal);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIMediator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */