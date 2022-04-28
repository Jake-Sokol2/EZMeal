package com.example.ezmeal.roomDatabase;

public class TextRatings
{
    private String textRating1;
    private String textRating2;
    private String textRating3;

    public TextRatings(String textRating1, String textRating2, String textRating3)
    {
        this.textRating1 = textRating1;
        this.textRating2 = textRating2;
        this.textRating3 = textRating3;
    }

    public String getTextRating1()
    {
        return textRating1;
    }

    public void setTextRating1(String textRating1)
    {
        this.textRating1 = textRating1;
    }

    public String getTextRating2()
    {
        return textRating2;
    }

    public void setTextRating2(String textRating2)
    {
        this.textRating2 = textRating2;
    }

    public String getTextRating3()
    {
        return textRating3;
    }

    public void setTextRating3(String textRating3)
    {
        this.textRating3 = textRating3;
    }
}
