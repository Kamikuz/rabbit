/*
 *    Copyright 2021 FightingGuys Team and khl-sdk-java contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package cn.fightingguys.kaiheila.event.message;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.api.Channel;
import cn.fightingguys.kaiheila.core.action.Operation;
import cn.fightingguys.kaiheila.event.AbstractEvent;
import cn.fightingguys.kaiheila.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class TextMessageEvent extends AbstractEvent {
    public enum Type {
        Group,
        Person;

        public final String value;

        Type() {
            this.value = this.name().toUpperCase();
        }

        public static Type from(String value) {
            for (Type type : values()) {
                if (type.value.toUpperCase().equals(value)) {
                    return type;
                }
            }
            return null;
        }
    }
    public final Type type;
    private final MessageExtra extra;
    public final String messageID;

    public TextMessageEvent(RabbitImpl rabbit, JsonNode node) {
        super(rabbit, node);
        type = Type.from(getEventChannelType());
        messageID = node.get("msg_id").asText();
        this.extra = MessageExtra.buildMessageExtra(rabbit, node);
    }

    protected TextMessageEvent(RabbitImpl rabbit, TextMessageEvent event) {
        super(rabbit, event);
        this.type = event.type;
        this.extra = event.extra;
        this.messageID = event.messageID;
    }

    @Override
    public Operation.ChatOperation action() {
        return new Operation.ChatOperation(getRabbitImpl(),this);
    }

    protected MessageExtra getExtra(){
        return this.extra;
    };

    public PrivateMessageEvent asPrivateMessageEvent(){
        return new PrivateMessageEvent(getRabbitImpl(),this);
    }

    public ChannelMessageEvent asChannelMessageEvent(){
        return new ChannelMessageEvent(getRabbitImpl(),this);
    }

    public Channel getChannel() {
        return getRabbitImpl().getCacheManager().getChannelCache().getElementById(super.getEventTargetId());
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }

    public static class PrivateMessageEvent extends TextMessageEvent {

        public PrivateMessageEvent(RabbitImpl rabbit, TextMessageEvent event) {
            super(rabbit, event);
        }

        @Override
        public MessageExtra.PersonalMessageExtra getExtra() {
            return (MessageExtra.PersonalMessageExtra) super.getExtra();
        }
    }

    public static class ChannelMessageEvent extends TextMessageEvent {

        public ChannelMessageEvent(RabbitImpl rabbit, TextMessageEvent event) {
            super(rabbit, event);
        }

        @Override
        public MessageExtra.ChannelMessageExtra getExtra() {
            return (MessageExtra.ChannelMessageExtra) super.getExtra();
        }
    }
}