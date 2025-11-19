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

import walkingkooka.reflect.PublicStaticHelper;

public final class SpreadsheetElementIds implements PublicStaticHelper {

    public final static String BUTTON = "-Button";

    public final static String CHECKBOX = "-Checkbox";

    public final static String CHIP = "-Chip";

    public final static String DATE = "-Date";

    public final static String DATE_PICKER = "-DatePicker";

    public final static String DATE_TIME_PICKER = "-DateTimePicker";

    public final static String DIALOG = "-Dialog";

    public final static String LABEL = "-Label";

    public final static String LINK = "-Link";

    public final static String MENU_ITEM = "-MenuItem";

    public final static String OPTION = "-Option";

    public final static String SELECT = "-Select";

    public final static String SLIDER = "-Slider";

    public final static String SUB_MENU = "-SubMenu";

    public final static String TABLE = "-Table";

    public final static String TABS = "-Tabs";
    public final static String TEXT_BOX = "-TextBox";

    public final static String TIME_PICKER = "-TimePicker";

    private SpreadsheetElementIds() {
        throw new UnsupportedOperationException();
    }
}
