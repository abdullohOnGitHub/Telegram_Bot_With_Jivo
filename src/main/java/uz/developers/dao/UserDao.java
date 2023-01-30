package uz.developers.dao;

import uz.developers.bot.enums.State;
import uz.developers.configuration.DBConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserDao {
    private final Connection connection = DBConnection.getInstance();
    private static UserDao userDao;

    public static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    public void save(Long userId, String firstname, String lastname, String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into telegram_bot_user (id, first_name, last_name, username) values (?, ?, ?, ?)");
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, firstname);
            preparedStatement.setString(3, lastname);
            preparedStatement.setString(4, username);
            preparedStatement.execute();
        } catch (SQLException ex) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("update telegram_bot_user set first_name = ?, last_name = ?, username = ? where id = ?");
                preparedStatement.setString(1, firstname);
                preparedStatement.setString(2, lastname);
                preparedStatement.setString(3, username);
                preparedStatement.setLong(4, userId);
                preparedStatement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<HashMap<String, Object>> getAll() {
        List<HashMap<String, Object>> data = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id, first_name, last_name, username from telegram_bot_user");
            while (resultSet.next()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", resultSet.getObject("id"));
                hashMap.put("firstname", resultSet.getObject("first_name"));
                hashMap.put("lastname", resultSet.getObject("last_name"));
                hashMap.put("username", resultSet.getObject("username"));
                data.add(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public boolean updateState(long userId, State state) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update telegram_bot_user set state = ? where id = ?");
            preparedStatement.setString(1, state.toString());
            preparedStatement.setLong(2, userId);
            return preparedStatement.execute();
        } catch (Exception e) {
            return false;

        }
    }

    public boolean isExists(Long userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from telegram_bot_user where id = ?");
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeLanguage(Long userId, String lan) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update telegram_bot_user set language = ? where id = ?");
            preparedStatement.setString(1, lan);
            preparedStatement.setLong(2, userId);
            return preparedStatement.execute();
        } catch (Exception e) {
            return false;

        }
    }

    public HashMap<String, Object> getUser(Long userId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select id, first_name,username,state, language from telegram_bot_user where id = ?");
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id",resultSet.getLong("id"));
                hashMap.put("first_name",resultSet.getString("first_name"));
                hashMap.put("username",resultSet.getString("username"));
                hashMap.put("state", resultSet.getString("state"));
                hashMap.put("language", resultSet.getString("language"));
                return hashMap;
            }else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

}
