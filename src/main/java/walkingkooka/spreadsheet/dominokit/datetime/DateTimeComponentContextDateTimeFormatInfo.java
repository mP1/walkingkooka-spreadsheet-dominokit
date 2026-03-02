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

import org.gwtproject.i18n.shared.cldr.DateTimeFormatInfo;
import walkingkooka.datetime.DateTimeSymbols;

/**
 * A {@link DateTimeFormatInfo} that sources its values from the given {@link DateTimeSymbols}.
 */
final class DateTimeComponentContextDateTimeFormatInfo extends TemporalComponentContextDateTimeFormatInfo<DateTimeComponentContext> {

    static DateTimeComponentContextDateTimeFormatInfo with(final DateTimeComponentContext context) {
        return new DateTimeComponentContextDateTimeFormatInfo(context);
    }

    private DateTimeComponentContextDateTimeFormatInfo(final DateTimeComponentContext context) {
        super(context);
    }

    @Override
    public String dateFormat() {
        return this.context.datePattern();
    }

    @Override
    public String timeFormat() {
        return this.context.timePattern();
    }
}
