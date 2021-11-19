package db.migration;

import com.example.resthony.utils.BCryptManagerUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.SQLException;
import java.sql.Statement;

public class V1_0_1_AddCountry extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws SQLException {
        Statement statement = context.getConnection().createStatement();
        statement.execute(
                "INSERT INTO pays " +
                        "(id_pays,name) values " +
                        "(1,'Belgique')");
    }
}