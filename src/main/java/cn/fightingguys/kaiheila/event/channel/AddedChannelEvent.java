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

package cn.fightingguys.kaiheila.event.channel;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.api.Channel;
import cn.fightingguys.kaiheila.api.Guild;
import cn.fightingguys.kaiheila.cache.BaseCache;
import cn.fightingguys.kaiheila.cache.CacheManager;
import cn.fightingguys.kaiheila.entity.ChannelEntity;
import cn.fightingguys.kaiheila.entity.GuildEntity;
import cn.fightingguys.kaiheila.event.AbstractEvent;
import cn.fightingguys.kaiheila.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class AddedChannelEvent extends AbstractEvent {

    public static final String _AcceptType = "added_channel";

    private final String guildId;
    private final String channelId;

    public AddedChannelEvent(RabbitImpl rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        guildId = body.get("guild_id").asText();
        channelId = body.get("id").asText();
    }

    public Guild getGuild() {
        return getRabbitImpl().getCacheManager().getGuildCache().getElementById(guildId);
    }

    public Channel getChannel() {
        return getRabbitImpl().getCacheManager().getChannelCache().getElementById(channelId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        JsonNode node = super.getEventExtraBody(body);
        ChannelEntity entity = getRabbitImpl().getEntitiesBuilder().buildChannelEntityForEvent(node);
        // 更新缓存
        CacheManager cacheManager = getRabbitImpl().getCacheManager();
        GuildEntity guild = cacheManager.getGuildCache().getElementById(guildId);
        guild.getChannels().add(entity.getId());
        ((BaseCache<String, ChannelEntity>) cacheManager.getChannelCache()).updateElementById(entity.getId(), entity);
        return this;
    }
}
