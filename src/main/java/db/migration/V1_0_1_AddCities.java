package db.migration;

import com.example.resthony.utils.BCryptManagerUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.SQLException;
import java.sql.Statement;

public class V1_0_1_AddCities extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws SQLException {
        Statement statement = context.getConnection().createStatement();
        statement.execute(
                "INSERT INTO villes " +
                        "( id_ville, name, pays) values " +
                        "(1,'Bruxelles',1)");
    }
}