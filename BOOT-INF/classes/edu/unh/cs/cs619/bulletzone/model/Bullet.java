/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ public class Bullet extends FieldEntity {
/*    */   private long tankId;
/*    */   private Direction direction;
/*    */   private int damage;
/*    */   private int bulletId;
/*    */   
/*    */   public Bullet(long tankId, Direction direction, int damage) {
/* 10 */     this.damage = damage;
/* 11 */     setTankId(tankId);
/* 12 */     setDirection(direction);
/*    */   }
/*    */   
/*    */   public int getIntValue()
/*    */   {
/* 17 */     return (int)(2000000L + 1000L * this.tankId + this.damage * 10 + this.bulletId);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 22 */     return "B";
/*    */   }
/*    */   
/*    */   public FieldEntity copy()
/*    */   {
/* 27 */     return new Bullet(this.tankId, this.direction, this.damage);
/*    */   }
/*    */   
/*    */   public long getTankId() {
/* 31 */     return this.tankId;
/*    */   }
/*    */   
/*    */   public void setTankId(long tankId) {
/* 35 */     this.tankId = tankId;
/*    */   }
/*    */   
/*    */   public Direction getDirection() {
/* 39 */     return this.direction;
/*    */   }
/*    */   
/*    */   public void setDirection(Direction direction) {
/* 43 */     this.direction = direction;
/*    */   }
/*    */   
/*    */   public int getDamage() {
/* 47 */     return this.damage;
/*    */   }
/*    */   
/*    */   public void setDamage(int damage) {
/* 51 */     this.damage = damage;
/*    */   }
/*    */   
/*    */   public void setBulletId(int bulletId) {
/* 55 */     this.bulletId = bulletId;
/*    */   }
/*    */   
/*    */   public int getBulletId() {
/* 59 */     return this.bulletId;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\Bullet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */