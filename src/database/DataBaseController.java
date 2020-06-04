package database;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class DataBaseController implements DataBaseController_I {
    private Connection connection;
    private  Statement statement;
    private final String F_BASE = "SELECT ";

    private boolean openConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LAB4","user_for_lab4", "43fyfyfc");
            connection.setAutoCommit(false);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean isDBExists() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LAB4","user_for_lab4", "43fyfyfc");
            connection.setAutoCommit(false);

        } catch (ClassNotFoundException | SQLException e) {
             return false;
        }
        closeConnection();
        return true;
    }

    @Override
    public void createDB() {
        openConnection();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","user_for_lab4", "43fyfyfc");
            statement = connection.createStatement();
            String command = "CREATE DATABASE \"LAB4\" WITH TEMPLATE \"LAB4_template\" OWNER = user_for_lab4 IS_TEMPLATE = false;";
            statement.execute(command);
            statement.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("No connection");
            alert.showAndWait();
        } finally {
            closeConnection();

        }
    }

    @Override
    public void dropDB() {

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","user_for_lab4", "43fyfyfc");
            statement = connection.createStatement();
            String command = "DROP DATABASE IF EXISTS \"LAB4\"";
            statement.executeUpdate(command);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {

                try {
                    if(statement != null) {
                    statement.close(); }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try{
                if(connection != null) {
                    connection.close();
                }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public void get_P_Inf(ObservableList<Player> list) {
        openConnection();
        try {
            statement = connection.createStatement();
            String command = F_BASE + "* FROM show_players();";
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()){
                list.add(new Player(resultSet.getString("id_code"),resultSet.getString("pname"),resultSet.getString("status"),resultSet.getInt("characters_count")));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();

        }

    }

    public  void get_C_Inf(ObservableList<Character> list) {
        openConnection();
        try {
            statement = connection.createStatement();
            String command = F_BASE + "* FROM show_characters();";
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()){
                list.add(new Character(resultSet.getString("id_code"), resultSet.getString("owner_id"),resultSet.getString("c_name"),resultSet.getString("c_class"),resultSet.getInt("lvl")));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        }finally {
            closeConnection();

        }

    }

    @Override
    public void clear_table(String table_name) {
        openConnection();
        String command = F_BASE + "clear_table";
        if(table_name.equals("players")) {
            command += "s();";
        }
        else {
            command += "_PCCARACTERS();";
        }
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        }finally {
            closeConnection();
        }

    }

    @Override
    public void clear_all_tables() {
        openConnection();
        try {
            statement = connection.createStatement();
            String command = F_BASE + "clear_tables();";
            statement.execute(command);
            statement.close();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void add(Player player) {
        String command = F_BASE + "add_player(\'" + player.getName() + "\', \'" + player.getId_code() +"\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            if (!player.getStatus().equals("")) {
                command = F_BASE + "add_status (\'" + player.getId_code() + "\', \'" + player.getStatus() + "\');";
                System.out.println(command);
                statement.execute(command);
            }

            statement.close();
            connection.commit();
        } catch (SQLException e) {
            //e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Add");
            alert.setContentText("try another ID");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void add(Character character) {
        // add_character(c_id char(5), p_id char(5), cname char(20), cclass char(15))
        System.out.println("add cha");
        String command = F_BASE + "add_character(\'" + character.getId_code() + "\', \'" + character.getOwner_id() +"\', \'" + character.getName() + "\', \'" + character.getC_class() +"\');";
        System.out.println(command);
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            //e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Add");
            alert.setContentText("try another ID");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void changeData(Player player) {
        //update_player_inf(p_id char(5), p_name char(20), p_status char(30))
        String command = F_BASE + "update_player_inf(\'" + player.getId_code() + "\', \'" + player.getName() + "\', \'" + player.getStatus() + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            //e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void changeData(Character character) {
        //update_character_inf(c_id char(5), newclass char(15), newname char(20), newlvl int)
        String command = F_BASE + "update_character_inf( \'" + character.getId_code() + "\', \'" + character.getC_class() + "\', \'" + character.getName() + "\', \'" + character.getLvl() + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void findClass(String c_class, ObservableList<Character> list) {
        //find_class(f_class char(15))
        String command = F_BASE + "* FROM find_class(\'" + c_class + "\');";
        System.out.println(command);
        openConnection();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                System.out.println("add f class");
                list.add(new Character(resultSet.getString("id_code"), resultSet.getString("owner_id"),resultSet.getString("c_name"),resultSet.getString("c_class"),resultSet.getInt("lvl")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void findPName(String name, ObservableList<Player> list) {
        String command = F_BASE + "* FROM find_p_name(\'" + name + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                list.add(new Player(resultSet.getString("id_code"),resultSet.getString("pname"),resultSet.getString("status"),resultSet.getInt("characters_count")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void findCName(String name, ObservableList<Character> list) {
        String command = F_BASE + "* FROM find_c_name(\'" + name + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                list.add(new Character(resultSet.getString("id_code"), resultSet.getString("owner_code"),resultSet.getString("c_name"),resultSet.getString("c_class"),resultSet.getInt("lvl")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void findAmount(int amount, ObservableList<Player> list) {
        String command = F_BASE + "* FROM find_amount(\'" + amount + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                list.add(new Player(resultSet.getString("id_code"),resultSet.getString("pname"),resultSet.getString("status"),resultSet.getInt("characters_count")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void deleteNode(Player player) {
        //delete_player(p_id char(5))
        String command = F_BASE + "delete_player(\'" + player.getId_code() + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void deleteNode(Character character) {
        //delete_character(c_id char(5))
        String command = F_BASE + "delete_character(\'" + character.getId_code() + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void deleteByClass(String d_class) {
    // delete_characters_by_class(d_class char(15))
        String command = F_BASE + " delete_characters_by_class(\'" + d_class + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        }finally {
            closeConnection();
        }
    }

    @Override
    public void deleteByAmount(int amount) {
        String command = F_BASE + " delete_players_by_amount(\'" + amount + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        }finally {
            closeConnection();
        }
    }

    @Override
    public void deleteByPName(String name) {
        String command = F_BASE + " delete_p_by_name(\'" + name + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        }finally {
            closeConnection();
        }
    }

    @Override
    public void deleteByCName(String name) {
        String command = F_BASE + " delete_c_by_name(\'" + name + "\');";
        openConnection();
        try {
            statement = connection.createStatement();
            statement.execute(command);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Error");
            alert.setContentText("Error");
            alert.showAndWait();
        }finally {
            closeConnection();
        }
    }

}
