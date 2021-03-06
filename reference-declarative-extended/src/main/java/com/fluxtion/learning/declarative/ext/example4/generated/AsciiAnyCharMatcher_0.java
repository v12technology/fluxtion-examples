/*
 * Copyright (C) 2016 Greg Higgins (greg.higgins@v12technology.com)
 *
 * This file is part of Fluxtion.
 *
 * Fluxtion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.fluxtion.learning.declarative.ext.example4.generated;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.extension.declarative.funclib.api.event.CharEvent;
import com.fluxtion.extension.declarative.funclib.api.filter.AnyCharMatchFilter;

/**
 * Char notifiers match = ','
 *  
 * @author Greg Higgins
 */
public class AsciiAnyCharMatcher_0 implements AnyCharMatchFilter{

    private transient char matchedChar;

    @EventHandler(filterId = ',') 
    public final boolean onChar_44 (CharEvent event) {
        matchedChar = ',';
        return true;
    }

    @Override
    public char matchedChar() {
        return matchedChar;
    }
}
