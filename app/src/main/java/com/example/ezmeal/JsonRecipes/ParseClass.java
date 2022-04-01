package com.example.ezmeal.JsonRecipes;

import java.util.List;

public class ParseClass
{
    String name;
    int age;
    List<String> messages;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public List<String> getMessages()
    {
        return messages;
    }

    public void setMessages(List<String> messages)
    {
        this.messages = messages;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", messages=" + messages +
                '}';
    }
}