package com.mercadolibre.android.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.mercadolibre.android.ui.R;
import com.mercadolibre.android.ui.font.Font;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link TextField}.
 *
 * @since 26/5/16
 */
// CHECKSTYLE:OFF
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
public class TextFieldTest {

    private TextField textField;
    private EditText editText;
    private TextView label;
    private TextInputLayout container;
    private Context context;

    @Before
    public void setUp() {

        final EnumMap<Font, String> fontsHashMap = new EnumMap<>(Font.class);
        fontsHashMap.put(Font.BOLD,"Roboto-Bold.ttf");
        fontsHashMap.put(Font.BLACK,"Roboto-Black.ttf");
        fontsHashMap.put(Font.EXTRA_BOLD,"RobotoCondensed-Bold.ttf");
        fontsHashMap.put(Font.LIGHT,"Roboto-Light.ttf");
        fontsHashMap.put(Font.MEDIUM,"Roboto-Medium.ttf");
        fontsHashMap.put(Font.REGULAR,"Roboto-Regular.ttf");
        fontsHashMap.put(Font.SEMI_BOLD,"Roboto-Medium.ttf");
        fontsHashMap.put(Font.THIN,"Roboto-Thin.ttf");

        Font.FontConfig.setFonts(fontsHashMap);

        context = new ContextWrapper(RuntimeEnvironment.application);
        context.setTheme(R.style.Theme_MLTheme);
        textField = new TextField(context);
        editText = textField.getEditText();
        label = ReflectionHelpers.getField(textField, "label");
        container = ReflectionHelpers.getField(textField, "container");
    }

    @After
    public void tearDown() {
        // Clear the fonts to avoid side effects
        Font.FontConfig.setFonts(null);
    }

    @Test
    public void testSetHelper_shouldNotSetError() {
        /*
            we set the helper, the method use reflection,
            if the method isErrorEnable returns false, it means that the exception was not thrown
         */
        textField.setHelper("test");
        final boolean isErrorShowing = ReflectionHelpers.getField(textField, "isShowingError");
        assertFalse(isErrorShowing);
    }

    @Test
    public void testNotNull() {
        assertNotNull(textField);
    }

    @Test
    public void testDefaultConfig() {
        //Default drawables
        final Drawable[] drawables = editText.getCompoundDrawables();
        assertNull(drawables[0]);
        assertNull(drawables[1]);
        assertNull(drawables[2]);
        assertNull(drawables[3]);

        //Default configs
        assertEquals(InputType.TYPE_CLASS_TEXT, editText.getInputType());
        assertEquals(TextUtils.TruncateAt.END, editText.getEllipsize());
        assertEquals(TypefaceUtils.load(context.getAssets(), Font.LIGHT.getFontPath()), editText.getTypeface());
        assertTrue(container.isHintEnabled());


        final int greyColor = Color.parseColor("#999999");

        final int[] states = {android.R.attr.state_enabled};

        //Default colors
        assertEquals(greyColor, label.getTextColors().getColorForState(states, greyColor));
        assertEquals(ColorStateList.valueOf(Color.parseColor("#999999")), editText.getHintTextColors());
        int currentColor = editText.getTextColors().getColorForState(editText.getDrawableState(), 0);
        assertEquals(Color.parseColor("#333333"), currentColor);
        editText.setEnabled(false);
        currentColor = editText.getTextColors().getColorForState(editText.getDrawableState(), 0);
        assertEquals(Color.parseColor("#CCCCCC"), currentColor);

        //Default sizes
        assertEquals(18, editText.getTextSize(), 0);
        assertEquals(14, label.getTextSize(), 0);
    }

    @Test
    public void testText() {
        final EditText text = textField.findViewById(R.id.ui_text_field_input);
        final String testText = "testText";
        textField.setText(testText);
        assertNotNull(text);
        assertEquals(testText, text.getText().toString());
    }

    @Test
    public void testLabel() {
        final TextView label = textField.findViewById(R.id.ui_text_field_label);
        final String testText = "testLabel";
        textField.setLabel(testText);
        assertNotNull(label);
        assertEquals(testText, textField.getLabel());
    }

    @Test
    public void testSetHint_withLabelAlreadySet_shouldSetItToTheInput() {
        final TextInputLayout container = textField.findViewById(R.id.ui_text_field_input_container);
        final EditText input = textField.findViewById(R.id.ui_text_field_input);
        textField.setLabel("A label");

        textField.setHint("A hint");
        assertNull(container.getHint());
        assertFalse(container.isHintAnimationEnabled());
        assertFalse(container.isHintEnabled());

        assertEquals("A hint", input.getHint());
    }

    @Test
    public void testSetHint_withLabelNotSet_shouldSetItToTheContainer() {
        textField.setHint("A hint");
        assertNull(editText.getHint());

        assertTrue(container.isHintAnimationEnabled());
        assertTrue(container.isHintEnabled());
        assertEquals("A hint", container.getHint());
    }

    @Test
    public void testDisableHintAnimation_shouldChangeTheHintToTheInput() {
        textField.setHint("A hint");

        assertTrue(textField.isHintAnimationEnabled());
        assertEquals("A hint", container.getHint());
        assertNull(editText.getHint());

        textField.setHintAnimationEnabled(false);

        assertFalse(textField.isHintAnimationEnabled());
        assertEquals("A hint", textField.getEditText().getHint());
        assertNull(container.getHint());
    }

    @Test
    public void testMaximumNumberOfCharacters() {
        final  int maximumNumberOfCharacters = 10;
        textField.setMaxCharacters(maximumNumberOfCharacters);
        final EditText text = textField.findViewById(R.id.ui_text_field_input);
        final String testText = "012345678912";
        textField.setText(testText);
        assertNotNull(text);
        assertEquals(maximumNumberOfCharacters, textField.getText().length());
        assertNotEquals(testText.length(), textField.getText().length());
    }

    @Test
    public void testStatus() {
        assertTrue(textField.isEnabled());
        textField.setEnabled(false);
        assertFalse(textField.isEnabled());
    }

    @Test
    public void testErrorDescription() {
        final String testText = "testError";
        textField.setError(testText);
        assertEquals(testText, textField.getError());
    }

    @Test
    public void testSetDrawables_shouldSetDrawables() {
        final EditText input = textField.findViewById(R.id.ui_text_field_input);

        final Drawable drawableLeft = mock(Drawable.class);
        final Drawable drawableBottom = mock(Drawable.class);
        textField.setCompoundDrawables(drawableLeft, null, null, drawableBottom);

        final Drawable[] compoundDrawables = input.getCompoundDrawables();
        assertEquals(drawableLeft, compoundDrawables[0]);
        assertNull(compoundDrawables[1]);
        assertNull(compoundDrawables[2]);
        assertEquals(drawableBottom, compoundDrawables[3]);
    }

    @Test
    public void testConfigurePasswordToggle_shouldConfigureItCorrectly() {
        final TextInputLayout container = textField.findViewById(R.id.ui_text_field_input_container);

        final Drawable toggleDrawable = mock(Drawable.class);
        textField.setPasswordVisibilityToggleDrawable(toggleDrawable);
        textField.setPasswordVisibilityToggleEnabled(true);

        assertTrue(container.isPasswordVisibilityToggleEnabled());
        assertEquals(toggleDrawable, container.getPasswordVisibilityToggleDrawable());
    }

    @Test
    public void testSetTextFont_withValidFont_shouldSetItToTheInput() {
        textField.setTextFont(Font.BOLD);

        final EditText editText = textField.getEditText();
        assertEquals(TypefaceUtils.load(context.getAssets(), Font.BOLD.getFontPath()), editText.getTypeface());
    }

    @Test
    public void testSetLabelFont_withValidFont_shouldSetItToTheInput() {
        textField.setLabelFont(Font.BLACK);

        final TextView label = ReflectionHelpers.getField(textField, "label");
        assertEquals(TypefaceUtils.load(context.getAssets(), Font.BLACK.getFontPath()), label.getTypeface());
    }

    @Test
    public void testSetTextColor_withResource_shouldSetTheColorToTheInput() {
        final int color = Color.parseColor("#fedc13");
        textField.setTextColor(color);

        assertEquals(ColorStateList.valueOf(color), textField.getEditText().getTextColors());
    }

    @Test
    public void testSetTextColor_withHexColor_shouldSetTheColorToTheInput() {
        final int color = Color.parseColor("#A4C8D1");

        textField.setTextColor(color);

        assertEquals(ColorStateList.valueOf(color), textField.getEditText().getTextColors());
    }

    @Test
    public void testSetTextColor_withColorStateList_shouldSetTheColorToTheInput() {
        final ColorStateList color = ColorStateList.valueOf(Color.parseColor("#A4C8D1"));

        textField.setTextColor(color);

        assertEquals(color, textField.getEditText().getTextColors());
    }

    @Test
    public void testSetLabelColor_withResource_shouldSetTheColorToTheLabel() {
        final int color = Color.parseColor("#fedc13");

        textField.setLabelColor(color);

        assertEquals(ColorStateList.valueOf(color), label.getTextColors());
    }

    @Test
    public void testSetLabelColor_withHexColor_shouldSetTheColorToTheLabel() {
        final int color = Color.parseColor("#A4C8D1");

        textField.setLabelColor(color);

        final TextView label = ReflectionHelpers.getField(textField, "label");
        assertEquals(ColorStateList.valueOf(color), label.getTextColors());
    }

    @Test
    public void testSetLabelColor_withColorStateList_shouldSetTheColorToTheLabel() {
        final ColorStateList color = ColorStateList.valueOf(Color.parseColor("#A4C8D1"));

        textField.setLabelColor(color);

        final TextView label = ReflectionHelpers.getField(textField, "label");
        assertEquals(color, label.getTextColors());
    }

    @Test
    public void testSetHintColor_withResource_shouldSetTheColorToTheHint() {
        final int color = Color.parseColor("#fedc13");

        textField.setHintColor(color);

        assertEquals(ColorStateList.valueOf(color), textField.getEditText().getHintTextColors());
    }

    @Test
    public void testSetHintColor_withHexColor_shouldSetTheColorToTheHint() {
        final int color = Color.parseColor("#A4C8D1");

        textField.setHintColor(color);

        assertEquals(ColorStateList.valueOf(color), textField.getEditText().getHintTextColors());
    }

    @Test
    public void testSetHintColor_withColorStateList_shouldSetTheColorToTheHint() {
        final ColorStateList color = ColorStateList.valueOf(Color.parseColor("#A4C8D1"));

        textField.setHintColor(color);

        assertEquals(color, textField.getEditText().getHintTextColors());
    }

    @Test
    @TargetApi(24)
    @Config(sdk = 24)
    public void testSetTextGravity_shouldSetItToTheInput() {
        textField.setTextGravity(Gravity.CENTER);

        assertEquals(17, textField.getEditText().getGravity());
        assertNotEquals(17, textField.getGravity());
    }

    @Test
    public void testSetEllipsize_withInt_shouldCallSetEllipsizeWithValue() {
        final TextField spyTextField = spy(textField);

        int ellipsize = 0;
        ReflectionHelpers.ClassParameter<Integer> ellipsizeParameter = ReflectionHelpers.ClassParameter.from(int.class, ellipsize);
        ReflectionHelpers.callInstanceMethod(spyTextField, "setEllipsize", ellipsizeParameter);
        verify(spyTextField, never()).setEllipsize(any(TextUtils.TruncateAt.class));

        ellipsize = 1;
        ellipsizeParameter = ReflectionHelpers.ClassParameter.from(int.class, ellipsize);
        ReflectionHelpers.callInstanceMethod(spyTextField, "setEllipsize", ellipsizeParameter);
        verify(spyTextField).setEllipsize(TextUtils.TruncateAt.START);

        ellipsize = 2;
        ellipsizeParameter = ReflectionHelpers.ClassParameter.from(int.class, ellipsize);
        ReflectionHelpers.callInstanceMethod(spyTextField, "setEllipsize", ellipsizeParameter);
        verify(spyTextField).setEllipsize(TextUtils.TruncateAt.MIDDLE);

        ellipsize = 3;
        ellipsizeParameter = ReflectionHelpers.ClassParameter.from(int.class, ellipsize);
        ReflectionHelpers.callInstanceMethod(spyTextField, "setEllipsize", ellipsizeParameter);
        verify(spyTextField).setEllipsize(TextUtils.TruncateAt.END);
    }

    @Test
    public void testSetInputType_withAnyNumber_shouldSetItToTheInput() {
        textField.setInputType(521);

        assertEquals(521, textField.getEditText().getInputType());
    }

    @Test
    public void testSetEnabled_withEnabledTrue_shouldChangeTheState() {
        textField.setEnabled(true);

        assertTrue(container.isFocusableInTouchMode());
        assertTrue(container.isEnabled());
    }

    @Test
    public void testSetEnabled_withEnabledFalse_shouldChangeTheState() {
        textField.setEnabled(false);

        assertFalse(container.isFocusableInTouchMode());
        assertFalse(container.isEnabled());
        assertFalse(container.isCounterEnabled());
    }

    @Test
    public void testSaveState_shouldSaveTheStateCorrectly() {
        final List<Object> parcelMemory = new ArrayList<>();
        textField.setHint("hint");
        textField.setText("text");
        textField.setLabel("label");
        textField.setError("error");
        textField.setMaxCharacters(12);
        textField.setMaxLines(3);
        textField.setEnabled(true);

        final Parcel savedState = mock(Parcel.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                parcelMemory.add(invocation.getArguments()[0]);
                return null;
            }
        }).when(savedState).writeString(anyString());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                parcelMemory.add(invocation.getArguments()[0]);
                return null;
            }
        }).when(savedState).writeInt(anyInt());

        textField.onSaveInstanceState().writeToParcel(savedState, 0);

        assertEquals("inputText", "text", parcelMemory.get(0));
        assertEquals("labelText", "label", parcelMemory.get(1));
        assertEquals("errorText", "error", parcelMemory.get(2));
        assertEquals("linesNumber", 3, parcelMemory.get(3));
        assertEquals("charactersNumber", 12, parcelMemory.get(4));
        assertEquals("charactersVisible ", 1, parcelMemory.get(5));
        assertEquals("hint", "hint", parcelMemory.get(6));
        assertEquals("enabled", 1, parcelMemory.get(7));
        parcelMemory.clear();
    }


    @Test
    public void testRestoreState_shouldRestoreCorrectly() {
        final ArrayList<Object> parcelMemory = new ArrayList<>();
        final AtomicInteger index = new AtomicInteger(0);

        parcelMemory.add("text");
        parcelMemory.add("label");
        parcelMemory.add("error");
        parcelMemory.add(3);
        parcelMemory.add(12);
        parcelMemory.add(0);
        parcelMemory.add("hint");
        parcelMemory.add(1);

        final Parcel parcel = mock(Parcel.class);
        when(parcel.readString()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                return (String) parcelMemory.get(index.getAndIncrement());
            }
        });

        when(parcel.readInt()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                return (Integer) parcelMemory.get(index.getAndIncrement());
            }
        });

        final Parcelable parcelable = TextField.SavedState.CREATOR.createFromParcel(parcel);

        textField.onRestoreInstanceState(parcelable);
        assertEquals("inputText", "text", textField.getText());
        assertEquals("labelText", "label", textField.getLabel());
        assertEquals("errorText", "error", textField.getError());
        assertEquals("linesNumber", 3, editText.getMaxLines());
        assertEquals("charactersNumber", 12, container.getCounterMaxLength());
        assertEquals("charactersVisible ", 1, container.isCounterEnabled() ? 1 : 0);
        assertEquals("hint", "hint", textField.getHint());
        assertEquals("enabled", 1, textField.isEnabled() ? 1 : 0);
        parcelMemory.clear();
    }
}
