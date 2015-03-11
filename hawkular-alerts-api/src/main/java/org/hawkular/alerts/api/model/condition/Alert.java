/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates
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
package org.hawkular.alerts.api.model.condition;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

/**
 * A status of an alert thrown by several matched conditions.
 *
 * @author Jay Shaughnessy
 * @author Lucas Ponce
 */
public class Alert {

    @JsonInclude
    private String triggerId;

    @JsonInclude(Include.NON_EMPTY)
    private List<Set<ConditionEval>> evalSets;

    @JsonInclude
    private long time;

    public Alert(String triggerId, List<Set<ConditionEval>> evalSets) {
        this.triggerId = triggerId;
        this.evalSets = evalSets;
        this.time = System.currentTimeMillis();
    }

    public List<Set<ConditionEval>> getEvalSets() {
        return evalSets;
    }

    public void setEvalSets(List<Set<ConditionEval>> evalSets) {
        this.evalSets = evalSets;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alert alert = (Alert) o;

        if (time != alert.time) return false;
        if (evalSets != null ? !evalSets.equals(alert.evalSets) : alert.evalSets != null) return false;
        if (triggerId != null ? !triggerId.equals(alert.triggerId) : alert.triggerId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = triggerId != null ? triggerId.hashCode() : 0;
        result = 31 * result + (evalSets != null ? evalSets.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Alert [triggerId=" + triggerId + ", " +
                "evals=" + evalSets + ", " +
                "time=" + time + "]";
    }

}
