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

package cn.fightingguys.kaiheila.event.guild;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.api.Guild;
import cn.fightingguys.kaiheila.cache.BaseCache;
import cn.fightingguys.kaiheila.core.action.Operation;
import cn.fightingguys.kaiheila.entity.GuildEntity;
import cn.fightingguys.kaiheila.event.AbstractEvent;
import cn.fightingguys.kaiheila.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class UpdatedGuildEvent extends AbstractEvent {

    public static final String _AcceptType = "updated_guild";

    private final String guildId;

    public UpdatedGuildEvent(RabbitImpl rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        guildId = body.get("id").asText();
    }

    @Override
    public Operation action() {
        return null;
    }

    public Guild getGuild() {
        return getRabbitImpl().getCacheManager().getGuildCache().getElementById(guildId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        BaseCache<String, GuildEntity> guildCache = (BaseCache<String, GuildEntity>) getRabbitImpl().getCacheManager().getGuildCache();
        GuildEntity guildEntity = guildCache.getElementById(guildId);
        guildEntity = getRabbitImpl().getEntitiesBuilder().updateGuildEntityForEvent(guildEntity, super.getEventExtraBody(body));
        guildCache.updateElementById(guildId, guildEntity);
        return this;
    }
}
