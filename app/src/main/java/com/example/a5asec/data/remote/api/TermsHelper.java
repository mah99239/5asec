package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Terms;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/* loaded from: classes3.dex */
public class TermsHelper {
private TermsApi mTermsInterface;

public TermsHelper(TermsApi termsInterface) {
this.mTermsInterface = termsInterface;
}

public Observable<List<Terms>> getTerms() {
return this.mTermsInterface.getTerms();
}
}