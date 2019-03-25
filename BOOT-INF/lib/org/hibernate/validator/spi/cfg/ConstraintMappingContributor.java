package org.hibernate.validator.spi.cfg;

import org.hibernate.validator.cfg.ConstraintMapping;

public abstract interface ConstraintMappingContributor
{
  public abstract void createConstraintMappings(ConstraintMappingBuilder paramConstraintMappingBuilder);
  
  public static abstract interface ConstraintMappingBuilder
  {
    public abstract ConstraintMapping addConstraintMapping();
  }
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\spi\cfg\ConstraintMappingContributor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */