package gui;

import database.DataBaseController;
import database.Player;
import database.Character;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.ParseException;
import java.util.Optional;
import java.util.regex.Pattern;


public class Controller {
    private boolean isDBExists;

    @FXML private TabPane tabPane;
    @FXML private Tab playersTab;

    @FXML private Button refreshButton;

    @FXML private TableView<Player> playersTableView;
    @FXML private TableColumn<Player,String> playerIdTableColumn;
    @FXML private TableColumn<Player,String> playerNameTableColumn;
    @FXML private TableColumn<Player,String> playerStatusTableColumn;
    @FXML private TableColumn<Player,Integer> playerAmountTableColumn;

    @FXML private TableView<Character> characterTableView;
    @FXML private TableColumn<Character,String> characterIdTableColumn;
    @FXML private TableColumn<Character,String> characterNameTableColumn;
    @FXML private TableColumn<Character,String> characterOwnerTableColumn;
    @FXML private TableColumn<Character,Integer> characterLvlTableColumn;
    @FXML private TableColumn<Character,String> characterClassTableColumn;

    @FXML private TextField p_id_TF;
    @FXML private TextField p_name_TF;
    @FXML private TextField p_amount_TF;
    @FXML private TextField p_status_TF;

    private TextField[] playersTF = new TextField[4];// = {p_id_TF,p_name_TF, p_amount_TF, p_status_TF};

    @FXML private TextField c_id_TF;
    @FXML private TextField c_owner_TF;
    @FXML private TextField c_name_TF;
    @FXML private TextField c_class_TF;
    @FXML private TextField c_lvl_TF;

    @FXML private TextField findField;


    @FXML private SplitMenuButton findButton;
    @FXML private MenuItem name_p_menu;
    @FXML private MenuItem name_c_menu;
    @FXML private MenuItem class_c_menu;
    @FXML private MenuItem amount_p_menu;

    @FXML private CheckBox andDeleteCheck;

    private Pattern numberABSPattern = Pattern.compile("(\\d*)");

    private TextField[] charactersTF = new TextField[5]; // = {c_id_TF, c_owner_TF, c_name_TF, c_class_TF, c_lvl_TF};

    private ObservableList<Player> players  = FXCollections.observableArrayList();
    private  ObservableList<Character> characters = FXCollections.observableArrayList();
    private final DataBaseController dataBaseController = new DataBaseController();

    @FXML
    private void initialize() {
//IDCol.setCellValueFactory(new PropertyValueFactory<Per,Integer>("id"));
        playerIdTableColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("id_code"));
        playerNameTableColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        playerStatusTableColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("status"));
        playerAmountTableColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("characters_amount"));
        playersTableView.setItems(players);

        characterIdTableColumn.setCellValueFactory(new PropertyValueFactory<Character, String>("id_code"));
        characterClassTableColumn.setCellValueFactory(new PropertyValueFactory<Character, String>("c_class"));
        characterOwnerTableColumn.setCellValueFactory(new PropertyValueFactory<Character, String>("owner_id"));
        characterNameTableColumn.setCellValueFactory(new PropertyValueFactory<Character, String>("name"));
        characterLvlTableColumn.setCellValueFactory(new PropertyValueFactory<Character, Integer>("lvl"));
        characterTableView.setItems(characters);

        // = {p_id_TF,p_name_TF, p_amount_TF, p_status_TF};
        playersTF[0] = p_id_TF;
        playersTF[1] = p_name_TF;
        playersTF[2] = p_amount_TF;
        playersTF[3] = p_status_TF;

        // = {c_id_TF, c_owner_TF, c_name_TF, c_class_TF, c_lvl_TF};
        charactersTF[0] = c_id_TF;
        charactersTF[1] = c_owner_TF;
        charactersTF[2] = c_name_TF;
        charactersTF[3] = c_class_TF;
        charactersTF[4] = c_lvl_TF;

        isDBExists = dataBaseController.isDBExists();

        c_lvl_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!numberABSPattern.matcher(newValue).matches()) c_lvl_TF.setText(oldValue);
        });
    }

    public void onPlayersTableClick() {
        Player player = playersTableView.getSelectionModel().selectedItemProperty().getValue();
        if(player == null) return;
        playersTF[0].setText(player.getId_code());
        playersTF[1].setText(player.getName());
        playersTF[2].setText(String.valueOf(player.getCharacters_amount()));
        playersTF[3].setText(player.getStatus());
    }

    public void onCharactersTableClick() {
        Character character = characterTableView.getSelectionModel().selectedItemProperty().getValue();
        if (character == null) return;
        charactersTF[0].setText(character.getId_code());
        charactersTF[1].setText(character.getOwner_id());
        charactersTF[2].setText(character.getName());
        charactersTF[3].setText(character.getC_class());
        charactersTF[4].setText(String.valueOf(character.getLvl()));
    }

    private void DBisDropped() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText("DB is dropped");
        alert.setContentText("Create DB again to do smth");
        alert.showAndWait();
    }
    public void refreshButtonClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
       if(playersTab.isSelected()) { //tabPane.getSelectionModel().getSelectedItem().getText().equals("players")) {
           players.clear();
           dataBaseController.get_P_Inf(players);
       } else {
           System.out.println("refresh characters");
           characters.clear();
           dataBaseController.get_C_Inf(characters);
       }
    }

    public void addButtonClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        if(playersTab.isSelected()) {
            if(playersTF[0].getText().equals("") || playersTF[1].getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("Can not add");
                alert.setContentText("Fill id and name!");
                alert.showAndWait();
            } else {
                System.out.println("player add inf " + playersTF[0].getText() + " " + p_id_TF.getText());
                Player player = new Player(playersTF[0].getText(),playersTF[1].getText(),playersTF[3].getText(),0);
                //Player player = new Player(p_id_TF.getText(),p_name_TF.getText(),p_status_TF.getText(),0);
                dataBaseController.add(player);
            }
        } else {
            if(charactersTF[0].getText().equals("") || charactersTF[1].getText().equals("") || charactersTF[2].getText().equals("") || charactersTF[3].getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("Can not add ");
                alert.setContentText("Fill id, owner_id,  name and class!");
                alert.showAndWait();
            } else {
                Character character = new Character(charactersTF[0].getText(),charactersTF[1].getText(),charactersTF[2].getText(),charactersTF[3].getText(),1);
                dataBaseController.add(character);

            }
        }
        refreshButtonClicked();
    }

    public void delButtonClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        if(playersTab.isSelected()) {
            Player player =  playersTableView.getSelectionModel().selectedItemProperty().getValue();
            if(player == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("Can not del");
                alert.setContentText("select player to del");
                alert.showAndWait();
            } else {
                dataBaseController.deleteNode(player);
            }
        } else {
            Character character = characterTableView.getSelectionModel().selectedItemProperty().getValue();
            if(character == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("Can not del");
                alert.setContentText("select character to del");
                alert.showAndWait();
            } else {
                dataBaseController.deleteNode(character);
            }
        }
        refreshButtonClicked();
    }

    public void changeButtonClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        if(playersTab.isSelected()) {
            if(playersTF[0].getText().equals("") || playersTF[1].getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("Can not  change");
                alert.setContentText("Fill id and name!");
                alert.showAndWait();
            } else {
                Player player = new Player(playersTF[0].getText(),playersTF[1].getText(),playersTF[3].getText(),0);
                dataBaseController.changeData(player);
            }
        } else {
            if(charactersTF[0].getText().equals("")  || charactersTF[2].getText().equals("") || charactersTF[3].getText().equals("") || charactersTF[4].getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("Can not change ");
                alert.setContentText("Fill id,  name and class!");
                alert.showAndWait();
            } else {
                String buff = charactersTF[4].getText();
                int lvl;
                if (buff.equals("")) { lvl = 1; }
                    else  lvl = Integer.parseInt(buff);
                Character character = new Character(charactersTF[0].getText(),"00000",charactersTF[2].getText(),charactersTF[3].getText(),lvl);
                dataBaseController.changeData(character);

            }
        }
        refreshButtonClicked();
    }

    public void find_p_nameClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        String buff = "";
        if ((buff = findField.getText()).equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Cant find");
            alert.setContentText("Fill find fied");
            alert.showAndWait();
        }
        else {
            players.clear();
            dataBaseController.findPName(buff,players);
            if(andDeleteCheck.isSelected()) {
                dataBaseController.deleteByPName(buff);
            }
        }
    }

    public void find_c_nameClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        String buff = "";
        if ((buff = findField.getText()).equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Cant find");
            alert.setContentText("Fill find fied");
            alert.showAndWait();
        }
        else {
            characters.clear();
            dataBaseController.findCName(buff, characters);
            if(andDeleteCheck.isSelected()) {
                dataBaseController.deleteByCName(buff);
            }
        }
    }

    public void find_classClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        String buff = "";
        if ((buff = findField.getText()).equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Cant find");
            alert.setContentText("Fill find fied");
            alert.showAndWait();
        }
        else {
            characters.clear();
            dataBaseController.findClass(buff, characters);
            if(andDeleteCheck.isSelected()) dataBaseController.deleteByClass(buff);
        }
    }

    public void find_amountClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        String buff = findField.getText();
        int a;
        try{
            a = Integer.parseInt(buff);
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("Cant find");
            alert.setContentText("Fill field correctly");
            alert.showAndWait();
            return;
        }
        players.clear();
        dataBaseController.findAmount(a, players);
        if (andDeleteCheck.isSelected()) {
            dataBaseController.deleteByAmount(a);
        }
    }


    public void clearPlayersClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        dataBaseController.clear_table("players");
        refreshButtonClicked();
    }

    public void clearCharactersClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        dataBaseController.clear_table("characters");
        refreshButtonClicked();
    }

    public void clearAllClicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        dataBaseController.clear_all_tables();
        refreshButtonClicked();
    }


    public void tabPaneOnChange() {
        if (playersTab.isSelected()) {
            name_p_menu.setVisible(true);
            amount_p_menu.setVisible(true);
            name_c_menu.setVisible(false);
            class_c_menu.setVisible(false);
        } else {
            name_p_menu.setVisible(false);
            amount_p_menu.setVisible(false);
            name_c_menu.setVisible(true);
            class_c_menu.setVisible(true);
        }
    }

    public void dropDBclicked() {
        if(!isDBExists)  {
            DBisDropped();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("DropDB");
        alert.setHeaderText("U want drop DB");
        alert.setContentText("continue?");
        Optional<ButtonType> option =  alert.showAndWait();
        if(option.get() == ButtonType.OK) {
            dataBaseController.dropDB();
            if(!dataBaseController.isDBExists()) isDBExists = false;
        }
    }

    public void createDB() {
        if(isDBExists) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error alert");
            alert.setHeaderText("DB already exists");
            alert.setContentText("Drop and create again if u want");
            alert.showAndWait();
            return;
        }
        dataBaseController.createDB();
        if(dataBaseController.isDBExists()) isDBExists = true;
    }


    public void showHelpMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("HELP");
        alert.setHeaderText("some useful inf");
        alert.setContentText("useful inf useful inf useful inf useful inf useful inf useful inf ");
        alert.show();
    }


}
