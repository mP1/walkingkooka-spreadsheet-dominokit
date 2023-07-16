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

final class ElementalLoggingContext implements LoggingContext {

    /**
     * Singleton
     */
    final static ElementalLoggingContext INSTANCE = new ElementalLoggingContext();

    /**
     * Stop creation use singleton
     */
    private ElementalLoggingContext() {
    }

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
     * Logs an error to the console and shows a DANGER notification.
     */
    @Override
    public void error(final Object... values) {
        // see App.debug
        final elemental2.dom.Console console = DomGlobal.console;
        console.error(values);
    }
}
