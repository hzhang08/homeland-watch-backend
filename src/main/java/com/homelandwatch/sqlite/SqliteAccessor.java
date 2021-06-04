package com.homelandwatch.sqlite;

import com.arakelian.faker.model.Gender;
import com.arakelian.faker.model.Person;
import com.arakelian.faker.service.RandomAddress;
import com.arakelian.faker.service.RandomPerson;
import com.homelandwatch.model.RequestDAO;
import com.homelandwatch.model.UserDAO;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SqliteAccessor {
    private String _sqliteUrl;

    private SqliteAccessor(String sqliteUrl) {
        _sqliteUrl = sqliteUrl;

    }

    public static void main(String[] args) {
        SqliteAccessor sqliteAccessor = new SqliteAccessor("jdbc:sqlite:src/main/resources/homeland_watch.db");

        // clean up
//        sqliteAccessor.dropUserTable();
        sqliteAccessor.dropRequestTable();

        // init DB
//        sqliteAccessor.createUserTable();
        sqliteAccessor.createRequestTable();

        // generate fake users
//        List<UserDAO> elderly = generateUserFromPerson(RandomPerson.get().listOf(20),
//                UserDAO.Role.Elderly, 0);
//        List<UserDAO> volunteers = generateUserFromPerson(RandomPerson.get().listOf(20),
//                UserDAO.Role.Volunteer, 20);
//        sqliteAccessor.insertUser(elderly);
//        sqliteAccessor.insertUser(volunteers);
//
//        List<UserDAO> users = sqliteAccessor.getAllUsers();
//        for (UserDAO user: users) {
//            System.out.println(user.toString());
//        }

        // TODO generate fake request
        
    }
    private static List<UserDAO> generateUserFromPerson(List<Person> people,
                                                 UserDAO.Role role,
                                                        int idStart) {
        List<UserDAO> users = new ArrayList<>();
        int id = idStart;
        Random rand = new Random();
        for (Person person : people) {
            UserDAO user = new UserDAO(id++, person.getFirstName() + " " + person.getLastName(),
                    "http://example.com", person.getAge(),
                    person.getGender().equals(Gender.MALE) ? UserDAO.Gender.Male : UserDAO.Gender.Female,
                    rand.nextInt(10000), RandomAddress.get().next().getStreet(), role);
            users.add(user);
        }
        return users;
    }

    /*********************** User Table CRUD ************************/

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

    private void createUserTable() {
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
                    "(ID INT PRIMARY KEY NOT NULL AUTOINCREMENT, " +
                    "NAME CHAR(20) NOT NULL, " +
                    "PHONEURL CHAR(500) NOT NULL," +
                    "AGE INT NOT NULL," +
                    "GENDER CHAR(20) NOT NULL," +
                    "CREDIT INT," +
                    "ADDRESS CHAR(300) NOT NULL," +
                    "ROLE CHAR(20) NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            // log error
            System.out.println("create User table error: " + ex);
        }
    }

    public List<UserDAO> getAllUsers() {
        List<UserDAO> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            String sql = "SELECT * from USER";
            try(Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {

                while (result.next()) {
                    UserDAO user = new UserDAO();
                    user.setUserID(Integer.parseInt(result.getString("ID")));
                    user.setName(result.getString("NAME"));
                    user.setPhotoUrl(result.getString("PHONEURL"));
                    user.setAge(Integer.parseInt(result.getString("AGE")));
                    user.setGender(UserDAO.Gender.Male.toString().equals(result.getString("GENDER")) ?
                            UserDAO.Gender.Male : UserDAO.Gender.Female);
                    user.setCredit(Integer.parseInt(result.getString("CREDIT")));
                    user.setAddress(result.getString("ADDRESS"));
                    user.setRole(UserDAO.Role.Volunteer.toString().equals(result.getString("ROLE")) ?
                            UserDAO.Role.Volunteer : UserDAO.Role.Elderly);
                    users.add(user);
                }

            } catch (SQLException ex) {
                System.out.println("getUser statement error: " + ex);
            }
        } catch (SQLException ex) {
            // log error
            System.out.println("getUser error: " + ex);
        }
        return users;
    }

    public UserDAO getUserById(int userId) {
        UserDAO user = new UserDAO();
        user.setUserID(userId);

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
                user.setGender(UserDAO.Gender.Male.toString().equals(result.getString("GENDER")) ?
                        UserDAO.Gender.Male : UserDAO.Gender.Female);
                user.setCredit(Integer.parseInt(result.getString("CREDIT")));
                user.setAddress(result.getString("ADDRESS"));
                user.setRole(UserDAO.Role.Volunteer.toString().equals(result.getString("ROLE")) ?
                        UserDAO.Role.Volunteer : UserDAO.Role.Elderly);

            } catch (SQLException ex) {
                System.out.println("getUser statement error: " + ex);
            }
        } catch (SQLException ex) {
            // log error
            System.out.println("getUser error: " + ex);
        }
        return user;
    }

    public void insertUser(UserDAO user) {
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {
            insertUserAndExecute(conn, user);
        } catch (SQLException ex) {
            // log error
            System.out.println("inserUser error: " + ex);
        }
    }

    public void insertUser(List<UserDAO> users) {
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {
            for (UserDAO user: users) {
                insertUserAndExecute(conn, user);
            }

        } catch (SQLException ex) {
            // log error
            System.out.println("inserUser error: " + ex);
        }
    }

    private void insertUserAndExecute(Connection conn, UserDAO user) {
        String sql = "INSERT INTO USER ( NAME, PHONEURL, AGE," +
                " GENDER, CREDIT, ADDRESS, ROLE) " +
                "VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            int idx = 1;
            //preparedStatement.setInt(idx++, user.getUserID());
            preparedStatement.setString(idx++, user.getName());
            preparedStatement.setString(idx++, user.getPhotoUrl());
            preparedStatement.setInt(idx++, user.getAge());
            preparedStatement.setString(idx++, user.getGender().toString());
            preparedStatement.setInt(idx++, user.getCredit());
            preparedStatement.setString(idx++, user.getAddress());
            preparedStatement.setString(idx, user.getRole().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("insert user error: " + ex);
            ex.printStackTrace();
        }
    }

    /*********************** Request Table CRUD ************************/

    private void dropRequestTable() {
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            Statement statement = conn.createStatement();
            String sql = "DROP TABLE REQUEST";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            // log error
            System.out.println(ex);
        }
    }

    private void createRequestTable() {
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            /**
             * Request table
             *         request_id
             *         request_type(ride, shop,companion)
             *         elderly_id
             *         elderly_name
             *         volunteer_id
             *         volunteer_name
             *         request_start_time
             *         request_end_time
             *         request_start_location
             *         request_destination_location
             *         request_status (open, accepted,inprogress,fulfilled)
             *         note
             * */
            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE REQUEST " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TYPE CHAR(30) NOT NULL," +
                    "ELDERLY_ID INT NOT NULL," +
                    "ELDERLY_NAME INT NOT NULL," +
                    "VOLUNTEER_ID INT ," +
                    "VOLUNTEER_NAME INT ," +
                    "START_TIME TIMESTAMP ," +
                    "END_TIME TIMESTAMP ," +
                    "ORIGIN VARCHAR(300)," +
                    "DESTINATION VARCHAR(300)," +
                    "STATUS VARCHAR(20) NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            // log error
            System.out.println("" + ex);
        }
    }

    public void insertRequest(List<RequestDAO> requests) {

    }


}
