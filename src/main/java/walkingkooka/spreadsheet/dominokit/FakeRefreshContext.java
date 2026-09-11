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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.logging.LoggingLevel;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;

public class FakeRefreshContext extends FakeHistoryContext implements RefreshContext {

    public FakeRefreshContext() {
        super();
    }

    // RefreshContext...................................................................................................

    @Override
    public boolean isSpreadsheetMetadataLoaded() {
        throw new UnsupportedOperationException();
    }

    // CanGiveFocus.....................................................................................................

    @Override
    public void giveFocus(Runnable focus) {
        throw new UnsupportedOperationException();
    }

    // BrowserLoggingContext............................................................................................

    @Override
    public void debug(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void info(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void warn(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void debug(final String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void debug(final String message,
                      final Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void info(final String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void info(final String message,
                     final Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void warn(final String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void warn(final String message,
                     final Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final String message,
                      final Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void log(final LoggingLevel level,
                    final String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void log(final LoggingLevel level,
                    final String message,
                    final Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDebugEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInfoEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWarnEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isErrorEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNoneEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLoggingEnabled(final LoggingLevel level) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LoggingLevel loggingLevel() {
        throw new UnsupportedOperationException();
    }
}
