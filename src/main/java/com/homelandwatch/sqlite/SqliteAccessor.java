package com.homelandwatch.sqlite;

import com.homelandwatch.model.RequestDAO;
import com.homelandwatch.model.UserDAO;

import java.sql.*;
import java.util.Collections;
import java.util.List;

public class SqliteAccessor {
    private String _sqliteUrl;

    private SqliteAccessor(String sqliteUrl) {
        _sqliteUrl = sqliteUrl;

    }

    public static void main(String[] args) {
        SqliteAccessor sqliteAccessor = new SqliteAccessor("jdbc:sqlite:src/main/resources/homeland_watch.db");
        try {
            // init DB
            sqliteAccessor.createUserTable();

            // generate fake users
            sqliteAccessor.insertUser(Collections.singletonList(new UserDAO(42, "Wong", "http://example.com", 74, false, 32, "San Jose", false)));
            UserDAO user = sqliteAccessor.getUser(42);
            System.out.println(user.toString());

        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } finally {
            sqliteAccessor.dropUserTable();
        }
    }

    private void dropUserTable() {
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            Statement statement = conn.createStatement();
            String sql = "DROP TABLE USER";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            // log error
            System.out.println(ex);
        }
    }

    private void createUserTable() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            /**
             *     int userID;
             *     String name;
             *     String photoUrl;
             *     int age;
             *     boolean gender;
             *     int credit;
             *     String address;
             *     boolean isVolunteer;
             */
            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE USER " +
                    "(ID INT PRIMARY KEY NOT NULL, " +
                    "NAME TEXT NOT NULL, " +
                    "PHONEURL TEXT NOT NULL," +
                    "AGE INT NOT NULL," +
                    "GENDER CHAR(1) NOT NULL," +
                    "CREDIT INT," +
                    "ADDRESS TEXT NOT NULL," +
                    "ROLE CHAR(1) NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            // log error
            System.out.println("create User table error: " + ex);
        }
    }

    public void createRequestTable() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            /**
             * Request table
             *         request_id
             *         request_type(ride, shop,companion)
             *         elderly_id
             *         volunteer_id
             *         request_start_time
             *         request_end_time
             *         request_start_location
             *         request_destination_location
             *         request_status (open, accepted,inprogress,fulfilled)
             *         note
             * */
            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE REQUEST " +
                    "()";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            // log error
            System.out.println("" + ex);
        }

    }

    public UserDAO getUser(int userId) throws ClassNotFoundException {
        UserDAO user = new UserDAO();
        user.setUserID(userId);
        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            String sql = "SELECT * from USER WHERE ID =" + userId;
            try(Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {

                if (result.isClosed()) {
                    return null;
                }
                user.setName(result.getString("NAME"));
                user.setPhotoUrl(result.getString("PHONEURL"));
                user.setAge(Integer.parseInt(result.getString("AGE")));
                user.setGender(Boolean.parseBoolean(result.getString("GENDER")));
                user.setCredit(Integer.parseInt(result.getString("CREDIT")));
                user.setAddress(result.getString("ADDRESS"));
                user.setVolunteer(Boolean.parseBoolean(result.getString("ROLE")));

            } catch (SQLException ex) {
                System.out.println("getUser statement error: " + ex);
            }
        } catch (SQLException ex) {
            // log error
            System.out.println("getUser error: " + ex);
        }
        return user;
    }

    public void insertUser(List<UserDAO> users) {
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            for (UserDAO user: users) {
                String sql = "INSERT INTO USER (ID, NAME, PHONEURL, AGE," +
                        " GENDER, CREDIT, ADDRESS, ROLE) " +
                        "VALUES (?,?,?,?,?,?,?,?)";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    int idx = 1;
                    preparedStatement.setInt(idx++, user.getUserID());
                    preparedStatement.setString(idx++, user.getName());
                    preparedStatement.setString(idx++, user.getPhotoUrl());
                    preparedStatement.setInt(idx++, user.getAge());
                    preparedStatement.setString(idx++, user.isMale() ? "Y" : "N");
                    preparedStatement.setInt(idx++, user.getCredit());
                    preparedStatement.setString(idx++, user.getAddress());
                    preparedStatement.setString(idx, user.isVolunteer() ? "Y" : "N");
                    
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println("insert user error: " + ex);
                    ex.printStackTrace();
                }
            }

        } catch (SQLException ex) {
            // log error
            System.out.println("inserUser error: " + ex);
        }
    }

    public void insertRequest(RequestDAO request) {

    }
}
