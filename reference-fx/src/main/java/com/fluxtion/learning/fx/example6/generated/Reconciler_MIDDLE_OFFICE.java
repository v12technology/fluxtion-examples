/* 
 * Copyright (C) 2017 V12 Technology Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.fluxtion.learning.fx.example6.generated;

import com.fluxtion.learning.fx.example6.reconciler.nodes.TradeAcknowledgementAuditor;
import com.fluxtion.api.annotations.Initialise;
import java.util.ArrayList;
import com.fluxtion.learning.fx.example6.reconciler.events.TradeAcknowledgement;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.fluxtion.fx.node.biascheck.TimedNotifier;
import java.util.ArrayDeque;
import com.fluxtion.learning.fx.example6.reconciler.nodes.TradeReconciler;
import com.fluxtion.learning.fx.example6.reconciler.helpers.ReconcileStatus;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.EventHandler;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.fluxtion.api.annotations.OnParentUpdate;
import static com.fluxtion.learning.fx.example6.reconciler.helpers.ReconcileStatus.Status.*;

/**
 * Fluxtion generated TradeReconciler id:MIDDLE_OFFICE
 * Acknowledgements must be received from all mandatory venues and from any of the one of venues
 * for a trade to have RECONCILED status.
 *<ul>
 * <li>Mandatory acknowledging venues: "MiddleOffice_efx"</li>
 * <li>One of acknowledging venues: "sdp", "LD_4_EBS", "dcln_1_reuters", "NY_3_FXALL" </li>
 * </ul>
 * @author greg higgins (greg.higgins@v12technology.com)
 */
public class  Reconciler_MIDDLE_OFFICE extends TradeReconciler<Reconciler_MIDDLE_OFFICE.ReconcileRecord>{

    @EventHandler(filterString = "MiddleOffice_efx")
    public void tradeAckFrom_MiddleOffice_efx (TradeAcknowledgement acknowledgement){
        currentRecord = getRecord(acknowledgement);
        currentRecord.time_MiddleOffice_efx = acknowledgement.time;
        if (currentRecord.matched()) {
            id2Reconcile.remove(currentRecord.id());
            currentRecord.status = RECONCILED;
            reconciled++;
            reconciling--;
        }
    }

    @EventHandler(filterString = "sdp")
    public void tradeAckFrom_sdp (TradeAcknowledgement acknowledgement){
        currentRecord = getRecord(acknowledgement);
        currentRecord.time_sdp = acknowledgement.time;
        if (currentRecord.matched()) {
            id2Reconcile.remove(currentRecord.id());
            currentRecord.status = RECONCILED;
            reconciled++;
            reconciling--;
        }
    }

    @EventHandler(filterString = "LD_4_EBS")
    public void tradeAckFrom_LD_4_EBS (TradeAcknowledgement acknowledgement){
        currentRecord = getRecord(acknowledgement);
        currentRecord.time_LD_4_EBS = acknowledgement.time;
        if (currentRecord.matched()) {
            id2Reconcile.remove(currentRecord.id());
            currentRecord.status = RECONCILED;
            reconciled++;
            reconciling--;
        }
    }

    @EventHandler(filterString = "dcln_1_reuters")
    public void tradeAckFrom_dcln_1_reuters (TradeAcknowledgement acknowledgement){
        currentRecord = getRecord(acknowledgement);
        currentRecord.time_dcln_1_reuters = acknowledgement.time;
        if (currentRecord.matched()) {
            id2Reconcile.remove(currentRecord.id());
            currentRecord.status = RECONCILED;
            reconciled++;
            reconciling--;
        }
    }

    @EventHandler(filterString = "NY_3_FXALL")
    public void tradeAckFrom_NY_3_FXALL (TradeAcknowledgement acknowledgement){
        currentRecord = getRecord(acknowledgement);
        currentRecord.time_NY_3_FXALL = acknowledgement.time;
        if (currentRecord.matched()) {
            id2Reconcile.remove(currentRecord.id());
            currentRecord.status = RECONCILED;
            reconciled++;
            reconciling--;
        }
    }


    private ReconcileRecord getRecord(TradeAcknowledgement acknowledgement) {
        ReconcileRecord record = id2Reconcile.get(acknowledgement.tradeId);
        if (record == null) {
            record = new ReconcileRecord();
            record.firstReceivedTime = acknowledgement.time;
            record.tradeId = acknowledgement.tradeId;
            id2Reconcile.put(acknowledgement.tradeId, record);
            reconciling++;
        }
        return record;
    }

    public static class ReconcileRecord implements ReconcileStatus<Integer>{

        private static final String[] VENUES = new String[]{"MiddleOffice_efx"};
        private static final String[] VENUES_ONE_OF = new String[]{"sdp", "LD_4_EBS", "dcln_1_reuters", "NY_3_FXALL"};
        Status status = RECONCILING;;
        int tradeId;
        long firstReceivedTime = -1;
        long time_MiddleOffice_efx = -1;
        long time_sdp = -1;
        long time_LD_4_EBS = -1;
        long time_dcln_1_reuters = -1;
        long time_NY_3_FXALL = -1;

        @Override
        public boolean matched(){
            return time_MiddleOffice_efx > 0 & ( time_sdp > 0 | time_LD_4_EBS > 0 | time_dcln_1_reuters > 0 | time_NY_3_FXALL > 0);
        }

        @Override
        public boolean expired(long currentTime, int expiryTimeout){
            return !matched()  & ((currentTime - firstReceivedTime) > expiryTimeout);
        }

        @Override
        public void setStatus(Status status){
            this.status = status;
        }

        @Override
        public Integer id() {
            return tradeId;
        }

        @Override
        public String[] venues(){
            return VENUES;
        }

        @Override
        public String[] oneOfVenues(){
            return VENUES_ONE_OF;
        }

        void reset(){
            status = RECONCILING;
            time_MiddleOffice_efx = -1;
            time_sdp = -1;
            time_LD_4_EBS = -1;
            time_dcln_1_reuters = -1;
            time_NY_3_FXALL = -1;
        }

        @Override
        public Status status() {
            return status;
        }

        public void appendAsJson(StringBuilder builder){
            builder.append('{')
                    .append("\"tradeId\": ").append(tradeId)
                    .append(", \"status\": \"").append(status.name()).append("\"")
                    .append(", \"acks\": [")
                    .append("{")
                    .append("\"venue\": \"MiddleOffice_efx\"")
                    .append(", \"ackTime\": ").append(time_MiddleOffice_efx)
                    .append("}, ")
                    .append("{")
                    .append("\"venue\": \"sdp\"")
                    .append(", \"ackTime\": ").append(time_sdp)
                    .append("}, ")
                    .append("{")
                    .append("\"venue\": \"LD_4_EBS\"")
                    .append(", \"ackTime\": ").append(time_LD_4_EBS)
                    .append("}, ")
                    .append("{")
                    .append("\"venue\": \"dcln_1_reuters\"")
                    .append(", \"ackTime\": ").append(time_dcln_1_reuters)
                    .append("}, ")
                    .append("{")
                    .append("\"venue\": \"NY_3_FXALL\"")
                    .append(", \"ackTime\": ").append(time_NY_3_FXALL)
                    .append("}")
                    .append("]}");
        }

        @Override
        public String toString() {
            return "ReconcileRecord{" 
                    + "tradeId=" + tradeId 
                    + ", firstReceivedTime=" + firstReceivedTime 
                    + ", status=" + status 
                    + '}';
        }
    }
}
