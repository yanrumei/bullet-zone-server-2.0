/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.util.Random;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.net.ServerSocketFactory;
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
/*     */ public class SocketUtils
/*     */ {
/*     */   public static final int PORT_RANGE_MIN = 1024;
/*     */   public static final int PORT_RANGE_MAX = 65535;
/*  56 */   private static final Random random = new Random(System.currentTimeMillis());
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
/*     */   public static int findAvailableTcpPort()
/*     */   {
/*  87 */     return findAvailableTcpPort(1024);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int findAvailableTcpPort(int minPort)
/*     */   {
/*  98 */     return findAvailableTcpPort(minPort, 65535);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int findAvailableTcpPort(int minPort, int maxPort)
/*     */   {
/* 110 */     return SocketType.TCP.findAvailablePort(minPort, maxPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SortedSet<Integer> findAvailableTcpPorts(int numRequested)
/*     */   {
/* 121 */     return findAvailableTcpPorts(numRequested, 1024, 65535);
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
/*     */   public static SortedSet<Integer> findAvailableTcpPorts(int numRequested, int minPort, int maxPort)
/*     */   {
/* 134 */     return SocketType.TCP.findAvailablePorts(numRequested, minPort, maxPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int findAvailableUdpPort()
/*     */   {
/* 144 */     return findAvailableUdpPort(1024);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int findAvailableUdpPort(int minPort)
/*     */   {
/* 155 */     return findAvailableUdpPort(minPort, 65535);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int findAvailableUdpPort(int minPort, int maxPort)
/*     */   {
/* 167 */     return SocketType.UDP.findAvailablePort(minPort, maxPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SortedSet<Integer> findAvailableUdpPorts(int numRequested)
/*     */   {
/* 178 */     return findAvailableUdpPorts(numRequested, 1024, 65535);
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
/*     */   public static SortedSet<Integer> findAvailableUdpPorts(int numRequested, int minPort, int maxPort)
/*     */   {
/* 191 */     return SocketType.UDP.findAvailablePorts(numRequested, minPort, maxPort);
/*     */   }
/*     */   
/*     */ 
/*     */   private static abstract enum SocketType
/*     */   {
/* 197 */     TCP, 
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
/* 212 */     UDP;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private SocketType() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected abstract boolean isPortAvailable(int paramInt);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int findRandomPort(int minPort, int maxPort)
/*     */     {
/* 240 */       int portRange = maxPort - minPort;
/* 241 */       return minPort + SocketUtils.random.nextInt(portRange + 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     int findAvailablePort(int minPort, int maxPort)
/*     */     {
/* 253 */       Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
/* 254 */       Assert.isTrue(maxPort >= minPort, "'maxPort' must be greater than or equals 'minPort'");
/* 255 */       Assert.isTrue(maxPort <= 65535, "'maxPort' must be less than or equal to 65535");
/*     */       
/* 257 */       int portRange = maxPort - minPort;
/*     */       
/* 259 */       int searchCounter = 0;
/*     */       int candidatePort;
/* 261 */       do { searchCounter++; if (searchCounter > portRange) {
/* 262 */           throw new IllegalStateException(String.format("Could not find an available %s port in the range [%d, %d] after %d attempts", new Object[] {
/*     */           
/* 264 */             name(), Integer.valueOf(minPort), Integer.valueOf(maxPort), Integer.valueOf(searchCounter) }));
/*     */         }
/* 266 */         candidatePort = findRandomPort(minPort, maxPort);
/*     */       }
/* 268 */       while (!isPortAvailable(candidatePort));
/*     */       
/* 270 */       return candidatePort;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     SortedSet<Integer> findAvailablePorts(int numRequested, int minPort, int maxPort)
/*     */     {
/* 283 */       Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
/* 284 */       Assert.isTrue(maxPort > minPort, "'maxPort' must be greater than 'minPort'");
/* 285 */       Assert.isTrue(maxPort <= 65535, "'maxPort' must be less than or equal to 65535");
/* 286 */       Assert.isTrue(numRequested > 0, "'numRequested' must be greater than 0");
/* 287 */       Assert.isTrue(maxPort - minPort >= numRequested, "'numRequested' must not be greater than 'maxPort' - 'minPort'");
/*     */       
/*     */ 
/* 290 */       SortedSet<Integer> availablePorts = new TreeSet();
/* 291 */       int attemptCount = 0;
/* 292 */       for (;;) { attemptCount++; if ((attemptCount > numRequested + 100) || (availablePorts.size() >= numRequested)) break;
/* 293 */         availablePorts.add(Integer.valueOf(findAvailablePort(minPort, maxPort)));
/*     */       }
/*     */       
/* 296 */       if (availablePorts.size() != numRequested) {
/* 297 */         throw new IllegalStateException(String.format("Could not find %d available %s ports in the range [%d, %d]", new Object[] {
/*     */         
/* 299 */           Integer.valueOf(numRequested), name(), Integer.valueOf(minPort), Integer.valueOf(maxPort) }));
/*     */       }
/*     */       
/* 302 */       return availablePorts;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\SocketUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */