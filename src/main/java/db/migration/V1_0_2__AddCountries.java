package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V1_0_2__AddCountries extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        Statement statement = context.getConnection().createStatement();

        statement.execute(
                "INSERT INTO pays" +
                        "(name) values" +
                        "('Belgique')");
        statement.execute(
                "INSERT INTO pays" +
                        "(name) values" +
                        "('France')");
        statement.execute(
                "INSERT INTO pays" +
                        "(name) values" +
                        "('Allemagne')");
        statement.execute(
                "INSERT INTO pays" +
                        "(name) values" +
                        "('Luxembourg')");
    }
}
