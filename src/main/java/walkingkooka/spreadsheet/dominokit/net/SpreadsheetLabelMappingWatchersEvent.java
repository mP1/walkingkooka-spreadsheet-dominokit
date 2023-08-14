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

package walkingkooka.spreadsheet.dominokit.net;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;

import java.util.function.Consumer;

/**
 * The event payload used by {@link SpreadsheetLabelMappingWatchers}.
 */
final class SpreadsheetLabelMappingWatchersEvent implements Consumer<SpreadsheetLabelMappingWatcher> {

    static SpreadsheetLabelMappingWatchersEvent with(final SpreadsheetLabelMapping mapping, final AppContext context) {
        return new SpreadsheetLabelMappingWatchersEvent(mapping, context);
    }

    private SpreadsheetLabelMappingWatchersEvent(final SpreadsheetLabelMapping mapping, final AppContext context) {
        this.mapping = mapping;
        this.context = context;
    }

    @Override
    public void accept(final SpreadsheetLabelMappingWatcher watcher) {
        try {
            watcher.onSpreadsheetLabelMapping(
                    this.mapping,
                    this.context
            );
        } catch (final Exception cause) {
            this.context.error(
                    "SpreadsheetLabelMappingWatchersEvent.accept exception: " + cause.getMessage(),
                    cause
            );
        }
    }

    private final SpreadsheetLabelMapping mapping;

    private final AppContext context;

    @Override
    public String toString() {
        return this.mapping + " " + this.context;
    }
}
