package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
public abstract interface TypeToInstanceMap<B>
  extends Map<TypeToken<? extends B>, B>
{
  @Nullable
  public abstract <T extends B> T getInstance(Class<T> paramClass);
  
  @Nullable
  @CanIgnoreReturnValue
  public abstract <T extends B> T putInstance(Class<T> paramClass, @Nullable T paramT);
  
  @Nullable
  public abstract <T extends B> T getInstance(TypeToken<T> paramTypeToken);
  
  @Nullable
  @CanIgnoreReturnValue
  public abstract <T extends B> T putInstance(TypeToken<T> paramTypeToken, @Nullable T paramT);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\reflect\TypeToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */