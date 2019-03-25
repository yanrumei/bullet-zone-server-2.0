/*     */ package edu.unh.cs.cs619.bulletzone.model;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.google.common.base.Optional;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Game
/*     */ {
/*     */   private static final int FIELD_DIM = 16;
/*     */   private final long id;
/*  17 */   private final ArrayList<FieldHolder> holderGrid = new ArrayList();
/*     */   
/*  19 */   private final ConcurrentMap<Long, Tank> tanks = new ConcurrentHashMap();
/*  20 */   private final ConcurrentHashMap<Long, Soldier> soldiers = new ConcurrentHashMap();
/*  21 */   private final ConcurrentMap<String, Long> playersIP = new ConcurrentHashMap();
/*     */   
/*  23 */   private final Object monitor = new Object();
/*     */   
/*     */   public Game() {
/*  26 */     this.id = 0L;
/*     */   }
/*     */   
/*     */   @JsonIgnore
/*     */   public long getId() {
/*  31 */     return this.id;
/*     */   }
/*     */   
/*     */   @JsonIgnore
/*     */   public ArrayList<FieldHolder> getHolderGrid() {
/*  36 */     return this.holderGrid;
/*     */   }
/*     */   
/*     */   public void addTank(String ip, Tank tank) {
/*  40 */     synchronized (this.tanks) {
/*  41 */       this.tanks.put(Long.valueOf(tank.getId()), tank);
/*  42 */       this.playersIP.put(ip, Long.valueOf(tank.getId()));
/*     */     }
/*     */   }
/*     */   
/*     */   public Tank getTank(long tankId) {
/*  47 */     return (Tank)this.tanks.get(Long.valueOf(tankId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSoldier(Soldier soldier)
/*     */   {
/*  56 */     synchronized (this.soldiers) {
/*  57 */       this.soldiers.put(Long.valueOf(soldier.getId()), soldier);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Soldier getSoldier(long tankId)
/*     */   {
/*  66 */     return (Soldier)this.soldiers.get(Long.valueOf(tankId));
/*     */   }
/*     */   
/*  69 */   public ConcurrentMap<Long, Tank> getTanks() { return this.tanks; }
/*     */   
/*     */   public List<Optional<FieldEntity>> getGrid()
/*     */   {
/*  73 */     synchronized (this.holderGrid) {
/*  74 */       List<Optional<FieldEntity>> entities = new ArrayList();
/*     */       
/*     */ 
/*  77 */       for (FieldHolder holder : this.holderGrid) {
/*  78 */         if (holder.isPresent()) {
/*  79 */           FieldEntity entity = holder.getEntity();
/*  80 */           entity = entity.copy();
/*     */           
/*  82 */           entities.add(Optional.of(entity));
/*     */         } else {
/*  84 */           entities.add(Optional.absent());
/*     */         }
/*     */       }
/*  87 */       return entities;
/*     */     }
/*     */   }
/*     */   
/*     */   public Tank getTank(String ip) {
/*  92 */     if (this.playersIP.containsKey(ip)) {
/*  93 */       return (Tank)this.tanks.get(this.playersIP.get(ip));
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   public void removeTank(long tankId) {
/*  99 */     synchronized (this.tanks) {
/* 100 */       Tank t = (Tank)this.tanks.remove(Long.valueOf(tankId));
/* 101 */       if (t != null) {
/* 102 */         this.playersIP.remove(t.getIp());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeSoldier(long tankId) {
/* 108 */     synchronized (this.soldiers) {
/* 109 */       Soldier localSoldier = (Soldier)this.soldiers.remove(Long.valueOf(tankId));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int[][] getGrid2D()
/*     */   {
/* 117 */     int[][] grid = new int[16][16];
/*     */     
/* 119 */     synchronized (this.holderGrid)
/*     */     {
/* 121 */       for (int i = 0; i < 16; i++) {
/* 122 */         for (int j = 0; j < 16; j++) {
/* 123 */           FieldHolder holder = (FieldHolder)this.holderGrid.get(i * 16 + j);
/* 124 */           if (holder.isPresent()) {
/* 125 */             grid[i][j] = holder.getEntity().getIntValue();
/*     */           } else {
/* 127 */             grid[i][j] = 0;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 133 */     return grid;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\Game.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */