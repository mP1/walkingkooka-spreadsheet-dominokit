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

package walkingkooka.spreadsheet.dominokit.log;

import walkingkooka.logging.LoggingContext;
import walkingkooka.logging.LoggingContextDelegator;

/**
 * Helper interface that supports delegating {@link BrowserLoggingContext} to a delegate fetched from a method {@link #browserLoggingContext()}.
 */
public interface BrowserLoggingContextDelegator extends BrowserLoggingContext,
    LoggingContextDelegator {

    @Override
    default LoggingContext loggingContext() {
        return this.browserLoggingContext();
    }

    // BrowserLoggingContextDelegator...................................................................................

    @Override
    default void debug(final Object... values) {
        this.browserLoggingContext()
            .debug(values);
    }

    @Override
    default void info(final Object... values) {
        this.browserLoggingContext()
            .info(values);
    }

    @Override
    default void warn(final Object... values) {
        this.browserLoggingContext()
            .warn(values);
    }

    @Override
    default void error(final Object... values) {
        this.browserLoggingContext()
            .error(values);
    }

    BrowserLoggingContext browserLoggingContext();
}
