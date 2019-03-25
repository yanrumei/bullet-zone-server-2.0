package edu.unh.cs.cs619.bulletzone.repository;

import edu.unh.cs.cs619.bulletzone.model.Direction;
import edu.unh.cs.cs619.bulletzone.model.IllegalTransitionException;
import edu.unh.cs.cs619.bulletzone.model.LimitExceededException;
import edu.unh.cs.cs619.bulletzone.model.Tank;
import edu.unh.cs.cs619.bulletzone.model.TankDoesNotExistException;

public abstract interface GameRepository
{
  public abstract Tank join(String paramString);
  
  public abstract int[][] getGrid();
  
  public abstract boolean eject(long paramLong)
    throws TankDoesNotExistException, IllegalTransitionException, LimitExceededException;
  
  public abstract boolean turn(long paramLong, Direction paramDirection)
    throws TankDoesNotExistException, IllegalTransitionException, LimitExceededException;
  
  public abstract boolean move(long paramLong, Direction paramDirection)
    throws TankDoesNotExistException, IllegalTransitionException, LimitExceededException;
  
  public abstract boolean fire(long paramLong, int paramInt)
    throws TankDoesNotExistException, LimitExceededException;
  
  public abstract void leave(long paramLong)
    throws TankDoesNotExistException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\repository\GameRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */