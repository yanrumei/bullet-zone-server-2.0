/*     */ package edu.unh.cs.cs619.bulletzone.repository;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Direction;
/*     */ import edu.unh.cs.cs619.bulletzone.model.FieldHolder;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Soldier;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Tank;
/*     */ import java.time.Clock;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class constraintChecker
/*     */ {
/*     */   Clock mClock;
/*     */   private ConcurrentHashMap<Long, Instant> tankTurnTimes;
/*     */   private ConcurrentHashMap<Long, Instant> tankMoveTimes;
/*     */   private ConcurrentHashMap<Long, Instant> tankFireTimes;
/*     */   private ConcurrentHashMap<Long, Instant> soldierTurnTimes;
/*     */   private ConcurrentHashMap<Long, Instant> soldierMoveTimes;
/*     */   private ConcurrentHashMap<Long, Instant> soldierFireTimes;
/*     */   private static final long ALLOWED_INTERVAL = 500L;
/*     */   
/*     */   public constraintChecker(Clock clock)
/*     */   {
/*  46 */     this.tankTurnTimes = new ConcurrentHashMap();
/*  47 */     this.tankMoveTimes = new ConcurrentHashMap();
/*  48 */     this.tankFireTimes = new ConcurrentHashMap();
/*  49 */     this.soldierTurnTimes = new ConcurrentHashMap();
/*  50 */     this.soldierMoveTimes = new ConcurrentHashMap();
/*  51 */     this.soldierFireTimes = new ConcurrentHashMap();
/*  52 */     this.mClock = clock;
/*     */   }
/*     */   
/*     */   public constraintChecker()
/*     */   {
/*  57 */     this.mClock = Clock.systemUTC();
/*  58 */     this.tankTurnTimes = new ConcurrentHashMap();
/*  59 */     this.tankMoveTimes = new ConcurrentHashMap();
/*  60 */     this.tankFireTimes = new ConcurrentHashMap();
/*  61 */     this.soldierTurnTimes = new ConcurrentHashMap();
/*  62 */     this.soldierMoveTimes = new ConcurrentHashMap();
/*  63 */     this.soldierFireTimes = new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean constraintTurn(Tank tank, Direction direction)
/*     */   {
/*  75 */     if ((tank == null) || (direction == null)) {
/*  76 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/*  80 */     Direction direction2 = tank.getDirection();
/*     */     
/*  82 */     if ((direction == Direction.Up) && (direction2 == Direction.Down)) {
/*  83 */       return Boolean.valueOf(false);
/*     */     }
/*  85 */     if ((direction == Direction.Down) && (direction2 == Direction.Up)) {
/*  86 */       return Boolean.valueOf(false);
/*     */     }
/*  88 */     if ((direction == Direction.Right) && (direction2 == Direction.Left)) {
/*  89 */       return Boolean.valueOf(false);
/*     */     }
/*  91 */     if ((direction == Direction.Left) && (direction2 == Direction.Right)) {
/*  92 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */     if (this.tankTurnTimes.get(Long.valueOf(tank.getId())) == null)
/*     */     {
/*     */ 
/* 102 */       this.tankTurnTimes.put(Long.valueOf(tank.getId()), this.mClock.instant());
/* 103 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 108 */     Instant start = (Instant)this.tankTurnTimes.get(Long.valueOf(tank.getId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 113 */     Duration timeElapsed = Duration.between(start, this.mClock.instant());
/*     */     
/*     */ 
/* 116 */     this.tankTurnTimes.put(Long.valueOf(tank.getId()), this.mClock.instant());
/*     */     
/* 118 */     if (timeElapsed.toMillis() < 500L) {
/* 119 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/* 123 */     return Boolean.valueOf(true);
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
/*     */   public Boolean constraintTurnSoldier(Soldier soldier, Direction direction)
/*     */   {
/* 137 */     if ((soldier == null) || (direction == null)) {
/* 138 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/* 142 */     Direction direction2 = soldier.getDirection();
/*     */     
/* 144 */     if ((direction == Direction.Up) && (direction2 == Direction.Down)) {
/* 145 */       return Boolean.valueOf(false);
/*     */     }
/* 147 */     if ((direction == Direction.Down) && (direction2 == Direction.Up)) {
/* 148 */       return Boolean.valueOf(false);
/*     */     }
/* 150 */     if ((direction == Direction.Right) && (direction2 == Direction.Left)) {
/* 151 */       return Boolean.valueOf(false);
/*     */     }
/* 153 */     if ((direction == Direction.Left) && (direction2 == Direction.Right)) {
/* 154 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */     if (this.soldierTurnTimes.get(Long.valueOf(soldier.getId())) == null)
/*     */     {
/*     */ 
/* 164 */       this.soldierTurnTimes.put(Long.valueOf(soldier.getId()), this.mClock.instant());
/* 165 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 170 */     Instant start = (Instant)this.soldierTurnTimes.get(Long.valueOf(soldier.getId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 175 */     Duration timeElapsed = Duration.between(start, this.mClock.instant());
/*     */     
/*     */ 
/* 178 */     this.soldierTurnTimes.put(Long.valueOf(soldier.getId()), this.mClock.instant());
/*     */     
/* 180 */     if (timeElapsed.toMillis() < 500L) {
/* 181 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/* 185 */     return Boolean.valueOf(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean constraintMove(Tank tank, Direction direction)
/*     */   {
/* 197 */     if ((tank == null) || (direction == null)) {
/* 198 */       return Boolean.valueOf(false);
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
/* 227 */     if (this.tankMoveTimes.get(Long.valueOf(tank.getId())) == null)
/*     */     {
/*     */ 
/* 230 */       this.tankMoveTimes.put(Long.valueOf(tank.getId()), this.mClock.instant());
/* 231 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 236 */     Instant start = (Instant)this.tankMoveTimes.get(Long.valueOf(tank.getId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 241 */     Duration timeElapsed = Duration.between(start, this.mClock.instant());
/*     */     
/*     */ 
/* 244 */     this.tankMoveTimes.put(Long.valueOf(tank.getId()), this.mClock.instant());
/*     */     
/*     */ 
/* 247 */     if (timeElapsed.toMillis() < 500L)
/*     */     {
/* 249 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/* 253 */     FieldHolder parent = tank.getParent();
/*     */     
/* 255 */     FieldHolder nextField = parent.getNeighbor(direction);
/* 256 */     Preconditions.checkNotNull(parent.getNeighbor(direction), "Neighbor is not available");
/*     */     
/* 258 */     if (!nextField.isPresent()) {
/* 259 */       return Boolean.valueOf(true);
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
/* 270 */     return Boolean.valueOf(false);
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
/*     */   public Boolean constraintMoveSoldier(Soldier soldier, Direction direction)
/*     */   {
/* 288 */     if ((soldier == null) || (direction == null)) {
/* 289 */       return Boolean.valueOf(false);
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
/* 319 */     if (this.soldierMoveTimes.get(Long.valueOf(soldier.getId())) == null)
/*     */     {
/*     */ 
/* 322 */       this.soldierMoveTimes.put(Long.valueOf(soldier.getId()), this.mClock.instant());
/* 323 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 328 */     Instant start = (Instant)this.soldierMoveTimes.get(Long.valueOf(soldier.getId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 333 */     Duration timeElapsed = Duration.between(start, this.mClock.instant());
/*     */     
/*     */ 
/* 336 */     this.soldierMoveTimes.put(Long.valueOf(soldier.getId()), this.mClock.instant());
/*     */     
/*     */ 
/* 339 */     if (timeElapsed.toMillis() < 500L)
/*     */     {
/* 341 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/* 345 */     FieldHolder parent = soldier.getParent();
/*     */     
/* 347 */     FieldHolder nextField = parent.getNeighbor(direction);
/* 348 */     Preconditions.checkNotNull(parent.getNeighbor(direction), "Neightbor is not available");
/*     */     
/* 350 */     if ((nextField.getEntity() instanceof Tank))
/*     */     {
/*     */ 
/* 353 */       Tank tank = (Tank)nextField.getEntity();
/* 354 */       if (soldier.getId() == tank.getId())
/*     */       {
/* 356 */         return Boolean.valueOf(true);
/*     */       }
/*     */     }
/* 359 */     if (!nextField.isPresent()) {
/* 360 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 369 */     return Boolean.valueOf(false);
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
/*     */   public Boolean constraintFire(Tank tank, int bulletType)
/*     */   {
/* 382 */     if (tank == null) {
/* 383 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 388 */     if (this.tankFireTimes.get(Long.valueOf(tank.getId())) == null)
/*     */     {
/*     */ 
/* 391 */       this.tankFireTimes.put(Long.valueOf(tank.getId()), this.mClock.instant());
/* 392 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/*     */ 
/* 396 */     Instant start = (Instant)this.tankFireTimes.get(Long.valueOf(tank.getId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 401 */     Duration timeElapsed = Duration.between(start, this.mClock.instant());
/*     */     
/*     */ 
/* 404 */     this.tankFireTimes.put(Long.valueOf(tank.getId()), this.mClock.instant());
/*     */     
/*     */ 
/* 407 */     if (timeElapsed.toMillis() < 500L)
/*     */     {
/* 409 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 412 */     if (tank.getNumberOfBullets() >= tank.getAllowedNumberOfBullets()) {
/* 413 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/* 417 */     return Boolean.valueOf(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean constraintFireSoldier(Soldier soldier, int bulletType)
/*     */   {
/* 428 */     if (soldier == null) {
/* 429 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 434 */     if (this.soldierFireTimes.get(Long.valueOf(soldier.getId())) == null)
/*     */     {
/*     */ 
/* 437 */       this.soldierFireTimes.put(Long.valueOf(soldier.getId()), this.mClock.instant());
/* 438 */       return Boolean.valueOf(true);
/*     */     }
/*     */     
/*     */ 
/* 442 */     Instant start = (Instant)this.soldierFireTimes.get(Long.valueOf(soldier.getId()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 447 */     Duration timeElapsed = Duration.between(start, this.mClock.instant());
/*     */     
/*     */ 
/* 450 */     this.soldierFireTimes.put(Long.valueOf(soldier.getId()), this.mClock.instant());
/*     */     
/*     */ 
/* 453 */     if (timeElapsed.toMillis() < 500L)
/*     */     {
/* 455 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 458 */     if (soldier.getNumberOfBullets() >= soldier.getAllowedNumberOfBullets()) {
/* 459 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/*     */ 
/* 463 */     return Boolean.valueOf(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setClock(Clock newClock)
/*     */   {
/* 473 */     if (newClock != null)
/*     */     {
/* 475 */       this.mClock = newClock;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\repository\constraintChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */