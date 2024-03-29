package com.szczepaniak.dawid.appezn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.vanniktech.emoji.EmojiTextView;

import java.io.IOException;

public class TextViewEmojiAndMore extends EmojiTextView {

    private static final int TRIM_MODE_LINES = 0;
    private static final int TRIM_MODE_LENGTH = 1;
    private static final int DEFAULT_TRIM_LENGTH = 240;
    private static final int DEFAULT_TRIM_LINES = 10;
    private static final int INVALID_END_INDEX = -1;
    private static final boolean DEFAULT_SHOW_TRIM_EXPANDED_TEXT = true;
    private static final String ELLIPSIZE = "... ";

    private CharSequence text;
    private BufferType bufferType;
    private boolean readMore = true;
    private int trimLength;
    private CharSequence trimCollapsedText;
    private CharSequence trimExpandedText;
    private TextViewEmojiAndMore.ReadMoreClickableSpan viewMoreSpan;
    private int colorClickableText;
    private boolean showTrimExpandedText;

    private int trimMode;
    private int lineEndIndex;
    private int trimLines;

    public TextViewEmojiAndMore(Context context) {
        super(context);
    }

    public TextViewEmojiAndMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.borjabravo.readmoretextview.R.styleable.ReadMoreTextView);
        this.trimLength = typedArray.getInt(com.borjabravo.readmoretextview.R.styleable.ReadMoreTextView_trimLength, DEFAULT_TRIM_LENGTH);

        this.trimCollapsedText = "więcej";
        this.trimExpandedText = " mniej";
        this.trimLines = typedArray.getInt(com.borjabravo.readmoretextview.R.styleable.ReadMoreTextView_trimLines, DEFAULT_TRIM_LINES);
        this.colorClickableText = typedArray.getColor(com.borjabravo.readmoretextview.R.styleable.ReadMoreTextView_colorClickableText,
                ContextCompat.getColor(context, com.borjabravo.readmoretextview.R.color.colorPrimary));
        this.showTrimExpandedText =
                typedArray.getBoolean(com.borjabravo.readmoretextview.R.styleable.ReadMoreTextView_showTrimExpandedText, DEFAULT_SHOW_TRIM_EXPANDED_TEXT);
        this.trimMode = typedArray.getInt(com.borjabravo.readmoretextview.R.styleable.ReadMoreTextView_trimMode, TRIM_MODE_LINES);
        typedArray.recycle();
        viewMoreSpan = new TextViewEmojiAndMore.ReadMoreClickableSpan();
        onGlobalLayoutLineEndIndex();
        setTextMethod();
    }
    private void setTextMethod() {
        super.setText(getDisplayableText(), bufferType);
        setMovementMethod(LinkMovementMethod.getInstance());
        setHighlightColor(Color.TRANSPARENT);
    }

    private CharSequence getDisplayableText() {
        return getTrimmedText(text);
    }


    @Override
    public void setText(CharSequence rawText, BufferType type) {
        super.setText(rawText, type);
        this.text = rawText;
        bufferType = type;
        setTextMethod();
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (trimMode == TRIM_MODE_LENGTH) {
            if (text != null && text.length() > trimLength) {
                if (readMore) {
                    return updateCollapsedText();
                } else {
                    return updateExpandedText();
                }
            }
        }
        if (trimMode == TRIM_MODE_LINES) {
            if (text != null && lineEndIndex > 0) {
                if (readMore) {
                    if (getLayout().getLineCount() > trimLines) {
                        return updateCollapsedText();
                    }
                } else {
                    return updateExpandedText();
                }
            }
        }
        return text;
    }

    private CharSequence updateCollapsedText() {
        int trimEndIndex = text.length();
        switch (trimMode) {
            case TRIM_MODE_LINES:
                trimEndIndex = lineEndIndex - (ELLIPSIZE.length() + trimCollapsedText.length() + 1);
                if (trimEndIndex < 0) {
                    trimEndIndex = trimLength + 1;
                }
                break;
            case TRIM_MODE_LENGTH:
                trimEndIndex = trimLength + 1;
                break;
        }
        try {
            SpannableStringBuilder s = new SpannableStringBuilder(text, 0, trimEndIndex)
                    .append(ELLIPSIZE)
                    .append(trimCollapsedText);
            return addClickableSpan(s, trimCollapsedText);

        }catch (IndexOutOfBoundsException e){
            return trimCollapsedText;
        }
    }

    private CharSequence updateExpandedText() {
        if (showTrimExpandedText) {
            SpannableStringBuilder s = new SpannableStringBuilder(text, 0, text.length()).append(trimExpandedText);
            return addClickableSpan(s, trimExpandedText);
        }
        return text;
    }

    private CharSequence addClickableSpan(SpannableStringBuilder s, CharSequence trimText) {
        s.setSpan(viewMoreSpan, s.length() - trimText.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        setTextMethod();
    }

    public void setColorClickableText(int colorClickableText) {
        this.colorClickableText = colorClickableText;
    }

    public void setTrimCollapsedText(CharSequence trimCollapsedText) {
        this.trimCollapsedText = trimCollapsedText;
    }

    public void setTrimExpandedText(CharSequence trimExpandedText) {
        this.trimExpandedText = trimExpandedText;
    }

    public void setTrimMode(int trimMode) {
        this.trimMode = trimMode;
    }

    public void setTrimLines(int trimLines) {
        this.trimLines = trimLines;
    }

    private class ReadMoreClickableSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            readMore = !readMore;
            setTextMethod();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(colorClickableText);
        }
    }

    private void onGlobalLayoutLineEndIndex() {
        if (trimMode == TRIM_MODE_LINES) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = getViewTreeObserver();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this);
                    } else {
                        obs.removeGlobalOnLayoutListener(this);
                    }
                    refreshLineEndIndex();
                    setTextMethod();
                }
            });
        }
    }

    private void refreshLineEndIndex() {
        try {
            if (trimLines == 0) {
                lineEndIndex = getLayout().getLineEnd(0);
            } else if (trimLines > 0 && getLineCount() >= trimLines) {
                lineEndIndex = getLayout().getLineEnd(trimLines - 1);
            } else {
                lineEndIndex = INVALID_END_INDEX;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
