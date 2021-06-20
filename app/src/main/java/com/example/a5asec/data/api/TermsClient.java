package com.example.a5asec.data.api;

import com.example.a5asec.data.model.Terms;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public class TermsClient extends Client
{
  private static TermsClient INSTANCE;

  private final TermsInterface termsInterface;

  public TermsClient()
  {
    termsInterface = getRetrofitAdapter().create(TermsInterface.class);
  }

  public static TermsClient getINSTANCE()
  {
    return null == INSTANCE ? new TermsClient() : INSTANCE;
  }


  public Single<List<Terms>> getClient()
  {
    return termsInterface.getTerms();
  }
}
