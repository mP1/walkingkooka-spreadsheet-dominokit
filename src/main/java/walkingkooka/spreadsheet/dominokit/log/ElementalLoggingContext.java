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

import elemental2.dom.DomGlobal;
import walkingkooka.logging.LoggingLevel;

import java.util.Objects;

final class ElementalLoggingContext implements BrowserLoggingContext {

    /**
     * Singleton
     */
    final static ElementalLoggingContext INSTANCE = new ElementalLoggingContext();

    /**
     * Stop creation use singleton
     */
    private ElementalLoggingContext() {
    }

    // BrowserLoggingContext............................................................................................

    @Override
    public void debug(final Object... values) {
        // invoking DomGlobal.console.debug caused GWT compile failures
        //
        //[INFO]                [ERROR] at App.java(719): <source info not available>
        //[INFO]                   com.google.gwt.dev.js.ast.JsExprStmt
        //[INFO]                [ERROR] at com.google.gwt.dev.js.ast.JsProgram(0): var _;
        //[INFO] $wnd.goog = $wnd.goog || {};
        //[INFO] $wnd.goog.global = $wnd.goog.global || $wnd;
        //[INFO] bootstrap();
        //[INFO] [...]
        //[INFO]
        //[INFO]                   com.google.gwt.dev.js.ast.JsGlobalBlock
        final elemental2.dom.Console console = DomGlobal.console;
        console.debug(values);
    }

    /**
     * Logs an INFO level message.
     */
    @Override
    public void info(final Object... values) {
        // see App.debug
        final elemental2.dom.Console console = DomGlobal.console;
        console.info(values);
    }


    /**
     * Logs a WARN level message.
     */
    @Override
    public void warn(final Object... values) {
        final elemental2.dom.Console console = DomGlobal.console;
        console.warn(values);
    }

    /**
     * Logs an error to the console and shows a DANGER notification.
     */
    @Override
    public void error(final Object... values) {
        // see App.debug
        final elemental2.dom.Console console = DomGlobal.console;
        console.error(values);
    }

    // LoggingContext...................................................................................................

    @Override
    public void debug(final String message) {
        this.debug(
            message,
            null
        );
    }

    @Override
    public void debug(final String message,
                      final Throwable throwable) {
        this.debug(
            (Object)message,
            throwable
        );
    }

    @Override
    public void info(final String message) {
        this.info(
            message
        );
    }

    @Override
    public void info(final String message,
                     final Throwable throwable) {
        this.info(
            (Object)message,
            (Object)throwable
        );
    }

    @Override
    public void warn(final String message) {
        this.warn(
            message,
            null
        );
    }

    @Override
    public void warn(final String message,
                     final Throwable throwable) {
        this.warn(
            (Object) message,
            throwable
        );
    }

    @Override
    public void error(final String message) {
        this.error(
            message
        );
    }

    @Override
    public void error(final String message,
                      final Throwable throwable) {
        this.error(
            (Object)message,
            throwable
        );
    }

    @Override
    public void log(final LoggingLevel loggingLevel,
                    final String message,
                    final Throwable throwable) {
        Objects.requireNonNull(loggingLevel, "loggingLevel");

        switch (loggingLevel) {
            case DEBUG:
                this.debug(
                    message,
                    throwable
                );
                break;
            case INFO:
                this.info(
                    message,
                    throwable
                );
                break;
            case WARN:
                this.warn(
                    message,
                    throwable
                );
                break;
            case ERROR:
                this.error(
                    message,
                    throwable
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown logging level " + loggingLevel);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isNoneEnabled() {
        return false;
    }

    @Override
    public boolean isLoggingEnabled(final LoggingLevel level) {
        Objects.requireNonNull(level, "level");

        return level != LoggingLevel.NONE;
    }

    @Override
    public LoggingLevel loggingLevel() {
        return LoggingLevel.DEBUG;
    }
}
