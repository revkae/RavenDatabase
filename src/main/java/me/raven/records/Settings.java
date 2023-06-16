package me.raven.records;

/**
 *
 * @param host
 * The MySQL server IP
 * @param database
 * The database you want to use from your MySQL server
 * @param username
 * The username of your user
 * @param password
 * The password of your user
 * @param maxPool
 * The maximum pool amount
 * @param port
 * The MySQL server port
 * @param useSSL
 * Using the SSL for the connection
 */
public record Settings(String host, String database, String username, String password, int maxPool, int port, boolean useSSL) {

    public static Settings with(String host, String database, String username, String password, int maxPool, int port, boolean useSSL) {
        return new Settings(host, database, username, password, maxPool, port, useSSL);
    }
}
