package com.example.facebook;

/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa
 * CPEN 410 - Mobile, Web, and Internet Programming
 *
 * This class represents a user profile with personal, location,
 * academic, and profile picture information. It supports construction
 * from individual fields or a JSON string and is serializable.
 */

/****
 *  This class defines a person object with the following attributes:
 *      name:         full name of the user
 *      userName:     login username
 *      dateOfBirth:  birth date in yyyy-MM-dd format
 *      gender:       gender identity
 *      street:       street address
 *      town:         town or city
 *      state:        state or province
 *      country:      country of residence
 *      degree:       educational degree
 *      school:       school or institution
 *      profilePicture: URL or path to profile picture
 *
 *  This class implements the Serializable interface so it can be passed between activities.
 */

import org.json.JSONObject;
import java.io.Serializable;

public class jsonPerson implements Serializable {

    private String name;
    private String userName;
    private String dateOfBirth;
    private String gender;
    private String street;
    private String town;
    private String state;
    private String country;
    private String degree;
    private String school;
    private String profilePicture;

    /**
     * Special constructor using all user profile values.
     *
     * @param name           Full name of the user
     * @param userName       Login username
     * @param dateOfBirth    Date of birth (yyyy-MM-dd)
     * @param gender         Gender identity
     * @param street         Street address
     * @param town           Town or city
     * @param state          State or province
     * @param country        Country of residence
     * @param degree         Educational degree
     * @param school         School or institution name
     * @param profilePicture URL or path to the profile picture
     */
    public jsonPerson(String name, String userName, String dateOfBirth, String gender, String street, String town,
                      String state, String country, String degree, String school, String profilePicture) {
        this.name = name;
        this.userName = userName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.street = street;
        this.town = town;
        this.state = state;
        this.country = country;
        this.degree = degree;
        this.school = school;
        this.profilePicture = profilePicture;
    }

    /**
     * Special constructor using a JSON string with the following keys:
     * {
     *     "name":           "...",
     *     "userName":       "...",
     *     "dateOfBirth":    "...",
     *     "gender":         "...",
     *     "street":         "...",
     *     "town":           "...",
     *     "state":          "...",
     *     "country":        "...",
     *     "degree":         "...",
     *     "school":         "...",
     *     "profilePicture": "..."
     * }
     *
     * @param jsonFile JSON string representing a user profile
     */
    public jsonPerson(String jsonFile) {
        try {
            // Define a JSON object from the received data
            JSONObject jsonObj = new JSONObject(jsonFile);
            name = jsonObj.getString("name");
            userName = jsonObj.getString("userName");
            dateOfBirth = jsonObj.getString("dateOfBirth");
            gender = jsonObj.getString("gender");
            street = jsonObj.getString("street");
            town = jsonObj.getString("town");
            state = jsonObj.getString("state");
            country = jsonObj.getString("country");
            degree = jsonObj.getString("degree");
            school = jsonObj.getString("school");
            profilePicture = jsonObj.getString("profilePicture");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /******************************************************************
     *  Observer methods
     ******************************************************************/

    /**
     * @return the full name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * @return the username used to log in
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the user's date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @return the user's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return the user's street address
     */
    public String getStreet() {
        return street;
    }

    /**
     * @return the user's town
     */
    public String getTown() {
        return town;
    }

    /**
     * @return the user's state
     */
    public String getState() {
        return state;
    }

    /**
     * @return the user's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the user's degree
     */
    public String getDegree() {
        return degree;
    }

    /**
     * @return the user's school name
     */
    public String getSchool() {
        return school;
    }

    /**
     * @return the profile picture path or URL
     */
    public String getProfilePicture() {
        return profilePicture;
    }
}
