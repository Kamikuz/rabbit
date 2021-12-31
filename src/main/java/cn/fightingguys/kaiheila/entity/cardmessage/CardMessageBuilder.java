package cn.fightingguys.kaiheila.entity.cardmessage;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class CardMessageBuilder {
    private final JSONArray messages = new JSONArray();

    public static CardMessageBuilder start() {
        return new CardMessageBuilder();
    }

    public JSONArray build() {
        return messages;
    }

    public CardMessageBuilder append(CardMessage message) {
        this.messages.put(message.toJSON());
        return this;
    }

    @Override
    public String toString() {
        return build().toString();
    }
}
