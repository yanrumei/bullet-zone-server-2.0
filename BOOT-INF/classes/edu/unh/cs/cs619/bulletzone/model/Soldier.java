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
/*     */ 
/*     */ public class Soldier
/*     */   extends FieldEntity
/*     */ {
/*     */   private static final String TAG = "Soldier";
/*     */   private final long id;
/*     */   private long lastMoveTime;
/*     */   private int allowedMoveInterval;
/*     */   private long lastFireTime;
/*     */   private int allowedFireInterval;
/*     */   private int numberOfBullets;
/*     */   private int allowedNumberOfBullets;
/*     */   private int life;
/*     */   private Direction direction;
/*     */   
/*     */   public Soldier(long id, Direction direction)
/*     */   {
/*  28 */     this.id = id;
/*  29 */     this.direction = direction;
/*  30 */     this.numberOfBullets = 0;
/*  31 */     this.allowedNumberOfBullets = 6;
/*  32 */     this.lastFireTime = 0L;
/*  33 */     this.allowedFireInterval = 1500;
/*  34 */     this.lastMoveTime = 0L;
/*  35 */     this.allowedMoveInterval = 500;
/*     */   }
/*     */   
/*     */   public FieldEntity copy()
/*     */   {
/*  40 */     return new Soldier(this.id, this.direction);
/*     */   }
/*     */   
/*     */   public void hit(int damage)
/*     */   {
/*  45 */     this.life -= damage;
/*  46 */     System.out.println("Soldier life: " + this.id + " : " + this.life);
/*     */     
/*     */ 
/*  49 */     if (this.life <= 0) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLastMoveTime()
/*     */   {
/*  57 */     return this.lastMoveTime;
/*     */   }
/*     */   
/*     */   public void setLastMoveTime(long lastMoveTime) {
/*  61 */     this.lastMoveTime = lastMoveTime;
/*     */   }
/*     */   
/*     */   public long getAllowedMoveInterval() {
/*  65 */     return this.allowedMoveInterval;
/*     */   }
/*     */   
/*     */   public void setAllowedMoveInterval(int allowedMoveInterval) {
/*  69 */     this.allowedMoveInterval = allowedMoveInterval;
/*     */   }
/*     */   
/*     */   public long getLastFireTime() {
/*  73 */     return this.lastFireTime;
/*     */   }
/*     */   
/*     */   public void setLastFireTime(long lastFireTime) {
/*  77 */     this.lastFireTime = lastFireTime;
/*     */   }
/*     */   
/*     */   public long getAllowedFireInterval() {
/*  81 */     return this.allowedFireInterval;
/*     */   }
/*     */   
/*     */   public void setAllowedFireInterval(int allowedFireInterval) {
/*  85 */     this.allowedFireInterval = allowedFireInterval;
/*     */   }
/*     */   
/*     */   public int getNumberOfBullets() {
/*  89 */     return this.numberOfBullets;
/*     */   }
/*     */   
/*     */   public void setNumberOfBullets(int numberOfBullets) {
/*  93 */     this.numberOfBullets = numberOfBullets;
/*     */   }
/*     */   
/*     */   public int getAllowedNumberOfBullets() {
/*  97 */     return this.allowedNumberOfBullets;
/*     */   }
/*     */   
/*     */   public void setAllowedNumberOfBullets(int allowedNumberOfBullets) {
/* 101 */     this.allowedNumberOfBullets = allowedNumberOfBullets;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/* 105 */     return this.direction;
/*     */   }
/*     */   
/*     */   public void setDirection(Direction direction) {
/* 109 */     this.direction = direction;
/*     */   }
/*     */   
/*     */   @JsonIgnore
/*     */   public long getId() {
/* 114 */     return this.id;
/*     */   }
/*     */   
/*     */   public int getIntValue()
/*     */   {
/* 119 */     return 
/* 120 */       (int)(10000000L + 10000L * this.id + 10 * this.life + Direction.toByte(this.direction));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 125 */     return "S";
/*     */   }
/*     */   
/*     */   public int getLife() {
/* 129 */     return this.life;
/*     */   }
/*     */   
/*     */   public void setLife(int life) {
/* 133 */     this.life = life;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\Soldier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */