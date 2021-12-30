package cn.fightingguys.kaiheila.entity.cardmessage;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class CardMessageBuilder {
    private final List<CardMessage> messages = new ArrayList<>();

    public static CardMessageBuilder start() {
        return new CardMessageBuilder();
    }

    public JSONArray build() {
        JSONArray array = new JSONArray();
        for (CardMessage message : messages) {
            array.put(message.toJSON());
        }
        return array;
    }

    public CardMessageBuilder append(CardMessage message) {
        this.messages.add(message);
        return this;
    }
}
