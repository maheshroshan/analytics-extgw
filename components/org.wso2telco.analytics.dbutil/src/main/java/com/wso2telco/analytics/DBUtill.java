package com.wso2telco.analytics;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.wso2telco.analytics.exception.DBUtilException;
import com.wso2telco.analytics.util.DataSourceNames;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DBUtill {

    /** The driver class. */
    String driverClass;

    /** The connection url. */
    String connectionUrl;

    /** The connection username. */
    String connectionUsername;

    /** The connection password. */
    String connectionPassword;

    /** The datasource. */
    private static volatile DataSource Datasource = null;

    /** The Constant DEP_DATA_SOURCE. */
    private static String DEP_DATA_SOURCE = null;

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(DBUtill.class);

    private static Map<DataSourceNames, DataSource> dbDataSourceMap;

    static {
        dbDataSourceMap = new HashMap<DataSourceNames, DataSource>();
    }

    /**
     * Initialize datasources.
     *
     * @throws SQLException
     *             the SQL exception
     *             the db util exception
     */
    public static void initializeDatasources() throws SQLException,DBUtilException {
        if (Datasource != null) {
            return;
        }

        try {
            log.info("Before DB Initialize");
            Context ctx = new InitialContext();
            DEP_DATA_SOURCE=(DataSourceNames.WSO2TELCO_RATE_DB.jndiName());
            Datasource = (DataSource) ctx.lookup(DEP_DATA_SOURCE);

        } catch (NamingException e) {
            handleException("Error while looking up the data source: " + DEP_DATA_SOURCE, e);
        }
    }

    /**
     * IMPORTANT : This method must be deprecated. going forward use
     * "getDbConnection(DataSourceNames dataSourceName)" method Gets the * db connection.
     *
     * @return the db connection
     * @throws SQLException
     *             the SQL exception
     * @throws DBUtilException
     *             the dbutilException exception
     */
    public static Connection getDBConnection() throws SQLException, DBUtilException {
        initializeDatasources();

        if (Datasource != null) {
            return Datasource.getConnection();
        }
        throw new SQLException("Datasource not initialized properly");
    }

    /**
     * Gets the db connection.
     *
     * @return the db connection
     * @throws SQLException
     *             the SQL exception
     */
    public static synchronized Connection getDbConnection(DataSourceNames dataSourceName) throws Exception {

        try {
            if (!dbDataSourceMap.containsKey(dataSourceName)) {

                Context ctx = new InitialContext();
                dbDataSourceMap.put(dataSourceName, (DataSource) ctx.lookup(dataSourceName.jndiName()));
            }

            DataSource dbDatasource = dbDataSourceMap.get(dataSourceName);

            if (dbDatasource != null) {

                log.info(dataSourceName.toString() + " DB Initialize successfully.");
                return dbDatasource.getConnection();
            } else {

                log.info(dataSourceName.toString() + " DB NOT Initialize successfully.");
                return null;
            }
        } catch (Exception e) {

            log.info("Error while looking up the data source: " + dataSourceName.toString(), e);
            throw e;
        }
    }

    /**
     * Format.
     *
     * @param strData
     *            the str data
     * @param finalLen
     *            the final len
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String format(String strData, int finalLen) throws Exception {
        String finalStr;
        if (finalLen <= strData.length()) {
            finalStr = strData.substring(0, finalLen);
        } else {
            finalStr = strData;
            for (int i = strData.length(); i < finalLen; i++) {
                finalStr = finalStr + " ";
            }
        }
        return (finalStr);
    }

    /**
     * Format.
     *
     * @param intData
     *            the int data
     * @param finalLen
     *            the final len
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String format(int intData, int finalLen) throws Exception {
        String strData = String.valueOf(intData);
        String finalStr;
        if (finalLen <= strData.length()) {
            finalStr = strData.substring(0, finalLen);
        } else {
            finalStr = "";
            for (int i = 0; i < finalLen - strData.length(); i++) {
                finalStr = finalStr + " ";
            }
            finalStr = finalStr + strData;
        }
        return (finalStr);
    } // format(int, int)

    /**
     *Format.
     *
     * @param integerData
     *            the integer data
     * @param finalLen
     *            the final len
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String format(Integer integerData, int finalLen) throws Exception {
        int intData;
        String finalStr;

        intData = integerData.intValue();
        finalStr = format(intData, finalLen);

        return (finalStr);
    }

    /**
     * Format.
     *
     * @param doubData
     *            the doub data
     * @param precision
     *            the precision
     * @param scale
     *            the scale
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String format(double doubData, int precision, int scale) throws Exception {
        BigDecimal decData = new BigDecimal(doubData);
        decData = decData.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
        String strData = decData.toString();

        // prepare the final string
        int finalLen = precision + 1;
        String finalStr;
        if (finalLen <= strData.length()) {
            finalStr = strData.substring(0, finalLen);
        } else {
            finalStr = "";
            for (int i = 0; i < finalLen - strData.length(); i++) {
                finalStr = finalStr + " ";
            }
            finalStr = finalStr + strData;
        }

        return (finalStr);
    }

    /**
     * Format.
     *
     * @param decData
     *            the dec data
     * @param precision
     *            the precision
     * @param scale
     *            the scale
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String format(BigDecimal decData, int precision, int scale) throws Exception {
        decData = decData.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
        String strData = decData.toString();

        // prepare the final string
        int finalLen = precision + 1;
        String finalStr;
        if (finalLen <= strData.length()) {
            finalStr = strData.substring(0, finalLen);
        } else {
            finalStr = "";
            for (int i = 0; i < finalLen - strData.length(); i++) {
                finalStr = finalStr + " ";
            }
            finalStr = finalStr + strData;
        }

        return (finalStr);
    }

    /**
     * Format.
     *
     * @param doubleData
     *            the double data
     * @param precision
     *            the precision
     * @param scale
     *            the scale
     * @return the string
     * @throws Exception
     *             the exception
     */
    public static String format(Double doubleData, int precision, int scale) throws Exception {
        double doubData;

        doubData = doubleData.doubleValue();
        return (format(doubData, precision, scale));
    }
    /**
     * Connect.
     *
     * @return the connection
     * @throws Exception
     *             the exception
     */
    public Connection connect() throws Exception {
        System.out.println("-------- JDBC Connection Init ------------");
        Connection connection = null;

        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver Error");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }
        connection.setAutoCommit(false);

        return connection;
    }

    /**
     * Disconnect.
     *
     * @param con
     *            the con
     * @throws Exception
     *             the exception
     */
    public void disconnect(Connection con) throws Exception {
        System.out.println();

        // makes all changes made since the previous commit/rollback permanent
        // and releases any database locks currrently held by the Connection.
        con.commit();

        // immediately disconnects from database and releases JDBC resources
        con.close();
    }

    /**
     * Handle exception.
     *
     * @param msg
     *            the msg
     * @param t
     *            the t
     * @throws DBUtilException
     *             the DBUtilException exception
     */
    public static void handleException(String msg, Throwable t) throws DBUtilException {
        log.error(msg, t);
        throw new DBUtilException(msg, t);
    }

    /**
     * Close all connections.
     *
     * @param preparedStatement
     *            the prepared statement
     * @param connection
     *            the connection
     * @param resultSet
     *            the result set
     */
    public static void closeAllConnections(PreparedStatement preparedStatement, Connection connection,
                                           ResultSet resultSet) {

        closeConnection(connection);
        closeStatement(preparedStatement);
        closeResultSet(resultSet);
    }

    /**
     * Close all connections.
     *
     * @param statement
     *            the statement
     * @param connection
     *            the connection
     * @param resultSet
     *            the result set
     */
    public static void closeAllConnections(Statement statement, Connection connection, ResultSet resultSet) {

        closeConnection(connection);
        closeStatement(statement);
        closeResultSet(resultSet);
    }

    public static void closeResutl_statment(PreparedStatement preparedStatement, ResultSet resultSet) {
        closeStatement(preparedStatement);
        closeResultSet(resultSet);
    }

    /**
     * Close connection.
     *
     * @param dbConnection
     *            the db connection
     */
    private static void closeConnection(Connection dbConnection) {

        try {

            if (dbConnection != null && dbConnection.getAutoCommit() != true) {

                log.debug("database connection is active and auto commit is false");
                dbConnection.setAutoCommit(true);
                dbConnection.close();
                log.debug("database connection set to close and auto commit set to true");
            } else if (dbConnection != null) {

                log.debug("database connection is active");
                dbConnection.close();
                log.debug("database connection set to closed");
            }
        } catch (SQLException e) {

            log.error(
                    "database error. Could not close database connection. continuing with others. - " + e.getMessage(),
                    e);
        }

    }

    /**
     * Close result set.
     *
     * @param resultSet
     *            the result set
     */
    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.warn("Database error. Could not close ResultSet  - " + e.getMessage(), e);
            }
        }

    }

    /**
     * Close statement.
     *
     * @param preparedStatement
     *            the prepared statement
     */
    public static void closeStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                log.warn("Database error. Could not close PreparedStatement. Continuing with" + " others. - "
                        + e.getMessage(), e);
            }
        }
    }

    /**
     * Close statement.
     *
     * @param statement
     *            the statement
     */
    private static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.warn("Database error. Could not close Statement. Continuing with" + " others. - " + e.getMessage(),
                        e);
            }
        }
    }


}
