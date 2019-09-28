package com.example.cardviewgridviewsample.objects;

public class Person {
    String person_name, person_image;
    double person_budget;

    public Person() {
    }

    public Person(String person_name, double person_budget) {
        this.person_name = person_name;
        this.person_budget = person_budget;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_image() {
        return person_image;
    }

    public void setPerson_image(String person_image) {
        this.person_image = person_image;
    }

    public double getPerson_budget() {
        return person_budget;
    }

    public void setPerson_budget(double person_budget) {
        this.person_budget = person_budget;
    }
}
