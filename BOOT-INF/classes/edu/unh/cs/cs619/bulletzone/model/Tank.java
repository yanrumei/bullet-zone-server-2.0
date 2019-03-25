/*     */ package edu.unh.cs.cs619.bulletzone.model;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tank
/*     */   extends FieldEntity
/*     */ {
/*     */   private static final String TAG = "Tank";
/*     */   private final long id;
/*     */   private final String ip;
/*     */   private long lastMoveTime;
/*     */   private int allowedMoveInterval;
/*     */   private long lastFireTime;
/*     */   private int allowedFireInterval;
/*     */   private int numberOfBullets;
/*     */   private int allowedNumberOfBullets;
/*     */   private int life;
/*     */   private Direction direction;
/*     */   
/*     */   public Tank(long id, Direction direction, String ip)
/*     */   {
/*  28 */     this.id = id;
/*  29 */     this.direction = direction;
/*  30 */     this.ip = ip;
/*  31 */     this.numberOfBullets = 0;
/*  32 */     this.allowedNumberOfBullets = 2;
/*  33 */     this.lastFireTime = 0L;
/*  34 */     this.allowedFireInterval = 1500;
/*  35 */     this.lastMoveTime = 0L;
/*  36 */     this.allowedMoveInterval = 500;
/*     */   }
/*     */   
/*     */   public FieldEntity copy()
/*     */   {
/*  41 */     return new Tank(this.id, this.direction, this.ip);
/*     */   }
/*     */   
/*     */   public void hit(int damage)
/*     */   {
/*  46 */     this.life -= damage;
/*  47 */     System.out.println("Tank life: " + this.id + " : " + this.life);
/*     */     
/*     */ 
/*  50 */     if (this.life <= 0) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLastMoveTime()
/*     */   {
/*  58 */     return this.lastMoveTime;
/*     */   }
/*     */   
/*     */   public void setLastMoveTime(long lastMoveTime) {
/*  62 */     this.lastMoveTime = lastMoveTime;
/*     */   }
/*     */   
/*     */   public long getAllowedMoveInterval() {
/*  66 */     return this.allowedMoveInterval;
/*     */   }
/*     */   
/*     */   public void setAllowedMoveInterval(int allowedMoveInterval) {
/*  70 */     this.allowedMoveInterval = allowedMoveInterval;
/*     */   }
/*     */   
/*     */   public long getLastFireTime() {
/*  74 */     return this.lastFireTime;
/*     */   }
/*     */   
/*     */   public void setLastFireTime(long lastFireTime) {
/*  78 */     this.lastFireTime = lastFireTime;
/*     */   }
/*     */   
/*     */   public long getAllowedFireInterval() {
/*  82 */     return this.allowedFireInterval;
/*     */   }
/*     */   
/*     */   public void setAllowedFireInterval(int allowedFireInterval) {
/*  86 */     this.allowedFireInterval = allowedFireInterval;
/*     */   }
/*     */   
/*     */   public int getNumberOfBullets() {
/*  90 */     return this.numberOfBullets;
/*     */   }
/*     */   
/*     */   public void setNumberOfBullets(int numberOfBullets) {
/*  94 */     this.numberOfBullets = numberOfBullets;
/*     */   }
/*     */   
/*     */   public int getAllowedNumberOfBullets() {
/*  98 */     return this.allowedNumberOfBullets;
/*     */   }
/*     */   
/*     */   public void setAllowedNumberOfBullets(int allowedNumberOfBullets) {
/* 102 */     this.allowedNumberOfBullets = allowedNumberOfBullets;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/* 106 */     return this.direction;
/*     */   }
/*     */   
/*     */   public void setDirection(Direction direction) {
/* 110 */     this.direction = direction;
/*     */   }
/*     */   
/*     */   @JsonIgnore
/*     */   public long getId() {
/* 115 */     return this.id;
/*     */   }
/*     */   
/*     */   public int getIntValue()
/*     */   {
/* 120 */     return 
/* 121 */       (int)(10000000L + 10000L * this.id + 10 * this.life + Direction.toByte(this.direction));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 126 */     return "T";
/*     */   }
/*     */   
/*     */   public int getLife() {
/* 130 */     return this.life;
/*     */   }
/*     */   
/*     */   public void setLife(int life) {
/* 134 */     this.life = life;
/*     */   }
/*     */   
/*     */   public String getIp() {
/* 138 */     return this.ip;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\Tank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */