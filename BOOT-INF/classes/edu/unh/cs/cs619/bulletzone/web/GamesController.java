/*     */ package edu.unh.cs.cs619.bulletzone.web;
/*     */ 
/*     */ import edu.unh.cs.cs619.bulletzone.model.Direction;
/*     */ import edu.unh.cs.cs619.bulletzone.model.IllegalTransitionException;
/*     */ import edu.unh.cs.cs619.bulletzone.model.LimitExceededException;
/*     */ import edu.unh.cs.cs619.bulletzone.model.Tank;
/*     */ import edu.unh.cs.cs619.bulletzone.model.TankDoesNotExistException;
/*     */ import edu.unh.cs.cs619.bulletzone.repository.GameRepository;
/*     */ import edu.unh.cs.cs619.bulletzone.util.BooleanWrapper;
/*     */ import edu.unh.cs.cs619.bulletzone.util.GridWrapper;
/*     */ import edu.unh.cs.cs619.bulletzone.util.LongWrapper;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ import org.springframework.web.client.RestClientException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/games"})
/*     */ class GamesController
/*     */ {
/*  42 */   private static final Logger log = LoggerFactory.getLogger(GamesController.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private final GameRepository gameRepository;
/*     */   
/*     */ 
/*     */ 
/*     */   @Autowired
/*     */   public GamesController(GameRepository gameRepository)
/*     */   {
/*  53 */     this.gameRepository = gameRepository;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.POST}, value={""}, produces={"application/json"})
/*     */   @ResponseStatus(HttpStatus.CREATED)
/*     */   @ResponseBody
/*     */   ResponseEntity<LongWrapper> join(HttpServletRequest request)
/*     */   {
/*     */     try
/*     */     {
/*  68 */       Tank tank = this.gameRepository.join(request.getRemoteAddr());
/*  69 */       log.info("Player joined: tankId={} IP={}", Long.valueOf(tank.getId()), request.getRemoteAddr());
/*  70 */       return new ResponseEntity(new LongWrapper(tank
/*  71 */         .getId()), HttpStatus.CREATED);
/*     */     }
/*     */     catch (RestClientException e)
/*     */     {
/*  75 */       e.printStackTrace();
/*     */     }
/*  77 */     return null;
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
/*     */   @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.PUT}, value={"{tankId}/eject/{0}"}, produces={"application/json"})
/*     */   @ResponseStatus(HttpStatus.ACCEPTED)
/*     */   ResponseEntity<BooleanWrapper> eject(@PathVariable long tankId)
/*     */     throws TankDoesNotExistException, LimitExceededException, IllegalTransitionException
/*     */   {
/*  93 */     return new ResponseEntity(new BooleanWrapper(this.gameRepository
/*  94 */       .eject(tankId)), HttpStatus.ACCEPTED);
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
/*     */   @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.GET}, value={""}, produces={"application/json"})
/*     */   @ResponseStatus(HttpStatus.OK)
/*     */   @ResponseBody
/*     */   public ResponseEntity<GridWrapper> grid()
/*     */   {
/* 110 */     return new ResponseEntity(new GridWrapper(this.gameRepository.getGrid()), HttpStatus.ACCEPTED);
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
/*     */   @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.PUT}, value={"{tankId}/turn/{direction}"}, produces={"application/json"})
/*     */   @ResponseStatus(HttpStatus.ACCEPTED)
/*     */   ResponseEntity<BooleanWrapper> turn(@PathVariable long tankId, @PathVariable byte direction)
/*     */     throws TankDoesNotExistException, LimitExceededException, IllegalTransitionException
/*     */   {
/* 127 */     return new ResponseEntity(new BooleanWrapper(this.gameRepository
/* 128 */       .turn(tankId, Direction.fromByte(direction))), HttpStatus.ACCEPTED);
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
/*     */   @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.PUT}, value={"{tankId}/move/{direction}"}, produces={"application/json"})
/*     */   @ResponseStatus(HttpStatus.ACCEPTED)
/*     */   ResponseEntity<BooleanWrapper> move(@PathVariable long tankId, @PathVariable byte direction)
/*     */     throws TankDoesNotExistException, LimitExceededException, IllegalTransitionException
/*     */   {
/* 149 */     return new ResponseEntity(new BooleanWrapper(this.gameRepository
/* 150 */       .move(tankId, Direction.fromByte(direction))), HttpStatus.ACCEPTED);
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
/*     */   @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.PUT}, value={"{tankId}/fire/{bulletType}"}, produces={"application/json"})
/*     */   @ResponseStatus(HttpStatus.ACCEPTED)
/*     */   ResponseEntity<BooleanWrapper> fire(@PathVariable long tankId, @PathVariable int bulletType)
/*     */     throws TankDoesNotExistException, LimitExceededException
/*     */   {
/* 169 */     return new ResponseEntity(new BooleanWrapper(this.gameRepository
/* 170 */       .fire(tankId, bulletType)), HttpStatus.ACCEPTED);
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
/*     */   @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.DELETE}, value={"{tankId}/leave"}, produces={"application/json"})
/*     */   @ResponseStatus(HttpStatus.ACCEPTED)
/*     */   HttpStatus leave(@PathVariable long tankId)
/*     */     throws TankDoesNotExistException
/*     */   {
/* 188 */     this.gameRepository.leave(tankId);
/* 189 */     return HttpStatus.ACCEPTED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ExceptionHandler({IllegalArgumentException.class})
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   String handleBadRequests(Exception e)
/*     */   {
/* 202 */     return e.getMessage();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\web\GamesController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */