package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V1_0_3__AddCities extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        Statement statement = context.getConnection().createStatement();
        statement.execute(
                "INSERT INTO villes" +
                        "(name, pays) values" +
                        "('Bruxelles',1)");
        statement.execute(
                "INSERT INTO villes" +
                        "(name, pays) values" +
                        "('Paris',2)");
        statement.execute(
                "INSERT INTO villes" +
                        "(name,pays) values" +
                        "('Berlin',3)");
        statement.execute(
                "INSERT INTO villes" +
                        "(name,pays) values" +
                        "('Wiltz',4)");
    }
}
