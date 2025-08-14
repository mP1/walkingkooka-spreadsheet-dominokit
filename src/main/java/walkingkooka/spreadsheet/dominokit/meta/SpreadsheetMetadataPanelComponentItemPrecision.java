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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.HTMLUListElement;
import org.dominokit.domino.ui.forms.IntegerBox;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.dom.UlComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} for {@link SpreadsheetMetadataPropertyName#PRECISION}
 */
final class SpreadsheetMetadataPanelComponentItemPrecision extends SpreadsheetMetadataPanelComponentItem<Integer, SpreadsheetMetadataPanelComponentItemPrecision, HTMLUListElement> {

    static SpreadsheetMetadataPanelComponentItemPrecision with(final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponentItemPrecision(
            context
        );
    }

    private SpreadsheetMetadataPanelComponentItemPrecision(final SpreadsheetMetadataPanelComponentContext context) {
        super(
            SpreadsheetMetadataPropertyName.PRECISION,
            context
        );

        final UlComponent list = this.ul();

        final IntegerBox integerBox = this.integerBox()
            .setMinValue(0)
            .setMaxValue(128)
            .setStep(1);
        this.integerBox = integerBox;

        list.appendChild(
            li()
                .appendChild(integerBox)
        );

        // build links for 0 | 32 | 64 | 128
        final HistoryToken token = context.historyToken();
        final Map<Integer, HistoryTokenAnchorComponent> valueToAnchors = Maps.sorted();

        for (final int value : Lists.of(0, 32, 64, 128)) {
            final HistoryTokenAnchorComponent anchor = token
                .link(SpreadsheetMetadataPanelComponent.id(SpreadsheetMetadataPropertyName.PRECISION) + "-" + value)
                .setTabIndex(0)
                .addPushHistoryToken(context)
                .setTextContent(0 == value ? "Unlimited" : String.valueOf(value));

            valueToAnchors.put(value, anchor);

            list.appendChild(
                li()
                    .appendChild(anchor)
            );
        }

        final HistoryTokenAnchorComponent defaultValueAnchor = this.defaultValueAnchor();
        list.appendChild(
            li()
                .appendChild(defaultValueAnchor)
        );
        this.defaultValueAnchor = defaultValueAnchor;

        this.list = list;
        this.valueToAnchors = valueToAnchors;
    }

    @Override
    void focus() {
        this.integerBox.focus();
    }

    private final IntegerBox integerBox;

    // ComponentRefreshable.............................................................................................

    @Override
    public void refresh(final RefreshContext context) {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();

        this.integerBox.setValue(
            metadata.getIgnoringDefaults(this.propertyName)
                .orElse(null)
        );

        final SpreadsheetMetadataPropertyName<Integer> propertyName = SpreadsheetMetadataPropertyName.PRECISION;

        final Integer metadataValue = metadata.getIgnoringDefaults(propertyName)
            .orElse(null);

        final HistoryToken token = context.historyToken()
            .setMetadataPropertyName(propertyName);

        for (final Entry<Integer, HistoryTokenAnchorComponent> valueAndAnchor : this.valueToAnchors.entrySet()) {
            final Integer value = valueAndAnchor.getKey();
            final HistoryTokenAnchorComponent anchor = valueAndAnchor.getValue();

            anchor.setHistoryToken(
                Optional.of(
                    token.setSaveValue(
                        Optional.of(value)
                    )
                )
            );
            anchor.setDisabled(
                Objects.equals(
                    metadataValue,
                    value
                )
            );
        }

        this.refreshDefaultValue(
            this.defaultValueAnchor,
            this.context.spreadsheetMetadata()
                .defaults()
                .get(propertyName)
                .map(Object::toString)
                .orElse("")
        );
    }

    private final Map<Integer, HistoryTokenAnchorComponent> valueToAnchors;

    private final HistoryTokenAnchorComponent defaultValueAnchor;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLUListElement, ?> htmlComponent() {
        return this.list;
    }

    private final UlComponent list;
}
