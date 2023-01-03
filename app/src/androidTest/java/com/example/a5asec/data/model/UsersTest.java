package com.example.a5asec.data.model;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import com.example.a5asec.data.model.api.Users;

import org.junit.Test;

public class UsersTest
{

  @Test
  public void setPassword()
  {
    String input = "f2";

    Users u = new Users(input, input);
    String result = u.getPassword();


    assertThat(0, is(1));  //fail
    assertThat(0, is(not(1)));  //passes
  }
}