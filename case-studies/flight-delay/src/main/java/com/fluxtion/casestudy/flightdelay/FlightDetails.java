/*
 * Copyright (C) 2018 greg
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
package com.fluxtion.casestudy.flightdelay;

import static com.fluxtion.extension.declarative.funclib.api.ascii.Conversion.atoi;
import com.fluxtion.runtime.event.Event;
import net.openhft.chronicle.wire.Marshallable;

/**
 *
 * @author greg
 */
public class FlightDetails extends Event implements Marshallable {

    public int delay;

    public void setCarrier(String carrier) {
        this.filterString = carrier;
    }

    public String getCarrier() {
        return filterString.toString();
    }

    public void setDelayString(StringBuilder seq) {
        if (seq.length() < 1 || !Character.isDigit(seq.charAt(0))) {
            delay = 0;
        } else {
            delay = atoi(seq);
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public String toString() {
        return "FlightDetails{" + "carrier=" + filterString + ", delay=" + delay + '}';
    }

    @Override
    public boolean equals(Object o) {
        return Marshallable.$equals(this, o);
    }

    @Override
    public int hashCode() {
        return Marshallable.$hashCode(this);
    }

}
