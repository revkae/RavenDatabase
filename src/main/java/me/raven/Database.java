package me.raven;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.NonNull;
import me.raven.records.Settings;
import org.h2.engine.Setting;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Database {

    @NonNull
    private static Database instance = null;
    @Getter
    private Query query;
    private HikariDataSource hikariDataSource;
    private Map<String, Table> tables = new HashMap<>();

    public Database(Settings settings) {
        instance = this;
        query = new Query(this);

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://" + settings.host() + ":"
                + settings.port() + "/" + settings.database() + "?useSSL=" + settings.useSSL());
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername(settings.username());
        hikariConfig.setPassword(settings.password());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setAutoCommit(true);
        hikariConfig.setMaximumPoolSize(settings.maxPool());

        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public Database(Properties properties) {
        instance = this;
        query = new Query(this);

        hikariDataSource = new HikariDataSource(new HikariConfig(properties));
    }

    public Database(String path) {
        instance = this;
        query = new Query(this);

        hikariDataSource = new HikariDataSource(new HikariConfig(path));
    }

    private void init() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/test?useSSL=false&sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        hikariConfig.addDataSourceProperty("verifyServerCertificate", "true");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setAutoCommit(true);
        hikariConfig.setMaximumPoolSize(15);

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() {
        try {
            if (hikariDataSource == null) {
                return null;
            }
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Database attachTable(Table table) {
        this.tables.put(table.getTableName(), table);
        return this;
    }

    public Table getTable(String name) {
        return this.tables.get(name);
    }

    public static Database get() {
        return instance;
    }
}
