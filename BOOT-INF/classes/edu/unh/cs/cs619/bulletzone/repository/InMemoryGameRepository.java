/*     */ package edu.unh.cs.cs619.bulletzone.repository;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Bullet;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Direction;
/*     */ import edu.unh.cs.cs619.bulletzone.model.FieldEntity;
/*     */ import edu.unh.cs.cs619.bulletzone.model.FieldHolder;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Game;
/*     */ import edu.unh.cs.cs619.bulletzone.model.IllegalTransitionException;
/*     */ import edu.unh.cs.cs619.bulletzone.model.LimitExceededException;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Soldier;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Tank;
/*     */ import edu.unh.cs.cs619.bulletzone.model.TankDoesNotExistException;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Wall;
/*     */ import java.io.PrintStream;
/*     */ import java.time.Clock;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
/*     */ import java.util.Timer;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class InMemoryGameRepository
/*     */   implements GameRepository
/*     */ {
/*     */   private static final int FIELD_DIM = 16;
/*  33 */   private final Timer timer = new Timer();
/*  34 */   private final AtomicLong idGenerator = new AtomicLong();
/*  35 */   private final Object monitor = new Object();
/*  36 */   private Game game = null;
/*     */   
/*     */ 
/*     */   private static final int BULLET_PERIOD = 200;
/*     */   
/*  41 */   private int[] bulletDamage = { 10, 30, 50 };
/*  42 */   private int[] bulletDelay = { 500, 1000, 1500 };
/*  43 */   private Bullet[] trackActiveBullets = { null, null };
/*     */   
/*     */   private static final int BULLET_DAMAGE = 1;
/*     */   
/*     */   private static final int TANK_LIFE = 100;
/*  48 */   private int[] soldierBulletDamage = { 5, 0, 0 };
/*  49 */   private Bullet[] soldierTrackActiveBullets = { null, null, null, null, null, null };
/*     */   
/*     */   private static final int SOLDIER_BULLET_DMG = 1;
/*     */   
/*     */   private static final int SOLDIER_LIFE = 25;
/*     */   
/*  55 */   private constraintChecker mConstraintChecker = new constraintChecker();
/*     */   
/*     */   public Tank join(String ip)
/*     */   {
/*  59 */     synchronized (this.monitor)
/*     */     {
/*  61 */       if (this.game == null) {
/*  62 */         create();
/*     */       }
/*     */       
/*  65 */       if ((tank = this.game.getTank(ip)) != null) {
/*  66 */         return tank;
/*     */       }
/*     */       
/*  69 */       Long tankId = Long.valueOf(this.idGenerator.getAndIncrement());
/*     */       
/*  71 */       Tank tank = new Tank(tankId.longValue(), Direction.Up, ip);
/*  72 */       tank.setLife(100);
/*     */       
/*  74 */       Random random = new Random();
/*     */       
/*     */ 
/*     */ 
/*     */       for (;;)
/*     */       {
/*  80 */         int x = random.nextInt(16);
/*  81 */         int y = random.nextInt(16);
/*  82 */         FieldHolder fieldElement = (FieldHolder)this.game.getHolderGrid().get(x * 16 + y);
/*  83 */         if (!fieldElement.isPresent()) {
/*  84 */           fieldElement.setFieldEntity(tank);
/*  85 */           tank.setParent(fieldElement);
/*  86 */           break;
/*     */         }
/*     */       }
/*     */       
/*  90 */       this.game.addTank(ip, tank);
/*     */       
/*  92 */       return tank;
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
/*     */   public boolean eject(long tankId)
/*     */     throws TankDoesNotExistException, IllegalTransitionException, LimitExceededException
/*     */   {
/* 108 */     synchronized (this.monitor)
/*     */     {
/*     */ 
/* 111 */       boolean isEjected = false;
/* 112 */       Tank tank = this.game.getTank(tankId);
/* 113 */       if (tank == null)
/*     */       {
/* 115 */         throw new TankDoesNotExistException(Long.valueOf(tankId));
/*     */       }
/*     */       
/*     */ 
/* 119 */       Soldier soldier = this.game.getSoldier(tankId);
/*     */       
/* 121 */       if (soldier != null) {
/* 122 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 128 */       for (int dir = 0; dir <= 6; dir += 2)
/*     */       {
/* 130 */         FieldHolder parent = tank.getParent();
/*     */         
/*     */ 
/*     */ 
/* 134 */         byte dirByte = (byte)dir;
/* 135 */         Direction direction = Direction.fromByte(dirByte);
/* 136 */         FieldHolder nextField = parent.getNeighbor(direction);
/*     */         
/*     */ 
/* 139 */         if (!nextField.isPresent())
/*     */         {
/* 141 */           Soldier workingSoldier = new Soldier(tankId, Direction.Right);
/* 142 */           this.game.addSoldier(soldier);
/* 143 */           soldier.setLife(25);
/* 144 */           nextField.setFieldEntity(soldier);
/* 145 */           soldier.setParent(nextField);
/* 146 */           return true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 153 */       return isEjected;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[][] getGrid()
/*     */   {
/* 165 */     synchronized (this.monitor) {
/* 166 */       if (this.game == null) {
/* 167 */         create();
/*     */       }
/*     */     }
/* 170 */     return this.game.getGrid2D();
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
/*     */   public boolean turn(long tankId, Direction direction)
/*     */     throws TankDoesNotExistException, IllegalTransitionException, LimitExceededException
/*     */   {
/* 186 */     synchronized (this.monitor) {
/* 187 */       Preconditions.checkNotNull(direction);
/*     */       
/*     */ 
/* 190 */       Tank tank = (Tank)this.game.getTanks().get(Long.valueOf(tankId));
/* 191 */       if (tank == null)
/*     */       {
/* 193 */         throw new TankDoesNotExistException(Long.valueOf(tankId));
/*     */       }
/*     */       
/* 196 */       Soldier soldier = this.game.getSoldier(tankId);
/* 197 */       if (soldier != null)
/*     */       {
/*     */ 
/* 200 */         if (!this.mConstraintChecker.constraintTurnSoldier(soldier, direction).booleanValue()) {
/* 201 */           return false;
/*     */         }
/* 203 */         soldier.setDirection(direction);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 208 */         if (!this.mConstraintChecker.constraintTurn(tank, direction).booleanValue()) {
/* 209 */           return false;
/*     */         }
/* 211 */         tank.setDirection(direction);
/*     */       }
/*     */       
/* 214 */       return true;
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
/*     */   public boolean move(long tankId, Direction direction)
/*     */     throws TankDoesNotExistException, IllegalTransitionException, LimitExceededException
/*     */   {
/* 231 */     synchronized (this.monitor)
/*     */     {
/*     */ 
/* 234 */       Tank tank = (Tank)this.game.getTanks().get(Long.valueOf(tankId));
/* 235 */       if (tank == null)
/*     */       {
/*     */ 
/* 238 */         throw new TankDoesNotExistException(Long.valueOf(tankId));
/*     */       }
/*     */       
/*     */ 
/* 242 */       Soldier soldier = this.game.getSoldier(tankId);
/* 243 */       if (soldier != null)
/*     */       {
/*     */ 
/* 246 */         if (this.mConstraintChecker.constraintMoveSoldier(soldier, direction).booleanValue())
/*     */         {
/* 248 */           FieldHolder parent = soldier.getParent();
/* 249 */           FieldHolder nextField = parent.getNeighbor(direction);
/* 250 */           parent.clearField();
/*     */           
/* 252 */           if ((nextField.getEntity() instanceof Tank))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 257 */             soldier.getParent().clearField();
/* 258 */             soldier.setParent(null);
/* 259 */             this.game.removeSoldier(soldier.getId());
/*     */           }
/* 261 */           nextField.setFieldEntity(soldier);
/* 262 */           soldier.setParent(nextField);
/* 263 */           return true;
/*     */         }
/*     */         
/* 266 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 272 */       if (this.mConstraintChecker.constraintMove(tank, direction).booleanValue())
/*     */       {
/* 274 */         FieldHolder parent = tank.getParent();
/* 275 */         FieldHolder nextField = parent.getNeighbor(direction);
/* 276 */         parent.clearField();
/* 277 */         nextField.setFieldEntity(tank);
/* 278 */         tank.setParent(nextField);
/* 279 */         return true;
/*     */       }
/*     */       
/* 282 */       return false;
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
/*     */ 
/*     */   public boolean fire(long tankId, int bulletType)
/*     */     throws TankDoesNotExistException, LimitExceededException
/*     */   {
/* 302 */     synchronized (this.monitor)
/*     */     {
/*     */ 
/* 305 */       Tank tank = (Tank)this.game.getTanks().get(Long.valueOf(tankId));
/* 306 */       if (tank == null)
/*     */       {
/* 308 */         throw new TankDoesNotExistException(Long.valueOf(tankId));
/*     */       }
/*     */       
/* 311 */       Soldier soldier = this.game.getSoldier(tankId);
/* 312 */       if (soldier != null)
/*     */       {
/*     */ 
/* 315 */         Direction direction = soldier.getDirection();
/* 316 */         FieldHolder parent = soldier.getParent();
/*     */         
/* 318 */         if ((bulletType < 1) || (bulletType > 3)) {
/* 319 */           System.out.println("Only one bullet type for soldier atm, set to 0.");
/* 320 */           bulletType = 1;
/*     */         }
/*     */         
/* 323 */         if (!this.mConstraintChecker.constraintFireSoldier(soldier, bulletType).booleanValue()) {
/* 324 */           return false;
/*     */         }
/*     */         
/* 327 */         soldier.setNumberOfBullets(soldier.getNumberOfBullets() + 1);
/* 328 */         Bullet bullet = new Bullet(tankId, direction, this.soldierBulletDamage[(bulletType - 1)]);
/*     */         
/*     */ 
/* 331 */         bullet.setParent(parent);
/* 332 */         bullet.setBulletId(returnBulletID(bullet, tankId));
/*     */         
/*     */ 
/* 335 */         soldierFired(soldier, bullet);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 340 */         Direction direction = tank.getDirection();
/* 341 */         FieldHolder parent = tank.getParent();
/*     */         
/* 343 */         if ((bulletType < 1) || (bulletType > 3)) {
/* 344 */           System.out.println("Bullet type must be 1, 2 or 3, set to 1 by default.");
/* 345 */           bulletType = 2;
/*     */         }
/*     */         
/*     */ 
/* 349 */         if (!this.mConstraintChecker.constraintFire(tank, bulletType).booleanValue()) {
/* 350 */           return false;
/*     */         }
/*     */         
/* 353 */         tank.setNumberOfBullets(tank.getNumberOfBullets() + 1);
/* 354 */         Bullet bullet = new Bullet(tankId, direction, this.bulletDamage[(bulletType - 1)]);
/*     */         
/*     */ 
/* 357 */         bullet.setParent(parent);
/* 358 */         bullet.setBulletId(returnBulletID(bullet, tankId));
/*     */         
/*     */ 
/* 361 */         tankFired(tank, bullet);
/*     */         
/*     */ 
/* 364 */         removeSoldierBullets();
/*     */       }
/*     */       
/* 367 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeSoldierBullets()
/*     */   {
/* 377 */     for (int i = 0; i < this.soldierTrackActiveBullets.length; i++)
/*     */     {
/* 379 */       if (this.soldierTrackActiveBullets[i] != null)
/*     */       {
/* 381 */         Bullet b = this.soldierTrackActiveBullets[i];
/* 382 */         FieldHolder p = b.getParent();
/*     */         
/*     */ 
/* 385 */         boolean isVisible = (p.isPresent()) && (p.getEntity() == b);
/* 386 */         if (isVisible)
/*     */         {
/* 388 */           p.clearField();
/*     */         }
/*     */         
/* 391 */         this.soldierTrackActiveBullets[i] = null;
/*     */       }
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
/*     */   private void tankFired(Tank tank, Bullet bullet)
/*     */   {
/* 405 */     this.timer.schedule(new InMemoryGameRepository.1(this, tank, bullet), 0L, 200L);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void soldierFired(Soldier soldier, Bullet bullet)
/*     */   {
/* 450 */     this.timer.schedule(new InMemoryGameRepository.2(this, soldier, bullet), 0L, 200L);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void bulletHitLogic(Bullet bullet, FieldHolder nextField, FieldHolder currentField, boolean isVisible)
/*     */   {
/* 499 */     nextField.getEntity().hit(bullet.getDamage());
/*     */     
/* 501 */     if ((nextField.getEntity() instanceof Tank)) {
/* 502 */       Tank t = (Tank)nextField.getEntity();
/* 503 */       System.out.println("tank is hit, tank life: " + t.getLife());
/* 504 */       if (t.getLife() <= 0) {
/* 505 */         t.getParent().clearField();
/* 506 */         t.setParent(null);
/* 507 */         this.game.removeTank(t.getId());
/*     */         
/* 509 */         Soldier soldier = this.game.getSoldier(t.getId() / 10L);
/* 510 */         if (soldier != null)
/*     */         {
/* 512 */           soldier.getParent().clearField();
/* 513 */           soldier.setParent(null);
/* 514 */           this.game.removeSoldier(soldier.getId());
/*     */         }
/*     */       }
/*     */     }
/* 518 */     else if ((nextField.getEntity() instanceof Soldier)) {
/* 519 */       Soldier s = (Soldier)nextField.getEntity();
/* 520 */       System.out.println("soldier is hit, soldier life: " + s.getLife());
/*     */       
/*     */ 
/* 523 */       if (s.getLife() <= 0) {
/* 524 */         s.getParent().clearField();
/* 525 */         s.setParent(null);
/* 526 */         this.game.removeSoldier(s.getId());
/*     */         
/*     */ 
/* 529 */         Tank t = this.game.getTank(s.getId() * 10L);
/* 530 */         t.getParent().clearField();
/* 531 */         t.setParent(null);
/* 532 */         this.game.removeTank(t.getId());
/*     */       }
/*     */     }
/* 535 */     else if ((nextField.getEntity() instanceof Wall)) {
/* 536 */       Wall w = (Wall)nextField.getEntity();
/* 537 */       if ((w.getIntValue() > 1000) && (w.getIntValue() <= 2000)) {
/* 538 */         ((FieldHolder)this.game.getHolderGrid().get(w.getPos())).clearField();
/*     */       }
/*     */     }
/*     */     
/* 542 */     if (isVisible)
/*     */     {
/* 544 */       currentField.clearField();
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
/*     */   private int returnBulletID(Bullet bullet, long tankId)
/*     */   {
/* 559 */     Soldier soldier = this.game.getSoldier(tankId);
/* 560 */     int bulletId = 0;
/* 561 */     if (soldier != null)
/*     */     {
/* 563 */       for (int i = 0; i < this.soldierTrackActiveBullets.length; i++)
/*     */       {
/*     */ 
/* 566 */         if (this.soldierTrackActiveBullets[i] == null)
/*     */         {
/* 568 */           this.soldierTrackActiveBullets[i] = bullet;
/* 569 */           bulletId = i;
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     } else {
/* 575 */       for (int k = 0; k < this.trackActiveBullets.length; k++)
/*     */       {
/* 577 */         if (this.trackActiveBullets[k] == null)
/*     */         {
/* 579 */           this.trackActiveBullets[k] = bullet;
/* 580 */           bulletId = k;
/*     */         }
/*     */       }
/*     */     }
/* 584 */     return bulletId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void leave(long tankId)
/*     */     throws TankDoesNotExistException
/*     */   {
/* 597 */     synchronized (this.monitor)
/*     */     {
/* 599 */       if (this.game.getTank(tankId) == null) {
/* 600 */         throw new TankDoesNotExistException(Long.valueOf(tankId));
/*     */       }
/*     */       
/* 603 */       System.out.println("leave() called, tank ID: " + tankId);
/*     */       
/* 605 */       Tank tank = this.game.getTank(tankId);
/*     */       
/* 607 */       Soldier soldier = this.game.getSoldier(tankId);
/* 608 */       if (soldier != null)
/*     */       {
/*     */ 
/* 611 */         soldier.getParent().clearField();
/* 612 */         soldier.setParent(null);
/* 613 */         this.game.removeSoldier(soldier.getId());
/*     */       }
/*     */       
/* 616 */       tank.getParent().clearField();
/* 617 */       tank.setParent(null);
/* 618 */       this.game.removeTank(tankId);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void create()
/*     */   {
/* 627 */     if (this.game != null) {
/* 628 */       return;
/*     */     }
/* 630 */     synchronized (this.monitor)
/*     */     {
/* 632 */       this.game = new Game();
/*     */       
/* 634 */       createFieldHolderGrid(this.game);
/*     */       
/*     */ 
/* 637 */       ((FieldHolder)this.game.getHolderGrid().get(1)).setFieldEntity(new Wall());
/* 638 */       ((FieldHolder)this.game.getHolderGrid().get(2)).setFieldEntity(new Wall());
/* 639 */       ((FieldHolder)this.game.getHolderGrid().get(3)).setFieldEntity(new Wall());
/*     */       
/* 641 */       ((FieldHolder)this.game.getHolderGrid().get(17)).setFieldEntity(new Wall());
/* 642 */       ((FieldHolder)this.game.getHolderGrid().get(18)).setFieldEntity(new Wall(1500, 18));
/* 643 */       ((FieldHolder)this.game.getHolderGrid().get(19)).setFieldEntity(new Wall(1500, 19));
/* 644 */       ((FieldHolder)this.game.getHolderGrid().get(20)).setFieldEntity(new Wall(1500, 20));
/*     */       
/* 646 */       ((FieldHolder)this.game.getHolderGrid().get(65)).setFieldEntity(new Wall());
/* 647 */       ((FieldHolder)this.game.getHolderGrid().get(66)).setFieldEntity(new Wall(1500, 66));
/*     */       
/* 649 */       ((FieldHolder)this.game.getHolderGrid().get(7)).setFieldEntity(new Wall());
/* 650 */       ((FieldHolder)this.game.getHolderGrid().get(23)).setFieldEntity(new Wall());
/* 651 */       ((FieldHolder)this.game.getHolderGrid().get(39)).setFieldEntity(new Wall(1500, 39));
/*     */       
/* 653 */       ((FieldHolder)this.game.getHolderGrid().get(139)).setFieldEntity(new Wall());
/* 654 */       ((FieldHolder)this.game.getHolderGrid().get(140)).setFieldEntity(new Wall());
/* 655 */       ((FieldHolder)this.game.getHolderGrid().get(141)).setFieldEntity(new Wall());
/* 656 */       ((FieldHolder)this.game.getHolderGrid().get(142)).setFieldEntity(new Wall());
/* 657 */       ((FieldHolder)this.game.getHolderGrid().get(143)).setFieldEntity(new Wall(1500, 143));
/*     */       
/* 659 */       ((FieldHolder)this.game.getHolderGrid().get(246)).setFieldEntity(new Wall(1500, 256));
/* 660 */       ((FieldHolder)this.game.getHolderGrid().get(230)).setFieldEntity(new Wall(1500, 230));
/* 661 */       ((FieldHolder)this.game.getHolderGrid().get(214)).setFieldEntity(new Wall(1500, 214));
/* 662 */       ((FieldHolder)this.game.getHolderGrid().get(198)).setFieldEntity(new Wall(1500, 198));
/*     */       
/* 664 */       ((FieldHolder)this.game.getHolderGrid().get(161)).setFieldEntity(new Wall());
/* 665 */       ((FieldHolder)this.game.getHolderGrid().get(162)).setFieldEntity(new Wall());
/* 666 */       ((FieldHolder)this.game.getHolderGrid().get(163)).setFieldEntity(new Wall(1500, 163));
/*     */       
/* 668 */       ((FieldHolder)this.game.getHolderGrid().get(117)).setFieldEntity(new Wall());
/* 669 */       ((FieldHolder)this.game.getHolderGrid().get(118)).setFieldEntity(new Wall());
/* 670 */       ((FieldHolder)this.game.getHolderGrid().get(119)).setFieldEntity(new Wall());
/* 671 */       ((FieldHolder)this.game.getHolderGrid().get(120)).setFieldEntity(new Wall());
/* 672 */       ((FieldHolder)this.game.getHolderGrid().get(121)).setFieldEntity(new Wall());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void createFieldHolderGrid(Game game)
/*     */   {
/* 682 */     synchronized (this.monitor) {
/* 683 */       game.getHolderGrid().clear();
/* 684 */       for (int i = 0; i < 256; i++) {
/* 685 */         game.getHolderGrid().add(new FieldHolder());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 693 */       for (int i = 0; i < 16; i++) {
/* 694 */         for (int j = 0; j < 16; j++) {
/* 695 */           FieldHolder targetHolder = (FieldHolder)game.getHolderGrid().get(i * 16 + j);
/* 696 */           FieldHolder rightHolder = (FieldHolder)game.getHolderGrid().get(i * 16 + (j + 1) % 16);
/*     */           
/* 698 */           FieldHolder downHolder = (FieldHolder)game.getHolderGrid().get((i + 1) % 16 * 16 + j);
/*     */           
/*     */ 
/* 701 */           targetHolder.addNeighbor(Direction.Right, rightHolder);
/* 702 */           rightHolder.addNeighbor(Direction.Left, targetHolder);
/*     */           
/* 704 */           targetHolder.addNeighbor(Direction.Down, downHolder);
/* 705 */           downHolder.addNeighbor(Direction.Up, targetHolder);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void advanceClock(long time)
/*     */   {
/* 719 */     Duration duration = Duration.ofMillis(time);
/*     */     
/* 721 */     Clock mClock = Clock.offset(this.mConstraintChecker.mClock, duration);
/* 722 */     this.mConstraintChecker.setClock(mClock);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\repository\InMemoryGameRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */