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

package walkingkooka.spreadsheet.dominokit.link;

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;

/**
 * Context for {@link CardLinkListComponent}.
 */
public interface CardLinkListComponentContext extends HistoryContext {

    /**
     * Called during a render to test if a text item is disabled.
     * <br>
     * This supports a UI to enter a {@link walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector} consisting of
     * links(this component) and a textbox where user may have entered a {@link walkingkooka.spreadsheet.format.SpreadsheetFormatterName}
     * and the link holding that formatter name should  be disabled.
     */
    boolean isDisabled(final String text);

    /**
     * Builds the save value text that will be passed to {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken#setSaveStringValue(String)}.
     */
    String saveValueText(final String text);
}
