/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.Immutable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @Immutable
/*     */ @GwtCompatible
/*     */ public final class HostAndPort
/*     */   implements Serializable
/*     */ {
/*     */   private static final int NO_PORT = -1;
/*     */   private final String host;
/*     */   private final int port;
/*     */   private final boolean hasBracketlessColons;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private HostAndPort(String host, int port, boolean hasBracketlessColons)
/*     */   {
/*  78 */     this.host = host;
/*  79 */     this.port = port;
/*  80 */     this.hasBracketlessColons = hasBracketlessColons;
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
/*     */   public String getHost()
/*     */   {
/*  93 */     return this.host;
/*     */   }
/*     */   
/*     */   public boolean hasPort()
/*     */   {
/*  98 */     return this.port >= 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 109 */     Preconditions.checkState(hasPort());
/* 110 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPortOrDefault(int defaultPort)
/*     */   {
/* 117 */     return hasPort() ? this.port : defaultPort;
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
/*     */   public static HostAndPort fromParts(String host, int port)
/*     */   {
/* 133 */     Preconditions.checkArgument(isValidPort(port), "Port out of range: %s", port);
/* 134 */     HostAndPort parsedHost = fromString(host);
/* 135 */     Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
/* 136 */     return new HostAndPort(parsedHost.host, port, parsedHost.hasBracketlessColons);
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
/*     */   public static HostAndPort fromHost(String host)
/*     */   {
/* 151 */     HostAndPort parsedHost = fromString(host);
/* 152 */     Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
/* 153 */     return parsedHost;
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
/*     */   public static HostAndPort fromString(String hostPortString)
/*     */   {
/* 167 */     Preconditions.checkNotNull(hostPortString);
/*     */     
/* 169 */     String portString = null;
/* 170 */     boolean hasBracketlessColons = false;
/*     */     String host;
/* 172 */     if (hostPortString.startsWith("[")) {
/* 173 */       String[] hostAndPort = getHostAndPortFromBracketedHost(hostPortString);
/* 174 */       String host = hostAndPort[0];
/* 175 */       portString = hostAndPort[1];
/*     */     } else {
/* 177 */       int colonPos = hostPortString.indexOf(':');
/* 178 */       if ((colonPos >= 0) && (hostPortString.indexOf(':', colonPos + 1) == -1))
/*     */       {
/* 180 */         String host = hostPortString.substring(0, colonPos);
/* 181 */         portString = hostPortString.substring(colonPos + 1);
/*     */       }
/*     */       else {
/* 184 */         host = hostPortString;
/* 185 */         hasBracketlessColons = colonPos >= 0;
/*     */       }
/*     */     }
/*     */     
/* 189 */     int port = -1;
/* 190 */     if (!Strings.isNullOrEmpty(portString))
/*     */     {
/*     */ 
/* 193 */       Preconditions.checkArgument(!portString.startsWith("+"), "Unparseable port number: %s", hostPortString);
/*     */       try {
/* 195 */         port = Integer.parseInt(portString);
/*     */       } catch (NumberFormatException e) {
/* 197 */         throw new IllegalArgumentException("Unparseable port number: " + hostPortString);
/*     */       }
/* 199 */       Preconditions.checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString);
/*     */     }
/*     */     
/* 202 */     return new HostAndPort(host, port, hasBracketlessColons);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String[] getHostAndPortFromBracketedHost(String hostPortString)
/*     */   {
/* 213 */     int colonIndex = 0;
/* 214 */     int closeBracketIndex = 0;
/* 215 */     Preconditions.checkArgument(hostPortString
/* 216 */       .charAt(0) == '[', "Bracketed host-port string must start with a bracket: %s", hostPortString);
/*     */     
/*     */ 
/* 219 */     colonIndex = hostPortString.indexOf(':');
/* 220 */     closeBracketIndex = hostPortString.lastIndexOf(']');
/* 221 */     Preconditions.checkArgument((colonIndex > -1) && (closeBracketIndex > colonIndex), "Invalid bracketed host/port: %s", hostPortString);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 226 */     String host = hostPortString.substring(1, closeBracketIndex);
/* 227 */     if (closeBracketIndex + 1 == hostPortString.length()) {
/* 228 */       return new String[] { host, "" };
/*     */     }
/* 230 */     Preconditions.checkArgument(
/* 231 */       hostPortString.charAt(closeBracketIndex + 1) == ':', "Only a colon may follow a close bracket: %s", hostPortString);
/*     */     
/*     */ 
/* 234 */     for (int i = closeBracketIndex + 2; i < hostPortString.length(); i++) {
/* 235 */       Preconditions.checkArgument(
/* 236 */         Character.isDigit(hostPortString.charAt(i)), "Port must be numeric: %s", hostPortString);
/*     */     }
/*     */     
/*     */ 
/* 240 */     return new String[] { host, hostPortString.substring(closeBracketIndex + 2) };
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
/*     */   public HostAndPort withDefaultPort(int defaultPort)
/*     */   {
/* 254 */     Preconditions.checkArgument(isValidPort(defaultPort));
/* 255 */     if ((hasPort()) || (this.port == defaultPort)) {
/* 256 */       return this;
/*     */     }
/* 258 */     return new HostAndPort(this.host, defaultPort, this.hasBracketlessColons);
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
/*     */   public HostAndPort requireBracketsForIPv6()
/*     */   {
/* 276 */     Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host);
/* 277 */     return this;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object other)
/*     */   {
/* 282 */     if (this == other) {
/* 283 */       return true;
/*     */     }
/* 285 */     if ((other instanceof HostAndPort)) {
/* 286 */       HostAndPort that = (HostAndPort)other;
/* 287 */       return (Objects.equal(this.host, that.host)) && (this.port == that.port) && (this.hasBracketlessColons == that.hasBracketlessColons);
/*     */     }
/*     */     
/*     */ 
/* 291 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 296 */     return Objects.hashCode(new Object[] { this.host, Integer.valueOf(this.port), Boolean.valueOf(this.hasBracketlessColons) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 303 */     StringBuilder builder = new StringBuilder(this.host.length() + 8);
/* 304 */     if (this.host.indexOf(':') >= 0) {
/* 305 */       builder.append('[').append(this.host).append(']');
/*     */     } else {
/* 307 */       builder.append(this.host);
/*     */     }
/* 309 */     if (hasPort()) {
/* 310 */       builder.append(':').append(this.port);
/*     */     }
/* 312 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static boolean isValidPort(int port)
/*     */   {
/* 317 */     return (port >= 0) && (port <= 65535);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\net\HostAndPort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */