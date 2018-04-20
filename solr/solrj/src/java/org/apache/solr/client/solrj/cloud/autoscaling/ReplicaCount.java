/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.client.solrj.cloud.autoscaling;

import java.io.IOException;
import java.util.List;

import org.apache.solr.common.MapWriter;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.util.Utils;

class ReplicaCount extends Number implements MapWriter {
  long nrt, tlog, pull;

  public long total() {
    return nrt + tlog + pull;
  }

  @Override
  public int intValue() {
    return (int) total();
  }

  @Override
  public long longValue() {
    return total();
  }

  @Override
  public float floatValue() {
    return total();
  }

  @Override
  public double doubleValue() {
    return total();
  }

  @Override
  public void writeMap(EntryWriter ew) throws IOException {
    ew.put(Replica.Type.NRT.name(), nrt);
    ew.put(Replica.Type.PULL.name(), pull);
    ew.put(Replica.Type.TLOG.name(), tlog);
    ew.put("count", total());
  }

  public Long getVal(Replica.Type type) {
    if (type == null) return total();
    switch (type) {
      case NRT:
        return nrt;
      case PULL:
        return pull;
      case TLOG:
        return tlog;
    }
    return total();
  }

  public void increment(List<ReplicaInfo> infos) {
    if (infos == null) return;
    for (ReplicaInfo info : infos) {
      switch (info.getType()) {
        case NRT:
          nrt++;
          break;
        case PULL:
          pull++;
          break;
        case TLOG:
          tlog++;
          break;
        default:
          nrt++;
      }
    }
  }

  @Override
  public String toString() {
    return Utils.toJSONString(this);
  }
}
