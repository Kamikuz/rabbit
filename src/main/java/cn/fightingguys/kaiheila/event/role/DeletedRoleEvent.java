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

package cn.fightingguys.kaiheila.event.role;

import cn.fightingguys.kaiheila.RabbitImpl;
import cn.fightingguys.kaiheila.api.Role;
import cn.fightingguys.kaiheila.cache.BaseCache;
import cn.fightingguys.kaiheila.cache.CacheManager;
import cn.fightingguys.kaiheila.core.action.Operation;
import cn.fightingguys.kaiheila.entity.GuildEntity;
import cn.fightingguys.kaiheila.entity.RoleEntity;
import cn.fightingguys.kaiheila.event.AbstractEvent;
import cn.fightingguys.kaiheila.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class DeletedRoleEvent extends AbstractEvent {

    public static final String _AcceptType = "deleted_role";

    private final Integer roleId;

    public DeletedRoleEvent(RabbitImpl rabbit, JsonNode node) {
        super(rabbit, node);
        JsonNode body = super.getEventExtraBody(node);
        roleId = body.get("role_id").asInt();
    }

    @Override
    public Operation action() {
        return null;
    }

    public Role getRole() {
        return getRabbitImpl().getCacheManager().getRoleCache().getElementById(roleId);
    }

    @Override
    public IEvent handleSystemEvent(JsonNode body) {
        CacheManager cacheManager = getRabbitImpl().getCacheManager();
        ((BaseCache<Integer, RoleEntity>) cacheManager.getRoleCache()).unloadElementById(roleId);
        BaseCache<String, GuildEntity> guildCache = (BaseCache<String, GuildEntity>) cacheManager.getGuildCache();
        for (GuildEntity guild : guildCache) {
            guild.getRoles().remove(roleId);
        }
        return this;
    }
}
