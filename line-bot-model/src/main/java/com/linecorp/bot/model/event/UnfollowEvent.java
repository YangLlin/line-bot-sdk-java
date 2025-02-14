/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.bot.model.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

/**
 * Event object for when your account is blocked.
 */
@Value
@JsonTypeName("unfollow")
public class UnfollowEvent implements Event {
    /**
     * JSON object which contains the source of the event.
     */
    private final Source source;

    /**
     * Time of the event.
     */
    private final Instant timestamp;

    @JsonCreator
    public UnfollowEvent(
    		@JsonProperty("source")final Source source,
    		@JsonProperty("timestamp")final Instant timestamp) {
        this.source = source;
        this.timestamp = timestamp;
    }
}
