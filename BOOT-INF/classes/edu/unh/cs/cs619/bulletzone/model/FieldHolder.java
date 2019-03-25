/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ import com.google.common.base.Optional;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldHolder
/*    */ {
/* 12 */   private final Map<Direction, FieldHolder> neighbors = new HashMap();
/* 13 */   private Optional<FieldEntity> entityHolder = Optional.absent();
/*    */   
/*    */   public void addNeighbor(Direction direction, FieldHolder fieldHolder) {
/* 16 */     this.neighbors.put(Preconditions.checkNotNull(direction), Preconditions.checkNotNull(fieldHolder));
/*    */   }
/*    */   
/*    */   public FieldHolder getNeighbor(Direction direction) {
/* 20 */     return (FieldHolder)this.neighbors.get(Preconditions.checkNotNull(direction, "Direction cannot be null."));
/*    */   }
/*    */   
/*    */   public boolean isPresent()
/*    */   {
/* 25 */     return this.entityHolder.isPresent();
/*    */   }
/*    */   
/*    */   public FieldEntity getEntity() {
/* 29 */     return (FieldEntity)this.entityHolder.get();
/*    */   }
/*    */   
/*    */   public void setFieldEntity(FieldEntity entity) {
/* 33 */     this.entityHolder = Optional.of(Preconditions.checkNotNull(entity, "FieldEntity cannot be null."));
/*    */   }
/*    */   
/*    */   public void clearField()
/*    */   {
/* 38 */     if (this.entityHolder.isPresent()) {
/* 39 */       this.entityHolder = Optional.absent();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\FieldHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */