package org.springframework.boot.autoconfigure.flyway;

import org.flywaydb.core.Flyway;

public abstract interface FlywayMigrationStrategy
{
  public abstract void migrate(Flyway paramFlyway);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\flyway\FlywayMigrationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */