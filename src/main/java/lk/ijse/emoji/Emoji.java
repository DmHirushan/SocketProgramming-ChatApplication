package lk.ijse.emoji;

import com.vdurmont.emoji.EmojiParser;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Emoji extends VBox {
    private javafx.scene.control.ListView<String> listView;

    public ListView <String> getListView(){
        return listView;
    }
    public Emoji(){
        List <String> emojis = new ArrayList<>();
        String [] emojiList ={"&#128515", "&#128526", "&#128514;", "&#128513;", "&#128521;", "&#129300;", "&#128517;", "&#128532;", "&#128580;", "&#128540;", "&#9829;", "&#9851;", "&#128530;",
                "&#128553;","&#9786;","&#128513;","&#128076;","&#128079;","&#128148;","&#128150;","&#128153;",
                "&#128546;","&#128170;","&#129303;","&#128156;","&#128526;","&#128519;","&#127801;","&#129318;",
                "&#127881;","&#128158;","&#9996;","&#10024;","&#129335;","&#128561;","&#128524;","&#127800;",
                "&#128588;","&#128523;","&#127770;","&#127773;","&#128584;","&#128585;","&#128586;"};

        for (String emoji : emojiList){
            emojis.add(EmojiParser.parseToUnicode(emoji));
        }

        listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(emojis));

        HBox hBox = new HBox(listView);
        hBox.setPadding(new Insets(10));

        getChildren().add(hBox);

    }
}
