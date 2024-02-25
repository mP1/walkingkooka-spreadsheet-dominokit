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

package walkingkooka.spreadsheet.dominokit.ui.meta;

import elemental2.dom.HTMLUListElement;
import org.dominokit.domino.ui.elements.UListElement;
import org.dominokit.domino.ui.forms.IntegerBox;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetMetadataPanelComponentItem} for {@link SpreadsheetMetadataPropertyName#PRECISION}
 */
final class SpreadsheetMetadataPanelComponentItemPrecision extends SpreadsheetMetadataPanelComponentItem<Integer> {

    static SpreadsheetMetadataPanelComponentItemPrecision with(final SpreadsheetMetadataPanelComponentContext context) {
        checkContext(context);

        return new SpreadsheetMetadataPanelComponentItemPrecision(
                context
        );
    }

    private SpreadsheetMetadataPanelComponentItemPrecision(final SpreadsheetMetadataPanelComponentContext context) {
        super(
                SpreadsheetMetadataPropertyName.PRECISION,
                context
        );

        final UListElement list = this.uListElement();

        final IntegerBox integerBox = this.integerBox()
                .setMinValue(0)
                .setMaxValue(128)
                .setStep(1);
        this.integerBox = integerBox;

        list.appendChild(
                liElement()
                        .appendChild(integerBox)
        );

        // build links for 0 | 32 | 64 | 128
        final HistoryToken token = context.historyToken()
                .setMetadataPropertyName(SpreadsheetMetadataPropertyName.PRECISION);
        final Map<Integer, HistoryTokenAnchorComponent> valueToAnchors = Maps.sorted();

        for (final int value : Lists.of(0, 32, 64, 128)) {
            final HistoryTokenAnchorComponent anchor = token
                    .setSave(Optional.of(value))
                    .link(SpreadsheetMetadataPanelComponent.id(SpreadsheetMetadataPropertyName.PRECISION) + "-" + value)
                    .setTabIndex(0)
                    .addPushHistoryToken(context)
                    .setTextContent(0 == value ? "Unlimited" : String.valueOf(value));

            valueToAnchors.put(value, anchor);

            list.appendChild(
                    liElement()
                            .appendChild(anchor)
            );
        }

        final HistoryTokenAnchorComponent defaultValueAnchor = this.defaultValueAnchor();
        list.appendChild(
                liElement()
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
    public void refresh(final AppContext context) {
        this.integerBox.setValue(
                context.spreadsheetMetadata()
                        .getIgnoringDefaults(this.propertyName)
                        .orElse(null)
        );

        final SpreadsheetMetadataPropertyName<Integer> propertyName = SpreadsheetMetadataPropertyName.PRECISION;

        final Integer metadataValue = context.spreadsheetMetadata()
                .getIgnoringDefaults(propertyName)
                .orElse(null);

        final HistoryToken token = context.historyToken()
                .setMetadataPropertyName(propertyName);

        for (final Entry<Integer, HistoryTokenAnchorComponent> valueAndAnchor : this.valueToAnchors.entrySet()) {
            final Integer value = valueAndAnchor.getKey();
            final HistoryTokenAnchorComponent anchor = valueAndAnchor.getValue();

            anchor.setHistoryToken(
                    Optional.of(
                            token.setSave(
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
                context.spreadsheetMetadata()
                        .defaults()
                        .get(propertyName)
                        .map(Object::toString)
                        .orElse("")
        );
    }

    private final Map<Integer, HistoryTokenAnchorComponent> valueToAnchors;

    private final HistoryTokenAnchorComponent defaultValueAnchor;

    // isElement........................................................................................................

    @Override
    public HTMLUListElement element() {
        return this.list.element();
    }

    private final UListElement list;
}
