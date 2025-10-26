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

package walkingkooka.spreadsheet.dominokit.toolbar;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.spreadsheet.dominokit.NopComponentLifecycleOpenGiveFocus;
import walkingkooka.spreadsheet.dominokit.NopComponentLifecycleRefresh;
import walkingkooka.spreadsheet.dominokit.SpreadsheetCellComponentLifecycle;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

/**
 * Base class for links that update a {@link TextStyle}.
 */
abstract class ToolbarComponentItemAnchorTextStyle<C extends ToolbarComponentItemAnchorTextStyle<C>>
    extends ToolbarComponentItemAnchor<C>
    implements SpreadsheetCellComponentLifecycle,
    NopComponentLifecycleOpenGiveFocus,
    NopComponentLifecycleRefresh {

    ToolbarComponentItemAnchorTextStyle(final String id,
                                        final Optional<Icon<?>> icon,
                                        final String text,
                                        final String tooltipText,
                                        final ToolbarComponentContext context) {
        super(
            id,
            icon,
            text,
            tooltipText,
            context
        );
    }
}
