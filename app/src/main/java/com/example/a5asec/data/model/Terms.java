package com.example.a5asec.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

 public class Terms
{

  @SerializedName("id")
  private int id;
  @SerializedName("nameEn")
  private String nameEn;
  @SerializedName("nameAr")
  private String nameAr;

  public ArrayList<Contents> contents;



  public int getId()
  {
    return id;
  }

  public String getNameEn()
  {
    return getId() + ". " + nameEn;
  }

  public String getNameAr()
  {
    return getId() + ". " + nameAr;
  }

  public int getContentsSize()
  {
    return contents.size();
  }

  public String getContentsEn()
  {
    StringBuilder contentArBuilder = new StringBuilder();
    for (int i = 0; i < getContentsSize(); i++)
    {
      int x = i + 1; // such 1 ,2 , 3, 4
      contentArBuilder.append("\n") //add  new line
          .append(getId() + "." + x + " ")//  such 1.1 , 1.2, 1.3
          .append(contents.get(i).contentValueEn) // get data with position
          .append("\n"); // add new line
    }

    return contentArBuilder.toString();
  }

  public String getContentsAr()
  {

    StringBuilder contentArBuilder = new StringBuilder();
    for (int i = 0; i < getContentsSize(); i++)
    {
      int x = i + 1; // such 1.1, 1.2
      // set data and modified such a: 1.1 this is change terms
      contentArBuilder.append("\n")//add new line
          .append(getId() + "." + x + " ") // add get id such as 1.1
          .append(contents.get(i).contentValueAr)// get data with position
          .append("\n");//add new line
    }

    return contentArBuilder.toString();
  }

  public static class Contents
  {
    @SerializedName("id")
    private int id;
    @SerializedName("valueEn")
    public String contentValueEn;
    @SerializedName("valueAr")
    private String contentValueAr;


    public String getContentValueEn()
    {
      return contentValueEn;
    }

    public String getContentValueAr()
    {
      return contentValueAr;
    }
  }

}
