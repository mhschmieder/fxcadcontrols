/**
 * MIT License
 *
 * Copyright (c) 2020, 2023 Mark Schmieder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the FxCadGui Library
 *
 * You should have received a copy of the MIT License along with the FxCadGui
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/fxcadgui
 */
package com.mhschmieder.fxcadcontrols.control;

import com.mhschmieder.fxcontrols.control.XToggleButton;

public final class CadLabeledControlFactory {

    public static XToggleButton getSurfaceBypassedToggleButton( final boolean applyAspectRatio,
                                                                final double aspectRatio,
                                                                final boolean wordWrap,
                                                                final boolean selected ) {
        final String selectedText = "Bypassed"; //$NON-NLS-1$
        final String deselectedText = "Enabled"; //$NON-NLS-1$
        final String tooltipText = "Click to Toggle Surface Status Between Bypassed and Enabled"; //$NON-NLS-1$

        // NOTE: JavaFX CSS automatically darkens unselected buttons, and
        //  auto-selects the foreground for text fill, but we override.
        final XToggleButton toggleButton = new XToggleButton( selectedText,
                                                              deselectedText,
                                                              tooltipText,
                                                              "bypass-toggle", //$NON-NLS-1$
                                                              applyAspectRatio,
                                                              aspectRatio,
                                                              wordWrap,
                                                              selected );

        return toggleButton;
    }
}
