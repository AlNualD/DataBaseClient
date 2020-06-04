package database;

import javafx.beans.Observable;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

public interface DataBaseController_I {
    public boolean isDBExists();
    public void createDB();
    public void dropDB();
    //public ResultSet get_data(String table_name);
    public void clear_table(String table_name);
    public void clear_all_tables();
    public void add(Player player);
    public void add(Character character);
    public void changeData(Player player);
    public void changeData(Character character);
    public void findClass(String c_class, ObservableList<Character> list);
    public void findPName(String name, ObservableList<Player> list);
    public void findCName(String name, ObservableList<Character> list);
    public void findAmount(int amount, ObservableList<Player> list);
    public void deleteNode(Player player);
    public void deleteNode(Character character);
    public void deleteByClass(String d_class);
    public void deleteByAmount(int amount);
    public void deleteByPName(String name);
    public void deleteByCName(String name);
}
