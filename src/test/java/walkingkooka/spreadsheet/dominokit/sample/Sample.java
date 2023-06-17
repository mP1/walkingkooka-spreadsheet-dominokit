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

package walkingkooka.spreadsheet.dominokit.sample;

import java.text.NumberFormat;

public final class Sample {

    public static void main(final String[] args) {
        System.out.println("NumberFormat.instance");
        print(NumberFormat.getInstance());

        System.out.println("NumberFormat.currency");
        print(NumberFormat.getCurrencyInstance());

        System.out.println("NumberFormat.percent");
        print(NumberFormat.getPercentInstance());
    }

    private static void print(final NumberFormat format) {
        System.out.println(format.toString());
        System.out.println(format.format(+1.25) + " " + format.format(-1.25) + " " + format.format(0));
    }
}
