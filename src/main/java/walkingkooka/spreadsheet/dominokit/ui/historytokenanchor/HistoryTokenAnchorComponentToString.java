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

package walkingkooka.spreadsheet.dominokit.ui.historytokenanchor;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.net.Url;

/**
 * Because {@link HistoryTokenAnchorComponentLike} cannot provide a default {@link Object#toString()}, this is necessary.
 */
final class HistoryTokenAnchorComponentToString {

    // "    test-clipboard-cut-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula]\n" +

    static String toString(final HistoryTokenAnchorComponentLike component) {
        // cant use surround values because null href will become LEFT BRACKET NULL RIGHT BRACKET - [null]
        Url href = component.href();

        String hrefString = null;
        if (null != href) {
            hrefString = "[" + href + "]";
        }

        String disabled = "";
        if (component.isDisabled()) {
            disabled = "DISABLED";
        }

        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                .value(component.iconBefore().map(Icon::getName))
                .enable(ToStringBuilderOption.QUOTE)
                .value(component.textContent())
                .disable(ToStringBuilderOption.QUOTE)
                .value(disabled)
                .value(hrefString)
                .value(component.target())
                .value(component.isChecked() ? "CHECKED" : "")
                .value(component.iconAfter().map(Icon::getName))
                .build();
    }

}
