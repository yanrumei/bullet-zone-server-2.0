package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;

@GwtCompatible(emulated=true)
public abstract interface SortedMultiset<E>
  extends SortedMultisetBridge<E>, SortedIterable<E>
{
  public abstract Comparator<? super E> comparator();
  
  public abstract Multiset.Entry<E> firstEntry();
  
  public abstract Multiset.Entry<E> lastEntry();
  
  public abstract Multiset.Entry<E> pollFirstEntry();
  
  public abstract Multiset.Entry<E> pollLastEntry();
  
  public abstract NavigableSet<E> elementSet();
  
  public abstract Set<Multiset.Entry<E>> entrySet();
  
  public abstract Iterator<E> iterator();
  
  public abstract SortedMultiset<E> descendingMultiset();
  
  public abstract SortedMultiset<E> headMultiset(E paramE, BoundType paramBoundType);
  
  public abstract SortedMultiset<E> subMultiset(E paramE1, BoundType paramBoundType1, E paramE2, BoundType paramBoundType2);
  
  public abstract SortedMultiset<E> tailMultiset(E paramE, BoundType paramBoundType);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\SortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */