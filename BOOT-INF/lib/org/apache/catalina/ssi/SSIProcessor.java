/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.catalina.util.IOTools;
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
/*     */ public class SSIProcessor
/*     */ {
/*     */   protected static final String COMMAND_START = "<!--#";
/*     */   protected static final String COMMAND_END = "-->";
/*     */   protected final SSIExternalResolver ssiExternalResolver;
/*  43 */   protected final HashMap<String, SSICommand> commands = new HashMap();
/*     */   
/*     */   protected final int debug;
/*     */   protected final boolean allowExec;
/*     */   
/*     */   public SSIProcessor(SSIExternalResolver ssiExternalResolver, int debug, boolean allowExec)
/*     */   {
/*  50 */     this.ssiExternalResolver = ssiExternalResolver;
/*  51 */     this.debug = debug;
/*  52 */     this.allowExec = allowExec;
/*  53 */     addBuiltinCommands();
/*     */   }
/*     */   
/*     */   protected void addBuiltinCommands()
/*     */   {
/*  58 */     addCommand("config", new SSIConfig());
/*  59 */     addCommand("echo", new SSIEcho());
/*  60 */     if (this.allowExec) {
/*  61 */       addCommand("exec", new SSIExec());
/*     */     }
/*  63 */     addCommand("include", new SSIInclude());
/*  64 */     addCommand("flastmod", new SSIFlastmod());
/*  65 */     addCommand("fsize", new SSIFsize());
/*  66 */     addCommand("printenv", new SSIPrintenv());
/*  67 */     addCommand("set", new SSISet());
/*  68 */     SSIConditional ssiConditional = new SSIConditional();
/*  69 */     addCommand("if", ssiConditional);
/*  70 */     addCommand("elif", ssiConditional);
/*  71 */     addCommand("endif", ssiConditional);
/*  72 */     addCommand("else", ssiConditional);
/*     */   }
/*     */   
/*     */   public void addCommand(String name, SSICommand command)
/*     */   {
/*  77 */     this.commands.put(name, command);
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
/*     */   public long process(Reader reader, long lastModifiedDate, PrintWriter writer)
/*     */     throws IOException
/*     */   {
/*  98 */     SSIMediator ssiMediator = new SSIMediator(this.ssiExternalResolver, lastModifiedDate);
/*     */     
/* 100 */     StringWriter stringWriter = new StringWriter();
/* 101 */     IOTools.flow(reader, stringWriter);
/* 102 */     String fileContents = stringWriter.toString();
/* 103 */     stringWriter = null;
/* 104 */     int index = 0;
/* 105 */     boolean inside = false;
/* 106 */     StringBuilder command = new StringBuilder();
/*     */     try {
/* 108 */       while (index < fileContents.length()) {
/* 109 */         char c = fileContents.charAt(index);
/* 110 */         if (!inside) {
/* 111 */           if ((c == "<!--#".charAt(0)) && 
/* 112 */             (charCmp(fileContents, index, "<!--#"))) {
/* 113 */             inside = true;
/* 114 */             index += "<!--#".length();
/* 115 */             command.setLength(0);
/*     */           } else {
/* 117 */             if (!ssiMediator.getConditionalState().processConditionalCommandsOnly) {
/* 118 */               writer.write(c);
/*     */             }
/* 120 */             index++;
/*     */           }
/*     */         }
/* 123 */         else if ((c == "-->".charAt(0)) && 
/* 124 */           (charCmp(fileContents, index, "-->"))) {
/* 125 */           inside = false;
/* 126 */           index += "-->".length();
/* 127 */           String strCmd = parseCmd(command);
/* 128 */           if (this.debug > 0) {
/* 129 */             this.ssiExternalResolver.log("SSIProcessor.process -- processing command: " + strCmd, null);
/*     */           }
/*     */           
/*     */ 
/* 133 */           String[] paramNames = parseParamNames(command, strCmd
/* 134 */             .length());
/* 135 */           String[] paramValues = parseParamValues(command, strCmd
/* 136 */             .length(), paramNames.length);
/*     */           
/*     */ 
/*     */ 
/* 140 */           String configErrMsg = ssiMediator.getConfigErrMsg();
/*     */           
/* 142 */           SSICommand ssiCommand = (SSICommand)this.commands.get(strCmd.toLowerCase(Locale.ENGLISH));
/* 143 */           String errorMessage = null;
/* 144 */           if (ssiCommand == null) {
/* 145 */             errorMessage = "Unknown command: " + strCmd;
/* 146 */           } else if (paramValues == null) {
/* 147 */             errorMessage = "Error parsing directive parameters.";
/* 148 */           } else if (paramNames.length != paramValues.length) {
/* 149 */             errorMessage = "Parameter names count does not match parameter values count on command: " + strCmd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 156 */           else if ((!ssiMediator.getConditionalState().processConditionalCommandsOnly) || ((ssiCommand instanceof SSIConditional)))
/*     */           {
/* 158 */             long lmd = ssiCommand.process(ssiMediator, strCmd, paramNames, paramValues, writer);
/*     */             
/* 160 */             if (lmd > lastModifiedDate) {
/* 161 */               lastModifiedDate = lmd;
/*     */             }
/*     */           }
/*     */           
/* 165 */           if (errorMessage != null) {
/* 166 */             this.ssiExternalResolver.log(errorMessage, null);
/* 167 */             writer.write(configErrMsg);
/*     */           }
/*     */         } else {
/* 170 */           command.append(c);
/* 171 */           index++;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SSIStopProcessingException localSSIStopProcessingException) {}
/*     */     
/*     */ 
/*     */ 
/* 179 */     return lastModifiedDate;
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
/*     */   protected String[] parseParamNames(StringBuilder cmd, int start)
/*     */   {
/* 193 */     int bIdx = start;
/* 194 */     int i = 0;
/* 195 */     int quotes = 0;
/* 196 */     boolean inside = false;
/* 197 */     StringBuilder retBuf = new StringBuilder();
/* 198 */     while (bIdx < cmd.length())
/* 199 */       if (!inside) {
/* 200 */         while ((bIdx < cmd.length()) && (isSpace(cmd.charAt(bIdx))))
/* 201 */           bIdx++;
/* 202 */         if (bIdx >= cmd.length()) break;
/* 203 */         inside = !inside;
/*     */       } else {
/* 205 */         while ((bIdx < cmd.length()) && (cmd.charAt(bIdx) != '=')) {
/* 206 */           retBuf.append(cmd.charAt(bIdx));
/* 207 */           bIdx++;
/*     */         }
/* 209 */         retBuf.append('=');
/* 210 */         inside = !inside;
/* 211 */         quotes = 0;
/* 212 */         boolean escaped = false;
/* 213 */         for (; (bIdx < cmd.length()) && (quotes != 2); bIdx++) {
/* 214 */           char c = cmd.charAt(bIdx);
/*     */           
/* 216 */           if ((c == '\\') && (!escaped)) {
/* 217 */             escaped = true;
/*     */           }
/*     */           else {
/* 220 */             if ((c == '"') && (!escaped)) quotes++;
/* 221 */             escaped = false;
/*     */           }
/*     */         }
/*     */       }
/* 225 */     StringTokenizer str = new StringTokenizer(retBuf.toString(), "=");
/* 226 */     String[] retString = new String[str.countTokens()];
/* 227 */     while (str.hasMoreTokens()) {
/* 228 */       retString[(i++)] = str.nextToken().trim();
/*     */     }
/* 230 */     return retString;
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
/*     */   protected String[] parseParamValues(StringBuilder cmd, int start, int count)
/*     */   {
/* 245 */     int valIndex = 0;
/* 246 */     boolean inside = false;
/* 247 */     String[] vals = new String[count];
/* 248 */     StringBuilder sb = new StringBuilder();
/* 249 */     char endQuote = '\000';
/* 250 */     for (int bIdx = start; bIdx < cmd.length(); bIdx++) {
/* 251 */       if (!inside) {
/* 252 */         while ((bIdx < cmd.length()) && (!isQuote(cmd.charAt(bIdx))))
/* 253 */           bIdx++;
/* 254 */         if (bIdx >= cmd.length()) break;
/* 255 */         inside = !inside;
/* 256 */         endQuote = cmd.charAt(bIdx);
/*     */       } else {
/* 258 */         boolean escaped = false;
/* 259 */         for (; bIdx < cmd.length(); bIdx++) {
/* 260 */           char c = cmd.charAt(bIdx);
/*     */           
/* 262 */           if ((c == '\\') && (!escaped)) {
/* 263 */             escaped = true;
/*     */           }
/*     */           else
/*     */           {
/* 267 */             if ((c == endQuote) && (!escaped)) {
/*     */               break;
/*     */             }
/*     */             
/* 271 */             if ((c == '$') && (escaped)) sb.append('\\');
/* 272 */             escaped = false;
/* 273 */             sb.append(c);
/*     */           }
/*     */         }
/*     */         
/* 277 */         if (bIdx == cmd.length()) return null;
/* 278 */         vals[(valIndex++)] = sb.toString();
/* 279 */         sb.delete(0, sb.length());
/* 280 */         inside = !inside;
/*     */       }
/*     */     }
/* 283 */     return vals;
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
/*     */   private String parseCmd(StringBuilder cmd)
/*     */   {
/* 296 */     int firstLetter = -1;
/* 297 */     int lastLetter = -1;
/* 298 */     for (int i = 0; i < cmd.length(); i++) {
/* 299 */       char c = cmd.charAt(i);
/* 300 */       if (Character.isLetter(c)) {
/* 301 */         if (firstLetter == -1) {
/* 302 */           firstLetter = i;
/*     */         }
/* 304 */         lastLetter = i;
/* 305 */       } else { if ((!isSpace(c)) || 
/* 306 */           (lastLetter > -1)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 313 */     if (firstLetter == -1) {
/* 314 */       return "";
/*     */     }
/* 316 */     return cmd.substring(firstLetter, lastLetter + 1);
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean charCmp(String buf, int index, String command)
/*     */   {
/* 322 */     return buf.regionMatches(index, command, 0, command.length());
/*     */   }
/*     */   
/*     */   protected boolean isSpace(char c)
/*     */   {
/* 327 */     return (c == ' ') || (c == '\n') || (c == '\t') || (c == '\r');
/*     */   }
/*     */   
/*     */   protected boolean isQuote(char c) {
/* 331 */     return (c == '\'') || (c == '"') || (c == '`');
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */