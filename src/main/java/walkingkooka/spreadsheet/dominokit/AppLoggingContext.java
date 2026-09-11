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

import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.notifications.Notification.Position;
import walkingkooka.spreadsheet.dominokit.logging.BrowserLoggingContext;
import walkingkooka.spreadsheet.dominokit.logging.BrowserLoggingContextDelegator;

import java.util.Objects;

final class AppLoggingContext implements BrowserLoggingContext,
    BrowserLoggingContextDelegator {

    static AppLoggingContext with(final BrowserLoggingContext context) {
        return new AppLoggingContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppLoggingContext(final BrowserLoggingContext context) {
        this.context = context;
    }

    /**
     * Logs an error to the console and shows a DANGER notification.
     */
    @Override
    public void error(final Object... values) {
        this.context.error(values);

        Notification.create(
                String.valueOf(values[0])
            ).setPosition(Position.TOP_MIDDLE)
            .show();
    }

    @Override
    public BrowserLoggingContext browserLoggingContext() {
        return this.context;
    }

    private final BrowserLoggingContext context;
}
