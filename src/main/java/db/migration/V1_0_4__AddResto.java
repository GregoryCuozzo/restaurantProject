package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V1_0_4__AddResto extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        Statement statement = context.getConnection().createStatement();
        statement.execute(
                "INSERT INTO restaurants" +
                        "(id_restaurants, name, adress, nb_place, email, telephone,ville,  restaurateur) values" +
                        "('1', 'RestoTest', 'adresseduresto', 150, 'restoTest@resthony.com', '0496824571',2, 3)" );
        statement.execute(
                "INSERT INTO restaurants" +
                        "(id_restaurants, name, adress, nb_place, email, telephone,ville,  restaurateur) values" +
                        "('2', 'RestScott', 'adresseduresto', 70, 'restoTest@resthony.com', '0496824571',3, 2)" );
    }
}
