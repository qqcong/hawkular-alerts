/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.alerts.engine.impl;

import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

/**
 * PreparedStatements need to be prepared only one time for the Datastax driver.  Avoid overhead and warnings by
 * caching the PreparedStatements in one place.
 *
 * @author Jay Shaughnessy
 * @author Lucas Ponce
 */
public class CassStatement {
    private static final String CASSANDRA_KEYSPACE = "hawkular-alerts.cassandra-keyspace";

    private static final String keyspace;

    private static final Map<String, PreparedStatement> statementMap = new HashMap<>();

    public static final String DELETE_ACTION;
    public static final String DELETE_ACTION_HISTORY;
    public static final String DELETE_ACTION_HISTORY_ACTION;
    public static final String DELETE_ACTION_HISTORY_ALERT;
    public static final String DELETE_ACTION_HISTORY_CTIME;
    public static final String DELETE_ACTION_HISTORY_RESULT;
    public static final String DELETE_ACTION_PLUGIN;
    public static final String DELETE_ALERT;
    public static final String DELETE_ALERT_CTIME;
    public static final String DELETE_ALERT_SEVERITY;
    public static final String DELETE_ALERT_STATUS;
    public static final String DELETE_ALERT_TRIGGER;
    public static final String DELETE_CONDITIONS;
    public static final String DELETE_CONDITIONS_MODE;
    public static final String DELETE_DAMPENING_ID;
    public static final String DELETE_DAMPENINGS;
    public static final String DELETE_EVENT;
    public static final String DELETE_EVENT_CATEGORY;
    public static final String DELETE_EVENT_CTIME;
    public static final String DELETE_EVENT_TRIGGER;
    public static final String DELETE_TAG;
    public static final String DELETE_TRIGGER_ACTIONS;
    public static final String DELETE_TRIGGER;

    public static final String INSERT_ACTION;
    public static final String INSERT_ACTION_HISTORY;
    public static final String INSERT_ACTION_HISTORY_ACTION;
    public static final String INSERT_ACTION_HISTORY_ALERT;
    public static final String INSERT_ACTION_HISTORY_CTIME;
    public static final String INSERT_ACTION_HISTORY_RESULT;
    public static final String INSERT_ACTION_PLUGIN;
    public static final String INSERT_ACTION_PLUGIN_DEFAULT_PROPERTIES;
    public static final String INSERT_ALERT;
    public static final String INSERT_ALERT_CTIME;
    public static final String INSERT_ALERT_SEVERITY;
    public static final String INSERT_ALERT_STATUS;
    public static final String INSERT_ALERT_TRIGGER;
    public static final String INSERT_CONDITION_AVAILABILITY;
    public static final String INSERT_CONDITION_COMPARE;
    public static final String INSERT_CONDITION_EVENT;
    public static final String INSERT_CONDITION_EXTERNAL;
    public static final String INSERT_CONDITION_RATE;
    public static final String INSERT_CONDITION_STRING;
    public static final String INSERT_CONDITION_THRESHOLD;
    public static final String INSERT_CONDITION_THRESHOLD_RANGE;
    public static final String INSERT_DAMPENING;
    public static final String INSERT_EVENT;
    public static final String INSERT_EVENT_CATEGORY;
    public static final String INSERT_EVENT_CTIME;
    public static final String INSERT_EVENT_TRIGGER;
    public static final String INSERT_TAG;
    public static final String INSERT_TRIGGER;
    public static final String INSERT_TRIGGER_ACTIONS;

    public static final String SELECT_ACTION;
    public static final String SELECT_ACTION_HISTORY;
    public static final String SELECT_ACTION_HISTORY_ACTION_ID;
    public static final String SELECT_ACTION_HISTORY_ACTION_PLUGIN;
    public static final String SELECT_ACTION_HISTORY_ALERT_ID;
    public static final String SELECT_ACTION_HISTORY_BY_TENANT;
    public static final String SELECT_ACTION_HISTORY_CTIME_END;
    public static final String SELECT_ACTION_HISTORY_CTIME_START;
    public static final String SELECT_ACTION_HISTORY_CTIME_START_END;
    public static final String SELECT_ACTION_HISTORY_RESULT;
    public static final String SELECT_ACTIONS_ALL;
    public static final String SELECT_ACTIONS_BY_TENANT;
    public static final String SELECT_ACTION_PLUGIN;
    public static final String SELECT_ACTION_PLUGIN_DEFAULT_PROPERTIES;
    public static final String SELECT_ACTION_PLUGINS;
    public static final String SELECT_ACTIONS_PLUGIN;
    public static final String SELECT_ALERT;
    public static final String SELECT_ALERT_CTIME_END;
    public static final String SELECT_ALERT_CTIME_START;
    public static final String SELECT_ALERT_CTIME_START_END;
    public static final String SELECT_ALERT_STATUS;
    public static final String SELECT_ALERT_SEVERITY;
    public static final String SELECT_ALERT_TRIGGER;
    public static final String SELECT_ALERTS_BY_TENANT;
    public static final String SELECT_CONDITION_ID;
    public static final String SELECT_CONDITIONS_ALL;
    public static final String SELECT_CONDITIONS_BY_TENANT;
    public static final String SELECT_DAMPENING_ID;
    public static final String SELECT_DAMPENINGS_ALL;
    public static final String SELECT_DAMPENINGS_BY_TENANT;
    public static final String SELECT_EVENT;
    public static final String SELECT_EVENT_CATEGORY;
    public static final String SELECT_EVENT_CTIME_END;
    public static final String SELECT_EVENT_CTIME_START;
    public static final String SELECT_EVENT_CTIME_START_END;
    public static final String SELECT_EVENT_TRIGGER;
    public static final String SELECT_EVENTS_BY_TENANT;
    //public static final String SELECT_EVENTS_BY_PARTITION;
    // public static final String SELECT_PARTITIONS_EVENTS;
    public static final String SELECT_PARTITIONS_TRIGGERS;
    public static final String SELECT_TAGS_BY_NAME;
    public static final String SELECT_TAGS_BY_NAME_AND_VALUE;
    public static final String SELECT_TRIGGER;
    public static final String SELECT_TRIGGER_ACTIONS;
    public static final String SELECT_TRIGGER_CONDITIONS;
    public static final String SELECT_TRIGGER_CONDITIONS_TRIGGER_MODE;
    public static final String SELECT_TRIGGER_DAMPENINGS;
    public static final String SELECT_TRIGGER_DAMPENINGS_MODE;
    public static final String SELECT_TRIGGERS_ALL;
    public static final String SELECT_TRIGGERS_TENANT;

    public static final String UPDATE_ACTION;
    public static final String UPDATE_ACTION_HISTORY;
    public static final String UPDATE_ACTION_PLUGIN;
    public static final String UPDATE_ACTION_PLUGIN_DEFAULT_PROPERTIES;
    public static final String UPDATE_ALERT;
    public static final String UPDATE_DAMPENING_ID;
    public static final String UPDATE_EVENT;
    public static final String UPDATE_TRIGGER;
    public static final String UPDATE_TRIGGER_ENABLED;

    static {
        keyspace = AlertProperties.getProperty(CASSANDRA_KEYSPACE, "hawkular_alerts");

        DELETE_ACTION = "DELETE FROM " + keyspace + ".actions "
                + "WHERE tenantId = ? AND actionPlugin = ? AND actionId = ? ";

        DELETE_ACTION_HISTORY = "DELETE FROM " + keyspace + ".actions_history " +
                "WHERE tenantId = ? AND actionPlugin = ? AND actionId = ? AND alertId = ? AND ctime = ?";

        DELETE_ACTION_HISTORY_ACTION = "DELETE FROM " + keyspace + ".actions_history_actions " +
                "WHERE tenantId = ? AND actionId = ? AND actionPlugin = ? AND alertId = ? AND ctime = ?";

        DELETE_ACTION_HISTORY_ALERT = "DELETE FROM " + keyspace + ".actions_history_alerts " +
                "WHERE tenantId = ? AND alertId = ? AND actionPlugin = ? AND actionId = ? AND ctime = ?";

        DELETE_ACTION_HISTORY_CTIME = "DELETE FROM " + keyspace + ".actions_history_ctimes " +
                "WHERE tenantId = ? AND ctime = ? AND actionPlugin = ? AND actionId = ? AND alertId = ?";

        DELETE_ACTION_HISTORY_RESULT = "DELETE FROM " + keyspace + ".actions_history_results " +
                "WHERE tenantId = ? AND result = ? AND actionPlugin = ? AND actionId = ? AND alertId = ? AND ctime = ?";

        DELETE_ACTION_PLUGIN = "DELETE FROM " + keyspace + ".action_plugins WHERE actionPlugin = ? ";

        DELETE_ALERT = "DELETE FROM " + keyspace + ".alerts " + "WHERE tenantId = ? AND alertId = ? ";

        DELETE_ALERT_CTIME = "DELETE FROM " + keyspace + ".alerts_ctimes "
                + "WHERE tenantId = ? AND ctime = ? AND alertId = ? ";

        DELETE_ALERT_SEVERITY = "DELETE FROM " + keyspace + ".alerts_severities "
                + "WHERE tenantId = ? AND severity = ? AND alertId = ? ";

        DELETE_ALERT_STATUS = "DELETE FROM " + keyspace + ".alerts_statuses "
                + "WHERE tenantId = ? AND status = ? AND alertId = ? ";

        DELETE_ALERT_TRIGGER = "DELETE FROM " + keyspace + ".alerts_triggers "
                + "WHERE tenantId = ? AND triggerId = ? AND alertId = ? ";

        DELETE_CONDITIONS = "DELETE FROM " + keyspace + ".conditions " + "WHERE tenantId = ? AND triggerId = ? ";

        DELETE_CONDITIONS_MODE = "DELETE FROM " + keyspace + ".conditions "
                + "WHERE tenantId = ? AND triggerId = ? AND triggerMode = ? ";

        DELETE_DAMPENING_ID = "DELETE FROM " + keyspace + ".dampenings "
                + "WHERE tenantId = ? AND triggerId = ? AND triggerMode = ? AND dampeningId = ? ";

        DELETE_DAMPENINGS = "DELETE FROM " + keyspace + ".dampenings " + "WHERE tenantId = ? AND triggerId = ? ";

        DELETE_EVENT = "DELETE FROM " + keyspace + ".events " + "WHERE tenantId = ? AND id = ? ";

        DELETE_EVENT_CTIME = "DELETE FROM " + keyspace + ".events_ctimes "
                + "WHERE tenantId = ? AND ctime = ? AND id = ? ";

        DELETE_EVENT_CATEGORY = "DELETE FROM " + keyspace + ".events_categories "
                + "WHERE tenantId = ? AND category = ? AND id = ? ";

        DELETE_EVENT_TRIGGER = "DELETE FROM " + keyspace + ".events_triggers "
                + "WHERE tenantId = ? AND triggerId = ? AND id = ? ";

        DELETE_TAG = "DELETE FROM " + keyspace + ".tags "
                + "WHERE tenantId = ? AND type = ? AND name = ? and value = ? AND id = ?";

        DELETE_TRIGGER_ACTIONS = "DELETE FROM " + keyspace + ".triggers_actions "
                + "WHERE tenantId = ? AND triggerId = ? ";

        DELETE_TRIGGER = "DELETE FROM " + keyspace + ".triggers " + "WHERE tenantId = ? AND id = ? ";

        INSERT_ACTION = "INSERT INTO " + keyspace + ".actions "
                + "(tenantId, actionPlugin, actionId, properties) VALUES (?, ?, ?, ?) ";

        INSERT_ACTION_HISTORY = "INSERT INTO " + keyspace + ".actions_history "
                + "(tenantId, actionPlugin, actionId, alertId, ctime, payload) VALUES (?, ?, ?, ?, ?, ?) " +
                "IF NOT EXISTS";

        INSERT_ACTION_HISTORY_ACTION = "INSERT INTO " + keyspace + ".actions_history_actions "
                + "(tenantId, actionId, actionPlugin, alertId, ctime) VALUES (?, ?, ?, ?, ?) " +
                "IF NOT EXISTS";

        INSERT_ACTION_HISTORY_ALERT = "INSERT INTO " + keyspace + ".actions_history_alerts "
                + "(tenantId, alertId, actionPlugin, actionId, ctime) VALUES (?, ?, ?, ?, ?) " +
                "IF NOT EXISTS";

        INSERT_ACTION_HISTORY_CTIME = "INSERT INTO " + keyspace + ".actions_history_ctimes "
                + "(tenantId, ctime, actionPlugin, actionId, alertId) VALUES (?, ?, ?, ?, ?) " +
                "IF NOT EXISTS";

        INSERT_ACTION_HISTORY_RESULT = "INSERT INTO " + keyspace + ".actions_history_results "
                + "(tenantId, result, actionPlugin, actionId, alertId, ctime) VALUES (?, ?, ?, ?, ?, ?) " +
                "IF NOT EXISTS";

        INSERT_ACTION_PLUGIN = "INSERT INTO " + keyspace + ".action_plugins "
                + "(actionPlugin, properties) VALUES (?, ?) ";

        INSERT_ACTION_PLUGIN_DEFAULT_PROPERTIES = "INSERT INTO " + keyspace + ".action_plugins "
                + "(actionPlugin, properties, defaultProperties) VALUES (?, ?, ?) ";

        INSERT_ALERT = "INSERT INTO " + keyspace + ".alerts " + "(tenantId, alertId, payload) VALUES (?, ?, ?) ";

        INSERT_ALERT_CTIME = "INSERT INTO " + keyspace + ".alerts_ctimes "
                + "(tenantId, alertId, ctime) VALUES (?, ?, ?) ";

        INSERT_ALERT_SEVERITY = "INSERT INTO " + keyspace + ".alerts_severities "
                + "(tenantId, alertId, severity) VALUES (?, ?, ?) ";

        INSERT_ALERT_STATUS = "INSERT INTO " + keyspace + ".alerts_statuses "
                + "(tenantId, alertId, status) VALUES (?, ?, ?) ";

        INSERT_ALERT_TRIGGER = "INSERT INTO " + keyspace + ".alerts_triggers "
                + "(tenantId, alertId, triggerId) VALUES (?, ?, ?) ";

        INSERT_CONDITION_AVAILABILITY = "INSERT INTO " + keyspace + ".conditions "
                + "(tenantId, triggerId, triggerMode, type, context, conditionSetSize, conditionSetIndex, " +
                "conditionId, dataId, operator) VALUES (?, ?, ?, 'AVAILABILITY', ?, ?, ?, ?, ?, ?) ";

        INSERT_CONDITION_COMPARE = "INSERT INTO " + keyspace + ".conditions "
                + "(tenantId, triggerId, triggerMode, type, context, conditionSetSize, conditionSetIndex, " +
                "conditionId, dataId, operator, data2Id, data2Multiplier) " +
                "VALUES (?, ?, ?, 'COMPARE', ?, ?, ?, ?, ?, ?, ?, ?) ";

        INSERT_CONDITION_EVENT = "INSERT INTO " + keyspace + ".conditions "
                + "(tenantId, triggerId, triggerMode, type, context, conditionSetSize, conditionSetIndex, " +
                "conditionId, dataId, pattern) VALUES (?, ?, ?, 'EVENT', ?, ?, ?, ?, ?, ?) ";

        INSERT_CONDITION_EXTERNAL = "INSERT INTO " + keyspace + ".conditions "
                + "(tenantId, triggerId, triggerMode, type, context, conditionSetSize, conditionSetIndex, " +
                "conditionId, dataId, operator, pattern) VALUES (?, ?, ?, 'EXTERNAL', ?, ?, ?, ?, ?, ?, ?) ";

        INSERT_CONDITION_RATE = "INSERT INTO " + keyspace + ".conditions "
                + "(tenantId, triggerId, triggerMode, type, context, conditionSetSize, conditionSetIndex, "
                + "conditionId, dataId, direction, period, operator, threshold) "
                + "VALUES (?, ?, ?, 'RATE', ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        INSERT_CONDITION_STRING = "INSERT INTO " + keyspace + ".conditions "
                + "(tenantId, triggerId, triggerMode, type, context, conditionSetSize, conditionSetIndex, " +
                "conditionId, dataId, operator, pattern, ignoreCase) " +
                "VALUES (?, ?, ?, 'STRING', ?, ?, ?, ?, ?, ?, ?, ?) ";

        INSERT_CONDITION_THRESHOLD = "INSERT INTO " + keyspace + ".conditions "
                + "(tenantId, triggerId, triggerMode, type, context, conditionSetSize, conditionSetIndex, " +
                "conditionId, dataId, operator, threshold) VALUES (?, ?, ?, 'THRESHOLD', ?, ?, ?, ?, ?, ?, ?) ";

        INSERT_CONDITION_THRESHOLD_RANGE = "INSERT INTO " + keyspace + ".conditions "
                + "(tenantId, triggerId, triggerMode, type, context, conditionSetSize, conditionSetIndex, "
                + "conditionId, dataId, operatorLow, operatorHigh, thresholdLow, thresholdHigh, inRange) "
                + "VALUES (?, ?, ?, 'RANGE', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        INSERT_DAMPENING = "INSERT INTO " + keyspace + ".dampenings "
                + "(tenantId, triggerId, triggerMode, type, evalTrueSetting, evalTotalSetting, evalTimeSetting, "
                + "dampeningId) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";

        INSERT_EVENT = "INSERT INTO " + keyspace + ".events "
                + "(tenantId, id, payload) VALUES (?, ?, ?) ";

        INSERT_EVENT_CTIME = "INSERT INTO " + keyspace + ".events_ctimes "
                + "(tenantId, ctime, id) VALUES (?, ?, ?) ";

        INSERT_EVENT_CATEGORY = "INSERT INTO " + keyspace + ".events_categories "
                + "(tenantId, category, id) VALUES (?, ?, ?) ";

        INSERT_EVENT_TRIGGER = "INSERT INTO " + keyspace + ".events_triggers "
                + "(tenantId, triggerId, id) VALUES (?, ?, ?) ";

        INSERT_TAG = "INSERT INTO " + keyspace + ".tags "
                + "(tenantId, type, name, value, id) VALUES (?, ?, ?, ?, ?) ";

        INSERT_TRIGGER = "INSERT INTO " + keyspace + ".triggers " +
                "(tenantId, id, autoDisable, autoEnable, autoResolve, autoResolveAlerts, autoResolveMatch, "
                + "context, description, enabled, eventCategory, eventText, eventType, firingMatch, memberOf, name, "
                + "severity, source, tags, type) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        INSERT_TRIGGER_ACTIONS = "INSERT INTO " + keyspace + ".triggers_actions "
                + "(tenantId, triggerId, actionPlugin, actions) VALUES (?, ?, ?, ?) ";

        SELECT_ACTION = "SELECT properties FROM " + keyspace + ".actions "
                + "WHERE tenantId = ? AND actionPlugin = ? AND actionId = ? ";

        SELECT_ACTION_HISTORY = "SELECT payload FROM " + keyspace + ".actions_history " +
                "WHERE tenantId = ? AND actionPlugin = ? AND actionId = ? AND alertId = ? and ctime = ?";

        SELECT_ACTION_HISTORY_ACTION_ID = "SELECT tenantId, actionPlugin, actionId, alertId, ctime FROM " +
                keyspace + ".actions_history_actions WHERE tenantId = ? AND actionId = ?";

        SELECT_ACTION_HISTORY_ACTION_PLUGIN = "SELECT tenantId, actionPlugin, actionId, alertId, ctime FROM " +
                keyspace + ".actions_history WHERE tenantId = ? AND actionPlugin = ?";

        SELECT_ACTION_HISTORY_ALERT_ID = "SELECT tenantId, actionPlugin, actionId, alertId, ctime FROM " +
                keyspace + ".actions_history_alerts WHERE tenantId = ? AND alertId = ?";

        SELECT_ACTION_HISTORY_BY_TENANT = "SELECT payload FROM " + keyspace + ".actions_history " +
                "WHERE tenantId = ?";

        SELECT_ACTION_HISTORY_CTIME_END = "SELECT tenantId, actionPlugin, actionId, alertId, ctime FROM " + keyspace +
                ".actions_history_ctimes WHERE tenantId = ? AND ctime <= ?";

        SELECT_ACTION_HISTORY_CTIME_START = "SELECT tenantId, actionPlugin, actionId, alertId, ctime FROM " + keyspace +
                ".actions_history_ctimes WHERE tenantId = ? AND ctime >= ?";

        SELECT_ACTION_HISTORY_CTIME_START_END = "SELECT tenantId, actionPlugin, actionId, alertId, ctime FROM " +
                keyspace + ".actions_history_ctimes WHERE tenantId = ? AND ctime >= ? AND ctime <= ?";

        SELECT_ACTION_HISTORY_RESULT = "SELECT tenantId, actionPlugin, actionId, alertId, ctime FROM " +
                keyspace + ".actions_history_results WHERE tenantId = ? AND result = ?";

        SELECT_ACTIONS_ALL = "SELECT tenantId, actionPlugin, actionId " + "FROM " + keyspace + ".actions ";

        SELECT_ACTIONS_BY_TENANT = "SELECT actionPlugin, actionId " + "FROM " + keyspace + ".actions "
                + "WHERE tenantId = ? ";

        SELECT_ACTION_PLUGIN = "SELECT properties FROM " + keyspace + ".action_plugins " + "WHERE actionPlugin = ? ";

        SELECT_ACTION_PLUGIN_DEFAULT_PROPERTIES = "SELECT defaultProperties FROM " + keyspace + ".action_plugins "
                + "WHERE actionPlugin = ? ";

        SELECT_ACTION_PLUGINS = "SELECT actionPlugin FROM " + keyspace + ".action_plugins";

        SELECT_ACTIONS_PLUGIN = "SELECT actionId FROM " + keyspace + ".actions "
                + "WHERE tenantId = ? AND actionPlugin = ? ";

        SELECT_ALERT = "SELECT payload FROM " + keyspace + ".alerts "
                + "WHERE tenantId = ? AND alertId = ? ";

        SELECT_ALERT_CTIME_END = "SELECT alertId FROM " + keyspace + ".alerts_ctimes "
                + "WHERE tenantId = ? AND ctime <= ? ";

        SELECT_ALERT_CTIME_START = "SELECT alertId FROM " + keyspace + ".alerts_ctimes "
                + "WHERE tenantId = ? AND ctime >= ? ";

        SELECT_ALERT_CTIME_START_END = "SELECT alertId FROM " + keyspace + ".alerts_ctimes "
                + "WHERE tenantId = ? AND ctime >= ? AND ctime <= ? ";

        SELECT_ALERT_SEVERITY = "SELECT alertId FROM " + keyspace + ".alerts_severities "
                + "WHERE tenantId = ? AND severity = ? ";

        SELECT_ALERT_STATUS = "SELECT alertId FROM " + keyspace + ".alerts_statuses "
                + "WHERE tenantId = ? AND status = ? ";

        SELECT_ALERTS_BY_TENANT = "SELECT payload FROM " + keyspace + ".alerts " + "WHERE tenantId = ? ";

        SELECT_ALERT_TRIGGER = "SELECT alertId FROM " + keyspace + ".alerts_triggers "
                + "WHERE tenantId = ? AND triggerId = ? ";

        SELECT_CONDITION_ID = "SELECT triggerId, triggerMode, type, conditionSetSize, "
                + "conditionSetIndex, conditionId, dataId, operator, data2Id, data2Multiplier, pattern, "
                + "ignoreCase, threshold, operatorLow, operatorHigh, thresholdLow, thresholdHigh, inRange, "
                + "direction, period, tenantId, context "
                + "FROM " + keyspace + ".conditions "
                + "WHERE tenantId = ? AND conditionId = ? ";

        SELECT_CONDITIONS_ALL = "SELECT triggerId, triggerMode, type, conditionSetSize, "
                + "conditionSetIndex, conditionId, dataId, operator, data2Id, data2Multiplier, pattern, "
                + "ignoreCase, threshold, operatorLow, operatorHigh, thresholdLow, thresholdHigh, inRange, "
                + "direction, period, tenantId, context "
                + "FROM " + keyspace + ".conditions ";

        SELECT_CONDITIONS_BY_TENANT = "SELECT triggerId, triggerMode, type, conditionSetSize, "
                + "conditionSetIndex, conditionId, dataId, operator, data2Id, data2Multiplier, pattern, "
                + "ignoreCase, threshold, operatorLow, operatorHigh, thresholdLow, thresholdHigh, inRange, "
                + "direction, period, tenantId, context "
                + "FROM " + keyspace + ".conditions "
                + "WHERE tenantId = ? ";

        SELECT_DAMPENING_ID = "SELECT triggerId, triggerMode, type, evalTrueSetting, "
                + "evalTotalSetting, evalTimeSetting, dampeningId, tenantId "
                + "FROM " + keyspace + ".dampenings "
                + "WHERE tenantId = ? AND dampeningId = ? ";

        SELECT_DAMPENINGS_ALL = "SELECT tenantId, triggerId, triggerMode, type, evalTrueSetting, "
                + "evalTotalSetting, evalTimeSetting, dampeningId "
                + "FROM " + keyspace + ".dampenings ";

        SELECT_DAMPENINGS_BY_TENANT = "SELECT tenantId, triggerId, triggerMode, type, " + "evalTrueSetting, "
                + "evalTotalSetting, evalTimeSetting, dampeningId "
                + "FROM " + keyspace + ".dampenings "
                + "WHERE tenantId = ? ";

        SELECT_EVENT = "SELECT payload FROM " + keyspace + ".events "
                + "WHERE tenantId = ? AND  id = ? ";

        SELECT_EVENT_CATEGORY = "SELECT id FROM " + keyspace + ".events_categories "
                + "WHERE tenantId = ? AND category = ? ";

        SELECT_EVENT_CTIME_END = "SELECT id FROM " + keyspace + ".events_ctimes "
                + "WHERE tenantId = ? AND ctime <= ? ";

        SELECT_EVENT_CTIME_START = "SELECT id FROM " + keyspace + ".events_ctimes "
                + "WHERE tenantId = ? AND ctime >= ? ";

        SELECT_EVENT_CTIME_START_END = "SELECT id FROM " + keyspace + ".events_ctimes "
                + "WHERE tenantId = ? AND ctime >= ? AND ctime <= ? ";

        SELECT_EVENT_TRIGGER = "SELECT id FROM " + keyspace + ".events_triggers "
                + "WHERE tenantId = ? AND triggerId = ? ";

        //SELECT_EVENTS_BY_PARTITION = "SELECT payload FROM " + keyspace + ".events "
        //        + "WHERE tenantId = ? AND category = ? ";

        SELECT_EVENTS_BY_TENANT = "SELECT payload FROM " + keyspace + ".events " + "WHERE tenantId = ? ";

        // This is for use as a pre-query to gather all partitions to be subsequently queried. If the
        // partition key changes this should also change.
        // SELECT_PARTITIONS_EVENTS = "SELECT DISTINCT tenantid, category FROM " + keyspace + ".events ";

        // This is for use as a pre-query to gather all partitions to be subsequently queried. If the
        // partition key changes this should also change.
        SELECT_PARTITIONS_TRIGGERS = "SELECT DISTINCT tenantid FROM " + keyspace + ".triggers ";

        SELECT_TAGS_BY_NAME = "SELECT tenantId, value, id "
                + "FROM " + keyspace + ".tags "
                + "WHERE tenantId = ? AND type = ? and name = ? ";

        SELECT_TAGS_BY_NAME_AND_VALUE = "SELECT tenantId, id "
                + "FROM " + keyspace + ".tags "
                + "WHERE tenantId = ? AND type = ? and name = ? AND value = ? ";

        SELECT_TRIGGER = "SELECT tenantId, id, autoDisable, autoEnable, autoResolve, autoResolveAlerts, "
                + "autoResolveMatch, context, description, enabled, eventCategory, eventText, eventType, "
                + "firingMatch, memberOf, name, severity, source, tags, type "
                + "FROM " + keyspace + ".triggers "
                + "WHERE tenantId = ? AND id = ? ";

        SELECT_TRIGGER_ACTIONS = "SELECT tenantId, triggerId, actionPlugin, actions "
                + "FROM " + keyspace + ".triggers_actions "
                + "WHERE tenantId = ? AND triggerId = ? ";

        SELECT_TRIGGER_CONDITIONS = "SELECT triggerId, triggerMode, type, conditionSetSize, "
                + "conditionSetIndex, conditionId, dataId, operator, data2Id, data2Multiplier, pattern, "
                + "ignoreCase, threshold, operatorLow, operatorHigh, thresholdLow, thresholdHigh, inRange, "
                + "direction, period, tenantId, context "
                + "FROM " + keyspace + ".conditions "
                + "WHERE tenantId = ? AND triggerId = ?";

        SELECT_TRIGGER_CONDITIONS_TRIGGER_MODE = "SELECT triggerId, triggerMode, type, conditionSetSize, "
                + "conditionSetIndex, conditionId, dataId, operator, data2Id, data2Multiplier, pattern, ignoreCase, "
                + "threshold, operatorLow, operatorHigh, thresholdLow, thresholdHigh, inRange, "
                + "direction, period, tenantId, context "
                + "FROM " + keyspace + ".conditions "
                + "WHERE tenantId = ? AND triggerId = ? AND triggerMode = ? ";

        SELECT_TRIGGER_DAMPENINGS = "SELECT tenantId, triggerId, triggerMode, type, "
                + "evalTrueSetting, evalTotalSetting, evalTimeSetting, dampeningId "
                + "FROM " + keyspace + ".dampenings "
                + "WHERE tenantId = ? AND triggerId = ? ";

        SELECT_TRIGGER_DAMPENINGS_MODE = "SELECT tenantId, triggerId, triggerMode, type, "
                + "evalTrueSetting, evalTotalSetting, evalTimeSetting, dampeningId "
                + "FROM " + keyspace + ".dampenings "
                + "WHERE tenantId = ? AND triggerId = ? and triggerMode = ? ";

        SELECT_TRIGGERS_ALL = "SELECT tenantId, id, autoDisable, autoEnable, autoResolve, autoResolveAlerts, "
                + "autoResolveMatch, context, description, enabled, eventCategory, eventText, eventType, "
                + "firingMatch, memberOf, name, severity, source, tags, type "
                + "FROM " + keyspace + ".triggers ";

        SELECT_TRIGGERS_TENANT = "SELECT tenantId, id, autoDisable, autoEnable, autoResolve, autoResolveAlerts, "
                + "autoResolveMatch, context, description, enabled, eventCategory, eventText, eventType, "
                + "firingMatch, memberOf, name, severity, source, tags, type "
                + "FROM " + keyspace + ".triggers WHERE tenantId = ? ";

        UPDATE_ACTION = "UPDATE " + keyspace + ".actions SET properties = ? "
                + "WHERE tenantId = ? AND actionPlugin = ? AND actionId = ? ";

        UPDATE_ACTION_HISTORY = "UPDATE " + keyspace + ".actions_history " +
                "SET payload = ? " +
                "WHERE tenantId = ? AND actionPlugin = ? AND actionId = ? AND alertId = ? AND ctime = ?";

        UPDATE_ACTION_PLUGIN = "UPDATE " + keyspace + ".action_plugins SET properties = ? WHERE actionPlugin = ? ";

        UPDATE_ACTION_PLUGIN_DEFAULT_PROPERTIES = "UPDATE " + keyspace + ".action_plugins " +
                "SET properties = ?, defaultProperties = ? WHERE actionPlugin = ? ";

        UPDATE_ALERT = "UPDATE " + keyspace + ".alerts SET payload = ? WHERE tenantId = ? AND alertId = ? ";

        UPDATE_DAMPENING_ID = "UPDATE " + keyspace + ".dampenings "
                + "SET type = ?, evalTrueSetting = ?, evalTotalSetting = ?, evalTimeSetting = ? "
                + "WHERE tenantId = ? AND triggerId = ? AND triggerMode = ? AND dampeningId = ? ";

        UPDATE_EVENT = "UPDATE " + keyspace + ".events SET payload = ? WHERE tenantId = ? AND id = ? ";

        UPDATE_TRIGGER = "UPDATE " + keyspace + ".triggers "
                + "SET autoDisable = ?, autoEnable = ?, autoResolve = ?, autoResolveAlerts = ?, autoResolveMatch = ?, "
                + "context = ?, description = ?,  enabled = ?, eventCategory = ?, eventText = ?, firingMatch = ?, "
                + "memberOf = ?, name = ?, severity = ?, source = ?, tags = ?, type = ? "
                + "WHERE tenantId = ? AND id = ? ";

        UPDATE_TRIGGER_ENABLED = "UPDATE " + keyspace + ".triggers "
                + "SET enabled = ? WHERE tenantId = ? AND id = ? ";

    }

    public static synchronized PreparedStatement get(Session session, String statement) {
        PreparedStatement result = statementMap.get(statement);
        if (null == result) {
            result = session.prepare(statement);
            statementMap.put(statement, result);
        }
        return result;
    }
}
