/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.datetime;

import walkingkooka.Context;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.util.HasLocale;

import java.time.temporal.Temporal;

/**
 * A {@link Context} used by {@link DateComponent}.
 */
public interface TemporalComponentContext<T extends Temporal> extends Context,
    HasLocale {

    /**
     * The {@link DateTimeSymbols}.
     */
    DateTimeSymbols dateTimeSymbols();

    /**
     * The {@link String pattern} for the accompanying {@link org.dominokit.domino.ui.forms.DateBox} or
     * {@link org.dominokit.domino.ui.forms.TimeBox}.
     */
    String pattern();
}
