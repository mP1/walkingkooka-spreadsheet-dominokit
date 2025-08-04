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

package walkingkooka.spreadsheet.dominokit.convert;

import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.net.UrlPath;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;

import java.util.Optional;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link ConverterSelectorDialogComponent} provided various inputs.
 */
public interface ConverterSelectorDialogComponentContext extends ComponentLifecycleMatcher,
    DialogComponentContext {

    /**
     * Provides the UNDO text.
     */
    Optional<ConverterSelector> undo();

    /**
     * Attempts to verify this {@link String} is valid and able to convert all the basic values.
     */
    void verifySelector(final String selector);

    /**
     * Verifies the give {@link UrlPath} is a match.
     */
    boolean isVerifyConverterSelectorUrl(final UrlPath path);

    /**
     * Adds a {@link ConverterFetcherWatcher} which will see results of any {@link #verifySelector(String).
     */
    Runnable addConverterFetcherWatcher(final ConverterFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);
}
