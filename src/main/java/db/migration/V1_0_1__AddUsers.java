package db.migration;

import com.example.resthony.utils.BCryptManagerUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V1_0_1__AddUsers extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        String adminPasswordValue = BCryptManagerUtil.passwordEncoder().encode("ADMIN");
        String userPasswordValue = BCryptManagerUtil.passwordEncoder().encode("USER");
        String restPasswordValue = BCryptManagerUtil.passwordEncoder().encode("RESTO");

        Statement statement = context.getConnection().createStatement();
        statement.execute(
                "INSERT INTO users " +
                        "(id_user, account_non_expired, account_non_locked, credentials_non_expired, enabled, firstname, lastname, password, username, email, resto) values " +
                        "(1, 1, 1, 1, 1, 'Admin FirstName', 'Admin Lastname', 'admin', 'admin','admin@admin.com', 1)");
        statement.execute(
                "INSERT INTO users " +
                        "(id_user, account_non_expired, account_non_locked, credentials_non_expired, enabled, firstname, lastname, password, username, email) values " +
                        "(2, 1, 1, 1, 1, 'User FirstName', 'User Lastname', 'user', 'user','user@user.com')");
        statement.execute(
                "INSERT INTO users " +
                        "(id_user, account_non_expired, account_non_locked, credentials_non_expired, enabled, firstname, lastname, password, username, email) values " +
                        "(3, 1, 1, 1, 1, 'Resto FirstName', 'Resto Lastname', 'resto', 'resto','resto@resto.com')");

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
        statement.execute(
                "INSERT INTO restaurants" +
                        "(id_restaurants, name, adress, nb_place, opening_day, email, telephone, restaurateur) values" +
                        "('1', 'RestoTest', 'adresseduresto', 150, 'lundi', 'restoTest@resthony.com', '0496824571', 3)" );

        statement.execute("UPDATE users SET password='" + adminPasswordValue + "' WHERE username='admin'");
        statement.execute("INSERT INTO roles (id_user, `role`) values (1, 'ADMIN')");
        statement.execute("UPDATE users SET password='" + userPasswordValue + "' WHERE username='user'");
        statement.execute("INSERT INTO roles (id_user, `role`) values (2, 'USER')");
        statement.execute("UPDATE users SET password='" + restPasswordValue + "' WHERE username='resto'");
        statement.execute("INSERT INTO roles (id_user, `role`) values (3, 'restaurateur')");


    }
}