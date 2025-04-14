package com.example.verkli;

import androidx.annotation.NonNull;

public class PasswordCharSequence implements CharSequence {
    private final CharSequence source;

    public PasswordCharSequence(CharSequence cs) {
        source = cs;
    }

    public char charAt(int index) {
        return '*';
    }

    @Override
    public int length() {
        return source.length();
    }

    @NonNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return source.subSequence(start, end);
    }
}
