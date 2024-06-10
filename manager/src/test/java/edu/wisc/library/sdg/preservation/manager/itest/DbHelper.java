package edu.wisc.library.sdg.preservation.manager.itest;

import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DbHelper {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Flyway flyway;

    public DbHelper(Flyway flyway) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(flyway.getConfiguration().getDataSource());
        this.flyway = flyway;
    }

    public void baseline() {
        flyway.clean();
        flyway.migrate();
    }

}
