/*
 * Copyright (c) 2017 Max Lee (https://github.com/Phoenix616)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package de.themoep.minedown.adventure.tests;

import de.themoep.minedown.adventure.MineDown;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Parser tests")
public class ParserTest {

    private void parse(String mineDownString) {
        System.out.print(mineDownString + "\n" + GsonComponentSerializer.gson().serialize(new MineDown(mineDownString).toComponent()) + "\n");
    }

    @Test
    @DisplayName("Test parsing")
    public void testParsing() {
        Assertions.assertAll(
                () -> parse("&eTest [&blue&bthis](https://example.com Hover text) string!"),
                () -> parse("&eTest [this \\&6 \\that](blue /example command hover=Hover text) string!"),
                () -> parse("[TestLink](https://example.com) [Testcommand](/command test  )"),
                () -> parse("&b&lTest [this](color=green format=bold,italic https://example.com Hover & text) string!"),
                () -> parse("&bTest [this](color=green format=bold,italic suggest_command=/example command hover=Hover text) string!"),
                () -> parse("&b[Test] [this](6 bold italic https://example.com) &as&bt&cr&di&en&5g&7!"),
                () -> parse("&bTest [this](https://example.com)!"),
                () -> parse("&lbold &oitalic &0not bold or italic but black!"),
                () -> parse("&cRed &land bold!"),
                () -> parse("&bTest \n&cexample.com &rstring!"),
                () -> parse("&bTest \n&chttps://example.com &rstring!"),
                () -> parse("&bTest &chttps://example.com/test?t=2&d002=da0s#d2q &rstring!"),
                () -> parse("Test inner escaping [\\]](gray)"),
                () -> parse("[Test insertion](insert={text to insert} color=red)")
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> MineDown.parse("&bTest [this](color=green format=green,bold,italic https://example.com) shit!"));
    }

    @Test
    @DisplayName("Test parsing hex colors")
    public void testParseHexColors() {
        System.out.println("testParseHexColors");
        Assertions.assertAll(
                () -> parse("&eTest [&#593&bthis](Text) string!"),
                () -> parse("&eTest [&#593593&bthis](Text) string!"),
                () -> parse("&eTest [this \\&6 \\that](#290329 /example command hover=Hover text) string!"),
                () -> parse("&eTest [this \\&6 \\that](color=#290329 /example command hover=Hover text) string!")
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> MineDown.parse("&bTest [this](color=green format=green,bold,italic https://example.com) shit!"));
    }

    @Test
    @DisplayName("Test parsing gradients")
    public void testParseGradient() {
        System.out.println("testParseGradient");
        Assertions.assertAll(
                () -> parse("[Test Gradient](#fff-#000) &7:D"),
                () -> parse("[Test Gradient](#fff-#666666-#555555) &7:D"),
                () -> parse("[Test Gradient](#fff-#000 Hover message) &7:D"),
                () -> parse("[Test Gradient](color=#fff,#000 format=bold,italic Hover message) &7:D"),
                () -> parse("&#fff-#000&Test Gradient&7No Gradient")
        );
    }

    @Test
    @DisplayName("Test parsing rainbows")
    public void testParseRainbow() {
        System.out.println("testParseRainbow");
        Assertions.assertAll(
                () -> parse("[Test Rainbow](color=rainbow)"),
                () -> parse("[Test Rainbow](rainbow)"),
                () -> parse("[Test Rainbow](rainbow:25)"),
                () -> parse("[Test Rainbow](rainbow:240)"),
                () -> parse("&Rainbow&Rainbow&7 Test")
        );
    }

    @Test
    @DisplayName("Test negation")
    public void testNegated() {
        Assertions.assertAll(
                () -> parse("&lBold [not bold](!bold) bold")
        );
    }

    @Test
    @DisplayName("Test nested parsing")
    public void testParseNested() {
        Assertions.assertAll(
                () -> parse("[outer start [inner](green) outer end](aqua)"),
                () -> parse("[outer start \\[[inner](green)\\] outer end](aqua)"),
                () -> parse("[outer start [inner](green) outer end](aqua hover={[red hover](red)})")
        );
    }

    @Test
    @DisplayName("Test empty events")
    public void testEmptyEvent() {
        Assertions.assertAll(
                () -> parse("[test]()")
        );
    }
}
