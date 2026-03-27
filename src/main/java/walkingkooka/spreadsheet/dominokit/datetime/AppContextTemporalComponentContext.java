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

import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeSymbols;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.Temporal;
import java.util.Locale;
import java.util.Objects;

abstract class AppContextTemporalComponentContext<V extends Temporal> implements TemporalComponentContext<V> {

    AppContextTemporalComponentContext(final DateTimeContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    @Override
    public final DateTimeSymbols dateTimeSymbols() {
        return this.context.dateTimeSymbols();
    }

    @Override
    public final Locale locale() {
        return this.context.locale();
    }

    final String datePatternForLocale() {
        final SimpleDateFormat simpleDateFormat = (SimpleDateFormat) DateFormat.getDateInstance(
            DateFormat.SHORT,
            this.locale()
        );
        return fourDigitYear(
            simpleDateFormat.toPattern()
        );
    }

    final String timePatternForLocale() {
        final SimpleDateFormat simpleDateFormat = (SimpleDateFormat) DateFormat.getTimeInstance(
            DateFormat.SHORT,
            this.locale()
        );
        return simpleDateFormat.toPattern();
    }

    // HACK that is necessary because many(most) DateFormat.SHORT return a 2 digit year.
    static String fourDigitYear(final String pattern) {
        return pattern.replace("yyyy", "yy")
            .replace("yy", "yyyy");
    }

    final DateTimeContext context;
}
