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
    private final String USER_ID = "ID";
    private final String USER_NAME = "NAME";
    private final String USER_PHONEURL = "PHONEURL";
    private final String USER_AGE = "AGE";
    private final String USER_GENDER = "GENDER";
    private final String USER_CREDIT = "CREDIT";
    private final String USER_ADDRESS = "ADDRESS";
    private final String USER_ROLE = "ROLE";


    private String _sqliteUrl;

    private SqliteAccessor(String sqliteUrl) {
        _sqliteUrl = sqliteUrl;

    }

    public static void main(String[] args) {
        // used to verification and pre-generate data in db
        SqliteAccessor sqliteAccessor = new SqliteAccessor("jdbc:sqlite:src/main/resources/homeland_watch.db");

        // clean up
        sqliteAccessor.dropUserTable();
        sqliteAccessor.dropRequestTable();

        // init DB
        sqliteAccessor.createUserTable();
        sqliteAccessor.createRequestTable();

        // generate fake users
        List<UserDAO> elderly = generateUserFromPerson(RandomPerson.get().listOf(20),
                UserDAO.Role.Elderly, 0);
        List<UserDAO> volunteers = generateUserFromPerson(RandomPerson.get().listOf(20),
                UserDAO.Role.Volunteer, 20);
        sqliteAccessor.insertUser(elderly);
        sqliteAccessor.insertUser(volunteers);

        List<UserDAO> users = sqliteAccessor.getAllUsers();
        for (UserDAO user: users) {
            System.out.println(user.toString());
        }

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
                    "(ID INT PRIMARY KEY NOT NULL, " +
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

    public UserDAO getUserByName(String username) {
        UserDAO user = new UserDAO();
        user.setName(username);

        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            String sql = "SELECT * from USER WHERE NAME =" + username;
            try(Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {

                if (result.isClosed()) {
                    return null;
                }
                user.setUserID(Integer.parseInt(result.getString("NAME")));
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
        String sql = "INSERT INTO USER (ID, NAME, PHONEURL, AGE," +
                " GENDER, CREDIT, ADDRESS, ROLE) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            int idx = 1;
            preparedStatement.setInt(idx++, user.getUserID());
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
                    "(ID INT PRIMARY KEY NOT NULL, " +
                    "TYPE CHAR(30) NOT NULL," +
                    "ELDERLY_ID INT NOT NULL," +
                    "VOLUNTEER_ID INT," +
                    "START_TIME TIMESTAMP NOT NULL," +
                    "END_TIME TIMESTAMP NOT NULL," +
                    "ORIGIN_LONG NUMBER(10, 8) NOT NULL," +
                    "ORIGIN_LAT NUMBER(10, 8) NOT NULL " +
                    "DESTINATION_LONG NUMBER(10, 8) NOT NULL," +
                    "DESTINATION_LAT NUMBER(10, 8) NOT NULL" +
                    "STATUS CHAR(20) NOT NULL)";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            // log error
            System.out.println("" + ex);
        }
    }

    public void insertRequest(RequestDAO request) {
        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {
            String sql = "INSERT INTO REQUEST (ID, TYPE, ELDERLY_ID, VOLUNTEER_ID," +
                    " START_TIME, END_TIME, ORIGIN_LONG, ORIGIN_LAT," +
                    " DESTINATION_LONG, DESTINATION_LAT, STATUS) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                int idx = 1;
                preparedStatement.setInt(idx++, request.getRequestId());
                preparedStatement.setString(idx++, request.getRequestType());
                preparedStatement.setInt(idx++, request.getElderlyId());
                preparedStatement.setInt(idx++, request.getVolunteerId());
                preparedStatement.setLong(idx++, request.getRequestStartTime());
                preparedStatement.setLong(idx++, request.getRequestEndTime());
                preparedStatement.setDouble(idx++, request.getStartLocationLongtitude());
                preparedStatement.setDouble(idx++, request.getStartLocationLatitude());
                preparedStatement.setDouble(idx++, request.getEndLocationLongtitude());
                preparedStatement.setDouble(idx++, request.getEndLocationLatitude());
                preparedStatement.setString(idx, request.getRequestStatus().toString());

                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("insert request error: " + ex);
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public RequestDAO getRequestById(int requestId) {
        RequestDAO request = new RequestDAO();
        request.setRequestId(requestId);

        try (Connection conn = DriverManager.getConnection(_sqliteUrl)) {

            String sql = "SELECT * from REQUEST WHERE ID =" + requestId;
            try(Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {

                if (result.isClosed()) {
                    return null;
                }
                request.setElderlyId(result.getInt("ELDERLY_ID"));
                if (RequestDAO.RequestStatus.valueOf(result.getString("STATUS"))
                        != RequestDAO.RequestStatus.Open) {
                    request.setVolunteerId(result.getInt("VOLUNTEER_ID"));
                }
                // set...

            } catch (SQLException ex) {
                System.out.println("getUser statement error: " + ex);
            }
        } catch (SQLException ex) {
            // log error
            System.out.println("getUser error: " + ex);
        }
        return request;
    }

    public void updateRequestWithVolunteer(int userId, int requestId) {

    }

    public void updateRequestStatus(int requestId, RequestDAO.RequestStatus requestStatus) {

    }

    public List<RequestDAO> getOpenRequest(long startTime) {
        List<RequestDAO> requestList = new ArrayList<>();
        // getOpenRequest according to startTime
        return requestList;
    }
}
