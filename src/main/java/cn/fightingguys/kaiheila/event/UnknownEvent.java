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

package cn.fightingguys.kaiheila.event;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.core.action.Operation;
import com.fasterxml.jackson.databind.JsonNode;

public final class UnknownEvent extends AbstractEvent {

    private final String rawEventData;

    public UnknownEvent(RabbitImpl rabbit, JsonNode node) {
        super(rabbit, node);
        this.rawEventData = node.toString();
    }

    @Override
    public Operation action() {
        return null;
    }

    public String getRawEventData() {
        return rawEventData;
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        return this;
    }
}
